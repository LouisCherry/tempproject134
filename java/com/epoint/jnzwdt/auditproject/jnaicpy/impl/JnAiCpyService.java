package com.epoint.jnzwdt.auditproject.jnaicpy.impl;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import com.epoint.core.grammar.Record;
import com.epoint.jnzwdt.auditproject.jnaicpy.api.entity.JnAiCpy;
import com.epoint.core.BaseEntity;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.dao.CommonDao;

/**
 * 成品油零售经营企业库对应的后台service
 * 
 * @author 18039505500
 * @version [版本号, 2020-06-23 19:57:28]
 */
public class JnAiCpyService
{
 /**
     * 数据增删改查组件
     */
    protected ICommonDao baseDao;

    public JnAiCpyService() {
        baseDao = CommonDao.getInstance();
    }
    /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(JnAiCpy record) {
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
        T t = baseDao.find(JnAiCpy.class, guid);
        return baseDao.delete(t);
    }

    /**
     * 更新数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(JnAiCpy record) {
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
    public JnAiCpy find(Object primaryKey) {
        return baseDao.find(JnAiCpy.class, primaryKey);
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
    public JnAiCpy find(String sql,  Object... args) {
        return baseDao.find(sql, JnAiCpy.class, args);
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
    public List<JnAiCpy> findList(String sql, Object... args) {
        return baseDao.findList(sql, JnAiCpy.class, args);
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
    public List<JnAiCpy> findList(String sql, int pageNumber, int pageSize, Object... args) {
        return baseDao.findList(sql, pageNumber, pageSize, JnAiCpy.class, args);
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
    public Integer countJnAiCpy(String sql, Object... args){
        return baseDao.queryInt(sql, args);
    }
    
    
    
    public Record getJnAiCpyDetail(String certnum) {
        String sql = "select * from jn_ai_cpy where code = ? order by version desc limit 1";
        return CommonDao.getInstance().find(sql, Record.class, certnum);
    }
    
    
    public int updateJnAiCpyByCode(String certnum) {
    	String sql = "update jn_ai_cpy set is_enable = '0' where code = '" + certnum + "'";
    	int result = CommonDao.getInstance().execute(sql);
    	CommonDao.getInstance().close();
    	return result;
    }
    public Record getJsgczljc(String certnum) {
        String sql = "select * from xmz_jsgczljcjgzzsp where qytyshxydm = ? limit 1";
        return CommonDao.getInstance().find(sql, Record.class, certnum);
    }
   
    public void updateJsgczljc(String itemcode, String bghjsfzr) {
        String sql = "update xmz_jsgczljcjgzzsp set jsfzr = '"+bghjsfzr+"' where qytyshxydm = '" + itemcode + "'";
        CommonDao.getInstance().execute(sql);
        CommonDao.getInstance().close();
        
    }
    public void updateJsgczljcbghdz(String itemcode, String bghdz) {
        String sql = "update xmz_jsgczljcjgzzsp set xxdz = '"+bghdz+"' where qytyshxydm = '" + itemcode + "'";
        CommonDao.getInstance().execute(sql);
        CommonDao.getInstance().close();
        
    }
    public void updateJsgczljcbghfr(String itemcode, String bghfr) {
        String sql = "update xmz_jsgczljcjgzzsp set fddbr = '"+bghfr+"' where qytyshxydm = '" + itemcode + "'";
        CommonDao.getInstance().execute(sql);
        CommonDao.getInstance().close();
        
    }
    public void updateJsgczljcbghqymc(String itemcode, String bghqymc) {
        String sql = "update xmz_jsgczljcjgzzsp set qymc = '"+bghqymc+"' where qytyshxydm = '" + itemcode + "'";
        CommonDao.getInstance().execute(sql);
        CommonDao.getInstance().close();
        
    }
    public Record getFdczzzs(String certnum) {
        String sql = "select * from xmz_fdckfzzzs where yyzz = ? limit 1";
        return CommonDao.getInstance().find(sql, Record.class, certnum);
    }
    
    public void updateFdczzzs(String itemcode, String bghssxq, String gxhzczb, String gxhqydz, String gxhqyfr,
            String gxhqym) {
        String sql = "update xmz_fdckfzzzs set qhdm = '"+bghssxq+"' ,  zczb='"+gxhzczb+"' , dz='"+gxhqydz+"' , fddbr='"+gxhqyfr+"' , qymc='"+gxhqym+"'    where yyzz = '" + itemcode + "'";
        CommonDao.getInstance().execute(sql);
        CommonDao.getInstance().close();   
    }
    
}
