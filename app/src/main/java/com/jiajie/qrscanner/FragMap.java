package com.jiajie.qrscanner;

import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

public class FragMap extends Fragment{
    Context context;
    private String title;
//    private int page;
    public static FragMap newInstance(int page, String title){
        FragMap fMap = new FragMap();
        Bundle args = new Bundle();
        args.putInt("1", page);
        args.putString("Map", title);
        fMap.setArguments(args);
        return fMap;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
//        page = getArguments().getInt("1", 1);
        title = getArguments().getString("Map");
        context = getContext();
        Configuration.getInstance().load(context, PreferenceManager.getDefaultSharedPreferences(context));

        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.frag_map, container, false);
        MapView map = new MapView(context);
        map.findViewById(R.id.mapview);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);

        IMapController mapController = map.getController();
        mapController.setZoom(10);
        GeoPoint startPoint = new GeoPoint(3.1365, 101.6865);
        mapController.setCenter(startPoint);
        return view;
    }

    @Override
    public void onResume(){
        super.onResume();
        Configuration.getInstance().load(context, PreferenceManager.getDefaultSharedPreferences(context));
    }

}
