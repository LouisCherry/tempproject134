package com.epoint.expert.expertiresult.action;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.expert.expertiresult.api.IExpertIResultService;
import com.epoint.expert.expertiresult.api.entity.ExpertIResult;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;

/**
 * 专家抽取结果表修改页面对应的后台
 * 
 * @author Lee
 * @version [版本号, 2019-08-21 15:42:03]
 */

@RestController("expertiresulteditaction")
@Scope("request")
public class ExpertIResultEditAction extends BaseController
{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Autowired
    private IExpertIResultService service;

    /**
     * 专家抽取结果表实体对象
     */
    private ExpertIResult dataBean = null;

    /**
     * 是否参加单选按钮组model
     */
    private List<SelectItem> is_attendModel = null;
    
    /**
     * 用于更新的专家抽取结果表实体对象
     */
    private ExpertIResult updateResult = null;

    public void pageLoad() {
        String guid = getRequestParameter("guid");
        dataBean = service.find(guid);
        if (dataBean == null) {
            dataBean = new ExpertIResult();
        }
        updateResult = dataBean;
    }

    /**
     * 保存修改
     * 
     */
    public void save() {
        updateResult.setOperatedate(new Date());
        //将从页面获取到的数据放入updateResult
        updateResult.setIs_attend(dataBean.getIs_attend());
        updateResult.setAbsentreason(dataBean.getAbsentreason());
        
        service.update(updateResult);
        addCallbackParam("msg", "修改成功！");
    }

    public ExpertIResult getDataBean() {
        return dataBean;
    }

    public void setDataBean(ExpertIResult dataBean) {
        this.dataBean = dataBean;
    }

    public List<SelectItem> getIs_attendModel() {
        if (is_attendModel == null) {
            is_attendModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("单选按钮组", "是否", null, false));
        }
        return this.is_attendModel;
    }

}
