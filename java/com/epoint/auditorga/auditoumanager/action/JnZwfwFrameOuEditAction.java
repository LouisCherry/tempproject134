package com.epoint.auditorga.auditoumanager.action;

import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.basic.soa.SOAService;
import com.epoint.core.dto.ExtValue;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.utils.convert.ConvertUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.frame.service.organ.ou.api.IOuService;
import com.epoint.frame.service.organ.ou.api.IOuServiceInternal;
import com.epoint.frame.service.organ.ou.entity.FrameOu;
import com.epoint.frame.service.organ.ou.entity.FrameOuExtendInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 部门修改
 * 
 * @author wry
 * @version [版本号, 2016年3月10日]
 */
@RestController("jnzwfwframeoueditaction")
@Scope("request")
public class JnZwfwFrameOuEditAction extends BaseController
{

    /**
     * 
     */
    private static final long serialVersionUID = 8803774929027640364L;
    /**
     * 当前部门对象
     */
    private FrameOu frameOu;

    /**
     * 父部门名称
     */
    private String parentOuName;

    /**
     * 子流转
     */
    private Boolean isSubWebFlow;

    /**
     * 父部门GUid
     */
    private String parentOUGuid;

    /**
     * 是否独立单位
     */
    private Boolean isIndependence;

    private String ouguid;

    private String baseOuGuid;

    private FrameOuExtendInfo frameOuExtendInfo;
    /**
     * 是否是窗口部门
     */
    private String isWindowOu;
    /**
     * 是否是窗口部门
     */
    private String isshow;
    /**
     * 辖区编号
     */
    private String areacode;

    /**
     * 组织机构代码/统一社会信用代码
     */
    private String orgcode;

    /**
     * 是否是窗口单选框
     */
    private List<SelectItem> isWindowOuModal;
    /**
     * 是否是窗口单选框
     */
    private List<SelectItem> isShowQz;

    @Autowired
    private IOuServiceInternal ouService;

    @Autowired
    private IOuService ouservice;
    
    @Override
    public void pageLoad() {
        if (SOAService.isEnableSOA()) {
            addCallbackParam("soa", SOAService.message);
            // return;
        }
        // 传入上级部门guid
        ouguid = getRequestParameter("ouguid");
        frameOu = ouService.getOuByOuGuid(ouguid);
        parentOUGuid = frameOu.getParentOuguid();
        frameOu.setOldParentOuGuid(parentOUGuid);
        baseOuGuid = frameOu.getBaseOuguid();
        frameOu.setOldBaseOuGuid(baseOuGuid);
        if (StringUtil.isNotBlank(frameOu.getParentOuguid())) {
            parentOuName = ouService.getParentOuName(frameOu.getParentOuguid(), false);
        }
        if (frameOu.getIsSubWebFlow() != null && frameOu.getIsSubWebFlow() == 1) {
            isSubWebFlow = true;
        }
        frameOuExtendInfo = ouService.getFrameOuExtendInfo(frameOu.getOuguid());
        if (frameOuExtendInfo == null) {
            frameOuExtendInfo = new FrameOuExtendInfo();
            frameOuExtendInfo.setIsIndependence(Integer.valueOf(0));
        }
        isIndependence = ConvertUtil.convertIntegerToBoolean(frameOuExtendInfo.getIsIndependence());
        if (StringUtil.isBlank(parentOuName)) {
            parentOuName = "根";
        }
        // 根据ouguid得到个性化的信息
        FrameOuExtendInfo ouExtendInfo = ouService.getFrameOuExtendInfo(ouguid);
        if (ouExtendInfo != null) {
            addCallbackParam("isWindowOu", ouExtendInfo.get("is_WindowOU"));
            //addCallbackParam("areacode", ouExtendInfo.get("areacode"));
            isWindowOu = ouExtendInfo.get("is_WindowOU")+"";
            addCallbackParam("orgcode", ouExtendInfo.get("orgcode"));
        }
    }

    /**
     * 修改部门信息
     */
    public void update() {
        if (frameOu.getOuname().length() > 50) {
            addCallbackParam("msg", "部门名称不允许超过50位！");
            return;
        }
        FrameOu frameOuTemp=   ouService.getOuByOuField("oucode", frameOu.getOucode());
        if(frameOuTemp!=null&&(!frameOuTemp.isEmpty())&&(!frameOu.getOuguid().equals(frameOuTemp.getOuguid())))
        {
            addCallbackParam("msg", "部门代码重复！");
            return;
        }
        List<FrameOuExtendInfo> extendinfos = ouService.getAllFrameOuExtendInfo();
        for(FrameOuExtendInfo extendinfo : extendinfos){
            if(StringUtil.isNotBlank(extendinfo.get("orgcode")) && !extendinfo.getOuGuid().equals(frameOu.getOuguid())){
                if(StringUtil.isNotBlank(orgcode) && orgcode.equals(extendinfo.get("orgcode"))) {
                    addCallbackParam("msg", "组织机构代码/统一社会信用代码重复！");
                    return;
                }
            }
        }
        frameOu.setIsSubWebFlow(ConvertUtil.convertBooleanToInteger(getIsSubWebFlow()));
        // 设置父部门guid
        frameOu.setParentOuguid(parentOUGuid);

        if (StringUtil.isBlank(frameOu.getOushortName())) {
            frameOu.setOushortName(frameOu.getOuname());
        }

        frameOuExtendInfo.setIsIndependence(ConvertUtil.convertBooleanToInteger(isIndependence));
        if (isIndependence) {
            // 如果是独立单位，就把ouguid赋值给baseouguid
            frameOu.setBaseOuguid(frameOu.getOuguid());
        }
        else {
            // 如果不是独立部门，首先查看父部门的baseouguid是否为空，如果不为空，则将本身的修改成父部门的baseouguid，否则设为空
            FrameOu parentOu = ouService.getOuByOuGuid(frameOu.getParentOuguid());
            if (parentOu != null && StringUtil.isNotBlank(parentOu.getBaseOuguid())) {
                frameOu.setBaseOuguid(parentOu.getBaseOuguid());
            }
            else {
                frameOu.setBaseOuguid("");
            }
        }
        FrameOuExtendInfo ouExtendInfo = ouService.getFrameOuExtendInfo(parentOUGuid);
        /*String areacode = "";
        if(ouExtendInfo!=null){
            areacode = ouExtendInfo.get("areacode");
        }*/
        frameOuExtendInfo.set("is_WindowOU", isWindowOu);
        //frameOuExtendInfo.set("areacode", areacode);
        frameOuExtendInfo.set("orgcode",orgcode);
        frameOu.set("isshow", isshow);
        ouService.updateFrameOu(frameOu, frameOuExtendInfo);
        //设置子部门名称路径
        List<FrameOu> ouList= ouservice.listOUByGuid(frameOu.getOuguid(), 3);
        if (ouList!=null) {
            for (FrameOu childou : ouList) {
                childou.setOucodeLevel(ouService.getParentOuName(childou.getOuguid(), true));
                FrameOuExtendInfo chilouExtendinfo = ouservice.getFrameOuExtendInfo(childou.getOuguid());
                ouService.updateFrameOu(childou, chilouExtendinfo);
            }
        }
        insertOperateLog(LOG_OPERATOR_TYPE_MODIFY, LOG_SUBSYSTEM_TYPE_OU, "修改部门:" + frameOu.getOuname(), "Frame_OU");
        addCallbackParam("msg", "修改成功！");
        frameOu = null;
    }

    public FrameOu getFrameOu() {
        if (frameOu == null) {
            frameOu = new FrameOu();
        }
        return frameOu;
    }

    public void setFrameOu(FrameOu frameOu) {
        this.frameOu = frameOu;
    }

    @ExtValue
    public String getParentOuName() {
        return parentOuName;
    }

    public void setParentOuName(String parentOuName) {
        this.parentOuName = parentOuName;
    }

    public Boolean getIsSubWebFlow() {
        if (isSubWebFlow == null) {
            isSubWebFlow = false;
        }
        return isSubWebFlow;
    }

    public void setIsSubWebFlow(Boolean isSubWebFlow) {
        this.isSubWebFlow = isSubWebFlow;
    }

    public String getParentOUGuid() {
        return parentOUGuid;
    }

    public void setParentOUGuid(String parentOUGuid) {
        this.parentOUGuid = parentOUGuid;
    }

    public Boolean getIsIndependence() {
        if (isIndependence == null) {
            isIndependence = false;
        }
        return isIndependence;
    }

    public void setIsIndependence(Boolean isIndependence) {
        this.isIndependence = isIndependence;
    }

    public String getOuguid() {
        return ouguid;
    }

    public void setOuguid(String ouguid) {
        this.ouguid = ouguid;
    }

    public String getBaseOuGuid() {
        return baseOuGuid;
    }

    public void setBaseOuGuid(String baseOuGuid) {
        this.baseOuGuid = baseOuGuid;
    }

    public FrameOuExtendInfo getFrameOuExtendInfo() {
        return frameOuExtendInfo;
    }

    public void setFrameOuExtendInfo(FrameOuExtendInfo frameOuExtendInfo) {
        this.frameOuExtendInfo = frameOuExtendInfo;
    }

    /**
     * 
     * 获取是否是部门窗口下拉框
     * 
     * @return List<SelectItem>
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @SuppressWarnings("unchecked")
    public List<SelectItem> getIsWindowOuModal() {
        if (isWindowOuModal == null) {
            isWindowOuModal = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("单选按钮组", "是否", null, false));
        }
        return this.isWindowOuModal;
    }
    @SuppressWarnings("unchecked")
    public List<SelectItem> getIsShowQz() {
        if (isShowQz == null) {
            isShowQz = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("单选按钮组", "是否", null, false));
        }
        return this.isShowQz;
    }
    public String getIsWindowOu() {
        return isWindowOu;
    }

    public void setIsWindowOu(String isWindowOu) {
        this.isWindowOu = isWindowOu;
    }

    public String getAreacode() {
        return areacode;
    }

    public void setAreacode(String areacode) {
        this.areacode = areacode;
    }

    public String getOrgcode() {
        return orgcode;
    }

    public void setOrgcode(String orgcode) {
        this.orgcode = orgcode;
    }

    public String getIsshow() {
        return isshow;
    }

    public void setIsshow(String isshow) {
        this.isshow = isshow;
    }

}
