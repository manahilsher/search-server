package com.manahilsher.search.Result;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ResultTrie {

    private ResultNode root;

    @Autowired
    private ResultService resultService;

    /*
     * Build the trie by first creating a root node, and then getting all the data
     * from the db.
     */
    public void buildTrie() {
        this.root = new ResultNode(null, resultService);

        List<ResultModel> results = resultService.findAll();

        // insert each db item into the trie
        for (ResultModel r : results) {
            insert(r);
        }
    }

    /*
     * Called only in the building of the trie. Creates a node for the result item
     * and as many nodes needed in the path that leads to that result.
     */
    public void insert(ResultModel result) {
        ResultNode current = root;

        String url = result.getUrl();

        for (char l : url.toLowerCase().toCharArray()) {
            ResultNode parent = current;

            /*
             * If the current node has already an existing reference to the current letter
             * (through one of the elements in the “children” field), then set current node
             * to that referenced node. Otherwise, create a new node, set the letter equal
             * to the current letter, and also initialize current node to this new node
             */
            current = current.getChildren().computeIfAbsent(l, c -> new ResultNode(parent, resultService));
        }

        // This node holds an actual db item, so set data accordingly
        current.setData(result);
        current.getParent().setCache(current);
    }

    /*
     * Called when the client sends in a query.
     * 
     * We get the query and follow the trie down to the correct node, which has the
     * top 5 results saved.
     * 
     * We return these top 5 results.
     */
    public ArrayList<ResultModel> search(String query) {
        ResultNode current = root;

        // Follow the trie down to the correct node
        for (int i = 0; i < query.length(); i++) {
            char c = query.charAt(i);
            ResultNode node = current.getChildren().get(c);

            // No matches in the db, so of course no cache
            if (node == null)
                return new ArrayList<>();

            current = node;
        }

        return current.getCache();
    }

    /*
     * Used to locate a result in trie
     * 
     * Follows the url string in the trie. If it reaches the end, this means the
     * trie has that url, which means the db has it. Return the respective db item.
     * 
     * If at any point we hit a null, this means the url does not exist in our trie,
     * nor in the db. Return null. (This does not actually ever happen in our code
     * right now, as this method is only called by the select method, which is
     * always passed an existing id, which means the url definitely exists in our
     * db, and our trie... theoretically).
     */
    public ResultNode find(String url) {
        url = url.toLowerCase();
        ResultNode current = root;

        // Follow the url down the trie
        for (int i = 0; i < url.length(); i++) {
            char c = url.charAt(i);
            ResultNode node = current.getChildren().get(c);

            // No path that leads to this url
            if (node == null) {
                return null;
            }

            current = node;
        }
        return current;
    }

    /*
     * Called when the client requests a specific result item.
     * 
     * We update frequency and caches as necessary.
     * 
     * Does not actually return anything right now. In the future I will update to
     * actually return something.
     */
    public void select(String id) {
        ResultModel result = resultService.findById(id);

        // Update the frequency of this result.
        int frequency = result.getFrequency() + 1;
        result.setFrequency(frequency);

        // Find the node that holds this result and update its data to match.
        ResultNode resultNode = find(result.getUrl());
        resultNode.updateData(result);

        /*
         * Also update its parents' cache in case this result's frequency is now greater
         * than one that's already in the cache.
         */
        resultNode.getParent().setCache(resultNode);

    }
}
