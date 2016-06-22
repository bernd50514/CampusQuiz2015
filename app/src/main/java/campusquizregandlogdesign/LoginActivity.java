package campusquizregandlogdesign;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.demo.materialdesignnavdrawer.R;
import com.demo.materialdesignnavdrawer.activities.MainActivity;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import campusquizregandlogdesign.com.example.helper.SQLiteHandler;
import campusquizregandlogdesign.com.example.helper.SessionManager;

/**
 * Created by Administrator on 19.05.2015.
 */


public class LoginActivity extends Activity {

    ProgressDialog prgDialog;
    private EditText inputEmail;
    private EditText inputPassword;
    private Button btnLinkToRegisterScreen;
    private Button btnLogin;
    private SessionManager session;
    private SQLiteHandler db;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        // setting default screen to login.xml
        setContentView(R.layout.activity_login);
        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);

        // Session manager
        session = new SessionManager(getApplicationContext());

        // Check if user is already logged in or not
        if (session.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        // -----START OF set OnClickListener Methode------
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLinkToRegisterScreen = (Button) findViewById(R.id.btnLinkToRegisterScreen);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                // Closing registration screen
                // Switching to Login Screen/closing register screen
                if (prgDialog == null) {
                    prgDialog = new ProgressDialog(LoginActivity.this);
                    prgDialog.setMessage("Bitte warten...");
                    prgDialog.setCancelable(false);
                }
                // Set Progress Dialog Text
                prgDialog.setMessage("Bitte warten...");
                // Set Cancelable as False
                prgDialog.setCancelable(false);
                String emailorUsername = inputEmail.getText().toString();
                String password = inputPassword.getText().toString();

                RequestParams params = new RequestParams();
                //TODO: implement the method
                // Create Propertyinfo to access variables in methods:
                if (!emailorUsername.isEmpty() && !password.isEmpty()) {

                    params.put("emailOrUsername", emailorUsername);
                    params.put("password", password);
                    invokeWS(params);


                } else {
                    Toast.makeText(getApplicationContext(),
                            R.string.error_register_not_all_fields, Toast.LENGTH_LONG)
                            .show();
                }


            }


        });


        // Listening to register new account link
        btnLinkToRegisterScreen.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // Switching to Register screen
                Intent i = new Intent(getApplicationContext(), RegisterActivity_WS.class);
                startActivity(i);
            }
        });

        // ----END OF LISTENER----
    }


    /**
     * Method that performs RESTful webservice invocations
     *
     * @param params
     */
    public void invokeWS(RequestParams params) {
        // Show Progress Dialog
        prgDialog.show();
        // Make RESTful webservice call using AsyncHttpClient object
        AsyncHttpClient client = new AsyncHttpClient();
        //http://172.17.11.80:8080/ 192.168.1.101:8080/ http://ps15server.cloudapp.net:8080/
        client.get("http://ps15server.cloudapp.net:8080/useraccount/login/dologin", params, new AsyncHttpResponseHandler() {
            // When the response returned by REST has Http response code '200'
            @Override
            public void onSuccess(String response) {
                // Hide Progress Dialog
                prgDialog.hide();
                try {
                    // JSON Object
                    JSONObject obj = new JSONObject(response);
                    // When the JSON response has status boolean value assigned with true
                    if (obj.getBoolean("status")) {
                        //Toast.makeText(getApplicationContext(), "You are successfully logged in!", Toast.LENGTH_LONG).show();
                        // Navigate to Home screen
                        // create login Session

                        // Test get Data from Json
                        String username = obj.getString("username");
                        String email = obj.getString("email");
                        String studiengang = obj.getString("studiengang");
                        String uni = obj.getString("uni");
                        // Now store the user in sqlite, Inserting row in users table
                        db = new SQLiteHandler(getApplicationContext());
                        db.getloginUser(username, email, studiengang, uni);

                        session.setLogin(true);
                        navigatetoHomeActivity();
                        finish();

                    }
                    // Else display error message
                    else {
                        //  errorMsg.setText(obj.getString("error_msg"));
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
                prgDialog.hide();
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


    public void navigatetoHomeActivity() {
        Intent homeIntent = new Intent(getApplicationContext(), MainActivity.class);
        //  homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(homeIntent);
    }


    public void navigatetoRegisterActivity(View view) {
        Intent loginIntent = new Intent(getApplicationContext(), RegisterActivity_WS.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(loginIntent);
    }

}
