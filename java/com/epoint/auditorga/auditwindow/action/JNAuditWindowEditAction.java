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

import com.epoint.basic.auditorga.audithall.domain.AuditOrgaHall;
import com.epoint.basic.auditorga.audithall.inter.IAuditOrgaHall;
import com.epoint.basic.auditorga.auditwindow.domain.AuditOrgaWindow;
import com.epoint.basic.auditorga.auditwindow.domain.AuditOrgaWindowTask;
import com.epoint.basic.auditorga.auditwindow.domain.AuditOrgaWindowUser;
import com.epoint.basic.auditorga.auditwindow.inter.IAuditOrgaWindow;
import com.epoint.basic.authentication.UserSession;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.common.service.AuditCommonResult;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.frame.service.organ.ou.api.IOuService;
import com.epoint.frame.service.organ.ou.entity.FrameOu;

/**
 * 窗口配置表修改页面对应的后台
 * 
 * @author WST-PC
 * @version [版本号, 2016-09-26 11:51:11]
 */
@RestController("jnauditwindoweditaction")
@RightRelation(AuditWindowListAction.class)
@Scope("request")
public class JNAuditWindowEditAction extends BaseController
{

    /**
     * 
     */
    private static final long serialVersionUID = -3183446252974532475L;
    @Autowired
    private IAuditOrgaWindow auditWindowImpl;
    /**
     * 部门service
     */
    @Autowired
    private IOuService ouService;
    
    /**
     * 大厅配置接口的实现类
     */
    @Autowired
    private IAuditOrgaHall auditHallImpl;
    
    @Autowired
    private IAuditOrgaWindow iauditWindow;
    
    @Autowired
    private IAuditOrgaHall iauditHall;
    
    @Autowired
    private IJNAuditWindow jnauditwindow;
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
    /**
     * 修改前的窗口编号
     */
    private String preWindowNo = null;

    /**
     * 修改前的MAC
     */
    private String preMAC = null;
    
    private String indicating = null;
    
    private String childindicating = null;
    
    private String hardwaremac;
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
    
    

    public String getHardwaremac() {
        return hardwaremac;
    }

    public void setHardwaremac(String hardwaremac) {
        this.hardwaremac = hardwaremac;
    }

    @Override
    public void pageLoad() {
        String guid = getRequestParameter("guid");
        dataBean = iauditWindow.getWindowByWindowGuid(guid).getResult();
        if (dataBean == null) {
            dataBean = new AuditOrgaWindow();
        }
        else {
            indicating = jnauditwindow.getauditwindow(guid).get("indicating");
            childindicating = jnauditwindow.getauditwindow(guid).get("childindicating");
            FrameOu frameOu = ouService.getOuByOuGuid(dataBean.getOuguid());
            addCallbackParam("ouname", frameOu == null ? "" : frameOu.getOuname());
        }
        preWindowNo = dataBean.getWindowno();
        preMAC = StringUtil.getNotNullString(dataBean.getMac()) ;
        AuditOrgaHall hall= iauditHall.getAuditHallByRowguid(dataBean.getLobbytype()).getResult();
        if(hall==null)
        {
            dataBean.setLobbytype("");
        }
        String sql = "select hardwaremac from audit_orga_window where rowguid=?1";
        hardwaremac = CommonDao.getInstance().queryString(sql, guid);
    }

    /**
     * 保存修改
     * 
     */
    public void save() {
        String msg = "";
        msg=saveAuditWindow(dataBean,preWindowNo,preMAC,ZwfwUserSession.getInstance().getCenterGuid());
        addCallbackParam("msg", msg);
    }
    
    public void copy() {
        String msg = "";
        AuditOrgaWindow copyAuditWindow = copyWindow(dataBean.getRowguid());
        msg=copyAndSaveAuditWindow(copyAuditWindow , ZwfwUserSession.getInstance().getCenterGuid());
        addCallbackParam("msg", msg);
    }

    //复制窗口
    public AuditOrgaWindow copyWindow(String rowguid) {
        //复制基本信息
        AuditOrgaWindow copyAuditWindow = new AuditOrgaWindow();
        copyAuditWindow.setRowguid(UUID.randomUUID().toString());
        copyAuditWindow.setPinyin(StringUtil.chineseToPinyin("(复制)" + dataBean.getWindowname()).toUpperCase());
        copyAuditWindow.setOperatedate(new Date());
        copyAuditWindow.setOperateusername(UserSession.getInstance().getDisplayName());
        copyAuditWindow.setCenterguid(dataBean.getCenterguid());
        copyAuditWindow.setOuguid(dataBean.getOuguid());
        copyAuditWindow.setWindowtype(dataBean.getWindowtype());
        copyAuditWindow.setCentername(dataBean.getCentername());
        copyAuditWindow.setTel(dataBean.getTel());
        copyAuditWindow.setLobbytype(dataBean.getLobbytype());
        copyAuditWindow.setAddress(dataBean.getAddress());
        copyAuditWindow.setNote(dataBean.getNote());
        copyAuditWindow.setOrdernum(dataBean.getOrdernum());
        copyAuditWindow.setWindowname(dataBean.getWindowname());
        copyAuditWindow.setWindowno(dataBean.getWindowno());
        //复制业务信息
        List<AuditOrgaWindowTask> oldtasklist = new ArrayList<AuditOrgaWindowTask>();
        oldtasklist = auditWindowImpl.getTaskByWindow(rowguid).getResult();
        String newWindowguid = copyAuditWindow.getRowguid();
        for (AuditOrgaWindowTask task : oldtasklist) {
            AuditOrgaWindowTask auditWindowTask = new AuditOrgaWindowTask();
            auditWindowTask.setRowguid(UUID.randomUUID().toString());
            auditWindowTask.setWindowguid(newWindowguid);
            auditWindowTask.setTaskguid(task.getTaskguid());
            auditWindowTask.setOperatedate(new Date());
            auditWindowTask.setOrdernum(task.getOrdernum());
            auditWindowTask.setTaskid(task.getTaskid());
            auditWindowTask.setEnabled(task.getEnabled());// 插入的数据默认为有效
            auditWindowImpl.insertWindowTask(auditWindowTask);
        }

        //复制人员信息
        List<AuditOrgaWindowUser> userList = new ArrayList<AuditOrgaWindowUser>();
        userList = auditWindowImpl.getUserByWindow(rowguid).getResult();
        for (AuditOrgaWindowUser user : userList) {
            AuditOrgaWindowUser auditWindowUser = new AuditOrgaWindowUser();
            auditWindowUser.setRowguid(UUID.randomUUID().toString());
            auditWindowUser.setWindowguid(newWindowguid);
            auditWindowUser.setUserguid(user.getUserguid());
            auditWindowUser.setUsername(user.getUsername());
            auditWindowUser.setOperatedate(new Date());
            auditWindowUser.setOperateusername(UserSession.getInstance().getDisplayName());
            auditWindowImpl.insertWindowUser(auditWindowUser);
        }

        return copyAuditWindow;
    }


    
    public AuditOrgaWindow getDataBean() {
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
     * 更新窗口
     * 
     * @param auditWindow
     *            窗口对象
     * @param preWindowNo
     *            更新前的窗口编号
     * @return String
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public String saveAuditWindow(AuditOrgaWindow auditWindow, String preWindowNo,String preMAC, String centerGuid) {
        String msg = "";
        auditWindow.setPinyin(StringUtil.chineseToPinyin(auditWindow.getWindowname()).toUpperCase());
        auditWindow.setOperatedate(new Date());
        auditWindow.setOperateusername(UserSession.getInstance().getDisplayName());
        auditWindow.setCenterguid(ZwfwUserSession.getInstance().getCenterGuid());
        auditWindow.set("hardwaremac", hardwaremac);
        jnauditwindow.Updateaudiywindow(auditWindow.getRowguid(), indicating, childindicating);
        AuditCommonResult<String> editResult =auditWindowImpl.updateAuditWindow(auditWindow,preWindowNo,preMAC,centerGuid);
        if (!editResult.isSystemCode()) {
            msg = editResult.getSystemDescription();
        } else if (!editResult.isBusinessCode()) {
            msg = editResult.getBusinessDescription();
        } else {
            msg = "修改成功！";
        }
        return msg;
    }
    
    public String copyAndSaveAuditWindow(AuditOrgaWindow auditWindow , String centerGuid) {
        String msg = "";
        auditWindow.setPinyin(StringUtil.chineseToPinyin(auditWindow.getWindowname()).toUpperCase());
        auditWindow.setOperatedate(new Date());
        auditWindow.setOperateusername(UserSession.getInstance().getDisplayName());
        auditWindow.setCenterguid(ZwfwUserSession.getInstance().getCenterGuid());
        AuditCommonResult<String> editResult =auditWindowImpl.insertWindow(auditWindow,centerGuid);
        if (!editResult.isSystemCode()) {
            msg = editResult.getSystemDescription();
        } else if (!editResult.isBusinessCode()) {
            msg = editResult.getBusinessDescription();
        } else {
            msg = "复制窗口并修改成功！";
        }
        return msg;
    }
}
