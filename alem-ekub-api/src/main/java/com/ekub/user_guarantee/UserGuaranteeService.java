package com.ekub.user_guarantee;

import com.ekub.common.BooleanResponse;
import com.ekub.ekub_users.EkubUserService;
import com.ekub.round.Round;
import com.ekub.round.RoundService;
import com.ekub.user.User;
import com.ekub.user.UserMapper;
import com.ekub.user.UserResponse;
import com.ekub.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserGuaranteeService {

    private final UserGuaranteeRepository repository;
    private final UserService userService;
    private final RoundService roundService;
    private final UserMapper userMapper;
    private final EkubUserService ekubUserService;

    // guarantee a user
    public void guaranteeUser(String roundId, String userId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loggedUserId = authentication.getName();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        User guarantor = userService.findUserById(loggedUserId);
        User guaranteed = userService.findUserById(userId);
        Round round = roundService.findRoundById(roundId);

        // can't guarantee one self
        if(loggedUserId.equalsIgnoreCase(guaranteed.getId())){
            throw new AccessDeniedException("You can't guarantee your self");
        }

        //if admin allow to guarantee
        var adminRole = new SimpleGrantedAuthority("ROLE_ADMIN");
        if(authorities.contains(adminRole)){
            createUserGuarantee(guarantor,guaranteed,round);
            return;
        }
        // is user member of ekub
        boolean isMember = ekubUserService.isMemberOfEkub( guarantor.getId(), round.getEkub().getId());

        //if user  guarantor or guaranteed before
        boolean isGrantorOrGuaranteed = isGuarantorOrGuaranteed(
                guarantor.getId(),
                round.getEkub().getId(),
                round.getVersion()
        );

        if(isMember && !isGrantorOrGuaranteed){
            createUserGuarantee(guarantor,guaranteed,round);
        } else {
            throw new AccessDeniedException("You are not allowed to guarantee");
        }
    }

    // is a user a guarantor or guaranteed in this version
    public boolean isGuarantorOrGuaranteed(String guarantorId, UUID ekubId, int version){
        return repository.isGuarantorOrGuaranteed(guarantorId, ekubId, version);
    }

    // is allowed to be guarantor in this version
    public BooleanResponse isAllowedToBeGuarantor(String ekubId, int version){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loggedUserId = authentication.getName();

        boolean hasNotWonYet = ekubUserService.hasNotWonYet(
                loggedUserId,
                UUID.fromString(ekubId),
                version
        );

        if(!hasNotWonYet){
            System.out.println("You already Win ");
        } else {
            System.out.println("you has not won yet on version " + version  );
        }

        boolean isMember = ekubUserService.isMemberOfEkub(loggedUserId, UUID.fromString(ekubId));
        if(!isMember){
            System.out.println("you're not member");
        }
        boolean isGuarantorOrGuaranteedBefore = isGuarantorOrGuaranteed(
                loggedUserId,
                UUID.fromString(ekubId),
                version);
        if(isGuarantorOrGuaranteedBefore){
            System.out.println("you were guarantor or guaranteed in this ekub on version: " + version);
        }


        boolean res = isMember && !isGuarantorOrGuaranteedBefore && hasNotWonYet;
        return new BooleanResponse(res);
    }

    // create userGuarantee
    public void createUserGuarantee(
            User guarantor,
            User guaranteed,
            Round round
    ) {
        repository.save(
            UserGuarantee.builder()
                    .id(UUID.randomUUID())
                    .guarantor(guarantor)
                    .guaranteed(guaranteed)
                    .round(round)
                    .build()
        );
    }

    // delete a guarantee
    public void deleteGuarantee(UUID roundId,String guarantorId, String guaranteedId){
        repository.deleteUserGuarantee(roundId,guarantorId,guaranteedId);
    }

    // cancel guarantee
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void cancelGuarantee(String roundId,String guarantorId, String guaranteedId){
        Round round = roundService.findRoundById(roundId);

        if(round.isPaid()){
            throw new AccessDeniedException("You're not allowed");
        } else {
            // delete table
            deleteGuarantee(round.getId(),guarantorId,guaranteedId);
        }

    }

    // get guarantors for a round
    public List<UserResponse> getGuarantors(String roundId, String userId){
        User user = userService.findUserById(userId);
        return repository.findGuarantors(userId,UUID.fromString(roundId))
                .stream()
                .map(userMapper::toUserResponse)
                .toList();
    }


    // get guaranteed users for a round
    public List<UserResponse> getGuaranteedUsers(String roundId, String userId){
        User user = userService.findUserById(userId);
        return repository.findGuaranteedUsers(userId, UUID.fromString(roundId))
                .stream()
                .map(userMapper::toUserResponse)
                .toList();
    }

    // find UserGuarantee
    public List<UserGuaranteeResponse> findUserGuaranteeByRound(UUID roundId){
        return repository.findByRound(roundId);
    }
}
