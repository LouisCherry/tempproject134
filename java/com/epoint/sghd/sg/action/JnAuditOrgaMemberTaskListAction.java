package com.epoint.sghd.sg.action;

import com.epoint.basic.auditorga.auditdepartment.domain.AuditOrgaDepartment;
import com.epoint.basic.auditorga.auditdepartment.inter.IAuditOrgaDepartment;
import com.epoint.basic.auditorga.auditmember.domain.AuditOrgaMember;
import com.epoint.basic.auditorga.auditmember.inter.IAuditOrgaMember;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.export.ExportModel;
import com.epoint.basic.faces.tree.LazyTreeModal9;
import com.epoint.basic.faces.tree.SimpleFetchHandler9;
import com.epoint.basic.faces.tree.TreeData;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.dto.model.TreeModel;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 中心人事人员表list页面对应的后台
 *
 * @version [版本号, 2018年11月3日]
 * @作者 shibin
 */
@RestController("jnauditorgamembertasklistaction")
@Scope("request")
@SuppressWarnings({"serial", "unchecked", "rawtypes"})
public class JnAuditOrgaMemberTaskListAction extends BaseController {
    /**
     *
     */
    private static final long serialVersionUID = 2120483808629593950L;

    @Autowired
    private IAuditOrgaMember iAuditOrgaMember;

    @Autowired
    private IAuditOrgaDepartment iAuditOrgaDepartment;


    /**
     * 部门树model
     */
    private LazyTreeModal9 departmentTreeModel = null;

    /**
     * 中心人事人员表实体对象
     */
    private AuditOrgaMember dataBean;

    /**
     * 表格控件model
     */
    private DataGridModel<AuditOrgaMember> model;

    /**
     * 导出模型
     */
    private ExportModel exportModel;

    /**
     * 树控件
     */
    private LazyTreeModal9 treeModel;
    /**
     * 人员分类
     */
    private List<SelectItem> userTypeModel = null;
    private List<SelectItem> kaoqinModel = null;
    private String departmentGuid;
    private String username;
    private String gonghao;
    private String zhiw;
    private String kaoqin;
    private String userType;


    @Override
    public void pageLoad() {
        addCallbackParam("centerguid", ZwfwUserSession.getInstance().getCenterGuid());
        this.addCallbackParam("departmentGuid", this.getRequestParameter("departmentGuid"));
        departmentGuid = this.getRequestParameter("departmentGuid");
    }

    /**
     * 获取系统参数左侧树数据
     *
     * @return String
     */
    public TreeModel getTreeModal() {
        String centerGuid = ZwfwUserSession.getInstance().getCenterGuid();
        // 获得树对象
        if (treeModel == null) {
            treeModel = getTreeModel(centerGuid);
        }
        return treeModel;
    }

    /**
     * 删除选定
     */
    public void deleteSelect() {
        List<String> select = getDataGridData().getSelectKeys();
        for (String sel : select) {
            iAuditOrgaMember.deleteMemberByRowguid(sel);
        }
        addCallbackParam("msg", "成功删除！");
    }

    public DataGridModel<AuditOrgaMember> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<AuditOrgaMember>() {

                @Override
                public List<AuditOrgaMember> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    SqlConditionUtil sql = new SqlConditionUtil();
                    if (StringUtil.isNotBlank(departmentGuid)) {
                        sql.eq("groupguid", getGroupguidByDepartmentGuid(departmentGuid));
                    }
                    if (StringUtil.isNotBlank(username)) {
                        sql.like("username", username);
                    }
                    if (StringUtil.isNotBlank(gonghao)) {
                        sql.eq("gonghao", gonghao);
                    }
                    if (StringUtil.isNotBlank(userType)) {
                        sql.eq("type", userType);
                    }
                    if (StringUtil.isNotBlank(kaoqin)) {
                        sql.eq("is_kaoqin", kaoqin);
                    }
                    // sql.eq("centerguid", ZwfwUserSession.getInstance().getCenterGuid());
                    if (StringUtil.isNotBlank(departmentGuid)) {
                        sql.eq("groupguid", getGroupguidByDepartmentGuid(departmentGuid));
                    } else {
                        sql.eq("belongxiaqucode", ZwfwUserSession.getInstance().getAreaCode());
                    }
                    PageData<AuditOrgaMember> pageData = getPageData(sql.getMap(), first, pageSize,
                            sortField, sortOrder);
                    for (AuditOrgaMember member : pageData.getList()) {
                        member.put("centerguid", ZwfwUserSession.getInstance().getCenterGuid());
                        member.put("areacode", ZwfwUserSession.getInstance().getAreaCode());

                    }
                    this.setRowCount(pageData.getRowCount());
                    return pageData.getList();
                }
            };
        }
        return model;
    }

    /**
     * 检查部门是否存在
     *
     * @return
     */
    public void checkDept() {
        Map<String, Object> map = getRequestParameterMap();
        String param = map.get("cmdParams").toString();
        String departmentGuid = param.substring(2, param.length() - 2);
        AuditOrgaDepartment dept = iAuditOrgaDepartment.getDepartmentByRowguid(departmentGuid).getResult();
        if (dept == null) {
            this.addCallbackParam("msg", "不存在");
        } else {
            this.addCallbackParam("msg", "存在");
        }
    }

    /**
     * 排序修改
     */
    public void saveEdit() {
        List<AuditOrgaMember> memberList = getDataGridData().getWrappedData();
        String msg = "修改成功！";
        for (AuditOrgaMember member : memberList) {
            if (member.getOrdernum() == null) {// null会跑到最前
                member.setOrdernum(0);
            }
            String update = iAuditOrgaMember.updateMember(member).getResult();
            if (update != null && !"".equals(update)) {
                // 如果出现问题跳出循环
                msg = update;
                continue;
            }
        }
        addCallbackParam("msg", msg);
    }

    /**
     * 更改事项开启状态
     *
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public void changeAuditTaskStatus() {
        String memberGuid = getDataGridData().getSelectKeys().get(0);
        changeStatus(memberGuid);
    }

    public List<SelectItem> getKaoqinModel() {
        if (kaoqinModel == null) {
            kaoqinModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("单选按钮组", "是否", null, false));
            kaoqinModel.add(new SelectItem("", "全部"));
        }
        return this.kaoqinModel;
    }

    public List<SelectItem> getUserTypeModel() {
        if (userTypeModel == null) {
            userTypeModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "人员分类", null, false));
        }
        return userTypeModel;
    }

    public AuditOrgaMember getDataBean() {
        if (dataBean == null) {
            dataBean = new AuditOrgaMember();
        }
        return dataBean;
    }

    public void setDataBean(AuditOrgaMember dataBean) {
        this.dataBean = dataBean;
    }

    public ExportModel getExportModel() {
        if (exportModel == null) {
            exportModel = new ExportModel("", "");
        }
        return exportModel;
    }

    public String getDepartmentGuid() {
        return departmentGuid;
    }

    public void setDepartmentGuid(String departmentGuid) {
        this.departmentGuid = departmentGuid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getGonghao() {
        return gonghao;
    }

    public void setGonghao(String gonghao) {
        this.gonghao = gonghao;
    }

    public String getZhiw() {
        return zhiw;
    }

    public void setZhiw(String zhiw) {
        this.zhiw = zhiw;
    }

    public String getKaoqin() {
        return kaoqin;
    }

    public void setKaoqin(String kaoqin) {
        this.kaoqin = kaoqin;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    /**
     * 获取系统参数左侧树数据
     *
     * @return String
     */
    public LazyTreeModal9 getTreeModel(String centerGuid) {
        if (departmentTreeModel == null) {
            departmentTreeModel = new LazyTreeModal9(new SimpleFetchHandler9() {

                @Override
                public <T> List<T> search(String condition) {

                    return null;
                }

                @Override
                public <T> List<T> fetchData(int arg0, TreeData treeData) {
                    SqlConditionUtil sql = new SqlConditionUtil();
//                    sql.eq("centerguid", centerGuid);
//                    List list = iAuditOrgaDepartment.getAuditDepartmentList(sql.getMap()).getResult();
                    sql.eq("belongxiaqucode", ZwfwUserSession.getInstance().getAreaCode());
                    PageData<AuditOrgaDepartment> pageData = iAuditOrgaDepartment
                            .getDepartmentPageData(sql.getMap(), 0, 50, "ordernum", "DESC").getResult();
                    List list = pageData.getList();
                    return list;
                }

                @Override
                public int fetchChildCount(TreeData arg0) {
                    return 0;
                }

                @Override
                public List<TreeData> changeDBListToTreeDataList(List<?> objlist) {
                    List<TreeData> treeDataList = new ArrayList<TreeData>();
                    if (objlist != null && objlist.size() > 0) {
                        for (Object ob : objlist) {
                            if (ob instanceof AuditOrgaDepartment) {
                                AuditOrgaDepartment department = (AuditOrgaDepartment) ob;
                                TreeData treeData = new TreeData();
                                treeData.setObjectcode(department.getRowguid());
                                treeData.setTitle(department.getOuname());
                                treeDataList.add(treeData);
                            }
                        }
                    }
                    return treeDataList;
                }
            });
            departmentTreeModel.setRootName("所有部门");
            departmentTreeModel.setRootSelect(false);
        }
        return departmentTreeModel;
    }

    public String getGroupguidByDepartmentGuid(String departmentguid) {
        SqlConditionUtil sql = new SqlConditionUtil();
        sql.eq("rowguid", departmentguid);
        return iAuditOrgaDepartment.getAuditDepartment(sql.getMap()).getResult().getGroupguid();
    }

    public PageData<AuditOrgaMember> getPageData(Map<String, String> conditionMap, Integer first, Integer pageSize,
                                                 String sortField, String sortOrder) {
        PageData<AuditOrgaMember> pageData = iAuditOrgaMember.getMemberPageData(conditionMap, first,
                pageSize, sortField, sortOrder).getResult();

        List<AuditOrgaMember> newList = new ArrayList<AuditOrgaMember>();
        for (AuditOrgaMember mem : pageData.getList()) {
            SqlConditionUtil sql = new SqlConditionUtil();
            sql.eq("groupguid", mem.getGroupguid());
            AuditOrgaDepartment dept = iAuditOrgaDepartment.getAuditDepartment(sql.getMap()).getResult();
            if (dept != null) {
                mem.setOuname(dept.getOuname());
                newList.add(mem);
            }
        }
        Integer count = iAuditOrgaMember.getAuditMemberCount(conditionMap).getResult();
        PageData<AuditOrgaMember> page = new PageData<AuditOrgaMember>();
        page.setList(newList);
        page.setRowCount(count);
        return page;
    }

    /**
     * 更改考勤状态
     *
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public void changeStatus(String memberGuid) {
        AuditOrgaMember resultMember = iAuditOrgaMember.getMemberByRowguid(memberGuid).getResult();
        if (StringUtil.isNotBlank(resultMember.getIs_kaoqin())) {
            if (Integer.parseInt(resultMember.getIs_kaoqin()) == ZwfwConstant.CONSTANT_INT_ZERO) {
                resultMember.setIs_kaoqin(ZwfwConstant.CONSTANT_INT_ONE + "");
            } else {
                resultMember.setIs_kaoqin(ZwfwConstant.CONSTANT_INT_ZERO + "");
            }
        } else {
            resultMember.setIs_kaoqin(ZwfwConstant.CONSTANT_INT_ONE + "");
        }
        iAuditOrgaMember.updateMember(resultMember);
    }

}
