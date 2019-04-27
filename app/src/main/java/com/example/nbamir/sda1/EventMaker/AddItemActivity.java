package com.example.nbamir.sda1.EventMaker;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.nbamir.sda1.Database.Database;
import com.example.nbamir.sda1.NavigationDrawer.NavigationDrawer;
import com.example.nbamir.sda1.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.Calendar;
import java.util.UUID;

public class AddItemActivity extends NavigationDrawer {
    Uri download_url;
    int i=1;
    private DatabaseReference myRef;
    private FirebaseAuth mAuth;
    //Firebase
    FirebaseStorage storage;
    StorageReference storageReference;
    String uid,poster;
    String user;
    String name;

    // Calendars
    private Calendar calendarFrom;
    private  Calendar calendarTo;
    private int mill;


    // date vars
    private int mYear;
    private int mMonth;
    private int mDay;

    // Widgets
    private Spinner catSpinner;
    private TextView locationView;
    private TextView itemName;
    private TextView priceView;
    private TextView itemDesc;
    private CalendarView calendarView;

    String selectedDate,imageid,imagelink;

    Database db;

    private Button btnChoose, btnUpload;
    private ImageView uploadImage;

    private Uri filePath;

    private final int PICK_IMAGE_REQUEST = 71;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame); //Remember this is the FrameLayout area within your activity_main.xml
        getLayoutInflater().inflate(R.layout.activity_add_event, contentFrameLayout);

        itemsCache.loadCache();

        setCategorySpinner();

        itemName = (TextView) findViewById(R.id.itemName);
        itemDesc = (TextView) findViewById(R.id.itemDesc);
        locationView = (TextView) findViewById(R.id.itemLocation);

        // ...
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(currentUser != null) {
            uid=currentUser.getUid();
            poster=currentUser.getDisplayName();
        }
        // Write a message to the database
        db = new Database();
        myRef=db.getDatabase();

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("datasnapshot",""+dataSnapshot);

                for(DataSnapshot datas: dataSnapshot.getChildren()){

                    String child =datas.getKey();
                    Log.d("children",""+child);
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child(child).child(uid);
                    ref.addValueEventListener(new ValueEventListener() {

                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.hasChild("type")){
                                Log.d("typevalue",""+dataSnapshot.child("type").getValue().toString());
                                user=dataSnapshot.child("type").getValue().toString();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        //Initialize Views
        btnChoose = (Button) findViewById(R.id.chooseImage);
        btnUpload = (Button) findViewById(R.id.uploadImage);
        uploadImage = (ImageView) findViewById(R.id.imageView2);

        btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();
            }
        });

        // Calendar
        calendarFrom = Calendar.getInstance();
        calendarTo = Calendar.getInstance();

        // Getting Widget By ID
        calendarView = (CalendarView) findViewById(R.id.calendarView);

        // Listener for Date - CALENDAR
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year,
                                            int month, int dayOfMonth) {
                mYear = year;
                mMonth = month;
                mDay = dayOfMonth;
                calendarFrom.set(year, month, dayOfMonth);

                selectedDate = new StringBuilder().append(mMonth + 1)
                        .append("-").append(mDay).append("-").append(mYear)
                        .append(" ").toString();


                Toast.makeText(getApplicationContext(), selectedDate, Toast.LENGTH_LONG).show();
            }
        });


    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                uploadImage.setImageBitmap(bitmap);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    private void uploadImage() {

        if(filePath != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();
            imageid="images/"+ UUID.randomUUID().toString();
            Log.d("imagelink",imageid);

            StorageReference ref = storageReference.child(imageid);
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(AddItemActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();
                            Task<Uri> urlTask=taskSnapshot.getStorage().getDownloadUrl();

                            while(!urlTask.isSuccessful()){
                            }
                            download_url = urlTask.getResult();
                            imagelink = String.valueOf(download_url);
                            Log.d("imagelink",imagelink);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(AddItemActivity.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded "+(int)progress+"%");
                        }
                    });
        }
    }

    public void addEvent(View view) {
        // Getting Values
        String category = catSpinner.getSelectedItem().toString();
        String location = locationView.getText().toString();
        String desc = itemDesc.getText().toString();
        String name = itemName.getText().toString();
        String price = priceView.getText().toString();
        long startTime = calendarFrom.getTimeInMillis();
        long endTime = calendarTo.getTimeInMillis();

        Item clonedEvent = (Item) itemsCache.getItem(category);

        clonedEvent.setCategory(category);
        clonedEvent.setDescription(desc);
        clonedEvent.setName(name);
        clonedEvent.setLocation(location);
        clonedEvent.setImage(imagelink);
        clonedEvent.setUid(uid);
        clonedEvent.setPrice(price);
        clonedEvent.setDate(selectedDate);
        clonedEvent.setOwner(poster);
        db.pendingEvent(clonedEvent,myRef,uid,user);
        this.finish();
    }

    public void setCategorySpinner() {
        catSpinner = (Spinner) findViewById(R.id.categories_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.categories, android.R.layout.simple_spinner_item);
//         Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//         Apply the adapter to the spinner
        catSpinner.setAdapter(adapter);
    }

    public void getUserType(int uid){


    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser!=null){
            uid = currentUser.getUid();
            String email = currentUser.getEmail();
            name = currentUser.getDisplayName();
            Log.d("UserFound"+uid+email+name,""+currentUser);
            Toast.makeText(this,""+uid+email+name,Toast.LENGTH_LONG);
        }
        else{
            Log.d("NoUserFound",""+currentUser);
        }
    }
}
