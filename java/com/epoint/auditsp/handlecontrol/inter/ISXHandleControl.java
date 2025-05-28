package com.epoint.auditsp.handlecontrol.inter;

import java.util.HashMap;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.common.service.AuditCommonResult;

/**
 *  按钮控制接口
 *  
 * @author CTY
 * @version [版本号, 2019年5月15日]
 */
@Service
public interface ISXHandleControl {

	/**
	 * 
	 * @param projectGuid 办件标识
	 * @param taskGuid 事项标识
	 * @param workItemGuid 工作项标识
	 * @param centerGuid 中心标识
	 * @param areaCode 辖区编码
	 * @param userGuid 用户标识
	 * @param windowguid 窗口标识
	 * @return
	 */
	public AuditCommonResult<JSONObject> initHandleControl(String projectGuid, String taskGuid, String workItemGuid,
			String centerGuid, String areaCode, String userGuid,String windowguid);
	
	/**
	 * 
	 * @param auditProject 办件
	 * @param taskGuid 事项标识
	 * @param workItemGuid 工作项标识
	 * @param centerGuid 中心标识
	 * @param areaCode 辖区编码
	 * @param userGuid 用户标识
	 * @param windowguid 窗口标识
	 * @return
	 */
	@SuppressWarnings("rawtypes")
    public HashMap<String, HashMap> initHandleControls(AuditProject auditProject, String taskGuid, String workItemGuid,
			String centerGuid, String areaCode, String userGuid,String windowguid);
	
	/**
	 * 
	 * @param auditProject 办件对象
	 * @param taskGuid 事项标识
	 * @param workItemGuid 工作项标识
	 * @param centerGuid 中心标识
	 * @param areaCode 辖区编码
	 * @param userGuid 用户标识
	 * @param windowguid 窗口标识
	 * @return
	 */
	public AuditCommonResult<JSONObject> initHandleControl(AuditProject auditProject, String taskGuid, String workItemGuid,
            String centerGuid, String areaCode, String userGuid,String windowguid);
}
