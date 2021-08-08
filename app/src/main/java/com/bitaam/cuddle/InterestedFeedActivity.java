package com.bitaam.cuddle;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;

import com.bitaam.cuddle.modals.ProfileModal;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class InterestedFeedActivity extends AppCompatActivity {

    FloatingActionButton chatActioBtn;
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
    ProfileModal modal;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interested_feed);

        Intent intent = getIntent();
        modal = (ProfileModal)intent.getSerializableExtra("profileInfo") ;

        chatActioBtn = findViewById(R.id.chatActionBtnfeed);

        chatActioBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),ChatActivity.class));
            }
        });

        auth = FirebaseAuth.getInstance();
        profileImgUrls = new ArrayList<>();
        selectedImagesBitmaps = new ArrayList<>();
        imgUriList = new ArrayList<>();

        nameEdt = findViewById(R.id.name_input);
        ageEdt = findViewById(R.id.age_input);
        bioEdt = findViewById(R.id.bio_input);
        submitBtn = findViewById(R.id.submitBtn);
        addLocationBtn = findViewById(R.id.addLocationBtn);
        imageView = findViewById(R.id.profImgView);
        rotateImgCiv = findViewById(R.id.rotateImgView);
        changeImgCiv = findViewById(R.id.changeImgView);
        genderExposedDropdown = findViewById(R.id.filled_exposed_dropdown);
        hobby1ExposedDropdown = findViewById(R.id.hobby1_input);
        hobby2ExposedDropdown = findViewById(R.id.hobby2_input);
        hobby3ExposedDropdown = findViewById(R.id.hobby3_input);

        if (modal!=null){
            updateData(modal);
        }

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