package campusquizregandlogdesign;

/**
 * Created by Administrator on 19.05.2015.
 */

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.demo.materialdesignnavdrawer.R;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

/*
This is a test version for .net Webservice
 */
public class RegisterActivity extends Activity implements AdapterView.OnItemSelectedListener {

    private static final String METHOD_NAME = "registerUser";
    private static final String NAME_SPACE = "http://ps15.uniw.de/";
    private static final String URL = "http://ps15server.cloudapp.net:8080/PS15Webservices/services/RegisterService";
    private static final String SOAP_ACTION = NAME_SPACE + METHOD_NAME;
    private Button btnRegister;
    private Button btnLinkToLogin;
    private EditText inputUserName;
    private EditText inputEmail;
    private EditText inputPassword;
    private EditText inputVorname;
    private EditText inputNachname;
    private Spinner inputStudiengang;
    private ProgressDialog pDialog;
    private String TAG = "Vik";


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        // Set View to register.xml
        setContentView(R.layout.activity_register);


        inputUserName = (EditText) findViewById(R.id.username);
        inputEmail = (EditText) findViewById(R.id.email);
        inputVorname = (EditText) findViewById(R.id.vorname);
        inputNachname = (EditText) findViewById(R.id.nachname);
        inputPassword = (EditText) findViewById(R.id.password);
        inputStudiengang = (Spinner) findViewById(R.id.spinner_studiengang);
        // inputStudiengang = (EditText) findViewById(R.id.studiengang);

        btnRegister = (Button) findViewById(R.id.btnRegister);
        btnLinkToLogin = (Button) findViewById(R.id.btnLinkToLoginScreen);
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);


        // Listening to Login Screen link
        btnRegister.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                // Closing registration screen
                // Switching to Login Screen/closing register screen
                String username = inputUserName.getText().toString();
                String email = inputEmail.getText().toString();
                String password = inputPassword.getText().toString();
                String vorname = inputVorname.getText().toString();
                String nachname = inputNachname.getText().toString();
                String studiengang = inputStudiengang.getSelectedItem().toString();
                //    String studiengang = inputStudiengang.getText().toString();


                //TODO: Implement the spinner methode


                AsyncCallWS asyncCallWS = new AsyncCallWS();

                // Create Propertyinfo to access variables in methods:
                if (!username.isEmpty() && !email.isEmpty() && !password.isEmpty() && !vorname.isEmpty() && !nachname.isEmpty()) {
                    if (Utility.validate(email)) {

                        if (password.length() < 3) {
                            Toast.makeText(getApplicationContext(), "Please enter more than 3 digits password", Toast.LENGTH_LONG).show();
                        } else {

                            asyncCallWS.execute();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Please enter valid email", Toast.LENGTH_LONG).show();
                    }


                } else {
                    Toast.makeText(getApplicationContext(),
                            "Please enter your details!", Toast.LENGTH_LONG)
                            .show();
                }


            }


        });


    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String item = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void invokeWebservice() {

        SoapObject request = new SoapObject(NAME_SPACE, METHOD_NAME);
        PropertyInfo p = new PropertyInfo();
        p.setName("username");
        p.setValue(inputUserName.getText().toString());
        p.setType(PropertyInfo.STRING_CLASS);
        request.addProperty(p);

        p = new PropertyInfo();
        p.setName("email");
        p.setValue(inputEmail.getText().toString());
        p.setType(PropertyInfo.STRING_CLASS);
        request.addProperty(p);

        p = new PropertyInfo();
        p.setName("name");
        p.setValue(inputNachname.getText().toString());
        p.setType(PropertyInfo.STRING_CLASS);
        request.addProperty(p);

        p = new PropertyInfo();
        p.setName("vorname");
        p.setValue(inputVorname.getText().toString());
        p.setType(PropertyInfo.STRING_CLASS);
        request.addProperty(p);

        p = new PropertyInfo();
        p.setName("password");
        p.setValue(inputPassword.getText().toString());
        p.setType(PropertyInfo.STRING_CLASS);
        request.addProperty(p);

        //TODO: Wait until the Webservices finish the studiengang Table matching ID
        p = new PropertyInfo();
        p.setName("studiengangID");
        p.setValue(2);
        p.setType(PropertyInfo.INTEGER_CLASS);
        request.addProperty(p);



    /*    p = new PropertyInfo();
        p.setName("studiengangID");
        p.setValue(Integer.parseInt(inputStudiengang.getText().toString()));
        p.setType(PropertyInfo.INTEGER_CLASS);
        request.addProperty(p);
                                    */
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
        try {
            androidHttpTransport.call(SOAP_ACTION, envelope);

            Object result = envelope.getResponse();


            //  Toast.makeText(this,"Register done!",Toast.LENGTH_LONG).show();
            Log.i(TAG, "Result: " + result);


        } catch (Exception e) {

            e.printStackTrace();

            //TODO: Try to capture different types of errors and specify those.
            Log.i(TAG, "Error:" + e.getMessage());
        }


    }

    //TODO: Implement Toast for Error Message

    // run the registration on background
    private class AsyncCallWS extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            Log.i(TAG, "doInBackground");
            invokeWebservice();
            runOnUiThread(new Runnable() {
                public void run() {

                    Toast.makeText(RegisterActivity.this, "register done", Toast.LENGTH_LONG).show();

                }
            });
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            Log.i(TAG, "onPostExecute");

        }

        @Override
        protected void onPreExecute() {
            Log.i(TAG, "onPreExecute");
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            Log.i(TAG, "onProgressUpdate");
        }

    }


    // Download Json Messages Methode

    //  private class DownloadJSON extends AsyncTask<Void, Void, Void> {


}


