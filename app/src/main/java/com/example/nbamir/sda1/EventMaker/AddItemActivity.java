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

    String selectedDate,imageid,imagelink;

    Database db;
    Calendar calendar;

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
        priceView = findViewById(R.id.itemPrice);

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

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        //Initialize Views
        btnChoose = (Button) findViewById(R.id.chooseImage);
        uploadImage = (ImageView) findViewById(R.id.imageView2);

        btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

        calendar = Calendar.getInstance();
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
                uploadImage.setMaxHeight(bitmap.getHeight());
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }


    public void addEvent(View view) {
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

                            // Getting Values
                            String category = catSpinner.getSelectedItem().toString();
                            String location = locationView.getText().toString();
                            String desc = itemDesc.getText().toString();
                            String name = itemName.getText().toString();
                            String price = priceView.getText().toString();

                            mYear = calendar.get(Calendar.YEAR);
                            mMonth = calendar.get(Calendar.MONTH) + 1;
                            mDay = calendar.get(Calendar.DAY_OF_MONTH);
                            selectedDate = mDay + ":" + mMonth + ":" + mYear;

                            Item item = new Item();

                            item.setCategory(category);
                            item.setDescription(desc);
                            item.setName(name);
                            item.setLocation(location);
                            item.setImage(imagelink);
                            item.setUid(uid);
                            item.setPrice(price);
                            item.setDate(selectedDate);
                            item.setOwner(poster);
                            db.addItem(item,myRef.child("Items").child(category));
                            finish();
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


    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser!=null){
            uid = currentUser.getUid();
            String email = currentUser.getEmail();
            name = currentUser.getDisplayName();
            Log.d("UserFound"+uid+email+name,""+currentUser);
        }
        else{
            Log.d("NoUserFound",""+currentUser);
        }
    }
}
