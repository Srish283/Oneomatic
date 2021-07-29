package com.srishti.oneomatic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.srishti.oneomatic.Authentication.LoginActivity;
import com.srishti.oneomatic.Database.SessionManager;
import com.srishti.oneomatic.MedicineReminder.MedominderActivity;
import com.srishti.oneomatic.Oxymeter.OxymeterActivity;
import com.srishti.oneomatic.StepCounter.StepCounterActivity;
import com.srishti.oneomatic.Translation.TranslationActivity;

public class Dashboard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public DrawerLayout drawerLayout;
    public Toolbar toolbar;
    public NavigationView navigationView;
    public ActionBarDrawerToggle actionBarDrawerToggle;
    public Button btns,btn2,btn3,btn4;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        toolbar=(Toolbar)findViewById(R.id.toolbar1);
        navigationView=(NavigationView)findViewById(R.id.nav_view);
        btns = (Button) findViewById(R.id.btn1);
        btn2=(Button)findViewById(R.id.btn2);
        btn3=(Button)findViewById(R.id.btn3);
        btn4=(Button)findViewById(R.id.btn4);




        //Go to Step Counter Activity
        btns.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getApplicationContext(), StepCounterActivity.class);
                startActivity(i);
                setContentView(R.layout.activity_step_counter);
                finish();
            }

        });


        //Go to Oxygen calculator Activity
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), OxymeterActivity.class);
                startActivity(i);
                setContentView(R.layout.activity_oxymeter);
                finish();
            }
        });





        //Go to Medicine Reminder Activity
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), MedominderActivity.class);
                startActivity(i);
                setContentView(R.layout.activity_medominder);
                finish();
            }
        });

        //Go to Translation Activity
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getApplicationContext(), TranslationActivity.class);
                startActivity(i);
                setContentView(R.layout.activity_translation);
                finish();
            }

        });

        navigationView.bringToFront();
        // drawer layout instance to toggle the menu icon to open
        // drawer and back button to close drawer
        drawerLayout = findViewById(R.id.my_drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);

        // pass the Open and Close toggle for the drawer layout listener
        // to toggle the button
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        // to make the Navigation drawer icon always appear on the action bar

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Oneomatic Dashboard");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        navigationView.setNavigationItemSelectedListener(this);
    }

    // override the onOptionsItemSelected()
    // function to implement
    // the item click listener callback
    // to open and close the navigation
    // drawer when the icon is clicked
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch (menuItem.getItemId()){
            case R.id.nav_home:
                break;
            case R.id.nav_account:
                Intent intent=new Intent(getApplicationContext(), ProfileActivity.class);
                startActivity(intent);
                finish();
                break;
            case  R.id.nav_about:
                Intent i=new Intent(getApplicationContext(), AboutActivity.class);
                startActivity(i);
                finish();
                break;

            case R.id.nav_logout:
                AlertDialog.Builder builder=new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.myDialog));
                builder.setTitle("Exit");
                builder.setMessage("Do you want to Logout?").setCancelable(false)
                        .setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {
                                SessionManager sessionManager=new SessionManager(Dashboard.this);
                                sessionManager.logoutUser();
                                Intent in=new Intent(getApplicationContext(), LoginActivity.class);
                                startActivity(in);
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
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}