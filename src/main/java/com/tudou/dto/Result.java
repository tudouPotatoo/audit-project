package com.tudou.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 返回结果类
 * @author 赖玉婷
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result {
    /**
     * 执行结果
     */
    private Boolean success;
    /**
     * 报错信息
     */
    private String errorMsg;
    /**
     * 返回数据
     */
    private Object data;
    /**
     * 数据的总数（通常在分页查询时使用）
     */
    private Long total;

    /**
     * 操作成功
     * 不返回任何数据
     * @return
     */
    public static Result ok(){
        return new Result(true, null, null, null);
    }

    /**
     * 操作成功
     * 携带返回的数据
     * @param data
     * @return
     */
    public static Result ok(Object data){
        return new Result(true, null, data, null);
    }

    /**
     * 操作成功
     * 携带多条数据（常用于分页查询）
     * @param data 数据列表
     * @param total 数据的总数
     * @return
     */
    public static Result ok(List<?> data, Long total){
        return new Result(true, null, data, total);
    }

    /**
     * 操作失败
     * 携带失败信息
     * @param errorMsg
     * @return
     */
    public static Result fail(String errorMsg){
        return new Result(false, errorMsg, null, null);
    }
}
