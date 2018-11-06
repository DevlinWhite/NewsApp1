package com.example.android.newsapp1;

import android.content.Context;
import android.content.AsyncTaskLoader;

import java.util.List;

public class NewsLoader extends AsyncTaskLoader <List<News>> {

    private static final String LOG_TAG = NewsLoader.class.getName();

    /** Query URL */
    private String url;

    /**
     * Constructs a new {@link NewsLoader}.
     *
     * @param context of the activity
     * @param url     to load data from
     */

    public NewsLoader (Context context, String url){
        super(context);
        this.url = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    /**
     * This is on a background thread.
     */
    @Override
    public List<News> loadInBackground() {
        if (this.url == null) {
            return null;
        }
        // Perform the network request, parse the response, and extract a list of news stories.
        return QueryUtils.getNewsData(this.url);
    }
}


