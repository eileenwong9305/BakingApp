package com.example.android.baking.database;

import android.arch.persistence.room.TypeConverter;

import com.example.android.baking.data.Step;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

public class StepTypeConverters {

    @TypeConverter
    public static List<Step> stringToStepList(String data) {
        if (data == null) {
            return Collections.emptyList();
        }
        Gson gson = new Gson();
        Type listType = new TypeToken<List<Step>>() {
        }.getType();
        return gson.fromJson(data, listType);
    }

    @TypeConverter
    public static String stepListToString(List<Step> steps) {
        Gson gson = new Gson();
        return gson.toJson(steps);
    }
}
