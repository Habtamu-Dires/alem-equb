package com.ekub.user_guarantee;

import com.ekub.common.BooleanResponse;
import com.ekub.user.UserResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("user-guarantee")
@Tag(name = "userGuarantees")
public class UserGuaranteeController {

    private final UserGuaranteeService service;

    // guarantee user
    @PostMapping("/{round-id}/{user-id}")
    public ResponseEntity<Void> guaranteeUser(
            @PathVariable("round-id") String roundId,
            @PathVariable("user-id") String userId
    ) {
        service.guaranteeUser(roundId, userId);
        return ResponseEntity.accepted().build();
    }

    // cancel guarantee
    @DeleteMapping("/cancel-guarantee/{round-id}/{guarantor-id}/{guaranteed-id}")
    public ResponseEntity<Void> cancelGuarantee(
            @PathVariable("round-id") String roundId,
            @PathVariable("guarantor-id") String guarantorId,
            @PathVariable("guaranteed-id") String guaranteedId
    ){
        service.cancelGuarantee(roundId,guarantorId,guaranteedId);
        return ResponseEntity.accepted().build();
    }

    // is allowed to guarantee
    @GetMapping("/allowed-guarantor/{ekub-id}/{version}")
    public ResponseEntity<BooleanResponse> isAllowedToBeGuarantor(
            @PathVariable("ekub-id") String ekubId,
            @PathVariable("version") int version
    ){
        return ResponseEntity.ok(service.isAllowedToBeGuarantor(ekubId,version));
    }

    // get guarantors
    @GetMapping("/guarantors/{round-id}/{user-id}")
    public ResponseEntity<List<UserResponse>> getGuarantors(
            @PathVariable("round-id") String roundId,
            @PathVariable("user-id") String userId
    ){
        return ResponseEntity.ok(service.getGuarantors(roundId,userId));
    }

    // get guaranteed users
    @GetMapping("/guaranteed-users/{round-id}/{user-id}")
    public ResponseEntity<List<UserResponse>> getGuaranteedUsers(
            @PathVariable("round-id") String roundId,
            @PathVariable("user-id") String userId
    ){
        return ResponseEntity.ok(service.getGuaranteedUsers(roundId,userId));
    }
}
