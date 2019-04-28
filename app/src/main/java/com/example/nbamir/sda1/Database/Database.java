package com.example.nbamir.sda1.Database;

import com.example.nbamir.sda1.EventMaker.Item;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Database {

    private Item item;
    private DatabaseReference databaseReference;
    private String uid;
    private String user;

    public DatabaseReference getDatabase(){
        return FirebaseDatabase.getInstance().getReference();
    }

    public void addItem(Item item, DatabaseReference databaseReference){
        databaseReference.push().setValue(item);
    }
}
