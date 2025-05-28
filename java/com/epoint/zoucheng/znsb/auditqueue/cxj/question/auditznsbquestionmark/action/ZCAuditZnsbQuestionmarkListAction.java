package com.epoint.zoucheng.znsb.auditqueue.cxj.question.auditznsbquestionmark.action;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.auditqueue.auditznsbequipment.domain.AuditZnsbEquipment;
import com.epoint.basic.auditqueue.auditznsbequipment.inter.IAuditZnsbEquipment;
import com.epoint.basic.controller.BaseController;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.znsb.util.QueueCommonUtil;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.httpclient.HttpUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.zoucheng.znsb.basic.auditqueue.cxj.question.auditznsbquestionnaireresult.domain.AuditZnsbQuestionnaireresult;
import com.epoint.zoucheng.znsb.basic.auditqueue.cxj.question.auditznsbquestionnaireresult.inter.IZCAuditZnsbQuestionnaireresultService;

@RestController("zcauditznsbquestionmarklistaction")
@Scope("request")
public class ZCAuditZnsbQuestionmarkListAction extends BaseController
{
    private static final long serialVersionUID = 1L;

    @Autowired
    private IZCAuditZnsbQuestionnaireresultService service;

    private AuditZnsbQuestionnaireresult dataBean;

    private DataGridModel<AuditZnsbQuestionnaireresult> model;

    @Autowired
    private IAuditZnsbEquipment auditZnsbEquipment;

    private String startdate;
    private String enddate;

    public void pageLoad() {
    }

    public DataGridModel<AuditZnsbQuestionnaireresult> getDataGridData() {
        if (model == null) {
            model = new DataGridModel<AuditZnsbQuestionnaireresult>()
            {

                private static final long serialVersionUID = 1L;

                @Override
                public List<AuditZnsbQuestionnaireresult> fetchData(int first, int pageSize, String sortField,
                        String sortOrder) {
                    String guid = getRequestParameter("guid");
                    AuditZnsbQuestionnaireresult questionnaireresult = service.find(guid);
                    SqlConditionUtil sql = new SqlConditionUtil();
                    sql.eq("centerguid", ZwfwUserSession.getInstance().getCenterGuid());
                    sql.eq("questionnaireguid", questionnaireresult.getQuestionnaireguid());
                    if (StringUtil.isNotBlank(startdate)) {
                        sql.between("OperateDate", EpointDateUtil.convertString2Date(startdate),
                                EpointDateUtil.convertString2Date(enddate));
                    }
                    PageData<AuditZnsbQuestionnaireresult> pageData = service
                            .getAuditZnsbQuestionnaireResult(sql.getMap(), first, pageSize, sortField, sortOrder)
                            .getResult();
                    for (AuditZnsbQuestionnaireresult auditZnsbQuestionnaireresult : pageData.getList()) {
                        String macaddress = auditZnsbQuestionnaireresult.getMacaddress();
                        if (StringUtil.isNotBlank(macaddress)) {
                            AuditZnsbEquipment equipment = auditZnsbEquipment.getDetailbyMacaddress(macaddress)
                                    .getResult();
                            auditZnsbQuestionnaireresult.setMacaddress(equipment.getMachinename());
                        }
                    }
                    this.setRowCount(pageData.getRowCount());
                    return pageData.getList();
                }
            };
        }
        return model;
    }

    public AuditZnsbQuestionnaireresult getDataBean() {
        if (dataBean == null) {
            dataBean = new AuditZnsbQuestionnaireresult();
        }
        return dataBean;
    }

    public void setDataBean(AuditZnsbQuestionnaireresult dataBean) {
        this.dataBean = dataBean;
    }

    public String getStartdate() {
        return startdate;
    }

    public void setStartdate(String startdate) {
        this.startdate = startdate;
    }

    public String getEnddate() {
        return enddate;
    }

    public void setEnddate(String enddate) {
        this.enddate = enddate;
    }

    /**
     * 导出
     */
    public void exportdata(String guid) {
        String url = QueueCommonUtil.getUrlPath(request.getRequestURL().toString())
                + "/rest/zcquestionnairerest/exportRoutinginspect";
        JSONObject sendParams = new JSONObject();
        JSONObject params = new JSONObject();
        params.put("centerguid", ZwfwUserSession.getInstance().getCenterGuid());
        params.put("guid", guid);
        sendParams.put("params", params);
        sendParams.put("tokens", "");
        // 调用接口返回值
        String returnvalue = HttpUtil.doPostJson(url, sendParams.toJSONString());
        if (StringUtil.isNotBlank(returnvalue) && !"[]".equals(returnvalue)) {
            JSONObject msgjson = JSON.parseObject(returnvalue);
            addCallbackParam("returnvalue", msgjson.getJSONObject("custom").getObject("list", List.class));
            addCallbackParam("msg", "导出成功");
        }
        else {
            addCallbackParam("msg", "无问卷信息");
        }
    }
}
