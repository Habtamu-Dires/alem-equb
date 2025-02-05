package com.ekub.round;

import com.ekub.user.UserResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rounds")
@Tag(name = "rounds")
@RequiredArgsConstructor
public class RoundController {

    private final RoundService service;

    // get list of rounds
    @GetMapping("/{ekub-id}")
    public ResponseEntity<List<RoundResponse>> getEkubRounds(
            @PathVariable("ekub-id") String ekbuId,
            @RequestParam("version") int version
    ){
        return ResponseEntity.ok(service.getRoundResByEkub(ekbuId,version));
    }

    //get round by ekubId, version and number
    @GetMapping("/{ekub-id}/{version}/{round-no}")
    public ResponseEntity<RoundResponse> getRoundByEkubAndRoundNo(
            @PathVariable("ekub-id") String ekubId,
            @PathVariable("version") int version,
            @PathVariable("round-no") int roundNo
    ){
        return ResponseEntity.ok(service.getRoundByEkubAndRoundNo(ekubId,version,roundNo));
    }

    // get user pending payments
    @GetMapping("/user/pending-payments/{user-id}")
    public ResponseEntity<List<UserPendingPaymentResponse>> getUserPendingPayments(
            @PathVariable("user-id") String userId
    ){
        return ResponseEntity.ok(service.getUserPendingPayments(userId));
    }

}
