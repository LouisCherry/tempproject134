package com.epoint.wsxznsb.auditqueue.service;

import java.util.List;
import java.util.Map;

import com.epoint.basic.cache.Cache;
import com.epoint.basic.cache.CacheService9;
import com.epoint.common.util.SQLManageUtil;
import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.jdbc.config.SplitTableConfig;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.frame.service.metadata.sharding.util.ShardingUtil;

@Cache
public class WsxAuditQueueBasicService<T> extends CacheService9
{

    /**
     * 
     */
    private static final long serialVersionUID = 1223708331052361525L;

    /**
     * 通用dao
     */
    private ICommonDao commonDao;

    public WsxAuditQueueBasicService() {
        commonDao = CommonDao.getInstance();
    }

    public WsxAuditQueueBasicService(Class<? extends BaseEntity> baseClass) {
        Entity en = baseClass.getAnnotation(Entity.class);
        SplitTableConfig conf = ShardingUtil.getSplitTableConfig(en.table());
        if (conf != null) {
            commonDao = CommonDao.getInstance(conf);
        }
        else {
            commonDao = CommonDao.getInstance();
        }
    }

    /**
     * 
     * 根据条件查询
     * 
     * @param fieldstr
     *            查询字段
     * @param conditionMap
     *            条件map， key为字段名称，value为值
     * @param first
     *            起始记录数
     * @param pageSize
     *            最大记录数
     * @param sortField
     *            排序值
     * @param sortOrder
     *            排序字段
     * @return PageData<AuditTask>
     */
    @SuppressWarnings({"unchecked", "hiding" })
    public <T> PageData<T> getRecordPageData(String fieldstr, Class<? extends BaseEntity> baseClass,
            Map<String, String> conditionMap, Integer first, Integer pageSize, String sortField, String sortOrder) {
        SQLManageUtil sqlManageUtil = new SQLManageUtil(baseClass);
        PageData<T> pageData = new PageData<T>();
        Entity en = baseClass.getAnnotation(Entity.class);
        // 条件sql
        StringBuffer sb = new StringBuffer();
        sb.append(sqlManageUtil.buildSql(conditionMap));
        // 增加Orderby语句
        sb.append(" and rowguid in (select distinct tasktypeguid from audit_queue_window_tasktype) ");
        if (StringUtil.isNotBlank(sortField)) {
            sb.append(" order by " + sortField + " " + sortOrder);
        }
        String sqlRecord = "select " + fieldstr + " from " + en.table() + sb.toString();
        String sqlCount = "select count(*) from " + en.table() + sb.toString();
        List<T> dataList = (List<T>) commonDao.findList(sqlRecord, first, pageSize, baseClass);

        int dataCount = commonDao.queryInt(sqlCount);
        pageData.setList(dataList);
        pageData.setRowCount(dataCount);
        return pageData;
    }

    @Override
    public void init(String arg0, String arg1, String arg3) {

    }
}
