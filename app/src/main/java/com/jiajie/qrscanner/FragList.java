package com.jiajie.qrscanner;

import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.jiajie.qrscanner.Adapter.*;
import com.jiajie.qrscanner.DB.ListContract;
import com.jiajie.qrscanner.DB.ListDbHelper;

import java.util.ArrayList;

public class FragList extends ListFragment {

    public CustomAdapter mAdapter; //Declare Adapter here in order for it to be used in Activity.
    public static FragList newInstance(int page, String title){
        FragList fList = new FragList();
        Bundle args = new Bundle();
        args.putInt("0", page);
        args.putString("List", title);
        fList.setArguments(args);
        return fList;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        ArrayList<DataModel>  aList = new ArrayList<>();
        //Initialize the ArrayAdapter with ArrayList instead of String[] to prevent
        //crashing when adding new items from FAB in Activity.
        mAdapter = new CustomAdapter(getActivity(), R.layout.data_model, aList);

        setListAdapter(mAdapter);
        mAdapter.setNotifyOnChange(true);
        setRetainInstance(true);
    }

    /*@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.frag_list, container, false);
    }*/

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

    public void deleteFromDb(DataModel dm) {
        String value = dm.getScanResult();
        Log.d("DeleteValue", "Value to be deleted"+value);
        ListDbHelper dbHelper = new ListDbHelper(getContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(ListContract.ScannedEntry.TABLE, ListContract.ScannedEntry.COL_RESULT+ " =? ", new String[]{value});
        db.close();
    }
}
