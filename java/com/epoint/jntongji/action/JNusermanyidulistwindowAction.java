package com.epoint.jntongji.action;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.auditorga.auditwindow.domain.AuditOrgaWindow;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.bizlogic.mis.CommonService;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.export.ExportModel;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.frame.service.organ.ou.entity.FrameOu;

@RestController("jnusermanyidulistwindowAction")
@Scope("request")
public class JNusermanyidulistwindowAction extends BaseController
{
    private static final long serialVersionUID = 1L;
    private DataGridModel<AuditProject> modelall;
    private ExportModel exportModel;
    private String windowguid;
    private String satisfied;
    private String WindowList;
    private String manyiList;
    //窗口查询
    private List<SelectItem> windownameModel = null;
    private List<SelectItem> manyiModel = null;
    private CommonService commservice = new CommonService();

    public List<SelectItem> getWindowNameModel() {
        if (windownameModel == null) {
            windownameModel = new ArrayList<SelectItem>();
            String centerguid=ZwfwUserSession.getInstance().getCenterGuid();
            String sql = "select RowGuid,WINDOWNAME,WINDOWNO from audit_orga_window " + " where CenterGuid=?"
                    + " order by WINDOWNO desc";
            List<AuditOrgaWindow> list = commservice.findList(sql, AuditOrgaWindow.class,centerguid);
            for (AuditOrgaWindow window : list) {
                windownameModel.add(new SelectItem(window.getRowguid(), window.getWindowname()));
            }
            windownameModel.add(0, new SelectItem("all", "所有窗口"));
        }
        return this.windownameModel;
    }

    @SuppressWarnings("unchecked")
	public List<SelectItem> getmanyiModel() {
        if (manyiModel == null) {
        	manyiModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "好差评满意度层级", null, false));
            }
          manyiModel.add(0, new SelectItem("all", "所有状态"));
          return this.manyiModel;
    }


    
    @Override
    public void pageLoad() {

        this.WindowList = getRequestParameter("WindowList");
        this.manyiList = this.getRequestParameter("manyiList");
    }

    public DataGridModel<AuditProject> getDataGridDataTask() {
        // 获得表格对象
        if (modelall == null) {
            modelall = new DataGridModel<AuditProject>()
            {
                @Override
                public List<AuditProject> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    String centerguid=ZwfwUserSession.getInstance().getCenterGuid();
                    String strsql =  " SELECT ouname,RECEIVEUSERNAME,ACCEPTUSERGUID,WINDOWNAME,PROJECTNAME,FLOWSN,Evaluatedate,(case when satisfied = '0' THEN 2 else satisfied END) satisfied" 
                                   + " from audit_online_evaluat e INNER JOIN audit_project p"
                                   + " on p.RowGuid = e.ClientIdentifier where clienttype in (10,20)"
                                   + " and ACCEPTUSERGUID is NOT NULL and CENTERGUID='"+centerguid+"'";
                    String strsql1 =  " SELECT count(1) as total" 
                            + " from audit_online_evaluat e INNER JOIN audit_project p"
                            + " on p.RowGuid = e.ClientIdentifier where clienttype in (10,20)"
                            + " and ACCEPTUSERGUID is NOT NULL and CENTERGUID='"+centerguid+"'";
                    
                    if (StringUtil.isNotBlank(WindowList) && !"all".equals(WindowList)) {
                        strsql += " and WINDOWGUID='"+WindowList+"'";
                        strsql1 += " and WINDOWGUID='"+WindowList+"'";
                    }
                    
                    if (StringUtil.isNotBlank(manyiList) && !"all".equals(manyiList)) {
                        strsql += " and satisfied='"+manyiList+"'";
                        strsql1 += " and satisfied='"+manyiList+"'";
                    }
                    strsql +=   " ORDER BY e.evaluatedate DESC";

                    List<AuditProject> list = commservice.findList(strsql, first, pageSize, AuditProject.class);
                   	int total = new CommonDao().queryInt(strsql1, Integer.class);
                    this.setRowCount(total);
                    return list;

                }
            };
        }
        return modelall;
    }

    public ExportModel getExportModel() {
       if (exportModel == null) {
            exportModel = new ExportModel("FLOWSN,PROJECTNAME,RECEIVEUSERNAME,WINDOWNAME,ouname,Evaluatedate,satisfied",
                    "流水号,办理业务,被评价人,被评价窗口,部门,评价时间,满意度");
        }
        return exportModel;
    }

	public String getWindowguid() {
		return windowguid;
	}

	public void setWindowguid(String windowguid) {
		this.windowguid = windowguid;
	}

	public String getSatisfied() {
		return satisfied;
	}

	public void setSatisfied(String satisfied) {
		this.satisfied = satisfied;
	}

	public String getWindowList() {
		return WindowList;
	}




	public void setWindowList(String windowList) {
		WindowList = windowList;
	}




	public String getManyiList() {
		return manyiList;
	}




	public void setManyiList(String manyiList) {
		this.manyiList = manyiList;
	}





}
