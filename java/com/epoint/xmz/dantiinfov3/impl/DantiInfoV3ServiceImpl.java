package com.epoint.xmz.dantiinfov3.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.common.util.SQLManageUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.xmz.dantiinfov3.api.IDantiInfoV3Service;
import com.epoint.xmz.dantiinfov3.api.entity.DantiInfoV3;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 项目单体信息表对应的后台service实现类
 *
 * @author ysai
 * @version [版本号, 2023-10-18 10:39:39]
 */
@Component
@Service
public class DantiInfoV3ServiceImpl implements IDantiInfoV3Service {
    /**
     * 插入数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(DantiInfoV3 record) {
        return new DantiInfoV3Service().insert(record);
    }

    /**
     * 删除数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int deleteByGuid(String guid) {
        return new DantiInfoV3Service().deleteByGuid(guid);
    }

    /**
     * 更新数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(DantiInfoV3 record) {
        return new DantiInfoV3Service().update(record);
    }

    /**
     * 根据ID查找单个实体
     *
     * @param clazz      类<必须继承BaseEntity>
     * @param primaryKey 主键
     * @return T extends BaseEntity
     */
    public DantiInfoV3 find(Object primaryKey) {
        return new DantiInfoV3Service().find(primaryKey);
    }

    /**
     * 查找单条记录
     *
     * @param sql   查询语句
     * @param clazz 可以是[Record.class(弱类型);FrameOu.class(强类型);Object[].class，返回一个数组 ;String.class;Integer.class;Long.class]
     * @param args  参数值数组
     * @return T {String、Integer、Long、Record、FrameOu、Object[]等}
     */
    public DantiInfoV3 find(String sql, Object... args) {
        return new DantiInfoV3Service().find(sql, args);
    }

    /**
     * 查找一个list
     *
     * @param sql   查询语句
     * @param clazz 可以是[Record.class(弱类型);FrameOu.class(强类型);Object[].class]
     * @param args  参数值数组
     * @return T extends BaseEntity
     */
    public List<DantiInfoV3> findList(String sql, Object... args) {
        return new DantiInfoV3Service().findList(sql, args);
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
    public List<DantiInfoV3> findList(String sql, int pageNumber, int pageSize, Object... args) {
        return new DantiInfoV3Service().findList(sql, pageNumber, pageSize, args);
    }

    /**
     * 查询数量
     *
     * @param sql  执行语句
     * @param args 参数
     * @return Integer
     */
    @Override
    public Integer countDantiInfoV3(String sql, Object... args) {
        return new DantiInfoV3Service().countDantiInfoV3(sql, args);
    }

    public List<DantiInfoV3> findListByProjectguid(String projectguid) {
        return new DantiInfoV3Service().findListByProjectguid(projectguid);
    }

    /**
     * 查找一个list
     *
     * @param sql   查询语句
     * @param clazz 可以是[Record.class(弱类型);FrameOu.class(强类型);Object[].class]
     * @param args  参数值数组
     * @return T extends BaseEntity
     */
    public List<DantiInfoV3> findListBySubAppguid(String subappguid) {
        return new DantiInfoV3Service().findListBySubAppguid(subappguid);
    }

    public List<DantiInfoV3> findListByGCguid(String gcguid) {
        return new DantiInfoV3Service().findListByGCguid(gcguid);
    }

    public PageData<DantiInfoV3> getDantiInfoPageData(Map<String, String> conditionMap, Integer firstResult,
                                                      Integer maxResults, String sortField, String sortOrder) {
        SQLManageUtil sqlManageUtil = new SQLManageUtil(DantiInfoV3.class);
        return sqlManageUtil.getDbListByPage(DantiInfoV3.class, conditionMap, firstResult, maxResults, sortField,
                sortOrder);
    }

    public PageData<DantiInfoV3> pageDantiInfo(String conditionSql, String projectguid, String selectedguid,
                                               String gongchengguid, List<Object> conditionList, int first, int pageSize, String sortField,
                                               String sortOrder) {
        return new DantiInfoV3Service().pageDantiInfo(conditionSql, projectguid, selectedguid, gongchengguid,
                conditionList, first, pageSize, sortField, sortOrder);
    }

    public PageData<DantiInfoV3> pageDantiLisrInfo(String conditionSql, String projectguid, String subappguid,
                                                   String gongchengguid, List<Object> conditionList, int first, int pageSize, String sortField,
                                                   String sortOrder) {
        return new DantiInfoV3Service().pageDantiLisrInfo(conditionSql, projectguid, subappguid, gongchengguid,
                conditionList, first, pageSize, sortField, sortOrder);
    }

    public List<DantiInfoV3> getDantiInfoListByCondition(Map<String, String> conditionMap) {
        SQLManageUtil sqlManageUtil = new SQLManageUtil(DantiInfoV3.class);
        if (sqlManageUtil.getListByCondition(DantiInfoV3.class, conditionMap) == null) {
            return new ArrayList<>();
        }
        return sqlManageUtil.getListByCondition(DantiInfoV3.class, conditionMap);
    }
}
