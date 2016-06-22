package com.demo.materialdesignnavdrawer.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.webkit.WebView;

import com.demo.materialdesignnavdrawer.R;

public class Impressum extends AppCompatActivity {
    Toolbar toolbar;

    private WebView wv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_impressum);

        initialise();

    }

    @Override
    public void onStart() {

        super.onStart();
        wv = (WebView) findViewById(R.id.wv_impressum);
        wv.loadUrl("file:///android_asset/web/impressum.html");
    }

    private void initialise() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(false);
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            getSupportActionBar().setTitle("Impressum");
            getSupportActionBar().setDisplayUseLogoEnabled(false);
        }

    }
}
