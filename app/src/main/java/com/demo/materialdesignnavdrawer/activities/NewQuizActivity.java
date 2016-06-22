package com.demo.materialdesignnavdrawer.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.demo.materialdesignnavdrawer.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

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


public class NewQuizActivity extends AppCompatActivity {
    //  JSON url
    private static final String OPENGAMES_URL = "http://ps15server.cloudapp.net:8080/useraccount/GameMatchingService/getAllOpenGames";
    // ALL JSON node names for OPENGAMES
    private static final String TAG_MESSAGES = "OpenGames";
    private static final String TAG_FBID = "fb_id";
    private static final String TAG_KURZNAME = "Kurzname";
    private static final String TAG_VOLLNAME = "Vollname";
    private static final String TAG_EXPDATE = "exp_date";
    private static final String TAG_SEARCHID = "search_id";
    private static final String TAG_USERNAME = "username";
    Toolbar toolbar;
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
        setContentView(R.layout.challenge_main);
        db = new SQLiteQuestionHandler(getApplicationContext());
        // Hashmap for ListView
        openGamesList = new ArrayList<HashMap<String, String>>();
        // Loading INBOX in Background Thread

        //new LoadInbox().execute();


        initialise();

        List<HashMap<String, String>> aList = new ArrayList<HashMap<String, String>>();
        SQLiteHandler db = new SQLiteHandler(getApplicationContext());
        HashMap<String, String> user = db.getUserDetails();
        myusername = user.get("name");

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);

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

    /*
    @Override
    public void onResume(){
        super.onResume();
        openGamesList = new ArrayList<HashMap<String, String>>();
        new LoadInbox().execute();
    }
    */

    private void initialise() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(false);
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            getSupportActionBar().setTitle("Challenge starten");
            getSupportActionBar().setDisplayUseLogoEnabled(false);
        }

    }

    public void startNewGame(View view) {

        Intent intent = new Intent(NewQuizActivity.this, NewGameSearch.class);
        startActivity(intent);

        //Toast.makeText(getApplicationContext(),"TEST",Toast.LENGTH_SHORT).show();
        //INVOKE WS
    }

    public void invokeRegisterDirectGame(RequestParams params) {

        //Toast.makeText(getApplicationContext(), params.toString(), Toast.LENGTH_LONG).show();

        AsyncHttpClient client = new AsyncHttpClient();
        client.get("http://ps15server.cloudapp.net:8080/useraccount/GameMatchingService/registerDirectGame", params, new AsyncHttpResponseHandler() {
            // When the response returned by REST has Http response code '200'
            @Override
            public void onSuccess(String response) {
                // Hide Progress Dialog

                openGamesList = new ArrayList<HashMap<String, String>>();
                new LoadInbox().execute();
                pDialog.hide();
            }

            // When the response returned by REST has Http response code other than '200'
            @Override
            public void onFailure(int statusCode, Throwable error,
                                  String content) {
                // Hide Progress Dialog
                pDialog.hide();
                // When Http response code is '404'

            }
        });
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
            pDialog = new ProgressDialog(NewQuizActivity.this);
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
                    String id = c.getString(TAG_SEARCHID);
                    String kurzname = c.getString(TAG_KURZNAME);
                    String username = c.getString(TAG_USERNAME);
                    //String subject = c.getString(TAG_SUBJECT);
                    //String date = c.getString(TAG_DATE);

                    // creating new HashMap
                    HashMap<String, String> map = new HashMap<String, String>();

                    // adding each child node to HashMap key => value
                    map.put(TAG_SEARCHID, id);
                    map.put(TAG_KURZNAME, kurzname);
                    map.put(TAG_USERNAME, username);
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
                            NewQuizActivity.this, openGamesList,
                            R.layout.challenge_search_item, new String[]{TAG_KURZNAME, TAG_USERNAME},
                            new int[]{R.id.challenge_search_item, R.id.challenge_item_player});
                    // updating listview
                    //  setListAdapter(adapter);

                    list = (ListView) findViewById(R.id.opGameList);
                    list.setAdapter(adapter);
                    list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                        @Override
                        public void onItemClick(AdapterView<?> parent, View view,
                                                int position, long id) {
                            String game_id = openGamesList.get(+position).get("search_id");
                            Toast.makeText(NewQuizActivity.this, "Trete Spiel(" + game_id + ") bei...", Toast.LENGTH_SHORT).show();


                            //getFb_ID = openGamesList.get(+position).get("FB_ID").toString(); // will transfer as param to invoke webservice
                            //RequestParams params = new RequestParams();

                            //params.put("fachbereich", getFb_ID);
                            //params.put("menge", "6");
                            //invokeWebService(params);

                            RequestParams params = new RequestParams();
                            params.put("gameID", game_id);
                            params.put("username", myusername);
                            invokeRegisterDirectGame(params);
                        }
                    });

                }
            });
            if (mSwipeRefreshLayout.isRefreshing())
                mSwipeRefreshLayout.setRefreshing(false);
        }

    }


}

