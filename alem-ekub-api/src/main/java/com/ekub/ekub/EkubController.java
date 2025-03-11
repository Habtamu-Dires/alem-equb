package com.ekub.ekub;

import com.ekub.common.PageResponse;
import com.ekub.round.RoundResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ekubs")
@Tag(name = "ekubs")
@RequiredArgsConstructor
public class EkubController {

    private final EkubService service;

    // create new ekub
    @PostMapping
    public ResponseEntity<Void> createEkub(
            @RequestBody @Valid EkubRequest ekubRequest
    ) {
        service.createEkub(ekubRequest);
        return ResponseEntity.accepted().build();
    }

    // update ekub
    @PutMapping
    public ResponseEntity<Void> updateEkub(
            @RequestBody @Valid EkubRequest request
    ){
        service.updateEkubInfo(request);
        return ResponseEntity.accepted().build();
    }

    // get page of ekub
    @GetMapping
    public ResponseEntity<PageResponse<EkubResponse>> getPageOfEkubs(
            @RequestParam(value = "page",defaultValue = "0", required = false) int page,
            @RequestParam(value = "size", defaultValue = "10", required = false) int size
    ){
        return ResponseEntity.ok(service.getPageOfEkubs(page,size));
    }

    //get public not active ekubs
    @GetMapping("/public")
    private ResponseEntity<List<EkubResponse>> getPublicEkubs(){
        return ResponseEntity.ok(service.getPublicEkubsToJoin());
    }

    // get ekub by id
    @GetMapping("/{ekub-id}")
    public ResponseEntity<EkubResponse> getEkubById(
            @PathVariable("ekub-id") String ekubId
    ){
        return ResponseEntity.ok(service.getEkubById(ekubId));
    }

    // get list of equbs of a user
    @GetMapping("/equbs/{user-id}")
    public ResponseEntity<List<EkubResponse>> getEkubsOfUser(
            @PathVariable("user-id") String userId
    ){
        return ResponseEntity.ok(service.getEkubsOfUser(userId));
    }


    // delete ekub id
    @DeleteMapping("/{ekub-id}")
    public ResponseEntity<Void> deleteEkub(
            @PathVariable("ekub-id") String ekubId
    ){
        service.deleteEkub(ekubId);
        return ResponseEntity.accepted().build();
    }

    // search ekbu by name
    @GetMapping("/search/by-name")
    public ResponseEntity<List<EkubResponse>> searchEkubByName(
            @RequestParam("ekub-name") String ekubName
    ){
        return ResponseEntity.ok(service.searchEkubByName(ekubName));
    }

    //find invited ekubs yet to join
    @GetMapping("/invited-ekubs")
    public ResponseEntity<List<EkubResponse>> getInvitedEkubsYetToJoin(){
        return ResponseEntity.ok(service.getInvitedEkubsYetToJoin());
    }

    //get ekub status
    @GetMapping("/ekub-status/{ekub-id}/{version}")
    public ResponseEntity<EkubStatusResponse> getEkubStatus(
            @PathVariable("ekub-id") String ekubId,
            @PathVariable("version") int version
    ){
        return ResponseEntity.ok(service.getEkubStatus(ekubId,version));
    }

}
