package com.ekub.user;

import com.ekub.common.IdResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/registration")
@Tag(name = "registration")
@RequiredArgsConstructor
public class RegistrationController {

    private final UserService userService;

    // register
    @PostMapping(value = "/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<IdResponse> register(
            @RequestPart @Valid UserRequest request,
            @RequestPart MultipartFile profilePic,
            @RequestPart MultipartFile idCardImg

    ) {
        userService.register(request,profilePic,idCardImg);
        return ResponseEntity.accepted().build();
    }

}
