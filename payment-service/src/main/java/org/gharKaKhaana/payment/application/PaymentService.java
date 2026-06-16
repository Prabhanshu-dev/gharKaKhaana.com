package org.gharKaKhaana.payment.application;

import org.gharKaKhaana.payment.application.dto.PaymentCallbackRequest;
import org.gharKaKhaana.payment.application.dto.PaymentInitializeRequest;
import org.gharKaKhaana.payment.application.dto.PaymentResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PaymentService {
    PaymentResponse initializePayment(Long userId, PaymentInitializeRequest request);
    PaymentResponse processCallback(Long userId, PaymentCallbackRequest request);
    PaymentResponse getPaymentByOrderId(Long orderId);
    Page<PaymentResponse> getPaymentsByUser(Long userId, Pageable pageable);
}
