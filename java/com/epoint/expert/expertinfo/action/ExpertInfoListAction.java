package com.epoint.expert.expertinfo.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.export.ExportModel;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.expert.expertcompany.api.IExpertCompanyService;
import com.epoint.expert.expertcompany.api.entity.ExpertCompany;
import com.epoint.expert.expertcompanyavoid.api.IExpertCompanyavoidService;
import com.epoint.expert.expertinfo.api.IExpertInfoService;
import com.epoint.expert.expertinfo.api.entity.ExpertInfo;
import com.epoint.expert.experttask.api.IExpertTaskService;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;

/**
 * 专家表list页面对应的后台
 * 
 * @author cqsong
 * @version [版本号, 2019-08-21 16:36:51]
 */
@RestController("expertinfolistaction")
@Scope("request")
public class ExpertInfoListAction extends BaseController
{
    /**
     * 
     */
    private static final long serialVersionUID = 709744135155456463L;

    @Autowired
    private IExpertInfoService service;
    
    @Autowired
    private IExpertCompanyavoidService expertCompanyavoidService;
    
    @Autowired
    private IExpertTaskService expertTaskService;

    @Autowired
    private ICodeItemsService codeItemService;
    
    @Autowired
    private IExpertCompanyService expertCompanyService;
    
    private IAttachService attachService;

    private static final String CITY_AREA = "市辖区";
    private static final String COUNTY = "县";

    private static final String IS_DEL = "1";

    private static final String LEAVE = "3";
    private static final String ENABLE = "1";
    private static final String DISABLE = "2";

    /**
     * 专家表实体对象
     */
    private ExpertInfo dataBean;

    /**
     * 表格控件model
     */
    private DataGridModel<ExpertInfo> model;

    /**
     * 导出模型
     */
    private ExportModel exportModel;

    /**
    * 评标专业下拉列表model
    */
    private List<SelectItem> pingbzyModel = null;

    public void pageLoad() {
    }

    /**
     * 删除选定
     * 
     */
    public void deleteSelect() {
        List<String> select = getDataGridData().getSelectKeys();
        for (String sel : select) {
            // 逻辑删除 is_del = 1
            ExpertInfo expertInfo = service.find(sel);
            expertInfo.setIs_del(IS_DEL);
            service.update(expertInfo);
            // 级联删除附件信息、回避单位信息、关联事项信息
            if(StringUtil.isNotBlank(dataBean.getCliengguid())) {
                attachService.deleteAttachByGuid(dataBean.getCliengguid());
            }
            expertCompanyavoidService.deleteByExpertGuid(dataBean.getRowguid());
            expertTaskService.deleteByExpertguid(dataBean.getRowguid());
        }
        addCallbackParam("msg", "成功删除！");
    }

    /**
     * 修改请假状态     
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public void changeQjStatus(String expertGuid) {
        ExpertInfo expertinfo = service.find(expertGuid);
        String state = expertinfo.getStatus();
        if (ENABLE.equals(state)) {
            expertinfo.setStatus(LEAVE);
            addCallbackParam("msg", "请假成功！");
        }
        if (LEAVE.equals(state)) {
            expertinfo.setStatus(ENABLE);
            addCallbackParam("msg", "销假成功！");
        }
        service.update(expertinfo);

    }

    /**
     * 修改专家状态
     *  @param expertGuid    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public void changeZjStatus(String expertGuid) {
        ExpertInfo expertinfo = service.find(expertGuid);
        String state = expertinfo.getStatus();
        if (ENABLE.equals(state) || LEAVE.equals(state)) {
            expertinfo.setStatus(DISABLE);
            addCallbackParam("msg", "停用成功！");
        }
        if (DISABLE.equals(state)) {
            expertinfo.setStatus(ENABLE);
            addCallbackParam("msg", "启用成功！");
        }
        service.update(expertinfo);

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
                    Map<String, String> conditionMap = new HashMap<>();
                    if (StringUtil.isNotBlank(dataBean.getExpertno())) {
                        conditionMap.put("expertno", dataBean.getExpertno());
                    }
                    if (StringUtil.isNotBlank(dataBean.getName())) {
                        conditionMap.put("name", dataBean.getName());
                    }
                    if (StringUtil.isNotBlank(dataBean.getPingbzy())) {
                        conditionMap.put("pingbzy", dataBean.getPingbzy());
                    }
                    if (StringUtil.isNotBlank(dataBean.get("shixiangname"))) {
                        conditionMap.put("shixiangname", dataBean.get("shixiangname"));
                    }

                    List<ExpertInfo> list = service.findList(conditionMap, first, pageSize, sortField, sortOrder);
                    if (list != null && !list.isEmpty()) {
                        for (ExpertInfo expertInfo : list) {
                            // 省
                            String province = codeItemService.getItemTextByCodeName("行政区划国标", expertInfo.getProvince());
                            // 市 当为县或者市辖区的时候不需要显示市
                            String city = codeItemService.getItemTextByCodeName("行政区划国标", expertInfo.getCity());
                            city = COUNTY.equals(city) ? "" : city;
                            city = CITY_AREA.equals(city) ? "" : city;
                            // 区
                            String country = codeItemService.getItemTextByCodeName("行政区划国标", expertInfo.getCountry());
                            expertInfo.setProvince(province + city + country);
                            // 渲染所属单位信息
                            String companyguid = expertInfo.getComanyguid();
                            if (!"close".equals(companyguid)) {
                            	 ExpertCompany expertCompany = expertCompanyService.find(companyguid);
                                 expertInfo.setComanyguid(expertCompany.getCompanyname());
                            }
                        }
                    }
                    int count = service.findList(conditionMap).size();
                    this.setRowCount(count);
                    return list;
                }

            };
        }
        return model;
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

    public ExportModel getExportModel() {
        if (exportModel == null) {
            exportModel = new ExportModel("expertno,name,comanyguid,cszy,status", "专家编号,专家姓名,所属单位,从事专业,专家状态");
        }
        return exportModel;
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
