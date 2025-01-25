package com.ekub.payment;

import com.ekub.common.PageResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/payments")
@Tag(name = "payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService service;

    //create payment
    @PostMapping
    public ResponseEntity<Void> createPayment(
            @RequestBody @Valid PaymentRequest request
    ){
        service.createPayment(request);
        return ResponseEntity.accepted().build();
    }

    //get payments of a user
    @GetMapping("/user/{user-id}")
    public ResponseEntity<List<PaymentResponse>> getUserPayments(
            @PathVariable("user-id") String userId
    ){
        return ResponseEntity.ok(this.service.getUserPayments(userId));
    }

    //get user round payments
    @GetMapping("/user/round/{ekub-id}")
    public ResponseEntity<PageResponse<UserRoundPaymentResponse>> getUserRoundPayments(
            @PathVariable("ekub-id") String ekubId,
            @RequestParam(value = "page", defaultValue = "10", required = false) int page,
            @RequestParam(value = "size", defaultValue = "10", required = false) int size
    ){
        return ResponseEntity.ok(service.getUserRoundPayments(ekubId,page,size));
    }

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

}
