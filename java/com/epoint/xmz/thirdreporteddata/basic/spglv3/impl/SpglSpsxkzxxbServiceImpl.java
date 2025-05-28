package com.epoint.xmz.thirdreporteddata.basic.spglv3.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.common.service.AuditCommonResult;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.domain.SpglSpsxkzxxb;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.inter.ISpglSpsxkzxxbService;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.service.SpglSpsxkzxxbService;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 审批事项扩展信息表对应的后台service实现类
 *
 * @author Epoint
 * @version [版本号, 2023-09-19 17:17:50]
 */
@Component
@Service
public class SpglSpsxkzxxbServiceImpl implements ISpglSpsxkzxxbService {
    /**
     * 插入数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(SpglSpsxkzxxb record) {
        return new SpglSpsxkzxxbService().insert(record);
    }

    /**
     * 删除数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int deleteByGuid(String guid) {
        return new SpglSpsxkzxxbService().deleteByGuid(guid);
    }

    /**
     * 更新数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(SpglSpsxkzxxb record) {
        return new SpglSpsxkzxxbService().update(record);
    }

    /**
     * 根据ID查找单个实体
     *
     * @param clazz      类<必须继承BaseEntity>
     * @param primaryKey 主键
     * @return T extends BaseEntity
     */
    public SpglSpsxkzxxb find(Object primaryKey) {
        return new SpglSpsxkzxxbService().find(primaryKey);
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
    public SpglSpsxkzxxb find(String sql, Object... args) {
        return new SpglSpsxkzxxbService().find(sql, args);
    }

    /**
     * 查找一个list
     *
     * @param sql   查询语句
     * @param clazz 可以是[Record.class(弱类型);FrameOu.class(强类型);Object[].class]
     * @param args  参数值数组
     * @return T extends BaseEntity
     */
    public List<SpglSpsxkzxxb> findList(String sql, Object... args) {
        return new SpglSpsxkzxxbService().findList(sql, args);
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
    public List<SpglSpsxkzxxb> findList(String sql, int pageNumber, int pageSize, Object... args) {
        return new SpglSpsxkzxxbService().findList(sql, pageNumber, pageSize, args);
    }

    /**
     * 查询数量
     *
     * @param sql  执行语句
     * @param args 参数
     * @return Integer
     */
    @Override
    public Integer countSpglSpsxkzxxb(String sql, Object... args) {
        return new SpglSpsxkzxxbService().countSpglSpsxkzxxb(sql, args);
    }

    @Override
    public AuditCommonResult<List<SpglSpsxkzxxb>> getListByCondition(Map<String, String> conditionMap) {
        SpglSpsxkzxxbService spglSpsxkzxxbService = new SpglSpsxkzxxbService();
        AuditCommonResult<List<SpglSpsxkzxxb>> result = new AuditCommonResult<List<SpglSpsxkzxxb>>();
        List<SpglSpsxkzxxb> list = new ArrayList<>();
        try {
            list = spglSpsxkzxxbService.getAllRecord(SpglSpsxkzxxb.class, conditionMap);
            result.setResult(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


}
