package com.manahilsher.search;

import com.manahilsher.search.Result.ResultTrie;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class StartupRunner implements CommandLineRunner {
    protected final Log logger = LogFactory.getLog(getClass());

    @Autowired
    private ResultTrie resultTrie;

    @Override
    public void run(String... args) throws Exception {
        logger.info("ApplicationStartupRunner run method Started !!");

        /*
         * Build the trie based on what is in the db when the server first starts.
         * This can be upgraded in the future so that the trie rebuilds itself in case
         * anything in the db is changed.
         */
        resultTrie.buildTrie();
    }
}