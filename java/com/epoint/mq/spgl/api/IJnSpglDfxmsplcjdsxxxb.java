package com.epoint.mq.spgl.api;

import com.epoint.basic.spgl.domain.SpglDfxmsplcjdsxxxb;
import com.epoint.common.service.AuditCommonResult;
import com.epoint.core.grammar.Record;
import com.epoint.database.peisistence.crud.impl.model.PageData;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 住建部_地方项目审批流程阶段事项信息表对应的后台service接口
 *
 * @author zhpengsy
 * @version [版本号, 2018-11-16 15:10:10]
 */
public interface IJnSpglDfxmsplcjdsxxxb extends Serializable {

    /**
     * 插入数据
     *
     * @param record BaseEntity或Record对象
     * @return int
     */
    public int insert(SpglDfxmsplcjdsxxxb record);

    /**
     * 删除数据
     *
     * @param guid BaseEntity或Record对象
     * @return int
     */
    public int deleteByGuid(String guid);

    /**
     * 更新数据
     *
     * @param record BaseEntity或Record对象
     * @return int
     */
    public int update(SpglDfxmsplcjdsxxxb record);

    /**
     * 根据ID查找单个实体
     *
     * @param primaryKey 主键
     * @return T extends BaseEntity
     */
    public SpglDfxmsplcjdsxxxb find(Object primaryKey);

    /**
     * 查找单条记录
     *
     * @param sql  查询语句
     * @param args 参数值数组
     * @return T {String、Integer、Long、Record、FrameOu、Object[]等}
     */
    public SpglDfxmsplcjdsxxxb find(String sql, Object... args);

    /**
     * 查找一个list
     *
     * @param sql  查询语句
     * @param args 参数值数组
     * @return T extends BaseEntity
     */
    public List<SpglDfxmsplcjdsxxxb> findList(String sql, Object... args);

    /**
     * 分页查找一个list
     *
     * @param sql        查询语句
     * @param pageNumber 记录行的偏移量
     * @param pageSize   记录行的最大数目
     * @param args       参数值数组
     * @return T extends BaseEntity
     */
    public List<SpglDfxmsplcjdsxxxb> findList(String sql, int pageNumber, int pageSize, Object... args);

    /**
     * 通过编号删除数据
     *
     * @param SPLCBM 编号
     */
    public void deleteBySpGuid(String SPLCBM);

    /**
     * 获取流程设置分页
     *
     * @param conditionMap 条件map
     * @param first        第一条记录
     * @param pageSize     页大小
     * @param sortField    排序字段
     * @param sortOrder    排序顺序
     * @return
     */
    public AuditCommonResult<PageData<SpglDfxmsplcjdsxxxb>> getAllByPage(Map<String, String> conditionMap,
                                                                         Integer first, Integer pageSize, String sortField, String sortOrder);

    /**
     * 获取流程列表
     *
     * @param conditionMap 条件map
     * @return
     */
    public AuditCommonResult<List<SpglDfxmsplcjdsxxxb>> getListByCondition(Map<String, String> conditionMap);

    /**
     * 获取流程数量
     *
     * @param conditionMap 条件map
     * @return
     */
    public AuditCommonResult<Integer> getCountByCondition(Map<String, String> conditionMap);

    /**
     * @param splcbbh 审批流程版本
     * @param splcbm  审批流程编码
     * @param spsxbbh 审批事项版本号
     * @param spsxbm  审批事项编码
     * @return
     */
    public boolean isExistSplcSx(Double splcbbh, String splcbm, Double spsxbbh, String spsxbm);

    /**
     * @param item_id 事项编码
     */
    public List<SpglDfxmsplcjdsxxxb> getNeedAddNewVersionByItemId(String item_id);

    /**
     * @return Record    返回类型
     * @throws
     * @Description: 查basetask表信息
     * @author male
     * @date 2020年7月21日 下午4:48:53
     */
    public Record getAuditSpBaseTaskInfo(String dfsjzj);

    public String getMaxSpsxbbh(Double splcbbh, String splcbm, String spsxbm);

    String getMaxSpsxbbhV3(Double splcbbh, String splcbm, String spsxbm);

    List<String> getBasetaskBySplclxAndPhaseid(String splclx, String phaseid);

}
