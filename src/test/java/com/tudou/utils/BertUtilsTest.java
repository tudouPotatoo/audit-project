package com.tudou.utils;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class BertUtilsTest {

    @Test
    public void testAudit() {
        // 2. 调用bert接口，分析infoContent内容，得到JSON文件的内容
        JSONArray auditResults = (JSONArray)BertUtils.audit("").get("auditItems");
        // 3. 遍历json文件的每一项
        for (Object result : auditResults) {
            JSONObject jsonObject = (JSONObject) result;
            System.out.println(result);
            System.out.println("=======");
        }
    }
}
