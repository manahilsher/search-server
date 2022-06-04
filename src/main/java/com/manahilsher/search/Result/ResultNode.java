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

    /*
     * This method updates the cache if the node has a higher frequency than a node
     * that's already in the cache
     */
    public void setCache(ResultNode result) {
        boolean cacheChanged = false;

        if (cache.size() < 5) {
            // If cache isn't yet full, just add this to cache
            cache.add(result.data);
            cacheChanged = true;
        } else {
            for (int i = 0; i < cache.size(); i++) {
                // Compare with the node in cache
                if (cache.get(i).getFrequency() < result.data.getFrequency()) {

                    // Replace the node in cache with this node
                    cache.remove(cache.get(i));
                    cache.add(result.data);

                    // In case this node holds an actual db item, update the item's cache as well
                    if (isUrl) {
                        data.setCache(cache);
                        updateDB();
                    }

                    cacheChanged = true;

                    break;
                }
            }
        }

        /*
         * Only need to update parent's cache if we updated the current node's cache.
         * This is because if the current cache wasn't updated, that means all of the
         * frequencies in the current cache were higher than the frequency of the node
         * in question, and the parent's cache frequencies are equal to or even
         * higher than them, so we would end up not changing the parent's cache anyway.
         */
        if (cacheChanged && this.parent != null) {
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

    /* Called only when originally building trie */
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
