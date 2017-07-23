package Layout.org.layoutlib;

import android.content.Context;
import android.media.Ringtone;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;

/**
 * Created by user on 24.05.2017.
 */

public class AutoLayoutlib extends FrameLayout{

    private Button LeftClieck;
    private Button RightClieck;
    private Button FrontClieck;
    private Button BackClieck;
    private TextView Lenkrad;
    private TextView Lenkrad1;
    private EditText et;



    static String MQttHOST = "tcp://192.168.178.36:1883";
    static String topicStr = "test/";
    MqttAndroidClient client;
    MqttConnectOptions options;
    Vibrator vibrator;
    Ringtone myRingtone;

    LinearLayout ll = new LinearLayout(getContext());
    ScrollView sv = new ScrollView(getContext());
    TextView tv = new TextView(getContext());



    public AutoLayoutlib(Context context) {
        super(context);
        init();
    }

    public AutoLayoutlib(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AutoLayoutlib(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public AutoLayoutlib(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    void init() {

        inflate(getContext(), R.layout.autolayoutlib, this);
        this.LeftClieck = (Button) findViewById(R.id.button23);
        this.RightClieck = (Button) findViewById(R.id.button33);
        this.FrontClieck = (Button) findViewById(R.id.button55);
        this.BackClieck = (Button) findViewById(R.id.button44);



    }
}
