package com.epoint.jnhospital.hospitalinfo.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import com.epoint.jnhospital.hospitalinfo.api.IHospitalinfoService;
import com.epoint.jnhospital.hospitalinfo.api.entity.Hospitalinfo;

/**
 * 定点医院名单详情页面对应的后台
 * 
 * @author JFei
 * @version [版本号, 2019-09-05 11:21:46]
 */
@RightRelation(HospitalinfoListAction.class)
@RestController("hospitalinfodetailaction")
@Scope("request")
public class HospitalinfoDetailAction extends BaseController
{
    @Autowired
    private IHospitalinfoService service;

    /**
     * 定点医院名单实体对象
     */
    private Hospitalinfo dataBean = null;

    public void pageLoad() {
        String guid = getRequestParameter("guid");
        dataBean = service.find(guid);
        if (dataBean == null) {
            dataBean = new Hospitalinfo();
        }
    }

    public Hospitalinfo getDataBean() {
        return dataBean;
    }
}
