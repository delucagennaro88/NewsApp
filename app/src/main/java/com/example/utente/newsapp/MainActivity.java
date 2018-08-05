package com.example.utente.newsapp;

import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
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

public class MainActivity extends AppCompatActivity {

    private static final String REQUEST_URL = "http://content.guardianapis.com/search";
    private static final int NEWS_LOADER_ID = 1;
    String API_KEY = BuildConfig.THE_GUARDIAN_API_KEY;
    private Context currentArticleContext;

    private NewsAdapter articleAdapter;

    private TextView emptyView;

    private final LoaderCallbacks<List<News>> newsLoader
            = new LoaderCallbacks<List<News>>() {

        @Override
        public Loader<List<News>> onCreateLoader(int i, Bundle bundle) {

            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(currentArticleContext);

            String searchPhrase = preferences.getString(
                    getString(R.string.title_key),
                    getString(R.string.title_default));

            String makeOrder = preferences.getString(
                    getString(R.string.order_by_key),
                    getString(R.string.order_by_default));

            Uri baseUri = Uri.parse(REQUEST_URL);

            Uri.Builder uriBuilder = baseUri.buildUpon();

            if (searchPhrase != "") {
                uriBuilder.appendQueryParameter("q", searchPhrase);
            }
            uriBuilder.appendQueryParameter("show-fields", "thumbnail");
            uriBuilder.appendQueryParameter("show-tags", "contributor");
            uriBuilder.appendQueryParameter("order-by", makeOrder);
            uriBuilder.appendQueryParameter("api-key", API_KEY);

            return new NewsLoader(currentArticleContext, uriBuilder.toString());
        }

        @Override
        public void onLoadFinished(Loader<List<News>> loader, List<News> news) {
            View loadingLine = findViewById(R.id.loading_line);
            loadingLine.setVisibility(View.GONE);

            emptyView.setText(R.string.no_articles);

            if (news != null && !news.isEmpty()) {
                articleAdapter.addAll(news);
            }
        }

        @Override
        public void onLoaderReset(Loader<List<News>> loader) {
            articleAdapter.clear();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_activity);

        currentArticleContext = this;

        ListView articleListView = findViewById(R.id.list);

        emptyView = findViewById(R.id.empty_view);
        articleListView.setEmptyView(emptyView);

        articleAdapter = new NewsAdapter(this, new ArrayList<News>());

        articleListView.setAdapter(articleAdapter);

        articleListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                News currentNews = articleAdapter.getItem(position);

                Uri articleUri = Uri.parse(currentNews.getArticleUrl());

                Intent webIntent = new Intent(Intent.ACTION_VIEW, articleUri);

                startActivity(webIntent);
            }
        });

        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {

            LoaderManager loaderManager = getLoaderManager();

            loaderManager.initLoader(NEWS_LOADER_ID, null, newsLoader);
        } else {
            View loadingLine = findViewById(R.id.loading_line);
            loadingLine.setVisibility(View.GONE);

            emptyView.setText(R.string.error_connection);
        }
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
            Intent settingsIntent = new Intent(this, SettingActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}