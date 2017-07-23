package org.hello.sdi_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import Layout.org.layoutlib.MQttActivityLibrary;


public class MattActivity extends MQttActivityLibrary{



/**
 * Created by user on 01.05.2017.
 */


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
     //   setContentView(R.layout.mqttlaout);


    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        Intent intent = null;
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == Layout.org.layoutlib.R.id.Database) {
            return true;
        }

        if (id == Layout.org.layoutlib.R.id.navigation_item_11) {

            intent = new Intent(this, MpchartActivity.class);
            startActivity(intent);
            return true;
        }

        if (id == Layout.org.layoutlib.R.id.navigation_item_21) {
            // intent = new Intent(this, SecondActivity.class);

            //startActivity(intent);

            return true;
        }

        if (id == Layout.org.layoutlib.R.id.navigation_item_31) {


            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}