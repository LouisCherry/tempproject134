package com.epoint.xmz.xmycslspinfo.impl;
import java.util.List;

import org.springframework.stereotype.Component;
import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.core.grammar.Record;
import com.epoint.xmz.xmycslspinfo.api.IXmYcslSpinfoService;
import com.epoint.xmz.xmycslspinfo.api.entity.XmYcslSpinfo;
import com.epoint.zjcs.zjcsprojectinfo.bizlogic.domain.ZjcsProjectInfo;
/**
 * 一窗受理审批环节信息表对应的后台service实现类
 * 
 * @author LYA
 * @version [版本号, 2020-07-22 16:31:09]
 */
@Component
@Service
public class XmYcslSpinfoServiceImpl implements IXmYcslSpinfoService
{
    /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(XmYcslSpinfo record) {
        return new XmYcslSpinfoService().insert(record);
    }

    /**
     * 删除数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int deleteByGuid(String guid) {
        return new XmYcslSpinfoService().deleteByGuid(guid);
    }

    /**
     * 更新数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(XmYcslSpinfo record) {
        return new XmYcslSpinfoService().update(record);
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
       return new XmYcslSpinfoService().find(primaryKey);
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
    public XmYcslSpinfo find(String sql, Object... args) {
        return new XmYcslSpinfoService().find(sql,args);
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
       return new XmYcslSpinfoService().findList(sql,args);
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
       return new XmYcslSpinfoService().findList(sql,pageNumber,pageSize,args);
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
     @Override
    public Integer countXmYcslSpinfo(String sql, Object... args){
        return new XmYcslSpinfoService().countXmYcslSpinfo(sql, args);
    }

    @Override
    public List<Record> findYyyzMaterialList(String biGuid, String taskid) {
        return new XmYcslSpinfoService().findYyyzMaterialList(biGuid, taskid);
    }

    @Override
    public XmYcslSpinfo findXmYcslSpinfoByFlowsn(String flowsn) {
        return new XmYcslSpinfoService().findXmYcslSpinfoByFlowsn(flowsn);
    }
    
    public List<ZjcsProjectInfo> getZjcsProjectInfoByItemcode(String itemcode) {
        return new XmYcslSpinfoService().getZjcsProjectInfoByItemcode(itemcode);
    }
    
    public Record getZjcsProjectResultByProjectGuid(String projectguid) {
    	return new XmYcslSpinfoService().getZjcsProjectResultByProjectGuid(projectguid);
    }

}
