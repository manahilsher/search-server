package com.manahilsher.search.Result;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/results")
@CrossOrigin(origins = { "http://localhost:3000", "https://localhost:3000" })
public class ResultController {

    @Autowired
    private ResultTrie resultTrie;

    /*
     * This is called when the user types anything in the search box.
     * 
     * We get the query and follow the trie down to the correct node, which has the
     * top 5 results saved.
     * 
     * We return these top 5 results.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public ArrayList<ResultModel> search(@RequestBody ReqBody reqBody) {
        String query = reqBody.getQuery();
        return resultTrie.search(query);
    }

    /*
     * This is called when the user selects one of the search results.
     * 
     * Atm, the db is updated by adding 1 to the result's frequency count
     * and updating the cache of all the nodes above it in case this result's
     * frequency becomes greater than a result that's in the cache.
     * 
     * I plan to refactor this to save in redis instead, and save in the db every 30
     * minutes.
     * 
     * I also plan to return something to the user.
     */
    @PostMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void select(@PathVariable String id) {
        resultTrie.select(id);
    }
}
