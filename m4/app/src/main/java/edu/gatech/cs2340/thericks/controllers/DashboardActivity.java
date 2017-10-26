package edu.gatech.cs2340.thericks.controllers;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;

import edu.gatech.cs2340.thericks.R;
import edu.gatech.cs2340.thericks.database.LoadedFilteredDataHolder;
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

    private MapView mapView;
    private GoogleMap map;

    private Button mapButton;
    private Button listRatDataButton;
    private Button profileButton;
    private Button settingsButton;
    private Button reportRatButton;
    private Button logoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        Log.d(TAG, "Entered dashboard activity");

        mapView = (MapView) findViewById(R.id.dashboard_map_view);
        mapView.getMapAsync(this);

        mapButton = (Button) findViewById(R.id.map_button);
        listRatDataButton = (Button) findViewById(R.id.rat_data_list_button);
        profileButton = (Button) findViewById(R.id.profile_button);
        settingsButton = (Button) findViewById(R.id.settings_button);
        reportRatButton = (Button) findViewById(R.id.report_rat_button);
        logoutButton = (Button) findViewById(R.id.logout_button);

        Bundle b = getIntent().getExtras();
        User user = b.getParcelable("edu.gatech.cs2340.thericks.User");
        user.login();
        Log.d(TAG, user.getLogin().getUsername() + " is logged in = " + user.isLoggedIn());

        mapButton.setOnClickListener((View v) -> {
            Log.d(TAG, "Rat Map Button pressed");
            Context context = v.getContext();
            Intent intent = new Intent(context, MapActivity.class);
            context.startActivity(intent);
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
    }

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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
    }
}
