package com.epoint.evainstanceck.action;

import com.epoint.basic.auditorga.auditarea.domain.AuditOrgaArea;
import com.epoint.basic.auditorga.auditarea.inter.IAuditOrgaArea;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.bizlogic.mis.CommonService;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.export.ExportModel;
import com.epoint.basic.faces.tree.TreeData;
import com.epoint.basic.faces.tree.TreeFunction9;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dto.base.TreeNode;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.dto.model.TreeModel;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.evainstanceck.api.IEvainstanceCkService;
import com.epoint.evainstanceck.api.entity.EvainstanceCk;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.frame.service.metadata.mis.util.ListGenerator;
import com.epoint.frame.service.organ.ou.api.IOuService;
import com.epoint.frame.service.organ.ou.entity.FrameOu;
import com.epoint.frame.service.organ.ou.entity.FrameOuExtendInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 好差评信息表list页面对应的后台
 *
 * @author 31220
 * @version [版本号, 2023-11-06 11:18:19]
 */
@RestController("evalortreplylistaction")
@Scope("request")
public class EvalortReplyListAction extends BaseController {
    @Autowired
    private IEvainstanceCkService service;

    /**
     * 好差评信息表实体对象
     */
    private EvainstanceCk dataBean;
    private String areacode = "";
    /**
     * 表格控件model
     */
    private DataGridModel<Record> model;

    /**
     * 导出模型
     */
    private ExportModel exportModel;
    @Autowired
    private IOuService ouService;

    @Autowired
    private IAuditOrgaArea auditOrgaAreaService;
    /**
     * 是否整改下拉列表model
     */
    private List<SelectItem> iszgModel = null;
    /**
     * Evalevel下拉列表model
     */
    private List<SelectItem> evalevelModel = null;

    private String RECEIVEUSERNAME;

    private CommonService commservice = new CommonService();

    public void pageLoad() {
    }

    /**
     * 删除选定
     */
    public void deleteSelect() {
        List<String> select = getDataGridData().getSelectKeys();
        for (String sel : select) {
            service.deleteByGuid(sel);
        }
        addCallbackParam("msg", l("成功删除！"));
    }

    public DataGridModel<Record> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<Record>() {

                @Override
                public List<Record> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    String centerguid = ZwfwUserSession.getInstance().getCenterGuid();
                    String strsql = " SELECT e.rowguid as rowguid,e.iszg as iszg,ouname,RECEIVEUSERNAME,ACCEPTUSERGUID,WINDOWNAME,PROJECTNAME,FLOWSN,Evaluatedate,(case when satisfied = '0' THEN 2 else satisfied END) satisfied"
                            + " from audit_online_evaluat e INNER JOIN audit_project p"
                            + " on p.RowGuid = e.ClientIdentifier where clienttype in (10,20)"
                            + " and ACCEPTUSERGUID is NOT NULL and e.status='1' and CENTERGUID='" + centerguid + "'";
                    String strsql1 = " SELECT count(1) as total"
                            + " from audit_online_evaluat e INNER JOIN audit_project p"
                            + " on p.RowGuid = e.ClientIdentifier where clienttype in (10,20)"
                            + " and ACCEPTUSERGUID is NOT NULL and e.status='1' and CENTERGUID='" + centerguid + "'";

//                    if (StringUtil.isNotBlank(WindowList) && !"all".equals(WindowList)) {
//                        strsql += " and WINDOWGUID='"+WindowList+"'";
//                        strsql1 += " and WINDOWGUID='"+WindowList+"'";
//                    }

//                    if (StringUtil.isNotBlank(manyiList) && !"all".equals(manyiList)) {
//                        strsql += " and satisfied='"+manyiList+"'";
//                        strsql1 += " and satisfied='"+manyiList+"'";
//                    }
                    strsql += " ORDER BY e.evaluatedate DESC";

                    List<Record> list = commservice.findList(strsql, first, pageSize, Record.class);
                    int total = new CommonDao().queryInt(strsql1, Integer.class);
                    this.setRowCount(total);
                    return list;
                }

            };
        }
        return model;
    }

    public EvainstanceCk getDataBean() {
        if (dataBean == null) {
            dataBean = new EvainstanceCk();
        }
        return dataBean;
    }

    public void setDataBean(EvainstanceCk dataBean) {
        this.dataBean = dataBean;
    }

    public ExportModel getExportModel() {
        if (exportModel == null) {
            exportModel = new ExportModel("", "");
        }
        return exportModel;
    }

    public List<SelectItem> getIszgModel() {
        if (iszgModel == null) {
            iszgModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "是否", null, true));
        }
        return this.iszgModel;
    }

    public List<SelectItem> getEvalevelModel() {
        if (evalevelModel == null) {
            evalevelModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "好差评满意度层级", null, true));
            evalevelModel.remove(1);// 非常满意
            evalevelModel.remove(1);// 满意

        }
        return this.evalevelModel;
    }


    public String getRECEIVEUSERNAME() {
        return RECEIVEUSERNAME;
    }

    public void setRECEIVEUSERNAME(String rECEIVEUSERNAME) {
        RECEIVEUSERNAME = rECEIVEUSERNAME;
    }

}
