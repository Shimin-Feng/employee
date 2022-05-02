package com.example.repository;

import com.example.entity.OperationLog;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author $himin F
 * @version 1.0
 * @description 员工管理操作日志工厂接口
 * @class OperationLogRepository
 * @created 2022/5/2 1:10 周一
 */
public interface OperationLogRepository extends JpaRepository<OperationLog, String> {
}