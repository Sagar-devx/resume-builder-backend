package com.sagar.resume.Controller;

import com.razorpay.RazorpayException;
import com.sagar.resume.Document.Payment;
import com.sagar.resume.Service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.sagar.resume.Util.AppConstants.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(PAYMENT)
@Slf4j
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping(CREATE_ORDER)
    public ResponseEntity<?> createOrder(@RequestBody Map<String,String> request, Authentication authentication) throws RazorpayException {

        String planType =request.get("planType");

        if(!PREMIUM.equalsIgnoreCase(planType))
        {
            return ResponseEntity.badRequest().body(Map.of("message","Invalid Plan type"));
        }

       Payment payment=paymentService.createOrder(authentication.getPrincipal(),planType);

        Map<String,Object> response =Map.of(
                "orderId",payment.getRazorPayOrderId(),
                "amount",payment.getAmount(),
                "currency",payment.getCurrency(),
                "recipt",payment.getReceipt()
        );

        return ResponseEntity.ok(response);

    }

    @PostMapping(VERIFY_PAYMENT)
    public ResponseEntity<?> verifyPayment(@RequestBody Map<String,String> request)
    {
       String razorpayOrderId =request.get("razorpay_order_id");
       String razorpayPaymentId= request.get("razorpay_payment_id");
       String razorpaySignature = request.get("razorpay_signature");

       if(Objects.isNull(razorpayPaymentId) ||Objects.isNull(razorpayOrderId) || Objects.isNull(razorpaySignature))
       {
           return  ResponseEntity.badRequest().body(Map.of("message","Missing required payment parameters"));
       }

       boolean isValid =paymentService.verifyPayment(razorpayOrderId,razorpayPaymentId,razorpaySignature);

       if(isValid)
       {
           return ResponseEntity.ok(Map.of(
                   "message","Payment verified successfully",
                   "status","success")
           );
       }
       else {
            return ResponseEntity.badRequest().body(Map.of("message","Payment verification failed"));
       }

    }

    @GetMapping
    public ResponseEntity<?> getPaymentHistory(Authentication authentication)
    {
       List<Payment> paymentList= paymentService.getUserPayments(authentication.getPrincipal());

       return ResponseEntity.ok(paymentList);

    }

    @GetMapping(ORDER_ID)
    public ResponseEntity<?> getOrderDetails(@PathVariable("orderId") String orderId)
    {
        Payment paymentDetails =paymentService.getPaymentDetails(orderId);

        return  ResponseEntity.ok(paymentDetails);

    }
}
