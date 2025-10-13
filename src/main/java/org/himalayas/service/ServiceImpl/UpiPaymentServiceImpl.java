package org.himalayas.service.ServiceImpl;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import org.json.JSONObject;
import org.himalayas.service.UpiPaymentService;
import org.springframework.stereotype.Service;

@Service
public class UpiPaymentServiceImpl implements UpiPaymentService {
    @Override
    public boolean processPayment(String upiId, double amount) {
        try {
            RazorpayClient razorpay = new RazorpayClient("YOUR_KEY_ID", "YOUR_KEY_SECRET");
            JSONObject orderRequest = new JSONObject();
            orderRequest.put("amount", (int)(amount * 100)); // amount in paise
            orderRequest.put("currency", "INR");
            orderRequest.put("payment_capture", 1);
            orderRequest.put("method", "upi");
            orderRequest.put("receipt", "order_rcptid_11");
            Order order = razorpay.orders.create(orderRequest);
            // You can return the order id or payment link to the frontend for further processing
            // For demo, return true if order is created
            return order != null && order.get("id") != null;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
