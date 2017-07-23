package org.hello.sdi_app;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import descover.org.mylibrary.ActivityDiscovery;

/**
 * Created by user on 27.05.2017.
 */

public class DescoveryActivity extends ActivityDiscovery {


    static ListView list;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Discover
        btn_discover = (Button) findViewById(descover.org.mylibrary.R.id.btn_discover);
        btn_discover.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startDiscovering();
            }
        });

        // Options

        // Hosts list
        adapter = new HostsAdapter(ctxt);
        list = (ListView) findViewById(descover.org.mylibrary.R.id.output);
        list.setAdapter(adapter);
        list.setItemsCanFocus(false);
        list.setOnItemClickListener(this);
        list.setEmptyView(findViewById(descover.org.mylibrary.R.id.list_empty));

   //    <list.getId(); i++);
    }

    @Override
    protected void setButtons(boolean disable) {

    }

    @Override
    protected void cancelTasks() {

    }
}
