package com.example.test.ppobo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Login
 * V1.2
 *
 * This activity handles logging in the user using an existing account
 * Logs user in using Firebase Authentication
 *
 * @author atilla
 * @author rqin1
 * @author dchen
 */
public class Login extends AppCompatActivity {

    // Initialize Variables
    String TAG = "Sample";
    EditText usernameTV,passwordTV;
    Button login, register;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        mAuth.signOut();

        // Assign Variables
        usernameTV=findViewById(R.id.username);
        passwordTV=findViewById(R.id.password);
        login=findViewById(R.id.login);
        register = findViewById(R.id.register_btn);


        // Upon Login button press
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = usernameTV.getText().toString();
                String password = passwordTV.getText().toString();
                if (email.equals("")){
                    email = " ";
                }
                if (password.equals("")){
                    password = " ";
                }
                // Login with FireStore
                authenticate(email, password);
            }
        });

        // See if a user is currently logged in
        register.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, Registration.class);
                startActivity(intent);
                finish();
            }
        });

    }

    /**
     * Disables back button on Login Activity
     */
    @Override
    public void onBackPressed(){
        ;
    }

    private void authenticate(String email, String password){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // If user and password combination is in FireStore
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Intent intent = new Intent(Login.this, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(Login.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }
}


