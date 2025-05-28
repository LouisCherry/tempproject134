package com.epoint.zoucheng.znsb.auditznsbselfmachinemodule.service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.epoint.common.service.AuditCommonService;
import com.epoint.common.znsb.util.QueueConstant;
import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.zoucheng.znsb.auditznsbselfmachinemodule.domain.ZCAuditZnsbSelfmachinemodule;

/**
 * 自助服务一体机模块配置对应的后台service
 * 
 * @author 54201
 * @version [版本号, 2018-06-14 15:50:54]
 */
public class ZCAuditZnsbSelfmachinemoduleService extends AuditCommonService
{
    /**
     * 
     */
    private static final long serialVersionUID = -4623215296863496033L;

    /**
     * 数据增删改查组件
     */

    public ZCAuditZnsbSelfmachinemoduleService() {
        commonDao = CommonDao.getInstance();
    }

    /**
     * 插入数据
     * 
     * @param record
     *            
     * @return int
     */
    public int insert(ZCAuditZnsbSelfmachinemodule record) {
        return commonDao.insert(record);
    }

    /**
     * 删除数据
     * 
     * @param record
     *            
     * @return int
     */
    public <T extends Record> int deleteByGuid(String guid) {
        T t = commonDao.find(ZCAuditZnsbSelfmachinemodule.class, guid);
        return commonDao.delete(t);
    }

    /**
     * 更新数据
     * 
     * @param record
     *            
     * @return int
     */
    public int update(ZCAuditZnsbSelfmachinemodule record) {
        return commonDao.update(record);
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
    public ZCAuditZnsbSelfmachinemodule find(Object primaryKey) {
        return commonDao.find(ZCAuditZnsbSelfmachinemodule.class, primaryKey);
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
    public ZCAuditZnsbSelfmachinemodule find(String sql, Object... args) {
        return commonDao.find(sql, ZCAuditZnsbSelfmachinemodule.class, args);
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
    public List<ZCAuditZnsbSelfmachinemodule> findList(String sql, Object... args) {
        return commonDao.findList(sql, ZCAuditZnsbSelfmachinemodule.class, args);
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
    public List<ZCAuditZnsbSelfmachinemodule> findList(String sql, int pageNumber, int pageSize, Object... args) {
        return commonDao.findList(sql, pageNumber, pageSize, ZCAuditZnsbSelfmachinemodule.class, args);
    }

    /**
     * 查找对应type的模块数量
     */
    public Integer findTypeCount(String moduleType, String centerguid) {
        String sql = "select count(1) from Audit_Znsb_SelfmachineModule where moduleType=?1 and centerguid=?2 and Enable='1' and (Macaddress is null or Macaddress ='')";
        return commonDao.find(sql, Integer.class, moduleType, centerguid);
    }
    
    /**
     * 查找对应type的模块数量
     */
    public Integer findTypeCount(String moduleType, String centerguid, String moduleconfigtype) {
        String sql = "select count(1) from Audit_Znsb_SelfmachineModule where moduleType=? and centerguid=? and ifnull(moduleconfigtype,'0')=? and Enable='1' and (Macaddress is null or Macaddress ='')";
        return commonDao.find(sql, Integer.class, moduleType, centerguid,moduleconfigtype);
    }

    /**
     * 查找对应type的模块
     */
    public List<ZCAuditZnsbSelfmachinemodule> findTypeList(String moduleType, String centerguid) {
        String sql = "select * from Audit_Znsb_SelfmachineModule where moduleType=? and centerguid=? and (Macaddress is null or Macaddress='') and Enable='1' order by Ordernum desc,modulename desc";
        return commonDao.findList(sql, ZCAuditZnsbSelfmachinemodule.class, moduleType, centerguid);
    }
    
    /**
     * 查找对应type的模块
     */
    public List<ZCAuditZnsbSelfmachinemodule> findTypeList(String moduleType, String centerguid,String moduleconfigtype) {
        String sql = "select * from Audit_Znsb_SelfmachineModule where moduleType=? and centerguid=?  and ifnull(moduleconfigtype,'0')=?  and (Macaddress is null or Macaddress='') and Enable='1' order by Ordernum desc,modulename desc";
        return commonDao.findList(sql, ZCAuditZnsbSelfmachinemodule.class, moduleType, centerguid,moduleconfigtype);
    }

    /**
     * 查找对应type的模块(没有父模块guid，为第二层模块)
     */
    public List<ZCAuditZnsbSelfmachinemodule> findNoParentModuleGuidTypeList(String moduleType, String centerguid) {
        String sql = "select * from Audit_Znsb_SelfmachineModule where moduleType=? and centerguid=? and (parentmoduleguid is null or parentmoduleguid ='') and (Macaddress is null or Macaddress='') and Enable='1' order by Ordernum desc,modulename desc";
        return commonDao.findList(sql, ZCAuditZnsbSelfmachinemodule.class, moduleType, centerguid);
    }

    /**
     * 查找对应type,mac的模块数量
     */
    public Integer findType_MacCount(String moduleType, String centerguid, String macaddress) {
        String sql = "select count(1) from Audit_Znsb_SelfmachineModule where moduleType=?1 and centerguid=?2 and Macaddress=?3 and Enable='1'";
        return commonDao.find(sql, Integer.class, moduleType, centerguid, macaddress);
    }
    
    /**
     * 查找对应type,mac的模块数量
     */
    public Integer findType_MacCount(String moduleType, String centerguid, String macaddress,String moduleconfigtype) {
        String sql = "select count(1) from Audit_Znsb_SelfmachineModule where moduleType=? and centerguid=?  and Macaddress=?  and ifnull(moduleconfigtype,'0')=? and Enable='1'";
        return commonDao.find(sql, Integer.class, moduleType, centerguid, macaddress,moduleconfigtype);
    }
    
    /**
     * 查找对应查询条件的模块数量
     */
    public Integer getCountByModuleName( String centerguid, String macaddress,String moduleconfigtype, String modulename) {
        String sql = "select count(1) from Audit_Znsb_SelfmachineModule where  centerguid=?  and (Macaddress=? or Macaddress is null or Macaddress = '')  and ifnull(moduleconfigtype,'0')=? and Enable='1' and ifnull(parentmoduleguid,'') !='' and modulename like ? ";
        return commonDao.find(sql, Integer.class, centerguid, macaddress,moduleconfigtype, "%" + modulename + "%");
    }

    /**
     * 查找对应type,mac的模块
     */
    public List<ZCAuditZnsbSelfmachinemodule> findType_MacList(String moduleType, String centerguid, String macaddress) {
        String sql = "select rowguid,modulename,moduletype,centerguid,picturepath,htmlurl,isneedlogin,enable,macaddress from Audit_Znsb_SelfmachineModule where moduleType=? and centerguid=? and Macaddress=? and Enable='1' order by Ordernum desc,modulename desc";
        return commonDao.findList(sql, ZCAuditZnsbSelfmachinemodule.class, moduleType, centerguid, macaddress);
    }
    
    /**
     * 查找对应type,mac的模块
     */
    public List<ZCAuditZnsbSelfmachinemodule> findType_MacList(String moduleType, String centerguid, String macaddress, String moduleconfigtype) {
        String sql = "select rowguid,modulename,moduletype,centerguid,picturepath,htmlurl,isneedlogin,enable,macaddress from Audit_Znsb_SelfmachineModule where moduleType=? and centerguid=? and Macaddress=?  and ifnull(moduleconfigtype,'0')=?  and Enable='1' order by Ordernum desc,modulename desc";
        return commonDao.findList(sql, ZCAuditZnsbSelfmachinemodule.class, moduleType, centerguid, macaddress,moduleconfigtype);
    }

    /**
     * 获取对应中心的模块
     */
    public List<ZCAuditZnsbSelfmachinemodule> findCenterList(String centerguid) {
        String sql = "select * from Audit_Znsb_SelfmachineModule where (Macaddress is null or Macaddress='') and CenterGuid =? and Enable='1' order by Ordernum desc";
        return commonDao.findList(sql, ZCAuditZnsbSelfmachinemodule.class, centerguid);
    }
    
    /**
     * 获取对应中心的模块
     */
    public List<ZCAuditZnsbSelfmachinemodule> findCenterList(String centerguid,String moduleconfigtype) {
        String sql = "select * from Audit_Znsb_SelfmachineModule where (Macaddress is null or Macaddress='') and CenterGuid =?  and ifnull(moduleconfigtype,'0')=?   and Enable='1' order by Ordernum desc";
        return commonDao.findList(sql, ZCAuditZnsbSelfmachinemodule.class, centerguid,moduleconfigtype);
    }

    /**
     * 获取对应Macaddress的模块
     */
    public List<ZCAuditZnsbSelfmachinemodule> findMacList(String macaddress, String centerguid) {
        String sql = "select rowguid,modulename,moduletype,centerguid,picturepath,htmlurl,isneedlogin,enable,macaddress from Audit_Znsb_SelfmachineModule where Macaddress=?1 and CenterGuid =?2 and Enable='1' order by Ordernum desc";
        return commonDao.findList(sql, ZCAuditZnsbSelfmachinemodule.class, macaddress, centerguid);
    }
    
    /**
     * 获取对应Macaddress的模块
     */
    public List<ZCAuditZnsbSelfmachinemodule> findMacListByConfigType(String macaddress, String centerguid,String moduleconfigtype) {
        String sql = "select * from Audit_Znsb_SelfmachineModule where Macaddress=? and CenterGuid =? and Enable='1' and ifnull(moduleconfigtype,'0')=? order by Ordernum desc";
        return commonDao.findList(sql, ZCAuditZnsbSelfmachinemodule.class, macaddress, centerguid,moduleconfigtype);
    }

    /**
     * 获取对应Macaddress和modulename的模块
     */
    public List<ZCAuditZnsbSelfmachinemodule> findMacListByName(String macaddress, String centerguid, String modulename) {
        String sql = "select rowguid,modulename,moduletype,centerguid,picturepath,htmlurl,isneedlogin,enable,macaddress from Audit_Znsb_SelfmachineModule where Macaddress=?1 and CenterGuid =?2 and modulename like ?3 and Enable='1' order by Ordernum desc";
        return commonDao.findList(sql, ZCAuditZnsbSelfmachinemodule.class, macaddress, centerguid,
                "%" + modulename + "%");
    }

    /**
     * 获取对应modulename的模块
     */
    public List<ZCAuditZnsbSelfmachinemodule> findCommonListByName(String centerguid, String modulename) {
        String sql = "select rowguid,modulename,moduletype,centerguid,picturepath,htmlurl,isneedlogin,enable,macaddress from Audit_Znsb_SelfmachineModule where (Macaddress is null or Macaddress='')  and CenterGuid =?1 and modulename like ?2 and Enable='1' order by Ordernum desc";
        return commonDao.findList(sql, ZCAuditZnsbSelfmachinemodule.class, centerguid, "%" + modulename + "%");
    }

    /**
     * 根据centerguid删除
     */
    public void deleteRecords(Class<? extends BaseEntity> baseClass, String keyValue, String key) {
        Entity en = baseClass.getAnnotation(Entity.class);
        if (StringUtil.isNotBlank(en.table())) {
            // 直接拼接删除语句进行处理
            String sql = "delete from " + en.table() + " where " + key + "=?1";
            commonDao.execute(sql, keyValue);
        }
    }
    
    public void deleteByCenterguidAndConfigType(String CenterGuid,String moduleconfigtype){
        String sql = "delete from audit_znsb_selfmachinemodule where centerguid=? and ifnull(moduleconfigtype,'0')=?";
        commonDao.execute(sql, CenterGuid,moduleconfigtype);
    }
    
    public void deleteByMacAndConfigType(String macaddress,String moduleconfigtype){
        String sql = "delete from audit_znsb_selfmachinemodule where Macaddress=? and ifnull(moduleconfigtype,'0')=?";
        commonDao.execute(sql, macaddress,moduleconfigtype);
    }

    /**
     * 根据modulecodelevel,CenterGuid获取子模块
     */
    public List<ZCAuditZnsbSelfmachinemodule> findListByModulecodelevel(String modulecodelevel, String centerguid) {
        String sql = "select * from Audit_Znsb_SelfmachineModule "
                + "where IFNULL(modulecodelevel,'') like ? and CenterGuid =? and  (Macaddress is null or Macaddress='') order by Ordernum desc";
        return commonDao.findList(sql, ZCAuditZnsbSelfmachinemodule.class, modulecodelevel + "%", centerguid);
    }

    /**
     * 根据modulecodelevel,CenterGuid获取子模块
     */
    public List<ZCAuditZnsbSelfmachinemodule> findListByParentmoduleguid(String parentmoduleguid, String centerguid) {
        String sql = "select * from Audit_Znsb_SelfmachineModule "
                + "where IFNULL(parentmoduleguid,'') = ? and CenterGuid =? and  (Macaddress is null or Macaddress='') and Enable='1' order by Ordernum desc";
        return commonDao.findList(sql, ZCAuditZnsbSelfmachinemodule.class, parentmoduleguid, centerguid);
    }
    
    /**
     * 根据modulecodelevel,CenterGuid获取子模块
     */
    public List<ZCAuditZnsbSelfmachinemodule> findHotListByParentmoduleguid(String parentmoduleguid, String centerguid) {
        String sql = "select * from Audit_Znsb_SelfmachineModule "
                + "where IFNULL(parentmoduleguid,'') = ? and CenterGuid =? and  (Macaddress is null or Macaddress='') and Enable='1' order by hotnum desc";
        return commonDao.findList(sql, ZCAuditZnsbSelfmachinemodule.class, parentmoduleguid, centerguid);
    }
    
    /**
     * 根据modulecodelevel,CenterGuid获取子模块
     */
    public List<ZCAuditZnsbSelfmachinemodule> getModuleListByParentmoduleguidAndMac(String parentmoduleguid, String centerguid, String macaddress) {
        String sql = "select * from Audit_Znsb_SelfmachineModule "
                + "where IFNULL(parentmoduleguid,'') = ? and CenterGuid =? and  Macaddress = ? and Enable='1' order by Ordernum desc";
        return commonDao.findList(sql, ZCAuditZnsbSelfmachinemodule.class, parentmoduleguid, centerguid, macaddress);
    }
    
    /**
     * 根据modulecodelevel,CenterGuid获取子模块
     */
    public List<ZCAuditZnsbSelfmachinemodule> getHotModuleListByParentmoduleguidAndMac(String parentmoduleguid, String centerguid, String macaddress) {
        String sql = "select * from Audit_Znsb_SelfmachineModule "
                + "where IFNULL(parentmoduleguid,'') = ? and CenterGuid =? and  Macaddress = ? and Enable='1' order by hotnum desc";
        return commonDao.findList(sql, ZCAuditZnsbSelfmachinemodule.class, parentmoduleguid, centerguid, macaddress);
    }
    
    public List<ZCAuditZnsbSelfmachinemodule> getParentListByConfigType(String moduleconfigtype,String centerguid) {
        String sql = "select * from Audit_Znsb_SelfmachineModule "
                + "where IFNULL(parentmoduleguid,'') = '' and moduleconfigtype=? and centerguid =? and  (Macaddress is null or Macaddress='') order by Ordernum desc";
        return commonDao.findList(sql, ZCAuditZnsbSelfmachinemodule.class, moduleconfigtype, centerguid);
    }
    
    public List<ZCAuditZnsbSelfmachinemodule> getParentListByConfigTypeAndMac(String moduleconfigtype,String centerguid,String macaddress) {
        String sql = "select * from Audit_Znsb_SelfmachineModule "
                + "where IFNULL(parentmoduleguid,'') = '' and moduleconfigtype=? and centerguid =? and  Macaddress=? order by Ordernum desc";
        return commonDao.findList(sql, ZCAuditZnsbSelfmachinemodule.class, moduleconfigtype, centerguid,macaddress);
    }

    /**
     * 初始化新增模块数据
     */
    public void initModule(String operateusername, String modulename, String moduletype, String centerguid,
            String picturepath, String htmlurl, String isneedlogin, String enable, int ordernum) {
        ZCAuditZnsbSelfmachinemodule module = new ZCAuditZnsbSelfmachinemodule();
        module.setRowguid(UUID.randomUUID().toString());
        module.setOperatedate(new Date());
        module.setOperateusername(operateusername);
        module.setModulename(modulename);
        module.setModuletype(moduletype);
        module.setCenterguid(centerguid);
        module.setPicturepath(picturepath);
        module.setHtmlurl(htmlurl);
        module.setIsneedlogin(isneedlogin);
        module.setEnable(enable);
        module.setOrdernum(ordernum);
        insert(module);
    }
    
    /**
     * 初始化新增模块数据
     */
    public void initModule(String operateusername,String centerGuid,Record modulerecord) {
        ZCAuditZnsbSelfmachinemodule module = new ZCAuditZnsbSelfmachinemodule();
        if(StringUtil.isNotBlank(modulerecord.getStr("rowguid"))){
            // 工作台模块增加配置
            module.setRowguid(modulerecord.getStr("rowguid"));
            if (StringUtil.isNotBlank(modulerecord.getStr("parentmoduleguid"))) {
                module.setParentmoduleguid(modulerecord.getStr("parentmoduleguid"));
            }
            if (StringUtil.isNotBlank(modulerecord.getStr("speciallabel"))) {
                module.setSpeciallabel(modulerecord.getStr("speciallabel"));
            }
            if (StringUtil.isNotBlank(modulerecord.getStr("linenum"))) {
                module.setLinenum(modulerecord.getStr("linenum"));
            }
            if (StringUtil.isNotBlank(modulerecord.getInt("hotnum"))) {
                module.setHotnum(modulerecord.getInt("hotnum"));
            }
        }
        else{
            module.setRowguid(UUID.randomUUID().toString());
        }
        module.setOperatedate(new Date());
        module.setOperateusername(operateusername);
        module.setModulename( modulerecord.getStr("modulename"));
        module.setModuletype( modulerecord.getStr("moduletype"));
        module.setCenterguid( centerGuid);
        module.setPicturepath( modulerecord.getStr("picturepath"));
        module.setHtmlurl( modulerecord.getStr("htmlurl"));
        module.setIsneedlogin( modulerecord.getStr("isneedlogin"));
        module.setEnable( QueueConstant.CONSTANT_STR_ONE);
        module.setOrdernum( modulerecord.getInt("ordernum"));
        module.setModuleconfigtype( modulerecord.getStr("moduleconfigtype"));
        insert(module);
    }
    
    

    /**
     * 
     *  复制模块初始化数据
     *  @param operateusername
     *  @param macaddress
     *  @param modulename
     *  @param moduletype
     *  @param centerguid
     *  @param picturepath
     *  @param htmlurl
     *  @param isneedlogin
     *  @param enable
     *  @param ordernum    
     * 
     * 
     */
    public void copyModule(String operateusername, String macaddress, String modulename, String moduletype,
            String centerguid, String picturepath, String htmlurl, String isneedlogin, String enable, int ordernum) {
        ZCAuditZnsbSelfmachinemodule module = new ZCAuditZnsbSelfmachinemodule();
        module.setRowguid(UUID.randomUUID().toString());
        module.setOperatedate(new Date());
        module.setOperateusername(operateusername);
        module.setMacaddress(macaddress);
        module.setModulename(modulename);
        module.setModuletype(moduletype);
        module.setCenterguid(centerguid);
        module.setPicturepath(picturepath);
        module.setHtmlurl(htmlurl);
        module.setIsneedlogin(isneedlogin);
        module.setEnable(enable);
        module.setOrdernum(ordernum);
        insert(module);

    }
    
    /**
     * 
     *  复制模块初始化数据
     *  @param operateusername
     *  @param macaddress
     *  @param modulename
     *  @param moduletype
     *  @param centerguid
     *  @param picturepath
     *  @param htmlurl
     *  @param isneedlogin
     *  @param enable
     *  @param ordernum    
     * 
     * 
     */
    public void copyModule(String operateusername, String macaddress, String centerGuid,  ZCAuditZnsbSelfmachinemodule moduleobj ) {
        ZCAuditZnsbSelfmachinemodule module = new ZCAuditZnsbSelfmachinemodule();
        module.setRowguid(UUID.randomUUID().toString());
        module.setOperatedate(new Date());
        module.setOperateusername(operateusername);
        module.setMacaddress(macaddress);
        module.setModulename(moduleobj.getModulename());
        module.setModuletype(moduleobj.getModuletype());
        module.setCenterguid(centerGuid);
        module.setPicturepath(moduleobj.getPicturepath());
        module.setHtmlurl(moduleobj.getHtmlurl());
        module.setIsneedlogin(moduleobj.getIsneedlogin());
        module.setEnable(QueueConstant.CONSTANT_STR_ONE);
        module.setOrdernum(moduleobj.getOrdernum());
        module.setModuleconfigtype(moduleobj.getModuleconfigtype());
        insert(module);

    }

    /**
     * 根据mac地址查找热门模块列表
     */
    public List<ZCAuditZnsbSelfmachinemodule> findHotTypeListByMac(String macaddress, String centerguid,
            int modulecount) {
        String sql = "";
        if (commonDao.isSqlserver()) {
            sql = "select top " + modulecount
                    + " * from Audit_Znsb_SelfmachineModule  where Macaddress=? and centerguid=?  and Enable='1' order by hotnum desc ";

        }
        else if (commonDao.isOracle()) {
            sql = "select * from (select * from Audit_Znsb_SelfmachineModule where Macaddress=? and centerguid=?  and Enable='1' order by hotnum desc)  q where rownum <= ?";
        }
        else if (commonDao.isDM()) {
            sql = "select * from (select * from Audit_Znsb_SelfmachineModule where Macaddress=? and centerguid=?  and Enable='1' order by hotnum desc)  q where rownum <= ?";
        }
        else if ("Atlas".equalsIgnoreCase(commonDao.getDataSource().getDatabase())) {
            sql = "select * from (select * from Audit_Znsb_SelfmachineModule where Macaddress=? and centerguid=?  and Enable='1' order by hotnum desc)  q where rownum <= ?";
        }
        else {
            sql = "select * from Audit_Znsb_SelfmachineModule  where Macaddress=? and centerguid=?  and Enable='1' order by hotnum desc limit ?";
        }

        return commonDao.findList(sql, ZCAuditZnsbSelfmachinemodule.class, macaddress, centerguid, modulecount);
    }

    /**
     *  查找centguid下通用热门模块列表
     */
    public List<ZCAuditZnsbSelfmachinemodule> findHotTypeList(String centerguid, int modulecount) {
        String sql = "";
        if (commonDao.isSqlserver()) {
            sql = "select top " + modulecount
                    + " * from Audit_Znsb_SelfmachineModule  where  (Macaddress is null or Macaddress ='') and centerguid=?  and Enable='1' order by hotnum desc ";

        }
        else if (commonDao.isOracle()) {
            sql = "select * from (select * from Audit_Znsb_SelfmachineModule where  (Macaddress is null or Macaddress ='')  and centerguid=?  and Enable='1' order by hotnum desc)  q where rownum <= ?";
        }
        else if (commonDao.isDM()) {
            sql = "select * from (select * from Audit_Znsb_SelfmachineModule where  (Macaddress is null or Macaddress ='')  and centerguid=?  and Enable='1' order by hotnum desc)  q where rownum <= ?";
        }
        else if ("Atlas".equalsIgnoreCase(commonDao.getDataSource().getDatabase())) {
            sql = "select * from (select * from Audit_Znsb_SelfmachineModule where  (Macaddress is null or Macaddress ='')  and centerguid=?  and Enable='1' order by hotnum desc)  q where rownum <= ?";
        }
        else {
            sql = "select * from Audit_Znsb_SelfmachineModule  where  (Macaddress is null or Macaddress ='') and centerguid=?  and Enable='1' order by hotnum desc limit ?";
        }

        return commonDao.findList(sql, ZCAuditZnsbSelfmachinemodule.class, centerguid, modulecount);
    }

    /**
     * 根据mac地址查找热门模块列表
     */
    public ZCAuditZnsbSelfmachinemodule getModuleByModulenameAndMac(String macaddress, String modulename) {
        String sql = "select * from Audit_Znsb_SelfmachineModule where Macaddress=?  and modulename=?";

        return commonDao.find(sql, ZCAuditZnsbSelfmachinemodule.class, macaddress, modulename);
    }

    /**
     * 根据centerguid地址查找热门模块列表
     */
    public ZCAuditZnsbSelfmachinemodule getModuleByModulenameAndCenterguid(String centerguid, String modulename) {
        String sql = "select * from Audit_Znsb_SelfmachineModule where centerguid=?  and modulename=? and (Macaddress is null or Macaddress ='')";

        return commonDao.find(sql, ZCAuditZnsbSelfmachinemodule.class, centerguid, modulename);
    }

}
