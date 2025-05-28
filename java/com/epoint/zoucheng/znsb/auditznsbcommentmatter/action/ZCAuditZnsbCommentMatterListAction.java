package com.epoint.zoucheng.znsb.auditznsbcommentmatter.action;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.auditorga.auditcenter.domain.AuditOrgaServiceCenter;
import com.epoint.basic.auditorga.auditcenter.inter.IAuditOrgaServiceCenter;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.zoucheng.znsb.auditznsbcommentmatter.domain.ZCAuditZnsbCommentMatter;
import com.epoint.zoucheng.znsb.auditznsbcommentmatter.inter.IZCAuditZnsbCommentMatterService;
import com.epoint.basic.controller.BaseController;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.znsb.util.QueueConstant;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;

/**
 * 工作台评价事项记录表list页面对应的后台
 * 
 * @author chencong
 * @version [版本号, 2020-04-08 13:37:36]
 */
@RestController("zcauditznsbcommentmatterlistaction")
@Scope("request")
public class ZCAuditZnsbCommentMatterListAction extends BaseController
{
    /**
     * 
     */
    private static final long serialVersionUID = 1438060341538241589L;

    @Autowired
    private IZCAuditZnsbCommentMatterService service;
    @Autowired
    private IAuditTask audittask;
    @Autowired
    private IAuditOrgaServiceCenter centerservice;
    @Autowired
    private ICodeItemsService itemservice;

    /**
     * 工作台评价事项记录表实体对象
     */
    private ZCAuditZnsbCommentMatter dataBean;
    private String taskname;

    /**
     * 表格控件model
     */
    private DataGridModel<ZCAuditZnsbCommentMatter> model;

    public void pageLoad() {
    }

    /**
     * 删除选定
     * 
     */
    public void deleteSelect() {
        List<String> select = getDataGridData().getSelectKeys();
        for (String sel : select) {
            service.deleteByGuid(sel);
        }
        addCallbackParam("msg", "成功删除！");
    }

    @SuppressWarnings("serial")
    public DataGridModel<ZCAuditZnsbCommentMatter> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<ZCAuditZnsbCommentMatter>()
            {

                @Override
                public List<ZCAuditZnsbCommentMatter> fetchData(int first, int pageSize, String sortField,
                        String sortOrder) {
                    SqlConditionUtil sql = new SqlConditionUtil();
                    if(StringUtil.isNotBlank(taskname)){
                        sql.like("taskname", taskname);
                    }
                    if(StringUtil.isNotBlank(dataBean.getCommentdate())){
                        sql.between("commentdate", dataBean.getCommentdate(), EpointDateUtil.addDay(dataBean.getCommentdate(),QueueConstant.CONSTANT_INT_ONE));
                    }
                    sql.setOrderDesc("commentdate");
                    PageData<ZCAuditZnsbCommentMatter> pagedata = service.getAuditZnsbCommentMatterByPage(sql.getMap(), first, pageSize, sortField, sortOrder).getResult();
                    for (ZCAuditZnsbCommentMatter commentmatter : pagedata.getList()) {
                        AuditOrgaServiceCenter center = centerservice.findAuditServiceCenterByGuid(commentmatter.getCenterguid()).getResult();
                        commentmatter.put("centername", center.getCentername());
                        if(StringUtil.isNotBlank(commentmatter.getSatisfiedtext())){
                            commentmatter.put("showsatisfiedtext", itemservice.getItemTextByCodeName("工作台评价事项满意度", commentmatter.getSatisfiedtext()));
                        }
                        else{
                            commentmatter.put("showsatisfiedtext", "无");
                        }
                        AuditTask task = audittask.getAuditTaskByGuid(commentmatter.getTaskguid(), true).getResult();
                        if(StringUtil.isNotBlank(task)&&StringUtil.isNotBlank(task.getTaskname())){
                            commentmatter.put("taskname", task.getTaskname());
                        }
                    }
                    this.setRowCount(pagedata.getRowCount());
                    return pagedata.getList();
                }

            };
        }
        return model;
    }

    public ZCAuditZnsbCommentMatter getDataBean() {
        if (dataBean == null) {
            dataBean = new ZCAuditZnsbCommentMatter();
        }
        return dataBean;
    }

    public void setDataBean(ZCAuditZnsbCommentMatter dataBean) {
        this.dataBean = dataBean;
    }

    public String getTaskname() {
        return taskname;
    }

    public void setTaskname(String taskname) {
        this.taskname = taskname;
    }

    
}
