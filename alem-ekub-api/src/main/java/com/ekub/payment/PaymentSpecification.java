package com.ekub.payment;

import com.ekub.ekub.Ekub;
import com.ekub.round.Round;
import com.ekub.user.User;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PaymentSpecification {

    //get payments
    public static Specification<Payment> findPayments(String ekubId, LocalDateTime dateTime){
        return (((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            //ekubId
            if(ekubId != null && !ekubId.isEmpty()){
                UUID ekub_id = UUID.fromString(ekubId);
                Join<Payment, Round> roundJoin = root.join("round");
                Join<Round, Ekub> roundEkubJoin = roundJoin.join("ekub");
                Predicate ekubPredicate = criteriaBuilder.equal(roundEkubJoin.get("id"), ekub_id);
                predicates.add(ekubPredicate);
            }
            // dateTime
            if(dateTime != null){
                Predicate dateTimePredicate = criteriaBuilder.greaterThan(root.get("createdDate"), dateTime);
                predicates.add(dateTimePredicate);
            }
            if(predicates.isEmpty()){
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        }));
    }

    //search payment by  username
    public static Specification<Payment> search(String name){
        return ((root, query, criteriaBuilder) -> {
            if(name == null || name.isEmpty()){
                return criteriaBuilder.conjunction();
            }
            // username predicate
            Join<Payment, User> user = root.join("user");
            return criteriaBuilder.like(
                    criteriaBuilder.lower(user.get("username")),"%"+name+"%"
            );
        });
    }

}
