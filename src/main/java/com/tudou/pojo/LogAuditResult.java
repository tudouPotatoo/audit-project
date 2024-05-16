package com.tudou.pojo;

import lombok.Data;

/**
 * 实体类
 * 一个用户操作对应的审计结果
 * @author 赖玉婷
 * @version 1.0
 */
@Data
public class LogAuditResult {
    /**
     * id
     */
    private Integer id;

    /**
     * 这条审计结果对应的用户操作日志id
     */
    private Integer userOptLogId;

    /**
     * 审计项
     */
    private String auditItem;

    /**
     * 审计子项
     */
    private String auditSubItem;

    /**
     * 审计通过阈值
     */
    private Double threshold;

    /**
     * 审计结果得分
     */
    private Double score;

    /**
     * 审计结果
     * 0-失败（score < threshold）
     * 1-成功（score >= threshold）
     */
    private Integer result;

    /**
     * 审计依据的内容
     */
    private String data;

}
