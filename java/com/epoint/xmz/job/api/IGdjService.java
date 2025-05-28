package com.epoint.xmz.job.api;

import java.io.Serializable;

import com.epoint.basic.auditresource.auditrsitembaseinfo.domain.AuditRsItemBaseinfo;
import com.epoint.core.grammar.Record;

/**
 * 好差评相关接口
 * @作者 atjiao
 * @version [版本号, 2020年6月8日]
 */
public interface IGdjService extends Serializable
{
	 /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
	 * @return 
     * @return int
     */
    void insertsw(AuditRsItemBaseinfo record);

    /**
     * 更新数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return 
     * @return int
     */
    void updatesw(AuditRsItemBaseinfo record);
    
    
    AuditRsItemBaseinfo getSwInteminfoByItemcode(String Itemcode);

    AuditRsItemBaseinfo getSwInteminfoByRowguid(String rowguid);
    

}
