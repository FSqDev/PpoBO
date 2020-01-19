package com.example.test.ppobo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Register
 * V1.1
 *
 * This activity deals with creating a new account
 * Creates new account using Firebase Authentication
 *
 * @author rqin1
 * @author dchen
 */
public class Registration extends AppCompatActivity {

    // Initialize Variables
    TextView emailTV;
    TextView usernameTV;
    TextView passwordTV;
    Button registerBTN;
    String TAG = "Sample";
    FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private String email;
    private String username;
    private String password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        // Access a Cloud Firestore instance from your Activity
        db = FirebaseFirestore.getInstance();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        // Assign Variables
        emailTV = findViewById(R.id.email_new_user_tv);
        passwordTV = findViewById(R.id.password_new_user_tv);
        registerBTN = findViewById(R.id.register_new_user_btn);
        usernameTV = findViewById(R.id.username_new_user_tv);
        final CollectionReference collectionReference = db.collection("Users");

        // Upon pressing the register button
        registerBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Reset error messages
                findViewById(R.id.register_invalid_username).setVisibility(View.INVISIBLE);
                findViewById(R.id.register_invalid_email).setVisibility(View.INVISIBLE);
                // Get strings from TextView
                email = emailTV.getText().toString();
                password = passwordTV.getText().toString();
                username = usernameTV.getText().toString();
                if (email.equals("")){
                    email = " ";
                }
                if (username.equals("")){
                    username = " ";
                }
                if (password.equals("")){
                    password = " ";
                }
                // Creates a new user with email and password input by user
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(Registration.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                // If email is valid and not in use
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "createUserWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    // Creates a new user in the database
                                    HashMap<String, String> data = new HashMap<>();
                                    data.put("username",username);
                                    collectionReference
                                            .document(email)
                                            .set(data)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Log.d(TAG,"Data Addition Successful");
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    // Failure to add user to database
                                                    Log.d(TAG,"Data Addition Failed" + e.toString());
                                                    findViewById(R.id.register_invalid_username).setVisibility(View.VISIBLE);
                                                }
                                            });
                                    Intent intent = new Intent(Registration.this, MainActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                    findViewById(R.id.register_invalid_email).setVisibility(View.VISIBLE);
                                }

                                // ...
                            }
                        });
            }
        });
    }

    @Override
    public void onBackPressed(){
        finish();
    }
}
