package com.hamdan.virus.e_learning;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.squareup.timessquare.CalendarPickerView;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddCalendar extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_calendar);
        firebaseAuth = FirebaseAuth.getInstance();

        Date today = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR,1);

        CalendarPickerView calendarPickerView = findViewById(R.id.calendar);
        calendarPickerView.init(today, calendar.getTime()).withSelectedDate(today);

        calendarPickerView.setOnDateSelectedListener(new CalendarPickerView.OnDateSelectedListener() {
            @Override
            public void onDateSelected(Date date) {
                String selectdate = DateFormat.getDateInstance(DateFormat.FULL).format(date);
                Log.e("Date:   ", "" + selectdate);
            }

            @Override
            public void onDateUnselected(Date date) {

            }
        });
    }
}
