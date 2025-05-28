package com.epoint.jnzwfw.sgxkzproject.impl;
import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.jnzwfw.sgxkzproject.api.IJzgcSgxkzService;
import com.epoint.jnzwfw.sgxkzproject.api.entity.SpxkzBanjianProject;
import com.epoint.jnzwfw.sgxkzproject.api.entity.jzgcsgxkz1;
/**
 * 车辆信息表对应的后台service实现类
 * 
 * @author 1
 * @version [版本号, 2021-02-03 15:42:45]
 */
@Component
@Service
public class JzgcSgxkzServiceImpl implements IJzgcSgxkzService
{
    /**
     * 
     */
    private static final long serialVersionUID = 9004347640464422463L;

    /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(jzgcsgxkz1 record) {
        return new JzgcSgxkzService().insert(record);
    }

    /**
     * 更新数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(jzgcsgxkz1 record) {
        return new JzgcSgxkzService().update(record);
    }

    
    public jzgcsgxkz1 getRecordByRowguid(String projectguid){
        return new JzgcSgxkzService().getRecordByRowguid(projectguid);
    }
    
   

}
