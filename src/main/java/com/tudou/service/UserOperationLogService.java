package com.tudou.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tudou.dto.Result;
import com.tudou.pojo.UserOperationLog;

import java.util.List;

/**
 * 用户操作日志 Service层接口
 * @author 赖玉婷
 * @version 1.0
 */
public interface UserOperationLogService extends IService<UserOperationLog> {
    Result auditLog(int id);
    List<UserOperationLog> readAndCleanJsonFile(String filePath);
}
