package com.example.test.ppobo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class AlertsFragment extends Fragment {

    FirebaseFirestore db;
    FirebaseAuth mAuth;
    ArrayList<String> alerts;
    ArrayAdapter<String> adapter;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_alerts, container, false);

        // DO STUFF
        ((MainActivity) getActivity()).hideFloatingActionButton();

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        alerts = new ArrayList<>();

        ListView alertLV = root.findViewById(R.id.alert_list_view);
        adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1, alerts);
        alertLV.setAdapter(adapter);

        db.collection("Users").document(mAuth.getCurrentUser().getEmail()).collection("alerts").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (DocumentSnapshot documentSnapshot : task.getResult()){
                        alerts.add(documentSnapshot.getId() + ": " + documentSnapshot.getString("name"));
                        adapter.notifyDataSetChanged();
                    }
                }
            }
        });



        return root;
    }
}
