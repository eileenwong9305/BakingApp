package com.example.android.baking;

import android.appwidget.AppWidgetManager;
import android.arch.lifecycle.Observer;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.util.Log;

import com.example.android.baking.data.Recipe;

import javax.inject.Inject;

import dagger.android.DaggerIntentService;

public class ShowIngredientService extends DaggerIntentService {

    public static final String ACTION_SHOW_INGREDIENTS = "com.example.android.baking.action.show.ingredient";

    @Inject
    AppRepository appRepository;

    public ShowIngredientService() {
        super("ShowIngredientService");
    }

    public static void startActionShowIngredient(Context context) {
        Log.e("ShowIngredientService", "startActionShowIngredient");
        Intent intent = new Intent(context, ShowIngredientService.class);
        intent.setAction(ACTION_SHOW_INGREDIENTS);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            if (ACTION_SHOW_INGREDIENTS.equals(intent.getAction())) {
                Log.e(getClass().getSimpleName(), "ACTION_SHOW_INGREDIENTS");
                handleActionShowIngredient();
            }
        }
    }

    private void handleActionShowIngredient() {
        SharedPreferences sharedPreferences = getSharedPreferences(
                getString(R.string.preference_key_file),
                Context.MODE_PRIVATE);
        int savedRecipeId = sharedPreferences.getInt(getString(R.string.preference_key_save_recipe), -1);
        Recipe recipe = appRepository.getRecipe(savedRecipeId);
        Log.e(getClass().getSimpleName(), String.valueOf(recipe));
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(ShowIngredientService.this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(ShowIngredientService.this, AppWidget.class));
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.ingredient_lv);
        AppWidget.updateRecipeWidgets(ShowIngredientService.this, appWidgetManager, appWidgetIds, recipe);
    }
}
