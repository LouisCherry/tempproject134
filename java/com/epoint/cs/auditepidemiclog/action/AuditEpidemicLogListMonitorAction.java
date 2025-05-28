package com.epoint.cs.auditepidemiclog.action;

import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.Map;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.cs.auditepidemiclog.api.entity.AuditEpidemicLog;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.export.ExportModel;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.common.util.CommonUtil;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import org.springframework.beans.factory.annotation.Autowired;
import com.epoint.cs.auditepidemiclog.api.IAuditEpidemicLogService;

/**
 * 访客登记list页面对应的后台
 * 
 * @author Mercury
 * @version [版本号, 2020-02-02 19:35:15]
 */
@RestController("auditepidemicloglistmonitoraction")
@Scope("request")
public class AuditEpidemicLogListMonitorAction extends BaseController
{
    /**
     * 
     */
    private static final long serialVersionUID = -7226873873051608309L;

    @Autowired
    private IAuditEpidemicLogService service;

    @Autowired
    private IAuditProject aservice;

    /**
     * 访客登记实体对象
     */
    private AuditEpidemicLog dataBean;

    /**
     * 表格控件model
     */
    private DataGridModel<AuditEpidemicLog> model;

    private DataGridModel<AuditProject> model1;

    /**
     * 导出模型
     */
    private ExportModel exportModel;

    /**
    * 登记状态单选按钮组model
    */
    private List<SelectItem> statusModel = null;

    private String centerguid = "";
    private String name = "";
    private String id = "";

    public void pageLoad() {
        centerguid = getRequestParameter("centerguid");
        name = getRequestParameter("name");
        id = getRequestParameter("id");
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

    public DataGridModel<AuditProject> getDataGridData1() {
        // 获得表格对象
        if (model1 == null) {
            model1 = new DataGridModel<AuditProject>()
            {

                /**
                 * 
                 */
                private static final long serialVersionUID = 1118116299707223311L;

                @SuppressWarnings("deprecation")
                @Override
                public List<AuditProject> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    SqlConditionUtil sql = new SqlConditionUtil();
                    if (StringUtil.isNotBlank(centerguid)) {
                        sql.eq("centerguid", centerguid);
                    }
                    if (StringUtil.isNotBlank(name)) {
                        try {
                            sql.eq("applyername", URLDecoder.decode(name, "UTF-8"));
                        }
                        catch (UnsupportedEncodingException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                    if (StringUtil.isNotBlank(id)) {
                        sql.eq("certnum", id);
                    }
                    PageData<AuditProject> pageData = aservice
                            .getAuditProjectPageData(sql.getMap(), first, pageSize, "applydate", "DESC").getResult();
                    Iterator<AuditProject> iterator = pageData.getList().iterator();
                    while (iterator.hasNext()) {
                        AuditProject auditProject = iterator.next();
                        if (!isToday(auditProject.getApplydate())) {
                            iterator.remove();
                        }
                    }
                    this.setRowCount(pageData.getRowCount());
                    if (pageData.getList().size() == 0) {
                        addCallbackParam("flag", 1);
                    }
                    return pageData.getList();
                }

            };
        }
        return model1;
    }

    public DataGridModel<AuditEpidemicLog> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<AuditEpidemicLog>()
            {

                /**
                 * 
                 */
                private static final long serialVersionUID = -8647759173390944744L;

                @Override
                public List<AuditEpidemicLog> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    SqlConditionUtil sql = new SqlConditionUtil();
                    if (StringUtil.isNotBlank(dataBean.getName())) {
                        sql.like("name", dataBean.getName());
                    }
                    if (StringUtil.isNotBlank(dataBean.getId())) {
                        sql.like("id", dataBean.getId());
                    }
                    sql.nq("name", name);
                    sql.eq("centerguid", ZwfwUserSession.getInstance().getCenterGuid());
                    PageData<AuditEpidemicLog> pageDataother = service
                            .getAuditEpidemicLogPageData(sql.getMap(), first, pageSize, "entrytime", "DESC")
                            .getResult();
                    SqlConditionUtil sqlself = new SqlConditionUtil();
                    sqlself.eq("name", name);
                    PageData<AuditEpidemicLog> pageDataSelf = service
                            .getAuditEpidemicLogPageData(sqlself.getMap(), first, pageSize, sortField, sortOrder)
                            .getResult();
                    Iterator<AuditEpidemicLog> iteratorother = pageDataother.getList().iterator();
                    for (AuditEpidemicLog auditEpidemicLogSelf : pageDataSelf.getList()) {
                        if (!pageDataother.getList().isEmpty()) {
                            while (iteratorother.hasNext()) {
                                AuditEpidemicLog auditEpidemicLogOther = iteratorother.next();
                                //自己没出厅
                                if ((("".equals(auditEpidemicLogSelf.getExittime()))
                                        || (StringUtil.isBlank(auditEpidemicLogSelf.getExittime())))) {
                                    //患者没出厅(一定接触无需考虑)

                                    //患者出厅
                                    if (((!"".equals(auditEpidemicLogOther.getExittime()))
                                            && (StringUtil.isNotBlank(auditEpidemicLogOther.getExittime())))) {
                                        try {
                                            if (dateconvertIndividual(auditEpidemicLogSelf.getEntrytime(),
                                                    auditEpidemicLogOther.getEntrytime(),
                                                    auditEpidemicLogOther.getExittime())) {

                                            }
                                            else {
                                                iteratorother.remove();
                                            }
                                        }
                                        catch (ParseException e) {
                                            // TODO Auto-generated catch block
                                            e.printStackTrace();
                                        }
                                    }
                                }
                                //自己出厅
                                if ((!"".equals(auditEpidemicLogSelf.getExittime()))
                                        && (StringUtil.isNotBlank(auditEpidemicLogSelf.getExittime()))) {
                                    //患者未出厅
                                    if (("".equals(auditEpidemicLogOther.getExittime()))
                                            || (StringUtil.isBlank(auditEpidemicLogOther.getExittime()))) {
                                        try {
                                            if (dateconvertIndividual(auditEpidemicLogOther.getEntrytime(),
                                                    auditEpidemicLogSelf.getEntrytime(),
                                                    auditEpidemicLogSelf.getExittime())) {

                                            }
                                            else {
                                                iteratorother.remove();
                                            }
                                        }
                                        catch (ParseException e) {
                                            // TODO Auto-generated catch block
                                            e.printStackTrace();
                                        }
                                    }
                                    //患者出厅
                                    if ((!"".equals(auditEpidemicLogOther.getExittime()))
                                            && (StringUtil.isNotBlank(auditEpidemicLogOther.getExittime()))) {
                                        try {
                                            if (dateconvert(auditEpidemicLogSelf.getEntrytime(),
                                                    auditEpidemicLogSelf.getExittime(),
                                                    auditEpidemicLogOther.getEntrytime(),
                                                    auditEpidemicLogOther.getExittime())) {

                                            }
                                            else {
                                                iteratorother.remove();
                                            }
                                        }
                                        catch (ParseException e) {
                                            // TODO Auto-generated catch block
                                            e.printStackTrace();
                                        }
                                    }
                                }

                            }
                        }
                    }

                    this.setRowCount(pageDataother.getRowCount());
                    return pageDataother.getList();
                }

            };
        }
        return model;
    }

    public AuditEpidemicLog getDataBean() {
        if (dataBean == null) {
            dataBean = new AuditEpidemicLog();
        }
        return dataBean;
    }

    public void setDataBean(AuditEpidemicLog dataBean) {
        this.dataBean = dataBean;
    }

    public ExportModel getExportModel() {
        if (exportModel == null) {
            exportModel = new ExportModel("address,centername,entrytime,exittime,id,name,status,tel",
                    "住址,中心名称,进厅时间,出厅时间,身份证号,姓名,登记状态,联系电话");
        }
        return exportModel;
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> getStatusModel() {
        if (statusModel == null) {
            statusModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("单选按钮组", "登记状态", null, true));
        }
        return this.statusModel;
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> getBjStatusModel() {
        if (statusModel == null) {
            statusModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "办件状态", null, true));
            if (statusModel != null) {
                Iterator<SelectItem> it = statusModel.iterator();
                if (it.hasNext()) {
                    it.next();
                }
                while (it.hasNext()) {
                    SelectItem item = it.next();
                    // 删除列表中不需要的列表选项
                    if (Integer.parseInt(item.getValue().toString()) < ZwfwConstant.BANJIAN_STATUS_DJJ
                            || Integer.parseInt(item.getValue().toString()) >= ZwfwConstant.BANJIAN_STATUS_ZCBJ
                            || Integer.parseInt(item.getValue().toString()) == ZwfwConstant.BANJIAN_STATUS_YSLDBJ) {
                        it.remove();
                    }
                }
            }
        }
        return this.statusModel;
    }

    //判断时间在当天
    public static boolean isToday(Date inputJudgeDate) {
        boolean flag = false;
        //获取当前系统时间
        long longDate = System.currentTimeMillis();
        Date nowDate = new Date(longDate);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String format = dateFormat.format(nowDate);
        String subDate = format.substring(0, 10);
        //定义每天的24h时间范围
        String beginTime = subDate + " 00:00:00";
        String endTime = subDate + " 23:59:59";
        Date paseBeginTime = null;
        Date paseEndTime = null;
        try {
            paseBeginTime = dateFormat.parse(beginTime);
            paseEndTime = dateFormat.parse(endTime);

        }
        catch (ParseException e) {
            log.error(e.getMessage());
        }
        if (inputJudgeDate.after(paseBeginTime) && inputJudgeDate.before(paseEndTime)) {
            flag = true;
        }
        return flag;
    }

    /**
     * 获取剩余时间
     * 
     * @return String
     */
    public String getSpareTime(int minutes, int taskType) {
        String result = "";
        if (StringUtil.isNotBlank(taskType)) {
            if (taskType != Integer.parseInt(ZwfwConstant.ITEMTYPE_JBJ)) {
                if (minutes > 0) {
                    if (minutes < 1440) {
                        result = "剩余" + CommonUtil.getSpareTimes(minutes)
                                + "<img src=\"../../../../epointzwfw/image/light/yellowLight.gif\"/>";
                    }
                    else {
                        result = "剩余" + CommonUtil.getSpareTimes(minutes)
                                + "<img src=\"../../../../epointzwfw/image/light/greenLight.gif\"/>";
                    }
                }
                else {
                    minutes = -minutes;
                    result = "超时" + CommonUtil.getSpareTimes(minutes)
                            + "<img src=\"../../../../epointzwfw/image/light/redLight.gif\"/>";
                }
            }
            else {
                result = "--";
            }
        }
        else {
            result = "--";
        }
        return result;
    }

    @SuppressWarnings("deprecation")
    public void hasProGuid(String rowguid) {
        String areacode = "";
        // 如果是镇村接件
        if (ZwfwUserSession.getInstance().getCitylevel() != null
                && (Integer.parseInt(ZwfwUserSession.getInstance().getCitylevel()) > Integer
                        .parseInt(ZwfwConstant.AREA_TYPE_XQJ))) {
            areacode = ZwfwUserSession.getInstance().getBaseAreaCode();
        }
        else {
            areacode = ZwfwUserSession.getInstance().getAreaCode();
        }
        AuditProject auditProject = aservice.getAuditProjectByRowGuid(rowguid, areacode).getResult();
        if (auditProject == null) {
            addCallbackParam("message", "办件已删除");
        }
    }

    public static boolean dateconvert(Date a1, Date a2, Date a3, Date a4) throws ParseException {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String stringa1 = sdf.format(a1);
        String stringa2 = sdf.format(a2);
        String stringa3 = sdf.format(a3);
        String stringa4 = sdf.format(a4);

        a1 = sdf.parse(stringa1);
        a2 = sdf.parse(stringa2);
        a3 = sdf.parse(stringa3);
        a4 = sdf.parse(stringa4);
        long aa1 = a1.getTime();
        long aa2 = a2.getTime();
        long aa3 = a3.getTime();
        long aa4 = a4.getTime();

        /*if((aa1<aa3) && (aa2<aa4)&&(aa3<aa2)){
            return true;
        }else if((aa3<aa1)&&(aa4<aa2)&&(aa1<aa4)){
            return true;
        }else if((aa1<=aa3)&&((aa4<=aa2))){
            return true;
        }else if((aa3<=aa1)&&((aa2<=aa4))){
            return true;
        }
        else{   
            return false;
        }*/
        if (aa2 <= aa3 || aa4 <= aa1) {
            return false;
        }
        else {
            return true;
        }
    }

    public static boolean dateconvertIndividual(Date a1, Date a3, Date a4) throws ParseException {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String stringa1 = sdf.format(a1);
        String stringa3 = sdf.format(a3);
        String stringa4 = sdf.format(a4);

        a1 = sdf.parse(stringa1);
        a3 = sdf.parse(stringa3);
        a4 = sdf.parse(stringa4);
        long aa1 = a1.getTime();
        long aa3 = a3.getTime();
        long aa4 = a4.getTime();

        /*if((aa1<aa3) && (aa2<aa4)&&(aa3<aa2)){
            return true;
        }else if((aa3<aa1)&&(aa4<aa2)&&(aa1<aa4)){
            return true;
        }else if((aa1<=aa3)&&((aa4<=aa2))){
            return true;
        }else if((aa3<=aa1)&&((aa2<=aa4))){
            return true;
        }
        else{   
            return false;
        }*/
        if (aa1 >= aa4) {
            return false;
        }
        else {
            return true;
        }
    }

}
