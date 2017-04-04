package com.example.ivan.recipefinder.database;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.example.ivan.recipefinder.MealDescription;
import com.example.ivan.recipefinder.database.FoodContract.mealEntry;
/**
 * Created by Ivan on 23/02/2017.
 */

public class CustomProvider extends ContentProvider {

    private FoodDbHelper mDbHelper;
    private static final int MEAL = 100;
    private static final int MEAL_ID = 101;
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(FoodContract.AUTHORITY, FoodContract.mealEntry.TABLE_NAME, MEAL);
        sUriMatcher.addURI(FoodContract.AUTHORITY, FoodContract.mealEntry.TABLE_NAME + "/#", MEAL_ID);
    }


    @Override
    public boolean onCreate() {
        mDbHelper = new FoodDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase database = mDbHelper.getReadableDatabase();
        int matcher = sUriMatcher.match(uri);

        Cursor cursor;
        if (matcher == MEAL) {
            cursor = database.query(FoodContract.mealEntry.TABLE_NAME,
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    sortOrder);
        } else if (matcher == MEAL_ID) {
            selection = mealEntry._ID + "=?";
            selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
            cursor = database.query(FoodContract.mealEntry.TABLE_NAME,
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    sortOrder);
        } else {
            throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        int matcher = sUriMatcher.match(uri);
        if (matcher == MEAL) {
            return insertMeal(uri, values);
        } else throw new IllegalArgumentException("Cannot query unknown URI" + uri);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int matcher = sUriMatcher.match(uri);
        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        int rowsDeleted;
                switch (matcher){
                    case MEAL:
                         rowsDeleted = database.delete(mealEntry.TABLE_NAME,null,null);
                        break;

                    case MEAL_ID:
                        selection = mealEntry._ID + "=?";
                        selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                        rowsDeleted = database.delete(mealEntry.TABLE_NAME, selection, selectionArgs);
                        break;
                    default:
                        throw new IllegalArgumentException("Cannot query unknown URI " + uri);
                }
        if(rowsDeleted != 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }
    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }


    private Uri insertMeal(Uri uri, ContentValues values) {
        SQLiteDatabase database = mDbHelper.getReadableDatabase();
        Cursor cursor = database.query(FoodContract.mealEntry.TABLE_NAME, null, null, null, null, null, null);
        //code below adds data validation for every meal, checking if there is already meal in our database

        int i = 0;
        long db = -1;
        int colum_url = cursor.getColumnIndex(FoodContract.mealEntry.COLUMN_MEAL_URL);
        while (cursor.moveToNext()) {
            if (values.get(FoodContract.mealEntry.COLUMN_MEAL_URL).equals(cursor.getString(colum_url))) {
                i = 1;
                //if i = 1 means ...meal is already in database
            }
        }
        if (i == 0) {
            database = mDbHelper.getWritableDatabase();
            db = database.insert(FoodContract.mealEntry.TABLE_NAME, null, values);
            if (db == -1) {
                Toast.makeText(getContext(), "An error occurred while inserting!", Toast.LENGTH_LONG).show();
                return null;
            } else {
                Toast.makeText(getContext(), "You have inserted a new meal to your recipes, and the number of meals is " +
                        (cursor.getCount() + 1), Toast.LENGTH_LONG).show();

            }
        } else {
            Toast.makeText(getContext(), "Sorry, you have already inserted this recipe to your recipes!",
                    Toast.LENGTH_LONG).show();
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(uri,db);
    }
}
