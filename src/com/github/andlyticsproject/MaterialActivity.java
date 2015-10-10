package com.github.andlyticsproject;

import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;


public abstract class MaterialActivity extends AppCompatActivity {
    private Toolbar toolbar;

    public Toolbar getToolbar() {
        return toolbar;
    }

    protected Toolbar initToolbar() {
        return initToolbar(false);
    }

    protected Toolbar initToolbar(boolean homeAsUp) {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(homeAsUp);
        return toolbar;
    }

    @NonNull
    @Override
    public ActionBar getSupportActionBar() {
        ActionBar actionBar = super.getSupportActionBar();
        if (actionBar == null)
            throw new UnsupportedOperationException("Call initToolbar first !");
        return actionBar;
    }


}