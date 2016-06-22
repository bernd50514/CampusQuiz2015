package com.demo.materialdesignnavdrawer.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.demo.materialdesignnavdrawer.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 07.06.2015.
 */
public class OptionFragment extends Fragment {
    public static final String TAG = "option";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.option_fragment, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        List<String> content = new ArrayList<>();
        content.add("Benachrichtigungen");
        content.add("Datenschutz");
        content.add("Informationen");
        content.add("Soundeffekt");

        ListView listView = (ListView) getActivity().findViewById(R.id.listView);
        listView.setAdapter(new ArrayAdapter<>(getActivity(), R.layout.test_option_custom_textview, content));
    }
}