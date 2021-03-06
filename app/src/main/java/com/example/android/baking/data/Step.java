package com.example.android.baking.data;

import android.arch.persistence.room.Ignore;
import android.os.Parcel;
import android.os.Parcelable;


import com.google.gson.annotations.SerializedName;

@org.parceler.Parcel
public class Step{

    @SerializedName("id")
    private int step;
    private String shortDescription;
    private String description;
    private String videoURL;
    private String thumbnailURL;

    @Ignore
    public Step() {}

    public Step(int step, String shortDescription, String description, String videoURL, String thumbnailURL) {
        this.step = step;
        this.shortDescription = shortDescription;
        this.description = description;
        this.videoURL = videoURL;
        this.thumbnailURL = thumbnailURL;
    }

    @Ignore
    public Step(String shortDescription, String description, String videoURL, String thumbnailURL) {
        this.shortDescription = shortDescription;
        this.description = description;
        this.videoURL = videoURL;
        this.thumbnailURL = thumbnailURL;
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVideoURL() {
        return videoURL;
    }

    public void setVideoURL(String videoURL) {
        this.videoURL = videoURL;
    }

    public String getThumbnailURL() {
        return thumbnailURL;
    }

    public void setThumbnailURL(String thumbnailURL) {
        this.thumbnailURL = thumbnailURL;
    }
}