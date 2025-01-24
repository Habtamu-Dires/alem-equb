package com.ekub.payment;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/payments")
@Tag(name = "payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService service;

    // get payments of current round
    @GetMapping("/current-round/{ekub-id}")
    public ResponseEntity<List<PaymentResponse>> getPaymentsOfCurrentRound(
            @PathVariable("ekub_id") String ekubId
    ){
        return ResponseEntity.ok(service.getCurrentRoundPayments(ekubId));
    }

    // get payment of given round of ekub
    @GetMapping("/{ekub-id}/{round-no}")
    public ResponseEntity<List<PaymentResponse>> getPaymentsOfEkubRound(
            @PathVariable("ekub_id") String ekubId,
            @PathVariable("round_no") int roundNo
    ){
        return ResponseEntity.ok(service.getLstOfPayment(ekubId,roundNo));
    }

    // get payment status of of a user in ekub
    @GetMapping("/payment-status/{ekub-id}")
    public ResponseEntity<List<EkubPaymentStatusResponse>> getEkubPaymentStatus(
            @PathVariable("ekub-id") String ekubId
    ) {
        return ResponseEntity.ok(service.getEkubPaymentStatus(ekubId));
    }
}
