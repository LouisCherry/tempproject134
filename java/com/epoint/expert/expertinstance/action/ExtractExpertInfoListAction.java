package com.epoint.expert.expertinstance.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.expert.expertcompany.api.IExpertCompanyService;
import com.epoint.expert.expertinfo.api.IExpertInfoService;
import com.epoint.expert.expertinfo.api.entity.ExpertInfo;
import com.epoint.expert.expertinstance.api.IExpertInstanceService;
import com.epoint.expert.expertinstance.api.entity.ExpertInstance;
import com.epoint.expert.expertiresult.api.IExpertIResultService;
import com.epoint.expert.expertiresult.api.entity.ExpertIResult;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.util.SqlUtils;

/**
 * 专家表list页面对应的后台
 * 
 * @author cqsong
 * @version [版本号, 2019-08-21 16:36:51]
 */
@RestController("extractexpertinfolistaction")
@Scope("request")
public class ExtractExpertInfoListAction extends BaseController
{
    /**
     * 
     */
    private static final long serialVersionUID = 709744135155456463L;

    @Autowired
    private IExpertInfoService service;
    @Autowired
    private IExpertInstanceService instanceService;
    @Autowired
    private IExpertIResultService resultService;
    @Autowired
    private IExpertCompanyService companyService;
    @Autowired
    private ICodeItemsService codeService;

    /**
     * 专家表实体对象
     */
    private ExpertInfo dataBean;

    /**
     * 表格控件model
     */
    private DataGridModel<ExpertInfo> model;



    /**
    * 评标专业下拉列表model
    */
    private List<SelectItem> pingbzyModel = null;
    private String expertGuids;
    
    private String instanceGuid;
    public void pageLoad() {
        expertGuids=getRequestParameter("expertguids");
        instanceGuid=getRequestParameter("instanceguid");
    }

    /**
     * 删除选定
     * 
     */
    public void deleteSelect() {
        List<String> select = getDataGridData().getSelectKeys();
        for (String sel : select) {
            service.deleteByGuid(sel);
        }
        addCallbackParam("msg", "成功删除！");
    }

    public DataGridModel<ExpertInfo> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<ExpertInfo>()
            {

                /**
                 * 
                 */
                private static final long serialVersionUID = -1314415790309696001L;

                @Override
                public List<ExpertInfo> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    String[] expertArray = expertGuids.split(",");
                    String expertGuid = String.join("','", expertArray);
                    SqlUtils sqlUtils = new SqlUtils();
                    sqlUtils.in("rowguid", "'" + expertGuid + "'");
                    PageData<ExpertInfo> pageData = service.getPageByCondition(sqlUtils.getMap(), first, pageSize,
                            sortField, sortOrder);
                    List<ExpertInfo> list=pageData.getList();
                    List<ExpertInfo> newList=new ArrayList<ExpertInfo>();
                    for (ExpertInfo expertInfo : list) {
                        String area=codeService.getItemTextByCodeName("行政区划国标",expertInfo.getProvince())+" "
                                +codeService.getItemTextByCodeName("行政区划国标",expertInfo.getCity())+" "
                                +codeService.getItemTextByCodeName("行政区划国标",expertInfo.getCountry());
                        
                        expertInfo.set("companyname", companyService.find(expertInfo.getComanyguid()).getCompanyname());
                        expertInfo.set("area", area);
                        newList.add(expertInfo);
                    }
                    this.setRowCount(pageData.getRowCount());
                    return newList;
                }

            };
        }
        return model;
    }

    
    public void confirm() {
        String[] expertArray = expertGuids.split(",");
        ExpertInstance expertInstance = instanceService.find(instanceGuid);
        expertInstance.setExtracttime(new Date());
        instanceService.update(expertInstance);
        ExpertInfo expertInfo;
        for (int i = 0; i < expertArray.length; i++) {
            expertInfo = service.find(expertArray[i]);
            ExpertIResult result = new ExpertIResult();
            result.setRowguid(UUID.randomUUID().toString());
            result.setInstanceguid(instanceGuid);
            result.setExpertguid(expertInfo.getRowguid());
            result.setExpertno(expertInfo.getExpertno());
            result.setExpertname(expertInfo.getName());
            result.setPingbzy(expertInfo.getPingbzy());
            result.setProvince(expertInfo.getProvince());
            result.setCountry(expertInfo.getCountry());
            result.setCity(expertInfo.getCity());
            result.setContactphone(expertInfo.getContactphone());
            result.setEmail(expertInfo.getEmail());
            result.setIs_attend("1");
            result.setIs_auto("1");
            result.set("bidtime", expertInstance.getBidtime());
            resultService.insert(result);
        }
        addCallbackParam("msg", "抽取成功");
    }
    public ExpertInfo getDataBean() {
        if (dataBean == null) {
            dataBean = new ExpertInfo();
        }
        return dataBean;
    }

    public void setDataBean(ExpertInfo dataBean) {
        this.dataBean = dataBean;
    }


    @SuppressWarnings("unchecked")
    public List<SelectItem> getPingbzyModel() {
        if (pingbzyModel == null) {
            pingbzyModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "专家库_评标专业", null, true));
        }
        return this.pingbzyModel;
    }

}
