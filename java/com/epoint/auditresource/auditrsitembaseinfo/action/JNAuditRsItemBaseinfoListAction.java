package com.epoint.auditresource.auditrsitembaseinfo.action;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.auditresource.auditrsitembaseinfo.api.IJNauditRsItemBaseinfoservice;
import com.epoint.basic.EpointKeyNames9;
import com.epoint.basic.auditresource.auditrsitembaseinfo.domain.AuditRsItemBaseinfo;
import com.epoint.basic.auditresource.auditrsitembaseinfo.inter.IAuditRsItemBaseinfo;
import com.epoint.basic.auditsp.auditspinstance.inter.IAuditSpInstance;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.frame.service.message.api.IMessagesCenterService;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.frame.service.organ.role.api.IRoleService;
import com.epoint.frame.service.organ.user.api.IUserService;
import com.epoint.frame.service.organ.user.entity.FrameUser;

/**
 * 项目基本信息表list页面对应的后台
 * 
 * @author Sonl
 * @version [版本号, 2017-04-05 10:25:43]
 */
@RestController("jnauditrsitembaseinfolistaction")
@Scope("request")
public class JNAuditRsItemBaseinfoListAction extends BaseController
{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * 项目基本信息表实体对象
     */
    private AuditRsItemBaseinfo dataBean;

    /**
     * 表格控件model
     */
    private DataGridModel<AuditRsItemBaseinfo> model;

    /**
     * 项目库接口
     */
    @Autowired
    private IAuditRsItemBaseinfo auditRsItemBaseinfoImpl;

    @Autowired
    private IAuditSpInstance spInstanceService;

    @Autowired
    private IUserService userService;

    @Autowired
    private IRoleService roleService;

    @Autowired
    private IMessagesCenterService messagesCenterService;
    
    @Autowired
    private IJNauditRsItemBaseinfoservice jnauditRsItemBaseinfoservice;

    /**
    * 项目类型代码项
    */
    private List<SelectItem> itemTypebModel;

    private String itemtype;

    @Override
    public void pageLoad() {
    }

    /**
     * 删除选定
     * 
     */
    public void deleteSelect() {
        List<String> select = getDataGridData().getSelectKeys();
        String msg = "";
        String roleGuid = roleService.listRole("窗口负责人", "").get(0).getRoleGuid();
        for (String sel : select) {
            // 是否有审批通过的评审，有则不删除，没有则级联删除
            if (spInstanceService.ifHasDoneReview(sel).getResult()) {
                msg += getDataGridData().getWrappedData().stream()
                        .filter(auditRsItemBaseinfo -> auditRsItemBaseinfo.getRowguid().equals(sel)).findFirst().get()
                        .getItemname() + ";";
            }
            else {
                List<Record> reviews = spInstanceService.deleteInstancAndEtc(sel).getResult();
                if (reviews != null && reviews.size() > 0) {
                    for (Record review : reviews) {
                        List<FrameUser> listUser = userService.listUserByOuGuid(review.get("ouguid"), roleGuid, "", "",
                                false, true, true, 3);
                        //删除已发送的评审待办
                        for (FrameUser frameUser : listUser) {
                            messagesCenterService.deleteMessageByIdentifier(review.get("rowguid"),
                                    frameUser.getUserGuid());
                        }
                    }
                }
                auditRsItemBaseinfoImpl.deleteAuditRsItemBaseinfo(sel);
            }
        }
        if (StringUtil.isBlank(msg)) {
            msg = "成功删除！";
        }
        else {
            msg += " 已评审通过，无法删除！";
        }
        addCallbackParam("msg", msg);
    }

    public DataGridModel<AuditRsItemBaseinfo> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<AuditRsItemBaseinfo>()
            {

                @Override
                public List<AuditRsItemBaseinfo> fetchData(int first, int pageSize, String sortField,
                        String sortOrder) {
                    SqlConditionUtil sql = new SqlConditionUtil();
                    String xiaqucode = ZwfwUserSession.getInstance().getAreaCode();
                    // 搜索条件
                    if (StringUtil.isNotBlank(dataBean.getItemcode())) {
                        sql.like("ITEMCODE", dataBean.getItemcode());
                    }
                    if (StringUtil.isNotBlank(dataBean.getItemname())) {
                        sql.like("ITEMNAME", dataBean.getItemname());
                    }
                    if (StringUtil.isNotBlank(itemtype)) {
                        sql.like("Itemtype", itemtype);
                    }
                    PageData<AuditRsItemBaseinfo> pageData = auditRsItemBaseinfoImpl.getAuditRsItemBaseinfoByPage(
                            AuditRsItemBaseinfo.class, sql.getMap(), first, pageSize, sortField, sortOrder).getResult();
                    this.setRowCount(pageData.getRowCount());
                    return pageData.getList();

                }

            };
        }
        return model;
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> getItemTypeModel() {
        if (itemTypebModel == null) {
            itemTypebModel = DataUtil.convertMap2ComboBox((List<Map<String, String>>) CodeModalFactory
                    .factory(EpointKeyNames9.CHECK_SELECT_GROUP, "项目类型", null, true));
        }
        return this.itemTypebModel;
    }

    public AuditRsItemBaseinfo getDataBean() {
        if (dataBean == null) {
            dataBean = new AuditRsItemBaseinfo();
        }
        return dataBean;
    }

    public void setDataBean(AuditRsItemBaseinfo dataBean) {
        this.dataBean = dataBean;
    }

    public String getItemtype() {
        return itemtype;
    }

    public void setItemtype(String itemtype) {
        this.itemtype = itemtype;
    }

}
