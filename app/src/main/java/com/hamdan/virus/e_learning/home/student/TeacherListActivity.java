package com.hamdan.virus.e_learning.home.student;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.hamdan.virus.e_learning.R;
import com.hamdan.virus.e_learning.utils.AllStaticNames;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.hamdan.virus.e_learning.utils.AllStaticNames.fbTeacherList;
import static com.hamdan.virus.e_learning.utils.AllStaticNames.fbUserDetails;
import static com.hamdan.virus.e_learning.utils.utils.getIdFromEmail;

public class TeacherListActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;
    private DatabaseReference databaseReference;
    private SharedPreferences preferences;

    ListView listView;
    String userEmail;
    TeacherListAdapter adapter;
    TextView emptyText;

    ArrayList<TeacherListModel> teacherList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listView = findViewById(R.id.teacherListView);
        emptyText = findViewById(R.id.empty);
        emptyText.setVisibility(View.VISIBLE);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        preferences = TeacherListActivity.this.getSharedPreferences(AllStaticNames.sharedTable, MODE_PRIVATE);
        userEmail = preferences.getString(AllStaticNames.sharedUserEmail, null);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(TeacherListActivity.this, AddTeacherActivity.class));
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        showAllTeachers();
    }

    private void showAllTeachers() {

        databaseReference.child(fbUserDetails)
                .child(getIdFromEmail(userEmail))
                .child(fbTeacherList)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Log.e(">>>", "" + dataSnapshot);

                        teacherList = new ArrayList<>();

                        for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                            TeacherListModel values = dsp.getValue(TeacherListModel.class);
                            Log.e(">>>", "" + values);
                            teacherList.add(values);
                        }

                        if (dataSnapshot.getValue() == null) {
                            if (emptyText.getVisibility() != View.GONE)
                                emptyText.setVisibility(View.VISIBLE);

                        } else {
                            emptyText.setVisibility(View.GONE);
                        }

                        adapter = new TeacherListAdapter(TeacherListActivity.this, teacherList);
                        listView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

    }

}
