package me.uptop.testmaps2.ui.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import me.uptop.testmaps2.R;

public class MapsActivity extends AppCompatActivity implements
        OnMapReadyCallback, GoogleMap.OnMapClickListener, GoogleMap.OnMapLongClickListener, View.OnClickListener {
    private static final String LATITUDE = "LATITUDE";
    private static final String LONGITUDE = "LONGITUDE";

    private GoogleMap mMap;
    private Toolbar mToolbar;
    private ActionBar mActionBar;
    private TextView mToolbarText;
    private CardView mCard;
    private Button mButtonAdd;
    Handler handler;
    Runnable afterExe;
    Marker marker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mToolbar = (Toolbar) findViewById(R.id.toolbar_animation);
        mToolbarText = (TextView) findViewById(R.id.my_points_btn);
        mCard = (CardView) findViewById(R.id.card_add_point);
        mButtonAdd = (Button) findViewById(R.id.btn_add_point);


        setTitle(getResources().getString(R.string.app_name));

        setupToolbar();


        //для перехода на другую активность с точками
        mToolbarText.setOnClickListener(this);
        mButtonAdd.setOnClickListener(this);

    }



    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.my_points_btn:
                Log.e("click", "onClick: ");
                break;
            case R.id.btn_add_point:
                startAddPointActivity();
                break;
            default:
                break;
        }
    }

    private void setupToolbar() {
        setSupportActionBar(mToolbar);
        mActionBar = getSupportActionBar();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        LatLng lul = new LatLng(-33, 150);
        Log.e("lul", "onMapReady: "+lul.latitude+"  "+lul.longitude);
        mMap.addMarker(new MarkerOptions().position(lul).title("lul").snippet("Desc lul"));
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney").snippet("Desc Sydney fdsfsdfsdfs"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(lul));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));


        mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter());
        mMap.setOnMapClickListener(this);
        mMap.setOnMapLongClickListener(this);
    }


    public void showCard() {
        mCard.setVisibility(View.VISIBLE);
    }

    public void hideCard() {
        mCard.setVisibility(View.GONE);
    }

    @Override
    public void onMapClick(LatLng latLng) {
//        if(handler != null) handler.removeCallbacksAndMessages(null);
        final Handler handler = new Handler();
        final Marker emptyMarker = mMap.addMarker(new MarkerOptions().position(latLng).title(""));
        marker = emptyMarker;
        emptyMarker.showInfoWindow();
        showCard();
        handler.postDelayed(() -> {
            // Actions to do after 10 seconds
            emptyMarker.setVisible(false);
            handler.postDelayed(afterExe,0);
        }, 2000);


        afterExe = () -> hideCard();
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        mMap.addMarker(new MarkerOptions().position(latLng).title("")).showInfoWindow();
        Log.e("lul", "onMapLongClick: "+ latLng);
    }

    public void startAddPointActivity() {
        Intent intent = new Intent(this, AddPointActivity.class);
        intent.putExtra(LATITUDE, marker.getPosition().latitude);
        intent.putExtra(LONGITUDE, marker.getPosition().longitude);
        startActivity(intent);
        finish();
    }

//    @Override
//    public void onInfoWindowClick(Marker marker) {
//
//    }
//
//    @Override
//    public void onInfoWindowClose(Marker marker) {
//
//    }
//
//    @Override
//    public void onInfoWindowLongClick(Marker marker) {
//
//    }
//
//    @Override
//    public boolean onMarkerClick(Marker marker) {
//        return false;
//    }


    class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

        // These are both viewgroups containing an ImageView with id "badge" and two TextViews with id
        // "title" and "snippet".
        private final View mWindow;

        private final View mContents;

        CustomInfoWindowAdapter() {
            mWindow = getLayoutInflater().inflate(R.layout.custom_info_window, null);
            mContents = getLayoutInflater().inflate(R.layout.custom_info_contents, null);
        }

        @Override
        public View getInfoWindow(Marker marker) {
//            if (mOptions.getCheckedRadioButtonId() != R.id.custom_info_window) {
//                // This means that getInfoContents will be called.
//                return null;
//            }
            render(marker, mWindow);
            return mWindow;
        }

        @Override
        public View getInfoContents(Marker marker) {
//            if (mOptions.getCheckedRadioButtonId() != R.id.custom_info_contents) {
//                // This means that the default info contents will be used.
//                return null;
//            }
            render(marker, mContents);
            return mContents;
        }

        private void render(Marker marker, View view) {
            int badge;
            // Use the equals() method on a Marker to check for equals.  Do not use ==.
            if (marker.getTitle().trim().isEmpty()) {
                badge = 0;
            }  else {
                // Passing 0 to setImageResource will clear the image view.
                badge = R.drawable.ic_info_black_24dp;
            }
//            badge = R.drawable.badge_wa;
            ImageView imageBagde = (ImageView) view.findViewById(R.id.badge);
            imageBagde.setImageResource(badge);

            String title = marker.getTitle();
            TextView titleUi = ((TextView) view.findViewById(R.id.title));
            if (title != null) {
                // Spannable string allows us to edit the formatting of the text.
                SpannableString titleText = new SpannableString(title);
                titleText.setSpan(new ForegroundColorSpan(Color.RED), 0, titleText.length(), 0);
                titleUi.setText(titleText);
            } else {
                titleUi.setText("");
            }

            String snippet = marker.getSnippet();
            TextView snippetUi = ((TextView) view.findViewById(R.id.snippet));
            Toast.makeText(MapsActivity.this, snippet, Toast.LENGTH_SHORT).show();
            if (snippet != null) {
                SpannableString snippetText = new SpannableString(snippet);
                snippetText.setSpan(new ForegroundColorSpan(Color.MAGENTA), 0, snippet.length(), 0);
//                snippetText.setSpan(new ForegroundColorSpan(Color.BLUE), 12, snippet.length(), 0);
                snippetUi.setText(snippetText);
            } else {
                snippetUi.setText("");
            }
        }
    }


}
