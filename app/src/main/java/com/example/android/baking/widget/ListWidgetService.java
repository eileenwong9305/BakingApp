package com.example.android.baking.widget;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.android.baking.R;
import com.example.android.baking.data.Ingredient;
import com.example.android.baking.data.Recipe;
import com.example.android.baking.ui.main.MainActivity;
import com.example.android.baking.util.AppRepository;

import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

public class ListWidgetService extends RemoteViewsService{

    @Inject
    AppRepository appRepository;

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        AndroidInjection.inject(this);
        int recipeId = intent.getIntExtra(AppWidget.EXTRA_INGREDIENTS, -1);
        return new ListRemoteViewFactory(this.getApplicationContext(), appRepository, recipeId);
    }
}

class ListRemoteViewFactory implements RemoteViewsService.RemoteViewsFactory {

    Context context;
    List<Ingredient> ingredients;
    AppRepository repository;
    Recipe recipe;
    int recipeId;

    public ListRemoteViewFactory(Context applicationContext, AppRepository appRepository, int recipeId) {
        context = applicationContext;
        repository = appRepository;
        this.recipeId = recipeId;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                context.getString(R.string.preference_key_file),
                Context.MODE_PRIVATE);
        int savedRecipeId = sharedPreferences.getInt(context.getString(R.string.preference_key_save_recipe), -1);
        recipe = repository.getRecipe(savedRecipeId);
        if (recipe == null) {
            ingredients = null;
        } else {
            ingredients = recipe.getIngredients();
        }
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        if (ingredients == null) return 0;
        return ingredients.size();
    }

    @Override
    public RemoteViews getViewAt(int i) {
        if (recipe == null || ingredients == null || ingredients.size() == 0) return null;
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.list_widget);
        Ingredient ingredient = ingredients.get(i);
        Log.e(getClass().getSimpleName(), ingredient.getIngredient());
        views.setTextViewText(R.id.detail_ingredient_tv, ingredient.getIngredient());
        views.setTextViewText(R.id.detail_quantity_tv, context.getString(R.string.quantity_text,
                decimalFormat(ingredient.getQuantity()),
                ingredient.getMeasure()));
        Bundle extras = new Bundle();
        extras.putParcelable(MainActivity.KEY_RECIPE, recipe);
        Intent fillInIntent = new Intent();
        fillInIntent.putExtras(extras);
        views.setOnClickFillInIntent(R.id.detail_ingredient_tv, fillInIntent);
        return views;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    private String decimalFormat(float f) {
        if (f == (long) f) {
            return String.format(Locale.US, "%d", (long) f);
        } else {
            return String.format("%s", f);
        }
    }


}
