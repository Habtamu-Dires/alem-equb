package com.ekub.user;

import com.ekub.common.IdResponse;
import com.ekub.common.PageResponse;
import com.ekub.ekub.Ekub;
import com.ekub.ekub.EkubService;
import com.ekub.ekub_users.EkubUser;
import com.ekub.file.FileStorageService;
import com.ekub.keycloak.KeycloakService;
import com.ekub.keycloak.KeycloakUserRequest;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final KeycloakService keycloakService;
    private final UserRepository repository;
    private final UserMapper mapper;
    private final EkubService ekubService;
    private final FileStorageService fileStorageService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Transactional
    public void createUser(UserRequest request,
                           MultipartFile profilePic,
                           MultipartFile idCardImg) {
        String keyCloakUserId = null;
        User savedUser = null;
        try{
            KeycloakUserRequest keycloakUserRequest = KeycloakUserRequest.builder()
                    .username(request.username())
                    .password(request.password())
                    .firstName(request.firstName())
                    .lastName(request.lastName())
                    .email(request.email())
                    .phoneNumber(request.phoneNumber())
                    .enabled(request.enabled())
                    .build();
            //save to keycloak
            keyCloakUserId = keycloakService.createUser(keycloakUserRequest);

            User user = User.builder()
                    .id(keyCloakUserId)
                    .username(request.username())
                    .firstName(request.firstName())
                    .lastName(request.lastName())
                    .email(request.email())
                    .phoneNumber(request.phoneNumber())
                    .profession(request.profession())
                    .enabled(request.enabled())
                    .build();

           // save to database
            user.setEkubUsers(createEkubUsers(request.ekubIds(),user));
            savedUser = repository.save(user);
            repository.flush();

            // save profile picture and id image.
            uploadProfilePicture(savedUser.getId(), profilePic);
            uploadIdCardImage(savedUser.getId(), idCardImg);

        } catch (Exception e){
            // RollBack in keycloak if database operation failed
            if(keyCloakUserId != null){
                try{
                    keycloakService.deleteUser(keyCloakUserId);
                } catch (Exception ex){
                    throw new RuntimeException("Failed to rollback Keycloak user creation after database failure", ex);
                }
            }
            //Roll back database if file upload failed
            if(savedUser != null){
                try{
                    //delete profile pic
                    if(savedUser.getProfilePicUrl() != null && !savedUser.getProfilePicUrl().isBlank()){
                        fileStorageService.deleteFile(savedUser.getProfilePicUrl());
                    }
                    //delete id card image
                    if(savedUser.getIdCardImageUrl() != null && !savedUser.getIdCardImageUrl().isBlank()){
                        fileStorageService.deleteFile(savedUser.getIdCardImageUrl());
                    }
                    //delete user
                    repository.deleteById(savedUser.getId());
                } catch (Exception ex){
                    throw new RuntimeException("Failed to rollback database after file failed", ex);
                }
            }

            throw new RuntimeException("Something went wrong: "
                    + e.getMessage());
        }

    }

    // get ekubUsers
    public List<EkubUser> createEkubUsers(List<String> ekubIds, User user){
        List<EkubUser> ekubUserList = new ArrayList<>();
        ekubIds.forEach(ekubId -> {
            Ekub ekub = ekubService.findEkubById(ekubId);
            EkubUser ekubUser = EkubUser.builder()
                    .id(UUID.randomUUID())
                    .user(user)
                    .ekub(ekub)
                    .build();
            ekubUserList.add(ekubUser);
        });

        return ekubUserList;
    }


    // registration
    public void register(UserRequest request,
                               MultipartFile profilePic,
                               MultipartFile idCardImg)
    {
        String keyCloakUserId = null;
        User savedUser = null;
        try{
            KeycloakUserRequest keycloakUserRequest = KeycloakUserRequest.builder()
                    .username(request.username())
                    .password(request.password())
                    .firstName(request.firstName())
                    .lastName(request.lastName())
                    .phoneNumber(request.phoneNumber())
                    .email(request.email())
                    .build();
            //save to keycloak
            keyCloakUserId = keycloakService.registration(keycloakUserRequest);

            User user = User.builder()
                    .id(keyCloakUserId)
                    .username(request.username())
                    .firstName(request.firstName())
                    .lastName(request.lastName())
                    .email(request.email())
                    .phoneNumber(request.phoneNumber())
                    .profession(request.profession())
                    .build();

            // save to database
            savedUser = repository.save(user);
            repository.flush();

            // save profile picture and id image.
            uploadProfilePicture(savedUser.getId(), profilePic);
            uploadIdCardImage(savedUser.getId(), idCardImg);

        } catch (Exception e){
            // RollBack in keycloak if database operation failed
            if(keyCloakUserId != null){
                try{
                    keycloakService.deleteUser(keyCloakUserId);
                } catch (Exception ex){
                    throw new RuntimeException("Failed to rollback Keycloak user creation after database failure", ex);
                }
            }
            //Roll back database if file upload failed
            if(savedUser != null){
                try{
                    //delete profile pic
                    if(savedUser.getProfilePicUrl() != null && !savedUser.getProfilePicUrl().isBlank()){
                        fileStorageService.deleteFile(savedUser.getProfilePicUrl());
                    }
                    //delete id card image
                    if(savedUser.getIdCardImageUrl() != null && !savedUser.getIdCardImageUrl().isBlank()){
                        fileStorageService.deleteFile(savedUser.getIdCardImageUrl());
                    }
                    //delete user
                    repository.deleteById(savedUser.getId());
                } catch (Exception ex){
                    throw new RuntimeException("Failed to rollback database after file failed", ex);
                }
            }

            throw new RuntimeException("Something went wrong: "
                    + e.getMessage());
        }
    }



    // get list of users
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public PageResponse<UserResponse> getPageOfUsers(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<User> res = repository.findAll(pageable);

        List<UserResponse> userResponseList = res.map(mapper::toUserResponse).toList();

        return PageResponse.<UserResponse>builder()
                .content(userResponseList)
                .totalElements(res.getTotalElements())
                .numberOfElements(res.getNumberOfElements())
                .totalPages(res.getTotalPages())
                .size(res.getSize())
                .number(res.getNumber())
                .first(res.isFirst())
                .last(res.isLast())
                .empty(res.isEmpty())
                .build();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public IdResponse updateUser(@Valid UserRequest request) {
        User user = findUserById(request.id());
        try{
            KeycloakUserRequest keycloakUserRequest = KeycloakUserRequest.builder()
                    .username(request.username())
                    .firstName(request.firstName())
                    .lastName(request.lastName())
                    .email(request.email())
                    .phoneNumber(request.phoneNumber())
                    .enabled(request.enabled())
                    .build();
            keycloakService.updateUser(user.getId(), keycloakUserRequest);
        } catch (Exception e){
            throw new RuntimeException("Keycloak user update failed: " + e.getMessage(), e);
        }

        try{
            user.setFirstName(request.firstName());
            user.setLastName(request.lastName());
            user.setEmail(request.email());
            user.setPhoneNumber(request.phoneNumber());
            user.setProfession(request.profession());
            user.setEnabled(request.enabled());
            user.setRemark(request.remark());

            User savedUser = repository.save(user);
            return new IdResponse(savedUser.getId());
        } catch (Exception e) {
            //roll back the keycloak update
            KeycloakUserRequest keycloakUserRequest = KeycloakUserRequest.builder()
                    .firstName(user.getFirstName())
                    .lastName(user.getLastName())
                    .email(user.getEmail())
                    .phoneNumber(user.getPhoneNumber())
                    .enabled(user.isEnabled())
                    .build();
            keycloakService.updateUser(user.getId(), keycloakUserRequest);
            throw new RuntimeException("Update not successful");
        }

    }

    // update personal info
    public void updateProfile(@Valid UserRequest request) {
        User user = findUserById(request.id());
        try{
            KeycloakUserRequest keycloakUserRequest = KeycloakUserRequest.builder()
                    .username(request.username())
                    .phoneNumber(request.phoneNumber())
                    .email(request.email())
                    .build();
            keycloakService.updateProfile(user.getId(), keycloakUserRequest);
        } catch (Exception e){
            throw new RuntimeException("Keycloak user update failed: " + e.getMessage(), e);
        }

        try{
            user.setUsername(request.username());
            user.setEmail(request.email());
            user.setPhoneNumber(request.phoneNumber());
            user.setProfession(request.profession());

            repository.save(user);
        } catch (Exception e) {
            //roll back the keycloak update
            KeycloakUserRequest keycloakUserRequest = KeycloakUserRequest.builder()
                    .username(user.getUsername())
                    .email(user.getEmail())
                    .build();
            keycloakService.updateProfile(user.getId(), keycloakUserRequest);
            throw new RuntimeException("Update not successful");
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void deleteUser(String userId) {
        User user = findUserById(userId);
        try{
            keycloakService.deleteUser(userId);
        } catch (Exception e){
            throw new RuntimeException("Keycloak user deletion failed: " + e.getMessage(), e);
        }

        try{
            repository.deleteById(userId);
        } catch (Exception e) {
            //roll back the keycloak deletion
            KeycloakUserRequest keycloakUserRequest = KeycloakUserRequest.builder()
                    .firstName(user.getFirstName())
                    .lastName(user.getLastName())
                    .email(user.getEmail())
                    .enabled(user.isEnabled())
                    .build();
            keycloakService.createUser(keycloakUserRequest); // create again
            throw  new RuntimeException("Database deletion failed " + e.getMessage());
        }
    }

    // update password
    public void updatePassword(String userId, PasswordUpdateRequest request){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loggedInUserId = authentication.getName();

        // check if logged user is the owner
        if (!loggedInUserId.equals(userId)) {
            throw new AccessDeniedException("You can not update this password.");
        }
        // login with the old password to confirm it
        keycloakService.login(request.username(),request.oldPassword());

        // update a password
        keycloakService.updatePassword(userId,request.newPassword());

        // logout of all devices
        keycloakService.logoutUserFromAllDevices(userId);
    }


    // find user by id
    public User findUserById(String userId){
        return repository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

    // get user response by id
    public UserResponse getUserById(String userId) {
        return mapper.toUserResponse(findUserById(userId));
    }


    // upload profile picture
    public void uploadProfilePicture(String userId, MultipartFile file) {
        User user = this.findUserById(userId);
        if(user.getProfilePicUrl() != null && !user.getProfilePicUrl().isBlank()){
            fileStorageService.deleteFile(user.getProfilePicUrl());
        }
        String url = fileStorageService.saveFile(file, userId, "profile");

        user.setProfilePicUrl(url);
        repository.save(user);
    }

    // upload id card
    public void uploadIdCardImage(String userId, MultipartFile file) {
        User user = this.findUserById(userId);
        if(user.getIdCardImageUrl() != null && !user.getIdCardImageUrl().isBlank()){
            fileStorageService.deleteFile(user.getIdCardImageUrl());
        }
        String url = fileStorageService.saveFile(file, userId, "id-card");

        user.setIdCardImageUrl(url);
        repository.save(user);
    }


    // invite a user into ekub
    @Transactional
    public void inviteUserToEkub(String userId, String ekubId){
        User user = findUserById(userId);
        Ekub ekub = ekubService.findEkubById(ekubId);

        user.addInvitedEkub(ekub); // helper method
        repository.save(user);
    }

    // cancel invitation of user
    @Transactional // it will commit automatically thanks to transaction
    public void cancelInvitation(String userId, String ekubId){
        User user = findUserById(userId);
        Ekub ekub = ekubService.findEkubById(ekubId);

        user.removeInvitedEkub(ekub);

        // the transaction commit automatically thanks to Transactional
    }

    // get users invited in ekub but not joined
    public List<UserResponse> getUsersInvitedInEkubsAndNotJoined(String ekubId){
        return  repository
                .findUsersInvitedInEkubAndNotJoined(UUID.fromString(ekubId))
                .stream()
                .map(mapper::toUserResponse)
                .toList();
    }

    //search user by username or firstName
    public List<UserResponse> searchByName(String name){
        Specification<User> spec = UserSpecification.searchUserByName(name);

        return repository.findAll(spec)
                .stream()
                .map(mapper::toUserResponse)
                .toList();
    }

    // search user to invite into ekub
    public List<UserResponse> searchUsersToInvite(String ekubId, String searchTerm) {
        Ekub ekub = ekubService.findEkubById(ekubId);

        Specification<User> spec = UserSpecification
                    .searchUsersNotInEkubOrInvited(searchTerm, ekub.getId());

        return repository.findAll(spec)
                .stream()
                .map(mapper::toUserResponse)
                .toList();
    }


}
