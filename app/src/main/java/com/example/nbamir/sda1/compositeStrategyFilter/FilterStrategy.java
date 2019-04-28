package com.example.nbamir.sda1.compositeStrategyFilter;

import com.example.nbamir.sda1.EventMaker.Item;

import java.util.ArrayList;

public abstract class FilterStrategy {

    ArrayList<Item> filteredList = new ArrayList<Item>();

    public abstract ArrayList<Item> filter(ArrayList<Item> eventList);

}
