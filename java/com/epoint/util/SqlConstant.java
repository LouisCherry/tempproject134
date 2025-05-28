package com.epoint.util;

/**
 * 常量类
 *
 */
public class SqlConstant
{
	public static final String STR_YES = "1";
    public static final String STR_NO = "0";

    public static final int INT_YES = 1;
    public static final int INT_NO = 0;
    
    public static final String STATUS_DTJCG = "1";
    public static final String STATUS_DTL = "2";
    public static final String STATUS_YTL = "3";
    
    public static final String TYPE_BZ = "BZ";
    public static final String TYPE_GX = "GX";
    public static final String TYPE_ZJ = "ZJ";

    //功能模块类型，标准化为1，业务组件为2，个性化已登记为3,个性化未登记为4(前端用)
    public static final String TYPE_BZ_QD = "1";
    public static final String TYPE_ZJ_QD = "2";
    public static final String TYPE_GX_YDJ_QD = "3";
    public static final String TYPE_GX_WDJ_QD = "4";
    
    

    //sql语句比较符
    public static final String SQL_OPERATOR_EQ = "eq"; //等于
    public static final String SQL_OPERATOR_NQ = "nq"; //不等于
    public static final String SQL_OPERATOR_GT = "gt"; //大于
    public static final String SQL_OPERATOR_GE = "ge"; //大于等于
    public static final String SQL_OPERATOR_LT = "lt"; //小于
    public static final String SQL_OPERATOR_LE = "le"; //小于等于
    public static final String SQL_OPERATOR_LIKE = "like"; //like
    public static final String SQL_OPERATOR_START_WITH = "startwith"; //startwith
    public static final String SQL_OPERATOR_END_WITH= "endwith"; //endwith
    public static final String SQL_OPERATOR_IN = "in"; //in
    public static final String SQL_OPERATOR_NOTIN = "noin"; //in
    public static final String SQL_OPERATOR_BETWEEN = "btw"; //between
    public static final String SQL_OPERATOR_ISNULL = "null"; //为空
    public static final String SQL_OPERATOR_ISNOTNULL = "notn"; //不为空
    
    public static final String PACK_STATU_ONE = "1"; //待提交成果
    public static final String PACK_STATU_TWO = "2"; //已提交成果待提炼
    public static final String PACK_STATU_THREE = "3"; //已提炼
    
    public static final String EVALUAT_STATU_YYZ = "0"; //未验证
    public static final String EVALUAT_STATU_DPJ = "1"; //已验证待评价
    public static final String EVALUAT_STATU_YPJ = "2"; //已评价

    //SQlUtil分割符
    public static final String SPLIT = "#split#";
    //token
    public static final String SysValidateData="Epoint_WebService_**##0601";
    //中台角色类别
    public static final String PLATFORMROLETYPE="中台管理员";
    

}
