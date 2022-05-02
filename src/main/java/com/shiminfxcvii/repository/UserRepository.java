package com.shiminfxcvii.repository;

import com.shiminfxcvii.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
}