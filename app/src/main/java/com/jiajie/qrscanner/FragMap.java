package com.jiajie.qrscanner;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jiajie.qrscanner.DB.ListContract;
import com.jiajie.qrscanner.DB.ListDbHelper;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.ItemizedOverlayWithFocus;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.gestures.RotationGestureOverlay;

import java.util.ArrayList;

public class FragMap extends Fragment{
    /*========== VARIABLES ==========*/
    Context context;
    private ListDbHelper dbHelper;
    public static MapView mapView;
    /*========== CONSTRUCTOR ==========*/
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
        String title = getArguments().getString("Map");
        context = getContext();
        Configuration.getInstance().load(context, PreferenceManager.getDefaultSharedPreferences(context));

        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.frag_map, container, false);
        mapView = new MapView(context);
        mapView.findViewById(R.id.mapview);
        mapView.setTileSource(TileSourceFactory.MAPNIK);
        /*========== Map Controls ==========*/
        RotationGestureOverlay rotationOverlay = new RotationGestureOverlay(context, mapView);
        rotationOverlay.setEnabled(true);
        IMapController mapController = mapView.getController();
        mapController.setZoom(10);
        GeoPoint startPoint = new GeoPoint(3.1365, 101.6865);
        mapController.setCenter(startPoint);
        /*========== Map Icons ==========*/
        markerInit();
        /*========== Set Conditions ==========*/
        mapView.setBuiltInZoomControls(true);
        mapView.setMultiTouchControls(true);
        //mapView.getOverlays().add(rotationOverlay);
        return mapView;
    }

    @Override
    public void onResume(){
        super.onResume();
        Configuration.getInstance().load(context, PreferenceManager.getDefaultSharedPreferences(context));
    }

    public void markerInit(){
        ArrayList<OverlayItem> markers;
        /*========== Connect to DB ==========*/
        markers = setMarkers();
        ItemizedOverlayWithFocus<OverlayItem> markOverlay = new ItemizedOverlayWithFocus<>(context, markers, new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {
            @Override
            public boolean onItemSingleTapUp(int index, OverlayItem item) {
                    return true;
            }
            @Override
            public boolean onItemLongPress(int index, OverlayItem item) {
                return true;
            }
        });
        /*========== Set Conditions ==========*/
        markOverlay.setFocusItemsOnTap(true);
        mapView.getOverlays().add(markOverlay);
        mapView.invalidate();
    }

    public ArrayList<OverlayItem> setMarkers(){
        /*========== VARIABLES ==========*/
        String scanResult;
        double lat;
        double lng;
        String date;
        /*========== FIELDS ==========*/
        SQLiteDatabase db;
        ArrayList<OverlayItem> markers = new ArrayList<>();
        /*========== Create/Open DB ==========*/
        dbHelper = new ListDbHelper(getActivity().getApplicationContext());
        db = dbHelper.getReadableDatabase();
        /*========== Cursor ==========*/
        Cursor cursor = db.query(ListContract.ScannedEntry.TABLE, new String[]{ListContract.ScannedEntry._ID, ListContract.ScannedEntry.COL_RESULT, ListContract.ScannedEntry.LAT, ListContract.ScannedEntry.LNG, ListContract.ScannedEntry.DATE}, null, null, null, null, null);
        while (cursor.moveToNext()) { //Write the information from the Table to the ArrayList
            int idx1 = cursor.getColumnIndex(ListContract.ScannedEntry.COL_RESULT);
            int idx2 = cursor.getColumnIndex(ListContract.ScannedEntry.LAT);
            int idx3 = cursor.getColumnIndex(ListContract.ScannedEntry.LNG);
            int idx4 = cursor.getColumnIndex(ListContract.ScannedEntry.DATE);
            scanResult = cursor.getString(idx1);
            lat = cursor.getDouble(idx2);
            lng = cursor.getDouble(idx3);
            date = cursor.getString(idx4);
            /*========== Add to List ==========*/
            markers.add(new OverlayItem(scanResult, date, new GeoPoint(lat, lng)));
        }
        /*=========== Close DB ==========*/
        db.close();
        cursor.close();
        dbHelper.close();
        Log.d("DB", "DB Closed");
        return markers;
    }
}