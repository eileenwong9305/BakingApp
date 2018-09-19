package com.example.android.baking.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.RemoteViews;

import com.example.android.baking.R;
import com.example.android.baking.data.Recipe;
import com.example.android.baking.ui.detail.DetailActivity;
import com.example.android.baking.ui.main.MainActivity;

/**
 * Implementation of App Widget functionality.
 */
public class AppWidget extends AppWidgetProvider {

    public static final String EXTRA_INGREDIENTS = "com.example.android.baking.extra.mIngredients";

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId, int recipeId, String recipeName) {

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.app_widget);
        if (recipeId == -1) {
            views.setViewVisibility(R.id.empty_tv, View.VISIBLE);
            views.setViewVisibility(R.id.show_ingredient, View.GONE);
            Intent intent = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            views.setOnClickPendingIntent(R.id.empty_tv, pendingIntent);
        } else {
            views.setViewVisibility(R.id.empty_tv, View.GONE);
            views.setViewVisibility(R.id.show_ingredient, View.VISIBLE);
            views.setTextViewText(R.id.recipe_name_tv, recipeName);

            Intent intent = new Intent(context, ListWidgetService.class);
            views.setRemoteAdapter(R.id.ingredient_lv, intent);
            views.setEmptyView(R.id.ingredient_lv, R.id.empty_tv);

            Intent appIntent = new Intent(context, DetailActivity.class);
            PendingIntent appPendingIntent = PendingIntent.getActivity(context, 0, appIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            views.setPendingIntentTemplate(R.id.ingredient_lv, appPendingIntent);
        }

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    public static void updateRecipeWidgets(Context context, AppWidgetManager appWidgetManager,
                                           int[] appWidgetIds, int recipeId, String recipeName) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId, recipeId, recipeName);
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                context.getString(R.string.preference_key_file),
                Context.MODE_PRIVATE);
        int savedRecipeId = sharedPreferences.getInt(context.getString(R.string.preference_key_save_recipe), -1);
        String savedRecipeName = sharedPreferences.getString(context.getString(R.string.preference_key_save_recipe_name), "");
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.ingredient_lv);
        AppWidget.updateRecipeWidgets(context, appWidgetManager, appWidgetIds, savedRecipeId, savedRecipeName);
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

