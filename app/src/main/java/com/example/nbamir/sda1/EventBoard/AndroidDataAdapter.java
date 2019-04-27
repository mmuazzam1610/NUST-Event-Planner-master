package com.example.nbamir.sda1.EventBoard;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.provider.ContactsContract;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nbamir.sda1.EventMaker.Item;
import com.example.nbamir.sda1.EventMaker.ItemPageActivity;
import com.example.nbamir.sda1.R;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class AndroidDataAdapter extends RecyclerView.Adapter<AndroidDataAdapter.ViewHolder> {
    private ArrayList<Item> arrayList;
    private Context mcontext;
    public static int position;
    public Calendar calendar;
    DateFormat df;
    public static String name;
    boolean selected=false;
    public static String id;


    public AndroidDataAdapter(Context context, ArrayList<Item> android) {
        this.arrayList = android;
        this.mcontext = context;

    }

    @Override
    public void onBindViewHolder(final AndroidDataAdapter.ViewHolder holder, int i) {

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = arrayList.get(position).name;
                Log.d("imagename",name);
                if(selected){
                    holder.imageView.clearColorFilter();
                    selected=false;
                }
                else{
                    ColorFilter cf = new PorterDuffColorFilter(Color.RED,PorterDuff.Mode.SCREEN);

                    holder.imageView.setColorFilter(cf);
                    selected=true;
                }
            }
        });

        Picasso.get().load(arrayList.get(i).image).into(holder.imageView);

//        if(currentuser.bookmarklist.contains(arrayList.get(i))){
//            holder.bookmark.setImageResource(R.drawable.starmarked);
//            holder.bookmark.setTag(R.drawable.starmarked);

//        }
//        else{
//            holder.bookmark.setImageResource(R.drawable.star);
//            holder.bookmark.setTag(R.drawable.star);

//        }

        calendar = Calendar.getInstance();
        df =  new SimpleDateFormat("HH:mm");

        holder.bookmark.setImageResource(R.drawable.star); //remove
        holder.bookmark.setTag(R.drawable.star); //remove

        holder.userImage.setImageResource(R.drawable.star);
        holder.itemText.setText(arrayList.get(i).name);
        holder.locationText.setText("Location: "+arrayList.get(i).location);
        holder.userText.setText(arrayList.get(i).owner);
        //Log.d("getadapternames","1");
        holder.bookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.bookmark.getTag().equals(R.drawable.star)){
                    holder.bookmark.setImageResource(R.drawable.starmarked);
                    holder.bookmark.setTag(R.drawable.starmarked);
                }
                else{
                    holder.bookmark.setImageResource(R.drawable.star);
                    holder.bookmark.setTag(R.drawable.star);
                }
            }
        });
        holder.itemText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(view.getContext(), "Event", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(mcontext, ItemPageActivity.class);
//                intent.putExtra("Event",position);
                intent.putExtra("image",arrayList.get(position).image);
                intent.putExtra("name",arrayList.get(position).name);
                intent.putExtra("date",arrayList.get(position).date);
                intent.putExtra("category",arrayList.get(position).category);
                intent.putExtra("price",arrayList.get(position).price);
                intent.putExtra("location",arrayList.get(position).location);
                intent.putExtra("description",arrayList.get(position).description);
                intent.putExtra("owner",arrayList.get(position).owner);
                mcontext.startActivity(intent);
            }
        });
    }

    @Override
    public AndroidDataAdapter.ViewHolder onCreateViewHolder(ViewGroup vGroup, int i) {

        View view = LayoutInflater.from(vGroup.getContext()).inflate(R.layout.event_panel_layout, vGroup, false);
        return new ViewHolder(view);
    }


    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView itemText,priceText,locationText,userText;
        private ImageView imageView,userImage,bookmark;

        public ViewHolder(View v) {
            super(v);

            itemText = (TextView) v.findViewById(R.id.itemtext);
            priceText = (TextView) v.findViewById(R.id.pricetext);
            locationText = (TextView) v.findViewById(R.id.locationtext);
            userText = (TextView) v.findViewById(R.id.usertext);

            imageView = (ImageView) v.findViewById(R.id.imagePanel);
            userImage =(ImageView)v.findViewById(R.id.userimage);
            bookmark =(ImageView)v.findViewById(R.id.bookmark);
        }
    }

}
