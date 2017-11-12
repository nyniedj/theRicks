package edu.gatech.cs2340.thericks.controllers;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import edu.gatech.cs2340.thericks.R;
import edu.gatech.cs2340.thericks.database.RatDatabase;
import edu.gatech.cs2340.thericks.database.RatTrackerApplication;
import edu.gatech.cs2340.thericks.models.RatData;

/**
 * Created by Cameron on 10/5/2017.
 * Displays the rat data from the database in a list format
 */
public class RatDataListActivity extends AppCompatActivity {

    private static final String TAG = RatDataListActivity.class.getSimpleName();
    private static final int EDIT_ENTRY = 0;

    private CustomListAdapter adapter;
    /* Filters for displayed rat data */
    private  List<Predicate<RatData>> filters;

    private ListView ratDataList;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rat_data_list);

        RatDatabase database = new RatDatabase();
        progressBar = (ProgressBar) findViewById(R.id.rat_data_list_progress_bar);

        if (adapter == null) {
            adapter = new CustomListAdapter(RatTrackerApplication.getAppContext(), new ArrayList<>());
        } else {
            // Check if data has been loaded and hide progress bar if so
            if (!adapter.listData.isEmpty()) {
                progressBar.setVisibility(View.GONE);
            }
        }

        // Display empty list view until data finishes loading
        ratDataList = (ListView) findViewById(R.id.rat_data_list_view);

        ratDataList.setAdapter(adapter);
        ratDataList.setOnItemClickListener((AdapterView<?> a, View v, int position, long id) -> {
            Object o = ratDataList.getItemAtPosition(position);
            Parcelable ratData = (RatData) o;
            Context context = v.getContext();
            Intent intent = new Intent(context, RatEntryActivity.class);
            intent.putExtra("edu.gatech.cs2340.thericks.RatData", ratData);
            startActivityForResult(intent, EDIT_ENTRY);
        });

        /* NOTE: Hard coded predicates for testing display filters. Remove once user can add filters. */
        Predicate<RatData> inJamaica = ratData -> "Jamaica".equalsIgnoreCase(ratData.getCity());
        Predicate<RatData> commercialLocation = ratData -> "Commercial Building".equalsIgnoreCase(ratData.getLocationType());
        if (filters == null) {
            filters = new ArrayList<>();
        }
        filters.add(inJamaica);
        filters.add(commercialLocation);
        /* End of predicates */

        // Load in rat data
        Log.d(TAG, "Calling the RatDatabase to load the data");
        progressBar.setVisibility(View.VISIBLE);
        database.loadData(adapter, adapter.listData, filters);
    }

    /**
     * Custom adapter to populate the list view of rat data
     */
    private class CustomListAdapter extends ArrayAdapter {
        private final List<RatData> listData;
        private final LayoutInflater layoutInflater;

        /**
         * Creates a list view with RatData Objects
         * @param aContext the Context that sends a populated list view object
         *                 to the layoutInflater
         * @param listData the list of RatData Objects
         */
        public CustomListAdapter(Context aContext, List<RatData> listData) {
            super(aContext, ArrayAdapter.NO_SELECTION);
            this.listData = new ArrayList<>(listData);
            layoutInflater = LayoutInflater.from(aContext);
        }

        @Override
        public void notifyDataSetChanged() {
            super.notifyDataSetChanged();
            progressBar.setVisibility(View.GONE);
        }

        @Override
        public int getCount() {
            return listData.size();
        }

        @Override
        public Object getItem(int position) {
            return listData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        @NonNull
        public View getView(int position, View convertView, @NonNull ViewGroup parent) {
            ViewHolder holder;
            View rtnView;
            if (convertView == null) {
                rtnView = layoutInflater.inflate(R.layout.rat_list_row_view, null);
                holder = new ViewHolder();
                holder.cityView = convertView.findViewById(R.id.rat_data_city_text);
                holder.addressView = convertView.findViewById(R.id.rat_data_incident_address_text);
                holder.createdDateView = convertView.findViewById(R.id.rat_data_date_text);
                rtnView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
                rtnView = convertView;
            }

            holder.cityView.setText(listData.get(position).getCity());
            holder.addressView.setText(listData.get(position).getIncidentAddress());
            holder.createdDateView.setText(listData.get(position).getCreatedDateTime().toString());
            return rtnView;
        }
        /**
         * Holds all of the views for each RatData Object
         */
        private class ViewHolder {
            private TextView cityView;
            private TextView addressView;
            private TextView createdDateView;
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (adapter != null && filters != null) {
            Log.d(TAG, "Updating list view to show changes to database");
            adapter.listData.clear();
            adapter.listData.addAll(new RatDatabase().getFilteredRatData(filters));
            adapter.notifyDataSetChanged();
        }
    }
}