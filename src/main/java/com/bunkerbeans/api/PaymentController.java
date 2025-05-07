package com.bunkerbeans.api;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Hex;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bunkerbeans.entity.OrderDetail;
import com.bunkerbeans.exception.CustomException;
import com.bunkerbeans.repository.OrderRepository;
import com.bunkerbeans.utility.Utilities;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;

@RestController
@RequestMapping("/users/payments")
public class PaymentController {

    @Autowired
    private OrderRepository orderRepo;

    @Autowired
    private Utilities utilities;

    @PostMapping("/create")
    public ResponseEntity<?> createOrder(@RequestBody Map<String, Object> data) throws RazorpayException,CustomException {
        RazorpayClient razorpay = new RazorpayClient("rzp_test_IKPQqvJLdl3VvO", "sWGvxC3krGS3yJdVdU9z9YUk");

        JSONObject options = new JSONObject();
        options.put("amount", data.get("amount")); // amount in paise
        options.put("currency", "INR");
        options.put("receipt", "txn_" + UUID.randomUUID());

        Order order = razorpay.orders.create(options);

        OrderDetail dbOrder = new OrderDetail();
        dbOrder.setOrderId(order.get("id"));
        Number amount = (Number) data.get("amount");
        dbOrder.setAmount(amount.doubleValue()/100);
        dbOrder.setCurrency("INR");
        dbOrder.setStatus("created");
        dbOrder.setReceipt(order.get("receipt"));
        dbOrder.setCreatedAt(new Date());
        dbOrder.setPaymentId(utilities.getNextSequence("orders"));

        orderRepo.save(dbOrder);

        return ResponseEntity.ok(order.toJson().toMap());

    }

    @PostMapping("/verify")
    public ResponseEntity<?> verifyPayment(@RequestBody Map<String, String> payload) {
    String razorpayOrderId = payload.get("razorpay_order_id");
    String razorpayPaymentId = payload.get("razorpay_payment_id");
    String razorpaySignature = payload.get("razorpay_signature");

    String generatedSignature;
    try {
        String secret = "sWGvxC3krGS3yJdVdU9z9YUk";
        String data = razorpayOrderId + '|' + razorpayPaymentId;

        Mac sha256Hmac = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKey = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
        sha256Hmac.init(secretKey);

        byte[] hash = sha256Hmac.doFinal(data.getBytes());
        generatedSignature = Hex.encodeHexString(hash);

        if (generatedSignature.equals(razorpaySignature)) {
            OrderDetail order = orderRepo.findByOrderId(razorpayOrderId);
            if (order != null) {
                order.setStatus("completed");
                orderRepo.save(order);
            }
            return ResponseEntity.ok("Payment verified and order updated");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid signature");
        }
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Verification failed");
    }
}

}
