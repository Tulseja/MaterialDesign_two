package com.wackydeveloper.designersaree.model;

import java.io.Serializable;

/**
 * Created by hp on 7/1/17.
 */

public class Image implements Serializable {
    private String name;
    private String small, medium, large;

    public Image() {
    }
    public Image(String name, String small, String medium, String large, String timestamp) {
        this.name = name;
        this.small = small;
        this.medium = medium;
        this.large = large;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getSmall() {
        return small;
    }

    public void setSmall(String small) {
        this.small = small;
    }

    public String getMedium() {
        return medium;
    }

    public void setMedium(String medium) {
        this.medium = medium;
    }

    public String getLarge() {
        return large;
    }

    public void setLarge(String large) {
        this.large = large;
    }



}
