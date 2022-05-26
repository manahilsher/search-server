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

    public void buildTrie() {
        this.root = new ResultNode(null, resultService);
        List<ResultModel> results = resultService.findAll();
        for (ResultModel r : results) {
            insert(r);
        }
    }

    public void insert(ResultModel result) {
        ResultNode current = root;

        String url = result.getUrl();

        for (char l : url.toLowerCase().toCharArray()) {
            ResultNode parent = current;
            current = parent.getChildren().computeIfAbsent(l, c -> new ResultNode(parent, resultService));
        }
        current.setData(result);
        current.getParent().setCache(current);
    }

    public ArrayList<ResultModel> search(String query) {
        ResultNode current = root;
        for (int i = 0; i < query.length(); i++) {
            char c = query.charAt(i);
            ResultNode node = current.getChildren().get(c);
            if (node == null)
                return new ArrayList<>();
            current = node;
        }
        return current.getCache();
    }

    public ResultNode find(String url) {
        url = url.toLowerCase();
        ResultNode current = root;
        for (int i = 0; i < url.length(); i++) {
            char c = url.charAt(i);
            ResultNode node = current.getChildren().get(c);
            current = node;
        }
        return current;
    }

    public void select(String id) {
        ResultModel result = resultService.findById(id);
        System.out.println("============================");
        System.out.println("============================");
        System.out.println("============================");
        System.out.println("============================");
        System.out.println("============================");
        System.out.println(result);
        System.out.println(result.getId());
        System.out.println(result.getUrl());

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
