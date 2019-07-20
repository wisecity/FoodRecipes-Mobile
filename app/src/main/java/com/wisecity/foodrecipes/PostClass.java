package com.wisecity.foodrecipes;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class PostClass extends ArrayAdapter<String> {

    private final ArrayList<String> recipeImageTag;
    private final ArrayList<Bitmap> recipeImage;
    private final Activity context;

    public PostClass(ArrayList<String> recipeImageTag, ArrayList<Bitmap> recipeImage, Activity context) {
        super(context, R.layout.custom_view, recipeImageTag);
        this.recipeImageTag = recipeImageTag;
        this.recipeImage = recipeImage;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater layoutInflater = context.getLayoutInflater();
        View customView = layoutInflater.inflate(R.layout.custom_view, null, true);
        TextView recipeImageTagText = customView.findViewById(R.id.custom_view_recipe_image_tag);
        ImageView imageView = customView.findViewById(R.id.custom_view_iVRecipeImage);

        recipeImageTagText.setText(recipeImageTag.get(position));
        imageView.setImageBitmap(recipeImage.get(position));
        return customView;
    }
}
