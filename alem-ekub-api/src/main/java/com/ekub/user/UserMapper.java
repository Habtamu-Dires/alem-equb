package com.ekub.user;

import com.ekub.ekub_users.EkubUser;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class UserMapper {

    public  UserResponse toUserResponse(User user){
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .firstname(user.getFirstName())
                .lastname(user.getLastName())
                .fullName(user.fullName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .profession(user.getProfession())
                .guarantor(user.getGuarantor() != null? user.getGuarantor().getUsername() : null)
                .profilePicUrl(user.getProfilePicUrl())
                .idCardImageUrl(user.getIdCardImageUrl())
                .enabled(user.isEnabled())
                .remark(user.getRemark())
                .ekubIds((user.getEkubs() != null && !user.getEkubs().isEmpty()) ? getEkubIds(user) : List.of())
                .ekubs((user.getEkubs() != null && !user.getEkubs().isEmpty()) ? getEkubNames(user) : List.of())
                .build();
    }

    public List<String> getEkubNames(User user){
        return user.getEkubs().stream()
                .sorted(Comparator.comparing(EkubUser::getCreatedDate))
                .map(ekubUser -> ekubUser.getEkub().getName())
                .toList();
    }

    public List<String> getEkubIds(User user){
        return user.getEkubs().stream()
                .sorted(Comparator.comparing(EkubUser::getCreatedDate))
                .map(ekubUser -> ekubUser.getEkub().getId().toString())
                .toList();
    }
}
