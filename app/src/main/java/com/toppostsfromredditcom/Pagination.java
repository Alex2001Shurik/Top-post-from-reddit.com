package com.toppostsfromredditcom;

import android.view.Display;

import com.toppostsfromredditcom.model.children.Data;

import java.util.ArrayList;

public class Pagination {
    private int itemsPerPage, lastPageItems, lastPage;
    private ArrayList<Data> models;


    public Pagination(int itemsPerPage, ArrayList<Data> models) {
        this.itemsPerPage = itemsPerPage;
        this.models = models;
        this.lastPage = models.size() % itemsPerPage == 0 ? (models.size() / itemsPerPage) - 1 : models.size() / itemsPerPage;
        this.lastPageItems = models.size() % itemsPerPage == 0 ? itemsPerPage : models.size() % itemsPerPage;
    }


    public ArrayList<Data> getModels() {
        return models;
    }

    public ArrayList<Data> generateData(int currentPage) {
        int startItem = currentPage * itemsPerPage;
        ArrayList<Data> newPageData = new ArrayList<>();
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


