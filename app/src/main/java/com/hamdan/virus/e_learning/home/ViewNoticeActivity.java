package com.hamdan.virus.e_learning.home;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.hamdan.virus.e_learning.R;

import java.util.Objects;

public class ViewNoticeActivity extends AppCompatActivity {

    TextView noticeNameText, noticeText;
    String noticeName, notice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_notice);

        noticeNameText = findViewById(R.id.noticeNameText);
        noticeText = findViewById(R.id.noticeText);

        Intent intent = getIntent();

        noticeName = Objects.requireNonNull(intent).getStringExtra("noticeName");
        notice = Objects.requireNonNull(intent).getStringExtra("notice");

        noticeNameText.setText(noticeName);
        noticeText.setText(notice);

    }
}
