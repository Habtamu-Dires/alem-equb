package com.ekub.user;

import com.ekub.ekub.Ekub;
import com.ekub.ekub_users.EkubUser;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UserSpecification {

    // search user by name
    public static Specification<User> searchUserByName(String  name){
        return ((root, query, criteriaBuilder)->{
             if(name == null || name.isEmpty()){
                 return criteriaBuilder.conjunction();
             }
            List<Predicate> predicates = new ArrayList<>();
             //username
              Predicate usernamePredicate = criteriaBuilder.like(
                     criteriaBuilder.lower(root.get("username")),"%"+name.toLowerCase()+"%"
              );
              // firstname
               Predicate firstnamePredicate = criteriaBuilder.like(
                       criteriaBuilder.lower(root.get("firstName")),"%"+name.toLowerCase()+"%"
               );
               Predicate namePredicate = criteriaBuilder.or(usernamePredicate,firstnamePredicate);
               predicates.add(namePredicate);

               return criteriaBuilder.and(predicates.toArray(new Predicate[0]));

        });
    }

    // search user not in ekub or invited
    public static Specification<User> searchUsersNotInEkubOrInvited(
            String text, UUID ekubId)
    {
        return ((root, query, criteriaBuilder) -> {
            // subquery1 : user already invited (invited_ekubs)
            Subquery<User> invitedSubquery = query.subquery(User.class);
            Root<User> invitedRoot = invitedSubquery.from(User.class);
            Join<User, Ekub> invitedEkubsJoin = invitedRoot.join("invitedEkubs");
            invitedSubquery.select(invitedRoot)
                    .where(criteriaBuilder.equal(invitedEkubsJoin.get("id"),ekubId));

            // subquery2: user already member
            Subquery<User> memberSubquery = query.subquery(User.class);
            Root<EkubUser> memberRoot = memberSubquery.from(EkubUser.class);
            memberSubquery.select(memberRoot.get("user"))
                    .where(criteriaBuilder.equal(memberRoot.get("ekub").get("id"),ekubId));

            //Main query conditon
            return criteriaBuilder.and(
                    criteriaBuilder.or(
                            criteriaBuilder.like(
                                    criteriaBuilder.lower(root.get("firstName")),
                                    "%" + text.toLowerCase() + "%"
                            ),
                            criteriaBuilder.like(
                                    criteriaBuilder.lower(root.get("username")),
                                    "%" + text.toLowerCase() + "%"
                            )
                    ),
                    criteriaBuilder.not(criteriaBuilder.in(root).value(invitedSubquery)),
                    criteriaBuilder.not(criteriaBuilder.in(root).value(memberSubquery))
            );
        });

    }
}
