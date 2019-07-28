package com.hamdan.virus.e_learning.home;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.hamdan.virus.e_learning.R;

import java.util.Objects;

public class ViewClassDetails extends AppCompatActivity {

    TextView eventNameText, eventMsgText, eventTimeText, eventDateText;
    String eventName, eventMsg, eventTime, eventDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_class_details);

        eventNameText = findViewById(R.id.eventNameText);
        eventMsgText = findViewById(R.id.eventMessageText);
        eventDateText = findViewById(R.id.eventDateText);
        eventTimeText = findViewById(R.id.eventTimeText);

        Intent intent = getIntent();

        eventName = Objects.requireNonNull(intent).getStringExtra("ClassName");
        eventMsg = Objects.requireNonNull(intent).getStringExtra("ClassMsg");
        eventDate = Objects.requireNonNull(intent).getStringExtra("ClassDate");
        eventTime = Objects.requireNonNull(intent).getStringExtra("ClassTime");

        eventNameText.setText(eventName);
        eventMsgText.setText(eventMsg);
        eventDateText.setText(eventDate);
        eventTimeText.setText(eventTime);
    }
}
