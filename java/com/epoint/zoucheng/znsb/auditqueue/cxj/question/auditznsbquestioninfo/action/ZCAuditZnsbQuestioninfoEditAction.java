package com.epoint.zoucheng.znsb.auditqueue.cxj.question.auditznsbquestioninfo.action;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.StringUtil;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.zoucheng.znsb.basic.auditqueue.cxj.question.auditznsbquestiondetail.domain.AuditZnsbQuestiondetail;
import com.epoint.zoucheng.znsb.basic.auditqueue.cxj.question.auditznsbquestiondetail.inter.IZCAuditZnsbQuestiondetailService;
import com.epoint.zoucheng.znsb.basic.auditqueue.cxj.question.auditznsbquestioninfo.domain.AuditZnsbQuestioninfo;
import com.epoint.zoucheng.znsb.basic.auditqueue.cxj.question.auditznsbquestioninfo.inter.IZCAuditZnsbQuestioninfoService;

/**
 * 问卷调查-问题修改页面对应的后台
 * 
 * @author LQ
 * @version [版本号, 2021-08-04 10:35:17]
 */
@RightRelation(ZCAuditZnsbQuestioninfoListAction.class)
@RestController("zcauditznsbquestioninfoeditaction")
@Scope("request")
public class ZCAuditZnsbQuestioninfoEditAction extends BaseController
{
    private static final long serialVersionUID = 1L;

    @Autowired
    private IZCAuditZnsbQuestioninfoService service;
    @Autowired
    private IZCAuditZnsbQuestiondetailService questiondetailService;

    /**
     * 问卷调查-问题实体对象
     */
    private AuditZnsbQuestioninfo dataBean = null;
    /**
     * 题目类型下拉列表model
     */
    private List<SelectItem> questiontypeModel = null;

    /**
     * 表格控件model
     */
    private DataGridModel<AuditZnsbQuestiondetail> model;

    String name;
    String guid;

    @Override
    public void pageLoad() {
        guid = getRequestParameter("guid");
        dataBean = service.find(guid);
        if (dataBean == null) {
            dataBean = new AuditZnsbQuestioninfo();
        }
        name = dataBean.getQuestion();
        // 同步刷新时间
        if (StringUtil.isBlank(getViewData("loadtime"))) {
            addViewData("loadtime", EpointDateUtil.convertDate2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
        }
    }

    /**
     * 保存修改
     * 
     */
    public void save() {
        dataBean.setOperatedate(new Date());
        if (StringUtil.isNotBlank(name)) {
            if (name.equals(dataBean.getQuestion())) {
                service.update(dataBean);
                addCallbackParam("msg", "修改成功！");
            }
            else {
                if (service.getCountByName(dataBean.getQuestion(), dataBean.getQuestionnaireguid()) > 0) {
                    addCallbackParam("msg", "问题名称重复，请重新添加！");
                }
                else {
                    AuditZnsbQuestioninfo questioninfo = service.find(dataBean.getRowguid());
                    if (questioninfo.getOperatedate().getTime() > EpointDateUtil
                            .convertString2Date(getViewData("loadtime"), "yyyy-MM-dd HH:mm:ss").getTime()) {
                        addCallbackParam("msg", "该问题已被修改，请退出后重新进入修改！");
                        addViewData("loadtime", EpointDateUtil
                                .convertDate2String(EpointDateUtil.addSeconds(new Date(), 1), "yyyy-MM-dd HH:mm:ss"));
                        return;
                    }
                    service.update(dataBean);
                    addViewData("loadtime", EpointDateUtil.convertDate2String(EpointDateUtil.addSeconds(new Date(), 1),
                            "yyyy-MM-dd HH:mm:ss"));
                    addCallbackParam("msg", "修改成功！");
                }
            }
        }
        else {
            addCallbackParam("msg", "该问题已被删除，保存失败！");
        }
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> getQuestiontypeModel() {
        if (questiontypeModel == null) {
            questiontypeModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "题目类型", null, false));
        }
        return questiontypeModel;
    }

    public AuditZnsbQuestioninfo getDataBean() {
        return dataBean;
    }

    public void setDataBean(AuditZnsbQuestioninfo dataBean) {
        this.dataBean = dataBean;
    }

    public DataGridModel<AuditZnsbQuestiondetail> getdetailDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<AuditZnsbQuestiondetail>()
            {
                private static final long serialVersionUID = 1L;

                @Override
                public List<AuditZnsbQuestiondetail> fetchData(int first, int pageSize, String sortField,
                        String sortOrder) {
                    SqlConditionUtil sql = new SqlConditionUtil();
                    if (StringUtil.isNotBlank(guid)) {
                        sql.eq("questionguid", guid);
                    }
                    sql.setSelectFields("answeroption,sort,rowguid,answer");
                    sql.setOrderDesc("sort");
                    PageData<AuditZnsbQuestiondetail> pageData = questiondetailService
                            .findListByQuestionInfoguid(sql.getMap(), first, pageSize, sortField, sortOrder)
                            .getResult();

                    return pageData.getList();
                }
            };
        }
        return model;
    }

    public void saveAll(String changedata) {
        // 获取新增修改数据
        List<Record> newList = JSONArray.parseArray(changedata, Record.class);
        Date date = new Date();
        AuditZnsbQuestiondetail questiondetail = null;
        if (!newList.isEmpty()) {           
            for (Record record : newList) {
                if (StringUtil.isBlank(record.getStr("rowguid"))) {
                    if (StringUtil.isBlank(record.getStr("answeroption"))
                            || StringUtil.isBlank(record.getInt("sort"))) {
                        addCallbackParam("msg", "选项名称或顺序不能为空，请重新添加！");
                    }
                    else if (questiondetailService.getCountByName(record.getStr("answeroption"), guid,
                            record.getStr("rowguid")) > 0) {
                        addCallbackParam("msg", "选项名称重复，请重新添加！");
                    }
                    else {
                        questiondetail = new AuditZnsbQuestiondetail();
                        questiondetail.setRowguid(UUID.randomUUID().toString());
                        questiondetail.setOperatedate(date);
                        questiondetail.setOperateusername(userSession.getDisplayName());
                        questiondetail.setQuestionguid(guid);
                        questiondetail.setAnsweroption(record.getStr("answeroption"));
                        questiondetail.setSort(record.getInt("sort"));
                        questiondetail.setAnswer(record.getStr("answer"));
                        questiondetailService.insert(questiondetail);
                    }
                }
                else {
                    if (StringUtil.isNotBlank(record.getStr("rowguid"))) {
                        AuditZnsbQuestiondetail znsbQuestiondetail = questiondetailService
                                .find(record.getStr("rowguid"));
                        if (StringUtil.isNotBlank(znsbQuestiondetail)) {
                            znsbQuestiondetail.setOperatedate(date);
                            if (questiondetailService.getCountByName(record.getStr("answeroption"), guid,
                                    record.getStr("rowguid")) > 0) {
                                addCallbackParam("msg", "选项名称重复，请重新添加！");
                            }
                            else {
                                znsbQuestiondetail.setAnsweroption(record.getStr("answeroption"));
                                znsbQuestiondetail.setSort(record.getInt("sort"));
                                znsbQuestiondetail.setAnswer(record.getStr("answer"));
                                questiondetailService.update(znsbQuestiondetail);
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * 删除选定
     */
    public void deleteSelect() {
        List<String> select = getdetailDataGridData().getSelectKeys();
        if (select != null && (!select.isEmpty())) {
            for (String sel : select) {
                questiondetailService.deleteByGuid(sel);
            }
        }
        addCallbackParam("msg", "成功删除！");
    }
}
