package com.epoint.jnzwfw.sgxkzproject.api;
import java.io.Serializable;

import com.epoint.jnzwfw.sgxkzproject.api.entity.jzgcsgxkz1;

/**
 * 建筑工程施工许可证对应的后台service接口
 * 
 * @author 1
 * @version [版本号, 2021-02-03 15:42:45]
 */
public interface IJzgcSgxkzService extends Serializable
{  
   
    /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(jzgcsgxkz1 record);
    
    public int update(jzgcsgxkz1 record);
    
    public jzgcsgxkz1 getRecordByRowguid(String projectguid);

   
}
