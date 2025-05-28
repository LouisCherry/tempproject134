package com.epoint.auditsp.auditspbasetask.action;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.EpointKeyNames9;
import com.epoint.basic.auditsp.auditspbasetask.domain.AuditSpBasetask;
import com.epoint.basic.auditsp.auditspbasetask.inter.IAuditSpBasetask;
import com.epoint.basic.authentication.UserSession;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.utils.collection.EpointCollectionUtils;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.xmz.thirdreporteddata.auditqypgtask.api.IAuditQypgTaskService;
import com.epoint.xmz.thirdreporteddata.auditqypgtask.api.entity.AuditQypgTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 并联审批事项表list页面对应的后台
 *
 * @author Administrator
 * @version [版本号, 2019-04-25 10:07:31]
 */
@RestController("jnauditspbasetaskeditaction")
@Scope("request")
public class JnAuditSpBasetaskEditAction extends BaseController {

    private static final long serialVersionUID = 7798753811284837045L;

    @Autowired
    private IAuditSpBasetask service;

    @Autowired
    private IAuditQypgTaskService iAuditQypgTaskService;

    private AuditSpBasetask dataBean;

    private String basetaskguid;

    private List<SelectItem> oumodel;
    //所属区县代码项
    private List<SelectItem> belongToAreaModel;

    /**
     * 是否代码项
     */
    private List<SelectItem> yesornoModal;

    /**
     * 问题
     */
    private String question;

    /**
     * 要办的回答
     */
    private String yesAnswer;

    /**
     * 不办的回答
     */
    private String noAnswer;

    public void pageLoad() {
        basetaskguid = getRequestParameter("basetaskguid");
        dataBean = service.getAuditSpBasetaskByrowguid(basetaskguid).getResult();
        if (dataBean == null) {
            dataBean = new AuditSpBasetask();
            addCallbackParam("msg", "未获取到需要修改的数据！");
        } else {
            String questions = dataBean.getQuestions();
            if (StringUtil.isNotBlank(questions)) {
                JSONObject obj = JSON.parseObject(questions);
                question = obj.getString("question");
                yesAnswer = obj.getString("answer_yes");
                noAnswer = obj.getString("answer_no");
            }
        }

        // 初始化区域评估事项
        List<AuditQypgTask> list = iAuditQypgTaskService.getAuditQypgistByBaseTaskGuid(basetaskguid);
        if (EpointCollectionUtils.isNotEmpty(list)) {
            dataBean.set("qypgsxbm", list.stream().map(item -> item.get("taskcode").toString()).collect(Collectors.toList())
                    .stream().map(String::valueOf).collect(Collectors.joining(",")));
        }

    }

    public void save() {
        //判断事项编码是不是重复
        if (!"9990".equals(dataBean.getTaskcode())) {
            SqlConditionUtil sqlc = new SqlConditionUtil();
            sqlc.eq("taskcode", dataBean.getTaskcode());
            sqlc.nq("rowguid", dataBean.getRowguid());
            Integer count = service.getCountByCondition(sqlc.getMap()).getResult();
            if (count != null && count > 0) {
                addCallbackParam("msg", "标准事项编码重复！");
                return;
            }
        }
        dataBean.setQuestions("{\"question\":\"" + question + "\",\"answer_yes\":\"" + yesAnswer + "\",\"answer_no\":\"" + noAnswer + "\"}");
        dataBean.setOperatedate(new Date());
        dataBean.setOperateusername(UserSession.getInstance().getDisplayName());

        // 绑定标准事项和区域评估事项
        if (StringUtil.isNotBlank(dataBean.get("qypgsxbm"))) {
            String[] split = dataBean.get("qypgsxbm").toString().split(",");
            for (String taskcode : split) {
                AuditQypgTask auditQypgTask = iAuditQypgTaskService.findByTaskcode(taskcode);
                auditQypgTask.set("basetaskcode", dataBean.getTaskcode());
                auditQypgTask.set("basetaskguid", dataBean.getRowguid());
                iAuditQypgTaskService.update(auditQypgTask);
            }
        }

        service.updateBasetask(dataBean);
        addCallbackParam("msg", "保存成功！");
    }

    public List<SelectItem> taskSelectModel() {
        return iAuditQypgTaskService.findAllList().stream().map(a -> {
            return new SelectItem(a.getTaskcode(), a.getTaskname());
        }).collect(Collectors.toList());
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> getOuModel() {
        if (oumodel == null) {
            oumodel = DataUtil.convertMap2ComboBox((List<Map<String, String>>) CodeModalFactory
                    .factory(EpointKeyNames9.CHECK_SELECT_GROUP, "审批部门", null, false));
        }
        return this.oumodel;
    }
    @SuppressWarnings("unchecked")
    public List<SelectItem> getBelongToAreaModel() {
        if (belongToAreaModel == null) {
            belongToAreaModel = DataUtil.convertMap2ComboBox((List<Map<String, String>>) CodeModalFactory
                    .factory(EpointKeyNames9.CHECK_SELECT_GROUP, "所属区县", null, false));
        }
        return this.belongToAreaModel;
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> getYesornoModal() {
        if (yesornoModal == null) {
            yesornoModal = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("单选按钮组", "是否", null, false));
        }
        return this.yesornoModal;
    }

    public AuditSpBasetask getDataBean() {
        if (dataBean == null) {
            dataBean = new AuditSpBasetask();
        }
        return dataBean;
    }

    public void setDataBean(AuditSpBasetask dataBean) {
        this.dataBean = dataBean;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getYesAnswer() {
        return yesAnswer;
    }

    public void setYesAnswer(String yesAnswer) {
        this.yesAnswer = yesAnswer;
    }

    public String getNoAnswer() {
        return noAnswer;
    }

    public void setNoAnswer(String noAnswer) {
        this.noAnswer = noAnswer;
    }
}
