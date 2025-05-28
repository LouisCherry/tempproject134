package com.epoint.auditproject.auditproject.service;

import com.epoint.auditproject.auditproject.api.entity.JnTcId;
import com.epoint.auditproject.entity.csfxxb;
import com.epoint.auditproject.entity.jbxxb;
import com.epoint.auditproject.entity.msfxxb;
import com.epoint.auditproject.zjxt.entity.AuditProjectZjxt;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.common.service.AuditCommonResult;
import com.epoint.common.util.SQLManageUtil;
import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.jdbc.connection.DataSourceConfig;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;

import java.util.List;
import java.util.Map;

/**
 * 建筑业企业资质数据库对应的后台service
 *
 * @author 86180
 * @version [版本号, 2020-04-15 11:11:06]
 */
public class JnProjectService {

    private static String URL = ConfigUtil.getConfigValue("datasyncjdbc", "qzzjurl");
    private static String NAME = ConfigUtil.getConfigValue("datasyncjdbc", "qzzjusername");
    private static String PASSWORD = ConfigUtil.getConfigValue("datasyncjdbc", "qzzjpassword");

    private static String qzkURL = ConfigUtil.getConfigValue("datasyncjdbc", "zzkqzkurl");
    private static String qzkNAME = ConfigUtil.getConfigValue("datasyncjdbc", "zzkqzkname");
    private static String qzkPASSWORD = ConfigUtil.getConfigValue("datasyncjdbc", "zzkqzkpassword");

    private static String DZBDURL = ConfigUtil.getConfigValue("datasyncjdbc", "dzbdurl");
    private static String DZBDNAME = ConfigUtil.getConfigValue("datasyncjdbc", "dzbdusername");
    private static String DZBDPASSWORD = ConfigUtil.getConfigValue("datasyncjdbc", "dzbdpassword");

    /**
     * 数据增删改查组件
     */
    protected ICommonDao baseDao;

    protected ICommonDao commonDaoFrom = null;

    protected ICommonDao commonDaoDzbd;

    protected ICommonDao commonDaoQZK;

    private DataSourceConfig dataSourceConfig = new DataSourceConfig(URL, NAME, PASSWORD);
    private DataSourceConfig dataSourceConfigDzbd = new DataSourceConfig(DZBDURL, DZBDNAME, DZBDPASSWORD);
    private DataSourceConfig dataSourceConfigQZK = new DataSourceConfig(qzkURL, qzkNAME, qzkPASSWORD);

    public JnProjectService() {
        baseDao = CommonDao.getInstance();
        // commonDaoFrom = CommonDao.getInstance(dataSourceConfig); // 数据库连接不上，影响到了其他功能，暂时先注释掉，放到调用方法初始化
        commonDaoDzbd = CommonDao.getInstance(dataSourceConfigDzbd);
        commonDaoQZK = CommonDao.getInstance(dataSourceConfigQZK);
    }

    public ICommonDao getCommonDaoFrom() {
        commonDaoFrom = CommonDao.getInstance(dataSourceConfig);
        return commonDaoFrom;
    }

    public ICommonDao getCommonDaoDzbd() {
        return commonDaoDzbd;
    }

    /**
     * 插入数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(JnTcId record) {
        return baseDao.insert(record);
    }

    /**
     * 插入数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insertJbxxb(jbxxb record) {
        commonDaoFrom = CommonDao.getInstance(dataSourceConfig);
        return commonDaoFrom.insert(record);
    }

    /**
     * 插入数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insertCsfxxb(csfxxb record) {
        commonDaoFrom = CommonDao.getInstance(dataSourceConfig);
        return commonDaoFrom.insert(record);
    }

    /**
     * 插入数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insertMsfxxb(msfxxb record) {
        commonDaoFrom = CommonDao.getInstance(dataSourceConfig);
        return commonDaoFrom.insert(record);
    }

    /**
     * 删除数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public <T extends Record> int deleteByGuid(String guid) {
        T t = baseDao.find(JnTcId.class, guid);
        return baseDao.delete(t);
    }

    /**
     * 更新数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(JnTcId record) {
        return baseDao.update(record);
    }

    /**
     * 更新数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int updateRecord(Record record) {
        int result =commonDaoQZK.update(record);
        
        return result;
    }

    /**
     * 根据ID查找单个实体
     *
     * @param clazz      类<必须继承BaseEntity>
     * @param primaryKey 主键
     * @return T extends BaseEntity
     */
    public JnTcId find(Object primaryKey) {
        return baseDao.find(JnTcId.class, primaryKey);
    }

    /**
     * 查找单条记录
     *
     * @param sql   查询语句
     * @param clazz 可以是[Record.class(弱类型);FrameOu.class(强类型);Object[].class，返回一个数组
     *              ;String.class;Integer.class;Long.class]
     * @param args  参数值数组
     * @return T {String、Integer、Long、Record、FrameOu、Object[]等}
     */
    public JnTcId find(String sql, Object... args) {
        return baseDao.find(sql, JnTcId.class, args);
    }

    /**
     * 查找一个list
     *
     * @param sql   查询语句
     * @param clazz 可以是[Record.class(弱类型);FrameOu.class(强类型);Object[].class]
     * @param args  参数值数组
     * @return T extends BaseEntity
     */
    public List<JnTcId> findList(String sql, Object... args) {
        return baseDao.findList(sql, JnTcId.class, args);
    }

    /**
     * 分页查找一个list
     *
     * @param sql        查询语句
     * @param pageNumber 记录行的偏移量
     * @param pageSize   记录行的最大数目
     * @param clazz      可以是[Record.class(弱类型);FrameOu.class(强类型);Object[].class]
     * @param args       参数值数组
     * @return T extends BaseEntity
     */
    public List<JnTcId> findList(String sql, int pageNumber, int pageSize, Object... args) {
        return baseDao.findList(sql, pageNumber, pageSize, JnTcId.class, args);
    }

    /**
     * 新增某条记录
     *
     * @param baseClass
     * @param record
     * @param useCache
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public void addRecord(Class<? extends BaseEntity> baseClass, Record record) {
        commonDaoFrom = CommonDao.getInstance(dataSourceConfig);
        Entity en = baseClass.getAnnotation(Entity.class);
        if (StringUtil.isNotBlank(en.table())) {
            commonDaoFrom.insert(record);
        }
    }

    /**
     * 查询数量
     *
     * @param sql  执行语句
     * @param args 参数
     * @return Integer
     */
    public Integer countJnTcId(String sql, Object... args) {
        return baseDao.queryInt(sql, args);
    }

    public int updateChargeDetail(String rowguid) {
        String sql = "update audit_project_chargedetail set chargestatus = '1' where rowguid = ? ";
        int result = CommonDao.getInstance().execute(sql, rowguid);
        return result;
    }

    public JnTcId getDzbdForBusiness(String yewguid) {
        String sql = "select * from jn_tc_jd where yewuguid = ?";
        JnTcId record = CommonDao.getInstance().find(sql, JnTcId.class, yewguid);
        return record;
    }

    public void insertCodeItemForTxxl(String addvaluetxxl) {
        String sql = "insert into code_items(itemvalue, codeid, orderno, itemlevcode, itemtext) values(?, '1016092', '0', ?, ?) ";
        baseDao.execute(sql, addvaluetxxl, addvaluetxxl, addvaluetxxl);
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

    public Record DzbdItemBaseinfoByProjectGuid(String rowguid) {
        String sql = "select * from fcjy where rowguid = ?";
        return commonDaoDzbd.find(sql, Record.class, rowguid);
    }

    public List<Record> findSource() {
        commonDaoFrom = CommonDao.getInstance(dataSourceConfig);
        String sql = "select * from source where flag = '0' ";
        return commonDaoFrom.findList(sql, Record.class);
    }

    public void updateSource(String flag, String flowsn) {
        commonDaoFrom = CommonDao.getInstance(dataSourceConfig);
        String sql = "update source set flag = ? where flowsn = ?";
        commonDaoFrom.execute(sql, flag, flowsn);
    }

    public AuditProject getProjectByflowsn(String flowsn) {
        String sql = "SELECT * from audit_project WHERE flowsn =?";
        return baseDao.find(sql, AuditProject.class, flowsn);
    }

    public void inserRecord(Record record) {
        commonDaoQZK.insert(record);
        
    }

    public void inserCsjzlj(Record record) {
        commonDaoDzbd.insert(record);
    }

    public boolean checkSignByCliengguid(String cliengguid) {
        String sql = "select signstatus from frame_attachinfo where cliengguid= ? limit 1";
        String res = baseDao.queryString(sql, cliengguid);
        if ("1".equals(res)) {
            return true;
        }
        return false;
    }

    public Record getYljgfsjshByNumber(String zmwh) {
        String sql = "select * from ex_yljgfspjsh_1 where LICENSE_NUMBER = ?";
        Record record =  commonDaoQZK.find(sql, Record.class, zmwh);
        
        return record;
    }

    public Record getGgcswsxkxsqByNumber(String zmwh) {
        String sql = "select * from ex_ggcswsxkxsq_1 where LICENSE_NUMBER = ?";
        Record record= commonDaoQZK.find(sql, Record.class, zmwh);
        
        return record;
    }

    public Record getYljgfsjgysByNumber(String zmwh) {
        String sql = "select * from ex_yljgfsjgys_1 where LICENSE_NUMBER = ?";
        Record record= commonDaoQZK.find(sql, Record.class, zmwh);
        
        return record;
    }

    public Record getYlggsczmByZmwh(String zmwh) {
        String sql = "select * from ex_ylggsczm_1 where zmwh = ?";
        Record record= commonDaoQZK.find(sql, Record.class, zmwh);
        
        return record;
    }

    public Record getCshwggpzsByZmwh(String wtsbh) {
        String sql = "select * from ex_cshwggpzs_1 where zmwh = ?";
        Record record= commonDaoQZK.find(sql, Record.class, wtsbh);
        
        return record;
    }

    public Record getTsqkyswtsByWtsbh(String zmwh) {
        String sql = "select * from ex_tsqkyswts_1 where wtsbh = ?";
        Record record= commonDaoQZK.find(sql, Record.class, zmwh);
        
        return record;
    }

    public AuditCommonResult<PageData<AuditProjectZjxt>> getAuditProjectZjxtPageData(Map<String, String> map, int first,
                                                                                     int pageSize, String string, String string2) {
        AuditCommonResult<PageData<AuditProjectZjxt>> result = new AuditCommonResult<PageData<AuditProjectZjxt>>();
        try {
            PageData<AuditProjectZjxt> projectList = new PageData<AuditProjectZjxt>();

            SQLManageUtil sqlManageUtil = new SQLManageUtil(AuditProjectZjxt.class);
            projectList = sqlManageUtil.getDbListByPage(AuditProjectZjxt.class, map, first, pageSize, string, string2);
            result.setResult(projectList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public Record getDzbdDetailByZzbh(String tablename, String zzbh) {
        String sql = " select * from " + tablename + " where LICENSE_NUMBER = ? ";
        Record record= commonDaoQZK.find(sql, Record.class, zzbh);
        
        return record;
    }

    public Record getDzbdDetailByfield(String tablename,String field, String zzbh) {
        String sql = " select * from " + tablename + " where "+field+" = ? ";
        Record record= commonDaoQZK.find(sql, Record.class, zzbh);
        
        return record;
    }

    public Record getSpfysxkByRowguid(String rowguid) {
        String sql = "select * from formtable20221024163724 where rowguid = ?";
        return commonDaoDzbd.find(sql, Record.class, rowguid);
    }

    public boolean checkIsky(String projectguid) {
        String sql = "select count(1) from ky_info where projectguid = ?";
        return baseDao.queryBoolean(sql, projectguid);
    }

}
