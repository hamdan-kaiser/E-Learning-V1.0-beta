package com.hamdan.virus.e_learning;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hamdan.virus.e_learning.starting.page.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity {

    private Button reset;
    private EditText reemail;
    private FirebaseAuth firebaseAuth;
    private TextView relogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        reset = findViewById(R.id.reset);
        reemail = findViewById(R.id.reemail);
        relogin = findViewById(R.id.blogin);
        firebaseAuth = FirebaseAuth.getInstance();

        reset.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final String remail = reemail.getText().toString().trim();

                if(remail.equals(""))
                {
                    Toast.makeText(getApplicationContext(),"Email is required",Toast.LENGTH_SHORT).show();
                }else {
                    firebaseAuth.sendPasswordResetEmail(remail).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getApplicationContext(), "Email has been sent to" + remail, Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(getApplicationContext(), "Error Sending Mail!", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });

        relogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }
}