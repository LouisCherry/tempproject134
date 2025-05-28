package com.epoint.jnhospital.hospitalinfo.action;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.jnhospital.hospitalinfo.api.IHospitalinfoService;
import com.epoint.jnhospital.hospitalinfo.api.entity.Hospitalinfo;

/**
 * 定点医院名单修改页面对应的后台
 * 
 * @author JFei
 * @version [版本号, 2019-09-05 11:21:46]
 */
@RightRelation(HospitalinfoListAction.class)
@RestController("hospitalinfoeditaction")
@Scope("request")
public class HospitalinfoEditAction extends BaseController
{

    @Autowired
    private IHospitalinfoService service;

    /**
     * 定点医院名单实体对象
     */
    private Hospitalinfo dataBean = null;

    /**
    * 医院等级下拉列表model
    */
    private List<SelectItem> hospital_gradeModel = null;
    /**
     * 医院级别下拉列表model
     */
    private List<SelectItem> hospital_levelModel = null;
    /**
     * 医院类型下拉列表model
     */
    private List<SelectItem> hospital_typeModel = null;

    public void pageLoad() {
        String guid = getRequestParameter("guid");
        dataBean = service.find(guid);
        if (dataBean == null) {
            dataBean = new Hospitalinfo();
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

    public Hospitalinfo getDataBean() {
        return dataBean;
    }

    public void setDataBean(Hospitalinfo dataBean) {
        this.dataBean = dataBean;
    }

    public List<SelectItem> getHospital_gradeModel() {
        if (hospital_gradeModel == null) {
            hospital_gradeModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "医院等级", null, false));
        }
        return this.hospital_gradeModel;
    }

    public List<SelectItem> getHospital_levelModel() {
        if (hospital_levelModel == null) {
            hospital_levelModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "医院级别", null, false));
        }
        return this.hospital_levelModel;
    }

    public List<SelectItem> getHospital_typeModel() {
        if (hospital_typeModel == null) {
            hospital_typeModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "医院类型", null, false));
        }
        return this.hospital_typeModel;
    }

}
