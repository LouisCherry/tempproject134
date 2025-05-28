package com.epoint.ggyjstj;

import com.epoint.auditsp.auditspintegrated.action.HandleIntegratedListViewAction;
import com.epoint.basic.EpointKeyNames9;
import com.epoint.basic.auditsp.auditspbusiness.domain.AuditSpBusiness;
import com.epoint.basic.auditsp.auditspbusiness.inter.IAuditSpBusiness;
import com.epoint.basic.auditsp.auditspinstance.domain.AuditSpInstance;
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
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.ggyjstj.api.IgetSpDataByAreacode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController("yjslistviewaction")
@Scope("request")
public class YjsListViewAction extends BaseController {
    private static final long serialVersionUID = 8713148757403132395L;
    private AuditSpBusiness dataBean;
    private DataGridModel<AuditSpInstance> model;
    private String applyername;
    private String itemname;
    private String status;
    @Autowired
    private IAuditSpInstance spInstanceService;
    @Autowired
    private IAuditSpBusiness iAuditSpBusiness;
    @Autowired
    private IgetSpDataByAreacode igetSpDataByAreacode;
    private List<SelectItem> belongToAreaModel;

    private Date beginTime;
    private Date endTime;
    public YjsListViewAction() {
    }

    public void pageLoad() {
    }

    public void checkDetail(String biGuid) {
        String registerUrl = "";
        AuditSpInstance auditSpInstance = spInstanceService.getDetailByBIGuid(biGuid).getResult();
        AuditSpBusiness auditSpBusiness =iAuditSpBusiness.getAuditSpBusinessByRowguid(auditSpInstance.getBusinessguid()).getResult();
        String businessType = auditSpBusiness.getBusinesstype();
        String filepath = auditSpBusiness.getHandleURL();
        if (StringUtil.isNotBlank(filepath)) {
            if ("1".equals(businessType)) {
                registerUrl = "epointzwfw/auditsp/auditsphandle/handlebilistviewdetail?guid=" + biGuid;
            }
            else if ("2".equals(businessType)) {
                registerUrl = filepath + "/auditspintegrateddetail" + "?guid=" + biGuid;
            }
            else {
                registerUrl = "epointzwfw/auditsp/auditsphandle/handlebilistviewdetail?guid=" + biGuid;
            }
        }
        else if ("1".equals(businessType)) {
            registerUrl = "epointzwfw/auditsp/auditsphandle/handlebilistviewdetail?guid=" + biGuid;
        }
        else if ("2".equals(businessType)) {
            registerUrl = "epointzwfw/auditsp/auditspintegrated/auditspintegrateddetail?guid=" + biGuid;
        }
        else {
            registerUrl = "epointzwfw/auditsp/auditspintegrated/auditspintegrateddetail?guid=" + biGuid;
        }

        this.addCallbackParam("msg", registerUrl);
    }

    public void deleteSelect() {
        List<String> guids = this.getDataGridData().getSelectKeys();
        Iterator var2 = guids.iterator();

        while(var2.hasNext()) {
            String guid = (String)var2.next();
            this.spInstanceService.deleteAuditSpInstanceByGuid(guid);
        }

        this.addCallbackParam("msg", "删除成功！");
    }

    public DataGridModel<AuditSpInstance> getDataGridData() {
        if (this.model == null) {
            this.model = new DataGridModel<AuditSpInstance>() {
                private static final long serialVersionUID = 2196016255341333981L;

                public List<AuditSpInstance> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    try {
                        PageData<AuditSpInstance> pageData = igetSpDataByAreacode.getyjsDataByAreacodeAndType(getRequestParameter("areacode"), getRequestParameter("type"), applyername, itemname,status,beginTime,endTime,first,pageSize);
                        if(pageData!=null){
                            this.setRowCount(pageData.getRowCount());
                            return pageData.getList();
                        }else{
                            this.setRowCount(0);
                            return new ArrayList<>();
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    return new ArrayList<>();
                }
            };
        }

        return this.model;
    }

    public AuditSpBusiness getDataBean() {
        if (this.dataBean == null) {
            this.dataBean = new AuditSpBusiness();
        }

        return this.dataBean;
    }

    public void setDataBean(AuditSpBusiness dataBean) {
        this.dataBean = dataBean;
    }

    public String getApplyername() {
        return this.applyername;
    }

    public void setApplyername(String applyername) {
        this.applyername = applyername;
    }

    public String getItemname() {
        return this.itemname;
    }

    public void setItemname(String itemname) {
        this.itemname = itemname;
    }
    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    public Date getBeginTime() {
        return this.beginTime;
    }

    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }

    public Date getEndTime() {
        return this.endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
    public List<SelectItem> getBelongToAreaModel() {
        if (belongToAreaModel == null) {
            belongToAreaModel = DataUtil.convertMap2ComboBox((List<Map<String, String>>) CodeModalFactory
                    .factory(EpointKeyNames9.CHECK_SELECT_GROUP, "一件事办件状态", null, false));
        }
        return this.belongToAreaModel;
    }
}
