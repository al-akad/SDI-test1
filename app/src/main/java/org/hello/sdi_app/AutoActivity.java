package org.hello.sdi_app;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;

import org.json.JSONException;

import java.util.Random;

import Layout.org.layoutlib.Analogdisplay;
import Layout.org.layoutlib.AutoActivityLib;

/**
 * Created by ahmed on 08.05.2017.
 */

public class AutoActivity extends AutoActivityLib  {


    CountDownTimer timer;

    NumberPicker numPicker;
    int newVall;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.autolayout);


        final Analogdisplay analogdisplay = (Analogdisplay) findViewById(R.id.analogdisplay);
        final Button btnStart = (Button) findViewById(R.id.button3);
        numPicker = (NumberPicker)findViewById(R.id.DrehZahl);



     //   String[] strings = new  String[1000];
     //   for(int i = 0; i<strings.length; i++)
     //       strings[i] = Integer.toString(i);

        numPicker.setMaxValue(1000);
        numPicker.setMinValue(0);
        numPicker.setWrapSelectorWheel(true);
     //   numPicker.setValue(100);
   //     numPicker.setDisplayedValues(strings);


        numPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {

            @Override
            public void onValueChange(NumberPicker picker1, int oldVal1, int newVal1) {


                try {
                    AutoSendData(newVal1,0,0,1);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });

        analogdisplay.setShowRangeValues(true);
        analogdisplay.setTargetValue(0);
        final Random random = new Random();

        timer = new CountDownTimer(10000, 2) {
            @Override
            public void onTick(long millisUntilFinished) {
                analogdisplay.setTargetValue(random.nextInt(5000));
            }

            @Override
            public void onFinish() {
                analogdisplay.setTargetValue(0);
            }
        };
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timer.start();
            }
        });














    }



        }
