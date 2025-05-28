package com.epoint.zoucheng.device.infopub.infopubplayterminal.impl;

import java.util.List;

import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.zoucheng.device.infopub.infopubplayterminal.api.entity.InfopubPlayterminal;

/**
 * 发布单终端列表对应的后台service
 * 
 * @author why
 * @version [版本号, 2019-09-23 11:20:38]
 */
public class InfopubPlayterminalService
{
    /**
     * 数据增删改查组件
     */
    protected ICommonDao baseDao;

    public InfopubPlayterminalService() {
        baseDao = CommonDao.getInstance();
    }

    /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(InfopubPlayterminal record) {
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
        T t = baseDao.find(InfopubPlayterminal.class, guid);
        return baseDao.delete(t);
    }

    /**
     * 更新数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(InfopubPlayterminal record) {
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
    public InfopubPlayterminal find(Object primaryKey) {
        return baseDao.find(InfopubPlayterminal.class, primaryKey);
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
    public InfopubPlayterminal find(String sql, Object... args) {
        return baseDao.find(sql, InfopubPlayterminal.class, args);
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
    public List<InfopubPlayterminal> findList(String sql, Object... args) {
        return baseDao.findList(sql, InfopubPlayterminal.class, args);
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
    public List<InfopubPlayterminal> findList(String sql, int pageNumber, int pageSize, Object... args) {
        return baseDao.findList(sql, pageNumber, pageSize, InfopubPlayterminal.class, args);
    }

    /**
     * 
     *  [删除] 
     *  @param sel    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public void deleteByPlayGuid(String playguid) {
        String sql = "delete from infopub_playterminal where playguid = ?";
        baseDao.execute(sql, playguid);
    }

    /**
     * 
     *  [获取设备guid] 
     *  @param terminalguid
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public String getMacByGuid(String terminalguid) {
        String sql = "select macaddress from AUDIT_ZNSB_EQUIPMENT where rowguid = ?";
        return baseDao.queryString(sql, terminalguid);
    }

    /**
     * 
     *  [获取位置] 
     *  @param terminalguid
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public String getLocationByGuid(String terminalguid) {
        String sql = "select hallname from AUDIT_ZNSB_EQUIPMENT where rowguid = ?";
        return baseDao.queryString(sql, terminalguid);
    }

    /**
     * 
     *  [获取ip] 
     *  @param terminalguid
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public String getIPByGuid(String terminalguid) {
        String sql = "select machinename from AUDIT_ZNSB_EQUIPMENT where rowguid = ?";
        return baseDao.queryString(sql, terminalguid);
    }

    /**
     * 
     *  [获得设备guid] 
     *  @param rowGuid
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public List<InfopubPlayterminal> getTerminalGuid(String rowGuid) {
        String sql = "select terminalguid from infopub_playterminal where playguid = ?";
        return baseDao.findList(sql, InfopubPlayterminal.class, rowGuid);
    }
    
    /**
     * 
     *  [添加播放设备时判断是否存在已经被使用的设备(查rowguid)] 
     *  @param rowguids
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public List<String> getRowguidInService(List<String> rowguids) {
        String sql = "select terminalguid from infopub_playterminal,infopub_play where infopub_playterminal.PlayGuid = infopub_play.RowGuid and terminalguid in (" + StringUtil.join(rowguids, ",") + ")";
        return baseDao.findList(sql, String.class);
    }
    /**
     * 
     *  [一句话功能简述] 
     *  @param playguid
     *  @param sel
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public int getCount(String playguid, String sel) {
        String sql = "select count(*) from infopub_playterminal where PlayGuid = ? and TerminalGuid = ?";
        return baseDao.queryInt(sql, playguid, sel);
    }

    public String getPlayGuid(String macaddress) {
        String sql = "select playguid from infopub_playterminal pt, AUDIT_ZNSB_EQUIPMENT t where  pt.TerminalGuid = t.RowGuid and t.macaddress = ?";
        return baseDao.queryString(sql, macaddress);
    }

}
