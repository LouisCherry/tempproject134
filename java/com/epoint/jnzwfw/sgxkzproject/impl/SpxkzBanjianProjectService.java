package com.epoint.jnzwfw.sgxkzproject.impl;
import java.util.List;

import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.grammar.Record;
import com.epoint.jnzwfw.sgxkzproject.api.entity.SpxkzBanjianProject;

/**
 * 竣工信息表对应的后台service
 * 
 * @author 86180
 * @version [版本号, 2019-07-08 15:07:59]
 */
public class SpxkzBanjianProjectService
{
 /**
     * 数据增删改查组件
     */
    protected ICommonDao baseDao;

    public SpxkzBanjianProjectService() {
        baseDao = CommonDao.getInstance();
    }
    /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(SpxkzBanjianProject record) {
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
        T t = baseDao.find(SpxkzBanjianProject.class, guid);
        return baseDao.delete(t);
    }

    /**
     * 更新数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(SpxkzBanjianProject record) {
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
    public SpxkzBanjianProject find(Object primaryKey) {
        return baseDao.find(SpxkzBanjianProject.class, primaryKey);
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
    public SpxkzBanjianProject find(String sql,  Object... args) {
        return baseDao.find(sql, SpxkzBanjianProject.class, args);
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
    public List<SpxkzBanjianProject> findList(String sql, Object... args) {
        return baseDao.findList(sql, SpxkzBanjianProject.class, args);
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
    public List<SpxkzBanjianProject> findList(String sql, int pageNumber, int pageSize, Object... args) {
        return baseDao.findList(sql, pageNumber, pageSize, SpxkzBanjianProject.class, args);
    }

    public SpxkzBanjianProject getRecordBy(String projectguid){
        String sql = "select * from spxkz_banjian_project where rowguid = ?";
        return baseDao.find(sql, SpxkzBanjianProject.class, projectguid);
    }
    
    public List<Record> getSpxkzProject() {
        String sql = "select a.areacode,a.ouguid,a.ouname,a.status,a.APPLYDATE,a.banjiedate,a.rowguid,i.CERTNO,i.awarddate,i.certownername,i.certownercerttype,i.certownerno,DATE_FORMAT(i.AWARDDATE,'%Y-%m-%d') as AWARDDATE,a.OUName,s.XiangMuMingChen,c.itemtext as XiangMuFenLei,s.LiXiangPiFuJiGuan,DATE_FORMAT(s.lixiangpifushijian,'%Y-%m-%d') as lixiangpifushijian,s.JianSheGongChengGuiHuaXuKeZhen,s.xiangmuzongjianzhumianjipingfa,s.xiangmudishangjianzhumianjipin,s.xiangmudixiajianzhumianjipingf,";
                sql += "s.hetongjiagewanyuan,DATE_FORMAT(s.hetongkaigongriqi,'%Y-%m-%d') as hetongkaigongriqi,DATE_FORMAT(s.hetongjungongriqi,'%Y-%m-%d') as hetongjungongriqi,s.jianshedanweimingchen,s.shigongzongchengbaoqiye,s.jianlidanwei,";
                sql += "s.shentudanwei,s.shejidanwei,s.kanchadanwei,a.contactperson,a.contactphone from audit_project a join audit_project_form_sgxkz s on a.rowguid = s.projectguid join cert_info i on a.CERTROWGUID = i.rowguid ";
                sql += " join code_items c on (s.XiangMuFenLei = c.itemvalue and c.codeid = '1015887') where ifnull(a.syncsgxkz,0) = 0 limit 10";
         return baseDao.findList(sql, Record.class);
    }
    
    public int updateflag(String projectguid) {
        String sql = "update audit_project set syncsgxkz='1' where rowguid=?";
        return baseDao.execute(sql, projectguid);
    }
    
    public int updateflagLose(String projectguid) {
        String sql = "update audit_project set syncsgxkz='9' where rowguid=?";
        return baseDao.execute(sql, projectguid);
    }
    
}
