package com.epoint.jntongji.action;

import java.util.ArrayList;
import java.util.List;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;
import com.epoint.basic.auditorga.auditwindow.domain.AuditOrgaWindow;
import com.epoint.basic.bizlogic.mis.CommonService;
import com.epoint.basic.controller.BaseController;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.string.StringUtil;


@RestController("jnusermanyiechartsAction")
@Scope("request")
public class JNusermanyiechartsAction extends BaseController
{
    private static final long serialVersionUID = 1L;
    private String startdate;
    private String enddate;
    private String WindowList;
    //窗口查询
    private List<SelectItem> windownameModel = null;
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

  
    
    @Override
    public void pageLoad() {

    }

    public Record getEvaluate(String ouguid,String startdate,String enddate){
        String centerguid=ZwfwUserSession.getInstance().getCenterGuid();
    	   String strsql =  "SELECT"
                   + " SUM(case when satisfied = '1' THEN 1 else 0 END) AS fcmanyi,"
                   + " SUM(case when satisfied = '2' THEN 1 else 0 END) AS manyi,"
                   + " SUM(case when satisfied = '3' THEN 1 else 0 END) AS jbmanyi,"
                   + " SUM(case when satisfied = '4' THEN 1 else 0 END) AS bumanyi,"
                   + " SUM(case when satisfied = '5' THEN 1 else 0 END) AS fcbumanyi,"
                   + " SUM(case when satisfied = '0' THEN 1 else 0 END) AS mrmanyi"
                   + " from audit_online_evaluat e INNER JOIN audit_project p"
                   + " on p.RowGuid = e.ClientIdentifier where clienttype =10"
                   + " and ACCEPTUSERGUID is NOT NULL and CENTERGUID='"+centerguid+"'";
    	  if (StringUtil.isNotBlank(ouguid) && !"all".equals(ouguid)) {
               strsql += " and ouguid='"+ouguid+"'";
           }
          if (StringUtil.isNotBlank(startdate)) {
              strsql += " and ACCEPTUSERDATE>=str_to_date('" + startdate + "', '%Y-%m-%d %H:%i:%s')";
          }
          
          if (StringUtil.isNotBlank(enddate)) {
              strsql += " and ACCEPTUSERDATE<=str_to_date('" + enddate + "', '%Y-%m-%d %H:%i:%s')";
          }
          
         Record list= commservice.find(strsql, Record.class);
        return list;
    }
    


	public String getWindowList() {
		return WindowList;
	}

	public void setWindowList(String windowList) {
		WindowList = windowList;
	}


	public String getStartdate() {
		return startdate;
	}



	public void setStartdate(String startdate) {
		this.startdate = startdate;
	}



	public String getEnddate() {
		return enddate;
	}



	public void setEnddate(String enddate) {
		this.enddate = enddate;
	}





}
