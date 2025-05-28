package com.epoint.auditorga.auditwindow.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.auditmq.sendMessage.api.ISendMQMessage;
import com.epoint.basic.auditorga.audithall.domain.AuditOrgaHall;
import com.epoint.basic.auditorga.audithall.inter.IAuditOrgaHall;
import com.epoint.basic.auditorga.auditwindow.domain.AuditOrgaWindow;
import com.epoint.basic.auditorga.auditwindow.inter.IAuditOrgaWindow;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.export.ExportModel;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.core.EpointFrameDsManager;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.utils.log.LogUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.frame.service.organ.ou.api.IOuService;
import com.epoint.frame.service.organ.ou.entity.FrameOu;

/**
 * 窗口配置表list页面对应的后台
 * 
 * @author WST-PC
 * @version [版本号, 2016-09-26 11:51:11]
 */
@RestController("jnauditwindowlistaction")
@Scope("request")
public class JNAuditWindowListAction extends BaseController
{
    
    transient Logger log = LogUtil.getLog(AuditWindowListAction.class);

    /**
     * 
     */
    private static final long serialVersionUID = 2840238303917537239L;
    @Autowired
    private IAuditOrgaWindow auditWindowImpl;

    /**
     * 部门service
     */
    @Autowired
    private IOuService ouService;
    @Autowired
    private ISendMQMessage sendMQMessageService;

    /**
     * 大厅配置接口的实现类
     */
    @Autowired
    private IAuditOrgaHall auditHallImpl;

    /**
     * 窗口配置表实体对象
     */
    private AuditOrgaWindow dataBean;
    /**
     * 导出模型
     */
    private ExportModel exportModel;
    /**
     * 表格控件model
     */
    private DataGridModel<AuditOrgaWindow> model;
    /**
     * 窗口性质下拉列表model
     */
    private List<SelectItem> windowtypeModel = null;
    /**
     * 大厅类型下拉列表model
     */
    private List<SelectItem> lobbytypeModel = null;

    /**
     * 窗口名称
     */
    private String windowName;
    /**
     * 窗口类型
     */
    private String windowType;
    /**
     * 窗口编号
     */
    private String windowno;
    /**
     * 大厅类型
     */
    private String lobbyType;

    private String windowpeople;

    private String windowtask;
    // 中心guid
    private String centerGuid;

    private String areacode;
    
    @Override
    public void pageLoad() {
        // 首先获取guid参数赋值给centerGuid
        // 为空时，说明是从窗口配置模块进来的，赋值session中的guid
        centerGuid = getRequestParameter("guid");
        areacode = getRequestParameter("areacode");
        if (StringUtil.isBlank(centerGuid)) {
            centerGuid = ZwfwUserSession.getInstance().getCenterGuid();
            areacode = ZwfwUserSession.getInstance().getAreaCode();
        }
        addCallbackParam("centerGuid", centerGuid);
        addCallbackParam("areacode", areacode);
        addCallbackParam("citylevel", ZwfwUserSession.getInstance().getCitylevel());
    }

    /**
     * 删除选定
     * 
     */
    public void deleteSelect() {
        List<String> select = getDataGridData().getSelectKeys();
        for (String windowGuid : select) {
            // 删除窗口，同时删除关联联系
            deleteWindowAndRelation(windowGuid);
            try {
                String RabbitMQMsg = "delete" + ";" + windowGuid + ";" + ZwfwUserSession.getInstance().getCenterGuid();
                sendMQMessageService.sendByExchange("znsb_exchange_handle", RabbitMQMsg,
                        "window." + ZwfwUserSession.getInstance().getAreaCode() + ".delete");
            }
            catch (Exception e) {
                log.info("========Exception信息========" + e.getMessage());
            }
        }
        addCallbackParam("msg", "成功删除！");

    }

    /**
     * 保存修改
     */
    public void saveInfo() {
        List<AuditOrgaWindow> windowList = getDataGridData().getWrappedData();
        String msg = "保存成功！";
        for (AuditOrgaWindow auditwindow : windowList) {
            auditwindow.remove("ouname");
            if (auditwindow.getOrdernum() == null) {// null会跑到最前
                auditwindow.setOrdernum(0);
            }
            auditwindow.keep("windowno", "rowguid", "mac", "ordernum");
            auditWindowImpl.updateAuditWindow(auditwindow, auditwindow.getWindowno(), auditwindow.getMac(),
                    ZwfwUserSession.getInstance().getCenterGuid());
            try {
                String RabbitMQMsg = "modify" + ";" + auditwindow.getRowguid() + ";"
                        + ZwfwUserSession.getInstance().getCenterGuid();
                sendMQMessageService.sendByExchange("znsb_exchange_handle", RabbitMQMsg,
                        "window." + ZwfwUserSession.getInstance().getAreaCode() + ".modify");
            }
            catch (Exception e) {
                log.info("========Exception信息========" + e.getMessage());
            }
        }
        addCallbackParam("msg", msg);
    }

    public DataGridModel<AuditOrgaWindow> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<AuditOrgaWindow>()
            {
                /**
                 * 
                 */
                private static final long serialVersionUID = 9020328332498815274L;

                @Override
                public List<AuditOrgaWindow> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    SqlConditionUtil sql = new SqlConditionUtil();
                    sql.eq("centerguid", centerGuid);
                    if (StringUtil.isNotBlank(windowName)) {
                        sql.like("windowname", windowName);
                    }
                    if (StringUtil.isNotBlank(windowType)) {
                        sql.eq("windowtype", windowType);
                    }
                    if (StringUtil.isNotBlank(windowno)) {
                        sql.like("windowno", windowno);
                    }
                    if (StringUtil.isNotBlank(lobbyType)) {
                        sql.eq("lobbytype", lobbyType);
                    }
                    if (StringUtil.isNotBlank(windowpeople)) {
                        List<String> listWindowguid = auditWindowImpl.getWindowGuidByUserName(windowpeople).getResult();
                        String windowguids = String.join("','", listWindowguid);
                        windowguids = "'" + windowguids + "'";
                        if (StringUtil.isNotBlank(windowguids)) {
                            sql.in("rowguid", windowguids);
                        }
                    }
                    ;
                    if (StringUtil.isNotBlank(windowtask)) {
                        List<String> listWindowguid = auditWindowImpl.getWindowGuidByTaskName(windowtask).getResult();
                        String windowguids = String.join("','", listWindowguid);
                        windowguids = "'" + windowguids + "'";
                        if (StringUtil.isNotBlank(windowguids)) {
                            sql.in("rowguid", windowguids);
                        }
                    }

                    PageData<AuditOrgaWindow> pageData = getWindowPageData(sql.getMap(), first, pageSize, sortField,
                            sortOrder);
                    for (AuditOrgaWindow auditOrgaWindow : pageData.getList()) {
                        auditOrgaWindow.put("windowname", auditOrgaWindow.getWindowname() + "("
                                + (auditOrgaWindow.getWindowno() == null ? "未设置窗口编号" : auditOrgaWindow.getWindowno())
                                + ")");
                        AuditOrgaHall auditOrgaHall = auditHallImpl
                                .getAuditHallByRowguid(auditOrgaWindow.getLobbytype()).getResult();
                        if (auditOrgaHall == null) {
                            auditOrgaWindow.put("lobbytype", "");
                        }
                        else {
                            auditOrgaWindow.put("lobbytype", auditOrgaHall.getHallname());
                        }
                    }
                    this.setRowCount(pageData.getRowCount());
                    return pageData.getList();
                }
            };
        }
        return model;
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
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "窗口性质", null, true));
        }
        return this.windowtypeModel;
    }

    public List<SelectItem> getLobbytypeModel() {
        if (lobbytypeModel == null) {
            lobbytypeModel = new ArrayList<>();
            lobbytypeModel.add(new SelectItem("", "请选择"));
            SqlConditionUtil sql = new SqlConditionUtil();
            sql.eq("centerguid", centerGuid);
            List<AuditOrgaHall> allHalls = auditHallImpl.getAllHall(sql.getMap()).getResult();
            if (allHalls != null && allHalls.size() > 0) {
                for (AuditOrgaHall auditHall : allHalls) {
                    lobbytypeModel.add(new SelectItem(auditHall.getRowguid(), auditHall.getHallname()));
                }
            }
        }
        return this.lobbytypeModel;
    }

    public String getWindowType() {
        return windowType;
    }

    public void setWindowType(String windowType) {
        this.windowType = windowType;
    }

    public String getLobbyType() {
        return lobbyType;
    }

    public void setLobbyType(String lobbyType) {
        this.lobbyType = lobbyType;
    }

    public String getWindowName() {
        return windowName;
    }

    public void setWindowName(String windowName) {
        this.windowName = windowName;
    }

    public String getWindowno() {
        return windowno;
    }

    public void setWindowno(String windowno) {
        this.windowno = windowno;
    }

    public String getWindowpeople() {
        return windowpeople;
    }

    public void setWindowpeople(String windowpeople) {
        this.windowpeople = windowpeople;
    }

    public String getWindowtask() {
        return windowtask;
    }

    public void setWindowtask(String windowtask) {
        this.windowtask = windowtask;
    }

    /**
     * 
     * 获取窗口的分页
     * 
     * @param conditionMap
     * @param first
     * @param pageSize
     * @param sortField
     * @param sortOrder
     * @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public PageData<AuditOrgaWindow> getWindowPageData(Map<String, String> conditionMap, int first, int pageSize,
            String sortField, String sortOrder) {
        PageData<AuditOrgaWindow> pageData = auditWindowImpl
                .getWindowPageData(conditionMap, first, pageSize, sortField, sortOrder).getResult();
        // 查询部门名称
        if (pageData.getRowCount() > 0) {
            for (AuditOrgaWindow auditWindow : pageData.getList()) {
                FrameOu frameOu = ouService.getOuByOuGuid(auditWindow.getOuguid());
                auditWindow.put("ouname", frameOu != null ? frameOu.getOuname() : "");
            }
        }
        return pageData;
    }

    public void deleteWindowAndRelation(String windowGuid) {
        try {
            EpointFrameDsManager.begin(null);
            List<String> taskidlist = auditWindowImpl.getTaskidsByWindow(windowGuid).getResult();
            auditWindowImpl.deleteWindowByWindowGuid(windowGuid);
            EpointFrameDsManager.commit();
            try {
                String RabbitMQMsg = "delete" + ";" + windowGuid + ";" + ZwfwUserSession.getInstance().getCenterGuid()
                        + ";" + taskidlist;
                sendMQMessageService.sendByExchange("znsb_exchange_handle", RabbitMQMsg,
                        "windowtask." + ZwfwUserSession.getInstance().getAreaCode() + ".delete");

            }
            catch (Exception e) {
                log.info("========Exception信息========" + e.getMessage());
            }
        }
        catch (Exception ee) {
            EpointFrameDsManager.rollback();
            log.info("========Exception信息========" + ee.getMessage());
        }
    }
    
    public ExportModel getExportModel() {
        if (exportModel == null) {
            exportModel = new ExportModel("windowname,windowtype,lobbytype,ouname",
                    "窗口名称,窗口性质,所属大厅,所属部门");
        }
        return exportModel;
    }

}
