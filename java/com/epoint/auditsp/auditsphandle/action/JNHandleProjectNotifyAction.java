package com.epoint.auditsp.auditsphandle.action;

import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.controller.BaseController;
import com.epoint.core.dto.model.SelectItem;

/**
 * 材料告知页面对应的后台
 * 
 * @author Administrator
 *
 */
@RestController("jnhandleprojectnotifyaction")
@Scope("request")
public class JNHandleProjectNotifyAction extends BaseController
{
    /**
     * 
     */
    private static final long serialVersionUID = 8713148757403132395L;

    @Override
    public void pageLoad() {
        
    }

    /**
     * 根据项目名称获取项目列表
     * 
     * @param query
     *            输入的证照号
     * @return
     */
    public List<SelectItem> searchApplyerNameHistory(String query) {
		return null;

        

    }
    
    
}
