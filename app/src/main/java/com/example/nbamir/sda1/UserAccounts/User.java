package com.example.nbamir.sda1.UserAccounts;

public class User {
    public String email;
    public String name;
    public String status;

    User(){

    }

    User(String email, String name, String status){
        this.email = email;
        this.name = name;
        this.status = status;
    }
}
