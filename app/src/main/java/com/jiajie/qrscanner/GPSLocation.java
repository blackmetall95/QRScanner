package com.jiajie.qrscanner;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import java.util.List;


public class GPSLocation implements LocationListener {

    private final Context mContext;
    private LocationManager locationManager;

    private boolean isGPSEnabled = false;
    private boolean isNetworkEnabled = false;
    private boolean canGetLocation = false;

    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 0;//10;
    private static final long MIN_TIME_BW_UPDATES = 0;//1000 * 60 * 1;

    private Location location;
    private double latitude;
    private double longitude;
    String TAG1 = "GPS";
    String TAG2 = "NETWORK";
    public GPSLocation(Context context) {
        this.mContext = context;
        getLocation();
    }

    List<String> permissions;
    public Location getLocation() {
        try {
            locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {
                //No network provider is enabled.
                ShowSettingsAlert();
            } else {
                this.canGetLocation = true;
                if (isGPSEnabled) {
                    if (location == null) {
                        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
                            permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
                        }
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                                MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        Log.d(TAG1, "GPS Enabled");
                        if (locationManager != null){
                            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (location != null){
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                                Log.d(TAG1, "Lat "+latitude+"Long "+longitude);
                            }
                        }
                    }
                }

                if (isNetworkEnabled){
                    if (location == null){
                        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                                MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        Log.d(TAG2, "Network Enabled");
                        if (locationManager != null){
                            location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                            if (location != null){
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                                Log.d(TAG2, "Lat: "+latitude+"Long "+longitude);
                            }
                        }
                    }
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return location;
    }

    /********** Return Location **********/
    public double getLatitude(){
        if (location != null){
            //latitude = Double.toString(location.getLatitude());
            latitude = location.getLatitude();
        }
        return latitude;
    }

    public double getLongitude(){
        if (location != null){
            //longitude = Double.toString(location.getLongitude());
            longitude = location.getLongitude();
        }
        return longitude;
    }

    /********** Check Settings **********/
    public void ShowSettingsAlert(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);

        alertDialog.setTitle("GPS Settings");
        alertDialog.setMessage("GPS is not enabled. Please enable GPS to use location based functions");
        alertDialog.setPositiveButton("Setting", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                mContext.startActivity(intent);
            }
        });
        alertDialog.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
            public  void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();
    }

    public boolean CanGetLocation(){
        return this.canGetLocation;
    }

    public void StopUsingGPS(){
        if (location != null){
            locationManager.removeUpdates(GPSLocation.this);
        }
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
