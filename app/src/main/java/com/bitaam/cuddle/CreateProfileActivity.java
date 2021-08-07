package com.bitaam.cuddle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.material.textfield.TextInputEditText;
import com.squareup.picasso.Picasso;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class CreateProfileActivity extends AppCompatActivity {

    AutoCompleteTextView genderExposedDropdown,hobby1ExposedDropdown,hobby2ExposedDropdown,hobby3ExposedDropdown;
    TextInputEditText bioEdt,nameEdt,ageEdt;
    CircleImageView addImagesCiv;
    CarouselView carouselView;
    Button submitBtn,addLocationBtn;
    SharedPreferences sharedPreferences;
    ArrayList<String> profileImgUrls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_profile);

        String[] type = new String[] {"Male", "Female", "Others"};
        String[] hobbies = new String[] {"Music", "Dance", "Cricket"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.dropdown_menu_popup_item, type);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this, R.layout.dropdown_menu_popup_item, hobbies);


        genderExposedDropdown = findViewById(R.id.filled_exposed_dropdown);
        genderExposedDropdown.setAdapter(adapter);
        hobby1ExposedDropdown = findViewById(R.id.hobby1_input);
        hobby1ExposedDropdown.setAdapter(adapter1);
        hobby2ExposedDropdown = findViewById(R.id.hobby2_input);
        hobby2ExposedDropdown.setAdapter(adapter1);
        hobby3ExposedDropdown = findViewById(R.id.hobby3_input);
        hobby3ExposedDropdown.setAdapter(adapter1);

        nameEdt = findViewById(R.id.name_input);
        ageEdt = findViewById(R.id.age_input);
        bioEdt = findViewById(R.id.bio_input);
        addImagesCiv = findViewById(R.id.addImages);
        submitBtn = findViewById(R.id.submitBtn);
        addLocationBtn = findViewById(R.id.addLocationBtn);

        addingImagesToCarousel();

        onClickActivities();

    }

    private void onClickActivities() {

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData();
            }
        });

        addLocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        addImagesCiv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    private void getData() {



    }

    private void addingImagesToCarousel() {

        profileImgUrls = new ArrayList<>();
        sharedPreferences = getSharedPreferences("ProfileImagesUrl", Context.MODE_PRIVATE);

        profileImgUrls.add(sharedPreferences.getString("img1",getString(R.string.profile_demo_url)));
        profileImgUrls.add(sharedPreferences.getString("img2",getString(R.string.profile_demo_url)));
        profileImgUrls.add(sharedPreferences.getString("img3",getString(R.string.profile_demo_url)));
        profileImgUrls.add(sharedPreferences.getString("img4",getString(R.string.profile_demo_url)));
        profileImgUrls.add(sharedPreferences.getString("img5",getString(R.string.profile_demo_url)));
        profileImgUrls.add(sharedPreferences.getString("img6",getString(R.string.profile_demo_url)));

        carouselView = findViewById(R.id.carouselView);
        carouselView.setPageCount(profileImgUrls.size());
        carouselView.setImageListener(imageListener);
    }

    ImageListener imageListener = new ImageListener() {
        @Override
        public void setImageForPosition(int position, ImageView imageView) {
            Picasso.get().load(profileImgUrls.get(position)).into(imageView);

        }
    };

}