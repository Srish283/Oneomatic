package com.srishti.oneomatic.Oxymeter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.srishti.oneomatic.Oxymeter.OxygenProcess;
import com.srishti.oneomatic.R;

public class OxymeterActivity extends AppCompatActivity {

    Toolbar toolbar;
    Animation animation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oxymeter);
        toolbar=(Toolbar)findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Oxymeter");


        ImageView img=findViewById(R.id.im);
        TextView txt=findViewById(R.id.desc);
        Button actionWrk = (Button)findViewById(R.id.Start);

        animation= AnimationUtils.loadAnimation(this,R.anim.slide_anim);
        toolbar.setAnimation(animation);
        img.setAnimation(animation);
        actionWrk.setAnimation(animation);
        txt.setAnimation(animation);


        actionWrk.setOnClickListener(v-> {
            Intent i=new Intent(getApplicationContext(), OxygenProcess.class);
            startActivity(i);
            setContentView(R.layout.activity_oxygen_process);

        });

    }
}