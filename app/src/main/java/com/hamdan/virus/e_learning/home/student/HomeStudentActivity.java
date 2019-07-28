package com.hamdan.virus.e_learning.home.student;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.hamdan.virus.e_learning.R;
import com.hamdan.virus.e_learning.home.ProfileActivity;
import com.hamdan.virus.e_learning.home.ViewNoticeActivity;
import com.hamdan.virus.e_learning.home.ViewNoticeAdapter;
import com.hamdan.virus.e_learning.home.chatbot.ChatWithBotActivity;
import com.hamdan.virus.e_learning.home.teacher.AddNoticeModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hamdan.virus.e_learning.starting.page.MainActivity;

import java.util.ArrayList;
import java.util.Objects;

import static com.hamdan.virus.e_learning.utils.AllStaticNames.fbNoticeBoard;
import static com.hamdan.virus.e_learning.utils.AllStaticNames.fbTeacherList;
import static com.hamdan.virus.e_learning.utils.AllStaticNames.fbUserDetails;
import static com.hamdan.virus.e_learning.utils.AllStaticNames.isUserLoggedIn;
import static com.hamdan.virus.e_learning.utils.AllStaticNames.sharedUserEmail;
import static com.hamdan.virus.e_learning.utils.AllStaticNames.sharedUserName;
import static com.hamdan.virus.e_learning.utils.AllStaticNames.sharedUserOccupation;
import static com.hamdan.virus.e_learning.utils.utils.getIdFromEmail;

public class HomeStudentActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    TextView navStudentName, navStudentEmail;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    String name, email;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    FirebaseUser user;

    ListView noticeListView;
    TextView emptyText;
    ViewNoticeAdapter adapter;
    ArrayList<AddNoticeModel> noticeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_student);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        firebaseAuth = FirebaseAuth.getInstance(); //calling instance of authenticated data
        databaseReference = FirebaseDatabase.getInstance().getReference();
        user = firebaseAuth.getCurrentUser();

        noticeListView = findViewById(R.id.noticeListView);
        emptyText = findViewById(R.id.empty);
        emptyText.setVisibility(View.VISIBLE);


        //preference that stored in device
        preferences = HomeStudentActivity.this.getSharedPreferences("LoginData", MODE_PRIVATE);
        name = preferences.getString("name", null);
        email = preferences.getString("email", null);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView =  navigationView.getHeaderView(0);
        navStudentName = headerView.findViewById(R.id.studentName);
        navStudentEmail = headerView.findViewById(R.id.studentEmail);
        navStudentName.setText(name);
        navStudentEmail.setText(email);
        navigationView.setNavigationItemSelectedListener(this);

        noticeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(HomeStudentActivity.this, ViewNoticeActivity.class);
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

        //downloading and displaying data from firebase user....

        databaseReference.child(fbUserDetails)
                .child(getIdFromEmail(email))
                .child(fbTeacherList)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Log.e(">>>", "" + dataSnapshot);

                        if (dataSnapshot.exists()) {

                            ArrayList<TeacherListModel> teacherList = new ArrayList<>();
                            for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                                TeacherListModel values = dsp.getValue(TeacherListModel.class);
                                Log.e(">>>", "" + values);
                                teacherList.add(values);
                            }

                            noticeList = new ArrayList<>();

                            for (int i = 0; i < teacherList.size(); i++) {

                                databaseReference.child(fbNoticeBoard)
                                        .child(getIdFromEmail(teacherList.get(i).getEmail()))
                                        .addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                Log.e(">>>", "" + dataSnapshot);

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

                                                adapter = new ViewNoticeAdapter(HomeStudentActivity.this, noticeList);
                                                noticeListView.setAdapter(adapter);
                                                adapter.notifyDataSetChanged();
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });
                            }

                        } else {
                            emptyText.setVisibility(View.VISIBLE);
                        }

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

        if (id == R.id.navProfile) {
            // Handle the camera action
            Intent intent = new Intent(HomeStudentActivity.this, ProfileActivity.class);
            intent.putExtra("occupation", "Student");
            startActivity(intent);

        } else if (id == R.id.navViewClasses) {

            startActivity(new Intent(HomeStudentActivity.this, ClassListActivity.class));

        } else if (id == R.id.navTeachers) {
            startActivity(new Intent(HomeStudentActivity.this, TeacherListActivity.class));

        } else if (id == R.id.chatBot) {
            startActivity(new Intent(HomeStudentActivity.this, ChatWithBotActivity.class));

        } else if(id == R.id.logout1){

            firebaseAuth.signOut();

            editor = preferences.edit();
            editor.putBoolean(isUserLoggedIn, false);
            editor.putString(sharedUserName, "");
            editor.putString(sharedUserEmail, "");
            editor.putString(sharedUserOccupation, "");
            editor.apply();

            startActivity(new Intent(HomeStudentActivity.this, MainActivity.class));
            finish();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
