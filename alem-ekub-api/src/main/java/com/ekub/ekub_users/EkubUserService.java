package com.ekub.ekub_users;

import com.ekub.ekub.Ekub;
import com.ekub.ekub.EkubMapper;
import com.ekub.ekub.EkubResponse;
import com.ekub.ekub.EkubService;
import com.ekub.round.Round;
import com.ekub.round.RoundService;
import com.ekub.user.User;
import com.ekub.user.UserMapper;
import com.ekub.user.UserResponse;
import com.ekub.user.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EkubUserService {

    private final EkubUserRepository repository;
    private final UserService userService;
    private final EkubService ekubService;
    private final UserMapper userMapper;
    private final RoundService roundService;
    private final EkubMapper ekubMapper;

    // create ekub user
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void createEkubUser(String ekubId, String userId){
        Ekub ekub = ekubService.findEkubById(ekubId);
        User user = userService.findUserById(userId);
        repository.save(
                EkubUser.builder()
                        .id(UUID.randomUUID())
                        .ekub(ekub)
                        .user(user)
                        .build()
        );
    }

    // remove user
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void removeUser(String ekubId, String userId){
        Ekub ekub = ekubService.findEkubById(ekubId);
        User user = userService.findUserById(userId);
        this.deleteEkubUser(ekub.getId(),user.getId());
    }

    // get list of ekub users
    public List<UserResponse> getEkubUserResponses(String ekubId){
        Ekub ekub = ekubService.findEkubById(ekubId);
        return this.getEkubUsers(ekub.getId())
                .stream()
                .map(userMapper::toUserResponse)
                .toList();
    }
    // ekub users
    public List<User> getEkubUsers(UUID ekubId) {
        return repository.findByEkubId(ekubId)
                .stream()
                .map(EkubUser::getUser)
                .toList();
    }

    // delete ekub user
    public void deleteEkubUser(UUID ekubId, String userId) {
        EkubUser ekubuser = this.getEkubUserByEkubAndUser(ekubId,userId);
        repository.delete(ekubuser);
    }

    // get EkubUser user by ekub and user
    public EkubUser getEkubUserByEkubAndUser(UUID ekubId, String  userId){
        return repository.findByEkubIdAndUserId(ekubId,userId)
                .orElseThrow(() -> new EntityNotFoundException("Ekub and/or User not found"));
    }


    // winners of the ekub.
    public List<User> getEKubWinners(UUID ekubId){
        return roundService.getRoundsByEkubId(ekubId).stream()
                .map(Round::getWinner)
                .toList();
    }

    // userResponses list of winners
    public List<UserResponse> getEkubWinnerResponses(String ekubId){
        Ekub ekub = ekubService.findEkubById(ekubId);

        return getEKubWinners(ekub.getId())
                 .stream()
                 .map(userMapper::toUserResponse)
                 .toList();
    }

    // get ekub draw participants
    public List<User> getEkubParticipants(UUID ekubId){
        List<User> winners = this.getEKubWinners(ekubId);
        return this.getEkubUsers(ekubId)
                .stream()
                .filter(user -> !winners.contains(user))
                .toList();
    }

    // UserResponse list of participants
    public List<UserResponse> getEkubDrawParticipantResponses(String ekubId){
        Ekub ekub = ekubService.findEkubById(ekubId);
        return getEkubParticipants(ekub.getId())
                .stream()
                .map(userMapper::toUserResponse)
                .toList();
    }

    // get list of ekubs for a user
    public List<EkubResponse> getEkubsOfUser(String userId){
        User user = userService.findUserById(userId);
        return repository.findByUserId(user.getId())
                .stream()
                .map(EkubUser::getEkub)
                .map(ekubMapper::toEkubResponse)
                .toList();
    }

    //get round winner
    public UserResponse getRoundWinner(String ekubId, int roundNumber) {
        Ekub ekub = ekubService.findEkubById(ekubId);
        Round round = roundService.getRoundByEkubIdAndRoundNo(ekub.getId(), roundNumber);
        return userMapper.toUserResponse(round.getWinner());
    }
}
