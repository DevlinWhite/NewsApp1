package com.example.android.newsapp1;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

import java.util.List;

public final class QueryUtils {

    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    private QueryUtils() {
    }

    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();


            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {

                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    private static ArrayList<News> extractFeatureFromJson(String newsJson) {
        if (TextUtils.isEmpty(newsJson)) {
            return null;
        }

        ArrayList<News> news = new ArrayList<>();

        try {
            JSONObject rootObject = new JSONObject(newsJson);
            JSONObject response = rootObject.optJSONObject("response");
            JSONArray jsonArray = response.optJSONArray("results");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject story = jsonArray.optJSONObject(i);
                String title = story.optString("Title");
                String section = story.optString("Section");
                String url = story.optString("Url");
                String publicationDate = story.getString("Date");
                JSONObject fields = story.optJSONObject("fields");
                if (fields != null) {

                }
                JSONArray tags = story.optJSONArray("tags");
                String authorName = null;
                if (tags.length() > 0) {
                    JSONObject author = tags.optJSONObject(0);
                    authorName = author.optString("Name");
                }
                news.add(new News(title, url, publicationDate, authorName, section));
                Log.d(LOG_TAG, news.get(i).toString());
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem with parsing JSON News results", e);
        }
        return news;
    }

    public static ArrayList<News> getNewsData(String requestUrl) {

        URL url = createUrl(requestUrl);

        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        ArrayList<News> news = extractFeatureFromJson(jsonResponse);

        return news;
    }
}

