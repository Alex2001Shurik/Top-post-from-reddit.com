package com.toppostsfromredditcom;

import android.view.Display;

import java.util.ArrayList;

public class Pagination {
    private final int itemsPerPage, lastPageItems, lastPage;
    private final ArrayList<Model> models;


    public Pagination(int itemsPerPage, ArrayList<Model> models) {
        this.itemsPerPage = itemsPerPage;
        this.models = models;
        this.lastPage = models.size() / itemsPerPage;
        this.lastPageItems = models.size() % itemsPerPage;
    }

    public ArrayList<Model> generateData(int currentPage) {
        int startItem = currentPage * itemsPerPage;
        ArrayList<Model> newPageData = new ArrayList<>();
        if (currentPage == lastPage) {
            for (int i = 0; i < lastPageItems; i++) {
                newPageData.add(models.get(startItem + i));
            }
        } else {
            for (int i = 0; i < itemsPerPage; i++) {
                newPageData.add(models.get(startItem + i));
            }
        }
        return newPageData;
    }

    public int getLastPage() {
        return lastPage;
    }

}


