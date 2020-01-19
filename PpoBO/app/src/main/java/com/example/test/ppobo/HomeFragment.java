package com.example.test.ppobo;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

import users.User;

public class HomeFragment extends Fragment {

    ListView listCareReceivers;
    CareGiverAdapter adapter;
    ArrayList<User> careReceivers;
    FirebaseFirestore db ;
    FirebaseAuth mAuth;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        // DO STUFF
        ((MainActivity) getActivity()).hideFloatingActionButton();

        listCareReceivers = root.findViewById(R.id.list_care_receivers);
        careReceivers = new ArrayList<>();
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        adapter = new CareGiverAdapter(getActivity(), R.layout.care_giver_adapter, careReceivers);

        listCareReceivers.setAdapter(adapter);

        if (mAuth.getCurrentUser() != null) {
            db.collection("Users").document(mAuth.getCurrentUser().getEmail()).collection("people").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            db.collection("Users").document(document.getId()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    DocumentSnapshot docSnap = task.getResult();
                                    User careReceiver = new User(docSnap.getString("name"), docSnap.getString("phoneNum"), docSnap.getString("email"), docSnap.getString("userType"));
                                    careReceivers.add(careReceiver);
                                    adapter.notifyDataSetChanged();
                                }
                            });
                        }
                    }
                }
            });
        }

        root.findViewById(R.id.move_map).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MapFragment.class);
                startActivity(intent);
            }
        });



        return root;
    }
}