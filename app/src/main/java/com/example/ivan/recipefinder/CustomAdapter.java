package com.example.ivan.recipefinder;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;

import java.util.List;



/**
 * Created by Ivan on 21/02/2017.
 */

public class CustomAdapter extends ArrayAdapter<Food> {
    public CustomAdapter(Context context, List<Food> food) {
        super(context,0, food);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.food_item, parent, false);
        }
        Food currentFood = getItem(position);
        TextView FoodName = (TextView)listItemView.findViewById(R.id.name);
        FoodName.setText(currentFood.getName());
        TextView ingredients = (TextView)listItemView.findViewById(R.id.ingredients);
        ingredients.setText(currentFood.getIngredients());

        ImageView picture = (ImageView)listItemView.findViewById(R.id.meal_image);
        //Picasso.with(getContext()).load(image).into(picture);
        String image = currentFood.getPicture();
        if(image.equals(""))
            //picture.setVisibility(View.GONE);
            picture.setImageResource(R.drawable.defaul_meal);
        else
            Picasso.with(getContext()).load(currentFood.getPicture()).into(picture);
        return listItemView;
    }
}
