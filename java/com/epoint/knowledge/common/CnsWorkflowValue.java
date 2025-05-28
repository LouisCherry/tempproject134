package com.epoint.knowledge.common;

/**
 * 
 * 12345便民服务工作流常量
 * @作者 Administrator
 * @version [版本号, 2017年2月22日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class CnsWorkflowValue
{
    //话务员处办方式枚举
    public static final class HWY_MANAGETYPE
    {
        public static final String DIRECT_ANSWER = "10"; //直接答复
        public static final String SEND_AUDIT = "20"; //送审核
        public static final String SEND_DEPT = "30"; //送部门
    }

    //初审岗处办方式枚举
    public static final class CSG_MANAGETYPE
    {
        public static final String SEND_AUDIT = "40"; //送审核
        public static final String SEND_DEPT = "50"; //送部门
        public static final String DIRECT_FINISH = "60"; //直接办结
        public static final String SEND_AREA = "70"; //送区域热线办
    }

    //终审岗处办方式枚举
    public static final class ZSG_MANAGETYPE
    {
        public static final String SEND_DEPT = "70"; //送部门
        public static final String DIRECT_FINISH = "80"; //直接办结
        public static final String SEND_AREA = "90"; //直接办结
    }

    //工作流每个岗位的备注，主要用来区分在哪个岗位、
    public static final class ACTIVITY_NOTE
    {
        public static final String HWY = "话务员";
        public static final String AUDIT_FIRST = "初审岗";
        public static final String AUDIT_FINAL = "终审岗";
        public static final String AUDIT_AREA = "区热线办";
        public static final String HANDLE_DEPT = "承办单位";
        public static final String HANDLE_LOWER = "下级部门处理";
        public static final String AUDIT_RESULT = "结果审核岗";
        public static final String AUDIT_REPLAY = "回访岗";
        public static final String DUTY_REGISTER= "受理登记";
        public static final String DUTY_FIRST = "值班坐席";
        public static final String DUTY_FINAL = "组长坐席";
    }

    //工作流所有操作备注，主要用来获取opeartionguid
    public static final class OPERATION_NOTE
    {
        public static final String HWY_AUDITFIRST = "话务员送初审";
        public static final String HWY_DEPT = "话务员送部门";
        public static final String HWY_FINISH = "退回办结";
        public static final String AUDITFIRST_AUDITFINAL = "初审送终审";
        public static final String AUDITFIRST_DEPT = "初审送部门";
        public static final String AUDITFIRST_AREA = "初审送区热线办";
        public static final String AUDITFIRST_HWY = "初审送话务员";
        public static final String AUDITFIRST_DIFFICULT = "初审送疑难";
        public static final String AUDITFIRST_FINISH = "初审办结";
        public static final String AUDITFINAL_DEPT = "终审送部门";
        public static final String AUDITFINAL_AREA = "终审送区热线办";
        public static final String AUDITFINAL_FIRST = "终审送初审";
        public static final String AUDITFINAL_DIFFICULT = "终审送疑难";
        public static final String AUDITFINAL_FINISH = "终审办结";
        public static final String DEPT_LOWER = "部门送下级";
        public static final String DEPT_AUDITRESULT = "部门送结果审核";
        public static final String AREA_AUDITRESULT = "区热线办送结果审核";
        public static final String DEPT_HWY = "部门送话务员";
        public static final String DEPT_AUDITFIRST = "部门送初审";
        public static final String DEPT_AUDITFINAL = "部门送终审";
        public static final String AREA_AUDITFIRST = "区热线办送初审";
        public static final String AREA_AUDITFINAL = "区热线办送终审";
        public static final String AREA_BACKSEND = "区热线送退回转派";
        public static final String DEPT_BACKSEND = "部门送退回转派";
        public static final String DEPT_FINSIH = "部门办结";
        public static final String LOWER_DEPT = "下级部门反馈";
        public static final String AUDITRESULT_ARCHIVE = "结果审核送归档";
        public static final String AUDITRESULT_REPLAY = "结果审核送回访";
        public static final String AUDITRESULT_DEPT = "结果审核送部门";
        public static final String AUDITRESULT_DIFFICULT = "结果审核送疑难";
        public static final String AUDITRESULT_FINISH = "结果审核办结";
        public static final String REPLAY_FINISH = "回访办结";
        public static final String ARCHIVE_FINISH = "归档办结";
        public static final String REPLAY_AUDITFINALL = "回访送终审";
        public static final String DIFFICULT_DEPT = "疑难送成员单位";
        public static final String DIFFICULT_AUDITRESULT = "疑难送结果审核";
        public static final String DIFFICULT_AUDITFIRST = "疑难送初审岗";
        public static final String DIFFICULT_AUDITFINAL = "疑难送终审岗";
        public static final String AREA_DEPT = "区热线办送成员单位";
        public static final String DEPT_AREA = "部门送区热线办";
        public static final String SHI_AREA = "市热线办送区热线办";
        public static final String SHI_DEPT = "市热线办送成员单位";
        public static final String AREA_FINISH = "区热线办送结束";
    }

    //工作流知识库操作备注，主要用来获取opeartionguid
    public static final class OPERATION_KNOW_NOTE
    {
        public static final String KNOW_SUBMIT = "知识上报";
        public static final String DIRE_AUDIT_PASS = "直接审核通过";
       
        public static final String KNOW_SUBMIT_AGAIN = "知识再次上报";
        public static final String KNOW_NOTIFY = "通知上报人";
        public static final String DEPT_KNOWEDIT = "市成员单位采编知识";
        public static final String AREA_DEPT_KNOWEDIT = "区成员单位采编知识";
        public static final String KNOW_EDIT = "市热线办采编知识";
        public static final String DEPT_KNOWAUDIT = "市成员单位送市热线办审核";
        public static final String AREA_DEPT_KNOWAUDIT = "区成员单位送区热线办";
        public static final String KNOW_AUDIT = "市热线办送市审核";
        public static final String AUDIT_PASS = "市热线办审核通过";
        public static final String AUDIT_NOTPASS = "市热线办知识采编退回";
        public static final String DEPT_AUDIT_NOTPASS = "成员单位知识采编退回";
        public static final String AREA_AUDIT_PASS = "区热线送市热线办审核";
        public static final String AREA_AUDIT_NOTPASS = "区成员单位知识采编退回";
        public static final String DEPT_KNOWAUDIT_AGAIN = "市成员单位知识重新采编";
        public static final String AREA_DEPT_KNOWAUDIT_AGAIN = "区成员单位知识重新采编";
        public static final String KNOWAUDIT_AGAIN = "市热线办知识重新采编";
        public static final String REQUEST_AREA_DEPT = "市热线办提交区成员单位资料";
        public static final String AREA_REQUEST_AREA_DEPT = "区热线办提交区成员单位资料";
        public static final String REQUEST_DEPT = "市热线办提交市成员单位资料";
        public static final String DEPT_REQUEST = "市热线办送市成员单位采编";
        public static final String AREA_DEPT_REQUEST = "市热线办送区热线办确认";
        public static final String AREA_DEPT_AREAREQUEST = "区热线办送区成员单位采编";
        public static final String AREA_AREA_DEPT_REQUEST = "区热线办确认送区成员单位采编";
        public static final String DEPT_REQUESTBACK = "市成员单位资料退回";
        public static final String AREA_REQUESTBACK = "区热线办资料退回";
        public static final String AREA_DEPT_REQUESTBACK = "区成员单位资料退回区热线审核";
        public static final String AREA_DEPT_REQUESTBACK_AREA = "区成员单位资料退回区热线采编";
        public static final String DEPT_REQUEST_AGAIN = "市热线办重新向市成员单位提交资料";
        public static final String AREA_REQUEST_AGAIN = "市热线办重新向区热线办提交资料";
        public static final String AREA_AREA_DEPT_REQUESTAGAIN ="区热线办审核重新向区成员单位提交资料";
        public static final String AREA_AREA_DEPT_REQUEST_AGAIN = "区热线办采编重新向区成员单位提交资料";
    }

    /**
     * 
     * 工单状态常量
     * @作者 Administrator
     * @version [版本号, 2017年2月24日]
     * @see [相关类/方法]
     * @since [产品/模块版本]
     */
    public static final class CSTATUS
    {
        public static final String HWY_ABANDON = "5";//话务员废单
        public static final String HWY_SAVE = "10";//话务员暂存
    }

    /**
    * 
    * 步骤操作类型
    * @作者 Administrator
    * @version [版本号, 2017年2月28日]
    * @see [相关类/方法]
    * @since [产品/模块版本]
    */
    public static final class OPERATION_STEP_TYPE
    {
        public static final String SEND_AUDIT = "送审核";
        public static final String SEND_DEPT = "派送部门";
        public static final String SEND_AREA = "派送区域热线办";
        public static final String SIGN = "签收";
        public static final String BACK = "退回";
        public static final String APPLY_BACK = "申请退回";
        public static final String AGREE_NOT_BACK = "不同意退回";
        public static final String AGREE__BACK = "同意退回";
        public static final String REPLAY = "送回访";
        public static final String SEND_ARCHIVE = "送归档";
        public static final String ARCHIVE = "归档";
        public static final String FINISH = "办结";
        public static final String SEND_DEPT_OTHER = "转交办";
        public static final String SEND_DEPT_AGSIN = "再交办";
        public static final String DELAY = "申请延时";
        public static final String DELAY_AGREE = "延时申请通过";
        public static final String DELAY_BACK = "延时申请退回";
        public static final String SEND_SUB = "下级处理";
        public static final String SEND_FEEDBACK = "已反馈";
        public static final String CASE_REMIND = "催单";
        public static final String CASE_ADD_CONTENT = "补单";
        public static final String CASE_REVOKE = "撤单";
        public static final String CASE_DIFFICULT = "送疑难工单处理";

    }

    /**
     * 
     * 消息队列处理类型
     * @作者 Administrator
     * @version [版本号, 2017年3月7日]
     * @see [相关类/方法]
     * @since [产品/模块版本]
     */
    public static final class MQ_HANDLE_TYPE
    {
        public static final String CASE_ADD_UPDATE = "10";//新增或者工单
        public static final String TASK_UPDATE = "15";//保存任务单
        public static final String START_WORKFLOW = "30";//启动流程，同时送下一步
        public static final String HANDLE_NEXT = "40"; //直接送下一步
        public static final String CREATE_TASK = "50"; //创建任务单
    }
}
