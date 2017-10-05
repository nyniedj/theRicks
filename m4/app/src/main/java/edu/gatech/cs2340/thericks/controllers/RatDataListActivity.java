package edu.gatech.cs2340.thericks.controllers;

import android.content.Context;
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

import java.time.LocalDateTime;
import java.util.ArrayList;

import edu.gatech.cs2340.thericks.R;
import edu.gatech.cs2340.thericks.models.RatData;

/**
 * Created by Cameron on 10/5/2017.
 */

public class RatDataListActivity extends AppCompatActivity {

    private ListView ratDataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rat_data_list);

        ratDataList = (ListView) findViewById(R.id.rat_data_list_view);

        ArrayList<RatData> ratDataArrayList = new ArrayList<RatData>(); //pull data from database

        ratDataList.setAdapter(new CustomListAdapter(this, ratDataArrayList));
        ratDataList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                Object o = ratDataList.getItemAtPosition(position);
                RatData ratData = (RatData) o;
                Log.d("Rat Data List", "Selected: " + ratData.toString());
            }
        });

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
}
