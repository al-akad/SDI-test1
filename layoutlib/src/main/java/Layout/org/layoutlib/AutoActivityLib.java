package Layout.org.layoutlib;

import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.NumberPicker;
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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by user on 25.05.2017.
 */

public class AutoActivityLib extends AppCompatActivity {
 static String MQttHOST = "tcp://192.168.0.108:1883";
 //   static String MQttHOST = "tcp://192.168.178.36:1883";
   // static String topicStr = "test/";
  static String topicStr = "cactusInput";
    MqttAndroidClient client;
    MqttConnectOptions options;
    protected TextView viewById;
    int left, Right, Front, Back;
    protected String strings;
    public ListView lst;
    public TextView txvv;
    protected Button btnn;
    public NumberPicker numberPick1;
    Vibrator vibrator;
    Ringtone myRingtone;

    DBConnections db = new DBConnections(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vibrator =(Vibrator)getSystemService(VIBRATOR_SERVICE);
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
                    Toast.makeText(AutoActivityLib.this, "connected", Toast.LENGTH_LONG ).show();
                    setSubscription();

                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Toast.makeText(AutoActivityLib.this, "not connected", Toast.LENGTH_LONG ).show();

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


                String JsonF= new String (message.getPayload());


                //SubText.setText(JsonF);


                try {
                    JSONObject json = new JSONObject(JsonF);
                    JSONArray jArr =json.getJSONArray("Machine");
                    for (int i=0; i < jArr.length(); i++) {

                        // JSONObject obj = jArr.getJSONObject(i);
                        // String MachNumber =obj.getString("Number");
                        // txtv3.setText(MachNumber);
                        // String MachRpm=obj.getString("rpm");
                        // txtv4.setText(MachRpm);
                        // String MachCurrent=obj.getString("current");
                        // txtv5.setText(MachCurrent);
                        //  String MachVoltage=obj.getString("voltage");
                        //  txtv1.setText(MachVoltage);
                        //  String MachTemperature=obj.getString("temperature");
                        //  txtv2.setText(MachTemperature);
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

                }
                catch (Exception ex){}
                vibrator.vibrate(500);
                myRingtone.play();

            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });







    };
    public void showData(){
        ArrayList<String> listData = db.getAllrecord();
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,listData);
        lst.setAdapter(arrayAdapter);
    }

    private void setSubscription(){
        try{
            client.subscribe(topicStr,0);

        }catch(MqttException e){
            e.printStackTrace();
        }
    }
    public void lifttcliek(View view) throws JSONException {

        AutoSendData(1,0,0,0);
    }

    public void Rightcliek(View view) throws JSONException{
        AutoSendData(0,1,0,0);
    }

    public void Frontclieck(View view) throws JSONException{
        AutoSendData(0,0,0,1);
    }

    public void Backclieck(View view) throws JSONException{
        AutoSendData(0,0,0,0);
    }


    public void AutoSendData(int left, int  right, int front, int back) throws JSONException {

        String topic = topicStr;

      // '[{"app":"simpleFikart","parameters":{"PARAM1":1}}]

    //    [{"app":"simpleFikart","parameters":{"PARAM2":1}}]
    //    [{"app":"simpleFikart","parameters":{"PI":1000}}]

        JSONArray jsonar = new JSONArray();

        JSONArray jsonarr1 = new JSONArray();
        JSONArray jsonarr2 = new JSONArray();


        JSONObject jsonob = new JSONObject();
        JSONObject jsonobbb2 = new JSONObject();
        JSONObject jsonobbb3 = new JSONObject();
        JSONObject jsonob1 = new JSONObject();
        JSONObject Jsonobb2 = new JSONObject();
        JSONObject Jsonobb3 = new JSONObject();

        jsonobbb2.put("PARAM2", back);
        jsonobbb3.put("PI",left);


         jsonob.put("PARAM1", back);
        jsonob1.put("app", "simpleFikart");
        jsonob1.put("parameters", jsonob);

        Jsonobb2.put("app", "simpleFikart");
        Jsonobb2.put("parameters", jsonobbb2);


        Jsonobb3.put("app", "simpleFikart");
        Jsonobb3.put("parameters", jsonobbb3);

        jsonar.put(jsonob1);
        jsonarr1.put(Jsonobb2);
        jsonarr2.put(Jsonobb3);




//        JSONObject jsonObject1 = new JSONObject();
//        JSONArray jsonArray = new JSONArray();

//        JSONObject number1 = new JSONObject();
//        number1.put("Number", "1");
//        number1.put("rpm", left);
 //       number1.put("current", right);
 //       number1.put("voltage", front);
  //      number1.put("temperature", back);
   //     jsonArray.put(number1);


 //       JSONObject number2 = new JSONObject();
 //       number2.put("Number", "2");
 //       number2.put("rpm", "1000");
 //       number2.put("current", "120");
 //       number2.put("voltage", "230");
  //      number2.put("temperature", "20");
  //      jsonArray.put(number2);
        //jsonObject1.put("Run","1");
  //      jsonObject1.put("Machine", jsonArray);

 //       JSONObject Machine1 = new JSONObject();
 //       Machine1.put("Number", "1");
  //      Machine1.put("rpm", "10");
  //      Machine1.put("current", "3");
  //      jsonObject1.put("Machine1", Machine1);
  //      jsonObject1.put("surname", "xxx");
  //      jsonObject1.put("name", "xxxx");
        String message = jsonar.toString();


        String message1 = jsonarr1.toString();
        String message2 = jsonarr2.toString();

 //       String message = jsonObject1.toString();

        try {

            client.publish(topic, message.getBytes(),0, false);
            client.publish(topic, message1.getBytes(),0, false);
            client.publish(topic, message2.getBytes(),0, false);
        } catch ( MqttException e) {
            e.printStackTrace();
        }

    }

}
