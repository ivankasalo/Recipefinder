package com.example.ivan.recipefinder;

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

/**
 * Created by Ivan on 21/02/2017.
 */
public class Queries {
    private static final String LOG_TAG = Queries.class.getSimpleName();

    private static URL createURL(String StringUrl) {
        URL url = null;
        try {
            url = new URL(StringUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            Log.e(LOG_TAG, "Problem reaching the url ", e);
        }
        return url;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder builder = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                builder.append(line);
                line = reader.readLine();
            }
            ;
        }
        return builder.toString();
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String JsonResponse = "";
        if (url == null)
            return JsonResponse;
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                JsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results. ", e);
        }
        return JsonResponse;
    }

    private static List<Food> extractFood(String foodJSON) {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(foodJSON)) {
            return null;
        }

        List<Food> food = new ArrayList<>();

        try {
            JSONObject JsonRootObject = new JSONObject(foodJSON);
            JSONArray array = JsonRootObject.getJSONArray("results");

            for (int i = 0; i < array.length(); i++) {
                JSONObject foodObject = array.getJSONObject(i);
                String foodTitle = foodObject.getString("title");
                String ingredients = foodObject.getString("ingredients");
                String url = foodObject.getString("href");
                String picture = foodObject.optString("thumbnail");
                Food currentFood = new Food(foodTitle, ingredients, picture, url);
                food.add(currentFood);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("Queries", "Problem parsing the food JSON results", e);
        }
        return food;
    }

    public static List<Food> fetchFoodData(String requestUrl) {
        Log.i(LOG_TAG, "calling fetchEarthquakeData:");

        try {
            Thread.sleep(800);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // Create URL object
        URL url = createURL(requestUrl);

        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        List<Food> food = extractFood(jsonResponse);
        return food;
    }
}
