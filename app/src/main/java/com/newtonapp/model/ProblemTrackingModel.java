package com.newtonapp.model;

public class ProblemTrackingModel {

    private int id;
    private int iconActive;
    private int iconInactive;
    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIconActive() {
        return iconActive;
    }

    public void setIconActive(int iconActive) {
        this.iconActive = iconActive;
    }

    public int getIconInactive() {
        return iconInactive;
    }

    public void setIconInactive(int iconInactive) {
        this.iconInactive = iconInactive;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
