package com.example.nbamir.sda1.compositeStrategyFilter;

import android.util.Log;

import com.example.nbamir.sda1.EventMaker.Item;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

public class CompositeFilter extends FilterStrategy {

    public ArrayList<FilterStrategy> filterList = new ArrayList<FilterStrategy>();
    private ArrayList<Item> filteredtemp = new ArrayList<Item>();

    @Override
    public ArrayList<Item> filter(ArrayList<Item> eventList) {
        if(filteredList!=null){
            filteredList.clear();
        }
        // TODO Auto-generated method stub
        if(filterList.size()==0){
            filteredList=(ArrayList) eventList.clone();
            Log.d("filteredList","nofilters"+filterList.size());

        }
        else{

            filteredtemp.clear();
            Log.d("filteredList","filters"+filterList.size());
        }

        for(FilterStrategy fs:filterList)
        {
            Log.d("filteredList","filteredtempbefore"+filteredtemp.toString());
            Log.d("filteredList","filterapplied"+fs.toString());
            filteredtemp=fs.filter(eventList);
            Log.d("filteredList","filteredtemp"+filteredtemp.toString());

            for(Item e:filteredtemp){
                filteredList.add(e);

            }
        }
        //remove duplicates
        Set<Item> s = new LinkedHashSet<>(filteredList);
        filteredList.clear();
        filteredList.addAll(s);
        //sort
        return filteredList;
    }

    public void addFilter(FilterStrategy fs)
    {
        filterList.add(fs);
    }

    public void removeFilter(FilterStrategy fs)
    {
        filterList.remove(fs);
    }
}