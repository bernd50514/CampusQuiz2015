package com.demo.materialdesignnavdrawer.fragments;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import campusquizregandlogdesign.com.example.helper.JsonParsingFunctions;

/**
 * Created by Administrator on 10.06.2015.
 */
public class Tab3 extends ListFragment {

    private static final String TAG_MESSAGES = "RangStudiengang";
    private static final String TAG_RANG = "rang";
    private static final String TAG_STUDIENGANG = "studiengang";
    ProgressDialog pDialog;
    String INDIVIDUAL_URL = "http://ps15server.cloudapp.net:8080/useraccount/InfoService/getRangStudiengang";
    JSONArray inbox = null;
    JSONArray jsonRankingArray = null;
    ListView list;
    ArrayList<HashMap<String, String>> rankingList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        rankingList = new ArrayList<HashMap<String, String>>();
        new LoadInbox().execute();
        return super.onCreateView(inflater, container, savedInstanceState);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getListView().setBackgroundColor(Color.WHITE);
    }

    class LoadInbox extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (pDialog == null) {
                pDialog = new ProgressDialog(getActivity());
                pDialog.setMessage("Bitte warten...");
                pDialog.setCancelable(false);
                pDialog.show();
            } else {
                pDialog.setMessage("Bitte warten...");
                pDialog.setIndeterminate(false);
                pDialog.setCancelable(false);
                pDialog.show();
            }
        }

        /**
         * getting Inbox JSON
         */
        protected String doInBackground(String... args) {

            try {
                JSONObject json = JsonParsingFunctions.readJsonFromUrl(INDIVIDUAL_URL);
                inbox = json.getJSONArray(TAG_MESSAGES);
                // looping through All messages
                for (int i = 0; i < inbox.length(); i++) {
                    JSONObject c = inbox.getJSONObject(i);

                    // Storing each json item in variable
                    String id = c.getString(TAG_RANG);
                    String studiengang = c.getString(TAG_STUDIENGANG);

                    // creating new HashMap
                    HashMap<String, String> map = new HashMap<String, String>();

                    // adding each child node to HashMap key => value
                    map.put(TAG_RANG, id + ".");
                    map.put(TAG_STUDIENGANG, studiengang);

                    rankingList.add(map);
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
            Log.i("rankingList length", Integer.toString(rankingList.size()));
            // dismiss the dialog after getting all products

            pDialog.dismiss();


            // updating UI from Background Thread
            getActivity().runOnUiThread(new Runnable() {
                public void run() {


                    ListAdapter adapter = new SimpleAdapter(
                            getActivity().getBaseContext(), rankingList,
                            R.layout.tab3_list, new String[]{TAG_STUDIENGANG, TAG_RANG},
                            new int[]{R.id.user_studiengang_tab3, R.id.user_ranking_tab3});
                    // updating listview

                    setListAdapter(adapter);

                }
            });


        }

    }

}
