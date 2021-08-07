package com.bitaam.cuddle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bitaam.cuddle.modals.ProfileModal;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class CreateProfileActivity extends AppCompatActivity {

    AutoCompleteTextView genderExposedDropdown,hobby1ExposedDropdown,hobby2ExposedDropdown,hobby3ExposedDropdown;
    TextInputEditText bioEdt,nameEdt,ageEdt;
    CircleImageView rotateImgCiv;
    ImageView imageView;
    Button submitBtn,addLocationBtn;
    ArrayList<String> profileImgUrls;
    ArrayList<Bitmap> selectedImagesBitmaps;
    byte[] filesInBytes;
    ArrayList<Uri> imgUriList;
    Bitmap qImg;

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
        submitBtn = findViewById(R.id.submitBtn);
        addLocationBtn = findViewById(R.id.addLocationBtn);
        imageView = findViewById(R.id.profImgView);
        rotateImgCiv = findViewById(R.id.rotateImgView);

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

        TextView i1Tv,i2Tv,i3Tv,i4Tv,i5Tv,i6Tv;
        i1Tv = findViewById(R.id.addImage1Tv);
        i2Tv = findViewById(R.id.addImage2Tv);
        i3Tv = findViewById(R.id.addImage3Tv);
        i4Tv = findViewById(R.id.addImage4Tv);
        i5Tv = findViewById(R.id.addImage5Tv);
        i6Tv = findViewById(R.id.addImage6Tv);

        i1Tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent,1000);
                i2Tv.setEnabled(true);
            }
        });

        i2Tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent,1000);
                i3Tv.setEnabled(true);
            }
        });

        i3Tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent,1000);
                i4Tv.setEnabled(true);
            }
        });

        i4Tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent,1000);
                i5Tv.setEnabled(true);
            }
        });

        i5Tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent,1000);
                i6Tv.setEnabled(true);
            }
        });

        i6Tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent,1000);
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

        if(requestCode == 1000){
            if(resultCode == Activity.RESULT_OK){
                Uri imgUri = data.getData();
                Bitmap bmp = null;
                try{
                    bmp = MediaStore.Images.Media.getBitmap(getContentResolver(),imgUri);
                    qImg = bmp;
                }catch (Exception e){}
//                ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                bmp.compress(Bitmap.CompressFormat.JPEG,25,baos);
//                byte[] fileInBytes = baos.toByteArray();
                filesInBytes = compressImg(qImg);
                rotateImgCiv.setVisibility(View.VISIBLE);
                imageView.setImageBitmap(qImg);

                //uploadImageToFirebase(fileInBytes);
            }
        }
    }

    private Bitmap rotateImage(Bitmap img, int degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        Bitmap rotatedImg = Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, true);
        filesInBytes = null;
        filesInBytes = compressImg(rotatedImg);
        imageView.setImageBitmap(rotatedImg);
        return rotatedImg;
    }

    private byte[] compressImg(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG,25,baos);
        byte[] fileInBytes = baos.toByteArray();
        //filesInByte = baos.toByteArray();
        return fileInBytes;
    }

    private void uploadImageToFirebase(byte[] fileInBytes) {

        //progressBar.setVisibility(View.VISIBLE);

        String imgId = new SimpleDateFormat("MMMM dd, yyyy HH:mm:ss", Locale.getDefault()).format(new Date())+""+ FirebaseAuth.getInstance().getCurrentUser().getUid().toString()+".jpg";
        final StorageReference fileRef = FirebaseStorage.getInstance().getReference("Images").child(imgId);
        fileRef.putBytes(fileInBytes).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(getApplicationContext(),"Image Uploaded",Toast.LENGTH_SHORT).show();
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        profileImgUrls.add(uri.toString());
                        //postQuestion();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),"Some error occurred while uploading image ,try again !",Toast.LENGTH_SHORT).show();
            }
        });
    }

}