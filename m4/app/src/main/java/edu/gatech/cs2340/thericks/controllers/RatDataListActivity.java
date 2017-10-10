package edu.gatech.cs2340.thericks.controllers;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import edu.gatech.cs2340.thericks.R;
import edu.gatech.cs2340.thericks.models.RatData;
import edu.gatech.cs2340.thericks.models.RatDataManager;

/**
 * Created by Cameron on 10/5/2017.
 */

public class RatDataListActivity extends AppCompatActivity {

    private static final String TAG = RatDataListActivity.class.getSimpleName();

    private final static int DISPLAY_LIMIT = 50; // Max number of rat sightings to display at once

    private ListView ratDataList;
    private CustomListAdapter adapter;

    /* Filters for displayed rat data */
    private List<Predicate<RatData>> filters;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rat_data_list);

        filters = new ArrayList<>();

        /* Display empty list view until data finishes loading. */
        ratDataList = (ListView) findViewById(R.id.rat_data_list_view);
        adapter = new CustomListAdapter(ratDataList.getContext(), new ArrayList<>());
        ratDataList.setAdapter(adapter);
        ratDataList.setOnItemClickListener((AdapterView<?> a, View v, int position, long id) -> {
            Object o = ratDataList.getItemAtPosition(position);
            RatData ratData = (RatData) o;
            Log.d("Rat Data List", "Selected: " + ratData.toString());
        });
        ratDataList.setOnItemLongClickListener((AdapterView<?> a, View v, int position, long id) -> {
            Object o = ratDataList.getItemAtPosition(position);
            RatData ratData = (RatData) o;
            Log.d("Rat Data List", "Selected for opening: " + ratData.toString());
            Context context = v.getContext();
            Intent intent = new Intent(context, RatEntryActivity.class);
            intent.putExtra("edu.gatech.cs2340.thericks.RatData", ratData);
            context.startActivity(intent);
            return true;
        });

        /* NOTE: Hard coded predicates for testing display filters. Remove once user can add filters. */
//        Predicate<RatData> inNewYork = ratData -> ratData.getCity().equalsIgnoreCase("NEW YORK");
//        Predicate<RatData> commercialLocation = ratData -> ratData.getLocationType().equalsIgnoreCase("Commercial Building");
//        filters.add(inNewYork);
//        filters.add(commercialLocation);
        /* End of predicates */

        /* Check if rat data has already been loaded. */
        if (!RatDataManager.getInstance().isDataLoaded()) {
            // Load in data for first time
            new LoadRatDataTask().execute();

        } else {
            // Already loaded
            updateRatDataList();
        }
    }

    private class CustomListAdapter extends BaseAdapter {
        private ArrayList<RatData> listData;
        private LayoutInflater layoutInflater;

        public CustomListAdapter(Context aContext, ArrayList<RatData> listData) {
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

        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = layoutInflater.inflate(R.layout.rat_list_row_layout, null);
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

    private class LoadRatDataTask extends AsyncTask<Void, Void, Long> {
        protected Long doInBackground(Void... voids) {
            RatDataManager mgr = RatDataManager.getInstance();
            long lineCount = 0;
            try {
                InputStream input = getResources().openRawResource(R.raw.rat_data);
                BufferedReader br = new BufferedReader(new InputStreamReader(input, StandardCharsets.UTF_8));

                String line;
                br.readLine(); //get rid of header line
                while ((line = br.readLine()) != null) {
                    lineCount++;
                    String[] tokens = line.split(",");

                    int key;
                    try {
                        key = Integer.parseInt(tokens[0]);
                    } catch (NumberFormatException e) {
                        key = 0;
                    }
                    String createdDateTime = tokens[1];
                    String locationType = tokens[7];
                    int incidentZip;
                    try {
                        incidentZip = Integer.parseInt(tokens[8]);
                    } catch (NumberFormatException e) {
                        incidentZip = 0;
                    }
                    String incidentAddress = tokens[9];
                    String city = tokens[16];
                    String borough = tokens[23];
                    double latitude;
                    try {
                        latitude = Double.parseDouble(tokens[25]);
                    } catch (NumberFormatException e) {
                        latitude = 0;
                    }
                    double longitude;
                    try {
                        longitude = Double.parseDouble(tokens[24]);
                    } catch (NumberFormatException e) {
                        longitude = 0;
                    }
                    mgr.addRatData(key, createdDateTime, locationType, incidentZip, incidentAddress, city, borough, latitude, longitude);
                }
                br.close();
            } catch (IOException e) {
                Log.e(TAG, "error reading rat data", e);
            }
            return lineCount;
        }

        protected void onPostExecute(Long result) {
            Log.d(TAG, "Loaded " + result + " rat data entries");
            updateRatDataList();
        }
    }

    /**
     * Update the list view to show the first %DISPLAY_LIMIT% rat data entries that satisfy all
     * currently applied filters.
     */
    private void updateRatDataList() {
        ArrayList<RatData> dataToDisplay = new ArrayList<>();
        List<RatData> fullList = RatDataManager.getInstance().getRatDataList();
        List<RatData> filteredDataList = applyFilters(fullList, filters);

        for (int i = 0; i < DISPLAY_LIMIT && i < filteredDataList.size(); i++) {
            dataToDisplay.add(filteredDataList.get(i));
        }
        adapter = new CustomListAdapter(ratDataList.getContext(), dataToDisplay);
        ratDataList.setAdapter(adapter);
        // Inform the list view of changes to adapter
        adapter.notifyDataSetChanged();
        // Done loading data
        RatDataManager.getInstance().finishLoading();
    }


    /**
     * Returns list of rat data that satisfy all of the provided filters.
     *
     * @param filters List of filters to apply
     * @return List of data in full list satisfying all filters
     */
    private List<RatData> applyFilters(List<RatData> fullList, List<Predicate<RatData>> filters) {
        Predicate<RatData> allPredicates = filters.stream().reduce(f -> true, Predicate::and);
        return fullList.stream().filter(allPredicates).collect(Collectors.toList());
    }
}
