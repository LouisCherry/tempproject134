package com.epoint.zoucheng.znsb.auditznsbaccessbox.inter;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.epoint.basic.auditqueue.auditznsbaccessbox.domain.AuditZnsbAccessbox;
import com.epoint.common.service.AuditCommonResult;

/**
 * 智能化存取盒表对应的后台service接口
 * 
 * @author 54201
 * @version [版本号, 2019-02-20 14:45:08]
 */
public interface IZCAuditZnsbAccessbox extends Serializable
{

    /**
     * 插入数据
     * 
     * @param record 智能化存取盒表实体
     *            
     * @return int
     */
    public int insert(AuditZnsbAccessbox record);

    /**
     * 删除数据
     * 
     * @param guid 主键
     *            
     * @return int
     */
    public int deleteByGuid(String guid);

    /**
     * 更新数据
     * 
     * @param record 智能化存取盒表实体
     *            
     * @return int
     */
    public int update(AuditZnsbAccessbox record);

    /**
     * 根据ID查找单个实体
     * 
     * @param primaryKey
     *            主键
     * @return T extends BaseEntity
     */
    public AuditZnsbAccessbox find(Object primaryKey);

    /**
     * 查找单条记录
     * 
     * @param sql
     *            查询语句
     * @param args
     *            参数值数组
     * @return T {String、Integer、Long、Record、FrameOu、Object[]等}
     */
    public AuditZnsbAccessbox find(String sql, Object... args);

    /**
     * 查找一个list
     * 
     * @param sql
     *            查询语句
     * @param args
     *            参数值数组
     * @return T extends BaseEntity
     */
    public List<AuditZnsbAccessbox> findList(String sql, Object... args);

    /**
     * 分页查找一个list
     * 
     * @param sql
     *            查询语句
     * @param pageNumber
     *            记录行的偏移量
     * @param pageSize
     *            记录行的最大数目
     * @param args
     *            参数值数组
     * @return T extends BaseEntity
     */
    public List<AuditZnsbAccessbox> findList(String sql, int pageNumber, int pageSize, Object... args);
    
    /**
     * 根据存储柜guid删除所有存储盒数据
     * @param CabinetGuid
     * @return
     */
    public AuditCommonResult<String> deletebyCabinetGuid(String CabinetGuid);
    
    /**
     * 根据CabinetGuid初始化模块数据
     * @param CabinetGuid
     * @return
     */
    public AuditCommonResult<String> initBox(String CabinetGuid);
    
    /**
     * 
     *  获取列表
     *  @param conditionMap 查询条件
     *  @param fields 查询字段
     *  @return
     */
    public AuditCommonResult<List<AuditZnsbAccessbox>> getBoxList(Map<String, String> conditionMap,String fields);
    
    /**
     * 获取详情
     * @param RowGuid
     * @return
     */
    public AuditCommonResult<AuditZnsbAccessbox> getDetailByGuid(String RowGuid);

}
