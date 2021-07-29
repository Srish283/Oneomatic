package com.srishti.oneomatic.Database;

public class UserHelperClass {
    String name,email,phone,passw;

    public UserHelperClass(){}

    public  UserHelperClass(String name,String email,String phone,String passw){
        this.name=name;
        this.email=email;
        this.phone=phone;
        this.passw=passw;

    }

    public String getName(){
        return name;
    }
    public String getEmail(){
        return email;
    }
    public String getPhoneNumber(){
        return phone;
    }
    public String getPassword(){
        return passw;
    }


    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhoneNumber(String phone) {
        this.phone = phone;
    }

    public void setPassword(String passw) {
        this.passw = passw;
    }


}
