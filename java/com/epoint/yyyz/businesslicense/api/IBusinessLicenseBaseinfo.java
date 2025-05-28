package com.epoint.yyyz.businesslicense.api;

import java.io.Serializable;
import java.util.List;

import com.epoint.cert.basic.certcatalog.areacertcatalog.domain.CertCatalog;
import com.epoint.core.grammar.Record;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import com.epoint.yyyz.businesslicense.api.entity.BusinessLicenseBaseinfo;
import com.epoint.yyyz.businesslicense.api.entity.BusinessLicenseExtension;

/**
 * 一业一证基本信息接口
 * @description
 * @author shibin
 * @date  2020年5月19日 下午2:52:35
 */
public interface IBusinessLicenseBaseinfo extends Serializable
{

    /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(BusinessLicenseBaseinfo record);

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
    public int update(BusinessLicenseBaseinfo record);

    /**
     * 根据ID查找单个实体
     * 
     * @param clazz
     *            类<必须继承BaseEntity>
     * @param primaryKey
     *            主键
     * @return T extends BaseEntity
     */
    public BusinessLicenseBaseinfo find(Object primaryKey);

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
    public BusinessLicenseBaseinfo find(String sql, Object... args);

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
    public List<BusinessLicenseBaseinfo> findList(String sql, Object... args);

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
    public List<BusinessLicenseBaseinfo> findList(String sql, int pageNumber, int pageSize, Object... args);

    /**
     * @description
     * @author shibin
     * @date  2020年5月23日 下午2:32:46
     */
    public Record getBusinessBaseinfoByBiguid(String biGuid);

    public List<Record> getTaskidsByBusinessguid(String businessguid);

    /**
     * 
     *  [根据办件实例获取该办件的上传材料信息] 
     *  @param rowGuid
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public BusinessLicenseExtension getBusinessBaseinfoByBiguidAndBuesiness(String biGuid, String businessguid);

    /**
     * 根据buguid获取申报信息
     * @description
     * @author shibin
     * @date  2020年6月22日 下午5:37:41
     */
    public BusinessLicenseBaseinfo getBaseinfoByBiguid(String biguid);

    /**
     * 根据certguid获取申报信息
     * @description
     * @author shibin
     * @date  2020年6月22日 下午6:22:39
     */
    public BusinessLicenseBaseinfo getBaseinfoByCertguid(String guid);
    
    /**
     * 根据yyyzcliengguid和yyyztype查询附件
     * @description
     * @author shibin
     * @date  2020年6月22日 下午6:22:39
     */
    public FrameAttachInfo getAttachByYyyzCliengguid(String yyyzcliengguid, String yyyztype);

    /**
     * 根据ouguid和
     *  [一句话功能简述] 
     *  @param ouguid
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public List<CertCatalog> findCertCatalogByOuguid(String ouguid);
    
    /**
     * 根据ouguid和
     *  [一句话功能简述] 
     *  @param ouguid
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public List<CertCatalog> findCertCatalogZzlbByOuguid(String ouguid);
    

}
