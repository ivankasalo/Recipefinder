package com.example.ivan.recipefinder;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ivan.recipefinder.database.FoodContract.mealEntry;
import com.squareup.picasso.Picasso;

/**
 * Created by Ivan on 24/02/2017.
 */

public class MealCursorAdapter extends CursorAdapter {
    public MealCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        if(context instanceof MyRecipes)
            return LayoutInflater.from(context).inflate(R.layout.food_item, parent,false);
        else
            return LayoutInflater.from(context).inflate(R.layout.grid_item,parent, false);

    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ImageView image;
        if(context instanceof MyRecipes){
        image = (ImageView)view.findViewById(R.id.meal_image);
        TextView name = (TextView)view.findViewById(R.id.name);
        TextView ingredients = (TextView)view.findViewById(R.id.ingredients);
        name.setText(cursor.getString(cursor.getColumnIndexOrThrow(mealEntry.COLUMN_MEAL_NAME)));
        ingredients.setText(cursor.getString(cursor.getColumnIndexOrThrow(mealEntry.COLUMN_MEAL_INGREDIENTS)));
        String photo = cursor.getString(cursor.getColumnIndexOrThrow(mealEntry.COLUMN_MEAL_IMAGE));
        if(photo.equals(""))
            image.setImageResource(R.drawable.defaul_meal);
        else
        Picasso.with(context)
                .load(photo)
                .into(image);
    }else{
            image = (ImageView)view.findViewById(R.id.pic);
            String photo = cursor.getString(cursor.getColumnIndexOrThrow(mealEntry.COLUMN_MEAL_IMAGE));
            if(photo.equals(""))
                image.setImageResource(R.drawable.defaul_meal);
            else
                Picasso.with(context)
                        .load(photo)
                        .into(image);
        }

    }
}
