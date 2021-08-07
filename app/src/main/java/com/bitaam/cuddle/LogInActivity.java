package com.bitaam.cuddle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.Objects;

public class LogInActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    TextInputEditText emailEdt,passwordEdt;
    Button signInBtn,googleSignInBtn,signUpBtn;
    ProgressDialog progressDialog;
    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 9001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.oauth_client))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


        mAuth = FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser()!=null){
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            finish();
        }

        emailEdt = findViewById(R.id.email_input);
        passwordEdt = findViewById(R.id.password_input);
        signInBtn = findViewById(R.id.LoginBtn);
        googleSignInBtn = findViewById(R.id.signin_google_btn);
        signUpBtn = findViewById(R.id.signupBtn);

        onClickActivities();

    }

    private void onClickActivities() {

        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(signInBtn.getText().toString().equals("Sign Up")){
                    progressDialogShow("Creating User","Please wait..");
                    signUp();

                }
                else{
                    progressDialogShow("Signing In","Please wait..");
                    loginEmailPassword();
                }
            }
        });

        googleSignInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialogShow("Signing with google","getting accounts");
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(signUpBtn.getText().toString().equals("Create Account")){
                    signInBtn.setText("Sign Up");
                    signUpBtn.setText("Log In");
                }
                else{
                    signInBtn.setText("LOG IN");
                }

            }
        });

    }

    private void signUp() {
        String useremail = Objects.requireNonNull(emailEdt.getText()).toString().trim();
        String userpassword = Objects.requireNonNull(passwordEdt.getText()).toString().trim();

        if(useremail.isEmpty()){
            emailEdt.setError("Enter email");
            emailEdt.requestFocus();
            progressDialog.dismiss();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(useremail).matches()){
            emailEdt.setError("Please enter valid email");
            emailEdt.requestFocus();
            progressDialog.dismiss();
            return;
        }

        if(userpassword.isEmpty()){

            passwordEdt.setError("Password is Empty");
            passwordEdt.requestFocus();
            progressDialog.dismiss();
            return;
        }
        if(userpassword.length()<6){
            passwordEdt.setError("Minimum length is 6");
            passwordEdt.requestFocus();
            progressDialog.dismiss();
            return;
        }

        mAuth.createUserWithEmailAndPassword(useremail,userpassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){

                    progressDialog.dismiss();
                    Intent intent = new Intent(getApplicationContext(),CreateProfileActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }else{
                    Toast.makeText(getApplicationContext(), Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                    emailEdt.requestFocus();
                    progressDialog.dismiss();
                }
            }
        });
    }

    private void loginEmailPassword() {
        String useremail = Objects.requireNonNull(emailEdt.getText()).toString().trim();
        String userpassword = Objects.requireNonNull(passwordEdt.getText()).toString().trim();

        if(useremail.isEmpty()){

            emailEdt.setError("Enter email");
            emailEdt.requestFocus();
            progressDialog.dismiss();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(useremail).matches()){
            emailEdt.setError("Please enter valid email");
            emailEdt.requestFocus();
            progressDialog.dismiss();
            return;
        }

        if(userpassword.isEmpty()){

            passwordEdt.setError("Password is Empty");
            passwordEdt.requestFocus();
            progressDialog.dismiss();
            return;
        }

        if(userpassword.length()<6){
            passwordEdt.setError("Minimum length is 6");
            passwordEdt.requestFocus();
            progressDialog.dismiss();
            return;
        }


        mAuth.signInWithEmailAndPassword(useremail,userpassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){

                    progressDialog.dismiss();
                    Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }else{
                    Toast.makeText(getApplicationContext(), Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                    emailEdt.requestFocus();
                    progressDialog.dismiss();
                }

            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                //Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
            }
        }

    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            progressDialog.dismiss();
                            startActivity(new Intent(getApplicationContext(),MainActivity.class));
                            finish();
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(LogInActivity.this, "Signing failed with google account", Toast.LENGTH_SHORT).show();
                        }
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