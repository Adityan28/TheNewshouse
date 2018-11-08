package com.example.android.thenewshouse;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<NewsItems>>{

    private static final int NEWS_LOADER_ID = 1;
    private static final String NEWS_APP_URL = "https://content.guardianapis.com/search";
    private NewsItemAdapter mAdapter;

    @Override
    public Loader<List<NewsItems>> onCreateLoader(int id, Bundle args) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        String user_query  = sharedPrefs.getString(
                getString(R.string.settings_min_magnitude_key),
                getString(R.string.settings_min_magnitude_default));
        Uri baseUri = Uri.parse(NEWS_APP_URL);
        Uri.Builder uribuilder = baseUri.buildUpon();
        uribuilder.appendQueryParameter("q",user_query );
        uribuilder.appendQueryParameter("order-by","newest");
        uribuilder.appendQueryParameter("show-fields","thumbnail");
        uribuilder.appendQueryParameter("api-key","test");
        uribuilder.appendQueryParameter("page-size","50");

        return new NewsAppLoader(this,uribuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<NewsItems>> loader, List<NewsItems> data) {
        View loadingIndicator = findViewById(R.id.progress_bar);
        loadingIndicator.setVisibility(View.GONE);

        TextView emptyView=(TextView)findViewById(R.id.empty_state_textview);
        emptyView.setText(R.string.no_news);
        mAdapter.clear();

        if (data != null && !data.isEmpty()) {
            mAdapter.addAll(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<NewsItems>> loader) {
        mAdapter.clear();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.am_listview);
        TextView emptyView = (TextView) findViewById(R.id.empty_state_textview);
        ConnectivityManager check = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo networkInfo = check.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(NEWS_LOADER_ID, null, this);
        } else {
            View loadingIndicator = findViewById(R.id.progress_bar);
            loadingIndicator.setVisibility(View.GONE);
            emptyView.setText(R.string.no_internet_connection);
        }
        mAdapter = new NewsItemAdapter(this, new ArrayList<NewsItems>());
        ListView bookListView = (ListView) findViewById(R.id.am_list);
        bookListView.setEmptyView(emptyView);

        bookListView.setAdapter(mAdapter);

        bookListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NewsItems newsItems = mAdapter.getItem(position);
                String weburl = newsItems.getmWebUrl();
                Uri uri = Uri.parse(weburl);
                Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                startActivity(intent);
            }
        });

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static class NewsAppLoader extends AsyncTaskLoader<List<NewsItems>>{
        private String mUrl;
        NewsAppLoader(Activity context , String url){
            super(context);
            mUrl=url;
        }

        @Override
        protected void onStartLoading() {
            forceLoad();
        }

        @Override
        public List<NewsItems> loadInBackground() {
            if(mUrl==null)
                return null;
            List<NewsItems> result = QueryUtils.fetchNewsData(mUrl);
            return result;
        }
    }
}
