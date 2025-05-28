package com.epoint.tazwfw.electricity.rest.service;

public class ElectricityConstant
{
    //审批系统状态
    //办件状态：整改办结
    public static final int BANJIAN_STATUS_ZGBJ = 100;
    //接口状态：成功
    public static final String HTTPOK = "200";
    //接口状态：失败
    public static final String HTTPERROR = "300";
    //办件操作：办结
    public static final String FINISHTYPE = "60";

    //电力状态 办件状态：99办结 5受理 4驳回 11在办 10未办
    public static final String DLBJ = "99";
    public static final String DLSL = "5";
    public static final String DLBH = "4";
    public static final String DLZB = "11";
    public static final String DLWB = "10";

    // 基本信息要素字段
    static String basicInfoColumn = " ROWGUID,PARENTROWGUID,SERVICEOBJ1,SERVICEOBJ2,CONSNAME1,CONSNO1,MOBILE,CERTTYPE1,CERTNO1,CONSNAME2,CONSNO2,CERTTYPE2,CERTNO2,CORPORATEREP,CORPORATECER,CORPORATEMOBILE,AGENTNAME,AGENTCER,AGENTMOBILE,ELECADDR,BUSITYPECODE,ELECTYPE,TCAP1,ORGNCAP1,APPCAP1,TCAP2,ORGNCAP2,APPCAP2,TCAPBF,VAT,APPCAPBF,HANDLENAME,APPNO,ACCEPTTIME";
    // 用电类别要素字段
    static String elecTypeColumn = " ROWGUID,PARENTROWGUID ,TCAP, ORGNCAP, APPCAP";
    // 规划许可申请内容要素字段
    static String ghxkColumn = " ROWGUID,PARENTROWGUID,PROORG, PROUSCI, PROENTNAME, PROENTADDR, PROLENTH, PRORA, PROVOL,PROMAT, PROLINETYPE, PROCONNAME, PROCONADDR, PROCONPER, PROCONMOB,PROREMARK ";
    // 线路走径图及说明proLineList要素字段
    static String proLineColumn = " ROWGUID, PARENTROWGUID,PROLINENO,PROLINENAME,PROLINEADDR,PROLINEST,PROLINELEN,PROLINEDE,PROLINERA,PROLINETI,PROLINEHI,PROLINEZ";
    // 线路走径图及说明proBuilist要素字段
    static String proBuilColumn = " ROWGUID, PARENTROWGUID,PROBUINO,PROBUINAME,PROBUIADDR,PROBUIAREA,PROBUIARCH,PROBUIP,PROBUILEN,PROBUIWI,PROBUIHI,PROBUIZ";
    // 绿地占用申请表要素字段
    static String ldzyColumn = " ROWGUID,PARENTROWGUID,GREECONNAME,GREEUSCI,GREECONPER,GREEMOL,GREEOFF,GREESITE,GREEAREA,GREETIME,GREEREA,GREECONREMARK ";
    // 施工起止时间要素字段
    static String greenInfoColumn = " ROWGUID, PARENTROWGUID ,GREENINFOTYPE,GREENINFORA,GREENINFONU,GREENINFOADDR";
    // 占路许可申请表要素字段
    static String zlxkColumn = " OPERATEDATE,ROWGUID,PARENTROWGUID,ROADCONPER,ROADMOL,ROADOFF,ROADSITE,ROADTIME,ROADLEN01,ROADWI01,ROADAREA01,ROADLEN02,ROADWI02,ROADAREA02,ROADLEN03,ROADWI03,ROADAREA03,ROADLEN04,ROADWI04,ROADAREA04,ROADCONAPT,ROADREA,ROADREMARK";
    // 临时占用城市道路许可申请表要素字段
    static String lszyColumn = " ROWGUID,PARENTROWGUID,TEMPCONNAME,TEMPCONPER,TEMPMOL,TEMPOFF,TEMPSITE,TEMPAREA,TEMPTIME,TEMPLEN01,TEMPWI01,TEMPAREA01,TEMPLEN02,TEMPWI02,TEMPAREA02,TEMPLEN03,TEMPWI03,TEMPAREA03,TEMPLEN04,TEMPWI04,TEMPAREA04,TEMPREA,TEMPCONREMARK";
    // 挖掘城市道路许可申请表要素字段
    static String dlwjColumn = " ROWGUID,PARENTROWGUID,EXCACONNAME,EXCACONPER,EXCAMOL,EXCAOFF,EXCASITE,EXCAAREA,EXCATIME,EXCALEN01,EXCAWI01,EXCAAREA01,EXCALEN02,EXCAWI02,EXCAAREA02,EXCALEN03,EXCAWI03,EXCAAREA03,EXCALEN04,EXCAWI04,EXCAAREA04,EXCAREA,EXCACONREMARK";
    // 涉路许可申请表要素字段
    static String slxkColumn = " ROWGUID,PARENTROWGUID,INVOHAND,INVOEEMARK,INVOTIME,INVOADDR,INVOINTRO,INVOSAFE,INVOCOMP";
    // 修复、改建公路措施/补偿数额要素字段
    static String invoRoadColumn = " ROWGUID,PARENTROWGUID,INVOROADNO,INVOROADNUMB,INVOROADADDR,INVOROADLEV,INVOROADWI,INVOROADANG,INVOROADRA,INVOROADHI,INVOROADDIS,INVOROADDIST,INVOROADWID,INVOROADFORM,INVOROADORG";

    /**************************************泰安市表单 *************************************************/
    // 泰安市
    // 基本信息表--简化获得电力业务办理申请表-用电申请信息
    public final static String BASICTABLE = "table20190325201131";
    // 用电类别表--capList
    public final static String ELECTYPETABLE = "table20190327100419";
    // 规划许可申请内容表--简化获得电力业务办理申请表-规划许可申请
    public final static String GHXKTABLE = "table20190327091900";
    // 线路走径图及说明proLineList表--管线工程设计方案相关指标
    public final static String PROLINETABLE = "table20190327105705";
    // 线路走径图及说明proBuilist表 --附属建构筑物
    public final static String PROBUILTABLE = "table20190327105807";
    // 绿地占用申请表--简化获得电力业务办理申请表-绿地占用申请
    public final static String LDZYTABLE = "table20190327110509";
    // 施工起止时间表--绿化信息
    public final static String GREENINFOTABLE = "table20190327135932";
    // 占路许可申请表--简化获得电力业务办理申请表-占路许可申请公安
    public final static String ZLXKTABLE = "table20190327114845";
    // 临时占用城市道路许可申请表--简化获得电力业务办理申请表-临时占用城市道路许可申请
    public final static String LSZYTABLE = "table20190327144950";
    // 挖掘城市道路许可申请表--简化获得电力业务办理申请表-挖掘城市道路许可申请
    public final static String DLWJTABLE = "table20190327145247";
    // 涉路许可申请表--简化获得电力业务办理申请表-涉路许可申请
    public final static String SLXKTABLE = "table20190327145319";
    // 修复、改建公路措施/补偿数额--invoRoadList
    public final static String INVOROADTABLE = "table20190327153834";

    /**************************************新泰市表单 *************************************************/
    // 新泰市表单 
    // 基本信息表--简化获得电力业务办理申请表-用电申请信息
    public final static String XTBASICTABLE = "table20190428105826";
    // 用电类别表--capList
    public final static String XTELECTYPETABLE = "table20190327100419";
    // 规划许可申请内容表--简化获得电力业务办理申请表-规划许可申请
    public final static String XTGHXKTABLE = "table20190428105937";
    // 线路走径图及说明proLineList表--管线工程设计方案相关指标
    public final static String XTPROLINETABLE = "table20190428110050";
    // 线路走径图及说明proBuilist表 --附属建构筑物
    public final static String XTPROBUILTABLE = "table20190428110101";
    // 绿地占用申请表--简化获得电力业务办理申请表-绿地占用申请
    public final static String XTLDZYTABLE = "table20190428110744";
    // 施工起止时间表--绿化信息
    public final static String XTGREENINFOTABLE = "table20190428110808";
    // 占路许可申请表--简化获得电力业务办理申请表-占路许可申请公安
    public final static String XTZLXKTABLE = "table20190428110956";
    // 临时占用城市道路许可申请表--简化获得电力业务办理申请表-临时占用城市道路许可申请
    public final static String XTLSZYTABLE = "table20190428111510";
    // 挖掘城市道路许可申请表--简化获得电力业务办理申请表-挖掘城市道路许可申请
    public final static String XTDLWJTABLE = "table20190428111123";
    // 涉路许可申请表--简化获得电力业务办理申请表-涉路许可申请
    public final static String XTSLXKTABLE = "table20190428111254";
    // 修复、改建公路措施/补偿数额--invoRoadList
    public final static String XTINVOROADTABLE = "table20190428111319";

    /**************************************泰山区表单 *************************************************/
    // 泰山区表单 
    // 基本信息表--简化获得电力业务办理申请表-用电申请信息
    public final static String TSBASICTABLE = "table20190428092422";
    // 用电类别表--capList
    public final static String TSELECTYPETABLE = "table20190327100419";
    // 规划许可申请内容表--简化获得电力业务办理申请表-规划许可申请
    public final static String TSGHXKTABLE = "table20190428090013";
    // 线路走径图及说明proLineList表--管线工程设计方案相关指标
    public final static String TSPROLINETABLE = "table20190428090308";
    // 线路走径图及说明proBuilist表 --附属建构筑物
    public final static String TSPROBUILTABLE = "table20190428090319";
    // 绿地占用申请表--简化获得电力业务办理申请表-绿地占用申请
    public final static String TSLDZYTABLE = "table20190428092937";
    // 施工起止时间表--绿化信息
    public final static String TSGREENINFOTABLE = "table20190428093036";
    // 占路许可申请表--简化获得电力业务办理申请表-占路许可申请公安
    public final static String TSZLXKTABLE = "table20190428093752";
    // 临时占用城市道路许可申请表--简化获得电力业务办理申请表-临时占用城市道路许可申请
    public final static String TSLSZYTABLE = "table20190428100213";
    // 挖掘城市道路许可申请表--简化获得电力业务办理申请表-挖掘城市道路许可申请
    public final static String TSDLWJTABLE = "table20190428094337";
    // 涉路许可申请表--简化获得电力业务办理申请表-涉路许可申请
    public final static String TSSLXKTABLE = "table20190428094549";
    // 修复、改建公路措施/补偿数额--invoRoadList
    public final static String TSINVOROADTABLE = "table20190428095008";

    /**************************************岱岳区表单*************************************************/
    // 岱岳区表单 
    // 基本信息表--简化获得电力业务办理申请表-用电申请信息
    public final static String DYBASICTABLE = "table20190428100642";
    // 用电类别表--capList
    public final static String DYELECTYPETABLE = "table20190327100419";
    // 规划许可申请内容表--简化获得电力业务办理申请表-规划许可申请
    public final static String DYGHXKTABLE = "table20190428100935";
    // 线路走径图及说明proLineList表--管线工程设计方案相关指标
    public final static String DYPROLINETABLE = "table20190428101013";
    // 线路走径图及说明proBuilist表 --附属建构筑物
    public final static String DYPROBUILTABLE = "table20190428101028";
    // 绿地占用申请表--简化获得电力业务办理申请表-绿地占用申请
    public final static String DYLDZYTABLE = "table20190428101312";
    // 施工起止时间表--绿化信息
    public final static String DYGREENINFOTABLE = "table20190428101340";
    // 占路许可申请表--简化获得电力业务办理申请表-占路许可申请公安
    public final static String DYZLXKTABLE = "table20190428101718";
    // 临时占用城市道路许可申请表--简化获得电力业务办理申请表-临时占用城市道路许可申请
    public final static String DYLSZYTABLE = "table20190428102826";
    // 挖掘城市道路许可申请表--简化获得电力业务办理申请表-挖掘城市道路许可申请
    public final static String DYDLWJTABLE = "table20190428102027";
    // 涉路许可申请表--简化获得电力业务办理申请表-涉路许可申请
    public final static String DYSLXKTABLE = "table20190428102212";
    // 修复、改建公路措施/补偿数额--invoRoadList
    public final static String DYINVOROADTABLE = "table20190428102237";

    /**************************************高新区表单 *************************************************/
    // 高新区表单 
    // 基本信息表--简化获得电力业务办理申请表-用电申请信息
    public final static String GXBASICTABLE = "table20190428103058";
    // 用电类别表--capList
    public final static String GXELECTYPETABLE = "table20190327100419";
    // 规划许可申请内容表--简化获得电力业务办理申请表-规划许可申请
    public final static String GXGHXKTABLE = "table20190428103230";
    // 线路走径图及说明proLineList表--管线工程设计方案相关指标
    public final static String GXPROLINETABLE = "table20190428103250";
    // 线路走径图及说明proBuilist表 --附属建构筑物
    public final static String GXPROBUILTABLE = "table20190428103302";
    // 绿地占用申请表--简化获得电力业务办理申请表-绿地占用申请
    public final static String GXLDZYTABLE = "table20190428103702";
    // 施工起止时间表--绿化信息
    public final static String GXGREENINFOTABLE = "table20190428103724";
    // 占路许可申请表--简化获得电力业务办理申请表-占路许可申请公安
    public final static String GXZLXKTABLE = "table20190428103935";
    // 临时占用城市道路许可申请表--简化获得电力业务办理申请表-临时占用城市道路许可申请
    public final static String GXLSZYTABLE = "table20190428104630";
    // 挖掘城市道路许可申请表--简化获得电力业务办理申请表-挖掘城市道路许可申请
    public final static String GXDLWJTABLE = "table20190428104152";
    // 涉路许可申请表--简化获得电力业务办理申请表-涉路许可申请
    public final static String GXSLXKTABLE = "table20190428104326";
    // 修复、改建公路措施/补偿数额--invoRoadList
    public final static String GXINVOROADTABLE = "table20190428104357";

    /************************************** 肥城市表单 *************************************************/
    // 肥城市表单 
    // 基本信息表--简化获得电力业务办理申请表-用电申请信息
    public final static String FCBASICTABLE = "table20190428133841";
    // 用电类别表--capList
    public final static String FCELECTYPETABLE = "table20190327100419";
    // 规划许可申请内容表--简化获得电力业务办理申请表-规划许可申请
    public final static String FCGHXKTABLE = "table20190428134039";
    // 线路走径图及说明proLineList表--管线工程设计方案相关指标
    public final static String FCPROLINETABLE = "table20190428134135";
    // 线路走径图及说明proBuilist表 --附属建构筑物
    public final static String FCPROBUILTABLE = "table20190428134151";
    // 绿地占用申请表--简化获得电力业务办理申请表-绿地占用申请
    public final static String FCLDZYTABLE = "table20190428134450";
    // 施工起止时间表--绿化信息
    public final static String FCGREENINFOTABLE = "table20190428134511";
    // 占路许可申请表--简化获得电力业务办理申请表-占路许可申请公安
    public final static String FCZLXKTABLE = "table20190428134723";
    // 临时占用城市道路许可申请表--简化获得电力业务办理申请表-临时占用城市道路许可申请
    public final static String FCLSZYTABLE = "table20190428135704";
    // 挖掘城市道路许可申请表--简化获得电力业务办理申请表-挖掘城市道路许可申请
    public final static String FCDLWJTABLE = "table20190428135043";
    // 涉路许可申请表--简化获得电力业务办理申请表-涉路许可申请
    public final static String FCSLXKTABLE = "table20190428135316";
    // 修复、改建公路措施/补偿数额--invoRoadList
    public final static String FCINVOROADTABLE = "table20190428135337";

    /**************************************东平县表单 *************************************************/
    // 东平县表单 
    // 基本信息表--简化获得电力业务办理申请表-用电申请信息
    public final static String DPBASICTABLE = "table20190428135853";
    // 用电类别表--capList
    public final static String DPELECTYPETABLE = "table20190327100419";
    // 规划许可申请内容表--简化获得电力业务办理申请表-规划许可申请
    public final static String DPGHXKTABLE = "table20190428141230";
    // 线路走径图及说明proLineList表--管线工程设计方案相关指标
    public final static String DPPROLINETABLE = "table20190428141248";
    // 线路走径图及说明proBuilist表 --附属建构筑物
    public final static String DPPROBUILTABLE = "table20190428141302";
    // 绿地占用申请表--简化获得电力业务办理申请表-绿地占用申请
    public final static String DPLDZYTABLE = "table20190428141510";
    // 施工起止时间表--绿化信息
    public final static String DPGREENINFOTABLE = "table20190428141728";
    // 占路许可申请表--简化获得电力业务办理申请表-占路许可申请公安
    public final static String DPZLXKTABLE = "table20190428142018";
    // 临时占用城市道路许可申请表--简化获得电力业务办理申请表-临时占用城市道路许可申请
    public final static String DPLSZYTABLE = "table20190428142445";
    // 挖掘城市道路许可申请表--简化获得电力业务办理申请表-挖掘城市道路许可申请
    public final static String DPDLWJTABLE = "table20190428142134";
    // 涉路许可申请表--简化获得电力业务办理申请表-涉路许可申请
    public final static String DPSLXKTABLE = "table20190428142248";
    // 修复、改建公路措施/补偿数额--invoRoadList
    public final static String DPINVOROADTABLE = "table20190428142304";

    /**************************************宁阳县表单*************************************************/
    // 宁阳县表单 
    // 基本信息表--简化获得电力业务办理申请表-用电申请信息
    public final static String NYBASICTABLE = "table20190428111917";
    // 用电类别表--capList
    public final static String NYELECTYPETABLE = "table20190327100419";
    // 规划许可申请内容表--简化获得电力业务办理申请表-规划许可申请
    public final static String NYGHXKTABLE = "table20190428112039";
    // 线路走径图及说明proLineList表--管线工程设计方案相关指标
    public final static String NYPROLINETABLE = "table20190428112049";
    // 线路走径图及说明proBuilist表 --附属建构筑物
    public final static String NYPROBUILTABLE = "table20190428112106";
    // 绿地占用申请表--简化获得电力业务办理申请表-绿地占用申请
    public final static String NYLDZYTABLE = "table20190428112323";
    // 施工起止时间表--绿化信息
    public final static String NYGREENINFOTABLE = "table20190428112341";
    // 占路许可申请表--简化获得电力业务办理申请表-占路许可申请公安
    public final static String NYZLXKTABLE = "table20190428112530";
    // 临时占用城市道路许可申请表--简化获得电力业务办理申请表-临时占用城市道路许可申请
    public final static String NYLSZYTABLE = "table20190428113642";
    // 挖掘城市道路许可申请表--简化获得电力业务办理申请表-挖掘城市道路许可申请
    public final static String NYDLWJTABLE = "table20190428112900";
    // 涉路许可申请表--简化获得电力业务办理申请表-涉路许可申请
    public final static String NYSLXKTABLE = "table20190428113121";
    // 修复、改建公路措施/补偿数额--invoRoadList
    public final static String NYINVOROADTABLE = "table20190428113450";

}
