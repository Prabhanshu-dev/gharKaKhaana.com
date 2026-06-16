package org.gharKaKhaana.payment.infrastructure.gateway;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Mock implementation of Razorpay Gateway.
 * In Phase 5, this will be replaced with actual Razorpay SDK calls.
 */
@Component
public class RazorpayGateway {

    public String createOrder(Long orderId, BigDecimal amount) {
        // Simulates calling Razorpay to create an order
        return "order_" + UUID.randomUUID().toString().replace("-", "").substring(0, 14);
    }

    public boolean verifySignature(String razorpayOrderId, String razorpayPaymentId, String razorpaySignature) {
        // Simulates signature verification
        return true;
    }
}
