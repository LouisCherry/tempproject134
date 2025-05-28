package com.epoint.auditsp.auditsphandle.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.auditsp.auditspimaterial.domain.AuditSpIMaterial;
import com.epoint.basic.auditsp.auditspimaterial.inter.IAuditSpIMaterial;
import com.epoint.basic.controller.BaseController;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.core.utils.string.StringUtil;

@RestController("jndtauditsharematerialattachlistaction")
@Scope("request")
public class JNDTAuditShareMaterialAttachlistAction extends BaseController
{
    /**
     * 
     */
    private static final long serialVersionUID = 6196380024743989243L;


    
    private String spimaterial;
    
    /**
     * 不通过理由
     */
    private String remark;



    @Autowired
    private IAuditSpIMaterial iauditspimaterial;
    
    

    @Override
    public void pageLoad() {
        spimaterial = getRequestParameter("materialInstanceGuid");
        if(StringUtil.isNotBlank(spimaterial)){
            AuditSpIMaterial auditspimaterial =  iauditspimaterial.getSpIMaterialByrowguid(spimaterial).getResult();
            if(auditspimaterial !=null){
                addCallbackParam("remark", auditspimaterial.get("remark"));
            }
        }
    }


    public void notpass(){
        String msg = "";
        if (StringUtil.isBlank(remark)) {
            addCallbackParam("msg", "审核不通过理由必填！");
            return;
        }
        if(StringUtil.isNotBlank(spimaterial)){
           AuditSpIMaterial auditspimaterial =  iauditspimaterial.getSpIMaterialByrowguid(spimaterial).getResult();
           if(auditspimaterial !=null){
               auditspimaterial.setIsbuzheng(ZwfwConstant.CONSTANT_STR_ONE);
               auditspimaterial.set("remark", remark);
               iauditspimaterial.updateSpIMaterial(auditspimaterial);
               msg = "提交成功！";
           }else{
               msg = "未获取到材料！";
           }
        }else{
            msg = "未获取到材料标识！";
        }
        addCallbackParam("msg", msg);
    }
    
    
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
    
}
