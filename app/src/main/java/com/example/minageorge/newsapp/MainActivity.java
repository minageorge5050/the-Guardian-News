package com.example.minageorge.newsapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import com.example.minageorge.newsapp.adapters.ExpandableListAdapter;
import com.example.minageorge.newsapp.adapters.RecyclerAdapter;
import com.example.minageorge.newsapp.network.CheckConnection;
import com.example.minageorge.newsapp.pojos.News;
import com.example.minageorge.newsapp.tasks.NewsLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;

    @BindView(R.id.exp_list)
    ExpandableListView mExpandableListView;

    @BindView(R.id.recycler)
    RecyclerView mRecyclerView;

    @BindView(R.id.progress)
    ProgressBar mProgressBar;

    @BindView(R.id.empty_view)
    LinearLayout empty_view;

    @BindView(R.id.net_view)
    LinearLayout net_view;

    private Intent mIntent;
    private ActionBarDrawerToggle toggle;
    private ExpandableListAdapter mExpandableListAdapter;
    private List<String> headers;
    private HashMap<String, List<String>> children;
    private RecyclerAdapter mRecyclerAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private NewsLoader mNewsLoader;
    private Bundle mBundle;
    private String url;
    private String url2;
    private int loaderId = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);

        toggle = new ActionBarDrawerToggle(
                this, drawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        headers = new ArrayList<>();
        children = new HashMap<String, List<String>>();

        headers.add(getResources().getString(R.string.home)); // 0
        headers.add(getResources().getString(R.string.uk));// 1
        headers.add(getResources().getString(R.string.world));// 2
        headers.add(getResources().getString(R.string.sport));// 3
        headers.add(getResources().getString(R.string.lifestyle));// 4
        headers.add(getResources().getString(R.string.money));// 5
        headers.add(getResources().getString(R.string.environment));// 6
        headers.add(getResources().getString(R.string.crosswords));// 7
        headers.add(getResources().getString(R.string.video));// 8
        List<String> array1 = new ArrayList<>();
        array1.addAll(Arrays.asList(getResources().getStringArray(R.array.uk_array)));
        List<String> array2 = new ArrayList<>();
        array2.addAll(Arrays.asList(getResources().getStringArray(R.array.world_array)));
        List<String> array3 = new ArrayList<>();
        array3.addAll(Arrays.asList(getResources().getStringArray(R.array.sport_array)));
        List<String> array4 = new ArrayList<>();
        array4.addAll(Arrays.asList(getResources().getStringArray(R.array.lifestyle_array)));
        List<String> array5 = new ArrayList<>();
        array5.addAll(Arrays.asList(getResources().getStringArray(R.array.money_array)));
        List<String> array6 = new ArrayList<>();
        array6.addAll(Arrays.asList(getResources().getStringArray(R.array.environment_array)));
        List<String> array7 = new ArrayList<>();
        array7.addAll(Arrays.asList(getResources().getStringArray(R.array.crosswords_array)));
        List<String> array8 = new ArrayList<>();
        array8.addAll(Arrays.asList(getResources().getStringArray(R.array.video_array)));
        children.put(headers.get(1), array1);
        children.put(headers.get(2), array2);
        children.put(headers.get(3), array3);
        children.put(headers.get(4), array4);
        children.put(headers.get(5), array5);
        children.put(headers.get(6), array6);
        children.put(headers.get(7), array7);
        children.put(headers.get(8), array8);

        mExpandableListAdapter = new ExpandableListAdapter(this, headers, children);
        mExpandableListView.setAdapter(mExpandableListAdapter);
        mExpandableListView.setGroupIndicator(null);
        mRecyclerAdapter = new RecyclerAdapter(getApplicationContext(), MainActivity.this);
        mLayoutManager = new GridLayoutManager(this, 1);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mBundle = new Bundle();
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        String qsearch = sharedPrefs.getString(
                getString(R.string.settings_home_key),
                getString(R.string.settings_home_default));
        url = "https://content.guardianapis.com/search?q=" + qsearch + "&show-fields=thumbnail&api-key=01732dbd-c8a9-4f87-8f4d-ab84fd99d5fa";
        mBundle.putString("url", url);
        mNewsLoader = (NewsLoader) getSupportLoaderManager().initLoader(loaderId, mBundle, mListLoaderCallbacks);
        mRecyclerView.setAdapter(mRecyclerAdapter);
        mExpandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                if (!headers.get(groupPosition).equals("home")) {
                    url2 = "https://content.guardianapis.com/search?q=" + headers.get(groupPosition) + "&show-fields=thumbnail&from-date=2010-07-01&api-key=01732dbd-c8a9-4f87-8f4d-ab84fd99d5fa";
                    mBundle.putString("url", url2);
                    getSupportLoaderManager().restartLoader(loaderId, mBundle, mListLoaderCallbacks);
                }
            }
        });
        mExpandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                if (headers.get(groupPosition).equals("home")) {
                    mBundle.putString("url", url);
                    getSupportLoaderManager().restartLoader(loaderId, mBundle, mListLoaderCallbacks);
                }
                return false;
            }
        });
        mExpandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                url2 = "https://content.guardianapis.com/search?q=" + children.get(headers.get(groupPosition)).get(childPosition) + "&show-fields=thumbnail&from-date=2010-07-01&api-key=01732dbd-c8a9-4f87-8f4d-ab84fd99d5fa";
                mBundle.putString("url", url2);
                getSupportLoaderManager().restartLoader(loaderId, mBundle, mListLoaderCallbacks);
                return false;
            }
        });
        empty_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportLoaderManager().restartLoader(loaderId, mBundle, mListLoaderCallbacks);
            }
        });
    }

    public LoaderManager.LoaderCallbacks<List<News>> mListLoaderCallbacks = new LoaderManager.LoaderCallbacks<List<News>>() {
        @Override
        public Loader<List<News>> onCreateLoader(int id, Bundle args) {
            mProgressBar.setVisibility(View.VISIBLE);
            empty_view.setVisibility(View.GONE);
            net_view.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
            return new NewsLoader(getApplicationContext(), args);
        }


        @Override
        public void onLoadFinished(Loader<List<News>> loader, List<News> data) {
            mProgressBar.setVisibility(View.INVISIBLE);
            mRecyclerAdapter.swapdata(data);
            if (data.isEmpty()) {
                empty_view.setVisibility(View.VISIBLE);
                mRecyclerView.setVisibility(View.GONE);
            }
            if (!new CheckConnection(getApplicationContext()).isconnected()) {
                net_view.setVisibility(View.VISIBLE);
                mRecyclerView.setVisibility(View.GONE);
                empty_view.setVisibility(View.GONE);
            }
        }

        @Override
        public void onLoaderReset(Loader<List<News>> loader) {
            mRecyclerAdapter.swapdata(Collections.EMPTY_LIST);
            mNewsLoader = null;
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_settings:
                mIntent = new Intent(this, SettActivity.class);
                startActivity(mIntent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
