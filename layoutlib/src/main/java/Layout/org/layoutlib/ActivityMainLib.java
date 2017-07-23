package Layout.org.layoutlib;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by user on 26.05.2017.
 */

public class ActivityMainLib extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    protected static final String SELECTED_ITEM_ID = "selected_item_id";
    private static final String FIRST_TIME = "first_time";
    private Toolbar mToolbar;
    private NavigationView mDrawer;
    protected DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    protected int mSelectedId;
    private boolean mUserSawDrawer = false;
    EditText edx;
    Button btnMachine;
    public static int NumMachine;
    public static int getMachineNumber = 4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.machinnumber);
        setContentView(R.layout.activity_main);
        mToolbar = (Toolbar) findViewById(R.id.app_bar);
        mDrawer = (NavigationView) findViewById(R.id.main_drawer);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawer.setNavigationItemSelectedListener(this);
        setSupportActionBar(mToolbar);

        edx = (EditText)findViewById(R.id.numMachine);
        btnMachine= (Button)findViewById(R.id.btnMachine);







        mDrawerToggle = new ActionBarDrawerToggle(this,
                mDrawerLayout,
                mToolbar,
                Layout.org.layoutlib.R.string.drawer_open,
                Layout.org.layoutlib.R.string.drawer_close);

        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();
        if (!didUserSeeDrawer()) {
            showDrawer();
            markDrawerSeen();
        } else {
            hideDrawer();
        }
        mSelectedId = savedInstanceState == null ? Layout.org.layoutlib.R.id.first_item : savedInstanceState.getInt(SELECTED_ITEM_ID);
        navigate(mSelectedId);
    }



    private boolean didUserSeeDrawer() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mUserSawDrawer = sharedPreferences.getBoolean(FIRST_TIME, false);
        return mUserSawDrawer;
    }

    private void markDrawerSeen() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mUserSawDrawer = true;
        sharedPreferences.edit().putBoolean(FIRST_TIME, mUserSawDrawer).apply();
    }

    private void showDrawer() {
        mDrawerLayout.openDrawer(GravityCompat.START);
    }

    private void hideDrawer() {
        mDrawerLayout.closeDrawer(GravityCompat.START);
    }

    private void navigate(int mSelectedId) {
        Intent intent = null;


        if (mSelectedId == Layout.org.layoutlib.R.id.first_item) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
  //          intent = new Intent(this, AutoActivity.class);
    //        startActivity(intent);
        }
        if (mSelectedId == Layout.org.layoutlib.R.id.secund_item) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
   //         intent = new Intent(this, MattActivity.class);
  //          startActivity(intent);
        }
        if (mSelectedId == Layout.org.layoutlib.R.id.Third_item) {
            mDrawerLayout.closeDrawer(GravityCompat.START);

        }
        if (mSelectedId == Layout.org.layoutlib.R.id.five_item) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
 //           intent = new Intent(this, FourthActivity.class);
 //           startActivity(intent);
        }
        if (mSelectedId == Layout.org.layoutlib.R.id.seven_item) {
            mDrawerLayout.closeDrawer(GravityCompat.START);

        }
        if (mSelectedId == Layout.org.layoutlib.R.id.acht_item) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
            //setContentView(R.layout.discovery);

            //  ActivityDiscovery activityDiscovery = new ActivityDiscovery();
            //   activityDiscovery.startDiscovering();
            //  intent = new Intent(this, descover.org.mylibrary.ActivityDiscovery.class);
            // startActivity(intent);
            // intent = new Intent(this, info.lamatricexiste.network.ActivityDiscovery.class);
            // startActivity(intent);
            // setContentView(R.layout.discovery);
            // setContentView(R.layout.discovery);
            //final Button btn_discover = (Button) findViewById(info.lamatricexiste.network.R.id.btn_discover);
            // btn_discover.setOnClickListener(new View.OnClickListener() {
            //   public void onClick(View v) {
            //    final ActivityDiscovery activityDiscovery = new ActivityDiscovery();
            //      activityDiscovery.startDiscovering();
            //    }
            //  });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(Layout.org.layoutlib.R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.MachineNumber) {
            setContentView(R.layout.machinnumber);

            return true;
        }
        if (id == Layout.org.layoutlib.R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override

    // zähler
    public boolean onNavigationItemSelected(MenuItem menuItem) {

        menuItem.setChecked(true);
        mSelectedId = menuItem.getItemId();

        navigate(mSelectedId);
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SELECTED_ITEM_ID, mSelectedId);
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

 //   public void SetMachineNumber(View view) {
 //      NumMachine = Integer.parseInt(edx.getText().toString());
 //       Toast.makeText(this, String.valueOf(NumMachine), Toast.LENGTH_SHORT).show();
   // }
}
