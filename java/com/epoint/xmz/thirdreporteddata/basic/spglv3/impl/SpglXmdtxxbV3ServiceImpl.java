package com.epoint.xmz.thirdreporteddata.basic.spglv3.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.common.service.AuditCommonResult;
import com.epoint.common.util.SQLManageUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.domain.SpglXmdtxxbV3;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.inter.ISpglXmdtxxbV3Service;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.service.SpglXmdtxxbV3Service;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;


/**
 * 项目单体信息表对应的后台service实现类
 *
 * @author Epoint
 * @version [版本号, 2023-09-26 16:08:13]
 */
@Component
@Service
public class SpglXmdtxxbV3ServiceImpl implements ISpglXmdtxxbV3Service {
    /**
     * 插入数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(SpglXmdtxxbV3 record) {
        return new SpglXmdtxxbV3Service().insert(record);
    }

    /**
     * 删除数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int deleteByGuid(String guid) {
        return new SpglXmdtxxbV3Service().deleteByGuid(guid);
    }

    /**
     * 更新数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(SpglXmdtxxbV3 record) {
        return new SpglXmdtxxbV3Service().update(record);
    }

    /**
     * 根据ID查找单个实体
     *
     * @param clazz      类<必须继承BaseEntity>
     * @param primaryKey 主键
     * @return T extends BaseEntity
     */
    public SpglXmdtxxbV3 find(Object primaryKey) {
        return new SpglXmdtxxbV3Service().find(primaryKey);
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
    public SpglXmdtxxbV3 find(String sql, Object... args) {
        return new SpglXmdtxxbV3Service().find(sql, args);
    }

    /**
     * 查找一个list
     *
     * @param sql   查询语句
     * @param clazz 可以是[Record.class(弱类型);FrameOu.class(强类型);Object[].class]
     * @param args  参数值数组
     * @return T extends BaseEntity
     */
    public List<SpglXmdtxxbV3> findList(String sql, Object... args) {
        return new SpglXmdtxxbV3Service().findList(sql, args);
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
    public List<SpglXmdtxxbV3> findList(String sql, int pageNumber, int pageSize, Object... args) {
        return new SpglXmdtxxbV3Service().findList(sql, pageNumber, pageSize, args);
    }

    /**
     * 查询数量
     *
     * @param sql  执行语句
     * @param args 参数
     * @return Integer
     */
    @Override
    public Integer countSpglXmdtxxbV3(String sql, Object... args) {
        return new SpglXmdtxxbV3Service().countSpglXmdtxxbV3(sql, args);
    }

    @Override
    public AuditCommonResult<PageData<SpglXmdtxxbV3>> getAllByPage(Map<String, String> conditionMap,
                                                                   Integer first, Integer pageSize, String sortField, String sortOrder) {
        SpglXmdtxxbV3Service service = new SpglXmdtxxbV3Service();
        AuditCommonResult<PageData<SpglXmdtxxbV3>> result = new AuditCommonResult<PageData<SpglXmdtxxbV3>>();
        PageData<SpglXmdtxxbV3> pageData = new PageData<>();
        try {
            pageData = service.getAllRecordByPage(SpglXmdtxxbV3.class, conditionMap, first, pageSize, sortField,
                    sortOrder);
            result.setResult(pageData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public AuditCommonResult<List<SpglXmdtxxbV3>> getListByCondition(Map<String, String> conditionMap) {
        AuditCommonResult<List<SpglXmdtxxbV3>> result = new AuditCommonResult<>();
        List<SpglXmdtxxbV3> listByCondition = new SQLManageUtil().getListByCondition(SpglXmdtxxbV3.class, conditionMap);
        result.setResult(listByCondition);
        return result;
    }


}
