package com.srishti.oneomatic.Authentication;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.appcompat.widget.Toolbar;

import android.app.ActivityOptions;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
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
import com.srishti.oneomatic.Dashboard;
import com.srishti.oneomatic.Database.CheckInternet;
import com.srishti.oneomatic.Database.SessionManager;
import com.srishti.oneomatic.R;


public class LoginActivity extends AppCompatActivity {
    Button register,login,forgot_PASS,cancel;
    ImageView logo,logIcon;
    TextView logoText,pageText;
    TextInputLayout phone,pass;
    Toolbar toolbar;
    ProgressBar progressBar;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        register=findViewById(R.id.btnSign);
        logo=findViewById(R.id.logoImage);
        login=findViewById(R.id.btnLogin);
        forgot_PASS=findViewById(R.id.forgetP);
        phone=findViewById(R.id.etPhoneNumber1);
        pass=findViewById(R.id.etPassword1);
        logoText=findViewById(R.id.logotext);
        pageText=findViewById(R.id.pagetext);
        logIcon=findViewById(R.id.loginIcon);
        cancel=findViewById(R.id.btnCancel1);
        toolbar=findViewById(R.id.toolbar);
        progressBar=findViewById(R.id.progressBar);


        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.login_page_title);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(LoginActivity.this, RegisterActivity.class);
                Pair[] pairs=new  Pair[10];
                pairs[0]=new Pair<View,String>(toolbar,"tool_trans");
                pairs[1]=new Pair<View,String>(logo,"logo_image");
                pairs[2]=new Pair<View,String>(logoText,"logo_text");
                pairs[3]=new Pair<View,String>(pageText,"sign_in");
                pairs[4]=new Pair<View,String>(logIcon,"login_image");
                pairs[5]=new Pair<View,String>(login,"login_sign");
                pairs[6]=new Pair<View,String>(cancel,"cancel_trans");
                pairs[7]=new Pair<View,String>(register,"account");
                pairs[8]=new Pair<View,String>(phone,"phone_num");
                pairs[9]=new Pair<View,String>(pass,"password");



                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    ActivityOptions options=ActivityOptions.makeSceneTransitionAnimation(LoginActivity.this,pairs);
                    startActivity(intent,options.toBundle());
                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                phone.getEditText().setText("");
                pass.getEditText().setText("");
                //finish();
            }
        });

        forgot_PASS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(LoginActivity.this, ForgetPassword.class);
                startActivity(intent);
                finish();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckInternet checkInternet=new CheckInternet();
                if(!checkInternet.isOnline(getApplicationContext())){
                    showCustommDialog();
                }
                if(!validatePhone()|!validatePass()){ //validate phone num and password
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);
                //get data
                String phoneNum=phone.getEditText().getText().toString().trim();
                String _password= pass.getEditText().getText().toString();
                if(phoneNum.charAt(0)=='0'){
                    phoneNum=phoneNum.substring(1);
                }
                //get complete phone num as
                //String completePhone="+"+countryCodePick.getFullNumber()+phoneNum;

                //Database Query
                Query checkUser= FirebaseDatabase.getInstance().getReference("users").orderByChild("phone").equalTo(phoneNum);
                final String _phone=phoneNum;
                checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            phone.setError(null);
                            phone.setErrorEnabled(false);

                            String systemPassword=snapshot.child(_phone).child("passw").getValue(String.class);
                            if(systemPassword.equals(_password)){
                                pass.setErrorEnabled(false);
                                pass.setError(null);

                                String nameFromDb=snapshot.child(_phone).child("name").getValue(String.class);
                                String emailFromDb=snapshot.child(_phone).child("email").getValue(String.class);
                                String phoneFromDb=snapshot.child(_phone).child("phone").getValue(String.class);
                                String passFromDb=snapshot.child(_phone).child("passw").getValue(String.class);

                                //create a session
                                SessionManager sessionManager=new SessionManager(LoginActivity.this);
                                sessionManager.createLoginSession(nameFromDb,emailFromDb,phoneFromDb,passFromDb);

                                Intent intent=new Intent(getApplicationContext(), Dashboard.class);
                                Pair[] pairs=new  Pair[10];
                                pairs[0]=new Pair<View,String>(toolbar,"tool_trans");
                                pairs[1]=new Pair<View,String>(logo,"logo_image");
                                pairs[2]=new Pair<View,String>(logoText,"logo_text");
                                pairs[3]=new Pair<View,String>(pageText,"sign_in");
                                pairs[4]=new Pair<View,String>(logIcon,"login_image");
                                pairs[5]=new Pair<View,String>(login,"login_sign");
                                pairs[6]=new Pair<View,String>(cancel,"cancel_trans");
                                pairs[7]=new Pair<View,String>(register,"account");
                                pairs[8]=new Pair<View,String>(phone,"phone_num");
                                pairs[9]=new Pair<View,String>(pass,"password");



                                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                                    ActivityOptions options=ActivityOptions.makeSceneTransitionAnimation(LoginActivity.this,pairs);
                                    startActivity(intent,options.toBundle());
                                }
                                finish();
                                
                                progressBar.setVisibility(View.GONE);


                            }else {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(LoginActivity.this,"Password does not Match",Toast.LENGTH_SHORT).show();
                            }
                        }
                        else {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(LoginActivity.this,"No such User Exists",Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(LoginActivity.this,error.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private  boolean validatePhone() {
        String phoneNum = phone.getEditText().getText().toString().trim();


        if (phoneNum.isEmpty()) {
            phone.setError("Phone Number Field cannot be Empty");
            phone.requestFocus();
            return false;
        } else {
            phone.setError(null);
            phone.setErrorEnabled(false);
            return true;
        }
    }
    private boolean validatePass(){
        String _password = pass.getEditText().getText().toString();
        if(_password.isEmpty()){
                pass.setError("Password Number Field cannot be Empty");
                pass.requestFocus();
                return false;
            }
            else{

                pass.setError(null);
                pass.setErrorEnabled(false);

                return true;
            }
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


}