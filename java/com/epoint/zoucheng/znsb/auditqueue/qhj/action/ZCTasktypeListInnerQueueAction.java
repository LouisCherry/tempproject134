package com.epoint.zoucheng.znsb.auditqueue.qhj.action;

import com.epoint.basic.auditorga.auditwindow.domain.AuditOrgaWindow;
import com.epoint.basic.auditorga.auditwindow.inter.IAuditOrgaWindowYjs;
import com.epoint.basic.auditqueue.auditqueue.inter.IAuditQueue;
import com.epoint.basic.auditqueue.auditqueuetasktype.domain.AuditQueueTasktype;
import com.epoint.basic.auditqueue.auditqueuetasktype.inter.IAuditQueueTasktype;
import com.epoint.basic.auditqueue.auditqueuewindowtasktype.domain.AuditQueueWindowTasktype;
import com.epoint.basic.auditqueue.auditqueuewindowtasktype.inter.IAuditQueueWindowTasktype;
import com.epoint.basic.auditqueue.auditqueuexianhaotime.domain.AuditQueueXianhaotime;
import com.epoint.basic.auditqueue.auditqueuexianhaotime.inter.IAuditQueueXianhaotime;
import com.epoint.basic.auditqueue.auditznsbequipment.domain.AuditZnsbEquipment;
import com.epoint.basic.auditqueue.auditznsbequipment.inter.IAuditZnsbEquipment;
import com.epoint.basic.controller.BaseController;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.composite.auditqueue.handlequeue.inter.IHandleQueue;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.json.JsonUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.organ.ou.api.IOuService;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

@RestController("zctasktypelistinnerqueueaction")
@Scope("request")
public class ZCTasktypeListInnerQueueAction extends BaseController {
	private static final long serialVersionUID = 1L;
	@Autowired
	private IAuditQueueTasktype tasktypeservice;
	@Autowired
	private IAuditOrgaWindowYjs windowservice;
	@Autowired
	private IAuditQueueWindowTasktype queuewindowservice;
	@Autowired
	private IHandleQueue handlequeueservice;
	@Autowired
	private IAuditQueueXianhaotime xianhaoservice;
	@Autowired
	private IAuditZnsbEquipment equipmentservice;
	@Autowired
	private IAuditQueue queueservice;
	@Autowired
	private IOuService ouservice;
	private String ouguid;
	private String Centerguid;
	private String hallguid;
	private String largeTasktypeGuid;
	private Calendar c = Calendar.getInstance();

	public void pageLoad() {
		this.ouguid = this.getRequestParameter("ouguid");
		this.Centerguid = this.getRequestParameter("Centerguid");
		this.hallguid = this.getRequestParameter("hallguid");
		this.largeTasktypeGuid = this.getRequestParameter("largetasktypeguid");
		Integer total;
		int pages;
		if (StringUtil.isNotBlank(this.largeTasktypeGuid)) {
			total = (Integer) this.tasktypeservice.getCountbyLargeTasktypeGuid(this.largeTasktypeGuid).getResult();
			pages = total % 10 == 0 ? total / 10 : total / 10 + 1;
			this.addCallbackParam("pages", pages);
		} else if (StringUtil.isNotBlank(this.ouguid)) {
			total = getCountbyOUGuid(ouguid, Centerguid, hallguid);
			pages = total % 10 == 0 ? total / 10 : total / 10 + 1;
			this.addCallbackParam("pages", pages);
		} else {
			this.addCallbackParam("pages", this.getTaskTypePagesbyHallguid(this.hallguid, this.Centerguid));
		}

	}

	public int getTaskTypePagesbyHallguid(String hallguid, String centerguid) {
		SqlConditionUtil sql = new SqlConditionUtil();
		sql.eq("centerguid", centerguid);
		if (!"all".equals(hallguid)) {
			sql.eq("lobbytype", hallguid);
		}

		sql.eq("IS_USEQUEUE", "1");
		List<AuditOrgaWindow> windows =  this.windowservice.getAllWindow(sql.getMap()).getResult();
		String windowguids = "";

		AuditOrgaWindow auditOrgaWindow;
		for (Iterator<AuditOrgaWindow> var6 = windows.iterator(); var6
				.hasNext(); windowguids = windowguids + auditOrgaWindow.getRowguid() + "','") {
			auditOrgaWindow = (AuditOrgaWindow) var6.next();
		}

		windowguids = "'" + windowguids + "'";
		SqlConditionUtil sql1 = new SqlConditionUtil();
		sql1.in("windowguid", windowguids);
		List<AuditQueueWindowTasktype> queuewindows =  this.queuewindowservice.getAllWindowTasktype(sql1.getMap())
				.getResult();
		String tasktypeguids = "";

		AuditQueueWindowTasktype auditQueueWindowTasktype;
		for (Iterator<AuditQueueWindowTasktype> var9 = queuewindows.iterator(); var9
				.hasNext(); tasktypeguids = tasktypeguids + auditQueueWindowTasktype.getTasktypeguid() + "','") {
			auditQueueWindowTasktype = (AuditQueueWindowTasktype) var9.next();
		}

		tasktypeguids = "'" + tasktypeguids + "'";
		SqlConditionUtil sql2 = new SqlConditionUtil();
		sql2.in("rowguid", tasktypeguids);
		sql2.eq("centerguid", centerguid);
		Integer total = this.tasktypeservice.getAuditQueueTasktypeCount(sql2.getMap(), " rowguid,TaskTypeName");
		return total % 10 == 0 ? total / 10 : total / 10 + 1;
	}

	public void getDataJson() {
		if (StringUtil.isNotBlank(this.largeTasktypeGuid)) {
			this.addCallbackParam("html", this.getTaskTypebyLargeTaskGuid(this.largeTasktypeGuid,
					Integer.parseInt(this.getRequestParameter("cur"))));
		} else if (StringUtil.isNotBlank(this.ouguid)) {
			this.addCallbackParam("html", this.getTaskTypePagesDatabyOuguid("department", this.ouguid, this.Centerguid,
					Integer.parseInt(this.getRequestParameter("cur")),this.hallguid));
		} else {
			this.addCallbackParam("html", this.getTaskTypePagesDatabyHallguid("Centerguid", this.hallguid,
					this.Centerguid, Integer.parseInt(this.getRequestParameter("cur"))));
		}

	}

	private String getTaskTypebyLargeTaskGuid(String largeTasktypeGuid, int curPage) {
		String taskwaitnum = "0";
		SqlConditionUtil sql = new SqlConditionUtil();
		sql.eq("largetasktypeguid", largeTasktypeGuid);
		List<AuditQueueTasktype> lists =  this.tasktypeservice
				.getAuditQueueTasktypeByPage("rowguid,tasktypename", sql.getMap(), Integer.valueOf(curPage) * 10, 10,
						"ordernum", "desc")
				.getResult().getList();
		if (null != lists && lists.size() > 0) {
			Iterator<AuditQueueTasktype> var6 = lists.iterator();

			while (var6.hasNext()) {
				AuditQueueTasktype tasktype = (AuditQueueTasktype) var6.next();
				taskwaitnum = StringUtil.getNotNullString(
						this.handlequeueservice.getTaskWaitNum(tasktype.getRowguid(), true).getResult());
				tasktype.put("taskwaitnum", taskwaitnum);
			}
		}

		String json = JsonUtil.listToJson(lists);
		return json;
	}

	public String getTaskTypePagesDatabyHallguid(String cate, String hallguid, String centerguid, int curPage) {
		SqlConditionUtil sql = new SqlConditionUtil();
		sql.eq("centerguid", centerguid);
		if (!"all".equals(hallguid)) {
			sql.eq("lobbytype", hallguid);
		}

		sql.eq("IS_USEQUEUE", "1");
		List<AuditOrgaWindow> windows = this.windowservice.getAllWindow(sql.getMap()).getResult();
		String windowguids = "";

		AuditOrgaWindow auditOrgaWindow;
		for (Iterator<AuditOrgaWindow> var8 = windows.iterator(); var8
				.hasNext(); windowguids = windowguids + auditOrgaWindow.getRowguid() + "','") {
			auditOrgaWindow = (AuditOrgaWindow) var8.next();
		}

		windowguids = "'" + windowguids + "'";
		SqlConditionUtil sql1 = new SqlConditionUtil();
		sql1.in("windowguid", windowguids);
		List<AuditQueueWindowTasktype> queuewindows =  this.queuewindowservice.getAllWindowTasktype(sql1.getMap())
				.getResult();
		String tasktypeguids = "";

		AuditQueueWindowTasktype auditQueueWindowTasktype;
		for (Iterator<AuditQueueWindowTasktype> var11 = queuewindows.iterator(); var11
				.hasNext(); tasktypeguids = tasktypeguids + auditQueueWindowTasktype.getTasktypeguid() + "','") {
			auditQueueWindowTasktype = (AuditQueueWindowTasktype) var11.next();
		}

		tasktypeguids = "'" + tasktypeguids + "'";
		SqlConditionUtil sql2 = new SqlConditionUtil();
		sql2.in("rowguid", tasktypeguids);
		sql2.eq("centerguid", centerguid);
		String taskwaitnum = "0";
		if ("theme".equals(cate)) {
			return null;
		} else {
			List<AuditQueueTasktype> lists =  this.tasktypeservice
					.getAuditQueueTasktypeByPage("rowguid,TaskTypeName", sql2.getMap(), Integer.valueOf(curPage) * 10,
							10, "ordernum", "desc")
					.getResult().getList();
			Iterator<AuditQueueTasktype> var14 = lists.iterator();

			while (var14.hasNext()) {
				AuditQueueTasktype list = (AuditQueueTasktype) var14.next();
				taskwaitnum = StringUtil
						.getNotNullString(this.handlequeueservice.getTaskWaitNum(list.getRowguid(), true).getResult());
				list.set("taskwaitnum", taskwaitnum);
			}

			String json = JsonUtil.listToJson(lists);
			return json;
		}
	}
	
	
	public int getCountbyOUGuid(String ouguid, String centerguid,String hallguid){
	    int total = 0;
	    if(StringUtil.isNotBlank(hallguid) && !"all".equals(hallguid)){
	        //获取当前大厅下部门以及窗口
            SqlConditionUtil sql = new SqlConditionUtil();
            sql.eq("centerguid", centerguid);
            sql.eq("lobbytype", hallguid);
            sql.eq("ouguid", ouguid);
            sql.eq("IS_USEQUEUE", "1");
            List<AuditOrgaWindow> windows = this.windowservice.getAllWindow(sql.getMap()).getResult();
            String windowguids = "";

            AuditOrgaWindow auditOrgaWindow;
            for (Iterator<AuditOrgaWindow> var8 = windows.iterator(); var8
                    .hasNext(); windowguids = windowguids + auditOrgaWindow.getRowguid() + "','") {
                auditOrgaWindow = (AuditOrgaWindow) var8.next();
            }

            windowguids = "'" + windowguids + "'";
            SqlConditionUtil sql1 = new SqlConditionUtil();
            sql1.in("windowguid", windowguids);
            List<AuditQueueWindowTasktype> queuewindows =  this.queuewindowservice.getAllWindowTasktype(sql1.getMap())
                    .getResult();
            String tasktypeguids = "";

            AuditQueueWindowTasktype auditQueueWindowTasktype;
            for (Iterator<AuditQueueWindowTasktype> var11 = queuewindows.iterator(); var11
                    .hasNext(); tasktypeguids = tasktypeguids + auditQueueWindowTasktype.getTasktypeguid() + "','") {
                auditQueueWindowTasktype = (AuditQueueWindowTasktype) var11.next();
            }
            tasktypeguids = "'" + tasktypeguids + "'";
            
            SqlConditionUtil sql2 = new SqlConditionUtil();
            sql2.in("rowguid", tasktypeguids);
            sql2.eq("centerguid", centerguid);
            List<AuditQueueTasktype> lists =  this.tasktypeservice.getAllTasktype(sql2.getMap()).getResult();

            total = lists.size();
	    }else{
	        total =  this.tasktypeservice.getCountbyOUGuid(this.ouguid, this.Centerguid).getResult();
	    }
	    
	    return total;
	}

	public String getTaskTypePagesDatabyOuguid(String cate, String ouguid, String centerguid, int curPage,String hallguid) {
		String taskwaitnum = "0";
		if ("theme".equals(cate)) {
			return null;
		} else {
		    if(StringUtil.isNotBlank(hallguid) && !"all".equals(hallguid)){
		        //获取当前大厅下部门以及窗口
		        SqlConditionUtil sql = new SqlConditionUtil();
		        sql.eq("centerguid", centerguid);
		        sql.eq("lobbytype", hallguid);
		        sql.eq("ouguid", ouguid);
		        sql.eq("IS_USEQUEUE", "1");
		        List<AuditOrgaWindow> windows = this.windowservice.getAllWindow(sql.getMap()).getResult();
		        String windowguids = "";

		        AuditOrgaWindow auditOrgaWindow;
		        for (Iterator<AuditOrgaWindow> var8 = windows.iterator(); var8
		                .hasNext(); windowguids = windowguids + auditOrgaWindow.getRowguid() + "','") {
		            auditOrgaWindow = (AuditOrgaWindow) var8.next();
		        }

		        windowguids = "'" + windowguids + "'";
		        SqlConditionUtil sql1 = new SqlConditionUtil();
		        sql1.in("windowguid", windowguids);
		        List<AuditQueueWindowTasktype> queuewindows =  this.queuewindowservice.getAllWindowTasktype(sql1.getMap())
		                .getResult();
		        String tasktypeguids = "";

		        AuditQueueWindowTasktype auditQueueWindowTasktype;
		        for (Iterator<AuditQueueWindowTasktype> var11 = queuewindows.iterator(); var11
		                .hasNext(); tasktypeguids = tasktypeguids + auditQueueWindowTasktype.getTasktypeguid() + "','") {
		            auditQueueWindowTasktype = (AuditQueueWindowTasktype) var11.next();
		        }

		        tasktypeguids = "'" + tasktypeguids + "'";
		        
		        SqlConditionUtil sql2 = new SqlConditionUtil();
		        sql2.in("rowguid", tasktypeguids);
		        sql2.eq("centerguid", centerguid);
		        List<AuditQueueTasktype> lists =  this.tasktypeservice
	                    .getAuditQueueTasktypeByPage("rowguid,TaskTypeName", sql2.getMap(), Integer.valueOf(curPage) * 10,
	                            10, "ordernum", "desc")
	                    .getResult().getList();
	            Iterator<AuditQueueTasktype> var14 = lists.iterator();

	            while (var14.hasNext()) {
	                AuditQueueTasktype list = (AuditQueueTasktype) var14.next();
	                taskwaitnum = StringUtil
	                        .getNotNullString(this.handlequeueservice.getTaskWaitNum(list.getRowguid(), true).getResult());
	                list.set("taskwaitnum", taskwaitnum);
	            }
	            String json = JsonUtil.listToJson(lists);
	            return json;
		        
		    }else{
		          List<Record> lists =  this.tasktypeservice
		                    .getAuditQueueTasktypeByPage(ouguid, centerguid, Integer.valueOf(curPage) * 10, 10).getResult();
		            Iterator<Record> var7 = lists.iterator();

		            while (var7.hasNext()) {
		                Record list = (Record) var7.next();
		                taskwaitnum = StringUtil.getNotNullString(
		                        this.handlequeueservice.getTaskWaitNum((String) list.get("rowguid"), true).getResult());
		                list.put("taskwaitnum", taskwaitnum);
		            }
		            String json = JsonUtil.listToJson(lists);
		            return json;
		    }
		}
	}

	public void checkIsclick(String taskname, String taskguid, String waitCount, String MacAddress) {
		String msg = "";
		int leftpaperpiece = Integer.valueOf(this.getleftPiece(MacAddress));
		if (leftpaperpiece <= 0) {
			msg = "剩余纸张数量为0，请联系管理员";
		} else if (StringUtil.isNotBlank(taskguid)) {
			int week = this.c.get(7) - 1;
			AuditQueueXianhaotime xianhaotime = null;
			xianhaotime = (AuditQueueXianhaotime) this.xianhaoservice
					.getDetailbyTasktypeguidandweek(String.valueOf(week), taskguid, this.Centerguid).getResult();
			if (xianhaotime != null) {
				msg = this.xianhaoBytime(xianhaotime, taskguid);
			} else {
				xianhaotime = (AuditQueueXianhaotime) this.xianhaoservice
						.getDetailbyTasktypeguidandweek(String.valueOf(week), "commonguid", this.Centerguid)
						.getResult();
				if (xianhaotime != null) {
					msg = this.xianhaoBytime(xianhaotime, taskguid);
				} else {
					AuditQueueTasktype tasktype = (AuditQueueTasktype) this.tasktypeservice.getTasktypeByguid(taskguid)
							.getResult();
					if (tasktype != null && StringUtil.isNotBlank(tasktype.getXianhaonum())
							&& (Integer) this.queueservice.getCountByTaskGuid(taskguid).getResult() >= Integer
									.parseInt(tasktype.getXianhaonum())) {
						msg = "该事项今日取号数已达上限，无法取号！";
					}
				}
			}
		}

		this.addCallbackParam("msg", msg);
		this.addCallbackParam("taskname", taskname);
		this.addCallbackParam("taskguid", taskguid);
		this.addCallbackParam("waitCount", waitCount);
	}

	public void getOUName(String OUGuid) {
		this.addCallbackParam("ouname",
				StringUtil.isNotBlank(this.ouservice.getOuByOuGuid(OUGuid).getOushortName())
						? this.ouservice.getOuByOuGuid(OUGuid).getOushortName()
						: this.ouservice.getOuByOuGuid(OUGuid).getOuname());
	}

	public int getleftPiece(String MacAddress) {
		int leftpaperpiece = 0;
		AuditZnsbEquipment equipment = (AuditZnsbEquipment) this.equipmentservice
				.getDetailbyMacaddress(MacAddress, " leftpaperpiece ").getResult();
		if (StringUtil.isNotBlank(equipment)) {
			leftpaperpiece = StringUtil.isBlank(equipment.getLeftpaperpiece()) ? 0 : equipment.getLeftpaperpiece();
		}

		return leftpaperpiece;
	}


    public Map<String, String> getPiece(String MacAddress) {
        Map<String, String> rtnMap = new HashMap<String, String>(16);
        double alertlength = 0.0;
        int leftpaperpiece = 0;
        String isBlink = "no";

        AuditZnsbEquipment equipment = equipmentservice
                .getDetailbyMacaddress(MacAddress, " Alertlength,Leftpaperpiece ").getResult();
        if (StringUtil.isNotBlank(equipment)) {
            alertlength = StringUtil.isBlank(equipment.getAlertlength()) ? 0.0 : equipment.getAlertlength();
            leftpaperpiece = StringUtil.isBlank(equipment.getLeftpaperpiece()) ? 0 : equipment.getLeftpaperpiece();

            if (alertlength >= leftpaperpiece) {
                isBlink = "is";
            }
            if (leftpaperpiece == 0) {
                isBlink = "red";
            }
            rtnMap.put("leftpaperpiece", String.valueOf(leftpaperpiece));
            rtnMap.put("alertlength", String.valueOf((int) alertlength));
            rtnMap.put("isBlink", isBlink);
        }
        return rtnMap;
    }

	public String xianhaoBytime(AuditQueueXianhaotime xianhaotime, String taskguid) {
		String msg = "";
		int year = this.c.get(1);
		int month = this.c.get(2) + 1;
		int date = this.c.get(5);
		String amstart = year + "-" + month + "-" + date + " " + xianhaotime.getAmstart();
		String amend = year + "-" + month + "-" + date + " " + xianhaotime.getAmend();
		String pmstart = year + "-" + month + "-" + date + " " + xianhaotime.getPmstart();
		String pmend = year + "-" + month + "-" + date + " " + xianhaotime.getPmend();

		try {
			long ams = (new SimpleDateFormat("yyyy-MM-dd HH:mm")).parse(amstart).getTime();
			long ame = (new SimpleDateFormat("yyyy-MM-dd HH:mm")).parse(amend).getTime();
			long pms = (new SimpleDateFormat("yyyy-MM-dd HH:mm")).parse(pmstart).getTime();
			long pme = (new SimpleDateFormat("yyyy-MM-dd HH:mm")).parse(pmend).getTime();
			long now = System.currentTimeMillis();
			if ((now <= ams || now >= ame) && (now <= pms || now >= pme)) {
				msg = "时间段" + xianhaotime.getAmstart() + "至" + xianhaotime.getAmend() + "以及" + xianhaotime.getPmstart()
						+ "至" + xianhaotime.getPmend() + "外无法取号";
			} else if (StringUtil.isNotBlank(taskguid)) {
				AuditQueueTasktype tasktype = (AuditQueueTasktype) this.tasktypeservice.getTasktypeByguid(taskguid)
						.getResult();
				if (tasktype != null && StringUtil.isNotBlank(tasktype.getXianhaonum())
						&& (Integer) this.queueservice.getCountByTaskGuid(taskguid).getResult() >= Integer
								.parseInt(tasktype.getXianhaonum())) {
					msg = "该事项今日取号数已达上限，无法取号！";
				}
			}
		} catch (ParseException var22) {
			msg = "请检查相关限号时间格式是否正确！";
		}

		return msg;
	}
}