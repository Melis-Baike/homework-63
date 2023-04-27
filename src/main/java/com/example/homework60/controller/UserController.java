package com.example.homework60.controller;

import com.example.homework60.dto.UserDTO;
import com.example.homework60.form.AuthForm;
import com.example.homework60.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService service;

    @PostMapping("/register")
    public Optional<UserDTO> registerUser(@RequestBody AuthForm form){
        return service.registerUser(form);
    }

    @PostMapping("/auth")
    public UserDTO login(Authentication authentication){
        return service.login(authentication);
    }
}
