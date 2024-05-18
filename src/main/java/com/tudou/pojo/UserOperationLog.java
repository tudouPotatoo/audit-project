package com.tudou.pojo;

import lombok.Data;

/**
 * 实体类
 * 表示一条用户操作日志
 * @author 赖玉婷
 * @version 1.0
 */
@Data
public class UserOperationLog {
    /**
     * 该操作的id
     */
    private  Integer id;

    /**
     * 进行操作的用户Id
     */
    private  Integer userId;

    /**
     * 用户模式：0-普通模式；1-青少年模式
     */
    private  Integer mode;

    /**
     * 操作时间
     */
    private double time;

    /**
     * 监护人操作
     * null-表示正常模式（即非青少年模式）
     * 0-不同意
     * 1-同意
     */
    private Integer parentOperation;

    /**
     * 用户操作
     *
     * 同意-1
     * 拒绝-2
     * 撤回同意-3
     * 拒绝自动化决策-4
     * 拒绝个性化商业营销推送-5
     * 删除用户标签-6
     * 查阅-7
     * 复制-8
     * 转移-9
     * 更正-10
     * 补充-11
     * 删除个人信息-12
     */
    private Integer operation;

    /**
     * 告知用户的内容
     */
    private String informContent;

    public void setId(Integer id) {
        this.id = id;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public void setMode(Integer mode) {
        this.mode = mode;
    }

    public void setTime(double time) {
        this.time = time;
    }

    public void setParentOperation(Integer parentOperation) {
        this.parentOperation = parentOperation;
    }

    public void setOperation(Integer operation) {
        this.operation = operation;
    }

    public void setInformContent(String informContent) {
        this.informContent = informContent;
    }

    public Integer getId() {
        return id;
    }

    public Integer getUserId() {
        return userId;
    }

    public Integer getMode() {
        return mode;
    }

    public double getTime() {
        return time;
    }

    public Integer getParentOperation() {
        return parentOperation;
    }

    public Integer getOperation() {
        return operation;
    }

    public String getInformContent() {
        return informContent;
    }

    /**
     * 用户被处理的信息内容
     * 例如：生物识别、宗教信仰、特定身份、医疗健康金融账户、行踪轨迹xxxx等
     */
    private String userInfo;

    /**
     * 处理用户信息的方式
     */
    private String processMethod;

    /**
     * 处理目的
     */
    private String processGoal;

    /**
     * 处理人员基本信息
     */
    private String processorInfo;

    /**
     * 第三方处理者信息
     */
    private String thirdPartyProcessorInfo;
}
