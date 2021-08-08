package com.bitaam.cuddle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.jetbrains.annotations.NotNull;

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
        changeImgCiv = findViewById(R.id.changeImgView);

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

        final boolean[] f1 = {false};
        final boolean[] f2 = {false};
        final boolean[] f3 = {false};
        final boolean[] f4 = {false};
        final boolean[] f5 = {false};
        final boolean[] f6 = {false};

        i1Tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgPos = 0;
                if (f1[0]){
                    imageView.setImageBitmap(selectedImagesBitmaps.get(imgPos));
                }else {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent,1000);
                    i2Tv.setEnabled(true);
                    f1[0] = true;
                }
            }
        });

        i2Tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgPos = 1;
                if (f2[0]){
                    imageView.setImageBitmap(selectedImagesBitmaps.get(imgPos));
                }else {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent,1000);
                    i3Tv.setEnabled(true);
                    f2[0] = true;
                }
            }
        });

        i3Tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgPos = 2;
                if (f3[0]){
                    imageView.setImageBitmap(selectedImagesBitmaps.get(imgPos));
                }else {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent,1000);
                    i4Tv.setEnabled(true);
                    f3[0] = true;
                }
            }
        });

        i4Tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgPos = 3;
                if (f4[0]){
                    imageView.setImageBitmap(selectedImagesBitmaps.get(imgPos));
                }else {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent,1000);
                    i5Tv.setEnabled(true);
                    f4[0] = true;
                }
            }
        });

        i5Tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgPos = 4;
                if (f5[0]){
                    imageView.setImageBitmap(selectedImagesBitmaps.get(imgPos));
                }else {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent,1000);
                    i6Tv.setEnabled(true);
                    f5[0] = true;
                }
            }
        });

        i6Tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgPos = 5;
                if (f6[0]){
                    imageView.setImageBitmap(selectedImagesBitmaps.get(imgPos));
                }else {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent,1000);
                    f6[0] = true;
                }
            }
        });

        rotateImgCiv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedImagesBitmaps.add(imgPos,rotateImage(selectedImagesBitmaps.get(imgPos),90));
            }
        });

        changeImgCiv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent,1000);
            }
        });

    }

    private void getData() {
        progressDialogShow("Creating Profile","Uploading images and information");


        if (selectedImagesBitmaps.size()<6){
            Toast.makeText(this, "Select all six images", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
            return;
        }
        for (int i=0;i<6;i++){
            uploadImageToFirebase(compressImg(selectedImagesBitmaps.get(i)),i+1);
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
                    selectedImagesBitmaps.add(imgPos,bmp);
                }catch (Exception e){}

                rotateImgCiv.setVisibility(View.VISIBLE);
                changeImgCiv.setVisibility(View.VISIBLE);
                imageView.setImageBitmap(qImg);

                //uploadImageToFirebase(fileInBytes);
            }
        }
    }

    private Bitmap rotateImage(Bitmap img, int degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        Bitmap rotatedImg = Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, true);
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

    private void uploadImageToFirebase(byte[] fileInBytes,int c) {

        //progressBar.setVisibility(View.VISIBLE);

        String imgId = System.currentTimeMillis()+""+ FirebaseAuth.getInstance().getCurrentUser().getUid().toString()+".jpg";
        final StorageReference fileRef = FirebaseStorage.getInstance().getReference("Images").child(imgId);
        fileRef.putBytes(fileInBytes).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(getApplicationContext(),c+" Image Uploaded",Toast.LENGTH_SHORT).show();
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        profileImgUrls.add(uri.toString());
                        if (c == 6){
                            updateData();
                        }
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),"Some error occurred while uploading image ,try again !",Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }

    private void updateData() {

        ProfileModal modal = new ProfileModal();
        String authId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

        String poDat = new SimpleDateFormat("MMMM dd, yyyy HH:mm:ss", Locale.getDefault()).format(new Date());
        modal.setProfileDate(poDat);
        modal.setLocation("Jaunpur");

        String name,age,bio,hobby1,hobby2,hobby3,location,gender,imgUrl1="na",imgUrl2="na",imgUrl3="na",imgUrl4="na",imgUrl5="na",imgUrl6="na";

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
            progressDialog.dismiss();
            return;
        }else {
            modal.setName(name);
        }
        if (age.isEmpty()){
            ageEdt.setError("Enter age");
            ageEdt.requestFocus();
            progressDialog.dismiss();
            return;
        }else {
            modal.setAge(age);
        }
        if (bio.isEmpty()){
            bioEdt.setError("Enter bio");
            bioEdt.requestFocus();
            progressDialog.dismiss();
            return;
        }else {
            modal.setBio(bio);
        }
        if (gender.isEmpty()){
            genderExposedDropdown.setError("Enter gender");
            genderExposedDropdown.requestFocus();
            progressDialog.dismiss();
            return;
        }else {
            modal.setGender(gender);
        }
        if (hobby1.isEmpty()){
            hobby1ExposedDropdown.setError("Enter 1st hobby");
            hobby1ExposedDropdown.requestFocus();
            progressDialog.dismiss();
            return;
        }else {
            modal.setHobby1(hobby1);
        }
        if (hobby2.isEmpty()){
            hobby2ExposedDropdown.setError("Enter 2nd hobby");
            hobby2ExposedDropdown.requestFocus();
            progressDialog.dismiss();
            return;
        }else {
            modal.setHobby2(hobby2);
        }
        if (hobby3.isEmpty()){
            hobby3ExposedDropdown.setError("Enter 3rd hobby");
            hobby3ExposedDropdown.requestFocus();
            progressDialog.dismiss();
            return;
        }else {
            modal.setHobby3(hobby3);
        }

        for (int i=0;i<profileImgUrls.size();i++){
            switch (i){
                case 0:
                    imgUrl1 = profileImgUrls.get(i);
                    modal.setImgUrl1(imgUrl1);
                    break;
                case 1:
                    imgUrl2 = profileImgUrls.get(i);
                    modal.setImgUrl2(imgUrl2);
                    break;
                case 2:
                    imgUrl3 = profileImgUrls.get(i);
                    modal.setImgUrl3(imgUrl3);
                    break;
                case 3:
                    imgUrl4 = profileImgUrls.get(i);
                    modal.setImgUrl4(imgUrl4);
                    break;
                case 4:
                    imgUrl5 = profileImgUrls.get(i);
                    modal.setImgUrl5(imgUrl5);
                    break;
                case 5:
                    imgUrl6 = profileImgUrls.get(i);
                    modal.setImgUrl6(imgUrl6);
                    break;

            }
            if (i == profileImgUrls.size()-1){
                uploadToDatabase(modal,authId);
            }
        }

    }

    private void uploadToDatabase(ProfileModal modal,String key) {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Profiles");

        databaseReference.child(key).setValue(modal).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                progressDialog.dismiss();
                Toast.makeText(CreateProfileActivity.this, "Profile Created Successfully", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(CreateProfileActivity.this, "Some error occurred try again", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void progressDialogShow(String title,String Msg){

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle(title);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(Msg);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        //progressDialog.setMax(100);
        progressDialog.show();

    }

}