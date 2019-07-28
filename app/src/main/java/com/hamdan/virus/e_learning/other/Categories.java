package com.hamdan.virus.e_learning.other;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.hamdan.virus.e_learning.R;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

public class Categories extends AppCompatActivity {
    MaterialSearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);

        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Course Search");
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));

         searchView = findViewById(R.id.search);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_items,menu);
        MenuItem item = menu.findItem(R.id.search_action);
        searchView.setMenuItem(item);
        return true;
    }
}
