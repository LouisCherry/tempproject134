package com.epoint.auditorga.auditoumanager.action;

import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.basic.soa.SOAService;
import com.epoint.core.dto.ExtValue;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.utils.convert.ConvertUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.frame.service.organ.ou.api.IOuServiceInternal;
import com.epoint.frame.service.organ.ou.entity.FrameOu;
import com.epoint.frame.service.organ.ou.entity.FrameOuExtendInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 部门新增
 *
 * @author wry
 * @version [版本号, 2016年3月10日]
 */
@RestController("jnzwfwframeouaddaction")
@Scope("request")
public class JnZwfwFrameOuAddAction extends BaseController {

    private static final long serialVersionUID = -1117979826281739997L;
    /**
     * 当前部门对象
     */
    private FrameOu frameOu;
    /**
     * 部门对象拓展信息
     */
    private FrameOuExtendInfo frameOuExtendInfo;

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

    /**
     * 是否是窗口部门
     */
    private String isWindowOu;
    /**
     * 是否网厅展示
     */
    private String isshow;

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


    /**
     * 组织机构代码/统一社会信用代码
     */
    private String orgcode;

    @Override
    public void pageLoad() {
        if (SOAService.isEnableSOA()) {
            addCallbackParam("soa", SOAService.message);
            return;
        }
        // 传入上级部门guid
        if (StringUtil.isBlank(parentOUGuid)) {
            parentOUGuid = getRequestParameter("parentOUGuid");
            String isSub = getRequestParameter("isSub");
            if ("1".equals(isSub) && StringUtil.isBlank(parentOUGuid)
                    && StringUtil.isNotBlank(userSession.getBaseOUGuid())) {// 如果左侧树没有选择任何节点，要默认当前部门
                FrameOu topOu = ouService.getTopOu(userSession.getOuGuid(), userSession.getBaseOUGuid());
                parentOUGuid = topOu.getOuguid();
            }
        }
        if (StringUtil.isNotBlank(parentOUGuid)) {
            parentOuName = ouService.getParentOuName(parentOUGuid, false);
        } else {
            parentOuName = "根";
        }
    }

    /**
     * 新增部门
     */
    public void add() {

        frameOu.setOuguid(UUID.randomUUID().toString());
        if (frameOu.getOuname().length() > 50) {
            addCallbackParam("msg", "部门名称不允许超过50位！");
            return;
        }
        FrameOu frameOuTemp = ouService.getOuByOuField("oucode", frameOu.getOucode());
        if (frameOuTemp != null && !frameOuTemp.isEmpty()) {
            addCallbackParam("msg", "部门代码重复！");
            return;
        }

        if (StringUtil.isBlank(frameOu.getOushortName())) {
            frameOu.setOushortName(frameOu.getOuname());
        }
        List<FrameOuExtendInfo> extendinfos = ouService.getAllFrameOuExtendInfo();
        for (FrameOuExtendInfo extendinfo : extendinfos) {
            if (StringUtil.isNotBlank(extendinfo.get("orgcode"))) {
                if (StringUtil.isNotBlank(orgcode) && orgcode.equals(extendinfo.get("orgcode"))) {
                    addCallbackParam("msg", "组织机构代码/统一社会信用代码重复！");
                    return;
                }
            }

        }

        // 自动生成下一个可用的oucode
        if (parentOUGuid != null && !"".equals(parentOUGuid)) {
            frameOu.setParentOuguid(parentOUGuid);
            // 设置部门名称全路径
            frameOu.setOucodeLevel(ouService.getParentOuName(parentOUGuid, true));
        }

        frameOu.setIsSubWebFlow(ConvertUtil.convertBooleanToInteger(getIsSubWebFlow()));
        // 如果是独立部门，那么不需要这些配置
        frameOuExtendInfo.setIsIndependence(ConvertUtil.convertBooleanToInteger(isIndependence));
        // 如果是独立部门，那么baseouGUid=ouguid
        if (isIndependence) {
            frameOu.setBaseOuguid(frameOu.getOuguid());
        } else {
            // 如果不是独立部门，那么看是否有父部门是独立部门，如果父部门有独立单位，则将baseouguid设置成父部门的baseouguid,否则设置成null
            FrameOu parentOu = ouService.getOuByOuGuid(frameOu.getParentOuguid());
            if (parentOu != null) {
                frameOu.setBaseOuguid(parentOu.getBaseOuguid());
            } else {
                frameOu.setBaseOuguid("");
            }
        }
        // 排序号默认为0
        if (frameOu.getOrderNumber() == null) {
            frameOu.setOrderNumber(0);
        }
        //根据父部门guid获取辖区编号
        FrameOuExtendInfo ouExtendInfo = ouService.getFrameOuExtendInfo(parentOUGuid);
        String areacode = "";
        if (ouExtendInfo != null) {
            areacode = ouExtendInfo.get("areacode");
        }
        //把是否是窗口部门和辖区编号存入部门
        frameOuExtendInfo.put("is_WindowOU", isWindowOu);
        frameOuExtendInfo.put("areacode", areacode);
        frameOuExtendInfo.put("orgcode", orgcode);
        frameOu.set("isshow", isshow);
        ouService.addFrameOu(frameOu, frameOuExtendInfo);
        addCallbackParam("msg", "新增成功！");
        frameOu = null;
    }

    public void addNew() {
        add();
        frameOu = new FrameOu();
        addCallbackParam("parentOUGuid", parentOUGuid);
        addCallbackParam("parentOuName", parentOuName);
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

    public FrameOuExtendInfo getFrameOuExtendInfo() {
        if (frameOuExtendInfo == null) {
            frameOuExtendInfo = new FrameOuExtendInfo();
        }
        return frameOuExtendInfo;
    }

    public void setFrameOuExtendInfo(FrameOuExtendInfo frameOuExtendInfo) {
        this.frameOuExtendInfo = frameOuExtendInfo;
    }

    /**
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
