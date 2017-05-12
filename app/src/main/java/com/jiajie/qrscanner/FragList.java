package com.jiajie.qrscanner;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

public class FragList extends ListFragment {

    public ArrayAdapter<String> mAdapter; //Declare Adapter here in order for it to be used in Activity.

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //String[] values = {"Alpha", "Bravo", "Charlie", "Delta", "Echo"};
        String[] values = {};
        ArrayList<String>  aList = new ArrayList<>(Arrays.asList(values));
        /*Initialize the ArrayAdapter with ArrayList instead of String[] to prevent
        crashing when adding new items from FAB in Activity.*/
        mAdapter = new ArrayAdapter<>(
                getActivity(), android.R.layout.simple_list_item_1, aList);
        setListAdapter(mAdapter);
    }

    @Override
    public void onListItemClick(ListView lv,View v, int position, long id) {
        //todo implement some logic
        Toast.makeText(getContext(), "List item clicked", Toast.LENGTH_SHORT).show();
    }
}
