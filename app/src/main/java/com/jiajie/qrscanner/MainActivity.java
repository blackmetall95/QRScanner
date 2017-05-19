package com.jiajie.qrscanner;

import android.Manifest;
import android.app.*;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.*;
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

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    String TAG="fList!=null";

    IntentIntegrator integrator = new IntentIntegrator(this);
    static final int PERM_WRITE_EXT_STORAGE = 0;
    static final int PERM_FINE_LOC = 1;
    static final int PERM_CAMERA = 2;
    private ListDbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dbHelper = new ListDbHelper(this);
        CheckPermissions();
        FragmentInit();
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

    void FragmentInit(){
        FragmentManager fragManager = getSupportFragmentManager();
        final FragList fList = new FragList();
        FragmentTransaction trans = fragManager.beginTransaction();
        trans.add(R.id.list, fList, "ListFrag"); //Assign a tag to the fragment.
        trans.commit();
    }

    /**
     * Adding a new item to the ListView
     */
    public void updateUI() {
        ArrayList<String> taskList = new ArrayList<>(); //Crate an ArrayList to hold the information
        FragmentManager fragManager = getSupportFragmentManager();
        FragList fList = (FragList) fragManager.findFragmentByTag("ListFrag"); //Get the transaction declared earlier

        SQLiteDatabase db = dbHelper.getReadableDatabase(); //Create/Open a database
        Cursor cursor = db.query(ListContract.ScannedEntry.TABLE, new String[]{ListContract.ScannedEntry._ID, ListContract.ScannedEntry.COL_TASK_TITLE}, null, null, null, null, null);
        while (cursor.moveToNext()) { //Write the information from the Table to the ArrayList
            int idx = cursor.getColumnIndex(ListContract.ScannedEntry.COL_TASK_TITLE);
            taskList.add(cursor.getString(idx));
        }

        if (fList != null) {
            Log.d(TAG, "Written data to List");
            fList.mAdapter.clear(); //Clear the existing list.
            fList.mAdapter.addAll(taskList); //Add the updated list.
            fList.mAdapter.notifyDataSetChanged(); //Update the list.
        }
            cursor.close();
            db.close();

    }

    /**
     * Intent to start activity with DTK library
     */
    void CameraActivityIntent() {
        Intent camIntent = new Intent(this, CameraActivity.class);
        startActivity(camIntent);
    }

    /**
     * Check for permission with the User for SDK23+
     */
    void CheckPermissions() {
        int writeExtStorageCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int fineLocationCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        int cameraCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);

        if (writeExtStorageCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERM_WRITE_EXT_STORAGE);
        }
        if (fineLocationCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERM_FINE_LOC);
        }
        if (cameraCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, PERM_CAMERA);
        }
    }

    @Override
    public void  onRequestPermissionsResult (int requestCode, String permissions[], int[] grantResults ) {
        switch(requestCode) {
            case PERM_WRITE_EXT_STORAGE: {
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

                SQLiteDatabase db = dbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put(ListContract.ScannedEntry.COL_TASK_TITLE, resultString);
                db.insertWithOnConflict(ListContract.ScannedEntry.TABLE, null, values, SQLiteDatabase.CONFLICT_IGNORE);
                db.close();

                updateUI();
            }
        }
    }


}
