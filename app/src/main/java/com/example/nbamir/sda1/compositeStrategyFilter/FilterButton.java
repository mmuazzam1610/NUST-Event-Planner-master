package com.example.nbamir.sda1.compositeStrategyFilter;

import com.example.nbamir.sda1.EventMaker.Item;

import java.util.ArrayList;

public class FilterButton {
    private FilterStrategy filter;
    private  ArrayList<Item> filteredList;

    public FilterButton(FilterStrategy filter){
        this.filter = filter;
    }

    public ArrayList<Item> executeStrategy(ArrayList<Item> eventList){
        if(filteredList!=null){
            filteredList.clear();

        }
        filteredList=filter.filter(eventList);
        return filteredList;
    }
}