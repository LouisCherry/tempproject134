package com.epoint.jnzwfw.auditproject.jnauditprojectrest.impl;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.spgl.domain.SpglXmspsxblxxb;
import com.epoint.jnzwfw.auditproject.jnauditprojectrest.api.IJNAuditProjectRestService;


/**
 * 微信公众号办件公告接口对应的后台service接口
 * 
 * @author hlxin
 * @version [版本号, 2019-07-23 11:00:17]
 */
@Component
@Service
public class JNAuditProjectRestServiceImpl implements IJNAuditProjectRestService
{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    
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
    public List<AuditProject> getAuditProjectRestList(int pageNumber, int pageSize, String areaCode) {
        return new JNAuditProjectRestService().getAuditProjectRestList( pageNumber, pageSize, areaCode);
    }
    
    /**
     * 查找办件的详细信息
     * 
     * @param rowGuid
     */
    public AuditProject getAuditProjectRestDetail(String rowGuid) {
        return new JNAuditProjectRestService().getAuditProjectRestDetail(rowGuid);
    }

    /**
     * 统计本年、月、日办件数量
     */
    @Override
    public AuditProject getAuditCount(String areaCode) {
        return new JNAuditProjectRestService().getAuditCount(areaCode);
    }



    /**
     * 统计办件基本满意和不满意的数量
     */
    @Override
    public AuditProject getSatisfiedCount(String areaCode) {
        return new JNAuditProjectRestService().getSatisfiedCount(areaCode);
    }
    
    @Override
    public AuditProject getLsSatisfiedCount() {
    	return new JNAuditProjectRestService().getLsSatisfiedCount();
    }

    /**
     * 统计办件总计的数量
     */
    @Override
    public AuditProject getTotalSatisfiedCount(String areaCode) {
        return new JNAuditProjectRestService().getTotalSatisfiedCount(areaCode);
    }

    /**
     * 修改办件的状态
     */
    @Override
    public int updateProjectByRowguid(String projectguid, Date banjiedate, String Status, String banjieusername, String Banjieresult) {
        return new JNAuditProjectRestService().updateProjectByRowguid( projectguid, banjiedate, Status, banjieusername, Banjieresult);
    }
    
    /**
     * 统计办件总计的数量
     */
    @Override
    public List<SpglXmspsxblxxb> getSPglXmspxxb() {
        return new JNAuditProjectRestService().getSPglXmspxxb();
    }
    
    /**
     * 统计办件总计的数量
     */
    @Override
    public int updateSPglXmspxxb(String rowguid) {
        return new JNAuditProjectRestService().updateSPglXmspxxb(rowguid);
    }
    
    
    /**
     * 统计办件总计的数量
     */
    @Override
    public List<AuditProject> getauditProjectByFlowsn(String flowsns) {
        return new JNAuditProjectRestService().getauditProjectByFlowsn(flowsns);
    }


}
