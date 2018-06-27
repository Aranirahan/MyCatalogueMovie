package com.aranirahan.mycataloguemovie.model.main;

import com.aranirahan.mycataloguemovie.model.sub.DatesItem;
import com.aranirahan.mycataloguemovie.model.sub.ResultsItem;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class PlayingItem {

    @SerializedName("datesItem")
    private DatesItem datesItem;

    @SerializedName("page")
    private int page;

    @SerializedName("total_pages")
    private int totalPages;

    @SerializedName("results")
    private ArrayList<ResultsItem> results;

    @SerializedName("total_results")
    private int totalResults;

    public void setDatesItem(DatesItem datesItem) {
        this.datesItem = datesItem;
    }

    public DatesItem getDatesItem() {
        return datesItem;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPage() {
        return page;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setResults(ArrayList<ResultsItem> results) {
        this.results = results;
    }

    public ArrayList<ResultsItem> getResults() {
        return results;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

    public int getTotalResults() {
        return totalResults;
    }

    @Override
    public String toString() {
        return
                "PlayingItem{" +
                        "datesItem = '" + datesItem + '\'' +
                        ",page = '" + page + '\'' +
                        ",total_pages = '" + totalPages + '\'' +
                        ",results = '" + results + '\'' +
                        ",total_results = '" + totalResults + '\'' +
                        "}";
    }
}