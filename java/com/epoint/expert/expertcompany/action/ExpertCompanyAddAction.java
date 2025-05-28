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
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;

/**
 * 从业单位表新增页面对应的后台
 * 
 * @author cqsong
 * @version [版本号, 2019-08-21 16:09:09]
 */
@RightRelation(ExpertCompanyListAction.class)
@RestController("expertcompanyaddaction")
@Scope("request")
public class ExpertCompanyAddAction extends BaseController
{
    /**
     * 
     */
    private static final long serialVersionUID = 328507474978670967L;
    
    /**
     * 默认状态启用
     */
    private static final String MRSTATUS = "1";
    
    /**
     * 不删除状态
     */
    private static final int IS_NOTDEL = 0;

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
    /**
     * 从业单位表实体对象
     */
    private ExpertCompany dataBean = null;

    /**
    * 单位类型下拉列表model
    */
    private List<SelectItem> typeModel = null;

    public void pageLoad() {
        dataBean = new ExpertCompany();
        dataBean.setStatus(MRSTATUS);
        if (StringUtil.isBlank(getViewData("cliengguid"))) {
            String cliengguid = UUID.randomUUID().toString();
            addViewData("cliengguid", cliengguid);
        }
    }

    /**
     * 保存并关闭
     * 
     */
    public void add() {
        dataBean.setRowguid(UUID.randomUUID().toString());
        dataBean.setOperatedate(new Date());
        dataBean.setOperateusername(userSession.getDisplayName());
        dataBean.setCliengguid(getViewData("cliengguid"));
        // 默认不删除
        dataBean.setIs_del(IS_NOTDEL);
        service.insert(dataBean);
        addCallbackParam("msg", "保存成功！");
        dataBean = null;
    }

    /**
     * 保存并新建
     * 
     */
    public void addNew() {
        add();
        dataBean = new ExpertCompany();
        String cliengguid = UUID.randomUUID().toString();
        addViewData("cliengguid", cliengguid);
        dataBean.setCliengguid(cliengguid);
    }

    public ExpertCompany getDataBean() {
        if (dataBean == null) {
            dataBean = new ExpertCompany();
        }
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
            filter = "110000";
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
