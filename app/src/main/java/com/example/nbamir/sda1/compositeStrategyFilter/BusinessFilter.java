package com.example.nbamir.sda1.compositeStrategyFilter;

import android.util.Log;

import com.example.nbamir.sda1.EventMaker.Item;

import java.util.ArrayList;

public class BusinessFilter extends FilterStrategy {

    @Override
    public ArrayList<Item> filter(ArrayList<Item> eventList) {
        if(filteredList!=null){
            filteredList.clear();

        }
        for(Item e:eventList){
            if(e.category.equals("Business")){
                filteredList.add(e);
                Log.d("filteredList","afterfilteredaddedevent"+e.toString());
            }
        }
        return filteredList;
    }
}