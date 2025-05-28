package com.epoint.gjdw;

import java.lang.invoke.MethodHandles;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.auditresource.auditrsitembaseinfo.domain.AuditRsItemBaseinfo;
import com.epoint.basic.auditresource.auditrsitembaseinfo.inter.IAuditRsItemBaseinfo;
import com.epoint.basic.auditresource.company.domain.AuditRsCompanyBaseinfo;
import com.epoint.basic.auditresource.company.inter.IAuditRsCompanyBaseinfo;
import com.epoint.basic.auditsp.auditspinstance.domain.AuditSpInstance;
import com.epoint.basic.auditsp.auditspinstance.inter.IAuditSpInstance;
import com.epoint.basic.basedata.participantsinfo.api.IParticipantsInfoService;
import com.epoint.basic.basedata.participantsinfo.api.entity.ParticipantsInfo;
import com.epoint.common.util.JsonUtils;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ValidateUtil;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.frame.service.metadata.code.entity.CodeItems;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/gjdw")
public class GJDWRestController
{

    @Autowired
    private IAuditRsItemBaseinfo iAuditRsItemBaseinfo;

    @Autowired
    private IAuditRsCompanyBaseinfo iAuditRsCompanyBaseinfo;

    @Autowired
    private IParticipantsInfoService iParticipantsInfoService;

    @Autowired
    private IAuditSpInstance iAuditSpInstance;

    @Autowired
    private ICodeItemsService iCodeItemsService;

    private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());

    @RequestMapping(value = "/getItemList", method = RequestMethod.POST)
    public String getItemList(@RequestBody String params, @Context HttpServletRequest request) {
        log.info("==================开始调用getItemList====================");
        try {
            JSONObject jsonObject = JSONObject.parseObject(params);
            JSONObject obj = jsonObject.getJSONObject("data");
            String serCode = obj.getString("serCode");
            String orgNo = obj.getString("orgNo");
            String paramXml = obj.getString("param");
            String serviceCode = obj.getString("serviceCode");
            String target = obj.getString("target");
            log.info("获取到的入参：");
            log.info("serCode：" + serCode);
            log.info("orgNo：" + orgNo);
            log.info("param：" + paramXml);
            log.info("serviceCode：" + serviceCode);
            log.info("target：" + target);
            // 1、解析param
            Document ducument = DocumentHelper.parseText(paramXml);
            Element root = ducument.getRootElement();
            List<Element> elements = root.elements();
            JSONObject jsonxml = xmlToJSON(elements);
            JSONObject conObject = JSONObject.parseObject(jsonxml.getString("CONDITION"));
            JSONObject itemObject = JSONObject.parseObject(conObject.getString("item"));
            String itemcode = itemObject.getString("VALUE");
            log.info("解析获取到的入参：" + jsonxml);
            log.info("项目itemcode：" + itemcode);
            // 2、TODO 逻辑处理（模拟数据）
            JSONArray itemArray = getDemoData(itemcode);
            if (itemArray == null) {
                return JsonUtils.zwdtRestReturn("1", "未查询到对应的项目信息！", "");
            }
            // 3、返回数据
            JSONObject dataJson = new JSONObject();
            JSONObject jsonObject2 = new JSONObject();
            jsonObject2.put("msg", "调用成功");
            jsonObject2.put("SUCCESS", true);
            jsonObject2.put("ERRCODE", ZwfwConstant.CONSTANT_STR_ONE);
            jsonObject2.put("data", jsonToXml(itemArray));// rtnJson转xml
            dataJson.put("data", jsonObject2);
            dataJson.put("code", ZwfwConstant.CONSTANT_STR_ONE);
            dataJson.put("message", "成功");
            return dataJson.toJSONString();
        }
        catch (Exception e) {
            log.info(e.getMessage(), e);
            return JsonUtils.zwdtRestReturn("0", "获取项目列表异常：" + e.getMessage(), "");
        }
        finally {
            log.info("==================结束调用getItemList====================");
        }

    }

    private JSONArray getDemoData(String itemcode) {
        AuditRsItemBaseinfo itemBaseinfo = iAuditRsItemBaseinfo.getAuditRsItemBaseinfoByItemcode(itemcode).getResult();
        if (itemBaseinfo == null) {
            return null;
        }
        AuditSpInstance spInstance = iAuditSpInstance.getDetailByBIGuid(itemBaseinfo.getBiguid()).getResult();
        JSONArray itemArray = new JSONArray();

        JSONObject itemJson = new JSONObject();
        itemJson.put("XZQHDM", spInstance != null ? spInstance.getAreacode() : "");
        itemJson.put("XMMC", itemBaseinfo.getItemname());
        itemJson.put("XMID", itemBaseinfo.getRowguid());
        itemJson.put("XMBH", itemBaseinfo.getItemcode());
        itemJson.put("XMZYDM", "");
        itemJson.put("FRDW", "");
        itemJson.put("SBDWMC", itemBaseinfo.getItemlegaldept());
        // 对应ITEMLEGALCERTTYPE，需转换16转A05300
        String SBDWZZLX = "";
        if (StringUtil.isNotBlank(itemBaseinfo.getItemlegalcerttype())) {
            if (ZwfwConstant.CERT_TYPE_TYSHXYDM.equals(itemBaseinfo.getItemlegalcerttype())) {
                SBDWZZLX = "A05300";
            }
            // 14则根据法人性质LEGALPROPERTY字段转
            else if (ZwfwConstant.CERT_TYPE_ZZJGDMZ.equals(itemBaseinfo.getItemlegalcerttype())) {
                // LEGALPROPERTY=国家机关转A05202
                if ("国家机关".equals(itemBaseinfo.getLegalproperty())) {
                    SBDWZZLX = "A05202";
                }
                // LEGALPROPERTY=事业法人转A05203
                else if ("事业法人".equals(itemBaseinfo.getLegalproperty())) {
                    SBDWZZLX = "A05203";
                }
                // LEGALPROPERTY=社团法人转A05204
                else if ("社团法人".equals(itemBaseinfo.getLegalproperty())) {
                    SBDWZZLX = "A05204";
                }
                // 上述之外的转A05201
                else {
                    SBDWZZLX = "A05201";
                }
            }
        }
        itemJson.put("SBDWZZLX", SBDWZZLX);
        itemJson.put("SBDWZZHM", itemBaseinfo.getItemlegalcertnum());

        AuditRsCompanyBaseinfo companyBaseinfo = iAuditRsCompanyBaseinfo
                .getCompanyByOneField("creditcode", itemBaseinfo.getItemlegalcertnum()).getResult();
        if (companyBaseinfo == null) {
            companyBaseinfo = iAuditRsCompanyBaseinfo
                    .getCompanyByOneField("organcode", itemBaseinfo.getItemlegalcertnum()).getResult();
        }
        itemJson.put("SBDWID", companyBaseinfo != null ? companyBaseinfo.getRowguid() : "");
        itemJson.put("SBDWLXR", itemBaseinfo.getContractperson());
        itemJson.put("SBDWSJHM", itemBaseinfo.getContractphone());
        itemJson.put("SBDWDZYX", itemBaseinfo.getContractemail());
        itemJson.put("SBDWBZ", "");
        itemJson.put("XMLB", "");
        if (StringUtil.isNotBlank(itemBaseinfo.getParentid())) {
            AuditRsItemBaseinfo parentItemBaseinfo = iAuditRsItemBaseinfo
                    .getAuditRsItemBaseinfoByRowguid(itemBaseinfo.getParentid()).getResult();
            if (parentItemBaseinfo != null) {
                itemJson.put("SJXMMC", parentItemBaseinfo.getItemname());
                itemJson.put("SJXMDM", parentItemBaseinfo.getItemcode());
                itemJson.put("GXMJSDD", parentItemBaseinfo.getConstructionsite());
                itemJson.put("GXMJSDDXQ", parentItemBaseinfo.getConstructionsitedesc());
            }
        }
        else {
            itemJson.put("SJXMMC", itemBaseinfo.getItemname());
            itemJson.put("SJXMDM", itemBaseinfo.getItemcode());
            itemJson.put("GXMJSDD", itemBaseinfo.getConstructionsite());
            itemJson.put("GXMJSDDXQ", itemBaseinfo.getConstructionsitedesc());
        }
        String jsxz = "";
        if (StringUtil.isNotBlank(itemBaseinfo.getConstructionproperty())) {
            List<CodeItems> codeItems = iCodeItemsService.listCodeItemsByCodeName("建设性质");
            List<String> itemtexts = codeItems.stream().map(CodeItems::getItemValue).collect(Collectors.toList());
            if (itemtexts.contains(itemBaseinfo.getConstructionproperty())) {
                jsxz = String.valueOf(Integer.parseInt(itemBaseinfo.getConstructionproperty()) - 1);
            }
        }
        itemJson.put("JSXZ", jsxz);
        itemJson.put("XMXZ", "");

        String xmsx = "";
        if (StringUtil.isNotBlank(itemBaseinfo.getXmzjsx())) {
            if (ZwfwConstant.CONSTANT_STR_ONE.equals(itemBaseinfo.getXmzjsx())) {
                xmsx = "A00001";
            }
            else if (ZwfwConstant.CONSTANT_STR_TWO.equals(itemBaseinfo.getXmzjsx())) {
                xmsx = "A00002";
            }
            else if (ZwfwConstant.CONSTANT_STR_THREE.equals(itemBaseinfo.getXmzjsx())) {
                xmsx = "A00003";
            }
        }
        itemJson.put("XMSX", xmsx);
        itemJson.put("ZJLY", itemBaseinfo.getXmtzly());
        itemJson.put("ZDGC", "");
        itemJson.put("YDMJ", itemBaseinfo.getLandarea());
        itemJson.put("JZMJ", itemBaseinfo.getJzmj());
        itemJson.put("JSGMC", "");
        itemJson.put("JSGMK", "1.0");
        itemJson.put("JSGMQT", "");
        itemJson.put("ZTZGM", itemBaseinfo.getTotalinvest());
        itemJson.put("TJTZ", "");
        itemJson.put("QTTZ", "");
        itemJson.put("SBJJSTZ", "");
        itemJson.put("JKYH", "");
        itemJson.put("LTZESM", "");
        itemJson.put("JSDD", itemBaseinfo.getConstructionsite());
        itemJson.put("JSDDXQ", itemBaseinfo.getConstructionsitedesc());
        itemJson.put("JSXXDZ", "");
        String tdhqfs = "";
        if (StringUtil.isNotBlank(itemBaseinfo.getTdhqfs())) {
            if (ZwfwConstant.CONSTANT_INT_ONE == itemBaseinfo.getTdhqfs()) {
                tdhqfs = "A00003";
            }
            else if (ZwfwConstant.CONSTANT_INT_TWO == itemBaseinfo.getTdhqfs()) {
                tdhqfs = "A00002";
            }
            else if (3 == itemBaseinfo.getTdhqfs()) {
                tdhqfs = "A000011";
            }
            else if (4 == itemBaseinfo.getTdhqfs()) {
                tdhqfs = "A000012";
            }
        }
        itemJson.put("TDHQFS", tdhqfs);
        itemJson.put("XMJD", "");
        itemJson.put("XMJLJDDM", "");
        itemJson.put("DYQQJH", "");
        itemJson.put("XMZRDWMC", itemBaseinfo.getDepartname());
        itemJson.put("SFJCH", "");
        itemJson.put("FJ", "");
        itemJson.put("SHJRHMD", "");
        itemJson.put("XMLX", "");

        String zhxmlx = "";
        if (StringUtil.isNotBlank(itemBaseinfo.getItemtype())) {
            if (ZwfwConstant.CONSTANT_STR_ONE.equals(itemBaseinfo.getItemtype())) {
                zhxmlx = "1";
            }
            else if (ZwfwConstant.CONSTANT_STR_TWO.equals(itemBaseinfo.getItemtype())) {
                zhxmlx = "2";
            }
            else if (ZwfwConstant.CONSTANT_STR_THREE.equals(itemBaseinfo.getItemtype())) {
                zhxmlx = "4";
            }
            else if ("4".equals(itemBaseinfo.getItemtype())) {
                zhxmlx = "3";
            }
            else if ("5".equals(itemBaseinfo.getItemtype())) {
                zhxmlx = "5";
            }
            else {
                zhxmlx = "6";
            }
        }
        itemJson.put("ZHXMLX", zhxmlx);
        itemJson.put("NKGSJ",
                StringUtil.isNotBlank(itemBaseinfo.getItemstartdate())
                        ? EpointDateUtil.convertDate2String(itemBaseinfo.getItemstartdate(), "yyyy-MM")
                        : "");
        itemJson.put("NJCSJ",
                StringUtil.isNotBlank(itemBaseinfo.getItemfinishdate())
                        ? EpointDateUtil.convertDate2String(itemBaseinfo.getItemfinishdate(), "yyyy-MM")
                        : "");
        itemJson.put("SSHY", itemBaseinfo.getBelongtindustry());
        itemJson.put("GBHY", itemBaseinfo.getGbhy());
        itemJson.put("JSGMJNR", itemBaseinfo.getConstructionscaleanddesc());
        itemJson.put("SBRQ", StringUtil.isNotBlank(itemBaseinfo.getOperatedate())
                ? EpointDateUtil.convertDate2String(itemBaseinfo.getOperatedate(), EpointDateUtil.DATE_TIME_FORMAT)
                : "");
        itemJson.put("CSYJ", "");
        itemJson.put("XMZT", "");
        itemJson.put("DJSJ", "");
        itemJson.put("KGSJ", "");
        itemJson.put("JGSJ", "");
        SqlConditionUtil sql = new SqlConditionUtil();
        sql.eq("itemguid", itemBaseinfo.getRowguid());
        sql.eq("CORPTYPE", ZwfwConstant.CONSTANT_STR_THREE);
        List<ParticipantsInfo> participantsInfos = iParticipantsInfoService
                .getParticipantsInfoListByCondition(sql.getMap());
        if (ValidateUtil.isNotBlankCollection(participantsInfos)) {
            itemJson.put("SGDWMC", participantsInfos.get(0).getCorpname());
            itemJson.put("SGDWZZHM", participantsInfos.get(0).getCorpcode());
        }
        else {
            itemJson.put("SGDWMC", "");
            itemJson.put("SGDWZZHM", "");
        }
        itemJson.put("SGDWZZLX", "A05300");

        itemJson.put("SFWSTZ", "");
        itemJson.put("TZFS", "");
        itemJson.put("SFSJAQ", "");
        itemJson.put("AQSCJDWH", "");
        itemJson.put("ZTEMY", "");
        itemJson.put("ZTEHL", "");
        itemJson.put("XMZBJ", itemBaseinfo.getItemcapital());
        itemJson.put("XMZBJMY", "");
        itemJson.put("XMZBJHL", "");
        itemJson.put("NJKSBSLJJE", "");
        itemJson.put("ZFTZE", "");
        itemJson.put("ZBJCZQK", "");
        itemJson.put("IS_LAND_DESIGN_PLAN", itemBaseinfo.getTdsfdsjfa());
        itemJson.put("IS_COMPLETED_REGIONAL", itemBaseinfo.getSfwcqypg());
        itemJson.put("JSDDXQMC", "");
        itemJson.put("ZXMJ", "");
        itemJson.put("XMMS", "");
        itemJson.put("CZXX", "");
        itemJson.put("CODE", "1");
        itemJson.put("MSG", "处理成功");
        itemArray.add(itemJson);

        return itemArray;
    }

    private String jsonToXml(JSONArray itemArray) throws JsonMappingException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Document rtnDoc = DocumentHelper.createDocument();
        Element root = rtnDoc.addElement("item");
        Element records = root.addElement("RECORDS");
        for (int i = 0; i < itemArray.size(); i++) {
            Element item = records.addElement("item");
            JsonNode node = mapper.readTree(itemArray.getJSONObject(i).toJSONString());
            Iterator<String> fieldnames = node.fieldNames();
            while (fieldnames.hasNext()) {
                String fieldName = fieldnames.next();
                String value = node.get(fieldName).asText();
                Element field = item.addElement(fieldName);
                field.setText(value);
            }
        }
        Element totalrow = root.addElement("TOTALROW");
        totalrow.setText(String.valueOf(itemArray.size()));
        Element streamingdatas = root.addElement("STREAMINGDATAS");
        streamingdatas.setText("");
        Element msg = root.addElement("MSG");
        msg.setText("");
        Element rtnPARAMS = root.addElement("PARAMS");
        rtnPARAMS.setText("");

        return rtnDoc.asXML();
    }

    private JSONObject xmlToJSON(List<Element> elements) {
        JSONObject jsonxml = new JSONObject();
        for (Element element : elements) {
            String name = element.getName();
            String value = element.getText();
            List<Element> subelemetns = element.elements();
            if (subelemetns != null && !subelemetns.isEmpty()) {
                JSONObject subjsonxml = xmlToJSON(subelemetns);
                jsonxml.put(name, subjsonxml);
            }
            else {
                jsonxml.put(name, value);
            }
        }
        return jsonxml;
    }

}
