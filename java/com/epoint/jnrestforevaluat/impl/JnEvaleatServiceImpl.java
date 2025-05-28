package com.epoint.jnrestforevaluat.impl;

import java.util.List;

import org.springframework.stereotype.Component;
import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.frame.service.organ.ou.entity.FrameOu;
import com.epoint.frame.service.organ.user.entity.FrameUser;
import com.epoint.jnrestforevaluat.api.IJnEvaluatService;
/**
 * 事项同步错误记录表对应的后台service实现类
 * 
 * @author zhaoy
 * @version [版本号, 2019-01-23 16:39:23]
 */
@Component
@Service
public class JnEvaleatServiceImpl implements IJnEvaluatService
{

    /**
     * 
     */
    private static final long serialVersionUID = 1915641747987393632L;

    @Override
    public List<FrameOu> getOuinfo(String ouguid,String areacode) {
        JnEvaluatService service = new JnEvaluatService();
        return service.getOuinfo(ouguid,areacode);
    }
 
    public PageData<AuditProject> getProjectinfo(String ouguid,String areacode,String startdate,
            String enddate,String flowsn,Integer pagesize,Integer index) {
        JnEvaluatService service = new JnEvaluatService();
        return service.getProjectinfo(ouguid, areacode, startdate, enddate, flowsn,pagesize,index);
    }
    
    public PageData<FrameUser> getUserinfo(String ouguid,String areacode,String userguid,Integer pagesize,Integer index) {
        JnEvaluatService service = new JnEvaluatService();
        return service.getUserinfo(ouguid, areacode, userguid, pagesize, index);
    }
    
    public Integer isExistProject(String projectguid){
        JnEvaluatService service = new JnEvaluatService();
        return service.isExistProject(projectguid);
    }
}
