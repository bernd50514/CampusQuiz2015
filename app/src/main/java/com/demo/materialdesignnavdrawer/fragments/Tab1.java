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
 * Created by Administrator on 29.07.2015.
 */
public class Tab1 extends ListFragment {

    private static final String TAG_MESSAGES = "RangUser";
    private static final String TAG_PUNKTE = "punkte";
    private static final String TAG_GAMESTOTAL = "gamestotal";
    private static final String TAG_KURZNAME = "KURZNAME";
    private static final String TAG_USERID = "userid";
    private static final String TAG_RANKING = "ranking";
    private static final String TAG_ICON = "icon";
    ProgressDialog pDialog;
    String INDIVIDUAL_URL = "http://ps15server.cloudapp.net:8080/useraccount/InfoService/getRangUser";
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
                    String id = c.getString(TAG_USERID);
                    String scores = c.getString(TAG_PUNKTE);
                    String gamesTotal = c.getString(TAG_GAMESTOTAL);
                    String getUniName = c.getString(TAG_KURZNAME);

                    // creating new HashMap
                    HashMap<String, String> map = new HashMap<String, String>();

                    // adding each child node to HashMap key => value
                    map.put(TAG_USERID, id);
                    map.put(TAG_PUNKTE, scores);
                    map.put(TAG_GAMESTOTAL, gamesTotal);
                    map.put(TAG_KURZNAME, getUniName);
                    map.put(TAG_RANKING, Integer.toString(i + 1) + ".");
                    map.put(TAG_ICON, Integer.toString(R.drawable.ic_profile_username_bluegrey));

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
                            R.layout.tab1_list, new String[]{TAG_USERID, TAG_PUNKTE, TAG_KURZNAME, TAG_RANKING, TAG_ICON},
                            new int[]{R.id.user_name, R.id.user_scores, R.id.user_uni, R.id.user_ranking, R.id.user_icon});
                    // updating listview

                    setListAdapter(adapter);

                }
            });


        }

    }
}
