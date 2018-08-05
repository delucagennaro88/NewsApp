package com.example.utente.newsapp;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

class NewsLoader extends AsyncTaskLoader<List<News>> {

    private final String queryUrl;

    public NewsLoader(Context context, String url) {
        super(context);
        queryUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<News> loadInBackground() {
        if (queryUrl == null) {
            return null;
        }

        return QueryUtils.fetchNewsData(queryUrl);
    }
}