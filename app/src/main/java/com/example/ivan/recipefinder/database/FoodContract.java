package com.example.ivan.recipefinder.database;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Ivan on 21/02/2017.
 */

public final class FoodContract {

    public static final String AUTHORITY = "com.example.ivan.recipefinder";
    public static final String PATH_MEAL ="meal";
    public static final String SCHEME = "content://";


    public static final Uri BASE_CONTENT_URI = Uri.parse(SCHEME + AUTHORITY);

    public static abstract class mealEntry implements BaseColumns{
        public static final String TABLE_NAME = "meal";
        public static final Uri TABLE_MEALS_URI = Uri.withAppendedPath(BASE_CONTENT_URI,PATH_MEAL);

        public static final String COLUMN_ID = BaseColumns._ID;
        public static final String COLUMN_MEAL_NAME = "meal";
        public static final String COLUMN_MEAL_INGREDIENTS = "ingredients";
        public static final String COLUMN_MEAL_IMAGE = "image";
        public static final String COLUMN_MEAL_URL = "URL";
    }
}
