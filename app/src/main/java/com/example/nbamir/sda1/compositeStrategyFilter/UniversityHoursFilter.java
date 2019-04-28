package com.example.nbamir.sda1.compositeStrategyFilter;

import com.example.nbamir.sda1.EventMaker.Item;

import java.util.ArrayList;

public class UniversityHoursFilter extends FilterStrategy {

    @Override
    public ArrayList<Item> filter(ArrayList<Item> eventList) {
        if(filteredList!=null){
            filteredList.clear();

        }
        // TODO Auto-generated method stub
        /*for(Item e:eventList){
            if(e.time.equals("University Hours")){
                filteredList.add(e);
            }
        }*/
        return filteredList;
    }
}
