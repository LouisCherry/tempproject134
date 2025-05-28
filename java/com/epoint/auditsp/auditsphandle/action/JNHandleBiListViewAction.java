package com.epoint.auditsp.auditsphandle.action;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.EpointKeyNames9;
import com.epoint.basic.auditorga.auditworkingday.inter.IAuditOrgaWorkingDay;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.auditproject.auditprojectsparetime.domain.AuditProjectSparetime;
import com.epoint.basic.auditproject.auditprojectsparetime.inter.IAuditProjectSparetime;
import com.epoint.basic.auditresource.auditrsitembaseinfo.domain.AuditRsItemBaseinfo;
import com.epoint.basic.auditresource.auditrsitembaseinfo.inter.IAuditRsItemBaseinfo;
import com.epoint.basic.auditsp.auditspbasetask.domain.AuditSpBasetask;
import com.epoint.basic.auditsp.auditspbasetask.inter.IAuditSpBasetask;
import com.epoint.basic.auditsp.auditspbasetaskr.domain.AuditSpBasetaskR;
import com.epoint.basic.auditsp.auditspbasetaskr.inter.IAuditSpBasetaskR;
import com.epoint.basic.auditsp.auditspbusiness.domain.AuditSpBusiness;
import com.epoint.basic.auditsp.auditspbusiness.inter.IAuditSpBusiness;
import com.epoint.basic.auditsp.auditspinstance.domain.AuditSpInstance;
import com.epoint.basic.auditsp.auditspinstance.inter.IAuditSpInstance;
import com.epoint.basic.auditsp.auditspisubapp.domain.AuditSpISubapp;
import com.epoint.basic.auditsp.auditspisubapp.inter.IAuditSpISubapp;
import com.epoint.basic.auditsp.auditspitask.domain.AuditSpITask;
import com.epoint.basic.auditsp.auditspitask.inter.IAuditSpITask;
import com.epoint.basic.auditsp.auditspphase.domain.AuditSpPhase;
import com.epoint.basic.auditsp.auditspphase.inter.IAuditSpPhase;
import com.epoint.basic.auditsp.auditsptask.domain.AuditSpTask;
import com.epoint.basic.auditsp.auditsptask.inter.IAuditSpTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.tree.TreeFunction9;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.json.JsonUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.frame.service.organ.ou.api.IOuService;

/**
 * 申报查询列表页面对应的后台
 * @author Administrator
 *
 */
@RestController("jnhandlebilistviewaction")
@Scope("request")
public class JNHandleBiListViewAction extends BaseController
{
    /**
     * 
     */
    private static final long serialVersionUID = 8713148757403132395L;

    private AuditSpBusiness dataBean;

    /**
     * 表格控件model
     */
    private DataGridModel<AuditSpInstance> model;

    /**
     * 建设单位
     */
    private String applyername;

    /**
     * 申报名称
     */
    private String itemname;

    @Autowired
    private IAuditSpInstance spInstanceService;

    @Autowired
    private IAuditRsItemBaseinfo rsItemBaseinfoService;

    @Autowired
    private IAuditSpBusiness spBusinessService;
    
    @Autowired
    private IAuditSpPhase spphaseservice;
    
    @Autowired
    private IAuditSpISubapp subappservice;
    
    
    @Autowired 
    private IAuditProject projectservice;
    
    
    @Autowired 
    private IAuditSpTask iauditsptask;
    
    @Autowired 
    private IAuditSpITask iauditspitask;
    
    @Autowired 
    private IAuditSpBasetask iauditspbasetask;

    @Autowired 
    private IAuditSpBasetaskR iauditspbasetaskr;
    
    
    @Autowired 
    private IAuditTask iaudittask;
    
    @Autowired 
    private IAuditProjectSparetime iauditprojectsparetime;
    
    
    @Autowired 
    private IOuService ouservice;
    
    @Autowired
    private IAuditOrgaWorkingDay iauditorgaworkingday;
    /**
     * 项目类型代码项
     */
    private List<SelectItem> itemTypebModel;

    private String itemtype;

    @Override
    public void pageLoad() {
    
    }

    /**
     * 删除选定
     * 
     */
    public void deleteSelect() {
        List<String> guids = getDataGridData().getSelectKeys();
        for (String guid : guids) {
            spInstanceService.deleteAuditSpInstanceByGuid(guid);
        }
        addCallbackParam("msg", "删除成功！");
    }

    public DataGridModel<AuditSpInstance> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<AuditSpInstance>()
            {
                /**
                 * 
                 */
                private static final long serialVersionUID = -7394189998668031754L;

                @Override
                public List<AuditSpInstance> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    SqlConditionUtil sql = new SqlConditionUtil();
                    //页面上搜索条件
                    sql.setInnerJoinTable("audit_rs_item_baseinfo b", "a.yewuguid", "b.rowguid");
                    if (StringUtil.isNotBlank(applyername)) {
                    	sql.like("a.applyername", applyername);
                    }

                    if (StringUtil.isNotBlank(itemname)) {
                    	sql.like("a.itemname", itemname);
                    }
                    if (StringUtil.isNotBlank(itemtype)) {
                        sql.like("b.itemtype", itemtype);
                    }
                    
                    sql.eq("a.businesstype", ZwfwConstant.CONSTANT_STR_ONE);
                    //查询不为草稿的
                    sql.isBlank("a.status");
                    sql.eq("a.areacode", ZwfwUserSession.getInstance().getAreaCode());
                    sortField = "createdate";
                    sortOrder = "desc";
                    PageData<AuditSpInstance> pageData = spInstanceService
                            .getAuditSpInstanceByPage(sql.getMap(), first, pageSize, sortField, sortOrder).getResult();

                    for (AuditSpInstance auditSpInstance : pageData.getList()) {
                        AuditRsItemBaseinfo auditRsItemBaseinfo = rsItemBaseinfoService
                                .getAuditRsItemBaseinfoByRowguid(auditSpInstance.getYewuguid()).getResult();
                        if (auditRsItemBaseinfo != null) {
                                auditSpInstance.put("itemname", auditRsItemBaseinfo.getItemname());
                                auditSpInstance.put("itemtype", auditRsItemBaseinfo.getItemtype());
                                auditSpInstance.put("itemlegaldept", auditRsItemBaseinfo.getItemlegaldept());
                                auditSpInstance.put("itemcode", auditRsItemBaseinfo.getItemcode());
                        }
                        AuditSpBusiness auditSpBusiness = spBusinessService
                                .getAuditSpBusinessByRowguid(auditSpInstance.getBusinessguid()).getResult();
                        if (auditSpBusiness != null) {
                            auditSpInstance.put("businessname", auditSpBusiness.getBusinessname());
                            auditSpInstance.put("del", auditSpBusiness.getDel());
                        }
                    }
                    this.setRowCount(pageData.getRowCount());
                    List<AuditSpInstance> list =  pageData.getList();
                    List<AuditSpInstance> list2 =  new ArrayList<>();
                    for (AuditSpInstance auditSpInstance : list) {
                        auditSpInstance.put("id", auditSpInstance.getRowguid());
                        auditSpInstance.put("pid", TreeFunction9.F9ROOT);
                        auditSpInstance.put("checked", false);
                        auditSpInstance.put("expanded", false);
                        auditSpInstance.put("isLeaf", false);
                     }
                    //system.out.println("spinstancelist:"+list);
                    return list;
                }
                
                
                
                @Override
                public List<AuditSpInstance> fetchChildrenData(JSONObject t) {
                      String biguid = t.getString("rowguid");
                      SqlConditionUtil sqlc =new SqlConditionUtil();
                      sqlc.eq("biguid", biguid);
                      sqlc.nq("status", "-1");
                      sqlc.setOrderDesc("createdate");
                      List<AuditSpISubapp> list = subappservice.getSubappListByMap(sqlc.getMap()).getResult();
                      List<AuditSpInstance> listi = new ArrayList<>();
                      StringBuilder sb = new StringBuilder();
                      for (AuditSpISubapp auditSpISubapp : list) {
                          AuditSpInstance spi= new AuditSpInstance(); 
                          spi.put("createdate",auditSpISubapp.getCreatedate());
                          spi.setRowguid(auditSpISubapp.getRowguid());
                          spi.put("pid", auditSpISubapp.getBiguid());
                          spi.put("id", auditSpISubapp.getRowguid());
                          
                          AuditRsItemBaseinfo auditRsItemBaseinfo = rsItemBaseinfoService
                                  .getAuditRsItemBaseinfoByRowguid(auditSpISubapp.getYewuguid()).getResult();
                          //查询阶段
                          AuditSpPhase sp = spphaseservice.getAuditSpPhaseByRowguid(auditSpISubapp.getPhaseguid()).getResult();
                          
                          if (auditRsItemBaseinfo != null) {
                              //拼上阶段名称
                              sb.setLength(0);
                             /* if(sp!=null){                                  
                                  sb.append("（").append(sp.getPhasename()).append(")");
                              }*/
                              spi.put("itemname", auditRsItemBaseinfo.getItemname()+sb.toString());
                              spi.put("itemlegaldept", auditRsItemBaseinfo.getItemlegaldept());
                              spi.put("itemcode", auditRsItemBaseinfo.getItemcode());
                          }
                          AuditSpBusiness auditSpBusiness = spBusinessService
                                  .getAuditSpBusinessByRowguid(auditSpISubapp.getBusinessguid()).getResult();
                          if (auditSpBusiness != null) {
                              spi.put("businessname", auditSpBusiness.getBusinessname());
                              spi.put("del", auditSpBusiness.getDel());
                          }
                          
                          listi.add(spi);
                      }
                      return listi;
                }
            };
        }
        return model;
    }

    public void getFlowData(){
        String biguid= getRequestParameter("biguid");
        String isflow= getRequestParameter("isflow");
        String phaseguid = getRequestParameter("phaseguid");
        
        String subappguid = "";
        
        SimpleDateFormat sdf = new SimpleDateFormat("y年M月d日");
        
        if(StringUtil.isBlank(biguid)){
            addCallbackParam("msg", "主题实例标识不能为空！");
            return;
        }
        JSONObject returnobj = new JSONObject();
        AuditSpInstance instance = spInstanceService.getDetailByBIGuid(biguid).getResult();
        if(instance==null){
            addCallbackParam("msg", "主题实例！");
            return;
        }
        DecimalFormat df = new DecimalFormat("0.00");
        AuditSpBusiness auditSpBusiness = spBusinessService
                .getAuditSpBusinessByRowguid(instance.getBusinessguid()).getResult();
        AuditRsItemBaseinfo auditRsItemBaseinfo = rsItemBaseinfoService
                .getAuditRsItemBaseinfoByRowguid(instance.getYewuguid()).getResult();
        returnobj.put("name", auditSpBusiness.getBusinessname());
        returnobj.put("itemname", auditRsItemBaseinfo.getItemname());
        returnobj.put("itemcode", auditRsItemBaseinfo.getItemcode());
        returnobj.put("startdate", EpointDateUtil.convertDate2String(instance.getCreatedate(), "y年M月d日")+"-当前");        
        Integer days = iauditorgaworkingday.GetWorkingDays_Between_From_To(ZwfwUserSession.getInstance().getCenterGuid(),instance.getCreatedate(),new Date()).getResult();
        returnobj.put("days", days);
        
        JSONArray phaselist = new JSONArray();
        returnobj.put("phaselist",phaselist);
        SqlConditionUtil sql = new SqlConditionUtil();
        sql.eq("businedssguid",auditSpBusiness.getRowguid());
        sql.setOrder("ordernumber", "desc");
        List<AuditSpPhase> listphase = spphaseservice.getAuditSpPhase(sql.getMap()).getResult();
        List<AuditSpISubapp> subapp = subappservice.getSubappByBIGuid(instance.getRowguid()).getResult();
        //去除草稿阶段
        subapp.removeIf(a->{
            if("-1".equals(a.getStatus())){
                return true;
            }else{
                return false;                
            }
        });
        boolean firstphase = true;
        for (AuditSpPhase auditSpPhase : listphase) {
            JSONObject phaseobj = new JSONObject();
            phaseobj.put("name", auditSpPhase.getPhasename());
            phaseobj.put("key", auditSpPhase.getRowguid());
            Date startdate = null;
            Date newdate = null;
            Date endDate = null;
            //记录未结束
            boolean isend = true;
            String status = "";
            for (AuditSpISubapp auditSpISubapp : subapp) {
                if(auditSpISubapp.getPhaseguid().equals(auditSpPhase.getRowguid())){
                    if(firstphase){
                        subappguid = auditSpISubapp.getRowguid();
                    }
                    if(startdate ==null || startdate.compareTo(auditSpISubapp.getCreatedate())==1){
                        startdate = auditSpISubapp.getCreatedate();
                    }
                    
                    if(auditSpISubapp.getFinishdate()==null){
                        isend = false;
                    }
                    if(endDate ==null || auditSpISubapp.getFinishdate()==null || endDate.compareTo(auditSpISubapp.getFinishdate())==-1){
                        endDate = auditSpISubapp.getFinishdate();
                    }
                    if(!"-1".equals(auditSpISubapp.getStatus())){
                        if(newdate ==null || newdate.compareTo(auditSpISubapp.getCreatedate()) != 1){
                           newdate = auditSpISubapp.getCreatedate();
                           status = auditSpISubapp.getStatus(); 
                        }
                    }
                }
                
                if(StringUtil.isNotBlank(phaseguid) && phaseguid.equals(auditSpISubapp.getPhaseguid())){
                    subappguid = auditSpISubapp.getRowguid();
                }
               
            }
            
            String start = startdate==null?"未开始":EpointDateUtil.convertDate2String(startdate, "y年M月d日");
            phaseobj.put("startdate",start);
            phaseobj.put("status",status);
            phaseobj.put("enddate", (isend && endDate!=null)?EpointDateUtil.convertDate2String(endDate, "y年M月d日"):"未结束");
            phaseobj.put("phasetime",auditSpPhase.getPhasetime());
            phaselist.add(phaseobj);
            firstphase = false;
        }
        
        JSONObject taskmap = new JSONObject();
        returnobj.put("tasklist",taskmap);
        List<AuditSpITask> spitasklist = null;
        if(!ZwfwConstant.CONSTANT_STR_ONE.equals(isflow)){
            spitasklist = iauditspitask.getTaskInstanceBySubappGuid(subappguid).getResult();
        }else{
            spitasklist = iauditspitask.getSpITaskByBIGuid(biguid).getResult();
        }
        // 根据auditspitask查询事项
        List<AuditProject> listproject = new ArrayList<>();
        //根据spitasklist查找办件
        for (AuditSpITask auditspitask : spitasklist) {
            if(StringUtil.isNotBlank(auditspitask.getProjectguid())){
                //查询办件
               AuditProject ap =  projectservice.getAuditProjectByRowGuid("rowguid,task_id,status,projectname,ouname,banjiedate,operatedate,ouguid,tasktype", auditspitask.getProjectguid(), auditspitask.getAreacode()).getResult();
               if(ap!=null){
                   listproject.add(ap);
               }
            }
        }
        StringBuilder projectname;
        StringBuilder projecttime;
        AuditProjectSparetime sptime;
        SqlConditionUtil sqlc = new SqlConditionUtil();
        for (AuditProject auditProject : listproject) {
            sqlc.clear();
            projectname = new StringBuilder();
            projecttime = new StringBuilder();
            sptime = iauditprojectsparetime.getSparetimeByProjectGuid(auditProject.getRowguid()).getResult();
            //根据taskid获取到标准事项
            sqlc.eq("taskid", auditProject.getTask_id());
            List<AuditSpBasetaskR> listtastr = iauditspbasetaskr.getAuditSpBasetaskrByCondition(sqlc.getMap()).getResult();
            if(listtastr.size() == 0){
                continue;
            }
            if(taskmap.get(listtastr.get(0).getBasetaskguid())==null){
                JSONArray arrjson = new JSONArray();
                String status = "1";
                int projectstatus = auditProject.getStatus();
                if(projectstatus == ZwfwConstant.BANJIAN_STATUS_YJJ){
                    status = "2";
                }else if(ZwfwConstant.BANJIAN_STATUS_YJJ<projectstatus && projectstatus<ZwfwConstant.BANJIAN_STATUS_ZCBJ){
                    status = "3"; 
                }else if(projectstatus >= ZwfwConstant.BANJIAN_STATUS_ZCBJ){
                    status = "4"; 
                }                
                arrjson.add(status);
                if("4".equals(status)){
                    arrjson.add(1);
                }else{
                    arrjson.add(0);
                }
                arrjson.add(1);
                projectname.append(auditProject.getProjectname()).append(" (").append(ouservice.getOuByOuGuid(auditProject.getOuguid()).getOuname()).append(")");
                //即办件不计时
                if(auditProject.getTasktype() == Integer.parseInt(ZwfwConstant.ITEMTYPE_JBJ)){
                    projecttime.append("即办件未计时");
                }else{
                    if(auditProject.getBanjiedate()!=null){
                        int daynum = iauditorgaworkingday.GetWorkingDays_Between_From_To(auditProject.getCenterguid(), new Date(), new Date()).getResult();
                        projecttime.append(sdf.format(auditProject.getOperatedate())).append("-").append(sdf.format(auditProject.getBanjiedate())).append(" 用时").append(daynum).append("工作日");
                    }else{
                        projecttime.append(sdf.format(auditProject.getOperatedate())).append("收件");
                    }                    
                }
                
                arrjson.add(projectname.toString());
                arrjson.add(projecttime.toString());
                if ( auditProject.getTasktype() == Integer.parseInt(ZwfwConstant.ITEMTYPE_JBJ)) {
                    arrjson.add("0");                        
                }else{
                    if(sptime !=null){
                        int spend = sptime.getSpendminutes();
                        int all  = sptime.getSpendminutes()+sptime.getSpareminutes();
                        arrjson.add(df.format((float)spend/all));
                        arrjson.add(sptime.getSpareminutes());                        
                    }else{
                        arrjson.add("0");
                    }
                }
                
                taskmap.put(listtastr.get(0).getBasetaskguid(), arrjson);
            }else{
                JSONArray arrjson = (JSONArray) taskmap.get(listtastr.get(0).getBasetaskguid());
                String status = "1";
                int projectstatus = auditProject.getStatus();
                if(projectstatus == ZwfwConstant.BANJIAN_STATUS_YJJ){
                    status = "2";
                }else if(ZwfwConstant.BANJIAN_STATUS_YJJ<projectstatus && projectstatus<ZwfwConstant.BANJIAN_STATUS_ZCBJ){
                    status = "3"; 
                }else if(projectstatus == ZwfwConstant.BANJIAN_STATUS_ZCBJ){
                    status = "4"; 
                }
                if("4".equals(status)){
                    arrjson.set(0,status);
                    arrjson.set(1,(int)arrjson.get(1)+1);
                    arrjson.set(2,(int)arrjson.get(2)+1);
                }else{
                    arrjson.set(2,(int)arrjson.get(2)+1);
                }
                //比较时间
                if(sptime !=null){
                    int spend = sptime.getSpendminutes();
                    int all  = sptime.getSpendminutes()+sptime.getSpareminutes();
                    try {
                        if(((float)spend/all) > df.parse(arrjson.get(5).toString()).doubleValue()){
                            arrjson.set(5, df.format((float)spend/all));
                        }
                        if(arrjson.size()>6){
                            if((Integer)arrjson.get(6)>sptime.getSpareminutes()){                                
                                arrjson.set(6,sptime.getSpareminutes());   
                            }
                        }else{
                            arrjson.add(sptime.getSpareminutes());                               
                        }
                        
                    }
                    catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                
            };
          
        }
        sendRespose(JsonUtil.objectToJson(returnobj));
    }
    
    public void getSubData(){
        String biguid= getRequestParameter("biguid");
        String phaseguid = getRequestParameter("phaseguid");
        List<AuditSpISubapp> listsub =  subappservice.getPhaseSubappList(biguid, phaseguid).getResult();
        JSONObject back = new JSONObject();
        JSONArray sublist = new JSONArray();
        back.put("sublist", sublist);
        SqlConditionUtil sql = new SqlConditionUtil();
        SimpleDateFormat sdf = new SimpleDateFormat("y年M月d日");
        StringBuilder projectname;
        StringBuilder projecttime;
        for (AuditSpISubapp subapp : listsub) {
            List<AuditSpITask> spitasklist = iauditspitask.getTaskInstanceBySubappGuid(subapp.getRowguid()).getResult();
            List<AuditProject> listproject = new ArrayList<>();
            //根据spitasklist查找办件
            for (AuditSpITask auditspitask : spitasklist) {
                if(StringUtil.isNotBlank(auditspitask.getProjectguid())){
                    //查询办件
                   AuditProject ap =  projectservice.getAuditProjectByRowGuid("rowguid,task_id,status,projectname,ouname,banjiedate,operatedate,ouguid,tasktype,centerguid", auditspitask.getProjectguid(), auditspitask.getAreacode()).getResult();
                   if(ap!=null){
                       listproject.add(ap);
                   }
                }
            }
            if(listproject== null || listproject.size()==0){
                continue;
            }
            JSONObject sub = new JSONObject();
            AuditRsItemBaseinfo rsitem = rsItemBaseinfoService.getAuditRsItemBaseinfoByRowguid(subapp.getYewuguid()).getResult();
            sub.put("itemname", rsitem.getItemname());
            JSONArray tasklist = new JSONArray();
            sub.put("tasklist", tasklist);
            for (AuditProject auditProject : listproject) {
                JSONObject obj = new JSONObject();
                projectname = new StringBuilder();
                projecttime = new StringBuilder();
                //办件名称
                projectname.append(auditProject.getProjectname()).append(" (").append(ouservice.getOuByOuGuid(auditProject.getOuguid()).getOuname()).append(")");
                obj.put("projectname", projectname.toString());
                //获取时间
                if(auditProject.getTasktype() == Integer.parseInt(ZwfwConstant.ITEMTYPE_JBJ)){
                    projecttime.append("即办件未计时");
                }else{
                    if(auditProject.getBanjiedate()!=null){
                        int daynum = iauditorgaworkingday.GetWorkingDays_Between_From_To(auditProject.getCenterguid(), auditProject.getOperatedate(), auditProject.getBanjiedate()).getResult();
                        if(daynum == 0){
                            daynum = 1;
                        }
                        projecttime.append(sdf.format(auditProject.getOperatedate())).append("-").append(sdf.format(auditProject.getBanjiedate())).append(" 用时").append(daynum).append("工作日");
                    }else{
                        projecttime.append(sdf.format(auditProject.getOperatedate())).append("收件");
                    }                    
                }
                obj.put("projecttime", projecttime.toString());
                //办件状态
                String status = "1";
                int projectstatus = auditProject.getStatus();
                if(projectstatus == ZwfwConstant.BANJIAN_STATUS_YJJ){
                    status = "2";
                }else if(ZwfwConstant.BANJIAN_STATUS_YJJ<projectstatus && projectstatus<ZwfwConstant.BANJIAN_STATUS_ZCBJ){
                    status = "3"; 
                }else if(projectstatus >= ZwfwConstant.BANJIAN_STATUS_ZCBJ){
                    status = "4"; 
                }     
                obj.put("status", status);
                if(ZwfwConstant.BANJIAN_STATUS_DBB == projectstatus){
                    obj.put("bz", 1);
                }else{
                    obj.put("bz", 0);
                }
                tasklist.add(obj);
            }
            sublist.add(sub);
        }
        sendRespose(JsonUtil.objectToJson(back));
    }
    
    
    
    
    public String getTaskAndOuData(){
       String bussinessguid = getRequestParameter("bussinessguid");
       JSONArray arr = new JSONArray();
       if(StringUtil.isBlank(bussinessguid)){
           addCallbackParam("msg", "未获取到参数！");
           return "";
       }else{
           List<AuditSpTask> listtask =  iauditsptask.getAllAuditSpTaskByBusinessGuid(bussinessguid).getResult();
           AuditSpBasetask audittask ;
           JSONObject obj;
           for (AuditSpTask auditsptask : listtask) {
               obj = new JSONObject();
               audittask = iauditspbasetask.getAuditSpBasetaskByrowguid(auditsptask.getBasetaskguid()).getResult();
               obj.put("name",audittask.getTaskname());
               obj.put("ouname", audittask.getOuname());
               obj.put("key",audittask.getRowguid());
               arr.add(obj);
           }
       }
       return arr.toJSONString();
    }
    
    
    /**
     * 
     *  获取详细页面地址
     * 
     *  @param biGuid  
     */
    public void checkDetail(String biGuid) {
        String registerUrl = "";
        //system.out.println("bigguid1:"+biGuid);
        AuditSpInstance auditSpInstance = spInstanceService.getDetailByBIGuid(biGuid).getResult();
        //system.out.println("auditSpInstance1"+auditSpInstance);
        AuditSpBusiness auditSpBusiness = spBusinessService.getAuditSpBusinessByRowguid(auditSpInstance.getBusinessguid()).getResult();
        //system.out.println("auditSpBusiness"+auditSpBusiness);
        String businessType = auditSpBusiness.getBusinesstype();
        String filepath = auditSpBusiness.getHandleURL();
        if (StringUtil.isNotBlank(filepath)) {
            if ("1".equals(businessType)) {
                // 建设项目
                registerUrl = "epointzwfw/auditsp/auditsphandle/handlebilistviewdetail?guid=" + biGuid;
            }
            else if ("2".equals(businessType)) {
                // 一般并联审批
                registerUrl = filepath + (filepath.substring(filepath.lastIndexOf("/"), filepath.length()) + "detail")
                        + "?guid=" + biGuid;
            }
        }
        else {
            if ("1".equals(businessType)) {
                // 建设项目
                registerUrl = "epointzwfw/auditsp/auditsphandle/handlebilistviewdetail?guid=" + biGuid;
            }
            else if ("2".equals(businessType)) {
                // 一般并联审批
                registerUrl = "epointzwfw/auditsp/auditspintegrated/auditspintegrateddetail?guid=" + biGuid;
            }
        }
        this.addCallbackParam("msg", registerUrl);
    }
    @SuppressWarnings("unchecked")
    public List<SelectItem> getItemTypeModel() {
        if (itemTypebModel == null) {
            itemTypebModel = DataUtil.convertMap2ComboBox((List<Map<String, String>>) CodeModalFactory
                    .factory(EpointKeyNames9.CHECK_SELECT_GROUP, "项目类型", null, true));
        }
        return this.itemTypebModel;
    }

    public AuditSpBusiness getDataBean() {
        if (dataBean == null) {
            dataBean = new AuditSpBusiness();
        }
        return dataBean;
    }

    public void setDataBean(AuditSpBusiness dataBean) {
        this.dataBean = dataBean;
    }

    public String getApplyername() {
        return applyername;
    }

    public void setApplyername(String applyername) {
        this.applyername = applyername;
    }

    public String getItemname() {
        return itemname;
    }

    public void setItemname(String itemname) {
        this.itemname = itemname;
    }

    public String getItemtype() {
        return itemtype;
    }

    public void setItemtype(String itemtype) {
        this.itemtype = itemtype;
    }
}
