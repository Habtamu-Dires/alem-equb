package com.ekub.sheduled_tasks;

import com.ekub.ekub.Ekub;
import com.ekub.ekub.EkubService;
import com.ekub.ekub_users.EkubUserService;
import com.ekub.round.Round;
import com.ekub.round.RoundService;
import com.ekub.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
public class DrawEkubService {

    private final EkubService ekubService;
    private final EkubUserService ekubUserService;
    private final RoundService roundService;

    @Transactional
    public void drawEkub(Ekub ekub){
        //get ekub participants
        List<User> drawParticipants = ekubUserService.findDrawParticipants(ekub.getId(), ekub.getVersion());
        if(drawParticipants.isEmpty()){
            // no participant
            ekub.setActive(false);
            log.info("no participant");
            ekubService.save(ekub);
            return;
        }

        //select random winner from participants
        int randIndex = getRandomValue(drawParticipants.size());
        User winner = drawParticipants.get(randIndex);
        // update the current round
        Round currentRound = roundService.getEkubCurrentRound(ekub);
        currentRound.setWinner(winner);
        roundService.save(currentRound);
        //update last draw date
        ekub.setLastDrawDateTime(LocalDateTime.now());

        // if is last round
        if(drawParticipants.size() == 1) {
            // last round
            log.info("The last round , ekub deactivated");
            ekub.setActive(false);
            ekub.setNextDrawDateTime(null);
        } else { // else continue creating new round
            // ekub.setNextDrawDateTime(LocalDateTime.now().plusDays(ekub.getFrequencyInDays()));
            //TODO: To Be Changed to plusDays instead of plusMinutes
            ekub.setNextDrawDateTime(LocalDateTime.now().plusMinutes(ekub.getFrequencyInDays()));
            // new round
            Round newRound = roundService.createNewRound(ekub);
            ekub.setRoundNumber(newRound.getRoundNumber());
        }
        // save ekub
        ekubService.save(ekub);
    }

    // randomly
    public int getRandomValue(int size){
        Random random = new Random();
        return random.nextInt(size);
    }
}
