package campusquizregandlogdesign;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.demo.materialdesignnavdrawer.R;
import com.demo.materialdesignnavdrawer.activities.MainActivity;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import campusquizregandlogdesign.com.example.helper.JsonParsingFunctions;
import campusquizregandlogdesign.com.example.helper.SQLiteHandler;
import campusquizregandlogdesign.com.example.helper.SessionManager;
import campusquizregandlogdesign.com.example.helper.Studiengaenge;

/**
 * Created by Administrator on 31.05.2015.
 * /*
 * This is a test version for Jersey Webservice
 */
public class RegisterActivity_WS extends RegisterActivity {


    ProgressDialog pDialog;
    JSONObject jsonobject;
    JSONArray jsonarray;
    ArrayList<String> studiengaengeList;
    ArrayList<Studiengaenge> studiengaenge;
    private Button btnRegister;
    private Button btnLinkToLogin;
    private EditText inputUserName;
    private EditText inputEmail;
    private EditText inputPassword;
    private EditText inputPasswordConfirmation;
    private EditText inputVorname;
    private EditText inputNachname;
    private String getStringfromSpinner;
    private Spinner spinerStudiengang;
    private String JsonURL_forStudiengang = "http://ps15server.cloudapp.net:8080/useraccount/InfoService/getStudiengaenge";
    private SessionManager session;
    private SQLiteHandler db;


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        // Set View to register.xml
        setContentView(R.layout.activity_register);


        if (isNetworkAvailable() == true) {
            new DownloadJSON().execute();

            inputUserName = (EditText) findViewById(R.id.username);
            inputEmail = (EditText) findViewById(R.id.email);
            inputVorname = (EditText) findViewById(R.id.vorname);
            inputNachname = (EditText) findViewById(R.id.nachname);
            inputPassword = (EditText) findViewById(R.id.password);
            inputPasswordConfirmation = (EditText) findViewById(R.id.password_confirmation);
            spinerStudiengang = (Spinner) findViewById(R.id.spinner_studiengang); // spiner for Studiengang
            btnRegister = (Button) findViewById(R.id.btnRegister);
            btnLinkToLogin = (Button) findViewById(R.id.btnLinkToLoginScreen);


            //Test Session and SQLiteDb

            // Session manager
            session = new SessionManager(getApplicationContext());

            // SQLite database handler
            db = new SQLiteHandler(getApplicationContext());

            // Check if user is already logged in or not
            if (session.isLoggedIn()) {
                // User is already logged in. Take him to main activity
                Intent intent = new Intent(RegisterActivity_WS.this,
                        MainActivity.class);
                startActivity(intent);
                finish();
            }

            // END of session

            // ---------START OF Register onclickListener---------
            // TODO: set time out for Progress Dialogue
            btnRegister.setOnClickListener(new View.OnClickListener() {

                public void onClick(View arg0) {
                    // Closing registration screen
                    // Switching to Login Screen/closing register screen

                    if (pDialog == null) {
                        pDialog = new ProgressDialog(RegisterActivity_WS.this);
                        pDialog.setMessage("Bitte warten...");
                        pDialog.setCancelable(false);
                    }

                    pDialog.setMessage("Bitte warten...");
                    pDialog.setCancelable(false);

                    String username = inputUserName.getText().toString();
                    String email = inputEmail.getText().toString();
                    String password = inputPassword.getText().toString();
                    String vorname = inputVorname.getText().toString();
                    String name = inputNachname.getText().toString();
                    String passwordConfirm = inputPasswordConfirmation.getText().toString();

                    RequestParams params = new RequestParams();

                    // Create Propertyinfo to access variables in methods:
                    if (!username.isEmpty() && !email.isEmpty() && !password.isEmpty() && !passwordConfirm.isEmpty()) {
                        if (Utility.validate(email)) {

                            if (password.length() <= 4) {
                                Toast.makeText(getApplicationContext(), R.string.error_register_passwordlength, Toast.LENGTH_LONG).show();

                            } else {

                                if (password.equals(passwordConfirm) == false) {
                                    Toast.makeText(getApplicationContext(), R.string.error_register_passwords_not_matching, Toast.LENGTH_LONG).show();
                                } else {
                                    params.put("username", username);
                                    params.put("email", email);
                                    params.put("password", password);
                                    params.put("vorname", vorname);
                                    params.put("name", name);
                                    params.put("studiengangID", getStringfromSpinner);

                                    invokeWS(params);


                                }
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), R.string.error_register_email_invalid, Toast.LENGTH_LONG).show();
                        }


                    } else {
                        Toast.makeText(getApplicationContext(),
                                R.string.error_register_not_all_fields, Toast.LENGTH_LONG)
                                .show();
                    }


                }


            });

        } else { //wenn keine Internetverbindung besteht....
            Toast.makeText(getApplicationContext(),
                    R.string.error_register_keine_internet_Verbindung, Toast.LENGTH_LONG)
                    .show();

            inputUserName = (EditText) findViewById(R.id.username);
            inputEmail = (EditText) findViewById(R.id.email);
            inputVorname = (EditText) findViewById(R.id.vorname);
            inputNachname = (EditText) findViewById(R.id.nachname);
            inputPassword = (EditText) findViewById(R.id.password);
            inputPasswordConfirmation = (EditText) findViewById(R.id.password_confirmation);
            spinerStudiengang = (Spinner) findViewById(R.id.spinner_studiengang); // spiner for Studiengang
            btnRegister = (Button) findViewById(R.id.btnRegister);
            btnLinkToLogin = (Button) findViewById(R.id.btnLinkToLoginScreen);


            //Test Session and SQLiteDb

            // Session manager
            session = new SessionManager(getApplicationContext());

            // SQLite database handler
            db = new SQLiteHandler(getApplicationContext());

            // Check if user is already logged in or not
            if (session.isLoggedIn()) {
                // User is already logged in. Take him to main activity
                Intent intent = new Intent(RegisterActivity_WS.this,
                        MainActivity.class);
                startActivity(intent);
                finish();
            }

            // END of session

            // ---------START OF Register onclickListener---------
            // TODO: set time out for Progress Dialogue
            btnRegister.setOnClickListener(new View.OnClickListener() {

                public void onClick(View arg0) {
                    // Closing registration screen
                    // Switching to Login Screen/closing register screen

                    if (pDialog == null) {
                        pDialog = new ProgressDialog(RegisterActivity_WS.this);
                        pDialog.setMessage("Bitte warten...");
                        pDialog.setCancelable(false);
                    }

                    pDialog.setMessage("Bitte warten...");
                    pDialog.setCancelable(false);

                    String username = inputUserName.getText().toString();
                    String email = inputEmail.getText().toString();
                    String password = inputPassword.getText().toString();
                    String vorname = inputVorname.getText().toString();
                    String name = inputNachname.getText().toString();
                    String passwordConfirm = inputPasswordConfirmation.getText().toString();

                    RequestParams params = new RequestParams();

                    // Create Propertyinfo to access variables in methods:
                    if (!username.isEmpty() && !email.isEmpty() && !password.isEmpty() && !passwordConfirm.isEmpty()) {
                        if (Utility.validate(email)) {

                            if (password.length() <= 4) {
                                Toast.makeText(getApplicationContext(), R.string.error_register_passwordlength, Toast.LENGTH_LONG).show();

                            } else {

                                if (password.equals(passwordConfirm) == false) {
                                    Toast.makeText(getApplicationContext(), R.string.error_register_passwords_not_matching, Toast.LENGTH_LONG).show();
                                } else {
                                    params.put("username", username);
                                    params.put("email", email);
                                    params.put("password", password);
                                    params.put("vorname", vorname);
                                    params.put("name", name);
                                    params.put("studiengangID", getStringfromSpinner);

                                    invokeWS(params);


                                }
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), R.string.error_register_email_invalid, Toast.LENGTH_LONG).show();
                        }


                    } else {
                        Toast.makeText(getApplicationContext(),
                                R.string.error_register_not_all_fields, Toast.LENGTH_LONG)
                                .show();
                    }


                }


            });
        }


    }


    public void invokeWS(RequestParams params) {
        // Show Progress Dialog
        pDialog.show();
        // Make RESTful webservice call using AsyncHttpClient object
        AsyncHttpClient client = new AsyncHttpClient();
        client.get("http://ps15server.cloudapp.net:8080/useraccount/register/doregister", params, new AsyncHttpResponseHandler() {
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
                        // Prepare for Session when user successfully register.
                        String username = obj.getString("username");
                        String email = obj.getString("email");
                        String studiengang = obj.getString("studiengang");
                        String uni = obj.getString("uni");
                        // Now store the user in sqlite, Inserting row in users table
                        db.addUser(username, email, studiengang, uni);

                        Toast.makeText(getApplicationContext(), R.string.info_register_emailsent, Toast.LENGTH_LONG).show();
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

    public void navigatetoLoginActivity(View view) {
        Intent loginIntent = new Intent(getApplicationContext(), LoginActivity.class);
        // Clears Histo ry of Activity
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(loginIntent);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    // lade Studiengang Tabelle aus dem webservice
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
                jsonarray = jsonobject.getJSONArray("Studiengaenge");
                for (int i = 0; i < jsonarray.length(); i++) {
                    jsonobject = jsonarray.getJSONObject(i);

                    Studiengaenge my_studiengaenge = new Studiengaenge();
                    my_studiengaenge.setKurzename(jsonobject.optString("Kurzname"));
                    my_studiengaenge.setStudiengang_id(jsonobject.optString("Studiengang_ID"));
                    studiengaenge.add(my_studiengaenge);

                    // Populate spinner with country names
                    studiengaengeList.add(jsonobject.optString("Kurzname"));

                }
            } catch (Exception e) {
                Log.e("Error", " " + e.getMessage());
                e.printStackTrace();
            }

            return null;
        }


        @Override
        protected void onPostExecute(Void args) {
            // Locate the spinner in RegisterActivity_WS


            spinerStudiengang = (Spinner) findViewById(R.id.spinner_studiengang);

            // Spinner adapter
            spinerStudiengang
                    .setAdapter(new ArrayAdapter<String>(RegisterActivity_WS.this,
                            android.R.layout.simple_spinner_dropdown_item,
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
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> arg0) {
                            // TODO Auto-generated method stub
                        }
                    });

        }
    }


}
