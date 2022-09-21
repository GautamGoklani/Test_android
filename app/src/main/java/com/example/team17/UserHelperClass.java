package com.example.team17;


public class UserHelperClass {
    String uname, email, phone, pass;

    public UserHelperClass(String uname, String phone, String email, String pass) {
        this.uname = uname;
        this.email = email;
        this.phone = phone;
        this.pass = pass;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }
}
