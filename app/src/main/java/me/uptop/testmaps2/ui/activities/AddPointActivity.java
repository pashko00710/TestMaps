package me.uptop.testmaps2.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.Random;

import io.realm.Realm;
import me.uptop.testmaps2.R;
import me.uptop.testmaps2.data.managers.DataManager;
import me.uptop.testmaps2.data.storage.dto.PointsDto;

public class AddPointActivity extends AppCompatActivity implements View.OnClickListener {
    Toolbar toolbar;
    Button mButtonSave;
    EditText title;
    EditText desc;
//    private PointsRealm mPoints;
    DataManager dataManager = DataManager.getInstance();

    double latitude, longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_point);
        toolbar= (Toolbar) findViewById(R.id.toolbar_acitivity_point);
        mButtonSave = (Button) findViewById(R.id.save_point);
        title= (EditText) findViewById(R.id.add_point_title);
        desc = (EditText) findViewById(R.id.add_point_desc);

        setupToolbar();

        Bundle bundle = getIntent().getExtras();

        latitude = bundle.getDouble("LATITUDE");
        longitude = bundle.getDouble("LONGITUDE");

        mButtonSave.setOnClickListener(this);
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    public void startMapsActivity() {
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            startMapsActivity(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.save_point:
                int id = new Random().nextInt() * 1000;
                Realm realm = Realm.getDefaultInstance();
                PointsDto point = new PointsDto(id, title.getText().toString(), desc.getText().toString(), latitude, longitude);
//                point.setId(id);
//                point.setDescription(desc.getText().toString());
//                point.setTitle(title.getText().toString());
//                point.setLatitude(latitude);
//                point.setLongitude(longitude);
                dataManager.getRealmManager().saveProductResponseToRealm(point);
                realm.close();
                break;
        }
    }
}
