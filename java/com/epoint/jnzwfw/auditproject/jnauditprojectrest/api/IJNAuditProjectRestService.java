package com.epoint.jnzwfw.auditproject.jnauditprojectrest.api;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.spgl.domain.SpglXmspsxblxxb;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 微信公众号办件公告接口对应的后台service接口
 * 
 * @author hlxin
 * @version [版本号, 2019-07-23 11:00:17]
 */
public interface IJNAuditProjectRestService extends Serializable
{ 
   
    /**
     * 分页查找办件表的列表清单
     * 
     * @param sql
     *            查询语句
     * @param pageNumber
     *            记录行的偏移量
     * @param pageSize
     *            记录行的最大数目
     * @param clazz
     *            可以是[Record.class(弱类型);FrameOu.class(强类型);Object[].class]
     * @return T extends BaseEntity
     */
    public List<AuditProject> getAuditProjectRestList(int pageNumber, int pageSize, String areaCode);
    
    /**
     * 查找办件的详细信息
     * 
     * @param rowGuid
     */
    public AuditProject getAuditProjectRestDetail(String rowGuid);
    
    /**
     * 统计本年、月、日办件数量
     */
    public AuditProject getAuditCount(String areaCode);
    
    
    /**
     * 统计办件非常满意和不满意的数量
     */
    public AuditProject getSatisfiedCount(String areaCode);
    
    public AuditProject getLsSatisfiedCount();
    
    /**
     * 统计办件总计的数量
     */
    public AuditProject getTotalSatisfiedCount(String areaCode);

    int updateProjectByRowguid(String projectguid, Date banjiedate, String Status, String banjieusername,
            String Banjieresult);
    
    public List<SpglXmspsxblxxb> getSPglXmspxxb();
    
    public int updateSPglXmspxxb(String rowguid);
    
    public List<AuditProject> getauditProjectByFlowsn(String flowsns);
    
}
