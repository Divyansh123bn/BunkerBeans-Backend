package com.bunkerbeans.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.bunkerbeans.entity.UserEntity;


public interface UserRepository extends MongoRepository<UserEntity,Long>{

    public Optional<UserEntity> findByEmail(String email);
}
