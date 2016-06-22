package com.demo.materialdesignnavdrawer.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.demo.materialdesignnavdrawer.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import campusquizregandlogdesign.com.example.helper.JsonParsingFunctions;
import campusquizregandlogdesign.com.example.helper.SQLiteHandler;
import campusquizregandlogdesign.com.example.helper.SQLiteQuestionHandler;
import trainingsspiel.ChallengeQuizActivity;

public class MainGameScreen extends AppCompatActivity {

    //  JSON url
    private static final String GAMEDETAIL_URL = "http://ps15server.cloudapp.net:8080/useraccount/GamePlayService/getGameDetails";
    private static final String TAG_MESSAGES = "GameDetails";
    private static final String TAG_USERNAME1 = "username1";
    private static final String TAG_USERNAME2 = "username2";
    private static final String TAG_STATGE = "stage";
    private static final String TAG_GAMEID = "gameid";
    private static final String TAG_IDKATEGORIE = "hkid";
    private static final String TAG_PTU1 = "ptu1";
    private static final String TAG_PTU2 = "ptu2";
    private static final String TAG_ACTIVEPLAYER = "activeplayer";
    private static final String TAG_U1S1F1 = "U1S1F1";
    private static final String TAG_U1S1F2 = "U1S1F2";
    private static final String TAG_U1S1F3 = "U1S1F3";
    private static final String TAG_U1S2F1 = "U1S2F1";
    private static final String TAG_U1S2F2 = "U1S2F2";
    private static final String TAG_U1S2F3 = "U1S2F3";
    private static final String TAG_U1S3F1 = "U1S3F1";
    private static final String TAG_U1S3F2 = "U1S3F2";
    private static final String TAG_U1S3F3 = "U1S3F3";
    private static final String TAG_U1S4F1 = "U1S4F1";
    private static final String TAG_U1S4F2 = "U1S4F2";
    private static final String TAG_U1S4F3 = "U1S4F3";
    private static final String TAG_U2S1F1 = "U2S1F1";
    private static final String TAG_U2S1F2 = "U2S1F2";
    private static final String TAG_U2S1F3 = "U2S1F3";
    private static final String TAG_U2S2F1 = "U2S2F1";
    private static final String TAG_U2S2F2 = "U2S2F2";
    private static final String TAG_U2S2F3 = "U2S2F3";
    private static final String TAG_U2S3F1 = "U2S3F1";
    private static final String TAG_U2S3F2 = "U2S3F2";
    private static final String TAG_U2S3F3 = "U2S3F3";
    private static final String TAG_U2S4F1 = "U2S4F1";
    private static final String TAG_U2S4F2 = "U2S4F2";
    private static final String TAG_U2S4F3 = "U2S4F3";
    ArrayList<HashMap<String, String>> gameDetailList = new ArrayList<HashMap<String, String>>();
    SwipeRefreshLayout mSwipeRefreshLayout;
    LoadInbox In = new LoadInbox();
    String dbgameid = null;
    int gameid = 0;
    int extragameid = 0;
    String dbusername = null;
    String myusername = null;
    String vsusername = null;
    int activeStage = 0;
    int activeplayer = 0;
    int myusernr = 0;
    int hauptkategorie = 0;
    // products JSONArray
    JSONArray inbox = null;
    private SQLiteQuestionHandler db = null;
    // Progress Dialog
    private ProgressDialog pDialog;
    private int[][] ResultsP1 = new int[4][3];
    private int[][] ResultsP2 = new int[4][3];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_game_screen);


        Bundle bundle = getIntent().getExtras();
        dbgameid = bundle.getString("GAMEID");
        String u1 = bundle.getString("USERNAME1");
        String u2 = bundle.getString("USERNAME2");

        initialize();

        SQLiteHandler db = new SQLiteHandler(getApplicationContext());
        HashMap<String, String> user = db.getUserDetails();
        dbusername = user.get("name");

        if (In.execute() == null) {
            reloadGameDetails();
        }


        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.main_game_swipe_container);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                final AsyncTask<String, String, String> execute = new LoadInbox().execute();
                reloadGameDetails();
            }
        });


        reloadGameDetails();
    }

    private void initialize() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(false);
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            getSupportActionBar().setTitle(R.string.toolbar_title_spieluebersicht);
            getSupportActionBar().setDisplayUseLogoEnabled(false);
        }
    }

    private void reloadGameDetails() {
        int punkteP1 = 0, punkteP2 = 0;


        //<editor-fold desc="Folding fuer Benutzername aktualisieren">
        //Benutzernamen aktualisieren.
        TextView tv_username1 = (TextView) findViewById(R.id.vsUsernameLeft);
        tv_username1.setText(myusername);

        TextView tv_username2 = (TextView) findViewById(R.id.vsUsernameRight);
        tv_username2.setText(vsusername);
        //</editor-fold>

        //<editor-fold desc="Folding fuer Bool-Tabelle">
        //Checken, ob BenutzerNr 1 oder 2 in der Datenbank, je nach dem sind die Felder in rechts-links Reihenfolge zu vertauschen --> U1Ergebnis ist dann eventuell der Gegenspieler.
        ImageView iv_U1S1F1 = (ImageView) findViewById(R.id.LS1F1);
        ImageView iv_U1S1F2 = (ImageView) findViewById(R.id.LS1F2);
        ImageView iv_U1S1F3 = (ImageView) findViewById(R.id.LS1F3);
        ImageView iv_U1S2F1 = (ImageView) findViewById(R.id.LS2F1);
        ImageView iv_U1S2F2 = (ImageView) findViewById(R.id.LS2F2);
        ImageView iv_U1S2F3 = (ImageView) findViewById(R.id.LS2F3);
        ImageView iv_U1S3F1 = (ImageView) findViewById(R.id.LS3F1);
        ImageView iv_U1S3F2 = (ImageView) findViewById(R.id.LS3F2);
        ImageView iv_U1S3F3 = (ImageView) findViewById(R.id.LS3F3);
        ImageView iv_U1S4F1 = (ImageView) findViewById(R.id.LS4F1);
        ImageView iv_U1S4F2 = (ImageView) findViewById(R.id.LS4F2);
        ImageView iv_U1S4F3 = (ImageView) findViewById(R.id.LS4F3);

        ImageView iv_U2S1F1 = (ImageView) findViewById(R.id.RS1F1);
        ImageView iv_U2S1F2 = (ImageView) findViewById(R.id.RS1F2);
        ImageView iv_U2S1F3 = (ImageView) findViewById(R.id.RS1F3);
        ImageView iv_U2S2F1 = (ImageView) findViewById(R.id.RS2F1);
        ImageView iv_U2S2F2 = (ImageView) findViewById(R.id.RS2F2);
        ImageView iv_U2S2F3 = (ImageView) findViewById(R.id.RS2F3);
        ImageView iv_U2S3F1 = (ImageView) findViewById(R.id.RS3F1);
        ImageView iv_U2S3F2 = (ImageView) findViewById(R.id.RS3F2);
        ImageView iv_U2S3F3 = (ImageView) findViewById(R.id.RS3F3);
        ImageView iv_U2S4F1 = (ImageView) findViewById(R.id.RS4F1);
        ImageView iv_U2S4F2 = (ImageView) findViewById(R.id.RS4F2);
        ImageView iv_U2S4F3 = (ImageView) findViewById(R.id.RS4F3);


        if (myusernr == 1) {
            if (ResultsP1[0][0] == 1) {
                iv_U1S1F1.setBackgroundColor(getResources().getColor(R.color.Light_Green_500));
                punkteP1++;
            } else if (ResultsP1[0][0] == 0) {
                iv_U1S1F1.setBackgroundColor(getResources().getColor(R.color.Red_500));
            } else {
                iv_U1S1F1.setBackgroundColor(getResources().getColor(R.color.blue_grey_500));
            }

            if (ResultsP1[0][1] == 1) {
                iv_U1S1F2.setBackgroundColor(getResources().getColor(R.color.Light_Green_500));
                punkteP1++;
            } else if (ResultsP1[0][1] == 0) {
                iv_U1S1F2.setBackgroundColor(getResources().getColor(R.color.Red_500));
            } else {
                iv_U1S1F2.setBackgroundColor(getResources().getColor(R.color.blue_grey_500));
            }

            if (ResultsP1[0][2] == 1) {
                iv_U1S1F3.setBackgroundColor(getResources().getColor(R.color.Light_Green_500));
                punkteP1++;
            } else if (ResultsP1[0][2] == 0) {
                iv_U1S1F3.setBackgroundColor(getResources().getColor(R.color.Red_500));
            } else {
                iv_U1S1F3.setBackgroundColor(getResources().getColor(R.color.blue_grey_500));
            }

            if (ResultsP1[1][0] == 1) {
                iv_U1S2F1.setBackgroundColor(getResources().getColor(R.color.Light_Green_500));
                punkteP1++;
            } else if (ResultsP1[1][0] == 0) {
                iv_U1S2F1.setBackgroundColor(getResources().getColor(R.color.Red_500));
            } else {
                iv_U1S2F1.setBackgroundColor(getResources().getColor(R.color.blue_grey_500));
            }

            if (ResultsP1[1][1] == 1) {
                iv_U1S2F2.setBackgroundColor(getResources().getColor(R.color.Light_Green_500));
                punkteP1++;
            } else if (ResultsP1[1][1] == 0) {
                iv_U1S2F2.setBackgroundColor(getResources().getColor(R.color.Red_500));
            } else {
                iv_U1S2F2.setBackgroundColor(getResources().getColor(R.color.blue_grey_500));
            }

            if (ResultsP1[1][2] == 1) {
                iv_U1S2F3.setBackgroundColor(getResources().getColor(R.color.Light_Green_500));
                punkteP1++;
            } else if (ResultsP1[1][2] == 0) {
                iv_U1S2F3.setBackgroundColor(getResources().getColor(R.color.Red_500));
            } else {
                iv_U1S2F3.setBackgroundColor(getResources().getColor(R.color.blue_grey_500));
            }

            if (ResultsP1[2][0] == 1) {
                iv_U1S3F1.setBackgroundColor(getResources().getColor(R.color.Light_Green_500));
                punkteP1++;
            } else if (ResultsP1[2][0] == 0) {
                iv_U1S3F1.setBackgroundColor(getResources().getColor(R.color.Red_500));
            } else {
                iv_U1S3F1.setBackgroundColor(getResources().getColor(R.color.blue_grey_500));
            }

            if (ResultsP1[2][1] == 1) {
                iv_U1S3F2.setBackgroundColor(getResources().getColor(R.color.Light_Green_500));
                punkteP1++;
            } else if (ResultsP1[2][1] == 0) {
                iv_U1S3F2.setBackgroundColor(getResources().getColor(R.color.Red_500));
            } else {
                iv_U1S3F2.setBackgroundColor(getResources().getColor(R.color.blue_grey_500));
            }

            if (ResultsP1[2][2] == 1) {
                iv_U1S3F3.setBackgroundColor(getResources().getColor(R.color.Light_Green_500));
                punkteP1++;
            } else if (ResultsP1[2][2] == 0) {
                iv_U1S3F3.setBackgroundColor(getResources().getColor(R.color.Red_500));
            } else {
                iv_U1S3F3.setBackgroundColor(getResources().getColor(R.color.blue_grey_500));
            }

            if (ResultsP1[3][0] == 1) {
                iv_U1S4F1.setBackgroundColor(getResources().getColor(R.color.Light_Green_500));
                punkteP1++;
            } else if (ResultsP1[3][0] == 0) {
                iv_U1S4F1.setBackgroundColor(getResources().getColor(R.color.Red_500));
            } else {
                iv_U1S4F1.setBackgroundColor(getResources().getColor(R.color.blue_grey_500));
            }

            if (ResultsP1[3][1] == 1) {
                iv_U1S4F2.setBackgroundColor(getResources().getColor(R.color.Light_Green_500));
                punkteP1++;
            } else if (ResultsP1[3][1] == 0) {
                iv_U1S4F2.setBackgroundColor(getResources().getColor(R.color.Red_500));
            } else {
                iv_U1S4F2.setBackgroundColor(getResources().getColor(R.color.blue_grey_500));
            }

            if (ResultsP1[3][2] == 1) {
                iv_U1S4F3.setBackgroundColor(getResources().getColor(R.color.Light_Green_500));
                punkteP1++;
            } else if (ResultsP1[3][2] == 0) {
                iv_U1S4F3.setBackgroundColor(getResources().getColor(R.color.Red_500));
            } else {
                iv_U1S4F3.setBackgroundColor(getResources().getColor(R.color.blue_grey_500));
            }

            // --- VSPLAYER ---

            if (ResultsP2[0][0] == 1) {
                iv_U2S1F1.setBackgroundColor(getResources().getColor(R.color.Light_Green_500));
                punkteP2++;
            } else if (ResultsP2[0][0] == 0) {
                iv_U2S1F1.setBackgroundColor(getResources().getColor(R.color.Red_500));
            } else {
                iv_U2S1F1.setBackgroundColor(getResources().getColor(R.color.blue_grey_500));
            }

            if (ResultsP2[0][1] == 1) {
                iv_U2S1F2.setBackgroundColor(getResources().getColor(R.color.Light_Green_500));
                punkteP2++;
            } else if (ResultsP2[0][1] == 0) {
                iv_U2S1F2.setBackgroundColor(getResources().getColor(R.color.Red_500));
            } else {
                iv_U2S1F2.setBackgroundColor(getResources().getColor(R.color.blue_grey_500));
            }

            if (ResultsP2[0][2] == 1) {
                iv_U2S1F3.setBackgroundColor(getResources().getColor(R.color.Light_Green_500));
                punkteP2++;
            } else if (ResultsP2[0][2] == 0) {
                iv_U2S1F3.setBackgroundColor(getResources().getColor(R.color.Red_500));
            } else {
                iv_U2S1F3.setBackgroundColor(getResources().getColor(R.color.blue_grey_500));
            }

            if (ResultsP2[1][0] == 1) {
                iv_U2S2F1.setBackgroundColor(getResources().getColor(R.color.Light_Green_500));
                punkteP2++;
            } else if (ResultsP2[1][0] == 0) {
                iv_U2S2F1.setBackgroundColor(getResources().getColor(R.color.Red_500));
            } else {
                iv_U2S2F1.setBackgroundColor(getResources().getColor(R.color.blue_grey_500));
            }

            if (ResultsP2[1][1] == 1) {
                iv_U2S2F2.setBackgroundColor(getResources().getColor(R.color.Light_Green_500));
                punkteP2++;
            } else if (ResultsP2[1][1] == 0) {
                iv_U2S2F2.setBackgroundColor(getResources().getColor(R.color.Red_500));
            } else {
                iv_U2S2F2.setBackgroundColor(getResources().getColor(R.color.blue_grey_500));
            }

            if (ResultsP2[1][2] == 1) {
                iv_U2S2F3.setBackgroundColor(getResources().getColor(R.color.Light_Green_500));
                punkteP2++;
            } else if (ResultsP2[1][2] == 0) {
                iv_U2S2F3.setBackgroundColor(getResources().getColor(R.color.Red_500));
            } else {
                iv_U2S2F3.setBackgroundColor(getResources().getColor(R.color.blue_grey_500));
            }

            if (ResultsP2[2][0] == 1) {
                iv_U2S3F1.setBackgroundColor(getResources().getColor(R.color.Light_Green_500));
                punkteP2++;
            } else if (ResultsP2[2][0] == 0) {
                iv_U2S3F1.setBackgroundColor(getResources().getColor(R.color.Red_500));
            } else {
                iv_U2S3F1.setBackgroundColor(getResources().getColor(R.color.blue_grey_500));
            }

            if (ResultsP2[2][1] == 1) {
                iv_U2S3F2.setBackgroundColor(getResources().getColor(R.color.Light_Green_500));
                punkteP2++;
            } else if (ResultsP2[2][1] == 0) {
                iv_U2S3F2.setBackgroundColor(getResources().getColor(R.color.Red_500));
            } else {
                iv_U2S3F2.setBackgroundColor(getResources().getColor(R.color.blue_grey_500));
            }

            if (ResultsP2[2][2] == 1) {
                iv_U2S3F3.setBackgroundColor(getResources().getColor(R.color.Light_Green_500));
                punkteP2++;
            } else if (ResultsP2[2][2] == 0) {
                iv_U2S3F3.setBackgroundColor(getResources().getColor(R.color.Red_500));
            } else {
                iv_U2S3F3.setBackgroundColor(getResources().getColor(R.color.blue_grey_500));
            }

            if (ResultsP2[3][0] == 1) {
                iv_U2S4F1.setBackgroundColor(getResources().getColor(R.color.Light_Green_500));
                punkteP2++;
            } else if (ResultsP2[3][0] == 0) {
                iv_U2S4F1.setBackgroundColor(getResources().getColor(R.color.Red_500));
            } else {
                iv_U2S4F1.setBackgroundColor(getResources().getColor(R.color.blue_grey_500));
            }

            if (ResultsP2[3][1] == 1) {
                iv_U2S4F2.setBackgroundColor(getResources().getColor(R.color.Light_Green_500));
                punkteP2++;
            } else if (ResultsP2[3][1] == 0) {
                iv_U2S4F2.setBackgroundColor(getResources().getColor(R.color.Red_500));
            } else {
                iv_U2S4F2.setBackgroundColor(getResources().getColor(R.color.blue_grey_500));
            }

            if (ResultsP2[3][2] == 1) {
                iv_U2S4F3.setBackgroundColor(getResources().getColor(R.color.Light_Green_500));
                punkteP2++;
            } else if (ResultsP2[3][2] == 0) {
                iv_U2S4F3.setBackgroundColor(getResources().getColor(R.color.Red_500));
            } else {
                iv_U2S4F3.setBackgroundColor(getResources().getColor(R.color.blue_grey_500));
            }

        } else {
            if (ResultsP2[0][0] == 1) {
                iv_U1S1F1.setBackgroundColor(getResources().getColor(R.color.Light_Green_500));
                punkteP1++;
            } else if (ResultsP2[0][0] == 0) {
                iv_U1S1F1.setBackgroundColor(getResources().getColor(R.color.Red_500));
            } else {
                iv_U1S1F1.setBackgroundColor(getResources().getColor(R.color.blue_grey_500));
            }

            if (ResultsP2[0][1] == 1) {
                iv_U1S1F2.setBackgroundColor(getResources().getColor(R.color.Light_Green_500));
                punkteP1++;
            } else if (ResultsP2[0][1] == 0) {
                iv_U1S1F2.setBackgroundColor(getResources().getColor(R.color.Red_500));
            } else {
                iv_U1S1F2.setBackgroundColor(getResources().getColor(R.color.blue_grey_500));
            }

            if (ResultsP2[0][2] == 1) {
                iv_U1S1F3.setBackgroundColor(getResources().getColor(R.color.Light_Green_500));
                punkteP1++;
            } else if (ResultsP2[0][2] == 0) {
                iv_U1S1F3.setBackgroundColor(getResources().getColor(R.color.Red_500));
            } else {
                iv_U1S1F3.setBackgroundColor(getResources().getColor(R.color.blue_grey_500));
            }

            if (ResultsP2[1][0] == 1) {
                iv_U1S2F1.setBackgroundColor(getResources().getColor(R.color.Light_Green_500));
                punkteP1++;
            } else if (ResultsP2[1][0] == 0) {
                iv_U1S2F1.setBackgroundColor(getResources().getColor(R.color.Red_500));
            } else {
                iv_U1S2F1.setBackgroundColor(getResources().getColor(R.color.blue_grey_500));
            }

            if (ResultsP2[1][1] == 1) {
                iv_U1S2F2.setBackgroundColor(getResources().getColor(R.color.Light_Green_500));
                punkteP1++;
            } else if (ResultsP2[1][1] == 0) {
                iv_U1S2F2.setBackgroundColor(getResources().getColor(R.color.Red_500));
            } else {
                iv_U1S2F2.setBackgroundColor(getResources().getColor(R.color.blue_grey_500));
            }

            if (ResultsP2[1][2] == 1) {
                iv_U1S2F3.setBackgroundColor(getResources().getColor(R.color.Light_Green_500));
                punkteP1++;
            } else if (ResultsP2[1][2] == 0) {
                iv_U1S2F3.setBackgroundColor(getResources().getColor(R.color.Red_500));
            } else {
                iv_U1S2F3.setBackgroundColor(getResources().getColor(R.color.blue_grey_500));
            }

            if (ResultsP2[2][0] == 1) {
                iv_U1S3F1.setBackgroundColor(getResources().getColor(R.color.Light_Green_500));
                punkteP1++;
            } else if (ResultsP2[2][0] == 0) {
                iv_U1S3F1.setBackgroundColor(getResources().getColor(R.color.Red_500));
            } else {
                iv_U1S3F1.setBackgroundColor(getResources().getColor(R.color.blue_grey_500));
            }

            if (ResultsP2[2][1] == 1) {
                iv_U1S3F2.setBackgroundColor(getResources().getColor(R.color.Light_Green_500));
                punkteP1++;
            } else if (ResultsP2[2][1] == 0) {
                iv_U1S3F2.setBackgroundColor(getResources().getColor(R.color.Red_500));
            } else {
                iv_U1S3F2.setBackgroundColor(getResources().getColor(R.color.blue_grey_500));
            }

            if (ResultsP2[2][2] == 1) {
                iv_U1S3F3.setBackgroundColor(getResources().getColor(R.color.Light_Green_500));
                punkteP1++;
            } else if (ResultsP2[2][2] == 0) {
                iv_U1S3F3.setBackgroundColor(getResources().getColor(R.color.Red_500));
            } else {
                iv_U1S3F3.setBackgroundColor(getResources().getColor(R.color.blue_grey_500));
            }

            if (ResultsP2[3][0] == 1) {
                iv_U1S4F1.setBackgroundColor(getResources().getColor(R.color.Light_Green_500));
                punkteP1++;
            } else if (ResultsP2[3][0] == 0) {
                iv_U1S4F1.setBackgroundColor(getResources().getColor(R.color.Red_500));
            } else {
                iv_U1S4F1.setBackgroundColor(getResources().getColor(R.color.blue_grey_500));
            }

            if (ResultsP2[3][1] == 1) {
                iv_U1S4F2.setBackgroundColor(getResources().getColor(R.color.Light_Green_500));
                punkteP1++;
            } else if (ResultsP2[3][1] == 0) {
                iv_U1S4F2.setBackgroundColor(getResources().getColor(R.color.Red_500));
            } else {
                iv_U1S4F2.setBackgroundColor(getResources().getColor(R.color.blue_grey_500));
            }

            if (ResultsP2[3][2] == 1) {
                iv_U1S4F3.setBackgroundColor(getResources().getColor(R.color.Light_Green_500));
                punkteP1++;
            } else if (ResultsP2[3][2] == 0) {
                iv_U1S4F3.setBackgroundColor(getResources().getColor(R.color.Red_500));
            } else {
                iv_U1S4F3.setBackgroundColor(getResources().getColor(R.color.blue_grey_500));
            }

            // --- VSPLAYER ---

            if (ResultsP1[0][0] == 1) {
                iv_U2S1F1.setBackgroundColor(getResources().getColor(R.color.Light_Green_500));
                punkteP2++;
            } else if (ResultsP1[0][0] == 0) {
                iv_U2S1F1.setBackgroundColor(getResources().getColor(R.color.Red_500));
            } else {
                iv_U2S1F1.setBackgroundColor(getResources().getColor(R.color.blue_grey_500));
            }

            if (ResultsP1[0][1] == 1) {
                iv_U2S1F2.setBackgroundColor(getResources().getColor(R.color.Light_Green_500));
                punkteP2++;
            } else if (ResultsP1[0][1] == 0) {
                iv_U2S1F2.setBackgroundColor(getResources().getColor(R.color.Red_500));
            } else {
                iv_U2S1F2.setBackgroundColor(getResources().getColor(R.color.blue_grey_500));
            }

            if (ResultsP1[0][2] == 1) {
                iv_U2S1F3.setBackgroundColor(getResources().getColor(R.color.Light_Green_500));
                punkteP2++;
            } else if (ResultsP1[0][2] == 0) {
                iv_U2S1F3.setBackgroundColor(getResources().getColor(R.color.Red_500));
            } else {
                iv_U2S1F3.setBackgroundColor(getResources().getColor(R.color.blue_grey_500));
            }

            if (ResultsP1[1][0] == 1) {
                iv_U2S2F1.setBackgroundColor(getResources().getColor(R.color.Light_Green_500));
                punkteP2++;
            } else if (ResultsP1[1][0] == 0) {
                iv_U2S2F1.setBackgroundColor(getResources().getColor(R.color.Red_500));
            } else {
                iv_U2S2F1.setBackgroundColor(getResources().getColor(R.color.blue_grey_500));
            }

            if (ResultsP1[1][1] == 1) {
                iv_U2S2F2.setBackgroundColor(getResources().getColor(R.color.Light_Green_500));
                punkteP2++;
            } else if (ResultsP1[1][1] == 0) {
                iv_U2S2F2.setBackgroundColor(getResources().getColor(R.color.Red_500));
            } else {
                iv_U2S2F2.setBackgroundColor(getResources().getColor(R.color.blue_grey_500));
            }

            if (ResultsP1[1][2] == 1) {
                iv_U2S2F3.setBackgroundColor(getResources().getColor(R.color.Light_Green_500));
                punkteP2++;
            } else if (ResultsP1[1][2] == 0) {
                iv_U2S2F3.setBackgroundColor(getResources().getColor(R.color.Red_500));
            } else {
                iv_U2S2F3.setBackgroundColor(getResources().getColor(R.color.blue_grey_500));
            }

            if (ResultsP1[2][0] == 1) {
                iv_U2S3F1.setBackgroundColor(getResources().getColor(R.color.Light_Green_500));
                punkteP2++;
            } else if (ResultsP1[2][0] == 0) {
                iv_U2S3F1.setBackgroundColor(getResources().getColor(R.color.Red_500));
            } else {
                iv_U2S3F1.setBackgroundColor(getResources().getColor(R.color.blue_grey_500));
            }

            if (ResultsP1[2][1] == 1) {
                iv_U2S3F2.setBackgroundColor(getResources().getColor(R.color.Light_Green_500));
                punkteP2++;
            } else if (ResultsP1[2][1] == 0) {
                iv_U2S3F2.setBackgroundColor(getResources().getColor(R.color.Red_500));
            } else {
                iv_U2S3F2.setBackgroundColor(getResources().getColor(R.color.blue_grey_500));
            }

            if (ResultsP1[2][2] == 1) {
                iv_U2S3F3.setBackgroundColor(getResources().getColor(R.color.Light_Green_500));
                punkteP2++;
            } else if (ResultsP1[2][2] == 0) {
                iv_U2S3F3.setBackgroundColor(getResources().getColor(R.color.Red_500));
            } else {
                iv_U2S3F3.setBackgroundColor(getResources().getColor(R.color.blue_grey_500));
            }

            if (ResultsP1[3][0] == 1) {
                iv_U2S4F1.setBackgroundColor(getResources().getColor(R.color.Light_Green_500));
                punkteP2++;
            } else if (ResultsP1[3][0] == 0) {
                iv_U2S4F1.setBackgroundColor(getResources().getColor(R.color.Red_500));
            } else {
                iv_U2S4F1.setBackgroundColor(getResources().getColor(R.color.blue_grey_500));
            }

            if (ResultsP1[3][1] == 1) {
                iv_U2S4F2.setBackgroundColor(getResources().getColor(R.color.Light_Green_500));
                punkteP2++;
            } else if (ResultsP1[3][1] == 0) {
                iv_U2S4F2.setBackgroundColor(getResources().getColor(R.color.Red_500));
            } else {
                iv_U2S4F2.setBackgroundColor(getResources().getColor(R.color.blue_grey_500));
            }

            if (ResultsP1[3][2] == 1) {
                iv_U2S4F3.setBackgroundColor(getResources().getColor(R.color.Light_Green_500));
                punkteP2++;
            } else if (ResultsP1[3][2] == 0) {
                iv_U2S4F3.setBackgroundColor(getResources().getColor(R.color.Red_500));
            } else {
                iv_U2S4F3.setBackgroundColor(getResources().getColor(R.color.blue_grey_500));
            }
        }
        //ENDE BEFUELLEN DER BOOLS
        //</editor-fold>

        //<editor-fold desc="Folding fuer Punkte aktualisieren">
        //Punkte aktualisieren
        TextView tv_punkte = (TextView) findViewById(R.id.Points);
        String punkte_text = punkteP1 + " - " + punkteP2;
        tv_punkte.setText(punkte_text);
        //</editor-fold>

        TextView tv_debug = (TextView) findViewById(R.id.maingame_top_text);
        tv_debug.setText("DEBUG GAMEID: " + gameid + ", STAGE: " + activeStage);
        extragameid = Integer.valueOf(gameid);

        Button b_stage1 = (Button) findViewById(R.id.stage1PlayButton);
        Button b_stage2 = (Button) findViewById(R.id.stage2PlayButton);
        Button b_stage3 = (Button) findViewById(R.id.stage3PlayButton);
        Button b_stage4 = (Button) findViewById(R.id.stage4PlayButton);

        //<editor-fold desc="Folding fuer Logik und Auswahl, ob man an der Reihe ist">
        if ((myusernr == 1 && activeplayer == 1 && activeStage == 1) || (myusernr == 1 && activeplayer == 1 && activeStage == 3)) {
            switch (activeStage) {
                case 1:
                    b_stage1.setEnabled(true);
                    b_stage1.setText("DU BIST DRAN");
                    b_stage2.setClickable(false);
                    b_stage2.setText("");
                    b_stage3.setClickable(false);
                    b_stage3.setText("");
                    b_stage4.setClickable(false);
                    b_stage4.setText("");

                    iv_U1S1F1.setBackgroundColor(getResources().getColor(R.color.amber_500));
                    iv_U1S1F2.setBackgroundColor(getResources().getColor(R.color.amber_500));
                    iv_U1S1F3.setBackgroundColor(getResources().getColor(R.color.amber_500));
                    break;
                case 3:
                    b_stage1.setEnabled(false);
                    b_stage1.setText("");
                    b_stage2.setClickable(false);
                    b_stage2.setText("");
                    b_stage3.setClickable(true);
                    b_stage3.setText("DU BIST DRAN");
                    b_stage4.setClickable(false);
                    b_stage4.setText("");

                    iv_U1S3F1.setBackgroundColor(getResources().getColor(R.color.amber_500));
                    iv_U1S3F2.setBackgroundColor(getResources().getColor(R.color.amber_500));
                    iv_U1S3F3.setBackgroundColor(getResources().getColor(R.color.amber_500));
                    break;
                default:
                    b_stage1.setEnabled(false);
                    b_stage1.setText("");
                    b_stage2.setClickable(false);
                    b_stage2.setText("");
                    b_stage3.setClickable(false);
                    b_stage3.setText("");
                    b_stage4.setClickable(false);
                    b_stage4.setText("");
                    break;
            }
        } else if ((myusernr == 2 && activeplayer == 2 && activeStage == 2) || (myusernr == 2 && activeplayer == 2 && activeStage == 4)) {
            switch (activeStage) {
                case 2:
                    b_stage1.setEnabled(false);
                    b_stage1.setText("");
                    b_stage2.setClickable(true);
                    b_stage2.setText("DU BIST DRAN");
                    b_stage3.setClickable(false);
                    b_stage3.setText("");
                    b_stage4.setClickable(false);
                    b_stage4.setText("");

                    iv_U1S2F1.setBackgroundColor(getResources().getColor(R.color.amber_500));
                    iv_U1S2F2.setBackgroundColor(getResources().getColor(R.color.amber_500));
                    iv_U1S2F3.setBackgroundColor(getResources().getColor(R.color.amber_500));
                    break;
                case 4:
                    b_stage1.setEnabled(false);
                    b_stage1.setText("");
                    b_stage2.setClickable(false);
                    b_stage2.setText("");
                    b_stage3.setClickable(false);
                    b_stage3.setText("");
                    b_stage4.setClickable(true);
                    b_stage4.setText("DU BIST DRAN");

                    iv_U1S4F1.setBackgroundColor(getResources().getColor(R.color.amber_500));
                    iv_U1S4F2.setBackgroundColor(getResources().getColor(R.color.amber_500));
                    iv_U1S4F3.setBackgroundColor(getResources().getColor(R.color.amber_500));
                    break;
                default:
                    b_stage1.setEnabled(false);
                    b_stage1.setText("");
                    b_stage2.setClickable(false);
                    b_stage2.setText("");
                    b_stage3.setClickable(false);
                    b_stage3.setText("");
                    b_stage4.setClickable(false);
                    b_stage4.setText("");
                    break;
            }

        } else if ((myusernr == 2 && activeplayer == 2 && activeStage == 1) || (myusernr == 2 && activeplayer == 2 && activeStage == 3)) {
            switch (activeStage) {
                case 1:
                    b_stage1.setEnabled(true);
                    b_stage1.setText("DU BIST DRAN");
                    b_stage2.setClickable(false);
                    b_stage2.setText("");
                    b_stage3.setClickable(false);
                    b_stage3.setText("");
                    b_stage4.setClickable(false);
                    b_stage4.setText("");

                    iv_U1S1F1.setBackgroundColor(getResources().getColor(R.color.amber_500));
                    iv_U1S1F2.setBackgroundColor(getResources().getColor(R.color.amber_500));
                    iv_U1S1F3.setBackgroundColor(getResources().getColor(R.color.amber_500));
                    break;
                case 3:
                    b_stage1.setEnabled(false);
                    b_stage1.setText("");
                    b_stage2.setClickable(false);
                    b_stage2.setText("");
                    b_stage3.setClickable(true);
                    b_stage3.setText("DU BIST DRAN");
                    b_stage4.setClickable(false);
                    b_stage4.setText("");

                    iv_U1S3F1.setBackgroundColor(getResources().getColor(R.color.amber_500));
                    iv_U1S3F2.setBackgroundColor(getResources().getColor(R.color.amber_500));
                    iv_U1S3F3.setBackgroundColor(getResources().getColor(R.color.amber_500));
                    break;
                default:
                    b_stage1.setEnabled(false);
                    b_stage1.setText("");
                    b_stage2.setClickable(false);
                    b_stage2.setText("");
                    b_stage3.setClickable(false);
                    b_stage3.setText("");
                    b_stage4.setClickable(false);
                    b_stage4.setText("");
                    break;
            }
        } else if ((myusernr == 1 && activeplayer == 1 && activeStage == 2) || (myusernr == 1 && activeplayer == 1 && activeStage == 4)) {
            switch (activeStage) {
                case 2:
                    b_stage1.setEnabled(false);
                    b_stage1.setText("");
                    b_stage2.setClickable(true);
                    b_stage2.setText("DU BIST DRAN");
                    b_stage3.setClickable(false);
                    b_stage3.setText("");
                    b_stage4.setClickable(false);
                    b_stage4.setText("");

                    iv_U1S2F1.setBackgroundColor(getResources().getColor(R.color.amber_500));
                    iv_U1S2F2.setBackgroundColor(getResources().getColor(R.color.amber_500));
                    iv_U1S2F3.setBackgroundColor(getResources().getColor(R.color.amber_500));
                    break;
                case 4:
                    b_stage1.setEnabled(false);
                    b_stage1.setText("");
                    b_stage2.setClickable(false);
                    b_stage2.setText("");
                    b_stage3.setClickable(false);
                    b_stage3.setText("");
                    b_stage4.setClickable(true);
                    b_stage4.setText("DU BIST DRAN");

                    iv_U1S4F1.setBackgroundColor(getResources().getColor(R.color.amber_500));
                    iv_U1S4F2.setBackgroundColor(getResources().getColor(R.color.amber_500));
                    iv_U1S4F3.setBackgroundColor(getResources().getColor(R.color.amber_500));
                    break;
                default:
                    b_stage1.setEnabled(false);
                    b_stage1.setText("");
                    b_stage2.setClickable(false);
                    b_stage2.setText("");
                    b_stage3.setClickable(false);
                    b_stage3.setText("");
                    b_stage4.setClickable(false);
                    b_stage4.setText("");
                    break;
            }

        } else {
            b_stage1.setEnabled(false);
            b_stage1.setText("");
            b_stage2.setClickable(false);
            b_stage2.setText("");
            b_stage3.setClickable(false);
            b_stage3.setText("");
            b_stage4.setClickable(false);
            b_stage4.setText("");
        }
        //</editor-fold>

        /*for(int y = 0; y < ResultsP1.length; y++) {
            Log.i("FLOW",ResultsP1[y][0]+","+ResultsP1[y][1]+","+ResultsP1[y][2]+" | "+ResultsP2[y][0]+","+ResultsP2[y][1]+","+ResultsP2[y][2]);
        }*/

    }

    public void bla(View v) {
        Log.i("FLOW", "myusernr:" + myusernr + " activeplayer:" + activeplayer + "  activeStage:" + activeStage);
        if ((myusernr == 1 && activeStage == 1) || (myusernr == 1 && activeStage == 3) || (myusernr == 2 && activeStage == 2) || (myusernr == 2 && activeStage == 4)) {
            Intent intent = new Intent(this.getApplicationContext(), SubChoices_challenge.class);
            intent.putExtra("game", gameid);
            intent.putExtra("hkid", hauptkategorie);
            intent.putExtra("username", myusername);
            intent.putExtra("stage", activeStage);
            startActivity(intent);
        } else if ((myusernr == 2 && activeplayer == 2 && activeStage == 1) || (myusernr == 2 && activeplayer == 2 && activeStage == 3) || (myusernr == 1 && activeplayer == 1 && activeStage == 2) || (myusernr == 1 && activeplayer == 1 && activeStage == 4)) {
            Intent intent = new Intent(this.getApplicationContext(), ChallengeQuizActivity.class);
            intent.putExtra("game", gameid);
            intent.putExtra("stage", activeStage);
            intent.putExtra("username", myusername);
            intent.putExtra("subkategorie", "Subkategorie");
            startActivity(intent);
        }
        //Toast.makeText(getApplicationContext(),"Stage: "+activeStage+" ",Toast.LENGTH_SHORT).show();
    }

    /**
     * Background Async Task to Load all INBOX messages by making HTTP Request
     */
    class LoadInbox extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //mSwipeRefreshLayout.setRefreshing(true);
            pDialog = new ProgressDialog(MainGameScreen.this);
            pDialog.setMessage("Lade Spiel ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }


        /**
         * getting Inbox JSON
         */
        protected String doInBackground(String... args) {

            try {
                JSONObject json = JsonParsingFunctions.readJsonFromUrl(GAMEDETAIL_URL + "?gameid=" + dbgameid);
                inbox = json.getJSONArray(TAG_MESSAGES);
                // looping through All messages
                for (int i = 0; i < inbox.length(); i++) {
                    JSONObject c = inbox.getJSONObject(i);

                    // Storing each json item in variable
                    gameid = c.getInt(TAG_GAMEID);
                    activeStage = c.getInt(TAG_STATGE);
                    hauptkategorie = c.getInt(TAG_IDKATEGORIE);
                    activeplayer = c.getInt(TAG_ACTIVEPLAYER);

                    String username1 = c.getString(TAG_USERNAME1);
                    String username2 = c.getString(TAG_USERNAME2);


                    for (int y = 0; y < ResultsP1.length; y++) {

                        ResultsP1[y][0] = c.getInt("U1S" + (y + 1) + "F1");
                        ResultsP1[y][1] = c.getInt("U1S" + (y + 1) + "F2");
                        ResultsP1[y][2] = c.getInt("U1S" + (y + 1) + "F3");

                        ResultsP2[y][0] = c.getInt("U2S" + (y + 1) + "F1");
                        ResultsP2[y][1] = c.getInt("U2S" + (y + 1) + "F2");
                        ResultsP2[y][2] = c.getInt("U2S" + (y + 1) + "F3");

                        Log.i("FLOW", ResultsP1[y][0] + "," + ResultsP1[y][1] + "," + ResultsP1[y][2] + " | " + ResultsP2[y][0] + "," + ResultsP2[y][1] + "," + ResultsP2[y][2]);
                    }


                    // creating new HashMap
                    HashMap<String, String> map = new HashMap<String, String>();

                    // adding each child node to HashMap key => value
                    //map.put(TAG_GAMEID, gameid);
                    //map.put(TAG_USERNAME1, username1);
                    //map.put(TAG_USERNAME2, username2);


                    // adding HashList to ArrayList
                    //gameDetailList.add(map);


                    if (username1.equalsIgnoreCase(dbusername)) {
                        vsusername = username2;
                        myusername = username1;
                        myusernr = 1;
                    } else if (username2.equalsIgnoreCase(dbusername)) {
                        vsusername = username1;
                        myusername = username2;
                        myusernr = 2;
                    }

                }
                // Test onClick listener


            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * *
         */
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all products
            pDialog.dismiss();
            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {

                    //TODO: SACHEN BEFUELLEN


                }
            });
            if (mSwipeRefreshLayout.isRefreshing())
                mSwipeRefreshLayout.setRefreshing(false);
            reloadGameDetails();
        }

    }
}
