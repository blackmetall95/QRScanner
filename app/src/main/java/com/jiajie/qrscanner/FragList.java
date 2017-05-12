package com.jiajie.qrscanner;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
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
        final ArrayList<String>  aList = new ArrayList<>(Arrays.asList(values));
        /*Initialize the ArrayAdapter with ArrayList instead of String[] to prevent
        crashing when adding new items from FAB in Activity.*/
        mAdapter = new ArrayAdapter<>(
                getActivity(), android.R.layout.simple_list_item_1, aList);
        setListAdapter(mAdapter);

        LongPress();
    }

    @Override
    public void onListItemClick(ListView lv,View v, int position, long id) {
        //todo implement some logic
        Toast.makeText(getContext(), "List item clicked", Toast.LENGTH_SHORT).show();
    }

    void LongPress(){
        getListView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
                alertDialog.setTitle("Delete");
                alertDialog.setTitle("Do you want to delete this entry?");
                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Delete",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which){
                                //Get the position of the current item instead of the String.
                                mAdapter.remove(mAdapter.getItem(position));
                                //Update the list.
                                mAdapter.notifyDataSetInvalidated();
                            }
                        });
                alertDialog.show();

                return true;
            }
        });
    }
}
