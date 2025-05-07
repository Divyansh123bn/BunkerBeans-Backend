package com.bunkerbeans.entity;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Document(collection = "orders")
@Data
public class OrderDetail {
    @Id
    private String id;
    private String orderId;
    private Double amount;
    private String currency;
    private String status;
    private String receipt;
    private Date createdAt;
    private Long paymentId;
}
