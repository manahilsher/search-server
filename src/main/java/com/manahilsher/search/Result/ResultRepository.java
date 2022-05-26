package com.manahilsher.search.Result;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResultRepository extends MongoRepository<ResultModel, String> {

}
