package com.epoint.audittc.elememt.action;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import com.epoint.auditspoptiontownship.api.IAuditSpOptiontownshipService;
import com.epoint.auditspoptiontownship.api.entity.AuditSpOptiontownship;
import com.epoint.basic.audittask.delegate.domain.AuditTaskDelegate;
import com.epoint.basic.audittask.delegate.inter.IAuditTaskDelegate;
import com.epoint.common.util.ValidateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.auditorga.auditarea.domain.AuditOrgaArea;
import com.epoint.basic.auditorga.auditarea.inter.IAuditOrgaArea;
import com.epoint.basic.auditsp.auditspbasetask.inter.IAuditSpBasetask;
import com.epoint.basic.auditsp.auditspbasetaskr.domain.AuditSpBasetaskR;
import com.epoint.basic.auditsp.auditspbasetaskr.inter.IAuditSpBasetaskR;
import com.epoint.basic.auditsp.auditspbusiness.domain.AuditSpBusiness;
import com.epoint.basic.auditsp.auditspbusiness.inter.IAuditSpBusiness;
import com.epoint.basic.auditsp.auditspshareoption.api.IAuditSpShareoption;
import com.epoint.basic.auditsp.auditspshareoption.domain.AuditSpShareoption;
import com.epoint.basic.auditsp.auditsptask.domain.AuditSpTask;
import com.epoint.basic.auditsp.auditsptask.inter.IAuditSpTask;
import com.epoint.basic.auditsp.element.domain.AuditSpElement;
import com.epoint.basic.auditsp.element.inter.IAuditSpElementService;
import com.epoint.basic.auditsp.option.domain.AuditSpOption;
import com.epoint.basic.auditsp.option.inter.IAuditSpOptionService;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.audittask.element.domain.AuditTaskElement;
import com.epoint.basic.audittask.element.inter.IAuditTaskElementService;
import com.epoint.basic.audittask.material.domain.AuditTaskMaterial;
import com.epoint.basic.audittask.material.inter.IAuditTaskMaterial;
import com.epoint.basic.audittask.option.domain.AuditTaskOption;
import com.epoint.basic.audittask.option.inter.IAuditTaskOptionService;
import com.epoint.basic.authentication.UserSession;
import com.epoint.basic.controller.BaseController;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.json.JsonUtil;
import com.epoint.core.utils.string.StringUtil;

/**
 * 事项的情形list页面对应的后台
 * 
 * @author Administrator
 * @version [版本号, 2016-10-14 16:13:58]
 */
@RestController("audittcnaotuaction")
@Scope("request")
public class AuditTcNaotuAction extends BaseController
{

    /**
     * 
     */
    private static final long serialVersionUID = -1532694550877407562L;

    @Autowired
    IAuditSpBusiness iauditspbusiness;

    @Autowired
    IAuditSpTask iauditsptask;

    @Autowired
    IAuditSpBasetask iauditspbasetask;

    @Autowired
    IAuditSpBasetaskR iauditspbasetaskr;

    @Autowired
    IAuditTask iaudittask;

    @Autowired
    IAuditTaskElementService iaudittaskelementservice;

    @Autowired
    IAuditTaskOptionService iaudittaskoptionservice;

    @Autowired
    IAuditTaskMaterial iaudittaskmaterial;

    @Autowired
    IAuditSpElementService iauditspelementservice;

    @Autowired
    IAuditSpOptionService iauditspoptionservice;

    @Autowired
    IAuditSpShareoption iauditspshareoption;

    @Autowired
    IAuditOrgaArea iauditorgaarea;

    @Autowired
    IAuditSpOptiontownshipService iauditspoptiontownshipservice;
    @Autowired
    IAuditTaskDelegate iaudittaskdelegate;

    @Override
    public void pageLoad() {
    }

    public void showNaotuData() {
        String guid = getRequestParameter("guid");
        Map<String, List<String>> enableelement = new HashMap<>();
        AuditSpBusiness auditspbusiness = iauditspbusiness.getAuditSpBusinessByRowguid(guid).getResult();
        JSONObject rtnjson = new JSONObject();
        Map<String, Object> rootmap = new HashMap<>();
        Map<String, Object> datamap = new HashMap<>();
        Map<String, Object> statusmap = new HashMap<>();
        List<Map<String, Object>> elementlist = new ArrayList<>();
        SqlConditionUtil sqlc = new SqlConditionUtil();
        List<String> alltaskids = new ArrayList<>();
        // 查找当前阶段关联的事项
        List<AuditSpTask> auditsptasklist = iauditsptask.getAllAuditSpTaskByBusinessGuid(guid).getResult();
        if (auditsptasklist != null && !auditsptasklist.isEmpty()) {
            List<String> basetaskguid = auditsptasklist.stream().map(a -> a.getBasetaskguid())
                    .collect(Collectors.toList());
            // 查询basetaskguid关联的taskid
            sqlc.in("basetaskguid", StringUtil.joinSql(basetaskguid));
            List<AuditSpBasetaskR> basetaskrlist = iauditspbasetaskr.getAuditSpBasetaskrByCondition(sqlc.getMap())
                    .getResult();
            alltaskids = basetaskrlist.stream().map(a -> a.getTaskid()).collect(Collectors.toList());
        }
        // 选办事项
        sqlc.clear();
        sqlc.eq("businessguid", guid);
        sqlc.rightLike("preoptionguid", "start");
        sqlc.setOrderDesc("ordernum");
        sqlc.setOrderAsc("operatedate");
        List<AuditSpElement> elelist = iauditspelementservice.findListByCondition(sqlc.getMap()).getResult();
        if (!elelist.isEmpty()) {
            for (int i = 0; i < elelist.size(); i++) {
                AuditSpElement auditTaskElement = elelist.get(i);
                Map<String, Object> elemap = new HashMap<>();
                Map<String, Object> eledatamap = new HashMap<>();
                eledatamap.put("id", auditTaskElement.getRowguid());
                if (ZwfwConstant.CONSTANT_STR_ONE.equals(auditTaskElement.getMultiselect())) {
                    eledatamap.put("progress", 2);
                }
                else {
                    eledatamap.put("progress", 1);
                }
                eledatamap.put("note", auditTaskElement.getElementnote());
                eledatamap.put("text", auditTaskElement.getElementname());
                nodeStyletc(eledatamap, 1);
                elemap.put("data", eledatamap);
                List<AuditSpOption> optlist = iauditspoptionservice
                        .findListByElementIdAndTaskid(auditTaskElement.getRowguid()).getResult();
                List<Map<String, Object>> optionlist = new ArrayList<>();
                for (int j = 0; j < optlist.size(); j++) {
                    AuditSpOption auditTaskOption = optlist.get(j);
                    Map<String, Object> optmap = new HashMap<>();
                    Map<String, Object> optdatamap = new HashMap<>();
                    optdatamap.put("id", auditTaskOption.getRowguid());
                    optdatamap.put("note", auditTaskOption.getOptionnote());
                    optdatamap.put("text", auditTaskOption.getOptionname());
                    if (StringUtil.isNotBlank(auditTaskOption.getStr("Taskelementguid"))) {
                        enableelement.put(auditTaskOption.getRowguid(),
                                Arrays.asList(auditTaskOption.getStr("Taskelementguid").split(";")));
                    }
                    nodeStyletc(optdatamap, 2);
                    optmap.put("data", optdatamap);
                    String materialids = auditTaskOption.getTaskid();
                    List<Map<String, Object>> materiallist = new ArrayList<>();
                    if (StringUtil.isNotBlank(materialids)) {
                        dealTasknode(materiallist, materialids, alltaskids, auditTaskOption);
                    }

                    // optmap.put("children", materiallist);
                    List<Map<String, Object>> childlisttemp = new ArrayList<>();
                    List<Map<String, Object>> childlist = new ArrayList<>();
                    childlist = getAllNaotuChildElement(auditTaskOption.getRowguid(), childlisttemp, guid, alltaskids);
                    materiallist.addAll(childlist);
                    optmap.put("children", materiallist);
                    optionlist.add(optmap);
                }
                elemap.put("children", optionlist);
                elementlist.add(elemap);
            }
        }

        // 查出必办事项
        sqlc.clear();
        sqlc.eq("businessguid", guid);
        sqlc.eq("elementguid", "root");
        List<AuditSpOption> list = iauditspoptionservice.findListByCondition(sqlc.getMap()).getResult();

        // 必须材料分类展示出来
        List<Map<String, Object>> rootlist = new ArrayList<>();
        Map<String, Object> necessitymaterial = new HashMap<>();
        Map<String, Object> necessitymaterialdata = new HashMap<>();
        necessitymaterialdata.put("id", "tcnecessity");
        necessitymaterialdata.put("text", "必办事项");
        necessitymaterialdata.put("nodeType", 0);
        necessitymaterialdata.put("allowremove", 1);
        necessitymaterial.put("data", necessitymaterialdata);
        if (!list.isEmpty()) {
            List<Map<String, Object>> materiallist = new ArrayList<>();
            String materialids = list.get(0).getTaskid();
            if (StringUtil.isNotBlank(materialids)) {
                dealTasknode(materiallist, materialids, alltaskids, list.get(0));
            }
            necessitymaterial.put("children", materiallist);

        }
        else {
            necessitymaterial.put("children", new ArrayList<>());
        }
        rootlist.add(necessitymaterial);

        // 非必须
        Map<String, Object> nnecessitymaterial = new HashMap<>();
        Map<String, Object> nnecessitymaterialdata = new HashMap<>();
        nnecessitymaterialdata.put("text", "情形事项");
        nnecessitymaterialdata.put("id", "tcnnecessity");
        nnecessitymaterialdata.put("nodeType", 0);
        nnecessitymaterialdata.put("allowremove", 1);
        nnecessitymaterial.put("data", nnecessitymaterialdata);
        nnecessitymaterial.put("children", elementlist);
        rootlist.add(nnecessitymaterial);

        datamap.put("id", guid);
        datamap.put("text", auditspbusiness.getBusinessname());
        datamap.put("allowremove", 1);
        rootmap.put("data", datamap);
        rootmap.put("children", rootlist);
        statusmap.put("code", 200);
        rtnjson.put("root", rootmap);
        rtnjson.put("template", "right");
        rtnjson.put("theme", "fresh-blue-compat");
        rtnjson.put("status", statusmap);
        // 查找數據
        List<String> defaultsel = new ArrayList<>();
        Map<String, String> sharesel = new HashMap<>();
        List<AuditSpShareoption> auditspshareoptionlist = iauditspshareoption.getlistBybusinessguid(guid).getResult();
        if (!auditspshareoptionlist.isEmpty()) {
            defaultsel = auditspshareoptionlist.stream()
                    .filter(a -> a.getSelecttype().equals(ZwfwConstant.CONSTANT_STR_ONE))
                    .map(AuditSpShareoption::getOptionguid).collect(Collectors.toList());
            List<AuditSpShareoption> shareselarr = auditspshareoptionlist.stream()
                    .filter(a -> a.getSelecttype().equals(ZwfwConstant.CONSTANT_STR_TWO)).collect(Collectors.toList());
            for (AuditSpShareoption auditSpShareoption : shareselarr) {
                sharesel.put(auditSpShareoption.getOptionguid(), auditSpShareoption.getSharevalue());
            }
        }
        this.addCallbackParam("data", rtnjson);
        this.addCallbackParam("defaultsel", defaultsel);
        this.addCallbackParam("sharesel", sharesel);
        this.addCallbackParam("enableelement", enableelement);
    }

    private void dealTasknode(List<Map<String, Object>> materiallist, String materialids, List<String> alltaskids,
            AuditSpOption auditTaskOption) {
        String[] material = materialids.split(";");
        boolean needupdate = false;
        for (int k = 0; k < material.length; k++) {
            // 判断事项是否已经删除
            if (!alltaskids.contains(material[k])) {
                materialids.replace(material[k] + ";", "");
                needupdate = true;
                continue;
            }
            AuditTask audittask = iaudittask.selectUsableTaskByTaskID(material[k]).getResult();
            if (audittask != null) {
                SqlConditionUtil sqlutil = new SqlConditionUtil();
                sqlutil.eq("taskid", material[k]);
                sqlutil.eq("optionguid", auditTaskOption.getRowguid());
                // 根据选项id以及事项id获取下放到乡镇的事项
                List<AuditSpOptiontownship> shiptList = iauditspoptiontownshipservice
                        .findListByCondition(sqlutil.getMap());
                if (ValidateUtil.isNotBlankCollection(shiptList)) {
                    for (AuditSpOptiontownship auditSpOptiontownship : shiptList) {
                        Map<String, Object> matermap = new HashMap<>();
                        Map<String, Object> materdatamap = new HashMap<>();
                        String areacode = auditSpOptiontownship.getTownshipcode();
                        String taskid = auditSpOptiontownship.getTaskid();
                        sqlutil.clear();
                        sqlutil.eq("xiaqucode", areacode);
                        AuditOrgaArea area = iauditorgaarea.getAuditArea(sqlutil.getMap()).getResult();
                        materdatamap.put("id", areacode + "_" + taskid);
                        materdatamap.put("text", "(" + area.getXiaquname() + ")" + audittask.getTaskname());
                        nodeStyletc(materdatamap, 4);
                        matermap.put("data", materdatamap);
                        materiallist.add(matermap);
                    }
                }
                // 老事项数据展示
                else {
                    Map<String, Object> matermap = new HashMap<>();
                    Map<String, Object> materdatamap = new HashMap<>();
                    String areacode = audittask.getAreacode();
                    String taskid = audittask.getTask_id();
                    sqlutil.clear();
                    sqlutil.eq("xiaqucode", areacode);
                    AuditOrgaArea area = iauditorgaarea.getAuditArea(sqlutil.getMap()).getResult();
                    materdatamap.put("id", areacode + "_" + taskid);
                    materdatamap.put("text", "(" + area.getXiaquname() + ")" + audittask.getTaskname());
                    nodeStyletc(materdatamap, 4);
                    matermap.put("data", materdatamap);
                    materiallist.add(matermap);
                }
            }

        }
        // 如果存在删除了的事项，则进行更新操作
        if (needupdate) {
            auditTaskOption.setTaskid(materialids);
            iauditspoptionservice.update(auditTaskOption);
        }
    }

    public List<Map<String, Object>> getAllNaotuChildElement(String optionguid, List<Map<String, Object>> childlist,
            String guid, List<String> alltaskids) {
        List<Map<String, Object>> elementlist = new ArrayList<>();
        List<AuditSpElement> templist = iauditspelementservice.findListByPreoptionGuid(optionguid).getResult();
        for (int t = 0; t < templist.size(); t++) {
            if (templist.get(t) != null) {
                Map<String, Object> elemap = new HashMap<>();
                Map<String, Object> eledatamap = new HashMap<>();
                eledatamap.put("id", templist.get(t).getRowguid());
                eledatamap.put("note", templist.get(t).getElementnote());
                eledatamap.put("text", templist.get(t).getElementname());
                if (ZwfwConstant.CONSTANT_STR_ONE.equals(templist.get(t).getMultiselect())) {
                    eledatamap.put("progress", 2);
                }
                else {
                    eledatamap.put("progress", 1);
                }
                nodeStyletc(eledatamap, 1);
                elemap.put("data", eledatamap);
                List<AuditSpOption> optlist = iauditspoptionservice
                        .findListByElementIdAndTaskid(templist.get(t).getRowguid()).getResult();
                List<Map<String, Object>> optionlist = new ArrayList<>();
                for (int j = 0; j < optlist.size(); j++) {
                    AuditSpOption auditTaskOption = optlist.get(j);
                    Map<String, Object> optmap = new HashMap<>();
                    Map<String, Object> optdatamap = new HashMap<>();

                    optdatamap.put("id", auditTaskOption.getRowguid());
                    optdatamap.put("note", auditTaskOption.getOptionnote());
                    optdatamap.put("text", auditTaskOption.getOptionname());
                    nodeStyletc(optdatamap, 2);
                    optmap.put("data", optdatamap);
                    String materialids = auditTaskOption.getTaskid();
                    List<Map<String, Object>> materiallist = new ArrayList<>();
                    if (StringUtil.isNotBlank(materialids)) {
                        dealTasknode(materiallist, materialids, alltaskids, auditTaskOption);
                    }
                    // optmap.put("chileren", materiallist);
                    List<Map<String, Object>> childlisttemp = new ArrayList<>();
                    List<Map<String, Object>> childrenlist = new ArrayList<>();
                    childrenlist = getAllNaotuChildElement(auditTaskOption.getRowguid(), childlisttemp, guid,
                            alltaskids);
                    materiallist.addAll(childrenlist);
                    optmap.put("children", materiallist);
                    optionlist.add(optmap);
                }
                elemap.put("children", optionlist);
                elementlist.add(elemap);
            }
        }
        return elementlist;
    }

    // 节点颜色设置 style 1 为条件 2 为选项 3为材料,4为事项
    public void nodeStyletc(Map<String, Object> node, Integer nodetype) {
        node.put("nodeType", nodetype);
        node.put("color", "#5c5c5c");
        node.put("priority", nodetype);
        switch (nodetype) {
            case 1:
                node.put("background", "#e7f3fc");
                break;
            case 2:
                node.put("background", "#f1edfb");
                break;
            case 3:
                node.put("background", "#e9f7ef");
                break;
            case 4:
                node.put("background", "#f3ccbfbd");
                break;
            default:
                break;
        }
    }

    // 节点颜色设置 style 1 为条件 2 为选项 3为材料
    public void nodeStyle(Map<String, Object> node, Integer nodetype) {
        node.put("nodeType", nodetype);
        node.put("color", "#5c5c5c");
        node.put("priority", nodetype);
        switch (nodetype) {
            case 1:
                node.put("background", "#e7f3fc");
                break;
            case 2:
                node.put("background", "#f1edfb");
                break;
            case 3:
                node.put("background", "#e9f7ef");
                break;
            default:
                break;
        }
        node.put("allowremove", 1);
    }

    public void showTaskList() {
        try {
            // 查询所有市级的areacode，和当前辖区
            SqlConditionUtil sqlc = new SqlConditionUtil();
            sqlc.eq("citylevel", ZwfwConstant.CONSTANT_STR_ONE);
            List<AuditOrgaArea> listarea = iauditorgaarea.selectAuditAreaList(sqlc.getMap()).getResult();
            List<String> sjcode;
            // 查询所有市级的areacode，和当前辖区
            //sjcode = iauditorgaarea.getAllAreacodeByAreacode(ZwfwUserSession.getInstance().getAreaCode()).getResult();

            String guid = getRequestParameter("guid");
            JSONObject rtnjson = new JSONObject();
            JSONArray sptasklist = new JSONArray();
            rtnjson.put("list", sptasklist);
            List<AuditSpTask> sptasks = iauditsptask.getAllAuditSpTaskByBusinessGuid(guid).getResult();
            sqlc.clear();
            for (AuditSpTask auditSpTask : sptasks) {
                JSONObject sptaskobj = new JSONObject();
                sptaskobj.put("basetaskname", auditSpTask.getTaskname());
                sptaskobj.put("guid", auditSpTask.getBasetaskguid());
                sptaskobj.put("child", true);
                // 查询子事项
                sqlc.clear();
                sqlc.eq("basetaskguid", auditSpTask.getBasetaskguid());
               // sqlc.in("areacode", StringUtil.joinSql(sjcode));
                List<AuditSpBasetaskR> taskrs = iauditspbasetaskr.getAuditSpBasetaskrByCondition(sqlc.getMap())
                        .getResult();
                if (!taskrs.isEmpty()) {
                    // 添加标准事项
                    sptasklist.add(sptaskobj);
                    JSONArray sptaskrlist = new JSONArray();
                    // 添加子项列表
                    sptaskobj.put("childList", sptaskrlist);
                    for (AuditSpBasetaskR basetaskr : taskrs) {
                        Record audittask = iaudittask.selectUsableTaskByTaskID(basetaskr.getTaskid()).getResult();
                        if (audittask != null) {
                            JSONObject basetaskrobj = new JSONObject();
                            String areacode = basetaskr.getAreacode();
                            StringBuilder taskname = new StringBuilder(audittask.get("taskname"));
                            taskname.insert(0, "【" + basetaskr.getXiaquname() + "】");
                            basetaskrobj.put("taskname", taskname.toString());
                            String cguid = areacode + "_" + basetaskr.getTaskid();
                            basetaskrobj.put("cguid", cguid);
                            // 添加具体事项
                            sptaskrlist.add(basetaskrobj);
                            // 市级的areacode长度为6
                            if (areacode.length() == 6) {
                                // 如果当前事项被下放到乡镇，将进一步筛选isallowaccept为1的数据
                                List<AuditTaskDelegate> delegateList = iaudittaskdelegate
                                        .selectDelegateByTaskID(basetaskr.getTaskid()).getResult();
                                if (ValidateUtil.isNotBlankCollection(delegateList)) {
                                    delegateList.removeIf(a -> !"1".equals(a.getIsallowaccept()));
                                    for (AuditTaskDelegate delegate : delegateList) {
                                        String xiaquName = iauditorgaarea.getAreaByAreacode(delegate.getAreacode())
                                                .getResult().getXiaquname();
                                        JSONObject obj = new JSONObject();
                                        taskname.replace(1, taskname.indexOf("】"), xiaquName);
                                        obj.put("taskname", taskname.toString());
                                        obj.put("cguid", delegate.getAreacode() + "_" + basetaskr.getTaskid());
                                        // 添加具体事项
                                        sptaskrlist.add(obj);
                                    }
                                }
                            }
                        }
                    }

                }
            }
            addCallbackParam("data", rtnjson);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void getNaotuDataBytaskids(String taskids) {
        if (StringUtil.isNotBlank(taskids)) {
            String[] taskarr = taskids.split(";");
            JSONObject resultjson = new JSONObject();
            for (String string : taskarr) {
                String taskid = string.split("_")[1];
                resultjson.put(string, showNaotuDataBytaskids(taskid));
            }
            addCallbackParam("result", resultjson);
        }

    }

    public List<Map<String, Object>> showNaotuDataBytaskids(String taskid) {
        List<Map<String, Object>> root = new ArrayList<>();
        if (StringUtil.isBlank(taskid)) {
            return root;
        }
        AuditTask audittask = iaudittask.selectUsableTaskByTaskID(taskid).getResult();
        if (audittask == null) {
            return root;
        }
        String taskguid = audittask.getRowguid();
        List<Map<String, Object>> elementlist = new ArrayList<>();
        SqlConditionUtil sqlc = new SqlConditionUtil();
        sqlc.eq("taskid", taskid);
        sqlc.rightLike("preoptionguid", "start");
        sqlc.isBlankOrValue("draft", ZwfwConstant.CONSTANT_STR_ZERO);
        sqlc.setOrderDesc("ordernum");
        sqlc.setOrderAsc("operatedate");
        List<AuditTaskElement> elelist = iaudittaskelementservice.findListByCondition(sqlc.getMap()).getResult();

        if (!elelist.isEmpty()) {
            for (int i = 0; i < elelist.size(); i++) {
                AuditTaskElement auditTaskElement = elelist.get(i);

                Map<String, Object> elemap = new HashMap<>();
                Map<String, Object> eledatamap = new HashMap<>();
                eledatamap.put("id", auditTaskElement.getRowguid());
                if (ZwfwConstant.CONSTANT_STR_ONE.equals(auditTaskElement.getMultiselect())) {
                    eledatamap.put("progress", 2);
                }
                else {
                    eledatamap.put("progress", 1);
                }
                if (StringUtil.isNotBlank(auditTaskElement.getElementnote())) {
                    eledatamap.put("note", auditTaskElement.getElementnote());
                }
                eledatamap.put("text", auditTaskElement.getElementname());
                nodeStyle(eledatamap, 1);
                elemap.put("data", eledatamap);
                List<AuditTaskOption> optlist = iaudittaskoptionservice
                        .findListByElementIdAndTaskid(auditTaskElement.getRowguid()).getResult();
                List<Map<String, Object>> optionlist = new ArrayList<>();
                for (int j = 0; j < optlist.size(); j++) {
                    AuditTaskOption auditTaskOption = optlist.get(j);
                    Map<String, Object> optmap = new HashMap<>();
                    Map<String, Object> optdatamap = new HashMap<>();
                    optdatamap.put("id", auditTaskOption.getRowguid());
                    if (StringUtil.isNotBlank(auditTaskOption.getOptionnote())) {
                        optdatamap.put("note", auditTaskOption.getOptionnote());
                    }
                    optdatamap.put("text", auditTaskOption.getOptionname());
                    nodeStyle(optdatamap, 2);
                    optmap.put("data", optdatamap);
                    String materialids = auditTaskOption.getMaterialids();
                    List<Map<String, Object>> materiallist = new ArrayList<>();
                    if (StringUtil.isNotBlank(materialids)) {
                        String[] material = materialids.split(";");
                        for (int k = 0; k < material.length; k++) {
                            Map<String, Object> matermap = new HashMap<>();
                            Map<String, Object> materdatamap = new HashMap<>();
                            AuditTaskMaterial auditTaskMaterial = iaudittaskmaterial
                                    .selectTaskMaterialByTaskGuidAndMaterialId(taskguid, material[k]).getResult();
                            if (auditTaskMaterial != null) {
                                materdatamap.put("id", material[k]);
                                materdatamap.put("text", auditTaskMaterial.getMaterialname());
                                nodeStyle(materdatamap, 3);
                                matermap.put("data", materdatamap);
                                materiallist.add(matermap);
                            }
                        }
                    }

                    // optmap.put("children", materiallist);
                    List<Map<String, Object>> childlisttemp = new ArrayList<>();
                    List<Map<String, Object>> childlist = new ArrayList<>();
                    childlist = getAllNaotuChildElement(auditTaskOption.getRowguid(), childlisttemp, taskid, taskguid);
                    materiallist.addAll(childlist);
                    optmap.put("children", materiallist);
                    optionlist.add(optmap);
                }
                elemap.put("children", optionlist);
                elementlist.add(elemap);
            }
        }
        // 必须材料分类展示出来
        List<Map<String, Object>> rootlist = new ArrayList<>();
        Map<String, Object> necessitymaterial = new HashMap<>();
        Map<String, Object> necessitymaterialdata = new HashMap<>();
        necessitymaterialdata.put("id", "necessitymaterial");
        necessitymaterialdata.put("text", "基础材料");
        necessitymaterialdata.put("nodeType", 0);
        necessitymaterialdata.put("allowremove", 1);
        necessitymaterial.put("data", necessitymaterialdata);

        List<AuditTaskMaterial> necessitymateriallist = iaudittaskmaterial
                .selectTaskMaterialListByTaskGuid(audittask.getRowguid(), false).getResult();
        List<String> pidlist = necessitymateriallist.stream().map(AuditTaskMaterial::getPid)
                .filter(a -> StringUtil.isNotBlank(a)).collect(Collectors.toList());
        // 过滤掉必须并且没有子项的
        necessitymateriallist.removeIf(a -> {
            if (Integer.valueOf(ZwfwConstant.NECESSITY_SET_YES).equals(a.getNecessity())
                    && !pidlist.contains(a.getMaterialid())) {
                return false;
            }
            else {
                return true;
            }
        });

        List<Map<String, Object>> materiallist = new ArrayList<>();
        if (necessitymateriallist != null && !necessitymateriallist.isEmpty()) {
            for (AuditTaskMaterial auditTaskMaterial : necessitymateriallist) {
                Map<String, Object> materialdata = new HashMap<>();
                Map<String, Object> material = new HashMap<>();
                materialdata.put("id", auditTaskMaterial.getMaterialid());
                materialdata.put("text", auditTaskMaterial.getMaterialname());
                // 设置不能删除
                materialdata.put("allowremove", 1);
                nodeStyle(materialdata, 3);
                material.put("data", materialdata);
                materiallist.add(material);
            }

        }
        necessitymaterial.put("children", materiallist);
        rootlist.add(necessitymaterial);

        // 非必须
        Map<String, Object> nnecessitymaterial = new HashMap<>();
        Map<String, Object> nnecessitymaterialdata = new HashMap<>();
        nnecessitymaterialdata.put("text", "情形材料");
        nnecessitymaterialdata.put("id", "nnecessitymaterial");
        nnecessitymaterialdata.put("nodeType", 0);
        nnecessitymaterialdata.put("allowremove", 1);
        nnecessitymaterial.put("data", nnecessitymaterialdata);
        nnecessitymaterial.put("children", elementlist);
        rootlist.add(nnecessitymaterial);
        return rootlist;
    }

    public List<Map<String, Object>> getAllNaotuChildElement(String optionguid, List<Map<String, Object>> childlist,
            String taskId, String copyTaskGuid) {
        List<Map<String, Object>> elementlist = new ArrayList<>();
        List<AuditTaskElement> templist = iaudittaskelementservice.findListByPreoptionGuidAndTaskId(optionguid, taskId)
                .getResult();
        // 去除删除数据
        templist.removeIf(a -> {
            return a.getIsDelete() != null && a.getIsDelete() == ZwfwConstant.CONSTANT_INT_ONE;
        });
        for (int t = 0; t < templist.size(); t++) {
            if (templist.get(t) != null) {
                Map<String, Object> elemap = new HashMap<>();
                Map<String, Object> eledatamap = new HashMap<>();
                eledatamap.put("id", templist.get(t).getRowguid());
                if (StringUtil.isNotBlank(templist.get(t).getElementnote())) {
                    eledatamap.put("note", templist.get(t).getElementnote());
                }
                eledatamap.put("text", templist.get(t).getElementname());
                if (ZwfwConstant.CONSTANT_STR_ONE.equals(templist.get(t).getMultiselect())) {
                    eledatamap.put("progress", 2);
                }
                else {
                    eledatamap.put("progress", 1);
                }
                nodeStyle(eledatamap, 1);
                elemap.put("data", eledatamap);
                List<AuditTaskOption> optlist = iaudittaskoptionservice
                        .findListByElementIdAndTaskid(templist.get(t).getRowguid()).getResult();
                List<Map<String, Object>> optionlist = new ArrayList<>();
                for (int j = 0; j < optlist.size(); j++) {
                    AuditTaskOption auditTaskOption = optlist.get(j);
                    Map<String, Object> optmap = new HashMap<>();
                    Map<String, Object> optdatamap = new HashMap<>();

                    optdatamap.put("id", auditTaskOption.getRowguid());
                    optdatamap.put("note", auditTaskOption.getOptionnote());
                    optdatamap.put("text", auditTaskOption.getOptionname());
                    nodeStyle(optdatamap, 2);
                    optmap.put("data", optdatamap);
                    String materialids = auditTaskOption.getMaterialids();
                    List<Map<String, Object>> materiallist = new ArrayList<>();
                    if (StringUtil.isNotBlank(materialids)) {
                        String[] material = materialids.split(";");
                        for (int k = 0; k < material.length; k++) {
                            Map<String, Object> matermap = new HashMap<>();
                            Map<String, Object> materdatamap = new HashMap<>();
                            AuditTaskMaterial auditTaskMaterial = iaudittaskmaterial
                                    .selectTaskMaterialByTaskGuidAndMaterialId(copyTaskGuid, material[k]).getResult();
                            if (auditTaskMaterial != null) {
                                materdatamap.put("id", material[k]);
                                materdatamap.put("text", auditTaskMaterial.getMaterialname());
                                nodeStyle(materdatamap, 3);
                                matermap.put("data", materdatamap);
                                materiallist.add(matermap);
                            }
                        }
                    }
                    // optmap.put("chileren", materiallist);
                    List<Map<String, Object>> childlisttemp = new ArrayList<>();
                    List<Map<String, Object>> childrenlist = new ArrayList<>();
                    childrenlist = getAllNaotuChildElement(auditTaskOption.getRowguid(), childlisttemp, taskId,
                            copyTaskGuid);
                    materiallist.addAll(childrenlist);
                    optmap.put("children", materiallist);
                    optionlist.add(optmap);
                }
                elemap.put("children", optionlist);
                elementlist.add(elemap);
            }
        }
        return elementlist;
    }

    @SuppressWarnings("rawtypes")
    public void savetcnaotu() {
        String nodesJson = getRequestParameter("nodes");
        String defaultsel = getRequestParameter("defaultsel");
        String sharesel = getRequestParameter("sharesel");
        String guid = getRequestParameter("guid");
        SqlConditionUtil sqlc = new SqlConditionUtil();
        sqlc.eq("businessguid", guid);
        List<AuditSpElement> liste = iauditspelementservice.findListByCondition(sqlc.getMap()).getResult();
        sqlc.clear();
        sqlc.eq("businessguid", guid);
        List<AuditSpOption> listo = iauditspoptionservice.findListByCondition(sqlc.getMap()).getResult();
        // 将问题和选项转换成map,记录没有改变的记录
        mape = liste.stream().collect(Collectors.toMap(AuditSpElement::getRowguid, AuditSpElement -> AuditSpElement));
        // 将问题和选项转换成map,记录没有改变的记录
        mapo = listo.stream().collect(Collectors.toMap(AuditSpOption::getRowguid, AuditSpOption -> AuditSpOption));

        iauditspelementservice.deleteByOneField("businessguid", guid);
        iauditspoptionservice.deleteByOneField("businessguid", guid);
        iauditspshareoption.delByOneField("businessguid", guid);
        iauditspoptiontownshipservice.deleteByBusinessGuid(guid);

        // 插人默认选择
        if (JsonUtil.isJSONValid(defaultsel)) {
            JSONArray defaultselarr = JSONArray.parseArray(defaultsel);
            for (int i = 0; i < defaultselarr.size(); i++) {
                AuditSpShareoption auditspshareoption = new AuditSpShareoption();
                auditspshareoption.setOperatedate(new Date());
                auditspshareoption.setRowguid(UUID.randomUUID().toString());
                auditspshareoption.setSelecttype(ZwfwConstant.CONSTANT_STR_ONE);
                auditspshareoption.setOptionguid(defaultselarr.getString(i));
                auditspshareoption.setBusinessguid(guid);
                iauditspshareoption.insert(auditspshareoption);
            }

        }
        if (JsonUtil.isJSONValid(sharesel)) {
            JSONObject shareselarr = JSONObject.parseObject(sharesel);
            Set<String> set = shareselarr.keySet();
            for (String optionguid : set) {
                String sharevalue = shareselarr.getString(optionguid);
                AuditSpShareoption auditspshareoption = new AuditSpShareoption();
                auditspshareoption.setOperatedate(new Date());
                auditspshareoption.setRowguid(UUID.randomUUID().toString());
                auditspshareoption.setSelecttype(ZwfwConstant.CONSTANT_STR_TWO);
                auditspshareoption.setOptionguid(optionguid);
                auditspshareoption.setBusinessguid(guid);
                auditspshareoption.setSharevalue(sharevalue);
                iauditspshareoption.insert(auditspshareoption);
            }
        }

        boolean isjson = JsonUtil.isJSONValid(nodesJson);
        if (isjson) {
            HashMap nodesMap = (HashMap) JsonUtil.jsonToMap(nodesJson);
            saveNode((HashMap) nodesMap.get("root"), guid, "", 0);
        }
    }

    private Map<String, AuditSpElement> mape;
    private Map<String, AuditSpOption> mapo;

    @SuppressWarnings({"rawtypes", "unchecked" })
    private void saveNode(HashMap nodeMap, String guid, String preGuid, Integer order) {
        HashMap dataNode = (HashMap) nodeMap.get("data");
        // 如果节点是红色，为前台没验证通过的
        if ("red".equals(dataNode.get("color"))) {
            return;
        }
        List<Object> childrenNode = (List<Object>) nodeMap.get("children");
        // 这里判断node的类型，并将data中的数据写入数据库
        switch (StringUtil.isBlank(dataNode.get("nodeType")) ? "" : dataNode.get("nodeType").toString()) {
            // 条件
            case "1":
            {
                String rowguid = dataNode.get("id").toString();
                String multi = "0";
                if ("2".equals(String.valueOf(dataNode.get("progress")))) {
                    multi = "1";
                }
                // 查找rowguid是否在历史记录
                AuditSpElement element = mape.get(rowguid);
                if (element == null) {
                    element = new AuditSpElement();
                }
                element.setOperatedate(new Date());
                element.setOperateusername(UserSession.getInstance().getDisplayName());
                element.setRowguid(rowguid);
                element.setBusinessguid(guid);
                element.setOrdernum(order);
                if (StringUtil.isNotBlank(dataNode.get("note"))) {
                    element.setElementnote(String.valueOf(dataNode.get("note")));
                }
                element.setElementname(String.valueOf(dataNode.get("text")));
                element.setMultiselect(multi);
                element.setDraft(ZwfwConstant.CONSTANT_STR_ONE);
                if (StringUtil.isNotBlank(preGuid)) {
                    element.setPreoptionguid(preGuid);
                }
                else {
                    element.setPreoptionguid("start");
                }
                // 不插入基础材料阶段
                if (!"tcnnecessity".equals(rowguid)) {
                    iauditspelementservice.insert(element);
                }
                // 处理完条件，再处理条件下的选项
                childrenNode.forEach(node -> {
                    saveNode((HashMap) JsonUtil.jsonToMap(node), guid, "tcnnecessity".equals(rowguid) ? "" : rowguid,
                            childrenNode.size() - childrenNode.indexOf(node));
                });
                break;
            }
            // 选项
            case "2":
            {
                String rowguid = dataNode.get("id").toString();
                StringBuilder materialIds = new StringBuilder("");
                AuditSpOption option = mapo.get(rowguid);
                if (option == null) {
                    option = new AuditSpOption();
                }
                option.setOperatedate(new Date());
                option.setOperateusername(UserSession.getInstance().getDisplayName());
                option.setRowguid(rowguid);
                option.setBusinessguid(guid);
                if (StringUtil.isNotBlank(dataNode.get("note"))) {
                    option.setOptionnote(String.valueOf(dataNode.get("note")));
                }
                option.setOptionname(String.valueOf(dataNode.get("text")));
                option.setOrdernum(order);
                // 此处对材料需要单独处理，条件放过
                Iterator<Object> iter = childrenNode.iterator();
                while (iter.hasNext()) {
                    HashMap materialnode = ((HashMap) JsonUtil.jsonToMap(iter.next()).get("data"));
                    String type = materialnode.get("nodeType").toString();
                    if ("4".equals(type)) {
                        String areacode = materialnode.get("id").toString().split("_")[0];
                        String id = materialnode.get("id").toString().split("_")[1];
                        // 保存下放事项的相关信息至audit_sp_optiontownship表中
                        AuditSpOptiontownship township = new AuditSpOptiontownship();
                        township.setRowguid(UUID.randomUUID().toString());
                        township.setOptionguid(option.getRowguid());
                        township.setBusinessguid(option.getBusinessguid());
                        township.setTaskid(id);
                        township.setTownshipcode(areacode);
                        township.setOperatedate(new Date());
                        iauditspoptiontownshipservice.insert(township);
                        if (materialIds.indexOf(id) < 0) {
                            materialIds.append(id + ";");
                        }
                        // 处理过的节点删掉
                        iter.remove();
                    }

                }

                option.setTaskid(materialIds.toString());
                option.setOrdernum(order);
                option.setElementguid(preGuid);
                iauditspoptionservice.insert(option);
                // 处理完条件，再处理条件下的选项
                childrenNode.forEach(node -> {
                    saveNode((HashMap) JsonUtil.jsonToMap(node), guid, rowguid,
                            childrenNode.size() - childrenNode.indexOf(node));
                });
                break;
            }
            // 第一层
            case "":
            {
                // 处理完条件，再处理条件下的选项
                childrenNode.forEach(node -> {
                    saveNode((HashMap) JsonUtil.jsonToMap(node), guid, preGuid,
                            childrenNode.size() - childrenNode.indexOf(node));
                });
                break;
            }
            case "0":
            {
                // 第一个节点需要单独处理
                String rowguid = dataNode.get("id").toString();
                if ("tcnecessity".equals(rowguid)) {
                    StringBuilder materialIds = new StringBuilder("");
                    AuditSpOption option = mapo.get(rowguid);
                    if (option == null) {
                        option = new AuditSpOption();
                    }
                    option.setOperatedate(new Date());
                    option.setOperateusername(UserSession.getInstance().getDisplayName());
                    option.setRowguid(UUID.randomUUID().toString());
                    option.setBusinessguid(guid);
                    // 设置必须
                    option.setOptionname(String.valueOf(dataNode.get("text")));
                    option.setOrdernum(order);
                    // 此处对材料需要单独处理，条件放过
                    Iterator<Object> iter = childrenNode.iterator();
                    while (iter.hasNext()) {
                        HashMap materialnode = ((HashMap) JsonUtil.jsonToMap(iter.next()).get("data"));
                        String type = materialnode.get("nodeType").toString();
                        if ("4".equals(type)) {
                            String areacode = materialnode.get("id").toString().split("_")[0];
                            String id = materialnode.get("id").toString().split("_")[1];
                            // 保存下放事项的相关信息至audit_sp_optiontownship表中
                            AuditSpOptiontownship township = new AuditSpOptiontownship();
                            township.setRowguid(UUID.randomUUID().toString());
                            township.setOptionguid(option.getRowguid());
                            township.setBusinessguid(option.getBusinessguid());
                            township.setTaskid(id);
                            township.setTownshipcode(areacode);
                            township.setOperatedate(new Date());
                            iauditspoptiontownshipservice.insert(township);
                            if (materialIds.indexOf(id) < 0) {
                                materialIds.append(id + ";");
                            }
                            // 处理过的节点删掉
                            iter.remove();
                        }
                    }

                    /*
                     * childrenNode.removeIf(removenode -> { });
                     */
                    option.setTaskid(materialIds.toString());
                    option.setOrdernum(order);
                    option.setElementguid("root");
                    iauditspoptionservice.insert(option);
                    break;
                }
                // 处理完条件，再处理条件下的选项
                childrenNode.forEach(node -> {
                    saveNode((HashMap) JsonUtil.jsonToMap(node), guid, preGuid,
                            childrenNode.size() - childrenNode.indexOf(node));
                });
                break;
            }
            default:
                break;
        }

    }

}
