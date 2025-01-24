package com.ekub.round;

import com.ekub.ekub.Ekub;
import com.ekub.user.UserResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RoundService {

    private final RoundRepository repository;
    private final RoundMapper mapper;

    // create new round
    public Round createNewRound(Ekub ekub){
        LocalDateTime endDate = ekub.getStartDateTime().plusDays(ekub.getFrequencyInDays());
        if(ekub.getLastDrawDateTime() != null){
            endDate = ekub.getLastDrawDateTime().plusDays(ekub.getFrequencyInDays());
        }
        return repository.save(
                Round.builder()
                    .id(UUID.randomUUID())
                    .ekub(ekub)
                    .roundNumber(ekub.getRoundNumber()+1)
                    .createdDate(LocalDateTime.now())
                    .endDate(endDate)
                    .totalAmount(BigDecimal.ZERO)
                    .payments(List.of())
                    .paid(false)
                    .build()
        );
    }

    // get round by id
    public Round findRoundById(String roundId){
        return repository.findById(UUID.fromString(roundId))
                .orElseThrow(() -> new EntityNotFoundException("Round not Found"));
    }

    // get round list by ekbu
    public List<Round> getRoundsByEkubId(UUID ekubId){
        return repository.findByEkubId(ekubId);
    }


    // get round response by ekub
    public List<RoundResponse> getRoundResByEkubId(String ekubId) {
        return this.getRoundsByEkubId(UUID.fromString(ekubId))
                .stream()
                .map(mapper::toRoundResponse)
                .toList();
    }

    // get round by ekub id and round number
    public Round getRoundByEkubIdAndRoundNo(UUID ekubId, Integer roundNumber) {
        return repository.findByEkubIdAndRoundNo(ekubId,roundNumber)
                .orElseThrow(() -> new EntityNotFoundException(String.format(
                        "Round with Ekub id %s and round number %s not found",ekubId,roundNumber
                        ))
                );
    }

    // save round
    public void save(Round round){
        repository.save(round);
    }

}
