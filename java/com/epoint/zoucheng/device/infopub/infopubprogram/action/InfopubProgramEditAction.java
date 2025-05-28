package com.epoint.zoucheng.device.infopub.infopubprogram.action;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.zoucheng.device.infopub.infopubprogram.api.IInfopubProgramService;
import com.epoint.zoucheng.device.infopub.infopubprogram.api.entity.InfopubProgram;
import com.epoint.zoucheng.device.infopub.infopubprogram.impl.InfopubProgramService;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;

/**
 * 节目表修改页面对应的后台
 * 
 * @author 15394
 * @version [版本号, 2017-08-16 15:39:32]
 */
@RestController("infopubprogrameditaction")
@Scope("request")
public class InfopubProgramEditAction extends BaseController
{
    private static final long serialVersionUID = -7608063972086830040L;

    @Autowired
    private IInfopubProgramService service;
    /**
     * 节目表实体对象
     */
    private InfopubProgram dataBean = null;

    /**
    * 分辨率下拉列表model
    */
    private List<SelectItem> resolutionModel = null;
    /**
     * 网页滚动速度下拉列表model
     */
    private List<SelectItem> scrollspeedModel = null;

    public void pageLoad() {
        String guid = getRequestParameter("guid");
        dataBean = service.find(guid);
        if (dataBean == null) {
            dataBean = new InfopubProgram();
        }
    }

    /**
     * 保存修改
     * 
     */
    public void save() {
        String host = request.getRequestURL().toString().split("rest")[0];
        dataBean.setOperatedate(new Date());
        dataBean.setUpdatetime(new Date());
        service.createProgramFile(dataBean, host);
        service.update(dataBean);
        service.createProgramFile(dataBean, host);
        addCallbackParam("msg", "修改成功！");
    }

    public InfopubProgram getDataBean() {
        return dataBean;
    }

    public void setDataBean(InfopubProgram dataBean) {
        this.dataBean = dataBean;
    }

    public List<SelectItem> getResolutionModel() {
        if (resolutionModel == null) {
            resolutionModel = DataUtil.convertMap2ComboBox(CodeModalFactory.factory("下拉列表", "分辨率", null, false));
        }
        return this.resolutionModel;
    }

    public List<SelectItem> getScrollspeedModel() {
        if (scrollspeedModel == null) {
            scrollspeedModel = DataUtil.convertMap2ComboBox(CodeModalFactory.factory("下拉列表", "网页滚动速度", null, false));
        }
        return this.scrollspeedModel;
    }

}
