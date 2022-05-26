package com.manahilsher.search.Result;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ResultService {

    @Autowired
    private ResultRepository resultRepository;

    public List<ResultModel> findAll() {
        try {
            List<ResultModel> results = resultRepository.findAll();
            return results;
        } catch (Exception e) {
            throw e;
        }
    }

    public ResultModel findById(String id) {
        try {
            Optional<ResultModel> result = resultRepository.findById(id);
            return result.get();
        } catch (Exception e) {
            throw e;
        }
    }

    public void update(ResultModel result) {
        this.resultRepository.save(result);
    }
}
