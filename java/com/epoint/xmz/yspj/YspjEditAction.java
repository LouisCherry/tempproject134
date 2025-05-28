package com.epoint.xmz.yspj;

import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;

/**import com.epoint.basic.faces.util.DataUtil;
 * 惠企政策库修改页面对应的后台
 * 
 * @author 86180
 * @version [版本号, 2019-10-08 23:39:45]
 */
@RestController("yspjditaction")
@Scope("request")
public class YspjEditAction extends BaseController
{


    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
     * 是否四新企业单选按钮组model
     */
    private List<SelectItem> sfsxqyModel = null;
    

    public void pageLoad() {
        
    }

    /**
     * 保存修改
     * 
     */
    public void save() {
        
    }

    
    public List<SelectItem> getSfsxqyModel() {
        if (sfsxqyModel == null) {
            sfsxqyModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("单选按钮组", "营商满意度", null, false));
        }
        return this.sfsxqyModel;
    }

   
    
    
}
