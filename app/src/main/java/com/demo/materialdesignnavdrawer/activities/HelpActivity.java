package com.demo.materialdesignnavdrawer.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.webkit.WebView;

import com.demo.materialdesignnavdrawer.R;

/**
 * Created by Administrator on 10.06.2015.
 */
public class HelpActivity extends AppCompatActivity {

    Toolbar toolbar;

    private WebView wv;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.help);
        initialise();

        initialise();

    }

    @Override
    public void onStart() {

        super.onStart();
        wv = (WebView) findViewById(R.id.wv_faq);
        wv.loadUrl("file:///android_asset/web/faq.html");
    }

    private void initialise() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(false);
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            getSupportActionBar().setTitle("FAQ");
            getSupportActionBar().setDisplayUseLogoEnabled(false);
        }

    }
}
