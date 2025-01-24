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
    private final DrawEkub drawEkub;

    @Scheduled(cron = "0 0 1 * * *") // every day at 1 Am
    public void taskScheduler(){
        List<Ekub> activeEkubs = ekubRepository.findActiveEkubs();
        for(Ekub ekub : activeEkubs){
            LocalDateTime startDate = ekub.getStartDateTime();
            if(startDate == null) continue;
            if(shouldRun(startDate, ekub.getLastDrawDateTime(), ekub.getFrequencyInDays())){
                this.executeTask(ekub);
            }

        }
    }
    // check whether to draw the task
    private boolean shouldRun(LocalDateTime startDate, LocalDateTime lastRunDate, int frequencyInDays){
        LocalDateTime now = LocalDateTime.now();
        // task hasn't started yet
        if(now.isBefore(startDate)){
            return false;
        }
        // if it is the first run
        if(lastRunDate == null){
            return true;
        }

        // Calculate the next expected run time
        LocalDateTime nextRun = lastRunDate.plusDays(frequencyInDays);
        return now.isAfter(nextRun) || now.isEqual(nextRun);
    }

    private void executeTask(Ekub ekub){
        System.out.println("draw ekub");
        drawEkub.drawEkub(ekub);
    }
}
