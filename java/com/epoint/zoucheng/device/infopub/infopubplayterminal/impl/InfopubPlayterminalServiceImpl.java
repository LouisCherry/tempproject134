package com.epoint.zoucheng.device.infopub.infopubplayterminal.impl;
import java.util.List;

import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.zoucheng.device.infopub.infopubplayterminal.api.IInfopubPlayterminalService;
import com.epoint.zoucheng.device.infopub.infopubplayterminal.api.entity.InfopubPlayterminal;
/**
 * 发布单终端列表对应的后台service实现类
 * 
 * @author why
 * @version [版本号, 2019-09-23 11:20:38]
 */
@Component
@Service
public class InfopubPlayterminalServiceImpl implements IInfopubPlayterminalService
{
    /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(InfopubPlayterminal record) {
        return new InfopubPlayterminalService().insert(record);
    }

    /**
     * 删除数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int deleteByGuid(String guid) {
        return new InfopubPlayterminalService().deleteByGuid(guid);
    }

    /**
     * 更新数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(InfopubPlayterminal record) {
        return new InfopubPlayterminalService().update(record);
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
       return new InfopubPlayterminalService().find(primaryKey);
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
        return new InfopubPlayterminalService().find(args);
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
       return new InfopubPlayterminalService().findList(sql,args);
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
       return new InfopubPlayterminalService().findList(sql,pageNumber,pageSize,args);
    }
    
    /**
     * 
     *  [删除] 
     *  @param sel    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @Override
    public void deleteByPlayGuid(String playguid) {
        new InfopubPlayterminalService().deleteByPlayGuid(playguid);
    }
    
    /**
     * 
     *  [获取设备guid] 
     *  @param terminalguid
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @Override
    public String getMacByGuid(String terminalguid) {
        return new InfopubPlayterminalService().getMacByGuid(terminalguid);
    }

    /**
     * 
     *  [获取位置] 
     *  @param terminalguid
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @Override
    public String getLocationByGuid(String terminalguid) {
        return new InfopubPlayterminalService().getLocationByGuid(terminalguid);
    }

    /**
     * 
     *  [获取ip] 
     *  @param terminalguid
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @Override
    public String getIPByGuid(String terminalguid) {
        return new InfopubPlayterminalService().getIPByGuid(terminalguid);
    }
    
    /**
     * 
     *  [获得设备guid] 
     *  @param rowGuid
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @Override
    public List<InfopubPlayterminal> getTerminalGuid(String rowGuid) {
        // TODO Auto-generated method stub
        return new InfopubPlayterminalService().getTerminalGuid(rowGuid);
    }
    
    /**
     * 
     *  [添加播放设备时判断是否存在已经被使用的设备(查rowguid)] 
     *  @param rowguids
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @Override
    public List<String> getRowguidInService(List<String> rowguids) {
        return new InfopubPlayterminalService().getRowguidInService(rowguids);
    }
    
    @Override
    public int getCount(String playguid, String sel) {
        return new InfopubPlayterminalService().getCount(playguid, sel);
    }

}
