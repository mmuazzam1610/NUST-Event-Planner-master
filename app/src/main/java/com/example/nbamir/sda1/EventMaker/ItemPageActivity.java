package com.example.nbamir.sda1.EventMaker;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nbamir.sda1.CheckoutActivity;
import com.example.nbamir.sda1.ItemBoard.ItemBoardSingleton;
import com.example.nbamir.sda1.NavigationDrawer.NavigationDrawer;
import com.example.nbamir.sda1.R;
import com.example.nbamir.sda1.UserAccounts.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ItemPageActivity extends NavigationDrawer {

    private TextView nameView; //-
    private TextView ownerView; //-
    private TextView dateView; //-
    private TextView priceView;//-
    private TextView locationView; //-
    private TextView descriptionView; //-
    private ImageView imageView;

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
        String price = intent.getStringExtra("price");
        String category = intent.getStringExtra("category");
        String owner = intent.getStringExtra("owner");
        String location = intent.getStringExtra("location");
        String description = intent.getStringExtra("description");

        eventBoardSingleton= ItemBoardSingleton.getInstance();

        nameView = findViewById(R.id.Name);
        ownerView = findViewById(R.id.itemOwner);
        dateView = findViewById(R.id.Date);
        priceView = findViewById(R.id.priceView);
        locationView = findViewById(R.id.LocationView);
        descriptionView = findViewById(R.id.Description);
        imageView = findViewById(R.id.Image);

        Picasso.get().load(image).into(imageView);
        nameView.setText(name);
        dateView.setText("Date: "+date);
        ownerView.setText("Posted by : " + owner);
        priceView.setText("Price: Rs. "+ price);
        locationView.setText(location);
        descriptionView.setText(description);
    }

    public void buyItem(View view){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if (user.status.equals("Unverified")){
                    Toast.makeText(ItemPageActivity.this, "You are not verified. Kindly verify your student status.", Toast.LENGTH_SHORT).show();
                }
                else{
                    startActivity(new Intent(ItemPageActivity.this, CheckoutActivity.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}

