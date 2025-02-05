package com.ekub.user;

import com.ekub.ekub.Ekub;
import com.ekub.ekub_users.EkubUser;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import org.springframework.data.jpa.domain.Specification;

import java.util.UUID;

public class UserSpecification {

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
                    criteriaBuilder.like(
                            criteriaBuilder.lower(root.get("firstName")),
                            "%" + text.toLowerCase() + "%"
                    ),
                    criteriaBuilder.not(criteriaBuilder.in(root).value(invitedSubquery)),
                    criteriaBuilder.not(criteriaBuilder.in(root).value(memberSubquery))
            );
        });

    }
}
