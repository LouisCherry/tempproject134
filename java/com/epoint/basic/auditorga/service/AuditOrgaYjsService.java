package com.epoint.basic.auditorga.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.epoint.auditorga.auditwindow.entiy.AuditOrgaWindowYjs;
import com.epoint.basic.auditorga.auditarea.domain.AuditOrgaArea;
import com.epoint.basic.auditorga.auditarea.service.AuditOrgaAreaService;
import com.epoint.basic.auditorga.auditcenter.domain.AuditOrgaServiceCenter;
import com.epoint.basic.auditorga.auditcenter.impl.AuditOrgaServiceCenterImpl;
import com.epoint.basic.auditorga.auditconfig.domain.AuditOrgaConfig;
import com.epoint.basic.auditorga.audithall.domain.AuditOrgaHall;
import com.epoint.basic.auditorga.auditwindow.domain.AuditOrgaWindow;
import com.epoint.basic.auditorga.auditwindow.domain.AuditOrgaWindowTask;
import com.epoint.basic.auditorga.auditwindow.domain.AuditOrgaWindowUser;
import com.epoint.basic.auditorga.auditwindow.impl.AuditOrgaWindowImpl;
import com.epoint.basic.auditorga.auditwindow.impl.AuditOrgaWindowYjsImpl;
import com.epoint.basic.auditorga.auditworkingday.domain.AuditOrgaWorkingDay;
import com.epoint.basic.auditorga.inter.IAuditOrga;
import com.epoint.basic.cache.Cache;
import com.epoint.basic.cache.CacheService9;
import com.epoint.common.service.AuditCommonService;
import com.epoint.common.util.SQLManageUtil;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.memory.RedisCacheUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.jdbc.connection.DataSourceConfig;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.frame.service.metadata.datasource.api.IDataSourceService;


@Cache
public class AuditOrgaYjsService<T> extends CacheService9
{
    /**
     * 
     */
    private static final long serialVersionUID = 1223708331052361525L;

    private IDataSourceService dataSourceService = ContainerFactory.getContainInfo()
            .getComponent(IDataSourceService.class);

    /**
     * 通用dao
     */
    private ICommonDao commonDao;

    public AuditOrgaYjsService() {
        setCommonDao(CommonDao.getInstance("orga"));
    }

    public AuditOrgaYjsService(String dataSourceName) {
        setCommonDao(CommonDao.getInstance(dataSourceService.getDataSourceByName(dataSourceName)));
    }

    public AuditOrgaYjsService(DataSourceConfig dataSource) {
        setCommonDao(CommonDao.getInstance(dataSource));
    }

    public AuditOrgaYjsService(ICommonDao dao) {
        this.setCommonDao(dao);
    }

    /**
     * 获取所有记录，如果没有缓存数据，就从数据库中获取
     * 
     * @param baseClass
     * @param useCache
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<T> getAllRecord(Class<? extends BaseEntity> baseClass, Map<String, String> conditionMap) {
        List<T> result = new ArrayList<T>();
        result = ((IAuditOrga<T>) getInstanceService()).getAllRecord(baseClass, conditionMap);
        return result;
    }

    @SuppressWarnings("unchecked")
    public Integer getAllRecordCount(Class<? extends BaseEntity> baseClass, Map<String, String> conditionMap) {
        List<T> result = ((IAuditOrga<T>) getInstanceService()).getAllRecord(baseClass, conditionMap);
        if (result != null) {
            return result.size();
        }
        else {
            return 0;
        }

    }

    /**
     * 获取分页数据
     * 
     * @param baseClass
     * @param conditionMap
     * @param first
     * @param pageSize
     * @param sortField
     * @param sortOrder
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<T> getAllRecordByPage(Class<? extends BaseEntity> baseClass, Map<String, String> conditionMap,
            Integer first, Integer pageSize, String sortField, String sortOrder) {
        List<T> returnList = new ArrayList<T>();
        returnList = ((IAuditOrga<T>) getInstanceService()).getAllRecordByPage(baseClass, conditionMap, first, pageSize,
                sortField, sortOrder);
        return returnList;
    }

    /**
     * 
     * 新增某条记录
     * 
     * @param baseClass
     * @param record
     * @param useCache
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @SuppressWarnings("unchecked")
    public void addRecord(Class<? extends BaseEntity> baseClass, Record record, boolean useCache) {
        Entity en = baseClass.getAnnotation(Entity.class);
        if (getMemService() != null && useCache) {
            if (StringUtil.isNotBlank(en.table()) && "audit_orga_area".equalsIgnoreCase(en.table())) {
                ((IAuditOrga<T>) getMemService()).addRecord(baseClass, record, "xiaqucode");
            }
            else {

                ((IAuditOrga<T>) getMemService()).addRecord(baseClass, record);
            }
        }
        if (instanceActive) {
            if (StringUtil.isNotBlank(en.table())) {
                // 如果需要执行实例
                ((IAuditOrga<T>) getInstanceService()).addRecord(baseClass, record);
                // 同步其他服务器,添加一个updateFlag方法调用，将当前类，当前方法名字，参数值数组，当前缓存服务的tag打包进去
                updateFlag("addRecord", new Object[] {record });
            }
        }
    }

    /**
     * 
     * 更新某条记录
     * 
     * @param baseClass
     * @param record
     * @param key
     * @param useCache
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @SuppressWarnings("unchecked")
    public void updateRecord(Class<? extends BaseEntity> baseClass, Record record, String key, boolean useCache) {
        if (getMemService() != null && useCache) {
            ((IAuditOrga<T>) getMemService()).updateRecord(baseClass, record, key);
        }
        if (instanceActive) {
            Entity en = baseClass.getAnnotation(Entity.class);
            if (StringUtil.isNotBlank(en.table())) {
                // 如果需要执行实例
                ((IAuditOrga<T>) getInstanceService()).updateRecord(baseClass, record, key);
                // 同步其他服务器,添加一个updateFlag方法调用，将当前类，当前方法名字，参数值数组，当前缓存服务的tag打包进去
                updateFlag("updateRecord", new Object[] {record });
            }
        }
    }

    /**
     * 
     * 删除某条记录
     * 
     * @param baseClass
     * @param record
     * @param key
     * @param useCache
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @SuppressWarnings("unchecked")
    public void deleteRecod(Class<? extends BaseEntity> baseClass, Record record, String key, boolean useCache) {
        if (getMemService() != null && useCache) {
            ((IAuditOrga<T>) getMemService()).deleteRecod(baseClass, record, key);
        }
        if (instanceActive) {
            Entity en = baseClass.getAnnotation(Entity.class);
            if (StringUtil.isNotBlank(en.table())) {
                // 如果需要执行实例
                ((IAuditOrga<T>) getInstanceService()).deleteRecod(baseClass, record, key);
                // 同步其他服务器,添加一个updateFlag方法调用，将当前类，当前方法名字，参数值数组，当前缓存服务的tag打包进去
                updateFlag("deleteRecod", new Object[] {record });
            }
        }
    }


    /**
     * 
     * 根据条件查询
     * 
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
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @SuppressWarnings("hiding")
    public <T> PageData<T> getRecordPageData(Class<? extends BaseEntity> baseClass, Map<String, String> conditionMap,
            int first, int pageSize, String sortField, String sortOrder) {
        PageData<T> pageData = new PageData<T>();
        SQLManageUtil sqlManageUtil = new SQLManageUtil(baseClass);
        pageData = sqlManageUtil.getDbListByPage(baseClass, conditionMap, first, pageSize, sortField, sortOrder);
        return pageData;
    }

    /**
     * 
     * 获取某条记录
     * 
     * @param baseClass
     * @param rowGuid
     * @param key
     * @param useCache
     * @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @SuppressWarnings("unchecked")
    public T getDetail(Class<? extends BaseEntity> baseClass, String rowGuid, String key, boolean useCache) {
        if (getMemService() != null && useCache) {
            return ((IAuditOrga<T>) getMemService()).getDetail(baseClass, rowGuid, key);
        }
        else {
            return ((IAuditOrga<T>) getInstanceService()).getDetail(baseClass, rowGuid, key);
        }
    }

    public boolean isExist(Class<? extends BaseEntity> baseClass, Map<String, String> conditionMap) {
        int count = new AuditCommonService().getListByPageCount(baseClass, conditionMap);
        return count > 0;
    }

    @SuppressWarnings("unchecked")
    public void deleteAreaByAreacode(String areacode) {
        if (getMemService() != null) {
            SqlConditionUtil sql = new SqlConditionUtil();
            sql.eq("xiaqucode", areacode);
            List<AuditOrgaArea> auditAreas = (List<AuditOrgaArea>) getAllRecord(AuditOrgaArea.class, sql.getMap());
            if (auditAreas != null && !auditAreas.isEmpty()) {
                for (AuditOrgaArea auditArea : auditAreas) {
                    ((IAuditOrga<T>) getMemService()).deleteRecod(AuditOrgaArea.class, auditArea, "rowguid");
                }
            }
        }
        if (instanceActive) {
            AuditOrgaAreaService areaService = new AuditOrgaAreaService();
            areaService.deleteAreaByAreacode(areacode);
        }
    }

    @SuppressWarnings("unchecked")
    public void deleteAreaByOuguid(String ouguid) {
        if (getMemService() != null) {
            SqlConditionUtil sql = new SqlConditionUtil();
            sql.eq("ouguid", ouguid);
            List<AuditOrgaArea> auditAreas = (List<AuditOrgaArea>) getAllRecord(AuditOrgaArea.class, sql.getMap());
            if (auditAreas != null && !auditAreas.isEmpty()) {
                for (AuditOrgaArea auditArea : auditAreas) {
                    ((IAuditOrga<T>) getMemService()).deleteRecod(AuditOrgaArea.class, auditArea, "rowguid");
                }
            }
        }
        if (instanceActive) {
            AuditOrgaAreaService areaService = new AuditOrgaAreaService();
            areaService.deleteAreaByOuguid(ouguid);
        }
    }


    // 删除窗口关联事项
    @SuppressWarnings("unchecked")
    public void deleteWindowTaskByWindowGuid(String windowGuid) {
        String sql = "delete FROM audit_orga_windowyjs where windowguid =? ";
        commonDao.execute(sql, windowGuid);
    }



    // 删除中心及中心关联
    @SuppressWarnings("unchecked")
    public void deleteCenterByCenterGuid(String centerGuid) {
        deleteWorkingDayByCenterGuid(centerGuid);
        deleteCenterConfigByCenterGuid(centerGuid);
        AuditOrgaServiceCenterImpl auditOrgaCenterImpl = new AuditOrgaServiceCenterImpl();
        //删除中心关联窗口
        auditOrgaCenterImpl.deleteWindowByCenterGuid(centerGuid);
        if (getMemService() != null) {
            AuditOrgaServiceCenter auditOrgaCenter = auditOrgaCenterImpl.findAuditServiceCenterByGuid(centerGuid)
                    .getResult();
            if (auditOrgaCenter != null) {
                ((IAuditOrga<T>) getMemService()).deleteRecod(AuditOrgaServiceCenter.class, auditOrgaCenter, "rowguid");
            }
        }
    }

    // 删除中心关联工作日
    @SuppressWarnings("unchecked")
    public void deleteWorkingDayByCenterGuid(String centerGuid) {
        if (getMemService() != null) {
            AuditOrgaServiceCenterImpl auditOrgaCenterImpl = new AuditOrgaServiceCenterImpl();
            List<AuditOrgaWorkingDay> auditOrgaWorkingDays = auditOrgaCenterImpl
                    .getAuditServiceCenterWorkingDayByGuid(centerGuid).getResult();
            if (auditOrgaWorkingDays != null && !auditOrgaWorkingDays.isEmpty()) {
                for (AuditOrgaWorkingDay auditOrgaWorkingDay : auditOrgaWorkingDays) {
                    ((IAuditOrga<T>) getMemService()).deleteRecod(AuditOrgaWorkingDay.class, auditOrgaWorkingDay,
                            "rowguid");
                }
            }
        }
    }

    // 删除中心关联系统参数
    @SuppressWarnings("unchecked")
    public void deleteCenterConfigByCenterGuid(String centerGuid) {
        if (getMemService() != null) {
            AuditOrgaServiceCenterImpl auditOrgaCenterImpl = new AuditOrgaServiceCenterImpl();
            List<AuditOrgaConfig> auditOrgaConfigs = auditOrgaCenterImpl.getAuditServiceCenterConfigByGuid(centerGuid)
                    .getResult();
            if (auditOrgaConfigs != null && !auditOrgaConfigs.isEmpty()) {
                for (AuditOrgaConfig auditOrgaConfig : auditOrgaConfigs) {
                    ((IAuditOrga<T>) getMemService()).deleteRecod(AuditOrgaConfig.class, auditOrgaConfig, "rowguid");
                }
            }
        }
    }

    public ICommonDao getCommonDao() {
        return commonDao;
    }

    public void setCommonDao(ICommonDao commonDao) {
        this.commonDao = commonDao;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void init(String arg0, String arg1, String arg2) {
        if (StringUtil.isNotBlank(ConfigUtil.getConfigValue("redisSetting"))) {
            RedisCacheUtil redis = null;
            try {
                redis = new RedisCacheUtil(false);
                redis.delPatten(AuditOrgaServiceCenter.class.getSimpleName() + "_*");
                redis.delPatten(AuditOrgaHall.class.getSimpleName() + "_*");
                redis.delPatten(AuditOrgaWindow.class.getSimpleName() + "_*");
                redis.delPatten(AuditOrgaWindowUser.class.getSimpleName() + "_*");
                redis.delPatten(AuditOrgaWindowTask.class.getSimpleName() + "_*");
                redis.delPatten(AuditOrgaConfig.class.getSimpleName() + "_*");
                redis.delPatten(AuditOrgaArea.class.getSimpleName() + "_*");
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            finally {
                if(redis!=null){
                    redis.close();
                }    
            }
        }
    }

    public List<AuditOrgaArea> selectAuditAreaListByAreaCode(String areaCode) {
        String sql = " SELECT * FROM audit_orga_area WHERE XiaQuCode LIKE ?  ORDER BY XiaQuCode asc";
        List<Object> params= new ArrayList<>();
        params.add(areaCode.replace("\\", "\\\\").replace("%", "\\%") + "%");
        return commonDao.findList(sql , AuditOrgaArea.class,params.toArray());
    }

    

    
}
