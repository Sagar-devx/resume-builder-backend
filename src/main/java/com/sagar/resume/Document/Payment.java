package com.sagar.resume.Document;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "payments")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {

    @Id
    @JsonProperty("_id")
    private String id;

    private String userId;
    private String razorPayOrderId;
    private String razorPayPaymentId;
    private String razorPaySignature;
    private Integer amount;
    private String currency;
    private String planType;

    @Builder.Default
    private String status = "created";   // created, paid, failed, refund_requested

    private String receipt;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}
