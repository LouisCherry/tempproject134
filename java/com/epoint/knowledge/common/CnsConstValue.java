package com.epoint.knowledge.common;

import com.epoint.core.utils.config.ConfigUtil;

/**
 * 
 * 12345标准版常量
 * @作者 Administrator
 * @version [版本号, 2017年2月14日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class CnsConstValue
{   
    /**
     * 
     */
    public static final String GXHML = ConfigUtil.getConfigValue("epointframe", "projectPagePath");
    /**
     * 12345标准版是否常量  整形
     */
    public static final String CNS_ONT_STR = "1";
    public static final String CNS_ZERO_STR = "0";

    /**
     * 12345标准版是否常量  整形
     */
    public static final Integer CNS_ONT_INT = 1;
    public static final Integer CNS_ZERO_INT = 0;

    /**
     * 业务主表
     */
    public static final String SQL_TABLE_NAME = "CNS_CINFO";
    
    /**
     * 12345标准版角色分类
     */
    public static final class CnsRole
    {
        public static final String HWY = "话务员";
        public static final String AUDIT_FIRST = "初审岗";
        public static final String AUDIT_FINALL = "终审岗";
        public static final String AUDIT_DELAY = "申请延时审核";
        public static final String AUDIT_RESULT = "结果审核";
        public static final String AUDIT_REPLAY = "回访岗";
        public static final String AUDIT_SUPERVISE = "监察督办";
        public static final String AUDIT_KNOWLEDGE = "知识库审核";
        public static final String HOTLINE = "市热线";
        public static final String HANDLE_DEPT = "成员单位";
        public static final String HANDLE_MANAGER = "成员单位";
        public static final String HANDLE_PERSON = "市成员单位下级";
        public static final String AREA_HANDLE_MANAGER = "区热线";
        public static final String AREADEPT_MANAGER = "区成员单位";
        public static final String AREADEPT_PERSON = "区成员单位下级";
        public static final String AUDIT_CHECK = "质检岗";
        public static final String AUDIT_DIFFICULT = "疑难工单处理人";
        public static final String ACCEPT_SEAT = "受理坐席";
        public static final String DUTY_SEAT = "值班坐席";
        public static final String DUTY_MANAGER = "值班长";
        public static final String KNOWLEDGE_MANAGER = "市成员单位知识库管理";
        public static final String AREA_KNOWLEDGE_MANAGER = "区成员单位知识库管理";
        public static final String KNOWLEDGE_COLLOECT = "市热线办采编";
        public static final String AREA_KNOWLEDGE_COLLOECT = "区热线办采编";
        public static final String KNOWLEDGE_AUDIT = "市热线办审核";
        public static final String AREA_KNOWLEDGE_AUDIT = "区热线办审核";

    }

    /**
     * 12345标准版知识库内容状态
     */
    public static final class KinfoStatus
    {
        public static final String NO_AUDIT = "0";//未审核
        public static final String NEED_REPORT = "10"; //待上报状态
        public static final String IN_AUDIT = "20"; //审核中状态
        public static final String PASS_AUDIT = "30";//审核通过状态
        public static final String BACK_AUDIT = "40";//审核被退回
        public static final String AREA_IN_AUDIT = "50"; //区审核中状态
        //        public static final String AREA_PASS_AUDIT = "50";//区审核通过状态
        //        public static final String AREA_BACK_AUDIT = "60";//区审核被退回
    }

    /**
     * 12345标准版知识库资料提交状态
     */
    public static final class KMaterialStatus
    {
        public static final String NEED_REQUEST = "10";//待提交资料
        public static final String IN_REQUEST = "20";//提交资料中
        public static final String BACK_REQUEST = "30";//资料被退回
    }

    /**
     * 12345标准版星级评价（比如一星，二星...）
     */
    public static final class EvlByStar
    {
        public static final String ONE_STAR = "1";
        public static final String TWO_STAR = "2";
        public static final String THREE_STAR = "3";
        public static final String FOUR_STAR = "4";
        public static final String FIVE_STAR = "5";
    }

    /**
     * 12345标准版知识库操作类型，比如审核，退回，上报等
     */
    public static final class KnowledgeOpt
    {
        public static final String KINFO_REPORT = "上报";//成员单位知识上报
        public static final String AUDIT_BACK = "审核不通过";// 审核机构对不合格知识进行退回
        public static final String AUDIT_PASS = "审核通过";// 审核机构审核通过
        public static final String AUDIT_PROGRESS = "审核中";//审核机构审核中
        public static final String REPORT_AGAIN = "重新上报";//重新上报
        public static final String KINFO_CHANGE = "知识变更";//知识变更
        public static final String KINFO_REQUEST = "提交资料";//热线办提交资料
        public static final String REQUEST_BACK = "资料退回";// 审核机构对不合格知识进行退回
    }

    /**
     * 12345标准版知识库操作节点
     */
    public static final class KnowledgeActivity
    {
        public static final String ACTIVITY_DEPT = "成员单位";//成员单位节点
        public static final String ACTIVITY_AUDIT = "知识库审核";//审核岗节点
    }

    /**
     * 回访类型
     */
    public static final class ReplayType
    {
        public static final String NEED_NO_REPLAY = "10";//无需回访
        public static final String NEED_REPLAY = "20";//正常回访
    }

    /**
     * 性别
     */
    public static final class RQSTSEX
    {
        public static final String RQST_MALE = "1";//男
        public static final String RQST_FEMALE = "2";//女
    }

    /**
     * 反映类型
     */
    public static final class CNS_User_Type
    {
        public static final String PERSON = "10";//个人
        public static final String COMPANY = "20";//企业
    }

    /**
     * 反映类型
     */
    public static final class CNS_Answer_Type
    {
        public static final String TELPHONE = "DH";//电话
        public static final String MESSAGE = "DX";//短信
        public static final String WEBSITE = "WZ";//网上
    }

    /**
     * 12345标准版代码项codeid
     */
    public static final class CNS_CodeIds
    {
        public static final String RQSTAREACODE_CODEID = "30291";//诉求区域代码项
        public static final String ACCORDTYPE_CODEID = "30303";//归口类型代码项
        public static final String RQSTTYPE_CODEID = "30285";//诉求类型代码项
    }

    /**
     * 12345标准版代码项codeName
     */
    public static final class CNS_CodeNames
    {
        public static final String RQSTAREACODE_CODENAME = "市级区域代码";//诉求区域代码项
        public static final String SEX_CODENAME = "性别代码";//诉求区域代码项
        public static final String ACCORDTYPE_CODENAME = "归口类型";//归口类型代码项
        public static final String RQSTTYPE_CODENAME = "诉求类型";//诉求类型代码项
        public static final String RQSTSOURCE_CODENAME = "诉求来源";//诉求来源代码项
        public static final String SHIFOU_CODENAME = "便民是否判断";//便民是否判断代码项
        public static final String REPLAYTYPE_CODENAME = "回访类型";//回访类型代码项
        public static final String MNTTYPE_CODENAME = "监察类型";//监察类型代码项
        public static final String KEYWORD_CODENAME = "关键词";//关键词代码项
        public static final String JXKHOUITEM_CODENAME = "成员单位绩效考核所属项目";//关键词代码项
        public static final String INFOCATEGORY_CODENAME = "知识库信息分类";//知识库信息分类代码项，如：办事指南、常见咨询
        public static final String XXFL_CODENAME = "信息分类优先级";//信息分类优先级代码项
        public static final String SSYXJ_CODENAME = "搜索优先级";//搜索优先级代码项
        public static final String YYL_CODENAME = "引用量优先级";//引用量优先级代码项
        public static final String FBSJ_CODENAME = "发布时间优先级";//发布时间优先级代码项
        public static final String YDL_CODENAME = "阅读量优先级";//阅读量优先级代码项
        public static final String KINFOWEIGHT_CODENAME = "知识检索权重配置";//知识检索权重配置代码项
        public static final String INFOKIND_CODENAME = "知识库信息类别";//知识库信息类别代码项，如：基本信息、收集信息、业务信息
        public static final String INFOMAXKIND_CODENAME = "知识库信息大类别";//知识库信息大类别代码项，如：审批信息知识库
        public static final String KNOWISPUBLIC_CODENAME = "知识是否公开";//知识是否公开代码项
        public static final String KNOWNOTESISPUBLIC_CODENAME = "知识备注是否公开";//知识备注是否公开代码项
        public static final String KCONTENTSTATUS_CODENAME = "知识库内容状态";//知识库内容状态代码项
    }

    /**
     * 
     *工单状态
     * @作者 Administrator
     * @version [版本号, 2017年2月28日]
     * @see [相关类/方法]
     * @since [产品/模块版本]
     */
    public static final class CNS_CaseStatus
    {
        public static final String CASE_PREAUDIT = "0";//微信、app单子预审
        public static final String HWY_ABANDON = "5";//废弃制单状态
        public static final String HWY_SAVE = "10";//工单暂存，只是保存了
        public static final String AUDITFIRST_SEND = "15";//初审岗待交办
        public static final String AUDITFIRST_BACK = "20";//初审岗退回
        public static final String AUDITFINAL_SEND = "25";//终审岗待交办
        public static final String AUDITFINAL_BACK = "30";//终审岗退回
        public static final String DEPT_SIGN = "35";//部门待签收
        public static final String AREA_SIGN = "35";//部门待签收
        public static final String DEPT_HANDLE = "40";//部门处理中
        public static final String DEPT_BACK = "45";//部门退回
        public static final String AUDITRESULT_SNED = "50";//结果审核待交办
        public static final String AUDITRESULT_BACK = "55";//结果审核退回
        public static final String REPLAY_SEND = "60";//待回访
        public static final String REPLAY_NO = "65";//回访不满意
        public static final String CASE_FINISH = "80";//直接办结
        public static final String CASE_DIFFICULT = "90";//疑难工单处理
        public static final String CASE_DIFFICULT_BACK = "95";//疑难工单退回
        public static final String CASE_GUIDANG = "100";//工单归档
    }

    /**
     * 
     * 交办部门方式
     * @作者 Administrator
     * @version [版本号, 2017年3月15日]
     * @see [相关类/方法]
     * @since [产品/模块版本]
     */
    public static final class CNS_SendOu_Type
    {
        public static final String SEND_ZHUXIE = "10";//主协办方式
        public static final String SEND_MANYOU = "20";//多部门方式
    }

    /**
     * 
     * 部门处理方式
     * @作者 Administrator
     * @version [版本号, 2017年3月15日]
     * @see [相关类/方法]
     * @since [产品/模块版本]
     */
    public static final class CNS_OuHandle_Type
    {
        public static final String OU_FINISH = "10";//部门办结
        public static final String OU_BACK = "20";//部门退回
    }

    /**
     * 
     * 任务单状态
     * @作者 Administrator
     * @version [版本号, 2017年3月1日]
     * @see [相关类/方法]
     * @since [产品/模块版本]
     */
    public static final class CNS_TaskStatus
    {
        public static final String DEPT_SIGN = "10";//任务单待签收
        public static final String DEPT_HANDLE = "20";//任务单处理中
        public static final String DEPT_BACK = "30";//任务单退回
        public static final String DEPT_FINISH = "80";//任务单完成
        public static final String DEPT_STOP = "90";//任务单异常终止
        public static final String DEPT_FEEDBACk = "100";//任务单反馈
    }

    /**
     * 
     * 子任务单状态
     * @作者 Administrator
     * @version [版本号, 2017年3月1日]
     * @see [相关类/方法]
     * @since [产品/模块版本]
     */
    public static final class CNS_SubTaskStatus
    {
        public static final String SUB_SIGN = "10";//任务单待签收
        public static final String SUB_HANDLE = "20";//任务单处理中
        public static final String SUB_FINISH = "80";//任务单办结
    }

    /**
     * 
     * 督办状态状态
     * @作者 Administrator
     * @version [版本号, 2017年3月1日]
     * @see [相关类/方法]
     * @since [产品/模块版本]
     */
    public static final class CNS_Supvs_Status
    {
        public static final String SUPVS_WAIT = "10";//待督办
        public static final String SUPVS_HANDLING = "20";//督办处理
        public static final String SUPVS_FINISH = "30";//督办完成
    }

    /**
     * 
     * 质检状态
     * @作者 Administrator
     * @version [版本号, 2017年3月1日]
     * @see [相关类/方法]
     * @since [产品/模块版本]
     */
    public static final class CNS_Check_Status
    {
        public static final String DISTRIBUTE_NO = "0";//未分配
        public static final String CHECK_HANDING = "1";//待质检
        public static final String CHECK_FINISH = "2";//已质检
    }

    /**
     * 
     * 质检状态
     * @作者 Administrator
     * @version [版本号, 2017年3月1日]
     * @see [相关类/方法]
     * @since [产品/模块版本]
     */
    public static final class CNS_Check_Deree
    {
        public static final String CHECKDEGREE_EXCELLENT = "40";
        public static final String CHECKDEGREE_GOOD = "30";
        public static final String CHECKDEGREE_QUALIFIED = "20";
        public static final String CHECKDEGREE_UNQUALIFIED = "10";
    }

    /**
     * 
     * 质检状态
     * @作者 Administrator
     * @version [版本号, 2017年3月1日]
     * @see [相关类/方法]
     * @since [产品/模块版本]
     */
    public static final class CNS_Distribute_Type
    {
        public static final String DISTRIBUTE_AVG = "10";//平均分配
        public static final String DISTRIBUTE_SPE = "20";//指定分配
    }

    /**
     * 
     * 红黄牌状态
     * @作者 Administrator
     * @version [版本号, 2017年8月12日]
     * @see [相关类/方法]
     * @since [产品/模块版本]
     */
    public static final class CNS_REDYELLOW_STATUS
    {
        public static final String WAIT_HANDLE = "10";//待处理
        public static final String HAS_HANDLE = "20";//已处理
        public static final String HAS_OVERTIME = "30";//已超期
    }

    /**
     * 
     * 任务单类型
     * @作者 Administrator
     * @version [版本号, 2017年3月1日]
     * @see [相关类/方法]
     * @since [产品/模块版本]
     */
    public static final class CNS_TaskType
    {
        public static final String TASK_ZHUBAN = "10";//主办任务单
        public static final String TASK_XIEBAN = "20";//协办任务单
    }

    /**
     * 
     * 任务单类型
     * @作者 Administrator
     * @version [版本号, 2017年3月1日]
     * @see [相关类/方法]
     * @since [产品/模块版本]
     */
    public static final class CNS_TaskTarget
    {
        public static final String TASK_QUREXIAN = "10";//区热线
        public static final String TASK_DEPT = "20";//成员单位
        public static final String TASK_QUREXIAN_DEPT = "30";//区热线派送成员单位任务单
        public static final String TASK_DEPT_XIAJI = "40";//成员单位派送下级任务单
        public static final String TASK_QUREXIAN_DEPT_XIAJI = "50";//区成员单位派送下级任务单
    }

    /*
     * Mq队列属性
     */
    public static final class CNS_RabbitMq
    {
        public static final String hostName = ConfigUtil.getConfigValue("cnsbzb", "hostname");
        public static final String userName = ConfigUtil.getConfigValue("cnsbzb", "username");
        public static final String password = ConfigUtil.getConfigValue("cnsbzb", "password");
        public static final String queueName = ConfigUtil.getConfigValue("cnsbzb", "workflow_queue");
        public static final String weixinQueueName = ConfigUtil.getConfigValue("cnsbzb", "weixin_queue");
        public static final String warningQueueName = ConfigUtil.getConfigValue("cnsbzb", "warning_queue");
    }

    /**
     * 
     * @作者 Administrator
     * @version [版本号, 2017年3月10日]
     * @see [相关类/方法]
     * @since [产品/模块版本]
     */
    public static final class CNS_Working_Unit
    {
        public static final String MINUTE = "0";//分钟
        public static final String WORKINGHOUR = "1";//工作时
        public static final String WORKINGDAY = "2";//工作日
    }

    /**
     * 
     * 便民时限配置
     * @作者 Administrator
     * @version [版本号, 2017年3月15日]
     * @see [相关类/方法]
     * @since [产品/模块版本]
     */
    public static final class CNS_Time_Limit
    {
        public static final String TASK_FINISH_LIMIT = "taskfinishlimit";
        public static final String TASK_SIGN_LIMIT = "tasksignlimit";
        public static final String CASE_FINISH_LIMIT = "casefinishlimit";
        public static final String CASE_SEND_LIMIT = "casesendlimit";
        public static final String TASK_BACK_LIMIT = "taskbacklimit";
        public static final String TASK_SENDSUB_LIMIT = "tasksendsublimit";
    }

    /**
     * 
     * 佛山12345时限配置
     * @作者 Administrator
     * @version [版本号, 2017年3月15日]
     * @see [相关类/方法]
     * @since [产品/模块版本]
     */
    public static final class CNS_FormType_Limit
    {
        public static final String OPINION_LIMIT = "opinion"; //初步意见时限
        public static final String OPINION_URGENT_LIMIT = "urgentopinion"; //紧急初步意见时限
        public static final String RESULT_LIMIT = "result"; //结果烦时限
        public static final String RESULT_URGENT_LIMIT = "urgentresult"; //紧急结果反馈时限
        public static final String SEND_LIMIT = "send"; //派单时限
        public static final String SEND_URGENT_LIMIT = "urgentsend"; //紧急判单时限
        public static final String ACCEPT_LIMIT = "accept"; //是否受理时限
        public static final String CASEPUT_LIMIT = "caseput"; //立案时限
    }

    /**
     * 
     * 预警程度
     * @作者 Administrator
     * @version [版本号, 2017年3月15日]
     * @see [相关类/方法]
     * @since [产品/模块版本]
     */
    public static final class CNS_Warning_Degree
    {
        public static final String WARNING_YUJING = "0"; //预警
        public static final String WARNING_YELLOW = "1"; //黄牌
        public static final String WARNING_RED = "2"; //红牌
    }

    /**
     * 
     * 预警程度
     * @作者 Administrator
     * @version [版本号, 2017年3月15日]
     * @see [相关类/方法]
     * @since [产品/模块版本]
     */
    public static final class CNS_Warning_Type
    {
        public static final String WARNING_LEFT = "1"; //剩余
        public static final String WARNING_BEYOND = "2"; //超过
    }

    /**
     * 
     * 佛山12345时限配置
     * @作者 Administrator
     * @version [版本号, 2017年3月15日]
     * @see [相关类/方法]
     * @since [产品/模块版本]
     */
    public static final class CNS_FormType
    {
        public static final String FORM_ZIXUN = "zixun"; //咨询
        public static final String FORM_SUQIU = "suqiu"; //诉求
        public static final String FORM_JYXC = "jyxc"; //建言献策
        public static final String FORM_ZXTS = "xzts"; //行政投诉
        public static final String FORM_JJWFJB = "jjwfjb"; //经济违法举报
        public static final String FORM_XFWQJB = "xfwqjb"; //消费维权举报
    }

    /**
     * 
     * 佛山12345时限配置
     * @作者 Administrator
     * @version [版本号, 2017年3月15日]
     * @see [相关类/方法]
     * @since [产品/模块版本]
     */
    public static final class CNS_Focus_Type
    {
        public static final String HIDENAME = "01";//匿名
        public static final String HIDENUM = "02";//匿电话
        public static final String URGENT = "03"; //紧急
        public static final String SUPERVISE = "04"; //督办
        public static final String TEPAI = "05";//特派
        public static final String REPORT = "06";//上报
    }

    /**
    * 
    * 申请延时时长单位
    * @作者 Administrator
    * @version [版本号, 2017年3月10日]
    * @see [相关类/方法]
    * @since [产品/模块版本]
    */
    public static final class CNS_Delay_Type
    {
        public static final String DELAY_FINISH = "10";//办结延时
        public static final String DELAY_BACK = "20";//退回延时
    }

    /**
    * 
    *  诉求来源代码项
    * @作者 Administrator
    * @version [版本号, 2017年3月15日]
    * @see [相关类/方法]
    * @since [产品/模块版本]
    */
    public static final class CNS_RqstSource
    {
        public static final String TELPHONE = "DH";//人工电话
        public static final String ZWZX = "ZW";//政务咨询
        public static final String WEBSITE = "WZ";//网站
        public static final String WEIXIN = "WX";//微信
        public static final String APP = "AP";//app
        public static final String WEIBO = "WB";//微博
        public static final String EMAIL = "YJ";//电子邮件
    }

    /**
    * 
    *  诉求类型代码项
    * @作者 Administrator
    * @version [版本号, 2017年3月15日]
    * @see [相关类/方法]
    * @since [产品/模块版本]
    */
    public static final class CNS_RqstType
    {
        public static final String ZIXUN = "10";//咨询
        public static final String JIANYI = "15";//建议
        public static final String TOUSU = "20";//投诉
        public static final String JUBAO = "25";//举报
        public static final String QIUZHU = "30";//求助
        public static final String BIAOYANG = "35";//表扬
    }

    /**
    * 
    *  回访类型代码项
    * @作者 Administrator
    * @version [版本号, 2017年3月15日]
    * @see [相关类/方法]
    * @since [产品/模块版本]
    */
    public static final class CNS_ReplayType
    {
        public static final String REPLAY_NO = "10";//无需回访
        public static final String REPLAY_YES = "20";//正常回访
    }

    /**
     * 
     *  监察规则适用范围代码项
     * @作者 Administrator
     * @version [版本号, 2017年3月15日]
     * @see [相关类/方法]
     * @since [产品/模块版本]
     */
    public static final class CNS_Monitor_UseTo
    {
        public static final String USERTO_FWDB = "1";//适用于服务代表
        public static final String USERTO_OU = "2";//适用于成员单位
    }

    /**
     * 
     *  计量单位代码项
     * @作者 Administrator
     * @version [版本号, 2017年3月15日]
     * @see [相关类/方法]
     * @since [产品/模块版本]
     */
    public static final class CNS_Monitor_Unit
    {
        public static final String UNIT_WORKDAY = "1";//工作日
        public static final String UNIT_HOUR = "2";//小时
        public static final String UNIT_MINUTE = "3";//分钟
        public static final String UNIT_TIMES = "4";//次
        public static final String UNIT_PERCENT = "5";//%
    }

    /**
     * 
     *  知识提问答复状态
     * @作者 Administrator
     * @version [版本号, 2017年3月15日]
     * @see [相关类/方法]
     * @since [产品/模块版本]
     */
    public static final class CNS_Answer_Status
    {
        public static final String ANSWER_WAIT = "10";//待答复
        public static final String ANSWER_FINISH = "20";//已答复
        public static final String ANSWER_REFUSE = "30";//拒绝答复
    }

    /**
     * 
     *  交办方式
     * @作者 Administrator
     * @version [版本号, 2017年3月15日]
     * @see [相关类/方法]
     * @since [产品/模块版本]
     */
    public static final class CNS_Send_TaskType
    {
        public static final String DIRE_SEND = "10";
        public static final String ZAIJIAOBAN = "20";
        public static final String ZHUANJIAOBAN = "30";
    }

    /**
     * 
     *  交办方式
     * @作者 Administrator
     * @version [版本号, 2017年3月15日]
     * @see [相关类/方法]
     * @since [产品/模块版本]
     */
    public static final class CNS_Jxkh_BelongPosition
    {
        public static final String JXKH_OU = "dept";
        public static final String JXKH_HWY = "hwy";
    }

    /**
     * 
     *  成员单位绩效考核项目
     * @作者 Administrator
     * @version [版本号, 2017年3月15日]
     * @see [相关类/方法]
     * @since [产品/模块版本]
     */
    public static final class CNS_Jxkh_HwyItem
    {
        public static final String TEL_NUM = "telNum"; //话务量
        public static final String QUALITY_CHECK = "qualityCheck"; //质检成绩
        public static final String STYLE_SATISFY = "styleSatisfy"; //满意率
        public static final String TEST = "test"; //考试
        public static final String PRAISE_COMPLAIN = "praiseComplain"; //投诉、表扬
        public static final String WORKATTITUDE = "workAttitude"; //工作态度
        public static final String WORKQUALITY = "workQuality"; //工作素质
        public static final String WORKSKILL = "workSkill"; //工作技能
        public static final String WORKRESULT = "workResult"; //工作质量
    }

    /**
     * 
     *  成员单位绩效考核项目
     * @作者 Administrator
     * @version [版本号, 2017年3月15日]
     * @see [相关类/方法]
     * @since [产品/模块版本]
     */
    public static final class CNS_Jxkh_OuItem
    {
        public static final String SIGN_INTIME = "sign"; //及时签收
        public static final String SEND_INTIME = "send"; //及时交办
        public static final String BACK_INTIME = "back"; //及时退单
        public static final String FINISH_INTIME = "finish"; //按时办结
        public static final String ATTITUDE_SATIFY = "attitude";//态度满意度
        public static final String RESULT_SATIFY = "result"; //态度满意度
    }

    /**
     * 
     *  成员单位绩效考核项目
     * @作者 Administrator
     * @version [版本号, 2017年3月15日]
     * @see [相关类/方法]
     * @since [产品/模块版本]
     */
    public static final class CNS_Jxkh_RangeCodeName
    {
        public static final String HWY_TELNUM_RANGE = "绩效考核话务量区间";
        public static final String HWY_QUALITYCHECK_RANGE = "绩效考核质检区间";
        public static final String HWY_STYLESTAFY_RANGE = "绩效考核作风满意度区间";
    }
    public static final class CNS_Kinfo_Abandon
    {
      
        public static final String Abandon_HAND = "3";
    }

}
