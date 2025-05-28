package com.epoint.znsb.jnzwfw.selfservicemachine;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.aspose.words.FontSettings;
import com.aspose.words.License;
import com.epoint.basic.auditorga.auditcenter.domain.AuditOrgaServiceCenter;
import com.epoint.basic.auditorga.auditcenter.inter.IAuditOrgaServiceCenter;
import com.epoint.basic.auditqueue.auditznsbequipment.domain.AuditZnsbEquipment;
import com.epoint.basic.auditqueue.auditznsbequipment.inter.IAuditZnsbEquipment;
import com.epoint.basic.auditqueue.auditznsbselfmachinemodule.domain.AuditZnsbSelfmachinemodule;
import com.epoint.basic.auditqueue.auditznsbselfmachinemodule.inter.IAuditZnsbSelfmachinemoduleService;
import com.epoint.common.util.JsonUtils;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.znsb.util.QueueCommonUtil;
import com.epoint.common.znsb.util.QueueConstant;
import com.epoint.composite.auditorga.handleconfig.inter.IHandleConfig;
import com.epoint.core.utils.classpath.ClassPathUtil;
import com.epoint.core.utils.code.DESEncrypt;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import com.epoint.frame.service.attach.entity.FrameAttachStorage;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.frame.service.metadata.code.entity.CodeItems;
import com.epoint.znsb.auditznsbytjextend.api.IAuditZnsbYtjextendService;
import com.epoint.znsb.auditznsbytjextend.api.entity.AuditZnsbYtjextend;
import com.epoint.znsb.auditznsbytjlabel.api.IAuditZnsbYtjlabelService;
import com.epoint.znsb.auditznsbytjlabel.api.entity.AuditZnsbYtjlabel;
import com.epoint.znsb.jnzwfw.module.AuditZnsbModuleServiceImpl;
import com.lowagie.text.Image;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfWriter;

@RestController
@RequestMapping("/jiningselfservicecommon")
public class JiNingCommonRestController
{

    @Autowired
    private IAuditZnsbEquipment equipmentservice;

    @Autowired
    private IAuditOrgaServiceCenter servicecenterservice;

    @Autowired
    private IAuditZnsbYtjextendService ytjextendService;

    @Autowired
    private IAuditZnsbYtjlabelService ytjlabelService;

    @Autowired
    private IAuditZnsbSelfmachinemoduleService moduleService;
    @Autowired
    private AuditZnsbModuleServiceImpl jnmoduleservice;
    @Autowired
    private IAttachService attachService;
    @Autowired
    private ICodeItemsService codeitemsservice;

    @Autowired
    private IHandleConfig handleConfigservice;

    @Autowired
    private IAttachService attachservice;

    public JiNingCommonRestController() throws FileNotFoundException {
    }

    /**
     * /**
     * 获取每个中心第一台设备
     *
     * @return
     * @params params
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/getCenterMachine", method = RequestMethod.POST)
    public String getCenterMachine(@RequestBody String params) {
        try {

            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");
            JSONObject dataJson = new JSONObject();
            String currentpage = obj.getString("currentpage");
            String pagesize = obj.getString("pagesize");

            SqlConditionUtil sql = new SqlConditionUtil();
            sql.eq("machinetype", QueueConstant.EQUIPMENT_TYPE_YTJ);
            sql.eq("status", QueueConstant.Common_yes_String);
            sql.isNotBlank("centerguid");
            sql.setSelectFields("distinct(centerguid)");
            List<AuditZnsbEquipment> equipmentlist = equipmentservice.getEquipmentList(sql.getMap()).getResult();
            String centerguids = "";
            for (AuditZnsbEquipment equipment : equipmentlist) {
                centerguids += equipment.getCenterguid() + "','";
            }
            centerguids = "'" + centerguids + "'";
            sql.clear();
            sql.in("rowguid", centerguids);
            sql.setOrderDesc("ordernum");
            sql.setSelectFields("rowguid,centername");
            List<AuditOrgaServiceCenter> centerlist = servicecenterservice
                    .getAuditOrgaServiceCenterByCondition(sql.getMap()).getResult();
            for (AuditOrgaServiceCenter center : centerlist) {
                sql.clear();
                sql.eq("centerguid", center.getRowguid());
                sql.eq("machinetype", QueueConstant.EQUIPMENT_TYPE_YTJ);
                sql.eq("status", QueueConstant.Common_yes_String);
                List<AuditZnsbEquipment> equipmentlistfinal = equipmentservice.getEquipmentList(sql.getMap())
                        .getResult();
                center.put("macaddress", equipmentlistfinal.get(0).getMacaddress());
            }

            List<AuditOrgaServiceCenter> centerpagedata = Page(centerlist, Integer.parseInt(pagesize),
                    Integer.parseInt(currentpage));

            dataJson.put("list", centerpagedata);
            dataJson.put("totalcount", centerlist.size());
            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (JSONException e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }
    }

    public static List<AuditOrgaServiceCenter> Page(List<AuditOrgaServiceCenter> dataList, int pageSize,
            int currentPage) {
        List<AuditOrgaServiceCenter> currentPageList = new ArrayList<>();
        if (dataList != null && dataList.size() > 0) {
            int currIdx = (currentPage >= 1 ? currentPage * pageSize : 0);
            for (int i = 0; i < pageSize && i < dataList.size() - currIdx; i++) {
                AuditOrgaServiceCenter data = dataList.get(currIdx + i);
                currentPageList.add(data);
            }
        }
        return currentPageList;
    }

    /**
     * 获取每个中心第一台设备
     *
     * @return
     * @params params
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/getnewmodulelistbynav", method = RequestMethod.POST)
    public String getnewModuleListByNav(@RequestBody String params, HttpServletRequest request) {
        try {
            String readattachString = QueueCommonUtil.getUrlPath(request.getRequestURL().toString())
                    + "/rest/auditattach/readAttach?attachguid=";
            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");
            JSONObject dataJson = new JSONObject();
            String centerguid = obj.getString("centerguid");
            String macaddress = obj.getString("macaddress");
            String moduleconfigtype = obj.getString("moduleconfigtype");
            List<JSONObject> typeList = new ArrayList<>();
            // 获取当前中心下的所有模块
            List<AuditZnsbSelfmachinemodule> alllist = null;
            int alllistCount = 0;
            String searchtype = "0";
            if (StringUtil.isNotBlank(moduleconfigtype)) {
                alllistCount = jnmoduleservice.findMacListCountByConfigType(macaddress, centerguid, moduleconfigtype);
                searchtype = "0";
            }
            else {
                alllistCount = jnmoduleservice.findMacListCount(macaddress, centerguid);
                searchtype = "1";
            }
            if (alllistCount == 0) {
                alllistCount = jnmoduleservice.findCenterListCount(centerguid);
                searchtype = "2";
            }
            if (alllistCount > 0) {
                List<AuditZnsbYtjlabel> labelList = ytjlabelService
                        .findList("select * from audit_znsb_ytjlabel where 1=1 order by ordernum desc");
                for (AuditZnsbYtjlabel label : labelList) {
                    List<JSONObject> modulelist = new ArrayList<>();
                    switch (searchtype) {
                        case "0":
                            alllist = jnmoduleservice.findMacListByConfigTypeAndLabel(macaddress, centerguid,
                                    moduleconfigtype, label.getRowguid());
                            break;
                        case "1":
                            alllist = jnmoduleservice.findMacListCountAndLabel(macaddress, centerguid,
                                    label.getRowguid());
                            break;
                        case "2":
                            alllist = jnmoduleservice.findCenterListCountAndLabel(centerguid, label.getRowguid());
                            break;
                    }
                    for (AuditZnsbSelfmachinemodule module : alllist) {
                        JSONObject moduleJson = new JSONObject();
                        moduleJson.put("modulename", module.getModulename());
                        moduleJson.put("isnew", module.getStr("is_new"));
                        moduleJson.put("ishot", module.getStr("is_hot"));
                        moduleJson.put("htmlurl", module.getHtmlurl());
                        moduleJson.put("isneedlogin", module.getIsneedlogin());
                        if (StringUtil.isNotBlank(module.getStr("ordernum1"))) {
                            moduleJson.put("ordernum", module.getStr("ordernum1"));
                        }
                        else {
                            moduleJson.put("ordernum", 0);
                        }
                        moduleJson.put("commontype", module.getStr("commontype"));

                        String moduleattachguid = "";
                        if (StringUtil.isNotBlank(module.getStr("pngattachguid"))) {
                            List<FrameAttachInfo> modulepngattachlist = attachService
                                    .getAttachInfoListByGuid(module.getStr("pngattachguid"));
                            if (modulepngattachlist != null && !modulepngattachlist.isEmpty()) {
                                moduleattachguid = modulepngattachlist.get(0).getAttachGuid();
                                moduleJson.put("moduleimg", readattachString + moduleattachguid);
                            }
                        }
                        modulelist.add(moduleJson);

                    }
                    if (!modulelist.isEmpty()) {
                        JSONObject typeJson = new JSONObject();
                        typeJson.put("typename", label.getLablename());
                        typeJson.put("color", label.getLabelcolor());
                        typeJson.put("typerowguid", label.getRowguid());
                        String pngattachguid = "";
                        if (StringUtil.isNotBlank(label.getPngattachguid())) {
                            List<FrameAttachInfo> pngattachlist = attachService
                                    .getAttachInfoListByGuid(label.getPngattachguid());
                            if (pngattachlist != null && !pngattachlist.isEmpty()) {
                                pngattachguid = pngattachlist.get(0).getAttachGuid();
                                typeJson.put("typeimg", readattachString + pngattachguid);
                            }
                        }

                        typeJson.put("modulelist", modulelist);
                        typeList.add(typeJson);
                    }
                }
            }
            dataJson.put("typelist", typeList);

            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (JSONException e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e, "");
        }
    }

    /**
     * 获取每个中心第一台设备
     *
     * @return
     * @params params
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/getmodulelistbynav", method = RequestMethod.POST)
    public String getModuleListByNav(@RequestBody String params, HttpServletRequest request) {
        try {
            String readattachString = QueueCommonUtil.getUrlPath(request.getRequestURL().toString())
                    + "/rest/auditattach/readAttach?attachguid=";
            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");
            JSONObject dataJson = new JSONObject();
            String centerguid = obj.getString("centerguid");
            String macaddress = obj.getString("macaddress");
            String moduleconfigtype = obj.getString("moduleconfigtype");
            List<JSONObject> typeList = new ArrayList<>();
            // 获取当前中心下的所有模块
            List<AuditZnsbSelfmachinemodule> alllist;
            if (StringUtil.isNotBlank(moduleconfigtype)) {
                alllist = moduleService.getModuleListByMacAndType(macaddress, centerguid, moduleconfigtype).getResult();
            }
            else {
                alllist = moduleService.getModuleListByMacaddress(macaddress, centerguid).getResult();
            }
            if (alllist.size() == 0) {
                alllist = moduleService.getModuleListByCenterguid(centerguid).getResult();
            }
            if (alllist != null && !alllist.isEmpty()) {
                List<AuditZnsbYtjlabel> labelList = ytjlabelService
                        .findList("select * from audit_znsb_ytjlabel where 1=1 order by ordernum desc");
                for (int i = 0; i < labelList.size(); i++) {
                    AuditZnsbYtjlabel ytjlabel = labelList.get(i);
                    List<JSONObject> modulelist = new ArrayList<>();
                    for (int j = 0; j < alllist.size(); j++) {
                        AuditZnsbSelfmachinemodule module = alllist.get(j);
                        List<AuditZnsbYtjextend> ytjextends = ytjextendService.findList("moduleguid",
                                module.getRowguid());
                        if (ytjextends.size() > 0) {
                            AuditZnsbYtjextend ytjextend = ytjextends.get(0);
                            if (ytjlabel.getRowguid().equals(ytjextend.getLableguid())) {
                                JSONObject moduleJson = new JSONObject();
                                moduleJson.put("modulename", module.getModulename());
                                moduleJson.put("isnew", ytjextend.getIs_new());
                                moduleJson.put("ishot", ytjextend.getIs_hot());
                                moduleJson.put("htmlurl", module.getHtmlurl());
                                moduleJson.put("isneedlogin", module.getIsneedlogin());
                                if (StringUtil.isNotBlank(ytjextend.getOrdernum())) {
                                    moduleJson.put("ordernum", ytjextend.getOrdernum());
                                }
                                else {
                                    moduleJson.put("ordernum", 0);
                                }
                                moduleJson.put("commontype", module.getStr("commontype"));

                                String moduleattachguid = "";
                                if (StringUtil.isNotBlank(ytjextend.getPngattachguid())) {
                                    List<FrameAttachInfo> modulepngattachlist = attachService
                                            .getAttachInfoListByGuid(ytjextend.getPngattachguid());
                                    if (modulepngattachlist != null && !modulepngattachlist.isEmpty()) {
                                        moduleattachguid = modulepngattachlist.get(0).getAttachGuid();
                                        moduleJson.put("moduleimg", readattachString + moduleattachguid);
                                    }
                                }

                                if (!"1".equals(ytjextend.getIs_show())) {
                                    modulelist.add(moduleJson);
                                }
                            }
                        }
                    }

                    if (modulelist.size() > 4) {
                        modulelist = modulelist.subList(0, 4);
                    }
                    // 排序
                    modulelist.sort((x, y) -> Integer.compare(y.getInteger("ordernum"), x.getInteger("ordernum")));

                    if (modulelist.size() > 0) {
                        JSONObject typeJson = new JSONObject();
                        typeJson.put("typename", ytjlabel.getLablename());
                        typeJson.put("color", ytjlabel.getLabelcolor());
                        typeJson.put("typerowguid", ytjlabel.getRowguid());
                        String pngattachguid = "";
                        if (StringUtil.isNotBlank(ytjlabel.getPngattachguid())) {
                            List<FrameAttachInfo> pngattachlist = attachService
                                    .getAttachInfoListByGuid(ytjlabel.getPngattachguid());
                            if (pngattachlist != null && !pngattachlist.isEmpty()) {
                                pngattachguid = pngattachlist.get(0).getAttachGuid();
                                typeJson.put("typeimg", readattachString + pngattachguid);
                            }
                        }

                        typeJson.put("modulelist", modulelist);
                        typeList.add(typeJson);
                    }
                }
            }
            dataJson.put("typelist", typeList);

            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (JSONException e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }
    }

    @RequestMapping(value = "/gethotmodulelistbynav", method = RequestMethod.POST)
    public String getHotModuleListByNav(@RequestBody String params, HttpServletRequest request) {
        try {
            String readattachString = QueueCommonUtil.getUrlPath(request.getRequestURL().toString())
                    + "/rest/auditattach/readAttach?attachguid=";
            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");
            JSONObject dataJson = new JSONObject();
            String centerguid = obj.getString("centerguid");
            String macaddress = obj.getString("macaddress");
            String moduleconfigtype = obj.getString("moduleconfigtype");
            List<JSONObject> modulelist = new ArrayList<>();
            // 获取当前中心下的所有模块
            List<AuditZnsbSelfmachinemodule> alllist;

            List<CodeItems> codeItemsList = codeitemsservice.listCodeItemsByCodeName("一体机标签底色");
            if (StringUtil.isNotBlank(moduleconfigtype)) {
                alllist = moduleService.getModuleListByMacAndType(macaddress, centerguid, moduleconfigtype).getResult();
            }
            else {
                alllist = moduleService.getModuleListByMacaddress(macaddress, centerguid).getResult();
            }
            if (alllist.size() == 0) {
                alllist = moduleService.getModuleListByCenterguid(centerguid).getResult();
            }
            if (alllist != null && !alllist.isEmpty()) {
                for (int j = 0; j < alllist.size(); j++) {
                    AuditZnsbSelfmachinemodule module = alllist.get(j);
                    List<AuditZnsbYtjextend> ytjextends = ytjextendService.findList("moduleguid", module.getRowguid());
                    if (ytjextends.size() > 0) {
                        AuditZnsbYtjextend ytjextend = ytjextends.get(0);
                        if ("1".equals(ytjextend.getIs_show())) {
                            JSONObject moduleJson = new JSONObject();
                            moduleJson.put("modulename", module.getModulename());
                            moduleJson.put("isnew", ytjextend.getIs_new());
                            moduleJson.put("ishot", ytjextend.getIs_hot());
                            moduleJson.put("htmlurl", module.getHtmlurl());
                            moduleJson.put("isneedlogin", module.getIsneedlogin());
                            moduleJson.put("commontype", module.getStr("commontype"));
                            AuditZnsbYtjlabel ytjlabel = ytjlabelService.find(ytjextend.getLableguid());
                            if (StringUtil.isNotBlank(ytjlabel)) {
                                moduleJson.put("typename", ytjlabel.getLablename());
                                moduleJson.put("color", ytjlabel.getLabelcolor());
                                String colortext = "blue";
                                for (CodeItems code : codeItemsList) {
                                    if (ytjlabel.getLabelcolor().equals(code.getItemValue())) {
                                        colortext = code.getDmAbr1();
                                    }
                                }
                                moduleJson.put("colortext", colortext);
                                String moduleattachguid = "";
                                if (StringUtil.isNotBlank(ytjextend.getPngattachguid())) {
                                    List<FrameAttachInfo> modulepngattachlist = attachService
                                            .getAttachInfoListByGuid(ytjextend.getPngattachguid());
                                    if (modulepngattachlist != null && !modulepngattachlist.isEmpty()) {
                                        moduleattachguid = modulepngattachlist.get(0).getAttachGuid();
                                        moduleJson.put("moduleimg", readattachString + moduleattachguid);
                                    }
                                }

                                modulelist.add(moduleJson);
                            }

                        }
                    }
                }
            }
            if (modulelist.size() > 5) {
                modulelist = modulelist.subList(0, 5);
            }

            dataJson.put("modulelist", modulelist);

            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (JSONException e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }
    }

    @RequestMapping(value = "/getmodulelistbytype", method = RequestMethod.POST)
    public String getmodulelistbytype(@RequestBody String params, HttpServletRequest request) {
        try {
            String readattachString = QueueCommonUtil.getUrlPath(request.getRequestURL().toString())
                    + "/rest/auditattach/readAttach?attachguid=";
            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");
            JSONObject dataJson = new JSONObject();
            String centerguid = obj.getString("centerguid");
            String macaddress = obj.getString("macaddress");
            String type = obj.getString("type");
            String moduleconfigtype = obj.getString("moduleconfigtype");
            String inputkey = obj.getString("inputkey");
            List<JSONObject> modulelist = new ArrayList<>();
            // 获取当前中心下的所有模块
            List<AuditZnsbSelfmachinemodule> alllist;
            // 查询方式得换
            if (StringUtil.isNotBlank(inputkey)) {
                alllist = moduleService.getModuleListByName(centerguid, inputkey).getResult();
            }
            else {
                if (StringUtil.isNotBlank(moduleconfigtype)) {
                    alllist = moduleService.getModuleListByMacAndType(macaddress, centerguid, moduleconfigtype)
                            .getResult();
                }
                else {
                    alllist = moduleService.getModuleListByMacaddress(macaddress, centerguid).getResult();
                }
                if (alllist.size() == 0) {
                    alllist = moduleService.getModuleListByCenterguid(centerguid).getResult();
                }
            }
            if (alllist != null && !alllist.isEmpty()) {
                for (int j = 0; j < alllist.size(); j++) {
                    AuditZnsbSelfmachinemodule module = alllist.get(j);
                    List<AuditZnsbYtjextend> ytjextends = ytjextendService.findList("moduleguid", module.getRowguid());
                    if (ytjextends.size() > 0) {
                        AuditZnsbYtjextend ytjextend = ytjextends.get(0);
                        if (StringUtil.isNotBlank(inputkey)
                                || (StringUtil.isNotBlank(type) && type.equals(ytjextend.getLableguid()))) {
                            JSONObject moduleJson = new JSONObject();
                            moduleJson.put("modulename", module.getModulename());
                            moduleJson.put("htmlurl", module.getHtmlurl());
                            moduleJson.put("isneedlogin", module.getIsneedlogin());
                            moduleJson.put("commontype", module.getStr("commontype"));
                            AuditZnsbYtjlabel ytjlabel = ytjlabelService.find(ytjextend.getLableguid());
                            String moduleattachguid = "";
                            if (StringUtil.isNotBlank(ytjextend.getPngattachguid())) {
                                List<FrameAttachInfo> modulepngattachlist = attachService
                                        .getAttachInfoListByGuid(ytjextend.getPngattachguid());
                                if (modulepngattachlist != null && !modulepngattachlist.isEmpty()) {
                                    moduleattachguid = modulepngattachlist.get(0).getAttachGuid();
                                    moduleJson.put("picturepath", readattachString + moduleattachguid);
                                }
                            }

                            modulelist.add(moduleJson);
                        }
                    }
                }
            }
            dataJson.put("modulelist", modulelist);

            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (JSONException e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }
    }

    @RequestMapping(value = "/changepdf", method = RequestMethod.POST)
    public String changepdf(@RequestBody String params, HttpServletRequest request) {
        try {
            JSONObject json = JSON.parseObject(params);
            JSONObject obj = (JSONObject) json.get("params");
            String base64 = obj.getString("base64");
            JSONObject dataJson = new JSONObject();
            String attachguid = "";
            // 如果为空，则调用aspose.word进行转换
            String licenseName = ClassPathUtil.getClassesPath() + "license.xml";
            FontSettings.setFontsFolder(ClassPathUtil.getClassesPath() + "font" + File.separator, true);
            new License().setLicense(licenseName);
            if (StringUtil.isBlank(handleConfigservice.getFrameConfig("AS_SAMPLE_URL", "").getResult())
                    || StringUtil.isBlank(handleConfigservice.getFrameConfig("AS_ZWFW_WEB", "").getResult())) {
                /*
                 * base64 = PDFUtil.addWatermarkToBase64(base64,
                 * "提取部门：济宁市行政审批服务局&&提取时间："
                 * + EpointDateUtil.convertDate2String(new Date(),
                 * "yyyy年MM月dd日HH时mm分ss秒"));
                 */
                byte[] bytes = Base64.decode(base64);
                ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
                long size = inputStream.available();
                // 附件信息
                String zbname = "社保" + new Date().getTime() + ".pdf";
                FrameAttachInfo frameAttachInfo = new FrameAttachInfo();
                frameAttachInfo.setCliengGuid(UUID.randomUUID().toString());
                frameAttachInfo.setAttachFileName(zbname);
                frameAttachInfo.setCliengTag("社保.pdf");
                frameAttachInfo.setUploadUserGuid("");
                frameAttachInfo.setUploadUserDisplayName("");
                frameAttachInfo.setUploadDateTime(new Date());
                frameAttachInfo.setContentType("application/msword");
                frameAttachInfo.setAttachLength(size);
                attachguid = attachservice.addAttach(frameAttachInfo, inputStream).getAttachGuid();
                inputStream.close();
            }
            String rootpath = QueueCommonUtil.getUrlPath(request.getRequestURL().toString())
                    + "/rest/auditattach/readAttach?attachguid=" + attachguid;
            // 加密key
            String deskey = ConfigUtil.getConfigValue("uploadpreview.encrypt.key");
            // 加密向量
            String desiv = ConfigUtil.getConfigValue("uploadpreview.encrypt.iv");
            if (StringUtil.isNotBlank(attachguid) && StringUtil.isNotBlank(deskey)) {
                DESEncrypt des = new DESEncrypt(deskey, desiv);
                dataJson.put("signfurl", des.encode(rootpath).replaceAll("(\r\n|\n)", ""));
            }
            else {
                dataJson.put("signfurl", rootpath);
            }
            dataJson.put("attachguid", attachguid);
            dataJson.put("pdfurl", rootpath);
            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (Exception e) {
            // TODO: handle exception
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }

    }

    /**
     * png转pdf
     * 
     * @param params
     * @param request
     * @return
     */
    @RequestMapping(value = "/qianzhangpdf", method = RequestMethod.POST)
    public String qianzhangpdf(@RequestBody String params, HttpServletRequest request) {
        try {
            JSONObject json = JSON.parseObject(params);
            JSONObject obj = (JSONObject) json.get("params");
            String clientguid = obj.getString("clientguid");
            JSONObject dataJson = new JSONObject();

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            // 每次进方法 重新生成一个pdf
            String attachguid = UUID.randomUUID().toString();

            /*
             * String doctem = ClassPathUtil.getDeployWarPath() +
             * "jiningzwfw/individuation/overall/equipmentdisplay/selfservicemachine/pdf/img.pdf";
             * // 输入流
             * FileOutputStream fos = new FileOutputStream(doctem);
             */
            // 创建文档
            com.lowagie.text.Document doc = new com.lowagie.text.Document(null, 0, 0, 0, 0);
            // doc.open();
            // 写入PDF文档
            PdfWriter.getInstance(doc, outputStream);

            // 所有附件拿出，生成pdf
            List<FrameAttachInfo> attachInfoList = attachService.getAttachInfoListByGuid(clientguid);

            attachInfoList
                    .sort((x, y) -> Double.compare(x.getUploadDateTime().getTime(), y.getUploadDateTime().getTime()));
            // 读取图片流
            BufferedImage img = null;
            // 实例化图片
            Image image = null;

            if (attachInfoList != null && !attachInfoList.isEmpty()) {

                if (attachInfoList.size() == 1 && ".pdf".equals(attachInfoList.get(0).getContentType())) {

                    attachInfoList = attachService.getAttachInfoListByGuid(attachInfoList.get(0).getAttachGuid());
                }

                for (FrameAttachInfo attachInfo : attachInfoList) {
                    if (!".pdf".equals(attachInfo.getContentType())) {
                        FrameAttachStorage storage = attachService.getAttach(attachInfo.getAttachGuid());
                        // 读取图片流
                        img = ImageIO.read(storage.getContent());
                        ByteArrayOutputStream out = new ByteArrayOutputStream();
                        boolean flag = ImageIO.write(img, "png", out);
                        byte[] b = out.toByteArray();
                        // 根据图片大小设置文档大小
                        doc.setPageSize(new Rectangle(img.getWidth(), img.getHeight()));
                        image = image.getInstance(b);
                        // 添加图片到文档
                        doc.open();
                        doc.add(image);
                        FrameAttachStorage newstorage = attachService.getAttach(attachInfo.getAttachGuid());
                        attachInfo.setCliengGuid(attachguid);
                        attachservice.updateAttach(attachInfo, newstorage.getContent());
                    }

                }
            }
            if (doc.isOpen()) {
                doc.close();
            }

            // 删除原先的签章文件
            attachservice.deleteAttachByGuid(clientguid);

            // 将合成成功的pdf转化成附件上传
            ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
            long size = inputStream.available();
            // 附件信息
            String zbname = "签章" + new Date().getTime() + ".pdf";
            FrameAttachInfo frameAttachInfo = new FrameAttachInfo();
            frameAttachInfo.setAttachGuid(attachguid);
            frameAttachInfo.setCliengGuid(clientguid);
            frameAttachInfo.setAttachFileName(zbname);
            frameAttachInfo.setCliengTag("签章.pdf");
            frameAttachInfo.setUploadUserGuid("");
            frameAttachInfo.setUploadUserDisplayName("");
            frameAttachInfo.setUploadDateTime(new Date());
            frameAttachInfo.setContentType(".pdf");
            frameAttachInfo.setAttachLength(size);
            attachservice.addAttach(frameAttachInfo, inputStream);
            inputStream.close();

            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (Exception e) {
            // TODO: handle exception
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }

    }

    /**
     * 在签章完之后，重新补充图片
     * 
     * @param params
     * @param request
     * @return
     */
    @RequestMapping(value = "/changeimage", method = RequestMethod.POST)
    public String changeimage(@RequestBody String params, HttpServletRequest request) {
        try {
            JSONObject json = JSON.parseObject(params);
            JSONObject obj = (JSONObject) json.get("params");
            String clientguid = obj.getString("clientguid");
            JSONObject dataJson = new JSONObject();

            List<FrameAttachInfo> attachInfoList = attachService.getAttachInfoListByGuid(clientguid);

            if (attachInfoList != null && !attachInfoList.isEmpty()) {
                if (attachInfoList.size() == 1 && ".pdf".equals(attachInfoList.get(0).getContentType())) {
                    // 两个操作 重新签章
                    String attachguid = attachInfoList.get(0).getAttachGuid();
                    // 把里面的签章先删除
                    attachService.deleteAttachByGuid(clientguid);
                    // 查询出
                    List<FrameAttachInfo> newattachInfoList = attachService.getAttachInfoListByGuid(attachguid);
                    if (newattachInfoList != null && !newattachInfoList.isEmpty()) {
                        // 图片的clientguid 全部替换
                        for (FrameAttachInfo newattach : newattachInfoList) {
                            newattach.setCliengGuid(clientguid);
                            FrameAttachStorage storage = attachService.getAttach(newattach.getAttachGuid());
                            attachService.updateAttach(newattach, storage.getContent());
                        }

                    }
                }

            }

            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (Exception e) {
            // TODO: handle exception
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }

    }

    /**
     * pdf预览参数加密
     * 
     * @param params
     * @param request
     * @return
     */

    @RequestMapping(value = "/desAttachguid", method = RequestMethod.POST)
    public String desAttachguid(@RequestBody String params, HttpServletRequest request) {
        try {
            JSONObject json = JSON.parseObject(params);
            JSONObject obj = (JSONObject) json.get("params");
            String url = obj.getString("url");
            JSONObject dataJson = new JSONObject();
            // 加密key
            String deskey = ConfigUtil.getConfigValue("uploadpreview.encrypt.key");
            // 加密向量
            String desiv = ConfigUtil.getConfigValue("uploadpreview.encrypt.iv");
            if (StringUtil.isNotBlank(deskey)) {
                DESEncrypt des = new DESEncrypt(deskey, desiv);
                dataJson.put("signfurl", des.encode(url).replaceAll("(\r\n|\n)", ""));
            }
            else {
                dataJson.put("signfurl", url);
            }
            dataJson.put("pdfurl", url);
            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (Exception e) {
            // TODO: handle exception
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }

    }

    /**
     * /**
     * 获取每个中心第一台设备
     *
     * @return
     * @params params
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/getCenterInfo", method = RequestMethod.POST)
    public String getCenterInfo(@RequestBody String params) {
        try {

            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");
            JSONObject dataJson = new JSONObject();
            String centerguid = obj.getString("centerguid");
            AuditOrgaServiceCenter ceneter = servicecenterservice.findAuditServiceCenterByGuid(centerguid).getResult();
            if (StringUtil.isNotBlank(ceneter)) {
                dataJson.put("centername", ceneter.getCentername());
                String phone = handleConfigservice.getFrameConfig("AS_CENTER_PHONE", centerguid).getResult();
                // 获取中心的经度纬度
                String longitude = handleConfigservice.getFrameConfig("AS_CENTER_LONGITUDE", centerguid).getResult();
                String latitude = handleConfigservice.getFrameConfig("AS_CENTER_LATITUDE", centerguid).getResult();
                dataJson.put("longitude", longitude);
                dataJson.put("latitude", latitude);
                dataJson.put("phone", phone);
            }

            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (JSONException e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }
    }

}
