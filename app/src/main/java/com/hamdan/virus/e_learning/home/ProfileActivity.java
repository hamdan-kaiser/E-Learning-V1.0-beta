package com.hamdan.virus.e_learning.home;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.hamdan.virus.e_learning.R;
import com.hamdan.virus.e_learning.home.student.TeacherListActivity;
import com.hamdan.virus.e_learning.utils.AllStaticNames;

public class ProfileActivity extends AppCompatActivity {

    ImageView proPic;
    TextView nameText, emailText, occupationText;

    SharedPreferences preferences;
    String name, email, occupation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        nameText = findViewById(R.id.nameText);
        emailText = findViewById(R.id.emailText);
        occupationText = findViewById(R.id.occupationText);
        proPic = findViewById(R.id.profilePicture);

        preferences = ProfileActivity.this.getSharedPreferences(AllStaticNames.sharedTable, MODE_PRIVATE);
        name = preferences.getString(AllStaticNames.sharedUserName, null);
        email = preferences.getString(AllStaticNames.sharedUserEmail, null);

        Intent intent = getIntent();

        occupation = intent.getStringExtra("occupation");

        if (!occupation.equals("Student")) {
            proPic.setBackgroundResource(R.drawable.ic_profile_teacher);
        } else {
            proPic.setBackgroundResource(R.drawable.ic_student);
        }

        nameText.setText(name);
        emailText.setText("" + email);
        occupationText.setText("Occupation: " + occupation);

    }
}
