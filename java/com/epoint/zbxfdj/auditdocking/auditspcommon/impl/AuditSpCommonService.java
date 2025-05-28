package com.epoint.zbxfdj.auditdocking.auditspcommon.impl;

import com.epoint.common.util.SQLManageUtil;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.jdbc.connection.DataSourceConfig;

import java.util.List;
import java.util.Map;

/**
 * 工程信息表对应的后台service
 * 
 * @author WZW
 * @version [版本号, 2022-12-07 14:07:28]
 */
public class AuditSpCommonService
{
    /**
     * 数据增删改查组件
     */
    protected ICommonDao baseDao;

    public AuditSpCommonService() {
        String eformdatasourceurl = ConfigUtil.getConfigValue("datasyncjdbc", "dzbdurl");// 电子表单系统数据库地址
        String eformdatasourceusername = ConfigUtil.getConfigValue("datasyncjdbc", "dzbdusername");// 电子表单系统数据库登录名
        String eformdatasourcepassword = ConfigUtil.getConfigValue("datasyncjdbc", "dzbdpassword");// 电子表单系统数据库密码
        if (StringUtil.isNotBlank(eformdatasourceurl) && StringUtil.isNotBlank(eformdatasourceusername)
                && StringUtil.isNotBlank(eformdatasourcepassword)) {
            DataSourceConfig dataSourceConfig = new DataSourceConfig(eformdatasourceurl, eformdatasourceusername,
                    eformdatasourcepassword);
            if (dataSourceConfig != null) {
                baseDao = CommonDao.getInstance(dataSourceConfig);
            }

        }
    }


    /**
     * 查找一个list
     * 
     * @param conditionMap
     *            查询条件集合
     * @return T extends BaseEntity
     */
    public List<Record> findList(Map<String, String> conditionMap,String tableName) {
        String sqlCondition = new SQLManageUtil().buildPatchSql(conditionMap);
        StringBuilder sqlBuilder = new StringBuilder();
        if(tableName.contains("AUDIT_SP_SINGLE")){
            sqlBuilder.append("select firedangercategorycode,nature,landuplayernumber,buildingheight,engineeringname," +
                    "buildinglength,fireresisratecode,structuralsystemcode,landdownlayernumber,buildingdownarea,buildinguparea,footprintarea from ").append(tableName).append(" where 1=1 ").append(sqlCondition);
        }
        else if (tableName.contains("AUDIT_SP_COMPANY")){
            sqlBuilder.append("select qualificationlevel,legalrepresentative,uscc,principal,companytype,designmajor,companyname,contactno from ").append(tableName).append(" where 1=1 ").append(sqlCondition);
        }
        else{
            sqlBuilder.append("select * from ").append(tableName).append(" where 1=1 ").append(sqlCondition);
        }
        return baseDao.findList(sqlBuilder.toString(),Record.class);
    }


    public Record find(Map<String, String> conditionMap, String tableName) {
        String sqlCondition = new SQLManageUtil().buildPatchSql(conditionMap);
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("select * from ").append(tableName).append(" where 1=1 ").append(sqlCondition);
        return baseDao.find(sqlBuilder.toString(),Record.class);
    }
}
