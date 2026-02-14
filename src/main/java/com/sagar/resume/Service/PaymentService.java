package com.sagar.resume.Service;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.razorpay.Utils;
import com.sagar.resume.Document.Payment;
import com.sagar.resume.Document.User;
import com.sagar.resume.Dto.AuthResponse;
import com.sagar.resume.Repository.PaymentRepository;
import com.sagar.resume.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static com.sagar.resume.Util.AppConstants.PREMIUM;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {

    private static final Logger log = LoggerFactory.getLogger(PaymentService.class);
    private final PaymentRepository paymentRepository;
    private final AuthService authService;
    private final UserRepository userRepository;

    @Value("${razorpay.key.id}")
    private String razorPayKeyId;

    @Value("${razorpay.key.secret}")
    private String razorPayKeySecret;

    public Payment createOrder(Object principal, String planType) throws RazorpayException {

        // Step 0: Fetch current user profile
        AuthResponse response = authService.getProfile(principal);

        // Step 1: Create Razorpay client
        RazorpayClient razorpayClient = new RazorpayClient(razorPayKeyId, razorPayKeySecret);

        // Step 2: Prepare Order details
        int amount = 99900;                           // amount in paise
        String currency = "INR";
        String receipt = PREMIUM + "_" + UUID.randomUUID().toString().substring(0, 8);

        JSONObject orderRequest = new JSONObject();
        orderRequest.put("amount", amount);
        orderRequest.put("currency", currency);
        orderRequest.put("receipt", receipt);

        // Step 3: Create order in Razorpay
        Order razorPayOrder = razorpayClient.orders.create(orderRequest);

        // Step 4: Save order details in MongoDB

        Payment newPayment = Payment.builder()
                            .userId(response.getId())
                            .razorPayOrderId(razorPayOrder.get("id"))
                            .amount(amount)
                            .currency(currency)
                            .planType(planType)
                            .receipt(receipt)
                            .status("created")
                            .build();

        paymentRepository.save(newPayment);

        // Step 5: return saved object
        return newPayment;
    }


    public boolean verifyPayment(String razorpayOrderId, String razorpayPaymentId, String razorpaySignature) {

        try {
            JSONObject attributes = new JSONObject();
            attributes.put("razorpay_order_id", razorpayOrderId);
            attributes.put("razorpay_payment_id", razorpayPaymentId);
            attributes.put("razorpay_signature", razorpaySignature);

            Utils.verifyPaymentSignature(attributes, razorPayKeySecret);

            Payment payment = paymentRepository.findByRazorPayOrderId(razorpayOrderId)
                    .orElseThrow(() -> new RuntimeException("Payment not found"));

            payment.setRazorPayPaymentId(razorpayPaymentId);
            payment.setRazorPaySignature(razorpaySignature);
            payment.setStatus("paid");
            paymentRepository.save(payment);

            upgradeUserSubscription(payment.getUserId(), payment.getPlanType());

            return true;

        } catch (RazorpayException e) {
            log.error("Invalid signature: {}", e.getMessage());
            return false;
        }
    }

    private void upgradeUserSubscription(String userId,String planType)
    {
        User existingUser = userRepository.findById(userId).orElseThrow(()->new UsernameNotFoundException("User not found"));

        existingUser.setSubscriptionPlan(planType);
        userRepository.save(existingUser);

        log.info("User {} upgraded to {} plan",userId,planType);

    }

    public List<Payment> getUserPayments(Object principal) {
        AuthResponse response = authService.getProfile(principal);

        return paymentRepository.findByUserIdOrderByCreatedAtDesc(response.getId());
    }

    public Payment getPaymentDetails(String orderId) {

        return paymentRepository.findByRazorPayOrderId(orderId).orElseThrow(()->new RuntimeException("Order not found"));
    }
}
