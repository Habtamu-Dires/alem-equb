package com.ekub.payment;

import com.ekub.common.PageResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
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

    //get pages of payments
    @GetMapping
    public ResponseEntity<PageResponse<PaymentResponse>> getPageOfPayments(
            @RequestParam(value = "ekubId-filter",required = false) String ekubId,
            @RequestParam(value = "dateTime-filter", required = false) LocalDateTime dateTime,
            @RequestParam(value = "page", defaultValue = "0", required = false) int page,
            @RequestParam(value = "size", defaultValue = "10", required = false) int size
    ){
        return ResponseEntity.ok(service.getPageOfPayments(ekubId,dateTime,page,size));
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
    public ResponseEntity<List<UserRoundPaymentResponse>> getUserRoundPayments(
            @PathVariable("ekub-id") String ekubId,
            @RequestParam("version") int version
    ){
        return ResponseEntity.ok(service.getUserRoundPayments(ekubId,version));
    }

    // search payment
    @GetMapping("/search/{username}")
    public ResponseEntity<List<PaymentResponse>> searchPayment(
            @PathVariable("username") String username
    ){
        return ResponseEntity.ok(service.search(username));
    }

}
