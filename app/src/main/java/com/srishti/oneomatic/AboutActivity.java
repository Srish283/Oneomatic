package com.srishti.oneomatic;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.srishti.oneomatic.Authentication.LoginActivity;
import com.srishti.oneomatic.Database.SessionManager;

public class AboutActivity extends AppCompatActivity {

    Toolbar toolbar;
    ImageButton home,logout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("About App");
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
                AlertDialog.Builder builder=new AlertDialog.Builder(AboutActivity.this);
                builder.setTitle("Exit");
                builder.setMessage("Do you want to Logout?").setCancelable(false)
                        .setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {
                                SessionManager sessionManager=new SessionManager(AboutActivity.this);
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

    }
    @Override
    public void onBackPressed() {

    }
}