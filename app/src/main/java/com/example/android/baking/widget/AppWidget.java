package com.example.android.baking.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
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

    public static final String EXTRA_INGREDIENTS = "com.example.android.baking.extra.ingredients";

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId, Recipe recipe) {

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.app_widget);
        if (recipe == null) {
            views.setViewVisibility(R.id.empty_tv, View.VISIBLE);
            views.setViewVisibility(R.id.show_ingredient, View.GONE);
            Intent intent = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            views.setOnClickPendingIntent(R.id.empty_tv, pendingIntent);
        } else {
            views.setViewVisibility(R.id.empty_tv, View.GONE);
            views.setViewVisibility(R.id.show_ingredient, View.VISIBLE);
            views.setTextViewText(R.id.recipe_name_tv, recipe.getName());

            Intent intent = new Intent(context, ListWidgetService.class);
            views.setRemoteAdapter(R.id.ingredient_lv, intent);
            views.setEmptyView(R.id.ingredient_lv, R.id.empty_tv);

            Intent appIntent = new Intent(context, DetailActivity.class);
            PendingIntent appPendingIntent = PendingIntent.getActivity(context, 0, appIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            views.setPendingIntentTemplate(R.id.ingredient_lv, appPendingIntent);
        }

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        ShowIngredientService.startActionShowIngredient(context);
    }

    public static void updateRecipeWidgets(Context context, AppWidgetManager appWidgetManager,
                                           int[] appWidgetIds, Recipe recipe) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId, recipe);
        }
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

