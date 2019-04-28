package com.example.nbamir.sda1.UserAccounts;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.nbamir.sda1.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignupActivity extends AppCompatActivity {

    EditText emailText;
    EditText passwordText;
    EditText nameText;
    FirebaseAuth firebaseAuth;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        emailText = findViewById(R.id.email);
        nameText = findViewById(R.id.name);
        passwordText = findViewById(R.id.password);

        firebaseAuth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference("Users");
    }

    public void signupUser(View view){
        final String email = emailText.getText().toString();
        String pass = passwordText.getText().toString();
        final String name = nameText.getText().toString();

        firebaseAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    reference.child(firebaseAuth.getCurrentUser().getUid()).setValue(new User(email, name, "Unverified"));
                }
            }
        });
    }
}
