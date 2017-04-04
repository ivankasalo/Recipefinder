package com.example.ivan.recipefinder;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.ClipData;
import android.content.ContentValues;
import android.content.Intent;
import android.content.Loader;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.ivan.recipefinder.database.FoodContract.mealEntry;
import com.example.ivan.recipefinder.database.FoodDbHelper;

import java.util.ArrayList;
import java.util.List;

public class FoodActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Food>>{
    private static final String BASE_URL = "http://www.recipepuppy.com/api/?q=";
    private static final int FOOD_LOADER_ID = 1;
    private CustomAdapter adapter;
    private  String FoodURL;
    private TextView emptyState;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food);

        //setTitle("");
        ListView foodList = (ListView)findViewById(R.id.list);
        emptyState = (TextView)findViewById(R.id.empty_view);
        foodList.setEmptyView(emptyState);
        adapter = new CustomAdapter(this,new ArrayList<Food>());
        foodList.setAdapter(adapter);

        foodList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Food currentMeal = adapter.getItem(position);

                Intent i = new Intent(FoodActivity.this,MealDescription.class);

                String Meal_name = currentMeal.getName();
                String ingredients = currentMeal.getIngredients();
                String image = currentMeal.getPicture();
                String Meal_url = currentMeal.getURL();
                i.putExtra("name", Meal_name);
                i.putExtra("Meal_ingredients",ingredients);
                i.putExtra("photo", image);
                i.putExtra("url", Meal_url);
                startActivity(i);
            }
        });

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            FoodURL = BASE_URL + extras.getString("key");
            //The key argument here must match that used in the other activity
        }
        //LoaderManager loaderManager = getLoaderManager();

        // Initialize the loader. Pass in the int ID constant defined above and pass in null for
        // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
        // because this activity implements the LoaderCallbacks interface).
        getLoaderManager().initLoader(FOOD_LOADER_ID, null,this);
    }
    @Override
    public Loader<List<Food>> onCreateLoader(int id, Bundle args) {
        // Create a new loader for the given URL

        return new FoodLoader(this, FoodURL);
    }

    @Override
    public void onLoadFinished(Loader <List<Food>> loader, List<Food> data) {

        // Hide loading indicator because the data has been loaded
        View loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);

        emptyState.setText("Sorry no internet connection, please reconnenct and try again!");
        // Clear the adapter of previous meal data
        adapter.clear();

        // If there is a valid list of meals, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (data != null && !data.isEmpty()) {
            adapter.addAll(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Food>> loader) {
        // Loader reset, so we can clear out our existing data.
        adapter.clear();
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
}
