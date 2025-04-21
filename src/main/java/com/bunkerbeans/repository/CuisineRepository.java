package com.bunkerbeans.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.bunkerbeans.entity.Cuisine;

public interface CuisineRepository extends MongoRepository<Cuisine,Long> {


}
