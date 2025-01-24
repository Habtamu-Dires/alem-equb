package com.ekub.sheduled_tasks;

import com.ekub.ekub.Ekub;
import com.ekub.ekub.EkubService;
import com.ekub.ekub_users.EkubUserService;
import com.ekub.round.Round;
import com.ekub.round.RoundService;
import com.ekub.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class DrawEkub {

    private final EkubService ekubService;
    private final EkubUserService ekubUserService;
    private final RoundService roundService;

    @Transactional
    public void drawEkub(Ekub ekub){
        //select random winner from participants
        List<User> ekubParticipants = ekubUserService.getEkubParticipants(ekub.getId());
        int randVal = getRandomValue(ekubParticipants.size());
        User winner = ekubParticipants.get(randVal);
        // update the current round
        Round currentRound = ekubService.getCurrentRound(ekub);
        currentRound.setWinner(winner);
        roundService.save(currentRound);

        //update last run date
        ekub.setLastDrawDateTime(LocalDateTime.now());

        //create new round
        Round newRound = roundService.createNewRound(ekub);
        ekub.setRoundNumber(newRound.getRoundNumber());
        // save ekub
        ekubService.save(ekub);
    }

    // randomly
    public int getRandomValue(int size){
        Random random = new Random();
        return random.nextInt(size);
    }
}
