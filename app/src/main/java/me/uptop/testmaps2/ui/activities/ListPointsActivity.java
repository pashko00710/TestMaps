package me.uptop.testmaps2.ui.activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import me.uptop.testmaps2.R;
import me.uptop.testmaps2.data.managers.DataManager;
import me.uptop.testmaps2.ui.adapters.PointsAdapter;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class ListPointsActivity extends AppCompatActivity {
    Toolbar toolbar;
    PointsAdapter mAdapter;
    RecyclerView mListOfPoints;
    private DataManager mDataManager = DataManager.getInstance();
    private LocationManager locationManager;
    private static Location location;

    //region =========================== LifeCycle =====================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_points);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        mListOfPoints = (RecyclerView) findViewById(R.id.list_of_points);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        setTitle("");
        setupToolbar();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (checkPermissionsAndRequestIfNotGranted(new String[]{ACCESS_FINE_LOCATION}, 1000) && checkPermissionsAndRequestIfNotGranted(new String[]{ACCESS_COARSE_LOCATION}, 2000)) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    1000 * 10, 10, locationListener);
            locationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER, 1000 * 10, 10,
                    locationListener);
        }

        if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            setupAdapter(getLocation());
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        locationManager.removeUpdates(locationListener);
    }
    //endregion

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            startMapsActivity(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }


    private void setupAdapter(Location location) {
        mAdapter = new PointsAdapter(mDataManager.getRealmManager().getAllQuotesFromRealm(), location);
        mListOfPoints.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mListOfPoints.setAdapter(mAdapter);
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }


    private void setLocation(Location loc) {
        location = loc;
    }

    public static Location getLocation() {
        return location;
    }

    private LocationListener locationListener = new LocationListener() {

        @Override
        public void onLocationChanged(Location location) {
            setLocation(location);
        }

        @Override
        public void onProviderDisabled(String provider) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            if (provider.equals(LocationManager.GPS_PROVIDER)) {
                Log.e("Status: ", " " + String.valueOf(status));
            }
        }
    };

    public boolean checkPermissionsAndRequestIfNotGranted(@NonNull String[] permissions, int requestCode) {
        boolean allGranted = true;
        for (String permission : permissions) {
            int selfPermission = ContextCompat.checkSelfPermission(getApplicationContext(), permission);
            if (selfPermission != PackageManager.PERMISSION_GRANTED) {
                allGranted = false;
                break;
            }
        }

        if (!allGranted) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(permissions, requestCode);
            }
            return false;
        }
        return allGranted;
    }

    public void startMapsActivity() {
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
        finish();
    }

}
