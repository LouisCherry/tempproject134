package com.epoint.screen.api;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.epoint.common.service.AuditCommonResult;
import com.epoint.frame.service.organ.ou.entity.FrameOu;
/**
 * 部门
 */
@Component
@Service
public interface ILyFrameOu {

	/**
     * 根据角色名获取角色信息
     */
    public AuditCommonResult<FrameOu> getFrameOuByOuguid(String ouguid);
    
    /**
     * 查询部门的id
     *  [一句话功能简述]
     *  [功能详细描述]
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public  AuditCommonResult<List<FrameOu>> getAllFrameOu();
    
    public AuditCommonResult<FrameOu> getFrameOuByOuname(String ouname);
    
    public AuditCommonResult<String> getOulevelByOuname(String ouguid);
    
    public AuditCommonResult<List<FrameOu>> getXZOu();
    
    public AuditCommonResult<List<FrameOu>> getSQOu(String parentouguid);
}
