package com.example.test.ppobo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Document;

import java.util.ArrayList;
import java.util.HashMap;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import users.User;

public class ManageCareTakers extends AppCompatActivity {

    EditText careTakerTV;
    Button careTakerBTN;
    String careTaker;
    FirebaseFirestore db;
    FirebaseAuth mAuth;
    private ArrayList<User> careGivers;
    private CareGiverAdapter adapter;
    ListView careGiverListView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_care_takers);

        careTakerTV = findViewById(R.id.add_care_taker_text);
        careTakerBTN = findViewById(R.id.add_care_taker);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        careGiverListView = findViewById(R.id.care_giver_list);
        getCareTakers();

        careTakerBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                careTaker = careTakerTV.getText().toString();
                if (!careTaker.equals("")) {
                    DocumentReference documentReference = db.collection("Users").document(careTaker);
                    documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot documentSnapshot = task.getResult();
                                if (documentSnapshot.exists()) {
                                    if (documentSnapshot.getString("userType").equals("Care Giver")) {
                                        HashMap<String, String> data = new HashMap<>();
                                        data.put("name", documentSnapshot.getString("name"));
                                        data.put("phoneNum", documentSnapshot.getString("phoneNum"));
                                        db.collection("Users").document(mAuth.getCurrentUser().getEmail()).collection("people").document(careTaker).set(data);
                                        HashMap<String, String> data2 = new HashMap<>();
                                        data2.put("email", mAuth.getCurrentUser().getEmail());
                                        db.collection("Users").document(careTaker).collection("people").document(mAuth.getCurrentUser().getEmail()).set(data2);
                                        careTakerTV.setText("");
                                        getCareTakers();
                                        adapter.notifyDataSetChanged();
                                    }
                                }
                            }
                        }
                    });
                }
            }
        });
    }
    private void getCareTakers(){
        careGivers = new ArrayList<>();
        adapter = new CareGiverAdapter(this,R.layout.care_giver_adapter, careGivers);
        careGiverListView.setAdapter(adapter);
        db.collection("Users").document(mAuth.getCurrentUser().getEmail()).collection("people").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                User careGiver = new User(document.getString("name"), document.getString("phoneNum"), document.getId(), "Care Giver");
                                careGivers.add(careGiver);
                                adapter.notifyDataSetChanged();
                            }
                        }
                    }
                });


    }

}
