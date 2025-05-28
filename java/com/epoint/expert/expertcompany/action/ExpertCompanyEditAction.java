package com.epoint.expert.expertcompany.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import com.epoint.basic.faces.fileupload.FileUploadModel9;
import com.epoint.basic.faces.tree.DefaultFileUploadHandlerImpl9;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.expert.expertcompany.api.IExpertCompanyService;
import com.epoint.expert.expertcompany.api.entity.ExpertCompany;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;

/**
 * 从业单位表修改页面对应的后台
 * 
 * @author cqsong
 * @version [版本号, 2019-08-21 16:09:09]
 */
@RightRelation(ExpertCompanyListAction.class)
@RestController("expertcompanyeditaction")
@Scope("request")
public class ExpertCompanyEditAction extends BaseController
{

    /**
     * 
     */
    private static final long serialVersionUID = 7551436421781758665L;
    
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
    
    private FileUploadModel9 fileUploadModel;
    
    @Autowired
    private IExpertCompanyService service;
    
    @Autowired
    private ICodeItemsService codeItemService;

    /**
     * 从业单位表实体对象
     */
    private ExpertCompany dataBean = null;

    /**
    * 单位类型下拉列表model
    */
    private List<SelectItem> typeModel = null;

    public void pageLoad() {
        String guid = getRequestParameter("guid");
        dataBean = service.find(guid);
        addCallbackParam("cityValue", dataBean.getCity());
        addCallbackParam("cityText", codeItemService.getItemTextByCodeName("行政区划国标", dataBean.getCity()));
        if (dataBean == null) {
            dataBean = new ExpertCompany();
        }
        String cliengguid = dataBean.getCliengguid();
        if(cliengguid == null) {
            cliengguid = UUID.randomUUID().toString();
        }
        addViewData("cliengguid", cliengguid);
    }

    /**
     * 保存修改
     * 
     */
    public void save() {
        dataBean.setOperatedate(new Date());
        dataBean.setCliengguid(getViewData("cliengguid"));
        service.update(dataBean);
        addCallbackParam("msg", "修改成功！");
    }

    public ExpertCompany getDataBean() {
        return dataBean;
    }

    public void setDataBean(ExpertCompany dataBean) {
        this.dataBean = dataBean;
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> getTypeModel() {
        if (typeModel == null) {
            typeModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "专家库_单位类型", null, false));
        }
        return this.typeModel;
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

    public FileUploadModel9 getFileUploadModel() {
        if (fileUploadModel == null) {
            fileUploadModel = new FileUploadModel9(new DefaultFileUploadHandlerImpl9(getViewData("cliengguid"), null,
                    null, null, userSession.getUserGuid(), userSession.getDisplayName()));
        }
        return fileUploadModel;
    }

}
