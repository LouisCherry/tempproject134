package com.epoint.knowledge.question.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.export.ExportModel;
import com.epoint.basic.faces.tree.LazyTreeModal9;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.knowledge.audit.service.CnsKinfoQuestionService;
import com.epoint.knowledge.category.service.CnsKinfoCategoryService;
import com.epoint.knowledge.common.CnsConstValue;
import com.epoint.knowledge.common.CnsUserSession;

import com.epoint.knowledge.common.ResourceModelService;
import com.epoint.knowledge.common.domain.CnsKinfoCategory;
import com.epoint.knowledge.common.domain.CnsKinfoQuestion;
import com.epoint.workflow.service.common.entity.execute.WorkflowWorkItem;


/**
 * 知识库提问表list页面对应的后台
 * 
 * @author wxlin
 * @version [版本号, 2017-06-01 13:43:10]
 */
@RestController("cnskinfoanswerlistaction")
@Scope("request")
public class CnsKinfoAnswerListAction extends BaseController
{
    
    private CnsKinfoQuestionService kinfoQuestionService=new CnsKinfoQuestionService();
    
    private ResourceModelService resourceModelService = new ResourceModelService();
    private CnsKinfoCategoryService kinfoCategoryService = new CnsKinfoCategoryService();

    /**
     * 知识库提问表实体对象
     */
    private CnsKinfoQuestion dataBean;
    
    /**
     * 搜索条件
     */
    private String startDate;

    private String endDate;
    
    /**
     * 类别标识
     */
    private String categoryguid;

    /**
     * 表格控件model
     */
    private DataGridModel<CnsKinfoQuestion> model;
    
    
    /**
     * 导出模型
     */
    private ExportModel exportModel;
    
    private List<SelectItem> answerstatusModel;

    @Override
    public void pageLoad() {
    }

    /**
     * 删除选定
     * 
     */
    public void deleteSelect() {
        List<String> select = getDataGridData().getSelectKeys();
        for (String sel : select) {
            kinfoQuestionService.deleteRecord(sel);
        }
        addCallbackParam("msg", "成功删除！");
    }

    public DataGridModel<CnsKinfoQuestion> getDataGridData() {
        // 获得表格对象
        Map<String, String> conditionMap = new HashMap<>();
        if (model == null) {
            model = new DataGridModel<CnsKinfoQuestion>()
            {

                @Override
                public List<CnsKinfoQuestion> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    if (StringUtil.isNotBlank(dataBean.getQuestioncontent())) {
                        conditionMap.put("QUESTIONCONTENTLIKE",dataBean.getQuestioncontent());
                    }
                    if (StringUtil.isNotBlank(dataBean.getAnswerstatus())) {
                        conditionMap.put("ANSWERSTATUS=", dataBean.getAnswerstatus());
                    }
                    if (StringUtil.isNotBlank(dataBean.getKinfocategoryname())) {
                        conditionMap.put("KINFOCATEGORYNAMELIKE", dataBean.getKinfocategoryname());
                    }
//                    if (StringUtil.isNotBlank(startDate)) {
//                        conditionMap.put("to_char(ASKTIME,'yyyy-mm-dd')GTE", EpointDateUtil.convertDate2String(EpointDateUtil.convertString2Date(startDate, EpointDateUtil.DATE_TIME_FORMAT), EpointDateUtil.DATE_FORMAT));
//                    }
//                    if (StringUtil.isNotBlank(endDate)) {
//                        conditionMap.put("to_char(ASKTIME,'yyyy-mm-dd')LTE", EpointDateUtil.convertDate2String(EpointDateUtil.convertString2Date(endDate, EpointDateUtil.DATE_TIME_FORMAT), EpointDateUtil.DATE_FORMAT));
//                    }
                    if (CnsUserSession.getInstance().getUserRolesName().contains(CnsConstValue.CnsRole.HANDLE_DEPT)) {  
                        conditionMap.put("DEPTGUID=",userSession.getOuGuid()); 
                    }
                    
                    if (StringUtil.isNotBlank(categoryguid)) {
                        //获取该类别下面所有类别，作为条件
                        List<CnsKinfoCategory> childCategory = kinfoCategoryService.getChildCategoryList(categoryguid);
                        if (childCategory != null && childCategory.size() > 0) {
                            String inStr = " ";
                            for (CnsKinfoCategory cnsKinfoCategory : childCategory) {
                                
                                inStr += "'" + cnsKinfoCategory.getRowguid() + "',";
                            }
                            inStr = inStr.substring(0, inStr.length() - 1);
                            conditionMap.put("KINFOCATEGORYGUIDIN", inStr);
                        }
                    }
                    List<CnsKinfoQuestion> list = kinfoQuestionService.getRecordPage(
                            "rowguid,questioncontent,asktime,kinfocategoryname,deptname,answerstatus,kstatus,pviguid", conditionMap, first,
                            pageSize, "asktime", "desc");
                    
                    int count = kinfoQuestionService.getRecordCount(conditionMap);
                    this.setRowCount(count);
                    return list;
                }

            };
        }
        return model;
    }
    
    public void answer(String guid)
    {
        dataBean=kinfoQuestionService.getBeanByGuid(guid);
        if(dataBean!=null)
        {
            if(dataBean.getAnswerstatus().equals(CnsConstValue.CNS_Answer_Status.ANSWER_FINISH)&&!dataBean.getKstatus().equals(CnsConstValue.KinfoStatus.BACK_AUDIT))
            {
                this.addCallbackParam("msg", "该提问已答复");
            }
            else if(dataBean.getAnswerstatus().equals(CnsConstValue.CNS_Answer_Status.ANSWER_REFUSE))
            {
                this.addCallbackParam("msg", "该提问已拒绝答复");
            }
        }
    }
    
    public LazyTreeModal9 getCategoryOuModal() {
        LazyTreeModal9 model = null;
        //成员单位只能看到自己的
        if (CnsUserSession.getInstance().getUserRolesName().contains(CnsConstValue.CnsRole.HANDLE_DEPT)) {
            String ouguid = userSession.getOuGuid();
            model = resourceModelService.getCategoryOuModel(ouguid);
        }
        else {
            model = resourceModelService.getCategoryAllModel();
        }
        return model;
    }

    public CnsKinfoQuestion getDataBean() {
        if (dataBean == null) {
            dataBean = new CnsKinfoQuestion();
        }
        return dataBean;
    }

    public void setDataBean(CnsKinfoQuestion dataBean) {
        this.dataBean = dataBean;
    }
    
    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public ExportModel getExportModel() {
        if (exportModel == null) {
            exportModel = new ExportModel("", "");
        }
        return exportModel;
    }

    public List<SelectItem> getAnsStatusModel() {
        answerstatusModel = DataUtil
                .convertMap2ComboBox((List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "提问答复状态", null, false));
        return answerstatusModel;
    }

    public String getCategoryguid() {
        return categoryguid;
    }

    public void setCategoryguid(String categoryguid) {
        this.categoryguid = categoryguid;
    }

    
}
