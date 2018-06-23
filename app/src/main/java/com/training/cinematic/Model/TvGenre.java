package com.training.cinematic.Model;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by dhruvisha on 6/21/2018.
 */

public class TvGenre extends RealmObject {
    @PrimaryKey
    @SerializedName("id")

    private Integer id;
    @SerializedName("name")

    private String name;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
