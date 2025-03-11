package com.ekub.round;

import com.ekub.ekub.Ekub;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RoundService {

    private final RoundRepository roundRepository;
    private final RoundMapper mapper;

    // create new round
    public Round createNewRound(Ekub ekub){
        LocalDateTime endDateTime = ekub.getNextDrawDateTime();
        return roundRepository.save(
                Round.builder()
                    .ekub(ekub)
                    .version(ekub.getVersion())
                    .roundNumber(ekub.getRoundNumber()+1)
                    .createdDateTime(LocalDateTime.now())
                    .endDateTime(endDateTime)
                    .totalAmount(BigDecimal.ZERO)
                    .payments(List.of())
                    .paid(false)
                    .build()
        );
    }

    // find round by id
    public Round findRoundByExId(String roundId){
        return roundRepository.findByExternalId(UUID.fromString(roundId))
                .orElseThrow(() -> new EntityNotFoundException("Round not Found"));
    }

    public Round findRoundById(int roundId){
        return roundRepository.findById(roundId)
                .orElseThrow(() -> new EntityNotFoundException("Round not Found"));
    }

    // get round response by ekub
    public List<RoundResponse> getRoundResByEkub(String ekubId, int version) {
        return roundRepository.findByEkub(UUID.fromString(ekubId),version)
                .stream()
                .sorted(Comparator.comparing(Round::getRoundNumber))
                .map(mapper::toRoundResponse)
                .toList();
    }

    // get round by ekub id and round number
    public Round findRoundByEkubAndRoundNo(int ekubId, int version, int roundNumber) {
        return roundRepository.findByEkubAndRoundNo(ekubId,version,roundNumber)
                .orElseThrow(() -> new EntityNotFoundException(
                    String.format(
                     "Round with Ekub id %s and round number %s not found",ekubId,roundNumber
                    ))
                );
    }

    // get round response by ekubId, version and round-no
    public LastRoundResponse getLastRound(String ekubId, int version, int roundNo) {
        return roundRepository.findLastRound(UUID.fromString(ekubId),version,roundNo)
                .orElse(null);
    }

    //get current round of ekub
    public Round getEkubCurrentRound(Ekub ekub){
        return findRoundByEkubAndRoundNo(
                ekub.getId(),
                ekub.getVersion(),
                ekub.getRoundNumber()
        );
    }

    // save round
    public void save(Round round){
        roundRepository.save(round);
    }

    //get user pending payments
    public List<UserPendingPaymentResponse> getUserPendingPayments(String userId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loggedUserId = authentication.getName();

        LocalDateTime now = LocalDateTime.now();

        List<UserPendingPaymentDTO> unPaidPayments = roundRepository
                .findUserPendingPayments(loggedUserId)
                .stream()
                .sorted(Comparator.comparing(UserPendingPaymentDTO::endDateTime))
                .toList();

        List<UserPendingPaymentResponse> responses = new ArrayList<>();

        for (UserPendingPaymentDTO payment : unPaidPayments){
            if(payment.endDateTime().isBefore(now)){
                double penaltyPercent = payment.penaltyPercentPerDay();

                long daysOverdue = Duration.between(payment.endDateTime(), now).toDays();

                BigDecimal dailyPenaltyRate = new BigDecimal(penaltyPercent/100);
                BigDecimal penaltyRate = dailyPenaltyRate.multiply(new BigDecimal(daysOverdue + 1)); // +1 to include the first overdue day
                BigDecimal penalty = payment.amount().multiply(penaltyRate);

                BigDecimal totalAmount = payment.amount().add(penalty);
                responses.add(
                    UserPendingPaymentResponse.builder()
                        .equbName(payment.equbName())
                        .version(payment.version())
                        .roundId(payment.roundId())
                        .roundNumber(payment.roundNumber())
                        .amount(payment.amount())
                        .penalty(penalty)
                        .totalAmount(totalAmount)
                        .endDateTime(payment.endDateTime())
                        .build()
                );

            } else {
                responses.add(
                    UserPendingPaymentResponse.builder()
                            .equbName(payment.equbName())
                            .version(payment.version())
                            .roundId(payment.roundId())
                            .roundNumber(payment.roundNumber())
                            .amount(payment.amount())
                            .penalty(BigDecimal.ZERO)
                            .totalAmount(payment.amount())
                            .endDateTime(payment.endDateTime())
                            .build()
                );
            }
        }
        return responses;
    }

    // set payment status
    public void setPaymentStatusToPaid(Round round) {
        round.setPaid(true);
        roundRepository.save(round);
    }
}
