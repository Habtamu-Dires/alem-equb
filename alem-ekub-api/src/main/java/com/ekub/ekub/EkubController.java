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
        System.out.println("we hre ?");
        service.createEkub(ekubRequest);
        return ResponseEntity.accepted().build();
    }

    // get page of ekbu
    @GetMapping
    public ResponseEntity<PageResponse<EkubResponse>> getPageOfEkubs(
            @RequestParam(value = "page",defaultValue = "0", required = false) int page,
            @RequestParam(value = "size", defaultValue = "10", required = false) int size
    ){
        return ResponseEntity.ok(service.getPageOfEkubs(page,size));
    }

    // get ekub by id
    @GetMapping("/{ekub-id}")
    public ResponseEntity<EkubResponse> getEkubById(
            @PathVariable("ekub-id") String ekubId
    ){
        return ResponseEntity.ok(service.getEkubById(ekubId));
    }

    // get current round of ekub
    @GetMapping("/current-round/{ekub-id}")
    public ResponseEntity<RoundResponse> getCurrentRound(
            @PathVariable("ekub-id") String ekubId
    ){
        return ResponseEntity.ok(service.getCurrentRound(ekubId));
    }

    // update ekub
    @PutMapping
    public ResponseEntity<Void> updateEkub(
            @RequestBody @Valid EkubRequest request
    ){
        service.updateEkubInfo(request);
        return ResponseEntity.accepted().build();
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
}
