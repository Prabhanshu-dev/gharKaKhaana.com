package org.gharKaKhaana.service;

public interface UpiPaymentService {
    boolean processPayment(String upiId, double amount);
}

