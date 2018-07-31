package com.example.utente.newsapp;

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

public class QueryUtils {
    private QueryUtils() {
    }

    public static List<News> fetchNewsData(String requestUrl) {
        URL url = createUrl(requestUrl);

        String jsonBack = null;
        try {
            jsonBack = makeHttpRequest(url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<News> news_on_list = extractFeatureFromJson(jsonBack);

        return news_on_list;
    }

    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {

            e.printStackTrace();
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonBack = "";

        if (url == null) {
            return jsonBack;
        }

        HttpURLConnection connectURL = null;
        InputStream stream = null;
        try {
            connectURL = (HttpURLConnection) url.openConnection();
            connectURL.setReadTimeout(10000 /* milliseconds */);
            connectURL.setConnectTimeout(15000 /* milliseconds */);
            connectURL.setRequestMethod("GET");
            connectURL.connect();

            if (connectURL.getResponseCode() == 200) {
                stream = connectURL.getInputStream();
                jsonBack = read(stream);
            } else {
                Log.d("Errore: ", String.valueOf(connectURL.getResponseCode()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (connectURL != null) {
                connectURL.disconnect();
            }
            if (stream != null) {

                stream.close();
            }
        }
        return jsonBack;
    }

    private static String read(InputStream stream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (stream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(stream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    private static List<News> extractFeatureFromJson(String newsJSON) {
        if (TextUtils.isEmpty(newsJSON)) {
            return null;
        }

        List<News> news_on_list = new ArrayList<>();

        try {

            JSONObject baseJsonResponse = new JSONObject(newsJSON);
            JSONObject response = baseJsonResponse.getJSONObject("response");
            JSONArray resultsArray = response.getJSONArray("results");

            for (int i = 0; i < resultsArray.length(); i++) {

                JSONObject currentResults = resultsArray.getJSONObject(i);

                String Title = currentResults.getString("webTitle");
                String category = currentResults.getString("sectionName");
                String date = currentResults.getString("webPublicationDate");
                String url = currentResults.getString("webUrl");
                JSONArray tagsauthor = currentResults.getJSONArray("tags");
                String author="";
                if (tagsauthor.length()!= 0) {
                    JSONObject currenttagsauthor = tagsauthor.getJSONObject(0);
                    author = currenttagsauthor.getString("webTitle");
                }else{
                    author = "No Author ..";
                }

                News news = new News(Title, category, date, url, author);

                news_on_list.add(news);
            }

        } catch (JSONException e) {

            e.printStackTrace();
        }

        return news_on_list;
    }
}
