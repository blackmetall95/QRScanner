package com.jiajie.qrscanner.Adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.jiajie.qrscanner.R;

import java.util.ArrayList;

public class CustomAdapter extends ArrayAdapter<DataModel>{
    private Context context;
    private ArrayList<DataModel> aList;
    private DataModel dm;

    public CustomAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull ArrayList<DataModel> aList) {
        super(context, resource, aList);
        this.context = context;
        this.aList=aList;
    }

    @Override
    public View getView(int pos, View convertView, ViewGroup parent){
        TextView mainTV;
        TextView subTV;
        TextView sub2TV;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = convertView;

        if (rowView == null){
            rowView = inflater.inflate(R.layout.data_model, parent, false);
        }

        mainTV = (TextView) rowView.findViewById(R.id.MainText);
        subTV = (TextView) rowView.findViewById(R.id.SubText);
        sub2TV = (TextView) rowView.findViewById(R.id.SubText2);
        dm = aList.get(pos);
        String mainText = dm.getScanResult();
        String subText = "Lat:"+dm.getLat()+" Long:"+dm.getLng();
        String sub2Text = dm.getDate();
        mainTV.setText(mainText);
        subTV.setText(subText);
        sub2TV.setText(sub2Text);

        return rowView;
    }
}
