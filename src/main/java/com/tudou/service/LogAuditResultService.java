package com.tudou.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tudou.pojo.LogAuditResult;

/**
 * 日志审计结果 Service层接口
 * @author 赖玉婷
 * @version 1.0
 */
public interface LogAuditResultService extends IService<LogAuditResult> {
    LogAuditResult queryByAuditItemAndAuditSubItem(String auditItem, String auditSubItem);
}
