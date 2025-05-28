package com.epoint.knowledge.question.action;

import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.knowledge.audit.service.CnsKinfoQuestionService;
import com.epoint.knowledge.common.domain.CnsKinfoQuestion;


@RestController("cnsrefuseansweraction")
@Scope("request")
public class CnsRefuseAnswerAction extends BaseController
{

    /**
     * 
     */
    private static final long serialVersionUID = 5475367764387155822L;


    private String guid="";

    private CnsKinfoQuestion dataBean=null;

    private CnsKinfoQuestionService kinfoQuestionService=new CnsKinfoQuestionService();
    
    /**
     * 拒绝答复原因类别下拉列表model
     */
    private List<SelectItem> reasontypeModel = null;

    @Override
    public void pageLoad() {
        guid = this.getRequestParameter("guid");
        dataBean = kinfoQuestionService.getBeanByGuid(guid);

    }

  
    public void setDataBean(CnsKinfoQuestion dataBean) {
        this.dataBean = dataBean;
    }

    public CnsKinfoQuestion getDataBean() {
        return dataBean;
    }



    public List<SelectItem> getReasontypeModel() {
        if (reasontypeModel == null)
        {
            reasontypeModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "提问拒绝答复原因类型", null, false));
        }
        return reasontypeModel;
    }

}
