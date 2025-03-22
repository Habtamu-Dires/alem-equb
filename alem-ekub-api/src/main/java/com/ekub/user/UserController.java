package com.ekub.user;

import com.ekub.common.IdResponse;
import com.ekub.common.PageResponse;
import com.ekub.ekub.EkubResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/users")
@Tag(name = "users")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    // create user
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<IdResponse> createUser(
            @RequestPart @Valid UserRequest request,
            @RequestPart MultipartFile profilePic,
            @RequestPart MultipartFile idCardImg
    ) {
        service.createUser(request,profilePic,idCardImg);
        return ResponseEntity.accepted().build();
    }

    // update user
    @PutMapping
    public ResponseEntity<IdResponse> updateUser(
            @RequestBody UserRequest request) {
        return ResponseEntity.ok( service.updateUser(request));
    }

    // get page of users
    @GetMapping
    public ResponseEntity<PageResponse<UserResponse>> getPageOfUsers(
            @RequestParam(value = "page",defaultValue = "0", required = false) int page,
            @RequestParam(value = "size", defaultValue = "10", required = false) int size
    ) {
       return ResponseEntity.ok(service.getPageOfUsers(page,size));
    }

    // get user by id
    @GetMapping("/{user-id}")
    public ResponseEntity<UserResponse> getUserById(
            @PathVariable("user-id") String userId
    ){
        return ResponseEntity.ok(service.getUserByExId(userId));
    }

    // delete user
    @DeleteMapping("/{user-id}")
    public ResponseEntity<Void> deleteUser(@PathVariable("user-id") String userId) {
        service.deleteUser(userId);
        return ResponseEntity.accepted().build();
    }

    // update self user profile
    @PutMapping("/profile")
    public ResponseEntity<Void> updateProfile(
          @Valid  @RequestBody UserRequest request) {
        service.updateProfile(request);
        return ResponseEntity.accepted().build();
    }

    // update self password
    @PutMapping("/password/{user-id}")
    public ResponseEntity<Void> updatePassword(
            @PathVariable("user-id") String userId,
            @Valid  @RequestBody PasswordUpdateRequest request
    ){
        service.updatePassword(userId, request);
        return ResponseEntity.accepted().build();
    }

    // upload profile picture
    @PostMapping(value = "/profile-picture", consumes = "multipart/form-data")
    public ResponseEntity<Void> uploadProfilePicture(
            @RequestParam("user-id") String userId,
            @RequestPart MultipartFile file
    ){
        service.uploadProfilePicture(userId,file);
        return ResponseEntity.accepted().build();
    }

    //upload id card image
    @PostMapping(value = "/id-card-image", consumes = "multipart/form-data")
    public ResponseEntity<Void> uploadIdCardImage(
            @RequestParam("user-id") String userId,
            @RequestPart MultipartFile file
    ){
        service.uploadIdCardImage(userId,file);
        return ResponseEntity.accepted().build();
    }

    // invite a user to ekub
    @PostMapping("/invite/{user-id}")
    public ResponseEntity<Void> inviteUserToEkub(
            @PathVariable("user-id") String userId,
            @RequestParam("ekub-id") String ekubId
    ){
        service.inviteUserToEkub(userId,ekubId);
        return ResponseEntity.accepted().build();
    }

    // cancel invitation
    @PutMapping("/cancel-invitation/{user-id}")
    public ResponseEntity<Void> cancelInvitation(
            @PathVariable("user-id") String userId,
            @RequestParam("ekub-id") String ekubId
    ){
        service.cancelInvitation(userId,ekubId);
        return ResponseEntity.accepted().build();
    }

    // get users invited in ekub and not joined
    @GetMapping("/invited-users/{ekub-id}")
    public ResponseEntity<List<UserResponse>> getInvitedUsersInEkubAndNotJoined(
            @PathVariable("ekub-id") String ekubId
    ){
        return ResponseEntity.ok(service.getUsersInvitedInEkubsAndNotJoined(ekubId));
    }

    //search user to invite into ekub
    @GetMapping("/search-to-invite/{ekub-id}")
    public ResponseEntity<List<UserResponse>> searchUsersToInvite(
            @PathVariable("ekub-id") String ekubId,
            @RequestParam("search-term") String searchTerm
    ){
        return ResponseEntity.ok(service.searchUsersToInvite(ekubId,searchTerm));
    }

    // search user by name
    @GetMapping("/search-by-name/{name}")
    public ResponseEntity<List<UserResponse>> searchByName(
            @PathVariable("name") String name
    ){
        return ResponseEntity.ok(service.searchByName(name));
    }

}
