package com.epoint.zoucheng.znsb.auditznsbcommentmatter.impl;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.basic.auditqueue.service.AuditQueueBasicService;
import com.epoint.zoucheng.znsb.auditznsbcommentmatter.domain.ZCAuditZnsbCommentMatter;
import com.epoint.zoucheng.znsb.auditznsbcommentmatter.inter.IZCAuditZnsbCommentMatterService;
import com.epoint.zoucheng.znsb.auditznsbcommentmatter.service.ZCAuditZnsbCommentMatterService;
import com.epoint.common.service.AuditCommonResult;
import com.epoint.database.peisistence.crud.impl.model.PageData;
/**
 * 工作台评价事项记录表对应的后台service实现类
 * 
 * @author chencong
 * @version [版本号, 2020-04-08 13:37:36]
 */
@Component
@Service
public class ZCAuditZnsbCommentMatterServiceImpl implements IZCAuditZnsbCommentMatterService
{
    /**
     * 
     */
    private static final long serialVersionUID = -7495189190469900975L;

    /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(ZCAuditZnsbCommentMatter record) {
        return new ZCAuditZnsbCommentMatterService().insert(record);
    }

    /**
     * 删除数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int deleteByGuid(String guid) {
        return new ZCAuditZnsbCommentMatterService().deleteByGuid(guid);
    }

    /**
     * 更新数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(ZCAuditZnsbCommentMatter record) {
        return new ZCAuditZnsbCommentMatterService().update(record);
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
    public ZCAuditZnsbCommentMatter find(Object primaryKey) {
       return new ZCAuditZnsbCommentMatterService().find(primaryKey);
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
    public ZCAuditZnsbCommentMatter find(String sql, Object... args) {
        return new ZCAuditZnsbCommentMatterService().find(sql,args);
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
    public List<ZCAuditZnsbCommentMatter> findList(String sql, Object... args) {
       return new ZCAuditZnsbCommentMatterService().findList(sql,args);
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
    public List<ZCAuditZnsbCommentMatter> findList(String sql, int pageNumber, int pageSize, Object... args) {
       return new ZCAuditZnsbCommentMatterService().findList(sql,pageNumber,pageSize,args);
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
    public Integer countAuditZnsbCommentMatter(String sql, Object... args){
        return new ZCAuditZnsbCommentMatterService().countAuditZnsbCommentMatter(sql, args);
    }

     /**
      * 
      *  [根据身份证获取当天评价信息] 
      *  @param card
      *  @return    
      * @exception/throws [违例类型] [违例说明]
      * @see [类、类#方法、类#成员]
      */
     @Override
     public AuditCommonResult<List<ZCAuditZnsbCommentMatter>> getAuditZnsbCommentMatterByCard(String card) {
         ZCAuditZnsbCommentMatterService matterService = new ZCAuditZnsbCommentMatterService();
         AuditCommonResult<List<ZCAuditZnsbCommentMatter>> result = new AuditCommonResult<List<ZCAuditZnsbCommentMatter>>();
         try {
             result.setResult(matterService.getAuditZnsbCommentMatterByCard(card));
         }
         catch (Exception e) {
             result.setSystemFail(e.toString());
         }
         return result;
     }
     /**
      * 
      *  [分页查询数据] 
      *  @param conditionMap
      *  @param first
      *  @param pageSize
      *  @param sortField
      *  @param sortOrder
      *  @return    
      * @exception/throws [违例类型] [违例说明]
      * @see [类、类#方法、类#成员]
      */
     @Override
     public AuditCommonResult<PageData<ZCAuditZnsbCommentMatter>> getAuditZnsbCommentMatterByPage(Map<String, String> conditionMap,
             Integer first, Integer pageSize, String sortField, String sortOrder) {

         AuditQueueBasicService<ZCAuditZnsbCommentMatter> commonservcie = new AuditQueueBasicService<ZCAuditZnsbCommentMatter>();
         AuditCommonResult<PageData<ZCAuditZnsbCommentMatter>> result = new AuditCommonResult<PageData<ZCAuditZnsbCommentMatter>>();
         try {
             PageData<ZCAuditZnsbCommentMatter> pagedata = commonservcie
                     .getRecordPageData(ZCAuditZnsbCommentMatter.class, conditionMap, first, pageSize, sortField, sortOrder);

             result.setResult(pagedata);
         }
         catch (Exception e) {
             result.setSystemFail(e.toString());
         }
         return result;
     }
}
