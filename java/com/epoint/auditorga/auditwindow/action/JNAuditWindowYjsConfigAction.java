package com.epoint.auditorga.auditwindow.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.auditorga.auditwindow.entiy.AuditOrgaWindowYjs;
import com.epoint.basic.auditorga.auditarea.domain.AuditOrgaArea;
import com.epoint.basic.auditorga.auditwindow.domain.AuditOrgaWindowTask;
import com.epoint.basic.auditorga.auditwindow.inter.IAuditOrgaWindowYjs;
import com.epoint.basic.auditsp.auditspbusiness.domain.AuditSpBusiness;
import com.epoint.basic.auditsp.auditspbusiness.inter.IAuditSpBusiness;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.tree.ConstValue9;
import com.epoint.basic.faces.tree.FetchHandler9;
import com.epoint.basic.faces.tree.LazyTreeModal9;
import com.epoint.basic.faces.tree.SimpleFetchHandler9;
import com.epoint.basic.faces.tree.TreeData;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.core.EpointFrameDsManager;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.utils.string.StringUtil;

/**
 * 窗口事项配置页面对应的后台
 * 
 * @author wry
 * @version [版本号, 2016-09-28 15:18:27]
 */
@RestController("jnauditwindowyjsconfigaction")
@Scope("request")
public class JNAuditWindowYjsConfigAction extends BaseController {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4632640761998959244L;
	@Autowired
	private IAuditOrgaWindowYjs auditWindowImpl;
	@Autowired
    private IAuditSpBusiness iAuditSpBusiness;
	
	/**
	 * 窗口与事项的关联实体对象
	 */
	private AuditOrgaWindowTask dataBean = null;
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
	 * @exception/throws [违例类型] [违例说明]
	 * @see [类、类#方法、类#成员]
	 */
	public LazyTreeModal9 getTreeModel() {
	   
		if (treeModel == null) {
			treeModel = new LazyTreeModal9(loadAllTask());
			if (!isPostback()) {
				treeModel.setSelectNode(this.getSelectTask(windowGuid));
			}
			treeModel.setTreeType(ConstValue9.CHECK_SINGLE);
			// treeModel.setInitType(ConstValue9.GUID_INIT);
			treeModel.setRootName("辖区列表");
			treeModel.setRootSelect(false);
		}
		return treeModel;
	}

	/**
	 * 
	 * 使用SimpleFetchHandler9构造树
	 * 
	 * @return
	 * @exception/throws [违例类型] [违例说明]
	 * @see [类、类#方法、类#成员]
	 */
	private SimpleFetchHandler9 loadAllTask() {

		SimpleFetchHandler9 fetchHandler9 = new SimpleFetchHandler9() {
		  

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			@SuppressWarnings({ "rawtypes", "unchecked" })
			public <T> List<T> search(String conndition) {
				List list = new ArrayList();
				if (StringUtil.isNotBlank(conndition)) {
					list = auditWindowImpl.getBusinessByAreacode(ZwfwUserSession.getInstance().getAreaCode(),conndition);
				}
				return list;
			}

			@Override
			@SuppressWarnings({ "rawtypes", "unchecked" })
			public <T> List<T> fetchData(int level, TreeData treeData) {
				List list = new ArrayList();
				// 一开始加载或者点击节点的时候触发
				if (level == FetchHandler9.FETCH_ONELEVEL) {
					// 最开始加载的时候，把所有的窗口部门加载出来，最开始treeData的guid为空
					if (StringUtil.isBlank(treeData.getObjectGuid())) {
						list = auditWindowImpl.getAreacodeList(ZwfwUserSession.getInstance().getAreaCode());
						// 如果treeData的guid不为空，则说明该节点下面有子节点，获取该窗口部门下的所有事项
					} else {
						// list =
						// auditWindowTaskBizlogic.selectAuditTaskOuByObjectGuid(treeData.getObjectGuid());
						// 如果点击的是部门的checkbox，则返回该部门下所有的事项的list
						if ("auditorgaarea".equals(treeData.getObjectcode())) {
							list = auditWindowImpl.getBusinessDetailByAreacode(treeData.getObjectGuid());
						}
						// 如果点击的是事项的checkbox，则返回该是事项的list
						else if ("auditspbusiness".equals(treeData.getObjectcode())) {
							list = auditWindowImpl.getBusinessDetailByRowguid(treeData.getObjectGuid());
						}
					}
					
				}
				// 点击checkbox的时候触发
				else {
					// 如果点击的是部门的checkbox，则返回该部门下所有的事项的list
					if ("auditorgaarea".equals(treeData.getObjectcode())) {
						list = auditWindowImpl.getBusinessDetailByAreacode(treeData.getObjectGuid());
					}
					// 如果点击的是事项的checkbox，则返回该是事项的list
					else if ("auditspbusiness".equals(treeData.getObjectcode())) {
						list = auditWindowImpl.getBusinessDetailByRowguid(treeData.getObjectGuid());
					}
				}
				return list;
			}

			@Override
			public int fetchChildCount(TreeData treeDate) {
				return auditWindowImpl.getAreacodeList(ZwfwUserSession.getInstance().getAreaCode()).size();
			}

			@Override
			public List<TreeData> changeDBListToTreeDataList(List<?> dataList) {
				List<TreeData> treeList = new ArrayList<TreeData>();
				if (dataList != null) {
					for (Object obj : dataList) { 
						if (obj instanceof AuditOrgaArea) {
							AuditOrgaArea area = (AuditOrgaArea) obj;
							TreeData treeData = new TreeData();
							treeData.setObjectGuid(area.getXiaqucode());
							treeData.setTitle(area.getXiaquname());
							treeData.setObjectcode("auditorgaarea");
							treeList.add(treeData);
						}
						if (obj instanceof AuditSpBusiness) {
							AuditSpBusiness business = (AuditSpBusiness) obj;
							TreeData treeData = new TreeData();
							treeData.setObjectGuid(business.getRowguid());
							treeData.setTitle(business.getBusinessname());
							treeData.setObjectcode("auditspbusiness");
							treeList.add(treeData);
						}
					}
				}
				return treeList;
			}
		};
		return fetchHandler9;
	
		
	}

	/**
	 * 根据windowguid获取事项SelectItem，初始化事项信息
	 * 
	 * @param windowGuid
	 * @return
	 * @exception/throws [违例类型] [违例说明]
	 * @see [类、类#方法、类#成员]
	 */
	private List<SelectItem> getSelectTask(String windowGuid) {
		List<SelectItem> auditTaskList = new ArrayList<SelectItem>();
		if (StringUtil.isNotBlank(windowGuid)) {
			List<AuditSpBusiness> taskList = getTaskByWindow(windowGuid);
			if (taskList != null && taskList.size() > 0) {
				for (AuditSpBusiness auditTask : taskList) {
					auditTaskList.add(new SelectItem(auditTask.getRowguid(),
							auditTask.getBusinessname().replace(",", "，")));
				}
			}
		}
		
		return auditTaskList;
	}

	/**
	 * 
	 * 把事项和窗口信息保存到关系表里面
	 * 
	 * @param guidList
	 *            前台获取的事项guid 用“;”分割
	 * @param windowGuid
	 *            窗口guid
	 * @exception/throws [违例类型] [违例说明]
	 * @see [类、类#方法、类#成员]
	 */
	public void saveTaskToWindow(String guidList, String windowGuid) {
		try {
			if (StringUtil.isNotBlank(windowGuid)) {
				EpointFrameDsManager.begin(null);
				log.info("删除窗口："+windowGuid+",操作人："+userSession.getDisplayName());
				auditWindowImpl.deleteWindowTaskByWindowGuid(windowGuid);
				// 添加新的事项关联关系
				if (StringUtil.isNotBlank(guidList)) {
					String[] taskGuids = guidList.split(";");
					if (StringUtil.isNotBlank(windowGuid)) {
						for (int i = 0; i < taskGuids.length; i++) {
								AuditOrgaWindowYjs auditWindowTask = new AuditOrgaWindowYjs();
								auditWindowTask.setRowguid(UUID.randomUUID().toString());
								auditWindowTask.setWindowguid(windowGuid);
								auditWindowTask.setTaskguid(taskGuids[i]);
								auditWindowTask.setOperatedate(new Date());
								auditWindowTask.setOrdernum(i);
								auditWindowTask.setTaskid(taskGuids[i]);
								auditWindowTask.setEnabled("1");// 插入的数据默认为有效
								auditWindowImpl.insertWindowTask(auditWindowTask);

						}
					}
				}
				EpointFrameDsManager.commit();
			}
		} catch (Exception e) {
			EpointFrameDsManager.rollback();
			e.printStackTrace();
		}
	}

	public String getWindowGuid() {
		return windowGuid;
	}

	public void setWindowGuid(String windowGuid) {
		this.windowGuid = windowGuid;
	}

	public AuditOrgaWindowTask getDataBean() {
		return dataBean;
	}

	public void setDataBean(AuditOrgaWindowTask dataBean) {
		this.dataBean = dataBean;
	}

	/**
	 * 
	 * 根据窗口guid获取事项列表
	 * 
	 * @param windowGuid
	 *            事项guid
	 * @return List<AuditTask>
	 * @see [类、类#方法、类#成员]
	 */
	public List<AuditSpBusiness> getTaskByWindow(String windowGuid) {
		List<AuditSpBusiness> taskList = new ArrayList<AuditSpBusiness>();
		if (StringUtil.isNotBlank(windowGuid)) {
			List<AuditOrgaWindowYjs> auditWindowList = auditWindowImpl.getTaskByWindow(windowGuid).getResult();
			if (auditWindowList != null && auditWindowList.size() > 0) {
				for (AuditOrgaWindowYjs auditWindowTask : auditWindowList) {
					
					AuditSpBusiness business = iAuditSpBusiness.getAuditSpBusinessByRowguid(auditWindowTask.getTaskid()).getResult();

					if (business != null && "0".equals(business.getDel())) {
						taskList.add(business);
					}
				}
			}
		}
		return taskList;
	}

}
