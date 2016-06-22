package com.demo.materialdesignnavdrawer.fragments;

/**
 * Created by Administrator on 08.06.2015.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.demo.materialdesignnavdrawer.R;
import com.demo.materialdesignnavdrawer.activities.ChooseDisciplineActivity;
import com.demo.materialdesignnavdrawer.activities.MainActivity;
import com.demo.materialdesignnavdrawer.activities.NewQuizActivity;
import com.demo.materialdesignnavdrawer.activities.OpenGamesActivity;
import com.demo.materialdesignnavdrawer.activities.SlidingTabActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import campusquizregandlogdesign.com.example.helper.SQLiteQuestionHandler;

public class MainMenuFragment extends ListFragment {

    public static final String TAG = "mainmenu";
    Intent intent = null;
    FrameLayout ranking = null;
    View view = ranking;
    MainActivity mainActivity;
    SQLiteQuestionHandler db;

    // Array of strings storing country names
    String[] rowname = new String[]{
            "Training starten",
            "Challenge starten",
            "Aktive Spiele",
            "Rankings",

    };

    // Array of integers points to images stored in /res/drawable/
    int[] images = new int[]{
            R.drawable.ic_training_start,
            R.drawable.ic_challenge_start,
            R.drawable.ic_aktive_spiele,
            R.drawable.ic_ranking_bluegrey,

    };

    // Array of strings to store currencies


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // setContentView(R.layout.activity_main);
        // Each row in the list stores country name, currency and flag
        List<HashMap<String, String>> aList = new ArrayList<HashMap<String, String>>();

        for (int i = 0; i < 4; i++) {
            HashMap<String, String> hm = new HashMap<String, String>();
            hm.put("txt", rowname[i]);

            hm.put("image", Integer.toString(images[i]));
            aList.add(hm);
        }

        String[] from = {"image", "txt"};

        // Ids of views in listview_layout
        int[] to = {R.id.image1, R.id.txt1};

        // Instantiating an adapter to store each items
        // R.layout.listview_layout defines the layout of each item
        SimpleAdapter adapter = new SimpleAdapter(getActivity().getBaseContext(), aList, R.layout.listview_layout_mainmenufragment, from, to);


        setListAdapter(adapter);

        return super.onCreateView(inflater, container, savedInstanceState);
        // return inflater.inflate(R.layout.main_menu_fragment,container,true);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {

        /** Invokes the implementation of the method onListFragmentItemClick in the hosting activity */
        super.onListItemClick(l, v, position, id);
        switch (position) {

            case 0:
//                FragmentTransaction ft = getFragmentManager().beginTransaction();
//                ft.replace(R.id.main_activity_content_frame, new QuizFragment());
//                ft.commit();
                intent = new Intent(getActivity().getApplicationContext(), ChooseDisciplineActivity.class);
                startActivity(intent);
                break;

            case 1:
                intent = new Intent(getActivity().getApplicationContext(), NewQuizActivity.class);

                startActivity(intent);
                break;

            case 2:
                intent = new Intent(getActivity().getApplicationContext(), OpenGamesActivity.class);
                startActivity(intent);
                break;
            case 3:
                intent = new Intent(getActivity().getApplicationContext(), SlidingTabActivity.class);
                startActivity(intent);
                break;


        }

    }


}