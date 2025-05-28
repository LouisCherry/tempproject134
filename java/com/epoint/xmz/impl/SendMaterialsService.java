package com.epoint.xmz.impl;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.auditonlineuser.auditonlinecompany.domain.AuditOnlineCompany;
import com.epoint.basic.auditonlineuser.auditonlinecompanygrant.domain.AuditOnlineCompanyGrant;
import com.epoint.basic.auditonlineuser.auditonlineindividual.domain.AuditOnlineIndividual;
import com.epoint.basic.auditonlineuser.auditonlineregister.domain.AuditOnlineRegister;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditresource.company.domain.AuditRsCompanyBaseinfo;
import com.epoint.basic.auditresource.company.domain.AuditRsCompanyLegal;
import com.epoint.basic.auditresource.company.domain.AuditRsCompanyRegister;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.string.StringUtil;

public class SendMaterialsService
{
    /**
     * 数据增删改查组件
     */
    protected ICommonDao baseDao;

    public SendMaterialsService() {
        baseDao = CommonDao.getInstance();
    }

    /**
     * 
     * @Description: 获取材料信息
     * @author male   
     * @date 2019年3月12日 上午11:36:17
     * @return Record    返回类型    
     * @throws
     */
    public Record getMaterialsInfo(String attachguid) {
        String sql = "select attachfilename,contenttype,filepath from frame_attachinfo where ATTACHGUID = ?";
        return baseDao.find(sql, Record.class, attachguid);
    }

    /**
     * 
     * @Description: 根据rowguid获取auditproject
     * @author male   
     * @date 2019年3月19日 下午4:32:53
     * @return AuditProject    返回类型    
     * @throws
     */
    public AuditProject getAuditProjectByRowguid(String rowguid) {
        String sql = "select * from audit_project where rowguid = ?";
        return baseDao.find(sql, AuditProject.class, rowguid);
    }

    /**
     * 
     * @Description: 生成个性化流水号
     * @author male   
     * @date 2019年3月20日 上午11:47:09
     * @return String    返回类型    
     * @throws
     */
    public String getStrFlowSn(String numberName, String numberFlag, int snLength) {

        String flowSn = "";

        Object[] args = new Object[3];

        args[0] = numberName;

        args[1] = numberFlag;

        args[2] = Integer.valueOf(snLength);

        try {

            flowSn = baseDao.executeProcudureWithResult(args.length + 1, 12, "Common_Getflowsn2", args).toString();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return flowSn;
    }

    /**
     * 
     * @Description: 获取查询密码字段
     * @author male   
     * @date 2019年3月20日 下午3:07:13
     * @return String    返回类型    
     * @throws
     */
    public String getSearchPwd(String projectguid) {
        String sql = "select SearchPwd from audit_project where RowGuid = ?";
        return baseDao.queryString(sql, projectguid);
    }

    /**
     * 
     * @Description: 生成查询密码字段
     * @author male   
     * @date 2019年3月20日 下午3:17:24
     * @return int    返回类型    
     * @throws
     */
    public int updateSearchPwd(String projectguid) {
        int searchPwd = (int) ((Math.random() * 9 + 1) * 100000);
        String sql = "update audit_project set searchpwd = ? where rowguid = ?";
        return baseDao.execute(sql, String.valueOf(searchPwd), projectguid);
    }

    /**
     * 
     * @Description: 獲取掃碼事項信息
     * @author male   
     * @date 2019年3月21日 上午10:07:25
     * @return Record    返回类型    
     * @throws
     */
    public Record getProjectProcessInfo(String projectGuid) {
        String sql = "select RowGuid,FLOWSN,PROJECTNAME,APPLYERNAME,OUNAME,BANJIEDATE,`STATUS`,ACCEPTUSERDATE from audit_project where RowGuid = ?";
        return baseDao.find(sql, Record.class, projectGuid);
    }

    /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insertIndivdual(AuditOnlineIndividual record) {
        //执行操作为不存在插入，存在更新
        String sql = "select count(1) from audit_Online_Individual where rowguid = ?";
        int a = 0;
        int b = 0;
        if (StringUtil.isNotBlank(record.getRowguid())) {
            a = baseDao.queryInt(sql, record.getRowguid());
            //存在
            if (a > 0) {
                b = baseDao.update(record);
            }
            else {
                b = baseDao.insert(record);
            }
        }
        return b;
    }

    /**
     * 
     * @Description: 插入更新auditonlineregister
     * @author male   
     * @date 2019年3月29日 上午9:18:12
     * @return int    返回类型    
     * @throws
     */
    public int insertRegister(AuditOnlineRegister record) {
        String sql = "select count(1) from audit_online_register where rowguid = ?";
        int a = 0;
        int b = 0;
        if (StringUtil.isNotBlank(record.getRowguid())) {
            a = baseDao.queryInt(sql, record.getRowguid());
            //存在
            if (a > 0) {
                b = baseDao.update(record);
            }
            else {
                b = baseDao.insert(record);
            }
        }
        return b;
    }

    /**
     * 
     * @Description: 插入 AuditRsCompanyRegister表
     * @author male   
     * @date 2020年4月9日 上午10:32:54
     * @return int    返回类型    
     * @throws
     */
    public int insertAuditRsCompanyRegister(AuditRsCompanyRegister record) {
        String sql = "select count(1) from AUDIT_RS_COMPANY_REGISTER where rowguid = ?";
        int a = 0;
        int b = 0;
        if (StringUtil.isNotBlank(record.getRowguid())) {
            record.setVersiontime(new Date());
            a = baseDao.queryInt(sql, record.getRowguid());
            //存在
            if (a > 0) {
                b = baseDao.update(record);
            }
            else {
                b = baseDao.insert(record);
            }
        }
        return b;
    }

    /**
     * 
     * @Description: 插入 AUDIT_RS_COMPANY_LEGAL表
     * @author male   
     * @date 2020年4月9日 上午10:38:21
     * @return int    返回类型    
     * @throws
     */
    public int insertAuditRsCompanyLegal(AuditRsCompanyLegal record) {
        String sql = "select count(1) from AUDIT_RS_COMPANY_LEGAL where rowguid = ?";
        int a = 0;
        int b = 0;
        if (StringUtil.isNotBlank(record.getRowguid())) {
            record.setVersiontime(new Date());
            record.setOperatedate(new Date());
            a = baseDao.queryInt(sql, record.getRowguid());
            //存在
            if (a > 0) {
                b = baseDao.update(record);
            }
            else {
                b = baseDao.insert(record);
            }
        }
        return b;
    }

    /**
     * 
     * @Description: 插入AuditOnlineCompanyGrant表
     * @author male   
     * @date 2020年4月9日 上午10:34:22
     * @return int    返回类型    
     * @throws
     */
    public int insertAuditOnlineCompanyGrant(AuditOnlineCompanyGrant record) {
        String sql = "select count(1) from AUDIT_ONLINE_COMPANY_GRANT where rowguid = ?";
        int a = 0;
        int b = 0;
        if (StringUtil.isNotBlank(record.getRowguid())) {
            record.setOperatedate(new Date());
            record.setSqtime(new Date());
            a = baseDao.queryInt(sql, record.getRowguid());
            //存在
            if (a > 0) {
                b = baseDao.update(record);
            }
            else {
                b = baseDao.insert(record);
            }
        }
        return b;
    }

    /**
     * 
     * @Description: audit_rs_company_baseinfo 
     * @author male   
     * @date 2020年4月9日 上午9:47:23
     * @return int    返回类型    
     * @throws
     */
    public int insertRsComoany(AuditRsCompanyBaseinfo record) {
        String sql = "select count(1) from audit_rs_company_baseinfo where rowguid = ?";
        int a = 0;
        int b = 0;
        if (StringUtil.isNotBlank(record.getRowguid())) {
            record.setOperatedate(new Date());
            record.setVersiontime(new Date());

            a = baseDao.queryInt(sql, record.getRowguid());
            //存在
            if (a > 0) {
                b = baseDao.update(record);
            }
            else {
                b = baseDao.insert(record);
            }
        }
        return b;
    }

    /**
     * 
     * @Description:   插入更新audit_online_company
     * @author male   
     * @date 2019年3月29日 上午9:46:06
     * @return int    返回类型    
     * @throws
     */
    public int insertCompany(AuditOnlineCompany record) {
        String sql = "select count(1) from audit_online_company where rowguid = ?";
        int a = 0;
        int b = 0;
        if (StringUtil.isNotBlank(record.getRowguid())) {
            a = baseDao.queryInt(sql, record.getRowguid());
            //存在
            if (a > 0) {
                b = baseDao.update(record);
            }
            else {
                b = baseDao.insert(record);
            }
        }
        return b;
    }

    /**
     * 
     * @Description: 插入操作 流程用
     * @author male   
     * @date 2019年3月31日 下午5:14:39
     * @return int    返回类型    
     * @throws
     */

    public int insertRegisterProcess(AuditOnlineRegister record) {
        String sql = "select count(1) from audit_online_register where rowguid = ?";
        int a = 0;
        int b = 0;
        if (StringUtil.isNotBlank(record.getRowguid())) {
            a = baseDao.queryInt(sql, record.getRowguid());
            //存在
            if (a > 0) {
                b = baseDao.update(record);
            }
            else {
                b = baseDao.insert(record);
            }
        }
        return b;

    }

    /**
     * 
     * @Description: 插入操作 流程用
     * @author male   
     * @date 2019年3月31日 下午5:14:39
     * @return int    返回类型    
     * @throws
     */
    public int insertIndivdualProcess(AuditOnlineIndividual record) {
        CommonDao dao = new CommonDao();
        //执行操作为不存在插入，存在更新
        String sql = "select count(1) from audit_Online_Individual where rowguid = ?";
        int a = 0;
        int b = 0;
        if (StringUtil.isNotBlank(record.getRowguid())) {
            a = dao.queryInt(sql, record.getRowguid());
            //存在
            if (a > 0) {
                b = dao.update(record);
            }
            else {
                b = dao.insert(record);
            }
        }
        dao.close();
        return b;
    }

    public int insertCompanyProcess(AuditOnlineCompany record) {
        CommonDao dao = new CommonDao();
        String sql = "select count(1) from audit_online_company where rowguid = ?";
        int a = 0;
        int b = 0;
        if (StringUtil.isNotBlank(record.getRowguid())) {
            a = dao.queryInt(sql, record.getRowguid());
            //存在
            if (a > 0) {
                b = dao.update(record);
            }
            else {
                b = dao.insert(record);
            }
        }
        dao.close();
        return b;
    }

    /**
     * 工建查询法人所拥有公司
     *  @param creditcode
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public AuditRsCompanyBaseinfo getAuditRsCompanyBaseinfo(String creditcode) {
        CommonDao dao = new CommonDao();
        String sql = "select * from Audit_Rs_Company_Baseinfo where creditcode = ?";
        AuditRsCompanyBaseinfo auditRsCompanyBaseinfo = dao.find(sql, AuditRsCompanyBaseinfo.class, creditcode);
        dao.close();
        return auditRsCompanyBaseinfo;
    }

    /**
     * 
     * @Description: 根据cliengguid查询attachguid
     * @author male   
     * @date 2019年9月18日 下午4:57:19
     * @return List<String>    返回类型    
     * @throws
     */
    public List<String> getAttachguidByClientguid(String cliengguid) {
        String sql = "select attachguid from frame_attachinfo where CLIENGGUID = ?";
        return baseDao.findList(sql, String.class, cliengguid);
    }

    /**
     * 
     * @Description: 根据flowsn查询事项信息 
     * @author male   
     * @date 2019年10月20日 下午1:05:06
     * @return Record    返回类型    
     * @throws
     */
    public Record getTaskInfoByFlowsn(String flowsn) {
        String sql = "select rowguid,taskguid from audit_project where flowsn = ?";
        return baseDao.find(sql, Record.class, flowsn);
    }

    /**
     * 
     * @Description: 单点登录如果
     * @author male   
     * @date 2019年10月21日 下午3:39:47
     * @return int    返回类型    
     * @throws
     */
    public int insertCompanyGrantInfo(JSONObject companyInfo, String cardid) {
        //企业法人身份证 
        String cardnumber = "";
        if (companyInfo == null)
            return 0;
        //根据身份证号查询Accountguid
        String sql = "select ACCOUNTGUID from audit_online_register where IDNUMBER = ?";
        String accountguid = baseDao.queryString(sql, cardid);

        int a = 0;
        if (StringUtil.isNotBlank(accountguid) && StringUtil.isNotBlank(cardid)) {
            cardnumber = companyInfo.getString("cardnumber");
            //企业法人
            AuditOnlineCompany company = new AuditOnlineCompany();
            if (cardid.equals(cardnumber)) {
                company.setRowguid(UUID.randomUUID().toString());
                company.setAccountguid(accountguid);
                company.setOperatedate(new Date());
                //判断类型
                if ("1".equals(companyInfo.getString("corcodetype"))) {
                    //统一社会信用代码
                    company.setCreditcode(companyInfo.getString("creditcode"));
                }
                else if ("3".equals(companyInfo.getString("corcodetype"))) {
                    company.setOrgancode(companyInfo.getString("creditcode"));
                }
                //企业名称
                company.setOrganname(companyInfo.getString("corname"));
                //地址
                company.setDeptaddress(companyInfo.getString("regaddress"));
                //经营范围
                company.setJyfw(companyInfo.getString("scope"));
                //法人或负责人
                company.setLegal(companyInfo.getString("realname"));
                //插入  TODO：需要根据信用代码判断是否已经有企业，有则更新，无则插入

            }
            else {
                //系统经办人需要授权，插入前因根据
                //经办人
                AuditOnlineCompanyGrant companyGrant = new AuditOnlineCompanyGrant();
                companyGrant.setRowguid(UUID.randomUUID().toString());
                companyGrant.setOperatedate(new Date());
                //TODO：这边需要判断是否company表有企业信息,包括企业的法人信息，accountguid等
                // companyGrant.setCompanyid("");
                //授权人姓名
                companyGrant.setSqusername("");
                //授权人accountguid
                companyGrant.setSquserguid("");
                //授权人证件信息
                companyGrant.setSqidnum("");
                //授权时间
                companyGrant.setSqtime(new Date());
                //被授权人姓名，guid，身份证号
                companyGrant.setBsqusername(companyInfo.getString("realname"));
                //这边应该单点登录那边传过来
                companyGrant.setBsquserguid("");
                companyGrant.setBsqidnum("");
                //是否解除授权 0否  1是
                companyGrant.setIsrelieve("0");
                //过期时间 
                //companyGrant.setExpiredate();
                //被授权级别2 企业管理者 3 代办人
                companyGrant.setBsqlevel("3");
                //被授权天数
                companyGrant.setBsqdays("");
                //被授权时限，代码项
                companyGrant.setBsqtimelimit("");
                //管理者是否激活 1是 
                companyGrant.setM_IsActive("");
                //TODO : 插入
            }

        }

        return 0;
    }

    /**
     * 
     * @Description: 根据公司唯一标识查询企业信息
     * @author male   
     * @date 2020年4月2日 下午4:03:04
     * @return String    返回类型    
     * @throws
     */
    public AuditOnlineCompanyGrant getIAuditOnlineCompanyGrantByCompanyID(String companyid) {
        String sql = "select * from AUDIT_ONLINE_COMPANY_GRANT where companyid = ?";
        return baseDao.find(sql, AuditOnlineCompanyGrant.class, companyid);
    }

    /**
     * 
     * @Description: 根据身份证号查询注册用户accountguid
     * @author male   
     * @date 2019年10月21日 下午6:28:53
     * @return String    返回类型    
     * @throws
     */
    public String getAccountGuidByCardID(String cardid) {
        String sql = "select ACCOUNTGUID from audit_online_register where IDNUMBER = ?";
        return baseDao.queryString(sql, cardid);
    }
    
    public AuditTask getAuditTaskByNewItemCode(String code) {
        String sql = "select Shenpilb,areacode,rowguid,task_id,ouguid,ouname,Taskname,Promise_day,type from audit_task where is_history='0' and IS_ENABLE=1 and IS_EDITAFTERIMPORT=1 and inner_code='" + code + "' limit 1 ";
        return baseDao.find(sql, AuditTask.class);
    }
    

}
