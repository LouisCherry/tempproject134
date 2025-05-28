package com.epoint.jntongji.action;

import java.util.ArrayList;
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

import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.evainstance.entity.Evainstance;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;

@RestController("jnusermanyidulistwindowAction2")
@Scope("request")
public class JNusermanyidulistwindowAction2 extends BaseController
{
    private static final long serialVersionUID = 1L;
    private DataGridModel<Evainstance> modelall;
    private ExportModel exportModel;
    private String windowguid;
    private String satisfied;
    private String WindowList;
    private String manyiList;
    private String isadmin;
    //窗口查询
    private List<SelectItem> windownameModel = null;
    private List<SelectItem> manyiModel = null;
    private CommonService commservice = new CommonService();

    public List<SelectItem> getWindowNameModel() {
        if (windownameModel == null) {
            windownameModel = new ArrayList<SelectItem>();
            String areaCode = ZwfwUserSession.getInstance().getAreaCode();
            String sql = "select proDepart,deptCode from evainstance where 1=1 ";
            if (StringUtil.isBlank(isadmin)) {
            	sql += " and areaCode ='"+areaCode+"' ";
            }
            sql +=  " group by proDepart";
            List<Evainstance> list = commservice.findList(sql, Evainstance.class, areaCode);
            for (Evainstance eva : list) {
                windownameModel.add(new SelectItem(eva.getStr("deptCode"), eva.getStr("proDepart")));
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
    	this.setIsadmin(getRequestParameter("isadmin"));
        this.WindowList = getRequestParameter("WindowList");
        this.manyiList = this.getRequestParameter("manyiList");
    }

    public DataGridModel<Evainstance> getDataGridDataTask() {
        // 获得表格对象
        if (modelall == null) {
            modelall = new DataGridModel<Evainstance>()
            {
                @Override
                public List<Evainstance> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    String strsql =  " SELECT e.RowGuid,userName,ProjectNO,e.Taskname,proDepart,deptCode,satisfaction,acceptDate,Createdate "
                    		+ " FROM evainstance e where 1=1 ";
                    if (StringUtil.isNotBlank(WindowList) && !"all".equals(WindowList)) {
                    	strsql += " and deptCode='"+WindowList+"'";
                    }
                    if (StringUtil.isBlank(isadmin)) {
                    	String areaCode = ZwfwUserSession.getInstance().getAreaCode();
                    	strsql += " and areaCode ='"+areaCode+"' " ;
                    }
                    
                    if (StringUtil.isNotBlank(manyiList) && !"all".equals(manyiList)) {
                        strsql += " and satisfaction='"+manyiList+"'";
                    }
                    strsql +=   " ORDER BY Createdate DESC";

                    List<Evainstance> list = commservice.findList(strsql, first, pageSize, Evainstance.class);
                    String sql = " select flowsn,WINDOWNAME,ACCEPTUSERNAME from audit_project where flowsn =?1";
                    for (Evainstance evainstance : list) {
                    	AuditProject project = commservice.find(sql, AuditProject.class, evainstance.getStr("ProjectNO"));
                    	if(project!=null) {
                    		evainstance.put("WINDOWNAME", project.getWindowname());
                    		evainstance.put("ACCEPTUSERNAME", project.getAcceptusername());
                    	}
					}
                    List<Evainstance> list1 = commservice.findList(strsql, Evainstance.class);
                    this.setRowCount(list1.size());
                    return list;

                }
            };
        }
        return modelall;
    }

    public ExportModel getExportModel() {
       if (exportModel == null) {
            exportModel = new ExportModel("ProjectNO,Taskname,ACCEPTUSERNAME,WINDOWNAME,proDepart,Createdate,satisfaction",
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

	public String getIsadmin() {
		return isadmin;
	}

	public void setIsadmin(String isadmin) {
		this.isadmin = isadmin;
	}





}
