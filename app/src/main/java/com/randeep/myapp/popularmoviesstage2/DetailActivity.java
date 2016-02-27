package com.randeep.myapp.popularmoviesstage2;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

/**
 * Created by randeep on 2/20/16.
 */
public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String movieId = getIntent().getStringExtra("MOVIE_ID");
        if (savedInstanceState == null ){

            Bundle arguments = new Bundle();
            arguments.putString("MOVIE_ID", movieId);
            DetailFragment detailFragment = new DetailFragment();
            detailFragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction().replace(R.id.container, detailFragment).commit();
        }
    }
}
