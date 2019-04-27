package com.example.nbamir.sda1.EventBoard;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.nbamir.sda1.EventMaker.Event;
import com.example.nbamir.sda1.EventMaker.Item;
import com.example.nbamir.sda1.R;

import java.util.ArrayList;

public class ItemBoardSingleton extends AppCompatActivity {

    private static ItemBoardSingleton instance;

    private ArrayList<Item> eventList=new ArrayList<>();

    private ItemBoardSingleton(){}

    public static ItemBoardSingleton getInstance(){
        if(instance==null){
            instance=new ItemBoardSingleton();
        }
        return instance;
    }

    public ArrayList<Item> getEventList(){

        return eventList;
    }

    public void initEventPanels(Context context, RecyclerView mRecyclerView, ArrayList<Item> itemList) {
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(context, 2);
        mRecyclerView.setLayoutManager(mLayoutManager);

        AndroidDataAdapter mAdapter = new AndroidDataAdapter(context, itemList);
        mRecyclerView.setAdapter(mAdapter);
    }

}

