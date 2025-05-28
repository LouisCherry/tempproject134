package com.epoint.cs.auditepidemiclog.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;


import com.epoint.common.util.ZwfwConstant;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.cs.auditepidemiclog.api.IAuditEpidemicLogService;
import com.epoint.cs.auditepidemiclog.api.entity.AuditEpidemicLog;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.basic.controller.RightRelation;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.basic.auditorga.auditcenter.domain.AuditOrgaServiceCenter;
import com.epoint.basic.auditorga.auditcenter.inter.IAuditOrgaServiceCenter;
import com.epoint.basic.controller.BaseController;


/**
 * AUDIT_EPIDEMIC_LOG新增页面对应的后台
 * 
 * @author 11818
 * @version [版本号, 2020-02-02 12:14:08]
 */
@RightRelation(AuditEpidemicLogListAction.class)
@RestController("auditepidemiclogaddaction")
@Scope("request")
public class AuditEpidemicLogAddAction extends BaseController
{
    /**
     * 
     */
    private static final long serialVersionUID = 335418582087247194L;
    @Autowired
    private IAuditEpidemicLogService service;
    /**
     * AUDIT_EPIDEMIC_LOG实体对象
     */
    private AuditEpidemicLog dataBean;
    @Autowired
    private IAuditOrgaServiceCenter auditServiceCenterImpl;
    /**
    * 登记状态下拉列表model
    */
    private List<SelectItem> statusModel = null;
    private AuditOrgaServiceCenter center;
    private String centerguid;
    private String centername;
    public void pageLoad() {
        System.err.println(11111);
        dataBean = new AuditEpidemicLog();
        centerguid = ZwfwUserSession.getInstance().getCenterGuid();
        if(StringUtil.isNotBlank(centerguid)){
            center = auditServiceCenterImpl.findAuditServiceCenterByGuid(centerguid).getResult();
            centername =center.getCentername();
        }
    }

    /**
     * 保存并关闭
     * 
     */
    public void add() {
        AuditEpidemicLog auditepidemiclog = service.selectLastestInfoAll(dataBean.getId()).getResult();
        if(auditepidemiclog!=null){
           if(ZwfwConstant.CONSTANT_STR_ONE.equals(auditepidemiclog.getStatus())){
               auditepidemiclog.setExittime(new Date());
               auditepidemiclog.setStatus(ZwfwConstant.CONSTANT_STR_TWO);
               auditepidemiclog.setCenterguid(centerguid);
               auditepidemiclog.setCentername(centername);
               service.update(auditepidemiclog);
               addCallbackParam("msg", "登记成功！");
               dataBean = null;
               
           }else if(ZwfwConstant.CONSTANT_STR_TWO.equals(auditepidemiclog.getStatus())){
               dataBean.setRowguid(UUID.randomUUID().toString());
               dataBean.setOperatedate(new Date());
               dataBean.setOperateusername(userSession.getDisplayName());
               dataBean.setEntrytime(new Date());
               dataBean.setCenterguid(centerguid);
               dataBean.setCentername(centername);
               dataBean.setStatus(ZwfwConstant.CONSTANT_STR_ONE);
               service.insert(dataBean);
               addCallbackParam("msg", "登记成功！");
               dataBean = null;
           }
        }else{
            
            dataBean.setRowguid(UUID.randomUUID().toString());
            dataBean.setOperatedate(new Date());
            dataBean.setOperateusername(userSession.getDisplayName());
            dataBean.setEntrytime(new Date());
            dataBean.setCenterguid(centerguid);
            dataBean.setCentername(centername);
            dataBean.setStatus(ZwfwConstant.CONSTANT_STR_ONE);
            service.insert(dataBean);
            addCallbackParam("msg", "登记成功！");
            dataBean = null;
        }
//        dataBean.setRowguid(UUID.randomUUID().toString());
//        dataBean.setOperatedate(new Date());
//        dataBean.setOperateusername(userSession.getDisplayName());
//        service.insert(dataBean);
//        addCallbackParam("msg", "保存成功！");
//        dataBean = null;
    }

    /**
     * 保存并新建
     * 
     */
    public void addNew() {
        add();
        dataBean = new AuditEpidemicLog();
    }

    public AuditEpidemicLog getDataBean() {
        if (dataBean == null) {
            dataBean = new AuditEpidemicLog();
        }
        return dataBean;
    }

    public void setDataBean(AuditEpidemicLog auditepidemiclog) {
        this.dataBean = auditepidemiclog;
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> getStatusModel() {
        if (statusModel == null) {
            statusModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "登记状态", null, false));
        }
        return this.statusModel;
    }
    
    /**
     * 根据证照编号获取申请人列表
     * 
     * @param query 输入的证照号
     * @return
     */
    public List<SelectItem> searchHistory(String query) {
        List<SelectItem> list = new ArrayList<SelectItem>();
        if (StringUtil.isNotBlank(query)) {
            List<AuditEpidemicLog> loglist =service.selectEpidemicLogByLikeID(query).getResult();
            for (AuditEpidemicLog auditepidemiclog : loglist) {
                String str = auditepidemiclog.getId() + "（" + auditepidemiclog.getName() + "）";
                SelectItem selectItem = new SelectItem();
                selectItem.setText(str);
                selectItem.setValue(auditepidemiclog.getId());
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
    public void selectApplyer(String id, String certType) {
            if (StringUtil.isNotBlank(id)) {
                AuditEpidemicLog auditepidemiclog = service.selectLastestInfo(id).getResult();
              addCallbackParam("msg", auditepidemiclog);

//                AuditRsIndividualBaseinfo auditIndividual = individualService
//                        .getAuditRsIndividualBaseinfoByIDNumber(certnum).getResult();
//                if (auditIndividual != null) {
//                    ownerguid.append(auditIndividual.getPersonid());
//                    // 设置办件信息
//                    HashMap<String, String> map = new HashMap<String, String>(16);
//                    map.put("applyername", auditIndividual.getClientname());
//                    map.put("address", auditIndividual.getDeptaddress());
//                    map.put("contactperson", auditIndividual.getContactperson());
//                    map.put("contactphone", auditIndividual.getContactphone());
//                    map.put("contactmobile", auditIndividual.getContactmobile());
//                    map.put("contactfax", auditIndividual.getContactfax());
//                    map.put("contactpostcode", auditIndividual.getContactpostcode());
//                    map.put("contactemail", auditIndividual.getContactemail());
//                    map.put("certnum", auditIndividual.getIdnumber());
//                    map.put("contactcertnum", auditIndividual.getContactcertnum());
//                    addCallbackParam("msg", map);
//                }
            }
    }
    
    
}
