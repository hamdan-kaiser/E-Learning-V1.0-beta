package com.hamdan.virus.e_learning.home.teacher;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.hamdan.virus.e_learning.R;
import com.hamdan.virus.e_learning.home.ViewClassDetails;
import com.hamdan.virus.e_learning.home.student.TeacherListActivity;
import com.hamdan.virus.e_learning.home.student.TeacherListModel;
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
import static com.hamdan.virus.e_learning.utils.utils.getIdFromEmail;

public class ViewClassesTeacherActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;
    private DatabaseReference databaseReference;
    private SharedPreferences preferences;

    ListView listView;
    String email;

    ViewClassesTeacherAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_classes_teacher);

        listView = findViewById(R.id.classTeacherListView);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        preferences = ViewClassesTeacherActivity.this.getSharedPreferences(AllStaticNames.sharedTable, MODE_PRIVATE);
        email = preferences.getString(AllStaticNames.sharedUserEmail, null);


        databaseReference.child(fbEventDetails)
                .child(getIdFromEmail(email))
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Log.e(">>>", "" + dataSnapshot);

                        ArrayList<AddClassModel> eventList = new ArrayList<>();
                        for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                            AddClassModel values = dsp.getValue(AddClassModel.class);
                            Log.e(">>>", "" + values);
                            eventList.add(values);
                        }

                        adapter = new ViewClassesTeacherAdapter(ViewClassesTeacherActivity.this, eventList);
                        listView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ViewClassesTeacherActivity.this, ViewClassDetails.class);
                intent.putExtra("ClassName", Objects.requireNonNull(adapter.getItem(position)).getEventName());
                intent.putExtra("ClassMsg", Objects.requireNonNull(adapter.getItem(position)).getEventMessage());
                intent.putExtra("ClassDate", Objects.requireNonNull(adapter.getItem(position)).getEventDate());
                intent.putExtra("ClassTime", Objects.requireNonNull(adapter.getItem(position)).getEventTime());
                startActivity(intent);
            }
        });


    }
}
