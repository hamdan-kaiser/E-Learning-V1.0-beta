package com.hamdan.virus.e_learning.home.student;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.hamdan.virus.e_learning.R;
import com.hamdan.virus.e_learning.SendInteltodb;
import com.hamdan.virus.e_learning.utils.AllStaticNames;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static com.hamdan.virus.e_learning.utils.AllStaticNames.fbTeacherList;
import static com.hamdan.virus.e_learning.utils.AllStaticNames.fbUserDetails;
import static com.hamdan.virus.e_learning.utils.AllStaticNames.sharedUserEmail;
import static com.hamdan.virus.e_learning.utils.AllStaticNames.userEmail;
import static com.hamdan.virus.e_learning.utils.utils.getIdFromEmail;

public class AddTeacherActivity extends AppCompatActivity {

    EditText teacherEmail;
    Button followTeacher;

    String studentEmail = "";
    SharedPreferences preferences;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener; //authenticate users information
    private DatabaseReference databaseReference;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_teacher);

        preferences = AddTeacherActivity.this.getSharedPreferences("LoginData", MODE_PRIVATE);
        studentEmail = preferences.getString(sharedUserEmail, null);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        teacherEmail = findViewById(R.id.publicTeacherEmail);
        followTeacher = findViewById(R.id.followTeacherBtn);

        followTeacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTeacherEmailToFirebase(teacherEmail.getText().toString().trim());
            }
        });
    }

    private void addTeacherEmailToFirebase(final String email) {

        databaseReference.child(fbUserDetails)
                .orderByChild(getIdFromEmail(userEmail)).equalTo(email)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            // Teacher Exists

                            databaseReference.child(AllStaticNames.fbUserDetails)
                                    .child(getIdFromEmail(email))
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                    String occup = "", teacherName = "";
                                    Log.e(">>>", "Entered 3     " + dataSnapshot);

                                    SendInteltodb sdb = dataSnapshot.getValue(SendInteltodb.class);

                                    if (sdb != null) {
                                        occup = sdb.getOccupation();
                                        teacherName = sdb.getUserName();
                                        Log.e(">>>", "" + occup + " --- " + sdb.getUserName());

                                        if (occup.equals("teacher")) {

                                            final String finalTeacherName = teacherName;
                                            databaseReference.child(fbUserDetails)
                                                    .child(getIdFromEmail(studentEmail))
                                                    .child(fbTeacherList)
                                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                            long size = dataSnapshot.getChildrenCount();
                                                            String name = "";

                                                            if (size < 10) {
                                                                name = "user000" + (size + 1);
                                                            } else if (size < 100) {
                                                                name = "user00" + (size + 1);
                                                            } else {
                                                                name = "user0" + (size + 1);
                                                            }

                                                            TeacherListModel teacherListModel = new TeacherListModel(email, finalTeacherName);

                                                            databaseReference.child(fbUserDetails).child(getIdFromEmail(studentEmail))
                                                                    .child(fbTeacherList).child(name).setValue(teacherListModel);
                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                                        }
                                                    });

                                            finish();

                                        } else {
                                            // Email is not a teacher
                                            Log.e(">>>", "Entered 0001");
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });


                        } else {
                            // No Teacher of this Email
                            Log.e(">>>", "Entered 0002");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }
}
