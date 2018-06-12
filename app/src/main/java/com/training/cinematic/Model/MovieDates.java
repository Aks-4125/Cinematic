package com.training.cinematic.Model;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

/**
 * Created by dhruvisha on 6/11/2018.
 */

public class MovieDates extends RealmObject{
    @SerializedName("maximum")

    private String maximum;
    @SerializedName("minimum")

    private String minimum;

    public String getMaximum() {
        return maximum;
    }

    public void setMaximum(String maximum) {
        this.maximum = maximum;
    }

    public String getMinimum() {
        return minimum;
    }

    public void setMinimum(String minimum) {
        this.minimum = minimum;
    }

}
