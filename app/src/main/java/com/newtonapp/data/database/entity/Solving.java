package com.newtonapp.data.database.entity;

import com.google.gson.annotations.SerializedName;

public class Solving {

    @SerializedName("option")
    private String solvingOption;

    @SerializedName("reason")
    private String solvingNote;

    public String getSolvingOption() {
        return solvingOption;
    }

    public void setSolvingOption(String solvingOption) {
        this.solvingOption = solvingOption;
    }

    public String getSolvingNote() {
        return solvingNote;
    }

    public void setSolvingNote(String solvingNote) {
        this.solvingNote = solvingNote;
    }
}
