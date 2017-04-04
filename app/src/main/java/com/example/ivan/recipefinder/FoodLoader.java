package com.example.ivan.recipefinder;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Loader;
import android.os.AsyncTask;

import java.util.List;

/**
 * Created by Ivan on 21/02/2017.
 */
public class FoodLoader extends AsyncTaskLoader<List<Food>> {
    private String URL;
    public FoodLoader(Context context, String URL) {
        super(context);
        this.URL = URL;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Food> loadInBackground() {
        if(URL == null)
            return null;
        List<Food> list = Queries.fetchFoodData(URL);
        return list;
    }
}
