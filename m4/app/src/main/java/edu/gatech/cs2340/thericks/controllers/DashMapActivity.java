package edu.gatech.cs2340.thericks.controllers;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import edu.gatech.cs2340.thericks.R;
import edu.gatech.cs2340.thericks.database.RatDatabase;
import edu.gatech.cs2340.thericks.models.RatData;
import edu.gatech.cs2340.thericks.models.RatDateTime;
import edu.gatech.cs2340.thericks.models.User;
import edu.gatech.cs2340.thericks.utils.RatDateTimeFilterer;

/**
 * Created by Cameron on 10/6/2017.
 * Dash-map activity in dash mode provides numerous activity options for a logged in user
 * to engage in. Defaults to dash mode, upon selecting map, dash-map switches to map mode,
 * where users can view rat data displayed on a Google Map, filtered by date
 */
public class DashMapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final String TAG = DashMapActivity.class.getSimpleName();
    private static final int ADD_RAT_DATA_REQUEST = 2;

    //default position, zoom, and bearing to set the map camera to
    private static final CameraPosition position = new CameraPosition(new LatLng(40.776278, -73.99086), 12, 0, 30);

    private GoogleMap map;

    private boolean onMap;

    private Button mapButton;
    private Button listRatDataButton;
    private Button profileButton;
    private Button settingsButton;
    private Button reportRatButton;
    private Button logoutButton;

    private Button returnToDashButton;
    private Button applyFiltersButton;

    private EditText date1Edit;
    private EditText date2Edit;
    // Begin and end of default date range for map markers.
    private static final String DEFAULT_START_DATE = "08/01/2017 12:00:00 AM";
    private static final String DEFAULT_END_DATE = "08/11/2017 12:00:00 AM";

    private List<Predicate<RatData>> filters;
    private Predicate<RatData> dateInRange;

    private TextView dateSeparator;

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_map);

        Log.d(TAG, "Entered dashboard activity");

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mapButton = (Button) findViewById(R.id.map_button);
        Button graphButton = (Button) findViewById(R.id.rat_graph_button);
        listRatDataButton = (Button) findViewById(R.id.rat_data_list_button);
        profileButton = (Button) findViewById(R.id.profile_button);
        settingsButton = (Button) findViewById(R.id.settings_button);
        reportRatButton = (Button) findViewById(R.id.report_rat_button);
        logoutButton = (Button) findViewById(R.id.logout_button);

        returnToDashButton = (Button) findViewById(R.id.return_to_dashboard_button);
        returnToDashButton.setVisibility(View.GONE);

        applyFiltersButton = (Button) findViewById(R.id.apply_filters_button);
        applyFiltersButton.setVisibility(View.GONE);

        filters = new ArrayList<>();
        //default date to filter out rat data that occurs after the specified date
        RatDateTime begin = RatDateTime.forDateTime(DEFAULT_START_DATE);
        RatDateTime end = RatDateTime.forDateTime(DEFAULT_END_DATE);
        dateInRange = RatDateTimeFilterer.createRatDateTimeRangeFilter(begin, end);
        filters.add(dateInRange);

        date1Edit = (EditText) findViewById(R.id.date1_dash_map);
        date1Edit.setVisibility(View.GONE);
        date1Edit.setText(DEFAULT_START_DATE);
        date1Edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String text = charSequence.toString();
                if (RatDateTime.isDateTime(text)) {
                    date1Edit.setTextColor(ResourcesCompat.getColor(getResources(), R.color.colorBlack, null));
                    applyFiltersButton.setEnabled(true);
                } else {
                    Log.d(TAG, "Improperly formatted input detected: " + text);
                    date1Edit.setTextColor(ResourcesCompat.getColor(getResources(), R.color.errorPrimary, null));
                    applyFiltersButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        date2Edit = (EditText) findViewById(R.id.date2_dash_map);
        date2Edit.setVisibility(View.GONE);
        date2Edit.setText(DEFAULT_END_DATE);
        date2Edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String text = charSequence.toString();
                if (RatDateTime.isDateTime(text)) {
                    date2Edit.setTextColor(ResourcesCompat.getColor(getResources(), R.color.colorBlack, null));
                    applyFiltersButton.setEnabled(true);
                } else {
                    Log.d(TAG, "Improperly formatted input detected: " + text);
                    date2Edit.setTextColor(ResourcesCompat.getColor(getResources(), R.color.errorPrimary, null));
                    applyFiltersButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        dateSeparator = (TextView) findViewById(R.id.date_separator_dash_map_text);
        dateSeparator.setVisibility(View.GONE);

        onMap = false;

        progressBar = (ProgressBar) findViewById(R.id.progress_bar_dash_map);
        progressBar.setVisibility(View.GONE);

        Bundle b = getIntent().getExtras();
        User user = b.getParcelable("edu.gatech.cs2340.thericks.User");
        user.login();
        Log.d(TAG, user.getLogin().getUsername() + " is logged in = " + user.isLoggedIn());

        mapButton.setOnClickListener((View v) -> {
            Log.d(TAG, "Rat Map Button pressed");
            if (map != null) {
                mapButton.setVisibility(View.GONE);
                listRatDataButton.setVisibility(View.GONE);
                profileButton.setVisibility(View.GONE);
                settingsButton.setVisibility(View.GONE);
                reportRatButton.setVisibility(View.GONE);
                logoutButton.setVisibility(View.GONE);

                loadFilteredMapMarkers();

                returnToDashButton.setVisibility(View.VISIBLE);
                applyFiltersButton.setVisibility(View.VISIBLE);

                date1Edit.setVisibility(View.VISIBLE);
                date2Edit.setVisibility(View.VISIBLE);

                dateSeparator.setVisibility(View.VISIBLE);

                onMap = !onMap;
                map.getUiSettings().setAllGesturesEnabled(onMap);
            }
        });

        graphButton.setOnClickListener((View v) ->  {
            Log.d(TAG, "Graph button pressed");
            Context context = v.getContext();
            Intent intent = new Intent(context, GraphActivity.class);
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

        returnToDashButton.setOnClickListener(v -> {
            if (map != null) {
                mapButton.setVisibility(View.VISIBLE);
                listRatDataButton.setVisibility(View.VISIBLE);
                profileButton.setVisibility(View.VISIBLE);
                settingsButton.setVisibility(View.VISIBLE);
                reportRatButton.setVisibility(View.VISIBLE);
                logoutButton.setVisibility(View.VISIBLE);

                returnToDashButton.setVisibility(View.GONE);
                applyFiltersButton.setVisibility(View.GONE);

                date1Edit.setVisibility(View.GONE);
                date2Edit.setVisibility(View.GONE);

                dateSeparator.setVisibility(View.GONE);

                onMap = !onMap;
                map.getUiSettings().setAllGesturesEnabled(onMap);

                map.clear();

                map.animateCamera(CameraUpdateFactory.newCameraPosition(position));
            }
        });

        applyFiltersButton.setOnClickListener(v -> {
            RatDateTime beginRange = RatDateTime.forDateTime(date1Edit.getText().toString());
            RatDateTime endRange = RatDateTime.forDateTime(date2Edit.getText().toString());
            filters.remove(dateInRange);
            dateInRange = RatDateTimeFilterer.createRatDateTimeRangeFilter(beginRange, endRange);
            filters.add(dateInRange);
            loadFilteredMapMarkers();
        });
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        Log.d(TAG, "Recieved result from RatEntryActivity");
//        if (requestCode == ADD_RAT_DATA_REQUEST) {
//            if (resultCode == RESULT_OK) {
//                Bundle b = data.getExtras();
//                RatData passedData = b.getParcelable("edu.gatech.cs2340.thericks.RatData");
//                LoadedFilteredDataHolder.add(passedData);
//            }
//        }
//    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.getUiSettings().setAllGesturesEnabled(onMap);
        map.animateCamera(CameraUpdateFactory.newCameraPosition(position));
        map.setInfoWindowAdapter(new CustomInfoWindowAdapter());
    }

    /**
     * Clears the current markers on the map, disables all map widgets,
     * then fetches rat data from the database, applying all current
     * filters, and adds a marker to the map for each one before
     * re-enabling all map widgets
     */
    private void loadFilteredMapMarkers() {
        progressBar.setVisibility(View.VISIBLE);
        date1Edit.setEnabled(false);
        date2Edit.setEnabled(false);
        returnToDashButton.setEnabled(false);
        applyFiltersButton.setEnabled(false);

        List<RatData> filteredList = new RatDatabase().getFilteredRatData(filters);
        map.clear();
        for (RatData r: filteredList) {
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(new LatLng(r.getLatitude(), r.getLongitude()));
            markerOptions.title(r.getKey() + "");
            markerOptions.snippet(r.getIncidentAddress() + ";" + r.getCreatedDateTime());
            map.addMarker(markerOptions);
        }
        progressBar.setVisibility(View.GONE);
        returnToDashButton.setEnabled(true);
        applyFiltersButton.setEnabled(true);
        date1Edit.setEnabled(true);
        date2Edit.setEnabled(true);
    }

    class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

        private final View myContentsView;

        CustomInfoWindowAdapter(){
            myContentsView = getLayoutInflater().inflate(R.layout.rat_marker_view, null);
        }

        @Override
        public View getInfoContents(Marker marker) {

            TextView titleText = (TextView) myContentsView.findViewById(R.id.rat_marker_view_title_text);
            titleText.setText(marker.getTitle());
            TextView snippetText1 = (TextView) myContentsView.findViewById(R.id.rat_marker_view_snippet1_text);
            TextView snippetText2 = (TextView) myContentsView.findViewById(R.id.rat_marker_view_snippet2_text);
            String[] splitSnippet = marker.getSnippet().split(";");
            snippetText1.setText(splitSnippet[0]);
            snippetText2.setText(splitSnippet[1]);

            return myContentsView;
        }

        @Override
        public View getInfoWindow(Marker marker) {
            return null;
        }

    }
}
