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
import android.widget.Toast;

import com.bitaam.cuddle.CreateProfileActivity;
import com.bitaam.cuddle.R;
import com.bitaam.cuddle.adapters.FeedAdapter;
import com.bitaam.cuddle.modals.ProfileModal;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Objects;

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

    @Override
    public void onStart() {
        super.onStart();
        databaseActivities();
    }

    private void databaseActivities(){

        feedAdapter = new FeedAdapter(new ArrayList<>(),getContext(),feedRecyclerView);
        feedRecyclerView.setAdapter(feedAdapter);

        feedAdapter.setOnItemClickListener(new FeedAdapter.OnItemClickListener() {
            @Override
            public void onLiked(ProfileModal model,int pos) {
                onProfileLiked(model,pos);
            }

            @Override
            public void onDisliked(ProfileModal model,int pos) {
                onProfileDisliked(model,pos);
            }
        });

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Profiles");
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                int likes=0,dislikes=0;
                if (snapshot.hasChild("likedBy")){
                    likes = (int) snapshot.child("dislikedBy").getChildrenCount();
                }
                if (snapshot.hasChild("likedBy")){
                    dislikes = (int) snapshot.child("likedBy").getChildrenCount();
                }
                ProfileModal modal = snapshot.getValue(ProfileModal.class);
                profileModals.add(modal);
                profileKeys.add(snapshot.getKey());
                ((FeedAdapter) Objects.requireNonNull(feedRecyclerView.getAdapter())).update(modal,likes,dislikes);
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

    private void onProfileDisliked(ProfileModal model, int pos) {
        downvoting(profileKeys.get(pos));
    }

    private void onProfileLiked(ProfileModal model, int pos) {

        upvoting(profileKeys.get(pos));

    }

    private void upvoting(String pId) {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Profiles").child(pId).child("likedBy")
                .child(Objects.requireNonNull(mAuth.getCurrentUser()).getUid());


        databaseReference.setValue("liked").addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getContext(), "Liked ,Added to Interested", Toast.LENGTH_SHORT).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                Toast.makeText(getContext(), "Error Try again", Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void downvoting(String pId) {



        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Profiles").child(pId).child("dislikedBy")
                .child(Objects.requireNonNull(mAuth.getCurrentUser()).getUid());


        databaseReference.setValue("disliked").addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getContext(), "Disliked feed", Toast.LENGTH_SHORT).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                Toast.makeText(getContext(), "Error Try again", Toast.LENGTH_SHORT).show();

            }
        });

    }


    private void spinnerInit() {

        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(requireContext(),R.array.feedTypes,android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter1);

        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ArrayAdapter<CharSequence> adapter2;
                switch (parent.getItemAtPosition(position).toString()){

                    case "Age":
                        adapter2 = ArrayAdapter.createFromResource(requireContext(),R.array.ageRange,android.R.layout.simple_spinner_item);
                        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner2.setAdapter(adapter2);
                        break;
                    case "Gender":
                        adapter2 = ArrayAdapter.createFromResource(requireContext(),R.array.gender,android.R.layout.simple_spinner_item);
                        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner2.setAdapter(adapter2);
                        break;
                    case "Distance Radius":
                        adapter2 = ArrayAdapter.createFromResource(requireContext(),R.array.distanceRadius,android.R.layout.simple_spinner_item);
                        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner2.setAdapter(adapter2);
                        break;

                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }
}