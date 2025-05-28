package com.epoint.jnhospital.hospitalinfo.action;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
 * 定点医院名单新增页面对应的后台
 * 
 * @author JFei
 * @version [版本号, 2019-09-05 11:21:46]
 */
@RightRelation(HospitalinfoListAction.class)
@RestController("hospitalinfoaddaction")
@Scope("request")
public class HospitalinfoAddAction extends BaseController
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
        dataBean = new Hospitalinfo();
    }

    /**
     * 保存并关闭
     * 
     */
    public void add() {
        dataBean.setRowguid(UUID.randomUUID().toString());
        dataBean.setOperatedate(new Date());
        dataBean.setOperateusername(userSession.getDisplayName());
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
        dataBean = new Hospitalinfo();
    }

    public Hospitalinfo getDataBean() {
        if (dataBean == null) {
            dataBean = new Hospitalinfo();
        }
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
