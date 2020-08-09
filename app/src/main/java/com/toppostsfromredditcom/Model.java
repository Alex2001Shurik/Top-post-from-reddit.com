package com.toppostsfromredditcom;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Model {

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("url")
    @Expose
    private String url;

    @SerializedName("num_comments")
    @Expose
    private long numComments;

    @SerializedName("author")
    @Expose
    private String author;

    @SerializedName("created_utc")
    @Expose
    private String created;

    @SerializedName("thumbnail")
    @Expose
    private String thumbnail;


    public Model(String title, String url, long numComments, String author, String created, String thumbnail) {
        this.title = title;
        this.url = url;
        this.numComments = numComments;
        this.author = author;
        this.created = created;
        this.thumbnail = thumbnail;
    }

    public long getNumComments() {
        return numComments;
    }

    public String getCreated() {
        return created;
    }

    public String getAuthor() { return author; }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setNumComments(long numComments) {
        this.numComments = numComments;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    @Override
    public String toString() {
        return "Model{" +
                "title='" + title + '\'' +
                ", url='" + url + '\'' +
                ", numComments=" + numComments +
                ", author='" + author + '\'' +
                ", created " + created +
                ", thumbnail='" + thumbnail + '\'' +
                '}';
    }
}
