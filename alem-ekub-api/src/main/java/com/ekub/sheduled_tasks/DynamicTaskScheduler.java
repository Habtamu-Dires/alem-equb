package com.ekub.sheduled_tasks;

import com.ekub.ekub.Ekub;
import com.ekub.ekub.EkubRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class DynamicTaskScheduler {

    private final EkubRepository ekubRepository;
    private final DrawEkubService drawEkubService;

    @Scheduled(cron = "0 1/10 * * * *")
    @Async("cpuBoundExecutor")
    public void taskScheduler(){
        log.info("running dynamic scheduler on {}", Thread.currentThread().getName());
        List<Ekub> activeEkubs = ekubRepository.findActiveEkubs();
        for(Ekub ekub : activeEkubs){
            if(ekub.getNextDrawDateTime() == null) continue;
            if(shouldRun(ekub.getNextDrawDateTime())){
                this.executeTask(ekub);
            }
        }
    }

    // check whether to draw the ekub or not
    private boolean shouldRun(LocalDateTime nextDrawDateTime) {

        LocalDateTime now = LocalDateTime.now();
        // task hasn't started yet
        if(now.isBefore(nextDrawDateTime)){
            return false;
        }

        //check
        return now.isAfter(nextDrawDateTime) || now.isEqual(nextDrawDateTime);

    }

    // execute task
    private void executeTask(Ekub ekub){
        log.info("draw ekub: {}", Thread.currentThread().getName());
        drawEkubService.drawEkub(ekub);
    }
}
