package com.ekub.ekub_users;

import com.ekub.ekub.Ekub;
import com.ekub.ekub.EkubMapper;
import com.ekub.ekub.EkubService;
import com.ekub.round.RoundRepository;
import com.ekub.user.User;
import com.ekub.user.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class EkubUserService {

    private final EkubUserRepository repository;
    private final UserService userService;
    private final EkubService ekubService;
    private final EkubMapper ekubMapper;
    private final RoundRepository roundRepository;

    // create ekub user
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void createEkubUser(String ekubId, String userId){
        Ekub ekub = ekubService.findEkubByExternalId(ekubId);
        User user = userService.findUserByExId(userId);
        repository.save(
                EkubUser.builder()
                        .ekub(ekub)
                        .user(user)
                        .build()
        );
    }

    // remove user
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void removeUser(String ekubId, String userId){
        this.deleteEkubUser(UUID.fromString(ekubId),userId);
    }


    // delete ekub user
    public void deleteEkubUser(UUID ekubId, String userId) {
        EkubUser ekubuser = this.getEkubUserByEkubAndUser(ekubId,userId);
        repository.delete(ekubuser);
    }

    // get EkubUser by ekub and user
    public EkubUser getEkubUserByEkubAndUser(UUID ekubId, String  userId){
        return repository.findByEkubIdAndUserId(ekubId,userId)
                .orElseThrow(() -> new EntityNotFoundException("EkubUser not found"));
    }


    // get ekub draw participants
    public List<User> findDrawParticipants(int ekubId, int version){
        return repository.findDrawParticipants(ekubId,version);
    }


    // join ekub
    public void joinEkub(String ekubId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loggedInUserId = authentication.getName();
        User user = userService.findUserByExId(loggedInUserId);
        Ekub ekub = ekubService.findEkubByExternalId(ekubId);

        if(ekub.isActive()){
            throw new AccessDeniedException("You Can't Join this Equb");
        }

        repository.save(
                EkubUser.builder()
                        .ekub(ekub)
                        .user(user)
                        .build()
        );
    }

    // leave ekub
    public void leaveEkub(String ekubId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loggedInUserId = authentication.getName();
        Ekub ekub = ekubService.findEkubByExternalId(ekubId);

        if(ekub.isActive()){
            throw new AccessDeniedException("You Can't leave active equb");
        }
        // pending payments
        if(roundRepository.hasPendingPayments(loggedInUserId, ekub.getId())){
            throw new AccessDeniedException("You have pending payments please pay them");
        }

        deleteEkubUser(ekub.getExternalId(),loggedInUserId);
    }

    // is member of ekub
    public boolean isMemberOfEkub(String userId,UUID ekubId){
        return repository.isMemberOfEkub(userId,ekubId);
    }

    // has not won yet
    public boolean hasNotWonYet(String userId,UUID ekubId, int version){
        return repository.hasNotWonYet(userId,ekubId,version);
    }

    // finding member detail
    public List<MemberDetailResponse> getMemberDetail(String ekubId, int version){
        return repository.findMemberDetail(UUID.fromString(ekubId), version);
    }
}
