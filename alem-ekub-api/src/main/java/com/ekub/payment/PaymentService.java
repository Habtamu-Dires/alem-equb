package com.ekub.payment;

import com.ekub.common.PageResponse;
import com.ekub.ekub.EkubService;
import com.ekub.round.Round;
import com.ekub.round.RoundService;
import com.ekub.user.User;
import com.ekub.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    // create payments
    @Transactional
    public void createPayment(PaymentRequest request){
        User user = userService.findUserByExId(request.userId());
        User toUser = null;
        if(request.toUserId() != null && !request.toUserId().isEmpty()){
            toUser = userService.findUserByExId(request.toUserId());
        }
        Round round = roundService.findRoundByExId(request.roundId());

        paymentRepository.save(
            Payment.builder()
                .type(request.type())
                .user(user)
                .toUser(toUser)
                .round(round)
                .amount(request.amount())
                .paymentMethod(request.paymentMethod())
                .createdDate(LocalDateTime.now())
                .remark(request.remark())
                .build()
        );
        BigDecimal newAmount = request.amount();
        if(request.type().equals(PaymentType.WINNING_PAYOUT)){
            //update total amount
            newAmount = newAmount.multiply(new BigDecimal(-1));
            //update round to paid
            roundService.setPaymentStatusToPaid(round);
        }
        //update total amount of ekub
        ekubService.updateTotalAmountOfEkub(round.getEkub().getId(),newAmount);

    }

    //get pages of payments
    public PageResponse<PaymentResponse> getPageOfPayments(
            String ekubId,
            LocalDateTime dateTime,
            int page,
            int size
    ) {
        Pageable pageable = PageRequest.of(page,size);
        Specification<Payment> spec = PaymentSpecification.findPayments(ekubId,dateTime);
        Page<Payment> res = paymentRepository.findAll(spec, pageable);
        List<PaymentResponse> paymentResponseList = res.map(mapper::toPaymentResponse).toList();

        return PageResponse.<PaymentResponse>builder()
                .content(paymentResponseList)
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

    //get User payments
    public List<PaymentResponse> getUserPayments(String exId) {
        User user = userService.findUserByExId(exId);
        return  paymentRepository.findUserPayments(exId,user.getId());
    }

    //get user round payments
    public List<UserRoundPaymentResponse> getUserRoundPayments(
            String ekubId,
            int version
    ) {
        List<UserRoundPaymentsDTO> res = paymentRepository
                .findUserRoundPayments(UUID.fromString(ekubId), version);

        //group by username and round
        Map<String, Map<Integer, BigDecimal>> groupedData = new HashMap<>();
        for(UserRoundPaymentsDTO dto : res){
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
        return table;
    }

    // search payment
    public List<PaymentResponse> search(String username){
        Specification<Payment> spec = PaymentSpecification.search(username);
        return paymentRepository.findAll(spec)
                .stream()
                .map(mapper::toPaymentResponse)
                .toList();
    }

}
