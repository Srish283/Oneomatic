package com.srishti.oneomatic.Authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ContextThemeWrapper;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hbb20.CountryCodePicker;
import com.srishti.oneomatic.Database.CheckInternet;
import com.srishti.oneomatic.R;

public class ForgetPassword extends AppCompatActivity {

    private  ImageButton back;
    private TextView logotext,regPhone;
    private ImageView logo;
    private Button next;
    private TextInputLayout phoneNumber;
    private CountryCodePicker countryCodePicker;
    ProgressBar progressBar;
    private Animation animation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_forget_password);

        logotext=findViewById(R.id.logotext1);
        regPhone=findViewById(R.id.regPhone);
        next=findViewById(R.id.next);
        phoneNumber=findViewById(R.id.etPhoneNumber2);
        countryCodePicker=findViewById(R.id.countryCodeHolder);
        back=findViewById(R.id.back);
        progressBar=findViewById(R.id.progressBar);

        animation= AnimationUtils.loadAnimation(this,R.anim.slide_anim);
        logotext.setAnimation(animation);
        regPhone.setAnimation(animation);
        phoneNumber.setAnimation(animation);
        countryCodePicker.setAnimation(animation);
        next.setAnimation(animation);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        });
    }
    public  void  verifyPhoneNumber(View view){
        //Check Internet
        CheckInternet checkInternet=new CheckInternet();
        if(!checkInternet.isOnline(this)){
            showCustommDialog();
        }

        if(!validatePhone()){ //validate phone num and password
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        //get data
        String phoneNum=phoneNumber.getEditText().getText().toString().trim();

        if(phoneNum.charAt(0)=='0'){
            phoneNum=phoneNum.substring(1);
        }
        //get complete phone num as
        final  String completePhone="+"+countryCodePicker.getFullNumber()+phoneNum;

        //Database Query
        Query checkUser= FirebaseDatabase.getInstance().getReference("users").orderByChild("phone").equalTo(phoneNum);

        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    phoneNumber.setError(null);
                    phoneNumber.setErrorEnabled(false);


                    Intent intent = new Intent(getApplicationContext(), VerificationActivity.class);
                    intent.putExtra("phone",completePhone);
                    intent.putExtra("whatToDo","updateData");
                    startActivity(intent);

                    progressBar.setVisibility(View.GONE);
                    finish();

                } else {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), "No such User Exists", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(ForgetPassword.this,error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });




    }

    private void showCustommDialog(){
        AlertDialog.Builder builder=new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.myDialog));
        builder.setTitle("Alert !");
        builder.setMessage("Please Connect to Internet to Proceed").setCancelable(false)
                .setPositiveButton("Connect", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                        dialog.cancel();
                    }
                });
        // Create the Alert dialog
        AlertDialog alertDialog = builder.create();

        // Show the Alert Dialog box
        alertDialog.show();
    }

    private  boolean validatePhone() {
        String phoneNum = phoneNumber.getEditText().getText().toString().trim();


        if (phoneNum.isEmpty()) {
            phoneNumber.setError("Phone Number Field cannot be Empty");
            phoneNumber.requestFocus();
            return false;
        } else {
            phoneNumber.setError(null);
            phoneNumber.setErrorEnabled(false);
            return true;
        }
    }
}