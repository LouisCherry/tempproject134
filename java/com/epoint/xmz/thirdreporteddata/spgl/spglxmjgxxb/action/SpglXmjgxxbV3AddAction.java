package com.epoint.xmz.thirdreporteddata.spgl.spglxmjgxxb.action;

import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.domain.SpglXmjgxxbV3;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.inter.ISpglXmjgxxbV3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 项目监管信息表新增页面对应的后台
 *
 * @author Administrator
 * @version [版本号, 2024-04-02 15:25:32]
 */
@RightRelation(SpglXmjgxxbV3ListAction.class)
@RestController("spglxmjgxxbv3addaction")
@Scope("request")
public class SpglXmjgxxbV3AddAction extends BaseController {
    @Autowired
    private ISpglXmjgxxbV3Service service;
    /**
     * 项目监管信息表实体对象
     */
    private SpglXmjgxxbV3 dataBean = null;

    /**
     * 数据上传状态下拉列表model
     */
    private List<SelectItem> sjscztModel = null;
    /**
     * 数据有效标识下拉列表model
     */
    private List<SelectItem> sjyxbsModel = null;
    /**
     * 异常级别下拉列表model
     */
    private List<SelectItem> ycjbModel = null;
    /**
     * 异常情形下拉列表model
     */
    private List<SelectItem> ycqxModel = null;


    public void pageLoad() {
        dataBean = new SpglXmjgxxbV3();
    }

    /**
     * 保存并关闭
     */
    public void add() {
        dataBean.setRowguid(UUID.randomUUID().toString());
        dataBean.setOperatedate(new Date());
        dataBean.setOperateusername(userSession.getDisplayName());
        dataBean.setDfsjzj(UUID.randomUUID().toString());
        dataBean.setXzqhdm("370800");
        dataBean.setSjsczt(0);
        dataBean.setSjyxbs(1);
        dataBean.set("sync", "0");
        service.insert(dataBean);
        addCallbackParam("msg", l("保存成功！"));
        dataBean = null;
    }

    /**
     * 保存并新建
     */
    public void addNew() {
        add();
        dataBean = new SpglXmjgxxbV3();
    }

    public SpglXmjgxxbV3 getDataBean() {
        if (dataBean == null) {
            dataBean = new SpglXmjgxxbV3();
        }
        return dataBean;
    }

    public void setDataBean(SpglXmjgxxbV3 dataBean) {
        this.dataBean = dataBean;
    }

    public List<SelectItem> getSjscztModel() {
        if (sjscztModel == null) {
            sjscztModel = DataUtil.convertMap2ComboBox((List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "国标_数据上传状态", null, false));
        }
        return this.sjscztModel;
    }

    public List<SelectItem> getSjyxbsModel() {
        if (sjyxbsModel == null) {
            sjyxbsModel = DataUtil.convertMap2ComboBox((List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "国标_数据有效标识", null, false));
        }
        return this.sjyxbsModel;
    }

    public List<SelectItem> getYcjbModel() {
        if (ycjbModel == null) {
            ycjbModel = DataUtil.convertMap2ComboBox((List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "国标_异常级别", null, false));
        }
        return this.ycjbModel;
    }

    public List<SelectItem> getYcqxModel() {
        if (ycqxModel == null) {
            ycqxModel = DataUtil.convertMap2ComboBox((List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "国标_异常情形", null, false));
        }
        return this.ycqxModel;
    }

}
