package com.tudou.controller;

import com.tudou.dto.Result;
import com.tudou.pojo.UserOperationLog;
import com.tudou.repository.UserOperationLogRepository;
import com.tudou.service.UserOperationLogService;
import com.tudou.service.impl.UserOperationLogServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * 用户操作日志 Controller层
 * @author 赖玉婷
 * @version 1.0
 */
@Slf4j
@RestController
@RequestMapping("/userOptLog")
public class UserOperationLogController {


    private UserOperationLogService userOptLogService;
    private UserOperationLogRepository userOperationLogRepository;

    /**
     * 导入日志并清洗后存入到数据库中
     * @param filePath
     * @return
     */

    @PostMapping("/processJson")
    public String processJson(@RequestBody String filePath) throws IOException {
        // 读取并清洗JSON文件数据
        List<UserOperationLog> cleanedData = userOptLogService.readAndCleanJsonFile(filePath);

        // 将清洗后的数据存储到数据库中
        userOperationLogRepository.saveAll(cleanedData);

        return "Data processed and saved successfully!";
    }

    /**
     * 对一条用户操作日志进行日志审计
     * @param id
     * @return
     */
    @PostMapping("/audit/{id}")
    @ResponseBody
    public Result auditUserOperationLog(@PathVariable int id) {
        return userOptLogService.auditLog(id);
    }
}
