package descover.org.myapplication;/*
 * Copyright (C) 2009-2010 Aubort Jean-Baptiste (Rorist)
 * Licensed under GNU's GPL 2, see README
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Vibrator;
import android.preference.PreferenceActivity;
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
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


final public class ActivityDiscovery extends ActivityNet implements OnItemClickListener {

    private final String TAG = "ActivityDiscovery";
    public final static long VIBRATE = (long) 250;

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
    private HostsAdapter adapter;
    private Button btn_discover;
    private AbstractDiscovery mDiscoveryTask = null;
    public final static String KEY_RESOLVE_NAME = "resolve_name";
    public final static boolean DEFAULT_RESOLVE_NAME = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_PROGRESS);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.discovery);
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
        ListView list = (ListView) findViewById(R.id.output);
        list.setAdapter(adapter);
        list.setItemsCanFocus(false);
        list.setOnItemClickListener(this);
        list.setEmptyView(findViewById(R.id.list_empty));


    }

    @Override
    public void onResume() {
        super.onResume();
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

        if (currentNetwork != net.hashCode()) {
            Log.i(TAG, "Network info has changed");
            currentNetwork = net.hashCode();

            // Cancel running tasks
            cancelTasks();
        } else {
            return;
        }

        // Get ip information
        network_ip = NetInfo.getUnsignedLongFromIp(net.ip);
        if (prefs.getBoolean(Prefs.KEY_IP_CUSTOM, Prefs.DEFAULT_IP_CUSTOM)) {
            // Custom IP
            network_start = NetInfo.getUnsignedLongFromIp(prefs.getString(Prefs.KEY_IP_START,
                    Prefs.DEFAULT_IP_START));
            network_end = NetInfo.getUnsignedLongFromIp(prefs.getString(Prefs.KEY_IP_END,
                    Prefs.DEFAULT_IP_END));
        } else {
            // Custom CIDR
            if (prefs.getBoolean(Prefs.KEY_CIDR_CUSTOM, Prefs.DEFAULT_CIDR_CUSTOM)) {
                net.cidr = Integer.parseInt(prefs.getString(Prefs.KEY_CIDR, Prefs.DEFAULT_CIDR));
            }
            // Detected IP
            int shift = (32 - net.cidr);
            if (net.cidr < 31) {
                network_start = (network_ip >> shift << shift) + 1;
                network_end = (network_start | ((1 << shift) - 1)) - 1;
            } else {
                network_start = (network_ip >> shift << shift);
                network_end = (network_start | ((1 << shift) - 1));
            }
            // Reset ip start-end (is it really convenient ?)
            Editor edit = prefs.edit();
            edit.putString(Prefs.KEY_IP_START, NetInfo.getIpFromLongUnsigned(network_start));
            edit.putString(Prefs.KEY_IP_END, NetInfo.getIpFromLongUnsigned(network_end));
            edit.commit();
        }
    }

    @Override
    protected void setButtons(boolean disable) {
    }

    @Override
    protected void cancelTasks() {
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
    private class HostsAdapter extends ArrayAdapter<Void> {
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
            } else if (host.isAlive == 1 || !host.hardwareAddress.equals(NetInfo.NOMAC)) {
                holder.logo.setImageResource(R.drawable.computer);
            } else {
                holder.logo.setImageResource(R.drawable.computer_down);
            }
            if (host.hostname != null && !host.hostname.equals(host.ipAddress)) {
                holder.host.setText(host.hostname + " (" + host.ipAddress + ")");
            } else {
                holder.host.setText(host.ipAddress);
            }
            if (!host.hardwareAddress.equals(NetInfo.NOMAC)) {
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
    private void startDiscovering() {

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
                    Log.v(TAG, "start=" + NetInfo.getIpFromLongUnsigned(start) + " (" + start
                            + "), end=" + NetInfo.getIpFromLongUnsigned(end) + " (" + end
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
                mPool.execute(new CheckRunnable(NetInfo.getIpFromLongUnsigned(i)));
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
                    if(!NetInfo.NOMAC.equals(host.hardwareAddress)){
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
                    if(!NetInfo.NOMAC.equals(host.hardwareAddress)){
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
                    if(!NetInfo.NOMAC.equals(host.hardwareAddress)){
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
                    if(NetInfo.NOMAC.equals(host.hardwareAddress)){
                        host.hardwareAddress = getHardwareAddress(host.ipAddress);
                    }
                    // NIC vendor
                  //  host.nicVendor = getNicVendor(host.hardwareAddress);

                    // Is gateway ?
                    if (discover.net.gatewayIp.equals(host.ipAddress)) {
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
            String hw = NetInfo.NOMAC;
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
                    discover.makeToast(R.string.discover_finished);
                 //   discover.stopDiscovering();
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
