package com.epoint.evainstance.evainstance.action;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.authentication.UserSession;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.export.ExportModel;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.evainstance.IEvainstanceService;
import com.epoint.evainstance.entity.Evainstance;
import com.epoint.frame.service.message.api.IMessagesCenterService;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;

/**
 * 评价表list页面对应的后台
 * 
 * @author lizhenjie
 * @version [版本号, 2020-09-06 14:00:35]
 */
@RestController("evainstancelistaction")
@Scope("request")
public class EvainstanceListAction extends BaseController
{
    @Autowired
    private IEvainstanceService service;

    /**
     * 评价表实体对象
     */
    private Evainstance dataBean;

    /**
     * 表格控件model
     */
    private DataGridModel<Evainstance> model;

    /**
     * 导出模型
     */
    private ExportModel exportModel;

    /**
    * 所属辖区下拉列表model
    */
    private List<SelectItem> areacodeModel = null;
    /**
     * 评价渠道下拉列表model
     */
    private List<SelectItem> pfModel = null;
    /**
     * 满意度下拉列表model
     */
    private List<SelectItem> satisfactionModel = null;
    /**
     * 是否回复单选按钮组model
     */
    private List<SelectItem> sfhfModel = null;
    private List<SelectItem> cxcpModel = null;
    /**
     * 是否回访单选按钮组model
     */
    private List<SelectItem> sfhfangModel = null;
    @Autowired
    private IMessagesCenterService msgService;
    private int  fcbmycount;
    private int bmycount;

    public void pageLoad() {
        
          String msgguid = getRequestParameter("MessageItemGuid");
          if(StringUtil.isNotBlank(msgguid)) {
              msgService.deleteMessage(msgguid, userSession.getUserGuid());
          }
          if(StringUtil.isNotBlank(dataBean)) {
            if(StringUtil.isNotBlank(dataBean.get("startDate"))&&StringUtil.isNotBlank(dataBean.get("endDate"))) {
                //system.out.println("==="+dataBean.get("startDate"));
                addCallbackParam("startDate",dataBean.get("startDate"));
                addCallbackParam("endDate",dataBean.get("endDate"));
            }
          }
         fcbmycount = service.findFcbmyTotal(ZwfwUserSession.getInstance().getAreaCode());
         bmycount = service.findBmyTotal(ZwfwUserSession.getInstance().getAreaCode());
        
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

    public DataGridModel<Evainstance> getDataGridData() {
     // 获得表格对象
        if (model == null) {
            model = new DataGridModel<Evainstance>()
            {
                @Override
                public List<Evainstance> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    SqlConditionUtil sql = new SqlConditionUtil();
                    String projectno  =  getRequestParameter("guid");
                    String fields = "pf,rowguid,taskCode,taskName,projectNo,acceptDate,userProp,userName,assessNumber,areacode,mobile,sfhf,sfhfang,khyj,khouname,ldps,ldmc"
                            + ",evalDetail,satisfaction";
                    if(!dataBean.isEmpty()) {
                       /* if(!dataBean.getAreacode().isEmpty()) {
                            sql.eq("areacode", dataBean.getAreacode());
                        }*/
                        if(!dataBean.getPf().isEmpty()) {
                            sql.eq("pf", dataBean.getPf());
                        }
                        if(!dataBean.getSatisfaction().isEmpty()) {
                            sql.eq("satisfaction", dataBean.getSatisfaction());
                        }
                        if(!dataBean.getSfhf().isEmpty()) {
                        	if (!"2".equals(dataBean.getSfhf())) {
                        		sql.eq("sfhf", dataBean.getSfhf());
                        	}
                            
                        }
                        if(!dataBean.getSfhfang().isEmpty()) {
                        	if (!"2".equals(dataBean.getSfhfang())) {
                        		sql.eq("sfhfang", dataBean.getSfhfang());
                        	}
                            
                        }
//                        if(!dataBean.getCxcp().isEmpty()&&dataBean.getCxcp().equals("1")) {
//                            //sql.eq("sfeypj", null);
//                            sql.isBlank("sfeypj");
//                            sql.le("satisfaction","2");
//                        }
                    
                        if(dataBean.get("startDate")!=null&&dataBean.get("startDate")!="") {
                            //system.out.println(dataBean.get("startDate")+"'");
                            sql.ge("assessTime","'"+dataBean.get("startDate")+"'");
                        }
                        if(dataBean.get("endDate")!=null&&dataBean.get("endDate")!="") {
                        	String endDate = EpointDateUtil.convertDate2String(dataBean.getDate("endDate"), "yyyy-MM-dd")+" 59:59:59";
							////system.out.println(endDate);
							sql.le("assessTime", "'" + endDate + "'");
                         //   sql.le("assessTime","'"+dataBean.get("endDate")+"'");
                            addCallbackParam("startDate",dataBean.get("startDate"));
                            addCallbackParam("endDate",dataBean.get("endDate"));
                            addCallbackParam("type",true);
                        }
                    }else {
                        Calendar cal = Calendar.getInstance();
                        cal.add(Calendar.DATE, -7);
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Date date=cal.getTime();
                        sql.ge("assessTime","'"+sdf.format(date)+"'");
                        sql.le("assessTime","'"+EpointDateUtil.convertDate2String(new Date(), "yyyy-MM-dd HH:mm:ss")+"'");
                        
                    }
                    String areacode = ZwfwUserSession.getInstance().getAreaCode();
                    sql.eq("areacode", areacode);
                    if(StringUtil.isBlank(sortField)) {
                        sortField = "acceptdate";
                    }
                    if(StringUtil.isNotBlank(projectno)) {
                        sql.eq("rowguid", projectno);
                    }
                   // sql.lt("satisfaction", "3");
                    PageData<Evainstance> pageProject = service.getEvaluateservicePageData(fields,
                            sql.getMap(), first, pageSize, sortField, sortOrder, UserSession.getInstance().getUserGuid()).getResult();
                    List<Evainstance> list = new ArrayList<>();
                    list = pageProject.getList();
                    int count = pageProject.getRowCount();
                    this.setRowCount(count);
                    return list;
                }

            };
        }
        return model;

    }

    public Evainstance getDataBean() {
        if (dataBean == null) {
            dataBean = new Evainstance();
        }
        return dataBean;
    }

    public void setDataBean(Evainstance dataBean) {
        this.dataBean = dataBean;
    }

  
    
    public ExportModel getExportModel() {
        if (exportModel == null) {
          //  exportModel = new ExportModel(
//                    "projectno,areacode,taskname,username,pf,satisfaction,winname,winusername,mobile,pjqd,sfhf,sfhfang,nbyj,zgjg,hfjg,ldps,sfeypj,",
//                    "办件编号,辖区编码,事项名称,评价人员,评价渠道,评分,窗口名称,窗口编号,联系方式,评价渠道,是否回复,是否回访,拟办意见,整改建议,回复结果,领导批示,是否恶意评价");
//            
            exportModel = new ExportModel(
                    "projectno,areacode,taskname,username,pf,satisfaction,winname,winusername,mobile,sfhf,sfhfang,nbyj,zgjg,hfjg,writingEvaluation,evaldetail",
                    "办件编号,辖区编码,事项名称,评价人员,评价渠道,满意度,窗口名称,窗口编号,联系方式,是否回复,是否回访,拟办意见,整改结果,回复结果,评价理由,评价指标");
            exportModel.addColumnWidth("projectno", 5000);
            exportModel.addColumnWidth("areacode", 5000);
            exportModel.addColumnWidth("taskname", 5000);
          
            
            exportModel.addColumnWidth("username", 5000);
            exportModel.addColumnWidth("pf", 5000);
            exportModel.addColumnWidth("satisfaction", 5000);
            
            
            exportModel.addColumnWidth("winname", 5000);
            exportModel.addColumnWidth("winusername", 5000);
            
            exportModel.addColumnWidth("mobile", 5000);
            /*exportModel.addColumnWidth("pjqd", 5000);*/
            exportModel.addColumnWidth("sfhf", 5000);
            exportModel.addColumnWidth("sfhfang", 5000);
            exportModel.addColumnWidth("nbyj", 5000);
            
            exportModel.addColumnWidth("zgjg", 5000);
            exportModel.addColumnWidth("hfjg", 5000);
            
            exportModel.addColumnWidth("writingEvaluation", 5000);
            exportModel.addColumnWidth("evaldetail", 5000);
        }
        return exportModel;
    }

    public List<SelectItem> getAreacodeModel() {
        if (areacodeModel == null) {
            areacodeModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "辖区", null, true));
        }
        return this.areacodeModel;
    }

    public List<SelectItem> getPfModel() {
        if (pfModel == null) {
            pfModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "评价渠道", null, true));
        }
        return this.pfModel;
    }

    public List<SelectItem> getSatisfactionModel() {
        if (satisfactionModel == null) {
            satisfactionModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "好差评满意度层级", null, true));
        }
        return this.satisfactionModel;
    }

    public List<SelectItem> getSfhfModel() {
        if (sfhfModel == null) {
            sfhfModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("单选按钮组", "是否回复", null, false));
        }
        return this.sfhfModel;
    }

    public List<SelectItem> getSfhfangModel() {
        if (sfhfangModel == null) {
            sfhfangModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("单选按钮组", "是否回访", null, false));
        }
        return this.sfhfangModel;
    }
    
    public List<SelectItem> getCxcpModel() {
        if (cxcpModel == null) {
            cxcpModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("单选按钮组", "差评查询", null, false));
        }
        return this.cxcpModel;
    }


    public int getFcbmycount() {
        return fcbmycount;
    }

    public void setFcbmycount(int fcbmycount) {
        this.fcbmycount = fcbmycount;
    }

    public int getBmycount() {
        return bmycount;
    }

    public void setBmycount(int bmycount) {
        this.bmycount = bmycount;
    }
    

}
