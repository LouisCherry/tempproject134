package com.epoint.xmz.userfeedback.impl;
import java.util.List;

import org.springframework.stereotype.Component;
import com.epoint.xmz.userfeedback.api.entity.UserFeedback;
import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.basic.auditonlineuser.auditonlineindividual.domain.AuditOnlineIndividual;
import com.epoint.basic.auditonlineuser.auditonlineproject.domain.AuditOnlineProject;
import com.epoint.basic.auditonlineuser.auditonlineregister.domain.AuditOnlineRegister;
import com.epoint.basic.auditresource.company.domain.AuditRsCompanyBaseinfo;
import com.epoint.xmz.userfeedback.api.IUserFeedbackService;
/**
 * 用户反馈表对应的后台service实现类
 * 
 * @author 1
 * @version [版本号, 2022-10-28 15:37:33]
 */
@Component
@Service
public class UserFeedbackServiceImpl implements IUserFeedbackService
{
    /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(UserFeedback record) {
        return new UserFeedbackService().insert(record);
    }

    /**
     * 删除数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int deleteByGuid(String guid) {
        return new UserFeedbackService().deleteByGuid(guid);
    }

    /**
     * 更新数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(UserFeedback record) {
        return new UserFeedbackService().update(record);
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
    public UserFeedback find(Object primaryKey) {
       return new UserFeedbackService().find(primaryKey);
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
    public UserFeedback find(String sql, Object... args) {
        return new UserFeedbackService().find(sql,args);
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
    public List<UserFeedback> findList(String sql, Object... args) {
       return new UserFeedbackService().findList(sql,args);
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
    public List<UserFeedback> findList(String sql, int pageNumber, int pageSize, Object... args) {
       return new UserFeedbackService().findList(sql,pageNumber,pageSize,args);
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
    public Integer countUserFeedback(String sql, Object... args){
        return new UserFeedbackService().countUserFeedback(sql, args);
    }
     
     @Override
     public AuditOnlineIndividual getOnlineProjectByIdNumber(String idbumber){
    	 return new UserFeedbackService().getOnlineProjectByIdNumber(idbumber);
     }
     
     @Override
     public AuditRsCompanyBaseinfo getCompanyByCreditcode(String Creditcode) {
    	 return new UserFeedbackService().getCompanyByCreditcode(Creditcode);
     }
     
     

}
