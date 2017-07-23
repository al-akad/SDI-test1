package Layout.org.layoutlib;

import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;

import java.util.ArrayList;

import Display.ang.AnalogDisplay.Analoglib.AnalogDisplay;

/**
 * Created by user on 25.05.2017.
 */

public class MQttActivityLibrary extends AppCompatActivity {



    protected Toolbar mToolbar;
    public TabLayout mTabLayout;
    public ViewPager mPager;
    private MyPagerAdapter mAdapter;

    TextView tv;
    Button submitButton;
    protected SeekBar customSeekBar;
    static String MQttHOST = "tcp://192.168.178.36:1883";
    static String topicStr = "test/";
    MqttAndroidClient client;
    MqttConnectOptions options;
    String Can_Adr;
    //TextView SubText, txtv1, txtv2, txtv3, txtv4, txtv5;
    Vibrator vibrator;
    Ringtone myRingtone;

    int UserParam, Run, RsRecale, Enable, newVal1, newVal2, progressChangedValue;
    static int MachRpm;
    static int MachCurrent;
    static int MachVoltage;
    static int MachTemperature;
    CountDownTimer timer;
    ListView lst;
    public NumberPicker numberPicker1;
    protected NumberPicker numberPicker2;
    DBConnections db;
    JSONParser parser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mqttlayoutlib);
        mAdapter = new MyPagerAdapter(getSupportFragmentManager());
        mToolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(mToolbar);
        mTabLayout = (TabLayout) findViewById(R.id.tab_layout);

        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(mAdapter);
        mTabLayout.setTabsFromPagerAdapter(mAdapter);
        db = new DBConnections(this);
        //       final DatabaseHelper db = new DatabaseHelper(this);

        mTabLayout.setupWithViewPager(mPager);
        mPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));


        numberPicker1 = (NumberPicker) findViewById(R.id.numberPicker1);
        numberPicker1.setMaxValue(50);
        numberPicker1.setMinValue(0);
        numberPicker1.setWrapSelectorWheel(true);
        numberPicker2 = (NumberPicker) findViewById(R.id.numberPicker2);
        numberPicker2.setMaxValue(10);
        numberPicker2.setMinValue(0);
        numberPicker2.setWrapSelectorWheel(true);
        parser = new JSONParser();


        //  lst = (ListView) findViewById(R.id.listView);

        //SubText = (TextView) findViewById(R.id.subText);
        // txtv1 = (TextView) findViewById(R.id.textView1);
        //txtv2 = (TextView) findViewById(R.id.textView2);
        // txtv3 = (TextView) findViewById(R.id.textView3);
        // txtv4 = (TextView) findViewById(R.id.textView4);
        //  txtv5 = (TextView) findViewById(R.id.textView5);




        Switch SwUseParam = (Switch) findViewById(R.id.switch15);
        Switch SwRun =(Switch)findViewById(R.id.switch13);
        Switch SwRsRecale =(Switch)findViewById(R.id.switch14);
        Switch SwEnable =(Switch)findViewById(R.id.switch19);

        SwUseParam.setChecked(false);
        SwUseParam.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {

                if(isChecked){
                    Toast.makeText(MQttActivityLibrary.this, "Switch User_Param is ON", Toast.LENGTH_LONG).show();
                    UserParam = 1;
                    SendData(UserParam, Run, RsRecale, Enable, newVal1, newVal2, progressChangedValue);
                    SendDatajson();


                }else{
                    Toast.makeText(MQttActivityLibrary.this, "Switch User_Param is OFF", Toast.LENGTH_LONG).show();
                    UserParam = 0;
                    SendData(UserParam, Run, RsRecale, Enable, newVal1, newVal2, progressChangedValue);
                    SendDatajson();
                }

            }
        });


        SwRun.setChecked(false);
        SwRun.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {

                if(isChecked){
                    Toast.makeText(MQttActivityLibrary.this, "Switch Run is ON", Toast.LENGTH_LONG).show();
                    Run = 1;
                    SendData(UserParam, Run, RsRecale, Enable, newVal1, newVal2, progressChangedValue);




                }else{
                    Toast.makeText(MQttActivityLibrary.this, "Switch Run is OFF", Toast.LENGTH_LONG).show();
                    Run = 0;
                    SendData(UserParam, Run, RsRecale, Enable, newVal1, newVal2, progressChangedValue);

                }

            }
        });


        SwRsRecale.setChecked(false);
        SwRsRecale.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {

                if(isChecked){
                    Toast.makeText(MQttActivityLibrary.this, "Switch  Rs_Recale is ON", Toast.LENGTH_LONG).show();
                    RsRecale = 1;
                    SendData(UserParam, Run, RsRecale, Enable, newVal1, newVal2, progressChangedValue);



                }else{
                    Toast.makeText(MQttActivityLibrary.this, "Switch Rs_Recale is OFF", Toast.LENGTH_LONG).show();
                    RsRecale = 0;
                    SendData(UserParam, Run, RsRecale, Enable, newVal1, newVal2, progressChangedValue);
                }

            }
        });



        SwEnable.setChecked(false);
        SwEnable.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {

                if(isChecked){
                    Toast.makeText(MQttActivityLibrary.this, "Switch  Enable is ON", Toast.LENGTH_LONG).show();
                    Enable = 1;
                    SendData(UserParam, Run, RsRecale, Enable, newVal1, newVal2, progressChangedValue);



                }else{
                    Toast.makeText(MQttActivityLibrary.this, "Switch Enable is OFF", Toast.LENGTH_LONG).show();
                    Enable = 0;
                    SendData(UserParam, Run, RsRecale, Enable, newVal1, newVal2, progressChangedValue);
                }

            }
        });




        numberPicker1.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {

            @Override
            public void onValueChange(NumberPicker picker1, int oldVal1, int newVal1) {



                SendData(UserParam, Run, RsRecale, Enable, newVal1, newVal2, progressChangedValue);


            }
        });

        numberPicker2.setOnValueChangedListener(new NumberPicker.OnValueChangeListener(){

            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {

                newVal = newVal2;

                SendData(UserParam, Run, RsRecale, Enable, newVal1, newVal2, progressChangedValue);

            }


        });




        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        myRingtone = RingtoneManager.getRingtone(getApplicationContext(), uri);

        String clientId = MqttClient.generateClientId();
        client = new MqttAndroidClient(this.getApplicationContext(), MQttHOST, clientId);
        options = new MqttConnectOptions();


        try {
            IMqttToken token = client.connect(options);
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Toast.makeText(MQttActivityLibrary.this, "connected", Toast.LENGTH_LONG).show();
                    setSubscription();

                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Toast.makeText(MQttActivityLibrary.this, "not connected", Toast.LENGTH_LONG).show();

                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }


        client.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {

            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {


                String JsonF = new String(message.getPayload());


                //SubText.setText(JsonF);
                //              Gson gson = new Gson();
                //             Apps apps = gson.fromJson(JsonF,Apps.class);
                //             Toast.makeText(MQttActivityLibrary.this, apps.dEBUGAPP.name, Toast.LENGTH_LONG).show();

                try {

                    Object obj = parser.parse(JsonF);

                    JSONObject obj1 = (JSONObject)obj;

                    System.out.println("The 2nd element of array");
                    JSONObject obj2 =(JSONObject)obj1.get("apps");


                    JSONObject obj3 = (JSONObject)obj2.get("CAN_APP_DECODER");
                    JSONObject obj4 = (JSONObject)obj3.get("params");

                    JSONObject Can_Adr = (JSONObject)obj4.get("Can_Adr");
                    int rpmValue = Can_Adr.getInt("value");
                    MachRpm = rpmValue;

                    JSONObject Can_Length = (JSONObject)obj4.get("Can_Length");
                    int currentValue = Can_Adr.getInt("value");
                    MachCurrent = currentValue;
                    JSONObject Can_Offset = (JSONObject)obj4.get("Can_Offset");
                    int voltageValue = Can_Adr.getInt("value");
                    MachVoltage= voltageValue;

                    JSONObject Can_Write_Adr = (JSONObject)obj4.get("Can_Write_Adr");
                    int temperatureValue = Can_Adr.getInt("value");
                    MachTemperature= temperatureValue;

                    JSONObject Can_Write_Length = (JSONObject)obj4.get("Can_Write_Length");
                    JSONObject Can_Write_Offset = (JSONObject)obj4.get("an_Write_Offset");
                    JSONObject Can_Write_Value = (JSONObject)obj4.get("Can_Write_Value");



                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (org.json.simple.parser.ParseException e) {
                    e.printStackTrace();
                }


                try {
                    JSONObject json = new JSONObject(JsonF);
                    JSONArray jArr = json.getJSONArray("Machine");
                    for (int i = 0; i < jArr.length(); i++) {

                        JSONObject obj = jArr.getJSONObject(i);
                        String MachNumber = obj.getString("Number");
                        //     txtv3.setText(MachNumber);
                        MachRpm = obj.getInt("rpm");
                        //    txtv4.setText(MachRpm);
                        MachCurrent = obj.getInt("current");
                        //     txtv5.setText(MachCurrent);
                        MachVoltage = obj.getInt("voltage");
                        //     txtv1.setText(MachVoltage);
                        MachTemperature = obj.getInt("temperature");
                        //     txtv2.setText(MachTemperature);
                        timer.start();


                    }

                    // {
                    //   "Machine": [
                    // {
                    //   "Number": "1",
                    //     "rpm": "10",
                    //   "current": "10",
                    // "voltage": "230",
                    // "temperature": "20"
                    // },
                    // {
                    //   "Number": "2",
                    //     "rpm": "11",
                    //   "current": "100",
                    // "voltage": "230",
                    // "temperature": "25"
                    // }
                    // ],
                    //                    "Machine1": {
                    //                  "Number": "1",
                    //                        "rpm": "10",
                    //                      "current": "3"
                    //        },
                    //          "surname": "xxx",
                    //            "name": "xxxx"
                    //  }

                } catch (Exception ex) {
                }
                vibrator.vibrate(500);
                myRingtone.play();


                Boolean result = db.insertData(MachRpm, MachCurrent, MachVoltage, MachTemperature );

                if (result == true){

                    Toast.makeText(MQttActivityLibrary.this, "Date is insertied", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(MQttActivityLibrary.this, "Data is not insertied", Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });

        final AnalogDisplay analogDisplay = (AnalogDisplay) findViewById(R.id.analogDisp);
        final AnalogDisplay analogDisplay1 = (AnalogDisplay) findViewById(R.id.analogDisp1);
        final AnalogDisplay analogDisplay2 = (AnalogDisplay) findViewById(R.id.analogDisp2);
        final AnalogDisplay analogDisplay3 = (AnalogDisplay) findViewById(R.id.gauge_view3);

        analogDisplay.setShowRangeValues(true);
        analogDisplay.setTargetValue(0);
        analogDisplay1.setShowRangeValues(true);
        analogDisplay1.setTargetValue(0);
        analogDisplay2.setShowRangeValues(true);
        analogDisplay2.setTargetValue(0);
        analogDisplay3.setShowRangeValues(true);
        analogDisplay3.setTargetValue(0);

        timer = new CountDownTimer(10000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                analogDisplay.setTargetValue(1000);
                analogDisplay1.setTargetValue(MachCurrent);
                analogDisplay2.setTargetValue(MachVoltage);
                analogDisplay3.setTargetValue(MachTemperature);
            }

            @Override
            public void onFinish() {
                analogDisplay.setTargetValue(0);
                analogDisplay1.setTargetValue(0);
                analogDisplay2.setTargetValue(0);
                analogDisplay3.setTargetValue(0);
            }
        };

        // initiate  views
        customSeekBar =(SeekBar)findViewById(R.id.customSeekBar);
        // perform seek bar change listener event used for getting the progress value
        customSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressChangedValue = 0;

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressChangedValue = progress;
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                Toast.makeText(MQttActivityLibrary.this, "Rpm is :" + progressChangedValue,
                        Toast.LENGTH_SHORT).show();
                // tv.setText(progressChangedValue);
                SendData(UserParam, Run, RsRecale, Enable, newVal1, newVal2, progressChangedValue);
            }
        });

    }







    public void SendData(int UserParam, int Run, int RsRecale, int Enable, int newVal1, int newVal2, int progressChangedValue){

        String topic = topicStr;
        try {

            JSONObject jsonObject1 = new JSONObject();
            JSONArray jsonArray = new JSONArray();

            JSONObject number1 = new JSONObject();
            number1.put("Number", progressChangedValue);
            number1.put("rpm", UserParam);
            number1.put("current", Run);
            number1.put("voltage", "230");
            number1.put("temperature", RsRecale);
            jsonArray.put(number1);


            JSONObject number2 = new JSONObject();
            number2.put("Number", "2");
            number2.put("rpm", Enable);
            number2.put("current", "120");
            number2.put("voltage", "230");
            number2.put("temperature", newVal1);
            jsonArray.put(number2);
            //jsonObject1.put("Run","1");
            jsonObject1.put("Machine", jsonArray);

            JSONObject Machine1 = new JSONObject();
            Machine1.put("Number", newVal2);
            Machine1.put("rpm", "10");
            Machine1.put("current", "3");
            jsonObject1.put("Machine1", Machine1);
            jsonObject1.put("surname", "xxx");
            jsonObject1.put("name", "xxxx");
            String message = jsonObject1.toString();

            client.publish(topic, message.getBytes(),0, false);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (MqttPersistenceException e) {
            e.printStackTrace();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }


    public  void SendDatajson(){
        String topic = topicStr;

        try {

            JSONObject jsonObject1 = new JSONObject();
            JSONObject jsonObject2 = new JSONObject();
            JSONObject jsonObject3 = new JSONObject();
            JSONObject jsonObject4 = new JSONObject();
            JSONObject jsonObject5 = new JSONObject();
            JSONObject jsonObject6 = new JSONObject();
            JSONObject jsonObject7 = new JSONObject();
            JSONObject jsonObject8 = new JSONObject();
            JSONObject jsonObject9 = new JSONObject();
            JSONObject jsonObject10 = new JSONObject();
            JSONObject jsonObject11 = new JSONObject();
            JSONObject jsonObject12 = new JSONObject();
            JSONObject jsonObject13 = new JSONObject();
            JSONObject jsonObject14 = new JSONObject();
            JSONObject jsonObject15 = new JSONObject();
            JSONObject jsonObject16 = new JSONObject();
            JSONObject jsonObject17 = new JSONObject();
            JSONObject jsonObject18 = new JSONObject();
            JSONObject jsonObject19 = new JSONObject();
            JSONObject jsonObject20 = new JSONObject();
            JSONObject jsonObject21 = new JSONObject();
            JSONObject jsonObject22 = new JSONObject();
            JSONObject jsonObject23 = new JSONObject();
            JSONObject jsonObject24 = new JSONObject();
            JSONObject jsonObject25 = new JSONObject();
            JSONObject jsonObject26 = new JSONObject();
            JSONObject jsonObject27 = new JSONObject();
            JSONObject jsonObject28 = new JSONObject();
            JSONObject jsonObject29 = new JSONObject();
            JSONObject jsonObject30 = new JSONObject();
            JSONObject jsonObject31 = new JSONObject();
            JSONObject jsonObject32 = new JSONObject();
            JSONObject jsonObject33 = new JSONObject();


            JSONArray jsonArray1 = new JSONArray();
            JSONArray jsonArray2 = new JSONArray();



            jsonObject1.put("name", "Can_Adr");
            jsonObject1.put("unit", "4");
            jsonObject1.put("value", "394");
            jsonObject1.put("type","int");



            jsonObject2.put("name", "Can_Length");
            jsonObject2.put("unit", "4");
            jsonObject2.put("value", "7");
            jsonObject2.put("type","int");


            jsonObject3.put("name", "Can_Offset");
            jsonObject3.put("unit", "4");
            jsonObject3.put("value", "0");
            jsonObject3.put("type","int");


            jsonObject4.put("name", "Can_Write_Adr");
            jsonObject4.put("unit", "4");
            jsonObject4.put("value", "394");
            jsonObject4.put("type","int");


            jsonObject5.put("name", "Can_Write_Length");
            jsonObject5.put("unit", "4");
            jsonObject5.put("value", "7");
            jsonObject5.put("type","int");


            jsonObject6.put("name", "Can_Write_Offset");
            jsonObject6.put("unit", "4");
            jsonObject6.put("value", "0");
            jsonObject6.put("type","int");

            jsonObject7.put("name", "Can_Write_Value");
            jsonObject7.put("unit", "4");
            jsonObject7.put("value", "7");
            jsonObject7.put("type","int");



            jsonObject8.put("Can_Adr", jsonObject1);
            jsonObject8.put("Can_Length", jsonObject2);
            jsonObject8.put("Can_Offset", jsonObject3);
            jsonObject8.put("Can_Write_Adr", jsonObject4);
            jsonObject8.put("Can_Write_Length", jsonObject5);
            jsonObject8.put("Can_Write_Offset", jsonObject6);
            jsonObject8.put("Can_Write_Value", jsonObject7);






            jsonObject16.put("name", "Mapper_Offset");
            jsonObject16.put("unit", "4");
            jsonObject16.put("value", "0");
            jsonObject16.put("type","int");


            jsonObject17.put("name", "Mapper_Size");
            jsonObject17.put("unit", "4");
            jsonObject17.put("value", "30");
            jsonObject17.put("type","int");



            jsonObject18.put("name", "Mapper_Type");
            jsonObject18.put("unit", "4");
            jsonObject18.put("value", "0");
            jsonObject18.put("type","int");




            jsonObject19.put("Mapper_Offset", jsonObject16);
            jsonObject20.put("Mapper_Size", jsonObject17);
            jsonObject21.put("Mapper_Type", jsonObject18);

            jsonObject22.put("Mapper_Offset",jsonObject16);
            jsonObject22.put("Mapper_Size",jsonObject17);
            jsonObject22.put("Mapper_Type",jsonObject18);




            jsonObject23.put("src_port_nr", "0");
            jsonObject23.put("dest_port_nr", "0");

            jsonObject24.put("src_port_nr", "1");
            jsonObject24.put("dest_port_nr","1");

            jsonArray1.put(jsonObject23);
            jsonArray1.put(jsonObject24);







            jsonObject25.put("name", "MAPPER_APP");
            jsonObject25.put("type","MAPPER_APP");
            jsonObject25.put("params", jsonObject22);








            jsonObject28.put("name", "DEBUG_APP");
            jsonObject28.put("type", "DEBUG_APP");
            jsonObject28.put("params",jsonObject27);





            jsonObject29.put("src","CAN_APP_DECODER");
            jsonObject29.put("dest","DEBUG_APP");
            jsonObject29.put("ports",jsonArray1);



            jsonObject30.put("src","DEBUG_APP");
            jsonObject30.put("dest","MAPPER_APP");
            jsonObject30.put("ports",jsonArray1);


            jsonObject31.put("name","CAN_APP_DECODER");
            jsonObject31.put("type","CAN_APP_DECODER");
            jsonObject31.put("params",jsonObject8);







            jsonObject32.put("CAN_APP_DECODER",jsonObject31);
            jsonObject32.put("DEBUG_APP",jsonObject28);
            jsonObject32.put("MAPPER_APP",jsonObject25);

            jsonArray2.put(jsonObject29);
            jsonArray2.put(jsonObject30);


            jsonObject33.put("apps", jsonObject32);
            jsonObject33.put("connections",jsonArray2);

            String message1 = jsonObject33.toString();

            client.publish(topic, message1.getBytes(),0, false);

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (MqttPersistenceException e) {
            e.printStackTrace();
        } catch (MqttException e) {
            e.printStackTrace();
        }

    }


//    public void SendDataToServer(){
//        String topic = topicStr;

//        try {

//            FileReader fr = new FileReader("JSONfile.txt");
    //           BufferedReader br = new BufferedReader(fr);
    //           String message2= br.readLine();

//            client.publish(topic, message2.getBytes(),0, false);

//        } catch (Exception e) {
    //          e.printStackTrace();
    //       }

//    }




    private void setSubscription() {
        try {
            client.subscribe(topicStr, 0);

        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_third, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        Intent intent = null;
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        if (id == R.id.Database) {

            showData();
            return true;
        }

        if (id == R.id.navigation_item_11) {

//           intent = new Intent(this, MQttActivityLibrary.class);
            //           startActivity(intent);
            return true;
        }

        if (id == R.id.navigation_item_21) {
            // intent = new Intent(this, SecondActivity.class);

            //startActivity(intent);

            return true;
        }

        if (id == R.id.navigation_item_31) {


            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void showData() {
        ArrayList<String> listData = db.getAllrecord();
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,listData);
        //     lst.setAdapter(arrayAdapter);
    }


    public static class MyFragment extends Fragment {

        public static final java.lang.String ARG_PAGE = "arg_page";


        public MyFragment() {

        }

        public static MyFragment newInstance(int pageNumber) {
            MyFragment myFragment = new MyFragment();
            Bundle arguments = new Bundle();
            arguments.putInt(ARG_PAGE, pageNumber + 1);
            myFragment.setArguments(arguments);
            return myFragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            Bundle arguments = getArguments();
            int pageNumber = arguments.getInt(ARG_PAGE);
            TextView myText = new TextView(getActivity());


            //myText.setText("Hello I am the text inside this Fragment " + pageNumber);
            //myText.setGravity(Gravity.CENTER);
            return myText;
        }

    }


    class MyPagerAdapter extends FragmentStatePagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            MyFragment myFragment = MyFragment.newInstance(position);
            return myFragment;
        }



        @Override
        public int getCount() {
            //descover.org.mylibrary.ActivityDiscovery.list.getId();
            //         return  ActivityMainLib.getMachineNumber;
            //        int DescMachine = ActivityDiscovery.list.getAdapter().getCount();
            //       int NumMachine = ActivityMainLib.NumMachine;
            //     if (NumMachine > 0) {
            //         return NumMachine;
            //        }  else
            //         if (DescMachine > 1){
            int anzahl  = 4;

            //                 return ActivityDiscovery.list.getAdapter().getCount();

            return anzahl;
            //           }
            //         else {
            //      return 1;
            //    }
        }



        @Override
        public CharSequence getPageTitle(int position) {
            return "M " + (position + 1);
        }
    }




}
