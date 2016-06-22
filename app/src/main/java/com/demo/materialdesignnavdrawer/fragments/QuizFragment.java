package com.demo.materialdesignnavdrawer.fragments;


import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Administrator on 08.06.2015.
 */
public class QuizFragment extends ListFragment {

    ArrayList<String> storage = new ArrayList<String>(
            Arrays.asList("Test", "Test"));
    ArrayAdapter<String> adapter;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, storage);

        setListAdapter(adapter);

    }


}


