package com.hamdan.virus.e_learning.starting.page;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.hamdan.virus.e_learning.HomeActivity;
import com.hamdan.virus.e_learning.R;
import com.hamdan.virus.e_learning.home.SplashScreen;
import com.hamdan.virus.e_learning.home.public_teacher;
import com.hamdan.virus.e_learning.home.teacher.HomeTeacherActivity;
import com.hamdan.virus.e_learning.login.Registration;
import com.hamdan.virus.e_learning.home.student.HomeStudentActivity;
import com.hamdan.virus.e_learning.login.LoginActivity;
import com.hamdan.virus.e_learning.other.Categories;
import com.hamdan.virus.e_learning.other.about;
import com.hamdan.virus.e_learning.other.faq;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import static com.hamdan.virus.e_learning.utils.AllStaticNames.isUserLoggedIn;
import static com.hamdan.virus.e_learning.utils.AllStaticNames.sharedTable;
import static com.hamdan.virus.e_learning.utils.AllStaticNames.sharedUserOccupation;


/**
 * E-Learning v1.0 beta
 * An online learning platform for all kinds of people
 * Its an application where all tutors and learners will have a community and establish a good level of
 * co-operation
 * Tutors can
 * - create a private classroom.
 * - add notice
 * - provide links for the required materials
 *
 * Where Students can
 * - Get enrolled in suitable classes.
 * - Get tutoring support **FREE**
 *
 * Technology Used:
 * 1. Android Studio.
 * 2. Firebase.
 *
 * Our Team Members:
 *
 * 1. Hamdan Kaiser
 * 2. Monim Kaiser*/



public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ViewPager mPager; // slideImage Viewer
    private ArrayList<ImageModel> imageModelArrayList;
    LinearLayout slideDots;
    int dotCount;
    ImageView[] dots;
    Boolean isLoggedin = false;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;


    //sliding image...
    private int[] myImageList = {R.drawable.slide_1, R.drawable.slide_2, R.drawable.slide_3, R.drawable.slide_4, R.drawable.slide_5};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        slideDots = findViewById(R.id.sliderDots);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        imageModelArrayList = new ArrayList<>();
        imageModelArrayList = populateList();

        init();
    }

    private ArrayList<ImageModel> populateList(){

        ArrayList<ImageModel> list = new ArrayList<>();

        for(int i = 0; i < 5; i++){
            ImageModel imageModel = new ImageModel();
            imageModel.setImage_drawable(myImageList[i]);
            list.add(imageModel);
        }

        return list;
    }

    @Override
    protected void onStart() {


        Toast.makeText(getApplicationContext(),"Always Check your internet connection before get started",Toast.LENGTH_LONG).show();
        super.onStart();

        sharedPreferences = MainActivity.this.getSharedPreferences(sharedTable, MODE_PRIVATE);
        isLoggedin = sharedPreferences.getBoolean(isUserLoggedIn, false);


        //goto homepage of user while starting app
        if (isLoggedin) {
            String occupation = sharedPreferences.getString(sharedUserOccupation, "");
            if (occupation.equals("student")) {
                Intent intent = new Intent(MainActivity.this, HomeStudentActivity.class);
                startActivity(intent);
                finish();

            } else {
                Intent intent = new Intent(MainActivity.this, HomeTeacherActivity.class);
                startActivity(intent);
                finish();
            }
        }
    }

    private void init() {

        mPager = findViewById(R.id.viewPager);
        mPager.setAdapter(new SlidingImageAdapter(MainActivity.this, imageModelArrayList));

        dotCount = myImageList.length;
        dots = new ImageView[dotCount];
        for (int i = 0; i < dotCount; i++) {
            dots[i] = new ImageView(this);
            dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.slide_inactive_dot));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(8, 0, 8, 0);
            slideDots.addView(dots[i], params);
        }

        dots[0].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.slide_active_dot));

        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                for (int i = 0; i < dotCount; i++) {
                    dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.slide_inactive_dot));
                }
                dots[position].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.slide_active_dot));

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new myTimerTask(), 2000, 2000);
    }

    public class myTimerTask extends TimerTask {
        @Override
        public void run() {

            //slideshow

            MainActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    if (mPager.getCurrentItem() == 0) {
                        mPager.setCurrentItem(1);
                    } else if (mPager.getCurrentItem() == 1) {
                        mPager.setCurrentItem(2);
                    } else if (mPager.getCurrentItem() == 2) {
                        mPager.setCurrentItem(3);
                    } else if (mPager.getCurrentItem() == 3) {
                        mPager.setCurrentItem(4);
                    } else {
                        mPager.setCurrentItem(0);
                    }
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.logIn) {
            // Handle the action
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);

        } else if (id == R.id.registration) {
            Intent intent = new Intent(MainActivity.this, Registration.class);
            startActivity(intent);

        } else if (id == R.id.category) {
            Intent intent = new Intent(MainActivity.this, Categories.class);
            startActivity(intent);
        } else if (id == R.id.aboutUs) {
            Intent intent = new Intent(MainActivity.this, about.class);
            startActivity(intent);

        } else if (id == R.id.faqs) {
            Intent intent = new Intent(MainActivity.this, faq.class);
            startActivity(intent);

        }
        else if(id== R.id.tpublic)
        {
            Intent intent = new Intent(MainActivity.this,public_teacher.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
