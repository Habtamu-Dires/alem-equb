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

    // join ekub
    @PostMapping("/{ekub-id}")
    public ResponseEntity<Void> joinEkub(@PathVariable("ekub-id") String ekubId){
        service.joinEkub(ekubId);
        return ResponseEntity.accepted().build();
    }

    // leave ekub
    @DeleteMapping("/{ekub-id}")
    public ResponseEntity<Void> leaveEkub(@PathVariable("ekub-id") String ekubId){
        service.leaveEkub(ekubId);
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

    // get list of equbs of a user
    @GetMapping("/equbs/{user-id}")
    public ResponseEntity<List<EkubResponse>> getEkubsOfUser(
            @PathVariable("user-id") String userId
    ){
        return ResponseEntity.ok(service.getEkubsOfUser(userId));
    }

    // get member details
    @GetMapping("/member-detail/{ekub-id}/{version}")
    public ResponseEntity<List<MemberDetailResponse>> getMemberDetail(
            @PathVariable("ekub-id") String ekubId,
            @PathVariable("version") int version
    ){
        return ResponseEntity.ok(service.getMemberDetail(ekubId,version));
    }

}
