package com.manahilsher.search.Result;

import org.springframework.stereotype.Component;

@Component
public class ReqBody {

    private String query;

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

}
