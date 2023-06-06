package com.shiminfxcvii.employee.repository;

import com.shiminfxcvii.employee.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 用户工厂接口
 *
 * @author ShiminFXCVII
 * @since 2022/6/2 20:04
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}