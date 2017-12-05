package edu.gatech.cs2340.thericks.controllers;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

import edu.gatech.cs2340.thericks.R;
import edu.gatech.cs2340.thericks.database.RatDatabase;
import edu.gatech.cs2340.thericks.models.RatData;
import edu.gatech.cs2340.thericks.models.RatFilter;

/**
 * Created by Cameron on 12/4/2017.
 * Activity to hold all the tabs for all graphs
 */
public class GraphTabActivity extends AppCompatActivity {

    private static final String TAG = GraphTabActivity.class.getSimpleName();

    private ProgressBar progressBar;
    private Button applyFiltersButton;

    private RatFilter filter;
    private List<RatData> loadedData;

    private ViewPager viewPager;
    private GraphPagerAdapter adapter;

    private boolean[] displayed;

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_graph_tab);

        filter = RatFilter.getDefaultInstance();

        progressBar = findViewById(R.id.graph_progress_bar);
        applyFiltersButton = findViewById(R.id.apply_filters_button_graph);
        applyFiltersButton.setEnabled(false);
        applyFiltersButton.setOnClickListener(v -> {
            Context context = v.getContext();
            Intent intent2 = new Intent(context, FilterActivity.class);
            intent2.putExtra(FilterActivity.FILTER, filter);
            startActivityForResult(intent2, FilterActivity.GET_FILTER);
        });

        TabLayout tabLayout = findViewById(R.id.tab_layout);
        tabLayout.addTab(
                tabLayout.newTab().setText(getResources().getString(R.string.line_graph_tab)));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.addTab(
                tabLayout.newTab().setText(getResources().getString(R.string.bar_graph_tab)));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        viewPager = findViewById(R.id.pager);
        adapter = new GraphPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                if (!adapter.isDisplayed(viewPager.getCurrentItem())) {
                    adapter.getItem(viewPager.getCurrentItem()).displayGraph(loadedData);
                    adapter.setDisplayed(viewPager.getCurrentItem(), true);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }

        });

        loadedData = new ArrayList<>();
        ArrayAdapter<RatData> tempAdapter
                = new ArrayAdapter<RatData>(this, ArrayAdapter.NO_SELECTION) {

            @Override
            public void notifyDataSetChanged() {
                super.notifyDataSetChanged();
                Log.d(TAG, "Notified that the data finished loading");

                GraphFragment<RatData> frag = adapter.getItem(viewPager.getCurrentItem());
                frag.displayGraph(loadedData);
                adapter.setDisplayed(viewPager.getCurrentItem(), true);

                progressBar.setVisibility(View.GONE);
                applyFiltersButton.setEnabled(true);
            }
        };

        RatDatabase db = new RatDatabase();
        db.loadData(tempAdapter, loadedData, filter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == FilterActivity.GET_FILTER) {
            if (resultCode == RESULT_OK) {
                filter = data.getParcelableExtra(FilterActivity.FILTER);
                ArrayAdapter<RatData> tempAdapter = new ArrayAdapter<RatData>(
                        getApplicationContext(), ArrayAdapter.NO_SELECTION) {

                    @Override
                    public void notifyDataSetChanged() {
                        super.notifyDataSetChanged();
                        Log.d(TAG, "Notified that the data finished loading");

                        GraphFragment<RatData> frag = adapter.getItem(viewPager.getCurrentItem());
                        frag.displayGraph(loadedData);
                        adapter.setDisplayed(viewPager.getCurrentItem(), true);

                        progressBar.setVisibility(View.GONE);
                        applyFiltersButton.setEnabled(true);
                    }
                };
                progressBar.setVisibility(View.VISIBLE);
                applyFiltersButton.setEnabled(false);

                for (int i = 0; i < adapter.getCount(); i++) {
                    adapter.getItem(i).getChart().setVisibility(View.GONE);
                    adapter.setDisplayed(i, false);
                }

                RatDatabase db = new RatDatabase();
                db.loadData(tempAdapter, loadedData, filter);
            }
        }
    }
}
