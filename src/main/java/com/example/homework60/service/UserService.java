package com.example.homework60.service;

import com.example.homework60.config.SecurityConfiguration;
import com.example.homework60.dao.UserDao;
import com.example.homework60.dto.UserDTO;
import com.example.homework60.entity.User;
import com.example.homework60.form.AuthForm;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserDao userDao;

    public Optional<UserDTO> registerUser(AuthForm form){
        boolean bool = userDao.checkExistingOfUser(form.getEmail());
        if(!bool){
            User user = User.builder()
                    .name(form.getName())
                    .email(form.getEmail())
                    .password(SecurityConfiguration.passwordEncoder().encode(form.getPassword()))
                    .publications(0)
                    .subscriptions(0)
                    .followers(0)
                    .enabled(true)
                    .roles("ROLE_USER")
                    .build();
            userDao.insertUser(user);
            return Optional.of(UserDTO.from(userDao.findUserByEmail(user.getEmail()).get()));
        } else {
            return Optional.empty();
        }
    }

    public UserDTO login(Authentication authentication){
        Optional<User> user = userDao.findUserByEmail(authentication.getName());
        return UserDTO.from(user.get());
    }
}
