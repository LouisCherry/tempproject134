package com.epoint.consultbyou.action;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.auditonlineuser.auditonlineconsult.domain.AuditOnlineConsult;
import com.epoint.basic.auditorga.auditcenter.domain.AuditOrgaServiceCenter;
import com.epoint.basic.auditorga.auditcenter.inter.IAuditOrgaServiceCenter;
import com.epoint.basic.auditorga.auditworkingday.inter.IAuditOrgaWorkingDay;
import com.epoint.basic.authentication.UserSession;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.tree.TreeData;
import com.epoint.basic.faces.tree.TreeFunction9;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.consultbyou.service.ConsultByouService;
import com.epoint.core.dto.base.TreeNode;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.dto.model.TreeModel;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.log.LogUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.frame.service.organ.ou.api.IOuService;
import com.epoint.frame.service.organ.ou.entity.FrameOu;

@RestController("consultbyou")
@Scope("request")
public class ConsultByou extends BaseController
{

    
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * 咨询类型
     */
    private List<SelectItem> consultTypeModel = null;
    
    transient Logger log = LogUtil.getLog(ConsultByou.class);
    
    @Autowired
    private IOuService ouService;
    
    private String type;
    private String status;
    private TreeModel checkboxtreeModel;
    
    
    private IAuditOrgaWorkingDay workingDay =  ContainerFactory.getContainInfo().getComponent(IAuditOrgaWorkingDay.class);    
    
    private IAuditOrgaServiceCenter iAuditOrgaServiceCenter =  ContainerFactory.getContainInfo().getComponent(IAuditOrgaServiceCenter.class);    
    
    private ConsultByouService service = new ConsultByouService();
    
    
    private String areaCode;
    
    public List<AuditOnlineConsult> getConsultByou(String startDate,String endDate,String areacode,String ouguid,String type,String status){
        Date endDate2 = new Date();
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, -1);
        Date startDate2 = c.getTime();
        int daysss = EpointDateUtil.getIntervalDays(startDate2, endDate2);
        Date after = EpointDateUtil.getDateAfter(startDate2, 30);
        List<AuditOnlineConsult> list = service.getConsultByou(startDate,endDate,areacode,ouguid,type,status);
        int code = 1;
        if(list!=null&&list.size()>0){
            String centerguid = ZwfwUserSession.getInstance().getCenterGuid();
            if (StringUtil.isBlank(centerguid)) {
                //system.out.println("areacode:"+areacode);
                SqlConditionUtil sql = new SqlConditionUtil();
                sql.eq("belongxiaqu", areacode);
                List<AuditOrgaServiceCenter> center =  iAuditOrgaServiceCenter.getAuditOrgaServiceCenterByCondition(sql.getMap()).getResult();
                //system.out.println("center:"+center);
                if (center != null) {
                    centerguid = center.get(0).getRowguid();
                }
            }
            for (AuditOnlineConsult auditOnlineConsult : list) {
                int days = 0;
                Date threeday = workingDay.getWorkingDayWithOfficeSet(centerguid, auditOnlineConsult.getAskdate(), 3).getResult();
                if(auditOnlineConsult.getAnswerdate()!=null){
                    days = EpointDateUtil.compareDateOnDay(threeday,auditOnlineConsult.getAnswerdate());
                    if(days<0){
                        auditOnlineConsult.set("QIXIAN", "超期已回复");
                        auditOnlineConsult.set("YUJING", "");
                    }else{
                        auditOnlineConsult.set("QIXIAN", "按期");
                        auditOnlineConsult.set("YUJING", "");
                    }
                }else{
                    //查询相隔日期的非工作日数量
                    Integer workday = workingDay.GetWeekendDays_Between_From_To(centerguid, auditOnlineConsult.getAskdate(), new Date()).getResult();
                    //查询相隔日期的总数量
                    int allday = EpointDateUtil.getIntervalDays(auditOnlineConsult.getAskdate(),new Date());
                    //计算相隔日期
                    days = allday - workday.intValue();
                    if(days>3){
                        auditOnlineConsult.set("QIXIAN", "超期未回复");
                        auditOnlineConsult.set("YUJING", "警告");
                    }else{
                        auditOnlineConsult.set("QIXIAN", "未回复");
                        auditOnlineConsult.set("YUJING", "请回复");
                    }
                }
                auditOnlineConsult.set("CODE", code);
                code ++;
            }
        }
        return list;
        
    }
    public List<SelectItem> getTypeModel() {
        if (consultTypeModel == null) {
            consultTypeModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "咨询投诉类型", null, false));
        }
        return consultTypeModel;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    @Override
    public void pageLoad() {
        areaCode = ZwfwUserSession.getInstance().getAreaCode();
        addCallbackParam("areaCode", areaCode);
        
    }
    public TreeModel getcheckboxModel() {

        if (checkboxtreeModel == null) {
            checkboxtreeModel = new TreeModel()
            {
                private static final long serialVersionUID = 1L;

                public List<TreeNode> fetch(TreeNode treeNode) {
                    TreeData treeData = TreeFunction9.getData(treeNode);
                    List<TreeNode> list = new ArrayList<>();

                    // 首次加载树结构
                    if (treeData == null) {
                        TreeNode root = new TreeNode();

                        root.setText("所有部门");
                        root.setId("");
                        root.setPid("-1");
                        list.add(root);
                        root.setExpanded(true);// 展开下一层节点
                        list.addAll(fetch(root));// 自动加载下一层树结构
                    }
                    // 每次点击树节点前的加号，进行加载
                    else {
                        String objectGuid = treeData.getObjectGuid();

                        List<FrameOu> listRootOu = new ArrayList<>();
                        
                        String userguid = ConfigUtil.getConfigValue("userguid");
                        
                        String userguid2 = UserSession.getInstance().getUserGuid();
                        if(userguid2.equals(userguid)){
                            listRootOu  =  ouService.listOUByGuid(objectGuid, 2);
                        }else{
                            listRootOu = service.getOUList(areaCode);
                        }

                        // 部门的绑定
                        for (int i = 0; i < listRootOu.size(); i++) {
                            if ((listRootOu.get(i).getParentOuguid() == null && objectGuid.equals(""))
                                    || (listRootOu.get(i).getParentOuguid().equals(objectGuid))) {
                                TreeNode node = new TreeNode();

                                node.setId(listRootOu.get(i).getOuguid());
                                node.setText(listRootOu.get(i).getOuname());
                                node.setPid(listRootOu.get(i).getParentOuguid());
                                node.setLeaf(true);

                                for (int j = 0; j < listRootOu.size(); j++) {
                                    if (listRootOu.get(i).getOuguid().equals(listRootOu.get(j).getParentOuguid())) {
                                        node.setLeaf(false);
                                        break;
                                    }
                                }
                                list.add(node);
                            }
                        }
                    }
                    return list;
                }
                public List<SelectItem> onLazyNodeSelect(TreeNode treeNode) {
                    //获取到tree原有的select

                    List<SelectItem> selectedItems = checkboxtreeModel.getSelectNode();
                    //获取到tree原有的select

                    if (selectedItems.size() != 0 && selectedItems.get(0).getValue().equals("请选择")) {
                        selectedItems.remove(0);
                    }

                    //复选框选中
                    TreeData treeData = TreeFunction9.getData(treeNode);
                    String objectGuid = treeData.getObjectGuid();
                    if (treeNode.isChecked() == true) {
                        //利用标记的isOU做判断
                      //  if (treeNode.getColumns().get("isOU").equals("true")) {
                            List<FrameOu> listRootOu = new ArrayList<>();
                            
                            String userguid = ConfigUtil.getConfigValue("userguid");
                            
                            String userguid2 = UserSession.getInstance().getUserGuid();
                            if(userguid2.equals(userguid)){
                                listRootOu  =  ouService.listOUByGuid(objectGuid, 2);
                            }else{
                                listRootOu = service.getOUList(areaCode);
                            }
                            for (int i = 0; i < listRootOu.size(); i++) {
                                for (int j = 0; j < selectedItems.size(); j++) {
                                    if (listRootOu.get(i).getOuguid().equals(selectedItems.get(j).getValue())) {
                                      selectedItems.remove(j);
                                  }
                                }
                                    selectedItems.add(new SelectItem(listRootOu.get(i).getOuguid(),
                                            listRootOu.get(i).getOuname()));
                            }
                             
                    }
                    //复选框取消选中
                    else {
                            List<FrameOu> listRootOu = new ArrayList<>();
                            
                            String userguid = ConfigUtil.getConfigValue("userguid");
                            
                            String userguid2 = UserSession.getInstance().getUserGuid();
                            if(userguid2.equals(userguid)){
                                listRootOu  =  ouService.listOUByGuid(objectGuid, 2);
                            }else{
                                listRootOu = service.getOUList(areaCode);
                            }
                            for (int i = 0; i < listRootOu.size(); i++) {
                                for (int j = 0; j < selectedItems.size(); j++) {
                                    if (listRootOu.get(i).getOuguid().equals(selectedItems.get(j).getValue())) {
                                      selectedItems.remove(j);
                                  }
                                }
                            }
                   }
                    return selectedItems;
                }
            };
        }

        return checkboxtreeModel;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    
}
