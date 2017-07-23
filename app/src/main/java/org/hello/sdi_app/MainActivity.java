package org.hello.sdi_app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.view.MenuItem;

import Layout.org.layoutlib.ActivityMainLib;


public class MainActivity extends ActivityMainLib {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }
    private void navigate(int mSelectedId) {
        Intent intent = null;


        if (mSelectedId == R.id.first_item) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
                     intent = new Intent(this, AutoActivity.class);
                     startActivity(intent);
        }
        if (mSelectedId == R.id.secund_item) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
                    intent = new Intent(this, MattActivity.class);
                    startActivity(intent);
        }
        if (mSelectedId == R.id.Third_item) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
            intent = new Intent(this, DescoveryActivity.class);
            startActivity(intent);
        }
        if (mSelectedId == R.id.five_item) {
            mDrawerLayout.closeDrawer(GravityCompat.START);

        }
        if (mSelectedId == R.id.seven_item) {
            mDrawerLayout.closeDrawer(GravityCompat.START);

        }
        if (mSelectedId == R.id.acht_item) {
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
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        menuItem.setChecked(true);
        mSelectedId = menuItem.getItemId();
        navigate(mSelectedId);
        return true;
    }
}
