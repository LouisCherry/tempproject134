package com.epoint.xmz.thirdreporteddata.audittaskzj.action;

import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.domain.SpglXmspsxblxxxxbV3;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.inter.ISpglXmspsxblxxxxbV3;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;


/**
 * 项目审批事项办理信息表修改页面对应的后台
 *
 * @author 95453
 * @version [版本号, 2019-06-20 14:31:29]
 */
@RestController("registerspglxmspsxblxxxxbeditv3action")
@Scope("request")
public class RegisterSpglXmspsxblxxxxbEditV3Action extends BaseController
{

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Autowired
    private ISpglXmspsxblxxxxbV3 service;
    @Autowired
    private ICodeItemsService codeItemsService;

    // 下拉框组件Model
    private List<SelectItem> blztModel;
    private List<SelectItem> shifouModel;

    /**
     * 实体对象
     */
    private SpglXmspsxblxxxxbV3 dataBean = null;

    public void pageLoad() {
        String guid = getRequestParameter("guid");
        dataBean = service.find(guid);
        if (dataBean != null) {
            addCallbackParam("sjsczt", dataBean.getSjsczt().toString());
            addCallbackParam("sbyy", StringUtil.isNotBlank(dataBean.getSbyy()) ? dataBean.getSbyy() : "无");
            addCallbackParam("sync", dataBean.getStr("sync"));
            int sjsczt = dataBean.getSjsczt();
            String sjscztText = "";
            if (sjsczt == -1) {
                sjscztText = "本地校验失败";
            }
            else {
                sjscztText = codeItemsService.getItemTextByCodeName("国标_数据上传状态", String.valueOf(sjsczt));
            }
            addCallbackParam("sjscztText", sjscztText);
        }
    }

    /**
     * 保存修改
     */
    public void save() {
        service.update(dataBean);
        addCallbackParam("msg", "保存成功");
    }

    // 办理状态
    @SuppressWarnings("unchecked")
    public List<SelectItem> getBlztModel() {
        if (blztModel == null) {
            blztModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "国标_事项办理状态", null, false));
        }
        return this.blztModel;
    }

    // 数据有效标识
    @SuppressWarnings("unchecked")
    public List<SelectItem> getShifouModel() {
        if (shifouModel == null) {
            shifouModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "国标_数据有效标识", null, false));
        }
        return this.shifouModel;
    }

    public SpglXmspsxblxxxxbV3 getDataBean() {
        return dataBean;
    }

    public void setDataBean(SpglXmspsxblxxxxbV3 dataBean) {
        this.dataBean = dataBean;
    }

}
