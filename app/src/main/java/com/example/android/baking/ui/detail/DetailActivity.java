package com.example.android.baking.ui.detail;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.example.android.baking.R;
import com.example.android.baking.data.Recipe;
import com.example.android.baking.data.Step;
import com.example.android.baking.ui.main.MainActivity;
import com.example.android.baking.ui.step.StepActivity;
import com.example.android.baking.ui.step.StepCollectionPagerAdapter;
import com.example.android.baking.ui.step.StepDetailFragment;
import com.example.android.baking.widget.AppWidget;

import org.parceler.Parcels;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity implements DetailListFragment.OnStepClickListener {

    public static final String KEY_STEPS = "steps";
    public static final String KEY_STEP_NUMBER = "step_number";
    @Nullable
    @BindView(R.id.step_container)
    public FrameLayout mStepContainerLayout;
    @Nullable
    @BindView(R.id.left_button)
    public ImageView mLeftButton;
    @Nullable
    @BindView(R.id.right_button)
    public ImageView mRightButton;
    private int mTotalSteps;
    private Recipe mRecipe;
    private SharedPreferences mSharedPreferences;
    private boolean mIsSaved = false;
    private boolean mTwoPane;
    private int mCurrentPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra(MainActivity.KEY_RECIPE)) {
                mRecipe = Parcels.unwrap(intent.getParcelableExtra(MainActivity.KEY_RECIPE));
                mTotalSteps = mRecipe.getSteps().size();
            }
        }

        mSharedPreferences = getSharedPreferences(
                getString(R.string.preference_key_file),
                Context.MODE_PRIVATE);
        int savedRecipeId = mSharedPreferences.getInt(getString(R.string.preference_key_save_recipe), -1);
        mIsSaved = savedRecipeId == mRecipe.getId();

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(mRecipe.getName());
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        if (mStepContainerLayout != null) {
            mTwoPane = true;
            mCurrentPosition = 0;
            setupButton(mCurrentPosition);
            if (mLeftButton != null) {
                mLeftButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mCurrentPosition -= 1;
                        setupButtonAndFragment(mCurrentPosition);
                    }
                });
            }
            if (mRightButton != null) {
                mRightButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mCurrentPosition += 1;
                        setupButtonAndFragment(mCurrentPosition);
                    }
                });
            }

            if (savedInstanceState == null) {
                Bundle bundle = new Bundle();
                bundle.putString(StepCollectionPagerAdapter.BUNDLE_KEY_DESC, mRecipe.getSteps().get(mCurrentPosition).getDescription());
                bundle.putString(StepCollectionPagerAdapter.BUNDLE_KEY_VIDEO_URL, mRecipe.getSteps().get(mCurrentPosition).getVideoURL());
                StepDetailFragment stepDetailFragment = new StepDetailFragment();
                stepDetailFragment.setArguments(bundle);
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().add(R.id.step_container, stepDetailFragment).commit();

                DetailListFragment detailListFragment = DetailListFragment.newInstance(mRecipe);
                fragmentManager.beginTransaction().add(R.id.detail_container, detailListFragment).commit();
            }
        } else {
            mTwoPane = false;
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
        if (mIsSaved) {
            menu.findItem(R.id.action_save).setIcon(getResources().getDrawable(R.drawable.ic_favorite));
        } else {
            menu.findItem(R.id.action_save).setIcon(getResources().getDrawable(R.drawable.ic_favorite_border));
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case R.id.action_save:
                if (mIsSaved) {
                    mSharedPreferences.edit().putInt(getString(R.string.preference_key_save_recipe), -1).apply();
                    mSharedPreferences.edit().putString(getString(R.string.preference_key_save_recipe_name), "").apply();
                    mIsSaved = false;
                    item.setIcon(R.drawable.ic_favorite_border);
                    Snackbar.make(findViewById(R.id.detail_container), getString(R.string.remove_recipe), Snackbar.LENGTH_SHORT).show();
                } else {
                    mSharedPreferences.edit().putInt(getString(R.string.preference_key_save_recipe), mRecipe.getId()).apply();
                    mSharedPreferences.edit().putString(getString(R.string.preference_key_save_recipe_name), mRecipe.getName()).apply();
                    mIsSaved = true;
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
        if (mTwoPane) {
            mCurrentPosition = position;
            setupButtonAndFragment(mCurrentPosition);
        } else {
            Intent intent = new Intent(this, StepActivity.class);
//            intent.putParcelableArrayListExtra(KEY_STEPS, (ArrayList) mRecipe.getSteps());
            intent.putExtra(KEY_STEPS, Parcels.wrap((ArrayList) mRecipe.getSteps()));
            intent.putExtra(KEY_STEP_NUMBER, position);
            startActivity(intent);
        }
    }

    private void changeFragment(int position) {
        mCurrentPosition = position;
        Step step = mRecipe.getSteps().get(position);
        Bundle bundle = new Bundle();
        bundle.putString(StepCollectionPagerAdapter.BUNDLE_KEY_DESC, step.getDescription());
        bundle.putString(StepCollectionPagerAdapter.BUNDLE_KEY_VIDEO_URL, step.getVideoURL());
        StepDetailFragment stepDetailFragment = new StepDetailFragment();
        stepDetailFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.step_container, stepDetailFragment).commit();

    }

    private void setupButton(int position) {
        if (mLeftButton != null && mRightButton != null) {
            if (position == 0) {
                mLeftButton.setVisibility(View.INVISIBLE);
                mRightButton.setVisibility(View.VISIBLE);
            } else if (position == mTotalSteps - 1) {
                mLeftButton.setVisibility(View.VISIBLE);
                mRightButton.setVisibility(View.INVISIBLE);
            } else {
                mRightButton.setVisibility(View.VISIBLE);
                mLeftButton.setVisibility(View.VISIBLE);
            }
        }
    }

    private void setupButtonAndFragment(int position) {
        setupButton(position);
        changeFragment(position);
    }
}
