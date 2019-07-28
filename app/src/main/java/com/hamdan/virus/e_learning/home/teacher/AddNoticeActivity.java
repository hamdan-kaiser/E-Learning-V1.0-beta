package com.hamdan.virus.e_learning.home.teacher;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.hamdan.virus.e_learning.R;
import com.hamdan.virus.e_learning.SendInteltodb;
import com.hamdan.virus.e_learning.home.student.HomeStudentActivity;
import com.hamdan.virus.e_learning.utils.AllStaticNames;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static com.hamdan.virus.e_learning.utils.AllStaticNames.fbEventDetails;
import static com.hamdan.virus.e_learning.utils.AllStaticNames.fbNoticeBoard;
import static com.hamdan.virus.e_learning.utils.AllStaticNames.fbUserDetails;
import static com.hamdan.virus.e_learning.utils.utils.getIdFromEmail;

public class AddNoticeActivity extends AppCompatActivity {

    EditText noticeText;
    Button publishButton;

    String notice = "", myEmail = "";
    int eventSerial;
    SharedPreferences preferences;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_notice);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        preferences = AddNoticeActivity.this.getSharedPreferences("LoginData", MODE_PRIVATE);
        myEmail = preferences.getString("email", null);

        noticeText = findViewById(R.id.noticeText);
        publishButton = findViewById(R.id.publishBtn);

        publishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notice = noticeText.getText().toString().trim();

                publishInFirebase(notice);
            }
        });
    }

    private void publishInFirebase(final String notice) {

        databaseReference.child(AllStaticNames.fbUserDetails)
                .child(getIdFromEmail(myEmail))
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        SendInteltodb sdb = dataSnapshot.getValue(SendInteltodb.class);

                        if (sdb != null) {
                            eventSerial = sdb.getEventSerial() + 1;
                            String name = sdb.getUserName();

                            AddNoticeModel noticeModel = new AddNoticeModel(name, notice);

                            // Save Notice to database
                            databaseReference.child(fbNoticeBoard)
                                    .child(getIdFromEmail(myEmail))
                                    .child(String.valueOf(eventSerial))
                                    .setValue(noticeModel);

                            // Update event serial number
                            databaseReference.child(fbUserDetails)
                                    .child(getIdFromEmail(myEmail))
                                    .child(AllStaticNames.eventSerial)
                                    .setValue(eventSerial);

                            finish() ;
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Notice save failed
                    }
                });

    }
}
