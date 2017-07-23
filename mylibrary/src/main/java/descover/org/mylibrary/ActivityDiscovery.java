/*
 * Copyright (C) 2009-2010 Aubort Jean-Baptiste (Rorist)
 * Licensed under GNU's GPL 2, see README
 */

package descover.org.mylibrary;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Vibrator;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public abstract class ActivityDiscovery extends Activity implements OnItemClickListener {

    private final String TAG = "ActivityDiscovery";
    public final static long VIBRATE = (long) 250;
    public static ListView list;
    public static final int MENU_SCAN_SINGLE = 0;
    public static final int MENU_OPTIONS = 1;
    public static final int MENU_HELP = 2;
    private static final int MENU_EXPORT = 3;
    private static LayoutInflater mInflater;
    private int currentNetwork = 0;
    private long network_ip = 0;
    private long network_start = 0;
    private long network_end = 0;
    private List<HostBean> hosts = null;
    protected HostsAdapter adapter;
    protected Button btn_discover;
    private AbstractDiscovery mDiscoveryTask = null;
    public final static String KEY_RESOLVE_NAME = "resolve_name";
    public final static boolean DEFAULT_RESOLVE_NAME = true;



    private final String TAG10 = "NetState";
    private ConnectivityManager connMgr;
    public static final String KEY_MOBILE = "allow_mobile";
    public static final boolean DEFAULT_MOBILE = false;
    public static final String KEY_INTF = "interface";
    public static final String DEFAULT_INTF = null;
    protected Context ctxt;
    protected SharedPreferences prefs = null;
    protected String info_ip_str = "";
    protected String info_in_str = "";
    protected String info_mo_str = "";

    private final String TAG12 = "NetInfo";
    private static final String NOIF = "0";
    public static final String NOIP = "0.0.0.0";
    public static final String NOMASK = "255.255.255.255";
    public static final String NOMAC = "00:00:00:00:00:00";
    private WifiInfo info;
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
    public static final String KEY_IP_START = "ip_start";
    public static final String DEFAULT_IP_START = "0.0.0.0";
    public static final String KEY_IP_END = "ip_end";
    public static final String DEFAULT_IP_END = "0.0.0.0";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_PROGRESS);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.discovery);
        ctxt = getApplicationContext();
        prefs = PreferenceManager.getDefaultSharedPreferences(ctxt);
        connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        mInflater = LayoutInflater.from(ctxt);

        // Discover
        btn_discover = (Button) findViewById(R.id.btn_discover);
        btn_discover.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startDiscovering();
            }
        });

        // Options

        // Hosts list
        adapter = new HostsAdapter(ctxt);
        list = (ListView) findViewById(R.id.output);
        list.setAdapter(adapter);
        list.setItemsCanFocus(false);
        list.setOnItemClickListener(this);
        list.setEmptyView(findViewById(R.id.list_empty));
        prefs = PreferenceManager.getDefaultSharedPreferences(ctxt);
        getIp();
        getWifiInfo();

    }

    @Override
    public void onResume() {
        super.onResume();
        setButtons(true);
        // Listening for network events
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        filter.addAction(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION);
        registerReceiver(receiver, filter);
    }


    protected abstract void setButtons(boolean disable);

    protected abstract void cancelTasks();

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        public void onReceive(Context ctxt, Intent intent) {
            info_ip_str = "";
            info_mo_str = "";

            // Wifi state
            String action = intent.getAction();
            if (action != null) {
                if (action.equals(WifiManager.WIFI_STATE_CHANGED_ACTION)) {
                    int WifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, -1);

                    switch (WifiState) {
                        case WifiManager.WIFI_STATE_ENABLING:
                            info_in_str = getString(R.string.wifi_enabling);
                            break;
                        case WifiManager.WIFI_STATE_ENABLED:
                            info_in_str = getString(R.string.wifi_enabled);
                            break;
                        case WifiManager.WIFI_STATE_DISABLING:
                            info_in_str = getString(R.string.wifi_disabling);
                            break;
                        case WifiManager.WIFI_STATE_DISABLED:
                            info_in_str = getString(R.string.wifi_disabled);
                            break;
                        default:
                            info_in_str = getString(R.string.wifi_unknown);
                    }
                }
            }

            final NetworkInfo ni = connMgr.getActiveNetworkInfo();
            if (ni != null) {

                if (ni.getDetailedState() == NetworkInfo.DetailedState.CONNECTED) {
                    int type = ni.getType();

                    if (type == ConnectivityManager.TYPE_WIFI) { // WIFI
                        getWifiInfo();
                        if (ssid != null) {
                            getIp();
                            info_ip_str = getString(R.string.net_ip, ip, cidr, intf);
                            info_in_str = getString(R.string.net_ssid, ssid);
                            info_mo_str = getString(R.string.net_mode, getString(
                                    R.string.net_mode_wifi, speed, WifiInfo.LINK_SPEED_UNITS));
                            setButtons(false);
                        }
                    } else if (type == ConnectivityManager.TYPE_MOBILE) { // 3G
                        if (prefs.getBoolean(KEY_MOBILE, DEFAULT_MOBILE)
                                || prefs.getString(KEY_INTF, DEFAULT_INTF) != null) {
                            getMobileInfo();
                            if (carrier != null) {
                                getIp();
                                info_ip_str = getString(R.string.net_ip, ip, cidr, intf);
                                info_in_str = getString(R.string.net_carrier, carrier);
                                info_mo_str = getString(R.string.net_mode,
                                        getString(R.string.net_mode_mobile));
                                setButtons(false);
                            }
                        }
                    } else if (type == 3 || type == 9) { // ETH
                        getIp();
                        info_ip_str = getString(R.string.net_ip, ip, cidr, intf);
                        info_in_str = "";
                        info_mo_str = getString(R.string.net_mode) + getString(R.string.net_mode_eth);
                        setButtons(false);
                        Log.i(TAG10, "Ethernet connectivity detected!");
                    } else {
                        Log.i(TAG, "Connectivity unknown!");
                        info_mo_str = getString(R.string.net_mode)
                                + getString(R.string.net_mode_unknown);
                    }
                } else {
                    cancelTasks();
                }
            } else {
                cancelTasks();
            }

            setInfo();
        }
    };
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
            Log.e(TAG12, e.getMessage());
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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, ActivityDiscovery.MENU_SCAN_SINGLE, 0, R.string.scan_single_title).setIcon(
                android.R.drawable.ic_menu_mylocation);
        menu.add(0, ActivityDiscovery.MENU_EXPORT, 0, R.string.preferences_export).setIcon(
                android.R.drawable.ic_menu_save);
        menu.add(0, ActivityDiscovery.MENU_OPTIONS, 0, R.string.btn_options).setIcon(
                android.R.drawable.ic_menu_preferences);
        menu.add(0, ActivityDiscovery.MENU_HELP, 0, R.string.preferences_help).setIcon(
                android.R.drawable.ic_menu_help);
        return true;
    }
    protected void setInfo() {
        // Info
        ((TextView) findViewById(R.id.info_ip)).setText(info_ip_str);
        ((TextView) findViewById(R.id.info_in)).setText(info_in_str);
        ((TextView) findViewById(R.id.info_mo)).setText(info_mo_str);

        // Scan button state
        if (mDiscoveryTask != null) {
        }

        if (currentNetwork != hashCode()) {
            Log.i(TAG, "Network info has changed");
            currentNetwork = hashCode();

            // Cancel running tasks
            cancelTasks();
        } else {
            return;
        }

        // Get ip information
        network_ip = getUnsignedLongFromIp(ip);
        if (prefs.getBoolean(Prefs.KEY_IP_CUSTOM, Prefs.DEFAULT_IP_CUSTOM)) {
            // Custom IP
            network_start = getUnsignedLongFromIp(prefs.getString(Prefs.KEY_IP_START,
                    Prefs.DEFAULT_IP_START));
            network_end = getUnsignedLongFromIp(prefs.getString(Prefs.KEY_IP_END,
                    Prefs.DEFAULT_IP_END));
        } else {
            // Custom CIDR
            if (prefs.getBoolean(Prefs.KEY_CIDR_CUSTOM, Prefs.DEFAULT_CIDR_CUSTOM)) {
                cidr = Integer.parseInt(prefs.getString(Prefs.KEY_CIDR, Prefs.DEFAULT_CIDR));
            }
            // Detected IP
            int shift = (32 - cidr);
            if (cidr < 31) {
                network_start = (network_ip >> shift << shift) + 1;
                network_end = (network_start | ((1 << shift) - 1)) - 1;
            } else {
                network_start = (network_ip >> shift << shift);
                network_end = (network_start | ((1 << shift) - 1));
            }
            // Reset ip start-end (is it really convenient ?)
            Editor edit = prefs.edit();
            edit.putString(Prefs.KEY_IP_START, getIpFromLongUnsigned(network_start));
            edit.putString(Prefs.KEY_IP_END, getIpFromLongUnsigned(network_end));
            edit.commit();
        }
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    }
    static class ViewHolder {
        TextView host;
        TextView mac;
        TextView vendor;
        ImageView logo;
    }
    // Custom ArrayAdapter
    protected class HostsAdapter extends ArrayAdapter<Void> {
        public HostsAdapter(Context ctxt)
        {
            super(ctxt, R.layout.list_host, R.id.list);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.list_host, null);
                holder = new ViewHolder();
                holder.host = (TextView) convertView.findViewById(R.id.list);
                holder.mac = (TextView) convertView.findViewById(R.id.mac);
                holder.vendor = (TextView) convertView.findViewById(R.id.vendor);
                holder.logo = (ImageView) convertView.findViewById(R.id.logo);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            final HostBean host = hosts.get(position);
            if (host.deviceType == HostBean.TYPE_GATEWAY) {
                holder.logo.setImageResource(R.drawable.router);
            } else if (host.isAlive == 1 || !host.hardwareAddress.equals(NOMAC)) {
                holder.logo.setImageResource(R.drawable.computer);
            } else {
                holder.logo.setImageResource(R.drawable.computer_down);
            }
            if (host.hostname != null && !host.hostname.equals(host.ipAddress)) {
                holder.host.setText(host.hostname + " (" + host.ipAddress + ")");
            } else {
                holder.host.setText(host.ipAddress);
            }
            if (!host.hardwareAddress.equals(NOMAC)) {
                holder.mac.setText(host.hardwareAddress);
                if(host.nicVendor != null){
                    holder.vendor.setText(host.nicVendor);
                } else {
                    holder.vendor.setText(R.string.info_unknown);
                }
                holder.mac.setVisibility(View.VISIBLE);
                holder.vendor.setVisibility(View.VISIBLE);
            } else {
                holder.mac.setVisibility(View.GONE);
                holder.vendor.setVisibility(View.GONE);
            }
            return convertView;
        }
    }
    /**
     * Discover hosts
     */
    public void startDiscovering() {

        // Switch  case 0
        mDiscoveryTask = new DefaultDiscovery(ActivityDiscovery.this);

        // mDiscoveryTask = new DnDiscovery(ActivityDiscovery.this);

        mDiscoveryTask.setNetwork(network_ip, network_start, network_end);
        mDiscoveryTask.execute();


        makeToast(R.string.discover_start);
        // setProgressBarVisibility(true);
        // setProgressBarIndeterminateVisibility(true);
        initList();
    }
    private void initList() {
        // setSelectedHosts(false);
        // adapter.clear();
        hosts = new ArrayList<HostBean>();
    }
    public void addHost(HostBean host) {
        host.position = hosts.size();
        hosts.add(host);
        adapter.add(null);
    }
    public void makeToast(int msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }
    public class Prefs extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

        public static final String KEY_IP_START = "ip_start";
        public static final String DEFAULT_IP_START = "0.0.0.0";

        public static final String KEY_IP_END = "ip_end";
        public static final String DEFAULT_IP_END = "0.0.0.0";

        public static final String KEY_IP_CUSTOM = "ip_custom";
        public static final boolean DEFAULT_IP_CUSTOM = false;

        public static final String KEY_CIDR_CUSTOM = "cidr_custom";
        public static final boolean DEFAULT_CIDR_CUSTOM = false;

        public static final String KEY_CIDR = "cidr";
        public static final String DEFAULT_CIDR = "24";

        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        }
    }
    public class DefaultDiscovery extends AbstractDiscovery {

        private final String TAG = "DefaultDiscovery";
        private final  int[] DPORTS = { 139, 445, 22, 80 };
        private final static int TIMEOUT_SCAN = 3600; // seconds
        private final static int TIMEOUT_SHUTDOWN = 10; // seconds
        private final static int THREADS = 10; //FIXME: Test, plz set in options again ?
        private final int mRateMult = 5; // Number of alive hosts between Rate
        private int pt_move = 2; // 1=backward 2=forward
        private ExecutorService mPool;
        private boolean doRateControl;


        public String indicator = null;
        public int rate = 800; // Slow start
        private final static String REQ = "select vendor from oui where mac=?";

        private final static String MAC_RE = "^%s\\s+0x1\\s+0x2\\s+([:0-9a-fA-F]+)\\s+\\*\\s+\\w+$";
        private final static int BUF2 = 8 * 1024;
        private final static String TAG2 = "HardwareAddress";
        public static final String PATH = "/data/data/info.lamatricexiste.network/files/";

        private  SQLiteDatabase db;
        public static final String DB_SAVES = "saves.db";
        private static final String TAG4 = "Save";

        public static final String KEY_RATECTRL_ENABLE = "ratecontrol_enable";
        public static final boolean DEFAULT_RATECTRL_ENABLE = true;

        public final static String KEY_TIMEOUT_DISCOVER = "timeout_discover";
        public final static String DEFAULT_TIMEOUT_DISCOVER = "500";


        public DefaultDiscovery(ActivityDiscovery discover) {
            super(discover);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (mDiscover != null) {
                final ActivityDiscovery discover = mDiscover.get();
                if (discover != null) {
                    doRateControl = discover.prefs.getBoolean(KEY_RATECTRL_ENABLE,
                            DEFAULT_RATECTRL_ENABLE);
                }
            }
        }

        @Override
        protected Void doInBackground(Void... params) {
            if (mDiscover != null) {
                final ActivityDiscovery discover = mDiscover.get();
                if (discover != null) {
                    Log.v(TAG, "start=" + getIpFromLongUnsigned(start) + " (" + start
                            + "), end=" + getIpFromLongUnsigned(end) + " (" + end
                            + "), length=" + size);
                    mPool = Executors.newFixedThreadPool(THREADS);
                    if (ip <= end && ip >= start) {
                        Log.i(TAG, "Back and forth scanning");
                        // gateway
                        launch(start);

                        // hosts
                        long pt_backward = ip;
                        long pt_forward = ip + 1;
                        long size_hosts = size - 1;

                        for (int i = 0; i < size_hosts; i++) {
                            // Set pointer if of limits
                            if (pt_backward <= start) {
                                pt_move = 2;
                            } else if (pt_forward > end) {
                                pt_move = 1;
                            }
                            // Move back and forth
                            if (pt_move == 1) {
                                launch(pt_backward);
                                pt_backward--;
                                pt_move = 2;
                            } else if (pt_move == 2) {
                                launch(pt_forward);
                                pt_forward++;
                                pt_move = 1;
                            }
                        }
                    } else {
                        Log.i(TAG, "Sequencial scanning");
                        for (long i = start; i <= end; i++) {
                            launch(i);
                        }
                    }
                    mPool.shutdown();
                    try {
                        if(!mPool.awaitTermination(TIMEOUT_SCAN, TimeUnit.SECONDS)){
                            mPool.shutdownNow();
                            Log.e(TAG, "Shutting down pool");
                            if(!mPool.awaitTermination(TIMEOUT_SHUTDOWN, TimeUnit.SECONDS)){
                                Log.e(TAG, "Pool did not terminate");
                            }
                        }
                    } catch (InterruptedException e){
                        Log.e(TAG, e.getMessage());
                        mPool.shutdownNow();
                        Thread.currentThread().interrupt();
                    } finally {
                        //   closeDb();
                    }
                }
            }
            return null;
        }

        @Override
        protected void onCancelled() {
            if (mPool != null) {
                synchronized (mPool) {
                    mPool.shutdownNow();
                    // FIXME: Prevents some task to end (and close the Save DB)
                }
            }
            super.onCancelled();
        }

        private void launch(long i) {
            if(!mPool.isShutdown()) {
                mPool.execute(new CheckRunnable(getIpFromLongUnsigned(i)));
            }
        }

        private int getRate() {
            if (doRateControl) {
                return rate;
            }

            if (mDiscover != null) {
                final ActivityDiscovery discover = mDiscover.get();
                if (discover != null) {
                    return Integer.parseInt(discover.prefs.getString(KEY_TIMEOUT_DISCOVER,
                            DEFAULT_TIMEOUT_DISCOVER));
                }
            }
            return 1;
        }

        private class CheckRunnable implements Runnable {
            private String addr;

            CheckRunnable(String addr) {
                this.addr = addr;
            }

            public void run() {
                if(isCancelled()) {
                    publish(null);
                }
                Log.e(TAG, "run="+addr);
                // Create host object
                final HostBean host = new HostBean();
                host.responseTime = getRate();
                host.ipAddress = addr;
                try {
                    InetAddress h = InetAddress.getByName(addr);
                    // Rate control check
                    if (doRateControl && indicator != null && hosts_done % mRateMult == 0) {
                        adaptRate();
                    }
                    // Arp Check #1
                    host.hardwareAddress = getHardwareAddress(addr);
                    if(!NOMAC.equals(host.hardwareAddress)){
                        Log.e(TAG, "found using arp #1 "+addr);
                        publish(host);
                        return;
                    }
                    // Native InetAddress check
                    if (h.isReachable(getRate())) {
                        Log.e(TAG, "found using InetAddress ping "+addr);
                        publish(host);
                        // Set indicator and get a rate
                        if (doRateControl && indicator == null) {
                            indicator = addr;
                            adaptRate();
                        }
                        return;
                    }
                    // Arp Check #2
                    host.hardwareAddress = getHardwareAddress(addr);
                    if(!NOMAC.equals(host.hardwareAddress)){
                        Log.e(TAG, "found using arp #2 "+addr);
                        publish(host);
                        return;
                    }
                    // Custom check
                    int port;
                    // TODO: Get ports from options
                    Socket s = new Socket();
                    for (int i = 0; i < DPORTS.length; i++) {
                        try {
                            s.bind(null);
                            s.connect(new InetSocketAddress(addr, DPORTS[i]), getRate());
                            Log.v(TAG, "found using TCP connect "+addr+" on port=" + DPORTS[i]);
                        } catch (IOException e) {
                        } catch (IllegalArgumentException e) {
                        } finally {
                            try {
                                s.close();
                            } catch (Exception e){
                            }
                        }
                    }
                    // Arp Check #3
                    host.hardwareAddress = getHardwareAddress(addr);
                    if(!NOMAC.equals(host.hardwareAddress)){
                        Log.e(TAG, "found using arp #3 "+addr);
                        publish(host);
                        return;
                    }
                    publish(null);

                } catch (IOException e) {
                    publish(null);
                    Log.e(TAG, e.getMessage());
                }
            }
        }

        private void publish(final HostBean host) {
            hosts_done++;
            if(host == null){
                publishProgress((HostBean) null);
                return;
            }

            if (mDiscover != null) {
                final ActivityDiscovery discover = mDiscover.get();
                if (discover != null) {
                    // Mac Addr not already detected
                    if(NOMAC.equals(host.hardwareAddress)){
                        host.hardwareAddress = getHardwareAddress(host.ipAddress);
                    }
                    // NIC vendor
                    //  host.nicVendor = getNicVendor(host.hardwareAddress);

                    // Is gateway ?
                    if (discover.gatewayIp.equals(host.ipAddress)) {
                        host.deviceType = HostBean.TYPE_GATEWAY;
                    }
                    // FQDN
                    // Static

                    // ermitteln IP + Name off Device
                    if ((host.hostname = getCustomName(host)) == null) {
                        // DNS
                        if (discover.prefs.getBoolean(KEY_RESOLVE_NAME,
                                DEFAULT_RESOLVE_NAME) == true) {
                            try {
                                host.hostname = (InetAddress.getByName(host.ipAddress)).getCanonicalHostName();
                            } catch (UnknownHostException e) {
                                Log.e(TAG, e.getMessage());
                            }
                        }
                        // TODO: NETBIOS

                    }
                }
            }

            publishProgress(host);
        }

        public void adaptRate() {
            int response_time = 0;
        }

        public  String getHardwareAddress(String ip) {
            String hw = NOMAC;
            BufferedReader bufferedReader = null;
            try {
                if (ip != null) {
                    String ptrn = String.format(MAC_RE, ip.replace(".", "\\."));
                    Pattern pattern = Pattern.compile(ptrn);
                    bufferedReader = new BufferedReader(new FileReader("/proc/net/arp"), BUF2);
                    String line;
                    Matcher matcher;
                    while ((line = bufferedReader.readLine()) != null) {
                        matcher = pattern.matcher(line);
                        if (matcher.matches()) {
                            hw = matcher.group(1);
                            break;
                        }
                    }
                } else {
                    Log.e(TAG2, "ip is null");
                }
            } catch (IOException e) {
                Log.e(TAG2, "Can't open/read file ARP: " + e.getMessage());
                return hw;
            } finally {
                try {
                    if(bufferedReader != null) {
                        bufferedReader.close();
                    }
                } catch (IOException e) {
                    Log.e(TAG2, e.getMessage());
                }
            }
            return hw;
        }
// ermittllen ip + Name off Device

        public synchronized String getCustomName(HostBean host) {
            String name = null;
            //    Cursor c = null;
            //   try {
            //     db = getDb();
            //     c = db.rawQuery(SELECT, new String[] { host.hardwareAddress.replace(":", "").toUpperCase() });
            //   if (c.moveToFirst()) {
            //     name = c.getString(0);
            //  } else if(host.hostname != null) {
            //    name = host.hostname;
            //   }
            //  } catch (SQLiteException e) {
            //    Log.e(TAG, e.getMessage());
            // } catch (IllegalStateException e) {
            //    Log.e(TAG, e.getMessage());
            // } finally {
            //   if (c != null) {
            //      c.close();
            //    }
            //  }
            return name;
        }
        private  synchronized SQLiteDatabase getDb(){
            if(db == null || !db.isOpen()) {
                // FIXME: read only ?
                db = openDb(DB_SAVES, SQLiteDatabase.NO_LOCALIZED_COLLATORS|SQLiteDatabase.OPEN_READONLY);
            }
            return db;
        }
        public  SQLiteDatabase openDb(String db_name, int flags) {
            try {
                return SQLiteDatabase.openDatabase(PATH + db_name, null, flags);
            } catch (SQLiteException e) {
                Log.e(TAG4, e.getMessage());
            }
            return null;
        }
    }
    public abstract class AbstractDiscovery extends AsyncTask<Void, HostBean, Void> {
        //private final String TAG = "AbstractDiscovery";

        protected int hosts_done = 0;
        final protected WeakReference<ActivityDiscovery> mDiscover;

        protected long ip;
        protected long start = 0;
        protected long end = 0;
        protected long size = 0;
        public final static String KEY_VIBRATE_FINISH = "vibrate_finish";
        public final static boolean DEFAULT_VIBRATE_FINISH = false;

        public AbstractDiscovery(ActivityDiscovery discover) {
            mDiscover = new WeakReference<ActivityDiscovery>(discover);
        }
        public void setNetwork(long ip, long start, long end) {
            this.ip = ip;
            this.start = start;
            this.end = end;
        }
        abstract protected Void doInBackground(Void... params);
        @Override
        protected void onPreExecute() {
            size = (int) (end - start + 1);
            if (mDiscover != null) {
                final ActivityDiscovery discover = mDiscover.get();
                if (discover != null) {
                    discover.setProgress(0);
                }
            }
        }
        @Override
        protected void onProgressUpdate(HostBean... host) {
            if (mDiscover != null) {
                final ActivityDiscovery discover = mDiscover.get();
                if (discover != null) {
                    if (!isCancelled()) {
                        if (host[0] != null) {
                            discover.addHost(host[0]);
                        }
                        if (size > 0) {
                            discover.setProgress((int) (hosts_done * 10000 / size));
                        }
                    }
                }
            }
        }
        @Override
        protected void onPostExecute(Void unused) {
            if (mDiscover != null) {
                final ActivityDiscovery discover = mDiscover.get();
                if (discover != null) {
                    if (discover.prefs.getBoolean(KEY_VIBRATE_FINISH,
                            DEFAULT_VIBRATE_FINISH) == true) {
                        Vibrator v = (Vibrator) discover.getSystemService(Context.VIBRATOR_SERVICE);
                        v.vibrate(ActivityDiscovery.VIBRATE);
                    }
                //    discover.makeToast(R.string.discover_finished);
                   //    discover.stopDiscovering();

                    int i = 3;
                    Toast.makeText(ActivityDiscovery.this,
                            String.valueOf(list.getAdapter().getCount()-1), Toast.LENGTH_LONG).show();

              //      discover.makeToast(list.g);
                }
            }
        }



    }
    public class HostBean implements Parcelable {
        public static final int TYPE_GATEWAY = 0;
        public static final int TYPE_COMPUTER = 1;

        public int deviceType = TYPE_COMPUTER;
        public int isAlive = 1;
        public int position = 0;
        public int responseTime = 0; // ms
        public String ipAddress = null;
        public String hostname = null;
        public String hardwareAddress = NOMAC;
        public String nicVendor = "Unknown";
        public static final String NOMAC = "00:00:00:00:00:00";

        public HostBean() {
            // New object
        }
        public HostBean(Parcel in) {
            // Object from parcel
        }

        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {

        }

    }
}
