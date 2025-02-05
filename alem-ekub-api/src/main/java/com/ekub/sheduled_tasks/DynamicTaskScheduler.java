package com.ekub.sheduled_tasks;

import com.ekub.ekub.Ekub;
import com.ekub.ekub.EkubRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DynamicTaskScheduler {

    private final EkubRepository ekubRepository;
    private final DrawEkubService drawEkubService;

    @Scheduled(cron = "0 1/5 * * * *") // every day at 1 Am
    public void taskScheduler(){
        System.out.println("Running dynamic Scheduler");
        List<Ekub> activeEkubs = ekubRepository.findActiveEkubs();
        for(Ekub ekub : activeEkubs){
            if(ekub.getNextDrawDateTime() == null) continue;
            if(shouldRun(
                    ekub.getLastDrawDateTime(),
                    ekub.getNextDrawDateTime())
            ){
                this.executeTask(ekub);
            }
        }
    }

    // check whether to draw the ekub
    private boolean shouldRun(
            LocalDateTime lastDrawDateTime,
            LocalDateTime nextDrawDateTime
    ) {

        LocalDateTime now = LocalDateTime.now();
        // task hasn't started yet
        if(now.isBefore(nextDrawDateTime)){
            return false;
        }
        // if it is the first run
        if(lastDrawDateTime == null){
            return true;
        }

        //check
        return now.isAfter(nextDrawDateTime) || now.isEqual(nextDrawDateTime);

    }

    private void executeTask(Ekub ekub){
        System.out.println("draw ekub");
        drawEkubService.drawEkub(ekub);
    }
}
