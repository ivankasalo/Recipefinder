package com.example.ivan.recipefinder;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.net.URI;
import java.net.URISyntaxException;

import com.example.ivan.recipefinder.database.FoodContract.mealEntry;

public class SearchActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    GridView view;
    EditText food;
    MealCursorAdapter adapter;
    private static final int LOADER_MEAL = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        //setTitle("");

        getLoaderManager().initLoader(LOADER_MEAL,null,this);

        food = (EditText)findViewById(R.id.food);
        LinearLayout search = (LinearLayout)findViewById(R.id.layoutID);
        search.setAlpha(0.6f);

        ImageView button = (ImageView) findViewById(R.id.search);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SearchActivity.this,FoodActivity.class);
                String placeholder = food.getText().toString().trim();
                i.putExtra("key", placeholder);
                startActivity(i);
            }
        });

        view = (GridView)findViewById(R.id.myMealsGrid_id);
        adapter = new MealCursorAdapter(this,null);
        view.setAdapter(adapter);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.myrecipes) {
            Intent intent= new Intent(this, MyRecipes.class);
            startActivity(intent);
        }
        else if(item.getItemId() == R.id.home){
            Intent upIntent = NavUtils.getParentActivityIntent(this);
            if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
                // This activity is NOT part of this app's task, so create a new task
                // when navigating up, with a synthesized back stack.
                TaskStackBuilder.create(this)
                        // Add all of this activity's parents to the back stack
                        .addNextIntentWithParentStack(upIntent)
                        // Navigate up to the closest parent
                        .startActivities();
            } else {
                // This activity is part of this app's task, so simply
                // navigate up to the logical parent activity.
                NavUtils.navigateUpTo(this, upIntent);
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[]projection = {
                mealEntry._ID,
                mealEntry.COLUMN_MEAL_NAME,
                mealEntry.COLUMN_MEAL_INGREDIENTS,
                mealEntry.COLUMN_MEAL_IMAGE,
                mealEntry.COLUMN_MEAL_URL
        };

        return new CursorLoader(this,mealEntry.TABLE_MEALS_URI,
                projection,
                null,
                null,
                null
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }
}

