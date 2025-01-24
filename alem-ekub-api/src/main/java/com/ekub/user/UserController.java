package com.ekub.user;

import com.ekub.common.IdResponse;
import com.ekub.common.PageResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/users")
@Tag(name = "users")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    // create user
    @PostMapping
    public ResponseEntity<IdResponse> createUser(@RequestBody @Valid UserRequest request) {
        return ResponseEntity.ok(service.createUser(request));
    }

    // list page of users
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
        return ResponseEntity.ok(service.getUserById(userId));
    }

    // update user
    @PutMapping
    public ResponseEntity<IdResponse> updateUser(
            @RequestBody UserRequest request) {
        return ResponseEntity.ok( service.updateUser(request));
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

    // delete profile picture . is it necessary ?
    @DeleteMapping("/profile-picture")
    public ResponseEntity<Void> removeProfilePicture(
            @RequestParam("user-id") String userId
    ){
        service.removeProfilePicture(userId);
        return ResponseEntity.accepted().build();
    }
}
