package com.example.android.baking.ui.detail;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.example.android.baking.AppExecutors;
import com.example.android.baking.AppWidget;
import com.example.android.baking.BakingApplication;
import com.example.android.baking.R;
import com.example.android.baking.ShowIngredientService;
import com.example.android.baking.adapter.IngredientAdapter;
import com.example.android.baking.adapter.StepAdapter;
import com.example.android.baking.data.Recipe;
import com.example.android.baking.data.Step;
import com.example.android.baking.ui.main.MainActivity;

import java.util.ArrayList;
import java.util.zip.Inflater;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.AndroidInjection;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.DaggerAppCompatActivity;
import dagger.android.support.HasSupportFragmentInjector;

public class DetailActivity extends AppCompatActivity implements DetailListFragment.OnStepClickListener{

    public static final String KEY_STEPS = "steps";
    public static final String KEY_STEP_NUMBER = "step_number";

    private Recipe mRecipe;
    private SharedPreferences sharedPreferences;
    private boolean isSaved = false;
    private boolean twoPane;

    @Nullable
    @BindView(R.id.step_container)
    FrameLayout stepContainerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra(MainActivity.KEY_RECIPE)) {
                mRecipe = intent.getParcelableExtra(MainActivity.KEY_RECIPE);
            }
        }

        sharedPreferences = getSharedPreferences(
                getString(R.string.preference_key_file),
                Context.MODE_PRIVATE);
        int savedRecipeId = sharedPreferences.getInt(getString(R.string.preference_key_save_recipe), -1);
        isSaved = savedRecipeId == mRecipe.getId();

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(mRecipe.getName());
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        if (stepContainerLayout != null) {
            twoPane = true;
            if (savedInstanceState == null) {
                Bundle bundle = new Bundle();
                bundle.putString(StepCollectionPagerAdapter.BUNDLE_KEY_DESC, mRecipe.getSteps().get(0).getDescription());
                bundle.putString(StepCollectionPagerAdapter.BUNDLE_KEY_VIDEO_URL, mRecipe.getSteps().get(0).getVideoURL());
                StepDetailFragment stepDetailFragment = new StepDetailFragment();
                stepDetailFragment.setArguments(bundle);
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().add(R.id.step_container, stepDetailFragment).commit();

                DetailListFragment detailListFragment = DetailListFragment.newInstance(mRecipe);
                fragmentManager.beginTransaction().add(R.id.detail_container, detailListFragment).commit();
            }
        } else {
            twoPane = false;
            if (savedInstanceState == null) {
                DetailListFragment detailListFragment = DetailListFragment.newInstance(mRecipe);
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().add(R.id.detail_container, detailListFragment).commit();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        invalidateOptionsMenu();
        if (isSaved){
            menu.findItem(R.id.action_save).setIcon(getResources().getDrawable(R.drawable.ic_favorite));
        } else {
            menu.findItem(R.id.action_save).setIcon(getResources().getDrawable(R.drawable.ic_favorite_border));
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch(itemId) {
            case R.id.action_save:
                if (isSaved) {
                    sharedPreferences.edit().putInt(getString(R.string.preference_key_save_recipe), -1).apply();
                    isSaved = false;
                    item.setIcon(R.drawable.ic_favorite_border);
                } else {
                    sharedPreferences.edit().putInt(getString(R.string.preference_key_save_recipe), mRecipe.getId()).apply();
                    isSaved = true;
                    item.setIcon(R.drawable.ic_favorite);
                }
                Intent intent = new Intent(this, AppWidget.class);
                intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
                int[] appWidgetIds = AppWidgetManager.getInstance(getApplication())
                        .getAppWidgetIds(new ComponentName(getApplication(), AppWidget.class));
                intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
                sendBroadcast(intent);
//                ShowIngredientService.startActionShowIngredient(getApplicationContext());
                return true;
            case android.R.id.home:
                Intent upIntent = NavUtils.getParentActivityIntent(this);
                if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
                    TaskStackBuilder.create(this).addNextIntentWithParentStack(upIntent)
                            .startActivities();
                } else {
                    NavUtils.navigateUpTo(this, upIntent);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onStepSelected(int position) {
        if (twoPane) {
            Step step = mRecipe.getSteps().get(position);
            Bundle bundle = new Bundle();
            bundle.putString(StepCollectionPagerAdapter.BUNDLE_KEY_DESC, step.getDescription());
            bundle.putString(StepCollectionPagerAdapter.BUNDLE_KEY_VIDEO_URL, step.getVideoURL());
            StepDetailFragment stepDetailFragment = new StepDetailFragment();
            stepDetailFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.step_container, stepDetailFragment).commit();
        } else {
            Intent intent = new Intent(this, StepActivity.class);
            intent.putParcelableArrayListExtra(KEY_STEPS, (ArrayList) mRecipe.getSteps());
            intent.putExtra(KEY_STEP_NUMBER, position);
            startActivity(intent);
        }
    }
}
