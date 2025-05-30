package com.epoint.zwdt.zwdtrest.project;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.epoint.authenticator.identity.Authenticator;
import com.epoint.authenticator.module.OAuthCheckTokenInfo;
import com.epoint.authenticator.utils.CheckTokenUtil;
import com.epoint.basic.auditonlineuser.auditonlineregister.domain.AuditOnlineRegister;
import com.epoint.basic.auditonlineuser.auditonlineregister.inter.IAuditOnlineRegister;
import com.epoint.basic.auditresource.auditrsitembaseinfo.domain.AuditRsItemBaseinfo;
import com.epoint.basic.auditresource.auditrsitembaseinfo.inter.IAuditRsItemBaseinfo;
import com.epoint.basic.auditresource.company.domain.AuditRsCompanyBaseinfo;
import com.epoint.basic.auditresource.company.inter.IAuditRsCompanyBaseinfo;
import com.epoint.basic.auditsp.dantiinfo.api.IDantiInfoService;
import com.epoint.basic.auditsp.dantiinfo.entity.DantiInfo;
import com.epoint.basic.auditsp.dantisubrelation.api.IDantiSubRelationService;
import com.epoint.basic.auditsp.dantisubrelation.entity.DantiSubRelation;
import com.epoint.basic.auditsp.dwgcinfo.api.IDwgcInfoService;
import com.epoint.basic.auditsp.dwgcinfo.entity.DwgcInfo;
import com.epoint.basic.auditsp.dwgcjlneed.api.IDwgcJlneedService;
import com.epoint.basic.auditsp.dwgcjlneed.entity.DwgcJlneed;
import com.epoint.basic.auditsp.dwgcpersoninfo.api.IDwgcPersoninfoService;
import com.epoint.basic.basedata.participantsinfo.api.IParticipantsInfoService;
import com.epoint.basic.basedata.participantsinfo.api.entity.ParticipantsInfo;
import com.epoint.common.util.JsonUtils;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ZwdtConstant;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.common.zwdt.authentication.ZwdtUserSession;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.httpclient.HttpUtil;
import com.epoint.core.utils.json.JsonUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.frame.service.metadata.code.entity.CodeItems;
import com.epoint.frame.service.metadata.systemparameters.api.IConfigService;
import com.epoint.xmz.buildinfo.api.IBuildInfoService;
import com.epoint.xmz.buildinfo.api.entity.BuildInfo;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import java.lang.invoke.MethodHandles;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 并联审批相关接口
 *
 * @version [F9.3, 2019年7月18日]
 * @作者 徐李
 */
@RestController
@RequestMapping("/jnzwdtItemUnit")
public class JNAuditOnlineItemUnitController {
    /**
     * 日志
     */
    private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());
    /**
     * 注册用户API
     */
    @Autowired
    private IAuditOnlineRegister iAuditOnlineRegister;

    /**
     * 单体API
     */
    @Autowired
    private IDantiInfoService iDantiInfoService;

    @Autowired
    private IDantiSubRelationService iDantiSubRelationService;

    /**
     * 代码项API
     */
    @Autowired
    private ICodeItemsService iCodeItemsService;

    /**
     * 单位工程API
     */
    @Autowired
    private IDwgcInfoService iDwgcInfoService;

    /**
     * 参建单位API
     */
    @Autowired
    private IParticipantsInfoService iParticipantsInfo;

    @Autowired
    private IBuildInfoService iBuildInfoService;
    /**
     * 单位工程人员API
     */
    @Autowired
    private IDwgcPersoninfoService iDwgcPersoninfoService;

    @Autowired
    private IDwgcJlneedService iDwgcJlneedService;
    @Autowired
    private IAuditRsItemBaseinfo iAuditRsItemBaseinfo;
    @Autowired
    private IConfigService iConfigService;

    // 企业库api
    @Autowired
    private IAuditRsCompanyBaseinfo iAuditRsCompanyBaseinfo;

    //==============================================================================================

    /**
     * 获取单体列表接口
     *
     * @param params  传入的参数
     * @param request HTTP请求
     * @return
     */
    @RequestMapping(value = "/getDantiList", method = RequestMethod.POST)
    public String getDantiList(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                // 1、接口的入参转化为JSON对象
                JSONObject obj = (JSONObject) jsonObject.get("params");
                String subappguid = obj.getString("subappguid");
                String itemguid = obj.getString("itemguid");
                // 2、获取用户注册信息
                AuditOnlineRegister auditOnlineRegister = this.getOnlineRegister(request);
                if (auditOnlineRegister != null) {
                    // 2.1、获取用户身份证
                    List<DantiInfo> dantiInfoList = iDantiInfoService.findListByProjectguid(itemguid);
                    //建筑单体栋数
                    //总建筑面积
                    Double zjzmjz = 0.0;
                    //最高高度
                    Double zgjzgd = 0.0;
                    for (DantiInfo dantiInfo : dantiInfoList) {
                        if (dantiInfo.getJzgd() != null && dantiInfo.getJzgd() > zgjzgd) {
                            zgjzgd = dantiInfo.getJzgd();
                        }
                        if (dantiInfo.getZjzmj() != null) {
                            zjzmjz += dantiInfo.getZjzmj();
                        }
                    }
                    List<DantiInfo> alldantiInfo = iDantiInfoService.findListBySubAppguid(subappguid);
                    List<JSONObject> materialList = new ArrayList<JSONObject>();
                    for (DantiInfo dantiInfo : alldantiInfo) {
                        JSONObject jsonMaterial = new JSONObject();
                        jsonMaterial.put("rowguid", dantiInfo.getRowguid());
                        jsonMaterial.put("phaseguid", dantiInfo.getPhaseguid());
                        jsonMaterial.put("dantiguid", dantiInfo.getRowguid());
                        jsonMaterial.put("dantiname", dantiInfo.getDantiname());
                        jsonMaterial.put("dtbm", dantiInfo.getStr("dtbm"));
                        jsonMaterial.put("fllb", dantiInfo.getFllb());
                        jsonMaterial.put("gclb", dantiInfo.getGclb());
                        if (StringUtil.isNotBlank(dantiInfo.getGclb())) {
                            String name = getNameByCode(dantiInfo.getGclb());
                            jsonMaterial.put("gclbmc", name);
                        }
                        if (StringUtil.isNotBlank(dantiInfo.getGcxz())) {
                            if (dantiInfo.getGcxz().contains(",")) {
                                String[] list = dantiInfo.getGcxz().split(",");
                                StringBuilder sb = new StringBuilder();
                                for (String code : list) {
                                    String name = getItemText(code, "工程性质");
                                    sb.append(name);
                                    sb.append(";");
                                }
                                jsonMaterial.put("gcxz", sb.toString().substring(0, sb.toString().length() - 1));
                            } else {
                                jsonMaterial.put("gcxz", getItemText(dantiInfo.getGcxz(), "工程性质"));
                            }
                        }
                        jsonMaterial.put("jzdts", dantiInfoList.size());
                        jsonMaterial.put("zjzmjz", zjzmjz);
                        jsonMaterial.put("zgjzgd", zgjzgd);
                        jsonMaterial.put("zzmj", dantiInfo.getZzmj());
                        jsonMaterial.put("zjzmj", dantiInfo.getZjzmj());
                        jsonMaterial.put("gjmj", dantiInfo.getGjmj());
                        jsonMaterial.put("dxgjmj", dantiInfo.getDxgjmj());
                        jsonMaterial.put("dxckmj", dantiInfo.getDxckmj());
                        jsonMaterial.put("dishangmianji", dantiInfo.getDishangmianji());
                        jsonMaterial.put("dixiamianji", dantiInfo.getDixiamianji());
                        jsonMaterial.put("jzgd", dantiInfo.getJzgd());
                        jsonMaterial.put("dscs", dantiInfo.getDscs());
                        jsonMaterial.put("dxcs", dantiInfo.getDxcs());
                        jsonMaterial.put("price", dantiInfo.getPrice());
                        jsonMaterial.put("gcguid", dantiInfo.getGongchengguid());
                        jsonMaterial.put("rguid", dantiInfo.getStr("rguid"));
                        jsonMaterial.put("zjurl", dantiInfo.getStr("zjurl"));
                        DwgcInfo dwgcInfo = new DwgcInfo();
                        if (StringUtil.isNotBlank(dantiInfo.getGongchengguid())) {
                            dwgcInfo = iDwgcInfoService.find(dantiInfo.getGongchengguid());
                            jsonMaterial.put("gongchengname", dwgcInfo.getGongchengname());
                        }
                        materialList.add(jsonMaterial);
                    }
                    JSONObject dataJson = new JSONObject();
                    dataJson.put("dantilist", materialList);
                    return JsonUtils.zwdtRestReturn("1", " 单体列表查询成功", dataJson.toString());
                } else {
                    return JsonUtils.zwdtRestReturn("0", "获取用户失败！", "");
                }
            } else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败", "");
            }
        } catch (Exception e) {
            return JsonUtils.zwdtRestReturn("0", "单体列表查询失败", "");
        }
    }

    /**
     * 获取单体单位列表接口
     *
     * @param params  传入的参数
     * @param request HTTP请求
     * @return
     */
    @RequestMapping(value = "/getDantiInfoList", method = RequestMethod.POST)
    public String getDantiInfoList(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                // 1、接口的入参转化为JSON对象
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、子申报标识
                String subappguid = obj.getString("subappguid");
                // 1.2、项目标识
                String itemguid = obj.getString("itemguid");
                // 1.3、工程标识
                String gongchengguid = obj.getString("gongchengguid");
                // 1.4、页码
                String pageSize = obj.getString("pagesize");
                // 1.4、当前页
                String currentPage = obj.getString("currentpage");
                // 2、获取用户注册信息
                AuditOnlineRegister auditOnlineRegister = this.getOnlineRegister(request);
                if (auditOnlineRegister != null) {
                    SqlConditionUtil sqlRelation = new SqlConditionUtil();
                    sqlRelation.eq("subappguid", subappguid);
                    sqlRelation.setSelectFields("dantiguid");
                    List<DantiSubRelation> dantiSubRelationList = iDantiSubRelationService
                            .getDantiSubRelationListByCondition(sqlRelation.getMap());
                    String strWhereIn = "";
                    for (DantiSubRelation dantiSubRelation : dantiSubRelationList) {
                        strWhereIn += "'" + dantiSubRelation.getDantiguid() + "',";
                    }

                    SqlConditionUtil sql = new SqlConditionUtil();
                    sql.eq("projectguid", itemguid);
                    if (StringUtil.isNotBlank(strWhereIn)) {
                        strWhereIn = strWhereIn.substring(0, strWhereIn.length() - 1);
                        sql.notin("rowguid", strWhereIn);
                    }
                    if (StringUtil.isNotBlank(gongchengguid)) {
                        sql.eq("gongchengguid", gongchengguid);
                    }
                    List<JSONObject> dantiList = new ArrayList<>(16);
                    PageData<DantiInfo> pageData = iDantiInfoService.getDantiInfoPageData(sql.getMap(),
                            Integer.parseInt(currentPage) * Integer.parseInt(pageSize), Integer.parseInt(pageSize),
                            "operatedate", "desc");
                    for (DantiInfo dantiInfo : pageData.getList()) {
                        JSONObject objReturn = new JSONObject();
                        objReturn.put("dantiguid", dantiInfo.getRowguid());
                        if (StringUtil.isNotBlank(dantiInfo.getDantiname())) {
                            objReturn.put("dantiname", dantiInfo.getDantiname());
                        } else {
                            objReturn.put("dantiname", dantiInfo.getStr("dantimingcheng"));
                        }
                        objReturn.put("gclbmc",
                                StringUtil.isNotBlank(dantiInfo.getGclb()) ? getNameByCode(dantiInfo.getGclb()) : "");
                        objReturn.put("gclb", dantiInfo.getGclb());
                        objReturn.put("price", dantiInfo.getPrice());
                        dantiList.add(objReturn);
                    }
                    JSONObject data = new JSONObject();
                    data.put("dantilist", dantiList);
                    data.put("total", pageData.getRowCount());
                    return JsonUtils.zwdtRestReturn("1", "查询单体单位成功", data.toString());
                } else {
                    return JsonUtils.zwdtRestReturn("0", "请登录后再试", "");
                }
            } else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败", "");
            }
        } catch (Exception e) {
            return JsonUtils.zwdtRestReturn("0", "查询单体单位失败", "");
        }
    }

    /**
     * 保存单体单位接口
     *
     * @param params  传入的参数
     * @param request HTTP请求
     * @return
     */
    @RequestMapping(value = "/saveDantiInfo", method = RequestMethod.POST)
    public String saveDantiInfo(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                // 1、接口的入参转化为JSON对象
                JSONObject obj = (JSONObject) jsonObject.get("params");
                String itemguid = obj.getString("itemguid");//工程标识
                String dantiguid = obj.getString("dantiguid");
                String dantiname = obj.getString("dantiname");
                String gclb = obj.getString("gclb");//工程类别
                String gongchengdizhi = obj.getString("gongchengdizhi");//工程地址
                String sfczrfgc = obj.getString("sfczrfgc");//是否存在人防工程
                String zuo = obj.getString("zuo");//厂站工程（座）
                String usecraft = obj.getString("usecraft");//厂站工程（采用工艺）
                String zjzmj = obj.getString("zjzmj");//建筑面积
                String jzgd = obj.getString("jzgd");//建筑高度
                String price = obj.getString("price");//工程造价
                String jianzhucengshu = obj.getString("jianzhucengshu");//建筑层数
                String dscs = obj.getString("dscs");//地上层数
                String dxcs = obj.getString("dxcs");//地下层数
                String dishangmianji = obj.getString("dishangmianji");//地上面积
                String dixiamianji = obj.getString("dixiamianji");//地下面积
                String dishanggaodu = obj.getString("dishanggaodu");//地上高度
                String dixiashendu = obj.getString("dixiashendu");//地下深度
                String iskanchan = obj.getString("iskanchan");//是否需要勘察
                String ykheight = obj.getString("ykheight");//檐口高度
                String span = obj.getString("span");//跨度（m）
                String cubage = obj.getString("cubage");//容积（L）
                String jiegoutx = obj.getString("jiegoutx");//结构体系
                String djdesignleavel = obj.getString("djdesignleavel");//地基基础设计等级
                String basictype = obj.getString("basictype");//基础型式
                String groundtype = obj.getString("groundtype");//场地土类别
                String buildgroundtype = obj.getString("buildgroundtype");//建筑场地类别
                String groundhandletype = obj.getString("groundhandletype");//地基处理方法
                String jikengtype = obj.getString("jikengtype");//基坑类别
                String kztype = obj.getString("kztype");//抗震设防类别
                String kzlevel = obj.getString("kzlevel");//抗震设防烈度
                String islowenergy = obj.getString("islowenergy");//是否超低能耗建筑
                String ispublicbuilding = obj.getString("ispublicbuilding");//是否有大型公共建筑
                String isfjsproject = obj.getString("isfjsproject");//是否有附建式人防工程
                String ishavecap = obj.getString("ishavecap");//是否采用无梁楼盖
                String ishaverebar = obj.getString("ishaverebar");//是否采用高强钢筋
                String islowshock = obj.getString("islowshock");//是否采用减隔震技术
                String ishaveequipment = obj.getString("ishaveequipment");//是否采用装配式建筑
                String ishavebim = obj.getString("ishavebim");//是否采用BIM技术
                String firelevel = obj.getString("firelevel");//耐火等级
                String givewatertype = obj.getString("givewatertype");//给水方式
                String heatingtype = obj.getString("heatingtype");//采暖方式
                String kongtiaotype = obj.getString("kongtiaotype");//空调方式
                String lighttype = obj.getString("lighttype");//照明方式
                String greenbuildingnorm = obj.getString("greenbuildingnorm");//绿色建筑设计标准
                String renewableenergy = obj.getString("renewableenergy");//设计可再生能源利用率(%)
                String wateruserate = obj.getString("wateruserate");//设计非传统水源利用率(%)
                String materialsrate = obj.getString("materialsrate");//设计可再生循环建筑材料用量比(%)
                String houseprove = obj.getString("houseprove");//房屋权属证明
                String changetype = obj.getString("changetype");//改造类型
                String isconstuctioncheck = obj.getString("isconstuctioncheck");//是否涉及结构检测鉴定
                String buildpart = obj.getString("buildpart");//装修部位
                String zhuangxiuarea = obj.getString("zhuangxiuarea");//装修面积(㎡)
                String buildplies = obj.getString("buildplies");//装修层数(层)
                String projectuseage = obj.getString("projectuseage");//工程用途
                String firedevice = obj.getString("firedevice");//消防设施
                String projectpricepercent = obj.getString("projectpricepercent");//装饰装修工程平米造价(元/㎡)
                String projectprice = obj.getString("projectprice");//装饰装修工程造价(万元)
                String linelength = obj.getString("linelength");//管线长度(m)
                String linedia = obj.getString("linedia");//管线管径(mm)
                String dayhandingnumber = obj.getString("dayhandingnumber");//管线日处理量(m³)
                String redlinewidth = obj.getString("redlinewidth");//管线红线宽度(m)
                String linedepth = obj.getString("linedepth");//管线深度(m)
                String roadlevel = obj.getString("roadlevel");//道路等级
                String roadflyovertype = obj.getString("roadflyovertype");//立交型式
                String roadlength = obj.getString("roadlength");//道路长度(m)
                String roadarea = obj.getString("roadarea");//道路面积(㎡)
                String roaddesignspeed = obj.getString("roaddesignspeed");//设计速度(km/h)
                String roadcarmax = obj.getString("roadcarmax");//车辆荷载
                String roadstructuretype = obj.getString("roadstructuretype");//路面结构类型
                String bridgelinelevel = obj.getString("bridgelinelevel");//桥梁线路等级
                String bridgelength = obj.getString("bridgelength");//桥梁长度(m)
                String bridgestructuretype = obj.getString("bridgestructuretype");//桥梁结构型式
                String bridgespantype = obj.getString("bridgespantype");//桥梁跨度型式
                String bridgearea = obj.getString("bridgearea");//桥梁面积(㎡)
                String bridgeup = obj.getString("bridgeup");//桥梁上部
                String bridgedown = obj.getString("bridgedown");//桥梁下部
                String bridgeholeset = obj.getString("bridgeholeset");//桥梁孔径布置
                String bridgeangle = obj.getString("bridgeangle");//桥梁斜交角度
                String bridgewaterrate = obj.getString("bridgewaterrate");//桥梁洪水频率
                String bridgeroadlevel = obj.getString("bridgeroadlevel");//航道等级
                String tunnellinelevel = obj.getString("tunnellinelevel");//城市隧道线路等级
                String tunnellength = obj.getString("tunnellength");//城市隧道长度(m)
                String tunnelarea = obj.getString("tunnelarea");//城市隧道面积(㎡)
                String BRTlength = obj.getString("brtlength");//快速公交系统长度(m)
                String tramsystemlength = obj.getString("tramsystemlength");//电车系统长度(m)
                String busroadlength = obj.getString("busroadlength");//公共交通专用道长度(m)
                String busstationarea = obj.getString("busstationarea");//公共交通场站面积(㎡)
                String bushubarea = obj.getString("bushubarea");//公共交通枢纽面积(㎡)
                String environmenthandle = obj.getString("environmenthandle");//环境卫生工程处理量
                String gasprojectnumber = obj.getString("gasprojectnumber");//燃气工程厂站(座)
                String gaslinelength = obj.getString("gaslinelength");//燃气管网线路长度(m)
                String gaslinepressure = obj.getString("gaslinepressure");//燃气管网线路设计压力
                String gaslinecaliber = obj.getString("gaslinecaliber");//燃气管网线路管径(mm)
                String heatprojectnumber = obj.getString("heatprojectnumber");//热力工程厂站(座)
                String heatlinelength = obj.getString("heatlinelength");//热力工程管网线路长度(m)
                String heatlinelevel = obj.getString("heatlinelevel");//热网等级
                String heatlinecaliber = obj.getString("heatlinecaliber");//热力工程管网线路管径(mm)
                String greenarea = obj.getString("greenarea");//园林绿化面积(㎡)
                String greenwaterarea = obj.getString("greenwaterarea");//园林绿化水域面积(㎡)
                String greentreetype = obj.getString("greentreetype");//园林绿化树木种类数
                String greenflowertype = obj.getString("greenflowertype");//园林绿化花木种类数
                String greenrockeryheight = obj.getString("greenrockeryheight");//园林绿化假山高度(m)
                String greenprice = obj.getString("greenprice");//园林绿化工程造价(万元)
                String installstandard = obj.getString("installstandard");//安装工程划分标准

                String dantimingcheng = obj.getString("dantimingcheng");
                String zhuangshileibie = obj.getString("zhuangshileibie");
                String jianzhumianji1 = obj.getString("jianzhumianji1");
                String dishangmianji1 = obj.getString("dishangmianji1");
                String dixiamianji1 = obj.getString("dixiamianji1");
                String taoshu = obj.getString("taoshu");
                String diantishuliang = obj.getString("diantishuliang");
                String jianzhuwutype = obj.getString("jianzhuwutype");
                String dishangjiegou = obj.getString("dishangjiegou");
                String dishangcengshu = obj.getString("dishangcengshu");
                String dixiajiegou = obj.getString("dixiajiegou");
                String dixiacengshu = obj.getString("dixiacengshu");
                String jichuleixing = obj.getString("jichuleixing");
                String dijileixing = obj.getString("dijileixing");
                String tujianmianji = obj.getString("tujianmianji");
                String tujianzaojia = obj.getString("tujianzaojia");
                String zhuangxiumianji = obj.getString("zhuangxiumianji");
                String zhuangshizaojia = obj.getString("zhuangshizaojia");
                String gangjiegoumianji = obj.getString("gangjiegoumianji");
                String gangjiegoudunwei = obj.getString("gangjiegoudunwei");
                String muqiangshigonggaodu = obj.getString("muqiangshigonggaodu");
                String shebeianzhuangzaojia = obj.getString("shebeianzhuangzaojia");
                String jikengzhihutype = obj.getString("jikengzhihutype");
                String zhihuzhuangtype = obj.getString("zhihuzhuangtype");
                String jikengmianji = obj.getString("jikengmianji");
                String jikengkaiwashendu = obj.getString("jikengkaiwashendu");
                String jikengzhihuzaojia = obj.getString("jikengzhihuzaojia");
                String qitamianji = obj.getString("qitamianji");
                String qitazaojia = obj.getString("qitazaojia");
                String qita = obj.getString("qita");
                String beizhuxinxi = obj.getString("beizhuxinxi");
                String jiaofeichengnuo = obj.getString("jiaofeichengnuo");

                // 2、获取用户注册信息
                AuditOnlineRegister auditOnlineRegister = this.getOnlineRegister(request);
                if (auditOnlineRegister != null) {
                    DantiInfo dantiInfo = new DantiInfo();
                    dantiInfo.setProjectguid(itemguid);
                    dantiInfo.setRowguid(StringUtil.isNotBlank(dantiguid) ? dantiguid : UUID.randomUUID().toString());
                    dantiInfo.setDantiname(dantiname);
                    if (StringUtil.isNotBlank(gclb)) {
                        dantiInfo.setGclb(gclb);
                    }
                    dantiInfo.setGongchengdizhi(gongchengdizhi);
                    dantiInfo.setSfczrfgc(sfczrfgc);
                    if (StringUtil.isNotBlank(zuo)) {
                        dantiInfo.setZuo(Double.valueOf(zuo));
                    }
                    if (StringUtil.isNotBlank(usecraft)) {
                        dantiInfo.setUsecraft(Integer.valueOf(usecraft));
                    }
                    if (StringUtil.isNotBlank(zjzmj)) {
                        dantiInfo.setZjzmj(Double.valueOf(zjzmj));
                    }
                    if (StringUtil.isNotBlank(jzgd)) {
                        dantiInfo.setJzgd(Double.valueOf(jzgd));
                    }
                    if (StringUtil.isNotBlank(price)) {
                        dantiInfo.setPrice(Double.valueOf(price));
                    }
                    dantiInfo.setJianzhucengshu(jianzhucengshu);
                    dantiInfo.setDscs(dscs);
                    dantiInfo.setDxcs(dxcs);
                    dantiInfo.setDishangmianji(dishangmianji);
                    dantiInfo.setDixiamianji(dixiamianji);
                    dantiInfo.setDishanggaodu(dishanggaodu);
                    dantiInfo.setDixiashendu(dixiashendu);
                    dantiInfo.setIskanchan(iskanchan);
                    if (StringUtil.isNotBlank(basictype)) {
                        dantiInfo.setBasictype(Integer.valueOf(basictype));
                    }
                    if (StringUtil.isNotBlank(ykheight)) {
                        dantiInfo.setYkheight(Double.valueOf(ykheight));
                    }
                    dantiInfo.setSpan(span);
                    dantiInfo.setCubage(cubage);
                    if (StringUtil.isNotBlank(jiegoutx)) {
                        dantiInfo.setJiegoutx(Integer.valueOf(jiegoutx));
                    }
                    if (StringUtil.isNotBlank(djdesignleavel)) {
                        dantiInfo.setDjdesignleavel(Integer.valueOf(djdesignleavel));
                    }
                    if (StringUtil.isNotBlank(groundtype)) {
                        dantiInfo.setGroundtype(Integer.valueOf(groundtype));
                    }
                    if (StringUtil.isNotBlank(buildgroundtype)) {
                        dantiInfo.setBuildgroundtype(Integer.valueOf(buildgroundtype));
                    }
                    if (StringUtil.isNotBlank(groundhandletype)) {
                        dantiInfo.setGroundhandletype(Integer.valueOf(groundhandletype));
                    }
                    if (StringUtil.isNotBlank(jikengtype)) {
                        dantiInfo.setJikengtype(Integer.valueOf(jikengtype));
                    }
                    if (StringUtil.isNotBlank(kztype)) {
                        dantiInfo.setKztype(Integer.valueOf(kztype));
                    }
                    if (StringUtil.isNotBlank(kzlevel)) {
                        dantiInfo.setKzlevel(Integer.valueOf(kzlevel));
                    }
                    dantiInfo.setIslowenergy(islowenergy);
                    dantiInfo.setIspublicbuilding(ispublicbuilding);
                    dantiInfo.setIsfjsproject(isfjsproject);
                    dantiInfo.setIshavecap(ishavecap);
                    dantiInfo.setIshaverebar(ishaverebar);
                    dantiInfo.setIslowshock(islowshock);
                    dantiInfo.setIshaveequipment(ishaveequipment);
                    dantiInfo.setIshavebim(ishavebim);

                    dantiInfo.set("dantimingcheng", dantimingcheng);
                    dantiInfo.set("zhuangshileibie", zhuangshileibie);
                    dantiInfo.set("jianzhumianji1", jianzhumianji1);
                    dantiInfo.set("dishangmianji1", dishangmianji1);
                    dantiInfo.set("dixiamianji1", dixiamianji1);
                    dantiInfo.set("taoshu", taoshu);
                    dantiInfo.set("diantishuliang", diantishuliang);
                    dantiInfo.set("jianzhuwutype", jianzhuwutype);
                    dantiInfo.set("dishangjiegou", dishangjiegou);
                    dantiInfo.set("dishangcengshu", dishangcengshu);
                    dantiInfo.set("dixiajiegou", dixiajiegou);
                    dantiInfo.set("dixiacengshu", dixiacengshu);
                    dantiInfo.set("jichuleixing", jichuleixing);
                    dantiInfo.set("dijileixing", dijileixing);
                    dantiInfo.set("tujianmianji", tujianmianji);
                    dantiInfo.set("tujianzaojia", tujianzaojia);
                    dantiInfo.set("zhuangxiumianji", zhuangxiumianji);
                    dantiInfo.set("zhuangshizaojia", zhuangshizaojia);
                    dantiInfo.set("gangjiegoumianji", gangjiegoumianji);
                    dantiInfo.set("gangjiegoudunwei", gangjiegoudunwei);
                    dantiInfo.set("muqiangshigonggaodu", muqiangshigonggaodu);
                    dantiInfo.set("shebeianzhuangzaojia", shebeianzhuangzaojia);
                    dantiInfo.set("jikengzhihutype", jikengzhihutype);
                    dantiInfo.set("zhihuzhuangtype", zhihuzhuangtype);
                    dantiInfo.set("jikengmianji", jikengmianji);
                    dantiInfo.set("jikengkaiwashendu", jikengkaiwashendu);
                    dantiInfo.set("jikengzhihuzaojia", jikengzhihuzaojia);
                    dantiInfo.set("qitamianji", qitamianji);
                    dantiInfo.set("qitazaojia", qitazaojia);
                    dantiInfo.set("qita", qita);
                    dantiInfo.set("beizhuxinxi", beizhuxinxi);
                    dantiInfo.set("jiaofeichengnuo", jiaofeichengnuo);

                    if (StringUtil.isNotBlank(firelevel)) {
                        dantiInfo.setFirelevel(Integer.valueOf(firelevel));
                    }
                    if (StringUtil.isNotBlank(givewatertype)) {
                        dantiInfo.setGivewatertype(Integer.valueOf(givewatertype));
                    }
                    if (StringUtil.isNotBlank(heatingtype)) {
                        dantiInfo.setHeatingtype(Integer.valueOf(heatingtype));
                    }
                    if (StringUtil.isNotBlank(kongtiaotype)) {
                        dantiInfo.setKongtiaotype(Integer.valueOf(kongtiaotype));
                    }
                    if (StringUtil.isNotBlank(lighttype)) {
                        dantiInfo.setLighttype(Integer.valueOf(lighttype));
                    }
                    if (StringUtil.isNotBlank(greenbuildingnorm)) {
                        dantiInfo.setGreenbuildingnorm(Integer.valueOf(greenbuildingnorm));
                    }
                    if (StringUtil.isNotBlank(renewableenergy)) {
                        dantiInfo.setRenewableenergy(Double.valueOf(renewableenergy));
                    }
                    if (StringUtil.isNotBlank(wateruserate)) {
                        dantiInfo.setWateruserate(Double.valueOf(wateruserate));
                    }
                    if (StringUtil.isNotBlank(materialsrate)) {
                        dantiInfo.setMaterialsrate(Double.valueOf(materialsrate));
                    }
                    dantiInfo.setHouseprove(houseprove);
                    if (StringUtil.isNotBlank(changetype)) {
                        dantiInfo.setChangetype(Integer.valueOf(changetype));
                    }

                    dantiInfo.setIsconstuctioncheck(isconstuctioncheck);
                    dantiInfo.setBuildpart(buildpart);
                    if (StringUtil.isNotBlank(zhuangxiuarea)) {
                        dantiInfo.setZhuangxiuarea(Double.valueOf(zhuangxiuarea));
                    }
                    if (StringUtil.isNotBlank(buildplies)) {
                        dantiInfo.setBuildplies(Integer.valueOf(buildplies));
                    }
                    if (StringUtil.isNotBlank(projectuseage)) {
                        dantiInfo.setProjectuseage(Integer.valueOf(projectuseage));
                    }
                    if (StringUtil.isNotBlank(firedevice)) {
                        dantiInfo.setFiredevice(Integer.valueOf(firedevice));
                    }
                    if (StringUtil.isNotBlank(projectpricepercent)) {
                        dantiInfo.setProjectpricepercent(Double.valueOf(projectpricepercent));
                    }
                    if (StringUtil.isNotBlank(projectprice)) {
                        dantiInfo.setProjectprice(Double.valueOf(projectprice));
                    }
                    if (StringUtil.isNotBlank(linelength)) {
                        dantiInfo.setLinelength(Double.valueOf(linelength));
                    }
                    if (StringUtil.isNotBlank(linedia)) {
                        dantiInfo.setLinedia(Double.valueOf(linedia));
                    }
                    if (StringUtil.isNotBlank(dayhandingnumber)) {
                        dantiInfo.setDayhandingnumber(Double.valueOf(dayhandingnumber));
                    }
                    if (StringUtil.isNotBlank(redlinewidth)) {
                        dantiInfo.setRedlinewidth(Double.valueOf(redlinewidth));
                    }
                    if (StringUtil.isNotBlank(linedepth)) {
                        dantiInfo.setLinedepth(Double.valueOf(linedepth));
                    }
                    if (StringUtil.isNotBlank(roadlevel)) {
                        dantiInfo.setRoadlevel(Integer.valueOf(roadlevel));
                    }
                    if (StringUtil.isNotBlank(roadflyovertype)) {
                        dantiInfo.setRoadflyovertype(Integer.valueOf(roadflyovertype));
                    }
                    if (StringUtil.isNotBlank(roadlength)) {
                        dantiInfo.setRoadlength(Double.valueOf(roadlength));
                    }
                    if (StringUtil.isNotBlank(roadarea)) {
                        dantiInfo.setRoadarea(Double.valueOf(roadarea));
                    }
                    if (StringUtil.isNotBlank(roaddesignspeed)) {
                        dantiInfo.setRoaddesignspeed(Double.valueOf(roaddesignspeed));
                    }

                    dantiInfo.setRoadcarmax(roadcarmax);
                    dantiInfo.setRoadstructuretype(roadstructuretype);
                    if (StringUtil.isNotBlank(bridgelinelevel)) {
                        dantiInfo.setBridgelinelevel(Integer.valueOf(bridgelinelevel));
                    }
                    if (StringUtil.isNotBlank(bridgelength)) {
                        dantiInfo.setBridgelength(Double.valueOf(bridgelength));
                    }
                    if (StringUtil.isNotBlank(bridgestructuretype)) {
                        dantiInfo.setBridgestructuretype(Integer.valueOf(bridgestructuretype));
                    }
                    if (StringUtil.isNotBlank(bridgespantype)) {
                        dantiInfo.setBridgespantype(Integer.valueOf(bridgespantype));
                    }
                    if (StringUtil.isNotBlank(bridgearea)) {
                        dantiInfo.setBridgearea(Double.valueOf(bridgearea));
                    }
                    dantiInfo.setBridgeup(bridgeup);
                    dantiInfo.setBridgedown(bridgedown);
                    dantiInfo.setBridgeholeset(bridgeholeset);
                    if (StringUtil.isNotBlank(bridgeangle)) {
                        dantiInfo.setBridgeangle(Double.valueOf(bridgeangle));
                    }
                    if (StringUtil.isNotBlank(bridgewaterrate)) {
                        dantiInfo.setBridgewaterrate(Double.valueOf(bridgewaterrate));
                    }
                    dantiInfo.setBridgeroadlevel(bridgeroadlevel);
                    if (StringUtil.isNotBlank(tunnellinelevel)) {
                        dantiInfo.setTunnellinelevel(Integer.valueOf(tunnellinelevel));
                    }
                    if (StringUtil.isNotBlank(tunnellength)) {
                        dantiInfo.setTunnellength(Double.valueOf(tunnellength));
                    }
                    if (StringUtil.isNotBlank(tunnelarea)) {
                        dantiInfo.setTunnelarea(Double.valueOf(tunnelarea));
                    }
                    if (StringUtil.isNotBlank(BRTlength)) {
                        dantiInfo.setBrtlength(Double.valueOf(BRTlength));
                    }
                    if (StringUtil.isNotBlank(tramsystemlength)) {
                        dantiInfo.setTramsystemlength(Double.valueOf(tramsystemlength));
                    }
                    if (StringUtil.isNotBlank(busroadlength)) {
                        dantiInfo.setBusroadlength(Double.valueOf(busroadlength));
                    }
                    if (StringUtil.isNotBlank(busstationarea)) {
                        dantiInfo.setBusstationarea(Double.valueOf(busstationarea));
                    }
                    if (StringUtil.isNotBlank(bushubarea)) {
                        dantiInfo.setBushubarea(Double.valueOf(bushubarea));
                    }
                    if (StringUtil.isNotBlank(environmenthandle)) {
                        dantiInfo.setEnvironmenthandle(Double.valueOf(environmenthandle));
                    }
                    if (StringUtil.isNotBlank(gasprojectnumber)) {
                        dantiInfo.setGasprojectnumber(Double.valueOf(gasprojectnumber));
                    }
                    if (StringUtil.isNotBlank(gaslinelength)) {
                        dantiInfo.setGaslinelength(Double.valueOf(gaslinelength));
                    }
                    if (StringUtil.isNotBlank(gaslinepressure)) {
                        dantiInfo.setGaslinepressure(Double.valueOf(gaslinepressure));
                    }
                    if (StringUtil.isNotBlank(gaslinecaliber)) {
                        dantiInfo.setGaslinecaliber(Double.valueOf(gaslinecaliber));
                    }
                    if (StringUtil.isNotBlank(heatprojectnumber)) {
                        dantiInfo.setHeatprojectnumber(Integer.valueOf(heatprojectnumber));
                    }
                    if (StringUtil.isNotBlank(heatlinelength)) {
                        dantiInfo.setHeatlinelength(Double.valueOf(heatlinelength));
                    }
                    if (StringUtil.isNotBlank(heatlinelevel)) {
                        dantiInfo.setHeatlinelevel(Integer.valueOf(heatlinelevel));
                    }
                    if (StringUtil.isNotBlank(heatlinecaliber)) {
                        dantiInfo.setHeatlinecaliber(Double.valueOf(heatlinecaliber));
                    }
                    if (StringUtil.isNotBlank(greenarea)) {
                        dantiInfo.setGreenarea(Double.valueOf(greenarea));
                    }
                    if (StringUtil.isNotBlank(greenwaterarea)) {
                        dantiInfo.setGreenwaterarea(Double.valueOf(greenwaterarea));
                    }
                    if (StringUtil.isNotBlank(greentreetype)) {
                        dantiInfo.setGreentreetype(Integer.valueOf(greentreetype));
                    }
                    if (StringUtil.isNotBlank(greenflowertype)) {
                        dantiInfo.setGreenflowertype(Integer.valueOf(greenflowertype));
                    }
                    if (StringUtil.isNotBlank(greenrockeryheight)) {
                        dantiInfo.setGreenrockeryheight(Double.valueOf(greenrockeryheight));
                    }
                    if (StringUtil.isNotBlank(greenprice)) {
                        dantiInfo.setGreenprice(Double.valueOf(greenprice));
                    }
                    dantiInfo.setInstallstandard(installstandard);

                    DantiInfo dantiInfoExit = iDantiInfoService.find(dantiguid);
                    if (dantiInfoExit != null) {
                        iDantiInfoService.update(dantiInfo);
                        return JsonUtils.zwdtRestReturn("1", "修改成功", "");
                    } else {
                        iDantiInfoService.insert(dantiInfo);
                        return JsonUtils.zwdtRestReturn("1", "保存成功", "");
                    }
                } else {
                    return JsonUtils.zwdtRestReturn("0", "请登录后再试", "");
                }
            } else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败", "");
            }
        } catch (Exception e) {
            return JsonUtils.zwdtRestReturn("0", "系统异常", "");
        }
    }

    /**
     * 查询单体单位详情接口
     *
     * @param params  传入的参数
     * @param request HTTP请求
     * @return
     */
    @RequestMapping(value = "/getDantiInfoDetail", method = RequestMethod.POST)
    public String getDantiInfoDetail(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                // 1、接口的入参转化为JSON对象
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、单体唯一标识
                String dantiguid = obj.getString("dantiguid");
                String gclb = obj.getString("gclb");
                // 2、获取用户注册信息
                AuditOnlineRegister auditOnlineRegister = this.getOnlineRegister(request);
                if (auditOnlineRegister != null) {
                    JSONObject dataJson = new JSONObject();
                    // 2.1、获取单体信息
                    DantiInfo dantiInfo = null;
                    String gclbmc = "";
                    if (StringUtil.isNotBlank(gclb)) {
                        gclbmc = getNameByCode(gclb);
                    }

                    if (StringUtil.isNotBlank(dantiguid)) {
                        dantiInfo = iDantiInfoService.find(dantiguid);
                        if (dantiInfo != null) {
                            dataJson.put("dantiname", dantiInfo.getDantiname());//单体名称
                            //dataJson.put("gclb", dantiInfo.getGclb());//工程类别
                            if (StringUtil.isNotBlank(dantiInfo.getGclb())) {
                                gclbmc = getNameByCode(dantiInfo.getGclb());
                            }
                            dataJson.put("gongchengdizhi", dantiInfo.getGongchengdizhi());//工程地址
                            dataJson.put("sfczrfgc", dantiInfo.getSfczrfgc());//是否存在人防工程
                            dataJson.put("zuo", dantiInfo.getZuo());//厂站工程(座)
                            dataJson.put("usecraft", dantiInfo.getUsecraft());//厂站工程(采用工艺)
                            dataJson.put("zjzmj", dantiInfo.getZjzmj());//建筑面积(m²)
                            dataJson.put("jzgd", dantiInfo.getJzgd());//建筑高度(m)
                            dataJson.put("price", dantiInfo.getPrice());//工程造价(万元)
                            dataJson.put("jianzhucengshu", dantiInfo.getJianzhucengshu());//建筑层数(层)
                            dataJson.put("dscs", dantiInfo.getDscs());//地上层数(层)
                            dataJson.put("dxcs", dantiInfo.getDxcs());//地下层数(层)
                            dataJson.put("dishangmianji", dantiInfo.getDishangmianji());//地上面积(m²)
                            dataJson.put("dixiamianji", dantiInfo.getDixiamianji());//地下面积(m²)
                            dataJson.put("dishanggaodu", dantiInfo.getDishanggaodu());//地上高度(m)
                            dataJson.put("dixiashendu", dantiInfo.getDixiashendu());//地下深度(m)
                            dataJson.put("ykheight", dantiInfo.getYkheight());//檐口高度(m)
                            dataJson.put("span", dantiInfo.getSpan());//跨度(m)
                            dataJson.put("cubage", dantiInfo.getCubage());//容积(L)
                            dataJson.put("greenbuildingnorm", dantiInfo.getGreenbuildingnorm());//绿色建筑设计标准
                            dataJson.put("renewableenergy", dantiInfo.getRenewableenergy());//设计可再生能源利用率(%)
                            dataJson.put("wateruserate", dantiInfo.getWateruserate());//设计非传统水源利用率(%)
                            dataJson.put("materialsrate", dantiInfo.getMaterialsrate());//设计可再生循环建筑材料用量比(%)
                            dataJson.put("houseprove", dantiInfo.getHouseprove());//房屋权属证明
                            //dataJson.put("changetype", dantiInfo.getChangetype());//改造类型
                            //dataJson.put("isconstuctioncheck", dantiInfo.getIsconstuctioncheck());//是否涉及结构检测鉴定
                            dataJson.put("buildpart", dantiInfo.getBuildpart());//装修部位
                            dataJson.put("zhuangxiuarea", dantiInfo.getZhuangxiuarea());//装修面积(㎡)
                            dataJson.put("buildplies", dantiInfo.getBuildplies());//装修层数(层)
                            //dataJson.put("projectuseage", dantiInfo.getProjectuseage());//工程用途
                            //dataJson.put("firedevice", dantiInfo.getFiredevice());//消防设施
                            dataJson.put("projectpricepercent", dantiInfo.getProjectpricepercent());//装饰装修工程平米造价(元/㎡)
                            dataJson.put("projectprice", dantiInfo.getProjectprice());//装饰装修工程造价(万元)
                            dataJson.put("linelength", dantiInfo.getLinelength());//管线长度(m)
                            dataJson.put("linedia", dantiInfo.getLinedia());//管线管径(mm)
                            dataJson.put("dayhandingnumber", dantiInfo.getDayhandingnumber());//管线日处理量(m³)
                            dataJson.put("redlinewidth", dantiInfo.getRedlinewidth());//管线红线宽度(m)
                            dataJson.put("linedepth", dantiInfo.getLinedepth());//管线深度(m)
                            dataJson.put("roadlevel", dantiInfo.getRoadlevel());//道路等级
                            dataJson.put("roadflyovertype", dantiInfo.getRoadflyovertype());//立交型式
                            dataJson.put("roadlength", dantiInfo.getRoadlength());//道路长度(m)
                            dataJson.put("roadarea", dantiInfo.getRoadarea());//道路面积(㎡)
                            dataJson.put("roaddesignspeed", dantiInfo.getRoaddesignspeed());//设计速度(km/h)
                            dataJson.put("roadcarmax", dantiInfo.getRoadcarmax());
                            dataJson.put("roadstructuretype", dantiInfo.getRoadstructuretype());
                            dataJson.put("bridgelinelevel", dantiInfo.getBridgelinelevel());
                            dataJson.put("bridgelength", dantiInfo.getBridgelength());
                            dataJson.put("bridgestructuretype", dantiInfo.getBridgestructuretype());
                            dataJson.put("bridgespantype", dantiInfo.getBridgespantype());
                            dataJson.put("bridgearea", dantiInfo.getBridgearea());
                            dataJson.put("bridgeup", dantiInfo.getBridgeup());
                            dataJson.put("bridgedown", dantiInfo.getBridgedown());
                            dataJson.put("bridgeholeset", dantiInfo.getBridgeholeset());
                            dataJson.put("bridgeangle", dantiInfo.getBridgeangle());
                            dataJson.put("bridgewaterrate", dantiInfo.getBridgewaterrate());
                            dataJson.put("bridgeroadlevel", dantiInfo.getBridgeroadlevel());
                            //dataJson.put("tunnellinelevel", dantiInfo.getTunnellinelevel());
                            dataJson.put("tunnellength", dantiInfo.getTunnellength());
                            dataJson.put("tunnelarea", dantiInfo.getTunnelarea());
                            dataJson.put("BRTlength", dantiInfo.getBrtlength());
                            dataJson.put("tramsystemlength", dantiInfo.getTramsystemlength());
                            dataJson.put("busroadlength", dantiInfo.getBusroadlength());
                            dataJson.put("busstationarea", dantiInfo.getBusstationarea());
                            dataJson.put("bushubarea", dantiInfo.getBushubarea());
                            dataJson.put("environmenthandle", dantiInfo.getEnvironmenthandle());
                            dataJson.put("gasprojectnumber", dantiInfo.getGasprojectnumber());
                            dataJson.put("gaslinelength", dantiInfo.getGaslinelength());
                            dataJson.put("gaslinepressure", dantiInfo.getGaslinepressure());
                            dataJson.put("gaslinecaliber", dantiInfo.getGaslinecaliber());
                            dataJson.put("heatprojectnumber", dantiInfo.getHeatprojectnumber());
                            dataJson.put("heatlinelength", dantiInfo.getHeatlinelength());
                            dataJson.put("heatlinelevel", dantiInfo.getHeatlinelevel());
                            dataJson.put("heatlinecaliber", dantiInfo.getHeatlinecaliber());
                            dataJson.put("greenarea", dantiInfo.getGreenarea());
                            dataJson.put("greenwaterarea", dantiInfo.getGreenwaterarea());
                            dataJson.put("greentreetype", dantiInfo.getGreentreetype());
                            dataJson.put("greenflowertype", dantiInfo.getGreenflowertype());
                            dataJson.put("greenrockeryheight", dantiInfo.getGreenrockeryheight());
                            dataJson.put("greenprice", dantiInfo.getGreenprice());
                            dataJson.put("installstandard", dantiInfo.getInstallstandard());

                            dataJson.put("dantimingcheng", dantiInfo.getStr("dantimingcheng"));
                            dataJson.put("zhuangshileibie", dantiInfo.getStr("zhuangshileibie"));
                            dataJson.put("jianzhumianji1", dantiInfo.getStr("jianzhumianji1"));
                            dataJson.put("dishangmianji1", dantiInfo.getStr("dishangmianji1"));
                            dataJson.put("dixiamianji1", dantiInfo.getStr("dixiamianji1"));
                            dataJson.put("taoshu", dantiInfo.getStr("taoshu"));
                            dataJson.put("diantishuliang", dantiInfo.getStr("diantishuliang"));
                            dataJson.put("jianzhuwutype", dantiInfo.getStr("jianzhuwutype"));
                            dataJson.put("dishangjiegou", dantiInfo.getStr("dishangjiegou"));
                            dataJson.put("dishangcengshu", dantiInfo.getStr("dishangcengshu"));
                            dataJson.put("dixiajiegou", dantiInfo.getStr("dixiajiegou"));
                            dataJson.put("dixiacengshu", dantiInfo.getStr("dixiacengshu"));
                            dataJson.put("jichuleixing", dantiInfo.getStr("jichuleixing"));
                            dataJson.put("dijileixing", dantiInfo.getStr("dijileixing"));
                            dataJson.put("tujianmianji", dantiInfo.getStr("tujianmianji"));
                            dataJson.put("tujianzaojia", dantiInfo.getStr("tujianzaojia"));
                            dataJson.put("zhuangxiumianji", dantiInfo.getStr("zhuangxiumianji"));
                            dataJson.put("zhuangshizaojia", dantiInfo.getStr("zhuangshizaojia"));
                            dataJson.put("gangjiegoumianji", dantiInfo.getStr("gangjiegoumianji"));
                            dataJson.put("gangjiegoudunwei", dantiInfo.getStr("gangjiegoudunwei"));
                            dataJson.put("muqiangshigonggaodu", dantiInfo.getStr("muqiangshigonggaodu"));
                            dataJson.put("shebeianzhuangzaojia", dantiInfo.getStr("shebeianzhuangzaojia"));
                            dataJson.put("jikengzhihutype", dantiInfo.getStr("jikengzhihutype"));
                            dataJson.put("zhihuzhuangtype", dantiInfo.getStr("zhihuzhuangtype"));
                            dataJson.put("jikengmianji", dantiInfo.getStr("jikengmianji"));
                            dataJson.put("jikengkaiwashendu", dantiInfo.getStr("jikengkaiwashendu"));
                            dataJson.put("jikengzhihuzaojia", dantiInfo.getStr("jikengzhihuzaojia"));
                            dataJson.put("qitamianji", dantiInfo.getStr("qitamianji"));
                            dataJson.put("qitazaojia", dantiInfo.getStr("qitazaojia"));
                            dataJson.put("qita", dantiInfo.getStr("qita"));
                            dataJson.put("beizhuxinxi", dantiInfo.getStr("beizhuxinxi"));
                            dataJson.put("jiaofeichengnuodetail", StringUtil.isNotBlank(dantiInfo.getStr("jiaofeichengnuo"))
                                    ? iCodeItemsService.getItemTextByCodeName("简易缴费承诺", dantiInfo.getStr("jiaofeichengnuo")) : "");
                            //-----------------------------------------------------------------------------
                            //代码项的detail值
                            dataJson.put("iskanchandetail", StringUtil.isNotBlank(dantiInfo.getIskanchan())
                                    ? iCodeItemsService.getItemTextByCodeName("是否", dantiInfo.getIskanchan()) : "");
                            dataJson.put("jiegoutxdetail", dantiInfo.getJiegoutx() != null ? iCodeItemsService
                                    .getItemTextByCodeName("结构体系", String.valueOf(dantiInfo.getJiegoutx())) : "");
                            dataJson.put("djdesignleaveldetail",
                                    dantiInfo.getDjdesignleavel() != null ? iCodeItemsService.getItemTextByCodeName(
                                            "地基基础设计等级", String.valueOf(dantiInfo.getDjdesignleavel())) : "");
                            dataJson.put("sfczrfgcdetail", StringUtil.isNotBlank(dantiInfo.getSfczrfgc())
                                    ? iCodeItemsService.getItemTextByCodeName("是否", dantiInfo.getSfczrfgc()) : "");
                            dataJson.put("basictypedetail", dantiInfo.getBasictype() != null ? iCodeItemsService
                                    .getItemTextByCodeName("基础型式", String.valueOf(dantiInfo.getBasictype())) : "");
                            dataJson.put("groundtypedetail", dantiInfo.getGroundtype() != null ? iCodeItemsService
                                    .getItemTextByCodeName("场地土类别", String.valueOf(dantiInfo.getGroundtype())) : "");
                            dataJson.put("buildgroundtypedetail",
                                    dantiInfo.getBuildgroundtype() != null ? iCodeItemsService.getItemTextByCodeName(
                                            "场地类别", String.valueOf(dantiInfo.getBuildgroundtype())) : "");
                            dataJson.put("groundhandletypedetail",
                                    dantiInfo.getGroundhandletype() != null ? iCodeItemsService.getItemTextByCodeName(
                                            "地基处理方法", String.valueOf(dantiInfo.getGroundhandletype())) : "");
                            dataJson.put("jikengtypedetail", dantiInfo.getJikengtype() != null ? iCodeItemsService
                                    .getItemTextByCodeName("基坑类别", String.valueOf(dantiInfo.getJikengtype())) : "");
                            dataJson.put("kztypedetail", dantiInfo.getKztype() != null ? iCodeItemsService
                                    .getItemTextByCodeName("抗震设防类别", String.valueOf(dantiInfo.getKztype())) : "");
                            dataJson.put("kzleveldetail", dantiInfo.getKzlevel() != null ? iCodeItemsService
                                    .getItemTextByCodeName("抗震设防烈度", String.valueOf(dantiInfo.getKzlevel())) : "");
                            dataJson.put("fireleveldetail", dantiInfo.getFirelevel() != null ? iCodeItemsService
                                    .getItemTextByCodeName("耐火等级", String.valueOf(dantiInfo.getFirelevel())) : "");
                            dataJson.put("islowenergydetail", StringUtil.isNotBlank(dantiInfo.getIslowenergy())
                                    ? iCodeItemsService.getItemTextByCodeName("是否", dantiInfo.getIslowenergy()) : "");
                            dataJson.put("ispublicbuildingdetail",
                                    StringUtil.isNotBlank(dantiInfo.getIspublicbuilding()) ? iCodeItemsService
                                            .getItemTextByCodeName("是否", dantiInfo.getIspublicbuilding()) : "");
                            dataJson.put("isfjsprojectdetail", StringUtil.isNotBlank(dantiInfo.getIsfjsproject())
                                    ? iCodeItemsService.getItemTextByCodeName("是否", dantiInfo.getIsfjsproject()) : "");
                            dataJson.put("ishavecapdetail", StringUtil.isNotBlank(dantiInfo.getIshavecap())
                                    ? iCodeItemsService.getItemTextByCodeName("是否", dantiInfo.getIshavecap()) : "");
                            dataJson.put("ishaverebardetail", StringUtil.isNotBlank(dantiInfo.getIshaverebar())
                                    ? iCodeItemsService.getItemTextByCodeName("是否", dantiInfo.getIshaverebar()) : "");
                            dataJson.put("islowshockdetail", StringUtil.isNotBlank(dantiInfo.getIslowshock())
                                    ? iCodeItemsService.getItemTextByCodeName("是否", dantiInfo.getIslowshock()) : "");
                            dataJson.put("ishaveequipmentdetail", StringUtil.isNotBlank(dantiInfo.getIshaveequipment())
                                    ? iCodeItemsService.getItemTextByCodeName("是否", dantiInfo.getIshaveequipment())
                                    : "");
                            dataJson.put("ishavebimdetail", StringUtil.isNotBlank(dantiInfo.getIshavebim())
                                    ? iCodeItemsService.getItemTextByCodeName("是否", dantiInfo.getIshavebim()) : "");
                            dataJson.put("givewatertypedetail", dantiInfo.getGivewatertype() != null ? iCodeItemsService
                                    .getItemTextByCodeName("给水方式", String.valueOf(dantiInfo.getGivewatertype())) : "");
                            dataJson.put("heatingtypedetail", dantiInfo.getHeatingtype() != null ? iCodeItemsService
                                    .getItemTextByCodeName("采暖方式", String.valueOf(dantiInfo.getHeatingtype())) : "");
                            dataJson.put("kongtiaotypedetail", dantiInfo.getKongtiaotype() != null ? iCodeItemsService
                                    .getItemTextByCodeName("空调方式", String.valueOf(dantiInfo.getKongtiaotype())) : "");
                            dataJson.put("lighttypedetail", dantiInfo.getLighttype() != null ? iCodeItemsService
                                    .getItemTextByCodeName("照明方式", String.valueOf(dantiInfo.getLighttype())) : "");
                            dataJson.put("greenbuildingnormdetail",
                                    dantiInfo.getGreenbuildingnorm() != null ? iCodeItemsService.getItemTextByCodeName(
                                            "绿色建筑设计标准", String.valueOf(dantiInfo.getGreenbuildingnorm())) : "");
                            //----------------------------------------------------------------------------------------------------
                            dataJson.put("changetypedetail", dantiInfo.getChangetype() != null ? iCodeItemsService
                                    .getItemTextByCodeName("装修改造工程类型", String.valueOf(dantiInfo.getChangetype())) : "");
                            dataJson.put("projectuseagedetail", dantiInfo.getProjectuseage() != null ? iCodeItemsService
                                    .getItemTextByCodeName("工程用途", String.valueOf(dantiInfo.getProjectuseage())) : "");
                            dataJson.put("firedevicedetail", dantiInfo.getFiredevice() != null ? iCodeItemsService
                                    .getItemTextByCodeName("消防设施种类", String.valueOf(dantiInfo.getFiredevice())) : "");
                            dataJson.put("isconstuctioncheckdetail",
                                    dantiInfo.getIsconstuctioncheck() != null ? iCodeItemsService.getItemTextByCodeName(
                                            "是否", String.valueOf(dantiInfo.getIsconstuctioncheck())) : "");
                            dataJson.put("tunnellineleveldetail",
                                    dantiInfo.getTunnellinelevel() != null ? iCodeItemsService.getItemTextByCodeName(
                                            "城市道路等级", String.valueOf(dantiInfo.getTunnellinelevel())) : "");
                        }
                    }
                    dataJson.put("gclbmc", gclbmc);//工程类别
                    List<JSONObject> list_sf = new ArrayList<>();
                    List<CodeItems> sf_codeItems = iCodeItemsService.listCodeItemsByCodeName("是否");
                    JSONObject obj_choice = new JSONObject();
                    obj_choice.put("itemvalue", "");
                    obj_choice.put("itemtext", "请选择");
                    list_sf.add(obj_choice);
                    for (CodeItems codeItems : sf_codeItems) {
                        JSONObject objCode = new JSONObject();
                        objCode.put("itemvalue", codeItems.getItemValue());
                        objCode.put("itemtext", codeItems.getItemText());
                        if (dantiInfo != null && StringUtil.isNotBlank(dantiInfo.getIskanchan())
                                && codeItems.getItemValue().equals(dantiInfo.getIskanchan())) {
                            objCode.put("isselected", 1);
                        }
                        list_sf.add(objCode);
                    }
                    dataJson.put("iskanchan", list_sf);//是否需要勘察
                    List<JSONObject> list_sfczrfgc = new ArrayList<>();
                    List<CodeItems> sfczrfgc_codeItems = iCodeItemsService.listCodeItemsByCodeName("是否");
                    for (CodeItems codeItems : sfczrfgc_codeItems) {
                        JSONObject objCode = new JSONObject();
                        objCode.put("itemvalue", codeItems.getItemValue());
                        objCode.put("itemtext", codeItems.getItemText());
                        if (dantiInfo != null && StringUtil.isNotBlank(dantiInfo.getSfczrfgc())
                                && codeItems.getItemValue().equals(dantiInfo.getSfczrfgc())) {
                            objCode.put("isselected", 1);
                        }
                        list_sfczrfgc.add(objCode);
                    }
                    dataJson.put("sfczrfgc", list_sfczrfgc);//是否存在人防工程
                    List<JSONObject> list_Jiegoutx = new ArrayList<>();
                    List<CodeItems> jiegoutx = iCodeItemsService.listCodeItemsByCodeName("结构体系");
                    obj_choice = new JSONObject();
                    obj_choice.put("itemvalue", "");
                    obj_choice.put("itemtext", "请选择");
                    list_Jiegoutx.add(obj_choice);
                    for (CodeItems codeItems : jiegoutx) {
                        JSONObject objCode = new JSONObject();
                        objCode.put("itemvalue", codeItems.getItemValue());
                        objCode.put("itemtext", codeItems.getItemText());
                        if (dantiInfo != null && StringUtil.isNotBlank(dantiInfo.getJiegoutx())
                                && codeItems.getItemValue().equals(String.valueOf(dantiInfo.getJiegoutx()))) {
                            objCode.put("isselected", 1);
                        }
                        list_Jiegoutx.add(objCode);
                    }
                    dataJson.put("jiegoutx", list_Jiegoutx);//结构体系
                    List<JSONObject> list_Djdesignleavel = new ArrayList<>();
                    List<CodeItems> djdesignleavel = iCodeItemsService.listCodeItemsByCodeName("地基基础设计等级");
                    obj_choice = new JSONObject();
                    obj_choice.put("itemvalue", "");
                    obj_choice.put("itemtext", "请选择");
                    list_Djdesignleavel.add(obj_choice);
                    for (CodeItems codeItems : djdesignleavel) {
                        JSONObject objCode = new JSONObject();
                        objCode.put("itemvalue", codeItems.getItemValue());
                        objCode.put("itemtext", codeItems.getItemText());
                        if (dantiInfo != null && StringUtil.isNotBlank(dantiInfo.getDjdesignleavel())
                                && codeItems.getItemValue().equals(String.valueOf(dantiInfo.getDjdesignleavel()))) {
                            objCode.put("isselected", 1);
                        }
                        list_Djdesignleavel.add(objCode);
                    }
                    dataJson.put("djdesignleavel", list_Djdesignleavel);//地基基础设计等级
                    List<JSONObject> list_Basictype = new ArrayList<>();
                    List<CodeItems> basictype = iCodeItemsService.listCodeItemsByCodeName("基础型式");
                    obj_choice = new JSONObject();
                    obj_choice.put("itemvalue", "");
                    obj_choice.put("itemtext", "请选择");
                    list_Basictype.add(obj_choice);
                    for (CodeItems codeItems : basictype) {
                        JSONObject objCode = new JSONObject();
                        objCode.put("itemvalue", codeItems.getItemValue());
                        objCode.put("itemtext", codeItems.getItemText());
                        if (dantiInfo != null && StringUtil.isNotBlank(dantiInfo.getBasictype())
                                && codeItems.getItemValue().equals(String.valueOf(dantiInfo.getBasictype()))) {
                            objCode.put("isselected", 1);
                        }
                        list_Basictype.add(objCode);
                    }
                    dataJson.put("basictype", list_Basictype);//基础型式

                    List<JSONObject> list_jianyi = new ArrayList<>();
                    List<CodeItems> jianyitype = iCodeItemsService.listCodeItemsByCodeName("简易缴费承诺");
                    obj_choice = new JSONObject();
                    obj_choice.put("itemvalue", "");
                    obj_choice.put("itemtext", "请选择");
                    list_jianyi.add(obj_choice);
                    for (CodeItems codeItems : jianyitype) {
                        JSONObject objCode = new JSONObject();
                        objCode.put("itemvalue", codeItems.getItemValue());
                        objCode.put("itemtext", codeItems.getItemText());
                        if (dantiInfo != null && StringUtil.isNotBlank(dantiInfo.getStr("jiaofeichengnuo"))
                                && codeItems.getItemValue().equals(String.valueOf(dantiInfo.getStr("jiaofeichengnuo")))) {
                            objCode.put("isselected", 1);
                        }
                        list_jianyi.add(objCode);
                    }
                    dataJson.put("jiaofeichengnuo", list_jianyi);//基础型式


                    List<JSONObject> list_Groundtype = new ArrayList<>();
                    List<CodeItems> groundtype = iCodeItemsService.listCodeItemsByCodeName("场地土类别");
                    obj_choice = new JSONObject();
                    obj_choice.put("itemvalue", "");
                    obj_choice.put("itemtext", "请选择");
                    list_Groundtype.add(obj_choice);
                    for (CodeItems codeItems : groundtype) {
                        JSONObject objCode = new JSONObject();
                        objCode.put("itemvalue", codeItems.getItemValue());
                        objCode.put("itemtext", codeItems.getItemText());
                        if (dantiInfo != null && StringUtil.isNotBlank(dantiInfo.getGroundtype())
                                && codeItems.getItemValue().equals(String.valueOf(dantiInfo.getGroundtype()))) {
                            objCode.put("isselected", 1);
                        }
                        list_Groundtype.add(objCode);
                    }
                    dataJson.put("groundtype", list_Groundtype);//场地土类别
                    List<JSONObject> list_Buildgroundtype = new ArrayList<>();
                    List<CodeItems> buildgroundtype = iCodeItemsService.listCodeItemsByCodeName("场地类别");
                    obj_choice = new JSONObject();
                    obj_choice.put("itemvalue", "");
                    obj_choice.put("itemtext", "请选择");
                    list_Buildgroundtype.add(obj_choice);
                    for (CodeItems codeItems : buildgroundtype) {
                        JSONObject objCode = new JSONObject();
                        objCode.put("itemvalue", codeItems.getItemValue());
                        objCode.put("itemtext", codeItems.getItemText());
                        if (dantiInfo != null && StringUtil.isNotBlank(dantiInfo.getBuildgroundtype())
                                && codeItems.getItemValue().equals(String.valueOf(dantiInfo.getBuildgroundtype()))) {
                            objCode.put("isselected", 1);
                        }
                        list_Buildgroundtype.add(objCode);
                    }
                    dataJson.put("buildgroundtype", list_Buildgroundtype);//建筑场地类别
                    List<JSONObject> list_Groundhandletype = new ArrayList<>();
                    List<CodeItems> groundhandletype = iCodeItemsService.listCodeItemsByCodeName("地基处理方法");
                    obj_choice = new JSONObject();
                    obj_choice.put("itemvalue", "");
                    obj_choice.put("itemtext", "请选择");
                    list_Groundhandletype.add(obj_choice);
                    for (CodeItems codeItems : groundhandletype) {
                        JSONObject objCode = new JSONObject();
                        objCode.put("itemvalue", codeItems.getItemValue());
                        objCode.put("itemtext", codeItems.getItemText());
                        if (dantiInfo != null && StringUtil.isNotBlank(dantiInfo.getGroundhandletype())
                                && codeItems.getItemValue().equals(String.valueOf(dantiInfo.getGroundhandletype()))) {
                            objCode.put("isselected", 1);
                        }
                        list_Groundhandletype.add(objCode);
                    }
                    dataJson.put("groundhandletype", list_Groundhandletype);//地基处理方法
                    List<JSONObject> list_Jikengtype = new ArrayList<>();
                    List<CodeItems> jikengtype = iCodeItemsService.listCodeItemsByCodeName("基坑类别");
                    obj_choice = new JSONObject();
                    obj_choice.put("itemvalue", "");
                    obj_choice.put("itemtext", "请选择");
                    list_Jikengtype.add(obj_choice);
                    for (CodeItems codeItems : jikengtype) {
                        JSONObject objCode = new JSONObject();
                        objCode.put("itemvalue", codeItems.getItemValue());
                        objCode.put("itemtext", codeItems.getItemText());
                        if (dantiInfo != null && StringUtil.isNotBlank(dantiInfo.getJikengtype())
                                && codeItems.getItemValue().equals(String.valueOf(dantiInfo.getJikengtype()))) {
                            objCode.put("isselected", 1);
                        }
                        list_Jikengtype.add(objCode);
                    }
                    dataJson.put("jikengtype", list_Jikengtype);//基坑类型
                    List<JSONObject> list_Kztype = new ArrayList<>();
                    List<CodeItems> kztype = iCodeItemsService.listCodeItemsByCodeName("抗震设防类别");
                    obj_choice = new JSONObject();
                    obj_choice.put("itemvalue", "");
                    obj_choice.put("itemtext", "请选择");
                    list_Kztype.add(obj_choice);
                    for (CodeItems codeItems : kztype) {
                        JSONObject objCode = new JSONObject();
                        objCode.put("itemvalue", codeItems.getItemValue());
                        objCode.put("itemtext", codeItems.getItemText());
                        if (dantiInfo != null && StringUtil.isNotBlank(dantiInfo.getKztype())
                                && codeItems.getItemValue().equals(String.valueOf(dantiInfo.getKztype()))) {
                            objCode.put("isselected", 1);
                        }
                        list_Kztype.add(objCode);
                    }
                    dataJson.put("kztype", list_Kztype);//抗震设防类别
                    List<JSONObject> list_Kzlevel = new ArrayList<>();
                    List<CodeItems> kzlevel = iCodeItemsService.listCodeItemsByCodeName("抗震设防烈度");
                    obj_choice = new JSONObject();
                    obj_choice.put("itemvalue", "");
                    obj_choice.put("itemtext", "请选择");
                    list_Kzlevel.add(obj_choice);
                    for (CodeItems codeItems : kzlevel) {
                        JSONObject objCode = new JSONObject();
                        objCode.put("itemvalue", codeItems.getItemValue());
                        objCode.put("itemtext", codeItems.getItemText());
                        if (dantiInfo != null && StringUtil.isNotBlank(dantiInfo.getKzlevel())
                                && codeItems.getItemValue().equals(String.valueOf(dantiInfo.getKzlevel()))) {
                            objCode.put("isselected", 1);
                        }
                        list_Kzlevel.add(objCode);
                    }
                    dataJson.put("kzlevel", list_Kzlevel);//抗震设防烈度
                    List<JSONObject> list_Firelevel = new ArrayList<>();
                    List<CodeItems> firelevel = iCodeItemsService.listCodeItemsByCodeName("耐火等级");
                    obj_choice = new JSONObject();
                    obj_choice.put("itemvalue", "");
                    obj_choice.put("itemtext", "请选择");
                    list_Firelevel.add(obj_choice);
                    for (CodeItems codeItems : firelevel) {
                        JSONObject objCode = new JSONObject();
                        objCode.put("itemvalue", codeItems.getItemValue());
                        objCode.put("itemtext", codeItems.getItemText());
                        if (dantiInfo != null && StringUtil.isNotBlank(dantiInfo.getFirelevel())
                                && codeItems.getItemValue().equals(String.valueOf(dantiInfo.getFirelevel()))) {
                            objCode.put("isselected", 1);
                        }
                        list_Firelevel.add(objCode);
                    }
                    dataJson.put("firelevel", list_Firelevel);//耐火等级
                    List<JSONObject> list_islowenergy = new ArrayList<>();
                    obj_choice = new JSONObject();
                    obj_choice.put("itemvalue", "");
                    obj_choice.put("itemtext", "请选择");
                    list_islowenergy.add(obj_choice);
                    for (CodeItems codeItems : sf_codeItems) {
                        JSONObject objCode = new JSONObject();
                        objCode.put("itemvalue", codeItems.getItemValue());
                        objCode.put("itemtext", codeItems.getItemText());
                        if (dantiInfo != null && StringUtil.isNotBlank(dantiInfo.getIslowenergy())
                                && codeItems.getItemValue().equals(dantiInfo.getIslowenergy())) {
                            objCode.put("isselected", 1);
                        }
                        list_islowenergy.add(objCode);
                    }
                    dataJson.put("islowenergy", list_islowenergy);//是否是超低能耗建筑
                    List<JSONObject> list_ispublicbuilding = new ArrayList<>();
                    obj_choice = new JSONObject();
                    obj_choice.put("itemvalue", "");
                    obj_choice.put("itemtext", "请选择");
                    list_ispublicbuilding.add(obj_choice);
                    for (CodeItems codeItems : sf_codeItems) {
                        JSONObject objCode = new JSONObject();
                        objCode.put("itemvalue", codeItems.getItemValue());
                        objCode.put("itemtext", codeItems.getItemText());
                        if (dantiInfo != null && StringUtil.isNotBlank(dantiInfo.getIspublicbuilding())
                                && codeItems.getItemValue().equals(dantiInfo.getIspublicbuilding())) {
                            objCode.put("isselected", 1);
                        }
                        list_ispublicbuilding.add(objCode);
                    }
                    dataJson.put("ispublicbuilding", list_ispublicbuilding);//是否有大型公共建筑
                    List<JSONObject> list_isfjsproject = new ArrayList<>();
                    obj_choice = new JSONObject();
                    obj_choice.put("itemvalue", "");
                    obj_choice.put("itemtext", "请选择");
                    list_isfjsproject.add(obj_choice);
                    for (CodeItems codeItems : sf_codeItems) {
                        JSONObject objCode = new JSONObject();
                        objCode.put("itemvalue", codeItems.getItemValue());
                        objCode.put("itemtext", codeItems.getItemText());
                        if (dantiInfo != null && StringUtil.isNotBlank(dantiInfo.getIsfjsproject())
                                && codeItems.getItemValue().equals(dantiInfo.getIsfjsproject())) {
                            objCode.put("isselected", 1);
                        }
                        list_isfjsproject.add(objCode);
                    }
                    dataJson.put("isfjsproject", list_isfjsproject);//是否有附建式人防工程
                    List<JSONObject> list_ishavecap = new ArrayList<>();
                    obj_choice = new JSONObject();
                    obj_choice.put("itemvalue", "");
                    obj_choice.put("itemtext", "请选择");
                    list_ishavecap.add(obj_choice);
                    for (CodeItems codeItems : sf_codeItems) {
                        JSONObject objCode = new JSONObject();
                        objCode.put("itemvalue", codeItems.getItemValue());
                        objCode.put("itemtext", codeItems.getItemText());
                        if (dantiInfo != null && StringUtil.isNotBlank(dantiInfo.getIshavecap())
                                && codeItems.getItemValue().equals(dantiInfo.getIshavecap())) {
                            objCode.put("isselected", 1);
                        }
                        list_ishavecap.add(objCode);
                    }
                    dataJson.put("ishavecap", list_ishavecap);//是否采用无梁楼盖
                    List<JSONObject> list_ishaverebar = new ArrayList<>();
                    obj_choice = new JSONObject();
                    obj_choice.put("itemvalue", "");
                    obj_choice.put("itemtext", "请选择");
                    list_ishaverebar.add(obj_choice);
                    for (CodeItems codeItems : sf_codeItems) {
                        JSONObject objCode = new JSONObject();
                        objCode.put("itemvalue", codeItems.getItemValue());
                        objCode.put("itemtext", codeItems.getItemText());
                        if (dantiInfo != null && StringUtil.isNotBlank(dantiInfo.getIshaverebar())
                                && codeItems.getItemValue().equals(dantiInfo.getIshaverebar())) {
                            objCode.put("isselected", 1);
                        }
                        list_ishaverebar.add(objCode);
                    }
                    dataJson.put("ishaverebar", list_ishaverebar);//是否采用高强钢筋
                    List<JSONObject> list_islowshock = new ArrayList<>();
                    obj_choice = new JSONObject();
                    obj_choice.put("itemvalue", "");
                    obj_choice.put("itemtext", "请选择");
                    list_islowshock.add(obj_choice);
                    for (CodeItems codeItems : sf_codeItems) {
                        JSONObject objCode = new JSONObject();
                        objCode.put("itemvalue", codeItems.getItemValue());
                        objCode.put("itemtext", codeItems.getItemText());
                        if (dantiInfo != null && StringUtil.isNotBlank(dantiInfo.getIslowshock())
                                && codeItems.getItemValue().equals(dantiInfo.getIslowshock())) {
                            objCode.put("isselected", 1);
                        }
                        list_islowshock.add(objCode);
                    }
                    dataJson.put("islowshock", list_islowshock);//是否采用减隔震技术
                    List<JSONObject> list_ishaveequipment = new ArrayList<>();
                    obj_choice = new JSONObject();
                    obj_choice.put("itemvalue", "");
                    obj_choice.put("itemtext", "请选择");
                    list_ishaveequipment.add(obj_choice);
                    for (CodeItems codeItems : sf_codeItems) {
                        JSONObject objCode = new JSONObject();
                        objCode.put("itemvalue", codeItems.getItemValue());
                        objCode.put("itemtext", codeItems.getItemText());
                        if (dantiInfo != null && StringUtil.isNotBlank(dantiInfo.getIshaveequipment())
                                && codeItems.getItemValue().equals(dantiInfo.getIshaveequipment())) {
                            objCode.put("isselected", 1);
                        }
                        list_ishaveequipment.add(objCode);
                    }
                    dataJson.put("ishaveequipment", list_ishaveequipment);//是否采用装配式建筑
                    List<JSONObject> list_ishavebim = new ArrayList<>();
                    obj_choice = new JSONObject();
                    obj_choice.put("itemvalue", "");
                    obj_choice.put("itemtext", "请选择");
                    list_ishavebim.add(obj_choice);
                    for (CodeItems codeItems : sf_codeItems) {
                        JSONObject objCode = new JSONObject();
                        objCode.put("itemvalue", codeItems.getItemValue());
                        objCode.put("itemtext", codeItems.getItemText());
                        if (dantiInfo != null && StringUtil.isNotBlank(dantiInfo.getIshavebim())
                                && codeItems.getItemValue().equals(dantiInfo.getIshavebim())) {
                            objCode.put("isselected", 1);
                        }
                        list_ishavebim.add(objCode);
                    }
                    dataJson.put("ishavebim", list_ishavebim);//是否采用BIM技术
                    List<JSONObject> list_Givewatertype = new ArrayList<>();
                    List<CodeItems> givewatertype = iCodeItemsService.listCodeItemsByCodeName("给水方式");
                    obj_choice = new JSONObject();
                    obj_choice.put("itemvalue", "");
                    obj_choice.put("itemtext", "请选择");
                    list_Givewatertype.add(obj_choice);
                    for (CodeItems codeItems : givewatertype) {
                        JSONObject objCode = new JSONObject();
                        objCode.put("itemvalue", codeItems.getItemValue());
                        objCode.put("itemtext", codeItems.getItemText());
                        if (dantiInfo != null && StringUtil.isNotBlank(dantiInfo.getGivewatertype())
                                && codeItems.getItemValue().equals(String.valueOf(dantiInfo.getGivewatertype()))) {
                            objCode.put("isselected", 1);
                        }
                        list_Givewatertype.add(objCode);
                    }
                    dataJson.put("givewatertype", list_Givewatertype);//给水方式
                    List<JSONObject> list_Heatingtype = new ArrayList<>();
                    List<CodeItems> heatingtype = iCodeItemsService.listCodeItemsByCodeName("采暖方式");
                    obj_choice = new JSONObject();
                    obj_choice.put("itemvalue", "");
                    obj_choice.put("itemtext", "请选择");
                    list_Heatingtype.add(obj_choice);
                    for (CodeItems codeItems : heatingtype) {
                        JSONObject objCode = new JSONObject();
                        objCode.put("itemvalue", codeItems.getItemValue());
                        objCode.put("itemtext", codeItems.getItemText());
                        if (dantiInfo != null && StringUtil.isNotBlank(dantiInfo.getHeatingtype())
                                && codeItems.getItemValue().equals(String.valueOf(dantiInfo.getHeatingtype()))) {
                            objCode.put("isselected", 1);
                        }
                        list_Heatingtype.add(objCode);
                    }
                    dataJson.put("heatingtype", list_Heatingtype);//采暖方式
                    List<JSONObject> list_Kongtiaotype = new ArrayList<>();
                    List<CodeItems> kongtiaotype = iCodeItemsService.listCodeItemsByCodeName("空调方式");
                    obj_choice = new JSONObject();
                    obj_choice.put("itemvalue", "");
                    obj_choice.put("itemtext", "请选择");
                    list_Kongtiaotype.add(obj_choice);
                    for (CodeItems codeItems : kongtiaotype) {
                        JSONObject objCode = new JSONObject();
                        objCode.put("itemvalue", codeItems.getItemValue());
                        objCode.put("itemtext", codeItems.getItemText());
                        if (dantiInfo != null && StringUtil.isNotBlank(dantiInfo.getKongtiaotype())
                                && codeItems.getItemValue().equals(String.valueOf(dantiInfo.getKongtiaotype()))) {
                            objCode.put("isselected", 1);
                        }
                        list_Kongtiaotype.add(objCode);
                    }
                    dataJson.put("kongtiaotype", list_Kongtiaotype);//空调方式
                    List<JSONObject> list_Lighttype = new ArrayList<>();
                    List<CodeItems> lighttype = iCodeItemsService.listCodeItemsByCodeName("照明方式");
                    obj_choice = new JSONObject();
                    obj_choice.put("itemvalue", "");
                    obj_choice.put("itemtext", "请选择");
                    list_Lighttype.add(obj_choice);
                    for (CodeItems codeItems : lighttype) {
                        JSONObject objCode = new JSONObject();
                        objCode.put("itemvalue", codeItems.getItemValue());
                        objCode.put("itemtext", codeItems.getItemText());
                        if (dantiInfo != null && StringUtil.isNotBlank(dantiInfo.getLighttype())
                                && codeItems.getItemValue().equals(String.valueOf(dantiInfo.getLighttype()))) {
                            objCode.put("isselected", 1);
                        }
                        list_Lighttype.add(objCode);
                    }
                    dataJson.put("lighttype", list_Lighttype);//照明方式
                    List<JSONObject> list_Greenbuildingnorm = new ArrayList<>();
                    List<CodeItems> greenbuildingnorm = iCodeItemsService.listCodeItemsByCodeName("绿色建筑设计标准");
                    obj_choice = new JSONObject();
                    obj_choice.put("itemvalue", "");
                    obj_choice.put("itemtext", "请选择");
                    list_Greenbuildingnorm.add(obj_choice);
                    for (CodeItems codeItems : greenbuildingnorm) {
                        JSONObject objCode = new JSONObject();
                        objCode.put("itemvalue", codeItems.getItemValue());
                        objCode.put("itemtext", codeItems.getItemText());
                        if (dantiInfo != null && StringUtil.isNotBlank(dantiInfo.getGreenbuildingnorm())
                                && codeItems.getItemValue().equals(String.valueOf(dantiInfo.getGreenbuildingnorm()))) {
                            objCode.put("isselected", 1);
                        }
                        list_Greenbuildingnorm.add(objCode);
                    }
                    dataJson.put("greenbuildingnorm", list_Greenbuildingnorm);//绿色建筑设计标准
                    //-----------------------------------------------------------------------------------------------------------------------------
                    List<JSONObject> list_Changetype = new ArrayList<>();
                    List<CodeItems> changetype = iCodeItemsService.listCodeItemsByCodeName("装修改造工程类型");
                    obj_choice = new JSONObject();
                    obj_choice.put("itemvalue", "");
                    obj_choice.put("itemtext", "请选择");
                    list_Changetype.add(obj_choice);
                    for (CodeItems codeItems : changetype) {
                        JSONObject objCode = new JSONObject();
                        objCode.put("itemvalue", codeItems.getItemValue());
                        objCode.put("itemtext", codeItems.getItemText());
                        if (dantiInfo != null && dantiInfo.getChangetype() != null
                                && codeItems.getItemValue().equals(String.valueOf(dantiInfo.getChangetype()))) {
                            objCode.put("isselected", 1);
                        }
                        list_Changetype.add(objCode);
                    }
                    dataJson.put("changetype", list_Changetype);//装修改造工程类型
                    List<JSONObject> list_Isconstuctioncheck = new ArrayList<>();
                    List<CodeItems> isconstuctioncheck = iCodeItemsService.listCodeItemsByCodeName("是否");
                    obj_choice = new JSONObject();
                    obj_choice.put("itemvalue", "");
                    obj_choice.put("itemtext", "请选择");
                    list_Isconstuctioncheck.add(obj_choice);
                    for (CodeItems codeItems : isconstuctioncheck) {
                        JSONObject objCode = new JSONObject();
                        objCode.put("itemvalue", codeItems.getItemValue());
                        objCode.put("itemtext", codeItems.getItemText());
                        if (dantiInfo != null && StringUtil.isNotBlank(dantiInfo.getIsconstuctioncheck())
                                && codeItems.getItemValue().equals(dantiInfo.getIsconstuctioncheck())) {
                            objCode.put("isselected", 1);
                        }
                        list_Isconstuctioncheck.add(objCode);
                    }
                    dataJson.put("isconstuctioncheck", list_Isconstuctioncheck);//是否涉及结构检测鉴定
                    List<JSONObject> list_Projectuseage = new ArrayList<>();
                    List<CodeItems> projectuseage = iCodeItemsService.listCodeItemsByCodeName("工程用途");
                    obj_choice = new JSONObject();
                    obj_choice.put("itemvalue", "");
                    obj_choice.put("itemtext", "请选择");
                    list_Projectuseage.add(obj_choice);
                    for (CodeItems codeItems : projectuseage) {
                        JSONObject objCode = new JSONObject();
                        objCode.put("itemvalue", codeItems.getItemValue());
                        objCode.put("itemtext", codeItems.getItemText());
                        if (dantiInfo != null && dantiInfo.getProjectuseage() != null
                                && codeItems.getItemValue().equals(String.valueOf(dantiInfo.getProjectuseage()))) {
                            objCode.put("isselected", 1);
                        }
                        list_Projectuseage.add(objCode);
                    }
                    dataJson.put("projectuseage", list_Projectuseage);//工程用途
                    List<JSONObject> list_Firedevice = new ArrayList<>();
                    List<CodeItems> firedevice = iCodeItemsService.listCodeItemsByCodeName("消防设施种类");
                    obj_choice = new JSONObject();
                    obj_choice.put("itemvalue", "");
                    obj_choice.put("itemtext", "请选择");
                    list_Firedevice.add(obj_choice);
                    for (CodeItems codeItems : firedevice) {
                        JSONObject objCode = new JSONObject();
                        objCode.put("itemvalue", codeItems.getItemValue());
                        objCode.put("itemtext", codeItems.getItemText());
                        if (dantiInfo != null && StringUtil.isNotBlank(dantiInfo.getFiredevice())
                                && codeItems.getItemValue().equals(String.valueOf(dantiInfo.getFiredevice()))) {
                            objCode.put("isselected", 1);
                        }
                        list_Firedevice.add(objCode);
                    }
                    dataJson.put("firedevice", list_Firedevice);//消防设施种类
                    List<JSONObject> list_Tunnellinelevel = new ArrayList<>();
                    List<CodeItems> tunnellinelevel = iCodeItemsService.listCodeItemsByCodeName("城市道路等级");
                    obj_choice = new JSONObject();
                    obj_choice.put("itemvalue", "");
                    obj_choice.put("itemtext", "请选择");
                    list_Tunnellinelevel.add(obj_choice);
                    for (CodeItems codeItems : tunnellinelevel) {
                        JSONObject objCode = new JSONObject();
                        objCode.put("itemvalue", codeItems.getItemValue());
                        objCode.put("itemtext", codeItems.getItemText());
                        if (dantiInfo != null && StringUtil.isNotBlank(dantiInfo.getTunnellinelevel())
                                && codeItems.getItemValue().equals(String.valueOf(dantiInfo.getTunnellinelevel()))) {
                            objCode.put("isselected", 1);
                        }
                        list_Tunnellinelevel.add(objCode);
                    }
                    dataJson.put("tunnellinelevel", list_Tunnellinelevel);//城市道路等级
                    return JsonUtils.zwdtRestReturn("1", " 查询成功", dataJson.toString());
                } else {
                    return JsonUtils.zwdtRestReturn("0", "请登录后再试", "");
                }
            } else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败", "");
            }
        } catch (Exception e) {
            return JsonUtils.zwdtRestReturn("0", "系统异常", "");
        }
    }

    /**
     * 删除单体单位接口
     *
     * @param params  传入的参数
     * @param request HTTP请求
     * @return
     */
    @RequestMapping(value = "/deleteDantiInfo", method = RequestMethod.POST)
    public String deleteDantiInfo(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                // 1、接口的入参转化为JSON对象
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、 单体唯一标识
                String dantiguids = obj.getString("dantiguids");
                // 2、获取用户注册信息
                AuditOnlineRegister auditOnlineRegister = this.getOnlineRegister(request);
                if (auditOnlineRegister != null) {
                    if (StringUtil.isNotBlank(dantiguids)) {
                        if (dantiguids.startsWith("[") && dantiguids.endsWith("]")) {
                            dantiguids = dantiguids.substring(1, dantiguids.length() - 2).replaceAll("\"", "");
                            String[] dantiguidArr = dantiguids.split(",");
                            for (String s : dantiguidArr) {
                                iDantiInfoService.deleteByGuid(s);
                            }
                        }
                    }
                    return JsonUtils.zwdtRestReturn("1", " 删除成功！", "");
                } else {
                    return JsonUtils.zwdtRestReturn("0", "请登录后再试", "");
                }
            } else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败", "");
            }
        } catch (Exception e) {
            return JsonUtils.zwdtRestReturn("0", "系统异常", "");
        }
    }

    /**
     * 删除单体单位接口
     *
     * @param params  传入的参数
     * @param request HTTP请求
     * @return
     */
    @RequestMapping(value = "/deleteDanti", method = RequestMethod.POST)
    public String deleteDanti(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                // 1、接口的入参转化为JSON对象
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、 单体唯一标识
                String relationguid = obj.getString("relationguid");
                // 2、获取用户注册信息
                AuditOnlineRegister auditOnlineRegister = this.getOnlineRegister(request);
                if (auditOnlineRegister != null) {
                    if (StringUtil.isNotBlank(relationguid)) {
                        iDantiSubRelationService.deleteByGuid(relationguid);
                    }
                    return JsonUtils.zwdtRestReturn("1", " 删除成功！", "");
                } else {
                    return JsonUtils.zwdtRestReturn("0", "请登录后再试", "");
                }
            } else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败", "");
            }
        } catch (Exception e) {
            return JsonUtils.zwdtRestReturn("0", "系统异常", "");
        }
    }

    /**
     * 确认单体单位接口
     *
     * @param params  传入的参数
     * @param request HTTP请求
     * @return
     */
    @RequestMapping(value = "/confirmDantiInfo", method = RequestMethod.POST)
    public String confirmDantiInfo(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                // 1、接口的入参转化为JSON对象
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、 单体唯一标识数组
                String dantiguids = obj.getString("dantiguids");
                // 1.2、 组建单位标识
                String subAppguid = obj.getString("subappguid");
                // 2、获取用户注册信息
                AuditOnlineRegister auditOnlineRegister = this.getOnlineRegister(request);
                if (auditOnlineRegister != null) {
                    if (StringUtil.isNotBlank(dantiguids)) {
                        if (dantiguids.startsWith("[") && dantiguids.endsWith("]")) {
                            dantiguids = dantiguids.substring(1, dantiguids.length() - 2).replaceAll("\"", "");
                            String[] dantiguidArr = dantiguids.split(",");
                            for (String s : dantiguidArr) {
                                DantiSubRelation dantiSubRelation = new DantiSubRelation();
                                dantiSubRelation.setSubappguid(subAppguid);
                                dantiSubRelation.setDantiguid(s);
                                dantiSubRelation.setRowguid(UUID.randomUUID().toString());
                                iDantiSubRelationService.insert(dantiSubRelation);
                            }
                        }
                    }
                    return JsonUtils.zwdtRestReturn("1", " 确认成功！", "");
                } else {
                    return JsonUtils.zwdtRestReturn("0", "请登录后再试", "");
                }
            } else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败", "");
            }
        } catch (Exception e) {
            return JsonUtils.zwdtRestReturn("0", "系统异常", "");
        }
    }

    /**
     * "选择单位"页面下的数据信息
     */
    @RequestMapping(value = "/getCompanyInfo", method = RequestMethod.POST)
    public String getCompanyInfo(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            // 接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject param = jsonObject.getJSONObject("params");
                String corpType = param.getString("corptype");
                String corpname = param.getString("corpname");
                String corpcode = param.getString("corpcode");
                int pageIndex = Integer.parseInt(param.getString("pageindex"));
                int pageSize = Integer.parseInt(param.getString("pagesize"));
                JSONObject json = new JSONObject();
                // 调用数字建设接口
                String churl = iConfigService.getFrameConfigValue("AS_BLSP_DWURL");
                //String churl = "http://192.168.212.97:8005/szjslhysgl/rest";
                String chadd = churl + "/DanWeiListInterface/getDanWeiList";
                // 条件map
                JSONObject map = new JSONObject();
                if (StringUtil.isNotBlank(corpname)) {
                    map.put("DANWEINAME", corpname);//单位名称 
                }
                if (StringUtil.isNotBlank(corpType)) {
                    if ("31".equals(corpType)) {
                        map.put("DANWITYPE", "11");//单位类型
                    } else if ("2".equals(corpType)) {
                        map.put("DANWITYPE", "16");//sj单位类型
                    } else if ("1".equals(corpType)) {
                        map.put("DANWITYPE", "15");//kc单位类型
                    } else if ("3".equals(corpType)) {
                        map.put("DANWITYPE", "13");//sg单位类型
                    } else if ("4".equals(corpType)) {
                        map.put("DANWITYPE", "14");//jl单位类型
                    } else if ("10".equals(corpType)) {
                        map.put("DANWITYPE", "10");//jc单位类型
                    }
                }
                if (StringUtil.isNotBlank(corpcode)) {
                    map.put("SOCIALCODE", corpcode); //统一社会信用代码 可传空值
                }
                map.put("INDEX", pageIndex * pageSize);//当前页码
                map.put("PAGESIZE", pageSize); //每页展示条数
                Map<String, Object> paramsCondition = new HashMap<>();
                paramsCondition.put("params", map);
                String result = HttpUtil.doPost(chadd, paramsCondition);
                if (StringUtil.isBlank(result)) {
                    log.info("单位列表初始化失败");
                    return null;
                }
                Map<String, Object> mapresult = JsonUtil.jsonToMap(result);
                String isok = mapresult.get("isok").toString();
                List<JSONObject> tbcorpbasicList = new ArrayList<JSONObject>();
                JSONObject countJson = new JSONObject();
                if (isok != null && isok.equals(ZwfwConstant.CONSTANT_STR_ONE)) {
                    @SuppressWarnings("unchecked")
                    List<Map<String, Object>> chlist = (List<Map<String, Object>>) mapresult.get("list");
                    String countStr = mapresult.get("count").toString();
                    countJson.put("countValue", Integer.parseInt(countStr));
                    json.put("count", countJson);
                    if (chlist.size() > 0) {
                        for (int i = 0; i < chlist.size(); i++) {
                            Map<String, Object> jsonstr = (Map<String, Object>) chlist.get(i);
                            JSONObject tbcorpbasicJson = new JSONObject();
                            tbcorpbasicJson.put("corpcode", jsonstr.get("socialcode").toString());
                            tbcorpbasicJson.put("corpname", jsonstr.get("danweiname").toString());
                            tbcorpbasicJson.put("legalman", jsonstr.get("farenname").toString());
                            tbcorpbasicJson.put("linktel", jsonstr.get("danweiphone").toString());
                            tbcorpbasicJson.put("address", jsonstr.get("address").toString());
                            tbcorpbasicList.add(tbcorpbasicJson);
                        }
                    }
                }
                json.put("tbcorpbasicList", tbcorpbasicList);
                return JsonUtils.zwdtRestReturn("1", "查询成功", json.toString());
            } else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        } catch (Exception e) {
            return JsonUtils.zwdtRestReturn("0", "单位信息获取异常", "");
        }
    }

    /**
     * "选择人员"页面下的数据信息
     */
    @RequestMapping(value = "/getPersonInfo", method = RequestMethod.POST)
    public String getPersonInfo(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            // 接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject param = jsonObject.getJSONObject("params");
                String personname = param.getString("personname");
                String corpcode = param.getString("corpcodestr");
                String idcard = param.getString("idcard");
                int pageIndex = Integer.parseInt(param.getString("pageindex"));
                int pageSize = Integer.parseInt(param.getString("pagesize"));
                String churl = iConfigService.getFrameConfigValue("AS_BLSP_DWURL");
                //String churl = "http://192.168.212.97:8005/szjslhysgl/rest";
                String chadd = churl + "/PersonListInterface/getPersonList";
                // 条件map
                JSONObject map = new JSONObject();
                map.put("PERSONTYPE", "");//单位名称  可传空值
                map.put("SOCIALCODE", corpcode); //统一社会信用代码 可传空值
                if (StringUtil.isNotBlank(personname)) {
                    map.put("NAME", personname);//人员姓名
                }
                if (StringUtil.isNotBlank(idcard)) {
                    map.put("IDENTITYNUM", idcard);//身份证号码
                }
                map.put("INDEX", pageIndex * pageSize);//当前页码
                map.put("PAGESIZE", pageSize); //每页展示条数
                Map<String, Object> paramsConditions = new HashMap<>();
                paramsConditions.put("params", map);
                String result = HttpUtil.doPost(chadd, paramsConditions);
                if (StringUtil.isBlank(result)) {
                    log.info("单位人员列表初始化失败");
                    return null;
                }
                Map<String, Object> mapresult = JsonUtil.jsonToMap(result);
                String isok = mapresult.get("isok").toString();
                // 结果json
                JSONObject json = new JSONObject();
                if (isok != null && isok.equals(ZwfwConstant.CONSTANT_STR_ONE)) {
                    @SuppressWarnings("unchecked")
                    List<Map<String, Object>> chlist = (List<Map<String, Object>>) mapresult.get("list");
                    String count = mapresult.get("count").toString();

                    if (chlist.size() > 0) {
                        JSONObject countJson = new JSONObject();
                        countJson.put("countValue", Integer.parseInt(count));
                        json.put("count", countJson);
                        List<JSONObject> tbpersonbasicList = new ArrayList<JSONObject>();
                        for (int i = 0; i < chlist.size(); i++) {
                            Map<String, Object> jsonstr = (Map<String, Object>) chlist.get(i);
                            JSONObject tbpersonbasicJson = new JSONObject();
                            tbpersonbasicJson.put("idcard", jsonstr.get("identitynum").toString());
                            tbpersonbasicJson.put("personname", jsonstr.get("name").toString());
                            tbpersonbasicJson.put("mobile", jsonstr.get("mobilephone").toString());
                            tbpersonbasicJson.put("sbzg", jsonstr.get("zhicheng").toString());
                            tbpersonbasicJson.put("personcorp", jsonstr.get("danweiname").toString());
                            tbpersonbasicList.add(tbpersonbasicJson);
                        }
                        json.put("tbpersonbasicList", tbpersonbasicList);
                        return JsonUtils.zwdtRestReturn("1", "查询成功", json.toString());
                    } else {
                        return JsonUtils.zwdtRestReturn("0", "该公司下无人员", "");
                    }
                } else {
                    return JsonUtils.zwdtRestReturn("0", "选择人员失败，请重试", "");
                }
            } else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败", "");
            }
        } catch (Exception e) {
            return JsonUtils.zwdtRestReturn("0", "项目人员信息获取异常", "");
        }
    }

    //----------------------------------------------------------------------------------------------

    /**
     * 查询单位列表接口
     *
     * @param params  传入的参数
     * @param request HTTP请求
     * @return
     */
    @RequestMapping(value = "/getDanweiList", method = RequestMethod.POST)
    public String getDanweiList(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                // 1、接口的入参转化为JSON对象
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、 项目标识
                String itemguid = obj.getString("itemguid");
                // 1.2、 子申报标识
                String subAppguid = obj.getString("subappguid");
                // 2、获取用户注册信息
                AuditOnlineRegister auditOnlineRegister = this.getOnlineRegister(request);
                if (auditOnlineRegister != null) {
                    List<ParticipantsInfo> participantsInfoList = iParticipantsInfo
                            .listParticipantsInfoBySubappGuid(subAppguid);
                    List<ParticipantsInfo> jsdwList = participantsInfoList.stream()
                            .filter(participantsInfo -> itemguid.equals(participantsInfo.getItemguid())
                                    && "31".equals(participantsInfo.getCbdanweitype()))
                            .collect(Collectors.toList());
                    List<ParticipantsInfo> sjdwList = participantsInfoList.stream()
                            .filter(participantsInfo -> itemguid.equals(participantsInfo.getItemguid())
                                    && "2".equals(participantsInfo.getCbdanweitype()))
                            .collect(Collectors.toList());
                    List<ParticipantsInfo> kcdwList = participantsInfoList.stream()
                            .filter(participantsInfo -> itemguid.equals(participantsInfo.getItemguid())
                                    && "1".equals(participantsInfo.getCbdanweitype()))
                            .collect(Collectors.toList());
                    List<ParticipantsInfo> sgdwList = participantsInfoList.stream()
                            .filter(participantsInfo -> itemguid.equals(participantsInfo.getItemguid())
                                    && "3".equals(participantsInfo.getCbdanweitype()))
                            .collect(Collectors.toList());
                    List<ParticipantsInfo> jldwList = participantsInfoList.stream()
                            .filter(participantsInfo -> itemguid.equals(participantsInfo.getItemguid())
                                    && "4".equals(participantsInfo.getCbdanweitype()))
                            .collect(Collectors.toList());
                    List<ParticipantsInfo> zljcdwList = participantsInfoList.stream()
                            .filter(participantsInfo -> itemguid.equals(participantsInfo.getItemguid())
                                    && "10".equals(participantsInfo.getCbdanweitype()))
                            .collect(Collectors.toList());
                    JSONObject dataJson = new JSONObject();
                    dataJson.put("jsdwlist", jsdwList);
                    dataJson.put("sjdwlist", sjdwList);
                    dataJson.put("kcdwlist", kcdwList);
                    dataJson.put("sgdwlist", sgdwList);
                    dataJson.put("jldwlist", jldwList);
                    dataJson.put("zljcdwlist", zljcdwList);
                    return JsonUtils.zwdtRestReturn("1", " 查询成功！", dataJson.toString());
                } else {
                    return JsonUtils.zwdtRestReturn("0", "获取用户失败！", "");
                }
            } else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败", "");
            }
        } catch (Exception e) {
            return JsonUtils.zwdtRestReturn("0", "系统异常", "");
        }
    }

    /**
     * 查询单位列表接口
     *
     * @param params  传入的参数
     * @param request HTTP请求
     * @return
     */
    @RequestMapping(value = "/saveDanwei", method = RequestMethod.POST)
    public String saveDanwei(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                // 1、接口的入参转化为JSON对象
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、 子申报标识
                String subappguid = obj.getString("subappguid");
                // 1.2、 项目标识
                String projectguid = obj.getString("projectguid");
                // 1.3、 单位名称
                String corpname = obj.getString("corpname");
                // 1.4、 统一社会信用代码
                String corpcode = obj.getString("corpcode");
                // 1.5、 法人代表
                String legal = obj.getString("legal");
                // 1.6、 单位联系电话
                String phone = obj.getString("phone");
                // 1.7、 单位地址
                String address = obj.getString("address");
                // 1.8、 项目负责人
                String xmfzr = obj.getString("xmfzr");
                // 1.9、 职称
                String xmfzrZc = obj.getString("xmfzr_zc");
                // 1.10、 身份证
                String xmfzrIdcard = obj.getString("xmfzr_idcard");
                // 1.11、 联系电话
                String xmfzrPhone = obj.getString("xmfzr_phone");
                // 2、获取用户注册信息
                AuditOnlineRegister auditOnlineRegister = this.getOnlineRegister(request);
                if (auditOnlineRegister != null) {
                    ParticipantsInfo participantsInfo = new ParticipantsInfo();
                    participantsInfo.setRowguid(UUID.randomUUID().toString());
                    participantsInfo.setSubappguid(subappguid);
                    participantsInfo.setItemguid(projectguid);
                    participantsInfo.setCorpname(corpname);
                    participantsInfo.setCorpcode(corpcode);
                    participantsInfo.setLegal(legal);
                    participantsInfo.setPhone(phone);
                    participantsInfo.setAddress(address);
                    participantsInfo.setXmfzr(xmfzr);
                    participantsInfo.setXmfzr_zc(xmfzrZc);
                    participantsInfo.setXmfzr_idcard(xmfzrIdcard);
                    participantsInfo.setXmfzrphonenum(xmfzrPhone);
                    iParticipantsInfo.insert(participantsInfo);
                    return JsonUtils.zwdtRestReturn("1", " 保存成功！", "");
                } else {
                    return JsonUtils.zwdtRestReturn("0", "获取用户失败！", "");
                }
            } else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败", "");
            }
        } catch (Exception e) {
            return JsonUtils.zwdtRestReturn("0", "系统异常", "");
        }
    }

    /**
     * 需要选择单位和人员的信息的单位新增
     */
    @RequestMapping(value = "/saveParticipants", method = RequestMethod.POST)
    public String saveParticipants(@RequestBody String params, @Context HttpServletRequest request) {
        JSONObject jsonObject = JSONObject.parseObject(params);
        String token = jsonObject.getString("token");
        if (ZwdtConstant.SysValidateData.equals(token)) {
            JSONObject param = jsonObject.getJSONObject("params");
            String dwguid = param.getString("dwguid");
            String itemguid = param.getString("itemguid");
            String subappguid = param.getString("subappguid");
            String corpname = param.getString("corpname");
            String corpcode = param.getString("corpcode");
            String legal = param.getString("legal");
            String phone = param.getString("phone");
            String address = param.getString("address");
            String xmfzr = param.getString("xmfzr");
            String xmfzr_zc = param.getString("xmfzr_zc");
            String xmfzr_idcard = param.getString("xmfzr_idcard");
            String xmfzr_phone = param.getString("xmfzr_phone");
            String corptype = param.getString("corptype");
            String legalproperty = param.getString("legalproperty");
            String itemlegalcerttype = param.getString("itemlegalcerttype");
            String itemlegalcertnum = param.getString("itemlegalcertnum");
            String itemlegaldept = param.getString("itemlegaldept");
            String legalpersonicardnum = param.getString("legalpersonicardnum");
            String frphone = param.getString("frphone");
            String fremail = param.getString("fremail");
            String danweilxr = param.getString("danweilxr");
            String danweilxrlxdh = param.getString("danweilxrlxdh");
            String danweilxrsfz = param.getString("danweilxrsfz");
            String cbtyple = param.getString("cbtyple");
            //施工阶段的字段=============================================================================================
            String cbdanweitype = param.getString("cbdanweitype");
            String jsfzr = param.getString("jsfzr");
            String jsfzr_zc = param.getString("jsfzr_zc");
            String jsfzr_phone = param.getString("jsfzr_phone");
            String xmfzperson = param.getString("xmfzperson");
            String xmfzrsafenum = param.getString("xmfzrsafenum");
            String xmfzrphonenum = param.getString("xmfzrphonenum");
            String qylxr = param.getString("qylxr");
            String qylxdh = param.getString("qylxdh");
            String gdlxr = param.getString("gdlxr");
            String gdlxdh = param.getString("gdlxdh");
            String fbsafenum = param.getString("fbsafenum");
            String fbtime = param.getString("fbtime");
            String fbscopeofcontract = param.getString("fbscopeofcontract");
            String fbqysettime = param.getString("fbqysettime");
            String fbaqglry = param.getString("fbaqglry");
            String fbaqglrysafenum = param.getString("fbaqglrysafenum");
            //----------------------------------------------------------------------------------------------------------
            // 判断统一社会信用代码是否重复
            SqlConditionUtil sql = new SqlConditionUtil();
            sql.eq("itemGuid", itemguid);
            sql.eq("corptype", corptype);
            sql.eq("subappguid", subappguid);
            sql.eq("corpcode", corpcode);
            sql.nq("rowguid", dwguid);
            List<ParticipantsInfo> participantsInfoList = iParticipantsInfo
                    .getParticipantsInfoListByCondition(sql.getMap());
            if (!participantsInfoList.isEmpty()) {
                return JsonUtils.zwdtRestReturn("0", "统一社会信用代码存在！", "");
            }
            if (StringUtil.isNotBlank(itemguid) && StringUtil.isNotBlank(subappguid)) {
                if ("31".equals(corptype)) {
                    // 项目敏感字段后台获取
                    AuditRsItemBaseinfo auditRsItemBaseinfo = iAuditRsItemBaseinfo
                            .getAuditRsItemBaseinfoByRowguid(itemguid).getResult();
                    // 获取统一社会信用代码/组织机构代码证
                    corpcode = auditRsItemBaseinfo.getStr("itemlegalcertnum");
                    // 通过这个字段查询企业信息
                    String codetype = auditRsItemBaseinfo.getStr("itemlegalcerttype");
                    itemlegalcerttype = codetype; // 项目证照类型
                    String field = "creditcode";
                    if ("14".equals(codetype)) {
                        field = "organcode";
                    }
                    AuditRsCompanyBaseinfo auditRsCompanyBaseinfo = iAuditRsCompanyBaseinfo
                            .getCompanyByOneField(field, corpcode).getResult();
                    itemlegalcertnum = corpcode;
                    if (auditRsCompanyBaseinfo == null) {
                        return JsonUtils.zwdtRestReturn("0", "获取企业信息失败,请检查统一社会信用代码或组织机构代码是否配置正确", "");
                    }
                    itemlegaldept = auditRsCompanyBaseinfo.getStr("organname");
                    legalpersonicardnum = auditRsCompanyBaseinfo.getStr("orgalegal_idnumber"); //法人身份证
                    corpname = auditRsCompanyBaseinfo.getStr("organname"); // 单位名字
                    legal = auditRsCompanyBaseinfo.getStr("organlegal");
                    // 如果是建设单位则需要同步一些字段到项目表
                    AuditRsItemBaseinfo auditRsBaseItemInfo = iAuditRsItemBaseinfo
                            .getAuditRsItemBaseinfoByRowguid(itemguid).getResult();
                    auditRsBaseItemInfo.set("departname", corpname); // 建设单位名字
                    auditRsBaseItemInfo.set("legalperson", legal); // 建设法人代表
                    auditRsBaseItemInfo.set("legalpersonicardnum", legalpersonicardnum); // 建设法人代表身份证
                    auditRsBaseItemInfo.set("contractidcart", danweilxrsfz); // 联系人身份证
                    auditRsBaseItemInfo.set("constructionaddress", address); // 联系地址
                    auditRsBaseItemInfo.set("contractperson", danweilxr); // 联系人
                    auditRsBaseItemInfo.set("contractphone", danweilxrlxdh); // 联系人电话
                    auditRsBaseItemInfo.set("legalproperty", legalproperty); // 法人性质
                    auditRsBaseItemInfo.set("itemlegaldept", itemlegaldept); // 项目建设单位
                    auditRsBaseItemInfo.set("frphone", frphone); // 法人电话
                    auditRsBaseItemInfo.set("fremail", fremail); // 法人邮箱
                    iAuditRsItemBaseinfo.updateAuditRsItemBaseinfo(auditRsBaseItemInfo);
                }

                ParticipantsInfo participantsInfo = new ParticipantsInfo();
                if (StringUtil.isNotBlank(dwguid)) {
                    SqlConditionUtil sqlConditionUtil = new SqlConditionUtil();
                    sqlConditionUtil.eq("rowguid", dwguid);
                    List<ParticipantsInfo> infos = iParticipantsInfo
                            .getParticipantsInfoListByCondition(sqlConditionUtil.getMap());
                    if (!infos.isEmpty()) {
                        ParticipantsInfo info = infos.get(0);
                        corpname = info.getCorpname();
                        corpcode = info.getCorpcode();
                    }
                }
                participantsInfo.setRowguid(StringUtil.isNotBlank(dwguid) ? dwguid : UUID.randomUUID().toString());
                participantsInfo.setOperatedate(new Date());
                participantsInfo.setSubappguid(subappguid);// 子申报示例
                participantsInfo.setItemguid(itemguid);// 项目标识
                participantsInfo.setCorpname(corpname);// 单位名称
                participantsInfo.setCorpcode(corpcode);// 统一社会信用代码
                participantsInfo.setLegal(legal);// 法人代表
                participantsInfo.setPhone(phone);// 联系电话
                participantsInfo.setAddress(address);// 单位地址
                participantsInfo.setXmfzr(xmfzr);// 项目负责人
                participantsInfo.setXmfzr_zc(xmfzr_zc);// 职称
                participantsInfo.setXmfzr_idcard(xmfzr_idcard);// 身份证
                participantsInfo.setXmfzr_phone(xmfzr_phone);// 项目负责任人联系电话
                participantsInfo.setCorptype(corptype);// 单位类型
                participantsInfo.setLegalproperty(legalproperty);
                participantsInfo.setItemlegalcerttype(itemlegalcerttype);
                participantsInfo.setItemlegalcertnum(itemlegalcertnum);
                participantsInfo.setItemlegaldept(itemlegaldept);
                participantsInfo.setLegalpersonicardnum(legalpersonicardnum);
                participantsInfo.setFrphone(frphone);
                participantsInfo.setFremail(fremail);
                participantsInfo.setDanweilxr(danweilxr);
                participantsInfo.setDanweilxrlxdh(danweilxrlxdh);
                participantsInfo.setDanweilxrsfz(danweilxrsfz);
                // 施工单位
                if ("3".equals(corptype)) {
                    participantsInfo.setCbdanweitype(cbdanweitype);
                    participantsInfo.setJsfzr(jsfzr);
                    participantsInfo.setJsfzr_zc(jsfzr_zc);
                    participantsInfo.setJsfzr_phone(jsfzr_phone);
                    participantsInfo.setXmfzperson(xmfzperson);
                    participantsInfo.setXmfzrsafenum(xmfzrsafenum);
                    participantsInfo.setXmfzrphonenum(xmfzrphonenum);
                    participantsInfo.setQylxr(qylxr);
                    participantsInfo.setQylxdh(qylxdh);
                    participantsInfo.setGdlxr(gdlxr);
                    participantsInfo.setGdlxdh(gdlxdh);
                    participantsInfo.setFbsafenum(fbsafenum);
                    participantsInfo.setFbtime(fbtime);
                    participantsInfo.setFbscopeofcontract(fbscopeofcontract);
                    if (StringUtil.isNotBlank(fbqysettime)) {
                        participantsInfo.setFbqysettime(EpointDateUtil.convertString2Date(fbqysettime, "yyyy-MM-dd"));
                    }
                    participantsInfo.setFbaqglry(fbaqglry);
                    participantsInfo.setFbaqglrysafenum(fbaqglrysafenum);
                }
                ParticipantsInfo participantsInfoExist = iParticipantsInfo.find(dwguid);
                if (participantsInfoExist != null) {
                    iParticipantsInfo.update(participantsInfo);
                    return JsonUtils.zwdtRestReturn("1", "修改成功", "");
                } else {
                    iParticipantsInfo.insert(participantsInfo);
                    return JsonUtils.zwdtRestReturn("1", "保存成功", "");
                }

            } else {
                return JsonUtils.zwdtRestReturn("0", "保存失败！", "");
            }
        } else {
            return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
        }
    }

    @RequestMapping(value = "/getParticipants", method = RequestMethod.POST)
    public String getParticipants(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                // 1、接口的入参转化为JSON对象
                JSONObject param = jsonObject.getJSONObject("params");
                // 1.1、获取子申报实例
                String subAppGuid = param.getString("subappguid");
                // 1.5、获取项目标识
                String itemGuid = param.getString("itemguid");
                // 1.6、 获得单位type
                String corpType = param.getString("corptype");
                JSONObject data = new JSONObject();
                // 2、初始化建设单位信息
                List<JSONObject> participantsList = new ArrayList<JSONObject>();
                // 判断这个是主项目还是子项目
                AuditRsItemBaseinfo auditRsItemBaseinfo = iAuditRsItemBaseinfo.getAuditRsItemBaseinfoByRowguid(itemGuid)
                        .getResult();
                if (auditRsItemBaseinfo != null) {
                    // 如果是子项目则获取主项目guid
                    if (StringUtil.isNotBlank(auditRsItemBaseinfo.getParentid())) {
                        itemGuid = auditRsItemBaseinfo.getParentid();
                    }
                }
                if ("31".equals(corpType)) {
                    SqlConditionUtil sql = new SqlConditionUtil();
                    sql.eq("itemGuid", itemGuid);
                    sql.eq("corptype", corpType);
                    List<ParticipantsInfo> participantsInfoList = iParticipantsInfo
                            .getParticipantsInfoListByCondition(sql.getMap());
                    if (participantsInfoList.size() > 0) {
                        JSONObject particiJson = new JSONObject();
                        particiJson.put("par_rowguid", participantsInfoList.get(0).getRowguid());// 单位信息的rowguid字段
                        particiJson.put("corpname", participantsInfoList.get(0).getCorpname());// 单位名称
                        particiJson.put("corpcodestr", participantsInfoList.get(0).getCorpcode());// 统一社会信用代码
                        particiJson.put("legal", participantsInfoList.get(0).getLegal());// 法人代表
                        particiJson.put("phone", participantsInfoList.get(0).getPhone());// 单位联系电话
                        particiJson.put("fbtime", participantsInfoList.get(0).getFbtime());// 法人性质
                        particiJson.put("itemlegalcerttype", participantsInfoList.get(0).getItemlegalcerttype());// 项目(法人)证件类型
                        particiJson.put("xmfzr", participantsInfoList.get(0).getXmfzr());// 项目负责人
                        particiJson.put("xmfzr_phone", participantsInfoList.get(0).getXmfzr_phone());// 项目负责人联系方式
                        particiJson.put("corptype", participantsInfoList.get(0).getCorptype());
                        if (StringUtil.isNotBlank(participantsInfoList.get(0).getCbdanweitype())) {
                            particiJson.put("cbdanweitype", "01".equals(participantsInfoList.get(0).getCbdanweitype())
                                    ? "总包单位" : "专业承包及劳务分包单位");// 项目负责人联系方式
                        }
                        participantsList.add(particiJson);
                    }
                } else {
                    SqlConditionUtil sql = new SqlConditionUtil();
                    sql.eq("itemGuid", itemGuid);
                    sql.eq("corptype", corpType);
                    sql.eq("subappguid", subAppGuid);
                    List<ParticipantsInfo> participantsInfoList = iParticipantsInfo
                            .getParticipantsInfoListByCondition(sql.getMap());
                    for (ParticipantsInfo participantsInfo : participantsInfoList) {
                        JSONObject particiJson = new JSONObject();
                        particiJson.put("par_rowguid", participantsInfo.getRowguid());// 单位信息的rowguid字段
                        particiJson.put("corpname", participantsInfo.getCorpname());// 单位名称
                        particiJson.put("corpcodestr", participantsInfo.getCorpcode());// 统一社会信用代码
                        particiJson.put("legal", participantsInfo.getLegal());// 法人代表
                        particiJson.put("phone", participantsInfo.getPhone());// 单位联系电话
                        particiJson.put("fbtime", participantsInfo.getFbtime());// 法人性质
                        particiJson.put("itemlegalcerttype", participantsInfo.getItemlegalcerttype());// 项目(法人)证件类型
                        particiJson.put("xmfzr", participantsInfo.getXmfzr());// 项目负责人
                        particiJson.put("xmfzr_phone", participantsInfo.getXmfzr_phone());// 项目负责人联系方式
                        particiJson.put("corptype", participantsInfo.getCorptype());
                        if (StringUtil.isNotBlank(participantsInfo.getCbdanweitype())) {
                            particiJson.put("cbdanweitype",
                                    "01".equals(participantsInfo.getCbdanweitype()) ? "总包单位" : "专业承包及劳务分包单位");// 项目负责人联系方式
                            particiJson.put("iscbdanweitype", 1);
                        }
                        participantsList.add(particiJson);
                    }
                }
                data.put("participantsList", participantsList);
                return JsonUtils.zwdtRestReturn("1", "查询成功", data.toString());
            } else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("=======getParticipants接口参数：params【" + params + "】=======");
            log.info("=======getParticipants接口异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "单位控件列表获取异常", "");
        }
    }

    /**
     * 查询单位列表接口
     *
     * @param params  传入的参数
     * @param request HTTP请求
     * @return
     */
    @RequestMapping(value = "/getDanwei", method = RequestMethod.POST)
    public String getDanwei(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用saveDanwei接口=======");
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                // 1、接口的入参转化为JSON对象
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.11、 联系电话
                String dwguid = obj.getString("dwguid");
                String itemguid = obj.getString("itemguid");
                // 2、获取用户注册信息
                AuditOnlineRegister auditOnlineRegister = this.getOnlineRegister(request);
                if (auditOnlineRegister != null) {
                    ParticipantsInfo participantsInfo = null;
                    String corpname = "";
                    String corpcode = "";
                    String address = "";
                    String phone = "";
                    String legal = "";
                    //String legalproperty = "";
                    List<JSONObject> itemlegalcerttypeList = new ArrayList<>();
                    List<JSONObject> legalpropertyList = new ArrayList<>();
                    String itemlegalcertnum = "";
                    String itemlegaldept = "";
                    String legalpersonicardnum = "";
                    String frphone = "";
                    String fremail = "";
                    if (StringUtil.isNotBlank(dwguid)) {
                        participantsInfo = iParticipantsInfo.find(dwguid);
                    }

                    JSONObject data = new JSONObject();
                    if (participantsInfo != null) {
                        corpname = participantsInfo.getCorpname();
                        corpcode = participantsInfo.getCorpcode();
                        address = participantsInfo.getAddress();
                        phone = participantsInfo.getPhone();
                        legal = participantsInfo.getLegal();
                        String itemlegalcerttype = "";
                        String legalproperty = "";
                        String cbtypledetail = "";
                        if (StringUtil.isNotBlank(participantsInfo.getItemlegalcerttype())) {
                            itemlegalcerttype = iCodeItemsService.getItemTextByCodeName("申请人用来唯一标识的证照类型",
                                    participantsInfo.getItemlegalcerttype());
                        }
                        if (StringUtil.isNotBlank(participantsInfo.getLegalproperty())) {
                            legalproperty = iCodeItemsService.getItemTextByCodeName("法人性质",
                                    participantsInfo.getLegalproperty());
                        }
                        data.put("itemlegalcerttypedetail", itemlegalcerttype);
                        data.put("legalpropertydetail", legalproperty);
                        if (StringUtil.isNotBlank(participantsInfo.getCbdanweitype())) {
                            cbtypledetail = iCodeItemsService.getItemTextByCodeName("单位类型",
                                    participantsInfo.getCbdanweitype());

                        }
                        data.put("cbtypledetail", cbtypledetail);
                        itemlegalcertnum = participantsInfo.getItemlegalcertnum();
                        itemlegaldept = participantsInfo.getItemlegaldept();
                        legalpersonicardnum = participantsInfo.getLegalpersonicardnum();
                        frphone = participantsInfo.getFrphone();
                        fremail = participantsInfo.getFremail();
                        data.put("xmfzr", participantsInfo.getXmfzr());// 项目负责人
                        data.put("xmfzr_zc", participantsInfo.getXmfzr_zc());// 职称
                        data.put("xmfzr_idcard", participantsInfo.getXmfzr_idcard());// 身份证
                        data.put("xmfzr_phone", participantsInfo.getXmfzr_phone());// 项目负责人联系电话
                        if (StringUtil.isNotBlank(participantsInfo.getCbdanweitype())) {
                            data.put("cbdanweitype", participantsInfo.getCbdanweitype());
                        }
                        data.put("jsfzr", participantsInfo.getJsfzr());
                        data.put("jsfzr_zc", participantsInfo.getJsfzr_zc());
                        data.put("jsfzr_phone", participantsInfo.getJsfzr_phone());
                        data.put("xmfzperson", participantsInfo.getXmfzperson());
                        data.put("xmfzrsafenum", participantsInfo.getXmfzrsafenum());
                        data.put("xmfzrphonenum", participantsInfo.getXmfzrphonenum());
                        data.put("qylxr", participantsInfo.getQylxr());
                        data.put("qylxdh", participantsInfo.getQylxdh());
                        data.put("gdlxr", participantsInfo.getGdlxr());
                        data.put("gdlxdh", participantsInfo.getGdlxdh());
                        data.put("fbsafenum", participantsInfo.getFbsafenum());
                        data.put("fbtime", participantsInfo.getFbtime());
                        data.put("fbscopeofcontract", participantsInfo.getFbscopeofcontract());
                        data.put("fbaqglrysafenum", participantsInfo.getFbaqglrysafenum());
                        data.put("fbqysettime",
                                EpointDateUtil.convertDate2String(participantsInfo.getFbqysettime(), "yyyy-MM-dd"));
                        data.put("fbaqglry", participantsInfo.getFbaqglry());
                        data.put("danweilxr", participantsInfo.getDanweilxr());
                        data.put("danweilxrlxdh", participantsInfo.getDanweilxrlxdh());
                        data.put("danweilxrsfz", participantsInfo.getDanweilxrsfz());
                        data.put("frphone", participantsInfo.getFrphone());
                        data.put("fremail", participantsInfo.getFremail());
                        data.put("legalproperty", participantsInfo.getLegalproperty());
                        data.put("legal", participantsInfo.getLegal());

                        List<CodeItems> itemlegalcerttypes = iCodeItemsService
                                .listCodeItemsByCodeName("申请人用来唯一标识的证照类型");
                        JSONObject objtype = new JSONObject();
                        objtype.put("itemvalue", "");
                        objtype.put("itemtext", "请选择");
                        itemlegalcerttypeList.add(objtype);
                        for (CodeItems codeItems : itemlegalcerttypes) {
                            if (Integer.parseInt(codeItems.getItemValue()) >= Integer
                                    .parseInt(ZwfwConstant.CERT_TYPE_SFZ)) {
                                continue;
                            }
                            JSONObject objJson = new JSONObject();
                            objJson.put("itemvalue", codeItems.getItemValue());
                            objJson.put("itemtext", codeItems.getItemText());
                            if (codeItems.getItemValue().equals(participantsInfo.getItemlegalcerttype())) {
                                objJson.put("isselected", 1);
                            }
                            itemlegalcerttypeList.add(objJson);
                        }
                        List<CodeItems> legalpropertys = iCodeItemsService.listCodeItemsByCodeName("法人性质");
                        objtype = new JSONObject();
                        objtype.put("itemvalue", "");
                        objtype.put("itemtext", "请选择");
                        legalpropertyList.add(objtype);
                        for (CodeItems codeItems : legalpropertys) {
                            JSONObject objJson = new JSONObject();
                            objJson.put("itemvalue", codeItems.getItemValue());
                            objJson.put("itemtext", codeItems.getItemText());
                            if (codeItems.getItemValue().equals(participantsInfo.getLegalproperty())) {
                                objJson.put("isselected", 1);
                            }
                            legalpropertyList.add(objJson);
                        }
                        data.put("corpname", corpname);
                        data.put("corpcode", corpcode);
                        data.put("address", address);
                        data.put("phone", phone);
                        data.put("legal", legal);
                        data.put("legalproperty", legalpropertyList);
                        data.put("itemlegalcerttype", itemlegalcerttypeList);
                        data.put("itemlegalcertnum", itemlegalcertnum);
                        data.put("itemlegaldept", itemlegaldept);
                        data.put("legalpersonicardnum", legalpersonicardnum);
                        data.put("frphone", frphone);
                        data.put("fremail", fremail);
                    } else {
                        AuditRsItemBaseinfo auditRsItemBaseinfo = iAuditRsItemBaseinfo
                                .getAuditRsItemBaseinfoByRowguid(itemguid).getResult();
                        if (auditRsItemBaseinfo != null) {
                            // 获取统一社会信用代码/组织机构代码证
                            corpcode = auditRsItemBaseinfo.getStr("itemlegalcertnum");
                            // 通过这个字段查询企业信息
                            String codetype = auditRsItemBaseinfo.getStr("itemlegalcerttype");
                            String field = "creditcode";
                            if ("14".equals(codetype)) {
                                field = "organcode";
                            }
                            AuditRsCompanyBaseinfo auditRsCompanyBaseinfo = iAuditRsCompanyBaseinfo
                                    .getCompanyByOneField(field, corpcode).getResult();
                            itemlegalcertnum = corpcode;
                            if (auditRsCompanyBaseinfo == null) {
                                return JsonUtils.zwdtRestReturn("0", "获取企业信息失败", "");
                            }
                            itemlegaldept = auditRsCompanyBaseinfo.getOrganname();
                            legalpersonicardnum = auditRsCompanyBaseinfo.getOrgalegal_idnumber(); //法人身份证
                            corpname = auditRsCompanyBaseinfo.getOrganname(); // 单位名字
                            legal = auditRsCompanyBaseinfo.getOrganlegal();
                            address = auditRsItemBaseinfo.getConstructionaddress();
                            phone = auditRsItemBaseinfo.getContractphone();
                            corpcode = StringUtils.isNotBlank(auditRsItemBaseinfo.getItemlegalcreditcode())?auditRsItemBaseinfo.getItemlegalcreditcode():auditRsItemBaseinfo.getItemlegalcertnum();
                            List<CodeItems> itemlegalcerttypes = iCodeItemsService
                                    .listCodeItemsByCodeName("申请人用来唯一标识的证照类型");
                            JSONObject objtype = new JSONObject();
                            objtype.put("itemvalue", "");
                            objtype.put("itemtext", "请选择");
                            itemlegalcerttypeList.add(objtype);
                            for (CodeItems codeItems : itemlegalcerttypes) {
                                if (Integer.parseInt(codeItems.getItemValue()) >= Integer
                                        .parseInt(ZwfwConstant.CERT_TYPE_SFZ)) {
                                    continue;
                                }
                                JSONObject objJson = new JSONObject();
                                objJson.put("itemvalue", codeItems.getItemValue());
                                objJson.put("itemtext", codeItems.getItemText());
                                if (codeItems.getItemValue().equals(auditRsItemBaseinfo.getItemlegalcerttype())) {
                                    objJson.put("isselected", 1);
                                }
                                itemlegalcerttypeList.add(objJson);
                            }
                            List<CodeItems> legalproperty = iCodeItemsService.listCodeItemsByCodeName("法人性质");
                            objtype = new JSONObject();
                            objtype.put("itemvalue", "");
                            objtype.put("itemtext", "请选择");
                            legalpropertyList.add(objtype);
                            for (CodeItems codeItems : legalproperty) {
                                JSONObject objJson = new JSONObject();
                                objJson.put("itemvalue", codeItems.getItemValue());
                                objJson.put("itemtext", codeItems.getItemText());
                                if (codeItems.getItemValue().equals(auditRsItemBaseinfo.getLegalproperty())) {
                                    objJson.put("isselected", 1);
                                }
                                legalpropertyList.add(objJson);
                            }
                            frphone = auditRsItemBaseinfo.getFrphone();
                            fremail = auditRsItemBaseinfo.getFremail();
                            data.put("item_corpname", corpname);
                            data.put("item_corpcode", corpcode);
                            data.put("item_address", address);
                            data.put("item_phone", phone);
                            data.put("item_legal", legal);
                            data.put("item_legalproperty", legalpropertyList);
                            data.put("item_itemlegalcerttype", itemlegalcerttypeList);
                            data.put("item_itemlegalcertnum", itemlegalcertnum);
                            data.put("item_itemlegaldept", itemlegaldept);
                            data.put("item_legalpersonicardnum", legalpersonicardnum);
                            data.put("item_frphone", frphone);
                            data.put("item_fremail", fremail);
                            data.put("danweilxr", auditRsItemBaseinfo.getContractperson());
                            data.put("danweilxrlxdh", auditRsItemBaseinfo.getContractphone());

                        }
                    }
                    List<JSONObject> cbtyple = new ArrayList<>();
                    List<CodeItems> cbtypleList = iCodeItemsService.listCodeItemsByCodeName("单位类型");
                    for (CodeItems codeItems : cbtypleList) {
                        JSONObject objJson = new JSONObject();
                        objJson.put("itemvalue", codeItems.getItemValue());
                        objJson.put("itemtext", codeItems.getItemText());
                        if (participantsInfo != null
                                && codeItems.getItemValue().equals(participantsInfo.getCbdanweitype())) {
                            objJson.put("isselected", 1);
                        }
                        cbtyple.add(objJson);
                    }
                    data.put("cbtyple", cbtyple);
                    log.info("=======结束调用saveDanwei接口=======");
                    return JsonUtils.zwdtRestReturn("1", " 保存成功！", data.toString());
                } else {
                    return JsonUtils.zwdtRestReturn("0", "获取用户失败！", "");
                }
            } else {
                log.info("=======结束调用saveDanwei接口=======");
                return JsonUtils.zwdtRestReturn("0", "身份验证失败", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("=======saveDanwei接口参数：params【" + params + "】=======");
            log.info("=======saveDanwei异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "保存失败", "");
        }
    }

    @RequestMapping(value = "/delParticipantInfo", method = RequestMethod.POST)
    public String delParticipantInfo(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始获取delParticipantInfo接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject json = JSONObject.parseObject(params);
            JSONObject obj = (JSONObject) json.get("params");
            String token = json.getString("token");
            String dwguid = obj.getString("dwguid");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                if (StringUtil.isNotBlank(dwguid)) {
                    iParticipantsInfo.deleteByGuid(obj.getString("dwguid"));
                }
                return JsonUtils.zwdtRestReturn("1", "删除成功", "");
            } else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        } catch (Exception e) {
            return JsonUtils.zwdtRestReturn("0", "系统异常", "");
        }
    }

    /**
     * 查询单体工程目录接口
     *
     * @param params  传入的参数
     * @param request HTTP请求
     * @return
     */
    @RequestMapping(value = "/getItemTreeList", method = RequestMethod.POST)
    public String getItemTreeList(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                // 1、接口的入参转化为JSON对象
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、 子申报标识
                String subappguid = obj.getString("subappguid");
                // 2、获取用户注册信息
                AuditOnlineRegister auditOnlineRegister = this.getOnlineRegister(request);
                if (auditOnlineRegister != null) {
                    List<DantiSubRelation> dantiSubRelationList = iDantiSubRelationService
                            .findListBySubappGuid(subappguid);
                    List<Record> records = new ArrayList<>();
                    for (DantiSubRelation dantiSubRelation : dantiSubRelationList) {
                        Record record = new Record();
                        record.set("pid", subappguid);
                        record.set("id", dantiSubRelation.getDantiguid());
                        record.set("text", "");
                        records.add(record);
                    }
                    JSONObject dataJson = new JSONObject();
                    dataJson.put("tree", records);
                    return JsonUtils.zwdtRestReturn("1", " 查询成功！", dataJson.toString());
                } else {
                    return JsonUtils.zwdtRestReturn("0", "获取用户失败！", "");
                }
            } else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败", "");
            }
        } catch (Exception e) {
            return JsonUtils.zwdtRestReturn("0", "系统异常", "");
        }
    }

    /**
     * 查询单体工程目录接口
     *
     * @param params  传入的参数
     * @param request HTTP请求
     * @return
     */
    @RequestMapping(value = "/getDantiTreeList", method = RequestMethod.POST)
    public String getDantiTreeList(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getItemTreeList接口=======");
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                // 1、接口的入参转化为JSON对象
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、 子申报标识
                String itemguid = obj.getString("itemguid");
                // 2、获取用户注册信息
                AuditOnlineRegister auditOnlineRegister = this.getOnlineRegister(request);
                if (auditOnlineRegister != null) {
                    AuditRsItemBaseinfo auditRsItemBaseinfo = iAuditRsItemBaseinfo
                            .getAuditRsItemBaseinfoByRowguid(itemguid).getResult();
                    List<JSONObject> list_tree = new ArrayList<>();
                    String itemname = "";
                    if (auditRsItemBaseinfo != null && StringUtil.isNotBlank(auditRsItemBaseinfo.getItemname())) {
                        itemname = auditRsItemBaseinfo.getItemname();
                    }
                    JSONObject objTree = new JSONObject();
                    objTree.put("pId", "root");
                    objTree.put("id", itemguid);
                    objTree.put("name", StringUtil.isNotBlank(itemname) ? itemname : "单位工程");
                    objTree.put("open", true);
                    list_tree.add(objTree);
                    SqlConditionUtil sql = new SqlConditionUtil();
                    sql.eq("projectguid", itemguid);
                    sql.setSelectFields("gongchengguid");
                    List<DantiInfo> dantiInfos = iDantiInfoService.getDantiInfoListByCondition(sql.getMap());
                    Map<String, String> map = new HashMap<>();
                    for (DantiInfo dantiInfo : dantiInfos) {
                        map.put(dantiInfo.getGongchengguid(), "");
                    }
                    for (String key : map.keySet()) {
                        if (StringUtil.isNotBlank(key)) {
                            objTree = new JSONObject();
                            objTree.put("id", key);
                            objTree.put("pId", itemguid);
                            DwgcInfo dwgcInfo = iDwgcInfoService.find(key);
                            if (dwgcInfo != null) {
                                if (StringUtil.isNotBlank(dwgcInfo.getGongchengname())) {
                                    objTree.put("name", dwgcInfo.getGongchengname());
                                } else {
                                    objTree.put("name", "未命名单体工程");
                                }
                            }
                            list_tree.add(objTree);
                        }
                    }
                    JSONObject dataJson = new JSONObject();
                    dataJson.put("tree", list_tree);
                    return JsonUtils.zwdtRestReturn("1", " 查询成功！", dataJson.toString());
                } else {
                    return JsonUtils.zwdtRestReturn("0", "获取用户失败！", "");
                }
            } else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败", "");
            }
        } catch (Exception e) {
            return JsonUtils.zwdtRestReturn("0", "系统异常", "");
        }
    }

    /**
     * 查询工程类别接口
     *
     * @param params  传入的参数
     * @param request HTTP请求
     * @return
     */
    @RequestMapping(value = "/getGongchengTreeList", method = RequestMethod.POST)
    public String getGongchengTreeList(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                // 2、获取用户注册信息
                AuditOnlineRegister auditOnlineRegister = this.getOnlineRegister(request);
                if (auditOnlineRegister != null) {
                    List<CodeItems> codeItems = iCodeItemsService.listCodeItemsByCodeName("项目分类");
                    List<JSONObject> jsonList = new ArrayList<>();
                    for (CodeItems codeItem : codeItems) {
                        if (codeItem.getItemValue().length() == 2 && !"02".equals(codeItem.getItemValue())) {
                            JSONObject obj = new JSONObject();
                            obj.put("id", codeItem.getItemValue());
                            obj.put("pId", "root");
                            obj.put("name", codeItem.getItemText());
                            jsonList.add(obj);
                        }
                        if (codeItem.getItemValue().length() == 4
                                && !"02".equals(codeItem.getItemValue().substring(0, 2))) {
                            JSONObject obj = new JSONObject();
                            obj.put("id", codeItem.getItemValue());
                            obj.put("pId", codeItem.getItemValue().substring(0, 2));
                            obj.put("name", codeItem.getItemText());
                            jsonList.add(obj);
                        }
                        if (codeItem.getItemValue().length() == 6
                                && !"02".equals(codeItem.getItemValue().substring(0, 2))) {
                            JSONObject obj = new JSONObject();
                            obj.put("id", codeItem.getItemValue());
                            obj.put("pId", codeItem.getItemValue().substring(0, 4));
                            obj.put("name", codeItem.getItemText());
                            jsonList.add(obj);
                        }
                        if (codeItem.getItemValue().length() == 8
                                && !"02".equals(codeItem.getItemValue().substring(0, 2))) {
                            JSONObject obj = new JSONObject();
                            obj.put("id", codeItem.getItemValue());
                            obj.put("pId", codeItem.getItemValue().substring(0, 6));
                            obj.put("name", codeItem.getItemText());
                            jsonList.add(obj);
                        }
                    }
                    JSONObject dataJson = new JSONObject();
                    dataJson.put("tree_list", jsonList);
                    return JsonUtils.zwdtRestReturn("1", " 查询成功", dataJson.toString());
                } else {
                    return JsonUtils.zwdtRestReturn("0", "请您登录后再试", "");
                }
            } else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败", "");
            }
        } catch (Exception e) {
            return JsonUtils.zwdtRestReturn("0", "查询失败", "");
        }
    }

    /**
     * 组建单位监理信息接口
     *
     * @param params  传入的参数
     * @param request HTTP请求
     * @return
     */
    @RequestMapping(value = "/saveJlneed", method = RequestMethod.POST)
    public String saveJlneed(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                // 1、接口的入参转化为JSON对象
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、单体标识数组
                String dantiguids = obj.getString("dantiguids");
                String gongchengnum = obj.getString("gongchengnum");
                String gongchengname = obj.getString("gongchengname");
                String zjyneedcnt = obj.getString("zjyneedcnt");
                String gongchengguid = obj.getString("gongchengguid");
                // 1.2、 jl_infos
                JSONArray jl_infos = obj.getJSONArray("jl_infos");
                // 2、获取用户注册信息
                AuditOnlineRegister auditOnlineRegister = this.getOnlineRegister(request);
                if (auditOnlineRegister != null) {
                    List<DwgcInfo> dwlist = iDwgcInfoService.findDwgcListByGongchengnum(gongchengnum);
                    if (dwlist != null && dwlist.size() > 0) {
                        return JsonUtils.zwdtRestReturn("0", "工程编号重复", "");
                    }
                    String rowguid = StringUtil.isNotBlank(gongchengguid) ? gongchengguid
                            : UUID.randomUUID().toString();
                    for (int i = 0; i < jl_infos.size(); i++) {
                        JSONObject object = (JSONObject) jl_infos.get(i);
                        DwgcJlneed dwgcJlneed = new DwgcJlneed();
                        String zhiwu = iCodeItemsService.getItemTextByCodeName("监理工程师职务", object.getString("jl_id"));
                        dwgcJlneed.setZhiwu(zhiwu);
                        dwgcJlneed.setNeedcnt(object.getInteger("jl_cnt"));
                        dwgcJlneed.setGongchengguid(rowguid);
                        dwgcJlneed.setRowguid(UUID.randomUUID().toString());
                        iDwgcJlneedService.insert(dwgcJlneed);
                    }
                    String[] dantiguidArr = dantiguids.split("_");
                    List<DantiInfo> dantiInfos = new ArrayList<>();
                    for (String dantiguid : dantiguidArr) {
                        dantiInfos.add(iDantiInfoService.find(dantiguid));
                    }
                    String jiegouTx = "";
                    String gclb = "";
                    Double totalprice = 0d;
                    Double buildarea = 0d;
                    List<String> dishang = new ArrayList<>();
                    List<String> dixia = new ArrayList<>();
                    for (DantiInfo dantiinfo : dantiInfos) {
                        if (dantiinfo.getJiegoutx() != null) {
                            String jiegoutixi = iCodeItemsService.getItemTextByCodeName("结构体系",
                                    dantiinfo.getJiegoutx() + "");
                            jiegouTx += (jiegoutixi + ";");
                        }
                        if (dantiinfo.getGclb() != null) {
                            String gongchengleibie = iCodeItemsService.getItemTextByCodeName("项目分类",
                                    dantiinfo.getGclb() + "");
                            gclb += (gongchengleibie + ";");
                        }
                        if (dantiinfo.getPrice() != null) {
                            totalprice += dantiinfo.getPrice();
                        }
                        if (dantiinfo.getZjzmj() != null) {
                            buildarea += dantiinfo.getZjzmj();
                        }
                        if (dantiinfo.getDscs() != null) {
                            dishang.add(dantiinfo.getDscs());
                        }
                        if (dantiinfo.getDxcs() != null) {
                            dixia.add(dantiinfo.getDxcs());
                        }
                    }
                    /*int dishangmax = 0;
                    int dixiamax = 0;
                    if (dishang.size() > 0) {
                        dishangmax = Collections.max(dishang);
                    }
                    if (dixia.size() > 0) {
                        dixiamax = Collections.max(dixia);
                    }*/
                    String jianzhuarea = buildarea + "平方米";
                    DwgcInfo dwgcInfo = new DwgcInfo();
                    dwgcInfo.setGongchengnum(gongchengnum);
                    dwgcInfo.setGongchengname(gongchengname);
                    dwgcInfo.setZjyneedcnt(StringUtil.isNotBlank(zjyneedcnt) ? Integer.parseInt(zjyneedcnt) : 0);
                    dwgcInfo.setJiegoutype(jiegouTx);
                    dwgcInfo.setProjecttype(gclb);
                    dwgcInfo.setProjectprice(totalprice);
                    dwgcInfo.setBuildarea(jianzhuarea);
                    //dwgcInfo.setDishangcs(dishangmax);
                    //dwgcInfo.setDixiacs(dixiamax);
                    dwgcInfo.setRowguid(rowguid);
                    //子单位工程组建
                    if (dantiguidArr != null) {
                        DantiInfo dantiInfo = new DantiInfo();
                        for (String dantirowguid : dantiguidArr) {
                            dantiInfo = iDantiInfoService.find(dantirowguid);
                            if (StringUtil.isBlank(dantiInfo.getGongchengguid())) {
                                dantiInfo.setGongchengguid(rowguid);
                                iDantiInfoService.update(dantiInfo);
                            }
                        }
                    }
                    dwgcInfo.setOperatedate(new Date());
                    iDwgcInfoService.insert(dwgcInfo);
                    return JsonUtils.zwdtRestReturn("1", " 保存成功！", "");
                } else {
                    return JsonUtils.zwdtRestReturn("0", "获取用户失败！", "");
                }
            } else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败", "");
            }
        } catch (Exception e) {
            return JsonUtils.zwdtRestReturn("0", "系统异常", "");
        }
    }

    /**
     * 初始化单位列表接口
     *
     * @param params  传入的参数
     * @param request HTTP请求
     * @return
     */
    @RequestMapping(value = "/initDanweiList", method = RequestMethod.POST)
    public String initDanweiList(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                // 1、接口的入参转化为JSON对象
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、 项目标识
                String itemguid = obj.getString("itemguid");
                // 1.2、 子申报标识
                String subAppguid = obj.getString("subappguid");
                // 1.3、 单位类型List
                String dwTypesStr = obj.getString("dwTypesStr");
                // 2、获取用户注册信息
                AuditOnlineRegister auditOnlineRegister = this.getOnlineRegister(request);
                if (auditOnlineRegister != null) {
                    //TODO API封装
                    SqlConditionUtil sql = new SqlConditionUtil();
                    sql.eq("corptype", "999");
                    sql.eq("subappguid", subAppguid);
                    List<ParticipantsInfo> infos = iParticipantsInfo.getParticipantsInfoListByCondition(sql.getMap());
                    ParticipantsInfo info = null;
                    if (infos != null && infos.size() > 0) {
                        info = infos.get(0);
                    }
                    //参见单位是否初始化判断
                    if (info == null) {
                        String dwTypes[] = dwTypesStr.split(",");
                        for (String dwType : dwTypes) {
                            if ("jsdw".equals(dwType)) {
                                continue;
                            }
                            String danweiNum = "";
                            switch (dwType) {
                                case "jsdw":
                                    danweiNum = "31";
                                    break;
                                case "sgdw":
                                    danweiNum = "3";
                                    break;
                                case "sjdw":
                                    danweiNum = "2";
                                    break;
                                case "kcdw":
                                    danweiNum = "1";
                                    break;
                                case "jldw":
                                    danweiNum = "4";
                                    break;
                                case "jcdw":
                                    danweiNum = "10";
                                    break;
                            }
                            SqlConditionUtil sqlParticipantsInfo = new SqlConditionUtil();
                            sqlParticipantsInfo.eq("itemguid", itemguid);
                            sqlParticipantsInfo.eq("corptype", danweiNum);
                            sqlParticipantsInfo.setSelectFields(
                                    "distinct corptype,danweilxrsfz,fbsafenum,cbdanweitype,fbtime,fbscopeofcontract,fbqysettime,fbaqglry,fbaqglrysafenum,xmfzperson,xmfzrsafenum,qylxr,qylxdh,gdlxr,gdlxdh,xmfzrphonenum,danweilxr,danweilxrlxdh, corpname, corpcode, legal, phone, address, cert, xmfzr, xmfzr_idcard, xmfzr_zc, xmfzr_phone, xmfzr_certlevel, xmfzr_certnum, xmfzr_certid, jsfzr, jsfzr_zc, jsfzr_phone, itemguid ,fremail ,frphone ,legalpersonicardnum,itemlegaldept,itemlegalcertnum,itemlegalcerttype,legalproperty");
                            List<ParticipantsInfo> list = iParticipantsInfo
                                    .getParticipantsInfoListByCondition(sqlParticipantsInfo.getMap());
                            for (ParticipantsInfo participantsInfo : list) {
                                participantsInfo.set("rowguid", UUID.randomUUID().toString());
                                participantsInfo.set("operatedate", new Date());
                                participantsInfo.set("subappguid", subAppguid);
                                iParticipantsInfo.insert(participantsInfo);
                                //iParticipantsInfo.insert(participantsInfo);
                            }
                        }
                        // 新增初始化数据
                        info = new ParticipantsInfo();
                        info.set("operatedate", new Date());
                        info.set("rowguid", UUID.randomUUID().toString());
                        info.set("subappguid", subAppguid);
                        info.set("corptype", "999");
                        info.set("corpname", "系统生成");
                        iParticipantsInfo.insert(info);
                    }
                    return JsonUtils.zwdtRestReturn("1", " 初始化成功！", "");
                } else {
                    return JsonUtils.zwdtRestReturn("0", "获取用户失败！", "");
                }
            } else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败", "");
            }
        } catch (Exception e) {
            return JsonUtils.zwdtRestReturn("0", "查询失败", "");
        }
    }

    /**
     * 获取用户唯一标识
     *
     * @param httpServletRequest
     * @return
     */
    private AuditOnlineRegister getOnlineRegister(HttpServletRequest httpServletRequest) {
        AuditOnlineRegister auditOnlineRegister;
        OAuthCheckTokenInfo oAuthCheckTokenInfo = CheckTokenUtil.getCheckTokenInfo(httpServletRequest);
        if (oAuthCheckTokenInfo != null) {
            // 手机端
            // 通过登录名获取用户
            auditOnlineRegister = iAuditOnlineRegister.getRegisterByIdorMobile(oAuthCheckTokenInfo.getLoginid())
                    .getResult();
        } else {
            // PC端
            String accountGuid = ZwdtUserSession.getInstance("").getAccountGuid();
            if (StringUtil.isNotBlank(accountGuid)) {
                auditOnlineRegister = iAuditOnlineRegister.getRegisterByAccountguid(accountGuid).getResult();
            } else {
                // 通过登录名获取用户
                auditOnlineRegister = iAuditOnlineRegister.getRegisterByIdorMobile(Authenticator.getCurrentIdentity())
                        .getResult();
            }
        }
        return auditOnlineRegister;
    }

    public String getNameByCode(String code) {
        //显示父节点及子节点的内容
        /*String grandson = "";
        String son = "";
        String father = "";
        String grdfather = "";
        StringBuilder sb = new StringBuilder();
        String allRood = "";
        if (code.contains(",")) {
            String[] codes = code.split(",");
            for (String codevalue : codes) {
                if (StringUtil.isNotBlank(codevalue)) {
                    grandson = iCodeItemsService.getItemTextByCodeName("项目分类", codevalue);

                    if (codevalue.length() > 2) {
                        grdfather = iCodeItemsService.getItemTextByCodeName("项目分类", codevalue.substring(0, 2));
                    }
                    if (codevalue.length() > 4) {
                        father = iCodeItemsService.getItemTextByCodeName("项目分类", codevalue.substring(0, 4));
                    }
                    if (codevalue.length() > 6) {
                        son = iCodeItemsService.getItemTextByCodeName("项目分类", codevalue.substring(0, 6));
                    }
                    if (codevalue.length() == 8) {
                        allRood = grdfather + "·" + father + "·" + son + "·" + grandson;
                    }
                    if (codevalue.length() == 6) {
                        allRood = grdfather + "·" + father + "·" + grandson;
                    }
                    if (codevalue.length() == 4) {
                        allRood = grdfather + "·" + grandson;
                    }
                }

                sb.append(allRood);
                sb.append(";");
            }
        } else {
            if (StringUtil.isNotBlank(code)) {
                grandson = iCodeItemsService.getItemTextByCodeName("项目分类", code);

                if (code.length() > 2) {
                    grdfather = iCodeItemsService.getItemTextByCodeName("项目分类", code.substring(0, 2));
                }
                if (code.length() > 2) {
                    father = iCodeItemsService.getItemTextByCodeName("项目分类", code.substring(0, 2));
                }
                if (code.length() > 4) {
                    father = iCodeItemsService.getItemTextByCodeName("项目分类", code.substring(0, 4));
                }
                if (code.length() > 6) {
                    son = iCodeItemsService.getItemTextByCodeName("项目分类", code.substring(0, 6));
                }
                if (code.length() == 8) {
                    return grdfather + "·" + father + "·" + son + "·" + grandson;
                }
                if (code.length() == 6) {
                    return grdfather + "·" + father + "·" + grandson;
                }
                if (code.length() == 4) {
                    return grdfather + "·" + grandson;
                } else {
                    return grandson;
                }
            }
        }

        return sb.toString().substring(0, sb.toString().length() - 1);*/
        return iCodeItemsService.getItemTextByCodeName("国标_工程类别", code);
    }

    public String getItemText(String itemvalue, String codename) {
        String itemText = iCodeItemsService.getItemTextByCodeName(codename, itemvalue);
        return itemText;
    }

    /**
     * 获取单位类型数值
     *
     * @param dwType 单位类型
     * @return
     */
    private String getDanweiNum(String dwType) {
        String danweiNum = "";
        switch (dwType) {
            case "jsdw":
                danweiNum = "31";
                break;
            case "sgdw":
                danweiNum = "3";
                break;
            case "sjdw":
                danweiNum = "2";
                break;
            case "kcdw":
                danweiNum = "1";
                break;
            case "jldw":
                danweiNum = "4";
                break;
            case "jcdw":
                danweiNum = "10";
                break;
        }
        return danweiNum;
    }


    /**
     * 需要选择单位和人员的信息的单位新增
     */
    @RequestMapping(value = "/saveBuildInfo", method = RequestMethod.POST)
    public String saveBuildInfo(@RequestBody String params, @Context HttpServletRequest request) {
        JSONObject jsonObject = JSONObject.parseObject(params);
        String token = jsonObject.getString("token");
        if (ZwdtConstant.SysValidateData.equals(token)) {
            JSONObject param = jsonObject.getJSONObject("params");
            String dwguid = param.getString("dwguid");
            String itemguid = param.getString("itemguid");
            String subappguid = param.getString("subappguid");
            String type = param.getString("type");
            String fhdy = param.getString("fhdy");
            String jzmj = param.getString("jzmj");
            String zsgn = param.getString("zsgn");
            String kljb = param.getString("kljb");
            String fhjb = param.getString("fhjb");
            String psyt = param.getString("psyt");

            //----------------------------------------------------------------------------------------------------------
            if (StringUtil.isNotBlank(itemguid) && StringUtil.isNotBlank(subappguid)) {
                BuildInfo buildinfo = new BuildInfo();

                buildinfo.setRowguid(StringUtil.isNotBlank(dwguid) ? dwguid : UUID.randomUUID().toString());
                buildinfo.setOperatedate(new Date());
                buildinfo.setSubappguid(subappguid);// 子申报示例
                buildinfo.setItemguid(itemguid);// 项目标识
                buildinfo.setFhdy(fhdy);
                buildinfo.setJzmj(jzmj);
                buildinfo.setZsgn(zsgn);
                buildinfo.setKljb(kljb);
                buildinfo.setFhjb(fhjb);
                buildinfo.setPsyt(psyt);

                BuildInfo participantsInfoExist = iBuildInfoService.find(dwguid);
                if (participantsInfoExist != null) {
                    iBuildInfoService.update(buildinfo);
                    return JsonUtils.zwdtRestReturn("1", "修改成功", "");
                } else {
                    iBuildInfoService.insert(buildinfo);
                    return JsonUtils.zwdtRestReturn("1", "保存成功", "");
                }

            } else {
                return JsonUtils.zwdtRestReturn("0", "保存失败！", "");
            }
        } else {
            return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
        }
    }

    @RequestMapping(value = "/delBuildInfo", method = RequestMethod.POST)
    public String delBuildInfo(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始获取delBuildInfo接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject json = JSONObject.parseObject(params);
            JSONObject obj = (JSONObject) json.get("params");
            String token = json.getString("token");
            String dwguid = obj.getString("dwguid");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                if (StringUtil.isNotBlank(dwguid)) {
                    iBuildInfoService.deleteByGuid(obj.getString("dwguid"));
                }
                return JsonUtils.zwdtRestReturn("1", "删除成功", "");
            } else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        } catch (Exception e) {
            return JsonUtils.zwdtRestReturn("0", "系统异常", "");
        }
    }


    /**
     * 查询单位列表接口
     *
     * @param params  传入的参数
     * @param request HTTP请求
     * @return
     */
    @RequestMapping(value = "/getBuildInfo", method = RequestMethod.POST)
    public String getBuildInfo(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getBuildInfo接口=======");
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                // 1、接口的入参转化为JSON对象
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.11、 联系电话
                String dwguid = obj.getString("dwguid");
                String itemguid = obj.getString("itemguid");
                // 2、获取用户注册信息
                AuditOnlineRegister auditOnlineRegister = this.getOnlineRegister(request);
                if (auditOnlineRegister != null) {
                    BuildInfo buildInfo = null;

                    if (StringUtil.isNotBlank(dwguid)) {
                        buildInfo = iBuildInfoService.find(dwguid);
                    }

                    JSONObject data = new JSONObject();
                    if (buildInfo != null) {
                        data.put("fhdy", buildInfo.getFhdy());
                        data.put("jzmj", buildInfo.getJzmj());
                        data.put("zsgn", buildInfo.getZsgn());
                        data.put("kljb", buildInfo.getKljb());
                        data.put("fhjb", buildInfo.getFhjb());
                        data.put("psyt", buildInfo.getPsyt());
                    }
                    log.info("=======结束调用getBuildInfo接口=======");
                    return JsonUtils.zwdtRestReturn("1", " 保存成功！", data.toString());
                } else {
                    return JsonUtils.zwdtRestReturn("0", "获取用户失败！", "");
                }
            } else {
                log.info("=======结束调用getBuildInfo接口=======");
                return JsonUtils.zwdtRestReturn("0", "身份验证失败", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("=======saveDanwei接口参数：params【" + params + "】=======");
            log.info("=======saveDanwei异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "保存失败", "");
        }
    }


    /**
     * 查询单位列表接口
     *
     * @param params  传入的参数
     * @param request HTTP请求
     * @return
     */
    @RequestMapping(value = "/getBuildInfos", method = RequestMethod.POST)
    public String getBuildInfos(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getBuildInfos接口=======");
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                // 1、接口的入参转化为JSON对象
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.11、 联系电话
                String subappguid = obj.getString("subappguid");
                String itemguid = obj.getString("itemguid");
                // 2、获取用户注册信息
                AuditOnlineRegister auditOnlineRegister = this.getOnlineRegister(request);
                if (auditOnlineRegister != null) {
                    List<BuildInfo> buildInfos = null;

                    if (StringUtil.isNotBlank(subappguid)) {
                        String sql = " select * from build_info where SUBAPPGUID = ? and itemguid  = ?";
                        buildInfos = iBuildInfoService.findList(sql, subappguid, itemguid);
                    }

                    JSONObject data = new JSONObject();
                    data.put("buildlist", buildInfos);
                    log.info("=======结束调用getBuildInfos接口=======");
                    return JsonUtils.zwdtRestReturn("1", "获取建筑清单成功！", data.toString());
                } else {
                    return JsonUtils.zwdtRestReturn("0", "获取用户失败！", "");
                }
            } else {
                log.info("=======结束调用getBuildInfos接口=======");
                return JsonUtils.zwdtRestReturn("0", "身份验证失败", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("=======getBuildInfos接口参数：params【" + params + "】=======");
            log.info("=======getBuildInfos异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取建筑清单失败", "");
        }
    }


}
