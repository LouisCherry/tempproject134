package com.epoint.xmz.api;

import java.io.Serializable;
import java.util.List;

import com.epoint.basic.auditonlineuser.auditonlinecompany.domain.AuditOnlineCompany;
import com.epoint.basic.auditonlineuser.auditonlinecompanygrant.domain.AuditOnlineCompanyGrant;
import com.epoint.basic.auditonlineuser.auditonlineindividual.domain.AuditOnlineIndividual;
import com.epoint.basic.auditonlineuser.auditonlineregister.domain.AuditOnlineRegister;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditresource.company.domain.AuditRsCompanyBaseinfo;
import com.epoint.basic.auditresource.company.domain.AuditRsCompanyLegal;
import com.epoint.basic.auditresource.company.domain.AuditRsCompanyRegister;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.core.grammar.Record;

public interface ISendMaterials extends Serializable
{
    /**
     * 
     * @Description: 获取材料信息
     * @author male   
     * @date 2019年3月12日 上午11:36:17
     * @return Record    返回类型    
     * @throws
     */
    public Record getMaterialsInfo(String attachguid);

    /**
     * 
     * @Description: 根据rowguid获取auditproject
     * @author male   
     * @date 2019年3月19日 下午4:32:53
     * @return AuditProject    返回类型    
     * @throws
     */
    public AuditProject getAuditProjectByRowguid(String rowguid);

    /**
     * 
     * @Description: 生成个性化流水号
     * @author male   
     * @date 2019年3月20日 上午11:47:09
     * @return String    返回类型    
     * @throws
     */
    public String getStrFlowSn(String numberName, String numberFlag, int snLength);

    /**
     * 
     * @Description: 获取查询密码字段
     * @author male   
     * @date 2019年3月20日 下午3:07:13
     * @return String    返回类型    
     * @throws
     */
    public String getSearchPwd(String projectguid);

    /**
     * 
     * @Description: 生成查询密码字段
     * @author male   
     * @date 2019年3月20日 下午3:17:24
     * @return int    返回类型    
     * @throws
     */
    public int updateSearchPwd(String projectguid);

    /**
     * 
     * @Description: 獲取掃碼事項信息
     * @author male   
     * @date 2019年3月21日 上午10:07:25
     * @return Record    返回类型    
     * @throws
     */
    public Record getProjectProcessInfo(String projectGuid);

    /**
     * 
     * @Description: 插入更新AuditOnlineIndividual
     * @author male   
     * @date 2019年3月29日 上午9:49:58
     * @return int    返回类型    
     * @throws
     */
    public int insertIndivdual(AuditOnlineIndividual record);

    /**
     * 
     * @Description: 插入更新auditonlineregister
     * @author male   
     * @date 2019年3月29日 上午9:18:12
     * @return int    返回类型    
     * @throws
     */
    public int insertRegister(AuditOnlineRegister record);

    /**
     * 
     * @Description:   插入更新audit_online_company
     * @author male   
     * @date 2019年3月29日 上午9:46:06
     * @return int    返回类型    
     * @throws
     */
    public int insertCompany(AuditOnlineCompany record);

    /**
     * 工建查询法人所拥有公司
     *  @param creditcode
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public AuditRsCompanyBaseinfo getAuditRsCompanyBaseinfo(String creditcode);

    /**
     * 
     * @Description: 根据cliengguid查询attachguid
     * @author male   
     * @date 2019年9月18日 下午4:57:19
     * @return List<String>    返回类型    
     * @throws
     */
    public List<String> getAttachguidByClientguid(String cliengguid);

    /**
     * 
     * @Description: 根据flowsn查询事项信息 
     * @author male   
     * @date 2019年10月20日 下午1:05:06
     * @return Record    返回类型    
     * @throws
     */
    public Record getTaskInfoByFlowsn(String flowsn);

    /**
     * 
     * @Description: 根据公司唯一标识查询企业信息
     * @author male   
     * @date 2020年4月2日 下午4:03:04
     * @return String    返回类型    
     * @throws
     */
    public AuditOnlineCompanyGrant getIAuditOnlineCompanyGrantByCompanyID(String companyid);

    /**
     * 
     * @Description: audit_rs_company_baseinfo 
     * @author male   
     * @date 2020年4月9日 上午9:47:23
     * @return int    返回类型    
     * @throws
     */
    public int insertRsComoany(AuditRsCompanyBaseinfo record);

    /**
     * 
     * @Description: 插入 AuditRsCompanyRegister表
     * @author male   
     * @date 2020年4月9日 上午10:32:54
     * @return int    返回类型    
     * @throws
     */
    public int insertAuditRsCompanyRegister(AuditRsCompanyRegister record);

    /**
     * 
     * @Description: 插入 AUDIT_RS_COMPANY_LEGAL表
     * @author male   
     * @date 2020年4月9日 上午10:38:21
     * @return int    返回类型    
     * @throws
     */
    public int insertAuditRsCompanyLegal(AuditRsCompanyLegal record);

    /**
     * 
     * @Description: 插入AuditOnlineCompanyGrant表
     * @author male   
     * @date 2020年4月9日 上午10:34:22
     * @return int    返回类型    
     * @throws
     */
    public int insertAuditOnlineCompanyGrant(AuditOnlineCompanyGrant record);
    
    public AuditTask getAuditTaskByNewItemCode(String code);
    
}
