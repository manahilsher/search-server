package com.manahilsher.search.Result;

import java.util.ArrayList;
import java.util.HashMap;

public class ResultNode {

    private final ResultNode parent;
    private final HashMap<Character, ResultNode> children;
    private ResultModel data;
    private ArrayList<ResultModel> cache;
    private boolean isUrl;

    private ResultService resultService;

    // is there a better way for ResultNode to have access to resultService?
    public ResultNode(ResultNode parent, ResultService resultService) {
        this.parent = parent;
        this.children = new HashMap<>();
        this.cache = new ArrayList<>();
        this.resultService = resultService;
    }

    public void setCache(ResultNode result) {
        if (cache.size() < 5) {
            cache.add(result.data);
        } else {
            for (int i = 0; i < cache.size(); i++) {
                if (cache.get(i).getFrequency() < result.data.getFrequency()) {
                    for (int j = cache.size() - 2; j >= i; j--) {
                        cache.set(j + 1, cache.get(j));
                    }
                    cache.set(i, result.data);
                    if (isUrl) {
                        data.setCache(cache);
                        updateDB();
                    }
                    break;
                }
            }
        }

        if (this.parent != null) {
            this.parent.setCache(result);
        }
    }

    public ResultNode getParent() {
        return parent;
    }

    public HashMap<Character, ResultNode> getChildren() {
        return children;
    }

    public ResultModel getData() {
        return data;
    }

    // called only when originally building trie
    public void setData(ResultModel data) {
        this.isUrl = true;
        this.data = data;
    }

    // Different than setData because this updates DB
    public void updateData(ResultModel data) {
        this.data = data;
        updateDB();
    }

    public ArrayList<ResultModel> getCache() {
        return cache;
    }

    public boolean getIsUrl() {
        return isUrl;
    }

    public void updateDB() {
        resultService.update(data);
    }

}
