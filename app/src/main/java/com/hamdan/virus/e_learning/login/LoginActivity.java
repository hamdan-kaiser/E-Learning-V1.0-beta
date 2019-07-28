package com.hamdan.virus.e_learning.login;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.hamdan.virus.e_learning.ForgotPassword;
import com.hamdan.virus.e_learning.utils.AllStaticNames;
import com.hamdan.virus.e_learning.R;
import com.hamdan.virus.e_learning.SendInteltodb;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static com.hamdan.virus.e_learning.utils.AllStaticNames.isUserLoggedIn;
import static com.hamdan.virus.e_learning.utils.AllStaticNames.sharedTable;
import static com.hamdan.virus.e_learning.utils.AllStaticNames.sharedUserEmail;
import static com.hamdan.virus.e_learning.utils.AllStaticNames.sharedUserName;
import static com.hamdan.virus.e_learning.utils.AllStaticNames.sharedUserOccupation;
import static com.hamdan.virus.e_learning.utils.AllStaticNames.userEmail;
import static com.hamdan.virus.e_learning.utils.utils.getIdFromEmail;
import static com.hamdan.virus.e_learning.utils.utils.showToast;

public class LoginActivity extends AppCompatActivity {

    private TextView forgotPass;
    EditText emailText, passText;
    RadioGroup radioGroup;
    RadioButton teacherBtn, studentBtn;
    Button loginBtn, signUpBtn;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener; //authenticate users information
    private DatabaseReference databaseReference;
    FirebaseUser user;

    private ProgressDialog progressDialog;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String occupation = "teacher", myEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        progressDialog = new ProgressDialog(this);

        sharedPreferences = LoginActivity.this.getSharedPreferences("LoginData", MODE_PRIVATE);

        forgotPass = findViewById(R.id.forgotPass);
        emailText = findViewById(R.id.emailText);
        passText = findViewById(R.id.passText);
        radioGroup = findViewById(R.id.occupationGroup);
        teacherBtn = findViewById(R.id.teacherOption);
        studentBtn = findViewById(R.id.studentOption);
        loginBtn = findViewById(R.id.loginBtn);
        signUpBtn = findViewById(R.id.signUp);


        //User Signup
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, Registration.class);
                startActivity(intent);
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startLogin();
            }
        });


        //radio button selection
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.teacherOption: {
                        occupation = "teacher";
                        break;
                    }
                    case R.id.studentOption: {
                        occupation = "student";
                        break;
                    }
                }
            }
        });
        forgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),ForgotPassword.class);
                startActivity(intent);
            }
        });
    }

    private void startLogin() {
        myEmail = emailText.getText().toString().trim();
        String pass = passText.getText().toString().trim();
        progressDialog.setMessage("Signing in...");
        progressDialog.show();

        if (TextUtils.isEmpty(myEmail) || TextUtils.isEmpty(pass)) {
            Toast.makeText(LoginActivity.this, "Please Fill Up Each Field", Toast.LENGTH_LONG).show();

        } else {

            //check for the authentication into firebase...

            firebaseAuth.signInWithEmailAndPassword(myEmail, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()) {

                        isEmailVerified();
                    }
                    else {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(),"Sign in Failed", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    //if account exists into firebase, it traces the user

    public void isEmailVerified() {
        user = FirebaseAuth.getInstance().getCurrentUser(); //firebase User e dhuikka
        //currentuser re trace korbe if thake
        assert user != null;
        Boolean emailVerified = user.isEmailVerified();

        if(emailVerified) {
            // progressDialog.dismiss();


            getDataFromFirebase();
        }

        else {
            progressDialog.dismiss();
            Toast.makeText(getApplicationContext(),"Go to your email and verify first", Toast.LENGTH_LONG).show();
            firebaseAuth.signOut();
        }
    }


    //collecting user information and data stored in firebase
    private void getDataFromFirebase() {
        databaseReference.child(AllStaticNames.fbUserDetails)
                .child(getIdFromEmail(myEmail)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Log.e("$$$", "" + dataSnapshot);

                String name = "", email = "", password = "", occup = "";

                SendInteltodb sdb = dataSnapshot.getValue(SendInteltodb.class);

                if (sdb != null) {
                    name = sdb.getUserName();
                    email = sdb.getUserEmail();
                    occup = sdb.getOccupation();
                    Log.e(">>>>>", "" + occup);
                    Log.e("###", "" + occupation);
                }


                //radio button info checking
                if (occup.equals(occupation)) {

                    sharedPreferences = LoginActivity.this.getSharedPreferences(sharedTable, MODE_PRIVATE);
                    editor = sharedPreferences.edit();
                    editor.putBoolean(isUserLoggedIn, true);
                    editor.putString(sharedUserName, name);
                    editor.putString(sharedUserEmail, email);
                    editor.putString(sharedUserOccupation, occupation);
                    editor.apply();

                    progressDialog.dismiss();

                    finish();

                } else {
                    progressDialog.dismiss();
                    showToast(LoginActivity.this, "Wrong Occupation");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                progressDialog.dismiss();

                //Show Alert Dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                builder.setTitle("Error!")
                        .setMessage("Error while retrieving user data.\nPlease Log in again")
                        .setCancelable(false)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        }).show();

            }
        });
    }
}
