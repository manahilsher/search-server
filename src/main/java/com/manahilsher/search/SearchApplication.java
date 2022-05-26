package com.manahilsher.search;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/*
	* Notes:
	* 1. No longer going to update the DB
	* 2. It's going to update a cache in memory called Redis
	* 3. Job that runs every 30 minutes it will from Redis -> put it into mongo
*/

@SpringBootApplication
public class SearchApplication {

	public static void main(String[] args) {
		SpringApplication.run(SearchApplication.class, args);
	}

}
