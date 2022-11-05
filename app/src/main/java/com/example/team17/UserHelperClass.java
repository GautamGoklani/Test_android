package com.example.team17;


public class UserHelperClass {
    String uname, email, phone, pass, msg, method;

    public UserHelperClass(String uname, String email, String msg, String method) {
        this.uname = uname;
        this.email = email;
        this.msg = msg;
        this.method = method;
    }

    public UserHelperClass(String uname, String phone, String email, String pass, String msg, String method) {
        this.uname = uname;
        this.email = email;
        this.phone = phone;
        this.pass = pass;
        this.msg = msg;
        this.method = method;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
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

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
