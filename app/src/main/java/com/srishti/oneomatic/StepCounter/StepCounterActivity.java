package com.srishti.oneomatic.StepCounter;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.os.Bundle;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.srishti.oneomatic.Authentication.LoginActivity;
import com.srishti.oneomatic.Dashboard;
import com.srishti.oneomatic.Database.SessionManager;
import com.srishti.oneomatic.R;

import pl.droidsonroids.gif.GifImageView;

public class StepCounterActivity extends AppCompatActivity implements SensorEventListener, StepListener {
    private TextView trck;
    private StepDetector simpleStepDetector;
    private SensorManager sensorManager;
    private Toolbar toolbar;
    private Sensor accel;
    private  String steps;
    private static final String TEXT_NUM_STEPS = "Number of Steps: ";
    private int numSteps;
    private ImageButton i1,i2;
    private TextView TvSteps;
    private Button BtnStart;
    private Button BtnStop;
    private Button backs;
    private Animation animation;
    private  GifImageView man;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_counter);


        animation= AnimationUtils.loadAnimation(this,R.anim.slide_anim_2);


        // Get an instance of the SensorManager
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accel = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        simpleStepDetector = new StepDetector();
        simpleStepDetector.registerListener(this);
        i1=(ImageButton)findViewById(R.id.homep);
        i2=(ImageButton)findViewById(R.id.logoutp);
        trck=(TextView)findViewById(R.id.trck);
        TvSteps =(TextView)findViewById(R.id.tv_steps);
        BtnStart = (Button) findViewById(R.id.btn_start);
        BtnStop = (Button) findViewById(R.id.btn_stop);
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        backs=(Button) findViewById(R.id.back);
        man=(GifImageView)findViewById(R.id.man);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Pedometer");

        toolbar.setAnimation(animation);
        trck.setAnimation(animation);
        TvSteps.setAnimation(animation);
        BtnStop.setAnimation(animation);
        backs.setAnimation(animation);
        BtnStart.setAnimation(animation);
        i1.setAnimation(animation);
        i2.setAnimation(animation);
        man.setAnimation(animation);

        i1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(getApplicationContext(), Dashboard.class);
                startActivity(i);
                setContentView(R.layout.activity_dashboard);
                finish();

            }
        });

        i2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                        AlertDialog.Builder builder=new AlertDialog.Builder(StepCounterActivity.this);
                        builder.setTitle("Exit");
                        builder.setMessage("Do you want to Logout?").setCancelable(false)
                                .setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int i) {
                                        SessionManager sessionManager=new SessionManager(StepCounterActivity.this);
                                        sessionManager.logoutUser();
                                        Intent in=new Intent(getApplicationContext(), LoginActivity.class);
                                        startActivity(in);
                                        finish();
                                        Toast.makeText(getApplicationContext(),"Logged Out",Toast.LENGTH_SHORT).show();

                                    }
                                })
                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int i) {
                                        dialog.cancel();
                                    }
                                });
                        // Create the Alert dialog
                        AlertDialog alertDialog = builder.create();
                        // Show the Alert Dialog box
                        alertDialog.show();


                    }
                });



        BtnStart.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {


                sensorManager.registerListener(StepCounterActivity.this, accel, SensorManager.SENSOR_DELAY_FASTEST);

            }
        });


        BtnStop.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                sensorManager.unregisterListener(StepCounterActivity.this);

            }
        });
        backs.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                numSteps = 0;
                TvSteps.setText("");
            }
        });



    }



    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            simpleStepDetector.updateAccel(
                    event.timestamp, event.values[0], event.values[1], event.values[2]);
        }
    }

    @Override
    public void step(long timeNs) {
        numSteps++;

        TvSteps.setText(TEXT_NUM_STEPS+String.valueOf(numSteps));
    }

}