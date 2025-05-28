package com.epoint.auditorga.auditwindow.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.auditorga.auditcenter.domain.AuditOrgaServiceCenter;
import com.epoint.basic.auditorga.auditcenter.inter.IAuditOrgaServiceCenter;
import com.epoint.basic.auditorga.audithall.domain.AuditOrgaHall;
import com.epoint.basic.auditorga.audithall.inter.IAuditOrgaHall;
import com.epoint.basic.auditorga.auditwindow.domain.AuditOrgaWindow;
import com.epoint.basic.auditorga.auditwindow.inter.IAuditOrgaWindow;
import com.epoint.basic.authentication.UserSession;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.common.service.AuditCommonResult;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;

/**
 * 窗口配置表新增页面对应的后台
 * 
 * @author WST-PC
 * @version [版本号, 2016-09-26 11:51:11]
 */
@RestController("jnauditwindowaddaction")
@RightRelation(AuditWindowListAction.class)
@Scope("request")
public class JNAuditWindowAddAction extends BaseController
{
    /**
     * 
     */
    private static final long serialVersionUID = -1414690950005506422L;
    @Autowired
    private IAuditOrgaWindow auditWindowImpl;
    @Autowired
    private IJNAuditWindow jnauditwindow;
    /**
     * 中心配置service
     */
    @Autowired
    private IAuditOrgaServiceCenter auditServiceCenterImpl;
    /**
     * 大厅配置接口的实现类
     */
    @Autowired
    private IAuditOrgaHall auditHallImpl;
    
    
    /**
     * 窗口配置表实体对象
     */
    private AuditOrgaWindow dataBean = null;
    /**
     * 窗口性质下拉列表model
     */
    private List<SelectItem> windowtypeModel = null;
    /**
     * 大厅类型下拉列表model
     */
    private List<SelectItem> lobbytypeModel = null;
    /**
     * 是否使用排队叫号单选按钮组model
     */
    private List<SelectItem> is_usequeueModel = null;
    
    private String centerGuid;
 private String indicating = null;
    
    private String childindicating = null;
    public String getIndicating() {
        return indicating;
    }

    public void setIndicating(String indicating) {
        this.indicating = indicating;
    }

    public String getChildindicating() {
        return childindicating;
    }

    public void setChildindicating(String childindicating) {
        this.childindicating = childindicating;
    }

    
    @Override
    public void pageLoad() {
        dataBean = new AuditOrgaWindow();
        centerGuid = ZwfwUserSession.getInstance().getCenterGuid();
        //system.out.println("centerGuid:"+centerGuid);
        AuditOrgaServiceCenter center = auditServiceCenterImpl.findAuditServiceCenterByGuid(centerGuid).getResult();
        //system.out.println("center:"+center);
        if(ZwfwConstant.CENTER_TYPE_XZJ.equals(center.getServicecentelevel())){
            dataBean.setWindowtype(ZwfwConstant.WINDOW_TYPE_XZZXCK);
            addCallbackParam("windowtype","乡镇中心窗口");
        }
        if(centerGuid!=null && StringUtil.isNotBlank(centerGuid)){
        	addCallbackParam("centername", auditServiceCenterImpl.findAuditServiceCenterByGuid(centerGuid).getResult().getCentername());
        }else{
        	addCallbackParam("centername", "");
        }
    }

    /**
     * 保存并关闭
     * 
     */
    public void add() {
        String msg = "";
      
        msg=addAuditWindow(dataBean);
        jnauditwindow.Updateaudiywindow(dataBean.getRowguid(), indicating, childindicating);
        addCallbackParam("msg", msg);
    }

    /**
     * 保存并新建
     * 
     */
    public void addNew() {
        add();
        dataBean = new AuditOrgaWindow();
    }

    public AuditOrgaWindow getDataBean() {
        if (dataBean == null) {
            dataBean = new AuditOrgaWindow();
        }
        return dataBean;
    }

    public void setDataBean(AuditOrgaWindow dataBean) {
        this.dataBean = dataBean;
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> getWindowtypeModel() {
        if (windowtypeModel == null) {
            windowtypeModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "窗口性质", null, false));
        }
        return this.windowtypeModel;
    }

    public List<SelectItem> getLobbytypeModel() {
        if (lobbytypeModel == null) {
        	lobbytypeModel = new ArrayList<>();
        	Map<String,String> map = new HashMap<String,String>(16);
        	map.put("centerguid=", ZwfwUserSession.getInstance().getCenterGuid());
        	List<AuditOrgaHall> allHalls = auditHallImpl.getAllHall(map).getResult();
        	if(allHalls!=null && allHalls.size()>0){
        		for(AuditOrgaHall auditHall : allHalls){
        			lobbytypeModel.add(new SelectItem(auditHall.getRowguid(),auditHall.getHallname()));
        		}
        	}
        }
        return this.lobbytypeModel;
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> getIs_usequeueModel() {
        if (is_usequeueModel == null) {
            is_usequeueModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("单选按钮组", "是否", null, false));
        }
        return this.is_usequeueModel;
    }
    
    /**
     * 
     * 判断窗口编号是否存在
     * 
     * @param auditWindow
     * @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public String addAuditWindow(AuditOrgaWindow auditWindow) {
        String msg = "";
        auditWindow.setRowguid(UUID.randomUUID().toString());
        auditWindow.setPinyin(StringUtil.chineseToPinyin(auditWindow.getWindowname()).toUpperCase());
        auditWindow.setOperatedate(new Date());
        auditWindow.setOperateusername(UserSession.getInstance().getDisplayName());
        auditWindow.setCenterguid(ZwfwUserSession.getInstance().getCenterGuid());
        AuditCommonResult<String> addResult = auditWindowImpl.insertWindow(auditWindow,ZwfwUserSession.getInstance().getCenterGuid());
        if (!addResult.isSystemCode()) {
            msg = addResult.getSystemDescription();
        } else if (!addResult.isBusinessCode()) {
            msg = addResult.getBusinessDescription();
        } else {
            msg = "保存成功！";
        }
        return msg;
    }
}
