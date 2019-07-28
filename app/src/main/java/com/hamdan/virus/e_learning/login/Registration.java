package com.hamdan.virus.e_learning.login;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.hamdan.virus.e_learning.utils.AllStaticNames;
import com.hamdan.virus.e_learning.R;
import com.hamdan.virus.e_learning.SendInteltodb;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static com.hamdan.virus.e_learning.utils.AllStaticNames.fbUserDetails;
import static com.hamdan.virus.e_learning.utils.AllStaticNames.userEmail;
import static com.hamdan.virus.e_learning.utils.AllStaticNames.userName;
import static com.hamdan.virus.e_learning.utils.AllStaticNames.userPassword;
import static com.hamdan.virus.e_learning.utils.utils.getIdFromEmail;


public class Registration extends AppCompatActivity {

    private EditText userNameText,email,password,conpassword;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    private CheckBox box;

    RadioGroup radioGroup;
    RadioButton teacherButton, studentButton;
    String occupation = "teacher";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        firebaseAuth = FirebaseAuth.getInstance();
        userNameText = findViewById(R.id.usernameText);
        email = findViewById(R.id.emailText);
        password = findViewById(R.id.passwordText);
        conpassword = findViewById(R.id.conpassText);
        Button signup = findViewById(R.id.signupButton);
        //TextView login1 = findViewById(R.id.loginText);
        box = findViewById(R.id.checkBox);

        radioGroup = findViewById(R.id.occupationGroup);
        teacherButton = findViewById(R.id.teacherOption);
        studentButton = findViewById(R.id.studentOption);

        progressDialog = new ProgressDialog(this);

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

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setMessage("Please Wait");
                progressDialog.show();

                userEmail = email.getText().toString().trim();
                userPassword = password.getText().toString().trim();
                String conpass_link = conpassword.getText().toString().trim();
                AllStaticNames.userName = userNameText.getText().toString();

                if(TextUtils.isEmpty(userEmail) || TextUtils.isEmpty(userPassword)
                        || TextUtils.isEmpty(conpass_link) || TextUtils.isEmpty((AllStaticNames.userName)) ) {

                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(),"All fields are required!",Toast.LENGTH_SHORT).show();

                }
                else {
                    if (!userPassword.equals(conpass_link)) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Password mismatchd", Toast.LENGTH_LONG).show();
                    } else {

                        firebaseAuth.createUserWithEmailAndPassword(userEmail, userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Log.e("Message:::::: ","DHUKSE RE MAMA!!!!!");
                                    sendInfoToDb();
                                    progressDialog.dismiss();
                                    sendEmailVerification();
                                    //sendSMSAuto();
                                } else {
                                    progressDialog.dismiss();
                                    Toast.makeText(getApplicationContext(), "Problem signing up,try Again", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }
            }
        });

        /*login1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
            }
        });*/
    }

  /**  public void sendSMSAuto()

    {
        String messageToSend = "go_learn";
        String number = "+8801687367928";
        SmsManager.getDefault().sendTextMessage(number,null,messageToSend,null,null);
        Toast.makeText(getApplicationContext(),"Subscription Successful!",Toast.LENGTH_LONG).show();
    }*/

    //Send Email Verification
    public void sendEmailVerification()
    {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if(firebaseUser != null) {
            firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Toast.makeText(getApplicationContext(),"Registered Successfully! Check email for verification", Toast.LENGTH_LONG).show();
                    firebaseAuth.signOut();
                    /*Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);*/
                    finish();
                }
            });
        }

        else {
            Toast.makeText(getApplicationContext(),"" + email.getText().toString() + " is not a valid email", Toast.LENGTH_LONG).show();
        }
    }

    // Add information to the db
    private void sendInfoToDb() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference(fbUserDetails).child(getIdFromEmail(userEmail)); //authentication e
        //per user er ekta UID ase oita diya redirect kora hoy...

        //ekhane ekhon ekta object create korte hobe karon setValue only ekta obj ney...

        SendInteltodb sdb = new SendInteltodb(userPassword, occupation, userName, userEmail, 0);
        reference.setValue(sdb);
    }
}
