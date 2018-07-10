package com.training.cinematic.activity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dhruvisha on 6/27/2018.
 */

public class CategoryEnum {

    public static List<String> getCategory() {
        List<String> cat = new ArrayList<>();
        for (category catRole : category.values()) {
            cat.add(catRole.getName());
        }
        return cat;
    }

    public enum category {
        MOVIES("Movies", 0),
        TV("Tv", 1);
        private int typeInt;
        private String typeString;

        category(String typeString, int typeInt) {
            this.typeInt = typeInt;
            this.typeString = typeString;
        }

        public String getName() {
            return this.typeString;
        }

        public Integer getValue() {
            return this.typeInt;
        }
    }
}