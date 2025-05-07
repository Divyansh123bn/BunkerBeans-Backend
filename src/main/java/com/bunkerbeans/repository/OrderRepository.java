package com.bunkerbeans.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.bunkerbeans.entity.OrderDetail;



public interface OrderRepository extends MongoRepository<OrderDetail,String> {
        OrderDetail findByOrderId(String orderId);
}
