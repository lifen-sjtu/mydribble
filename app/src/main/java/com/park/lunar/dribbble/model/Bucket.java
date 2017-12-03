package com.park.lunar.dribbble.model;

import java.util.Date;

/**
 * Created by jlwang on 10/16/17.
 */

public class Bucket {
    public String id;
    public String name;
    public String description;
    public int shots_count;
    public Date created_at;

    public boolean isChoosing;

    @Override
    public boolean equals(Object bucket) {
        if (bucket instanceof Bucket) {
            return id.equals(((Bucket) bucket).id);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return id + " " + name + "\n";
    }
}
