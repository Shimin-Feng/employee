package com.shiminfxcvii.repository;

import com.shiminfxcvii.entity.OperationLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author shiminfxcvii
 * @version 1.0
 * @description 员工管理操作日志工厂接口
 * @interface OperationLogRepository
 * @created 2022/5/2 1:10 周一
 */
@Repository
public interface OperationLogRepository extends JpaRepository<OperationLog, String> {
}