package com.ekub.payment;

import com.ekub.common.PageResponse;
import com.ekub.ekub.Ekub;
import com.ekub.ekub.EkubService;
import com.ekub.ekub_users.EkubUserService;
import com.ekub.round.Round;
import com.ekub.round.RoundService;
import com.ekub.user.User;
import com.ekub.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentMapper mapper;
    private final UserService userService;
    private final RoundService roundService;
    private final EkubService ekubService;
    private final EkubUserService ekubUserService;

    // create payments
    public void createPayment(PaymentRequest request){
        User user = userService.findUserById(request.userId());
        Round round = roundService.findRoundById(request.roundId());

        paymentRepository.save(
            Payment.builder()
                .id(UUID.randomUUID())
                .user(user)
                .round(round)
                .amount(request.amount())
                .createdDate(LocalDateTime.now())
                .build()
        );
    }

    // get payments of round
    public List<PaymentResponse> getRoundPayments(UUID roundId){
      return paymentRepository.findByRoundId(roundId)
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

//        System.out.println("pay with paymentAmount " + ekub.getWinAmount());

        //upate round status to paid
        round.setPaid(true);
        roundService.save(round);
    }


    //get User payments
    public List<PaymentResponse> getUserPayments(String userId) {
        User user = userService.findUserById(userId);
        List<Payment> userPayments = paymentRepository.findByUserId(user.getId());
        return userPayments.stream()
                .map(mapper::toPaymentResponse)
                .toList();
    }

    //get user round payments
    public PageResponse<UserRoundPaymentResponse> getUserRoundPayments(String ekubId, int page, int size){
        Ekub ekub = ekubService.findEkubById(ekubId);

        Pageable pageable = PageRequest.of(page,size);
        Page<UserRoundPaymentsDTO> res = paymentRepository
                .findUserRoundPayments(ekub.getId(), pageable);

        //group by username and round
        Map<String, Map<Integer, BigDecimal>> groupedData = new HashMap<>();
        for(UserRoundPaymentsDTO dto : res.getContent()){
            groupedData.computeIfAbsent(dto.username(), k -> new HashMap<>())
                    .put(dto.roundNumber(), dto.paymentAmount());
        }

        //transform grouped data into table
        List<UserRoundPaymentResponse> table = groupedData.entrySet()
                .stream()
                .map(entry -> {
                    Map<String, Object> row = new LinkedHashMap<>();
                    row.put("name", entry.getKey());
                    entry.getValue().forEach((roundNumber, amount) -> {
                        row.put("round" + roundNumber, amount);
                    });
                    return row;
                })
                .map(UserRoundPaymentResponse::new)
                .toList();

        return PageResponse.<UserRoundPaymentResponse>builder()
                .content(table)
                .totalElements(res.getTotalElements())
                .numberOfElements(res.getNumberOfElements())
                .totalPages(res.getTotalPages())
                .size(res.getSize())
                .number(res.getNumber())
                .first(res.isFirst())
                .last(res.isLast())
                .empty(res.isEmpty())
                .build();
    }

}
