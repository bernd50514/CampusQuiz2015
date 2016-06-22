package com.demo.materialdesignnavdrawer.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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

import campusquizregandlogdesign.com.example.helper.JsonParsingFunctions;
import campusquizregandlogdesign.com.example.helper.SQLiteQuestionHandler;
import trainingsspiel.AnalogyExaminationActivity;

/**
 * Created by Administrator on 29.06.2015.
 */
public class ChooseDisciplineActivity extends AppCompatActivity {
    //  JSON url
    private static final String QUESTION_URL = "http://ps15server.cloudapp.net:8080/useraccount/QuestionService/getQuestions";
    private static final String FACHBEREICHE_URL = "http://ps15server.cloudapp.net:8080/useraccount/InfoService/getFb";
    // ALL JSON node names for Fachbereiche
    private static final String TAG_MESSAGES = "Fachbereiche";
    private static final String TAG_FB_ID = "FB_ID";
    private static final String TAG_KURZNAME = "Kurzname";
    // ALL JSON node names for Question
    private static final String TAG_MESSAGES_2 = "Questions";
    private static final String TAG_FRAGE = "Frage";
    private static final String TAG_NAME_SUBKATEGORIEN = "Kurzname";
    private static final String TAG_FRAGEN_ID = "Fragen_ID";
    private static final String TAG_ANTWORT_1 = "Antwort_1";
    private static final String TAG_ANTWORT_2 = "Antwort_2";
    private static final String TAG_ANTWORT_3 = "Antwort_3";
    private static final String TAG_ANTWORT_4 = "Antwort_4";
    private static final String TAG_RIGHTANSWER = "Antwort_Richtig";
    ArrayList<HashMap<String, String>> fachbereicheList;
    ArrayList<HashMap<String, String>> questionsList;
    // products JSONArray
    JSONArray inbox = null;
    JSONArray jsonQuestionsArray = null;
    ListView list;
    // Progress Dialog
    private ProgressDialog pDialog;
    // Creating JSON Parser object
    private String getFb_ID;
    private SQLiteQuestionHandler db = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.discipline_list);
        db = new SQLiteQuestionHandler(getApplicationContext());
        // Hashmap for ListView
        fachbereicheList = new ArrayList<HashMap<String, String>>();
        // Umwandlung der Fachbereiche aus Webservice im Hintergrund
        new LoadInbox().execute();
        initialise();
    }

    private void initialise() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(false);
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            getSupportActionBar().setTitle("Auswahl des Fachbereichs");
            getSupportActionBar().setDisplayUseLogoEnabled(false);

        }

    }

    public void invokeWebService(RequestParams params) {
        AsyncHttpClient client = new AsyncHttpClient();
        client.get("http://ps15server.cloudapp.net:8080/useraccount/QuestionService/getQuestions", params, new AsyncHttpResponseHandler() {
            // When the response returned by REST has Http response code '200'
            @Override
            public void onSuccess(String response) {
                // Hide Progress Dialog
                pDialog.hide();
                try {
                    // JSON Object
                    //TODO: here should varify wether any question exist inside the jsonobject..
                    JSONObject obj = new JSONObject(response);
                    if (obj.length() == 0) {
                        Toast.makeText(getApplicationContext(), "Leider haben wir keine genügende Fragen in diesem Bereich", Toast.LENGTH_LONG).show();
                    } else {
                        jsonQuestionsArray = obj.getJSONArray(TAG_MESSAGES_2);

                        for (int i = 0; i < jsonQuestionsArray.length(); i++) {
                            JSONObject myQuestion = jsonQuestionsArray.getJSONObject(i);
                            String frage = myQuestion.getString(TAG_FRAGE);
                            String frage_id = Integer.toString(myQuestion.getInt(TAG_FRAGEN_ID));
                            String antwort_1 = myQuestion.getString(TAG_ANTWORT_1);
                            String antwort_2 = myQuestion.getString(TAG_ANTWORT_2);
                            String antwort_3 = myQuestion.getString(TAG_ANTWORT_3);
                            String antwort_4 = myQuestion.getString(TAG_ANTWORT_4);
                            String correct_answer = myQuestion.getString(TAG_RIGHTANSWER);
                            String sub__name = myQuestion.getString(TAG_NAME_SUBKATEGORIEN);

                            // TODO:SET UP a sqlite database to store the questions and answers
                            // db.addQueue(.... );
                            // db.addQuestion(frage_id,frage,antwort_1,antwort_2,antwort_3,antwort_4,correct_answer,subc__name);
                            db.addQuestion(frage_id, frage, antwort_1, antwort_2, antwort_3, antwort_4, correct_answer, sub__name);

                            Log.i("Test Database", db.getQuestionDetails().get("Kurzname").toString());
                        }
                        NaviTrainingsspiel();
                    }


                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    //  Toast.makeText(getApplicationContext(), "Error Occured [Server's JSON response might be invalid]!", Toast.LENGTH_LONG).show();
                    Toast.makeText(getApplicationContext(), "Error Occured [Server's JSON response might be invalid]!", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                    Log.i("JsonError", e.toString());
                    Log.d("Test exception", e.toString());

                }
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

    public void NaviTrainingsspiel() {
        Intent startTraining = new Intent(ChooseDisciplineActivity.this, AnalogyExaminationActivity.class);
        // startTraining.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(startTraining);

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
            pDialog = new ProgressDialog(ChooseDisciplineActivity.this);
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
                    //map.put(TAG_SUBJECT, subject);
                    // map.put(TAG_DATE, date);

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
                            ChooseDisciplineActivity.this, fachbereicheList,
                            R.layout.discipline_list_item, new String[]{TAG_KURZNAME},
                            new int[]{R.id.discipline_item});
                    // updating listview
                    //  setListAdapter(adapter);

                    list = (ListView) findViewById(android.R.id.list);
                    list.setAdapter(adapter);
                    list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                        @Override
                        public void onItemClick(AdapterView<?> parent, View view,
                                                int position, long id) {
                            //Toast.makeText(ChooseDisciplineActivity.this, "You Clicked at " + fachbereicheList.get(+position).get("FB_ID"), Toast.LENGTH_SHORT).show();
                            getFb_ID = fachbereicheList.get(+position).get("FB_ID").toString(); // will transfer as param to invoke webservice
                            RequestParams params = new RequestParams();

                            params.put("fachbereich", getFb_ID);
                            params.put("menge", "6");
                            invokeWebService(params);

                        }
                    });

                }
            });

        }

    }


}
