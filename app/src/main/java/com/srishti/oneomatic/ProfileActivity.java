package com.srishti.oneomatic;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.srishti.oneomatic.Authentication.LoginActivity;
import com.srishti.oneomatic.Database.SessionManager;

import java.util.HashMap;

public class ProfileActivity extends AppCompatActivity {
    TextView t1;
    TextInputLayout t2,t3,t4,t5;
    Toolbar toolbar;
    ImageButton home,logout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        SessionManager sessionManager=new SessionManager(this);
        HashMap<String,String>userDetails=sessionManager.getUserDetailFromSession();

        String fullName =userDetails.get(SessionManager.KEY_NAME);
        String email=userDetails.get(SessionManager.KEY_EMAIL);
        String phone=userDetails.get(SessionManager.KEY_PHONE);
        String password=userDetails.get(SessionManager.KEY_PASSWORD);

        t1=(TextView)findViewById(R.id.t1);
        t2=(TextInputLayout) findViewById(R.id.t2);
        t3=(TextInputLayout) findViewById(R.id.t3);
        t4=(TextInputLayout) findViewById(R.id.t4);
        t5=(TextInputLayout) findViewById(R.id.t5);
        toolbar=(Toolbar)findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Profile");

        t1.setText(fullName);

        t2.getEditText().setText(fullName);
        t3.getEditText().setText(email);
        t4.getEditText().setText(phone);
        t5.getEditText().setText(password);

        home=(ImageButton)findViewById(R.id.h);
        logout=(ImageButton)findViewById(R.id.l);



        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),Dashboard.class));
                finish();
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder=new AlertDialog.Builder(ProfileActivity.this);
                builder.setTitle("Exit");
                builder.setMessage("Do you want to Logout?").setCancelable(false)
                        .setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {
                                SessionManager sessionManager=new SessionManager(ProfileActivity.this);
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

    }
    @Override
    public void onBackPressed() {

    }



}
