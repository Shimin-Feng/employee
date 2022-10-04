package com.shiminfxcvii.controller;

import com.shiminfxcvii.service.OperationLogService;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static com.shiminfxcvii.util.Constants.*;

/**
 * 保存和查询操作员工信息后的日志
 *
 * @author shiminfxcvii
 * @since 2022/5/2 1:15 周一
 */
@Controller
@RequestMapping("operationLog")
public class OperationLogController {

    private final OperationLogService operationLogService;

    @Lazy
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
            params = {PAGE_NUM, PAGE_SIZE},
            headers = {HttpHeaders.CACHE_CONTROL, X_CSRF_TOKEN},
            consumes = MediaType.ALL_VALUE,
            produces = MediaType.TEXT_HTML_VALUE
    )
    public String findOperationLogsBy(@RequestParam(value = PAGE_NUM, defaultValue = ZERO) Integer pageNum,
                                      @RequestParam(value = PAGE_SIZE, defaultValue = TEN) Integer pageSize,
                                      Model model) {
        operationLogService.findOperationLogsBy(pageNum, pageSize, model);
        return "operation-log";
    }

}
