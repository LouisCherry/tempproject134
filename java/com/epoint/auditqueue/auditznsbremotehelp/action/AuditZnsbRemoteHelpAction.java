package com.epoint.auditqueue.auditznsbremotehelp.action;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.auditqueue.auditznsbequipment.domain.AuditZnsbEquipment;
import com.epoint.basic.auditqueue.auditznsbequipment.inter.IAuditZnsbEquipment;
import com.epoint.basic.auditqueue.auditznsbremotehelp.domain.AuditZnsbRemoteHelp;
import com.epoint.basic.auditqueue.auditznsbremotehelp.inter.IAuditZnsbRemoteHelpService;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;

/**
 * 设备维护表list页面对应的后台
 * 
 * @author wj
 * @version [版本号, 2016-11-07 14:37:54]
 */
@RestController("auditznsbremotehelpaction")
@Scope("request")
public class AuditZnsbRemoteHelpAction extends BaseController {

	private static final long serialVersionUID = 5633310185213201266L;
	@Autowired
    private IAuditZnsbEquipment equipmentservice ;
	
    @Autowired
    private IAuditZnsbRemoteHelpService auditZnsbRemoteHelpService;   
	
	/**
	 * 设备维护表实体对象
	 */
	private AuditZnsbEquipment dataBean;

	/**
	 * 表格控件model
	 */
	private DataGridModel<AuditZnsbEquipment> model;
	/**
	 * 设备类型下拉列表model
	 */
	private List<SelectItem> machinetypeModel = null;
	

	private String machinename;
	private String machinetype;
	private String machineno;
	private String macaddress;
	private String rowguid;

	public void pageLoad() {
	    rowguid =  request.getParameter("guid");
	}
	
	   /**
     * 保存选中的一体机
     * 
     */
    public void saveAio() {
        List<String> select = getDataGridData().getSelectKeys();
        for (String sel : select) {
                AuditZnsbRemoteHelp auditZnsbRemoteHelp = auditZnsbRemoteHelpService.getDetail(rowguid).getResult();
                int count = auditZnsbRemoteHelpService.getAuditZnsbRemoteHelpMachineguid(sel);
                if(count>0){
                    addCallbackParam("msg", "一体机已被绑定！");
                }else{
                    auditZnsbRemoteHelp.setMachineguid(sel);                 
                    auditZnsbRemoteHelpService.update(auditZnsbRemoteHelp);
                    addCallbackParam("msg", "保存一体机信息成功！");
                }
            }
            
        }
        
    
	
	
	
	
	public DataGridModel<AuditZnsbEquipment> getDataGridData() {

		// 获得表格对象
		if (model == null) {
			model = new DataGridModel<AuditZnsbEquipment>() {
				@Override
				public List<AuditZnsbEquipment> fetchData(int first, int pageSize, String sortField, String sortOrder) {
			
					 SqlConditionUtil sql = new SqlConditionUtil();				
					if (StringUtil.isNotBlank(getRequestParameter("guid"))
							&& getRequestParameter("guid").equals("NoPZ")) {
					
						sql.isBlank("centerguid");
					}
					else
					{
						sql.eq("centerguid", ZwfwUserSession.getInstance().getCenterGuid());
					}
					if (StringUtil.isNotBlank(machinename)) {				
						sql.like("machinename", machinename);
					}
					if (StringUtil.isNotBlank(machinetype)) {					
						 sql.eq("machinetype", machinetype);
					}
					if (StringUtil.isNotBlank(machineno)) {				
						 sql.eq("machineno", machineno);
					}
					if (StringUtil.isNotBlank(macaddress)) {				
						sql.eq("macaddress", macaddress);
					}
					
					sql.eq("machinetype", "3");
					sortField="machinetype asc,OperateDate";
					PageData<AuditZnsbEquipment> pageData = equipmentservice
			                .getEquipmentByPage(sql.getMap(), first,
							pageSize, sortField, sortOrder).getResult();
					this.setRowCount(pageData.getRowCount());
					return pageData.getList();
				}

			};
		}
		return model;

	}
	

	@SuppressWarnings("unchecked")
	public List<SelectItem> getMachinetypeModel() {
		if (machinetypeModel == null) {
			machinetypeModel = DataUtil.convertMap2ComboBox(
					(List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "设备类型", null, true));
		}
		return this.machinetypeModel;
	}

	public AuditZnsbEquipment getDataBean() {
		if (dataBean == null) {
			dataBean = new AuditZnsbEquipment();
		}
		return dataBean;
	}

	public void setDataBean(AuditZnsbEquipment dataBean) {
		this.dataBean = dataBean;
	}

	public String getMachinename() {
		return machinename;
	}

	public void setMachinename(String machinename) {
		this.machinename = machinename;
	}

	public String getMachineno() {
		return machineno;
	}

	public void setMachineno(String machineno) {
		this.machineno = machineno;
	}



	public String getMachinetype() {
		return machinetype;
	}

	public void setMachinetype(String machinetype) {
		this.machinetype = machinetype;
	}

	public String getMacaddress() {
		return macaddress;
	}

	public void setMacaddress(String macaddress) {
		this.macaddress = macaddress;
	}
}
