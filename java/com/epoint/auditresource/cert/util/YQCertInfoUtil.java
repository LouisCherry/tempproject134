package com.epoint.auditresource.cert.util;

import com.epoint.core.dao.CommonDao;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.database.jdbc.connection.DataSourceConfig;

public class YQCertInfoUtil
{
    private static String URL = ConfigUtil.getConfigValue("datasyncjdbc", "zzkqzkurl");
    private static String NAME = ConfigUtil.getConfigValue("datasyncjdbc", "zzkqzkname");
    private static String PASSWORD = ConfigUtil.getConfigValue("datasyncjdbc", "zzkqzkpassword");

    static DataSourceConfig dataSourceConfig = new DataSourceConfig(URL, NAME, PASSWORD);

    public static int insertInfo(Record record) {
        String sql = "INSERT INTO ex_hdglfwnjsfasczysxzxkjds_1(RowGuid, LICENSE_NUMBER, CERTIFICATE_DATE, VALID_PERIOD_BEGIN, VALID_PERIOD_END, DEPT_ORGANIZE_CODE, DEPT_NAME, DISTRICTS_CODE, DISTRICTS_NAME, HOLDER_TYPE, HOLDER_NAME, CERTIFICATE_TYPE, CERTIFICATE_NO, CONTACT_PHONE, PROJECT_NAME, STATE, PERMIT_CONTENT, CERTIFICATE_LEVEL, GUANYUXX, DANWEIMINGCHEN, BAOSONGBAOGAO, DUIXXSHENCHA, FUJIANMINGCHEN, OperateDate) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        return CommonDao.getInstance(dataSourceConfig).execute(sql, record.getStr("Rowguid"),
                record.getStr("LICENSE_NUMBER"), record.getDate("CERTIFICATE_DATE"),
                record.getDate("VALID_PERIOD_BEGIN"), record.getDate("VALID_PERIOD_END"),
                record.getStr("DEPT_ORGANIZE_CODE"), record.getStr("DEPT_NAME"), record.getStr("DISTRICTS_CODE"),
                record.getStr("DISTRICTS_NAME"), record.getStr("HOLDER_TYPE"), record.getStr("HOLDER_NAME"),
                record.getStr("CERTIFICATE_TYPE"), record.getStr("CERTIFICATE_NO"), record.getStr("CONTACT_PHONE"),
                record.getStr("PROJECT_NAME"), record.getStr("STATE"), record.getStr("PERMIT_CONTENT"),
                record.getStr("CERTIFICATE_LEVEL"), record.getStr("GUANYUXX"), record.getStr("DANWEIMINGCHEN"),
                record.getStr("BAOSONGBAOGAO"), record.getStr("DUIXXSHENCHA"), record.getStr("FUJIANMINGCHEN"),
                record.getDate("OperateDate"));
    }

}
