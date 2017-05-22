package com.jiajie.qrscanner;

import android.app.Activity;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.jiajie.qrscanner.DB.ListContract;
import com.jiajie.qrscanner.DB.ListDbHelper;

import java.util.ArrayList;
import java.util.zip.Inflater;

public class FragList extends ListFragment {

    public ArrayAdapter<String> mAdapter; //Declare Adapter here in order for it to be used in Activity.

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        ArrayList<String>  aList = new ArrayList<>();
        //Initialize the ArrayAdapter with ArrayList instead of String[] to prevent
        //crashing when adding new items from FAB in Activity.
        mAdapter = new ArrayAdapter<>(
                getActivity(), android.R.layout.simple_list_item_1, aList);

        setListAdapter(mAdapter);
        mAdapter.setNotifyOnChange(true);
        setRetainInstance(true);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
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
                Log.d("FragList", "Position"+position);
                Log.d("FragList","mAdapter Size"+mAdapter.getCount());
                Log.d("FragList", "String"+mAdapter.getItem(position));
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
                                deleteFromDb(mAdapter.getItem(position)); //MUST BE DELETED FROM DB BEFORE DELETING FROM ARRAY ADAPTER OR RESULTS WILL BE INCORRECT.
                                mAdapter.remove(mAdapter.getItem(position));
                                //Update the list.
                                mAdapter.notifyDataSetChanged();
                            }
                        });
                alertDialog.show();
                return true;
            }
        });
    }

    public void deleteFromDb(String value) {
        Log.d("DeleteValue", "Value to be deleted"+value);
        ListDbHelper dbHelper = new ListDbHelper(getContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(ListContract.ScannedEntry.TABLE, ListContract.ScannedEntry.COL_TASK_TITLE+ " =? ", new String[]{value});
        db.close();
    }
}
