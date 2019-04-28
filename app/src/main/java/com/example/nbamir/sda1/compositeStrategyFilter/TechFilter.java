package com.example.nbamir.sda1.compositeStrategyFilter;

import android.util.Log;

import com.example.nbamir.sda1.EventMaker.Item;

import java.util.ArrayList;

public class TechFilter extends FilterStrategy {

    @Override
    public ArrayList<Item> filter(ArrayList<Item> eventList) {
        Log.d("filteredList","initially"+filteredList.toString());
        if(filteredList!=null){
            filteredList.clear();

        }

        for(Item e:eventList){
            if(e.category.equals("Tech")){
                filteredList.add(e); //check this
                Log.d("filteredList","addedtotemp"+e.toString());

            }
        }

        return filteredList;
    }
}
