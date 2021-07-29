package com.srishti.oneomatic.Database;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

public class SessionManager {
    SharedPreferences userSession;
    SharedPreferences.Editor editor;
    Context context;

    private static final String IS_LOGIN = "IsLoggedIn";

    public static final String KEY_NAME = "fullname";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_PHONE = "phoneNumber";
    public static final String KEY_PASSWORD = "password";

    public SessionManager(Context context){
        userSession=context.getSharedPreferences("userLoginSession",Context.MODE_PRIVATE);
        editor=userSession.edit();
    }

    public  void  createLoginSession(String fullname,String email,String phone,String password){
        editor.putBoolean(IS_LOGIN,true);
        editor.putString(KEY_NAME,fullname);
        editor.putString(KEY_EMAIL,email);
        editor.putString(KEY_PHONE,phone);
        editor.putString(KEY_PASSWORD,password);
        editor.commit();
    }

    //Get value from session using hashmap as data gets stored as key and values
    public HashMap<String,String> getUserDetailFromSession(){
        HashMap<String,String> userData=new HashMap<String, String>();
        userData.put(KEY_NAME,userSession.getString(KEY_NAME,null));
        userData.put(KEY_EMAIL,userSession.getString(KEY_EMAIL,null));
        userData.put(KEY_PHONE,userSession.getString(KEY_PHONE,null));
        userData.put(KEY_PASSWORD,userSession.getString(KEY_PASSWORD,null));

        return  userData;
    }
    public boolean checkLogin(){
        if(userSession.getBoolean(IS_LOGIN,false)){
            return true;
        }else {
            return false;
        }
    }

    public void  logoutUser() {
        editor.clear();
        editor.commit();
    }

}