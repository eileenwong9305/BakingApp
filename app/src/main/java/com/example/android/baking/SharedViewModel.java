package com.example.android.baking;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.ClipData;

import com.example.android.baking.data.Recipe;

public class SharedViewModel extends ViewModel {

    private final MutableLiveData<String> videoUrl = new MutableLiveData<>();
    private final MutableLiveData<String> desc = new MutableLiveData<>();

    public void selectVideoUrl(String url) {
        videoUrl.setValue(url);
    }

    public LiveData<String> getSelectedVideoUrl() {
        return videoUrl;
    }

    public void selectDesc(String url) {
        desc.setValue(url);
    }

    public LiveData<String> getSelectedDesc() {
        return desc;
    }
}
