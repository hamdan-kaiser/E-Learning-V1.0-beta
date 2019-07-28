package com.hamdan.virus.e_learning.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hamdan.virus.e_learning.ForgotPassword;
import com.hamdan.virus.e_learning.HomeActivity;
import com.hamdan.virus.e_learning.R;
import com.hamdan.virus.e_learning.starting.page.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginFinal extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private EditText email, password;
    private Button login;
    private TextView signup,forgotpass;
    private FirebaseAuth.AuthStateListener authStateListener; //authenticate users information

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_final);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        login = findViewById(R.id.login);
        signup = findViewById(R.id.signup);
        forgotpass = findViewById(R.id.forgotpass);

        firebaseAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(this);

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser() != null)
                {
                    //user has already signed in....
                    Intent i = new Intent(LoginFinal.this, HomeActivity.class);
                    startActivity(i);

                }
            }
        };


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSignIn();
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),Registration.class);
                startActivity(intent);
            }
        });

        forgotpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent intent = new Intent(getApplicationContext(),ForgotPassword.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener); //it will initialize page
        //in authStateListener if logged in
    }

    private void startSignIn() {
        String email1 = email.getText().toString();
        String pass = password.getText().toString();
        progressDialog.setMessage("Signing in");
        progressDialog.show();

        if (TextUtils.isEmpty(email1) || TextUtils.isEmpty(pass)) {
            progressDialog.dismiss();
            Toast.makeText(LoginFinal.this, "Please Fill Up Each Field", Toast.LENGTH_LONG).show();

        } else {
            firebaseAuth.signInWithEmailAndPassword(email1,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful())
                    {
                        progressDialog.dismiss();
                        isEmailVerified();
                    }
                    else
                    {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(),"Sign in Failed",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    public void isEmailVerified()
    {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser(); //firebase User e dhuikka
        //currentuser re trace korbe if thake
        Boolean emailSend = firebaseUser.isEmailVerified();

        if(emailSend)
        {
            // progressDialog.dismiss();
            finish();
            Intent i = new Intent(getApplicationContext(),HomeActivity.class);
            startActivity(i);
        }

        else
        {
            Toast.makeText(getApplicationContext(),"Go to your email and verify first",Toast.LENGTH_LONG).show();
            firebaseAuth.signOut();
            finish();
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }
    }
}
