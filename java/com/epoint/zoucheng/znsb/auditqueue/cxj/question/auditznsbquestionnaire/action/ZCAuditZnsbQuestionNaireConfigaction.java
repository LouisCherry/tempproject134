package com.epoint.zoucheng.znsb.auditqueue.cxj.question.auditznsbquestionnaire.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.authentication.UserSession;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.tree.ConstValue9;
import com.epoint.basic.faces.tree.FetchHandler9;
import com.epoint.basic.faces.tree.LazyTreeModal9;
import com.epoint.basic.faces.tree.SimpleFetchHandler9;
import com.epoint.basic.faces.tree.TreeData;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.frame.service.metadata.code.entity.CodeItems;
import com.epoint.zoucheng.znsb.auditznsbquestionnairequestion.api.IAuditZnsbQuestionnaireQuestionService;
import com.epoint.zoucheng.znsb.auditznsbquestionnairequestion.api.entity.AuditZnsbQuestionnaireQuestion;
import com.epoint.zoucheng.znsb.basic.auditqueue.cxj.question.auditznsbquestioninfo.domain.AuditZnsbQuestioninfo;
import com.epoint.zoucheng.znsb.basic.auditqueue.cxj.question.auditznsbquestioninfo.inter.IZCAuditZnsbQuestioninfoService;

@RestController("zcauditznsbquestionnaireconfigaction")
@Scope("request")
public class ZCAuditZnsbQuestionNaireConfigaction extends BaseController
{
    /**
     * 
     */
    private static final long serialVersionUID = -4632640761998959244L;

    @Autowired
    private IAuditZnsbQuestionnaireQuestionService questionnaireQuestionService;
    @Autowired
    private IZCAuditZnsbQuestioninfoService questioninfoService;
    @Autowired
    private ICodeItemsService codeItemsService;

    /**
     * 题目与问题关联关系实体对象
     */
    private AuditZnsbQuestionnaireQuestion dataBean = null;

    private LazyTreeModal9 treeModel = null;
    /**
     * 题目标识
     */
    private String questionnaireguid = null;
    private String centerguid = null;

    @Override
    public void pageLoad() {
        questionnaireguid = getRequestParameter("guid");
        centerguid = ZwfwUserSession.getInstance().getCenterGuid();
    }

    /***
     * 
     * 用treebean构造事项树
     * 
     * @return
     * 
     * 
     */
    public LazyTreeModal9 getTreeModel() {
        if (treeModel == null) {
            treeModel = new LazyTreeModal9(loadAllTask());
            treeModel.setSelectNode(this.getSelectQuestioin(questionnaireguid));
            treeModel.setTreeType(ConstValue9.CHECK_SINGLE);
            treeModel.setInitType(ConstValue9.GUID_INIT);
            treeModel.setRootName("添加该题目下问题");
            treeModel.setRootSelect(false);
        }
        return treeModel;
    }

    /**
     * 
     * 使用SimpleFetchHandler9构造树
     * 
     * @return
     * 
     * 
     */
    private SimpleFetchHandler9 loadAllTask() {
        return new SimpleFetchHandler9()
        {
            private static final long serialVersionUID = 1L;

            @Override
            @SuppressWarnings({"rawtypes", "unchecked" })
            public <T> List<T> search(String conndition) {
                List list = new ArrayList();
                if (StringUtil.isNotBlank(conndition)) {
                    list.clear();
                    list = selectQuestionByCondition(conndition);
                }
                return list;
            }

            /**
             * 题目配置问题根据条件查询问题
             * 
             * @param condition
             * @return
             * 
             * 
             */
            public List<AuditZnsbQuestioninfo> selectQuestionByCondition(String condition) {
                return questioninfoService.findListByQuestion(condition, centerguid);
            }

            @Override
            @SuppressWarnings({"rawtypes", "unchecked" })
            public <T> List<T> fetchData(int level, TreeData treeData) {
                List list = new ArrayList();
                // 一开始加载或者点击节点的时候触发
                if (level == FetchHandler9.FETCH_ONELEVEL) {
                    // 最开始加载的时候，把所有的题目类型加载出来，最开始treeData的guid为空
                    if (StringUtil.isBlank(treeData.getObjectGuid())) {
                        list = codeItemsService.listCodeItemsByCodeName("题目类型");
                        // 如果treeData的guid不为空，则说明该节点下面有子节点，获取该窗口部门下的所有事项
                    }
                    else {
                        list = questioninfoService.findListByQuestionType(treeData.getObjectGuid(), centerguid);
                    }
                }
                // 点击checkbox的时候触发
                else {
                    // 如果点击的是部门的checkbox，则返回该部门下所有的事项的list
                    if ("questiontype".equals(treeData.getObjectcode())) {
                        list = questioninfoService.findListByQuestionType(treeData.getObjectGuid(), centerguid);
                    }
                }
                return list;
            }

            @Override
            public int fetchChildCount(TreeData treeData) {
                return questioninfoService.findListByQuestionType(treeData.getObjectGuid(), centerguid).size();
            }

            @Override
            public List<TreeData> changeDBListToTreeDataList(List<?> dataList) {
                List<TreeData> treeList = new ArrayList<TreeData>();
                if (dataList != null) {
                    for (Object obj : dataList) {
                        // 将dataList转化为CodeItems的list
                        if (obj instanceof CodeItems) {
                            CodeItems codeItems = (CodeItems) obj;
                            TreeData treeData = new TreeData();
                            treeData.setObjectGuid(codeItems.getItemValue());
                            treeData.setTitle(codeItems.getItemText());
                            List<AuditZnsbQuestioninfo> questionlist = questioninfoService
                                    .findListByQuestionType(treeData.getObjectGuid(), centerguid);
                            // 没有子节点的不加载
                            if (questionlist != null && !questionlist.isEmpty()) {
                                treeData.setObjectcode("questiontype");
                                treeList.add(treeData);
                            }

                        }
                        // 将dataList转化为AuditZnsbQuestioninfo的list
                        if (obj instanceof AuditZnsbQuestioninfo) {
                            AuditZnsbQuestioninfo questioninfo = (AuditZnsbQuestioninfo) obj;
                            TreeData treeData = new TreeData();
                            treeData.setObjectGuid(questioninfo.getRowguid());
                            treeData.setTitle(questioninfo.getQuestion());
                            // objectcode的作用是来区分是点击了类型还是问题
                            treeData.setObjectcode("auditznsbquestioninfo");
                            treeList.add(treeData);
                        }
                    }
                }
                return treeList;
            }
        };
    }

    /**
     * 根据questionnaireguid获取问题SelectItem，初始化问题信息
     * 
     * @param questionnaireguid
     * @return
     * 
     * 
     */
    public List<SelectItem> getSelectQuestioin(String questionnaireguid) {
        List<SelectItem> showquestioinList = new ArrayList<SelectItem>();
        if (StringUtil.isNotBlank(questionnaireguid)) {
            List<AuditZnsbQuestionnaireQuestion> questionnaireQuestions = questionnaireQuestionService
                    .findListByQuestionNaireGuid(questionnaireguid);
            if (questionnaireQuestions != null && !questionnaireQuestions.isEmpty()) {
                for (AuditZnsbQuestionnaireQuestion questionnaireQuestion : questionnaireQuestions) {
                    AuditZnsbQuestioninfo questioninfo = questioninfoService.find(questionnaireQuestion.getQuestion());
                    if (questioninfo != null) {
                        showquestioinList.add(new SelectItem(questioninfo.getRowguid(), questioninfo.getQuestion()));
                    }
                }
            }
        }
        return showquestioinList;
    }

    /**
     * 
     * 把问题和题目信息保存到关系表里面
     * 
     * @param guidList
     *            前台获取的事项guid 用“;”分割
     * @param questionnaireguid
     *            题目guid
     * 
     * 
     */
    public void saveQuestionToQuestionNaire(String questionguidList, String questionnaireguid) {
        // 添加新的问题关联关系
        if (StringUtil.isNotBlank(questionnaireguid)) {
            questionnaireQuestionService.deleteByQuestionNaireGuid(questionnaireguid);

            if (StringUtil.isNotBlank(questionguidList)) {
                String[] questionGuids = questionguidList.split(";");
                Date now = new Date();
                for (int i = 0; i < questionGuids.length; i++) {

                    // 操作audit_znsb_questionnaire_question
                    AuditZnsbQuestionnaireQuestion questionnaireQuestion = new AuditZnsbQuestionnaireQuestion();
                    questionnaireQuestion.setOperatedate(now);
                    questionnaireQuestion.setOperateusername(UserSession.getInstance().getDisplayName());
                    questionnaireQuestion.setRowguid(UUID.randomUUID().toString());

                    questionnaireQuestion.setQuestionnaireguid(questionnaireguid);
                    questionnaireQuestion.setQuestion(questionGuids[i]);
                    questionnaireQuestionService.insert(questionnaireQuestion);
                }
            }
        }
        addCallbackParam("msg", "选题成功！");
    }

    public String getQuestionnaireguid() {
        return questionnaireguid;
    }

    public void setQuestionnaireguid(String questionnaireguid) {
        this.questionnaireguid = questionnaireguid;
    }

    public AuditZnsbQuestionnaireQuestion getDataBean() {
        return dataBean;
    }

    public void setDataBean(AuditZnsbQuestionnaireQuestion dataBean) {
        this.dataBean = dataBean;
    }

}
