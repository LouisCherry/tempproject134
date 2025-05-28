package com.epoint.union.auditunionproject.api;

import java.io.Serializable;
import java.util.List;

import com.epoint.common.service.AuditCommonResult;
import com.epoint.union.auditunionproject.api.entity.AuditUnionProject;

/**
 * 异地通办办件信息表对应的后台service接口
 * 
 * @author zhaoyan
 * @version [版本号, 2020-03-22 11:18:36]
 */
public interface IAuditUnionProjectService extends Serializable {

	/**
	 * 插入数据
	 * 
	 * @param record BaseEntity或Record对象 <必须继承Record>
	 * @return int
	 */
	public int insert(AuditUnionProject record);

	/**
	 * 删除数据
	 * 
	 * @param record BaseEntity或Record对象 <必须继承Record>
	 * @return int
	 */
	public int deleteByGuid(String guid);

	/**
	 * 更新数据
	 * 
	 * @param record BaseEntity或Record对象 <必须继承Record>
	 * @return int
	 */
	public int update(AuditUnionProject record);

	/**
	 * 根据ID查找单个实体
	 * 
	 * @param clazz      类<必须继承BaseEntity>
	 * @param primaryKey 主键
	 * @return T extends BaseEntity
	 */
	public AuditUnionProject find(Object primaryKey);

	/**
	 * 查找单条记录
	 * 
	 * @param sql   查询语句
	 * @param clazz 可以是[Record.class(弱类型);FrameOu.class(强类型);Object[].class，返回一个数组
	 *              ;String.class;Integer.class;Long.class]
	 * @param args  参数值数组
	 * @return T {String、Integer、Long、Record、FrameOu、Object[]等}
	 */
	public AuditUnionProject find(String sql, Object... args);

	/**
	 * 查找一个list
	 * 
	 * @param sql   查询语句
	 * @param clazz 可以是[Record.class(弱类型);FrameOu.class(强类型);Object[].class]
	 * @param args  参数值数组
	 * @return T extends BaseEntity
	 */
	public List<AuditUnionProject> findList(String sql, Object... args);

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
	public List<AuditUnionProject> findList(String sql, int pageNumber, int pageSize, Object... args);

	/**
	 * 查询数量
	 * 
	 * @param sql  执行语句
	 * @param args 参数
	 * @return Integer
	 */
	public Integer countAuditUnionProject(String sql, Object... args);

	
	public AuditCommonResult<String> InitProject(String taskGuid, String projectGuid, String operateUserName,
	            String operateUserGuid, String windowGuid, String windowName, String centerGuid, String certNum, String qno,
	            String acceptareacode, String cityLevel, String delegatetype);
}
