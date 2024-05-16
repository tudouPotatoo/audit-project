package com.tudou.utils;


import com.alibaba.fastjson2.JSONObject;
import org.springframework.core.io.ClassPathResource;

import java.io.*;

public class BertUtils {

    /**
     * 对infoContent的内容进行分析审计
     * @param infoContent
     * @return
     */
    public static JSONObject audit(String infoContent) {
        String fileName = "audit-all-50-update-out-wangyi-new-cn.json"; // 修改为你的JSON文件路径
        JSONObject jsonObject = readJsonFile(fileName);
        System.out.println(jsonObject);
        return jsonObject;
    }

    private static JSONObject readJsonFile(String fileName) {

        String jsonStr = "";
        try {
            ClassPathResource classPathResource = new ClassPathResource(fileName);
            InputStream inputStream = classPathResource.getInputStream();
            Reader reader = new InputStreamReader(inputStream,"utf-8");
            int ch = 0;
            StringBuffer sb = new StringBuffer();
            while ((ch = reader.read()) != -1) {
                sb.append((char) ch);
            }
            jsonStr = sb.toString();
            //转json对象
            JSONObject parse = JSONObject.parse(jsonStr);
            return parse;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
