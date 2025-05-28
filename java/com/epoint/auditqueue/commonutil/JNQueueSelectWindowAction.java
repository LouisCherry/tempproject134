package com.epoint.auditqueue.commonutil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.auditorga.auditwindow.domain.AuditOrgaWindow;
import com.epoint.basic.auditorga.auditwindow.inter.IAuditOrgaWindowYjs;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.tree.LazyTreeModal9;
import com.epoint.basic.faces.tree.SimpleFetchHandler9;
import com.epoint.basic.faces.tree.TreeData;
import com.epoint.common.util.SqlConditionUtil;
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
@RestController("jnqueueselectwindowaction")
@Scope("request")
public class JNQueueSelectWindowAction extends BaseController {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Autowired
	private IAuditOrgaWindowYjs iwindow;
	@Autowired
	private IHandleFrameOU iHandleFrameOU;

	

	/**
	 * 部门树model
	 */
	private LazyTreeModal9 treeModel = null;
	/**
	 * 
	 */
	private String rtnValue;

	@Override
	public void pageLoad() {
	}

	@SuppressWarnings("serial")
	public LazyTreeModal9 getTreeModel() {
		if (treeModel == null) {
			treeModel = new LazyTreeModal9(new SimpleFetchHandler9() {
				@Override
				@SuppressWarnings({ "rawtypes", "unchecked" })
				public <T> List<T> search(String condition) {
					List list = new ArrayList();
					SqlConditionUtil sql = new SqlConditionUtil();
		            sql.in("Centerguid", "'4391ec2f-6903-4a1a-af2d-6ba281bc5884','46db0d30-b3ea-4d9c-8a66-771731e4b33a'");
					sql.like("windowname", condition);
					list = iwindow.getAllWindow(sql.getMap()).getResult();
					return list;
				}

				@Override
				@SuppressWarnings({ "rawtypes", "unchecked" })
				public <T> List<T> fetchData(int arg0, TreeData treeData) {
					List list = new ArrayList();

					if (StringUtil.isBlank(treeData.getObjectcode())) {
					    String ae = ZwfwUserSession.getInstance().getAreaCode();
						list = iHandleFrameOU.getWindowOU("370891").getResult();
						list.addAll(iHandleFrameOU.getWindowOU("370800").getResult());
					} else {
					    SqlConditionUtil sql = new SqlConditionUtil();
			            sql.in("Centerguid", "'4391ec2f-6903-4a1a-af2d-6ba281bc5884','46db0d30-b3ea-4d9c-8a66-771731e4b33a'");
					    sql.eq("ouguid", treeData.getObjectcode());

						list = iwindow.getAllWindow(sql.getMap()).getResult();
					}
					return list;
				}

				@Override
				public int fetchChildCount(TreeData treeData) {
				    SqlConditionUtil sql = new SqlConditionUtil();
				    sql.in("Centerguid", "'4391ec2f-6903-4a1a-af2d-6ba281bc5884','46db0d30-b3ea-4d9c-8a66-771731e4b33a'");
                    sql.eq("ouguid", treeData.getObjectcode());

					return iwindow.getAllWindow(sql.getMap()).getResult().size();
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
								treeDataList.add(treeData);
							}
							if (ob instanceof AuditOrgaWindow) {
								AuditOrgaWindow auditWindow = (AuditOrgaWindow) ob;
								TreeData treeData = new TreeData();
								treeData.setObjectcode(auditWindow.getRowguid());
								treeData.setTitle(auditWindow.getWindowname());
								treeData.setNoClick(false);
								treeDataList.add(treeData);
							}
						}
					}
					return treeDataList;
				}
			});
			treeModel.setRootName("所有窗口");
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

		List<FrameOu> frameOus = iHandleFrameOU.getWindowOU("370891").getResult();
		List<FrameOu> frameOus2 = iHandleFrameOU.getWindowOU("370800").getResult();
	   frameOus.addAll(frameOus2);
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
