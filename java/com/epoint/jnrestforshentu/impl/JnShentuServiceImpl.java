package com.epoint.jnrestforshentu.impl;


import java.util.List;

import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.basic.auditresource.auditrsitembaseinfo.domain.AuditRsItemBaseinfo;
import com.epoint.core.grammar.Record;
import com.epoint.jnrestforshentu.api.IJnShentuService;
import com.epoint.jnrestforshentu.api.entity.AuditThreeFirst;
/**
 * 事项同步错误记录表对应的后台service实现类
 * 
 * @author zhaoy
 * @version [版本号, 2019-01-23 16:39:23]
 */
@Component
@Service
public class JnShentuServiceImpl implements IJnShentuService
{

    /**
     * 
     */
    private static final long serialVersionUID = 1915641747987393632L;
    
    public Integer isExistProject(String projectguid){
        JnShentuService service = new JnShentuService();
        return service.isExistProject(projectguid);
    }

    public List<AuditRsItemBaseinfo> getTsProjectInfo(String flowsn, String name, String creditcode) {
        JnShentuService service = new JnShentuService();
        return service.getTsProjectInfo(flowsn, name, creditcode);
    }

    public Integer isExistEvaluate(String projectguid) {
        JnShentuService service = new JnShentuService();
        return service.isExistEvaluate(projectguid);
    }
    
    public Integer addTsOpinion(Record opinion) {
        JnShentuService service = new JnShentuService();
        return service.addTsOpinion(opinion);
    }
    
    public Record getTsProjectInfoByRowguid(String biguid) {
        JnShentuService service = new JnShentuService();
        return service.getTsProjectInfoByRowguid(biguid);
    }
    
    public AuditRsItemBaseinfo getTsClientguidByRowguid(String biguid) {
        JnShentuService service = new JnShentuService();
        return service.getTsClientguidByRowguid(biguid);
    }
    
    public List<Record> getThreeFirstByAttachguid(String type) {
        JnShentuService service = new JnShentuService();
        return service.getThreeFirstByAttachguid(type);
    }
    
    /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(AuditThreeFirst record) {
        return new JnShentuService().insert(record);
    }

}
