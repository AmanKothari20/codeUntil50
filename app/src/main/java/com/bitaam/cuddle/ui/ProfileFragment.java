package com.bitaam.cuddle.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;

import com.bitaam.cuddle.LogInActivity;
import com.bitaam.cuddle.R;
import com.bitaam.cuddle.adapters.InterestedFeedAdapter;
import com.bitaam.cuddle.modals.ProfileModal;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {

    FirebaseAuth auth;
    ProfileModal profileModal;
    AutoCompleteTextView genderExposedDropdown,hobby1ExposedDropdown,hobby2ExposedDropdown,hobby3ExposedDropdown;
    TextInputEditText bioEdt,nameEdt,ageEdt;
    CircleImageView rotateImgCiv,changeImgCiv;
    ImageView imageView;
    Button submitBtn,addLocationBtn;
    ArrayList<String> profileImgUrls;
    ArrayList<Bitmap> selectedImagesBitmaps;
    byte[] filesInBytes;
    ArrayList<Uri> imgUriList;
    Bitmap qImg;
    int imgPos= 0;
    ProgressDialog progressDialog;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        auth = FirebaseAuth.getInstance();
        profileImgUrls = new ArrayList<>();
        selectedImagesBitmaps = new ArrayList<>();
        imgUriList = new ArrayList<>();

        nameEdt = view.findViewById(R.id.name_input);
        ageEdt = view.findViewById(R.id.age_input);
        bioEdt = view.findViewById(R.id.bio_input);
        submitBtn = view.findViewById(R.id.submitBtn);
        addLocationBtn = view.findViewById(R.id.addLocationBtn);
        imageView = view.findViewById(R.id.profImgView);
        rotateImgCiv = view.findViewById(R.id.rotateImgView);
        changeImgCiv = view.findViewById(R.id.changeImgView);
        genderExposedDropdown = view.findViewById(R.id.filled_exposed_dropdown);
        hobby1ExposedDropdown = view.findViewById(R.id.hobby1_input);
        hobby2ExposedDropdown = view.findViewById(R.id.hobby2_input);
        hobby3ExposedDropdown = view.findViewById(R.id.hobby3_input);

        Button btn = view.findViewById(R.id.logoutBtn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
                startActivity(new Intent(getContext(), LogInActivity.class));
                getActivity().finish();
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        databaseActivities();
    }

    private void databaseActivities() {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Profiles").child(auth.getCurrentUser().getUid());

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                profileModal = snapshot.getValue(ProfileModal.class);
                updateData(profileModal);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

    }

    private void updateData(ProfileModal profileModal) {

        nameEdt.setText(profileModal.getName());
        ageEdt.setText(profileModal.getAge());
        bioEdt.setText(profileModal.getBio());
        genderExposedDropdown.setText(profileModal.getGender());
        hobby1ExposedDropdown.setText(profileModal.getHobby1());
        hobby2ExposedDropdown.setText(profileModal.getHobby2());
        hobby3ExposedDropdown.setText(profileModal.getHobby3());
        Picasso.get().load(profileModal.getImgUrl1()).into(imageView);

    }

}