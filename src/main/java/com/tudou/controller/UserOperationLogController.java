package com.tudou.controller;

import com.tudou.dto.Result;
import com.tudou.service.UserOperationLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * 用户操作日志 Controller层
 * @author 赖玉婷
 * @version 1.0
 */
@Slf4j
@Controller("/userOptLog")
public class UserOperationLogController {

    @Autowired
    private UserOperationLogService userOptLogService;

    /**
     * 对一条用户操作日志进行日志审计
     * @param id
     * @return
     */
    @PostMapping("/audit/{id}")
    public Result auditUserOperationLog(@PathVariable int id) {
        userOptLogService.auditLog(id);
        return Result.ok();
    }
}
