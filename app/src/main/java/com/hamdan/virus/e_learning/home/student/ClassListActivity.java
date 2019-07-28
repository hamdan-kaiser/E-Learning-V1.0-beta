package com.hamdan.virus.e_learning.home.student;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.hamdan.virus.e_learning.R;
import com.hamdan.virus.e_learning.home.ViewClassDetails;
import com.hamdan.virus.e_learning.home.teacher.AddClassModel;
import com.hamdan.virus.e_learning.home.teacher.ViewClassesTeacherActivity;
import com.hamdan.virus.e_learning.home.teacher.ViewClassesTeacherAdapter;
import com.hamdan.virus.e_learning.utils.AllStaticNames;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

import static com.hamdan.virus.e_learning.utils.AllStaticNames.fbEventDetails;
import static com.hamdan.virus.e_learning.utils.AllStaticNames.fbTeacherList;
import static com.hamdan.virus.e_learning.utils.AllStaticNames.fbUserDetails;
import static com.hamdan.virus.e_learning.utils.AllStaticNames.userEmail;
import static com.hamdan.virus.e_learning.utils.utils.getIdFromEmail;

public class ClassListActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;
    private DatabaseReference databaseReference;
    private SharedPreferences preferences;

    ListView listView;
    String myEmail;
    TextView emptyText;
    ViewClassesTeacherAdapter adapter;
    ArrayList<AddClassModel> eventList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_list);

        listView = findViewById(R.id.classTeacherListView);
        emptyText = findViewById(R.id.empty);
        emptyText.setVisibility(View.VISIBLE);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        preferences = ClassListActivity.this.getSharedPreferences(AllStaticNames.sharedTable, MODE_PRIVATE);
        myEmail = preferences.getString(AllStaticNames.sharedUserEmail, null);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ClassListActivity.this, ViewClassDetails.class);
                intent.putExtra("ClassName", Objects.requireNonNull(adapter.getItem(position)).getEventName());
                intent.putExtra("ClassMsg", Objects.requireNonNull(adapter.getItem(position)).getEventMessage());
                intent.putExtra("ClassDate", Objects.requireNonNull(adapter.getItem(position)).getEventDate());
                intent.putExtra("ClassTime", Objects.requireNonNull(adapter.getItem(position)).getEventTime());
                startActivity(intent);
            }
        });

        databaseReference.child(fbUserDetails)
                .child(getIdFromEmail(myEmail))
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

                            eventList = new ArrayList<>();

                            for (int i = 0; i < teacherList.size(); i++) {

                                databaseReference.child(fbEventDetails)
                                        .child(getIdFromEmail(teacherList.get(i).getEmail()))
                                        .addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                Log.e(">>>", "" + dataSnapshot);

                                                for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                                                    AddClassModel values = dsp.getValue(AddClassModel.class);
                                                    Log.e(">>>", "" + values);
                                                    eventList.add(values);
                                                }

                                                if (dataSnapshot.getValue() == null) {
                                                    if (emptyText.getVisibility() != View.GONE)
                                                        emptyText.setVisibility(View.VISIBLE);

                                                } else {
                                                    emptyText.setVisibility(View.GONE);
                                                }

                                                adapter = new ViewClassesTeacherAdapter(ClassListActivity.this, eventList);
                                                listView.setAdapter(adapter);
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
}
