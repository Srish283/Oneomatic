package com.srishti.oneomatic.Authentication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.srishti.oneomatic.Dashboard;
import com.srishti.oneomatic.Database.SessionManager;
import com.srishti.oneomatic.Database.UserHelperClass;
import com.srishti.oneomatic.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    Toolbar toolbar;
    TextInputLayout name,email,phone,passw,cpass;
    Button register,login,cancel;
    ImageView logo,signIcon;
    TextView logoT,pageT;
    FirebaseDatabase rootNode;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_register);

        register=findViewById(R.id.btnRegister);
        login=findViewById(R.id.btnLogin1);
        cancel=findViewById(R.id.btnCancel);
        toolbar=findViewById(R.id.toolbar);
        name=findViewById(R.id.etName);
        email=findViewById(R.id.etEmail);
        phone=findViewById(R.id.etPhoneNumber);
        passw=findViewById(R.id.etPassword);
        logo=findViewById(R.id.logoImage);
        signIcon=findViewById(R.id.signup);
        logoT=findViewById(R.id.logotext1);
        pageT=findViewById(R.id.reg);


        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Register Page");

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!validateName()|!validateEmail()|!validatePhone()|!validatePassword()){
                    return;
                }

                rootNode=FirebaseDatabase.getInstance();
                reference=rootNode.getReference("users");

                //get all values
                String rname= name.getEditText().getText().toString();
                String remail= email.getEditText().getText().toString();
                String rphone= phone.getEditText().getText().toString();
                String rpass= passw.getEditText().getText().toString();

                //create a session
                SessionManager sessionManager=new SessionManager(RegisterActivity.this);
                sessionManager.createLoginSession(rname,remail,rphone,rpass);



                UserHelperClass helperClass=new UserHelperClass(rname,remail,rphone,rpass);
                reference.child(rphone).setValue(helperClass);
                Toast.makeText(getApplicationContext(),"Registration Success",Toast.LENGTH_SHORT).show();

                Intent intent=new Intent(RegisterActivity.this, Dashboard.class);
                Pair[] pairs=new  Pair[12];
                pairs[0]=new Pair<View,String>(toolbar,"tool_trans");
                pairs[1]=new Pair<View,String>(logo,"logo_image");
                pairs[2]=new Pair<View,String>(logoT,"logo_text");
                pairs[3]=new Pair<View,String>(pageT,"sign_in");
                pairs[4]=new Pair<View,String>(signIcon,"login_image");
                pairs[5]=new Pair<View,String>(login,"login_sign");
                pairs[6]=new Pair<View,String>(cancel,"cancel_trans");
                pairs[7]=new Pair<View,String>(register,"account");
                pairs[8]=new Pair<View,String>(phone,"phone_num");
                pairs[9]=new Pair<View,String>(passw,"password");
                pairs[10]=new Pair<View,String>(name,"username");
                pairs[11]=new Pair<View,String>(email,"email");



                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    ActivityOptions options=ActivityOptions.makeSceneTransitionAnimation(RegisterActivity.this,pairs);
                    startActivity(intent,options.toBundle());
                    finish();
                }
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(RegisterActivity.this, LoginActivity.class);
                Pair[] pairs=new  Pair[12];
                pairs[0]=new Pair<View,String>(toolbar,"tool_trans");
                pairs[1]=new Pair<View,String>(logo,"logo_image");
                pairs[2]=new Pair<View,String>(logoT,"logo_text");
                pairs[3]=new Pair<View,String>(pageT,"sign_in");
                pairs[4]=new Pair<View,String>(signIcon,"login_image");
                pairs[5]=new Pair<View,String>(login,"login_sign");
                pairs[6]=new Pair<View,String>(cancel,"cancel_trans");
                pairs[7]=new Pair<View,String>(register,"account");
                pairs[8]=new Pair<View,String>(phone,"phone_num");
                pairs[9]=new Pair<View,String>(passw,"password");
                pairs[10]=new Pair<View,String>(name,"username");
                pairs[11]=new Pair<View,String>(email,"email");



                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    ActivityOptions options=ActivityOptions.makeSceneTransitionAnimation(RegisterActivity.this,pairs);
                    startActivity(intent,options.toBundle());
                    finish();
                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name.getEditText().setText("");
                phone.getEditText().setText("");
                passw.getEditText().setText("");
                email.getEditText().setText("");
                //finish();
            }
        });
    }
    private  Boolean validateName(){
        String val=name.getEditText().getText().toString();
        if(val.isEmpty()){
            name.setError("Name Field cannot be Empty");
            return false;
        }else {
            name.setError(null);
            name.setErrorEnabled(false);
            return true;
        }
    }

    private  Boolean validateEmail(){
        String val=email.getEditText().getText().toString();
        String pattern="[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if(val.isEmpty()){
            email.setError("Email Field cannot be Empty");
            return false;
        }else  if(!val.matches(pattern)){
            email.setError("Invalid Email Address");
            return false;
        }
        else {
            email.setError(null);
            email.setErrorEnabled(false);
            return true;
        }
    }



    private  Boolean validatePhone(){
        String val=phone.getEditText().getText().toString();
        if(val.isEmpty()){
            phone.setError("Phone Number Field cannot be Empty");
            return false;
        }else if(val.length()!=10){
            phone.setError("Length of Digits Should be 10");
            return false;
        } else{
            phone.setError(null);
            phone.setErrorEnabled(false);
            return true;
        }
    }

    private  Boolean validatePassword(){
        Pattern pattern;
        Matcher matcher;
        String val=passw.getEditText().getText().toString();
        String Password_Pattern="^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[#$%^*+_])(?=\\S+$).{8,}$";
        pattern= Pattern.compile(Password_Pattern);
        matcher=pattern.matcher(val);

        if(val.isEmpty()){
            passw.setError("Password Field cannot be Empty");
            return false;
        }else  if(!matcher.matches()){
            passw.setError("Password is weak");
            return false;
        }
        else {
            passw.setError(null);
            passw.setErrorEnabled(false);
            return true;
        }
    }


}