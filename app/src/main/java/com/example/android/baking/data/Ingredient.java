package com.example.android.baking.data;

import android.arch.persistence.room.Ignore;
import android.os.Parcel;
import android.os.Parcelable;

@org.parceler.Parcel
public class Ingredient{

    private float quantity;
    private String measure;
    private String ingredient;

    @Ignore
    public Ingredient() {}

    public Ingredient(float quantity, String measure, String ingredient) {
        this.quantity = quantity;
        this.measure = measure;
        this.ingredient = ingredient;
    }

    public float getQuantity() {
        return quantity;
    }

    public void setQuantity(float quantity) {
        this.quantity = quantity;
    }

    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

    public String getIngredient() {
        return ingredient;
    }

    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }
}

