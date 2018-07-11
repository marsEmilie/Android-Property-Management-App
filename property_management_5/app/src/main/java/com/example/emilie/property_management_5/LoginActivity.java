package com.example.emilie.property_management_5;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    private EditText inputEmail;
    private EditText inputPassword;
    private Button signUpBtn, loginBtn;
    private TextView loginErrorMessage;
    ProgressDialog progressDialog;
    FirebaseAuth Auth;

    /* enteredEmail & enteredPassword is a holder to hold the values */
                /* getting value from inputEmail & inputPassword EditText and fill into the holder string variable */
    String enteredEmail;
    String enteredPassword;
    Boolean checkEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.hide();
        }

         /* assign ID to button */
        signUpBtn = (Button) findViewById(R.id.signUp_button_signUpPage);
        loginBtn = (Button) findViewById(R.id.login_button_signUpPage);

        loginErrorMessage = (TextView)findViewById(R.id.login_error);

        /* assign ID to EditText */
        inputEmail = (EditText)findViewById(R.id.email);
        inputPassword = (EditText)findViewById(R.id.password);

        progressDialog = new ProgressDialog(LoginActivity.this);

        /* Get firebase Auth instance */
        Auth = FirebaseAuth.getInstance();

        if(Auth.getCurrentUser() != null){
            /* finishing current login activity */
            finish();
            Intent i = new Intent(LoginActivity.this,MainActivity.class);
            startActivity(i);
        }

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Intent signUpIntent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(signUpIntent);
            }
        });


        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            checkEditText();
            if(checkEditText){
                LoginFunction();
            }else {
                Toast.makeText(LoginActivity.this, "Email and Password is not entered",Toast.LENGTH_LONG).show();
                return;
            }
            }
        });
    }

    /* check if the holder is empty */
    public void checkEditText() {
        enteredEmail = inputEmail.getText().toString();
        enteredPassword = inputPassword.getText().toString();
        if(TextUtils.isEmpty(enteredEmail)||TextUtils.isEmpty(enteredPassword)){
            checkEditText = false;
        }else{
            checkEditText = true;
        }
    }

    public void LoginFunction(){
        /* set up message and show */
        progressDialog.setMessage("Please wait...");
        progressDialog.show();

        /* Calling  signInWithEmailAndPassword() with firebase object and passing EmailHolder and PasswordHolder inside it. */
        Auth.signInWithEmailAndPassword(enteredEmail,enteredPassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {


                        if(task.isSuccessful()){
                            progressDialog.dismiss();
                            finish();

                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                        }
                        else {
                            progressDialog.dismiss();
                            Toast.makeText(LoginActivity.this,"Email or Password not found, Please try again.",Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}