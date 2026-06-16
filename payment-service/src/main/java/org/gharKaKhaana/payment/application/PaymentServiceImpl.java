package org.gharKaKhaana.payment.application;

import lombok.RequiredArgsConstructor;
import org.gharKaKhaana.payment.application.dto.PaymentCallbackRequest;
import org.gharKaKhaana.payment.application.dto.PaymentInitializeRequest;
import org.gharKaKhaana.payment.application.dto.PaymentResponse;
import org.gharKaKhaana.payment.common.exception.PaymentNotFoundException;
import org.gharKaKhaana.payment.domain.Payment;
import org.gharKaKhaana.payment.domain.PaymentStatus;
import org.gharKaKhaana.payment.infrastructure.PaymentRepository;
import org.gharKaKhaana.payment.infrastructure.gateway.RazorpayGateway;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final RazorpayGateway razorpayGateway;

    @Override
    @Transactional
    public PaymentResponse initializePayment(Long userId, PaymentInitializeRequest request) {
        // Optional: Check if a payment for this order already exists
        if (paymentRepository.findByOrderId(request.getOrderId()).isPresent()) {
            throw new IllegalStateException("Payment already initialized for this order.");
        }

        // Generate Razorpay Order ID (Mocked for now)
        String rzpOrderId = razorpayGateway.createOrder(request.getOrderId(), request.getAmount());

        Payment payment = Payment.builder()
                .userId(userId)
                .orderId(request.getOrderId())
                .amount(request.getAmount())
                .status(PaymentStatus.PENDING)
                .transactionId(rzpOrderId) // Store Razorpay Order ID temporarily as txId
                .build();

        return toResponse(paymentRepository.save(payment));
    }

    @Override
    @Transactional
    public PaymentResponse processCallback(Long userId, PaymentCallbackRequest request) {
        Payment payment = paymentRepository.findByOrderId(request.getOrderId())
                .orElseThrow(() -> new PaymentNotFoundException("Payment not found for order: " + request.getOrderId()));

        if (!payment.getUserId().equals(userId)) {
            throw new IllegalStateException("Unauthorized access to payment.");
        }

        boolean isValid = razorpayGateway.verifySignature(
                request.getRazorpayOrderId(),
                request.getRazorpayPaymentId(),
                request.getRazorpaySignature()
        );

        if (isValid) {
            payment.setStatus(PaymentStatus.SUCCESS);
            payment.setTransactionId(request.getRazorpayPaymentId()); // Final Transaction ID
            payment.setPaymentMethod("RAZORPAY"); // Could be parsed further
        } else {
            payment.setStatus(PaymentStatus.FAILED);
        }

        return toResponse(paymentRepository.save(payment));
    }

    @Override
    public PaymentResponse getPaymentByOrderId(Long orderId) {
        return paymentRepository.findByOrderId(orderId)
                .map(this::toResponse)
                .orElseThrow(() -> new PaymentNotFoundException("Payment not found for order: " + orderId));
    }

    @Override
    public Page<PaymentResponse> getPaymentsByUser(Long userId, Pageable pageable) {
        return paymentRepository.findByUserId(userId, pageable).map(this::toResponse);
    }

    private PaymentResponse toResponse(Payment payment) {
        return PaymentResponse.builder()
                .id(payment.getId())
                .orderId(payment.getOrderId())
                .userId(payment.getUserId())
                .amount(payment.getAmount())
                .status(payment.getStatus())
                .transactionId(payment.getTransactionId())
                .paymentMethod(payment.getPaymentMethod())
                .createdAt(payment.getCreatedAt())
                .updatedAt(payment.getUpdatedAt())
                .build();
    }
}
