package com.ekub.user_guarantee;

import com.ekub.common.BooleanResponse;
import com.ekub.ekub_users.EkubUserService;
import com.ekub.round.Round;
import com.ekub.round.RoundService;
import com.ekub.user.User;
import com.ekub.user.UserMapper;
import com.ekub.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserGuaranteeService {

    private final UserGuaranteeRepository repository;
    private final UserService userService;
    private final RoundService roundService;
    private final EkubUserService ekubUserService;

    // guarantee a user
    @Transactional
    public void guaranteeUser(String roundId, String userId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loggedUserId = authentication.getName();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        User guarantor = userService.findUserByExId(loggedUserId);
        User guaranteed = userService.findUserByExId(userId);
        Round round = roundService.findRoundByExId(roundId);

        // can't guarantee one self
        if(loggedUserId.equalsIgnoreCase(guaranteed.getExternalId())){
            throw new AccessDeniedException("You can't guarantee your self");
        }

        //if admin allow to guarantee
        var adminRole = new SimpleGrantedAuthority("ROLE_ADMIN");
        if(authorities.contains(adminRole)){
            createUserGuarantee(guarantor,guaranteed,round);
            return;
        }
        // is user member of ekub
        boolean isMember = ekubUserService.isMemberOfEkub( guarantor.getExternalId(), round.getEkub().getExternalId());

        //if user  guarantor or guaranteed before
        boolean isGrantorOrGuaranteed = isGuarantorOrGuaranteed(
                guarantor.getExternalId(),
                round.getEkub().getExternalId(),
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
    @Transactional
    public BooleanResponse isAllowedToBeGuarantor(String ekubId, int version){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loggedUserId = authentication.getName();

        boolean hasNotWonYet = ekubUserService.hasNotWonYet(
                loggedUserId,
                UUID.fromString(ekubId),
                version
        );

        boolean isGuarantorOrGuaranteedBefore = isGuarantorOrGuaranteed(
                loggedUserId,
                UUID.fromString(ekubId),
                version
        );

        boolean res = !isGuarantorOrGuaranteedBefore && hasNotWonYet;
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
                    .guarantor(guarantor)
                    .guaranteed(guaranteed)
                    .round(round)
                    .build()
        );
    }

    // delete a guarantee
    public void deleteGuarantee(int roundId,String guarantorId, String guaranteedId){
        repository.deleteUserGuarantee(roundId,guarantorId,guaranteedId);
    }

    // cancel guarantee
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void cancelGuarantee(String roundId,String guarantorId, String guaranteedId){
        Round round = roundService.findRoundByExId(roundId);

        if(round.isPaid()){
            throw new AccessDeniedException("You're not allowed");
        } else {
            // delete table
            deleteGuarantee(round.getId(),guarantorId,guaranteedId);
        }

    }

}
