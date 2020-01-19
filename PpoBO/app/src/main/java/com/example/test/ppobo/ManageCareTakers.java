package com.example.test.ppobo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Document;

import java.util.HashMap;

public class ManageCareTakers extends AppCompatActivity {

    EditText careTakerTV;
    Button careTakerBTN;
    String careTaker;
    FirebaseFirestore db;
    FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_care_takers);

        careTakerTV = findViewById(R.id.add_care_taker_text);
        careTakerBTN = findViewById(R.id.add_care_taker);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        careTakerBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                careTaker = careTakerTV.getText().toString();
                DocumentReference documentReference = db.collection("Users").document(careTaker);
                documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            DocumentSnapshot documentSnapshot = task.getResult();
                            if (documentSnapshot.exists()) {
                                if (documentSnapshot.getString("userType").equals("Care Giver")) {
                                    HashMap<String, String> data = new HashMap<>();
                                    data.put("name", documentSnapshot.getString("name"));
                                    data.put("phoneNum", documentSnapshot.getString("phoneNum"));
                                    db.collection("Users").document(mAuth.getCurrentUser().getEmail()).collection("people").document(careTaker).set(data);
                                    careTakerTV.setText("");
                                }
                            }
                        }
                    }
                });
            }
        });
    }


}
