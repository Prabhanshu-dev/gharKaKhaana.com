package org.gharKaKhaana.payment.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.gharKaKhaana.payment.application.PaymentService;
import org.gharKaKhaana.payment.application.dto.PaymentCallbackRequest;
import org.gharKaKhaana.payment.application.dto.PaymentInitializeRequest;
import org.gharKaKhaana.payment.application.dto.PaymentResponse;
import org.gharKaKhaana.payment.common.ApiResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private static final int DEFAULT_PAGE_SIZE = 20;
    private final PaymentService paymentService;

    @PostMapping("/initialize")
    public ResponseEntity<ApiResponse<PaymentResponse>> initializePayment(
            @RequestHeader("X-Auth-User-Id") Long userId,
            @Valid @RequestBody PaymentInitializeRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Payment initialized", paymentService.initializePayment(userId, request)));
    }

    @PostMapping("/callback")
    public ResponseEntity<ApiResponse<PaymentResponse>> processCallback(
            @RequestHeader("X-Auth-User-Id") Long userId,
            @Valid @RequestBody PaymentCallbackRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Payment processed", paymentService.processCallback(userId, request)));
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<ApiResponse<PaymentResponse>> getPaymentByOrderId(
            @PathVariable Long orderId,
            @RequestHeader("X-Auth-User-Id") Long userId) {
        // Additional auth check could go here if needed, but the gateway already authenticates.
        return ResponseEntity.ok(ApiResponse.success("Payment retrieved", paymentService.getPaymentByOrderId(orderId)));
    }

    @GetMapping("/customer")
    public ResponseEntity<ApiResponse<Page<PaymentResponse>>> getMyPayments(
            @RequestHeader("X-Auth-User-Id") Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "" + DEFAULT_PAGE_SIZE) int size) {
        Page<PaymentResponse> payments = paymentService.getPaymentsByUser(userId, PageRequest.of(page, size, Sort.by("createdAt").descending()));
        return ResponseEntity.ok(ApiResponse.success("Payments retrieved", payments));
    }
}
