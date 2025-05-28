package com.epoint.xmz.thirdreporteddata.basic.spglv3.service;

import com.epoint.common.service.AuditCommonResult;
import com.epoint.common.util.SQLManageUtil;
import com.epoint.core.BaseEntity;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.collection.EpointCollectionUtils;
import com.epoint.core.utils.log.LogUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.domain.SpglXmspsxblxxxxbV3;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 住建部_项目审批事项办理详细信息表对应的后台service
 *
 * @author zhpengsy
 * @version [版本号, 2018-11-16 15:09:04]
 */
public class SpglXmspsxblxxxxbV3Service {

    transient Logger log = LogUtil.getLog(SpglXmspsxblxxxxbV3Service.class);
    /**
     * 数据增删改查组件
     */
    protected ICommonDao baseDao;

    public SpglXmspsxblxxxxbV3Service() {
        baseDao = CommonDao.getInstance();
    }

    /**
     * 插入数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(SpglXmspsxblxxxxbV3 record) {
        return baseDao.insert(record);
    }

    /**
     * 删除数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public <T extends Record> int deleteByGuid(String guid) {
        T t = baseDao.find(SpglXmspsxblxxxxbV3.class, guid);
        return baseDao.delete(t);
    }

    /**
     * 更新数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(SpglXmspsxblxxxxbV3 record) {
        return baseDao.update(record);
    }

    /**
     * 根据ID查找单个实体
     *
     * @param clazz      类<必须继承BaseEntity>
     * @param primaryKey 主键
     * @return T extends BaseEntity
     */
    public SpglXmspsxblxxxxbV3 find(Object primaryKey) {
        return baseDao.find(SpglXmspsxblxxxxbV3.class, primaryKey);
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
    public SpglXmspsxblxxxxbV3 find(String sql, Object... args) {
        return baseDao.find(sql, SpglXmspsxblxxxxbV3.class, args);
    }

    /**
     * 查找一个list
     *
     * @param sql   查询语句
     * @param clazz 可以是[Record.class(弱类型);FrameOu.class(强类型);Object[].class]
     * @param args  参数值数组
     * @return T extends BaseEntity
     */
    public List<SpglXmspsxblxxxxbV3> findList(String sql, Object... args) {
        return baseDao.findList(sql, SpglXmspsxblxxxxbV3.class, args);
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
    public List<SpglXmspsxblxxxxbV3> findList(String sql, int pageNumber, int pageSize, Object... args) {
        return baseDao.findList(sql, pageNumber, pageSize, SpglXmspsxblxxxxbV3.class, args);
    }

    public List<SpglXmspsxblxxxxbV3> findListByflowsn(String flowsn) {
        String sql = "select * from SPGL_XMSPSXBLXXXXB_V3 where SPSXSLBM=?";
        return baseDao.findList(sql, SpglXmspsxblxxxxbV3.class, flowsn);
    }

    public AuditCommonResult<PageData<SpglXmspsxblxxxxbV3>> getAllByPage(Map<String, String> map, int first, int pageSize,
                                                                         String sortField, String sortOrder) {
        AuditCommonResult<PageData<SpglXmspsxblxxxxbV3>> auditCommonResult = new AuditCommonResult<PageData<SpglXmspsxblxxxxbV3>>();
        try {
            SQLManageUtil sqlManageUtil = new SQLManageUtil(SpglXmspsxblxxxxbV3.class);
            PageData<SpglXmspsxblxxxxbV3> pageData = sqlManageUtil.getDbListByPage(SpglXmspsxblxxxxbV3.class, map, first,
                    pageSize, sortField, sortOrder);
            auditCommonResult.setResult(pageData);
        } catch (Exception e) {
            log.info(e.getMessage(), e);
            auditCommonResult.setBusinessFail(e.getMessage());
        }
        return auditCommonResult;
    }

    public List<SpglXmspsxblxxxxbV3> getAllRecord(Class<? extends BaseEntity> baseClass,
                                                  Map<String, String> conditionMap) {
        SQLManageUtil sqlManageUtil = new SQLManageUtil(baseClass);
        return sqlManageUtil.getListByCondition(baseClass, conditionMap);
    }

    public Integer getAllRecordCount(Class<? extends BaseEntity> baseClass, Map<String, String> conditionMap) {
        List<SpglXmspsxblxxxxbV3> result = getAllRecord(baseClass, conditionMap);
        if (result != null) {
            return result.size();
        } else {
            return 0;
        }

    }

    public PageData<SpglXmspsxblxxxxbV3> getAllRecordByPage(Class<? extends BaseEntity> baseClass,
                                                            Map<String, String> conditionMap, Integer first, Integer pageSize, String sortField, String sortOrder) {
        SQLManageUtil sqlManageUtil = new SQLManageUtil(baseClass);
        return sqlManageUtil.getDbListByPage(baseClass, conditionMap, first, pageSize, sortField, sortOrder);
    }

    public PageData<SpglXmspsxblxxxxbV3> getRiskRepeatData(Map<String, String> conditionMap, Integer first,
                                                           Integer pageSize, String sortField, String sortOrder) {
        PageData<SpglXmspsxblxxxxbV3> result = new PageData<SpglXmspsxblxxxxbV3>();
        String conditionsql = new SQLManageUtil().buildPatchSql(conditionMap);
        String sql = "select * from SPGL_XMSPSXBLXXXXB_V3 where  dfsjzj in ( select dfsjzj from SPGL_XMSPSXBLXXXXB_V3 where SJSCZT in ('0','1','3') and sjyxbs='1' group by dfsjzj,GCDM,SPSXSLBM,BLZT having count(1)>1) "
                + conditionsql;
        result.setList(baseDao.findList(sql, first, pageSize, SpglXmspsxblxxxxbV3.class));
        result.setRowCount(baseDao.queryInt(sql.replace("*", "count(1)")));
        return result;
    }

    public List<String> filterHjltSxslbm(List<String> spsxslbm) {
        if (EpointCollectionUtils.isEmpty(spsxslbm)) {
            return new ArrayList<>();
        }
        String joinSql = StringUtil.joinSql(spsxslbm);
        String sql = "select SPSXSLBM, group_concat(BLZT) blzts from spgl_xmspsxblxxxxb_v3 sx where SPSXSLBM in ("
                + joinSql
                + ") and  SJYXBS = '1' group by SPSXSLBM having(blzts not like '1%') or (blzts regexp '3,[^8]' or blzts regexp '[^8],3') or  (blzts regexp '6,[^7]' or blzts regexp '[^6],7') or (blzts regexp '9,([2-9]|1,|11|12|13)' or blzts regexp '[^9],10') or (blzts regexp '11|12|13' and blzts not regexp '3,8') ";
        List<Record> findList = baseDao.findList(sql, Record.class);
        return findList.stream().map(a -> a.getStr("spsxslbm")).collect(Collectors.toList());
    }

}
