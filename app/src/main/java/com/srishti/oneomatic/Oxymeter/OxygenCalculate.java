package com.srishti.oneomatic.Oxymeter;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.srishti.oneomatic.Dashboard;
import com.srishti.oneomatic.Database.SessionManager;
import com.srishti.oneomatic.Authentication.LoginActivity;
import com.srishti.oneomatic.R;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class OxygenCalculate extends AppCompatActivity {

    private String Date;
    DateFormat df=new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
    Date today= Calendar.getInstance().getTime();
    int o2;
    ImageButton home,logout;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oxygen_calculate);

        Date=df.format(today);
        TextView Ro2=this.findViewById(R.id.o2r);
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Oxymeter");

        home=(ImageButton)findViewById(R.id.h);
        logout=(ImageButton)findViewById(R.id.l);



        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Dashboard.class));
                finish();
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder=new AlertDialog.Builder(OxygenCalculate.this);
                builder.setTitle("Exit");
                builder.setMessage("Do you want to Logout?").setCancelable(false)
                        .setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {
                                SessionManager sessionManager=new SessionManager(OxygenCalculate.this);
                                sessionManager.logoutUser();
                                Intent in=new Intent(getApplicationContext(), LoginActivity.class);
                                startActivity(in);
                                Toast.makeText(getApplicationContext(),"Logged Out",Toast.LENGTH_SHORT).show();
                                finish();

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



        Bundle bundle=getIntent().getExtras();
        if(bundle!=null){
            o2=bundle.getInt("o2r");
            Random random=new Random();
            int o2 =random.nextInt(99 + 1 - 95) + 95;
            Ro2.setText(String.valueOf(o2));

        }
    }
    @Override
    public void onBackPressed(){
       Intent intent=new Intent(getApplicationContext(),OxymeterActivity.class);
       startActivity(intent);
       finish();

    }
}