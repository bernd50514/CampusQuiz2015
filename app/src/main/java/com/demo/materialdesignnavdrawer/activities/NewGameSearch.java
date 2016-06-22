package com.demo.materialdesignnavdrawer.activities;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

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

import campusquizregandlogdesign.com.example.helper.JsonParsingFunctions;
import campusquizregandlogdesign.com.example.helper.SQLiteHandler;
import campusquizregandlogdesign.com.example.helper.SQLiteQuestionHandler;

public class NewGameSearch extends ActionBarActivity {
    private static final String FACHBEREICHE_URL = "http://ps15server.cloudapp.net:8080/useraccount/InfoService/getFb";
    // ALL JSON node names for Fachbereiche
    private static final String TAG_MESSAGES = "Fachbereiche";
    private static final String TAG_FB_ID = "FB_ID";
    private static final String TAG_KURZNAME = "Kurzname";
    ArrayList<HashMap<String, String>> fachbereicheList;
    ArrayList<HashMap<String, String>> questionsList;
    // products JSONArray
    JSONArray inbox = null;
    JSONArray jsonQuestionsArray = null;
    ListView list;
    String myusername = null;
    // Progress Dialog
    private ProgressDialog pDialog;
    // Creating JSON Parser object
    private String getFb_ID;
    private SQLiteQuestionHandler db = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.open_games_new_game);
        db = new SQLiteQuestionHandler(getApplicationContext());
        // Hashmap for ListView
        fachbereicheList = new ArrayList<HashMap<String, String>>();
        // Loading INBOX in Background Thread
        new LoadInbox().execute();

        initialise();

        SQLiteHandler db = new SQLiteHandler(getApplicationContext());
        HashMap<String, String> user = db.getUserDetails();
        myusername = user.get("name");
    }

    private void initialise() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(false);
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            getSupportActionBar().setTitle("Neue Challenge starten");
            getSupportActionBar().setDisplayUseLogoEnabled(false);
        }

    }

    public void invokePlaceNewGameRequest(RequestParams params) {

        //Toast.makeText(getApplicationContext(), params.toString(), Toast.LENGTH_LONG).show();

        AsyncHttpClient client = new AsyncHttpClient();
        client.get("http://ps15server.cloudapp.net:8080/useraccount/GameMatchingService/placeGameRequest", params, new AsyncHttpResponseHandler() {
            // When the response returned by REST has Http response code '200'
            @Override
            public void onSuccess(String response) {
                // Hide Progress Dialog
                finish();
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
            pDialog = new ProgressDialog(NewGameSearch.this);
            pDialog.setMessage("Lade Hauptkategorien ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        /**
         * getting Inbox JSON
         */
        protected String doInBackground(String... args) {

            try {
                JSONObject json = JsonParsingFunctions.readJsonFromUrl(FACHBEREICHE_URL);
                inbox = json.getJSONArray(TAG_MESSAGES);
                // looping through All messages
                for (int i = 0; i < inbox.length(); i++) {
                    JSONObject c = inbox.getJSONObject(i);

                    // Storing each json item in variable
                    String id = c.getString(TAG_FB_ID);
                    String kurzname = c.getString(TAG_KURZNAME);
                    //String subject = c.getString(TAG_SUBJECT);
                    //String date = c.getString(TAG_DATE);

                    // creating new HashMap
                    HashMap<String, String> map = new HashMap<String, String>();

                    // adding each child node to HashMap key => value
                    map.put(TAG_FB_ID, id);
                    map.put(TAG_KURZNAME, kurzname);

                    Log.i("Test", kurzname);
                    // adding HashList to ArrayList
                    fachbereicheList.add(map);
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
                            NewGameSearch.this, fachbereicheList,
                            R.layout.open_games_new_game_item, new String[]{TAG_KURZNAME},
                            new int[]{R.id.open_games_new_game_item});
                    // updating listview
                    //  setListAdapter(adapter);

                    list = (ListView) findViewById(R.id.open_games_new_game_list);
                    list.setAdapter(adapter);
                    list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                        @Override
                        public void onItemClick(AdapterView<?> parent, View view,
                                                int position, long id) {
                            getFb_ID = fachbereicheList.get(+position).get("FB_ID").toString(); // will transfer as param to invoke webservice
                            RequestParams params = new RequestParams();

                            params.put("fachbereich", getFb_ID);
                            params.put("username", myusername);
                            params.put("hours", "72");
                            invokePlaceNewGameRequest(params);
                            //Toast.makeText(getApplicationContext(),params.toString(),Toast.LENGTH_LONG).show();
                        }
                    });

                }
            });

        }

    }
}
