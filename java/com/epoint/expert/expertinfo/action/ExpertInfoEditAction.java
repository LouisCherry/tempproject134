package com.epoint.expert.expertinfo.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import com.epoint.basic.faces.fileupload.FileUploadModel9;
import com.epoint.basic.faces.tree.DefaultFileUploadHandlerImpl9;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.expert.expertcompany.api.IExpertCompanyService;
import com.epoint.expert.expertcompany.api.entity.ExpertCompany;
import com.epoint.expert.expertcompanyavoid.api.IExpertCompanyavoidService;
import com.epoint.expert.expertcompanyavoid.api.entity.ExpertCompanyavoid;
import com.epoint.expert.expertinfo.api.IExpertInfoService;
import com.epoint.expert.expertinfo.api.entity.ExpertInfo;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.frame.service.metadata.mis.util.ListGenerator;

/**
 * 专家表修改页面对应的后台
 * 
 * @author cqsong
 * @version [版本号, 2019-08-21 16:36:51]
 */
@RightRelation(ExpertInfoListAction.class)
@RestController("expertinfoeditaction")
@Scope("request")
public class ExpertInfoEditAction extends BaseController
{

    /**
     * 
     */
    private static final long serialVersionUID = 4878292913967787495L;
    
    @Autowired
    private IExpertInfoService service;

    @Autowired
    private IAttachService attachService;

    @Autowired
    private ICodeItemsService codeItemService;

    @Autowired
    private IExpertCompanyavoidService expertCompanyavoidService;
    
    @Autowired
    private IExpertCompanyService expertCompanyService;
    
    
    /**
     * 回避单位表格控件model
     */
    private DataGridModel<ExpertCompanyavoid> hbmodel;
    
    private FileUploadModel9 fileUploadModel;

    /**
     * 专家表实体对象
     */
    private ExpertInfo dataBean = null;

    /**
    * 学历下拉列表model
    */
    private List<SelectItem> xueliModel = null;
    /**
     * 文化程度下拉列表model
     */
    private List<SelectItem> wenhuacdModel = null;
    /**
     * 技术职称下拉列表model
     */
    private List<SelectItem> zhicModel = null;
    /**
     * 评标专业下拉列表model
     */
    private List<SelectItem> pingbzyModel = null;
    /**
     * 专家状态下拉列表model
     */
    private List<SelectItem> statusModel = null;

    /**
     * 省Model
     */
    private List<SelectItem> provModel = null;

    /**
     * 市Model
     */
    private List<SelectItem> cityModel = null;

    /**
     * 区Model
     */
    private List<SelectItem> countryModel = null;

    /**
     * 表格控件model
     */
    private DataGridModel<FrameAttachInfo> model;

    public void pageLoad() {
        String guid = getRequestParameter("guid");
        dataBean = service.find(guid);
        if (dataBean == null) {
            return;
        }
        else {
            addCallbackParam("cityValue", dataBean.getCity());
            addCallbackParam("cityText", codeItemService.getItemTextByCodeName("行政区划国标", dataBean.getCity()));
            // 渲染所属单位信息
            String companyguid = dataBean.getComanyguid();
            ExpertCompany expertCompany = expertCompanyService.find(companyguid);
            addCallbackParam("companyname", expertCompany.getCompanyname());
            addCallbackParam("companyguid", companyguid);
        }
    }

    /**
     * 保存修改
     * 
     */
    public void save() {
        dataBean.setOperatedate(new Date());
        service.update(dataBean);
        addCallbackParam("msg", "修改成功！");
    }
    
    /**
     * 删除回避单位选定
     * 
     */
    public void deleteHBSelect() {
        List<String> select = getHbDataGridData().getSelectKeys();
        for (String sel : select) {
            expertCompanyavoidService.deleteByGuid(sel);
        }
        addCallbackParam("msg", "成功删除！");
    }

    /**
     * 回避单位表格 
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public DataGridModel<ExpertCompanyavoid> getHbDataGridData() {
        // 获得表格对象
        if (hbmodel == null) {
            hbmodel = new DataGridModel<ExpertCompanyavoid>()
            {
                private static final long serialVersionUID = -7024735175738605919L;

                @Override
                public List<ExpertCompanyavoid> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    List<Object> conditionList = new ArrayList<Object>();
                    String conditionSql = ListGenerator.getSearchSql(getRequestContext().getComponents(),
                            conditionList);
                    String expertguid = getRequestParameter("guid");
                    List<ExpertCompanyavoid> list = expertCompanyavoidService.findListByexpertguid(
                            ListGenerator.generateSql("Expert_CompanyAvoid", conditionSql, sortField, sortOrder),
                            expertguid, first, pageSize, conditionList.toArray());
                    int count = expertCompanyavoidService.findListByexpertguid(
                            ListGenerator.generateSql("Expert_CompanyAvoid", conditionSql, sortField, sortOrder),
                            expertguid, conditionList.toArray()).size();
                    this.setRowCount(count);
                    return list;
                }
            };
        }
        return hbmodel;
    }
    
    
    /**
     * 附件列表 
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public DataGridModel<FrameAttachInfo> getUploadDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<FrameAttachInfo>()
            {
                /**
                 * 
                 */
                private static final long serialVersionUID = 5834872773407058979L;

                @Override
                public List<FrameAttachInfo> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    String expertguid = getRequestParameter("guid");
                    ExpertInfo expertInfo = service.find(expertguid);
                    String cliengguid = expertInfo.getCliengguid();
                    List<FrameAttachInfo> list = new ArrayList<>();
                    int count = 0;
                    if (StringUtil.isNotBlank(cliengguid)) {
                        if (attachService.getAttachInfoListByGuid(cliengguid) != null
                                && !attachService.getAttachInfoListByGuid(cliengguid).isEmpty()) {
                            list = attachService.getAttachInfoListByGuid(cliengguid);
                            count = attachService.getAttachInfoListByGuid(cliengguid).size();
                        }
                    }
                    this.setRowCount(count);
                    return list;
                }

            };
        }
        return model;
    }

    /**
     * 附件删除
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public void deleteSelect() {
        List<String> select = getUploadDataGridData().getSelectKeys();
        for (String sel : select) {
            attachService.deleteAttachByAttachGuid(sel);
        }
        addCallbackParam("msg", "成功删除！");
    }

    /**
     * 附件上传 
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public FileUploadModel9 getFileUploadModel() {
        if (fileUploadModel == null) {
                fileUploadModel = new FileUploadModel9(new DefaultFileUploadHandlerImpl9(dataBean.getCliengguid(), null, null, null,
                        userSession.getUserGuid(), userSession.getDisplayName()));
        }
        return fileUploadModel;
    }

    public ExpertInfo getDataBean() {
        return dataBean;
    }

    public void setDataBean(ExpertInfo dataBean) {
        this.dataBean = dataBean;
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> getXueliModel() {
        if (xueliModel == null) {
            xueliModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "学历", null, false));
        }
        return this.xueliModel;
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> getWenhuacdModel() {
        if (wenhuacdModel == null) {
            wenhuacdModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "文化程度", null, false));
        }
        return this.wenhuacdModel;
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> getZhicModel() {
        if (zhicModel == null) {
            zhicModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "专家库_技术职称", null, false));
        }
        return this.zhicModel;
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> getPingbzyModel() {
        if (pingbzyModel == null) {
            pingbzyModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "专家库_评标专业", null, false));
        }
        return this.pingbzyModel;
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> getStatusModel() {
        if (statusModel == null) {
            statusModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "专家库_专家状态", null, false));
        }
        return this.statusModel;
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> getProvModel() {
        if (provModel == null) {
            List<SelectItem> silist = new ArrayList<SelectItem>();
            provModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "行政区划国标", null, false));
            for (SelectItem selectitem : provModel) {
                if (selectitem.getValue().toString().endsWith("0000")) {
                    silist.add(selectitem);
                }
            }
            provModel = silist;
        }
        return this.provModel;
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> getCityModel() {
        String filter = this.getRequestParameter("filter");
        if (StringUtil.isBlank(filter)) {
            filter = "320000";
        }
        if (cityModel == null) {
            List<SelectItem> silist = new ArrayList<SelectItem>();
            cityModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "行政区划国标", null, false));
            if (StringUtil.isNotBlank(filter)) {
                for (SelectItem selectitem : cityModel) {
                    if (!selectitem.getValue().toString().equals(filter)
                            && selectitem.getValue().toString().startsWith(filter.substring(0, 2))
                            && selectitem.getValue().toString().endsWith("00")) {
                        silist.add(selectitem);
                    }
                }
                cityModel = silist;
            }

        }
        return cityModel;
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> getCountryModel() {
        String filter = this.getRequestParameter("filter");
        if (countryModel == null) {
            List<SelectItem> silist = new ArrayList<SelectItem>();
            countryModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "行政区划国标", null, false));
            if (StringUtil.isNotBlank(filter)) {
                for (SelectItem selectitem : countryModel) {
                    if (!selectitem.getValue().toString().equals(filter)
                            && selectitem.getValue().toString().startsWith(filter.substring(0, 4))) {
                        silist.add(selectitem);
                    }
                }
                countryModel = silist;
            }

        }
        return countryModel;
    }

}
