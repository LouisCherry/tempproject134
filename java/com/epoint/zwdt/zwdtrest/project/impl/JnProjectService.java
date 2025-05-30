package com.epoint.zwdt.zwdtrest.project.impl;

import com.epoint.basic.auditonlineuser.auditonlinecompany.domain.AuditOnlineCompany;
import com.epoint.basic.auditonlineuser.auditonlineindividual.domain.AuditOnlineIndividual;
import com.epoint.basic.auditorga.auditarea.domain.AuditOrgaArea;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditsp.auditspbusiness.domain.AuditSpBusiness;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.jdbc.connection.DataSourceConfig;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import com.epoint.zwdt.zwdtrest.project.api.entity.JnTcId;

import java.util.ArrayList;
import java.util.List;

/**
 * 建筑业企业资质数据库对应的后台service
 *
 * @author 86180
 * @version [版本号, 2020-04-15 11:11:06]
 */
public class JnProjectService
{
    private static String qzkURL = ConfigUtil.getConfigValue("datasyncjdbc", "zzkqzkurl");
    private static String qzkNAME = ConfigUtil.getConfigValue("datasyncjdbc", "zzkqzkname");
    private static String qzkPASSWORD = ConfigUtil.getConfigValue("datasyncjdbc", "zzkqzkpassword");

    /**
     * 数据增删改查组件
     */
    protected ICommonDao baseDao;

    protected ICommonDao commonDaoQZK;

    private DataSourceConfig dataSourceConfigQZK = new DataSourceConfig(qzkURL, qzkNAME, qzkPASSWORD);

    public JnProjectService() {
        baseDao = CommonDao.getInstance();
        commonDaoQZK = CommonDao.getInstance(dataSourceConfigQZK);
    }

    /**
     * 插入数据
     *
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(JnTcId record) {
        return baseDao.insert(record);
    }

    /**
     * 删除数据
     *
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public <T extends Record> int deleteByGuid(String guid) {
        T t = baseDao.find(JnTcId.class, guid);
        return baseDao.delete(t);
    }

    /**
     * 更新数据
     *
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(JnTcId record) {
        return baseDao.update(record);
    }

    /**
     * 根据ID查找单个实体
     *
     * @param clazz
     *            类<必须继承BaseEntity>
     * @param primaryKey
     *            主键
     * @return T extends BaseEntity
     */
    public JnTcId find(Object primaryKey) {
        return baseDao.find(JnTcId.class, primaryKey);
    }

    /**
     * 查找单条记录
     *
     * @param sql
     *            查询语句
     * @param clazz
     *            可以是[Record.class(弱类型);FrameOu.class(强类型);Object[].class，返回一个数组
     *            ;String.class;Integer.class;Long.class]
     * @param args
     *            参数值数组
     * @return T {String、Integer、Long、Record、FrameOu、Object[]等}
     */
    public JnTcId find(String sql, Object... args) {
        return baseDao.find(sql, JnTcId.class, args);
    }

    /**
     * 查找一个list
     *
     * @param sql
     *            查询语句
     * @param clazz
     *            可以是[Record.class(弱类型);FrameOu.class(强类型);Object[].class]
     * @param args
     *            参数值数组
     * @return T extends BaseEntity
     */
    public List<JnTcId> findList(String sql, Object... args) {
        return baseDao.findList(sql, JnTcId.class, args);
    }

    /**
     * 分页查找一个list
     *
     * @param sql
     *            查询语句
     * @param pageNumber
     *            记录行的偏移量
     * @param pageSize
     *            记录行的最大数目
     * @param clazz
     *            可以是[Record.class(弱类型);FrameOu.class(强类型);Object[].class]
     * @param args
     *            参数值数组
     * @return T extends BaseEntity
     */
    public List<JnTcId> findList(String sql, int pageNumber, int pageSize, Object... args) {
        return baseDao.findList(sql, pageNumber, pageSize, JnTcId.class, args);
    }

    /**
     * 更新数据
     *
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int updateRecord(Record record) {
        String sql = "update ex_ggcswsxkxsq_1 set state = 'cancel',operatedate=now() where LICENSE_NUMBER = '"
                + record.getStr("LICENSE_NUMBER") + "'";
        commonDaoQZK.execute(sql);
        return 1;
    }

    public int CancelRecord(String tableName, String licenNumber) {
        String sql = "update " + tableName + " set state = 'cancel',operatedate=now() where LICENSE_NUMBER = '"
                + licenNumber + "'";
        commonDaoQZK.execute(sql);
        return 1;
    }

    /**
     * 查询数量
     *
     * @param sql
     *            执行语句
     * @param args
     *            参数
     * @return Integer
     */
    public Integer countJnTcId(String sql, Object... args) {
        return baseDao.queryInt(sql, args);
    }

    public int updateChargeDetail(String rowguid) {
        String sql = "update audit_project_chargedetail set chargestatus = '1' where rowguid = '" + rowguid + "'";
        int result = CommonDao.getInstance().execute(sql);
        CommonDao.getInstance().close();
        return result;
    }

    public JnTcId getDzbdForBusiness(String yewguid) {
        String sql = "select * from jn_tc_jd where yewuguid = ?";
        JnTcId record = CommonDao.getInstance().find(sql, JnTcId.class, yewguid);
        return record;
    }

    public void insertCodeItemForTxxl(String addvaluetxxl) {
        String sql = "insert into code_items(itemvalue, codeid, orderno, itemlevcode, itemtext) values('" + addvaluetxxl
                + "', '1016092', '0', '" + addvaluetxxl + "', '" + addvaluetxxl + "') ";
        baseDao.execute(sql);
        baseDao.close();
    }

    public FrameAttachInfo getFrameAttachByIdenumber(String idnumber) {
        String sql = "select * from frame_attachinfo where idnumber = ? order by uploaddatetime desc limit 1";
        return baseDao.find(sql, FrameAttachInfo.class, idnumber);
    }

    public List<FrameAttachInfo> getFrameAttachByIdenumberBigType(String idnumber, String bigshowtype) {
        String sql = "select * from frame_attachinfo where idnumber = ?1 and bigshowtype = ?2 order by uploaddatetime desc";
        return baseDao.findList(sql, FrameAttachInfo.class, idnumber, bigshowtype);
    }

    public List<FrameAttachInfo> getFrameAttachByIdenumberTag(String idnumber, String cliengtag) {
        String sql = "select * from frame_attachinfo where idnumber = ?1 and cliengtag = ?2";
        return baseDao.findList(sql, FrameAttachInfo.class, idnumber, cliengtag);
    }

    public List<Record> getNoEvaluteProject(String time) {
        String sql = "SELECT rowguid,taskguid,areacode FROM AUDIt_PROJECT WHERE 1 = 1 AND STATUS = '90' AND DATE_FORMAT(APPLYDATE, '%Y%m%d') LIKE ? ";
        return baseDao.findList(sql, Record.class, time);
    }

    public AuditSpBusiness getAuditBusinessByName(String name, String areacode) {
        String sql = "select * from audit_sp_business where businessname = ? and areacode = ? and del = '0'";
        return baseDao.find(sql, AuditSpBusiness.class, name, areacode);
    }

    public Record getGgcswsxkxsqByNumber(String zmwh) {
        String sql = "select * from ex_ggcswsxkxsq_1 where LICENSE_NUMBER = ?";
        return commonDaoQZK.find(sql, Record.class, zmwh);
    }

    public Record getDzbdDetailByZzbh(String tablename, String zzbh) {
        String sql = " select * from " + tablename + " where LICENSE_NUMBER = ? ";
        return commonDaoQZK.find(sql, Record.class, zzbh);
    }

    public List<AuditOrgaArea> selectAuditAreaList() {
        // String sql = "select * from AUDIT_ORGA_AREA where 1=1 and (citylevel in (1,2)
        // or xiaqucode = '370811010') order by ordernum desc ";
        String sql = "select  * from AUDIT_ORGA_AREA where citylevel in (1,2,3) order by ordernum desc ";
        return baseDao.findList(sql, AuditOrgaArea.class);
    }

    public List<AuditProject> selectAuditProjectByBiguid(String biguid) {
        String sql = "select certrowguid,taskguid,projectname,rowguid from audit_project where status = '90' and biguid = ?";
        return baseDao.findList(sql, AuditProject.class, biguid);
    }

    public List<Record> getBiguidsByParentId(String parentguid) {
        String sql = "select distinct biguid from AUDIT_SP_I_SUBAPP where yewuguid in (select rowguid from AUDIT_RS_ITEM_BASEINFO where parentid = ? and IFNULL(is_history,0) = 0)";
        return baseDao.findList(sql, Record.class, parentguid);
    }

    public List<Record> getBiguidsByYewuguid(String yewuguid) {
        String sql = "select distinct biguid from AUDIT_SP_I_SUBAPP where yewuguid = ?";
        return baseDao.findList(sql, Record.class, yewuguid);
    }

    public PageData<AuditSpBusiness> getAuditSpBusinessByPage(Record record, int pageIndex, int pageSize) {
        String sql = "select * from audit_sp_business where 1=1 and businesstype = '2' and del = '0' ";
        String sqlcount = "select count(1) from audit_sp_business where 1=1 and businesstype = '2' and del = '0' ";
        List<String> params = new ArrayList<>();
        if (StringUtil.isNotBlank(record.getStr("businessname")) && StringUtil.isNotBlank(record.getStr("keyword"))) {
            // 新增关键词搜索
            sql += " and (businessname like ? or keywordcontent like ? )";
            sqlcount += " and (businessname like ? or keywordcontent like ? )";
            params.add("%" + record.getStr("businessname") + "%");
            params.add("%" + record.getStr("keyword") + "%");
        }
        if (StringUtil.isNotBlank(record.getStr("areacode"))) {
            sql += " and areacode = ? ";
            sqlcount += " and areacode = ? ";
            params.add(record.getStr("areacode"));
        }
        PageData<AuditSpBusiness> pageData = new PageData<>();
        List<AuditSpBusiness> list = baseDao.findList(sql, pageIndex, pageSize, AuditSpBusiness.class,
                params.toArray());
        int count = baseDao.queryInt(sqlcount, params.toArray());
        pageData.setList(list);
        pageData.setRowCount(count);
        return pageData;
    }

    public String getSdqYjsBusinessGuid(String areacode, String businessname) {
        String sql = "select rowguid from AUDIT_SP_BUSINESS\n" + "where BUSINESSNAME like '%" + businessname + "%' \n"
                + "  and BUSINESSTYPE = 2 and areacode = ?";
        return baseDao.queryString(sql, areacode);
    }

    public AuditOnlineCompany getOnlineCompany(String itemlegalcertnum) {
        String sql = "select * from audit_online_company where CREDITCODE = ?";
        return baseDao.find(sql, AuditOnlineCompany.class, itemlegalcertnum);
    }

    public AuditOnlineIndividual getOnlineIndividualByCertnum(String itemlegalcertnum) {
        String sql = "select a.* from audit_online_individual a , audit_online_register b where a.IDNUMBER = ? and a.ACCOUNTGUID = b.ACCOUNTGUID";
        return baseDao.find(sql, AuditOnlineIndividual.class, itemlegalcertnum);
    }
}
