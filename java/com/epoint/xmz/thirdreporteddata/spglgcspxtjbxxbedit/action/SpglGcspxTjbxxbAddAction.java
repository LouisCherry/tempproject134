package com.epoint.xmz.thirdreporteddata.spglgcspxtjbxxbedit.action;

import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.xmz.thirdreporteddata.spglgcspxtjbxxbedit.api.ISpglGcspxtjbxxbEditService;
import com.epoint.xmz.thirdreporteddata.spglgcspxtjbxxbedit.api.entity.SpglGcspxtjbxxbEdit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 工改信息维护维护页面对应的后台
 * 
 * @author lzm
 * @version [版本号, 2023-09-01 10:29:56]
 */
@RestController("spglgcspxtjbxxbaddaction")
@Scope("request")
public class SpglGcspxTjbxxbAddAction extends BaseController
{
    @Autowired
    private ISpglGcspxtjbxxbEditService service;

    private SpglGcspxtjbxxbEdit dataBean;

    private List<SelectItem> xqModel = null;
    private List<SelectItem> xtjsfsModel = null;
    public void pageLoad() {
    }

    /**
     * 保存并新建
     *
     */
    public void addNew() {
        add();
        dataBean = new SpglGcspxtjbxxbEdit();
    }

    /**
     * 保存并关闭
     *
     */
    public void add() {
        //行政区划唯一判断
        if (StringUtil.isNotBlank(dataBean.getXzqhdm())) {
            Integer count = service.IsExistXzqhdm(dataBean.getXzqhdm());
            if (count > 0) {
                addCallbackParam("samename", "该区划已存在系统！");
            }
            else {
                dataBean.setRowguid(UUID.randomUUID().toString());
                dataBean.setOperateusername(userSession.getDisplayName());
                dataBean.setOperatedate(new Date());
                dataBean.setSync(ZwfwConstant.CONSTANT_STR_ZERO);
                dataBean.setSjyxbs(ZwfwConstant.CONSTANT_STR_ONE);
                dataBean.setSjsczt(ZwfwConstant.CONSTANT_STR_ZERO);
                service.insert(dataBean);
                addCallbackParam("msg", "保存成功！");
                dataBean = null;
            }
        }
    }

    public SpglGcspxtjbxxbEdit getDataBean() {
        if (dataBean == null) {
            dataBean = new SpglGcspxtjbxxbEdit();
        }
        return dataBean;
    }

    public void setDataBean(SpglGcspxtjbxxbEdit dataBean) {
        this.dataBean = dataBean;
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> getXqModel() {
        if (xqModel == null) {
            xqModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "行政区划", null, false));
        }
        return this.xqModel;
    }
    @SuppressWarnings("unchecked")
    public List<SelectItem> getXtjsfsModel() {
        if (xtjsfsModel == null) {
            xtjsfsModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "系统建设方式", null, false));
        }
        return this.xtjsfsModel;
    }
}
