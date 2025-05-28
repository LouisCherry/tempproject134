package com.epoint.jiningzwfw.baobiao.api;



import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 工改事项分类关联表对应的后台service接口
 *
 * @author qichudong
 * @version [版本号, 2024-09-22 11:34:59]
 */
public interface ITaskBanjianService extends Serializable {



    /**
     * 查询数量
     *
     * @param sql  执行语句
     * @param args 参数
     * @return Integer
     */
    public Integer countTask(String sql, Object... args);


}
