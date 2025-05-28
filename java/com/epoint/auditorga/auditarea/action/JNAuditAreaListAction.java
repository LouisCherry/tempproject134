package com.epoint.auditorga.auditarea.action;

import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.auditorga.auditarea.domain.AuditOrgaArea;
import com.epoint.basic.auditorga.auditarea.inter.IAuditOrgaArea;
import com.epoint.basic.auditorga.auditcenter.domain.AuditOrgaServiceCenter;
import com.epoint.basic.auditorga.auditcenter.inter.IAuditOrgaServiceCenter;
import com.epoint.basic.auditorga.auditwindow.inter.IAuditOrgaWindow;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.export.ExportModel;
import com.epoint.basic.faces.tree.TreeFunction9;
import com.epoint.common.rabbitmq.ProducerMQ;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.log.LogUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.frame.service.organ.ou.api.IOuServiceInternal;
import com.epoint.frame.service.organ.ou.entity.FrameOu;
import com.epoint.frame.service.organ.ou.entity.FrameOuExtendInfo;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 区域配置list页面对应的后台
 *
 * @author Dong
 * @version [版本号, 2016-09-26 17:13:31]
 */
@RestController("jnauditarealistaction")
@Scope("request")
public class JNAuditAreaListAction extends BaseController {
    transient Logger log = LogUtil.getLog(JNAuditAreaListAction.class);

    /**
     *
     */
    private static final long serialVersionUID = 8713148757403132395L;

    @Autowired
    private IAuditOrgaArea iAuditorgaAtra;

    @Autowired
    private IAuditOrgaServiceCenter iAuditCenter;

    /**
     * 区域配置接口的实现类
     */
    @Autowired
    private IAuditOrgaArea areaImpl;
    @Autowired
    private IOuServiceInternal ouService;
    /**
     * 事项窗口基础服务api
     */
    @Autowired
    private IAuditOrgaWindow auditWindowImpl;
    /**
     * 中心配置service
     */
    @Autowired
    private IAuditOrgaServiceCenter auditServiceCenterImpl;
    /**
     * 区域配置实体对象
     */
    private AuditOrgaArea dataBean;

    /**
     * 表格控件model
     */
    private DataGridModel<AuditOrgaArea> model;

    /**
     * 导出模型
     */
    private ExportModel exportModel;

    @Override
    public void pageLoad() {

    }


    /**
     * 保存修改
     */
    public void saveInfo() {
        List<Map<String, Object>> auditareaList = getDataGridData().getFeedbackdata();
        for (Map<String, Object> auditarea : auditareaList) {
            AuditOrgaArea area = iAuditorgaAtra.getAreaByRowguid(auditarea.get("rowguid").toString()).getResult();
            if (StringUtil.isBlank(auditarea.get("ordernum"))) {
                area.setOrdernum(ZwfwConstant.CONSTANT_INT_ZERO);
            } else {
                area.setOrdernum(Integer.valueOf(auditarea.get("ordernum").toString()));
            }
            areaImpl.updateArea(area);
        }
        String msg = "保存成功！";
        addCallbackParam("msg", msg);
    }

    /**
     * 删除选定
     */
    public void deleteSelect() {
        List<String> select = getDataGridData().getSelectKeys();
        for (String sel : select) {
            AuditOrgaArea area = iAuditorgaAtra.getAreaByRowguid(sel).getResult();
            if (area != null) {
                // 查询乡镇辖区
                List<AuditOrgaArea> xzareas = iAuditorgaAtra.selectZXAuditAreaListByAreaCode(area.getXiaqucode())
                        .getResult();
                SqlConditionUtil sql = new SqlConditionUtil();
                sql.eq("belongxiaqu", area.getXiaqucode());
                try {
                    List<AuditOrgaServiceCenter> auditOrgaServiceCenterList = iAuditCenter
                            .getAuditOrgaServiceCenterByCondition(sql.getMap()).getResult();
                    for (AuditOrgaServiceCenter auditOrgaServiceCenter : auditOrgaServiceCenterList) {
                        // 删除中心以及窗口，中心系统参数，中心工作日
                        auditServiceCenterImpl.deleteAuditServiceCenterByGuid(auditOrgaServiceCenter.getRowguid());
                    }
                } catch (Exception e) {
                    log.info("========Exception信息========" + e.getMessage());
                }
                // 删除的时候要清空和部门扩展表的关系
                List<FrameOuExtendInfo> frameOuExtendInfos = ouService.getAllFrameOuExtendInfo();
                // 有乡镇辖区情况
                if (xzareas != null && xzareas.size() > 0) {
                    for (AuditOrgaArea xzarea : xzareas) {
                        for (FrameOuExtendInfo frameOuExtendInfo : frameOuExtendInfos) {
                            if (frameOuExtendInfo.get("areacode") != null
                                    && frameOuExtendInfo.get("areacode").toString().equals(xzarea.getXiaqucode())) {
                                frameOuExtendInfo.set("areacode", "");
                                FrameOu frameOu = ouService.getOuByOuGuid(frameOuExtendInfo.getOuGuid());
                                ouService.updateFrameOu(frameOu, frameOuExtendInfo);
                            }
                        }
//	                    areaImpl.deleteArea(xzarea);
                    }
                }

                for (FrameOuExtendInfo frameOuExtendInfo : frameOuExtendInfos) {
                    if (frameOuExtendInfo.get("areacode") != null
                            && frameOuExtendInfo.get("areacode").toString().equals(area.getXiaqucode())) {
                        if (ZwfwConstant.AREA_TYPE_XZJ.equals(area.getCitylevel())) {
                            frameOuExtendInfo.set("areacode", area.getBaseAreaCode());
                        } else {
                            frameOuExtendInfo.set("areacode", "");
                        }
                        FrameOu frameOu = ouService.getOuByOuGuid(frameOuExtendInfo.getOuGuid());
                        ouService.updateFrameOu(frameOu, frameOuExtendInfo);
                    }
                }
                areaImpl.deleteArea(area);
            }
            // 清除缓存
            ContainerFactory.getContainInfo().getSessionObj().remove(ZwfwConstant.ZWFW_USER_SESSION);
            addCallbackParam("msg", "成功删除！");
        }
    }

    public DataGridModel<AuditOrgaArea> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<AuditOrgaArea>() {
                /**
                 *
                 */
                private static final long serialVersionUID = 5925727181973702200L;

                @Override
                public List<AuditOrgaArea> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    SqlConditionUtil sql = new SqlConditionUtil();
                    SqlConditionUtil sql2 = new SqlConditionUtil();
                    sortField = "ordernum desc,xiaqucode";
                    sortOrder = "asc";
                    // 页面上搜索条件
                    boolean flag = true;
                    if (StringUtil.isNotBlank(dataBean.getXiaquname())) {
                        sql.like("xiaquname", dataBean.getXiaquname());
                        flag = false;
                    }
                    if (StringUtil.isNotBlank(dataBean.getXiaqucode())) {
                        sql.like("xiaqucode", dataBean.getXiaqucode());
                        flag = false;
                    }
                    if (StringUtil.isBlank(dataBean.getXiaquname()) && StringUtil.isBlank(dataBean.getXiaqucode())) {
                        sql.in("citylevel", "1,2");
                    }
                    sql.setOrder(sortField, sortOrder);
                    PageData<AuditOrgaArea> pageData = iAuditorgaAtra
                            .getAuditAreaPageData(sql.getMap(), first, 1000, sortField, sortOrder).getResult();
                    this.setRowCount(pageData.getRowCount());
                    List<AuditOrgaArea> areas = new ArrayList<AuditOrgaArea>();
                    List<AuditOrgaArea> lists = pageData.getList();
                    int num;
                    for (AuditOrgaArea area : lists) {
                        if (area != null) {
                            area.put("id", area.getRowguid());
                            area.put("pid", TreeFunction9.F9ROOT);
                            area.put("checked", false);
                            area.put("expanded", false);
                            area.put("isLeaf", true);
                            if ("032701".equals(area.getXiaqucode())) {
                            }
                            num = iAuditorgaAtra
                                    .selectZXAuditAreaCountByAreaCodeAndCondition(area.getXiaqucode(), sql2.getMap())
                                    .getResult();
                            if (flag && num > 0) {
                                area.put("isLeaf", false);
                            }
                            FrameOu ou = ouService.getOuByOuGuid(area.getOuguid());
                            if (ou != null) {
                                area.put("ouname", ou.getOuname());
                            }
                            areas.add(area);
                        }
                    }
                    return areas;
                }

                @Override
                public List<AuditOrgaArea> fetchChildrenData(JSONObject t) {
                    List<AuditOrgaArea> lists = new ArrayList<AuditOrgaArea>();
                    if (t.getString("xiaqucode") != null && t.getString("xiaqucode").length() == 6) {
                        if (StringUtil.isBlank(t.getString("namecondition"))
                                && StringUtil.isBlank(t.getString("codecondition"))) {
                            lists = iAuditorgaAtra.selectZXAuditAreaListByAreaCode(t.getString("xiaqucode"))
                                    .getResult();

                        } else {
                            SqlConditionUtil sql = new SqlConditionUtil();
                            String sortField = "ordernum";
                            String sortOrder = "desc";
                            // 页面上搜索条件
                            if (StringUtil.isNotBlank(t.getString("namecondition"))) {
                                sql.like("xiaquname", t.getString("namecondition"));
                            }
                            if (StringUtil.isNotBlank(t.getString("codecondition"))) {
                                sql.like("xiaqucode", t.getString("codecondition"));
                            }
                            sql.eq("citylevel", "3");
                            sql.nq("xiaqucode", t.getString("xiaqucode"));
                            sql.leftLike("xiaqucode", t.getString("xiaqucode"));
                            sql.eq("baseareacode", t.getString("xiaqucode"));
                            PageData<AuditOrgaArea> pageData = iAuditorgaAtra
                                    .getAuditAreaPageData(sql.getMap(), 0, 1000, sortField, sortOrder).getResult();
                            lists = pageData.getList();
                        }
                        for (AuditOrgaArea auditOrgaArea : lists) {
                            auditOrgaArea.setCitylevel("3");
                            int num = iAuditorgaAtra
                                    .selectCJAuditAreaCountByAreaCodeAndCondition(auditOrgaArea.get("xiaqucode"), null)
                                    .getResult();
                            if (num > 0) {
                                auditOrgaArea.put("isLeaf", false);
                                auditOrgaArea.put("checked", false);
                                auditOrgaArea.put("expanded", false);
                            }
                        }
                    } else if (t.getString("xiaqucode") != null && t.getString("xiaqucode").length() == 9) {
                        SqlConditionUtil sql = new SqlConditionUtil();
                        String sortField = "ordernum";
                        String sortOrder = "desc";
                        // 页面上搜索条件
                        if (StringUtil.isNotBlank(t.getString("namecondition"))) {
                            sql.like("xiaquname", t.getString("namecondition"));
                        }
                        if (StringUtil.isNotBlank(t.getString("codecondition"))) {
                            sql.like("xiaqucode", t.getString("codecondition"));
                        }
                        sql.eq("citylevel", ZwfwConstant.AREA_TYPE_CJ);
                        sql.nq("xiaqucode", t.getString("xiaqucode"));
                        sql.leftLike("xiaqucode", t.getString("xiaqucode"));
                        sql.eq("baseareacode", t.getString("xiaqucode").substring(0, 6));
                        PageData<AuditOrgaArea> pageData = iAuditorgaAtra
                                .getAuditAreaPageData(sql.getMap(), 0, 0, sortField, sortOrder).getResult();
                        lists = pageData.getList();
                        for (AuditOrgaArea auditOrgaArea : lists) {
                            auditOrgaArea.setCitylevel("4");
                        }
                    }
                    return lists;
                }
            };
        }
        return model;
    }

    public AuditOrgaArea getDataBean() {
        if (dataBean == null) {
            dataBean = new AuditOrgaArea();
        }
        return dataBean;
    }

    public void setDataBean(AuditOrgaArea dataBean) {
        this.dataBean = dataBean;
    }

    public ExportModel getExportModel() {
        if (exportModel == null) {
            exportModel = new ExportModel("", "");
        }
        return exportModel;
    }

    /**
     * 根据guid删除中心配置
     *
     * @param guid
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public void deleteAuditServiceCenterByGuid(String guid) {
        auditServiceCenterImpl.deleteAuditServiceCenterByGuid(guid);
    }

    public void deleteWindowAndRelation(String windowGuid) {
        auditWindowImpl.deleteWindowByWindowGuid(windowGuid);
        try {
            String RabbitMQMsg = "handleCenterWindowTask:" + "delete" + ";" + windowGuid + ";"
                    + ZwfwUserSession.getInstance().getCenterGuid()
                    + "/com.epoint.auditjob.rabbitmqhandle.MQHandleCenterTask";
            ProducerMQ.TopicPublish("auditorgewindowtask", "delete", RabbitMQMsg, true);
        } catch (Exception e) {
            log.info("========Exception信息========" + e.getMessage());
        }
    }

}
