package com.epoint.jiningzwfw.baobiao.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.jiningzwfw.baobiao.api.ITaskBanjianService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 工改事项分类关联表对应的后台service实现类
 *
 * @author qichudong
 * @version [版本号, 2024-09-22 11:34:59]
 */
@Component
@Service
public class TaskBanjianServiceImpl implements ITaskBanjianService {


    /**
     * 查询数量
     *
     * @param sql  执行语句
     * @param args 参数
     * @return Integer
     */
    @Override
    public Integer countTask(String sql, Object... args) {
        return new TaskBanjianService().countTask(sql, args);
    }


}
