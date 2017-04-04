package com.example.ivan.recipefinder;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.ivan.recipefinder.database.FoodContract.mealEntry;


public class MyRecipes extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    private static final int LOADER_MEAL = 0;
    MealCursorAdapter mCursorAdapter;
    ListView list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_recipes);
        //setTitle("");
        getLoaderManager().initLoader(LOADER_MEAL,null,this);

        list = (ListView)findViewById(R.id.recipesListViewId);
        mCursorAdapter = new MealCursorAdapter(this,null);
        list.setAdapter(mCursorAdapter);
        registerForContextMenu(list);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                view.setLongClickable(false);
                openContextMenu(view);
            }
        });
        list.setLongClickable(isRestricted());
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
        // Hide loading indicator because the data has been loaded
        View loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);
        mCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCursorAdapter.swapCursor(null);
        // new comments

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.list_item_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info =
                (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        Cursor cursor = mCursorAdapter.getCursor();
        int itemPosition = info.position;
        cursor.moveToPosition(itemPosition);
        Uri currentUri = ContentUris.withAppendedId(mealEntry.TABLE_MEALS_URI,cursor.getInt(cursor.getColumnIndex(mealEntry.COLUMN_ID)));
        switch (item.getItemId()){
            case R.id.recipe:

                Intent intent = new Intent(MyRecipes.this,MealDescription.class);
                intent.setData(currentUri);
                startActivity(intent);

                return true;
            case R.id.delete:

                getContentResolver().delete(currentUri, null, null);
                return true;
            default:
                return super.onContextItemSelected(item);
        }

    }
}

