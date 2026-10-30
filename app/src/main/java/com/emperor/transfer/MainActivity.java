package com.emperor.transfer;

import android.app.Activity;
import android.os.Bundle;
import android.os.Build;
import android.os.BatteryManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.view.Gravity;
import android.view.View;
import android.widget.*;

import java.net.NetworkInterface;
import java.net.InetAddress;
import java.util.Collections;

public class MainActivity extends Activity {

    private LinearLayout mainLayout;
    private LinearLayout content;
    private int cyan = Color.rgb(0, 230, 255);
    private int pink = Color.rgb(255, 40, 170);
    private int purple = Color.rgb(160, 70, 255);
    private int white = Color.WHITE;
    private int dark = Color.rgb(12, 12, 22);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showDashboard();
    }

    private int dp(int value) {
        return (int) (value * getResources().getDisplayMetrics().density);
    }

    private GradientDrawable shape(int color, int stroke) {
        GradientDrawable bg = new GradientDrawable();
        bg.setColor(color);
        bg.setCornerRadius(dp(16));
        bg.setStroke(dp(1), stroke);
        return bg;
    }

    private TextView text(String value, int size, int color) {
        TextView t = new TextView(this);
        t.setText(value);
        t.setTextSize(size);
        t.setTextColor(color);
        t.setGravity(Gravity.CENTER);
        t.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        t.setPadding(dp(8), dp(8), dp(8), dp(8));
        return t;
    }

    private void setup(String title) {
        mainLayout = new LinearLayout(this);
        mainLayout.setOrientation(LinearLayout.VERTICAL);
        mainLayout.setBackgroundColor(Color.rgb(5, 5, 12));

        TextView header = text("👑 EMPEROR", 25, cyan);
        header.setPadding(10, dp(18), 10, dp(5));
        mainLayout.addView(header);

        TextView subtitle = text(title, 14, pink);
        mainLayout.addView(subtitle);

        ScrollView scroll = new ScrollView(this);
        content = new LinearLayout(this);
        content.setOrientation(LinearLayout.VERTICAL);
        content.setPadding(dp(14), dp(10), dp(14), dp(20));
        scroll.addView(content);

        mainLayout.addView(scroll,
                new LinearLayout.LayoutParams(-1, 0, 1));

        LinearLayout nav = new LinearLayout(this);
        nav.setOrientation(LinearLayout.HORIZONTAL);
        nav.setGravity(Gravity.CENTER);
        nav.setPadding(dp(4), dp(8), dp(4), dp(8));
        nav.setBackgroundColor(Color.rgb(15, 15, 28));

        addNav(nav, "الرئيسية", this::showDashboard);
        addNav(nav, "الجهاز", this::showDevice);
        addNav(nav, "الشبكة", this::showNetwork);
        addNav(nav, "الملفات", this::showFiles);
        addNav(nav, "الإعدادات", this::showSettings);

        mainLayout.addView(nav);
        setContentView(mainLayout);
    }

    private void addNav(LinearLayout nav, String name, Runnable action) {
        Button b = new Button(this);
        b.setText(name);
        b.setTextSize(10);
        b.setTextColor(cyan);
        b.setAllCaps(false);
        b.setBackgroundColor(Color.TRANSPARENT);
        b.setOnClickListener(v -> action.run());
        nav.addView(b, new LinearLayout.LayoutParams(0, dp(52), 1));
    }

    private void card(String title, String value, int color) {
        LinearLayout box = new LinearLayout(this);
        box.setOrientation(LinearLayout.VERTICAL);
        box.setPadding(dp(14), dp(12), dp(14), dp(12));
        box.setBackground(shape(Color.rgb(17, 17, 32), color));

        TextView heading = text(title, 16, color);
        TextView detail = text(value, 14, white);

        box.addView(heading);
        box.addView(detail);

        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(-1, -2);
        params.setMargins(0, dp(7), 0, dp(7));
        content.addView(box, params);
    }

    private void button(String title, Runnable action) {
        Button b = new Button(this);
        b.setText(title);
        b.setTextColor(white);
        b.setTextSize(14);
        b.setAllCaps(false);
        b.setBackground(shape(Color.rgb(25, 18, 45), purple));
        b.setOnClickListener(v -> action.run());

        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(-1, dp(55));
        params.setMargins(0, dp(6), 0, dp(6));
        content.addView(b, params);
    }

    // =========================
    // الصفحة الرئيسية
    // =========================

    private void showDashboard() {
        setup("نظام إدارة وأمان الجهاز");

        card("🛡️ حالة النظام",
                "EMPEROR يعمل بنجاح", cyan);

        card("📱 الجهاز",
                Build.MANUFACTURER + " " + Build.MODEL, pink);

        card("🔋 البطارية",
                getBattery() + "%", purple);

        card("🌐 عنوان IP المحلي",
                getLocalIP(), cyan);

        card("💾 مساحة التخزين المتاحة",
                getStorage(), pink);

        button("🛡️ فحص معلومات الجهاز", this::showScan);
        button("📱 معلومات الجهاز", this::showDevice);
        button("🌐 الشبكة والاتصال", this::showNetwork);
        button("📂 مستكشف الملفات", this::showFiles);
        button("📋 سجل العمليات", this::showLogs);
        button("⚙️ الإعدادات", this::showSettings);
    }

    // =========================
    // معلومات الجهاز
    // =========================

    private void showDevice() {
        setup("معلومات الجهاز");

        card("الشركة المصنعة", Build.MANUFACTURER, cyan);
        card("اسم الجهاز", Build.MODEL, pink);
        card("إصدار أندرويد", Build.VERSION.RELEASE, purple);
        card("مستوى API", String.valueOf(Build.VERSION.SDK_INT), cyan);
        card("البطارية", getBattery() + "%", pink);
        card("التخزين المتاح", getStorage(), purple);

        button("العودة للرئيسية", this::showDashboard);
    }

    // =========================
    // معلومات الشبكة
    // =========================

    private void showNetwork() {
        setup("الشبكة والاتصال");

        card("عنوان IP المحلي", getLocalIP(), cyan);
        card("حالة الاتصال", getConnection(), pink);

        button("🔄 تحديث المعلومات", this::showNetwork);

        button("📋 نسخ عنوان IP", () -> {
            android.content.ClipboardManager clipboard =
                    (android.content.ClipboardManager)
                    getSystemService(CLIPBOARD_SERVICE);

            clipboard.setPrimaryClip(
                    android.content.ClipData.newPlainText(
                            "IP", getLocalIP()));

            Toast.makeText(this,
                    "تم نسخ عنوان IP",
                    Toast.LENGTH_SHORT).show();
        });
    }

    // =========================
    // فحص معلومات الجهاز
    // =========================

    private void showScan() {
        setup("فحص معلومات الجهاز");

        card("حالة الفحص",
                "الفحص الأساسي جاهز", cyan);

        card("معلومات النظام",
                "يمكن قراءة إصدار أندرويد وطراز الجهاز", pink);

        card("الشبكة",
                getConnection(), purple);

        card("مساحة التخزين",
                getStorage(), cyan);

        card("البطارية",
                getBattery() + "%", pink);

        button("🔄 إعادة الفحص", this::showScan);

        TextView note = text(
                "تنبيه: هذا فحص للمعلومات الأساسية فقط، وليس مضاد فيروسات.",
                13, white);

        content.addView(note);
    }

    // =========================
    // مستكشف الملفات
    // =========================

    private void showFiles() {
        setup("مستكشف الملفات");

        card("إدارة الملفات",
                "اختر ملفًا أو مجلدًا من جهازك", cyan);

        button("📄 اختيار ملف", () -> {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.setType("*/*");
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            startActivityForResult(intent, 100);
        });

        button("📁 اختيار مجلد", () -> {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
            startActivityForResult(intent, 101);
        });

        card("الخصوصية",
                "لن يتم إرسال ملفاتك لأي مكان دون موافقتك.", pink);
    }

    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode,
                                    Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {
            String message = requestCode == 101
                    ? "تم اختيار المجلد"
                    : "تم اختيار الملف";

            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        }
    }

    // =========================
    // سجل العمليات
    // =========================

    private void showLogs() {
        setup("سجل العمليات");

        card("النظام",
                "تم فتح تطبيق EMPEROR", cyan);

        card("الواجهة",
                "تم تحميل لوحة التحكم", pink);

        card("الخصوصية",
                "لا توجد عمليات إرسال ملفات في هذه النسخة", purple);
    }

    // =========================
    // الإعدادات
    // =========================

    private void showSettings() {
        setup("إعدادات EMPEROR");

        card("اسم التطبيق", "EMPEROR", cyan);
        card("الإصدار", "1.0", pink);
        card("المظهر", "Dark Neon", purple);

        button("معلومات التطبيق", () -> {
            new android.app.AlertDialog.Builder(this)
                    .setTitle("EMPEROR")
                    .setMessage("تطبيق أدوات ومعلومات الجهاز.")
                    .setPositiveButton("حسنًا", null)
                    .show();
        });

        button("العودة للرئيسية", this::showDashboard);
    }

    // =========================
    // وظائف مساعدة
    // =========================

    private int getBattery() {
        IntentFilter filter =
                new IntentFilter(Intent.ACTION_BATTERY_CHANGED);

        Intent battery = registerReceiver(null, filter);

        if (battery == null) return 0;

        int level = battery.getIntExtra(
                BatteryManager.EXTRA_LEVEL, -1);

        int scale = battery.getIntExtra(
                BatteryManager.EXTRA_SCALE, -1);

        if (level < 0 || scale <= 0) return 0;

        return (level * 100) / scale;
    }

    private String getStorage() {
        try {
            long bytes = Environment.getDataDirectory().getFreeSpace();
            long gb = bytes / (1024 * 1024 * 1024);
            return gb + " GB متاحة تقريبًا";
        } catch (Exception e) {
            return "غير متاح";
        }
    }

    private String getConnection() {
        try {
            ConnectivityManager cm =
                    (ConnectivityManager)
                    getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo info = cm.getActiveNetworkInfo();

            if (info != null && info.isConnected()) {
                return "متصل بالإنترنت";
            }

            return "غير متصل";
        } catch (Exception e) {
            return "غير معروف";
        }
    }

    private String getLocalIP() {
        try {
            for (NetworkInterface network :
                    Collections.list(NetworkInterface.getNetworkInterfaces())) {

                for (InetAddress address :
                        Collections.list(network.getInetAddresses())) {

                    if (!address.isLoopbackAddress()
                            && address instanceof java.net.Inet4Address) {
                        return address.getHostAddress();
                    }
                }
            }
        } catch (Exception ignored) {}

        return "غير متاح";
    }
                }
