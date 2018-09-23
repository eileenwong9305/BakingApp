package com.example.android.baking.ui.detail;

import android.appwidget.AppWidgetManager;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.ComponentName;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.android.baking.R;
import com.example.android.baking.SnackbarMessage;
import com.example.android.baking.data.Recipe;
import com.example.android.baking.databinding.ActivityDetailBinding;
import com.example.android.baking.ui.main.MainActivity;
import com.example.android.baking.ui.step.StepActivity;
import com.example.android.baking.ui.step.StepCollectionPagerAdapter;
import com.example.android.baking.ui.step.StepDetailFragment;
import com.example.android.baking.util.SnackbarUtils;
import com.example.android.baking.widget.AppWidget;

import org.parceler.Parcels;

public class DetailActivity extends AppCompatActivity implements DetailListFragment.OnStepClickListener {

    public static final String KEY_STEPS = "steps";
    public static final String KEY_STEP_NUMBER = "step_number";
    private Recipe mRecipe;
    private boolean mTwoPane;
    private DetailViewModel mViewModel;

    public static DetailViewModel obtainViewModel(FragmentActivity activity) {
        return ViewModelProviders.of(activity).get(DetailViewModel.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityDetailBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_detail);
        Intent intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra(MainActivity.KEY_RECIPE)) {
                mRecipe = Parcels.unwrap(intent.getParcelableExtra(MainActivity.KEY_RECIPE));
            }
        }

        mViewModel = obtainViewModel(this);
        mViewModel.start(mRecipe);
        binding.setViewmodel(mViewModel);
        setupSnackbar();
        setupActionBar();

        mTwoPane = (findViewById(R.id.step_container) != null);
        if (savedInstanceState == null) {
            setupFragment();
        }
        mViewModel.getChangeFragmentEvent().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer pos) {
                Log.e(getClass().getSimpleName(), String.valueOf(pos));
                if (pos != null) {
                    changeFragment(pos);
                }
            }
        });
    }

    private void setupSnackbar() {
        mViewModel.getSnackbarText().observe(this, new SnackbarMessage.SnackbarObserver() {
            @Override
            public void onNewMessage(int snackbarMessageResourceId) {
                SnackbarUtils.showSnackbar(findViewById(R.id.detail_container),
                        getString(snackbarMessageResourceId));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        invalidateOptionsMenu();
        setupFavouriteIcon(menu.findItem(R.id.action_save), mViewModel.getIsSaved());
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case R.id.action_save:
                mViewModel.saveOrUnsaveRecipe();
                setupFavouriteIcon(item, mViewModel.getIsSaved());
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
            mViewModel.setPosition(position);
            changeFragment(position);
        } else {
            Intent intent = new Intent(this, StepActivity.class);
            intent.putExtra(KEY_STEPS, Parcels.wrap(mRecipe.getSteps()));
            intent.putExtra(KEY_STEP_NUMBER, position);
            startActivity(intent);
        }
    }

    private void setupFragment() {
        if (mTwoPane) {
            Bundle bundle = new Bundle();
            bundle.putParcelable(StepCollectionPagerAdapter.BUNDLE_KEY_STEP, Parcels.wrap(mRecipe.getSteps().get(0)));
            StepDetailFragment stepDetailFragment = new StepDetailFragment();
            stepDetailFragment.setArguments(bundle);
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().add(R.id.step_container, stepDetailFragment).commit();

            DetailListFragment detailListFragment = DetailListFragment.newInstance(mRecipe);
            getSupportFragmentManager().beginTransaction().add(R.id.detail_container, detailListFragment).commit();
        } else {
            DetailListFragment detailListFragment = DetailListFragment.newInstance(mRecipe);
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().add(R.id.detail_container, detailListFragment).commit();
        }
    }

    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(mRecipe.getName());
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void changeFragment(int position) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(StepCollectionPagerAdapter.BUNDLE_KEY_STEP, Parcels.wrap(mRecipe.getSteps().get(position)));
        StepDetailFragment stepDetailFragment = new StepDetailFragment();
        stepDetailFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.step_container, stepDetailFragment).commit();

    }

    private void setupFavouriteIcon(MenuItem item, boolean isSaved) {
        if (isSaved) {
            item.setIcon(getResources().getDrawable(R.drawable.ic_favorite));
        } else {
            item.setIcon(getResources().getDrawable(R.drawable.ic_favorite_border));
        }
    }
}
