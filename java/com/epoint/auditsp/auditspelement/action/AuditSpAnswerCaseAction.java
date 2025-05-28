package com.epoint.auditsp.auditspelement.action;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import com.epoint.auditspoptiontownship.api.IAuditSpOptiontownshipService;
import com.epoint.auditspoptiontownship.api.entity.AuditSpOptiontownship;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.auditorga.auditarea.inter.IAuditOrgaArea;
import com.epoint.basic.auditsp.auditspbasetaskr.inter.IAuditSpBasetaskR;
import com.epoint.basic.auditsp.auditspimaterial.inter.IAuditSpIMaterial;
import com.epoint.basic.auditsp.auditspitask.inter.IAuditSpITask;
import com.epoint.basic.auditsp.auditspselectedoption.domain.AuditSpSelectedOption;
import com.epoint.basic.auditsp.auditspselectedoption.inter.IAuditSpSelectedOptionService;
import com.epoint.basic.auditsp.auditspshareoption.api.IAuditSpShareoption;
import com.epoint.basic.auditsp.auditspshareoption.domain.AuditSpShareoption;
import com.epoint.basic.auditsp.element.domain.AuditSpElement;
import com.epoint.basic.auditsp.element.inter.IAuditSpElementService;
import com.epoint.basic.auditsp.option.domain.AuditSpOption;
import com.epoint.basic.auditsp.option.inter.IAuditSpOptionService;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.audittask.element.domain.AuditTaskElement;
import com.epoint.basic.audittask.element.inter.IAuditTaskElementService;
import com.epoint.basic.audittask.material.domain.AuditTaskCase;
import com.epoint.basic.audittask.material.domain.AuditTaskMaterial;
import com.epoint.basic.audittask.material.inter.IAuditTaskCase;
import com.epoint.basic.audittask.material.inter.IAuditTaskMaterial;
import com.epoint.basic.audittask.option.domain.AuditTaskOption;
import com.epoint.basic.audittask.option.inter.IAuditTaskOptionService;
import com.epoint.basic.authentication.UserSession;
import com.epoint.basic.controller.BaseController;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ValidateUtil;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.composite.auditsp.handlespimaterial.inter.IHandleSPIMaterial;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.security.crypto.MDUtils;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;

/**
 * 事项的情形list页面对应的后台
 * 
 * @author Administrator
 * @version [版本号, 2016-10-14 16:13:58]
 */
@RestController("auditspanswercaseaction")
@Scope("request")
public class AuditSpAnswerCaseAction extends BaseController
{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * 事项下放相关信息
     */
    @Autowired
    private IAuditSpOptionService iAuditSpOptionService;

    /**
     * 事项下放相关信息
     */
    @Autowired
    private IAuditSpElementService iAuditSpElementService;

    @Autowired
    private IAuditSpBasetaskR iauditspbasetaskr;

    @Autowired
    private ICodeItemsService icodeitemsservice;

    @Autowired
    private IAuditSpSelectedOptionService iAuditSpSelectedOptionService;

    @Autowired
    private IAuditSpIMaterial iauditspimaterial;

    @Autowired
    private IAuditSpITask auditSpITaskService;

    @Autowired
    private IAuditTaskMaterial iaudittaskmaterial;

    @Autowired
    private IAuditTask iaudittask;

    @Autowired
    private IAuditTaskCase iaudittaskcase;

    @Autowired
    private IAuditOrgaArea iauditorgaarea;

    @Autowired
    private IHandleSPIMaterial ihandlespimaterial;

    @Autowired
    private IAuditTaskElementService iaudittaskelementservice;

    @Autowired
    private IAuditTaskOptionService iaudittaskoptionservice;

    @Autowired
    private IAuditSpShareoption iauditspshareoption;

    @Autowired
    IAuditSpOptiontownshipService iauditspoptiontownshipservice;

    @Override
    public void pageLoad() {

    }

    public void showData() {
        // 如果存在阶段，则过滤
        String phaseid = getRequestParameter("phaseid");
        List<String> materialidlist = null;
        if (StringUtil.isNotBlank(phaseid)) {
            List<String> taskids = iauditspbasetaskr.getTaskidlistbyPhaseid(phaseid).getResult();
            materialidlist = iaudittaskmaterial.getMaterialidlistBytaskids(taskids).getResult();
        }
        String areacode = ZwfwUserSession.getInstance().getAreaCode();
        if (StringUtil.isNotBlank(areacode)) {
            JSONObject rtnjson = new JSONObject();
            Map<String, Object> custommap = new HashMap<>();
            Map<String, Object> statusmap = new HashMap<>();
            List<Map<String, Object>> elementlist = new ArrayList<>();
            SqlConditionUtil sqlc = new SqlConditionUtil();
            sqlc.eq("areacode", ZwfwUserSession.getInstance().getAreaCode());
            sqlc.isBlank("preoptionguid");
            sqlc.isBlank("businessguid");
            sqlc.setOrderDesc("ordernum");
            sqlc.setOrderAsc("operatedate");
            List<AuditSpElement> elelist = iAuditSpElementService.findListByCondition(sqlc.getMap()).getResult();
            if (!elelist.isEmpty()) {
                for (int i = 0; i < elelist.size(); i++) {
                    AuditSpElement auditSpElement = elelist.get(i);
                    if (!(StringUtil.isBlank(auditSpElement.getElementname())
                            && StringUtil.isBlank(auditSpElement.getElementquestion()))) {
                        Map<String, Object> elemap = new HashMap<>();
                        elemap.put("showSortUp", i != 0);
                        elemap.put("showSortDown", i != elelist.size());
                        elemap.put("id", auditSpElement.getRowguid());
                        elemap.put("multiple", ZwfwConstant.CONSTANT_STR_ONE.equals(auditSpElement.getMultiselect()));
                        elemap.put("type", auditSpElement.getElementname());
                        elemap.put("question", auditSpElement.getElementquestion());
                        List<AuditSpOption> optlist = iAuditSpOptionService
                                .findListByElementId(auditSpElement.getRowguid()).getResult();
                        List<Map<String, Object>> optionlist = new ArrayList<>();
                        for (int j = 0; j < optlist.size(); j++) {
                            AuditSpOption auditSpOption = optlist.get(j);
                            if (StringUtil.isNotBlank(auditSpOption.getOptionname())) {
                                Map<String, Object> optmap = new HashMap<>();
                                optmap.put("checkbox",
                                        ZwfwConstant.CONSTANT_STR_ONE.equals(auditSpElement.getMultiselect()));
                                optmap.put("showDelete", false);
                                optmap.put("id", auditSpOption.getRowguid());
                                optmap.put("content", auditSpOption.getOptionname());
                                optmap.put("equalvalue", auditSpOption.getEqualvalue());
                                String materialids = auditSpOption.getMaterialids();
                                List<Map<String, Object>> materiallist = new ArrayList<>();
                                if (StringUtil.isNotBlank(materialids)) {
                                    String[] material = materialids.split(";");
                                    for (int k = 0; k < material.length; k++) {
                                        if (materialidlist != null) {
                                            if (!materialidlist.contains(material[k])) {
                                                continue;
                                            }
                                        }
                                        Map<String, Object> matermap = new HashMap<>();
                                        String materialname = iaudittaskmaterial
                                                .getMaterialnameByMaterialid(material[k]).getResult();
                                        if (StringUtil.isNotBlank(materialname)) {
                                            matermap.put("id", material[k]);
                                            matermap.put("name", materialname);
                                            matermap.put("url", "");
                                            materiallist.add(matermap);
                                        }
                                    }
                                }
                                optmap.put("relates", materiallist);
                                List<Map<String, Object>> childlisttemp = new ArrayList<>();
                                List<Map<String, Object>> childlist = new ArrayList<>();
                                childlist = getAllChildElement(auditSpOption.getRowguid(), childlisttemp,
                                        materialidlist);
                                optmap.put("collection", childlist);
                                optionlist.add(optmap);

                            }
                        }
                        elemap.put("options", optionlist);
                        elementlist.add(elemap);

                    }
                }
            }
            Record area = iauditorgaarea.getAreaByAreacode(ZwfwUserSession.getInstance().getAreaCode()).getResult();
            // 拼接阶段的值
            String phasename = "";
            if (StringUtil.isNotBlank(phaseid)) {
                phasename = icodeitemsservice.getItemTextByCodeName("审批阶段", phaseid);
            }
            if (area != null) {
                if (StringUtil.isNotBlank(phasename)) {
                    custommap.put("title", area.get("xiaquname") + "【" + phasename + "阶段】");
                }
                else {
                    custommap.put("title", area.get("xiaquname"));
                }
            }
            custommap.put("collection", elementlist);
            statusmap.put("code", 200);
            rtnjson.put("custom", custommap);
            rtnjson.put("status", statusmap);
            this.addCallbackParam("data", rtnjson);
        }
        else {
            this.addCallbackParam("msg", "辖区标识为空！");
        }
    }

    public List<Map<String, Object>> getAllChildElement(String optionguid, List<Map<String, Object>> childlist,
            List<String> materialidlist) {
        List<Map<String, Object>> elementlist = new ArrayList<>();
        List<AuditSpElement> templist = iAuditSpElementService
                .findListByPreoptionGuidAndAreacode(optionguid, ZwfwUserSession.getInstance().getAreaCode())
                .getResult();
        for (int t = 0; t < templist.size(); t++) {
            if (!(templist.get(t) != null && StringUtil.isBlank(templist.get(t).getElementname())
                    && StringUtil.isBlank(templist.get(t).getElementquestion()))) {

                Map<String, Object> elemap = new HashMap<>();
                elemap.put("showSortUp", false);
                elemap.put("showSortDown", false);
                elemap.put("id", templist.get(t).getRowguid());
                elemap.put("type", templist.get(t).getElementname());
                elemap.put("question", templist.get(t).getElementquestion());
                elemap.put("multiple", ZwfwConstant.CONSTANT_STR_ONE.equals(templist.get(t).getMultiselect()));
                List<AuditSpOption> optlist = iAuditSpOptionService.findListByElementId(templist.get(t).getRowguid())
                        .getResult();
                List<Map<String, Object>> optionlist = new ArrayList<>();
                for (int j = 0; j < optlist.size(); j++) {
                    AuditSpOption auditSpOption = optlist.get(j);
                    if (StringUtil.isNotBlank(auditSpOption.getOptionname())) {

                        Map<String, Object> optmap = new HashMap<>();
                        optmap.put("checkbox", !ZwfwConstant.CONSTANT_STR_ONE.equals(templist.get(t).getMultiselect()));
                        optmap.put("showDelete", false);
                        optmap.put("id", auditSpOption.getRowguid());
                        optmap.put("content", auditSpOption.getOptionname());
                        optmap.put("equalvalue", auditSpOption.getEqualvalue());
                        String materialids = auditSpOption.getMaterialids();
                        List<Map<String, Object>> materiallist = new ArrayList<>();
                        if (StringUtil.isNotBlank(materialids)) {
                            String[] material = materialids.split(";");
                            for (int k = 0; k < material.length; k++) {
                                if (materialidlist != null) {
                                    if (!materialidlist.contains(material[k])) {
                                        continue;
                                    }
                                }
                                Map<String, Object> matermap = new HashMap<>();
                                String materialname = iaudittaskmaterial.getMaterialnameByMaterialid(material[k])
                                        .getResult();
                                if (StringUtil.isNotBlank(materialname)) {
                                    matermap.put("id", material[k]);
                                    matermap.put("name", materialname);
                                    matermap.put("url", "");
                                    materiallist.add(matermap);
                                }
                            }
                        }
                        optmap.put("relates", materiallist);
                        List<Map<String, Object>> childlisttemp = new ArrayList<>();
                        List<Map<String, Object>> childrenlist = new ArrayList<>();
                        childrenlist = getAllChildElement(auditSpOption.getRowguid(), childlisttemp, materialidlist);
                        optmap.put("collection", childrenlist);
                        optionlist.add(optmap);
                    }
                }
                elemap.put("options", optionlist);
                elementlist.add(elemap);
            }
        }
        return elementlist;
    }

    public void saveOrder(String guids, String type) {
        if (StringUtil.isNotBlank(guids) && guids.length() > 1) {
            String[] guid = guids.split(";");
            int num = guid.length;
            // 选项
            for (int i = 0; i < num; i++) {
                if ("option".equals(type)) {
                    iAuditSpOptionService.updateByField("ordernum", String.valueOf(num - i), guid[i]);
                }
                else {
                    iAuditSpElementService.updateByField("ordernum", String.valueOf(num - i), guid[i]);
                }
            }
            addCallbackParam("msg", "成功！");
        }
        else {
            addCallbackParam("msg", "未获取排序标识！");
        }
    }

    public void savePreoptionguid(String preoptionguid, String rowguid) {
        if (StringUtil.isNotBlank(preoptionguid) && StringUtil.isNotBlank(rowguid)) {
            iAuditSpElementService.updateByField("preoptionguid", preoptionguid, rowguid);
            addCallbackParam("msg", "成功");
        }
        else {
            addCallbackParam("msg", "未获取参数");
        }
    }

    public void saveText(String rowguid, String text, String type) {
        if (ZwfwConstant.CONSTANT_STR_ONE.equals(type)) {
            iAuditSpOptionService.updateByField("optionname", text, rowguid);
        }
        else if (ZwfwConstant.CONSTANT_STR_TWO.equals(type)) {
            iAuditSpElementService.updateByField("elementquestion", text, rowguid);
        }
        else if (ZwfwConstant.CONSTANT_STR_THREE.equals(type)) {
            iAuditSpElementService.updateByField("elementname", text, rowguid);
        }
        else if ("4".equals(type)) {
            iAuditSpOptionService.updateByField("equalvalue", text, rowguid);
        }
        else {
            addCallbackParam("msg", "未获取到类型！");
        }
    }

    public void delObj(String rowguid, String type) {
        if (StringUtil.isNotBlank(rowguid) && StringUtil.isNotBlank(type)) {
            if (ZwfwConstant.CONSTANT_STR_ONE.equals(type)) {
                delEleAndOption(rowguid, type);
            }
            else if (ZwfwConstant.CONSTANT_STR_TWO.equals(type)) {
                delEleAndOption(rowguid, type);
            }
            else {
                addCallbackParam("msg", "类型获取异常！");
            }
        }
        else {
            addCallbackParam("msg", "数据异常！");
        }
    }

    public void delEleAndOption(String rowguid, String type) {
        // 删除选项
        if (ZwfwConstant.CONSTANT_STR_ONE.equals(type)) {
            // 查询选项有无子选项
            iAuditSpOptionService.deleteByGuid(rowguid);
            SqlConditionUtil sqlc = new SqlConditionUtil();
            sqlc.eq("preoptionguid", rowguid);
            List<AuditSpElement> list = iAuditSpElementService.findListByCondition(sqlc.getMap()).getResult();
            for (AuditSpElement auditSpElement : list) {
                delEleAndOption(auditSpElement.getRowguid(), ZwfwConstant.CONSTANT_STR_TWO);
            }
        }
        else {
            iAuditSpElementService.deleteByGuid(rowguid);
            SqlConditionUtil sqlc = new SqlConditionUtil();
            sqlc.eq("elementguid", rowguid);
            List<AuditSpOption> list = iAuditSpOptionService.findListByCondition(sqlc.getMap()).getResult();
            for (AuditSpOption auditSpOption : list) {
                delEleAndOption(auditSpOption.getRowguid(), ZwfwConstant.CONSTANT_STR_ONE);
            }
        }
    }

    public void reSet(String taskid) {
        // 删除草稿的要素，重新复制
        SqlConditionUtil sqlc = new SqlConditionUtil();
        sqlc.eq("taskid", taskid);
        sqlc.eq("draft", ZwfwConstant.CONSTANT_STR_ONE);
        List<AuditSpElement> liste = iAuditSpElementService.findListByCondition(sqlc.getMap()).getResult();
        for (AuditSpElement auditSpElement : liste) {
            // 删除要素
            iAuditSpElementService.deleteByGuid(auditSpElement.getRowguid());
            // 删除情形
            iAuditSpOptionService.deleteByOneField("elementguid", auditSpElement.getRowguid());
        }
        copyelement(taskid);
    }

    public void save(String taskid) {
        String msg = "";
        if (StringUtil.isNotBlank(taskid)) {
            try {
                // 删除草稿的要素，重新复制
                SqlConditionUtil sqlc = new SqlConditionUtil();
                sqlc.eq("taskid", taskid);
                sqlc.isBlankOrValue("draft", ZwfwConstant.CONSTANT_STR_ZERO);
                List<AuditSpElement> liste = iAuditSpElementService.findListByCondition(sqlc.getMap()).getResult();
                for (AuditSpElement auditSpElement : liste) {
                    // 删除要素
                    iAuditSpElementService.deleteByGuid(auditSpElement.getRowguid());
                    // 删除情形
                    iAuditSpOptionService.deleteByOneField("elementguid", auditSpElement.getRowguid());
                }
                // 修改草稿为正式事项
                sqlc.clear();
                sqlc.eq("taskid", taskid);
                sqlc.eq("draft", ZwfwConstant.CONSTANT_STR_ONE);
                liste = iAuditSpElementService.findListByCondition(sqlc.getMap()).getResult();
                for (AuditSpElement auditSpElement : liste) {
                    iAuditSpElementService.updateByField("draft", ZwfwConstant.CONSTANT_STR_ZERO,
                            auditSpElement.getRowguid());
                }
                msg = "成功";
            }
            catch (Exception e) {
                msg = "数据异常！";
            }
        }
        else {
            msg = "未获取去到事项标识";
        }
        addCallbackParam("msg", msg);
    }

    public void copyelement(String taskid) {
        // 查询是否有草稿情形
        String msg;
        if (StringUtil.isBlank(taskid)) {
            addCallbackParam("msg", "未获取到事项版本唯一标识！");
            return;
        }
        SqlConditionUtil sql = new SqlConditionUtil();
        sql.eq("taskid", taskid);
        sql.eq("draft", ZwfwConstant.CONSTANT_STR_ONE);
        Integer count = iAuditSpElementService.findCountByCondition(sql.getMap()).getResult();
        if (count != null && count > 0) {
            msg = "成功！";
        }
        else {
            // 没有草稿，重新复制草稿
            sql.clear();
            sql.eq("taskid", taskid);
            sql.isBlankOrValue("draft", ZwfwConstant.CONSTANT_STR_ZERO);
            List<AuditSpElement> liste = iAuditSpElementService.findListByCondition(sql.getMap()).getResult();
            for (AuditSpElement auditSpElement : liste) {
                auditSpElement.setOperateusername("复制");
                auditSpElement.setRowguid(MDUtils.md5Hex(auditSpElement.getRowguid()));

                if (StringUtil.isNotBlank(auditSpElement.getPreoptionguid())) {
                    auditSpElement.setPreoptionguid(MDUtils.md5Hex(auditSpElement.getPreoptionguid()));
                }
                iAuditSpElementService.insert(auditSpElement);
            }
            List<AuditSpOption> listo = iAuditSpOptionService.findListByTaskid(taskid).getResult();
            for (AuditSpOption auditSpOption : listo) {
                auditSpOption.setOperateusername("复制");
                auditSpOption.setRowguid(MDUtils.md5Hex(auditSpOption.getRowguid()));
                auditSpOption.setElementguid(MDUtils.md5Hex(auditSpOption.getElementguid()));
                iAuditSpOptionService.insert(auditSpOption);
            }
            msg = "成功！";

        }
        addCallbackParam("msg", msg);
    }

    public void addelement(String optionguid) {
        String rowguid = UUID.randomUUID().toString();
        String msg = "";
        String areacode = ZwfwUserSession.getInstance().getAreaCode();
        if (StringUtil.isNotBlank(areacode)) {
            try {
                AuditSpElement element = new AuditSpElement();
                element.setOperatedate(new Date());
                element.setOperateusername(UserSession.getInstance().getDisplayName());
                element.setRowguid(rowguid);
                element.setAreacode(areacode);
                element.setOrdernum(0);
                element.setMultiselect("0");
                if (StringUtil.isNotBlank(optionguid)) {
                    element.setPreoptionguid(optionguid);
                }
                iAuditSpElementService.insert(element);
                msg = "成功！";
                addCallbackParam("guid", rowguid);
            }
            catch (Exception e) {
                msg = "添加数据异常！";
            }
        }
        else {
            msg = "未获辖区标识！";
        }
        addCallbackParam("msg", msg);
    }

    public void addoption(String elementguid) {
        String rowguid = UUID.randomUUID().toString();
        String msg = "";
        String areacode = ZwfwUserSession.getInstance().getAreaCode();
        if (StringUtil.isNotBlank(areacode) && StringUtil.isNotBlank(elementguid)) {
            try {
                AuditSpOption option = new AuditSpOption();
                option.setOperatedate(new Date());
                option.setOperateusername(UserSession.getInstance().getDisplayName());
                option.setRowguid(rowguid);
                option.setOrdernum(0);
                option.setElementguid(elementguid);
                option.setAreacode(areacode);
                iAuditSpOptionService.insert(option);
                msg = "成功！";
                addCallbackParam("guid", rowguid);
            }
            catch (Exception e) {
                msg = "添加数据异常！";
            }
        }
        else {
            msg = "未获取到辖区编码！";
        }
        addCallbackParam("msg", msg);
    }

    /**
     * 修改多选  
     */
    public void editmultiselect(String type, String rowguid) {
        String msg = "";
        if (StringUtil.isNotBlank(type) && StringUtil.isNotBlank(rowguid)) {
            try {
                if ("true".equals(type)) {
                    iAuditSpElementService.updateByField("multiselect", ZwfwConstant.CONSTANT_STR_ZERO, rowguid);
                }
                else {
                    iAuditSpElementService.updateByField("multiselect", ZwfwConstant.CONSTANT_STR_ONE, rowguid);
                }
                msg = "成功！";
            }
            catch (Exception e) {
                msg = "数据异常！";
            }
        }
        else {
            msg = "参数未获取！";
        }
        addCallbackParam("msg", msg);

    }

    public void refreshNewMaterial() {
        String optionguid = getRequestParameter("optionguid");
        String materialstemp = getRequestParameter("materialstemp");
        String materialsnametemp = getRequestParameter("materialsnametemp");
        AuditSpOption auditSpOption = iAuditSpOptionService.find(optionguid).getResult();
        if (auditSpOption != null) {
            auditSpOption.setMaterialids(materialstemp);
            auditSpOption.setMaterialnames(materialsnametemp);
            iAuditSpOptionService.update(auditSpOption);
            this.addCallbackParam("msg", "修改成功！");
        }
        else {
            this.addCallbackParam("error", "修改失败，请重新尝试！");
        }
    }

    public void delmaterial(String materialid, String materialname, String optionguid) {
        String msg = "";
        if (StringUtil.isNotBlank(materialid) && StringUtil.isNotBlank(optionguid)) {
            try {
                AuditSpOption option = iAuditSpOptionService.find(optionguid).getResult();
                String materials = option.getMaterialids();
                String materialnames = option.getMaterialnames();
                materials = materials.replaceAll(materialid + ";", "");
                materials = materials.replaceAll(materialid, "");
                option.setMaterialids(materials);
                option.setMaterialnames(materialnames);
                iAuditSpOptionService.update(option);
                msg = "成功！";
            }
            catch (Exception e) {
                msg = "数据异常！";
            }

        }
        else {
            msg = "参数未获取！";
        }
        addCallbackParam("msg", msg);
    }

    public void selectTaskCase() {
        String taskGuid = getRequestParameter("taskGuid");
        List<Map<String, Object>> rtnlist = new ArrayList<>();
        List<AuditTaskCase> auditTaskCaselist = iaudittaskcase.selectTaskCaseByTaskGuid(taskGuid, "1").getResult();
        if (!auditTaskCaselist.isEmpty()) {
            for (Record auditTaskCase : auditTaskCaselist) {
                Map<String, Object> map = new HashMap<>();
                map.put("value", auditTaskCase.get("rowguid"));
                map.put("text", auditTaskCase.get("casename"));
                rtnlist.add(map);
            }
            this.addCallbackParam("data", rtnlist);
        }
        else {
            this.addCallbackParam("msg", "暂无常用情形");
        }
    }

    // 核验时获取所有未绑定选项的材料list
    public void getAllUnBindMaterials() {
        String taskguid = getRequestParameter("copytaskguid");
        String taskId = getRequestParameter("taskid");
        SqlConditionUtil sql = new SqlConditionUtil();
        sql.eq("taskid", taskId);
        sql.eq("draft", ZwfwConstant.CONSTANT_STR_ONE);
        List<AuditSpElement> elementlist = iAuditSpElementService.findListByCondition(sql.getMap()).getResult();
        List<String> materiallist = new ArrayList<>();
        String rtnMaterialname = "";
        // 获取所有已选中材料list
        if (!elementlist.isEmpty()) {
            for (AuditSpElement auditSpElement : elementlist) {
                List<AuditSpOption> optionlist = iAuditSpOptionService.findListByElementId(auditSpElement.getRowguid())
                        .getResult();
                if (!optionlist.isEmpty()) {
                    for (AuditSpOption auditSpOption : optionlist) {
                        String materials = auditSpOption.getMaterialids();
                        if (StringUtil.isNotBlank(materials)) {
                            String[] temp = materials.split(";");
                            if (temp.length > 0) {
                                for (int i = 0; i < temp.length; i++) {
                                    if (temp[i] != null && !materiallist.contains(temp[i])) {
                                        materiallist.add(temp[i]);
                                    }
                                }
                            }
                        }
                    }

                }
            }
            SqlConditionUtil sqlc = new SqlConditionUtil();
            sqlc.eq("taskguid", taskguid);
            sqlc.eq("necessity", "20");
            List<AuditTaskMaterial> materialslist = iaudittaskmaterial.selectMaterialListByCondition(sqlc.getMap())
                    .getResult();
            if (!materialslist.isEmpty()) {
                for (Record auditTaskMaterial : materialslist) {
                    Boolean flag = true;
                    for (String materialid : materiallist) {
                        if (auditTaskMaterial.getStr("materialid").equals(materialid)) {
                            flag = false;
                        }
                    }
                    if (flag) {
                        if (StringUtil.isBlank(rtnMaterialname)) {
                            rtnMaterialname = auditTaskMaterial.get("materialname");
                        }
                        else {
                            rtnMaterialname += "、" + auditTaskMaterial.get("materialname");
                        }
                    }
                }
            }
            if (StringUtil.isBlank(rtnMaterialname)) {
                this.addCallbackParam("msg", "材料已全部绑定！");
            }
            else {
                this.addCallbackParam("data", rtnMaterialname);
            }
            if (materialslist.isEmpty()) {
                this.addCallbackParam("msg", "暂未有材料需要绑定！");
            }
        }
        else {
            this.addCallbackParam("msg", "事项尚未配置情形！");
        }

        // 获取事项所有非必要材料
    }

    // ###########################################套餐情形代码 start
    // 套餐情形代码###########################################
    public void getSpElementByOption() {
//        AuditTask audit = audittaskImpl.selectTaskByRowGuid(taskguid).getResult();
        String optionGuid = getRequestParameter("optionguid");
//        String taskId = audit.getTask_id();
        String type = getRequestParameter("type");
        SqlConditionUtil sql = new SqlConditionUtil();
        JSONObject dataJson = new JSONObject();
        // 定义存储要素信息的List
        List<JSONObject> subElementJsonList = new ArrayList<JSONObject>();
        try {
            if (("checkbox").equals(type)) {
                if (StringUtil.isNotBlank(optionGuid)) {
                    sql.eq("preoptionguid", "");
                }
                else {
                    String guids[] = optionGuid.split(";");
                    String options = null;
                    List<String> list = new ArrayList<>();
                    if (guids != null && guids.length > 0) {
                        for (int i = 0; i < guids.length; i++) {
                            list.add(guids[i]);
                        }
                        options = "'" + StringUtil.join(list, "','") + "'";
                        sql.in("preoptionguid", options);
                    }
                }
            }
            else {
                if (StringUtil.isNotBlank(optionGuid)) {
                    sql.eq("preoptionguid", optionGuid);
                }
            }
            sql.isBlankOrValue("draft", ZwfwConstant.CONSTANT_STR_ZERO);
            sql.setOrder("ordernum desc,operatedate", "asc");
            // 1、接口的入参转化为JSON对象
            // 1.1、获取要素选项唯一标识
            // 1.2、获取事项唯一标识
            // 2、获取事项的要素列表
            List<AuditSpElement> auditSpElements = iAuditSpElementService.findListByCondition(sql.getMap()).getResult();
            if (auditSpElements != null && !auditSpElements.isEmpty()) {
                for (AuditSpElement auditSpElement : auditSpElements) {
                    // 定义存储要素信息的Json
                    JSONObject subElementJson = new JSONObject();
                    // 2.1、查询要素选项信息
                    List<AuditSpOption> auditSpOptions = iAuditSpOptionService
                            .findListByElementIdWithoutNoName(auditSpElement.getRowguid()).getResult();
                    if (auditSpOptions != null && auditSpOptions.size() > 1) {
                        // 定义存储要素选项信息的List
                        List<JSONObject> optionJsonList = new ArrayList<JSONObject>();
                        for (AuditSpOption auditSpOption : auditSpOptions) {
                            // 定义存储要素选项的Json
                            JSONObject optionJson = new JSONObject();
                            optionJson.put("optionname", auditSpOption.getOptionname());
                            optionJson.put("optionnote", auditSpOption.getOptionnote());
                            optionJson.put("ounderline", StringUtil.isNotBlank(auditSpOption.getOptionnote()));
                            optionJson.put("optionguid", auditSpOption.getRowguid());
                            optionJsonList.add(optionJson);
                        }
                        subElementJson.put("optionlist", optionJsonList); // 要素选项列表
                        subElementJson.put("elementquestion", auditSpElement.getElementquestion()); // 要素问题
                        subElementJson.put("elementname", auditSpElement.getElementname()); // 要素问题
                        subElementJson.put("elementnote", auditSpElement.getElementnote()); // 要素问题
                        subElementJson.put("eunderline", StringUtil.isNotBlank(auditSpElement.getElementnote()));
                        subElementJson.put("elementguid", auditSpElement.getRowguid()); // 要素唯一标识
                        subElementJson.put("preoptionguid", auditSpElement.getPreoptionguid()); // 要素唯一标识
                        if (StringUtil.isNotBlank(auditSpElement.getMultiselect())
                                && ZwfwConstant.CONSTANT_STR_ZERO.equals(auditSpElement.getMultiselect())) {
                            subElementJson.put("type", "radio");
                            subElementJson.put("multitype", "单选");
                        }
                        else {
                            subElementJson.put("type", "checkbox");
                            subElementJson.put("multitype", "多选");
                        }
                        subElementJson.put("multiselect", auditSpElement.getMultiselect());
                        subElementJsonList.add(subElementJson);
                    }
                }
            }
            dataJson.put("subelementlist", subElementJsonList);// 要素及要素选项列表
            this.addCallbackParam("data", dataJson);
        }
        catch (Exception e) {
            e.printStackTrace();
            this.addCallbackParam("msg", "套餐后置要素获取失败：");
        }
    }

    public void getelementByoption() {
        String task_id = getRequestParameter("task_id");
        if (StringUtil.isBlank(task_id)) {
            getSpNewElementByOption();
        }
        else {
            getTaskElementByOption();
        }
    }

    public void getTaskElementByOption() {
        String optionGuid = getRequestParameter("optionguid");
        String type = getRequestParameter("type");
        String task_id = getRequestParameter("task_id");
        SqlConditionUtil sql = new SqlConditionUtil();
        JSONObject dataJson = new JSONObject();
        // 定义存储要素信息的List
        List<JSONObject> subElementJsonList = new ArrayList<JSONObject>();
        try {
            if (("checkbox").equals(type)) {
                if (StringUtil.isBlank(optionGuid)) {
                    sql.rightLike("preoptionguid", "start");
                }
                else {
                    String guids[] = optionGuid.split(";");
                    String options = null;
                    List<String> list = new ArrayList<>();
                    if (guids != null && guids.length > 0) {
                        for (int i = 0; i < guids.length; i++) {
                            list.add(guids[i]);
                        }
                        options = "'" + StringUtil.join(list, "','") + "'";
                        sql.in("preoptionguid", options);
                    }
                }
            }
            else {
                if (StringUtil.isNotBlank(optionGuid)) {
                    sql.eq("preoptionguid", optionGuid);
                }
            }
            sql.eq("taskid", task_id);
            sql.setOrder("ordernum desc,operatedate", "asc");
            // 1、接口的入参转化为JSON对象
            // 1.1、获取要素选项唯一标识
            // 1.2、获取事项唯一标识
            // 2、获取事项的要素列表
            List<AuditTaskElement> audittaskelements = iaudittaskelementservice.findListByCondition(sql.getMap())
                    .getResult();
            dealElementObj(audittaskelements, subElementJsonList);
            dataJson.put("subelementlist", subElementJsonList);// 要素及要素选项列表
            this.addCallbackParam("data", dataJson);
        }
        catch (Exception e) {
            log.info(e);
            this.addCallbackParam("msg", "套餐后置要素获取失败：");
        }
    }

    public void getSpNewElementByOption() {
        String optionGuid = getRequestParameter("optionguid");
        String type = getRequestParameter("type");
        SqlConditionUtil sql = new SqlConditionUtil();
        SqlConditionUtil sqlo = new SqlConditionUtil();
        JSONObject dataJson = new JSONObject();
        List<String> tasklist = new ArrayList<>();
        // 定义存储要素信息的List
        List<JSONObject> subElementJsonList = new ArrayList<JSONObject>();
        try {
            if (("checkbox").equals(type)) {
                String guids[] = optionGuid.split(";");
                String options = null;
                List<String> list = new ArrayList<>();
                if (guids != null && guids.length > 0) {
                    for (int i = 0; i < guids.length; i++) {
                        list.add(guids[i]);
                    }
                    options = "'" + StringUtil.join(list, "','") + "'";
                    sql.in("preoptionguid", options);
                    sqlo.in("rowguid", options);
                }
            }
            else {
                if (StringUtil.isNotBlank(optionGuid)) {
                    sql.eq("preoptionguid", optionGuid);
                    sqlo.eq("rowguid", optionGuid);
                }
            }
            sql.setOrder("ordernum desc,operatedate", "asc");
            // 1、接口的入参转化为JSON对象
            // 1.1、获取要素选项唯一标识
            // 1.2、获取事项唯一标识
            // 2、获取事项的要素列表
            List<AuditSpElement> auditSpElements = iAuditSpElementService.findListByCondition(sql.getMap()).getResult();
            List<AuditSpOption> listo = iAuditSpOptionService.findListByCondition(sqlo.getMap()).getResult();
            // 如果关联了事项获事项问题
            Map<String, String> task_option = new HashMap<>();
            for (AuditSpOption auditSpOption : listo) {
                if (StringUtil.isNotBlank(auditSpOption.getTaskid())) {
                    String[] taskids = auditSpOption.getTaskid().split(";");
                    for (String string : taskids) {
                        if (!tasklist.contains(string)) {
                            tasklist.add(string);
                            task_option.put(string, auditSpOption.getRowguid());
                        }
                    }
                }
            }
            dealElementObjByTaskid(subElementJsonList, tasklist);
            for (JSONObject element : subElementJsonList) {
                element.put("preoptionguid", task_option.get(element.get("task_id")));
            }
            if (auditSpElements != null && !auditSpElements.isEmpty()) {
                for (AuditSpElement auditSpElement : auditSpElements) {
                    // 定义存储要素信息的Json
                    JSONObject subElementJson = new JSONObject();
                    // 2.1、查询要素选项信息
                    List<AuditSpOption> auditSpOptions = iAuditSpOptionService
                            .findListByElementIdWithoutNoName(auditSpElement.getRowguid()).getResult();
                    if (auditSpOptions != null && auditSpOptions.size() > 1) {
                        // 定义存储要素选项信息的List
                        List<JSONObject> optionJsonList = new ArrayList<JSONObject>();
                        for (AuditSpOption auditSpOption : auditSpOptions) {
                            // 定义存储要素选项的Json
                            JSONObject optionJson = new JSONObject();
                            optionJson.put("optionname", auditSpOption.getOptionname());
                            optionJson.put("optionnote", auditSpOption.getOptionnote());
                            optionJson.put("ounderline", StringUtil.isNotBlank(auditSpOption.getOptionnote()));
                            optionJson.put("optionguid", auditSpOption.getRowguid());
                            optionJsonList.add(optionJson);
                        }
                        subElementJson.put("optionlist", optionJsonList); // 要素选项列表
                        subElementJson.put("elementnote", auditSpElement.getElementnote()); // 要素问题
                        subElementJson.put("eunderline", StringUtil.isNotBlank(auditSpElement.getElementnote()));
                        subElementJson.put("elementname", auditSpElement.getElementname()); // 要素问题
                        subElementJson.put("elementguid", auditSpElement.getRowguid()); // 要素唯一标识
                        subElementJson.put("preoptionguid", auditSpElement.getPreoptionguid()); // 要素唯一标识
                        if (StringUtil.isNotBlank(auditSpElement.getMultiselect())
                                && ZwfwConstant.CONSTANT_STR_ZERO.equals(auditSpElement.getMultiselect())) {
                            subElementJson.put("type", "radio");
                            subElementJson.put("multitype", "单选");
                        }
                        else {
                            subElementJson.put("type", "checkbox");
                            subElementJson.put("multitype", "多选");
                        }
                        subElementJson.put("multiselect", auditSpElement.getMultiselect());
                        subElementJsonList.add(subElementJson);
                    }
                }
            }
            dataJson.put("subelementlist", subElementJsonList);// 要素及要素选项列表
            this.addCallbackParam("data", dataJson);
        }
        catch (Exception e) {
            log.info("========Exception信息========" + e.getMessage());
            this.addCallbackParam("msg", "套餐后置要素获取失败：");
        }
    }

    public void dealElementObjByTaskid(List<JSONObject> subElementJsonList, List<String> tasklist) {
        if (tasklist == null || tasklist.isEmpty()) {
            return;
        }
        // 过滤已禁用的或者删除的是香港
        tasklist.removeIf(a -> {
            return iaudittask.selectUsableTaskByTaskID(a).getResult() == null;
        });
        SqlConditionUtil sqlc = new SqlConditionUtil();
        sqlc.in("taskid", StringUtil.joinSql(tasklist));
        sqlc.isBlankOrValue("draft", ZwfwConstant.CONSTANT_STR_ZERO);
        sqlc.rightLike("preoptionguid", "start");
        sqlc.setOrder("ordernum desc,operatedate", "asc");
        List<AuditTaskElement> audittaskelements = iaudittaskelementservice.findListByCondition(sqlc.getMap())
                .getResult();
        dealElementObj(audittaskelements, subElementJsonList);
    }

    public void dealElementObj(List<AuditTaskElement> audittaskelements, List<JSONObject> subElementJsonList) {
        if (audittaskelements != null && !audittaskelements.isEmpty()) {
            for (AuditTaskElement audittaskelement : audittaskelements) {
                // 定义存储要素信息的Json
                JSONObject subElementJson = new JSONObject();
                // 2.1、查询要素选项信息
                List<AuditTaskOption> audittaskoptions = iaudittaskoptionservice
                        .findListByElementIdWithoutNoName(audittaskelement.getRowguid()).getResult();
                if (audittaskoptions != null && audittaskoptions.size() > 1) {
                    // 定义存储要素选项信息的List
                    List<JSONObject> optionJsonList = new ArrayList<JSONObject>();
                    for (AuditTaskOption audittaskoption : audittaskoptions) {
                        // 定义存储要素选项的Json
                        JSONObject optionJson = new JSONObject();
                        optionJson.put("optionname", audittaskoption.getOptionname());
                        optionJson.put("optionnote", audittaskoption.getOptionnote());
                        optionJson.put("ounderline", StringUtil.isNotBlank(audittaskoption.getOptionnote()));
                        optionJson.put("optionguid", audittaskoption.getRowguid());
                        optionJsonList.add(optionJson);
                    }
                    subElementJson.put("optionlist", optionJsonList); // 要素选项列表
                    subElementJson.put("elementquestion", audittaskelement.getElementquestion()); // 要素问题
                    subElementJson.put("elementname", audittaskelement.getElementname()); // 要素问题
                    subElementJson.put("elementnote", audittaskelement.getElementnote()); // 要素问题
                    subElementJson.put("eunderline", StringUtil.isNotBlank(audittaskelement.getElementnote()));
                    subElementJson.put("elementguid", audittaskelement.getRowguid()); // 要素唯一标识
                    subElementJson.put("preoptionguid", audittaskelement.getPreoptionguid()); // 要素唯一标识
                    subElementJson.put("task_id", audittaskelement.getTaskid()); // 要素唯一标识
                    if (StringUtil.isNotBlank(audittaskelement.getMultiselect())
                            && ZwfwConstant.CONSTANT_STR_ZERO.equals(audittaskelement.getMultiselect())) {
                        subElementJson.put("type", "radio");
                        subElementJson.put("multitype", "单选");
                    }
                    else {
                        subElementJson.put("type", "checkbox");
                        subElementJson.put("multitype", "多选");
                    }
                    subElementJson.put("multiselect", audittaskelement.getMultiselect());
                    subElementJsonList.add(subElementJson);
                }
            }
        }
    }

    /**
     * 获取套餐下要素及要素选项
     * 
     * @param params
     *            接口的入参
     * @return
     */
    public void getSpElement() {
        try {
            String businessguid = getRequestParameter("businessguid");
            SqlConditionUtil sql = new SqlConditionUtil();
            sql.isBlankOrValue("draft", ZwfwConstant.CONSTANT_STR_ZERO);
            sql.isBlank("preoptionguid");
            if (StringUtil.isNotBlank(businessguid)) {
                sql.eq("businessguid", businessguid);
            }
            sql.setOrder("ordernum desc,operatedate", "asc");
            // 1、接口的入参转化为JSON对象
            // 1.1、获取事项唯一标识
            // 2、获取事项的情景要素列表 (没有前置要素的)
            List<AuditSpElement> auditSpElements = iAuditSpElementService.findListByCondition(sql.getMap()).getResult();
            // 定义存储要素信息的List
            List<JSONObject> elementJsonList = new ArrayList<JSONObject>();
            if (auditSpElements != null && !auditSpElements.isEmpty()) {
                for (AuditSpElement auditSpElement : auditSpElements) {
                    // 定义存储要素信息的json
                    JSONObject elementJson = new JSONObject();
                    // 2.1 根据事项要素唯一标识查询事项要素选项
                    List<AuditSpOption> auditSpOptions = iAuditSpOptionService
                            .findListByElementId(auditSpElement.getRowguid()).getResult();
                    if (auditSpOptions != null && auditSpOptions.size() > 1) {
                        // 定义存储要素选项信息的List
                        List<JSONObject> optionJsonList = new ArrayList<JSONObject>();
                        for (AuditSpOption auditSpOption : auditSpOptions) {
                            // 定义存储要素选项信息的json
                            JSONObject optionJson = new JSONObject();
                            optionJson.put("optionname", auditSpOption.getOptionname());
                            optionJson.put("optionnote", auditSpOption.getOptionnote());
                            optionJson.put("ounderline", StringUtil.isNotBlank(auditSpOption.getOptionnote()));
                            optionJson.put("optionguid", auditSpOption.getRowguid());
                            optionJsonList.add(optionJson);
                        }
                        elementJson.put("optionlist", optionJsonList); // 要素选项列表
                        elementJson.put("elementname", auditSpElement.getElementname()); // 要素问题
                        elementJson.put("elementnote", auditSpElement.getElementnote()); // 要素问题
                        elementJson.put("eunderline", StringUtil.isNotBlank(auditSpElement.getElementnote()));
                        elementJson.put("elementquestion", auditSpElement.getElementquestion()); // 要素问题
                        elementJson.put("elementguid", auditSpElement.getRowguid()); // 要素唯一标识
                        if (StringUtil.isNotBlank(auditSpElement.getMultiselect())
                                && ZwfwConstant.CONSTANT_STR_ZERO.equals(auditSpElement.getMultiselect())) {
                            elementJson.put("type", "radio");
                            elementJson.put("multitype", "单选");
                        }
                        else {
                            elementJson.put("type", "checkbox");
                            elementJson.put("multitype", "多选");
                        }
                        elementJson.put("multiselect", auditSpElement.getMultiselect());
                        elementJsonList.add(elementJson);
                    }
                }
            }
            // 定义返回的JSON
            JSONObject dataJson = new JSONObject();
            dataJson.put("elementlist", elementJsonList);// 要素及要素选项列表
            this.addCallbackParam("data", dataJson);
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======getSPElement异常信息：" + e.getMessage() + "=======");
            this.addCallbackParam("msg", "套餐要素获取失败：");
        }
    }

    /**
     * 获取套餐下要素及要素选项
     * 
     * @param params
     *            接口的入参
     * @return
     */
    public void getSpNewElement() {
        try {
            String businessguid = getRequestParameter("businessguid");
            if (StringUtil.isBlank(businessguid)) {
                this.addCallbackParam("msg", "未获取到主题标识！");
            }
            SqlConditionUtil sqlc = new SqlConditionUtil();
            sqlc.eq("businessguid", businessguid);
            sqlc.rightLike("preoptionguid", "start");
            sqlc.setOrderDesc("ordernum");
            sqlc.setOrderAsc("operatedate");
            List<AuditSpElement> auditSpElements = iAuditSpElementService.findListByCondition(sqlc.getMap())
                    .getResult();
            // 定义存储要素信息的List
            List<JSONObject> elementJsonList = new ArrayList<JSONObject>();
            // 查找必办事项的情形问题
            sqlc.clear();

            if (auditSpElements != null && !auditSpElements.isEmpty()) {
                for (AuditSpElement auditSpElement : auditSpElements) {
                    // 定义存储要素信息的json
                    JSONObject elementJson = new JSONObject();
                    // 2.1 根据事项要素唯一标识查询事项要素选项
                    List<AuditSpOption> auditSpOptions = iAuditSpOptionService
                            .findListByElementId(auditSpElement.getRowguid()).getResult();
                    if (auditSpOptions != null && auditSpOptions.size() > 1) {
                        // 定义存储要素选项信息的List
                        List<JSONObject> optionJsonList = new ArrayList<JSONObject>();
                        for (AuditSpOption auditSpOption : auditSpOptions) {
                            // 定义存储要素选项信息的json
                            JSONObject optionJson = new JSONObject();
                            optionJson.put("optionname", auditSpOption.getOptionname());
                            optionJson.put("optionnote", auditSpOption.getOptionnote());
                            optionJson.put("ounderline", StringUtil.isNotBlank(auditSpOption.getOptionnote()));
                            optionJson.put("optionguid", auditSpOption.getRowguid());
                            optionJsonList.add(optionJson);
                        }
                        elementJson.put("optionlist", optionJsonList); // 要素选项列表
                        elementJson.put("elementname", auditSpElement.getElementname()); // 要素问题
                        elementJson.put("elementnote", auditSpElement.getElementnote()); // 要素问题
                        elementJson.put("eunderline", StringUtil.isNotBlank(auditSpElement.getElementnote()));
                        elementJson.put("elementquestion", auditSpElement.getElementquestion()); // 要素问题
                        elementJson.put("elementguid", auditSpElement.getRowguid()); // 要素唯一标识
                        if (StringUtil.isNotBlank(auditSpElement.getMultiselect())
                                && ZwfwConstant.CONSTANT_STR_ZERO.equals(auditSpElement.getMultiselect())) {
                            elementJson.put("type", "radio");
                            elementJson.put("multitype", "单选");
                        }
                        else {
                            elementJson.put("type", "checkbox");
                            elementJson.put("multitype", "多选");
                        }
                        elementJson.put("multiselect", auditSpElement.getMultiselect());
                        elementJsonList.add(elementJson);
                    }
                }
            }
            // 定义返回的JSON
            JSONObject dataJson = new JSONObject();
            dataJson.put("elementlist", elementJsonList);// 要素及要素选项列表
            this.addCallbackParam("data", dataJson);
        }
        catch (Exception e) {
            log.info("========Exception信息========" + e.getMessage());
            log.info("=======getSPElement异常信息：" + e.getMessage() + "=======");
            this.addCallbackParam("msg", "套餐要素获取失败：");
        }
    }

    public void getNecessaryElement() {
        String businessguid = getRequestParameter("businessguid");
        if (StringUtil.isBlank(businessguid)) {
            this.addCallbackParam("msg", "未获取到主题标识！");
        }
        // 定义存储要素信息的List
        List<JSONObject> elementJsonList = new ArrayList<JSONObject>();
        SqlConditionUtil sqlc = new SqlConditionUtil();
        sqlc.eq("businessguid", businessguid);
        sqlc.eq("elementguid", "root");
        List<AuditSpOption> optionlist = iAuditSpOptionService.findListByCondition(sqlc.getMap()).getResult();
        if (optionlist != null && !optionlist.isEmpty()) {
            String taskids = optionlist.get(0).getTaskid();
            if (StringUtil.isNotBlank(taskids)) {
                List<String> taskidlist = Arrays.asList(taskids.split(";"));
                dealElementObjByTaskid(elementJsonList, taskidlist);
            }
        }
        // 定义返回的JSON
        JSONObject dataJson = new JSONObject();
        dataJson.put("elementlist", elementJsonList);// 要素及要素选项列表
        this.addCallbackParam("data", dataJson);

    }

//    @SuppressWarnings("unchecked")
    public void getTasklistBySelectedOptions() {
        try {
            String selectedoptions = getRequestParameter("selectedoptions");
            String Subappguid = getRequestParameter("Subappguid");
            String areacodeselect = getRequestParameter("areacodeselect");
            String businessGuid = getRequestParameter("businessGuid");
            String areacode = ZwfwUserSession.getInstance().getAreaCode();
            if (StringUtil.isNotBlank(areacodeselect)) {
                areacode = areacodeselect;
            }
            // 1、接口的入参转化为JSON对象
            // 1.1、获取用户选择信息
            // 按照传过来的数据进行解析，取出optionguidlist
            if (StringUtil.isNotBlank(selectedoptions)) {
                // 定义返回的JSON
                List<Record> recordlist = new ArrayList<>();
                List<JSONObject> tasklist = new ArrayList<>();
                JSONObject rtnJson = new JSONObject();
                recordlist = ihandlespimaterial
                        .initTasklistBySelectedOptions(selectedoptions, Subappguid, areacode, businessGuid).getResult();
                for (Record record : recordlist) {
                    // 定义存储要素信息的json
                    JSONObject elementJson = new JSONObject();
                    // 定义存储要素选项信息的List
                    List<JSONObject> optionJsonList = new ArrayList<JSONObject>();
                    // 定义存储要素选项信息的json
                    JSONObject optionJson1 = new JSONObject();
                    JSONObject optionJson2 = new JSONObject();
                    String questions = record.get("questions");
                    if (StringUtil.isNotBlank(questions)) {
                        JSONObject json = (JSONObject) JSONObject.parse(questions);
                        if (StringUtil.isNotBlank(json.get("question"))) {
                            elementJson.put("question", json.get("question") + " (" + record.get("taskname") + ")"); // 要素问题
                            optionJson1.put("optionname", json.get("answer_yes"));
                            optionJson1.put("optionguid", record.get("taskid"));
                            optionJson2.put("optionname", json.get("answer_no"));
                            optionJson2.put("optionguid", "1");
                        }
                        else {
                            elementJson.put("question", record.get("taskname")); // 要素问题
                            optionJson1.put("optionname", "未办理过，需要办理");
                            optionJson1.put("optionguid", record.get("taskid"));
                            optionJson2.put("optionname", "已办理过");
                            optionJson2.put("optionguid", "1");
                        }
                    }
                    else {
                        elementJson.put("question", record.get("taskname")); // 要素问题
                        optionJson1.put("optionname", "未办理过，需要办理");
                        optionJson1.put("optionguid", record.get("taskid"));
                        optionJson2.put("optionname", "已办理过");
                        optionJson2.put("optionguid", "1");
                    }
                    optionJsonList.add(optionJson1);
                    optionJsonList.add(optionJson2);

                    elementJson.put("taskname", record.get("taskname")); // 要素问题
                    elementJson.put("optionlist", optionJsonList); // 要素选项列表
                    elementJson.put("questions", record.get("questions")); // 要素问题
                    elementJson.put("areacode", record.get("areacode")); // 要素问题
                    elementJson.put("taskid", record.get("taskid")); // 要素问题
                    elementJson.put("basetaskguid", record.get("basetaskguid")); // 要素唯一标识
                    elementJson.put("type", "radio");
                    // elementJson.put("multiselect",
                    // auditSpElement.getMultiselect());
                    tasklist.add(elementJson);
                }
                // 定义返回的JSON
                rtnJson.put("tasklist", tasklist);// 要素及要素选项列表

                this.addCallbackParam("data", rtnJson);

            }
            else {
                this.addCallbackParam("msg", "初始化材料出错！");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======getCaseGuidBySelectedOptions异常信息：" + e.getMessage() + "=======");
        }
    }

    public void getnewTasklistBySelectedOptions() {
        try {
            String selectedoptions = getRequestParameter("selectedoptions");
            String Subappguid = getRequestParameter("Subappguid");
            String businessGuid = getRequestParameter("businessGuid");
            String areacode = ZwfwUserSession.getInstance().getAreaCode();
            // 1、接口的入参转化为JSON对象
            // 1.1、获取用户选择信息
            // 按照传过来的数据进行解析，取出optionguidlist
            if (StringUtil.isNotBlank(selectedoptions)) {
                // 定义返回的JSON
                List<Record> recordlist = new ArrayList<>();
                JSONObject rtnJson = new JSONObject();
                recordlist = ihandlespimaterial
                        .initTasklistBySelectedOptions(selectedoptions, Subappguid, areacode, businessGuid).getResult();

                // 定义返回的JSON
                rtnJson.put("tasklist", recordlist);// 要素及要素选项列表
                this.addCallbackParam("data", rtnJson);
            }
            else {
                this.addCallbackParam("msg", "初始化材料出错！");
            }
        }
        catch (Exception e) {
            log.info("========Exception信息========" + e.getMessage());
            log.info("=======getCaseGuidBySelectedOptions异常信息：" + e.getMessage() + "=======");
        }
    }

    public void saveSelectedOptions() {
        try {
            String selectedoptions = getRequestParameter("selectedoptions");
            String Subappguid = getRequestParameter("Subappguid");
            String areacode = ZwfwUserSession.getInstance().getAreaCode();
            // 1、接口的入参转化为JSON对象
            // 1.1、获取用户选择信息
            // 按照传过来的数据进行解析，取出optionguidlist
            if (StringUtil.isNotBlank(selectedoptions)) {
                iAuditSpSelectedOptionService.saveSelectedOptions(selectedoptions, Subappguid, areacode);
                this.addCallbackParam("msg", "保存成功！");
            }
            else {
                this.addCallbackParam("msg", "初始化材料出错！");
            }
        }
        catch (Exception e) {
            log.info(e);
            log.info("=======saveSelectedOptions异常信息：" + e.getMessage() + "=======");
        }
    }

    @SuppressWarnings("unchecked")
    public void getMateriallistBySelectedOptions() {
        try {
            String selectedoptions = getRequestParameter("selectedoptions");
            String subAppGuid = getRequestParameter("subAppGuid");
            String businessGuid = getRequestParameter("businessGuid");
            String biGuid = getRequestParameter("biGuid");
            String phaseGuid = getRequestParameter("phaseGuid");
            // 1、接口的入参转化为JSON对象
            // 1.1、获取用户选择信息
            // 所有选择 否 的guid列表
            List<String> taskids = new ArrayList<>();
            // 所有事项id列表
            List<String> taskidlist = new ArrayList<>();
            // 区域编码列表
            // 将选择得数据添加到selected option表中去
            AuditSpSelectedOption auditspselectedoption = iAuditSpSelectedOptionService
                    .getSelectedoptionsBySubappGuid(subAppGuid).getResult();
            if (auditspselectedoption != null) {
                auditspselectedoption.setInsertdate(new Date());
                auditspselectedoption.setTaskSelectedoptions(selectedoptions);
                iAuditSpSelectedOptionService.update(auditspselectedoption);
            }

            // 按照传过来的数据进行解析，取出optionguidlist
            if (StringUtil.isNotBlank(selectedoptions)) {

                // 清空spimaterial表
                iauditspimaterial.deleteSpIMaterialBySubappguid(subAppGuid);
                // 清空spitask表
                auditSpITaskService.deleteSpITaskBySubappGuid(subAppGuid);
                JSONObject jsonObject = JSONObject.parseObject(selectedoptions);
                List<Map<String, Object>> jsona = (List<Map<String, Object>>) jsonObject.get("selectedoptions");
                for (Map<String, Object> map : jsona) {
                    List<Map<String, Object>> maplist = (List<Map<String, Object>>) map.get("optionlist");
                    for (Map<String, Object> map2 : maplist) {
                        if (StringUtil.isNotBlank(String.valueOf(map2.get("optionguid")))) {
                            taskids.add(String.valueOf(map2.get("optionguid")));
                        }
                    }
                }

                for (String taskid : taskids) {
                    Record auditTask = iaudittask.selectUsableTaskByTaskID(taskid).getResult();
                    if (auditTask != null) {
                        auditSpITaskService.addTaskInstance(businessGuid, biGuid, phaseGuid, auditTask.get("rowguid"),
                                auditTask.get("taskname"), subAppGuid,
                                auditTask.get("ordernum") == null ? 0 : auditTask.get("ordernum"),
                                auditTask.get("areacode"), "");
                    }
                }

                ihandlespimaterial.initIntegratedMaterial(subAppGuid, businessGuid, biGuid, phaseGuid, "", "")
                        .getResult();

                // 定义存储要素信息的List
                List<JSONObject> elementJsonList = new ArrayList<JSONObject>();
                if (ValidateUtil.isNotBlankCollection(taskidlist)) {
                }
                // 定义返回的JSON
                JSONObject dataJson = new JSONObject();
                dataJson.put("elementlist", elementJsonList);// 要素及要素选项列表
                this.addCallbackParam("data", dataJson);
            }
            else {
                this.addCallbackParam("msg", "初始化材料出错！");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======getCaseGuidBySelectedOptions异常信息：" + e.getMessage() + "=======");
        }
    }

    @SuppressWarnings("unchecked")
    public void getnewMateriallistBySelectedOptions() {
        try {
            String subAppGuid = getRequestParameter("subAppGuid");
            String businessGuid = getRequestParameter("businessGuid");
            String biGuid = getRequestParameter("biGuid");
            String phaseGuid = getRequestParameter("phaseGuid");
            // 1、接口的入参转化为JSON对象
            // 1.1、获取用户选择信息
            // 所有选择 否 的guid列表
            List<String> optionlist = new ArrayList<>();
            // 所有事项id列表
            List<String> taskidlist = new ArrayList<>();
            // 区域编码列表
            // 将选择得数据添加到selected option表中去
            AuditSpSelectedOption auditspselectedoption = iAuditSpSelectedOptionService
                    .getSelectedoptionsBySubappGuid(subAppGuid).getResult();
            String selectedoptions = auditspselectedoption.getSelectedoptions();
            // 按照传过来的数据进行解析，取出optionguidlist
            if (StringUtil.isNotBlank(selectedoptions)) {

                // 清空spimaterial表
                iauditspimaterial.deleteSpIMaterialBySubappguid(subAppGuid);
                // 清空spitask表
                auditSpITaskService.deleteSpITaskBySubappGuid(subAppGuid);
                JSONObject jsonObject = JSONObject.parseObject(selectedoptions);
                List<Map<String, Object>> jsona = (List<Map<String, Object>>) jsonObject.get("selectedoptions");
                for (Map<String, Object> map : jsona) {
                    List<Map<String, Object>> maplist = (List<Map<String, Object>>) map.get("optionlist");
                    for (Map<String, Object> map2 : maplist) {
                        if (StringUtil.isNotBlank(String.valueOf(map2.get("optionguid")))) {
                            optionlist.add(String.valueOf(map2.get("optionguid")));
                        }
                    }
                }
                List<AuditSpOption> seloptionlist = new ArrayList<>();
                // 查找所有选项关联的事项
                SqlConditionUtil sqlc = new SqlConditionUtil();
                if (!optionlist.isEmpty()) {
                    sqlc.in("rowguid", StringUtil.joinSql(optionlist));
                    sqlc.eq("businessGuid", businessGuid);
                    seloptionlist.addAll(iAuditSpOptionService.findListByCondition(sqlc.getMap()).getResult());
                }
                sqlc.clear();
                sqlc.eq("businessGuid", businessGuid);
                sqlc.eq("elementguid", "root");
                seloptionlist.addAll(iAuditSpOptionService.findListByCondition(sqlc.getMap()).getResult());
                List<String> taskids = new ArrayList<>();
                for (AuditSpOption auditSpOption : seloptionlist) {
                    if (StringUtil.isNotBlank(auditSpOption.getTaskid())) {
                        String[] taskid = auditSpOption.getTaskid().split(";");
                        for (String string : taskid) {
                            if (!taskids.contains(string)) {
                                String str = auditSpOption.getRowguid() + "_" + string;
                                if (!taskids.contains(str)) {
                                    taskids.add(str);
                                }
                            }

                        }
                    }
                }

                // 存放areacode_taskid，防止下放到统一辖区的同一事项出现两条实例数据
                for (String taskid : taskids) {
                    AuditTask auditTask = iaudittask.selectUsableTaskByTaskID(taskid.split("_")[1]).getResult();
                    /*                    if (auditTask != null) {
                        auditSpITaskService.addTaskInstance(businessGuid, biGuid, phaseGuid, auditTask.get("rowguid"),
                                auditTask.get("taskname"), subAppGuid,
                                auditTask.get("ordernum") == null ? 0 : auditTask.get("ordernum"),
                                auditTask.get("areacode"), "");
                    }*/
                    List<String> list = new ArrayList<>();
                    if (auditTask != null && auditTask.getIs_enable() == ZwfwConstant.CONSTANT_INT_ONE) {
                        // 判断节点在audit_sp_optiontownship中是否有市级事项数据
                        SqlConditionUtil sqlutil = new SqlConditionUtil();
                        sqlutil.eq("optionguid", taskid.split("_")[0]);
                        sqlutil.eq("taskid", taskid.split("_")[1]);
                        sqlutil.eq("townshipcode", auditTask.get("areacode"));
                        List<AuditSpOptiontownship> shiplist = iauditspoptiontownshipservice
                                .findListByCondition(sqlutil.getMap());
                        if (ValidateUtil.isNotBlankCollection(shiplist)) {
                            // 防止出现重复的数据
                            if (!list.contains(auditTask.get("areacode") + "_" + taskid.split("_")[1])) {
                                auditSpITaskService.addTaskInstance(businessGuid, biGuid, phaseGuid,
                                        auditTask.get("rowguid"), auditTask.get("taskname"), subAppGuid,
                                        auditTask.get("ordernum") == null ? 0 : auditTask.get("ordernum"),
                                        auditTask.get("areacode"), "", "", "");
                                list.add(auditTask.get("areacode") + "_" + taskid.split("_")[1]);
                            }
                        }
                        // 根据选项的optionguid以及事项的taskid判断事项是否下放到乡镇
                        sqlutil.clear();
                        shiplist.clear();
                        sqlutil.eq("optionguid", taskid.split("_")[0]);
                        sqlutil.eq("taskid", taskid.split("_")[1]);
                        sqlutil.eq("length(townshipcode)", "9");
                        shiplist = iauditspoptiontownshipservice.findListByCondition(sqlutil.getMap());
                        if (ValidateUtil.isNotBlankCollection(shiplist)) {
                            for (AuditSpOptiontownship ship : shiplist) {
                                if (!list.contains(ship.getTownshipcode() + "_" + taskid.split("_")[1])) {
                                    auditSpITaskService.addTaskInstance(businessGuid, biGuid, phaseGuid,
                                            auditTask.get("rowguid"), auditTask.get("taskname"), subAppGuid,
                                            auditTask.get("ordernum") == null ? 0 : auditTask.get("ordernum"),
                                            auditTask.get("areacode"), "", "", ship.getTownshipcode());
                                    list.add(ship.getTownshipcode() + "_" + taskid.split("_")[1]);
                                }
                            }
                        }
                    }

                }

                ihandlespimaterial.initNewIntegratedMaterial(subAppGuid, businessGuid, biGuid, phaseGuid, "", "")
                        .getResult();

                // 定义存储要素信息的List
                List<JSONObject> elementJsonList = new ArrayList<JSONObject>();
                if (ValidateUtil.isNotBlankCollection(taskidlist)) {
                }
                // 定义返回的JSON
                JSONObject dataJson = new JSONObject();
                dataJson.put("elementlist", elementJsonList);// 要素及要素选项列表
                this.addCallbackParam("data", dataJson);
            }
            else {
                this.addCallbackParam("msg", "初始化材料出错！");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======getCaseGuidBySelectedOptions异常信息：" + e.getMessage() + "=======");
        }
    }

    public void getSelectedoptionsBySubappGuid() {
        String subAppGuid = getRequestParameter("subAppGuid");
        String selectedoptions = "";
        String taskselectedoptions = "";
        AuditSpSelectedOption auditspselectedoption = iAuditSpSelectedOptionService
                .getSelectedoptionsBySubappGuid(subAppGuid).getResult();
        if (auditspselectedoption != null) {
            selectedoptions = auditspselectedoption.get("selectedoptions");
            taskselectedoptions = auditspselectedoption.get("taskselectedoptions");
        }
        this.addCallbackParam("selectedoptions", selectedoptions);
        this.addCallbackParam("taskselectedoptions", taskselectedoptions);
    }

    public void getDefaultAndShareOptionguid() {
        String businessguid = getRequestParameter("businessguid");
        List<String> defaultsel = new ArrayList<>();
        Map<String, String> sharesel = new HashMap<>();
        List<AuditSpShareoption> auditspshareoptionlist = iauditspshareoption.getlistBybusinessguid(businessguid)
                .getResult();
        if (!auditspshareoptionlist.isEmpty()) {
            defaultsel = auditspshareoptionlist.stream()
                    .filter(a -> a.getSelecttype().equals(ZwfwConstant.CONSTANT_STR_ONE))
                    .map(AuditSpShareoption::getOptionguid).collect(Collectors.toList());
            Map<String, List<AuditSpShareoption>> shareselarr = auditspshareoptionlist.stream()
                    .filter(a -> a.getSelecttype().equals(ZwfwConstant.CONSTANT_STR_TWO))
                    .collect(Collectors.groupingBy(a -> a.getSharevalue()));
            for (Map.Entry<String, List<AuditSpShareoption>> entry : shareselarr.entrySet()) {
                List<AuditSpShareoption> value = entry.getValue();
                if (value != null && !value.isEmpty()) {
                    sharesel.put(entry.getKey(),
                            value.stream().map(a -> a.getOptionguid()).collect(Collectors.joining(";")));
                }
            }
        }
        this.addCallbackParam("sharesel", sharesel);
        this.addCallbackParam("defaultsel", defaultsel);
    }

    // ###########################################套餐情形代码 end
    // 套餐情形代码###########################################

}
