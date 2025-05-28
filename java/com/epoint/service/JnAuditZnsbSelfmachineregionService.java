package com.epoint.service;
import java.util.List;

import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.grammar.Record;
import com.epoint.domain.JnAuditZnsbSelfmachineregion;

/**
 * 智能化一体机区域配置对应的后台service
 * 
 * @author 54201
 * @version [版本号, 2019-09-27 11:16:41]
 */
public class JnAuditZnsbSelfmachineregionService
{
 /**
     * 数据增删改查组件
     */
    protected ICommonDao baseDao;

    public JnAuditZnsbSelfmachineregionService() {
        baseDao = CommonDao.getInstance();
    }
    /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(JnAuditZnsbSelfmachineregion record) {
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
        T t = baseDao.find(JnAuditZnsbSelfmachineregion.class, guid);
        return baseDao.delete(t);
    }

    /**
     * 更新数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(JnAuditZnsbSelfmachineregion record) {
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
    public JnAuditZnsbSelfmachineregion find(Object primaryKey) {
        return baseDao.find(JnAuditZnsbSelfmachineregion.class, primaryKey);
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
    public JnAuditZnsbSelfmachineregion find(String sql,  Object... args) {
        return baseDao.find(sql, JnAuditZnsbSelfmachineregion.class, args);
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
    public List<JnAuditZnsbSelfmachineregion> findList(String sql, Object... args) {
        return baseDao.findList(sql, JnAuditZnsbSelfmachineregion.class, args);
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
    public List<JnAuditZnsbSelfmachineregion> findList(String sql, int pageNumber, int pageSize, Object... args) {
        return baseDao.findList(sql, pageNumber, pageSize, JnAuditZnsbSelfmachineregion.class, args);
    }
    
    public List<JnAuditZnsbSelfmachineregion> getRegionList(){
        String sql="select rowguid,regionname  from audit_znsb_selfmachineregion  order by ordernum desc";
        List<JnAuditZnsbSelfmachineregion> list=baseDao.findList(sql, JnAuditZnsbSelfmachineregion.class);
        
        return list;
    }
    
    public List<JnAuditZnsbSelfmachineregion> getRegionListByLevel(String level){
        String sql="select *  from audit_znsb_selfmachineregion where  regionlevel=?  order by ordernum desc";
        List<JnAuditZnsbSelfmachineregion> list=baseDao.findList(sql, JnAuditZnsbSelfmachineregion.class,level);
        
        return list;
    }
    
    public List<JnAuditZnsbSelfmachineregion> getRegionListByLevelAndParent(String level,String parentguid){
        String sql="select *  from audit_znsb_selfmachineregion where  regionlevel=? and parentguid=?  order by ordernum desc";
        List<JnAuditZnsbSelfmachineregion> list=baseDao.findList(sql, JnAuditZnsbSelfmachineregion.class,level,parentguid);
        
        return list;
    }
    
    public List<JnAuditZnsbSelfmachineregion> getParentRegionList(){
        String sql="select *  from audit_znsb_selfmachineregion where   parentguid='' or parentguid is null  order by ordernum desc";
        List<JnAuditZnsbSelfmachineregion> list=baseDao.findList(sql, JnAuditZnsbSelfmachineregion.class);
        
        return list;
    }
    
    public List<JnAuditZnsbSelfmachineregion> getParentRegionListInuse(){
        //and ifnull(isenable,'0')='1'
        String sql="select *  from audit_znsb_selfmachineregion where   (parentguid='' or parentguid is null )  order by ordernum desc";
        List<JnAuditZnsbSelfmachineregion> list=baseDao.findList(sql, JnAuditZnsbSelfmachineregion.class);
        
        return list;
    }
    
    public List<JnAuditZnsbSelfmachineregion> getChildRegionListByParentguid(String parentguid){
        String sql="select *  from audit_znsb_selfmachineregion where  parentguid=?  order by ordernum desc";
        List<JnAuditZnsbSelfmachineregion> list=baseDao.findList(sql, JnAuditZnsbSelfmachineregion.class,parentguid);
        
        return list;
    }
    
    public List<JnAuditZnsbSelfmachineregion> getChildRegionListInuseByParentguid(String parentguid){
        //and  ifnull(isenable,'0')='1
        String sql="select *  from audit_znsb_selfmachineregion where  parentguid=?  order by ordernum desc";
        List<JnAuditZnsbSelfmachineregion> list=baseDao.findList(sql, JnAuditZnsbSelfmachineregion.class,parentguid);
        
        return list;
    }
    
    public JnAuditZnsbSelfmachineregion getRegionByRowguid(String rowguid){
        String sql="select *  from audit_znsb_selfmachineregion where  rowguid=?";
        
        
        return baseDao.find(sql, JnAuditZnsbSelfmachineregion.class,rowguid);
    }

    public List<JnAuditZnsbSelfmachineregion> getAllCommonRegionList(){
        String sql="select *  from audit_znsb_selfmachineregion where  ifnull(IsIndividuation,'0')='0' and  ifnull(isenable,'0')='1'  order by ordernum desc";
        List<JnAuditZnsbSelfmachineregion> list=baseDao.findList(sql, JnAuditZnsbSelfmachineregion.class);
        
        return list;
    }
    
    public void deleteOldRegion() {
        String sql = "delete from audit_znsb_selfmachineregion where ifnull(IsTobeUpdated,'0')='0' and ifnull(IsIndividuation,'0')='0'   ";
        baseDao.execute(sql);
    }
    
    public void updateNewRegion() {
        String sql = "update  audit_znsb_selfmachineregion set IsTobeUpdated='0' where IsTobeUpdated='1' and ifnull(IsIndividuation,'0')='0'   ";
        baseDao.execute(sql);
    }
    public JnAuditZnsbSelfmachineregion getRegionByCode(String areacode) {
        String sql="select * from audit_znsb_selfmachineregion where areacode=? ";
        return baseDao.find(sql, JnAuditZnsbSelfmachineregion.class, areacode);
    }
    public List<JnAuditZnsbSelfmachineregion> getAllUsedPlaceList(int currentpage, int pagesize) {
        String sql="select RegionName,AreaCode,PictureUrl from audit_znsb_selfmachineregion where IsEnable = '1' and ClickUrl is not null ";
        return baseDao.findList(sql, currentpage, pagesize, JnAuditZnsbSelfmachineregion.class);
    }
    public int getAllUsedPlaceListNum() {
        String sql = "select count(rowguid) from audit_znsb_selfmachineregion where IsEnable = '1' and ClickUrl is not null ";
        return baseDao.queryInt(sql);
    }

}
