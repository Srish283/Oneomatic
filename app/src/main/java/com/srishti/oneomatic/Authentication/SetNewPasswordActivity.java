package com.srishti.oneomatic.Authentication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.srishti.oneomatic.Authentication.LoginActivity;
import com.srishti.oneomatic.Database.CheckInternet;
import com.srishti.oneomatic.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SetNewPasswordActivity extends AppCompatActivity {

    Button update;
    ProgressBar progressBar;
    TextInputLayout newP,confirmP;
    TextView credencode;
    ImageView logo;
    private Animation animation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_set_new_password);
        update=findViewById(R.id.update);
        progressBar=findViewById(R.id.progressBar);
        newP=findViewById(R.id.etPasswordN);
        confirmP=findViewById(R.id.etPasswordC);
        logo=findViewById(R.id.logo);
        credencode=findViewById(R.id.credencode);

        animation= AnimationUtils.loadAnimation(this,R.anim.slide_anim);
        update.setAnimation(animation);
        newP.setAnimation(animation);
        confirmP.setAnimation(animation);
        logo.setAnimation(animation);
        credencode.setAnimation(animation);




    }
    public void newPasswordOnUpdate(View view){
        CheckInternet checkInternet=new CheckInternet();
        if(!checkInternet.isOnline(this)){
            showCustommDialog();
            return;
        }

        if(!validePassword()|!validConfirm()){
            return;
        }
        progressBar.setVisibility(View.VISIBLE);

        String _newP=newP.getEditText().getText().toString();
        String _phone=getIntent().getStringExtra("phone");
        String phn=_phone.substring(3);

        //UPDATE DATABASE IN SESSION
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("users");
        reference.child(phn).child("passw").setValue(_newP);

        Toast.makeText(getApplicationContext(),"Password Set Successfully",Toast.LENGTH_SHORT).show();
        Intent intent=new Intent(getApplicationContext(), LoginActivity.class);
        Pair[] pairs=new  Pair[5];
        pairs[0]=new Pair<View,String>(credencode,"logo_text");
        pairs[1]=new Pair<View,String>(logo,"logo_image");
        pairs[2]=new Pair<View,String>(newP,"phone_num");
        pairs[3]=new Pair<View,String>(confirmP,"password");
        pairs[4]=new Pair<View,String>(update,"login_sign");




        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptions options=ActivityOptions.makeSceneTransitionAnimation(SetNewPasswordActivity.this,pairs);
            startActivity(intent,options.toBundle());
        }

        finish();
    }

    private boolean validConfirm() {

        String val=confirmP.getEditText().getText().toString();
        String val1=newP.getEditText().getText().toString();


        if(val.isEmpty()){
            confirmP.setError("Password Field cannot be Empty");
            return false;
        }else if(!val.equals(val1)){
            confirmP.setError("Password does not match");
            return  false;
        }
        else {
            confirmP.setError(null);
            confirmP.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validePassword() {
        Pattern pattern;
        Matcher matcher;
        String val=newP.getEditText().getText().toString();
        String Password_Pattern="^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[#$%^*+_])(?=\\S+$).{8,}$";
        pattern= Pattern.compile(Password_Pattern);
        matcher=pattern.matcher(val);

        if(val.isEmpty()){
            newP.setError("Password Field cannot be Empty");
            return false;
        }else  if(!matcher.matches()){
            newP.setError("Password is weak");
            return false;
        }
        else {
            newP.setError(null);
            newP.setErrorEnabled(false);
            return true;
        }
    }

    private void showCustommDialog(){
        AlertDialog.Builder builder=new AlertDialog.Builder(getApplicationContext());
        builder.setMessage("Please Connect to Internet to Proceed").setCancelable(false)
                .setPositiveButton("Connect", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(getApplicationContext(),"Please Connect to Internet to Update Password",Toast.LENGTH_LONG).show();
                        dialogInterface.cancel();
                    }
                });
    }
}