package com.hamdan.virus.e_learning.home.teacher;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.hamdan.virus.e_learning.R;
import com.hamdan.virus.e_learning.home.ProfileActivity;
import com.hamdan.virus.e_learning.home.ViewNoticeActivity;
import com.hamdan.virus.e_learning.home.ViewNoticeAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hamdan.virus.e_learning.starting.page.MainActivity;

import java.util.ArrayList;
import java.util.Objects;

import static com.hamdan.virus.e_learning.utils.AllStaticNames.fbNoticeBoard;
import static com.hamdan.virus.e_learning.utils.AllStaticNames.isUserLoggedIn;
import static com.hamdan.virus.e_learning.utils.AllStaticNames.sharedUserEmail;
import static com.hamdan.virus.e_learning.utils.AllStaticNames.sharedUserName;
import static com.hamdan.virus.e_learning.utils.AllStaticNames.sharedUserOccupation;
import static com.hamdan.virus.e_learning.utils.utils.getIdFromEmail;

public class HomeTeacherActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    String myEmail = "", name;
    ViewNoticeAdapter adapter;
    ArrayList<AddNoticeModel> noticeList = new ArrayList<>();

    ListView noticeListView;
    TextView emptyText;

    TextView navTeacherName, navTeacherEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_teacher);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        preferences = HomeTeacherActivity.this.getSharedPreferences("LoginData", MODE_PRIVATE);
        myEmail = preferences.getString("email", null);
        name = preferences.getString("name", null);

        noticeListView = findViewById(R.id.noticeListView);
        emptyText = findViewById(R.id.empty);
        emptyText.setVisibility(View.VISIBLE);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView =  navigationView.getHeaderView(0);
        navTeacherName = headerView.findViewById(R.id.teacherName);
        navTeacherEmail = headerView.findViewById(R.id.teacherEmail);
        navTeacherName.setText(name);
        navTeacherEmail.setText(myEmail);
        navigationView.setNavigationItemSelectedListener(this);

        noticeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(HomeTeacherActivity.this, ViewNoticeActivity.class);
                intent.putExtra("noticeName", Objects.requireNonNull(adapter.getItem(position)).getName());
                intent.putExtra("notice", Objects.requireNonNull(adapter.getItem(position)).getNotice());
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        showAllNotice();
    }

    private void showAllNotice() {

        databaseReference.child(fbNoticeBoard)
                .child(getIdFromEmail(myEmail))
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Log.e(">>>", "" + dataSnapshot);

                        noticeList.clear();

                        for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                            AddNoticeModel values = dsp.getValue(AddNoticeModel.class);
                            Log.e(">>>", "" + values);
                            noticeList.add(values);
                        }

                        if (dataSnapshot.getValue() == null) {
                            if (emptyText.getVisibility() != View.GONE)
                                emptyText.setVisibility(View.VISIBLE);

                        } else {
                            emptyText.setVisibility(View.GONE);
                        }

                        adapter = new ViewNoticeAdapter(HomeTeacherActivity.this, noticeList);
                        noticeListView.setAdapter(adapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

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
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.navProfileTeacher) {
            // Handle the camera action

            Intent intent = new Intent(HomeTeacherActivity.this, ProfileActivity.class);
            intent.putExtra("occupation", "Teacher");
            startActivity(intent);

        } else if (id == R.id.navViewClassesTeacher) {

            startActivity(new Intent(HomeTeacherActivity.this, ViewClassesTeacherActivity.class));

        } else if (id == R.id.navNewClass) {

            startActivity(new Intent(HomeTeacherActivity.this, AddNewClassActivity.class));

        } else if (id == R.id.navAddNotice) {

            startActivity(new Intent(HomeTeacherActivity.this, AddNoticeActivity.class));

        }
        else if(id == R.id.logout1){
            firebaseAuth.signOut();

            editor = preferences.edit();
            editor.putBoolean(isUserLoggedIn, false);
            editor.putString(sharedUserName, "");
            editor.putString(sharedUserEmail, "");
            editor.putString(sharedUserOccupation, "");
            editor.apply();

            startActivity(new Intent(HomeTeacherActivity.this, MainActivity.class));
            finish();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
