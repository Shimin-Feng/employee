package com.shiminfxcvii.repository;

import com.shiminfxcvii.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 用户接口
 *
 * @author shiminfxcvii
 * @since 2022/6/2 20:04
 */
@Repository
public interface UserRepository extends JpaRepository<User, String> {
}