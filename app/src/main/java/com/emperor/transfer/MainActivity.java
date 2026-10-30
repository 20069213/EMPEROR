package com.emperor.transfer;

import android.app.*;
import android.os.*;
import android.content.*;
import android.graphics.*;
import android.graphics.drawable.GradientDrawable;
import android.net.*;
import android.view.*;
import android.widget.*;
import android.text.InputType;

import java.io.*;
import java.util.*;

public class MainActivity extends Activity {

    private LinearLayout root, body;
    private SharedPreferences prefs;
    private boolean english = false;

    private final int BG = Color.rgb(5, 7, 16);
    private final int PANEL = Color.rgb(18, 22, 39);
    private final int CYAN = Color.rgb(0, 225, 255);
    private final int PINK = Color.rgb(255, 65, 180);
    private final int PURPLE = Color.rgb(165, 100, 255);
    private final int WHITE = Color.WHITE;
    private final int MUTED = Color.rgb(160, 175, 195);

    private String currentReport = "";

    private final String[] courses = {
        "Cybersecurity Fundamentals",
        "Computer Fundamentals",
        "Networking & TCP/IP",
        "Linux Fundamentals",
        "Python Security",
        "Web Security",
        "Ethical Hacking",
        "Cryptography",
        "Digital Forensics",
        "Incident Response",
        "Android Security",
        "Cloud Security",
        "Risk Management",
        "Threat Modeling",
        "Secure Programming",
        "SOC & Log Analysis",
        "CTF Training"
    };

    private final String[][] lessons = {
        {
            "CIA Triad: Confidentiality, Integrity and Availability.",
            "Learn least privilege, defense in depth and risk management."
        },
        {
            "Learn operating systems, files, processes and memory.",
            "Understand users, permissions and system updates."
        },
        {
            "Learn IP, DNS, TCP, UDP, HTTP and HTTPS.",
            "Common ports: 22 SSH, 53 DNS, 80 HTTP, 443 HTTPS."
        },
        {
            "Practice pwd, ls, cd, mkdir, cp and mv.",
            "Understand Linux users, groups and permissions."
        },
        {
            "Learn Python variables, loops, functions and files.",
            "Build defensive tools to analyze your own files and logs."
        },
        {
            "Learn authentication, sessions and access control.",
            "Study OWASP concepts in authorized training labs."
        },
        {
            "Get written permission before any security assessment.",
            "Define scope, targets, methods and reporting requirements."
        },
        {
            "Understand hashing, encryption and digital signatures.",
            "Learn SHA-256, AES, RSA and HMAC concepts."
        },
        {
            "Preserve evidence and calculate file hashes.",
            "Understand timestamps and chain of custody."
        },
        {
            "Prepare, identify, contain, eradicate and recover.",
            "Preserve relevant evidence during incident response."
        },
        {
            "Review Android permissions and install updates.",
            "Use secure screen locks and trusted app sources."
        },
        {
            "Learn cloud identity, access and shared responsibility.",
            "Use MFA, backups and least privilege."
        },
        {
            "Understand security policies and standards.",
            "Track assets, risks and remediation."
        },
        {
            "Identify assets, trust boundaries and threats.",
            "Prioritize mitigations based on risk."
        },
        {
            "Use secure defaults and validate input.",
            "Protect secrets and review dependencies."
        },
        {
            "Understand security logs and event monitoring.",
            "Investigate alerts using context and evidence."
        },
        {
            "Practice challenges in legal CTF environments.",
            "Document your reasoning and learning."
        }
    };

    private final String[] glossary = {
        "Authentication: إثبات هوية المستخدم.",
        "Authorization: تحديد الصلاحيات.",
        "Firewall: جدار حماية الشبكة.",
        "Phishing: التصيد الإلكتروني.",
        "Vulnerability: ثغرة أمنية.",
        "Patch: تحديث لإصلاح مشكلة.",
        "Hash: بصمة رقمية للبيانات.",
        "MFA: المصادقة متعددة العوامل.",
        "SIEM: تحليل وتجميع سجلات الأمن.",
        "Least Privilege: أقل صلاحيات لازمة.",
        "IOC: مؤشر اختراق محتمل.",
        "EDR: اكتشاف والاستجابة لتهديدات الأجهزة.",
        "PKI: البنية التحتية للمفاتيح العامة.",
        "Threat Model: نموذج تحليل التهديدات."
    };

    @Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    prefs = getSharedPreferences("emperor", MODE_PRIVATE);
    english = prefs.getBoolean("english", false);

    Intent musicIntent = new Intent(this, EmperorMusicService.class);

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        startForegroundService(musicIntent);
    } else {
        startService(musicIntent);
    }

    home();
}

@Override
protected void onStop() {
    super.onStop();

    Intent musicIntent = new Intent(this, EmperorMusicService.class);
    stopService(musicIntent);


}
    

    private String tr(String ar, String en) {
        return english ? en : ar;
    }

    private int dp(int n) {
        return (int) (n * getResources()
                .getDisplayMetrics().density);
    }

    private GradientDrawable shape(int color, int stroke) {
        GradientDrawable d = new GradientDrawable();
        d.setColor(color);
        d.setCornerRadius(dp(20));
        d.setStroke(dp(1), stroke);
        return d;
    }

    private GradientDrawable gradient(int first, int second) {
        GradientDrawable d = new GradientDrawable(
                GradientDrawable.Orientation.LEFT_RIGHT,
                new int[]{first, second}
        );
        d.setCornerRadius(dp(20));
        return d;
    }

    private TextView label(String value, int size, int color) {
        TextView t = new TextView(this);
        t.setText(value);
        t.setTextSize(size);
        t.setTextColor(color);
        t.setPadding(dp(8), dp(7), dp(8), dp(7));
        t.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        return t;
    }

    // MAIN PAGE DESIGN

    private void page(String title) {

        root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setBackgroundColor(BG);

        LinearLayout header = new LinearLayout(this);
        header.setOrientation(LinearLayout.VERTICAL);
        header.setGravity(Gravity.CENTER);
        header.setPadding(dp(10), dp(12), dp(10), dp(12));
        header.setBackground(gradient(
                Color.rgb(10, 28, 48),
                Color.rgb(32, 15, 55)
        ));

        TextView brand = label("👑 EMPEROR", 27, CYAN);
        brand.setGravity(Gravity.CENTER);

        TextView subtitle = label(
                "CYBER SECURITY SYSTEM",
                11,
                PINK
        );
        subtitle.setGravity(Gravity.CENTER);

        header.addView(brand);
header.addView(subtitle);

// ABDO SAAD ANIMATION
TextView owner = label("💀 ABDO SAAD 💀", 20, PINK);
owner.setGravity(Gravity.CENTER);

header.addView(owner);

android.view.animation.Animation shake =
        new android.view.animation.TranslateAnimation(
                -8, 8, 0, 0
        );

shake.setDuration(90);
shake.setRepeatMode(
        android.view.animation.Animation.REVERSE
);
shake.setRepeatCount(
        android.view.animation.Animation.INFINITE
);

owner.startAnimation(shake);

        LinearLayout.LayoutParams headerParams =
                new LinearLayout.LayoutParams(-1, -2);

        headerParams.setMargins(
                dp(12), dp(8), dp(12), dp(5)
        );

        root.addView(header, headerParams);

        TextView heading = label(title, 19, PINK);
        heading.setGravity(Gravity.CENTER);
        heading.setPadding(dp(8), dp(8), dp(8), dp(10));

        root.addView(heading);

        ScrollView scroll = new ScrollView(this);
        scroll.setFillViewport(true);

        body = new LinearLayout(this);
        body.setOrientation(LinearLayout.VERTICAL);
        body.setPadding(dp(14), dp(8), dp(14), dp(20));

        scroll.addView(body);

        root.addView(
                scroll,
                new LinearLayout.LayoutParams(-1, 0, 1)
        );

        LinearLayout nav = new LinearLayout(this);
        nav.setOrientation(LinearLayout.HORIZONTAL);
        nav.setGravity(Gravity.CENTER);
        nav.setBackgroundColor(Color.rgb(10, 13, 27));
        nav.setPadding(dp(3), dp(4), dp(3), dp(4));

        navButton(nav, "⌂\n" + tr("الرئيسية", "Home"), this::home);
        navButton(nav, "▤\n" + tr("التعلم", "Learn"), this::academy);
        navButton(nav, "⚙\n" + tr("الأدوات", "Tools"), this::tools);
        navButton(nav, "⌘\n" + tr("المختبر", "Labs"), this::labs);
        navButton(nav, "⋮\n" + tr("المزيد", "More"), this::more);

        root.addView(
                nav,
                new LinearLayout.LayoutParams(-1, dp(62))
        );

        setContentView(root);
    }

    // NAV BUTTON

    private void navButton(
            LinearLayout nav,
            String title,
            Runnable action) {

        TextView b = new TextView(this);
        b.setText(title);
        b.setTextSize(11);
        b.setGravity(Gravity.CENTER);
        b.setTextColor(CYAN);
        b.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        b.setBackgroundColor(Color.TRANSPARENT);
        b.setOnClickListener(v -> action.run());

        nav.addView(
                b,
                new LinearLayout.LayoutParams(0, -1, 1)
        );
    }

    // CARDS

    private void card(String title, String value, int accent) {

        LinearLayout box = new LinearLayout(this);
        box.setOrientation(LinearLayout.VERTICAL);
        box.setPadding(dp(15), dp(13), dp(15), dp(13));
        box.setBackground(shape(PANEL, accent));

        TextView heading = label(title, 15, accent);
        heading.setGravity(Gravity.RIGHT);

        box.addView(heading);

        TextView detail = label(value, 14, WHITE);
        detail.setTypeface(Typeface.DEFAULT);
        detail.setGravity(Gravity.LEFT);

        box.addView(detail);

        LinearLayout.LayoutParams p =
                new LinearLayout.LayoutParams(-1, -2);

        p.setMargins(0, dp(7), 0, dp(7));

        body.addView(box, p);
    }

    // BUTTONS

    private void button(String title, Runnable action) {

        Button b = new Button(this);
        b.setText(title);
        b.setTextColor(WHITE);
        b.setTextSize(14);
        b.setAllCaps(false);
        b.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        b.setBackground(gradient(
                Color.rgb(29, 27, 52),
                Color.rgb(20, 25, 47)
        ));

        b.setOnClickListener(v -> {
            v.animate()
                    .scaleX(0.97f)
                    .scaleY(0.97f)
                    .setDuration(80)
                    .withEndAction(() -> {
                        v.setScaleX(1f);
                        v.setScaleY(1f);
                        action.run();
                    })
                    .start();
        });

        LinearLayout.LayoutParams p =
                new LinearLayout.LayoutParams(-1, dp(58));

        p.setMargins(0, dp(6), 0, dp(6));

        body.addView(b, p);
    }

    private void toast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    private void showResult(String title, String message) {

        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", null)
                .show();
    }

    // HOME

    private void home() {

        page(tr("لوحة التحكم الرئيسية", "Main Dashboard"));

        card(
                "👑 EMPEROR CYBER SECURITY",
                tr(
                        "منصة تعليمية وأدوات دفاعية للأمن السيبراني",
                        "Cybersecurity Learning & Defensive Platform"
                ),
                CYAN
        );

        card(
                "📚 " + tr("المجالات التعليمية", "Learning Domains"),
                "17 Cybersecurity Domains",
                PINK
        );

        card(
                "🏆 " + tr("النقاط", "Learning Points"),
                String.valueOf(prefs.getInt("points", 0)),
                PURPLE
        );

        card(
                "📁 " + tr("التقارير المحفوظة", "Saved Reports"),
                String.valueOf(ReportManager.getReportCount(this)),
                CYAN
        );

        card(
                "🛡️ " + tr("مركز الحماية", "Security Center"),
                tr(
                        "افحص إعدادات أمان جهازك واعرض النتائج.",
                        "Check your device security settings."
                ),
                CYAN
        );

        button(
                "🔍 " + tr("بدء فحص أمان الجهاز", "Start Device Security Scan"),
                this::scanDevice
        );

        button(
                "🎓 " + tr("أكاديمية الأمن السيبراني", "Cybersecurity Academy"),
                this::academy
        );

        button(
                "🛡️ " + tr("أدوات الحماية", "Security Tools"),
                this::tools
        );

        button(
                "🧪 " + tr("مختبر التدريب", "Training Labs"),
                this::labs
        );

        button(
                "📖 " + tr("قاموس المصطلحات", "Security Glossary"),
                this::glossary
        );

        button(
                "🗺️ " + tr("خطة الدراسة", "Study Roadmap"),
                this::roadmap
        );

        button(
                "📱 " + tr("معلومات الجهاز", "Device Information"),
                this::scanDevice
        );
    }

    // ACADEMY

    private void academy() {

        page(tr("أكاديمية الأمن السيبراني",
                "Cybersecurity Academy"));

        card(
                "🎓 " + tr("مسار التعلم", "Learning Path"),
                tr(
                        "ابدأ من الأساسيات ثم انتقل للمستوى المتقدم.",
                        "Start with fundamentals and progress to advanced topics."
                ),
                CYAN
        );

        for (int i = 0; i < courses.length; i++) {

            final int index = i;

            button(
                    "📘 " + (i + 1) + " - " + courses[i],
                    () -> lesson(index)
            );
        }
    }

    private void lesson(int index) {

        page(courses[index]);

        card(
                tr("الدرس الأول", "Lesson One"),
                lessons[index][0],
                CYAN
        );

        card(
                tr("الدرس الثاني", "Lesson Two"),
                lessons[index][1],
                PINK
        );

        card(
                tr("تطبيق عملي", "Practical Exercise"),
                practical(index),
                PURPLE
        );

        button(
                "🏆 " + tr("تسجيل إكمال الدرس +10 نقاط",
                           "Complete Lesson +10 Points"),
                () -> {

                    String key = "completed_" + index;

                    if (!prefs.getBoolean(key, false)) {

                        prefs.edit()
                                .putBoolean(key, true)
                                .putInt(
                                        "points",
                                        prefs.getInt("points", 0) + 10
                                )
                                .apply();
                    }

                    toast(tr("تم حفظ تقدمك", "Progress saved"));
                    academy();
                }
        );

        button(
                "📝 " + tr("اختبار قصير", "Quick Quiz"),
                this::quiz
        );
    }

    private String practical(int i) {

        String[] exercises = {
            "اكتب أمثلة على السرية والسلامة والتوافر.",
            "اشرح الفرق بين نظام التشغيل والبرنامج.",
            "ارسم شبكة منزلية وحدد IP وDNS.",
            "جرّب أوامر Linux في بيئة تدريبية.",
            "اكتب برنامج Python يحسب عدد أسطر ملف نصي.",
            "حلل طلب HTTP تجريبيًا.",
            "اكتب نطاق اختبار أمني افتراضي.",
            "اشرح الفرق بين Hash وEncryption.",
            "احسب بصمة ملف تجريبي.",
            "اكتب خطة استجابة لحادث تصيد.",
            "راجع صلاحيات التطبيقات على Android.",
            "جهز قائمة حماية لحساب سحابي تجريبي.",
            "حدد ثلاثة مخاطر لنظام افتراضي.",
            "ارسم حدود الثقة في تطبيق بسيط.",
            "اكتب قائمة مراجعة للكود.",
            "حلل سجلات تدريبية.",
            "حل تحدي CTF قانوني."
        };

        return exercises[i];
    }

    // QUIZ

    private void quiz() {

        page(tr("اختبار المعرفة", "Knowledge Quiz"));

        card(
                tr("السؤال", "Question"),
                tr(
                        "ما وظيفة SHA-256؟",
                        "What does SHA-256 do?"
                ),
                CYAN
        );

        button("A - Hashing", () -> quizAnswer(true));
        button("B - Network Routing", () -> quizAnswer(false));
        button("C - Operating System", () -> quizAnswer(false));
    }

    private void quizAnswer(boolean correct) {

        if (correct) {

            prefs.edit()
                    .putInt(
                            "points",
                            prefs.getInt("points", 0) + 5
                    )
                    .apply();
        }

        new AlertDialog.Builder(this)
                .setTitle(
                        correct
                                ? tr("إجابة صحيحة", "Correct")
                                : tr("إجابة خاطئة", "Incorrect")
                )
                .setMessage(
                        correct
                                ? "SHA-256 is a cryptographic hash function."
                                : tr("راجع درس التشفير.", "Review Cryptography.")
                )
                .setPositiveButton("OK", (d, w) -> quiz())
                .show();
    }

    // TOOLS

    private void tools() {

        page(tr("الأدوات الدفاعية", "Defensive Tools"));

        card(
                "⚠️ " + tr("تنبيه أمني", "Security Notice"),
                tr(
                        "الأدوات محلية وتعليمية وليست مضاد فيروسات شاملًا.",
                        "Local educational tools, not a full antivirus."
                ),
                PINK
        );

        button(
                "🛡️ " + tr("فحص أمان الجهاز", "Device Security Scan"),
                this::scanDevice
        );

        // NEW: OFFLINE VIRUS SCANNER

        button(
                "🦠 " + tr("فحص الفيروسات Offline", "Offline Virus Scanner"),
                this::pickVirusFile
        );

        button(
                "🌐 " + tr("فحص معلومات الشبكة", "Network Information"),
                this::scanNetwork
        );

        button(
                "🔐 " + tr("تشفير وفك تشفير النصوص", "AES Encryption"),
                this::encryptionMenu
        );

        button(
                "🔑 " + tr("حساب SHA-256 للنص", "Text SHA-256"),
                this::hashText
        );

        button(
                "📄 " + tr("حساب SHA-256 للملف", "File SHA-256"),
                this::pickFile
        );

        button(
                "🔒 " + tr("تقييم قوة كلمة المرور", "Password Analyzer"),
                this::password
        );

        button(
                "💾 " + tr("حفظ تقرير الفحص", "Save Scan Report"),
                this::saveScanReport
        );

        button(
                "📁 " + tr("عرض التقارير المحفوظة", "Saved Reports"),
                this::showReports
        );
    }

    // DEVICE SCAN

    private void scanDevice() {

        page(tr("فحص أمان الجهاز", "Device Security Scan"));

        try {

            java.util.List<SecurityScanner.ScanResult> results =
                    SecurityScanner.scanDevice(this);

            StringBuilder report = new StringBuilder();

            int warnings = 0;

            for (SecurityScanner.ScanResult result : results) {

                card(
                        result.title,
                        result.status + "\n" + result.details,
                        result.warning ? PINK : CYAN
                );

                report.append(result.title)
                        .append(": ")
                        .append(result.status)
                        .append("\n")
                        .append(result.details)
                        .append("\n\n");

                if (result.warning) {
                    warnings++;
                }
            }

            currentReport = report.toString();

            card(
                    tr("النتيجة", "Result"),
                    tr("عدد التنبيهات: ", "Warnings: ") + warnings,
                    warnings > 0 ? PINK : CYAN
            );

            button(
                    "💾 " + tr("حفظ تقرير الفحص", "Save Scan Report"),
                    this::saveScanReport
            );

        } catch (Exception e) {

            toast("Scan Error: " + e.getMessage());
        }
    }

    // NETWORK SCAN

    private void scanNetwork() {

        page(tr("معلومات الشبكة", "Network Information"));

        try {

            java.util.List<NetworkScanner.NetworkResult> results =
                    NetworkScanner.scan(this);

            StringBuilder report = new StringBuilder();

            for (NetworkScanner.NetworkResult result : results) {

                card(result.title, result.value, CYAN);

                report.append(result.title)
                        .append(": ")
                        .append(result.value)
                        .append("\n");
            }

            currentReport = report.toString();

            button(
                    "💾 " + tr("حفظ تقرير الشبكة", "Save Network Report"),
                    this::saveScanReport
            );

        } catch (Exception e) {

            toast("Network Error: " + e.getMessage());
        }
    }

    // ENCRYPTION MENU

    private void encryptionMenu() {

        page(tr("التشفير AES-256", "AES-256 Encryption"));

        card(
                "🔐 " + tr("التشفير المحلي", "Local Encryption"),
                tr(
                        "التشفير يتم على جهازك باستخدام AES-GCM.",
                        "Encryption runs locally using AES-GCM."
                ),
                CYAN
        );

        button(
                tr("إنشاء مفتاح جديد", "Generate New Key"),
                this::generateEncryptionKey
        );

        button(
                tr("تشفير نص", "Encrypt Text"),
                this::encryptText
        );

        button(
                tr("فك تشفير نص", "Decrypt Text"),
                this::decryptText
        );
    }

    private void generateEncryptionKey() {

        try {

            String key = EncryptionManager.generateKey();

            prefs.edit()
                    .putString("aes_key", key)
                    .apply();

            EditText output = new EditText(this);
            output.setText(key);
            output.setTextIsSelectable(true);

            new AlertDialog.Builder(this)
                    .setTitle(tr("مفتاح AES الجديد", "New AES Key"))
                    .setMessage(tr(
                            "انسخ المفتاح واحفظه في مكان آمن. لا تشاركه.",
                            "Copy and store this key safely. Do not share it."
                    ))
                    .setView(output)
                    .setPositiveButton("OK", null)
                    .show();

        } catch (Exception e) {

            toast("Key Error: " + e.getMessage());
        }
    }

    private void encryptText() {

        String key = prefs.getString("aes_key", null);

        if (key == null) {

            toast(tr("أنشئ مفتاحًا أولًا", "Generate a key first"));
            return;
        }

        EditText input = new EditText(this);
        input.setHint(tr("اكتب النص", "Enter text"));

        new AlertDialog.Builder(this)
                .setTitle(tr("تشفير نص", "Encrypt Text"))
                .setView(input)
                .setPositiveButton(
                        tr("تشفير", "Encrypt"),
                        (d, w) -> {

                    try {

                        String encrypted = EncryptionManager.encrypt(
                                input.getText().toString(),
                                key
                        );

                        showResult("Encrypted Text", encrypted);

                    } catch (Exception e) {

                        toast("Encryption Error");
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void decryptText() {

        String key = prefs.getString("aes_key", null);

        if (key == null) {

            toast(tr("أنشئ مفتاحًا أولًا", "Generate a key first"));
            return;
        }

        EditText input = new EditText(this);
        input.setHint(tr("الصق النص المشفر", "Paste encrypted text"));

        new AlertDialog.Builder(this)
                .setTitle(tr("فك التشفير", "Decrypt Text"))
                .setView(input)
                .setPositiveButton(
                        tr("فك التشفير", "Decrypt"),
                        (d, w) -> {

                    try {

                        String decrypted = EncryptionManager.decrypt(
                                input.getText().toString(),
                                key
                        );

                        showResult("Decrypted Text", decrypted);

                    } catch (Exception e) {

                        toast(tr(
                                "فشل فك التشفير أو المفتاح غير صحيح",
                                "Decryption failed or incorrect key"
                        ));
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    // TEXT HASH

    private void hashText() {

        EditText input = new EditText(this);

        input.setHint(tr("اكتب نصًا تجريبيًا", "Enter sample text"));

        new AlertDialog.Builder(this)
                .setTitle("SHA-256")
                .setView(input)
                .setPositiveButton(
                        tr("احسب", "Calculate"),
                        (d, w) -> {

                    try {

                        String result = HashManager.hashText(
                                input.getText().toString()
                        );

                        showResult("SHA-256", result);

                    } catch (Exception e) {

                        toast("Hash Error");
                    }
                })
                .setNegativeButton(
                        tr("إلغاء", "Cancel"),
                        null
                )
                .show();
    }

    // FILE HASH

    private void pickFile() {

        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        startActivityForResult(intent, 700);
    }

    // NEW: PICK FILE FOR OFFLINE VIRUS SCAN

    private void pickVirusFile() {

        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        startActivityForResult(intent, 701);
    }

    // HANDLE SELECTED FILE

    @Override
    protected void onActivityResult(
            int requestCode,
            int resultCode,
            Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK ||
                data == null ||
                data.getData() == null) {
            return;
        }

        Uri fileUri = data.getData();

        // FILE HASH

        if (requestCode == 700) {

            try (InputStream in =
                    getContentResolver().openInputStream(fileUri)) {

                if (in == null) {
                    toast("Could not read file");
                    return;
                }

                java.security.MessageDigest md =
                        java.security.MessageDigest.getInstance("SHA-256");

                byte[] buffer = new byte[8192];

                int n;

                while ((n = in.read(buffer)) > 0) {
                    md.update(buffer, 0, n);
                }

                StringBuilder result = new StringBuilder();

                for (byte b : md.digest()) {

                    result.append(String.format(
                            Locale.US,
                            "%02x",
                            b & 255
                    ));
                }

                currentReport = "File SHA-256:\n" + result;

                showResult("File SHA-256", result.toString());

            } catch (Exception e) {

                toast(tr("تعذر قراءة الملف", "Could not read file"));
            }

            return;
        }

        // OFFLINE VIRUS SCAN

        if (requestCode == 701) {

            toast(tr(
                    "جاري فحص الملف...",
                    "Scanning file..."
            ));

            new Thread(() -> {

                OfflineVirusScanner.ScanResult result =
                        OfflineVirusScanner.scanFile(this, fileUri);

                runOnUiThread(() -> {

                    String status;

                    switch (result.status) {

                        case KNOWN_THREAT:
                            status = tr(
                                    "🚨 تهديد معروف",
                                    "🚨 Known Threat"
                            );
                            break;

                        case SUSPICIOUS:
                            status = tr(
                                    "⚠️ ملف يحتاج مراجعة",
                                    "⚠️ Suspicious File"
                            );
                            break;

                        case NO_INDICATORS:
                            status = tr(
                                    "لم تظهر مؤشرات في الفحص المحدود",
                                    "No Indicators Found"
                            );
                            break;

                        default:
                            status = tr(
                                    "❌ خطأ أثناء الفحص",
                                    "❌ Scan Error"
                            );
                            break;
                    }

                    String report =
                            "EMPEROR OFFLINE VIRUS SCAN\n\n" +
                            "File: " + result.fileName + "\n" +
                            "Status: " + status + "\n" +
                            "SHA-256: " + result.sha256 + "\n\n" +
                            "Details: " + result.details + "\n\n" +
                            "Note: This scan cannot guarantee that a file is safe.";

                    currentReport = report;

                    showResult(
                            "🛡️ EMPEROR SCANNER",
                            "File: " + result.fileName +
                            "\n\nResult: " + status +
                            "\n\nDetails: " + result.details +
                            "\n\nSHA-256:\n" + result.sha256
                    );

                    button(
                            "💾 " + tr(
                                    "حفظ تقرير الفحص",
                                    "Save Scan Report"
                            ),
                            this::saveScanReport
                    );
                });

            }).start();
        }
    }

    // PASSWORD ANALYZER

    private void password() {

        page(tr("تحليل قوة كلمة المرور", "Password Analyzer"));

        card(
                tr("تنبيه الخصوصية", "Privacy Notice"),
                tr(
                        "استخدم كلمة تجريبية فقط، ولا تدخل كلمة مرورك الحقيقية.",
                        "Use a sample password only. Never enter your real password."
                ),
                PINK
        );

        EditText input = new EditText(this);
        input.setSingleLine(true);

        input.setInputType(
                InputType.TYPE_CLASS_TEXT |
                InputType.TYPE_TEXT_VARIATION_PASSWORD
        );

        input.setHint(tr("كلمة مرور تجريبية", "Sample password"));

        body.addView(input);

        button(
                tr("تحليل القوة", "Analyze Strength"),
                () -> {

            PasswordAnalyzer.Result result =
                    PasswordAnalyzer.analyze(
                            input.getText().toString()
                    );

            card(
                    tr("النتيجة", "Result"),
                    result.level,
                    CYAN
            );

            card(
                    tr("النقاط", "Score"),
                    result.score + " / 100",
                    PINK
            );

            card(
                    tr("النصيحة", "Advice"),
                    result.advice,
                    PURPLE
            );

            currentReport =
                    "Password Analysis\n" +
                    "Level: " + result.level + "\n" +
                    "Score: " + result.score + "/100\n" +
                    "Advice: " + result.advice;
        });
    }

    // REPORTS

    private void saveScanReport() {

        if (currentReport == null ||
                currentReport.trim().isEmpty()) {

            toast(tr("لا توجد نتائج للحفظ", "No results to save"));
            return;
        }

        try {

            File file = ReportManager.saveReport(
                    this,
                    "Security Scan",
                    currentReport
            );

            toast(
                    tr("تم حفظ التقرير: ", "Report saved: ") +
                    file.getName()
            );

        } catch (Exception e) {

            toast("Report Error: " + e.getMessage());
        }
    }

    private void showReports() {

        page(tr("التقارير المحفوظة", "Saved Reports"));

        File directory =
                ReportManager.getReportsDirectory(this);

        File[] files = directory.listFiles();

        if (files == null || files.length == 0) {

            card(
                    tr("التقارير", "Reports"),
                    tr("لا توجد تقارير محفوظة.", "No saved reports."),
                    PINK
            );

            return;
        }

        for (File file : files) {

            button(
                    "📄 " + file.getName(),
                    () -> openReport(file)
            );
        }
    }

    private void openReport(File file) {

        try {

            StringBuilder content = new StringBuilder();

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(
                            new FileInputStream(file),
                            "UTF-8"
                    )
            );

            String line;

            while ((line = reader.readLine()) != null) {

                content.append(line).append("\n");
            }

            reader.close();

            showResult(file.getName(), content.toString());

        } catch (Exception e) {

            toast("Could not open report");
        }
    }

    // ANDROID SECURITY

    private void androidSecurity() {

        page(tr("حماية Android", "Android Security"));

        try {

            java.util.List<SecurityScanner.ScanResult> results =
                    SecurityScanner.scanDevice(this);

            for (SecurityScanner.ScanResult result : results) {

                card(
                        result.title,
                        result.status + "\n" + result.details,
                        result.warning ? PINK : CYAN
                );
            }

        } catch (Exception e) {

            toast("Security Error");
        }
    }

    // LABS

    private void labs() {

        page(tr("المختبر الأمني", "Security Training Labs"));

        card(
                "🧪 " + tr("بيئة تدريب آمنة", "Safe Training Environment"),
                tr(
                        "اختبر فقط الأنظمة التي تملكها أو لديك تصريح لاختبارها.",
                        "Test only systems you own or are authorized to assess."
                ),
                CYAN
        );

        button(
                tr("تحدي التصيد الإلكتروني", "Phishing Challenge"),
                () -> challenge(
                        "Phishing",
                        tr(
                                "لا تشارك رمز OTP مع أي شخص.",
                                "Never share an OTP with anyone."
                        )
                )
        );

        button(
                tr("تحدي سلامة الملفات", "File Integrity Challenge"),
                () -> challenge(
                        "File Integrity",
                        tr(
                                "قارن بصمة الملف بمصدر موثوق قبل تشغيله.",
                                "Compare the file hash with a trusted source."
                        )
                )
        );

        button(
                tr("تحدي الصلاحيات", "Permissions Challenge"),
                () -> challenge(
                        "Permissions",
                        tr(
                                "ارفض الصلاحيات التي لا يحتاجها التطبيق.",
                                "Deny permissions that an app does not need."
                        )
                )
        );

        button(
                tr("اختبار المعلومات", "Knowledge Quiz"),
                this::quiz
        );
    }

    private void challenge(String title, String answer) {

        showResult(title, answer);
    }

    // GLOSSARY

    private void glossary() {

        page(tr("قاموس الأمن السيبراني", "Cybersecurity Glossary"));

        for (String term : glossary) {

            card("CYBER TERM", term, CYAN);
        }
    }

    // ROADMAP

    private void roadmap() {

        page(tr("خطة الدراسة", "Study Roadmap"));

        String[] phases = {
            "1 - Computer Fundamentals",
            "2 - Networking",
            "3 - Linux",
            "4 - Python Programming",
            "5 - Security Fundamentals",
            "6 - Cryptography",
            "7 - Web Security",
            "8 - Digital Forensics",
            "9 - Incident Response",
            "10 - CTF Practice"
        };

        for (String phase : phases) {

            card(
                    tr("مرحلة تعليمية", "Learning Phase"),
                    phase,
                    PURPLE
            );
        }
    }

    // MORE

    private void more() {

        page(tr("المزيد", "More"));

        button(
                tr("قاموس المصطلحات", "Glossary"),
                this::glossary
        );

        button(
                tr("خطة الدراسة", "Study Roadmap"),
                this::roadmap
        );

        button(
                tr("حماية Android", "Android Security"),
                this::androidSecurity
        );

        button(
                tr("التقارير", "Reports"),
                this::showReports
        );

        button(
                tr("الإعدادات", "Settings"),
                this::settings
        );
    }

    // SETTINGS

    private void settings() {

        page(tr("الإعدادات", "Settings"));

        card(
                tr("اللغة الحالية", "Current Language"),
                english ? "English" : "العربية",
                CYAN
        );

        button(
                tr("تغيير اللغة", "Change Language"),
                () -> {

                    english = !english;

                    prefs.edit()
                            .putBoolean("english", english)
                            .apply();

                    home();
                }
        );

        card(
                tr("الإصدار", "Version"),
                "4.0 OFFLINE",
                PINK
        );

        card(
                tr("الخصوصية", "Privacy"),
                tr(
                        "الأدوات تعمل محليًا ولا ترفع بياناتك إلى سيرفر خارجي.",
                        "Tools work locally and do not upload data to an external server."
                ),
                PURPLE
        );
    }
                                      }
