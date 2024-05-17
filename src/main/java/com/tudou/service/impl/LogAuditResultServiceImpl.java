package com.tudou.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tudou.mapper.LogAuditResultMapper;
import com.tudou.pojo.LogAuditResult;
import com.tudou.service.LogAuditResultService;
import org.springframework.stereotype.Service;

/**
 * 日志审计结果 Service层实现类
 * @author 赖玉婷
 * @version 1.0
 */
@Service
public class LogAuditResultServiceImpl extends ServiceImpl<LogAuditResultMapper, LogAuditResult> implements LogAuditResultService {

    /**
     * 根据auditItem和auditSubItem的值查询
     * @param auditItem
     * @param auditSubItem
     * @return
     */
    @Override
    public LogAuditResult queryByAuditItemAndAuditSubItem(String auditItem, String auditSubItem) {
        LogAuditResult auditResult = query().eq("audit_item", auditItem).eq("audit_sub_item", auditSubItem).one();
        return auditResult;
    }
}
