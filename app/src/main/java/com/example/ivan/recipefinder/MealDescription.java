package com.example.ivan.recipefinder;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ivan.recipefinder.database.FoodContract;
import com.example.ivan.recipefinder.database.FoodContract.mealEntry;
import com.example.ivan.recipefinder.database.FoodDbHelper;
import com.squareup.picasso.Picasso;

public class MealDescription extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    Button save;
    Button directions;
    TextView Meal_name;
    TextView Meal_ingredients;
    private final static int EXISTING_MEAL_LOADER = 0;
    ImageView image1;
    Uri currentUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_description);
        //setTitle("");

        save = (Button) findViewById(R.id.save);
        Intent intent = getIntent();
        currentUri = intent.getData();
        Meal_name = (TextView)findViewById(R.id.Meal_name);
        Meal_ingredients = (TextView)findViewById(R.id.Meal_ingredients);
        directions = (Button) findViewById(R.id.directions);
        image1 = (ImageView)findViewById(R.id.imageView);
        if(currentUri == null) {
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                final String name = extras.getString("name");
                final String ingredients = extras.getString("Meal_ingredients");
                final String image = extras.getString("photo");
                final String url = extras.getString("url");

                Meal_name.setText(name);
                Meal_ingredients.setText(ingredients);
               if(image.equals(""))
                    image1.setVisibility(View.GONE);
                else
                Picasso.with(this).load(image).into(image1);
                final Food newMeal = new Food(name, ingredients, image, url);

                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ContentValues values = new ContentValues();
                        values.put(mealEntry.COLUMN_MEAL_NAME, name);
                        values.put(mealEntry.COLUMN_MEAL_INGREDIENTS, ingredients);
                        values.put(mealEntry.COLUMN_MEAL_IMAGE, image);
                        values.put(mealEntry.COLUMN_MEAL_URL, url);
                        getContentResolver().insert(mealEntry.TABLE_MEALS_URI, values);
                    }
                });


                directions.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Uri MealUri = Uri.parse(newMeal.getURL());

                    // Create a new intent to view the meal URI
                    Intent websiteIntent = new Intent(Intent.ACTION_VIEW, MealUri);

                    // Send the intent to launch a new activity
                    startActivity(websiteIntent);
                }
                });

            }
        }else {
            //visibility gone because the meal is already in our database
            save.setVisibility(View.GONE);
            getLoaderManager().initLoader(EXISTING_MEAL_LOADER, null, this);
        }
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
        String[] projection = {
                mealEntry._ID,
                mealEntry.COLUMN_MEAL_NAME,
                mealEntry.COLUMN_MEAL_INGREDIENTS,
                mealEntry.COLUMN_MEAL_IMAGE,
                mealEntry.COLUMN_MEAL_URL,};

        return new CursorLoader(this,   // Parent activity context
                currentUri,         // Query the content URI for the current pet
                projection,             // Columns to include in the resulting Cursor
                null,                   // No selection clause
                null,                   // No selection arguments
                null);                  // Default sort order
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor == null || cursor.getCount() < 1) {
            return;
        }
        if (cursor.moveToFirst()) {

           int nameColumnIndex = cursor.getColumnIndex(mealEntry.COLUMN_MEAL_NAME);
           Meal_name.setText(cursor.getString(nameColumnIndex));

           int ingredientsColumnIndex = cursor.getColumnIndex(mealEntry.COLUMN_MEAL_INGREDIENTS);
           Meal_ingredients.setText(cursor.getString(ingredientsColumnIndex));

            int imageColumnIndex = cursor.getColumnIndex(mealEntry.COLUMN_MEAL_IMAGE);
            Picasso.with(this)
                    .load(cursor.getString(imageColumnIndex))
                    .into(image1);
            Meal_ingredients.setText(cursor.getString(ingredientsColumnIndex));

           int urlColumnIndex = cursor.getColumnIndex(mealEntry.COLUMN_MEAL_URL);
           final String url = cursor.getString(urlColumnIndex);
            directions.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    // Create a new intent to view the meal URI
                    Intent websiteIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));

                    // Send the intent to launch a new activity
                    startActivity(websiteIntent);
                }
            });

        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        Meal_name.setText("");
        Meal_ingredients.setText("");
    }

}
