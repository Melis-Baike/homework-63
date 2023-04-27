package com.example.homework60.dto;

import com.example.homework60.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    public static UserDTO from(User user){
        return UserDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .publications(user.getPublications())
                .subscriptions(user.getSubscriptions())
                .followers(user.getFollowers())
                .enabled(user.isEnabled())
                .roles(user.getRoles())
                .build();
    }
    private Long id;
    private String name;
    private String email;
    private int publications;
    private int subscriptions;
    private int followers;
    private boolean enabled;
    private String roles;
}
