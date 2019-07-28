package com.hamdan.virus.e_learning;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.hamdan.virus.e_learning.starting.page.MainActivity;
import com.google.firebase.auth.FirebaseAuth;


public class HomeActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        firebaseAuth  = FirebaseAuth.getInstance();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    public void Logout()
    {
        firebaseAuth.signOut();
        finish();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    public void CalendarShow()
    {
        Intent intent = new Intent(getApplicationContext(),AddCalendar.class);
        startActivity(intent);
    }

    public void profile()
    {
        /*Intent intent = new Intent(getApplicationContext(), Profile_student.class);
        startActivity(intent);*/

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
           case R.id.logout:
                Logout();

            case R.id.profile:
                profile();

            case R.id.calendar:
                CalendarShow();


        }
        return true;
    }
}
