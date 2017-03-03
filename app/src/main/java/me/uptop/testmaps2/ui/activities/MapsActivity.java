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
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import me.uptop.testmaps2.R;
import me.uptop.testmaps2.data.managers.DataManager;
import me.uptop.testmaps2.data.storage.realm.PointsRealm;

public class MapsActivity extends AppCompatActivity implements
        OnMapReadyCallback, GoogleMap.OnMapClickListener, GoogleMap.OnMapLongClickListener,
        View.OnClickListener, GoogleMap.OnInfoWindowClickListener {
    private static final String LATITUDE = "LATITUDE";
    private static final String LONGITUDE = "LONGITUDE";
    private static final String MODE = "MODE";
    private static final String TITLE = "TITLE";
    private static final String DESCRIPTION = "DESCRIPTION";

    private GoogleMap mMap;
    private Toolbar mToolbar;
    private ActionBar mActionBar;
    private TextView mToolbarText;
    private CardView mCard;
    private Button mButtonAdd;
    Runnable afterExe;
    Marker marker;
    DataManager dataManager = DataManager.getInstance();
    List<PointsRealm> points = new ArrayList<>();

    //region =========================== LifeCycle =====================
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

        mToolbarText.setOnClickListener(this);
        mButtonAdd.setOnClickListener(this);
    }
    //endregion

    //region =========================== Events =====================
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.my_points_btn:
                startListPointsActivity();
                break;
            case R.id.btn_add_point:
                startAddPointActivity("add", marker);
                break;
            default:
                break;
        }
    }

    //region =========================== Listeners =====================
    @Override
    public void onMapClick(LatLng latLng) {
        final Handler handler = new Handler();
        final Marker emptyMarker = mMap.addMarker(new MarkerOptions().position(latLng).title(""));
        marker = emptyMarker;
        emptyMarker.showInfoWindow();
        showCard();
        handler.postDelayed(() -> {
            emptyMarker.setVisible(false);
            handler.postDelayed(afterExe,0);
        }, 2000);
        afterExe = () -> hideCard();
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        mMap.addMarker(new MarkerOptions().position(latLng).title("")).showInfoWindow();
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        startAddPointActivity("read", marker);
    }

    //endregion

    //endregion

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        points = dataManager.getRealmManager().getAllQuotesFromRealm();

        for (PointsRealm point: points) {
            mMap.addMarker(new MarkerOptions().position(new LatLng(point.getLatitude(), point.getLongitude()))
                    .title(point.getTitle())
                    .snippet(point.getDescription()));
        }

        mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter());
        mMap.setOnMapClickListener(this);
        mMap.setOnMapLongClickListener(this);
        mMap.setOnInfoWindowClickListener(this);
    }

    private void startListPointsActivity() {
        Intent intent = new Intent(this, ListPointsActivity.class);
        startActivity(intent);
        finish();
    }

    private void setupToolbar() {
        setSupportActionBar(mToolbar);
        mActionBar = getSupportActionBar();
    }

    public void showCard() {
        mCard.setVisibility(View.VISIBLE);
    }

    public void hideCard() {
        mCard.setVisibility(View.GONE);
    }

    public void startAddPointActivity(String mode, Marker marker) {
        Intent intent = new Intent(this, AddPointActivity.class);
        intent.putExtra(TITLE, marker.getTitle());
        intent.putExtra(DESCRIPTION, marker.getSnippet());
        intent.putExtra(MODE, mode);
        intent.putExtra(LATITUDE, marker.getPosition().latitude);
        intent.putExtra(LONGITUDE, marker.getPosition().longitude);
        startActivity(intent);
        finish();
    }

    class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {
        private final View mWindow;

        private final View mContents;

        CustomInfoWindowAdapter() {
            mWindow = getLayoutInflater().inflate(R.layout.custom_info_window, null);
            mContents = getLayoutInflater().inflate(R.layout.custom_info_contents, null);
        }

        @Override
        public View getInfoWindow(Marker marker) {
            render(marker, mWindow);
            return mWindow;
        }

        @Override
        public View getInfoContents(Marker marker) {
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
            if (snippet != null) {
                SpannableString snippetText = new SpannableString(snippet);
                snippetText.setSpan(new ForegroundColorSpan(Color.MAGENTA), 0, snippet.length(), 0);
                snippetUi.setText(snippetText);
            } else {
                snippetUi.setText("");
            }
        }
    }
}
