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
import edu.gatech.cs2340.thericks.models.RatData;

/**
 * Created by Cameron on 10/5/2017.
 */

public class RatDataListActivity extends AppCompatActivity {

    private static final String TAG = RatDataListActivity.class.getSimpleName();

    private ListView ratDataList;
    private static CustomListAdapter adapter;
    private static ArrayList<RatData> ratDataArrayList;
    private ProgressBar progressBar;

    private static RatDatabase database;

    /* Filters for displayed rat data */
    private static List<Predicate<RatData>> filters;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rat_data_list);

        progressBar = (ProgressBar) findViewById(R.id.rat_data_list_progress_bar);

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
            adapter = new CustomListAdapter(RatTrackerApplication.getAppContext(),ratDataArrayList);
        }
        /* Display empty list view until data finishes loading. */
        ratDataList = (ListView) findViewById(R.id.rat_data_list_view);

        ratDataList.setAdapter(adapter);
        ratDataList.setOnItemClickListener((AdapterView<?> a, View v, int position, long id) -> {
            Object o = ratDataList.getItemAtPosition(position);
            RatData ratData = (RatData) o;
            Context context = v.getContext();
            Intent intent = new Intent(context, RatEntryActivity.class);
            intent.putExtra("edu.gatech.cs2340.thericks.RatData", ratData);
            context.startActivity(intent);
        });

        /* NOTE: Hard coded predicates for testing display filters. Remove once user can add filters. */
        Predicate<RatData> inNewYork = ratData -> ratData.getCity().equalsIgnoreCase("NEW YORK");
        Predicate<RatData> commercialLocation = ratData -> ratData.getLocationType().equalsIgnoreCase("Commercial Building");
        if (filters.isEmpty()) {
            filters.add(inNewYork);
            filters.add(commercialLocation);
        }
        /* End of predicates */

        database.loadData(adapter, adapter.listData, progressBar, filters);
    }

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
}