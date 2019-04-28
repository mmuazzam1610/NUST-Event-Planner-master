package com.example.nbamir.sda1.EventMaker;

import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.nbamir.sda1.ItemBoard.ItemBoardSingleton;
import com.example.nbamir.sda1.NavigationDrawer.NavigationDrawer;
import com.example.nbamir.sda1.R;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class ItemPageActivity extends NavigationDrawer {

    private TextView nameView; //-
    private TextView ownerView; //-
    private TextView dateView; //-
    private TextView priceView;//-
    private TextView locationView; //-
    private TextView descriptionView; //-
    private ImageView imageView;

    Calendar calendar1,calendar2;
    DateFormat df;
    ItemBoardSingleton eventBoardSingleton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame); //Remember this is the FrameLayout area within your activity_main.xml
        getLayoutInflater().inflate(R.layout.activity_event_page, contentFrameLayout);

        Intent intent = getIntent();
        String image = intent.getStringExtra("image");
        String name = intent.getStringExtra("name");
        String date = intent.getStringExtra("date");
        long startTime = intent.getLongExtra("startTime",0);
        long endTime = intent.getLongExtra("endTime",0);
        String category = intent.getStringExtra("category");
        String time = intent.getStringExtra("time");
        String heldBy = intent.getStringExtra("heldBy");
        String poster = intent.getStringExtra("poster");
        String venue = intent.getStringExtra("venue");
        String description = intent.getStringExtra("description");

        eventBoardSingleton= ItemBoardSingleton.getInstance();

        nameView = findViewById(R.id.Name);
        ownerView= findViewById(R.id.itemOwner);
        dateView = findViewById(R.id.Date);
        priceView = findViewById(R.id.priceView);
        locationView = findViewById(R.id.LocationView);
        descriptionView = findViewById(R.id.Description);
        imageView = findViewById(R.id.Image);

        calendar1 = Calendar.getInstance();
        calendar2 = Calendar.getInstance();
        calendar1.setTimeInMillis(startTime);
        calendar2.setTimeInMillis(endTime);


        df =  new SimpleDateFormat("HH:mm");

        Picasso.get().load(image).into(imageView);
        nameView.setText(name);
        dateView.setText("Date: "+date);
        ownerView.setText("Held By: "+heldBy);
        priceView.setText("Rs. "+df.format(calendar1.getTime())+" - "+df.format(calendar2.getTime()));
        locationView.setText(venue);
        descriptionView.setText(description);
    }
}

