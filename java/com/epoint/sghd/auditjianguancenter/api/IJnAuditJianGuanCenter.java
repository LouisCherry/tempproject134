package com.epoint.sghd.auditjianguancenter.api;

import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.core.grammar.Record;

import java.io.Serializable;
import java.util.List;

public interface IJnAuditJianGuanCenter extends Serializable
{

    /**
     * 获取中心审批办件数量
     * @return
     */
    public int getSpxxCount(String sql);

    /**
     *  中心许可变更意见数目
     *  @return    
     */
    public int getBianGengNum(String areaCode);

    /**
     * 中心领导获取互动协助数量
     * @return
     */
    public int getHdxzCount(String areaCode);

    /**
     *  中心获取已认领的办件数目
     *  @return    
     */
    public int getYrl(String areaCode);

    /**
     *  中心获取未认领的办件数目
     *  @return    
     */
    public int getWrl(String areaCode);

    /**
     *  获取已认领的办件数目 areacode
     *  @return    
     */
    public int getYrlByAreaCode(String areaCode);

    /**
     *  获取已认领的办件数目 byouguid
     *  @return    
     */
    public int getYrlByOuguid(String ouguid);

    /**
     *  获取未认领的办件数目 areaCode
     *  @return    
     */
    public int getWrlByAreaCode(String areaCode);

    /**
     *  获取未认领的办件数目 ouguid
     *  @return    
     */
    public int getWrlByOuguid(String ouguid);

    /**
     * 
     * @Description: 获取树ou信息
     * @author male   
     * @date 2019年1月23日 下午3:31:48
     * @return List<Record>    返回类型    
     * @throws
     */
    public List<Record> getOuInfo(String areaCode);

    /**
     * 
     * @Description: 获取TaProjectCenterAction类DateGrid
     * @author male   
     * @date 2019年1月23日 下午3:45:05
     * @return List<AuditProject>    返回类型    
     * @throws
     */
    public List<AuditProject> getProejctCenter(String sql, int first, int pageSize);

    /**
     * 
     * @Description: 根据rowguid获取audit_task ouname
     * @author male   
     * @date 2019年1月23日 下午3:45:09
     * @return String    返回类型    
     * @throws
     */
    public String getOuNameFromAuditTask(String rowguid);

    /**
     * 
     * @Description: 获取TaProjectCenterAction类DateGrid数量 
     * @author male   
     * @date 2019年1月23日 下午3:45:13
     * @return int    返回类型    
     * @throws
     */
    public int getProejctCenterNum(String sql);

    /**
     *  根据辖区获取划转事项的原部门信息
     *  @param areacode
     *  @return    
     */
    public List<Record> listOuByAreacode(String areacode);
}
