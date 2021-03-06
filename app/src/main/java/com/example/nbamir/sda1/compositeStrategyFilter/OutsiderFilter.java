package com.example.nbamir.sda1.compositeStrategyFilter;


import com.example.nbamir.sda1.EventMaker.Item;

import java.util.ArrayList;

public class OutsiderFilter extends FilterStrategy {

    @Override
    public ArrayList<Item> filter(ArrayList<Item> eventList) {
        if(filteredList!=null){
            filteredList.clear();

        }
        for(Item e:eventList){
            if(e.category.equals("Outsider")){
                filteredList.add(e);
            }
        }
        return filteredList;
    }

}