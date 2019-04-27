package com.example.nbamir.sda1.EventMaker;

import android.graphics.drawable.shapes.Shape;
import android.support.constraint.solver.widgets.Rectangle;

import com.google.firebase.database.DatabaseReference;

import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;


abstract class items implements Cloneable {

    public String date; //-
    public String name; //-
    public String location; //-
    public String category; //-
    public String price;
    public String image; //-
    public String owner; //published by name
    public String description; //-
    public String uid;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getOwner() {
        return owner;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Object clone() {
        Object clone = null;

        try {
            clone = super.clone();

        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }

        return clone;
    }
}

public class Item extends items{

    public Item() {
    }

    public Item(String date, String name, String location, String category, String price, String image, String owner, String description, String uid) {
        this.date = date;
        this.name = name;
        this.location = location;
        this.category = category;
        this.price = price;
        this.image = image;
        this.owner = owner;
        this.description = description;
        this.uid = uid;
    }

    public void addItem(DatabaseReference myRef) { //add to database package
        myRef.child("Items").push().setValue(this);
    }


}


class itemsCache {

    private static Hashtable<String, items> itemMap  = new Hashtable<String, items>();

    public static items getItem(String eventId) {
        items cachedEvents = itemMap.get(eventId);
        return (items) cachedEvents.clone();
    }

    // for each shape run database query and create shape
    // shapeMap.put(shapeKey, shape);
    // for example, we are adding three shapes

    public static void loadCache() {
        Item item = new Item();
        item.setCategory("Notes");
        itemMap.put(item.getCategory(), item);
        Item item1 = new Item();
        item1.setCategory("Books");
        itemMap.put(item1.getCategory(), item1);
        Item item2 = new Item();
        item2.setCategory("Laptop/PC");
        itemMap.put(item2.getCategory(), item2);
    }
}
