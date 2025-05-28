package com.epoint.zzlb.auditsptask.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.auditorga.auditarea.domain.AuditOrgaArea;
import com.epoint.basic.auditorga.auditarea.inter.IAuditOrgaArea;
import com.epoint.basic.auditsp.auditspbasetask.domain.AuditSpBasetask;
import com.epoint.basic.auditsp.auditspbasetask.inter.IAuditSpBasetask;
import com.epoint.basic.auditsp.auditspbasetaskr.domain.AuditSpBasetaskR;
import com.epoint.basic.auditsp.auditspbasetaskr.inter.IAuditSpBasetaskR;
import com.epoint.basic.auditsp.auditspbusiness.domain.AuditSpBusiness;
import com.epoint.basic.auditsp.auditspbusiness.inter.IAuditSpBusiness;
import com.epoint.basic.auditsp.auditspphase.domain.AuditSpPhase;
import com.epoint.basic.auditsp.auditspphase.inter.IAuditSpPhase;
import com.epoint.basic.auditsp.auditsptask.domain.AuditSpTask;
import com.epoint.basic.auditsp.auditsptask.inter.IAuditSpTask;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.tree.TreeData;
import com.epoint.basic.faces.tree.TreeFunction9;
import com.epoint.common.service.AuditCommonResult;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.composite.auditorga.handleconfig.inter.IHandleConfig;
import com.epoint.composite.auditorga.handleframeou.IHandleFrameOU;
import com.epoint.composite.auditsp.handlespsharematerial.inter.IHandleSPShareMaterial;
import com.epoint.core.dto.base.TreeNode;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.dto.model.TreeModel;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.organ.ou.api.IOuService;
import com.epoint.frame.service.organ.ou.api.IOuServiceInternal;
import com.epoint.frame.service.organ.ou.entity.FrameOu;
import com.epoint.frame.service.organ.ou.entity.FrameOuExtendInfo;
import com.epoint.yyyz.auditspyyyzmaterial.api.IAuditSpYyyzMaterialService;

/**
 * 阶段事项配置页面对应的后台
 * 
 * @author wry
 * @version [版本号, 2016-09-28 15:18:27]
 */
@RestController("zzlbauditsptaskconfigaction")
@Scope("request")
@SuppressWarnings("serial")
public class ZzlbAuditSpTaskConfigAction extends BaseController
{
    /**
     * 
     */
    private static final long serialVersionUID = -4632640761998959244L;
    /**
     * 窗口与事项的关联实体对象
     */
    private AuditTask dataBean = null;
    /**
     * 窗口部门事项model
     */
    private TreeModel treeModel_Task;
    /**
     * 阶段标识
     */
    private String phaseguid = null;

    private String fields = "rowguid,task_id,type,taskname,ouguid,areacode";
    private String itemcategory= null;
    @Autowired
    private IAuditTask auditTask;
    
    @Autowired
    private IHandleFrameOU frameOu;

    @Autowired
    private IAuditSpTask AuditSpTaskImpl;

    
    @Autowired
    private IAuditSpBusiness IAuditSpBusinessImpl;
    
    @Autowired
    private IAuditOrgaArea auditAreaImpl;

    @Autowired
    private IOuService ouService;
    @Autowired
    private IHandleConfig config;
    
    @Autowired
    private IAuditSpBasetask iauditspbasetask;
    
    @Autowired
    private IAuditSpPhase iauditspphase;
    
    @Autowired
    private IHandleSPShareMaterial spShareMaterialService;
    
    @Autowired
    private IAuditSpBasetask iAuditSpBasetask;
    
    @Autowired
    private IAuditSpBasetaskR iauditspbasetaskr;
    
    @Autowired
    private IAuditOrgaArea auditArea;
    @Autowired
    private IOuServiceInternal ouServiceInternal;
    
    
    @Autowired
    private IAuditSpYyyzMaterialService auditSpYyyzMaterialService;
    private List<String> redlist;

    private List<String> yellowlist;
    


    @Override
    public void pageLoad() {
        phaseguid = getRequestParameter("phaseguid");
        long starttime = System.currentTimeMillis();
        AuditSpPhase phase = iauditspphase.getAuditSpPhaseByRowguid(phaseguid).getResult();
        long endtime = System.currentTimeMillis();
        
        if(phase!=null){
            addCallbackParam("phaseid", phase.getPhaseId());
        }
        itemcategory=config.getFrameConfig("AS_ITEM_CATEGORY", "").getResult();
        redlist = iAuditSpBasetask.getNoAuthorityRowListByAreacode(ZwfwUserSession.getInstance().getAreaCode()).getResult();
        yellowlist = iAuditSpBasetask.getDisabledBasetaskRowList().getResult();
        redlist.removeAll(yellowlist);
        addCallbackParam("redlist", redlist);
        addCallbackParam("yellowlist", yellowlist);
        //system.out.println("查询耗时："+(endtime-starttime));
    }

    /**
     * 根据phaseguid获取事项SelectItem，初始化事项信息
     * 
     * @param phaseguid
     * @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    private List<SelectItem> getSelectTaskPhaseguid(String phaseguid) {
        List<SelectItem> auditTaskList = new ArrayList<SelectItem>();
        if (StringUtil.isNotBlank(phaseguid)) {
            List<AuditSpTask> auditSpList = AuditSpTaskImpl.getAllAuditSpTaskByPhaseguid(phaseguid).getResult();
            if (auditSpList != null && auditSpList.size() > 0) {
                for (AuditSpTask auditsptask : auditSpList) {
                    if(StringUtil.isNotBlank(auditsptask.getBasetaskguid())){
                        AuditSpBasetask auditSpBasetask = iAuditSpBasetask.getAuditSpBasetaskByrowguid(auditsptask.getBasetaskguid()).getResult();
                        if(auditSpBasetask!=null){
                            if(ZwfwConstant.CONSTANT_STR_ONE.equals(auditSpBasetask.getSflcbsx())){                                
                                auditTaskList.add(new SelectItem(auditSpBasetask.getRowguid(),"【里程碑】"+ auditSpBasetask.getTaskname().replace(",", "，")));
                            }else{
                                auditTaskList.add(new SelectItem(auditSpBasetask.getRowguid(), auditSpBasetask.getTaskname().replace(",", "，")));
                            }
                        }else{
                            //标准事项被删除，删除关联关系
                            AuditSpTaskImpl.deleteAuditSpTask(auditsptask);
                        }
                    }else{ //套餐事项
                        AuditTask task = auditTask.selectUsableTaskByTaskID(auditsptask.getTaskid()).getResult();
                        if(task== null){
                            continue;
                        }
                        auditTaskList.add(new SelectItem(task.getTask_id(), task.getTaskname().replace(",", "，")));
                    }
                }
            }
        }
        return auditTaskList;
    }

    public TreeModel getTaskTreeModel() {
        if (treeModel_Task == null) {
            treeModel_Task = new TreeModel()
            {
                /***
                 * 加载树，懒加载
                 */
                @Override
                public List<TreeNode> fetch(TreeNode treeNode) {
                    long starttime = System.currentTimeMillis();
                    TreeData treeData = TreeFunction9.getData(treeNode);
                    List<TreeNode> list = new ArrayList<>();

                    if (treeData == null) {
                        TreeNode root = new TreeNode();
                        root.setText("所有部门");
                        root.setId("root");
                        root.setPid("-1");
                        root.getColumns().put("isOU", "true");//标记：不是部门节点
                        list.add(root);
                        root.setCkr(false);
                        root.setExpanded(true);//展开下一层节点
                        list.addAll(fetch(root));//自动加载下一层树结构
                    }
                    else {
                        if (StringUtil.isNotBlank(treeNode.getSearchCondition())) {
                            //以下是框架封装的方法，具体参数请参见api
                            List<AuditTask> listsearchTask = new ArrayList<AuditTask>();
                            if(StringUtil.isNotBlank(itemcategory) && ZwfwConstant.CONSTANT_STR_ONE.equals(itemcategory) ){
                                SqlConditionUtil sql  = new SqlConditionUtil();
                                sql.eq("is_enable", ZwfwConstant.CONSTANT_STR_ONE);
                                sql.eq("ISTEMPLATE",  ZwfwConstant.CONSTANT_STR_ZERO);
                                sql.like("taskname", treeNode.getSearchCondition());
                                sql.eq("areacode", ZwfwUserSession.getInstance().getAreaCode());
                                sql.isBlankOrValue("IS_HISTORY", ZwfwConstant.CONSTANT_STR_ZERO);
                                listsearchTask = auditTask.getAuditEnableTaskList(fields, sql.getMap(), 0, 0, "ordernum", "desc").getResult();

                            }else{
                                listsearchTask = auditTask
                                        .selectAuditTaskByCondition(treeNode.getSearchCondition(),
                                                ZwfwUserSession.getInstance().getAreaCode())
                                        .getResult();
                            }
                            //搜索完成后，进行树的绑定
                            for (int j = 0; j < listsearchTask.size(); j++) {
                                TreeNode node = new TreeNode();
                                node.setId(listsearchTask.get(j).getTask_id());
                                node.setText(listsearchTask.get(j).getTaskname());
                                node.setPid("-1");
                                node.setLeaf(true);
                                list.add(node);
                            }
                        }
                        else {
                            String objectGuid = treeData.getObjectGuid();

                            List<AuditTask> listTask = new ArrayList<>();
                            
                            
                            List<FrameOu> listOu = new ArrayList<>();
                            SqlConditionUtil sql  = new SqlConditionUtil();
                          
//                            String topOuGuid = "";
//                            if (auditarea != null) {
//                                topOuGuid = auditarea.getOuguid();
//                            }
//                            List<FrameOu> listou = ouService.listOUByGuid(topOuGuid, 3);
                            if("root".equals(objectGuid)){
                                listOu = frameOu.getWindowOU(ZwfwUserSession.getInstance().getAreaCode()).getResult();
                            }else{
                                if(StringUtil.isNotBlank(itemcategory) && ZwfwConstant.CONSTANT_STR_ONE.equals(itemcategory) ){
                                    sql.clear();
                                    sql.eq("is_enable", ZwfwConstant.CONSTANT_STR_ONE);
                                    sql.eq("ISTEMPLATE",  ZwfwConstant.CONSTANT_STR_ZERO);
                                    sql.eq("OUGUID", treeData.getObjectGuid());
                                    sql.eq("areacode", ZwfwUserSession.getInstance().getAreaCode());
                                    sql.isBlankOrValue("IS_HISTORY", ZwfwConstant.CONSTANT_STR_ZERO);
                                    listTask = auditTask.getAuditEnableTaskList(fields, sql.getMap(), 0, 0, "ordernum", "desc").getResult();

                                }else{
                                    FrameOuExtendInfo frameOuExtendInfo = ouServiceInternal.getFrameOuExtendInfo(objectGuid);
                                    if(StringUtil.isNotBlank(frameOuExtendInfo)) {
                                        String areacode = frameOuExtendInfo.getStr("areacode");
                                        if(areacode.equals(ZwfwUserSession.getInstance().getAreaCode())) {
                                            listTask = auditTask.selectAuditTaskOuByObjectGuid(treeData.getObjectGuid(),
                                                    ZwfwUserSession.getInstance().getAreaCode()).getResult();
                                        }
                                        else {
                                            listTask = auditSpYyyzMaterialService.selectAuditTaskOuByObjectGuids(treeData.getObjectGuid(),areacode);
                                        }
                                    }
                                }
                            }
                            String isBusCommonTask = "1";
                            //获取主题共享事项的ouguid的集合
                            List<Record> commonAuditTask = auditSpYyyzMaterialService.getAuditTaskByCommonTask(isBusCommonTask);
                            List<String> ouGuidList = new ArrayList<String>();
                            if(StringUtil.isNotBlank(commonAuditTask))
                            for (Record comTask : commonAuditTask) {
                                String ouguid = comTask.getStr("ouguid");
                                ouGuidList.add(ouguid);
                            }
                            String ouguids = StringUtil.join(ouGuidList, "','");
                            List<FrameOu> commonFrameOu =  auditSpYyyzMaterialService.getCommonFrameOuByOuguids(ouguids);
                            // 添加通用部门
                            for (FrameOu frameOu : commonFrameOu) {
                                Boolean addFlag = true;
                                String ouguid = frameOu.getOuguid();
                               
                                for (int i = listOu.size()-1; i >=0; i--) {
                                    if(ouguid.equals(listOu.get(i).getOuguid())) {
                                        addFlag = false;
                                    }
                                }
                                if(addFlag) {
                                    listOu.add(frameOu);
                                }
                            }
                            // 部门绑定
                            for (int i = 0; i < listOu.size(); i++) {
                                FrameOuExtendInfo ouExt = ouService.getFrameOuExtendInfo(listOu.get(i).getOuguid());
                                AuditOrgaArea area = auditAreaImpl.getAreaByAreacode(ouExt.get("areacode")).getResult();
                                if (StringUtil.isBlank(ouExt.get("areacode")) || StringUtil.isBlank(area.getCitylevel())
                                        || Integer.parseInt(area.getCitylevel()) < Integer
                                                .parseInt(ZwfwConstant.AREA_TYPE_XZJ)) {

                                    if ("root".equals(objectGuid)
                                            || (listOu.get(i).getParentOuguid().equals(objectGuid))) {
                                        TreeNode node = new TreeNode();
                                        node.setId(listOu.get(i).getOuguid());
                                        node.setText(listOu.get(i).getOuname());                                       
                                        if ("root".equals(objectGuid)) {
                                            node.setPid("root");
                                        }
                                        else {
                                            node.setPid(listOu.get(i).getParentOuguid());
                                        }
                                        node.getColumns().put("isOU", "true");//标记：是部门节点
                                        node.setLeaf(false);
                                        int taskCount = 0;
                                        String areacodes = ZwfwUserSession.getInstance().getAreaCode()+"','"+ouExt.get("areacode");
                                        if (StringUtil.isNotBlank(itemcategory)
                                                && ZwfwConstant.CONSTANT_STR_ONE.equals(itemcategory)) {
                                            sql.clear();
                                            sql.eq("is_enable", ZwfwConstant.CONSTANT_STR_ONE);
                                            sql.eq("ISTEMPLATE", ZwfwConstant.CONSTANT_STR_ZERO);
                                            sql.eq("OUGUID", listOu.get(i).getOuguid());
                                            sql.in("areacode", areacodes);
                                            sql.isBlankOrValue("IS_HISTORY", ZwfwConstant.CONSTANT_STR_ZERO);
                                            taskCount = auditTask.getAuditEnableTaskCount(sql.getMap(), 0, 0,
                                                    "ordernum", "desc").getResult();
                                        }
                                        else {
                                            taskCount = auditSpYyyzMaterialService
                                                    .selectAuditTaskOuByObjectGuid(listOu.get(i).getOuguid(),
                                                            areacodes).size();
                                        }
                                        if (taskCount > 0) {
                                            list.add(node);
                                        }
                                    }
                                }
                            }
                            //事项的绑定
                            for (int j = 0; j < listTask.size(); j++) {
                                TreeNode node2 = new TreeNode();
                                node2.setId(listTask.get(j).getTask_id());
                                node2.setText(listTask.get(j).getTaskname().replace(",", "，"));
                                node2.setPid(listTask.get(j).getOuguid());
                                node2.getColumns().put("isOU", "false");//标记：不是部门节点
                                node2.setLeaf(true);
                                list.add(node2);
                            }
                        }
                    }
                    long endtime = System.currentTimeMillis();
                    //system.out.println("事项查询列表用时="+(endtime-starttime));
                    return list;
                }

                /***
                 * 懒加载进行获取数据，把左边树中选择的内容加载到右边
                 */
                @Override
                public List<SelectItem> onLazyNodeSelect(TreeNode treeNode) {
                    //获取到tree原有的select
                    List<SelectItem> selectedItems = treeModel_Task.getSelectNode();
                    //复选框选中
                    if (treeNode.isChecked() == true) {
                        //利用标记的isOU做判断
                        if ("true".equals(treeNode.getColumns().get("isOU"))) {
                            List<AuditTask> listTask = auditTask.selectAuditTaskOuByObjectGuid(treeNode.getId(),
                                    ZwfwUserSession.getInstance().getAreaCode()).getResult();
                            if(StringUtil.isNotBlank(itemcategory) && ZwfwConstant.CONSTANT_STR_ONE.equals(itemcategory) ){
                                for (int i = 0; i < listTask.size(); i++) {
                                    if(listTask.get(i).getItem_id().length()<31){
                                        continue;
                                    }
                                    for (int j = 0; j < selectedItems.size(); j++) {
                                        if (listTask.get(i).getTask_id().equals(selectedItems.get(j).getValue())) {
                                            selectedItems.remove(j);
                                        }
                                    }
                                    selectedItems.add(
                                            new SelectItem(listTask.get(i).getTask_id(), listTask.get(i).getTaskname().replace(",", "，")));
                                }

                            }else{
                                for (int i = 0; i < listTask.size(); i++) {
                                    for (int j = 0; j < selectedItems.size(); j++) {
                                        if (listTask.get(i).getTask_id().equals(selectedItems.get(j).getValue())) {
                                            selectedItems.remove(j);
                                        }
                                    }
                                    selectedItems.add(
                                            new SelectItem(listTask.get(i).getTask_id(), listTask.get(i).getTaskname().replace(",", "，")));
                                }
                                
                            }
                        }
                        else {
                            selectedItems.add(new SelectItem(treeNode.getId(), treeNode.getText()));
                        }
                    }
                    //复选框取消选中
                    else {
                        if ("true".equals(treeNode.getColumns().get("isOU"))) {

                            if ("".equals(treeNode.getId())) {
                                selectedItems.clear();
                            }
                            else {

                                List<AuditTask> listTask = auditTask.selectAuditTaskOuByObjectGuid(treeNode.getId(),
                                        ZwfwUserSession.getInstance().getAreaCode()).getResult();
                                for (int i = 0; i < listTask.size(); i++) {
                                    for (int j = 0; j < selectedItems.size(); j++) {
                                        if (selectedItems.get(j).getValue().equals(listTask.get(i).getTask_id())) {
                                            selectedItems.remove(j);
                                        }
                                    }
                                }
                            }
                        }
                        else {
                            for (int i = 0; i < selectedItems.size(); i++) {
                                if (selectedItems.get(i).getValue().equals(treeNode.getId())) {
                                    selectedItems.remove(i);
                                }
                            }
                        }
                    }
                    return selectedItems;
                }
            };
            if (!isPostback()) {
                treeModel_Task.setSelectNode(getSelectTaskPhaseguid(phaseguid));
            }
        }
        return treeModel_Task;
    }
    // blsp 事项配置
    public TreeModel getSpTaskTreeModel() {
        boolean  hasvalue = true;
        if (treeModel_Task == null) {
            hasvalue = false;
            treeModel_Task = new TreeModel()
            {

                @Override
                public List<TreeNode> fetch(TreeNode treeNode) {
                    TreeData treeData = TreeFunction9.getData(treeNode);
                    SqlConditionUtil sql = new SqlConditionUtil();
                    List<TreeNode> list = new ArrayList<>();
                    List<AuditSpBasetask> basetasklist = new ArrayList<>();

                    if (treeData == null) {
                        TreeNode root = new TreeNode();
                        root.setText("所有部门");
                        root.setId("root");
                        root.setPid("-1");
                        list.add(root);
                        root.setCkr(false);
                        root.setExpanded(true);//展开下一层节点
                        list.addAll(fetch(root));//自动加载下一层树结构
                    }else{
                        if (StringUtil.isNotBlank(treeNode.getSearchCondition())) {
                            sql.clear();
                            //根据phaseid过滤
                            AuditSpPhase phase =  iauditspphase.getAuditSpPhaseByRowguid(phaseguid).getResult();
                            if(phase!=null){                                
                                if(StringUtil.isNotBlank( phase.getPhaseId())){ 
                                    String[] ids = phase.getPhaseId().split(",");
                                  //  sql.in("phaseid", StringUtil.joinSql(ids));
                                }
                            }
                            sql.like("taskname", treeNode.getSearchCondition());
                            basetasklist =  iauditspbasetask.getAuditSpBasetaskByCondition(sql.getMap()).getResult();
                        }else{
                            if("root".equals(treeNode.getId())){
                                //第一层获取所有部门
                                sql.clear();
                                AuditSpPhase phase =  iauditspphase.getAuditSpPhaseByRowguid(phaseguid).getResult();
                                if(phase!=null){
                                    if(StringUtil.isNotBlank( phase.getPhaseId())){ 
                                        String[] ids = phase.getPhaseId().split(",");
                                    //    sql.in("phaseid", StringUtil.joinSql(ids));
                                    }
                                }
                                List<Record> listou = iauditspbasetask.getDistinctOuByCondition(sql.getMap()).getResult();
                                for (Record record : listou) {
                                    TreeNode node = new TreeNode();
                                    node.setText(record.getStr("ouname"));
                                    node.setId(record.getStr("ouname"));
                                    node.setPid(treeNode.getId());
                                    node.setCkr(true);
                                    list.add(node);
                                }
                            }else{
                                sql.clear();
                                AuditSpPhase phase =  iauditspphase.getAuditSpPhaseByRowguid(phaseguid).getResult();
                                if(phase!=null){                                
                                    if(StringUtil.isNotBlank( phase.getPhaseId())){ 
                                        String[] ids = phase.getPhaseId().split(",");
                                       // sql.in("phaseid", StringUtil.joinSql(ids));
                                    }
                                }
                                sql.eq("ouname", treeNode.getId());
                                basetasklist =  iauditspbasetask.getAuditSpBasetaskByCondition(sql.getMap()).getResult();
                            }
                        }
                        //树节点转换
                        for (AuditSpBasetask auditSpBasetask : basetasklist) {
                            TreeNode node = new TreeNode();
                            //判断改标准事项有没有绑定当前辖区事项
                            if(ZwfwConstant.CONSTANT_STR_ONE.equals(auditSpBasetask.getSflcbsx())){                                
                                node.setText("【里程碑】"+auditSpBasetask.getTaskname());
                            }else{
                                node.setText(auditSpBasetask.getTaskname());
                            }
                            if(redlist.contains(auditSpBasetask.getRowguid())){
                                node.getColumns().put("isred", "1");
                            }
                            if(yellowlist.contains(auditSpBasetask.getRowguid())){
                                node.getColumns().put("isyellow", "1");                                
                            }
                            node.getColumns().put("ouname", auditSpBasetask.getOuname());
                            node.getColumns().put("sflcbsx", auditSpBasetask.getSflcbsx());
                            node.setId(auditSpBasetask.getRowguid());
                            node.setPid(treeNode.getId());
                            node.setLeaf(true);
                            list.add(node);
                        }
                    }
                    
                    return list;
                }
                
                
                @Override
                public List<SelectItem> onLazyNodeSelect(TreeNode treeNode) {
                    List<SelectItem> selectedItems = treeModel_Task.getSelectNode();
                    SqlConditionUtil sql = new SqlConditionUtil();
                    //部门下的所有审批事项
                    sql.clear();
                    AuditSpPhase phase =  iauditspphase.getAuditSpPhaseByRowguid(phaseguid).getResult();
                    if(StringUtil.isNotBlank( phase.getPhaseId())){ 
                        String[] ids = phase.getPhaseId().split(",");
                       // sql.in("phaseid", StringUtil.joinSql(ids));
                    }
                    sql.eq("ouname", treeNode.getId());
                    List<AuditSpBasetask> basetasklist =  iauditspbasetask.getAuditSpBasetaskByCondition(sql.getMap()).getResult();
                    List<SelectItem> resel = new ArrayList<>();
                    //删除选中的
                    for (SelectItem selectitem : selectedItems) {
                        for (AuditSpBasetask auditSpBasetask : basetasklist) {
                            if(auditSpBasetask.getRowguid().equals(selectitem.getValue())){
                                resel.add(selectitem);
                            }
                        }
                    }
                    selectedItems.removeAll(resel);
                    //判断节点为选中，再追加上节点数据
                    if(treeNode.isChecked()){
                        for (AuditSpBasetask auditSpBasetask : basetasklist) {
                            if(ZwfwConstant.CONSTANT_STR_ONE.equals(auditSpBasetask.getSflcbsx())){                                
                                selectedItems.add(new SelectItem(auditSpBasetask.getRowguid(), "【里程碑】"+auditSpBasetask.getTaskname().replace(",", "，")));                                
                            }else{
                                selectedItems.add(new SelectItem(auditSpBasetask.getRowguid(), auditSpBasetask.getTaskname().replace(",", "，")));                                
                            }
                            
                        }
                    };
                    return selectedItems;
                }
            };
        }
        if (!isPostback()) {
            treeModel_Task.setSelectNode(getSelectTaskPhaseguid(phaseguid));
        }
        if(StringUtil.isNotBlank(getViewData("isInit")) && ZwfwConstant.CONSTANT_STR_ONE.equals(getViewData("isInit")) && hasvalue){
            List<SelectItem> selectItemList = treeModel_Task.getSelectNode();
            AuditSpPhase auditSpPhase = iauditspphase.getAuditSpPhaseByRowguid(phaseguid).getResult();
            List<SelectItem> auditTaskList = new ArrayList<SelectItem>();
            SqlConditionUtil sql = new SqlConditionUtil();
            String s[] = auditSpPhase.getPhaseId().split(";");
            List<String> list = new ArrayList<>();
            for(int i =0 ;i<s.length;i++){
                list.add(s[i]);
            }
            sql.in("phaseid", StringUtil.join(list,"','"));
            List<AuditSpBasetask> basetasklist = iAuditSpBasetask.getAuditSpBasetaskByCondition(sql.getMap())
                    .getResult();
            if (basetasklist.size() > 0) {
                for (AuditSpBasetask auditspbaseTask : basetasklist) {
                    if (auditspbaseTask != null) {
                        Boolean temp = true;
                        for(SelectItem selectItem: selectItemList){
                           if(selectItem.getValue().equals(auditspbaseTask.getRowguid())){
                               temp = false;
                           }
                        }
                        if(temp){
                            if(ZwfwConstant.CONSTANT_STR_ONE.equals(auditspbaseTask.getSflcbsx())){
                                auditTaskList.add(new SelectItem(auditspbaseTask.getRowguid(),
                                        "【里程碑】" + auditspbaseTask.getTaskname().replace(",", "，")));
                            }else{
                                auditTaskList.add(new SelectItem(auditspbaseTask.getRowguid(),
                                        auditspbaseTask.getTaskname().replace(",", "，")));
                            }
                        }
                    }
                }
            }
            for(SelectItem selectItem: selectItemList){
                auditTaskList.add(new SelectItem(selectItem.getValue(),
                        selectItem.getText().replace(",", "，")));
             }
            treeModel_Task.setSelectNode(auditTaskList);
            addViewData("isInit","0");
        }
        return treeModel_Task;
    }
    
    
    

    /**
     * 
     * 把事项和阶段信息保存到关系表里面
     * 
     * @param guidList
     *            前台获取的事项guid 用“;”分割
     * @param windowGuid
     *            窗口guid
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public void saveTaskToSp(String guidList, String phaseguid, String businedssguid) {

        String msg = "";
        if (StringUtil.isNotBlank(phaseguid) && StringUtil.isNotBlank(businedssguid)) {
            
            
            if (StringUtil.isNotBlank(phaseguid) && StringUtil.isNotBlank(businedssguid)) {
                AuditSpTaskImpl.deleteAuditSpTaskByPhaseguid(phaseguid);
                // auditWindowImpl.deleteWindowTaskByWindowGuid(windowGuid);
                // 添加新的事项关联关系
                if (StringUtil.isNotBlank(guidList)) {
                    String[] basetaskguids = guidList.split(";");
                    AuditSpBusiness AuditSpBusiness = IAuditSpBusinessImpl
                            .getAuditSpBusinessByRowguid(businedssguid).getResult();
                    SqlConditionUtil sqlc = new SqlConditionUtil();
                    if (StringUtil.isNotBlank(phaseguid)) {
                        for (int i = 0; i < basetaskguids.length; i++) {
                            sqlc.clear();
                            AuditSpTask auditPhaseTask = new AuditSpTask();
                            auditPhaseTask.setRowguid(UUID.randomUUID().toString());
                            auditPhaseTask.setOperatedate(new Date());
                            auditPhaseTask.setBusinessguid(businedssguid);
                            auditPhaseTask.setPhaseguid(phaseguid);
                            auditPhaseTask.setCustomtag(userSession.getUserGuid());   
                            auditPhaseTask.setBasetaskguid(basetaskguids[i]);
                            if(ZwfwConstant.CONSTANT_STR_TWO.equals(AuditSpBusiness.getBusinesstype())
                            		|| ZwfwConstant.CONSTANT_STR_THREE.equals(AuditSpBusiness.getBusinesstype())){
                                AuditTask audittask = auditTask.selectUsableTaskByTaskID(basetaskguids[i]).getResult();
                                if(audittask!=null){
                                    auditPhaseTask.setTaskid(audittask.getTask_id());
                                    auditPhaseTask.setTaskname(audittask.getTaskname());
                                    auditPhaseTask.setBasetaskguid("");
                                }else{
                                    continue;
                                }
                            }else{
                                sqlc.eq("areacode", ZwfwUserSession.getInstance().getAreaCode());
                                sqlc.eq("basetaskguid", basetaskguids[i]);
                                List<AuditSpBasetaskR> basetaskrs = iauditspbasetaskr.getAuditSpBasetaskrByCondition(sqlc.getMap()).getResult();
                                if(basetaskrs.size()>0){
                                    auditPhaseTask.setTaskid(basetaskrs.get(0).getTaskid()); 
                                }
                                AuditSpBasetask basetask = iauditspbasetask.getAuditSpBasetaskByrowguid(basetaskguids[i]).getResult();
                                if(basetask == null){
                                    continue;
                                }
                                auditPhaseTask.setTaskname(basetask.getTaskname());
                            }
                            if (AuditSpBusiness != null) {
                                auditPhaseTask.setAreacode(AuditSpBusiness.getAreacode());
                            }
                            AuditCommonResult<String> addResult = AuditSpTaskImpl.addAuditSpTask(auditPhaseTask);
                            if (!addResult.isSystemCode()) {
                                msg = "99";
                            } else if (!addResult.isBusinessCode()) {
                                msg = "88";
                            } else {
                                msg = "保存成功！";
                            }
                        }
                    }
                }else{
                    msg = "保存成功！";
                }
                String areacode="";
                    // 如果是镇村接件
                if (Integer.parseInt(ZwfwUserSession.getInstance().getCitylevel()) > Integer.parseInt(ZwfwConstant.AREA_TYPE_XQJ)){
                    areacode = ZwfwUserSession.getInstance().getBaseAreaCode();
                }else{
                    areacode = ZwfwUserSession.getInstance().getAreaCode();
                }
                // 处理共享材料和对应关系
                spShareMaterialService.initSPShareMaterialFromTask(businedssguid , areacode);
            }
            addCallbackParam("msg", msg);
        }
        else {
            addCallbackParam("msg", "添加失败！");
        }

    }
    
    public void taskinit(){
        AuditSpPhase auditSpPhase = iauditspphase.getAuditSpPhaseByRowguid(phaseguid).getResult();
        if(auditSpPhase!=null&&StringUtil.isNotBlank(auditSpPhase.getPhaseId())){
            addViewData("isInit", "1");
        }else{
            addCallbackParam("msg", "请先配置基本阶段！");
        }
    }
   

    public String getPhaseguid() {
        return phaseguid;
    }

    public void setPhaseguid(String phaseguid) {
        this.phaseguid = phaseguid;
    }

    public AuditTask getDataBean() {
        return dataBean;
    }

    public void setDataBean(AuditTask dataBean) {
        this.dataBean = dataBean;
    }

}
