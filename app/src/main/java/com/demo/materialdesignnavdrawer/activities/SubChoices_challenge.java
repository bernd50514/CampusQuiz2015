package com.demo.materialdesignnavdrawer.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.demo.materialdesignnavdrawer.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import campusquizregandlogdesign.com.example.helper.JsonParsingFunctions;
import trainingsspiel.ChallengeQuizActivity;

public class SubChoices_challenge extends AppCompatActivity {

    //  JSON url
    private static final String CHOICES_URL = "http://ps15server.cloudapp.net:8080/useraccount/GamePlayService/getSubcategoryChoices";
    // ALL JSON node names for CHOICES
    private static final String TAG_MESSAGES = "Subkategorien";
    private static final String TAG_DZID = "DZ_ID";
    private static final String TAG_Vollname = "Vollname";
    //Webservice um Fragen zu generieren
    private static final String GENERATEQUESTIONS_URL = "http://ps15server.cloudapp.net:8080/useraccount/GamePlayService/generateQuestions";
    static String[][] choices = new String[4][2];
    final LoadInbox2 In2 = new LoadInbox2();
    String myusername = null, subkategorie = null;
    int hauptkategorieID = 0, gameid = 0, stage = 0, endchoice = 0;
    // products JSONArray
    JSONArray inbox = null;
    LoadInbox In = new LoadInbox();
    // Progress Dialog
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_quiz2);

        Bundle bundle = getIntent().getExtras();
        gameid = bundle.getInt("game");
        hauptkategorieID = bundle.getInt("hkid");
        myusername = bundle.getString("username");
        stage = bundle.getInt("stage");

        initialize();

        Log.i("FLOW", String.valueOf(gameid));

        TextView tv_debug = (TextView) findViewById(R.id.subchoicesdebugtext);
        tv_debug.setText("USERNAME: " + myusername + ", HK_ID: " + hauptkategorieID + ", GAMEID: " + gameid);

        In.execute();
        Log.i("FLOW", String.valueOf(gameid));
    }

    private void initialize() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setDisplayShowHomeEnabled(false);
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            getSupportActionBar().setTitle(R.string.toolbar_title_subchoices);
            getSupportActionBar().setDisplayUseLogoEnabled(false);
        }
    }

    public void fillInData() {
        final Button b_choice1 = (Button) findViewById(R.id.buttonChoice1);
        final Button b_choice2 = (Button) findViewById(R.id.buttonChoice2);
        final Button b_choice3 = (Button) findViewById(R.id.buttonChoice3);
        final Button b_choice4 = (Button) findViewById(R.id.buttonChoice4);


        b_choice1.setText(choices[0][1]);
        b_choice2.setText(choices[1][1]);
        b_choice3.setText(choices[2][1]);
        b_choice4.setText(choices[3][1]);

        b_choice1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                b_choice1.setBackgroundColor(getResources().getColor(R.color.amber_500));
                subkategorie = choices[0][1];
                prepareGame(0, Integer.valueOf(choices[0][0]));
            }
        });
        b_choice2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                b_choice2.setBackgroundColor(getResources().getColor(R.color.amber_500));
                subkategorie = choices[1][1];
                prepareGame(1, Integer.valueOf(choices[1][0]));
            }
        });
        b_choice3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                b_choice3.setBackgroundColor(getResources().getColor(R.color.amber_500));
                subkategorie = choices[2][1];
                prepareGame(2, Integer.valueOf(choices[2][0]));
            }
        });
        b_choice4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                b_choice4.setBackgroundColor(getResources().getColor(R.color.amber_500));
                subkategorie = choices[3][1];
                prepareGame(3, Integer.valueOf(choices[3][0]));
            }
        });
    }

    public void prepareGame(int num, int dzid) {

        //Toast.makeText(getApplicationContext(),"Bernds Part - Spiel starten "+choices[num][1],Toast.LENGTH_SHORT).show();

        // generateQuestions stage,gameid,dz_id
        endchoice = Integer.valueOf(choices[num][0]);
        Log.i("FLOW", num + "Starte Game " + choices[num][1] + "endchoice:" + endchoice);
        In2.execute();
    }

    public void startGame() {
        //BERNDS PART
        Intent intent = new Intent(this.getApplicationContext(), ChallengeQuizActivity.class);
        intent.putExtra("game", gameid);
        intent.putExtra("stage", stage);
        intent.putExtra("username", myusername);
        intent.putExtra("subkategorie", subkategorie);
        startActivity(intent);
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
            pDialog = new ProgressDialog(SubChoices_challenge.this);
            pDialog.setMessage("Lade Wahlmoeglichkeiten ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }


        /**
         * getting Inbox JSON
         */
        protected String doInBackground(String... args) {

            try {
                JSONObject json = JsonParsingFunctions.readJsonFromUrl(CHOICES_URL + "?hauptkategorie=" + hauptkategorieID);
                inbox = json.getJSONArray(TAG_MESSAGES);
                // looping through All messages
                for (int i = 0; i < inbox.length(); i++) {
                    JSONObject c = inbox.getJSONObject(i);

                    // Storing each json item in variable
                    String dz_id = c.getString(TAG_DZID);
                    String vollname = c.getString(TAG_Vollname);
                    choices[i][0] = dz_id;
                    choices[i][1] = vollname;

                    Log.i("FLOW", choices[i][0] + "   -   " + choices[i][1]);

                }

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

                }
            });
            fillInData();
        }

    }

    /**
     * Background Async Task to Load all INBOX messages by making HTTP Request
     */
    class LoadInbox2 extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //mSwipeRefreshLayout.setRefreshing(true);
            pDialog = new ProgressDialog(SubChoices_challenge.this);
            pDialog.setMessage("Uebertrage...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }


        /**
         * getting Inbox JSON
         */
        protected String doInBackground(String... args) {
            Log.i("FLOW", GENERATEQUESTIONS_URL + "?stage=" + stage + "&game_id=" + gameid + "&subkategorie=" + endchoice);
            try {
                JSONObject json = JsonParsingFunctions.readJsonFromUrl(GENERATEQUESTIONS_URL + "?stage=" + stage + "&game_id=" + gameid + "&subkategorie=" + endchoice);
                inbox = json.getJSONArray("");
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

                }
            });
            startGame();
        }

    }
}
