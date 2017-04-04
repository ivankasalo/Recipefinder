package com.example.ivan.recipefinder.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.ivan.recipefinder.database.FoodContract.mealEntry;

/**
 * Created by Ivan on 22/02/2017.
 */

public class    FoodDbHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "meals.db";
    public static final int DB_VERSION = 2;

    public static final String CREATE_MEAL_TABLE = "CREATE TABLE " + mealEntry.TABLE_NAME + " ( "
            + mealEntry.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + mealEntry.COLUMN_MEAL_NAME + " Text NOT NULL, "
            + mealEntry.COLUMN_MEAL_INGREDIENTS + " Text, "
            + mealEntry.COLUMN_MEAL_IMAGE + " Text, "
            + mealEntry.COLUMN_MEAL_URL + " Text NOT NULL)";
    public static final String DELETE_MEAL_TABLE = "DROP TABLE IF EXIST " + mealEntry.TABLE_NAME;

    public FoodDbHelper(Context context){
        super(context,DB_NAME,null,DB_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_MEAL_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DELETE_MEAL_TABLE);
        onCreate(db);
    }
}
