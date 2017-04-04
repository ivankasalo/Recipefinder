package com.example.ivan.recipefinder;

/**
 * Created by Ivan on 21/02/2017.
 */

public class Food {
    private String name;
    private String URL;
    private String ingredients;
    private String picture;
    public Food(String name, String ingredients, String URL){
        this.name = name;
        this.ingredients = ingredients;
        this.URL = URL;
    }
    public Food(String name, String ingredients, String picture, String URL){
        this.name = name;
        this.ingredients = ingredients;
        this.picture = picture;
        this.URL = URL;
    }
    public String getName(){
        return name;
    }
    public String getIngredients(){
        return ingredients;
    }
    public String getPicture(){
        return picture;
    }
    public String getURL(){
        return URL;
    }
}
