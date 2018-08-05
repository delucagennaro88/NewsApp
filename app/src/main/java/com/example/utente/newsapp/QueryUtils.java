package com.example.utente.newsapp;

import android.text.TextUtils;
import android.util.Log;

import org.apache.commons.lang3.StringUtils;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

final class QueryUtils {

    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    private QueryUtils() {
    }

    public static List<News> fetchNewsData(String requestUrl) {

        URL articleUrl = createUrl(requestUrl);

        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(articleUrl);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problema con la richiesta HTTP.", e);
        }

        return extractFeatureFromJson(jsonResponse);
    }

    private static URL createUrl(String stringUrl) {
        URL articleUrl = null;
        try {
            articleUrl = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problema con URL ", e);
        }
        return articleUrl;
    }

    private static String makeHttpRequest(URL articleUrl) throws IOException {
        String jsonResponse = "";

        if (articleUrl == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) articleUrl.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Errore: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problema con JSON.", e);
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

    private static List<News> extractFeatureFromJson(String newsJSON) {

        if (TextUtils.isEmpty(newsJSON)) {
            return null;
        }

        List<News> articleList = new ArrayList<>();

        try {
            JSONObject baseJsonResponse = new JSONObject(newsJSON);

            JSONArray newsArray = baseJsonResponse.getJSONObject("response").getJSONArray("results");

            for (int i = 0; i < newsArray.length(); i++) {

                JSONObject currentNews = newsArray.getJSONObject(i);

                String articleTitle = currentNews.getString("webTitle");

                String articleSection = currentNews.getString("sectionName");

                JSONArray tags = currentNews.getJSONArray("tags");

                String articleAuthor = "";
                if (!tags.isNull(0)) {
                    JSONObject currentTag = tags.getJSONObject(0);

                    String authorFirstName = !currentTag.isNull("firstName") ? currentTag.getString("firstName") : "";

                    String authorLastName = !currentTag.isNull("lastName") ? currentTag.getString("lastName") : "";

                    articleAuthor = StringUtils.capitalize(authorFirstName.toLowerCase().trim()) + " " + StringUtils.capitalize(authorLastName.toLowerCase().trim());
                    if (authorFirstName.trim() != "" || authorLastName.trim() != "") {
                        articleAuthor = ("Author: ").concat(articleAuthor);
                    } else {
                        articleAuthor = "";
                    }
                }

                String originaleArticleDate = currentNews.getString("webPublicationDate");

                Date articleDate = null;
                try {
                    articleDate = (new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")).parse(originaleArticleDate);
                } catch (Exception e) {

                    Log.e("QueryUtils", "Problema con la data", e);
                }

                String articleUrl = currentNews.getString("webUrl");

                String articlePic = currentNews.getJSONObject("fields").getString("thumbnail");
                if (articlePic == "") {
                    articlePic = "http://via.placeholder.com/500x500";
                }

                News news = new News(articleTitle, articleSection, articleAuthor, articleDate, articleUrl, articlePic);

                articleList.add(news);
            }
        } catch (JSONException e) {
            Log.e("QueryUtils", "Problema con JSON", e);
        }

        return articleList;
    }
}