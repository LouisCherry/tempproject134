package com.epoint.auditsp.auditsphandle.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.auditorga.auditarea.domain.AuditOrgaArea;
import com.epoint.basic.auditorga.auditarea.inter.IAuditOrgaArea;
import com.epoint.basic.auditresource.auditrsitembaseinfo.domain.AuditRsItemBaseinfo;
import com.epoint.basic.auditresource.auditrsitembaseinfo.inter.IAuditRsItemBaseinfo;
import com.epoint.basic.auditresource.company.domain.AuditRsCompanyBaseinfo;
import com.epoint.basic.auditresource.company.inter.IAuditRsCompanyBaseinfo;
import com.epoint.basic.auditsp.auditspbasetask.domain.AuditSpBasetask;
import com.epoint.basic.auditsp.auditspbasetask.inter.IAuditSpBasetask;
import com.epoint.basic.auditsp.auditspbasetaskr.domain.AuditSpBasetaskR;
import com.epoint.basic.auditsp.auditspbasetaskr.inter.IAuditSpBasetaskR;
import com.epoint.basic.auditsp.auditspbusiness.domain.AuditSpBusiness;
import com.epoint.basic.auditsp.auditspbusiness.inter.IAuditSpBusiness;
import com.epoint.basic.auditsp.auditspisubapp.domain.AuditSpISubapp;
import com.epoint.basic.auditsp.auditspisubapp.inter.IAuditSpISubapp;
import com.epoint.basic.auditsp.auditspitask.domain.AuditSpITask;
import com.epoint.basic.auditsp.auditspitask.inter.IAuditSpITask;
import com.epoint.basic.auditsp.auditspphase.domain.AuditSpPhase;
import com.epoint.basic.auditsp.auditspphase.inter.IAuditSpPhase;
import com.epoint.basic.auditsp.auditsptask.inter.IAuditSpTask;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.authentication.UserSession;
import com.epoint.basic.controller.BaseController;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.composite.auditsp.handlespimaterial.inter.IHandleSPIMaterial;
import com.epoint.core.dto.base.TreeNode;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.dto.model.TreeModel;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.message.api.IMessagesCenterService;

@RestController("handlesubaddtaskconfigaction")
@Scope("request")
public class HandleSubAddtaskConfigAction extends BaseController
{
    /**
     * 
     */
    private static final long serialVersionUID = 3065709190052887109L;

    @Autowired
    private IAuditSpTask iauditsptask;

    @Autowired
    private IAuditSpITask iauditspitask;

    @Autowired
    private IAuditSpISubapp iauditspisubapp;

    @Autowired
    private IAuditSpBusiness iauditspbusiness;
    
    @Autowired
    private IAuditSpPhase iauditspphase;

    @Autowired
    private IAuditSpBasetask iauditspbasetask;

    @Autowired
    private IAuditSpBasetaskR iauditspbasetaskr;
    
    @Autowired
    private IMessagesCenterService messageCenterService;
    
    @Autowired
    private IHandleSPIMaterial handleSPIMaterialService;
    
    @Autowired
    private IAuditTask iaudittask;

    @Autowired
    private IAuditOrgaArea iauditorgaarea;

    @Autowired
    private IAuditRsItemBaseinfo iauditrsitembaseinfo;

    @Autowired
    private IAuditRsCompanyBaseinfo companyBaseinfoService;
    
    private String guid;
    
    private String phaseguid;
    
    private TreeModel treeModel = null;
    
    public List<String> sjcode;
    
    private AuditSpISubapp subapp;
    
    public static final String LHSP_Status_SJZ = "23";

        
    @Override
    public void pageLoad() {
        guid = getRequestParameter("subappGuid");
        //查询所有市级的areacode，和当前辖区
        subapp = iauditspisubapp.getSubappByGuid(guid).getResult();
        if(subapp != null){
            phaseguid = subapp.getPhaseguid();
        }
        else{
            addCallbackParam("msg", "未获取到申报数据！");
        }
        sjcode = iauditorgaarea.getAllAreacodeByAreacode(ZwfwUserSession.getInstance().getAreaCode()).getResult();
    }
     
    public TreeModel getTaskTreeModel() {
        if(treeModel == null){
            treeModel = new TreeModel()
            {
                
                /**
                 * 
                 */
                private static final long serialVersionUID = -8073287689498990143L;

                @Override
                public List<TreeNode> fetch(TreeNode treenode) {
                    List<String> fxtaskids = iauditspbasetaskr.gettaskidsByBusinedssguid(subapp.getBusinessguid(),"5").getResult();

                    //第一层加载所有事项
                    List<AuditSpBasetask> listsptask = new ArrayList<AuditSpBasetask>();
                    List<AuditSpBasetaskR> listsptaskr = new ArrayList<AuditSpBasetaskR>();
                    List<TreeNode> listtreenode = new ArrayList<TreeNode>();
                    if(treenode == null){
                        listsptask =  iauditsptask.selectAuditSpBaseTasksByPhaseguid(phaseguid).getResult();
                        List<String> list = new ArrayList<>();
                        for (AuditSpBasetask auditSpBasetask : listsptask) {
                            list.add(auditSpBasetask.getRowguid());
                        }
                        SqlConditionUtil sqlc= new SqlConditionUtil();
                        if(StringUtil.isNotBlank(StringUtil.joinSql(list))){                            
                            sqlc.in("basetaskguid", StringUtil.joinSql(list));
                        }else{
                            sqlc.in("basetaskguid", "''");
                        }
                        //查询已经求的事项
                        List<AuditSpITask> spitasklist = iauditspitask.getTaskInstanceBySubappGuid(guid).getResult();
                        List<String> taskids = new ArrayList<>();
                        //过滤之前选中的事项，一个事项只能选择一次
                        for (AuditSpITask auditspitask : spitasklist) {
                            AuditTask audittask = iaudittask.selectTaskByRowGuid(auditspitask.getTaskguid()).getResult();
                            //辅线事项不添加
                            if(audittask!=null && !fxtaskids.contains(audittask.getTask_id())){
                                taskids.add(audittask.getTask_id());                              
                            }
                        }
                        if(!taskids.isEmpty()){                            
                            sqlc.notin("taskid", StringUtil.joinSql(taskids));
                        }
                        listsptaskr = iauditspbasetaskr.getAuditSpBasetaskrByCondition(sqlc.getMap()).getResult();                        
                    }
                    listsptaskr = listsptaskr.stream().filter(a->sjcode.contains(a.getAreacode())).collect(Collectors.toList());
                    List<String> basetaskguidlist =  listsptaskr.stream().map(a->a.getBasetaskguid()).collect(Collectors.toList());
                    for (AuditSpBasetaskR auditSpBasetaskR : listsptaskr) {
                        //查询在用事项
                        AuditTask audittask = iaudittask.selectUsableTaskByTaskID(auditSpBasetaskR.getTaskid()).getResult();
                        if(audittask!= null && !audittask.isEmpty()){   
                        	 AuditOrgaArea area = iauditorgaarea.getAreaByAreacode(audittask.getAreacode()).getResult();
                            TreeNode node = new TreeNode();
                            if (area != null) {
                            	 node.setText("（"+area.getXiaquname()+"）"+audittask.getStr("taskname"));
                            }else {
                            	 node.setText(audittask.getStr("taskname"));
                            }
                           
                            node.setId(audittask.getTask_id());
                            node.setPid(auditSpBasetaskR.getBasetaskguid());
                            node.getColumns().put("isbasetask", "false");//标记：不是部门节点
                            node.setCkr(true);
                            node.setChecked(true);
                            node.setLeaf(true);
                            listtreenode.add(node);
                        }
                    }
                    for (AuditSpBasetask auditSpBasetask : listsptask) {
                        TreeNode root = new TreeNode();
                        root.setText(auditSpBasetask.getTaskname());
                        root.setId(auditSpBasetask.getRowguid());
                        root.setPid("-1");
                        root.getColumns().put("isbasetask", "true");//标记：不是部门节点
                        root.setCkr(false);
                        root.setExpanded(true);//展开下一层节点
                        //过滤掉没有子节点的数据
                        if(basetaskguidlist.contains(auditSpBasetask.getRowguid())){                            
                            listtreenode.add(root);
                        }
                    }
                    return listtreenode;
                }
            };
        }
        
        return treeModel;
    }
    
    public void saveAddtask(){
        List<SelectItem> selects= treeModel.getSelectNode();
        if(!selects.isEmpty()){
            subapp = iauditspisubapp.getSubappByGuid(guid).getResult();
            StringBuffer sb = new StringBuffer();
            for (SelectItem selectItem : selects) {
                String taskid = (String) selectItem.getValue();
                AuditTask audittask = iaudittask.selectUsableTaskByTaskID(taskid).getResult();
                if(audittask == null){
                    continue;
                }
                sb.append(audittask.getTaskname()).append("、");
                AuditSpBasetask basetask = iauditspbasetask.getAuditSpBasetaskBytaskid(taskid).getResult();
                iauditspitask.addTaskInstance(subapp.getBusinessguid(), subapp.getBiguid(), subapp.getPhaseguid(), audittask.getRowguid(), audittask.getTaskname(), subapp.getRowguid(), 0, audittask.getAreacode(),"",basetask.getSflcbsx());
            }
            if(StringUtil.isBlank(sb)){
                return;
            }
            String taskname = sb.toString();
            taskname = taskname.substring(0,taskname.length()-1);
            //修改子申报的状态为收件中
            iauditspisubapp.updateSubapp(guid, LHSP_Status_SJZ, null);
            // 组织机构代码证
            AuditRsItemBaseinfo dataBean = iauditrsitembaseinfo.getAuditRsItemBaseinfoByRowguid(subapp.getYewuguid()).getResult();
            
            String ownerGuid = "";
            
            if (ZwfwConstant.CERT_TYPE_ZZJGDMZ.equals(dataBean.getItemlegalcerttype())) {
                AuditRsCompanyBaseinfo auditCompany = companyBaseinfoService
                        .getCompanyByOneField("organcode", dataBean.getItemlegalcertnum()).getResult();
                if (auditCompany != null) {
                    ownerGuid = auditCompany.getCompanyid();
                }
            }
            // 统一社会信用代码
            else {
                AuditRsCompanyBaseinfo auditCompany = companyBaseinfoService
                        .getCompanyByOneField("creditcode", dataBean.getItemlegalcertnum()).getResult();
                if (auditCompany != null) {
                    ownerGuid = auditCompany.getCompanyid();
                }
            }
            //重新初始化材料
            handleSPIMaterialService.initSubappMaterial(guid, subapp.getBusinessguid(), subapp.getBiguid(), subapp.getPhaseguid(),ownerGuid, dataBean.getItemlegalcertnum()).getResult();
            
            //向网厅发送消息提醒
            if(StringUtil.isNotBlank(subapp.getApplyerway()) && ZwfwConstant.APPLY_WAY_NETSBYS.equals(subapp.getApplyerway())){
                String openUrl = configService.getFrameConfigValue("zwdtMsgurl")
                        + "/epointzwmhwz/pages/approve/perioddetail?biguid=" + subapp.getBiguid()
                        + "&phaseguid=" + subapp.getPhaseguid() + "&businessguid="
                        + subapp.getBusinessguid() + "&subappguid=" + subapp.getRowguid() + "&itemguid="
                        + subapp.getYewuguid() + "&type=watch";
                AuditSpBusiness business = iauditspbusiness.getAuditSpBusinessByRowguid(subapp.getBusinessguid()).getResult();
                AuditSpPhase phase = iauditspphase.getAuditSpPhaseByRowguid(subapp.getPhaseguid()).getResult();
                if(business == null || phase==null){
                    return;
                }
                String title = "您申报的"+business.getBusinessname() +"主题"+phase.getPhasename() + "阶段，添加了"+taskname+"事项，请到企业空间继续办理！";
                messageCenterService.insertWaitHandleMessage(UUID.randomUUID().toString(), title,
                        IMessagesCenterService.MESSAGETYPE_WAIT, subapp.getApplyerguid(), "",
                        UserSession.getInstance().getUserGuid(), UserSession.getInstance().getDisplayName(), "",
                        openUrl, UserSession.getInstance().getOuGuid(), UserSession.getInstance().getBaseOUGuid(), 1,
                        null, "", null, null, new Date(), null, UserSession.getInstance().getUserGuid(), null, "");
            }
        }
        addCallbackParam("msg","保存成功！");
    }
    
}
