package com.mustafa.sar.instagramthesis.models;

import java.util.List;


// Response
public class ImageList {

    private int total;
    private List<Image> hits;
    private int totalHits;

    public ImageList(int total, int totalHits, List<Image> hits) {
        this.total = total;
        this.totalHits = totalHits;
        this.hits = hits;
    }

    public int getTotal() {
        return total;
    }

    public int getTotalHits() {
        return totalHits;
    }

    public List<Image> getHits() {
        return hits;
    }

    public int getTotalOfPages() {
        return (int) Math.ceil(total / 20.0);
    }
}