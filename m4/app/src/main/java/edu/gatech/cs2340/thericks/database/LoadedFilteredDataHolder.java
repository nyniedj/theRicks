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
 * Created by Cameron on 10/24/2017.
 * Temporary data holder while database is under construction
 */
public class LoadedFilteredDataHolder {
    private static ArrayList<RatData> filteredData = new ArrayList<>();

    private static boolean loadedData = false;

    /**
     * Loads in and filters RatData Objects
     * @param adapter the ArrayAdapter that returns a view for each RatData Object
     * @param progressBar indicates the progress of loading in data
     * @param filters the filters used to select certain RatData Objects
     */
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

    /**
     * Adds a RatData Object to a filtered list
     * @param data the RatData Object being added
     */
    public static void add(RatData data) {
        for (int i = 0; i < filteredData.size(); i++) {
            if (filteredData.get(i).getKey() == data.getKey()) {
                filteredData.set(i, data);
                return;
            }
        }
        filteredData.add(data);
    }

    /**
     * Adds a Collection of RatData Objects to the class' ArrayList
     * @param c the Collection of RatData Objects being added
     */
    public static void addAll(Collection<RatData> c) {
        for (RatData r: c) {
            add(r);
        }
    }

    /**
     * Getter for a RatData Object at a specific index
     * @param index the index of the RatData Object wanted
     * @return the RatData Object at the specified index
     */
    public static RatData get(int index) {
        return filteredData.get(index);
    }

    /**
     * Getter for the filtered list of RatData Objects
     * @return the filtered list
     */
    public static final ArrayList<RatData> getFilteredData() {
        return filteredData;
    }
}
