package com.hamdan.virus.e_learning.home;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hamdan.virus.e_learning.R;
import com.hamdan.virus.e_learning.SendInteltodb;

import java.util.ArrayList;

import static com.hamdan.virus.e_learning.utils.AllStaticNames.fbUserDetails;

public class public_teacher extends AppCompatActivity {
    ListView listView;
    String userEmail;
    public_adapter adapter;
    TextView emptyText;

    ArrayList<SendInteltodb> teacherList = new ArrayList<>();

    private DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_public_list);
        //Toolbar toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        listView = findViewById(R.id.teacherListView);
        databaseReference = FirebaseDatabase.getInstance().getReference();


    }

    @Override
    protected void onStart() {
        super.onStart();
        showAllTeacherList();
    }

    public void showAllTeacherList()
    {

        databaseReference.child(fbUserDetails).orderByChild("occupation").equalTo("teacher")
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                teacherList.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    SendInteltodb sdb = snapshot.getValue(SendInteltodb.class);
                    teacherList.add(sdb);
                    Log.e("TDGSFF:   ","     "+dataSnapshot);
                }

                adapter = new public_adapter(public_teacher.this,teacherList);
                listView.setAdapter(adapter);
                adapter.notifyDataSetChanged();

              /*  for (int i=0;i<teacherList.size();i++)
                {
                    Log.e("Teacher: ","  "+teacherList.get(i).getUserEmail());

                }*/
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
