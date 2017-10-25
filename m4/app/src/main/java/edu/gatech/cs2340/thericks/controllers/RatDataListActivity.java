package edu.gatech.cs2340.thericks.controllers;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import edu.gatech.cs2340.thericks.database.LoadedFilteredDataHolder;
import edu.gatech.cs2340.thericks.models.RatData;

/**
 * Created by Cameron on 10/5/2017.
 */

public class RatDataListActivity extends AppCompatActivity {

    static final int EDIT_RAT_DATA_REQUEST = 0;

    private static final String TAG = RatDataListActivity.class.getSimpleName();

    private static CustomListAdapter adapter;
    /* Filters for displayed rat data */
    private static List<Predicate<RatData>> filters = new ArrayList<>();

    private ListView ratDataList;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rat_data_list);

        RatDatabase database = new RatDatabase(this);
        progressBar = (ProgressBar) findViewById(R.id.rat_data_list_progress_bar);

        if (adapter == null) {
            adapter = new CustomListAdapter(RatTrackerApplication.getAppContext(), LoadedFilteredDataHolder.getFilteredData());
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
            RatData ratData = (RatData) o;
            Context context = v.getContext();
            Intent intent = new Intent(context, RatEntryActivity.class);
            intent.putExtra("edu.gatech.cs2340.thericks.RatData", ratData);
            intent.putExtra("INDEX", position);
            startActivityForResult(intent, EDIT_RAT_DATA_REQUEST);
        });

        /* NOTE: Hard coded predicates for testing display filters. Remove once user can add filters. */
        Predicate<RatData> inNewYork = ratData -> ratData.getCity().equals("Jamaica");
        Predicate<RatData> commercialLocation = ratData -> ratData.getLocationType().equalsIgnoreCase("Commercial Building");
        if (filters.isEmpty()) {
            filters.add(inNewYork);
            filters.add(commercialLocation);
        }
        /* End of predicates */

        // Load in rat data
//        Log.d(TAG, "Calling the RatDatabase to load the data");
//        database.loadData(adapter, adapter.listData, progressBar, filters);

        Log.d(TAG, "Calling the DataHolder to load the data");
        database.loadData(adapter, adapter.listData, progressBar, filters);
        LoadedFilteredDataHolder.loadData(adapter, progressBar, filters);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "Recieved result from RatEntryActivity");
        if (requestCode == EDIT_RAT_DATA_REQUEST) {
            if (resultCode == RESULT_OK) {
                progressBar.setVisibility(View.VISIBLE);
                ratDataList.setVisibility(View.GONE);
                Bundle b = data.getExtras();
                RatData passedData = b.getParcelable("edu.gatech.cs2340.thericks.RatData");
                int index = b.getInt("INDEX");
                RatData currDataAtIndex = adapter.listData.get(index);
                if (passedData.getKey() == currDataAtIndex.getKey()) {
                    Log.d(TAG, "Updating existing RatData");
                    adapter.listData.set(index, passedData);
                    ((CustomListAdapter) ratDataList.getAdapter()).notifyDataSetChanged();
                } else {
                    Log.d(TAG, "Adding new RatData");
                    adapter.listData.add(passedData);
                }
                ((CustomListAdapter) ratDataList.getAdapter()).notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
                ratDataList.setVisibility(View.VISIBLE);
//                progressBar.setVisibility(View.VISIBLE);
//                ratDataList.setVisibility(View.GONE);
//                ratDataArrayList.clear();
//                Log.d(TAG, "Refreshing data from the database");
//                ratDataArrayList.addAll(database.getFilteredRatData(filters));
//                ((CustomListAdapter) ratDataList.getAdapter()).notifyDataSetChanged();
//                progressBar.setVisibility(View.GONE);
//                ratDataList.setVisibility(View.VISIBLE);
            }
        }
    }

    /**
     * A custom array adapter for displaying RatData entries.
     * Displays the city as the primary data item, with the
     * date and time and address as secondary items
     */
    private class CustomListAdapter extends ArrayAdapter {
        private ArrayList<RatData> listData;
        private LayoutInflater layoutInflater;

        public CustomListAdapter(Context aContext, ArrayList<RatData> listData) {
            super(aContext, ArrayAdapter.NO_SELECTION);
            this.listData = listData;
            layoutInflater = LayoutInflater.from(aContext);
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

        @NonNull
        public View getView(int position, View convertView, @NonNull ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = layoutInflater.inflate(R.layout.rat_list_row_view, null);
                holder = new ViewHolder();
                holder.cityView = (TextView) convertView.findViewById(R.id.rat_data_city_text);
                holder.addressView = (TextView) convertView.findViewById(R.id.rat_data_incident_address_text);
                holder.createdDateView = (TextView) convertView.findViewById(R.id.rat_data_date_text);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.cityView.setText(listData.get(position).getCity());
            holder.addressView.setText(listData.get(position).getIncidentAddress());
            holder.createdDateView.setText(listData.get(position).getCreatedDateTime());
            return convertView;
        }

        private class ViewHolder {
            private TextView cityView;
            private TextView addressView;
            private TextView createdDateView;
        }
    }

    // Updates list view to display any changes to the database from entering/editing reports
    public static void updateUI() {
        if (adapter != null && filters != null) {
            Log.d(TAG, "Updating list view to show changes to database");
            adapter.listData.clear();
            adapter.listData.addAll(new RatDatabase(RatTrackerApplication.getAppContext()).getAllRatData());
            adapter.notifyDataSetChanged();
        }
    }
}