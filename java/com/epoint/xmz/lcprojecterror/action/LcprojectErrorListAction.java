package com.epoint.xmz.lcprojecterror.action;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.controller.BaseController;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.core.EpointFrameDsManager;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.frame.service.metadata.mis.util.ListGenerator;
import com.epoint.util.TARequestUtil;
import com.epoint.xmz.lcprojecterror.api.ILcprojectErrorService;
import com.epoint.xmz.lcprojecterror.api.entity.LcprojectError;

/**
 * 浪潮推送失败记录表list页面对应的后台
 * 
 * @author 1
 * @version [版本号, 2021-06-29 10:03:18]
 */
@RestController("lcprojecterrorlistaction")
@Scope("request")
public class LcprojectErrorListAction extends BaseController
{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    // lcjbxxurl=http://59.206.96.143:25990/jnzwfw/rest/lcprojectrest/otherSystemAccpet
    // lclcurl=http://59.206.96.143:25990/jnzwfw/rest/lcprojectrest/otherSystemAddCourse
    public static final String lclcurl = ConfigUtil.getConfigValue("epointframe", "lclcurl");

    public static final String lcjbxxurl = ConfigUtil.getConfigValue("epointframe", "lcjbxxurl");

    Logger logger = Logger.getLogger(LcprojectErrorListAction.class);

    @Autowired
    private ILcprojectErrorService service;

    /**
     * 浪潮推送失败记录表实体对象
     */
    private LcprojectError dataBean;

    /**
     * 表格控件model
     */
    private DataGridModel<LcprojectError> model;

    /**
     * 导出模型
     */
    // private ExportModel exportModel;

    @Autowired
    private IAuditProject auditProjectServcie;

    private String projectname = "";

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

    /**
     * 
     * 批量重新推送
     * 
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public void pushAccpetAll() {
        // 成功条数
        int successCount = 0;
        SqlConditionUtil sqlutil = new SqlConditionUtil();

        // 如果存在勾选，只处理勾选记录
        List<String> select = getDataGridData().getSelectKeys();
        logger.info("勾选记录：" + select);
        if (select != null && select.size() > 0) {
            for (String guid : select) {
                LcprojectError find = service.find(guid);
                logger.info("勾选记录find：" + find);
                AuditProject project = auditProjectServcie.getAuditProjectByRowGuid(null, find.getProjectguid(), null)
                        .getResult();
                logger.info("勾选记录project：" + project);
                if (project != null && StringUtil.isNotBlank(find.getStr("record"))) {
                    logger.info("===========重新推送:" + find.getFlowsn() + " ===========");
                    String resultsign = TARequestUtil.sendPostInner(lcjbxxurl, find.getStr("record"), "", "");
                    JSONObject jsonObject = JSONObject.parseObject(resultsign);
                    logger.info(jsonObject.toString());
                    JSONObject custom = jsonObject.getJSONObject("custom");
                    if ("1".equals(custom.getString("code"))) {
                        successCount++;
                        sqlutil.clear();
                        sqlutil.eq("status", "0");
                        sqlutil.eq("type", "otherSystemAccpet");
                        sqlutil.eq("flowsn", find.getFlowsn());
                        List<LcprojectError> updatelist = service.getAllLCProject(sqlutil.getMap(),
                                LcprojectError.class);
                        for (LcprojectError updateitem : updatelist) {
                            updateitem.setStatus("1");
                            service.update(updateitem);
                        }
                    }
                }
            }
        }
        else {
            // 查询50条推送失败的记录
            sqlutil.clear();
            sqlutil.eq("status", "0");
            sqlutil.eq("type", "otherSystemAccpet");
            PageData<LcprojectError> pageData = service.getAllLCProjectByPage(sqlutil.getMap(), LcprojectError.class, 0,
                    50, null, null);
            List<LcprojectError> errorList = pageData.getList();
            if (errorList != null && errorList.size() > 0) {
                for (LcprojectError lcproject : errorList) {
                    EpointFrameDsManager.begin(null);
                    logger.info("开始批量重新推送otherSystemAccpet错误数据，数量 =" + errorList.size());
                    AuditProject auditProject = auditProjectServcie
                            .getAuditProjectByRowGuid(null, lcproject.getProjectguid(), null).getResult();
                    if (auditProject != null && StringUtil.isNotBlank(lcproject.getStr("record"))) {
                        try {

                            logger.info("重新推送办件主键:" + lcproject.getProjectguid());
                            String resultsign = TARequestUtil.sendPostInner(lcjbxxurl, lcproject.getStr("record"), "",
                                    "");
                            JSONObject jsonObject = JSONObject.parseObject(resultsign);
                            logger.info(jsonObject.toString());
                            JSONObject custom = jsonObject.getJSONObject("custom");
                            if ("1".equals(custom.getString("code"))) {
                                successCount++;
                                sqlutil.clear();
                                sqlutil.eq("status", "0");
                                sqlutil.eq("type", "otherSystemAccpet");
                                sqlutil.eq("flowsn", lcproject.getFlowsn());
                                List<LcprojectError> updatelist = service.getAllLCProject(sqlutil.getMap(),
                                        LcprojectError.class);
                                if (updatelist != null && updatelist.size() > 0) {
                                    for (LcprojectError updateitem : updatelist) {
                                        updateitem.setStatus("1");
                                        service.update(updateitem);
                                    }
                                }
                                logger.info("===========推送:" + lcproject.getProjectguid() + "成功 ===========");
                            }
                            EpointFrameDsManager.commit();
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                        finally {

                            EpointFrameDsManager.close();
                        }
                    }
                }
            }
        }

        addCallbackParam("msg", "成功重推" + successCount + "条数据！");

    }

    /**
     * 
     * 批量重新同步
     * 
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public void pushAddCourseAll() {
        SqlConditionUtil sqlutil = new SqlConditionUtil();
        sqlutil.eq("status", "0");
        sqlutil.eq("type", "otherSystemAddCourse");
        int pageSize = 50;
        int successCount01 = 0;
        int successCount04 = 0;
        // 批量重新同步status为01
        for (int i = 0; i < 1; i++) {
            // 查询100条推送失败的记录
            sqlutil.clear();
            sqlutil.eq("status", "0");
            sqlutil.eq("type", "otherSystemAddCourse");
            PageData<LcprojectError> pageData = service.getAllLCProjectByPage(sqlutil.getMap(), LcprojectError.class,
                    i * pageSize, pageSize, null, null);
            if (pageData != null && !pageData.getList().isEmpty()) {
                for (LcprojectError lcproject : pageData.getList()) {
                    EpointFrameDsManager.begin(null);
                    logger.info("===========开始批量重新同步01 ===========");

                    AuditProject auditProject = auditProjectServcie
                            .getAuditProjectByRowGuid(null, lcproject.getProjectguid(), null).getResult();

                    if (auditProject != null && StringUtil.isNotBlank(lcproject.getStr("record"))) {
                        if ("01".equals(JSONObject.parseObject(lcproject.getStr("record")).get("status"))) {
                            try {
                                logger.info("===========重新同步:" + lcproject.getProjectguid() + " ===========");
                                String resultsign = TARequestUtil.sendPostInner(lclcurl, lcproject.getStr("record"), "",
                                        "");
                                JSONObject jsonObject = JSONObject.parseObject(resultsign);

                                JSONObject custom = jsonObject.getJSONObject("custom");
                                if ("1".equals(custom.getString("code"))) {
                                    successCount01++;
                                    sqlutil.clear();
                                    sqlutil.eq("status", "0");
                                    sqlutil.eq("type", "otherSystemAddCourse");
                                    sqlutil.eq("flowsn", lcproject.getFlowsn());
                                    List<LcprojectError> updatelist = service.getAllLCProject(sqlutil.getMap(),
                                            LcprojectError.class);
                                    for (LcprojectError updateitem : updatelist) {
                                        AuditProject updateProject = auditProjectServcie
                                                .getAuditProjectByRowGuid(null, updateitem.getProjectguid(), null)
                                                .getResult();

                                        if (updateProject != null
                                                && StringUtil.isNotBlank(updateProject.getStr("record"))) {
                                            if ("01".equals(JSONObject.parseObject(updateProject.getStr("record"))
                                                    .get("status"))) {
                                                updateitem.setStatus("1");
                                                service.update(updateitem);
                                            }
                                        }

                                    }
                                    logger.info("===========同步:" + lcproject.getProjectguid() + "成功 ===========");
                                }

                                EpointFrameDsManager.commit();
                                EpointFrameDsManager.close();
                            }
                            catch (Exception e) {
                                e.printStackTrace();
                            }
                            finally {
                                EpointFrameDsManager.close();
                            }
                        }
                    }

                }
            }
        }
        // 批量重新同步status为04
        for (int i = 0; i < 1; i++) {
            // 查询100条推送失败的记录
            sqlutil.clear();
            sqlutil.eq("status", "0");
            sqlutil.eq("type", "otherSystemAddCourse");
            PageData<LcprojectError> pageData = service.getAllLCProjectByPage(sqlutil.getMap(), LcprojectError.class,
                    i * pageSize, pageSize, null, null);
            if (pageData != null && !pageData.getList().isEmpty()) {
                for (LcprojectError lcproject : pageData.getList()) {
                    logger.info("===========开始批量重新同步04 ===========");
                    AuditProject auditProject = auditProjectServcie
                            .getAuditProjectByRowGuid(null, lcproject.getProjectguid(), null).getResult();

                    if (auditProject != null && StringUtil.isNotBlank(lcproject.getStr("record"))) {
                        if ("04".equals(JSONObject.parseObject(lcproject.getStr("record")).get("status"))) {
                            try {
                                EpointFrameDsManager.begin(null);
                                logger.info("===========重新同步:" + lcproject.getProjectguid() + " ===========");
                                String resultsign = TARequestUtil.sendPostInner(lclcurl, lcproject.getStr("record"), "",
                                        "");
                                JSONObject jsonObject = JSONObject.parseObject(resultsign);
                                JSONObject custom = jsonObject.getJSONObject("custom");
                                if ("1".equals(custom.getString("code"))) {
                                    successCount04++;
                                    sqlutil.clear();
                                    sqlutil.eq("status", "0");
                                    sqlutil.eq("type", "otherSystemAddCourse");
                                    sqlutil.eq("flowsn", lcproject.getFlowsn());
                                    List<LcprojectError> updatelist = service.getAllLCProject(sqlutil.getMap(),
                                            LcprojectError.class);
                                    for (LcprojectError updateitem : updatelist) {
                                        updateitem.setStatus("1");
                                        service.update(updateitem);
                                    }
                                    logger.info("===========同步:" + lcproject.getProjectguid() + "成功 ===========");
                                }

                                EpointFrameDsManager.commit();
                            }
                            catch (Exception e) {
                                e.printStackTrace();
                            }
                            finally {
                                EpointFrameDsManager.close();
                            }
                        }
                    }

                }
            }

        }
        addCallbackParam("msg", "成功重推" + (successCount01 + successCount04) + "条数据！");

    }

    public DataGridModel<LcprojectError> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<LcprojectError>()
            {

                /**
                 * serialVersionUID
                 */
                private static final long serialVersionUID = 1L;

                @Override
                public List<LcprojectError> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    List<Object> conditionList = new ArrayList<Object>();
                    ArrayList<String> projectguids = new ArrayList<>();
                    if(StringUtil.isNotBlank(projectname)) {
                        SqlConditionUtil sql = new SqlConditionUtil();
                        sql.like("PROJECTNAME",projectname);
                        List<AuditProject> auditProjectList = auditProjectServcie.getAuditProjectListByCondition(sql.getMap()).getResult();
                        for (AuditProject auditProject : auditProjectList) {
                            projectguids.add(auditProject.getRowguid());
                        }
                    }
                    String conditionSql = ListGenerator.getSearchSql(getRequestContext().getComponents(),
                            conditionList);
                    if(StringUtil.isNotBlank(projectname)) {
                        if(projectguids!=null&&!projectguids.isEmpty()) {
                            conditionSql += " and projectguid in ("+StringUtil.joinSql(projectguids)+")";
                        } else {
                            conditionSql += " and projectguid = '-1' ";
                        }
                    }

                    sortField = " operatedate ";
                    sortOrder = " desc ";
                    List<LcprojectError> list = service.findList(
                            ListGenerator.generateSql("lcproject_error", conditionSql, sortField, sortOrder), first,
                            pageSize, conditionList.toArray());
                    int count = service.countLcprojectError(ListGenerator.generateSql("lcproject_error", conditionSql),
                            conditionList.toArray());
                    this.setRowCount(count);
                    // 添加事项名称
                    List<LcprojectError> reslist = new ArrayList<>();
                    for (LcprojectError lcproject : list) {
                        AuditProject auditProject = auditProjectServcie
                                .getAuditProjectByRowGuid(null, lcproject.getProjectguid(), null).getResult();
                        lcproject.put("projectname", auditProject != null ? auditProject.getProjectname() : "");
                        reslist.add(lcproject);
                    }
                    return list;
                }

            };
        }
        return model;
    }

    public LcprojectError getDataBean() {
        if (dataBean == null) {
            dataBean = new LcprojectError();
        }
        return dataBean;
    }

    public void setDataBean(LcprojectError dataBean) {
        this.dataBean = dataBean;
    }

    /*public ExportModel getExportModel() {
        if (exportModel == null) {
            exportModel = new ExportModel("", "");
        }
        return exportModel;
    }
    */

    public String getProjectname() {
        return projectname;
    }

    public void setProjectname(String projectname) {
        this.projectname = projectname;
    }
}
