package org.himalayas.service;

public interface UpiPaymentService {
    boolean processPayment(String upiId, double amount);
}

