package com.epoint.zoucheng.znsb.auditznsbcommentpeople.action;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.auditorga.auditcenter.domain.AuditOrgaServiceCenter;
import com.epoint.basic.auditorga.auditcenter.inter.IAuditOrgaServiceCenter;
import com.epoint.basic.auditorga.auditwindow.domain.AuditOrgaWindow;
import com.epoint.basic.auditorga.auditwindow.inter.IAuditOrgaWindowYjs;
import com.epoint.zoucheng.znsb.auditznsbcommentpeople.domain.ZCAuditZnsbCommentPeople;
import com.epoint.zoucheng.znsb.auditznsbcommentpeople.inter.IZCAuditZnsbCommentPeopleService;
import com.epoint.basic.controller.BaseController;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.znsb.util.QueueConstant;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.frame.service.organ.ou.api.IOuService;
import com.epoint.frame.service.organ.ou.entity.FrameOu;

/**
 * 工作台评价窗口人员记录表list页面对应的后台
 * 
 * @author chencong
 * @version [版本号, 2020-04-01 16:23:17]
 */
@RestController("zcauditznsbcommentpeoplelistaction")
@Scope("request")
public class ZCAuditZnsbCommentPeopleListAction extends BaseController
{
    /**
     * 
     */
    private static final long serialVersionUID = -6155574393711522892L;

    @Autowired
    private IZCAuditZnsbCommentPeopleService service;
    @Autowired
    private IAuditOrgaServiceCenter centerservice;
    @Autowired
    private IOuService ouservice;
    @Autowired
    private IAuditOrgaWindowYjs auditorgawindow;
    @Autowired
    private ICodeItemsService itemservice;

    /**
     * 工作台评价窗口人员记录表实体对象
     */
    private ZCAuditZnsbCommentPeople dataBean;

    /**
     * 表格控件model
     */
    private DataGridModel<ZCAuditZnsbCommentPeople> model;


    
    private String windowpeoplename;

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
    public DataGridModel<ZCAuditZnsbCommentPeople> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<ZCAuditZnsbCommentPeople>()
            {

                @Override
                public List<ZCAuditZnsbCommentPeople> fetchData(int first, int pageSize, String sortField,
                        String sortOrder) {
                    SqlConditionUtil sql = new SqlConditionUtil();
                    if(StringUtil.isNotBlank(windowpeoplename)){
                        sql.like("windowpeoplename", windowpeoplename);
                    }
                    if(StringUtil.isNotBlank(dataBean.getCommentdate())){
                        sql.between("commentdate", dataBean.getCommentdate(), EpointDateUtil.addDay(dataBean.getCommentdate(),QueueConstant.CONSTANT_INT_ONE));
                    }
                    
                    sql.setOrderDesc("commentdate");
                    PageData<ZCAuditZnsbCommentPeople> pagedata = service.getAuditZnsbCommentPeopleByPage(sql.getMap(), first, pageSize, sortField, sortOrder).getResult();
                    for (ZCAuditZnsbCommentPeople commentpeople : pagedata.getList()) {
                        AuditOrgaServiceCenter center = centerservice.findAuditServiceCenterByGuid(commentpeople.getCenterguid()).getResult();
                        commentpeople.put("centername", center.getCentername());
                        FrameOu ouByOuGuid = ouservice.getOuByOuGuid(commentpeople.getOuguid());
                        if(StringUtil.isNotBlank(ouByOuGuid)){
                            commentpeople.put("ouname", ouByOuGuid.getOuname());
                        }
                        AuditOrgaWindow window = auditorgawindow.getWindowByWindowGuid(commentpeople.getWindowguid()).getResult();
                        commentpeople.put("windowname", window.getWindowname());
                        if(StringUtil.isNotBlank(commentpeople.getSatisfiedtext())){
                            commentpeople.put("showsatisfiedtext", itemservice.getItemTextByCodeName("工作台评价人员满意度", commentpeople.getSatisfiedtext()));
                        }
                        else{
                            commentpeople.put("showsatisfiedtext", "无");
                        }
                    }
                    this.setRowCount(pagedata.getRowCount());
                    return pagedata.getList();
                }

            };
        }
        return model;
    }

    public ZCAuditZnsbCommentPeople getDataBean() {
        if (dataBean == null) {
            dataBean = new ZCAuditZnsbCommentPeople();
        }
        return dataBean;
    }

    public void setDataBean(ZCAuditZnsbCommentPeople dataBean) {
        this.dataBean = dataBean;
    }


    public String getWindowpeoplename() {
        return windowpeoplename;
    }

    public void setWindowpeoplename(String windowpeoplename) {
        this.windowpeoplename = windowpeoplename;
    }
    
    

}
