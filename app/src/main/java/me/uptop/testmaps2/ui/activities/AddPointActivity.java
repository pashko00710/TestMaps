package me.uptop.testmaps2.ui.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.Random;

import me.uptop.testmaps2.R;
import me.uptop.testmaps2.data.managers.DataManager;
import me.uptop.testmaps2.data.storage.dto.PointsDto;

public class AddPointActivity extends AppCompatActivity implements View.OnClickListener {
    Toolbar toolbar;
    Button mButtonSave;
    EditText titleEditText;
    EditText descEditText;
    DataManager dataManager = DataManager.getInstance();
    double latitude, longitude;
    String mode,title, desc;
    private static final String LATITUDE = "LATITUDE";
    private static final String LONGITUDE = "LONGITUDE";
    private static final String MODE = "MODE";
    private static final String TITLE = "TITLE";
    private static final String DESCRIPTION = "DESCRIPTION";

    //region =========================== LifeCycle =====================
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_point);
        toolbar= (Toolbar) findViewById(R.id.toolbar_acitivity_point);
        mButtonSave = (Button) findViewById(R.id.save_point);
        titleEditText = (EditText) findViewById(R.id.add_point_title);
        descEditText = (EditText) findViewById(R.id.add_point_desc);

        setupToolbar();
        getBundleData();

        if(mode.contains("add")) {
            addModePoint();
        } else {
            readModePoint();
        }
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


    private void getBundleData() {
        Bundle bundle = getIntent().getExtras();
        latitude = bundle.getDouble(LATITUDE);
        longitude = bundle.getDouble(LONGITUDE);
        title = bundle.getString(TITLE);
        desc = bundle.getString(DESCRIPTION);
        mode = bundle.getString(MODE);
    }

    private void addModePoint() {
        showBtnSave();
        titleEditText.setEnabled(true);
        titleEditText.setFocusable(true);
        descEditText.setEnabled(true);
        descEditText.setFocusable(true);
        mButtonSave.setOnClickListener(this);
    }

    private void readModePoint() {
        titleEditText.setText(title);
        descEditText.setText(desc);
        titleEditText.setEnabled(false);
        titleEditText.setFocusable(false);
        descEditText.setEnabled(false);
        descEditText.setFocusable(false);
        titleEditText.setTextColor(Color.BLACK);
        descEditText.setTextColor(Color.BLACK);
        hideBtnSave();
    }

    private void hideBtnSave() {
        mButtonSave.setVisibility(View.GONE);
    }

    private void showBtnSave() {
        mButtonSave.setVisibility(View.VISIBLE);
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

    public static String generateId() {
        StringBuilder randomString = new StringBuilder();
        for (int i = 0; i < 24; ++i) {
            int randomNumber = (int) (new Random().nextFloat() * 9);
            randomString.append(randomNumber);
        }
        return randomString.toString();
    }


    //region =========================== Events =====================
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.save_point:
                String id = generateId();
                PointsDto point = new PointsDto(id, titleEditText.getText().toString(), descEditText.getText().toString(), latitude, longitude);
                dataManager.getRealmManager().saveProductResponseToRealm(point);
                startMapsActivity();
                break;
        }
    }

    //endregion
}
