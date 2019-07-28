package com.hamdan.virus.e_learning.home.teacher;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.hamdan.virus.e_learning.R;
import com.hamdan.virus.e_learning.SendInteltodb;
import com.hamdan.virus.e_learning.utils.AllStaticNames;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

import static com.hamdan.virus.e_learning.utils.AllStaticNames.fbEventDetails;
import static com.hamdan.virus.e_learning.utils.AllStaticNames.fbUserDetails;
import static com.hamdan.virus.e_learning.utils.AllStaticNames.sharedTable;
import static com.hamdan.virus.e_learning.utils.AllStaticNames.sharedUserEmail;
import static com.hamdan.virus.e_learning.utils.AllStaticNames.userEmail;
import static com.hamdan.virus.e_learning.utils.utils.getIdFromEmail;
import static com.hamdan.virus.e_learning.utils.utils.showToast;

public class AddNewClassActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    String setFormat = "", hourIn12Format = "", selectMinute = "", dateOfEvent, timeOfEvent;
    TimePickerDialog mTimePicker;
    TextView datePickerText, timePickerText;
    int hour, minute, year, month, day;
    Calendar calendar;
    RelativeLayout timeLayout, dateLayout;
    int eventSerial;
    EditText eventNameText, eventMessageText;
    String eventName, eventMessage, email;

    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_class);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        preferences = AddNewClassActivity.this.getSharedPreferences(sharedTable, MODE_PRIVATE);
        email = preferences.getString(sharedUserEmail, "");

        datePickerText = findViewById(R.id.datePickerText);
        timePickerText = findViewById(R.id.timePickerText);
        timeLayout = findViewById(R.id.timeLayout);
        dateLayout = findViewById(R.id.dateLayout);
        eventNameText = findViewById(R.id.eventNameText);
        eventMessageText = findViewById(R.id.eventMessageText);

        calendar = Calendar.getInstance();
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        if (hour == 0){
            hourIn12Format = "12";
            setFormat = "AM";
        }
        else if (hour == 12){
            hourIn12Format = "12";
            setFormat = "PM";
        }

        else if(hour<12){
            if (hour <10){
                hourIn12Format = "0"+String.valueOf(hour);
                Log.e("Time:", "AM");
            }
            else
                hourIn12Format = String.valueOf(hour);

            setFormat = "AM";
        }
        else{
            if (hour-12 <10){
                hourIn12Format = "0"+String.valueOf(hour-12);
                Log.e("Time:", "PM");
            }
            else
                hourIn12Format = String.valueOf(hour-12);

            setFormat = "PM";
        }

        if (minute < 10)
            selectMinute = "0"+String.valueOf(minute);
        else
            selectMinute = String.valueOf(minute);

        timeOfEvent = hourIn12Format + ":" + selectMinute + " "+setFormat;

        timePickerText.setText(hourIn12Format + ":" + selectMinute + " "+setFormat);

        dateOfEvent = day + "-" + (month+1) + "-" + year;
        datePickerText.setText(dateOfEvent);

        final DatePickerDialog.OnDateSetListener dateChangedListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                Log.e("DATE: ", "THIS WORKS");

                dateOfEvent = dayOfMonth + "-" + (monthOfYear+1) + "-" + year;
                datePickerText.setText(dateOfEvent);

            }
        };

        dateLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new DatePickerDialog(AddNewClassActivity.this, dateChangedListener, calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();

                day = calendar.get(Calendar.DAY_OF_MONTH);
                month = calendar.get(Calendar.MONTH);
                year = calendar.get(Calendar.YEAR);
            }
        });

        timeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTimePicker = new TimePickerDialog(AddNewClassActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int selectedHour, int selectedMinute) {

                        if (selectedHour == 0){
                            hourIn12Format = "12";
                            setFormat = "AM";
                        }
                        else if (selectedHour == 12){
                            hourIn12Format = "12";
                            setFormat = "PM";
                        }

                        else if(selectedHour<12){
                            if (selectedHour <10){
                                hourIn12Format = "0"+String.valueOf(selectedHour);
                                Log.e("Time:", "AM");
                            }
                            else
                                hourIn12Format = String.valueOf(selectedHour);

                            setFormat = "AM";
                        }
                        else{
                            if (selectedHour-12 <10){
                                hourIn12Format = "0"+String.valueOf(selectedHour-12);
                                Log.e("Time:", "PM");
                            }
                            else
                                hourIn12Format = String.valueOf(selectedHour-12);

                            setFormat = "PM";
                        }

                        if (selectedMinute < 10)
                            selectMinute = "0"+String.valueOf(selectedMinute);
                        else
                            selectMinute = String.valueOf(selectedMinute);

                        timeOfEvent = hourIn12Format + ":" + selectMinute + " " + setFormat;
                        timePickerText.setText(timeOfEvent);

                        Log.e("$$$", "Time is " + timeOfEvent);

                    }
                }, hour, minute, false);//Yes 24 hour time
                mTimePicker.setTitle(null);
                mTimePicker.show();
            }
        });



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_event, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.actionDone) {
            saveEvent();
            return true;
        }

        if (id == R.id.actionCancel) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void saveEvent() {

        eventName = eventNameText.getText().toString().trim();
        eventMessage = eventMessageText.getText().toString().trim();

        final AddClassModel addClassModel = new AddClassModel(eventName, eventMessage, dateOfEvent, timeOfEvent);

        databaseReference.child(AllStaticNames.fbUserDetails)
                .child(getIdFromEmail(email))
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        SendInteltodb sdb = dataSnapshot.getValue(SendInteltodb.class);

                        if (sdb != null) {
                            eventSerial = sdb.getEventSerial() + 1;

                            // Save Event to database
                            databaseReference.child(fbEventDetails)
                                    .child(getIdFromEmail(email))
                                    .child(String.valueOf(eventSerial))
                                    .setValue(addClassModel);

                            // Update event serial number
                            databaseReference.child(fbUserDetails)
                                    .child(getIdFromEmail(email))
                                    .child(AllStaticNames.eventSerial)
                                    .setValue(eventSerial);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Event create cancelled
                    }
                });

        finish();

    }
}
