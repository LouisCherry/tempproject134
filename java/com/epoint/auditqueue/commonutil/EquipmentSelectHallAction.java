package com.epoint.auditqueue.commonutil;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.auditorga.audithall.domain.AuditOrgaHall;
import com.epoint.basic.auditorga.audithall.inter.IAuditOrgaHall;
import com.epoint.basic.auditorga.auditwindow.inter.IAuditOrgaWindowYjs;
import com.epoint.basic.auditqueue.auditqueuetasktype.domain.AuditQueueTasktype;
import com.epoint.basic.auditqueue.auditqueuetasktype.inter.IAuditQueueTasktype;
import com.epoint.basic.auditqueue.auditqueuewindowtasktype.domain.AuditQueueWindowTasktype;
import com.epoint.basic.auditqueue.auditqueuewindowtasktype.inter.IAuditQueueWindowTasktype;
import com.epoint.basic.auditqueue.auditznsbequipment.domain.AuditZnsbEquipment;
import com.epoint.basic.auditqueue.auditznsbequipment.inter.IAuditZnsbEquipment;
import com.epoint.basic.authentication.UserSession;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.tree.ConstValue9;
import com.epoint.basic.faces.tree.FetchHandler9;
import com.epoint.basic.faces.tree.LazyTreeModal9;
import com.epoint.basic.faces.tree.SimpleFetchHandler9;
import com.epoint.basic.faces.tree.TreeData;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.composite.auditorga.handleframeou.IHandleFrameOU;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.organ.ou.entity.FrameOu;

/**
 * 窗口事项配置页面对应的后台
 * 
 * @author wry
 * @version [版本号, 2016-09-28 15:18:27]
 */

@RestController("EquipmentSelectHallAction")
@Scope("request")
public class EquipmentSelectHallAction extends BaseController {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4632640761998959244L;


	@Autowired
    private IAuditOrgaHall ihall;
	@Autowired
	private IAuditZnsbEquipment equipmentservice;
	/**
	 * 窗口与事项分类的关联实体对象
	 */
	private AuditQueueWindowTasktype dataBean = null;
	/**
	 * 窗口人员model
	 */
	private LazyTreeModal9 treeModel = null;
	/**
	 * 窗口标识
	 */
	private String windowGuid = null;

	@Override
	public void pageLoad() {
		windowGuid = getRequestParameter("guid");

		
	}

	/***
	 * 
	 * 用treebean构造事项树
	 * 
	 * @return
	 * 
	 * 
	 */
	public LazyTreeModal9 getTreeModel() {
		if (treeModel == null) {
			treeModel = new LazyTreeModal9(loadAllTask());
			treeModel.setSelectNode(this.getSelectTasktype(windowGuid));
			treeModel.setTreeType(ConstValue9.CHECK_SINGLE);
			treeModel.setInitType(ConstValue9.GUID_INIT);
			treeModel.setRootName("获取大厅");
			treeModel.setRootSelect(false);
		}
		return treeModel;
	}

	/**
	 * 
	 * 使用SimpleFetchHandler9构造树
	 * 
	 * @return
	 * 
	 * 
	 */
	private SimpleFetchHandler9 loadAllTask() {
		SimpleFetchHandler9 fetchHandler9 = new SimpleFetchHandler9() {
			private static final long serialVersionUID = 1L;

			@Override
			@SuppressWarnings({ "rawtypes", "unchecked" })
			public <T> List<T> search(String conndition) {
				List list = new ArrayList();
				if (StringUtil.isNotBlank(conndition)) {
					list.clear();
					list = selectTasktypeByCondition(conndition);
				}
				return list;
			}

			/**
			 * 窗口配置事项分类根据条件查询事项分类
			 * 
			 * @param condition
			 * @return
			 * 
			 * 
			 */
			public List<AuditOrgaHall> selectTasktypeByCondition(String condition) {
			
				Map<String,String> s = new HashMap<String,String>(); 
				s.put("Centerguid=", ZwfwUserSession.getInstance().getCenterGuid());
                if(StringUtil.isNotBlank(condition)){
                      s.put("hallnameLike", condition);
                 }
                  return ihall.getAuditHallByPage(s, 0, 1000, "ordernum", "desc").getResult();
			}

			@Override
			@SuppressWarnings({ "rawtypes", "unchecked" })
			public <T> List<T> fetchData(int level, TreeData treeData) {
				List list = new ArrayList();
				// 一开始加载或者点击节点的时候触发
				if (level == FetchHandler9.FETCH_ONELEVEL) {
			        Map<String,String> s = new HashMap<String,String>(); 
	                s.put("Centerguid=", ZwfwUserSession.getInstance().getCenterGuid());
	               
	                list = ihall.getAuditHallByPage(s, 0, 1000, "ordernum", "desc").getResult();
					
				
					
				}
				// 点击checkbox的时候触发
				else {
					// 如果点击的是部门的checkbox，则返回该部门下所有的事项的list
				
						
						Map<String,String> s = new HashMap<String,String>(); 
		                s.put("rowguid=", treeData.getObjectGuid());
		                list = ihall.getAuditHallByPage(s, 0, 1000, "ordernum", "desc").getResult();
				}
				return list;
			}

			@Override
			public int fetchChildCount(TreeData treeData) {
				
				return 0;

			}

			@Override
			public List<TreeData> changeDBListToTreeDataList(List<?> dataList) {
				List<TreeData> treeList = new ArrayList<TreeData>();
				if (dataList != null) {
					for (Object obj : dataList) {
						// 将dataList转化为frameou的list
					
					        AuditOrgaHall frameOu = (AuditOrgaHall) obj;
							TreeData treeData = new TreeData();
							treeData.setObjectGuid(frameOu.getRowguid());
							treeData.setTitle(frameOu.getHallname());
							treeList.add(treeData);
					
							
						
			
						
					}
				}
				return treeList;
			}
		};
		return fetchHandler9;
	}

	/**
	 * 根据windowguid获取事项分类SelectItem，初始化事项分类信息
	 * 
	 * @param windowGuid
	 * @return
	 * 
	 * 
	 */
	public List<SelectItem> getSelectTasktype(String windowGuid) {
		List<SelectItem> showtasktypeList = new ArrayList<SelectItem>();
		if (StringUtil.isNotBlank(windowGuid)) {

			List<AuditOrgaHall> tasktypeList = new ArrayList<AuditOrgaHall>();
			if (StringUtil.isNotBlank(windowGuid)) {
			    String hallguid =   equipmentservice.getEquipmentByRowguid(windowGuid).getResult().getHallguid();
				if(StringUtil.isNotBlank(hallguid)){
				    Map<String,String> s = new HashMap<String,String>(); 
				    if(!"all".equals(hallguid)){
				        hallguid= hallguid.replace(",", "','");
	                    s.put("rowguidIN","'"+ hallguid+"'");
				    }else{
				        s.put("Centerguid=", ZwfwUserSession.getInstance().getCenterGuid());
				    }
				    
				    tasktypeList = ihall.getAuditHallByPage(s, 0, 1000, "ordernum", "desc").getResult();
				}
			}
			if (tasktypeList != null && tasktypeList.size() > 0) {
				for (AuditOrgaHall tasktype : tasktypeList) {
					showtasktypeList.add(new SelectItem(tasktype.getRowguid(), tasktype.getHallname()));
				}
			}
		}
		return showtasktypeList;
	}

	/**
	 * 
	 * 把事项和窗口信息保存到关系表里面
	 * 
	 * @param guidList
	 *            前台获取的事项guid 用“;”分割
	 * @param windowGuid
	 *            窗口guid
	 * 
	 * 
	 */
	public void saveTasktypeToWindow(String tasktypeguidList, String windowGuid) {
		// 添加新的事项关联关系
		if (StringUtil.isNotBlank(windowGuid)) {
			

			if (StringUtil.isNotBlank(tasktypeguidList)) {
				String taskGuids = tasktypeguidList.replace(";", ",");
				AuditZnsbEquipment equipment = new AuditZnsbEquipment();
				equipment.setRowguid(windowGuid);
				equipment.setHallguid(taskGuids);
				equipmentservice.updateAuditZnsbEquipment(equipment);
			}
		}

	}

	public String getWindowGuid() {
		return windowGuid;
	}

	public void setWindowGuid(String windowGuid) {
		this.windowGuid = windowGuid;
	}

	public AuditQueueWindowTasktype getDataBean() {
		return dataBean;
	}

	public void setDataBean(AuditQueueWindowTasktype dataBean) {
		this.dataBean = dataBean;
	}

}
