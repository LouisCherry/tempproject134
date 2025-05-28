package com.epoint.auditsp.auditsphandle.action;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.auditsp.auditsphandle.api.IAuditSpITaskfwxm;
import com.epoint.auditsp.auditsphandle.api.IIndividualAuditRsitemBaseinfo;
import com.epoint.basic.auditresource.auditrsitembaseinfo.domain.AuditRsItemBaseinfo;
import com.epoint.basic.auditresource.auditrsitembaseinfo.inter.IAuditRsItemBaseinfo;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.audittask.material.domain.AuditTaskMaterial;
import com.epoint.basic.audittask.material.inter.IAuditTaskMaterial;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.zhenggai.api.ZhenggaiService;

/**
 * 材料告知页面对应的后台
 * 
 * @author Administrator
 *
 */
@RestController("jnjnhandlematerialnotifyaction")
@Scope("request")
public class JNjnHandleMaterialNotifyAction extends BaseController
{
    /**
     * 
     */
    private static final long serialVersionUID = 8713148757403132395L;

    private AuditRsItemBaseinfo dataBean;

    /**
     * 建设单位
     */
    private String applyername;
    /**
     * 是否代码项
     */
    private List<SelectItem> bbdbModal;
    
    private List<SelectItem> userModal;
    /**
     * 项目阶段下拉列表model
     */
    private List<SelectItem> itemStageModel = null;
    /**
     * 申报名称
     */
    private String itemname;

    private DataGridModel<AuditTask> modelTask = null;

    private DataGridModel<AuditTaskMaterial> modelMaterial = null;

    @Autowired
    private IIndividualAuditRsitemBaseinfo iIndividualAuditRsitemBaseinfo;

    @Autowired
    private IAuditRsItemBaseinfo rsItemBaseinfoService;

    @Autowired
    private IAuditTask auditTaskService;

    @Autowired
    private IAuditTaskMaterial spIMaterialService;

    @Autowired
    private ICodeItemsService codeItemsService;

    
    @Autowired
    private IAuditSpITaskfwxm iTaskfwxm;
    @Autowired
    private ZhenggaiService zhenggaiImpl;
    private String taskIdList;
    
    @Override
    public void pageLoad() {
        dataBean = new AuditRsItemBaseinfo();
        String itemcode = getRequestParameter("itemcode");
        String itemname = getRequestParameter("itemname");
        String itemstage = getRequestParameter("itemstage");
        String taskIdLists = getRequestParameter("taskIdList");
        if(StringUtil.isNotBlank(itemcode)) {
            addCallbackParam("itemcode", itemcode);
        }
        if(StringUtil.isNotBlank(itemname)) {
            try {
                String newitemname = new String(itemname.getBytes(),"UTF-8");
                addCallbackParam("itemname", newitemname);
            }
            catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
           
        }
        if(StringUtil.isNotBlank(itemstage)) {
            List<SelectItem> SelectItems = getItemStageModel();
                for (SelectItem item :SelectItems) {
                    if (itemstage.equals(item.getValue())) {
                        addCallbackParam("itemstage", item.getText());
                        break;
                    }
                    
                }
           
        }
        if(StringUtil.isNotBlank(taskIdLists)) {
            addCallbackParam("taskIdList", taskIdLists);
            taskIdList = taskIdLists;
        }
    }

    public DataGridModel<AuditTask> getDataGridTask() {
        // 获得表格对象
        if (modelTask == null) {
            modelTask = new DataGridModel<AuditTask>()
            {
                @Override
                public List<AuditTask> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    List<AuditTask> auditTaskList = new ArrayList<AuditTask>();
                    String[] taskids = null;
                    if (taskIdList != null && !"tasknull".equals(taskIdList)) {
                        taskids = taskIdList.split(";");
                        if (taskids != null && taskids.length > 0) {
                            for (int i =0;i<taskids.length;i++) {
                                AuditTask auditTask = auditTaskService.getAuditTaskByGuid(taskids[i], true)
                                        .getResult();
                                auditTaskList.add(auditTask);
                            } 

                        }
                    }
                    this.setRowCount(auditTaskList == null ? 0 : auditTaskList.size());
                    return auditTaskList;
                }
            };
        }
        return modelTask;
    }

    public DataGridModel<AuditTaskMaterial> getDataGridMaterial() {
        // 获得表格对象
        if (modelMaterial == null) {
            modelMaterial = new DataGridModel<AuditTaskMaterial>()
            {
                @Override
                public List<AuditTaskMaterial> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    List<AuditTaskMaterial> spIMaterials = new ArrayList<AuditTaskMaterial>();
                    String[] taskids = null;
                    if (taskIdList != null && !"tasknull".equals(taskIdList)) {
                        taskids = taskIdList.split(";");
                        if (taskids != null && taskids.length > 0) {
                            for (int i =0;i<taskids.length;i++) {
                                List<AuditTaskMaterial> resultMateials = spIMaterialService.selectTaskMaterialListByTaskGuid(taskids[i], false).getResult();
                                if (resultMateials != null && resultMateials.size() > 0) {
                                    String taskname=null;
                                    for (AuditTaskMaterial AuditMaterial : resultMateials) {
                                        Record record = iTaskfwxm.findTasknameByTaskguid(AuditMaterial.getTaskguid());

                                        if(AuditMaterial!=null){
                                            if(record.get("taskname").equals(taskname)){
                                                AuditMaterial.put("taskname", "");  
                                            }else{
                                                AuditMaterial.put("taskname", record.get("taskname"));
                                            }
                                            taskname=record.get("taskname");
                                            
                                            AuditMaterial.put("file_source", record.get("file_source"));
                                            AuditMaterial.put("necessity", record.get("necessity"));
                                        }
                                        if (AuditMaterial.getSubmittype() != null) {
                                            AuditMaterial.put("submittype",
                                                    codeItemsService.getItemTextByCodeName("提交方式", AuditMaterial.getSubmittype())
                                                            .replace("同时", "").replace("提交", ""));
                                        }
                                        spIMaterials.add(AuditMaterial);
                                    }
                                }
                            } 

                        }
                    }
                    this.setRowCount(spIMaterials == null ? 0 : spIMaterials.size());
                    return spIMaterials == null ? new ArrayList<>() : spIMaterials;
                }
            };
        }
        return modelMaterial;
    }
    
    /**
     * 根据项目名称获取项目列表
     * 
     * @param query
     *            输入的证照号
     * @return
     */
    public List<SelectItem> searchApplyerNameHistory(String query) {

        List<SelectItem> list = new ArrayList<SelectItem>();

        if (StringUtil.isNotBlank(query)) {
                  String xiaqucode=ZwfwUserSession.getInstance().getAreaCode();
                    List<AuditRsItemBaseinfo> individuallist = iIndividualAuditRsitemBaseinfo
                            .selectIndividualByLikeIDname(query,xiaqucode);
                    for (AuditRsItemBaseinfo auditIndividual : individuallist) {
                        String str = auditIndividual.getItemname();
                        SelectItem selectItem = new SelectItem();
                        selectItem.setText(str);
                        selectItem.setValue(auditIndividual.getItemcode());
                        list.add(selectItem);
                    }
    
        }
        return list;

    }
    
    /**
     * 根据证照编号获取申请人详细信息
     * 
     * @param certnum
     */
    public void selectApplyer(String itemcode,String itemname) {
            if (StringUtil.isNotBlank(itemcode)) {
              AuditRsItemBaseinfo auditRsItemBaseinfo  = iIndividualAuditRsitemBaseinfo.getAuditRsItemBaseinfobyitemcode(itemcode);
              
                if (auditRsItemBaseinfo != null) {
                    // 设置办件信息
                    HashMap<String, String> map = new HashMap<String, String>(16);
                    map.put("itemname", auditRsItemBaseinfo.getItemname());
                    map.put("itemcode", auditRsItemBaseinfo.getItemcode());
                    map.put("itemtype", auditRsItemBaseinfo.getItemtype());
                    map.put("constructionproperty", auditRsItemBaseinfo.getConstructionproperty());
                    map.put("itemlegaldept", auditRsItemBaseinfo.getItemlegaldept());
                    map.put("itemlegalcreditcode", auditRsItemBaseinfo.getItemlegalcreditcode());
                    map.put("itemlegalcerttype", auditRsItemBaseinfo.getItemlegalcerttype());
                    map.put("itemlegalcertnum", auditRsItemBaseinfo.getItemlegalcertnum());
                    map.put("itemstartdate", auditRsItemBaseinfo.getItemstartdate()+"");
                    map.put("itemfinishdate", auditRsItemBaseinfo.getItemfinishdate()+"");
                    map.put("legalproperty", auditRsItemBaseinfo.getLegalproperty());
                    map.put("contractperson", auditRsItemBaseinfo.getContractperson());
                    map.put("contractphone", auditRsItemBaseinfo.getContractphone());
                    map.put("totalinvest", String.valueOf(auditRsItemBaseinfo.getTotalinvest()));
                    map.put("quantifyconstructtype", auditRsItemBaseinfo.getQuantifyconstructtype());
                    map.put("quantifyconstructdept", auditRsItemBaseinfo.getQuantifyconstructdept());
                    map.put("constructionsite", auditRsItemBaseinfo.getConstructionsite());
                    map.put("constructionsitedesc", auditRsItemBaseinfo.getConstructionsitedesc());
                    map.put("constructionscaleanddesc", auditRsItemBaseinfo.getConstructionscaleanddesc());
                    addCallbackParam("msg", map);
                }
        }
     
    }
    
    /**
     * 根据证照编号获取申请人列表
     * 
     * @param query
     *            输入的证照号
     * @return
     */
    public List<SelectItem> searchHistory(String query) {

        List<SelectItem> list = new ArrayList<SelectItem>();

        if (StringUtil.isNotBlank(query)) {
                  String xiaqucode=ZwfwUserSession.getInstance().getAreaCode();
                  //system.out.println("辖区code："+xiaqucode);
                    List<AuditRsItemBaseinfo> individuallist = iIndividualAuditRsitemBaseinfo
                            .selectIndividualByLikeIDNumber(query,xiaqucode);
                    for (AuditRsItemBaseinfo auditIndividual : individuallist) {
                        String str = auditIndividual.getItemcode();
                        SelectItem selectItem = new SelectItem();
                        selectItem.setText(str);
                        selectItem.setValue(auditIndividual.getItemcode());
                        list.add(selectItem);
                    }
    
        }
        return list;

    }

    
    @SuppressWarnings("unchecked")
	public List<SelectItem> getbbdbModal() {
        if (bbdbModal == null) {
        	bbdbModal = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("单选按钮组", "是否", null, false));
        }
        return this.bbdbModal;
    }

    public List<SelectItem> getLogTypeList() {
        if (userModal == null) {
        	userModal = new ArrayList<SelectItem>();
        	 List<Record> list=zhenggaiImpl.getbbdb();
        	
            for (Record window : list) {
            	userModal.add(new SelectItem(window.getStr("userguid"), window.getStr("username")+"             联系电话:"+window.getStr("telephoneOffice")));
            }
        }
        return this.userModal;
    }

    public void updatexiangmu(){
    	rsItemBaseinfoService.updateAuditRsItemBaseinfo(dataBean);
    	addCallbackParam("msg", "保存成功");

    }
    
    public AuditRsItemBaseinfo getDataBean() {
        if (dataBean == null) {
            dataBean = new AuditRsItemBaseinfo();
        }
        return dataBean;
    }

    public void setDataBean(AuditRsItemBaseinfo dataBean) {
        this.dataBean = dataBean;
    }

    public String getApplyername() {
        return applyername;
    }

    public void setApplyername(String applyername) {
        this.applyername = applyername;
    }

    public String getItemname() {
        return itemname;
    }

    public void setItemname(String itemname) {
        this.itemname = itemname;
    }
    @SuppressWarnings("unchecked")
    public List<SelectItem> getItemStageModel() {
        if (itemStageModel == null) {
            itemStageModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "项目阶段", null, false));
        }
        return itemStageModel;
    }

    public String getTaskIdList() {
        return taskIdList;
    }

    public void setTaskIdList(String taskIdList) {
        this.taskIdList = taskIdList;
    }

    
}
