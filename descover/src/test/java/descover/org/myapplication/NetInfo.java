package descover.org.myapplication;/*
 * Copyright (C) 2009-2010 Aubort Jean-Baptiste (Rorist)
 * Licensed under GNU's GPL 2, see README
 */

//am start -a android.intent.action.MAIN -n com.android.settings/.wifi.WifiSettings

import android.content.Context;
import android.content.SharedPreferences;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

// TODO: IPv6 support

public class NetInfo {
    private final String TAG = "NetInfo";
    private static final String NOIF = "0";
    public static final String NOIP = "0.0.0.0";
    public static final String NOMASK = "255.255.255.255";
    public static final String NOMAC = "00:00:00:00:00:00";
    private Context ctxt;
    private WifiInfo info;
    private SharedPreferences prefs;
    public String intf = "eth0";
    public String ip = NOIP;
    public int cidr = 24;
    public int speed = 0;
    public String ssid = null;
    public String bssid = null;
    public String carrier = null;
    public String macAddress = NOMAC;
    public String netmaskIp = NOMASK;
    public String gatewayIp = NOIP;
    public static final String KEY_INTF = "interface";
    public  final String DEFAULT_INTF = null;
    public static final String KEY_IP_START = "ip_start";
    public static final String DEFAULT_IP_START = "0.0.0.0";
    public static final String KEY_IP_END = "ip_end";
    public static final String DEFAULT_IP_END = "0.0.0.0";


    public NetInfo(final Context ctxt) {
        this.ctxt = ctxt;
        prefs = PreferenceManager.getDefaultSharedPreferences(ctxt);
        getIp();
        getWifiInfo();
    }
    @Override
    public int hashCode() {
        int ip_start = prefs.getString(KEY_IP_START, DEFAULT_IP_START).hashCode();
        int ip_end = prefs.getString(KEY_IP_END, DEFAULT_IP_END).hashCode();
        return 42 + intf.hashCode() + ip.hashCode() + cidr +  + ip_start + ip_end ;
    }

    public void getIp() {
        intf = prefs.getString(KEY_INTF, DEFAULT_INTF);
        try {
            if (intf == DEFAULT_INTF || NOIF.equals(intf)) {
                for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en
                        .hasMoreElements();) {
                    NetworkInterface ni = en.nextElement();
                    intf = ni.getName();
                    ip = getInterfaceFirstIp(ni);
                    if (ip != NOIP) {
                        break;
                    }
                }
            } else {

                ip = getInterfaceFirstIp(NetworkInterface.getByName(intf));
            }
        } catch (SocketException e) {
            Log.e(TAG, e.getMessage());
        }
        //getCidr();
    }

    private String getInterfaceFirstIp(NetworkInterface ni) {
        if (ni != null) {
            for (Enumeration<InetAddress> nis = ni.getInetAddresses(); nis.hasMoreElements();) {
                InetAddress ia = nis.nextElement();
                if (!ia.isLoopbackAddress()) {
                    if (ia instanceof Inet6Address) {
                        Log.i(TAG, "IPv6 detected and not supported yet!");
                        continue;
                    }
                    return ia.getHostAddress();
                }
            }
        }
        return NOIP;
    }

    // FIXME: Factorize, this isn't a generic runCommand()


    public boolean getMobileInfo() {
        TelephonyManager tm = (TelephonyManager) ctxt.getSystemService(Context.TELEPHONY_SERVICE);
        if (tm != null) {
            carrier = tm.getNetworkOperatorName();
        }
        return false;
    }

    public boolean getWifiInfo() {
        WifiManager wifi = (WifiManager) ctxt.getSystemService(Context.WIFI_SERVICE);
        if (wifi != null) {
            info = wifi.getConnectionInfo();

            speed = info.getLinkSpeed();
            ssid = info.getSSID();
            bssid = info.getBSSID();
            macAddress = info.getMacAddress();
            gatewayIp = getIpFromIntSigned(wifi.getDhcpInfo().gateway);

            netmaskIp = getIpFromIntSigned(wifi.getDhcpInfo().netmask);
            return true;
        }
        return false;
    }



    public static long getUnsignedLongFromIp(String ip_addr) {
        String[] a = ip_addr.split("\\.");
        return (Integer.parseInt(a[0]) * 16777216 + Integer.parseInt(a[1]) * 65536
                + Integer.parseInt(a[2]) * 256 + Integer.parseInt(a[3]));
    }

    public static String getIpFromIntSigned(int ip_int) {
        String ip = "";
        for (int k = 0; k < 4; k++) {
            ip = ip + ((ip_int >> k * 8) & 0xFF) + ".";
        }
        return ip.substring(0, ip.length() - 1);
    }

    public static String getIpFromLongUnsigned(long ip_long) {
        String ip = "";
        for (int k = 3; k > -1; k--) {
            ip = ip + ((ip_long >> k * 8) & 0xFF) + ".";
        }
        return ip.substring(0, ip.length() - 1);
    }




}
