package com.epoint.xmz.xmycslspinfo.impl;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import com.epoint.core.grammar.Record;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import com.epoint.xmz.xmycslspinfo.api.entity.XmYcslSpinfo;
import com.epoint.zjcs.zjcsprojectinfo.bizlogic.domain.ZjcsProjectInfo;
import com.epoint.core.BaseEntity;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.dao.CommonDao;

/**
 * 一窗受理审批环节信息表对应的后台service
 * 
 * @author LYA
 * @version [版本号, 2020-07-22 16:31:09]
 */
public class XmYcslSpinfoService
{
 /**
     * 数据增删改查组件
     */
    protected ICommonDao baseDao;

    public XmYcslSpinfoService() {
        baseDao = CommonDao.getInstance();
    }
    /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(XmYcslSpinfo record) {
        return baseDao.insert(record);
    }

    /**
     * 删除数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public <T extends Record> int deleteByGuid(String guid) {
        T t = baseDao.find(XmYcslSpinfo.class, guid);
        return baseDao.delete(t);
    }

    /**
     * 更新数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(XmYcslSpinfo record) {
        return baseDao.update(record);
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
    public XmYcslSpinfo find(Object primaryKey) {
        return baseDao.find(XmYcslSpinfo.class, primaryKey);
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
    public XmYcslSpinfo find(String sql,  Object... args) {
        return baseDao.find(sql, XmYcslSpinfo.class, args);
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
    public List<XmYcslSpinfo> findList(String sql, Object... args) {
        return baseDao.findList(sql, XmYcslSpinfo.class, args);
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
    public List<XmYcslSpinfo> findList(String sql, int pageNumber, int pageSize, Object... args) {
        return baseDao.findList(sql, pageNumber, pageSize, XmYcslSpinfo.class, args);
    }

	/**
     * 查询数量
     * 
     * @param sql
     *            执行语句
     * @param args
     *            参数
     * @return Integer
     */
    public Integer countXmYcslSpinfo(String sql, Object... args){
        return baseDao.queryInt(sql, args);
    }
    /**
     * 
     *  [根据主题实例唯一标识和事项唯一标识获取材料信息] 
     *  @param sql
     *  @param pageNumber
     *  @param pageSize
     *  @param args
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public List<Record> findYyyzMaterialList(String biGuid, String taskid) {
        String sql = "select * from audit_sp_i_yyyz_material where (biguid =?1 and task_id is null) or (biguid = ?1 and task_id in ('" +taskid+ "'))";
        return baseDao.findList(sql, Record.class, biGuid);
    }
    /**
     * 
     *  [根据申报流水号查询办结表数据信息] 
     *  @param flowsn
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public XmYcslSpinfo findXmYcslSpinfoByFlowsn(String flowsn) {
        String sql = "select * from xm_yyyz_bjinfo where flowsn = ?";
        return this.find(sql, flowsn);
    }
    
    public List<ZjcsProjectInfo> getZjcsProjectInfoByItemcode(String itemcode) {
    	 String sql = "select * from zjcs_project_info where txtitemcode  = ?";
        return baseDao.findList(sql,ZjcsProjectInfo.class, itemcode);
    }
    
    public Record getZjcsProjectResultByProjectGuid(String projectguid) {
   	 String sql = "select * from zjcs_project_result where projectGuid = ?";
       return baseDao.find(sql,Record.class, projectguid);
   }
    
}
