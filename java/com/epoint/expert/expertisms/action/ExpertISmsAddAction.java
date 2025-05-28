package com.epoint.expert.expertisms.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;
import org.xm.similarity.util.StringUtil;

import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.expert.expertiresult.api.IExpertIResultService;
import com.epoint.expert.expertiresult.api.entity.ExpertIResult;
import com.epoint.expert.expertisms.api.IExpertISmsService;
import com.epoint.expert.expertisms.api.entity.ExpertISms;
import com.epoint.frame.service.message.api.IMessagesCenterService;
import com.epoint.frame.service.metadata.mis.util.ListGenerator;

/**
 * 专家抽取短信发送表新增页面对应的后台
 * 
 * @author Lee
 * @version [版本号, 2019-08-21 15:42:10]
 */
@RightRelation(ExpertISmsListAction.class)
@RestController("expertismsaddaction")
@Scope("request")
public class ExpertISmsAddAction extends BaseController
{
    /**
     * 
     */
    private static final long serialVersionUID = -4839207986530463523L;

    @Autowired
    private IExpertISmsService service;

    /**
     * 专家抽取结果API
     */
    @Autowired
    private IExpertIResultService resultService;

    /**
     * 待办API
     */
    @Autowired
    private IMessagesCenterService messageService;

    /**
     * 表格控件model
     */
    private DataGridModel<ExpertIResult> model;

    /**
     * 专家抽取短信发送表实体对象
     */
    private ExpertISms dataBean = null;

    /**
     * 专家抽取实例guid
     */
    private String instanceGuid = null;

    /**
     * 信息表guid
     */
    private String rowGuid = null;

    /**
     * 被选中专家的手机号
     */
    private String contactPhone = null;

    public void pageLoad() {
        dataBean = new ExpertISms();
        instanceGuid = request.getParameter("instanceGuid");
        rowGuid = request.getParameter("guid");
        if (StringUtil.isNotBlank(rowGuid)) {
            dataBean = service.find(rowGuid);
            instanceGuid = dataBean.getInstanceguid();
            addCallbackParam("userguids", dataBean.getUserguids());
        }

    }

    /**
     * 保存并关闭
     * 
     */
    public void add() {
        // 发送短信
        if (StringUtil.isNotBlank(contactPhone)) {
            String[] phone = contactPhone.split(";");
            String[] userGuid = dataBean.getUserguids().split(";");
            for (int i = 0; i < phone.length; i++) {
                String messageItemGuid = UUID.randomUUID().toString();
                messageService.insertSmsMessage(messageItemGuid, dataBean.getContent(), new Date(), 0, new Date(),
                        phone[i], userGuid[i], "", "", "", "", "", "", false, "短信");
            }
        }

        dataBean.setRowguid(UUID.randomUUID().toString());
        dataBean.setOperatedate(new Date());
        dataBean.setOperateusername(userSession.getDisplayName());
        dataBean.setInstanceguid(instanceGuid);
        service.insert(dataBean);
        addCallbackParam("msg", "发送成功！");
        dataBean = null;
    }

    /**
     * 保存并新建
     * 
     */
    public void addNew() {
        add();
        dataBean = new ExpertISms();
    }

    public ExpertISms getDataBean() {
        if (dataBean == null) {
            dataBean = new ExpertISms();
        }
        return dataBean;
    }

    /**
     * 
     *  根据InstanceGuid获取专家抽取结果的专家名单
     *  @return  DataGridModel<ExpertIResult>
     */
    public DataGridModel<ExpertIResult> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<ExpertIResult>()
            {
                /**
                 * 
                 */
                private static final long serialVersionUID = 1L;

                @Override
                public List<ExpertIResult> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    List<Object> conditionList = new ArrayList<Object>();
                    String conditionSql = ListGenerator.getSearchSql(getRequestContext().getComponents(),
                            conditionList);

                    // 查询条件
                    conditionSql = conditionSql + " and InstanceGuid=? and Is_Attend=1";
                    if (StringUtil.isNotBlank(instanceGuid)) {
                        conditionList.add(instanceGuid);
                    }

                    List<ExpertIResult> list = resultService.findList(
                            ListGenerator.generateSql("Expert_I_Result", conditionSql, sortField, sortOrder), first,
                            pageSize, conditionList.toArray());
                    int count = resultService
                            .findList(ListGenerator.generateSql("Expert_I_Result", conditionSql, sortField, sortOrder),
                                    conditionList.toArray())
                            .size();
                    this.setRowCount(count);
                    return list;
                }

            };
        }
        return model;
    }

    public void setDataBean(ExpertISms dataBean) {
        this.dataBean = dataBean;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

}
