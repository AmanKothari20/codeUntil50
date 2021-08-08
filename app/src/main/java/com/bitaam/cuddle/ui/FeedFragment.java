package com.bitaam.cuddle.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.bitaam.cuddle.CreateProfileActivity;
import com.bitaam.cuddle.R;
import com.bitaam.cuddle.adapters.FeedAdapter;
import com.bitaam.cuddle.modals.ProfileModal;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class FeedFragment extends Fragment {

    Spinner spinner1,spinner2;
    RecyclerView feedRecyclerView;
    FeedAdapter feedAdapter;
    ArrayList<ProfileModal> profileModals;
    ArrayList<String> profileKeys;
    FirebaseAuth mAuth;

    public FeedFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feed, container, false);

        spinner1 = view.findViewById(R.id.feed_type_spinner);
        spinner2 = view.findViewById(R.id.feed_type_data_spinner);
        spinnerInit();

        feedRecyclerView = view.findViewById(R.id.feedRecycler);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setSmoothScrollbarEnabled(true);

        feedRecyclerView.setLayoutManager(linearLayoutManager);
        profileModals = new ArrayList<>();
        profileKeys = new ArrayList<>();

        mAuth = FirebaseAuth.getInstance();

        return view;
    }

    private void databaseActivities(){

        feedAdapter = new FeedAdapter(profileModals,getContext(),feedRecyclerView);
        feedRecyclerView.setAdapter(feedAdapter);

        feedAdapter.setOnItemClickListener(new FeedAdapter.OnItemClickListener() {
            @Override
            public void onLiked(ProfileModal model) {

            }

            @Override
            public void onDisliked(ProfileModal model) {

            }
        });

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Profiles");
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                ProfileModal modal = snapshot.getValue(ProfileModal.class);
                profileModals.add(modal);
                profileKeys.add(snapshot.getKey());
            }

            @Override
            public void onChildChanged(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull @NotNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

    }

    private void spinnerInit() {

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(requireContext(),R.array.feedTypes,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter);

        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ArrayAdapter<CharSequence> adapter1;
                switch (parent.getItemAtPosition(position).toString()){

                    case "Age":
                        adapter1 = ArrayAdapter.createFromResource(requireContext(),R.array.ageRange,android.R.layout.simple_spinner_item);
                        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        break;
                    case "Gender":
                        adapter1 = ArrayAdapter.createFromResource(requireContext(),R.array.gender,android.R.layout.simple_spinner_item);
                        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        break;
                    case "Distance Radius":
                        adapter1 = ArrayAdapter.createFromResource(requireContext(),R.array.distanceRadius,android.R.layout.simple_spinner_item);
                        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        break;

                }
                spinner2.setAdapter(adapter);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }
}