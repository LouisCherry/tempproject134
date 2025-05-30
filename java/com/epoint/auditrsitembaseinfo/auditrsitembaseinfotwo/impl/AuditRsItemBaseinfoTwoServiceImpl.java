package com.epoint.auditrsitembaseinfo.auditrsitembaseinfotwo.impl;
import java.util.List;

import org.springframework.stereotype.Component;

import com.epoint.auditrsitembaseinfo.auditrsitembaseinfotwo.api.entity.AuditRsItemBaseinfoTwo;
import com.epoint.auditrsitembaseinfo.auditrsitembaseinfotwo.api.IAuditRsItemBaseinfoTwoService;
import com.alibaba.dubbo.config.annotation.Service;
/**
 * 泰安建设项目第二阶段基本信息拓展表对应的后台service实现类
 * 
 * @author wangxiaolong
 * @version [版本号, 2019-08-26 08:59:06]
 */
@Component
@Service
public class AuditRsItemBaseinfoTwoServiceImpl implements IAuditRsItemBaseinfoTwoService
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(AuditRsItemBaseinfoTwo record) {
        return new AuditRsItemBaseinfoTwoService().insert(record);
    }

    /**
     * 删除数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int deleteByGuid(String guid) {
        return new AuditRsItemBaseinfoTwoService().deleteByGuid(guid);
    }

    /**
     * 更新数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(AuditRsItemBaseinfoTwo record) {
        return new AuditRsItemBaseinfoTwoService().update(record);
    }

    /**
     * 根据ID查找单个实体
     * 
     * @param clazz
     *            类<必须继承BaseEntity>
     * @param primaryKey
     *            主键
     * @return T extends BaseEntity
     */
    public AuditRsItemBaseinfoTwo find(Object primaryKey) {
       return new AuditRsItemBaseinfoTwoService().find(primaryKey);
    }

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
    public AuditRsItemBaseinfoTwo find(String sql, Object... args) {
        return new AuditRsItemBaseinfoTwoService().find(args);
    }

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
    public List<AuditRsItemBaseinfoTwo> findList(String sql, Object... args) {
       return new AuditRsItemBaseinfoTwoService().findList(sql,args);
    }

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
    public List<AuditRsItemBaseinfoTwo> findList(String sql, int pageNumber, int pageSize, Object... args) {
       return new AuditRsItemBaseinfoTwoService().findList(sql,pageNumber,pageSize,args);
    }

    @Override
    public AuditRsItemBaseinfoTwo getAuditRsItemBaseinfoTwoByparentidandpaseguid(String parentid, String subappguid) {
        // TODO Auto-generated method stub
        return new AuditRsItemBaseinfoTwoService().getAuditRsItemBaseinfoTwoByparentidandpaseguid(parentid, subappguid);
    }

}
