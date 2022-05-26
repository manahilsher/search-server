package com.manahilsher.search.Result;

import java.util.ArrayList;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "results")
public class ResultModel {

    @Id
    private String id;
    private String url;
    private int frequency;
    private ArrayList<ResultModel> cache;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public ArrayList<ResultModel> getCache() {
        return this.cache;
    }

    public void setCache(ArrayList<ResultModel> cache) {
        this.cache = cache;
    }

}
