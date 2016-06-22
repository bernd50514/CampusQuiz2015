package com.demo.materialdesignnavdrawer.fragments;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;

import com.demo.materialdesignnavdrawer.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import campusquizregandlogdesign.com.example.helper.SQLiteHandler;

/**
 * Created by Administrator on 10.06.2015.
 */
public class ProfileFragment extends ListFragment {

    public static final String TAG = "profile";


    String[] txt = new String[]{
            "Benutzername:",
            "Studiengang:",
            "Universität:",
            "Vorname:",
            "Name:",
            "Email:",


    };

    // Array of integers points to images stored in /res/drawable/
    int[] flags = new int[]{
            R.drawable.ic_profile_username_bluegrey,
            R.drawable.ic_profile_studiengang_bluegrey,
            R.drawable.ic_profile_uni_bluegrey,
            R.drawable.ic_profile_vname_bluegrey,
            R.drawable.ic_profile_vname_bluegrey,
            R.drawable.ic_profile_mail_bluegrey,

    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Each row in the list stores country name, currency and flag
        List<HashMap<String, String>> aList = new ArrayList<HashMap<String, String>>();
        SQLiteHandler db = new SQLiteHandler(getActivity());
        HashMap<String, String> user = db.getUserDetails();
        // get data from sqlite database
        // TODO: retrieve user surname and name by successful login and register as well
        String name = user.get("name");
        String email = user.get("email");
        String uni = user.get("uni");
        String studiengang = user.get("studiengang");
        String[] info = new String[]{
                name,
                studiengang,
                uni,
                "",
                "",
                email,
        };
        for (int i = 0; i < 6; i++) {
            HashMap<String, String> hm = new HashMap<String, String>();
            hm.put("txt", "" + txt[i]);
            hm.put("info", info[i]);
            hm.put("flag", Integer.toString(flags[i]));
            aList.add(hm);
        }

        // Keys used in Hashmap
        String[] from = {"flag", "txt", "info"};

        // Ids of views in listview_layout
        int[] to = {R.id.icon, R.id.item, R.id.text_for_item};

        // Instantiating an adapter to store each items
        // R.layout.listview_layout defines the layout of each item
        SimpleAdapter adapter = new SimpleAdapter(getActivity().getBaseContext(), aList, R.layout.mylist, from, to);

        setListAdapter(adapter);

        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
