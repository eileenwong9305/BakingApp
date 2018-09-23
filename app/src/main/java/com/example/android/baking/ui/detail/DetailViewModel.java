package com.example.android.baking.ui.detail;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.content.Context;
import android.content.SharedPreferences;
import android.databinding.ObservableInt;
import android.support.annotation.StringRes;

import com.example.android.baking.R;
import com.example.android.baking.SingleLiveEvent;
import com.example.android.baking.SnackbarMessage;
import com.example.android.baking.data.Recipe;

import javax.inject.Inject;

public class DetailViewModel extends AndroidViewModel {

    public final ObservableInt position = new ObservableInt();
    private final SingleLiveEvent<Integer> mChangeFragmentEvent = new SingleLiveEvent<>();
    private final SnackbarMessage mSnackbarText = new SnackbarMessage();
    public Recipe recipe;
    private Application mApplication;
    private boolean isSaved;

    @Inject
    public DetailViewModel(Application application) {
        super(application);
        mApplication = application;
        position.set(0);
    }

    public void start(Recipe recipe) {
        this.recipe = recipe;
        getRecipeIdInSharedPred(recipe.getId());
    }

    public SnackbarMessage getSnackbarText() {
        return mSnackbarText;
    }

    public boolean getIsSaved() {
        return isSaved;
    }

    public void saveOrUnsaveRecipe() {
        if (isSaved) {
            unsaveRecipe();
        } else {
            saveRecipe(recipe.getId(), recipe.getName());
        }
    }

    public SingleLiveEvent<Integer> getChangeFragmentEvent() {
        return mChangeFragmentEvent;
    }

    public void setPosition(int position) {
        this.position.set(position);
    }

    public void onLeftClick() {
        int newPosition = this.position.get() - 1;
        this.position.set(newPosition);
        getChangeFragmentEvent().setValue(position.get());
    }

    public void onRightClick() {
        int newPosition = this.position.get() + 1;
        this.position.set(newPosition);
        getChangeFragmentEvent().setValue(position.get());
    }

    private void unsaveRecipe() {
        saveRecipeIdInSharedPref(-1, "");
        isSaved = false;
        showSnackbarText(R.string.remove_recipe);
    }

    private void saveRecipe(int savedRecipeId, String savedRecipeName) {
        saveRecipeIdInSharedPref(savedRecipeId, savedRecipeName);
        isSaved = true;
        showSnackbarText(R.string.add_recipe);
    }

    private void getRecipeIdInSharedPred(int recipeId) {
        SharedPreferences sharedPreferences = mApplication.getSharedPreferences(
                mApplication.getString(R.string.preference_key_file),
                Context.MODE_PRIVATE);
        int savedRecipeId = sharedPreferences.getInt(mApplication.getString(R.string.preference_key_save_recipe), -1);
        isSaved = (savedRecipeId == recipeId);
    }

    private void saveRecipeIdInSharedPref(int savedRecipeId, String savedRecipeName) {
        SharedPreferences sharedPreferences = mApplication.getSharedPreferences(
                mApplication.getString(R.string.preference_key_file),
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(mApplication.getString(R.string.preference_key_save_recipe), savedRecipeId);
        editor.putString(mApplication.getString(R.string.preference_key_save_recipe_name), savedRecipeName);
        editor.apply();
    }

    private void showSnackbarText(@StringRes Integer message) {
        mSnackbarText.setValue(message);
    }

}
