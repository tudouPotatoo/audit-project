package com.tudou.service.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.tudou.dto.Result;
import com.tudou.mapper.UserOperationLogMapper;
import com.tudou.pojo.LogAuditResult;
import com.tudou.pojo.UserOperationLog;
import com.tudou.service.UserOperationLogService;
import com.tudou.utils.BertUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 用户操作日志 Service层实现类
 * @author 赖玉婷
 * @version 1.0
 */
@Service
public class UserOperationLogServiceImpl extends ServiceImpl<UserOperationLogMapper, UserOperationLog> implements UserOperationLogService {

    @Autowired
    private LogAuditResultServiceImpl logAuditResultService;

    /**
     * 1. 读json文件
     * 2. 将json文件的每一个项转换为jsonObject
     * 3. 将该项转为一个UserOperationLog对象
     * 4. 将该对象存入List
     * @param filePath
     * @return
     */
    public List<UserOperationLog> readAndCleanJsonFile(String filePath) {
        List<UserOperationLog> userOperationLogs = new ArrayList<>();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        // 1. 读json文件
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            // 将整个文件内容解析为一个 JSONArray
            JSONArray jsonArray = JSON.parseArray(sb.toString());

            // 遍历 JSONArray 并将每个项转换为 UserOperationLog 对象
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                UserOperationLog cleanedLog = new UserOperationLog();
                // cleanedLog.setId((Integer) jsonObject.get("id"));
                cleanedLog.setUserId((Integer) jsonObject.get("userId"));
                cleanedLog.setMode((Integer) jsonObject.get("mode"));
                cleanedLog.setTime(formatter.parse((String) jsonObject.get("time")));
                cleanedLog.setParentOperation((Integer) jsonObject.get("parentOperation"));
                cleanedLog.setOperation((Integer) jsonObject.get("operation"));
                cleanedLog.setInformContent((String) jsonObject.get("informedContent"));
                userOperationLogs.add(cleanedLog);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return userOperationLogs;
    }
    // public List<UserOperationLog> readAndCleanJsonFile(String filePath) {
    //
    //     List<UserOperationLog> userLogs = null;
    //     try {
    //         ObjectMapper objectMapper = new ObjectMapper();
    //         CollectionType listType = objectMapper.getTypeFactory().constructCollectionType(List.class, UserOperationLog.class);
    //         userLogs = objectMapper.readValue(new File(filePath), listType);
    //     } catch (IOException e) {
    //         throw new RuntimeException(e);
    //     }
    //
    //     // 清洗数据，只保留指定字段
    //     List<UserOperationLog> cleanedUserLogs = new ArrayList<>();
    //     for (UserOperationLog userLog : userLogs) {
    //         UserOperationLog cleanedLog = new UserOperationLog();
    //         cleanedLog.setId(userLog.getId());
    //         cleanedLog.setUserId(userLog.getUserId());
    //         cleanedLog.setMode(userLog.getMode());
    //         cleanedLog.setTime(userLog.getTime());
    //         cleanedLog.setParentOperation(userLog.getParentOperation());
    //         cleanedLog.setOperation(userLog.getOperation());
    //         cleanedLog.setInformContent(userLog.getInformContent());
    //         cleanedUserLogs.add(cleanedLog);
    //     }
    //     return cleanedUserLogs;
    // }


    /**
     * 日志审计
     * 1. 根据id查询该条用户操作日志
     *   1.1 日志不存在 返回报错信息
     *   1.2 日志存在   继续往下
     * 2. 调用bert接口，分析infoContent内容，得到JSON文件的内容
     * 3. 遍历json文件的每一项
     *      3.1 根据当前项auditItem和auditSubItem的值去数据库找对应的一条审计结果数据
     *          3.2.1 不存在 继续往下-->3.2
     *          3.2.2 存在 判断该条数据是否已经通过
     *              3.2.2.1 是 说明该子项已经通过 直接返回结果
     *              3.2.2.2 不是 继续往下-->3.2
     *      3.2 将当前项转化为一个LogAuditResult对象
     *      3.3 判断是否满足status==1(score>=threshold)
     *          3.3.1 是  判断用户是否有操作
     *                  3.3.1.1 是 则将result设为1-成功
     *                  3.3.1.2 不是 则将result设为0-失败
     *          3.3.2 不是 将result设为0-失败
     *      3.4 完善LogAuditResult对象信息
     *      3.5 将当前LogAuditResult对象写入数据库表tb_log_audit_result
     *          3.5.1 如果该审计子项已经存在，则先删除已有那条数据，再将新的数据加入
     *          3.5.2 如果该审计子项还不存在，追加一条数据
     * @param id 要进行审计的日志id
     */
    @Override
    public Result auditLog(int id) {
        // 1. 根据id查询该条用户操作日志
        UserOperationLog log = getById(id);
        // 1.1 日志不存在 返回报错信息
        if (log == null) {
            return Result.fail("该条日志不存在！");
        }
        // 1.2 日志存在   继续往下

        // 2. 调用bert接口，分析infoContent内容，得到JSON文件的内容
        JSONArray auditResults = (JSONArray)BertUtils.audit(log.getInformContent()).get("auditItems");
        // 3. 遍历json文件的每一项
        for (Object result : auditResults) {
            // 将当前项转化成一个JSONObject对象
            JSONObject jsonObject = (JSONObject) result;
            // 3.1 根据当前项auditItem和auditSubItem的值去数据库找对应的一条审计结果数据
            LogAuditResult existedResult = logAuditResultService.queryByAuditItemAndAuditSubItem((String) jsonObject.get("auditItem"), (String) jsonObject.get("auditSubItem"));
            // 该条数据存在且子项已经通过 则直接返回
            if (existedResult != null && existedResult.getStatus() == 1) {
                return Result.ok();
            }
            // 3.2 将当前项转化为一个LogAuditResult对象
            LogAuditResult logAuditResult = JSONObject2LogAuditResult(jsonObject);
            // 3.3 判断是否满足status==1(score>=threshold)
            int status = logAuditResult.getStatus() == null ? 0 : logAuditResult.getStatus().intValue();
            // 如果status==1，且用户/监护人有操作，则将status置为1
            if (status == 1 && (log.getOperation() != null|| log.getParentOperation() != null)) {
                status = 1;
            } else {
                status = 0;
            }
            // 3.4 完善LogAuditResult对象信息
            logAuditResult.setStatus(status);
            logAuditResult.setUserOptLogId(log.getId());

            // 3.5 将当前LogAuditResult对象写入数据库表tb_log_audit_result
            if (existedResult != null) {
                // 如果该审计子项已经存在，则先删除已有那条数据
                logAuditResultService.removeById(existedResult.getId());
            }
            logAuditResultService.save(logAuditResult);
        }
        return Result.ok();
    }

    private LogAuditResult JSONObject2LogAuditResult(JSONObject jsonObject) {
        LogAuditResult logAuditResult = new LogAuditResult();

        logAuditResult.setAuditItem(String.valueOf(jsonObject.get("auditItem")));
        logAuditResult.setAuditSubItem(String.valueOf(jsonObject.get("auditSubItem")));
        logAuditResult.setThreshold(Double.valueOf(String.valueOf(jsonObject.get("auditSubThreshold"))));
        logAuditResult.setScore(Double.valueOf(String.valueOf(jsonObject.get("auditSubScore"))));
        logAuditResult.setStatus(Integer.valueOf(String.valueOf(jsonObject.get("auditSubStatus"))));
        logAuditResult.setData(String.valueOf(jsonObject.get("auditData")));

        return logAuditResult;
    }
}
