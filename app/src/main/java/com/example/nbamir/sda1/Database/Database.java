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

    public void addEvent(Item item, DatabaseReference databaseReference){
        databaseReference.push().setValue(item);
    }

    /**
     * @param item
     * @param databaseReference
     * @param uid
     * @param user
     */
    public void pendingEvent(Item item, DatabaseReference databaseReference, String uid, String user){
        this.item = item;
        this.databaseReference = databaseReference;
        this.uid = uid;
        this.user = user;
        databaseReference.child("Pending Events").child(user).child(uid).push().setValue(item);
    }

}
