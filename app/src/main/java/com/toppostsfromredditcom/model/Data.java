package com.toppostsfromredditcom.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.toppostsfromredditcom.model.children.Children;

import java.util.ArrayList;

public class Data {
    @SerializedName("children")
    @Expose
    ArrayList<Children> children;

    public ArrayList<Children> getChildren() {
        return children;
    }

    public void setChildren(ArrayList<Children> children) {
        this.children = children;
    }
}
