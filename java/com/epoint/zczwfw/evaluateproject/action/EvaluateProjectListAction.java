package com.epoint.zczwfw.evaluateproject.action;

import java.io.InputStream;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.export.ExportModel;
import com.epoint.basic.faces.fileupload.FileUploadModel9;
import com.epoint.basic.faces.tree.DefaultFileUploadHandlerImpl9;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.StringUtil;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.core.EpointFrameDsManager;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.httpclient.HttpUtil;
import com.epoint.core.utils.web.WebUtil;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import com.epoint.frame.service.attach.entity.FrameAttachStorage;
import com.epoint.frame.service.message.api.IMessagesCenterService;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.frame.service.metadata.mis.util.ListGenerator;
import com.epoint.frame.service.metadata.systemparameters.api.IConfigService;
import com.epoint.zczwfw.evaluateproject.api.IEvaluateProjectService;
import com.epoint.zczwfw.evaluateproject.api.entity.EvaluateProject;
import com.epoint.zczwfw.evaluateprojecterr.api.IEvaluateProjectErrService;
import com.epoint.zczwfw.evaluateprojecterr.api.entity.EvaluateProjectErr;
import com.epoint.zczwfw.tsuimportinfo.api.ITsuImportInfoService;
import com.epoint.zczwfw.tsuimportinfo.api.entity.TsuImportInfo;
import com.epoint.zczwfw.util.ZcEvaluateConstant;

/**
 * 评价办件信息表list页面对应的后台
 * 
 * @author yrchan
 * @version [版本号, 2022-04-11 15:52:28]
 */
@RestController("evaluateprojectlistaction")
@Scope("request")
public class EvaluateProjectListAction extends BaseController
{
    private static final long serialVersionUID = -451865688835591446L;

    /**
     * 每个线程最多处理数据数量
     */
    private static final int xcCount = 2000;

    /**
     * 日志
     */
    private static Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());

    @Autowired
    private IEvaluateProjectService iEvaluateProjectService;

    /**
     * 导入反馈信息表API
     */
    @Autowired
    private ITsuImportInfoService iTsuImportInfoService;

    /**
     * 评价办件异常信息表API
     */
    @Autowired
    private IEvaluateProjectErrService iEvaluateProjectErrService;
    /**
     * 附件API
     */
    @Autowired
    private IAttachService iAttachService;

    /**
     * 系统参数API
     */
    @Autowired
    private IConfigService iConfigService;

    /**
     * 相关消息API
     */
    @Autowired
    private IMessagesCenterService iMessagesCenterService;

    /**
     * 代码项API
     */
    @Autowired
    private ICodeItemsService iCodeItemsService;

    /**
     * 评价办件信息表实体对象
     */
    private EvaluateProject dataBean;

    /**
     * 表格控件model
     */
    private DataGridModel<EvaluateProject> model;

    /**
     * 办件评价来源下拉列表model
     */
    private List<SelectItem> project_sourceModel = null;

    /**
     * 评价结果下拉列表model
     */
    private List<SelectItem> evaluate_resultModel = null;

    /**
     * 短信发送状态下拉列表model
     */
    private List<SelectItem> is_sendModel = null;

    /**
     * 办件导入：上传附件
     */
    private FileUploadModel9 fileUploadModel;

    /**
     * 导出模型
     */
    private ExportModel exportModel;

    /**
     * 搜索条件，时间
     */
    private Date acceptDateStart;
    private Date acceptDateEnd;

    private Date handleDateStart;
    private Date handleDateEnd;

    private Date evaluateDateStart;
    private Date evaluateDateEnd;

    private Date creatDateStart;
    private Date creatDateEnd;

    public void pageLoad() {
    }

    public DataGridModel<EvaluateProject> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<EvaluateProject>()
            {

                private static final long serialVersionUID = 4513112501517567356L;

                @Override
                public List<EvaluateProject> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    List<Object> conditionList = new ArrayList<Object>();
                    String conditionSql = "";
                    // 来源
                    if (StringUtil.isNotBlank(dataBean.getProject_source())) {
                        conditionSql += " and project_source = ? ";
                        conditionList.add(dataBean.getProject_source());
                    }
                    // 评价结果
                    if (StringUtil.isNotBlank(dataBean.getEvaluate_result())) {
                        conditionSql += " and evaluate_result = ? ";
                        conditionList.add(dataBean.getEvaluate_result());
                    }
                    // 短信发送状态
                    if (StringUtil.isNotBlank(dataBean.getIs_send())) {
                        conditionSql += " and is_send = ? ";
                        conditionList.add(dataBean.getIs_send());
                    }
                    // 受理人
                    if (StringUtil.isNotBlank(dataBean.getAccept_user())) {
                        conditionSql += " and accept_user = ? ";
                        conditionList.add(dataBean.getAccept_user());
                    }
                    // 受理所属部门
                    if (StringUtil.isNotBlank(dataBean.getAccept_department())) {
                        conditionSql += " and accept_department like ? ";
                        conditionList.add("%" + dataBean.getAccept_department() + "%");
                    }
                    // 事项名称
                    if (StringUtil.isNotBlank(dataBean.getTask_name())) {
                        conditionSql += " and task_name like ? ";
                        conditionList.add("%" + dataBean.getTask_name() + "%");
                    }

                    // 受理时间
                    if (acceptDateStart != null) {
                        conditionSql += " and accept_date >= ? ";
                        conditionList.add(EpointDateUtil.convertDate2String(
                                EpointDateUtil.getBeginOfDate(acceptDateStart), EpointDateUtil.DATE_TIME_FORMAT));
                    }
                    if (acceptDateEnd != null) {
                        conditionSql += " and accept_date <= ? ";
                        conditionList.add(EpointDateUtil.convertDate2String(EpointDateUtil.getEndOfDate(acceptDateEnd),
                                EpointDateUtil.DATE_TIME_FORMAT));
                    }

                    // 办结时间
                    if (handleDateStart != null) {
                        conditionSql += " and handle_date >= ? ";
                        conditionList.add(EpointDateUtil.convertDate2String(
                                EpointDateUtil.getBeginOfDate(handleDateStart), EpointDateUtil.DATE_TIME_FORMAT));
                    }
                    if (handleDateEnd != null) {
                        conditionSql += " and handle_date <= ? ";
                        conditionList.add(EpointDateUtil.convertDate2String(EpointDateUtil.getEndOfDate(handleDateEnd),
                                EpointDateUtil.DATE_TIME_FORMAT));
                    }

                    // 评价时间
                    if (evaluateDateStart != null) {
                        conditionSql += " and evaluate_date >= ? ";
                        conditionList.add(EpointDateUtil.convertDate2String(
                                EpointDateUtil.getBeginOfDate(evaluateDateStart), EpointDateUtil.DATE_TIME_FORMAT));
                    }
                    if (evaluateDateEnd != null) {
                        conditionSql += " and evaluate_date <= ? ";
                        conditionList.add(EpointDateUtil.convertDate2String(
                                EpointDateUtil.getEndOfDate(evaluateDateEnd), EpointDateUtil.DATE_TIME_FORMAT));
                    }

                    // 创建时间/导入时间
                    if (creatDateStart != null) {
                        conditionSql += " and creat_date >= ? ";
                        conditionList.add(EpointDateUtil.convertDate2String(
                                EpointDateUtil.getBeginOfDate(creatDateStart), EpointDateUtil.DATE_TIME_FORMAT));
                    }
                    if (creatDateEnd != null) {
                        conditionSql += " and creat_date <= ? ";
                        conditionList.add(EpointDateUtil.convertDate2String(EpointDateUtil.getEndOfDate(creatDateEnd),
                                EpointDateUtil.DATE_TIME_FORMAT));
                    }

                    // 联系人
                    if (StringUtil.isNotBlank(dataBean.getLink_user())) {
                        conditionSql += " and link_user like ? ";
                        conditionList.add("%" + dataBean.getLink_user() + "%");
                    }

                    // 联系电话
                    if (StringUtil.isNotBlank(dataBean.getLink_phone())) {
                        conditionSql += " and link_phone like ? ";
                        conditionList.add("%" + dataBean.getLink_phone() + "%");
                    }

                    // 身份证号/统一社会信用代码
                    if (StringUtil.isNotBlank(dataBean.getApply_id())) {
                        conditionSql += " and apply_id like ? ";
                        conditionList.add("%" + dataBean.getApply_id() + "%");
                    }

                    List<EvaluateProject> list = iEvaluateProjectService.findList(
                            ListGenerator.generateSql("evaluate_project", conditionSql, sortField, sortOrder), first,
                            pageSize, conditionList.toArray());
                    if (!list.isEmpty()) {
                        for (EvaluateProject evaluateProject : list) {
                            if (StringUtil.isNotBlank(evaluateProject.getEvaluate_result())) {
                                String itemText = iCodeItemsService.getItemTextByCodeName("评价结果",
                                        evaluateProject.getEvaluate_result());
                                if (StringUtil.isNotBlank(itemText)) {
                                    evaluateProject.set("evaluate_result", itemText);
                                }
                            }
                        }
                    }

                    int count = iEvaluateProjectService.countEvaluateProject(
                            ListGenerator.generateSql("evaluate_project", conditionSql), conditionList.toArray());
                    this.setRowCount(count);
                    return list;
                }

            };
        }
        return model;
    }

    /**
     * 
     * [附件上传]
     * 
     * @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public FileUploadModel9 getFileUploadModel() {
        if (fileUploadModel == null) {
            String cliengGuid = UUID.randomUUID().toString();
            fileUploadModel = new FileUploadModel9(new DefaultFileUploadHandlerImpl9(cliengGuid,
                    userSession.getUserGuid(), userSession.getDisplayName()));
        }
        return fileUploadModel;
    }

    /**
     * 
     * 导入完成
     * 
     * @param attachGuid
     */
    public void fileUp(String attachGuid) {
        FrameAttachStorage frameAttachStorage = iAttachService.getAttach(attachGuid);
        if (frameAttachStorage == null) {
            return;
        }

        TsuImportInfo tsuImportInfo = new TsuImportInfo();
        tsuImportInfo.setRowguid(UUID.randomUUID().toString());
        tsuImportInfo.setOperatedate(new Date());
        tsuImportInfo.setOperateusername(userSession.getDisplayName());

        tsuImportInfo.setImp_datetime(new Date());
        tsuImportInfo.setImp_user(userSession.getDisplayName());
        tsuImportInfo.setImp_user_guid(userSession.getUserGuid());
        // 导入处理进度，1：待处理，2：处理中，3：处理完成
        tsuImportInfo.setImp_processing(ZcEvaluateConstant.impProcessing.DCL);
        tsuImportInfo.setClient_guid(frameAttachStorage.getCliengGuid());
        iTsuImportInfoService.insert(tsuImportInfo);

        // 调用导入信息处理异步处理服务
        dataImport(tsuImportInfo.getRowguid());
    }

    /**
     * 
     * 办件导入处理逻辑
     * 
     * @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public void dataImport(String impGuid) {
        // 1.获取导入反馈信息数据
        TsuImportInfo tsuImportInfo = iTsuImportInfoService.find(impGuid);
        if (tsuImportInfo == null) {
            return;
        }
        List<FrameAttachInfo> attachInfoList = iAttachService.getAttachInfoListByGuid(tsuImportInfo.getClient_guid());
        if (attachInfoList.isEmpty()) {
            return;
        }
        String attachGuid = attachInfoList.get(0).getAttachGuid();

        try {
            // 2.更新对应记录imp_processing为2
            tsuImportInfo.setImp_processing(ZcEvaluateConstant.impProcessing.DCZ);
            iTsuImportInfoService.update(tsuImportInfo);

            // 3.后台获取excel附件后解析
            InputStream fileData = HttpUtil.doHttp(WebUtil.getRequestCompleteUrl(request)
                    + "/rest/frame/base/attach/attachAction/getContent?isCommondto=true&attachGuid=" + attachGuid, null,
                    "get", 1);
            // 获取Excel工作簿对象
            HSSFWorkbook workbook = new HSSFWorkbook(fileData);
            // 得到Excel工作表对象
            HSSFSheet sheetAt = workbook.getSheetAt(0);

            // 获得总行数
            int rowNum = sheetAt.getLastRowNum() + 1;
            if (sheetAt != null && rowNum > 0) {
                log.info("====================整理差评信息,多少条数据====================" + sheetAt.getLastRowNum());
                // 需要多少个线程
                int threadnum = rowNum % xcCount == 0 ? rowNum / xcCount : (rowNum / xcCount + 1);

                if (rowNum <= xcCount) {
                    threadnum = 1;
                }
                this.handleProject(tsuImportInfo, sheetAt, threadnum, impGuid);
            }
            log.info("====================结束整理差评信息====================");

            workbook.close();
        }
        catch (Exception e) {
            log.error("=======办件导入处理逻辑dataImport出现异常信息：" + e.getMessage() + "=======", e);
            addCallbackParam("errormsg", "办件导入发送异常，请联系管理员！");
            iTsuImportInfoService.deleteByGuid(impGuid);
            iAttachService.deleteAttachByAttachGuid(attachGuid);
        }

    }

    /**
     * 
     * 线程处理数据
     * 
     * @param tsuImportInfo
     * 
     * @param workbook
     * 
     * @param sheetAt
     *            差评数据
     * @param threadNum
     *            线程数量
     * @param impGuid
     */
    public synchronized void handleProject(TsuImportInfo tsuImportInfo, HSSFSheet sheetAt, int threadNum,
            String impGuid) {
        int length = sheetAt.getLastRowNum() + 1;
        // 定义线程池
        CountDownLatch countDownLatch = new CountDownLatch(threadNum);
        for (int i = 0; i < threadNum; i++) {
            int end = (i + 1) * xcCount;
            HandleThread thread = new HandleThread("线程[" + (i + 1) + "] ", sheetAt, i * xcCount,
                    end > length ? length : end, countDownLatch, impGuid);
            thread.start();
        }

        try {
            countDownLatch.await();

            // 6.入库完成之后更新对应记录imp_processing为3，finish_datetime为当前时间；
            tsuImportInfo.setImp_processing(ZcEvaluateConstant.impProcessing.CLWC);
            tsuImportInfo.setFinish_datetime(new Date());
            iTsuImportInfoService.update(tsuImportInfo);

            // 7. 给导入人员发送附件入库完成待办消息 【通知】评价办件已处理完成，请查看 ；待办地址为5.3、办件导入通知页面
            // 待办地址为:办件导入通知页面
            String handleUrl = "jiningzwfw/zcevaluate/evaluateproject/evaluateprojectimpdetail?impGuid=" + impGuid
                    + "&totalCount=" + sheetAt.getLastRowNum();
            iMessagesCenterService.insertWaitHandleMessage(UUID.randomUUID().toString(), "【通知】评价办件已处理完成",
                    IMessagesCenterService.MESSAGETYPE_WAIT, userSession.getUserGuid(), userSession.getDisplayName(),
                    userSession.getUserGuid(), userSession.getDisplayName(), "【通知】评价办件已处理完成", handleUrl,
                    userSession.getOuGuid(), null, 1, null, null, impGuid, null, new Date(), null,
                    userSession.getUserGuid(), null, null);
            addCallbackParam("msg", "导入成功！");

        }
        catch (Exception e) {
            Thread.currentThread().interrupt();
            log.error("======线程服务handleProject出现异常===========" + e.getMessage(), e);
        }
    }

    /**
     * 
     * 业务处理逻辑
     * 
     * @author yrchan
     * @version 2021年11月23日
     */
    public class HandleThread extends Thread
    {
        private String threadName;
        private HSSFSheet sheetAt;
        private int start;
        private int end;
        private CountDownLatch countDownLatch;
        private String impGuid;

        public HandleThread(String threadName, HSSFSheet sheetAt, int start, int end, CountDownLatch countDownLatch,
                String impGuid) {
            this.threadName = threadName;
            this.sheetAt = sheetAt;
            this.start = start;
            this.end = end;
            this.countDownLatch = countDownLatch;
            this.impGuid = impGuid;
        }

        /**
         * 业务处理逻辑
         */
        @Override
        public void run() {
            EvaluateProjectErr evaluateProjectError = null;

            // 获取系统参数：评价短信发送模板
            String content = iConfigService.getFrameConfigValue("EAVL_MSG_CONTENT");

            Date nowDate = new Date();
            // 时分秒
            String hourMS = EpointDateUtil.convertDate2String(nowDate, "HH:mm:ss");

            // 循环读取表格数据
            for (int i = start; i < end; i++) {
                Row row = sheetAt.getRow(i);
                // 首行（即表头）不读取
                if (row.getRowNum() == 0) {
                    continue;
                }
                evaluateProjectError = new EvaluateProjectErr();
                String accept_user = "";
                String accept_department = "";
                String task_name = "";
                String apply_object = "";
                String apply_id = "";
                String link_user = "";
                String link_phone = "";

                try {
                    evaluateProjectError.setCreat_date(nowDate);

                    // 读取当前行中单元格数据，索引从0开始
                    row.getCell(0).setCellType(CellType.STRING);
                    accept_user = row.getCell(0).getStringCellValue();
                    evaluateProjectError.setAccept_user(accept_user);

                    row.getCell(1).setCellType(CellType.STRING);
                    accept_department = row.getCell(1).getStringCellValue();
                    evaluateProjectError.setAccept_department(accept_department);

                    row.getCell(2).setCellType(CellType.STRING);
                    task_name = row.getCell(2).getStringCellValue();
                    evaluateProjectError.setTask_name(task_name);

                    row.getCell(3).setCellType(CellType.STRING);
                    apply_object = row.getCell(3).getStringCellValue();
                    evaluateProjectError.setApply_object(apply_object);

                    row.getCell(4).setCellType(CellType.STRING);
                    apply_id = row.getCell(4).getStringCellValue();
                    evaluateProjectError.setApply_id(apply_id);

                    row.getCell(7).setCellType(CellType.STRING);
                    link_user = row.getCell(7).getStringCellValue();
                    evaluateProjectError.setLink_user(link_user);

                    row.getCell(8).setCellType(CellType.STRING);
                    link_phone = row.getCell(8).getStringCellValue();
                    evaluateProjectError.setLink_phone(link_phone);

                    row.getCell(5).setCellType(CellType.NUMERIC);
                    row.getCell(6).setCellType(CellType.NUMERIC);
                }
                catch (Exception e) {
                    log.error("---------办件导入处理逻辑线程出现异常---------" + e.getMessage(), e);
                    if (evaluateProjectError != null) {
                        // 失败
                        evaluateProjectError.setRowguid(UUID.randomUUID().toString());
                        evaluateProjectError.setOperatedate(nowDate);
                        evaluateProjectError.setOperateusername(userSession.getDisplayName());
                        evaluateProjectError.setImp_guid(impGuid);
                        // 4.校验未通过，评价办件异常信息表（evaluate_project_err）
                        iEvaluateProjectErrService.insert(evaluateProjectError);
                        EpointFrameDsManager.commit();
                    }
                    continue;
                }
                finally {
                    EpointFrameDsManager.close();
                }

                EvaluateProject evaluateProject = new EvaluateProject();
                EvaluateProjectErr evaluateProjectErr = new EvaluateProjectErr();

                try {
                    String acceptDate = "";
                    // 是日期格式
                    if (HSSFDateUtil.isCellDateFormatted(row.getCell(5))) {
                        double value = row.getCell(5).getNumericCellValue();
                        Date da = DateUtil.getJavaDate(value);
                        acceptDate = EpointDateUtil.convertDate2String(da, EpointDateUtil.DATE_FORMAT);
                        evaluateProjectError
                                .setAccept_date(EpointDateUtil.convertString2Date(acceptDate + " " + hourMS));
                    }

                    String handleDate = "";
                    // 是日期格式
                    if (HSSFDateUtil.isCellDateFormatted(row.getCell(6))) {
                        double value = row.getCell(6).getNumericCellValue();
                        Date da = DateUtil.getJavaDate(value);
                        handleDate = EpointDateUtil.convertDate2String(da, EpointDateUtil.DATE_FORMAT);
                        evaluateProjectError
                                .setHandle_date(EpointDateUtil.convertString2Date(handleDate + " " + hourMS));
                    }

                    // 3.1、解析验证数据
                    if (StringUtil.isBlank(accept_user) || StringUtil.isBlank(accept_department)
                            || StringUtil.isBlank(task_name) || StringUtil.isBlank(apply_object)
                            || StringUtil.isBlank(apply_id) || StringUtil.isBlank(acceptDate)
                            || StringUtil.isBlank(handleDate) || StringUtil.isBlank(link_user)
                            || StringUtil.isBlank(link_phone)) {
                        // 3.1.1、数据必填校验
                        evaluateProjectErr.clear();
                        evaluateProjectErr.setRowguid(UUID.randomUUID().toString());
                        evaluateProjectErr.setOperatedate(nowDate);
                        evaluateProjectErr.setOperateusername(userSession.getDisplayName());
                        evaluateProjectErr.setImp_guid(impGuid);

                        evaluateProjectErr.setCreat_date(nowDate);
                        evaluateProjectErr.setAccept_user(accept_user);
                        evaluateProjectErr.setAccept_department(accept_department);
                        evaluateProjectErr.setTask_name(task_name);
                        evaluateProjectErr.setApply_object(apply_object);
                        evaluateProjectErr.setApply_id(apply_id);

                        // 判断是否符合日期格式，"2021-12-08";
                        String regex = "^[0-9]{4}-[0-9]{1,2}-[0-9]{1,2}$";
                        if (StringUtil.isNotBlank(acceptDate)) {
                            boolean isMatch = Pattern.matches(regex, acceptDate);
                            if (isMatch) {
                                evaluateProjectErr
                                        .setAccept_date(EpointDateUtil.convertString2Date(acceptDate + " " + hourMS));
                            }
                        }

                        if (StringUtil.isNotBlank(handleDate)) {
                            boolean isHandleDateMatch = Pattern.matches(regex, handleDate);
                            if (isHandleDateMatch) {
                                evaluateProjectErr
                                        .setHandle_date(EpointDateUtil.convertString2Date(handleDate + " " + hourMS));
                            }
                        }

                        evaluateProjectErr.setLink_user(link_user);
                        evaluateProjectErr.setLink_phone(link_phone);
                        // 4.校验未通过，评价办件异常信息表（evaluate_project_err）
                        iEvaluateProjectErrService.insert(evaluateProjectErr);
                        EpointFrameDsManager.commit();
                    }
                    else {
                        // 3.1.2、都不是空，判断 受理时间、办结时间是否为yyyy-mm-dd格式,联系电话校验
                        // 判断是否符合日期格式，"2021-12-08";
                        String regex = "^[0-9]{4}-[0-9]{1,2}-[0-9]{1,2}$";
                        boolean isMatch = Pattern.matches(regex, acceptDate);
                        boolean isHandleDateMatch = Pattern.matches(regex, handleDate);

                        // 联系电话校验
                        String phonePartten = "^1[3|4|5|7|8][0-9]{9}$";
                        boolean isPhoneMatch = Pattern.matches(phonePartten, link_phone);

                        if (!isMatch || !isHandleDateMatch || !isPhoneMatch) {
                            // 验证不通过，存放在评价办件异常信息表（evaluate_project_err）
                            evaluateProjectErr.clear();
                            evaluateProjectErr.setRowguid(UUID.randomUUID().toString());
                            evaluateProjectErr.setOperatedate(nowDate);
                            evaluateProjectErr.setOperateusername(userSession.getDisplayName());
                            evaluateProjectErr.setImp_guid(impGuid);

                            evaluateProjectErr.setCreat_date(nowDate);
                            evaluateProjectErr.setAccept_user(accept_user);
                            evaluateProjectErr.setAccept_department(accept_department);
                            evaluateProjectErr.setTask_name(task_name);
                            evaluateProjectErr.setApply_object(apply_object);
                            evaluateProjectErr.setApply_id(apply_id);

                            // 判断是否符合日期格式，"2021-12-08";
                            if (isMatch) {
                                evaluateProjectErr
                                        .setAccept_date(EpointDateUtil.convertString2Date(acceptDate + " " + hourMS));
                            }

                            if (isHandleDateMatch) {
                                evaluateProjectErr
                                        .setHandle_date(EpointDateUtil.convertString2Date(handleDate + " " + hourMS));
                            }

                            evaluateProjectErr.setLink_user(link_user);
                            evaluateProjectErr.setLink_phone(link_phone);
                            // 4.校验未通过，评价办件异常信息表（evaluate_project_err）
                            iEvaluateProjectErrService.insert(evaluateProjectErr);
                            EpointFrameDsManager.commit();
                        }
                        else {
                            // 5。存放在评价办件信息表（evaluate_project）
                            evaluateProject.clear();
                            evaluateProject.setRowguid(UUID.randomUUID().toString());
                            evaluateProject.setOperatedate(nowDate);
                            evaluateProject.setOperateusername(userSession.getDisplayName());

                            evaluateProject.setCreat_date(nowDate);
                            evaluateProject.setAccept_user(accept_user);
                            evaluateProject.setAccept_department(accept_department);
                            evaluateProject.setTask_name(task_name);
                            evaluateProject.setApply_object(apply_object);
                            evaluateProject.setApply_id(apply_id);
                            evaluateProject
                                    .setAccept_date(EpointDateUtil.convertString2Date(acceptDate + " " + hourMS));
                            evaluateProject
                                    .setHandle_date(EpointDateUtil.convertString2Date(handleDate + " " + hourMS));
                            evaluateProject.setLink_user(link_user);
                            evaluateProject.setLink_phone(link_phone);
                            // 是否发送短信：1
                            evaluateProject.setIs_send(ZwfwConstant.CONSTANT_INT_ONE);
                            // 是否评价：0：未评价
                            evaluateProject.setIs_evaluate(ZwfwConstant.CONSTANT_INT_ZERO);
                            // 评价来源：1：导入办件，2：审批办件
                            evaluateProject.setProject_source(ZwfwConstant.CONSTANT_INT_ONE);

                            // 编号 。办件编号IMP+时间+7位序列 例如：IMP202204070000001
                            String no = "IMP" + EpointDateUtil.convertDate2String(evaluateProject.getHandle_date(),
                                    EpointDateUtil.DATE_NOSPLIT_FORMAT);
                            // 获取编号
                            SqlConditionUtil sql = new SqlConditionUtil();
                            sql.leftLike("project_no", no);
                            String maxNo = iEvaluateProjectService.getMaxNoByCondition(sql.getMap());
                            if (StringUtil.isBlank(maxNo)) {
                                maxNo = no + "0000001";
                            }
                            else {
                                // 序号
                                int count1 = Integer.parseInt(maxNo.substring(11, maxNo.length())) + 1;
                                if (count1 > 0 && count1 < 10) {
                                    maxNo = no + "000000" + count1;
                                }
                                else if (count1 >= 10 && count1 < 100) {
                                    maxNo = no + "00000" + count1;
                                }
                                else if (count1 >= 100 && count1 < 1000) {
                                    maxNo = no + "0000" + count1;
                                }
                                else if (count1 >= 1000 && count1 < 10000) {
                                    maxNo = no + "000" + count1;
                                }
                                else if (count1 >= 10000 && count1 < 100000) {
                                    maxNo = no + "00" + count1;
                                }
                                else if (count1 >= 100000 && count1 < 1000000) {
                                    maxNo = no + "0" + count1;
                                }
                                else {
                                    maxNo = no + count1;
                                }
                            }
                            evaluateProject.setProject_no(maxNo);

                            // 5.判断办结时间为同一天且同一个手机号的评价办件信息是否存在
                            boolean isExistBj = iEvaluateProjectService.isExistPhoneAndHandleDate(link_phone,
                                    handleDate);
                            // 若存在，则将成功信息入库在评价办件信息表（evaluate_project）表中,不发短信
                            if (isExistBj) {
                                iEvaluateProjectService.insert(evaluateProject);
                            }
                            else {
                                // 若不存在，则将成功信息入库在评价办件信息表（evaluate_project）表中,并发短信
                                iEvaluateProjectService.insert(evaluateProject);
                                // 发短信
                                String contentText = content.replaceAll("#=TASK_NAME=#",
                                        evaluateProject.getTask_name());
                                iMessagesCenterService.insertSmsMessage(UUID.randomUUID().toString(), contentText,
                                        nowDate, 0, nowDate, evaluateProject.getLink_phone(), "-",
                                        evaluateProject.getLink_user(), userSession.getUserGuid(),
                                        userSession.getDisplayName(), evaluateProject.getLink_phone(),
                                        userSession.getOuGuid(), "", true, "370883");
                            }
                            EpointFrameDsManager.commit();
                        }

                    }
                    EpointFrameDsManager.commit();
                }
                catch (Exception e) {
                    log.error("---------办件导入处理逻辑线程出现异常---------" + e.getMessage(), e);
                    if (evaluateProjectError != null) {
                        // 失败
                        evaluateProjectError.setRowguid(UUID.randomUUID().toString());
                        evaluateProjectError.setOperatedate(nowDate);
                        evaluateProjectError.setOperateusername(userSession.getDisplayName());
                        evaluateProjectError.setImp_guid(impGuid);
                        // 4.校验未通过，评价办件异常信息表（evaluate_project_err）
                        iEvaluateProjectErrService.insert(evaluateProjectError);
                        EpointFrameDsManager.commit();
                    }
                }
                finally {
                    EpointFrameDsManager.close();
                }

            }

            log.info(threadName + "处理完" + (end - start) + "条数据！");
            countDownLatch.countDown();
        }

    }

    public EvaluateProject getDataBean() {
        if (dataBean == null) {
            dataBean = new EvaluateProject();
        }
        return dataBean;
    }

    public void setDataBean(EvaluateProject dataBean) {
        this.dataBean = dataBean;
    }

    /**
     * 导出
     * 
     * @return
     */
    public ExportModel getExportModel() {
        if (exportModel == null) {
            exportModel = new ExportModel(
                    "project_source,project_no,accept_user,accept_department,task_name,apply_object,apply_id,accept_date,handle_date,creat_date,link_user,link_phone,evaluate_date,evaluate_result,is_send",
                    "来源,办件编号,受理人,受理所属部门,事项名称,申请人或申请单位,身份证号/统一社会信用代码,受理时间,办结时间,创建时间/导入时间,联系人,联系电话,评价时间,评价结果,短信发送状态");
        }
        return exportModel;
    }

    /**
     * 代码项：【评价办件来源】
     * 
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<SelectItem> getProject_sourceModel() {
        if (project_sourceModel == null) {
            project_sourceModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "评价办件来源", null, true));
        }
        return this.project_sourceModel;
    }

    /**
     * 代码项：【评价结果】
     * 
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<SelectItem> getEvaluate_resultModel() {
        if (evaluate_resultModel == null) {
            evaluate_resultModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "评价结果", null, true));
        }
        return this.evaluate_resultModel;
    }

    /**
     * 代码项：【短信发送状态】
     * 
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<SelectItem> getIs_sendModel() {
        if (is_sendModel == null) {
            is_sendModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "短信发送状态", null, true));
        }
        return this.is_sendModel;
    }

    public Date getAcceptDateStart() {
        return acceptDateStart;
    }

    public void setAcceptDateStart(Date acceptDateStart) {
        this.acceptDateStart = acceptDateStart;
    }

    public Date getAcceptDateEnd() {
        return acceptDateEnd;
    }

    public void setAcceptDateEnd(Date acceptDateEnd) {
        this.acceptDateEnd = acceptDateEnd;
    }

    public Date getHandleDateStart() {
        return handleDateStart;
    }

    public void setHandleDateStart(Date handleDateStart) {
        this.handleDateStart = handleDateStart;
    }

    public Date getHandleDateEnd() {
        return handleDateEnd;
    }

    public void setHandleDateEnd(Date handleDateEnd) {
        this.handleDateEnd = handleDateEnd;
    }

    public Date getEvaluateDateStart() {
        return evaluateDateStart;
    }

    public void setEvaluateDateStart(Date evaluateDateStart) {
        this.evaluateDateStart = evaluateDateStart;
    }

    public Date getEvaluateDateEnd() {
        return evaluateDateEnd;
    }

    public void setEvaluateDateEnd(Date evaluateDateEnd) {
        this.evaluateDateEnd = evaluateDateEnd;
    }

    public Date getCreatDateStart() {
        return creatDateStart;
    }

    public void setCreatDateStart(Date creatDateStart) {
        this.creatDateStart = creatDateStart;
    }

    public Date getCreatDateEnd() {
        return creatDateEnd;
    }

    public void setCreatDateEnd(Date creatDateEnd) {
        this.creatDateEnd = creatDateEnd;
    }

}
