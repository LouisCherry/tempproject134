package com.epoint.zoucheng.znsb.worktablecomment.util;

/**
 * 排队叫号常量类
 * 
 * @author Administrator
 * 
 */
public class QueueConstant {
	/**
	 * 常量
	 */
	public static final String CONSTANT_STR_ONE = "1";
	public static final String CONSTANT_STR_ZERO = "0";
	public static final int CONSTANT_INT_ONE = 1;
	public static final int CONSTANT_INT_ZERO = 0;
	
	public static final String CONSTANT_STR_TWO = "2";

	/**
	 * 是否
	 */
	public static final String Common_yes_String = "1";
	public static final int Common_yes_Int = 1;
	public static final String Common_no_String = "0";
	public static final int Common_no_Int = 0;
	
	/**
	 * 排队叫号参数规范:默认空办理人员
	 */
	public static final String Current_None = "无办理人员！";// 默认空办理人员
	/**
	 * 设备类型
	 */
	public static final String EQUIPMENT_TYPE_QHJ = "1";// 取号机
	public static final String EQUIPMENT_TYPE_CKP = "2";// 窗口屏
	public static final String EQUIPMENT_TYPE_YTJ = "3";// 一体机
	public static final String EQUIPMENT_TYPE_PJPAD = "4";// 评价PAD
	public static final String EQUIPMENT_TYPE_DDP = "5";// 等待屏
	public static final String EQUIPMENT_TYPE_YuYin = "6";// 语音机顶盒
	public static final String EQUIPMENT_TYPE_YDPS = "7";// 智能引导终端(竖)
	public static final String EQUIPMENT_TYPE_YDPH = "8";// 智能引导终端(横)
	public static final String EQUIPMENT_TYPE_JHPAD = "9";// 叫号PAD
	public static final String EQUIPMENT_TYPE_YDJ = "10";// 电子样单机
	public static final String EQUIPMENT_TYPE_CABINET = "11";// 智能存储柜
	public static final String EQUIPMENT_TYPE_YTJ24 = "12";// 24小时无人值守终端
	public static final String EQUIPMENT_TYPE_ROBOT = "13";// 智能机器人
	/**
	 * app类型
	 */
	public static final String TerminalApp_Type_Window = "1";// 窗口屏app
	public static final String TerminalApp_Type_QueuePad = "2";// 叫号padapp	
	public static final String TerminalApp_Type_Evaluation = "3";// 评价app	
	public static final String TerminalApp_Type_Sample = "4";// 样单机
	public static final String TerminalApp_Type_ZMQHJ = "5";// 桌面取号机
	public static final String TerminalApp_Type_CXP = "6";// 查询屏
	
	/**
	 * 窗口状态
	 */
	public static final String Window_WorkStatus_NotLogin = "0";// 未登录
	public static final String Window_WorkStatus_Free = "1";// 空闲	
	public static final String Window_WorkStatus_Processing  = "2";// 正在处理	
	public static final String Window_WorkStatus_WaitProcess = "3";// 等待处理	
	public static final String Window_WorkStatus_Pause = "4";// 暂停	
	
	/**
	 * 工具条状态
	 */
	public static final String Window_Bar_status_Pause = "暂离！";// 未登录
	public static final String Window_Bar_status_NotLogin = "未登录！";// 空闲	
	public static final String Window_Bar_status_None  = "无办理人员！";// 无办理人员		
	
	
	/**
	 * 取号状态
	 */
	public static final String Qno_Status_Init = "0";// 未分配;	
	public static final String Qno_Status_Processing = "1";// 正在处理;
	public static final String Qno_Status_Processed = "2";// 处理完成;
	public static final String Qno_Status_Pass = "3";// 过号;
	/**
	 * 设备状态
	 */
	public static final String EQUIPMENT_STATUS_OFFLINE = "0";// 离线
	public static final String EQUIPMENT_STATUS_ONLINE = "1";// 在线
	
	/**
	 * 预约状态
	 */
	public static final String Appoint_Status_Init = "0";// 未取号
	public static final String Appoint_Status_Process = "1";// 已取号
	public static final String Appoint_Status_Pass = "2";// 过号
	public static final String Appoint_Status_Delete = "3";// 删除
	
	/**
	 * 评价器推送状态
	 */
	public static final String Evaluate_Status_Login = "1";// 登录
	public static final String Evaluate_Status_QueRen = "2";// 申请人确认
	public static final String Evaluate_Status_Evaluate = "3";// 评价
	public static final String Evaluate_Status_Pause = "4";// 暂停
	public static final String Evaluate_Status_OnLine = "5";// 在线
	public static final String Evaluate_Status_HandWrite = "6";// 签批
	
	/**
	 * 窗口屏推送状态
	 */
	public static final String WindowAPK_Status_Off = "0";// 关机
	public static final String WindowAPK_Status_JS = "1";// 页面js刷新
	public static final String WindowAPK_Status_Restart = "2";// apk重启
	public static final String WindowAPK_Status_Onoff = "3";// 设置自动开关机
	
	/**
	 * 安卓日志推送状态
	 */
	
	public static final String Logfile_Status_Today = "todaylog";// 上传当天的日志
	public static final String Logfile_Status_Allday = "alldaylog";// 上传所有的日志
	
	/**
	 * 取号人员类型
	 */
	public static final String Qno_Type_Common = "1";// 普通号;	
	public static final String Qno_Type_AP = "2";// 预约号;
	public static final String Qno_Type_VIP = "3";// VIP号;
	public static final String Qno_Type_LOVE = "4";// 爱心绿色通道号;
	
	/**
	 * 设备当前状态
	 */
	public static final String EQUIPMENT_CurrentStatus_Offline = "0";// 离线;	
	public static final String EQUIPMENT_CurrentStatus_Normal = "1";// 正常;	
	public static final String EQUIPMENT_CurrentStatus_Warn = "2";// 警告;	
	public static final String EQUIPMENT_CurrentStatus_Error = "3";// 异常;	
	
	/**
	 * 取号机读卡器类型
	 */
	public static final String QHJ_READCARD_HS = "1";// 华视;	
	public static final String QHJ_READCARD_JL = "2";// 精伦;	
	
	 /**
     * 一体机热敏状态
     */
    public static final String RM_STUTAS_ZERO = "0";//缺纸
    public static final String RM_STUTAS_ONE = "1";//纸将尽
    public static final String RM_STUTAS_TWO = "2";//纸量充足
    /**
     * 远程协助状态
     */
    public static final String AIO_STUTAS_ZERO = "0";
    public static final String AIO_STUTAS_ONE = "1";
    public static final String AIO_STUTAS_TWO = "2";
    
    
    /**
     * 接口测试状态
     */
    public static final String INTERFACE_NORMAL = "1";
    public static final String INTERFACE_ABNORMAL = "0";
    public static final String INTERFACE_REST_POST = "1";
    public static final String INTERFACE_REST_GET = "2";
    public static final String INTERFACE_WEBSERVICE_POST = "3";
    public static final String INTERFACE_WEBSERVICE_GET = "4";
    public static final String INTERFACE_ERROR_TIMEOUT = "1";
    public static final String INTERFACE_ERROR_CANTREACH= "2";
    public static final String INTERFACE_ERROR_NOTMATCH = "3";
    public static final String INTERFACE_PARAM_KEYVALUE = "1";
    public static final String INTERFACE_PARAM_TEXT= "2";
    /**
     * 视频图片类型
     */
    public static final String VIDEO_TYPE = "1";// 视频; 
    public static final String PIC_TYPE = "2";// 图片;
    
    /**
     * 智能存储柜盒子
     */
    public static final String CABINETBOX_NO_FILE = "1";// 无单
    public static final String CABINETBOX_HAVE_FILE = "2"; // 有单
    public static final String CABINETBOX_ABNORMAL = "0"; // 异常
    public static final String FILE_CERTFILE = "1";// 相关证照
    public static final String FILE_APPLYFILE = "2"; // 申报材料
    
    /**
     * 转号的类型
     */
    public static final String QUEUE_TURN_TASK = "1";// 办理的事项改变
    public static final String QUEUE_TURN_WINDOW = "2"; // 办理的窗口改变
    
    /**
     * 转号的优先级
     */
    public static final String TURN_PRIORITY_ORIGIN= "1";// 队列最前
    public static final String TURN_PRIORITY_LAST= "2"; // 队列最后
    
    /**
     * 首页模块 类型
     */
    public enum ModuleType
    {        
        政务服务("1"),
        公共服务("2"),
        便民服务("3"),
        其他服务("4");
        
        public String value;
        
        ModuleType(String s) {
            value = s;
        }
    }
    
    /**
     * 引导屏页面参数类型
     */
    public enum YdpContentType
    {        
        中心简介("0"),
        新闻动态("1"),
        配套服务("2"),
        中心简介横版("3");
        
        public String value;
        
        YdpContentType(String s) {
            value = s;
        }
    }
    
    /**
     * 指令接收 类型
     */
    public enum ReceiveType
    {        
        已接收("1"),
        未接收("0"),
        等待接收("2");       
        public String value;
        
        ReceiveType(String s) {
            value = s;
        }
    }
    
    /**
     * 寄存件类型
     */
    public enum MaterialType
    {        
        相关证照("1"),
        申报材料("2");       
        public String value;
        
        MaterialType(String s) {
            value = s;
        }
    }
    
    
}
