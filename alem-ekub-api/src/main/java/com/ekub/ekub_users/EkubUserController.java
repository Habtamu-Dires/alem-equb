package com.ekub.ekub_users;

import com.ekub.ekub.EkubResponse;
import com.ekub.user.UserResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ekub-users")
@Tag(name = "ekub-users")
@RequiredArgsConstructor
public class EkubUserController {

    private final EkubUserService service;

    // add user to ekub
    @PostMapping("/{ekub_id}/{user_id}")
    public ResponseEntity<Void> addEkubUser(
            @PathVariable("ekub_id") String ekubId,
            @PathVariable("user_id") String userId
    ){
        service.createEkubUser(ekubId,userId);
        return ResponseEntity.accepted().build();
    }

    // remove user from ekub
    @PutMapping("/{ekub_id}/{user_id}")
    public ResponseEntity<Void> removeUser(
            @PathVariable("ekub_id") String ekubId,
            @PathVariable("user_id") String userId
    ){
        service.removeUser(ekubId,userId);
        return ResponseEntity.accepted().build();
    }

    // get ekub users
    @GetMapping("/ekub-users/{ekub-id}")
    public ResponseEntity<List<UserResponse>> getEkubUsers(
            @PathVariable("ekub-id") String ekubId
    ){
        return  ResponseEntity.ok(service.getEkubUserResponses(ekubId));
    }

    // get list of winners
    @GetMapping("/winners/{ekub-id}")
    public ResponseEntity<List<UserResponse>> getEkubWinners(
            @PathVariable("ekub-id") String ekubId
    ){
        return ResponseEntity.ok(service.getEkubWinnerResponses(ekubId));
    }
    //get list of draw participants
    @GetMapping("/participants/{ekub-id}")
    public ResponseEntity<List<UserResponse>> getDrawParticipants(
            @PathVariable("ekub-id") String ekubId
    ){
        return ResponseEntity.ok(service.getEkubDrawParticipantResponses(ekubId));
    }

    // get list of equbs of a user
    @GetMapping("/equbs/{user-id}")
    public ResponseEntity<List<EkubResponse>> getEkubsOfUser(
            @PathVariable("user-id") String userId
    ){
        return ResponseEntity.ok(service.getEkubsOfUser(userId));
    }

    //get round winner by ekubId and round number
    @GetMapping("/winner/{ekub-id}")
    public ResponseEntity<UserResponse> getRoundWinner(
            @PathVariable("ekub-id") String ekubId,
            @RequestParam("round-no") Integer roundNumber
    ){
        return ResponseEntity.ok(service.getRoundWinner(ekubId,roundNumber));
    }


}
