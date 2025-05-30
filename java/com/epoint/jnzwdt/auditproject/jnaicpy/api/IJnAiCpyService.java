package com.epoint.jnzwdt.auditproject.jnaicpy.api;
import java.io.Serializable;
import java.util.List;

import com.epoint.core.grammar.Record;
import com.epoint.jnzwdt.auditproject.jnaicpy.api.entity.JnAiCpy;

/**
 * 成品油零售经营企业库对应的后台service接口
 * 
 * @author 18039505500
 * @version [版本号, 2020-06-23 19:57:28]
 */
public interface IJnAiCpyService extends Serializable
{ 
   
    /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(JnAiCpy record);

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
    public int update(JnAiCpy record);

    /**
     * 根据ID查找单个实体
     * 
     * @param clazz
     *            类<必须继承BaseEntity>
     * @param primaryKey
     *            主键
     * @return T extends BaseEntity
     */
    public JnAiCpy find(Object primaryKey);

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
    public JnAiCpy find(String sql,Object... args);

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
    public List<JnAiCpy> findList(String sql, Object... args);

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
    public List<JnAiCpy> findList(String sql, int pageNumber, int pageSize,Object... args);

	 /**
     * 查询数量
     * 
     * @param sql
     *            执行语句
     * @param args
     *            参数
     * @return Integer
     */
	 public Integer countJnAiCpy(String sql, Object... args);
	 
	 Record getJnAiCpyDetail(String certnum);
	 
	 int updateJnAiCpyByCode(String certnum);

    public Record getJsgczljc(String certnum);


    public void updateJsgczljc(String itemcode, String bghjsfzr);

    public void updateJsgczljcbghdz(String itemcode, String bghdz);

    public void updateJsgczljcbghfr(String itemcode, String bghfr);

    public void updateJsgczljcbghqymc(String itemcode, String bghqymc);

    public Record getFdczzzs(String certnum);

    public void updateFdczzzs(String itemcode, String bghssxq, String gxhzczb, String gxhqydz, String gxhqyfr,
            String gxhqym);
}
