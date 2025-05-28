package com.epoint.xmz.xmztaskguideconfig.action;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.auditorga.auditarea.domain.AuditOrgaArea;
import com.epoint.basic.auditorga.auditarea.inter.IAuditOrgaArea;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import com.epoint.basic.faces.fileupload.FileUploadModel9;
import com.epoint.basic.faces.tree.DefaultFileUploadHandlerImpl9;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.xmz.xmztaskguideconfig.api.IXmzTaskguideConfigService;
import com.epoint.xmz.xmztaskguideconfig.api.entity.XmzTaskguideConfig;

/**
 * 事项指南配置表新增页面对应的后台
 * 
 * @author xczheng0314
 * @version [版本号, 2023-03-21 11:38:55]
 */
@RightRelation(XmzTaskguideConfigListAction.class)
@RestController("xmztaskguideconfigaddaction")
@Scope("request")
public class XmzTaskguideConfigAddAction  extends BaseController
{
	@Autowired
	private IXmzTaskguideConfigService service;  
    /**
     * 事项指南配置表实体对象
     */
  	private XmzTaskguideConfig dataBean=null;
  
  	@Autowired
  	private IAuditOrgaArea iAuditOrgaArea;
  	private List<AuditOrgaArea> areaList ;
  	private List<AuditTask> taskList ;
  	/**
     * 附件上传
     */
    private FileUploadModel9 fileUploadModel1;
  	private String guidecliengguid ="";
    public void pageLoad()
    {
        dataBean=new XmzTaskguideConfig();
        guidecliengguid =getViewData("guidecliengguid");
        if(StringUtil.isBlank(guidecliengguid)){
            guidecliengguid =UUID.randomUUID().toString();
            addViewData("guidecliengguid", guidecliengguid);
        }
    }

    /**
     * 保存并关闭
     * 
     */
	public void add()
    {
        dataBean.setRowguid( UUID.randomUUID().toString());
        dataBean.setOperatedate(new Date());
        dataBean.setOperateusername(userSession.getDisplayName());
        dataBean.setCreatedate(new Date());
        dataBean.setGuidecliengguid(getViewData("guidecliengguid"));
	    service.insert(dataBean);
	    addCallbackParam("msg", l("保存成功！"));
	    dataBean = null;
	}

    /**
     * 保存并新建
     * 
     */
	public void addNew() {
		add();
		guidecliengguid =UUID.randomUUID().toString();
        addViewData("guidecliengguid", guidecliengguid);
		dataBean = new XmzTaskguideConfig();
	}

    public List<AuditOrgaArea> getAreaList() {
        if (areaList == null) {
            SqlConditionUtil sql = new SqlConditionUtil();
            sql.in("CityLevel", "'1','2'");
            areaList = iAuditOrgaArea.selectAuditAreaList(sql.getMap()).getResult();
        }
        return areaList;
    }
    public List<AuditTask> getTaskList() {
        //IS_ENABLE=1,IS_HISTORY=0,IS_EDITAFTERIMPORT=1
        if (StringUtil.isNotBlank(dataBean.getAreacode())) {
            taskList = service.selectTaskList(dataBean.getAreacode());
        }
        return taskList;
    }
    
    //上传控件
    public FileUploadModel9 getFileUploadModel() {
        if (fileUploadModel1 == null) {
            // DefaultFileUploadHandlerImpl9参数为：clientGuid，clientTag，clientInfo，AttachHandler9，attachHandler，userGuid，userName
            guidecliengguid =getViewData("guidecliengguid");
            if(StringUtil.isBlank(guidecliengguid)){
                guidecliengguid =UUID.randomUUID().toString();
                addViewData("guidecliengguid", guidecliengguid);
            }
            fileUploadModel1 = new FileUploadModel9(
                    new DefaultFileUploadHandlerImpl9(guidecliengguid, null, null, null, userSession.getUserGuid(), userSession.getDisplayName()));
        }
        // 该属性设置他为只读，不能被删除
        // fileUploadModel1.setReadOnly("true");
        return fileUploadModel1;
    }
    public XmzTaskguideConfig getDataBean()
    {
        if(dataBean==null)
        {
          dataBean = new XmzTaskguideConfig();
        }
        return dataBean;
    }

    public void setDataBean(XmzTaskguideConfig dataBean)
    {
        this.dataBean = dataBean;
    }

}
