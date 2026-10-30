package com.emperor.transfer;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.LinkProperties;
import android.net.LinkAddress;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

public class NetworkScanner {

    public static class NetworkResult {
        public String title;
        public String value;

        public NetworkResult(String title, String value) {
            this.title = title;
            this.value = value;
        }
    }

    public static List<NetworkResult> scan(Context context) {

        List<NetworkResult> results = new ArrayList<>();

        ConnectivityManager manager =
                (ConnectivityManager) context.getSystemService(
                        Context.CONNECTIVITY_SERVICE
                );

        if (manager == null) {
            results.add(new NetworkResult(
                    "Network",
                    "Unavailable"
            ));
            return results;
        }

        Network network = manager.getActiveNetwork();

        if (network == null) {
            results.add(new NetworkResult(
                    "Connection",
                    "No active connection"
            ));
            return results;
        }

        NetworkCapabilities capabilities =
                manager.getNetworkCapabilities(network);

        if (capabilities != null) {

            boolean wifi = capabilities.hasTransport(
                    NetworkCapabilities.TRANSPORT_WIFI
            );

            boolean mobile = capabilities.hasTransport(
                    NetworkCapabilities.TRANSPORT_CELLULAR
            );

            String type = wifi ? "Wi-Fi" :
                    mobile ? "Mobile Data" : "Other";

            results.add(new NetworkResult(
                    "Connection Type",
                    type
            ));

            results.add(new NetworkResult(
                    "Internet Capability",
                    capabilities.hasCapability(
                            NetworkCapabilities.NET_CAPABILITY_INTERNET
                    ) ? "Available by network configuration"
                      : "Not reported"
            ));
        }

        LinkProperties properties =
                manager.getLinkProperties(network);

        if (properties != null) {

            for (LinkAddress address :
                    properties.getLinkAddresses()) {

                InetAddress ip = address.getAddress();

                if (ip != null && !ip.isLoopbackAddress()) {

                    results.add(new NetworkResult(
                            "IP Address",
                            ip.getHostAddress()
                    ));
                }
            }

            if (properties.getDnsServers() != null) {
                for (InetAddress dns :
                        properties.getDnsServers()) {

                    results.add(new NetworkResult(
                            "DNS Server",
                            dns.getHostAddress()
                    ));
                }
            }
        }

        return results;
    }
}
