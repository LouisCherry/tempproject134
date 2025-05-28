package com.epoint.xmz.thirdreporteddata.spgl.spglzjfwsxblxxb.action;

import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.domain.SpglZjfwsxblxxbV3;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.inter.ISpglZjfwsxblxxbV3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 中介服务事项办理信息表修改页面对应的后台
 *
 * @author Administrator
 * @version [版本号, 2024-04-02 15:25:38]
 */
@RightRelation(SpglZjfwsxblxxbV3ListAction.class)
@RestController("spglzjfwsxblxxbv3editaction")
@Scope("request")
public class SpglZjfwsxblxxbV3EditAction extends BaseController {

    @Autowired
    private ISpglZjfwsxblxxbV3Service service;

    /**
     * 中介服务事项办理信息表实体对象
     */
    private SpglZjfwsxblxxbV3 dataBean = null;

    /**
     * 数据上传状态下拉列表model
     */
    private List<SelectItem> sjscztModel = null;
    /**
     * 数据有效标识下拉列表model
     */
    private List<SelectItem> sjyxbsModel = null;
    /**
     * 时限类型下拉列表model
     */
    private List<SelectItem> sxlxModel = null;


    public void pageLoad() {
        String guid = getRequestParameter("guid");
        dataBean = service.find(guid);
        if (dataBean == null) {
            dataBean = new SpglZjfwsxblxxbV3();
        }
    }

    /**
     * 保存修改
     */
    public void save() {
        dataBean.setOperatedate(new Date());
        dataBean.setSjsczt(0);
        dataBean.setSjyxbs(1);
        dataBean.set("sync", "0");
        service.update(dataBean);
        addCallbackParam("msg", l("修改成功") + "！");
    }

    public SpglZjfwsxblxxbV3 getDataBean() {
        return dataBean;
    }

    public void setDataBean(SpglZjfwsxblxxbV3 dataBean) {
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

    public List<SelectItem> getSxlxModel() {
        if (sxlxModel == null) {
            sxlxModel = DataUtil.convertMap2ComboBox((List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "国标_时限类型", null, false));
        }
        return this.sxlxModel;
    }

}
