package com.epoint.projectstatisticsconfig.api;
import java.io.Serializable;
import java.util.List;

import com.epoint.core.grammar.Record;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.projectstatisticsconfig.api.entity.ProjectStatisticsConfig;

/**
 * 办件统计配置表对应的后台service接口
 * 
 * @author 15056
 * @version [版本号, 2022-05-23 17:43:09]
 */
public interface IProjectStatisticsConfigService extends Serializable
{ 
   
    /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(ProjectStatisticsConfig record);

    /**
     * 删除数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int deleteByGuid(String guid);

    /**
     * 更新数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(ProjectStatisticsConfig record);

    /**
     * 根据ID查找单个实体
     * 
     * @param clazz
     *            类<必须继承BaseEntity>
     * @param primaryKey
     *            主键
     * @return T extends BaseEntity
     */
    public ProjectStatisticsConfig find(Object primaryKey);

    /**
     * 查找单条记录
     * 
     * @param sql
     *            查询语句
     * @param clazz
     *            可以是[Record.class(弱类型);FrameOu.class(强类型);Object[].class，返回一个数组
     *            ;String.class;Integer.class;Long.class]
     * @param args
     *            参数值数组
     * @return T {String、Integer、Long、Record、FrameOu、Object[]等}
     */
    public ProjectStatisticsConfig find(String sql,Object... args);

    /**
     * 查找一个list
     * 
     * @param sql
     *            查询语句
     * @param clazz
     *            可以是[Record.class(弱类型);FrameOu.class(强类型);Object[].class]
     * @param args
     *            参数值数组
     * @return T extends BaseEntity
     */
    public List<ProjectStatisticsConfig> findList(String sql, Object... args);

    /**
     * 分页查找一个list
     * 
     * @param sql
     *            查询语句
     * @param pageNumber
     *            记录行的偏移量
     * @param pageSize
     *            记录行的最大数目
     * @param clazz
     *            可以是[Record.class(弱类型);FrameOu.class(强类型);Object[].class]
     * @param args
     *            参数值数组
     * @return T extends BaseEntity
     */
    public List<ProjectStatisticsConfig> findList(String sql, int pageNumber, int pageSize,Object... args);

	 /**
     * 查询数量
     * 
     * @param sql
     *            执行语句
     * @param args
     *            参数
     * @return Integer
     */
	 public Integer countProjectStatisticsConfig(String sql, Object... args);

	 /**
	  * getAreaCodeByAreaName 根据areaname找到areacode
	  *
	  * @author 成都研发4部-付荣煜
	  * @date 2022/5/24 11:12
	  * @param areaName
	  * @return String
	  */
    String getAreaCodeByAreaName(String areaName);

    /**
     * getAllOuguidByAreacode 根据areacode，获取所有部门
     *
     * @author 成都研发4部-付荣煜
     * @date 2022/5/24 13:53
     * @param areacode
     * @return List<String>
     */
    List<String> getAllOuguidByAreacode(String areacode);

    /**
     * getInfoByAreacodeAndOuguid 根据条件查找信息
     *
     * @author 成都研发4部-付荣煜
     * @date 2022/5/24 14:12
     * @param areacode
     * @param ouguid
     * @return ProjectStatisticsConfig
     */
    ProjectStatisticsConfig getInfoByAreacodeAndOuguid(String areacode, String ouguid);

    /**
     * pageData 根据条件查询List
     *
     * @author 成都研发4部-付荣煜
     * @date 2022/5/24 15:09
     * @param first
     * @param pageSize
     * @param dataBean
     * @return PageData<Record>
     */
    PageData<Record> pageData(int first, int pageSize, Record dataBean);
}
