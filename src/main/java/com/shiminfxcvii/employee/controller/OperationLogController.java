package com.shiminfxcvii.employee.controller;

import com.shiminfxcvii.employee.util.Constants;
import com.shiminfxcvii.employee.service.OperationLogService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 保存和查询操作员工信息后的日志
 *
 * @author ShiminFXCVII
 * @since 2022/5/2 1:15 周一
 */
@Controller
@RequestMapping("operationLog")
public class OperationLogController {

    private final OperationLogService operationLogService;

    public OperationLogController(OperationLogService operationLogService) {
        this.operationLogService = operationLogService;
    }

    @GetMapping
    public String operationLog(Model model) {
        operationLogService.operationLog(model);
        return "operation-log";
    }

    @GetMapping(
            value = "findOperationLogsBy",
            params = {Constants.PAGE_NUM, Constants.PAGE_SIZE},
            headers = {HttpHeaders.CACHE_CONTROL, Constants.X_CSRF_TOKEN},
            consumes = MediaType.ALL_VALUE,
            produces = MediaType.TEXT_HTML_VALUE
    )
    public String findOperationLogsBy(@RequestParam(value = Constants.PAGE_NUM, defaultValue = Constants.ZERO) Integer pageNum,
                                      @RequestParam(value = Constants.PAGE_SIZE, defaultValue = Constants.TEN) Integer pageSize,
                                      Model model) {
        operationLogService.findOperationLogsBy(pageNum, pageSize, model);
        return "operation-log";
    }

}