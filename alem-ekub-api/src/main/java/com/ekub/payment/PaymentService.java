package com.ekub.payment;

import com.ekub.ekub.Ekub;
import com.ekub.ekub.EkubService;
import com.ekub.ekub_users.EkubUserService;
import com.ekub.round.Round;
import com.ekub.round.RoundResponse;
import com.ekub.round.RoundService;
import com.ekub.user.User;
import com.ekub.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository repository;
    private final PaymentMapper mapper;
    private final UserService userService;
    private final RoundService roundService;
    private final EkubService ekubService;
    private final EkubUserService ekubUserService;

    // create payments
    public void createPayment(PaymentRequest request){
        User user = userService.findUserById(request.userId());
        Round round = roundService.findRoundById(request.roundId());

        repository.save(
            Payment.builder()
                .id(UUID.randomUUID())
                .user(user)
                .round(round)
                .amount(request.amount())
                .penaltyAmount(request.penaltyAmount())
                .createdDate(LocalDateTime.now())
                .build()
        );
    }

    // get payments of round
    public List<PaymentResponse> getRoundPayments(UUID roundId){
      return repository.findByRoundId(roundId)
              .stream()
              .map(mapper::toPaymentResponse)
              .toList();
    }

    //get payment of a round
    public List<PaymentResponse> getLstOfPayment(String ekubId, int roundNumber){
        Ekub ekub = ekubService.findEkubById(ekubId);
        Round round = roundService.getRoundByEkubIdAndRoundNo(ekub.getId(), roundNumber);
        return getRoundPayments(round.getId());
    }

    // get payment of current round
    public List<PaymentResponse> getCurrentRoundPayments(String ekubId){
        Ekub ekub = ekubService.findEkubById(ekubId);
        Round round = ekubService.getCurrentRound(ekub);
        return getRoundPayments(round.getId());
    }


    // pay to a winner
    public void payToAWinner(String ekubId, String userId){
        Ekub ekub = ekubService.findEkubById(ekubId);
        User winner = userService.findUserById(userId);
        // is the user a winner of previous round
        Round round = roundService
                .getRoundByEkubIdAndRoundNo(ekub.getId(), ekub.getRoundNumber() - 1);
        if(round.isPaid() || !round.getWinner().getId().equals(winner.getId())){
            throw new AccessDeniedException("The user is not a winner");
        }

        List<User> members = ekubUserService.getEkubUsers(ekub.getId());
        List<User> winners = ekubUserService.getEKubWinners(ekub.getId());

        //check if user has a guarantor
        User guarantor = winner.getGuarantor();
        if(guarantor != null){
            throw new AccessDeniedException("The user doesn't hava a guarantor");
        }
        if(!members.contains(guarantor)){
            throw new AccessDeniedException("The guarantor is not member of "+ ekub.getName());
        }

        if(winners.contains(guarantor)){
            throw new AccessDeniedException("The guarantor has already won");
        }

//        System.out.println("pay with amount " + ekub.getWinAmount());

        //upate round status to paid
        round.setPaid(true);
        roundService.save(round);
    }

    // get payment status of a user in each round of the ekub
    public List<EkubPaymentStatusResponse> getEkubPaymentStatus(String ekubId){
        Ekub ekub = ekubService.findEkubById(ekubId);
        List<User> members = ekubUserService.getEkubUsers(ekub.getId());
        List<RoundResponse> roundList = roundService.getRoundResByEkubId(ekubId);
        List<User> winnerList = ekubUserService.getEKubWinners(ekub.getId());

        List<EkubPaymentStatusResponse> paymentStatusResList = new ArrayList<>();

        members.forEach(user ->{
            paymentStatusResList.add(
                EkubPaymentStatusResponse.builder()
                    .userId(user.getId())
                    .username(user.getUsername())
                    .hasWon(winnerList.contains(user))
                    .roundPaymentsStatus(
                            getUserPaymentsStatusOnRounds(ekub.getId(),user)
                    )
                    .build()
            );
        });

        return paymentStatusResList;
    }

    //
    public List<Boolean> getUserPaymentsStatusOnRounds(UUID ekubId, User user){
        List<Boolean> status = new ArrayList<>();

        roundService.getRoundsByEkubId(ekubId)
                .stream()
                .sorted(Comparator.comparing(Round::getRoundNumber))
                .forEach(round -> {
                    List<User> userPayedList = getUsersPayedInRound(round.getId());
                    if(userPayedList.contains(user)){
                        status.add(true);
                    } else {
                        status.add(false);
                    }
                }
                );

        return status;
    }

    // get uses who pays on this round
    public List<User> getUsersPayedInRound(UUID roundId){
        return repository.findByRoundId(roundId)
                .stream()
                .map(Payment::getUser)
                .toList();
    }

}
