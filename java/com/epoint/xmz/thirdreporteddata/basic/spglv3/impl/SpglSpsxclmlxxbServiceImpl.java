package com.epoint.xmz.thirdreporteddata.basic.spglv3.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.common.service.AuditCommonResult;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.domain.SpglSpsxclmlxxb;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.inter.ISpglSpsxclmlxxbService;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.service.SpglSpsxclmlxxbService;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * 审批事项材料目录信息表对应的后台service实现类
 *
 * @author Epoint
 * @version [版本号, 2023-09-19 17:17:26]
 */
@Component
@Service
public class SpglSpsxclmlxxbServiceImpl implements ISpglSpsxclmlxxbService {
    /**
     * 插入数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(SpglSpsxclmlxxb record) {
        return new SpglSpsxclmlxxbService().insert(record);
    }

    /**
     * 删除数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int deleteByGuid(String guid) {
        return new SpglSpsxclmlxxbService().deleteByGuid(guid);
    }

    /**
     * 更新数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(SpglSpsxclmlxxb record) {
        return new SpglSpsxclmlxxbService().update(record);
    }

    /**
     * 根据ID查找单个实体
     *
     * @param clazz      类<必须继承BaseEntity>
     * @param primaryKey 主键
     * @return T extends BaseEntity
     */
    public SpglSpsxclmlxxb find(Object primaryKey) {
        return new SpglSpsxclmlxxbService().find(primaryKey);
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
    public SpglSpsxclmlxxb find(String sql, Object... args) {
        return new SpglSpsxclmlxxbService().find(sql, args);
    }

    /**
     * 查找一个list
     *
     * @param sql   查询语句
     * @param clazz 可以是[Record.class(弱类型);FrameOu.class(强类型);Object[].class]
     * @param args  参数值数组
     * @return T extends BaseEntity
     */
    public List<SpglSpsxclmlxxb> findList(String sql, Object... args) {
        return new SpglSpsxclmlxxbService().findList(sql, args);
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
    public List<SpglSpsxclmlxxb> findList(String sql, int pageNumber, int pageSize, Object... args) {
        return new SpglSpsxclmlxxbService().findList(sql, pageNumber, pageSize, args);
    }

    /**
     * 查询数量
     *
     * @param sql  执行语句
     * @param args 参数
     * @return Integer
     */
    @Override
    public Integer countSpglSpsxclmlxxb(String sql, Object... args) {
        return new SpglSpsxclmlxxbService().countSpglSpsxclmlxxb(sql, args);
    }

    @Override
    public AuditCommonResult<List<SpglSpsxclmlxxb>> getListByCondition(Map<String, String> conditionMap) {
        SpglSpsxclmlxxbService spglSpsxkzxxbService = new SpglSpsxclmlxxbService();
        AuditCommonResult<List<SpglSpsxclmlxxb>> result = new AuditCommonResult<List<SpglSpsxclmlxxb>>();
        List<SpglSpsxclmlxxb> list = new ArrayList<>();
        try {
            list = spglSpsxkzxxbService.getAllRecord(SpglSpsxclmlxxb.class, conditionMap);
            result.setResult(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public AuditCommonResult<PageData<SpglSpsxclmlxxb>> getAllByPage(Map<String, String> conditionMap, Integer first,
                                                                     Integer pageSize, String sortField, String sortOrder) {
        SpglSpsxclmlxxbService spglSpsxclmlxxbService = new SpglSpsxclmlxxbService();
        AuditCommonResult<PageData<SpglSpsxclmlxxb>> result = new AuditCommonResult<PageData<SpglSpsxclmlxxb>>();
        PageData<SpglSpsxclmlxxb> pageData = new PageData<>();
        try {
            pageData = spglSpsxclmlxxbService.getAllRecordByPage(SpglSpsxclmlxxb.class, conditionMap, first, pageSize,
                    sortField, sortOrder);
            result.setResult(pageData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
