package com.epoint.xmz.dataexcahnge.impl;

import java.lang.invoke.MethodHandles;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.auditresource.auditrsitembaseinfo.domain.AuditRsItemBaseinfo;
import com.epoint.basic.auditresource.auditrsitembaseinfo.impl.AuditRsItemBaseinfoImpl;
import com.epoint.cert.commonutils.HttpUtil;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.metadata.systemparameters.api.IConfigService;

public class SzjtXmExchangeService
{
    protected ICommonDao baseDao = CommonDao.getInstance();
    /**
     * 日志
     */
    private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());

    //获取项目信息
    public Object getProjectInfo(@RequestBody String xmdm, String creditcode) {

        Map<String, Object> params = new HashMap<>();
        Map<String, String> headers = new HashMap<>();
        Map<String, Object> map = new HashMap<>();
        headers.put("apiKey", "860094973448552448");
        //从配置文件中读取url
        // String url = "http://172.20.84.14/gateway/api/1/tzxmzxjg";本地政务外网地址

        IConfigService iconfigservice = ContainerFactory.getContainInfo().getComponent(IConfigService.class);
        String projectCode = xmdm;
        String url = iconfigservice.getFrameConfigValue("tzxmurl");
        if (StringUtil.isBlank(url)) {
            url = "http://59.206.96.178:9960/gateway/api/3/tzxmxx";
        }
        String appCode = "JNSWB";
        String salt = "JNWB0918";
        String token = DigestUtils.md5Hex(projectCode + salt);
        url = url + "?token=" + token + "&projectCode=" + projectCode + "&appCode=" + appCode;
        //调用省发改获取项目信息接口
        String resultXml = HttpUtil.doGet(url, headers);     
        log.info("请求发改接口地址返回结果"+resultXml);
        JSONObject jsonObject1 = new JSONObject();
        if (StringUtil.isNotBlank(resultXml)) {
            jsonObject1 = JSONObject.parseObject(resultXml);
        }else{
        	map.put("result", "5");
            return map.get("result");
        }

        //第一个data
        String data1 = jsonObject1.getString("data");
        //第二个data
        JSONObject jsonObject2 = JSONObject.parseObject(data1);
        String data2 = jsonObject2.getString("data");

        //截取去除中括号
        JSONObject data3 = JSONObject.parseObject(data2);
        if(StringUtil.isBlank(data3)){
        	map.put("result", "5");
            return map.get("result");
        }
        String tzxmdm = data3.getString("projectCode");//项目代码
        JSONObject project = data3.getJSONObject("project");
        //根据项目编码确定是否得到项目信息
        String project_code = tzxmdm;
        //法人省份证号判断是否得到项目信息
        if (StringUtil.isBlank(project_code)) {
            map.put("result", "2");
            return map.get("result");
        }
        else {
            //判断是否已经入库
            int count2 = baseDao
                    .queryInt("select count(1) from audit_rs_item_baseinfo where itemcode='" + project_code + "'");
            if (count2 != 0) {
                map.put("result", "3");
            }
            else {
                int result = insertIntoAuditProject(data3, creditcode);
                //成功的情况
                if (result == 1) {
                    map.put("result", "1");
                }
                else {
                    map.put("result", "4");
                }
            }
        }

        return map.get("result");
    }

    //项目入库
    private int insertIntoAuditProject(JSONObject data, String creditcode) {
        //业务库项目实体
        try {
            AuditRsItemBaseinfo auditrsitembaseinfo = new AuditRsItemBaseinfo();

            //项目代码
            String itemcode = data.getString("projectCode");//项目代码
            JSONObject project = data.getJSONObject("project");
            //项目名称   项目代码
            String itemname = project.getString("projectName");//项目名称
            String itemtype = project.getString("projectType");//项目类型
            String itemlegalcreditcode = project.getString("lerepCertno");//统一社会信用代码
            String itemstartdate = project.getString("startYear");//拟开工时间
            String itemfinishdate = project.getString("endYear");//拟建成时间
            String totalinvest = project.getString("investment");//总投资--总投资（万元）
            String contractperson = project.getString("linkMan");//联系人名称
            String legalpersonicardnum ="";//项目法人证照号码,法人身份证
            String itemlegaldept = project.getString("enterpriseName");//项目（法人）单位名称 ，项目（法人）单位
            String itemlegalcertnum = ""; //项目法人证照号码,项目法人证照号码

            String itemlegalcerttype = "";//项目法人证照类型，项目法人证照类型

            JSONArray lerepsbList = new JSONArray();
            lerepsbList = project.getJSONArray("lerepList");//联系人名称
            if (lerepsbList != null && !lerepsbList.isEmpty()) {
                for (Object r : lerepsbList) {
                    JSONObject rr = (JSONObject) r;
                    itemlegalcreditcode = rr.getString("lerepCertno");//统一社会信用代码
                    legalpersonicardnum = rr.getString("lerepCertno");//统一社会信用代码
                    itemlegalcertnum = rr.getString("lerepCertno");//统一社会信用代码
                    itemlegaldept = rr.getString("enterpriseName");//项目（法人）单位
                    //项目法人证照类型 转换
                    String lerepCerttype =rr.getString("lerepCerttype");
                    switch (lerepCerttype) {
                        //统一社会信用代码
                        case "A05300":
                            itemlegalcerttype="16";
                            break; 
                            //组织机构代码证
                        case "A05201":                          
                        case "A05202":
                        case "A05203":
                        case "A05204":
                            itemlegalcerttype="14";
                            break; 
                            //组织机构代码证 
                        default:
                            itemlegalcerttype="16";
                            break; 
                    }                 
                    //itemlegalcreditcode = rr.getString("contactName");//企业联系人姓名
                }
            }
            String contractphone = project.getString("linkPhone");//联系电话
            String constructionscaleanddesc = project.getString("projectContent");//建设内容---建设规模及内容

            String versiontime = project.getString("applyDate"); //申报日期对应并联审批信息表数据产生时间
            // String landarea = project.getString("areaAll");//总用地面积 暂时无对应
            // String jzmj = project.getString("areaBuild");//建筑面积

            System.out.println("project======" + project.toJSONString());
            double jzmj = StringUtil.isBlank(project.getString("areaBuild")) ? 0
                    : Double.parseDouble(project.getString("areaBuild"));
            //     

            String constructionsite = project.getString("placeCode");//建设地点 是code 不是详细
            String constructionsitedesc ="";//建设地点 是code 是详细
            String  getLandMode =project.getString("getLandMode");
            int tdhqfs=5;
            //土地获取方式进行转换
            if(StringUtil.isNotBlank(getLandMode)){
                switch (getLandMode) {
                    //统一社会信用代码
                    case "A00001":
                        tdhqfs=4;
                        break; 
                        //组织机构代码证
                    case "A00002":                                 
                        tdhqfs=2;
                        break; 
                        //组织机构代码证 
                    default:
                        tdhqfs=5;
                        break; 
                } 
            }          
            String constructionproperty = project.getString("constructPer");//建设性质 
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
            auditrsitembaseinfo.setRowguid(UUID.randomUUID().toString());
            auditrsitembaseinfo.setOperatedate(new Date());
            auditrsitembaseinfo.setItemcode(itemcode); //项目代码
            auditrsitembaseinfo.setItemname(itemname);//项目名称
            auditrsitembaseinfo.setItemtype(itemtype);//项目性质
            auditrsitembaseinfo.setItemlegalcreditcode(itemlegalcreditcode);//统一社会信用代码
            auditrsitembaseinfo.setConstructionscaleanddesc(constructionscaleanddesc);//建设内容---建设规模及内容
            auditrsitembaseinfo.setConstructionsite(constructionsite);//建设地点
            auditrsitembaseinfo.setConstructionsitedesc(constructionsitedesc);//建设地点详细
            auditrsitembaseinfo.setContractperson(contractperson);//联系人
            auditrsitembaseinfo.setContractphone(contractphone);//联系电话
            //auditrsitembaseinfo.setLegalpersonicardnum(legalpersonicardnum);//法人身份证号           
            auditrsitembaseinfo.setItemlegaldept(itemlegaldept);//项目（法人）单位
            auditrsitembaseinfo.setItemlegalcertnum(itemlegalcertnum);//项目（法人）单位
            auditrsitembaseinfo.setItemlegalcerttype("16");//项目（法人）单位
            auditrsitembaseinfo.setConstructionproperty(constructionproperty);//建设性质 
            auditrsitembaseinfo.setJzmj(jzmj);//建筑面积
            auditrsitembaseinfo.setTdhqfs(tdhqfs);//土地获取方式        
            if (StringUtil.isNotBlank(itemstartdate)) {
                Date dateStart = formatter.parse(itemstartdate);
                auditrsitembaseinfo.setItemstartdate(dateStart); //拟开工时间
            }
            if (StringUtil.isNotBlank(itemfinishdate)) {
                Date dateEnd = formatter.parse(itemfinishdate);
                auditrsitembaseinfo.setItemfinishdate(dateEnd); //拟建成时间
            }
            if (StringUtil.isNotBlank(totalinvest)) {
                double totalinvest2 = Double.parseDouble(totalinvest);
                auditrsitembaseinfo.setTotalinvest(totalinvest2);//总投资额
            }
            new AuditRsItemBaseinfoImpl().addAuditRsItemBaseinfo(auditrsitembaseinfo);
            if(StringUtil.isNotBlank(creditcode) && !creditcode.equals(itemlegalcreditcode)){
            	// 统一社会信用代码找拉取的项目信用代码不匹配
            	return 3;
            }
            return 1;
        }
        catch (Exception e) {
            e.printStackTrace();
            return 2;
        }

    }

    public Object getclshyj(String cliengguid) {
        String sql = "select notpassreason from clshyj where cliengguid=? order by operatedate desc";
        String notpassreason = baseDao.queryString(sql, cliengguid).toString();
        return notpassreason;

    }

    //查询接口的地址：String url = "http://59.209.86.249:8800/zzwk-service/maintenance/queryCertificateHuangShan";
    public String getCertificate(String url, String json) {
        Map<String, String> headers = new HashMap<>();

        String resultXml = HttpUtil.doPostJson(url, json, headers);
        JSONObject jsonc = JSON.parseObject(resultXml);
        String result = jsonc.getString("result");
        String flag = jsonc.getString("flag");
        String data = jsonc.getString("data");
        return flag;
    }
}
