package edu.gatech.cs2340.thericks.controllers;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import edu.gatech.cs2340.thericks.R;
import edu.gatech.cs2340.thericks.database.RatDatabase;
import edu.gatech.cs2340.thericks.database.RatTrackerApplication;
import edu.gatech.cs2340.thericks.models.RatData;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * Created by Cameron on 10/23/2017.
 * A simple Google Map displayer that loads in RatData and displays
 * markers at each LatLng position defined by the data
 */
public class MapActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final String TAG = MapActivity.class.getSimpleName();

    private GoogleMap mMap;

    private ArrayList<RatData> ratDataArrayList;
    private static RatDatabase database;

    /* Filters for displayed rat data */
    private static List<Predicate<RatData>> filters;

    private static ArrayAdapter<RatData> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        if (database == null) {
            database = new RatDatabase(this);
        }
        if (filters == null) {
            filters = new ArrayList<>();
        }
        if (ratDataArrayList == null) {
            ratDataArrayList = new ArrayList<>();
        }
        if (adapter == null) {
            adapter = new ArrayAdapter<RatData>(RatTrackerApplication.getAppContext(), ArrayAdapter.NO_SELECTION) {

                @Override
                public void notifyDataSetChanged() {
                    super.notifyDataSetChanged();
                    //mMap.clear();
                    for (RatData r: ratDataArrayList) {
                        MarkerOptions markerOptions = new MarkerOptions();
                        markerOptions.position(new LatLng(r.getLatitude(), r.getLongitude()));
                        mMap.addMarker(markerOptions);
                    }
                }

            };
        }

        /* NOTE: Hard coded predicates for testing display filters. Remove once user can add filters. */
        Predicate<RatData> inNewYork = ratData -> ratData.getCity().equalsIgnoreCase("NEW YORK");
        Predicate<RatData> commercialLocation = ratData -> ratData.getLocationType().equalsIgnoreCase("Commercial Building");
        if (filters.isEmpty()) {
            filters.add(inNewYork);
            filters.add(commercialLocation);
        }
        /* End of predicates */
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        database.loadData(adapter, ratDataArrayList, null, filters);

        // Setting a click event handler for the map
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng latLng) {

                // Creating a marker
//                MarkerOptions markerOptions = new MarkerOptions();
//
//                // Setting the position for the marker
//                markerOptions.position(latLng);
//
//                // Animating to the touched position
//                mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
//
//                // Placing a marker on the touched position
//                mMap.addMarker(markerOptions);
            }
        });

        //mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter());
    }

//    class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {
//
//        private final View myContentsView;
//
//        CustomInfoWindowAdapter(){
//            myContentsView = getLayoutInflater().inflate(R.layout.custom_info_contents, null);
//        }
//
//        @Override
//        public View getInfoContents(Marker marker) {
//
//            TextView tvTitle = ((TextView)myContentsView.findViewById(R.id.title));
//            tvTitle.setText(marker.getTitle());
//            TextView tvSnippet = ((TextView)myContentsView.findViewById(R.id.snippet));
//            tvSnippet.setText(marker.getSnippet());
//
//            return myContentsView;
//        }
//
//        @Override
//        public View getInfoWindow(Marker marker) {
//            return null;
//        }
//
//    }

}
