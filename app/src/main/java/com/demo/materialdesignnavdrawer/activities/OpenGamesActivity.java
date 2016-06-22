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
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.demo.materialdesignnavdrawer.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import campusquizregandlogdesign.com.example.helper.JsonParsingFunctions;
import campusquizregandlogdesign.com.example.helper.SQLiteHandler;
import campusquizregandlogdesign.com.example.helper.SQLiteQuestionHandler;

/**
 * Created by Administrator on 10.06.2015.
 */
public class OpenGamesActivity extends AppCompatActivity {
    //  JSON url
    private static final String OPENGAMES_URL = "http://ps15server.cloudapp.net:8080/useraccount/PlayerInfoServices/getOpenGames";
    // ALL JSON node names for OPENGAMES
    private static final String TAG_MESSAGES = "PlayerOpenGames";
    private static final String TAG_FBID = "fb_id";
    private static final String TAG_USERNAME1 = "username1";
    private static final String TAG_USERNAME2 = "username2";
    private static final String TAG_EXPDATE = "exp_date";
    private static final String TAG_GAMEID = "game_id";
    private static final String TAG_ACTIVEPLAYER = "activeplayer";
    private static final String TAG_VSUSER = "";
    ArrayList<HashMap<String, String>> openGamesList;
    // products JSONArray
    JSONArray inbox = null;
    JSONArray JSONOpenGamesArray = null;
    ListView list;
    SwipeRefreshLayout mSwipeRefreshLayout;
    LoadInbox In = new LoadInbox();
    String myusername = null;
    // Progress Dialog
    private ProgressDialog pDialog;
    // Creating JSON Parser object
    private String JSONOpenGames;
    private SQLiteQuestionHandler db = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.open_games_main);


        db = new SQLiteQuestionHandler(getApplicationContext());
        // Hashmap for ListView
        openGamesList = new ArrayList<HashMap<String, String>>();
        // Loading INBOX in Background Thread


        initialize();

        List<HashMap<String, String>> aList = new ArrayList<HashMap<String, String>>();
        SQLiteHandler db = new SQLiteHandler(getApplicationContext());
        HashMap<String, String> user = db.getUserDetails();
        myusername = user.get("name");

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.open_games_swipe_container);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //Toast.makeText(getApplicationContext(), "YEEEAHH!!!", Toast.LENGTH_SHORT).show();
                //In.execute();
                openGamesList = new ArrayList<HashMap<String, String>>();
                new LoadInbox().execute();

            }
        });
        In.execute();
    }

    /**
     * Create, bind and set up the resources
     */
    private void initialize() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(false);
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            getSupportActionBar().setTitle("Aktive Spiele");
            getSupportActionBar().setDisplayUseLogoEnabled(false);
        }

    }

    private void startGameMainScreen(String gameid, String u1, String u2) {
        Intent intent = new Intent(this.getApplicationContext(), MainGameScreen.class);
        intent.putExtra("GAMEID", gameid);
        intent.putExtra("USERNAME1", u1);
        intent.putExtra("USERNAME2", u2);
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
            pDialog = new ProgressDialog(OpenGamesActivity.this);
            pDialog.setMessage("Lade offene Spiele ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }


        /**
         * getting Inbox JSON
         */
        protected String doInBackground(String... args) {

            try {
                JSONObject json = JsonParsingFunctions.readJsonFromUrl(OPENGAMES_URL + "?username=" + myusername);
                inbox = json.getJSONArray(TAG_MESSAGES);
                // looping through All messages
                for (int i = 0; i < inbox.length(); i++) {
                    JSONObject c = inbox.getJSONObject(i);

                    // Storing each json item in variable
                    String id = c.getString(TAG_GAMEID);
                    String username1 = c.getString(TAG_USERNAME1);
                    String username2 = c.getString(TAG_USERNAME2);
                    //String subject = c.getString(TAG_SUBJECT);
                    //String date = c.getString(TAG_DATE);

                    // creating new HashMap
                    HashMap<String, String> map = new HashMap<String, String>();

                    // adding each child node to HashMap key => value
                    map.put(TAG_GAMEID, id);
                    map.put(TAG_USERNAME1, username1);
                    map.put(TAG_USERNAME2, username2);

                    String vsuser = "";
                    if (username1.equalsIgnoreCase(myusername)) {
                        vsuser = username2;
                    } else {
                        vsuser = username1;
                    }

                    map.put(TAG_VSUSER, vsuser);
                    //map.put(TAG_SUBJECT, subject);
                    // map.put(TAG_DATE, date);

                    // adding HashList to ArrayList
                    openGamesList.add(map);
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


                    ListAdapter adapter = new SimpleAdapter(
                            OpenGamesActivity.this, openGamesList,
                            R.layout.open_games_item, new String[]{TAG_VSUSER},
                            new int[]{R.id.open_game_vsPlayername});
                    // updating listview
                    //  setListAdapter(adapter);

                    list = (ListView) findViewById(R.id.vsGamesList);
                    list.setAdapter(adapter);
                    list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                        @Override
                        public void onItemClick(AdapterView<?> parent, View view,
                                                int position, long id) {
                            String game_id = openGamesList.get(+position).get("game_id");
                            String u1 = openGamesList.get(+position).get("username1");
                            String u2 = openGamesList.get(+position).get("username2");
                            Log.i("FLOW", "GAMEID: " + game_id + "  |   " + u1 + "  |   " + u2);
                            //getFb_ID = openGamesList.get(+position).get("FB_ID").toString(); // will transfer as param to invoke webservice
                            //RequestParams params = new RequestParams();

                            //params.put("fachbereich", getFb_ID);
                            //params.put("menge", "6");
                            //invokeWebService(params);


                            startGameMainScreen(game_id, u1, u2);
                        }
                    });

                }
            });
            if (mSwipeRefreshLayout.isRefreshing())
                mSwipeRefreshLayout.setRefreshing(false);
        }

    }
}
