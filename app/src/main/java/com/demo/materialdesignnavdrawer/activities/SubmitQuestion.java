package com.demo.materialdesignnavdrawer.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
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

import campusquizregandlogdesign.com.example.helper.JsonParsingFunctions;
import campusquizregandlogdesign.com.example.helper.Studiengaenge;

/**
 * Created by Administrator on 25.07.2015.
 */
public class SubmitQuestion extends AppCompatActivity implements View.OnClickListener {

    EditText Question;


    LinearLayout layoutA;
    ImageView ivA;
    EditText editTextA;

    LinearLayout layoutB;
    ImageView ivB;
    EditText editTextB;

    LinearLayout layoutC;
    ImageView ivC;
    EditText editTextC;

    LinearLayout layoutD;
    ImageView ivD;
    EditText editTextD;
    String returnRightId = "";
    JSONObject jsonobject;
    JSONArray jsonarray;
    ArrayList<String> studiengaengeList;
    ArrayList<Studiengaenge> studiengaenge;
    ProgressDialog pDialog;
    ArrayList<String> subCategorieList;
    ArrayList<Studiengaenge> subCategorie;
    private Button submit;
    // Sipnner process 1
    private String getStringfromSpinner;
    private Spinner spinerStudiengang;
    private String JsonURL_forStudiengang = "http://ps15server.cloudapp.net:8080/useraccount/InfoService/getFb";
    // Spinner process 2
    private String getStringfromSpinnerSubCategorie;
    private Spinner spinerSubcategorie;

    @Override
    public void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_submit_question);
        initialise();

        Question = (EditText) findViewById(R.id.user_submit_question);

        spinerStudiengang = (Spinner) findViewById(R.id.spinner_hauptkategorie);
        spinerSubcategorie = (Spinner) findViewById(R.id.spinner_metaKategorie2);

        layoutA = (LinearLayout) findViewById(R.id.answer_submit_layout_a);
        ivA = (ImageView) findViewById(R.id.answer_submit_select_image_a);
        editTextA = (EditText) findViewById(R.id.answer_submit_select_text_a);


        layoutB = (LinearLayout) findViewById(R.id.answer_submit_layout_b);
        ivB = (ImageView) findViewById(R.id.answer_submit_select_image_b);
        editTextB = (EditText) findViewById(R.id.answer_submit_select_text_b);


        layoutC = (LinearLayout) findViewById(R.id.answer_submit_layout_c);
        ivC = (ImageView) findViewById(R.id.answer_submit_select_image_c);
        editTextC = (EditText) findViewById(R.id.answer_submit_select_text_c);


        layoutD = (LinearLayout) findViewById(R.id.answer_submit_layout_d);
        ivD = (ImageView) findViewById(R.id.answer_submit_select_image_d);
        editTextD = (EditText) findViewById(R.id.answer_submit_select_text_d);


        submit = (Button) findViewById(R.id.submit_button);

        // layoutA.setOnClickListener(this);
        layoutA.setOnClickListener(this);
        layoutB.setOnClickListener(this);
        layoutC.setOnClickListener(this);
        layoutD.setOnClickListener(this);
        submit.setOnClickListener(this);

        new DownloadJSON().execute();


    }


    private void initialise() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(false);
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            getSupportActionBar().setTitle("Fragen einreichen");
            getSupportActionBar().setDisplayUseLogoEnabled(false);

        }

    }

    public void invokeWS(RequestParams params) {
        // Show Progress Dialog

        // Make RESTful webservice call using AsyncHttpClient object
        AsyncHttpClient client = new AsyncHttpClient();
        client.get("http://ps15server.cloudapp.net:8080/useraccount/QuestionService/UsersubmitNewQuestion", params, new AsyncHttpResponseHandler() {
            // When the response returned by REST has Http response code '200'
            @Override
            public void onSuccess(String response) {
                // Hide Progress Dialog
                pDialog.hide();
                try {
                    // JSON Object
                    JSONObject obj = new JSONObject(response);
                    // When the JSON response has status boolean value assigned with true
                    if (obj.getBoolean("status")) {

                        Toast.makeText(getApplicationContext(), "Fragen wurden in Datenbank eingereicht!", Toast.LENGTH_LONG).show();
                        finish();
                    }
                    // Else display error message
                    else {
                        // errorMsg.setText(obj.getString("error_msg"));
                        Toast.makeText(getApplicationContext(), obj.getString("error_msg"), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    Toast.makeText(getApplicationContext(), "Error Occured [Server's JSON response might be invalid]!", Toast.LENGTH_LONG).show();
                    e.printStackTrace();

                }
            }

            // When the response returned by REST has Http response code other than '200'
            @Override
            public void onFailure(int statusCode, Throwable error,
                                  String content) {
                // Hide Progress Dialog
                pDialog.hide();
                // When Http response code is '404'
                if (statusCode == 404) {
                    Toast.makeText(getApplicationContext(), "Requested resource not found", Toast.LENGTH_LONG).show();
                }
                // When Http response code is '500'
                else if (statusCode == 500) {
                    Toast.makeText(getApplicationContext(), "Something went wrong at server end", Toast.LENGTH_LONG).show();
                }
                // When Http response code other than 404, 500
                else {
                    Toast.makeText(getApplicationContext(), "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet or remote server is not up and running]", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.answer_submit_layout_a:
                // do your code
                returnRightId = "1";

                ivB.setImageResource(R.drawable.ic_practice_test_normal_new);
                editTextB.setTextColor(Color.parseColor("#202020"));

                ivC.setImageResource(R.drawable.ic_practice_test_normal_new);
                editTextC.setTextColor(Color.parseColor("#202020"));

                ivD.setImageResource(R.drawable.ic_practice_test_normal_new);
                editTextD.setTextColor(Color.parseColor("#202020"));


                ivA.setImageResource(R.drawable.ic_practice_test_right_new);
                editTextA.setTextColor(Color.parseColor("#61bc31"));
                Log.i("rightId", returnRightId);
                break;

            case R.id.answer_submit_layout_b:
                // do your code

                returnRightId = "2";
                ivA.setImageResource(R.drawable.ic_practice_test_normal_new);
                editTextA.setTextColor(Color.parseColor("#202020"));

                ivC.setImageResource(R.drawable.ic_practice_test_normal_new);
                editTextC.setTextColor(Color.parseColor("#202020"));

                ivD.setImageResource(R.drawable.ic_practice_test_normal_new);
                editTextD.setTextColor(Color.parseColor("#202020"));


                ivB.setImageResource(R.drawable.ic_practice_test_right_new);
                editTextB.setTextColor(Color.parseColor("#61bc31"));
                Log.i("rightId", returnRightId);
                break;

            case R.id.answer_submit_layout_c:
                // do your code
                returnRightId = "3";
                ivA.setImageResource(R.drawable.ic_practice_test_normal_new);
                editTextA.setTextColor(Color.parseColor("#202020"));

                ivB.setImageResource(R.drawable.ic_practice_test_normal_new);
                editTextB.setTextColor(Color.parseColor("#202020"));

                ivD.setImageResource(R.drawable.ic_practice_test_normal_new);
                editTextD.setTextColor(Color.parseColor("#202020"));


                ivC.setImageResource(R.drawable.ic_practice_test_right_new);
                editTextC.setTextColor(Color.parseColor("#61bc31"));
                Log.i("rightId", returnRightId);
                break;
            case R.id.answer_submit_layout_d:
                returnRightId = "4";
                ivA.setImageResource(R.drawable.ic_practice_test_normal_new);
                editTextA.setTextColor(Color.parseColor("#202020"));

                ivB.setImageResource(R.drawable.ic_practice_test_normal_new);
                editTextB.setTextColor(Color.parseColor("#202020"));

                ivC.setImageResource(R.drawable.ic_practice_test_normal_new);
                editTextC.setTextColor(Color.parseColor("#202020"));


                ivD.setImageResource(R.drawable.ic_practice_test_right_new);
                editTextD.setTextColor(Color.parseColor("#61bc31"));
                Log.i("rightId", returnRightId);
                break;

            case R.id.submit_button:
                if (pDialog == null) {
                    pDialog = new ProgressDialog(SubmitQuestion.this);
                    pDialog.setMessage("Please wait...");
                    pDialog.setCancelable(false);
                }

                pDialog.setMessage("Please wait...");
                pDialog.setCancelable(false);
                String inputQuestion = Question.getText().toString();
                String inputAnswer1 = editTextA.getText().toString();
                String inputAnswer2 = editTextB.getText().toString();
                String inputAnswer3 = editTextC.getText().toString();
                String inputAnswer4 = editTextD.getText().toString();
                String getRightAnswer = returnRightId.toString();

                RequestParams params = new RequestParams();
                if (!inputQuestion.isEmpty() && !inputAnswer1.isEmpty() && !inputAnswer2.isEmpty() && !inputAnswer3.isEmpty() && !inputAnswer4.isEmpty() && !getRightAnswer.isEmpty()) {
                    //TODO: invoke webservice and connect to server
                    // invokeWebservice(Param)

                    if (isNetworkAvailable() == false) {
                        Toast.makeText(getApplicationContext(), R.string.error_register_keine_internet_Verbindung, Toast.LENGTH_LONG).show();
                    } else {
                        params.put("frage", inputQuestion);
                        params.put("a1", inputAnswer1);
                        params.put("a2", inputAnswer2);
                        params.put("a3", inputAnswer3);
                        params.put("a4", inputAnswer4);
                        params.put("ar", getRightAnswer);
                        params.put("hkid", getStringfromSpinner.toString());
                        params.put("subid", getStringfromSpinnerSubCategorie.toString());
                        invokeWS(params);
                    }


                } else {
                    Toast.makeText(getApplicationContext(), R.string.error_register_not_all_fields, Toast.LENGTH_LONG).show();
                }


            default:
                break;
        }


    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private class DownloadJSON extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            // Locate the studiengaenge Class

            studiengaenge = new ArrayList<Studiengaenge>();
            // Create an array to populate the spinner
            studiengaengeList = new ArrayList<String>();
            // JSON file URL address
            try {
                jsonobject = JsonParsingFunctions
                        .readJsonFromUrl(JsonURL_forStudiengang);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                // Locate the NodeList name
                jsonarray = jsonobject.getJSONArray("Fachbereiche");
                for (int i = 0; i < jsonarray.length(); i++) {
                    jsonobject = jsonarray.getJSONObject(i);

                    Studiengaenge my_studiengaenge = new Studiengaenge();
                    my_studiengaenge.setKurzename(jsonobject.optString("Kurzname"));
                    my_studiengaenge.setStudiengang_id(jsonobject.optString("FB_ID"));
                    studiengaenge.add(my_studiengaenge);
                    studiengaengeList.add(jsonobject.optString("Kurzname"));

                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }


        @Override
        protected void onPostExecute(Void args) {
            // Locate the spinner in RegisterActivity_WS


            spinerStudiengang = (Spinner) findViewById(R.id.spinner_hauptkategorie);

            // Spinner adapter
            spinerStudiengang
                    .setAdapter(new ArrayAdapter<String>(SubmitQuestion.this,
                            R.layout.spinner_items,
                            studiengaengeList));

            // Spinner on item click listener
            spinerStudiengang
                    .setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                        @Override
                        public void onItemSelected(AdapterView<?> studiengange,
                                                   View arg1, int position, long arg3) {
                            // TODO Auto-generated method stub

                            getStringfromSpinner = studiengaenge.get(position).getStudiengang_id();
                            //((TextView)studiengange.getChildAt(0)).setTextColor(Color.BLACK);
                            Toast.makeText(getApplicationContext(), getStringfromSpinner.toString(), Toast.LENGTH_LONG).show();
                            new DownloadJSONNew().execute();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> arg0) {
                            // TODO Auto-generated method stub
                        }
                    });

        }
    }

    private class DownloadJSONNew extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            // Locate the studiengaenge Class

            subCategorie = new ArrayList<Studiengaenge>();
            // Create an array to populate the spinner
            subCategorieList = new ArrayList<String>();
            // JSON file URL address
            try {
                String JsonURL_subCategorie = "http://ps15server.cloudapp.net:8080/useraccount/InfoService/getSubkategorieByHauptkategorie?hauptkategorie=" + getStringfromSpinner.toString();
                jsonobject = JsonParsingFunctions
                        .readJsonFromUrl(JsonURL_subCategorie);
                Log.i("url", JsonURL_subCategorie);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                // Locate the NodeList name
                jsonarray = jsonobject.getJSONArray("Subkategorie");
                for (int i = 0; i < jsonarray.length(); i++) {
                    jsonobject = jsonarray.getJSONObject(i);

                    Studiengaenge new_studiengaenge = new Studiengaenge();
                    new_studiengaenge.setKurzename(jsonobject.optString("Kurzname"));
                    new_studiengaenge.setStudiengang_id(jsonobject.optString("DZ_ID"));
                    subCategorie.add(new_studiengaenge);

                    // Populate spinner
                    subCategorieList.add(jsonobject.optString("Kurzname"));
                    Log.i("size", Integer.toString(subCategorie.size()));
                }
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }

            return null;
        }


        @Override
        protected void onPostExecute(Void args) {
            // Locate the spinner in RegisterActivity_WS
            spinerSubcategorie = (Spinner) findViewById(R.id.spinner_metaKategorie2);

            // Spinner adapter
            spinerSubcategorie
                    .setAdapter(new ArrayAdapter<String>(SubmitQuestion.this,
                            R.layout.spinner_items,
                            subCategorieList));

            // Spinner on item click listener
            spinerSubcategorie
                    .setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                        @Override
                        public void onItemSelected(AdapterView<?> studiengange,
                                                   View arg1, int position, long arg3) {
                            // TODO Auto-generated method stub

                            getStringfromSpinnerSubCategorie = subCategorie.get(position).getStudiengang_id();

                            Toast.makeText(getApplicationContext(), getStringfromSpinnerSubCategorie.toString(), Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> arg0) {
                            // TODO Auto-generated method stub
                        }
                    });

        }
    }


}
