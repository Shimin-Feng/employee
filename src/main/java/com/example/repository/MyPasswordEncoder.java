package com.example.repository;

import org.jetbrains.annotations.NotNull;
import org.springframework.security.crypto.password.PasswordEncoder;

// 这个类还可以如何使用？
public class MyPasswordEncoder implements PasswordEncoder {
    @Override
    public String encode(
            @NotNull
            CharSequence rawPassword) {
        return rawPassword.toString();
    }

    @Override
    public boolean matches(
            @NotNull
            CharSequence rawPassword,
            @NotNull
            String encodedPassword) {
        return encodedPassword.equals(rawPassword.toString());
    }
}