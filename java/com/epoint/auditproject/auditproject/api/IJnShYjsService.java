package com.epoint.auditproject.auditproject.api;
import java.io.Serializable;

import com.epoint.core.grammar.Record;

/**
 * 成品油零售经营企业库对应的后台service接口
 * 
 * @author 18039505500
 * @version [版本号, 2020-06-23 19:57:28]
 */
public interface IJnShYjsService extends Serializable
{ 
   
    /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
	public void inserRecord(Record record);
	
}
