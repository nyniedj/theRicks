package edu.gatech.cs2340.thericks.controllers;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.function.Predicate;

import edu.gatech.cs2340.thericks.R;
import edu.gatech.cs2340.thericks.database.LoadedFilteredDataHolder;
import edu.gatech.cs2340.thericks.database.RatDatabase;
import edu.gatech.cs2340.thericks.database.RatTrackerApplication;
import edu.gatech.cs2340.thericks.models.RatData;
import edu.gatech.cs2340.thericks.models.User;

/**
 * Created by Cameron on 10/6/2017.
 * Dashboard activity provides numerous activity options for a logged in user
 * to engage in
 */
public class DashboardActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final String TAG = DashboardActivity.class.getSimpleName();
    static final int ADD_RAT_DATA_REQUEST = 2;

    private GoogleMap map;

    private boolean onMap;

    private Button mapButton;
    private Button listRatDataButton;
    private Button profileButton;
    private Button settingsButton;
    private Button reportRatButton;
    private Button logoutButton;

    private Button returnToDashButton;

    private ProgressBar progressBar;

    /**
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        Log.d(TAG, "Entered dashboard activity");

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mapButton = (Button) findViewById(R.id.map_button);
        listRatDataButton = (Button) findViewById(R.id.rat_data_list_button);
        profileButton = (Button) findViewById(R.id.profile_button);
        settingsButton = (Button) findViewById(R.id.settings_button);
        reportRatButton = (Button) findViewById(R.id.report_rat_button);
        logoutButton = (Button) findViewById(R.id.logout_button);

        returnToDashButton = (Button) findViewById(R.id.return_to_dashboard_button);
        returnToDashButton.setVisibility(View.GONE);
        onMap = false;

        progressBar = (ProgressBar) findViewById(R.id.progress_bar_dash_map);
        progressBar.setVisibility(View.GONE);

        Bundle b = getIntent().getExtras();
        User user = b.getParcelable("edu.gatech.cs2340.thericks.User");
        user.login();
        Log.d(TAG, user.getLogin().getUsername() + " is logged in = " + user.isLoggedIn());

        mapButton.setOnClickListener((View v) -> {
            Log.d(TAG, "Rat Map Button pressed");
//            Context context = v.getContext();
//            Intent intent = new Intent(context, MapActivity.class);
//            context.startActivity(intent);
            if (map != null) {
                mapButton.setVisibility(View.GONE);
                listRatDataButton.setVisibility(View.GONE);
                profileButton.setVisibility(View.GONE);
                settingsButton.setVisibility(View.GONE);
                reportRatButton.setVisibility(View.GONE);
                logoutButton.setVisibility(View.GONE);

                progressBar.setVisibility(View.VISIBLE);

                ArrayList<RatData> ratDataArrayList = new ArrayList<RatData>();

                ArrayAdapter<RatData> tempAdapter = new ArrayAdapter<RatData>(RatTrackerApplication.getAppContext(),
                        ArrayAdapter.NO_SELECTION) {

                    @Override
                    public void notifyDataSetChanged() {
                        super.notifyDataSetChanged();
                        for (RatData r: ratDataArrayList) {
                            MarkerOptions markerOptions = new MarkerOptions();
                            markerOptions.position(new LatLng(r.getLatitude(), r.getLongitude()));
                            map.addMarker(markerOptions);
                        }
                        returnToDashButton.setEnabled(true);
                    }

                };

                RatDatabase database = new RatDatabase(RatTrackerApplication.getAppContext());
                database.loadData(tempAdapter, ratDataArrayList, progressBar, new ArrayList<Predicate<RatData>>());

                returnToDashButton.setVisibility(View.VISIBLE);
                returnToDashButton.setEnabled(false);

                onMap = !onMap;
                map.getUiSettings().setAllGesturesEnabled(onMap);
            }
        });

        listRatDataButton.setOnClickListener((View v) -> {
            Log.d(TAG, "Rat Data List button pressed");
            Context context = v.getContext();
            Intent intent = new Intent(context, RatDataListActivity.class);
            context.startActivity(intent);
        });

        reportRatButton.setOnClickListener((View v) -> {
            Log.d(TAG, "Report a Rat button pushed");
            Context context = v.getContext();
            Intent intent = new Intent(context, RatEntryActivity.class);
            startActivityForResult(intent, ADD_RAT_DATA_REQUEST);
        });

        logoutButton.setOnClickListener(v -> {
            user.logout();
            Log.d(TAG, "Logout button pressed");
            Log.d(TAG, user.getLogin().getUsername() + " is logged in = " + user.isLoggedIn());
            Context context = v.getContext();
            Intent intent = new Intent(context, WelcomeActivity.class);
            context.startActivity(intent);
            finish();
        });

        returnToDashButton.setOnClickListener(v -> {
            if (map != null) {
                mapButton.setVisibility(View.VISIBLE);
                listRatDataButton.setVisibility(View.VISIBLE);
                profileButton.setVisibility(View.VISIBLE);
                settingsButton.setVisibility(View.VISIBLE);
                reportRatButton.setVisibility(View.VISIBLE);
                logoutButton.setVisibility(View.VISIBLE);

                returnToDashButton.setVisibility(View.GONE);

                onMap = !onMap;
                map.getUiSettings().setAllGesturesEnabled(onMap);

                map.clear();

                CameraPosition position = new CameraPosition(new LatLng(40.776278, -73.99086), 12, 0, 30);
                map.animateCamera(CameraUpdateFactory.newCameraPosition(position));
            }
        });
    }

    /**
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "Recieved result from RatEntryActivity");
        if (requestCode == ADD_RAT_DATA_REQUEST) {
            if (resultCode == RESULT_OK) {
                Bundle b = data.getExtras();
                RatData passedData = b.getParcelable("edu.gatech.cs2340.thericks.RatData");
                LoadedFilteredDataHolder.add(passedData);
            }
        }
    }

    /**
     *
     * @param googleMap
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.getUiSettings().setAllGesturesEnabled(onMap);
        CameraPosition position = new CameraPosition(new LatLng(40.776278, -73.99086), 12, 0, 30);
        map.animateCamera(CameraUpdateFactory.newCameraPosition(position));
    }
}
