package com.jiajie.qrscanner;

import android.Manifest;
import android.app.*;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.*;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.*;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import com.jiajie.qrscanner.DB.ListContract;
import com.jiajie.qrscanner.DB.ListDbHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    double latitude;
    double longitude;
    double lat = 3.1365;
    double lng = 101.6865;
    double count = 0;

    FragmentPagerAdapter adapterViewPager;
    MyPagerAdapter mPagerAdapter;
    IntentIntegrator integrator = new IntentIntegrator(this);
    static final int PERM_WRITE_EXT_STORAGE = 0;
    static final int PERM_COARSE_LOC = 1;
    static final int PERM_FINE_LOC = 2;
    static final int PERM_CAMERA = 3;
    private GPSLocation gps;
    private ListDbHelper dbHelper;
    private String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        /* ViewPager */
        ViewPager vPager = (ViewPager) findViewById(R.id.ViewPager);
        adapterViewPager = new MyPagerAdapter(getSupportFragmentManager());
        vPager.setAdapter(adapterViewPager);

        gps = new GPSLocation(MainActivity.this);
        dbHelper = new ListDbHelper(this);
        CheckPermissions(this);
        FragmentManager fM = getSupportFragmentManager();
        mPagerAdapter = new MyPagerAdapter(fM);

        integrator.setOrientationLocked(false);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                integrator.initiateScan();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
            this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    protected void onStart(){
        super.onStart();
        loadTitles();
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * Check for permission with the User for SDK23+
     */
    List<String> permissions = new ArrayList<>();
    void CheckPermissions(Context context) {
        int writeExtStorageCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int coarseLocationCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
        int fineLocationCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        int cameraCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            if (writeExtStorageCheck != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
            if (coarseLocationCheck != PackageManager.PERMISSION_GRANTED){
                permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
            }
            if (fineLocationCheck != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
            }
            if (cameraCheck != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.CAMERA);
            }

            if (!permissions.isEmpty()) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERM_WRITE_EXT_STORAGE);
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERM_COARSE_LOC);
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERM_FINE_LOC);
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, PERM_CAMERA);
            }
        }
    }

    @Override
    public void  onRequestPermissionsResult (int requestCode, @NonNull String permissions[], @NonNull int[] grantResults ) {
        switch(requestCode) {
            case PERM_WRITE_EXT_STORAGE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {}
                else{//Disable related functions
                }
            }
            case PERM_COARSE_LOC: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {}
            }
            case PERM_FINE_LOC: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {}
            }
            case PERM_CAMERA: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {}
            }
        }
    }

    /**
     * Respond to the IntentIntegrator call
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent){
        super.onActivityResult(requestCode, resultCode, intent);

        if (resultCode == Activity.RESULT_OK) {
            //Parsing barcode reader result
            IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);

            if (result.getContents()==null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show();
            } else {
                String resultString = result.getContents();
                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
                builder.setTitle("Scan Result");
                builder.setMessage(resultString);
                android.support.v7.app.AlertDialog alert1 = builder.create();
                alert1.show();

                getLocation();

                date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.PRC).format(Calendar.getInstance().getTime());
                Log.d("Date ", date);
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                ContentValues value = new ContentValues();
                value.clear();
                value.put(ListContract.ScannedEntry.COL_RESULT, resultString);
                value.put(ListContract.ScannedEntry.LAT, latitude);
                value.put(ListContract.ScannedEntry.LNG, longitude);
                value.put(ListContract.ScannedEntry.DATE, date);
                db.insertWithOnConflict(ListContract.ScannedEntry.TABLE, null, value, SQLiteDatabase.CONFLICT_IGNORE);
                count = count+0.5;

                db.close();
                mPagerAdapter.dataChanged();
                gps.StopUsingGPS();
            }
        }
    }

    double getLocation(){
        if (gps.CanGetLocation()){
            latitude = gps.getLatitude();
            longitude = gps.getLongitude();
            //Toast.makeText(this, "Latitude: "+latitude+" Longitude: "+longitude, Toast.LENGTH_LONG).show();
            Log.d("Main", "Latitude: "+latitude);
            Log.d("Main", "Longitude: "+longitude);
        }
        return latitude+longitude;
    }

    private static ArrayList<String> titles = new ArrayList<>();
    private void loadTitles(){
        //ToDo find a better way of implementing this
        String[] titleString = {"List", "Map"};
        for (int i = 0; i < titleString.length; i++) {
            titles.add(titleString[i]);
        }
    }

    private static class MyPagerAdapter extends FragmentPagerAdapter{
        private static int NUM_ITEMS = 2;

        private MyPagerAdapter(FragmentManager fragManager){
            super(fragManager);
        }
        private static FragList fList = FragList.newInstance(0, "List");
        private static FragMap fMap = FragMap.newInstance(1,"Map");

        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return fList;
                case 1:
                    return fMap;
            }
            return null;
        }

        @Override
        public CharSequence getPageTitle(int position){
            return titles.get(position);
        }

        public void dataChanged(){
            fList.updateFromDb();
            fMap.markerInit();
        }
    }

}