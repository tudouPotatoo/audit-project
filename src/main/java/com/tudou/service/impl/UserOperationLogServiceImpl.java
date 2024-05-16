package com.tudou.service.impl;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tudou.dto.Result;
import com.tudou.mapper.UserOperationLogMapper;
import com.tudou.pojo.LogAuditResult;
import com.tudou.pojo.UserOperationLog;
import com.tudou.service.UserOperationLogService;
import com.tudou.utils.BertUtils;
import org.springframework.stereotype.Service;

/**
 * 用户操作日志 Service层实现类
 * @author 赖玉婷
 * @version 1.0
 */
@Service
public class UserOperationLogServiceImpl extends ServiceImpl<UserOperationLogMapper, UserOperationLog> implements UserOperationLogService {

    /**
     * 日志审计
     * 1. 根据id查询该条用户操作日志
     *   1.1 日志不存在 返回报错信息
     *   1.2 日志存在   继续往下
     * 2. 调用bert接口，分析infoContent内容，得到JSON文件的内容
     * 3. 遍历json文件的每一项
     *      3.1 将当前项转化为一个LogAuditResult对象
     *      3.2 判断是否满足status==1(score>=threshold)
     *          3.2.1 是  判断用户是否有操作
     *                  3.2.1.1 是 则将result设为1-成功
     *                  3.2.1.2 不是 则将result设为0-失败
     *          3.2.2 不是 将result设为0-失败
     *      3.3 将当前LogAuditResult对象写入数据库表tb_log_audit_result
     * @param id
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
            // 3.1 将当前项转化为一个LogAuditResult对象
            LogAuditResult logAuditResult = JSONObject.parseObject(jsonObject.toString(), LogAuditResult.class);

            // 3.2 判断是否满足status==1(score>=threshold)

        }
        // 4. 根据分析之后的结果进行合规审计
        // 5. 将审计结果存入数据库
        return Result.ok();
    }

    /*
       {
            "auditItem": "处理个人信息合法性基础",
            "auditSubItem": "取得个人同意",
            "auditSubStatus": 1,
            "auditSubThreshold": 0.7,
            "auditSubScore": 0.8525984287261963,
            "auditData": [
                {
                    "auditDataKey": "隐私声明文本",
                    "auditDataValue": "如您发现账号、密码及/或其他身份要素可能或已经泄露时，请您立即和我们取得联系，以便我们及时采取相应措施以避免或降低相关损失"
                }
            ]
        }
     */
}
