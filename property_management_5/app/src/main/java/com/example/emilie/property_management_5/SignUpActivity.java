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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity extends AppCompatActivity {

    private EditText inputEmail_signUpPage;
    private EditText inputPassword_signUpPage;

    private Button signUpBtn_signUpPage, loginBtn_signUpPage;
    ProgressDialog progressDialog;
    FirebaseAuth AuthS;

    /* enteredEmail & enteredPassword is a holder to hold the values */
                /* getting value from inputEmail & inputPassword EditText and fill into the holder string variable */
    String enteredEmail_signUpPage;
    String enteredPassword_signUpPage;

    Boolean checkEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.hide();
        }

         /* assign ID to button */
        signUpBtn_signUpPage = (Button) findViewById(R.id.signUp_button_signUpPage);
        loginBtn_signUpPage = (Button) findViewById(R.id.login_button_signUpPage);


        /* assign ID to EditText */
        inputEmail_signUpPage = (EditText)findViewById(R.id.email_signUpPage);
        inputPassword_signUpPage = (EditText)findViewById(R.id.password_signUpPage);

        progressDialog = new ProgressDialog(SignUpActivity.this);

        /* Auth = ((FirebaseApplication)getApplication()).getFirebaseAuth();
        ((FirebaseApplication)getApplication()).checkUserLogin(LoginActivity.this); */
        /* Get firebase Auth instance */
        AuthS = FirebaseAuth.getInstance();

        loginBtn_signUpPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Intent LoginIntent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(LoginIntent);
            }
        });

        signUpBtn_signUpPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkEditText();

                if(checkEditText){
                    SignUpFunction();
                }else {
                    Toast.makeText(SignUpActivity.this, "Email and Password is not entered",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    /* check if the holder is empty */
    public void checkEditText() {
        enteredEmail_signUpPage = inputEmail_signUpPage.getText().toString();
        enteredPassword_signUpPage = inputPassword_signUpPage.getText().toString();
        if(TextUtils.isEmpty(enteredEmail_signUpPage)||TextUtils.isEmpty(enteredPassword_signUpPage)){
            checkEditText = false;
        }else{
            checkEditText = true;
        }
    }

    public void SignUpFunction(){
        /* set up message and show */
        progressDialog.setMessage("Please wait... Signing Up...");
        progressDialog.show();


        if(!enteredEmail_signUpPage.contains("@")){
            progressDialog.dismiss();
            Toast.makeText(SignUpActivity.this,"Please enter a valid password",Toast.LENGTH_LONG).show();
        }

        if(enteredPassword_signUpPage.length() < 6){
            progressDialog.dismiss();
            Toast.makeText(SignUpActivity.this,"Password must be more than 6 characters",Toast.LENGTH_LONG).show();
        }

        /* Calling  signInWithEmailAndPassword() with firebase object and passing EmailHolder and PasswordHolder inside it. */
        AuthS.createUserWithEmailAndPassword(enteredEmail_signUpPage,enteredPassword_signUpPage)
                .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){

                            Toast.makeText(SignUpActivity.this,"Sign Up successfully, Please Login",Toast.LENGTH_LONG).show();
                            AuthS.signOut();
                            /* Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                            startActivity(intent); */
                        }
                        else {
                            Toast.makeText(SignUpActivity.this,"Sign up failed, Please try again.",Toast.LENGTH_LONG).show();
                        }
                        progressDialog.dismiss();
                    }
                });
    }
}