package com.toppostsfromredditcom;

import com.toppostsfromredditcom.model.children.Data;

public class Model extends Data {

    private final String save = "save";

    public Model(String title, String url, long numComments, String author, long created, String thumbnail) {
        super(title, url, numComments, author, created, thumbnail);
    }

    public String getSave() {
        return save;
    }
}
