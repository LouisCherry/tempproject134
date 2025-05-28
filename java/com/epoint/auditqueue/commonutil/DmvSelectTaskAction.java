package com.epoint.auditqueue.commonutil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.auditorga.auditwindow.domain.AuditOrgaWindow;
import com.epoint.basic.auditorga.auditwindow.inter.IAuditOrgaWindowYjs;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.tree.LazyTreeModal9;
import com.epoint.basic.faces.tree.SimpleFetchHandler9;
import com.epoint.basic.faces.tree.TreeData;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.composite.auditorga.handleframeou.IHandleFrameOU;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.organ.ou.entity.FrameOu;

/**
 * 
 * 政务服务选择窗口部门 注意点：在编辑页面跳转到该action的时候，需要传入两个参数，分别是guid和tablename,
 * guid表示XXX表的guid，tablename表示与ouguid关联的表名。主要的作用是用来回显部门。
 * 
 * @version [版本号, 2016年8月25日]
 */
@RestController("dmvselecttaskaction")
@Scope("request")
public class DmvSelectTaskAction extends BaseController {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Autowired
	private IAuditOrgaWindowYjs iwindow;
	
	@Autowired
	private IHandleFrameOU iHandleFrameOU;
	@Autowired
    private IAuditTask taskservice;
	/**
	 * 部门树model
	 */
	private LazyTreeModal9 treeModel = null;
	/**
	 * 
	 */
	private String rtnValue;
	private String areacode;
	@Override
	public void pageLoad() {
	    areacode = ZwfwUserSession.getInstance().getAreaCode();
	}

	@SuppressWarnings("serial")
	public LazyTreeModal9 getTreeModel() {
		if (treeModel == null) {
			treeModel = new LazyTreeModal9(new SimpleFetchHandler9() {
				@Override
				@SuppressWarnings({ "rawtypes", "unchecked" })
				public <T> List<T> search(String condition) {
				    List<AuditTask> list = new ArrayList<AuditTask>();
	                if (StringUtil.isNotBlank(condition)) {

	                    list = taskservice.selectAuditTaskByCondition(condition, areacode).getResult();
	                }
					return  (List<T>) list;
				}

				@Override
				@SuppressWarnings({ "rawtypes", "unchecked" })
				public <T> List<T> fetchData(int arg0, TreeData treeData) {
					List list = new ArrayList();

					if (StringUtil.isBlank(treeData.getObjectcode())) {
					    list = iHandleFrameOU.getWindowOUList(areacode, true).getResult();
					} else {
					  /*  SqlConditionUtil sql = new SqlConditionUtil();
                        sql.eq("OUGUID", treeData.getObjectcode());
                        sql.eq("ISTEMPLATE", ZwfwConstant.CONSTANT_STR_ZERO);
                        sql.eq("areacode", areacode);
                        sql.eq("IS_EDITAFTERIMPORT", ZwfwConstant.CONSTANT_STR_ONE);
                        sql.eq("IS_ENABLE", ZwfwConstant.CONSTANT_STR_ONE);
                        sql.isBlankOrValue("IS_HISTORY", ZwfwConstant.CONSTANT_STR_ZERO);
                        sql.rightLike("item_id", "000");
                        Map<String, String> a = sql.getMap();*/
                        list = taskservice.selectAuditTaskOuByObjectGuid(treeData.getObjectcode(), areacode)
                                .getResult();
					}
					return list;
				}

				@Override
				public int fetchChildCount(TreeData treeData) {
				    int childCount = 0;
				    //system.out.println(treeData.getObjectcode());
                        childCount = taskservice.selectAuditTaskOuByObjectGuid(treeData.getObjectcode(), areacode)
                                .getResult().size();
                        //system.out.println(childCount);
                        return childCount;
                    
				}

				@Override
				public List<TreeData> changeDBListToTreeDataList(List<?> objlist) {
					List<TreeData> treeDataList = new ArrayList<TreeData>();
					if (objlist != null && objlist.size() > 0) {
						for (Object ob : objlist) {
							if (ob instanceof FrameOu) {
								FrameOu frameOu = (FrameOu) ob;
								TreeData treeData = new TreeData();
								treeData.setObjectcode(frameOu.getOuguid());
								treeData.setTitle(frameOu.getOuname());
								treeData.setNoClick(true);
								// 没有子节点的不加载
								SqlConditionUtil sql = new SqlConditionUtil();
								sql.eq("ouguid", frameOu.getOuguid());
								
								int windowcount =  taskservice.selectAuditTaskOuByObjectGuid(frameOu.getOuguid(), areacode)
	                                .getResult().size();
								if (windowcount > 0) {
									treeDataList.add(treeData);
								}
							}
							if (ob instanceof AuditTask) {
							    AuditTask auditWindow = (AuditTask) ob;
								TreeData treeData = new TreeData();
								treeData.setObjectcode(auditWindow.getTask_id());
								treeData.setTitle(auditWindow.getTaskname());
								treeData.setNoClick(false);
								treeDataList.add(treeData);
							}
						}
					}
					return treeDataList;
				}
			});
			treeModel.setRootName("所有事项");
			treeModel.setRootSelect(false);
			treeModel.setSelectNode(null);
		}
		return treeModel;
	}

	public void dealTextName(String allNames) {
		// 数组转List
		String[] names = allNames.split(",");
		List<String> namesList = new ArrayList<String>();
		Collections.addAll(namesList, names);

		List<FrameOu> frameOus = iHandleFrameOU.getWindowOUList(ZwfwUserSession.getInstance().getAreaCode(), true)
				.getResult();
		for (FrameOu string : frameOus) {
			namesList.removeIf(name -> {
				return name.equals(string.getOuname());
			});
		}
		addCallbackParam("rtnValue", namesList);
	}

	public String getRtnValue() {
		return rtnValue;
	}

	public void setRtnValue(String rtnValue) {
		this.rtnValue = rtnValue;
	}

}
