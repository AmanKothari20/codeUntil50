package com.bitaam.cuddle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bitaam.cuddle.modals.ProfileModal;
import com.google.android.material.textfield.TextInputEditText;
import com.squareup.picasso.Picasso;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class CreateProfileActivity extends AppCompatActivity {

    AutoCompleteTextView genderExposedDropdown,hobby1ExposedDropdown,hobby2ExposedDropdown,hobby3ExposedDropdown;
    TextInputEditText bioEdt,nameEdt,ageEdt;
    CircleImageView addImagesCiv;
    CarouselView carouselView;
    Button submitBtn,addLocationBtn;
    SharedPreferences sharedPreferences;
    ArrayList<String> profileImgUrls;
    ArrayList<Bitmap> selectedImagesBitmaps;
    Byte[] filesInBytes;
    ArrayList<Uri> imgUriList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_profile);

        profileImgUrls = new ArrayList<>();
        selectedImagesBitmaps = new ArrayList<>();
        imgUriList = new ArrayList<>();

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
        addImagesCiv = findViewById(R.id.addImage1);
        submitBtn = findViewById(R.id.submitBtn);
        addLocationBtn = findViewById(R.id.addLocationBtn);

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

        ProfileModal modal = new ProfileModal();

        String name,age,bio,hobby1,hobby2,hobby3,location,gender,imgUrl1,imgUrl2,imgUrl3,imgUrl4,imgUrl5,imgUrl6;

        name = Objects.requireNonNull(nameEdt.getText()).toString();
        age = Objects.requireNonNull(ageEdt.getText()).toString();
        bio = Objects.requireNonNull(bioEdt.getText()).toString();
        gender = Objects.requireNonNull(genderExposedDropdown.getText()).toString();
        hobby1 = Objects.requireNonNull(hobby1ExposedDropdown.getText()).toString();
        hobby2 = Objects.requireNonNull(hobby2ExposedDropdown.getText()).toString();
        hobby3 = Objects.requireNonNull(hobby3ExposedDropdown.getText()).toString();

        if (name.isEmpty()){
            nameEdt.setError("Enter name");
            nameEdt.requestFocus();
            return;
        }
        if (age.isEmpty()){
            ageEdt.setError("Enter age");
            ageEdt.requestFocus();
            return;
        }
        if (bio.isEmpty()){
            bioEdt.setError("Enter bio");
            bioEdt.requestFocus();
            return;
        }
        if (gender.isEmpty()){
            genderExposedDropdown.setError("Enter gender");
            genderExposedDropdown.requestFocus();
            return;
        }
        if (hobby1.isEmpty()){
            hobby1ExposedDropdown.setError("Enter 1st hobby");
            hobby1ExposedDropdown.requestFocus();
            return;
        }
        if (hobby2.isEmpty()){
            hobby2ExposedDropdown.setError("Enter 2nd hobby");
            hobby2ExposedDropdown.requestFocus();
            return;
        }
        if (hobby3.isEmpty()){
            hobby3ExposedDropdown.setError("Enter 3rd hobby");
            hobby3ExposedDropdown.requestFocus();
            return;
        }



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == 1000) {


            assert data != null;
            if (data.getClipData() != null) {

                int countClipData = data.getClipData().getItemCount();
                int currentImageSlect = 0;

                while (currentImageSlect < countClipData && currentImageSlect<6) {

                    Uri imgUri = data.getClipData().getItemAt(currentImageSlect).getUri();
                    imgUriList.add(imgUri);
                    getImageFromStorage(imgUri);
                    currentImageSlect = currentImageSlect + 1;
                }


            }else
                Toast.makeText(this, "Please Select at least 2 Images", Toast.LENGTH_SHORT).show();
            }

        }


    private void getImageFromStorage(Uri uri){

        Bitmap bmp = null;
        try{
            bmp = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
            selectedImagesBitmaps.add(bmp);
//            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//            bmp.compress(Bitmap.CompressFormat.JPEG,25,baos);
//            byte[] fileInBytes = baos.toByteArray();
            //uploadImageToFirebase(fileInBytes);
        }catch (Exception e){}


    }


    ImageListener imageListener = new ImageListener() {
        @Override
        public void setImageForPosition(int position, ImageView imageView) {
            //Picasso.get().load(profileImgUrls.get(position)).into(imageView);
            imageView.setImageBitmap(selectedImagesBitmaps.get(position));

        }
    };

}