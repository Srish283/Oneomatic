package com.srishti.oneomatic.Authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

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

import com.chaos.view.PinView;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.srishti.oneomatic.Database.CheckInternet;
import com.srishti.oneomatic.R;

import java.util.concurrent.TimeUnit;

public class VerificationActivity extends AppCompatActivity {

    String phoneN,codeBySystem,whatToDo,code;
    PinView pinView;
    Button verify;
    ImageButton close;
    ImageView logo;
    TextView vercode,otp;
    ProgressBar progressBar;
    private FirebaseAuth mAuth;
    private Animation animation;

    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_verification);

        pinView=findViewById(R.id.pin);
        verify=findViewById(R.id.verify);
        close=findViewById(R.id.close);
        phoneN=getIntent().getStringExtra("phone");
        whatToDo=getIntent().getStringExtra("whatToDo");
        progressBar=findViewById(R.id.progressBar);
        vercode=findViewById(R.id.vercode);
        logo=findViewById(R.id.logo);
        otp=findViewById(R.id.otp);
        
        sendVerificationCode(phoneN);
        mAuth = FirebaseAuth.getInstance();

        animation= AnimationUtils.loadAnimation(this,R.anim.slide_anim);
        pinView.setAnimation(animation);
        verify.setAnimation(animation);
        close.setAnimation(animation);
        logo.setAnimation(animation);
        vercode.setAnimation(animation);
        otp.setAnimation(animation);




        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pin=pinView.getText().toString();
                try {
                    if (!pin.isEmpty()) {
                        //erifyCode(code);
                        updateUserData();
                        Toast.makeText(getApplicationContext(),"VERIFICATION SUCCESS",Toast.LENGTH_SHORT).show();

                    }
                }catch (Exception e){
                    Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_SHORT).show();
                }

            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });

    }
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential){

        mAuth.signInWithCredential(credential).addOnCompleteListener(this,task -> {
            if(task.isSuccessful()){
                if(whatToDo.equals("updateData")){
                    updateUserData();
                }
            }else {
                if(task.getException() instanceof FirebaseAuthInvalidCredentialsException){
                    Toast.makeText(getApplicationContext(),"Could Not complete Verification",Toast.LENGTH_SHORT).show();
                }
            }
        });
        progressBar.setVisibility(View.GONE);
    }

    private void updateUserData() {
        Intent intent=new Intent(getApplicationContext(), SetNewPasswordActivity.class);
        intent.putExtra("phone",phoneN);
        startActivity(intent);
        finish();
    }


    private void sendVerificationCode(String phoneN) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneN,
                60,
                TimeUnit.SECONDS,
                VerificationActivity.this,mCallbacks
        );
        progressBar.setVisibility(View.VISIBLE);
    }
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks=
            new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                @Override
                public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                    String code = phoneAuthCredential.getSmsCode();

                    //sometime the code is not detected automatically
                    //in this case the code will be null
                    //so user has to manually enter the code
                    if (code != null) {
                        pinView.setText(code);
                        verifyCode(code);
                    }

                }

                @Override
                public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                    super.onCodeSent(s, forceResendingToken);
                    codeBySystem=s;
                }


                @Override
                public void onVerificationFailed(@NonNull FirebaseException e) {
                    Toast.makeText(VerificationActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            };

    private void verifyCode(String code) {
        CheckInternet checkInternet=new CheckInternet();
        if(!checkInternet.isOnline(this)){
            showCustommDialog();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        PhoneAuthCredential credential=PhoneAuthProvider.getCredential(codeBySystem,code);
        signInWithPhoneAuthCredential(credential);
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
                        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                    }
                });
    }

}