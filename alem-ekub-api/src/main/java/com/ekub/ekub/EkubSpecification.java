package com.ekub.ekub;

import org.springframework.data.jpa.domain.Specification;

public class EkubSpecification {

    public static Specification<Ekub> searchEkubByName(String name){
        return (root, query, criteriaBuilder) -> {
           if(name == null || name.isEmpty()){
                return criteriaBuilder.conjunction();
           }
           return criteriaBuilder.like(
                   criteriaBuilder.lower(root.get("name")),"%"+name.toLowerCase()+"%");
        };
    }
}
