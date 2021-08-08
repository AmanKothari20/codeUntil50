package com.bitaam.cuddle.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bitaam.cuddle.R;
import com.bitaam.cuddle.adapters.FeedAdapter;
import com.bitaam.cuddle.adapters.InterestedFeedAdapter;
import com.bitaam.cuddle.modals.ProfileModal;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Objects;

public class InterestedFragment extends Fragment {

    RecyclerView feedRecyclerView;
    InterestedFeedAdapter feedAdapter;
    ArrayList<ProfileModal> profileModals;
    ArrayList<String> profileKeys;
    FirebaseAuth mAuth;

    public InterestedFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_interested, container, false);

        feedRecyclerView = view.findViewById(R.id.interestedFeedRecycler);

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

    private void databaseActivities() {

        feedAdapter = new InterestedFeedAdapter(new ArrayList<>(),getContext(),feedRecyclerView);
        feedRecyclerView.setAdapter(feedAdapter);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Profiles");
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                if (snapshot.hasChild("likedBy") && snapshot.child("likedBy").hasChild(mAuth.getCurrentUser().getUid())){

                    ProfileModal modal = snapshot.getValue(ProfileModal.class);
                    profileModals.add(modal);
                    profileKeys.add(snapshot.getKey());
                    ((InterestedFeedAdapter) Objects.requireNonNull(feedRecyclerView.getAdapter())).update(modal);

                }

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

}