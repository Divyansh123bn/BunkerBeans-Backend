package com.bunkerbeans.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.bunkerbeans.entity.User;

public interface UserRepository extends MongoRepository<User,String>{

}
