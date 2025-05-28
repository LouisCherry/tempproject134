package com.epoint.audittask.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.controller.BaseController;
import com.epoint.common.zwfw.workflow.AuditWorkflowBizlogic;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.json.JsonUtil;
import com.epoint.core.utils.string.StringUtil;

/**
 * 
 * 主要是生成json字符串传入到leftwizard控件中。 必须传入的参数有name（左边流程名字），code（左边流程的代码），
 * url（右边加载的url地址），status（导航栏的状态，包括4种， active 编辑状态；done 完成状态 （已处理）；default
 * 默认状态（未处理） disable 锁定状态（失效）） showPrev(是否显示上一步按钮，传入boolean类型)
 * showNext(是否显示下一步按钮，传入boolean类型) showSubmit(是否显示提交按钮，传入boolean类型)
 * showArrow(是否显示箭头，传入boolean类型) 如果按钮不够，请自行在leftWizard_snippet.html模板页面添加按钮。
 * 
 * @author Administrator
 * @version [版本号, 2016年10月11日]
 
 标板待审核未添加情形类别
 */
@RestController("jnaudittasktopwizardshaction")
@Scope("request")
public class JNAuditTaskTopWizardSHAction extends BaseController
{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    private AuditWorkflowBizlogic auditWorkflowBizlogic = new AuditWorkflowBizlogic();
    
    @Autowired
    private IAuditTask auditTaskBasicImpl;

    /**
     * 事项guid
     */
    private String taskGuid = "";
    /**
     * 操纵
     */
    private String operation = "";
    /**
     * 部门guid，新增时候需要
     */
    private String ouguid = "";

    /**
     * 事项复制过来的rowguid
     */
    private String copyTaskGuid = "";
    /**
     * 事项id
     */
    private String taskId = "";

    /**
     * 初始化按钮区域
     */
    private Map<String, Object> handleControl = new HashMap<String, Object>();

    /**
     * url参数
     */
    private String urlParam = "";

    /**
     * 事项基本信息
     */
    AuditTask auditTask = new AuditTask();


    @Override
    public void pageLoad() {
        // 当前操作类型
        operation = this.getRequestParameter("operation");
        // 对于不同的操作类型，按钮的显示应该有所不同；
        handleControl = auditWorkflowBizlogic.initHandleControl(handleControl, operation);
        ouguid = this.getRequestParameter("ouguid");
        // 一些数据的初始化
        taskGuid = this.getRequestParameter("taskGuid");
        // 中心人员事项变更和窗口事项变更如果存在一条正在编辑的事项，copyTaskGuid不为空
        copyTaskGuid = this.getRequestParameter("copyTaskGuid");

        //这里是直接获取的数据库数据
        auditTask = auditTaskBasicImpl.getAuditTaskByGuid(taskGuid, false).getResult();

        taskId = auditTask.getTask_id();
        // 中心人员事项变更和窗口事项变更如果存在一条正在编辑的事项，copyTaskGuid不为空

        // 通用的url传参，用到的都写上，如果没有值也没关系
        urlParam = "?taskGuid=" + taskGuid + "&ouguid=" + ouguid + "&taskId=" + taskId + "&operation=" + operation
                + "&copyTaskGuid=" + copyTaskGuid;

    }

    /**
     * 
     * 加载各个模块的信息
     * 
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public void initWizardData() {
        List<Record> recordList = new ArrayList<Record>();

        // 基本信息
        Record basic = new Record();
        basic.put("name", "基本信息");
        basic.put("code", "basic");
        // 新增页面的操作
        if (StringUtil.isBlank(copyTaskGuid) || taskGuid.equals(copyTaskGuid)) {
			basic.put("url", "epointzwfw/audittask/basic/audittaskbasicedit" + urlParam);
		}
        // 变更页面，需要复制事项的，需要传timestamp
        else {
            basic.put("url", "epointzwfw/audittask/basic/audittaskbasiccompare" + urlParam);
        }
        basic.put("status", "active");
        basic.put("showArrow", this.isShowArrow(operation));
        basic.put("showPrev", this.isShowPrev(operation));
        basic.put("showNext", this.isShowNext(operation));
        basic.put("showSubmit", this.isShowSubmit(operation));
        basic.put("showSave", this.isShowSave(operation));
        basic.put("showPass", this.isShowPass(operation));
        basic.put("showNotPass", this.isShowNotPass(operation));
        Map<String, Object> basicHandleControl = new HashMap<String, Object>(16);
        basicHandleControl.putAll(handleControl);
        basic.put("handleControl", basicHandleControl);
        recordList.add(basic);

        // 办理流程
        Record bllc = new Record();
        bllc.put("name", "办理流程");
        bllc.put("code", "bllc");

        if (StringUtil.isBlank(copyTaskGuid) || taskGuid.equals(copyTaskGuid)) {
            bllc.put("url", "epointzwfw/audittask/workflow/audittaskriskpointlist" + urlParam);
        }
        else {
            bllc.put("url", "epointzwfw/audittask/workflow/audittaskriskpointcompare" + urlParam);
        }
        bllc.put("handleControl", handleControl);
        bllc.put("status", this.getStatusByOpr(operation));
        bllc.put("showPrev", this.isShowPrev(operation));
        bllc.put("showNext", this.isShowNext(operation));
        bllc.put("showSubmit", this.isShowSubmit(operation));
        bllc.put("showArrow", this.isShowArrow(operation));
        bllc.put("showPass", this.isShowPass(operation));
        bllc.put("showNotPass", this.isShowNotPass(operation));
        recordList.add(bllc);

        // 系统设置
        Record xtsz = new Record();
        xtsz.put("name", "系统设置");
        xtsz.put("code", "xtsz");
        if (StringUtil.isBlank(copyTaskGuid) || taskGuid.equals(copyTaskGuid)) {
            xtsz.put("url", "epointzwfw/audittask/extend/audittaskconfig" + urlParam);
        }
        else {
            xtsz.put("url", "epointzwfw/audittask/extend/audittaskconfigcompare" + urlParam);
        }
        xtsz.put("status", this.getStatusByOpr(operation));
        xtsz.put("showArrow", this.isShowArrow(operation));
        Map<String, Object> extendHandleControl = new HashMap<String, Object>(16);
        extendHandleControl.putAll(handleControl);
        xtsz.put("status", this.getStatusByOpr(operation));
        xtsz.put("showPrev", this.isShowPrev(operation));
        xtsz.put("showNext", this.isShowNext(operation));
        xtsz.put("showSubmit", false);
        xtsz.put("handleControl", extendHandleControl);
        xtsz.put("showArrow", this.isShowArrow(operation));
        xtsz.put("showSave", this.isShowSave(operation));
        xtsz.put("showPass", this.isShowPass(operation));
        xtsz.put("showNotPass", this.isShowNotPass(operation));
        recordList.add(xtsz);

        // 申报材料
        Record sbcl = new Record();
        sbcl.put("name", "申报材料");
        sbcl.put("code", "sbcl");
        // 新增操作
        if (StringUtil.isBlank(copyTaskGuid) || taskGuid.equals(copyTaskGuid)) {
            sbcl.put("url", "epointzwfw/audittask/material/audittaskmateriallist" + urlParam);
        }
        else {
            sbcl.put("url", "epointzwfw/audittask/material/audittaskmaterialcompare" + urlParam);
        }
        sbcl.put("status", this.getStatusByOpr(operation));
        sbcl.put("showArrow", this.isShowArrow(operation));
        sbcl.put("handleControl", handleControl);
        sbcl.put("showPrev", this.isShowPrev(operation));
        sbcl.put("showNext", this.isShowNext(operation));
        sbcl.put("showSubmit", this.isShowSubmit(operation));
        sbcl.put("showPass", this.isShowPass(operation));
        sbcl.put("showNotPass", this.isShowNotPass(operation));
        recordList.add(sbcl);
        
        // 情形类别
        Record ysxx = new Record();
        ysxx.put("name", "情形类别");
        ysxx.put("code", "ysxx");
        // 新增操作
        if (StringUtil.isBlank(copyTaskGuid) || taskGuid.equals(copyTaskGuid)) {
            ysxx.put("url", "epointzwfw/audittask/element/audittaskelementandoption" + urlParam);
        }
        else {
            ysxx.put("url", "epointzwfw/audittask/element/audittaskelementandoption" + urlParam);
        }
        ysxx.put("status", this.getStatusByOpr(operation));
        ysxx.put("showArrow", this.isShowArrow(operation));
        ysxx.put("handleControl", handleControl);
        ysxx.put("showPrev", this.isShowPrev(operation));
        ysxx.put("showNext", this.isShowNext(operation));
        ysxx.put("showSubmit", this.isShowSubmit(operation));
        ysxx.put("showPass", this.isShowPass(operation));
        ysxx.put("showNotPass", this.isShowNotPass(operation));
        recordList.add(ysxx);
        
        // 收费项目
        Record sfxm = new Record();
        sfxm.put("name", "收费项目");
        sfxm.put("code", "sfxm");
        // 新增操作
        if (StringUtil.isBlank(copyTaskGuid) || taskGuid.equals(copyTaskGuid)) {
            sfxm.put("url", "epointzwfw/audittask/fee/audittaskchargeitemlist" + urlParam);
        }
        else {
            sfxm.put("url", "epointzwfw/audittask/fee/audittaskchargeitemcompare" + urlParam);
        }
        sfxm.put("status", this.getStatusByOpr(operation));
        sfxm.put("showArrow", this.isShowArrow(operation));
        sfxm.put("handleControl", handleControl);
        sfxm.put("showPrev", this.isShowPrev(operation));
        sfxm.put("showNext", this.isShowNext(operation));
        sfxm.put("showSubmit", this.isShowSubmit(operation));
        sfxm.put("showPass", this.isShowPass(operation));
        sfxm.put("showNotPass", this.isShowNotPass(operation));
        recordList.add(sfxm);

        // 文书配置
        Record wspz = new Record();
        wspz.put("name", "文书配置");
        wspz.put("code", "wspz");
        if (StringUtil.isBlank(copyTaskGuid) || taskGuid.equals(copyTaskGuid)) {
            wspz.put("url", "epointzwfw/audittask/docs/audittaskdoclist" + urlParam);
        }
        else {
            wspz.put("url", "epointzwfw/audittask/docs/audittaskdoccompare" + urlParam);
        }
        wspz.put("status", this.getStatusByOpr(operation));
        wspz.put("showArrow", this.isShowArrow(operation));
        wspz.put("handleControl", handleControl);
        wspz.put("showPrev", this.isShowPrev(operation));
        wspz.put("showNext", this.isShowNext(operation));
        wspz.put("showSubmit", this.isShowSubmit(operation));
        wspz.put("showPass", this.isShowPass(operation));
        wspz.put("showNotPass", this.isShowNotPass(operation));
        recordList.add(wspz);

        // 审批结果
        Record spjg = new Record();
        spjg.put("name", "审批结果");
        spjg.put("code", "spjg");
        if (StringUtil.isBlank(copyTaskGuid) || taskGuid.equals(copyTaskGuid)) {
            spjg.put("url", "epointzwfw/audittask/result/auditresultadd" + urlParam);
        }
        else {
            spjg.put("url", "epointzwfw/audittask/result/auditresultcompare" + urlParam);
        }

        spjg.put("status", this.getStatusByOpr(operation));
        spjg.put("showArrow", this.isShowArrow(operation));
        spjg.put("handleControl", handleControl);
        spjg.put("showPrev", this.isShowPrev(operation));
        spjg.put("showNext", this.isShowNext(operation));
        spjg.put("showSubmit", this.isShowSubmit(operation));
        spjg.put("showSave", this.isShowSave(operation));
        spjg.put("showPass", this.isShowPass(operation));
        spjg.put("showNotPass", this.isShowNotPass(operation));
        recordList.add(spjg);

        // 常见问题
        Record cjwt = new Record();
        cjwt.put("name", "常见问题");
        cjwt.put("code", "cjwt");
        if (StringUtil.isBlank(copyTaskGuid) || taskGuid.equals(copyTaskGuid)) {
            cjwt.put("url", "epointzwfw/audittask/faq/audittaskfaqlist" + urlParam);
        }
        else {
            cjwt.put("url", "epointzwfw/audittask/faq/audittaskfaqcompare" + urlParam);
        }
        cjwt.put("status", this.getStatusByOpr(operation));
        cjwt.put("showArrow", this.isShowArrow(operation));
        cjwt.put("handleControl", handleControl);
        cjwt.put("showPrev", this.isShowPrev(operation));
        cjwt.put("showNext", this.isShowNext(operation));
        cjwt.put("showSubmit", this.isShowSubmit(operation));
        cjwt.put("showPass", this.isShowPass(operation));
        cjwt.put("showNotPass", this.isShowNotPass(operation));
        recordList.add(cjwt);
        sendRespose(JsonUtil.listToJson(recordList));
    }

    public String getTaskGuid() {
        return taskGuid;
    }

    public void setTaskGuid(String taskGuid) {
        this.taskGuid = taskGuid;
    }

    /**
     * 
     * 根据操作判断流程的状态，如果不为add，则状态为active，如果为add，则状态为default
     * 
     * @param operator
     * @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    private String getStatusByOpr(String operator) {
    	return "done";
    }

    /**
     * 
     * 是否显示箭头的按钮，当操作类型为detail是不显示箭头
     * 
     * @param operation
     *            操作类型
     * @return boolean
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    private boolean isShowArrow(String operation) {
    	return true;
    }

    /**
     * 
     * 是否显示上一步的按钮，当操作类型为detail不显示上一步
     * 
     * @param operation
     *            操作类型
     * @return boolean
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    private boolean isShowPrev(String operation) {
    	return false;
    }

    /**
     * 
     * 是否显示下一步的按钮，当操作类型为detail不显示下一步
     * 
     * @param operation
     *            操作类型
     * @return boolean
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    private boolean isShowNext(String operation) {
    	return false;
    }

    /**
     * 
     * 是否显示提交的按钮，当操作类型为edit显示提交
     * 
     * @param operation
     *            操作类型
     * @return boolean
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    private boolean isShowSubmit(String operation) {
    	return false;
    }

    /**
     * 
     * 是否显示箭头的按钮，当操作类型为detail是不显示箭头
     * 
     * @param operation
     *            操作类型
     * @return boolean
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    private boolean isShowSave(String operation) {
    	return true;
    }

    /**
     * 
     * 是否显示箭头的按钮，当操作类型为detail是不显示箭头
     * 
     * @param operation
     *            操作类型
     * @return boolean
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    private boolean isShowPass(String operation) {
    	return true;
    }

    /**
     * 
     * 是否显示箭头的按钮，当操作类型为detail是不显示箭头
     * 
     * @param operation
     *            操作类型
     * @return boolean
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    private boolean isShowNotPass(String operation) {
    	return true;
    }
}
