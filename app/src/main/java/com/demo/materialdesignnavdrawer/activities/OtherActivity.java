package com.demo.materialdesignnavdrawer.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.demo.materialdesignnavdrawer.R;

/**
 * Represents other activity different from the main activity
 *
 * @author Sotti https://plus.google.com/+PabloCostaTirado/about
 */
public class OtherActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.help);

        initialise();
    }

    /**
     * Create, bind and set up the resources
     */
    private void initialise() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(false);
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            getSupportActionBar().setTitle("other Activity");
            getSupportActionBar().setDisplayUseLogoEnabled(false);
        }

    }
}
