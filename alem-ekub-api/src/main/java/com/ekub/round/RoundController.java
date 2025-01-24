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
            @PathVariable("ekub-id") String ekbuId
    ){
        return ResponseEntity.ok(service.getRoundResByEkubId(ekbuId));
    }
}
