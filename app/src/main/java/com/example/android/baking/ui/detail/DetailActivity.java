package com.example.android.baking.ui.detail;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.example.android.baking.widget.AppWidget;
import com.example.android.baking.R;
import com.example.android.baking.data.Recipe;
import com.example.android.baking.data.Step;
import com.example.android.baking.ui.main.MainActivity;
import com.example.android.baking.ui.step.StepActivity;
import com.example.android.baking.ui.step.StepCollectionPagerAdapter;
import com.example.android.baking.ui.step.StepDetailFragment;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity implements DetailListFragment.OnStepClickListener{

    public static final String KEY_STEPS = "steps";
    public static final String KEY_STEP_NUMBER = "step_number";

    private int totalSteps;
    private Recipe mRecipe;
    private SharedPreferences sharedPreferences;
    private boolean isSaved = false;
    private boolean twoPane;
    private int currentPosition;

    @Nullable
    @BindView(R.id.step_container)
    public FrameLayout stepContainerLayout;
    @Nullable
    @BindView(R.id.left_button)
    public ImageView leftButton;
    @Nullable
    @BindView(R.id.right_button)
    public ImageView rightButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra(MainActivity.KEY_RECIPE)) {
                mRecipe = intent.getParcelableExtra(MainActivity.KEY_RECIPE);
                totalSteps = mRecipe.getSteps().size();
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
            currentPosition = 0;
            setupButton(currentPosition);
            if (leftButton != null) {
                leftButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        currentPosition -= 1;
                        setupButtonAndFragment(currentPosition);
                    }
                });
            }
            if (rightButton != null) {
                rightButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        currentPosition += 1;
                        setupButtonAndFragment(currentPosition);
                    }
                });
            }

            if (savedInstanceState == null) {
                Bundle bundle = new Bundle();
                bundle.putString(StepCollectionPagerAdapter.BUNDLE_KEY_DESC, mRecipe.getSteps().get(currentPosition).getDescription());
                bundle.putString(StepCollectionPagerAdapter.BUNDLE_KEY_VIDEO_URL, mRecipe.getSteps().get(currentPosition).getVideoURL());
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
                    Snackbar.make(findViewById(R.id.detail_container), getString(R.string.remove_recipe), Snackbar.LENGTH_SHORT).show();
                } else {
                    sharedPreferences.edit().putInt(getString(R.string.preference_key_save_recipe), mRecipe.getId()).apply();
                    isSaved = true;
                    item.setIcon(R.drawable.ic_favorite);
                    Snackbar.make(findViewById(R.id.detail_container), getString(R.string.add_recipe), Snackbar.LENGTH_SHORT).show();
                }
                Intent intent = new Intent(this, AppWidget.class);
                intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
                int[] appWidgetIds = AppWidgetManager.getInstance(getApplication())
                        .getAppWidgetIds(new ComponentName(getApplication(), AppWidget.class));
                intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
                sendBroadcast(intent);
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
            currentPosition = position;
            setupButtonAndFragment(currentPosition);
        } else {
            Intent intent = new Intent(this, StepActivity.class);
            intent.putParcelableArrayListExtra(KEY_STEPS, (ArrayList) mRecipe.getSteps());
            intent.putExtra(KEY_STEP_NUMBER, position);
            startActivity(intent);
        }
    }

    private void changeFragment(int position) {
        currentPosition = position;
        Step step = mRecipe.getSteps().get(position);
        Bundle bundle = new Bundle();
        bundle.putString(StepCollectionPagerAdapter.BUNDLE_KEY_DESC, step.getDescription());
        bundle.putString(StepCollectionPagerAdapter.BUNDLE_KEY_VIDEO_URL, step.getVideoURL());
        StepDetailFragment stepDetailFragment = new StepDetailFragment();
        stepDetailFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.step_container, stepDetailFragment).commit();

    }

    private void setupButton(int position) {
        if (leftButton != null && rightButton != null) {
            if (position == 0) {
                leftButton.setVisibility(View.INVISIBLE);
                rightButton.setVisibility(View.VISIBLE);
            } else if (position == totalSteps - 1) {
                leftButton.setVisibility(View.VISIBLE);
                rightButton.setVisibility(View.INVISIBLE);
            } else {
                rightButton.setVisibility(View.VISIBLE);
                leftButton.setVisibility(View.VISIBLE);
            }
        }
    }

    private void setupButtonAndFragment(int position) {
        setupButton(position);
        changeFragment(position);
    }
}
