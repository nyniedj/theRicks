package edu.gatech.cs2340.thericks.database;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

import edu.gatech.cs2340.thericks.models.RatData;

/**
 * Created by mkcac on 10/24/2017.
 */

public class LoadedFilteredDataHolder {
    private static ArrayList<RatData> filteredData = new ArrayList<>();

    private static boolean loadedData = false;

    public static void loadData(ArrayAdapter<RatData> adapter, ProgressBar progressBar, List<Predicate<RatData>> filters) {
        if (!loadedData) {
            ArrayList<RatData> holdArr = new ArrayList<>();
            ArrayAdapter<RatData> tempAdapter = new ArrayAdapter<RatData>(RatTrackerApplication.getAppContext(), ArrayAdapter.NO_SELECTION) {

                @Override
                public void notifyDataSetChanged() {
                    super.notifyDataSetChanged();
                    if (adapter != null) {
                        adapter.notifyDataSetChanged();
                    }
                    loadedData = true;
                    LoadedFilteredDataHolder.addAll(holdArr);
                }

            };
            RatDatabase database = new RatDatabase(RatTrackerApplication.getAppContext());
            if (filteredData.isEmpty()) {
                database.loadData(tempAdapter, filteredData, progressBar, filters);
            } else {
                holdArr.addAll(filteredData);
                filteredData.clear();
                database.loadData(tempAdapter, filteredData, progressBar, filters);
            }
        } else {
            progressBar.setVisibility(View.GONE);
            adapter.notifyDataSetChanged();
        }
    }

    public static void add(RatData data) {
        for (int i = 0; i < filteredData.size(); i++) {
            if (filteredData.get(i).getKey() == data.getKey()) {
                filteredData.set(i, data);
                return;
            }
        }
        filteredData.add(data);
    }

    public static void addAll(Collection<RatData> c) {
        for (RatData r: c) {
            add(r);
        }
    }

    public static RatData get(int index) {
        return filteredData.get(index);
    }

    public static final ArrayList<RatData> getFilteredData() {
        return filteredData;
    }
}
