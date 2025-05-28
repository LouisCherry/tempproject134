package com.epoint.auditperformance.auditperformancerecordobject.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.auditorga.auditwindow.domain.AuditOrgaWindow;
import com.epoint.basic.auditorga.auditwindow.inter.IAuditOrgaWindow;
import com.epoint.basic.auditperformance.auditperformancerecordobject.api.IAuditPerformanceRecordObject;
import com.epoint.basic.auditperformance.auditperformancerecordobject.domain.AuditPerformanceRecordObject;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.tree.ConstValue9;
import com.epoint.basic.faces.tree.FetchHandler9;
import com.epoint.basic.faces.tree.LazyTreeModal9;
import com.epoint.basic.faces.tree.SimpleFetchHandler9;
import com.epoint.basic.faces.tree.TreeData;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.utils.json.JsonUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.organ.ou.api.IOuService;
import com.epoint.frame.service.organ.ou.entity.FrameOu;
import com.epoint.frame.service.organ.ou.entity.FrameOuExtendInfo;

/**
 * 考评记录考核窗口list页面对应的后台
 * 
 * @author 14408
 * @version [版本号, 2018-01-09 16:33:26]
 */
@RestController("auditperformancerecordwindowlistaction")
@Scope("request")
public class AuditPerformanceRecordWindowListAction extends BaseController
{
    /**
     * 
     */
    private static final long serialVersionUID = -4632640761998959244L;
    /**
     * 窗口人员model
     */
    private LazyTreeModal9 treeModel = null;
    /**
     * 对象类型标识
     */
    private String recordguid = null;
    /**
     * 页面文本框中已选的对象
     */
    private String obj = null;
    

    

    @Autowired
    private IOuService ouService ;
    @Autowired
    private IAuditOrgaWindow auditWindowImpl ;
    @Autowired
    IAuditPerformanceRecordObject objectservice;

    @Override
    public void pageLoad() {
        recordguid = getRequestParameter("guid");
        //这个地方获取一下
        String cmdParams = getRequestParameter("cmdParams");
        Map<String, Object> mapparams = JsonUtil.jsonToMap(cmdParams);
        obj = String.valueOf(mapparams.get("obj"));
        List<SelectItem> windowItemList = new ArrayList<SelectItem>();
        
        if (StringUtil.isNotBlank(obj)) {
            String[] userStringArray = obj.split("_");
            String[] nameList = userStringArray[0].split(";");
            String[] idList = userStringArray[1].split(";");
            for (int i = 0; i < nameList.length; i++) {
                windowItemList.add(new SelectItem(idList[i], nameList[i]));
            }
            getTreeModel().setSelectNode(windowItemList);
        }
    }

    /***
     * 
     * 用treebean构造人员树
     * 
     * @return LazyTreeModal9
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public LazyTreeModal9 getTreeModel() {
        if (treeModel == null) {
            treeModel = new LazyTreeModal9(loadAllWindow());
            if (StringUtil.isNotBlank(recordguid)) {
                treeModel.setSelectNode(getSelectWindow(recordguid));
            }
            treeModel.setTreeType(ConstValue9.CHECK_SINGLE);
            treeModel.setInitType(ConstValue9.GUID_INIT);
            treeModel.setRootName("所有窗口");
            treeModel.setRootSelect(true);
        }
        return treeModel;
    }

    /**
     * 
     * 获取窗口人员树
     * 
     * @param windowGuid
     *            窗口guid
     * @return LazyTreeModal9
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */

    public SimpleFetchHandler9 loadAllWindow() {
        SimpleFetchHandler9 fetchHandler9 = new SimpleFetchHandler9()
        {

            /**
             * 
             */
            private static final long serialVersionUID = 1L;

            @Override
            @SuppressWarnings({"rawtypes", "unchecked" })
            public <T> List<T> search(String conndition) {
                List listwindow = new ArrayList<>();
                if (StringUtil.isNotBlank(conndition)) {
                    listwindow.addAll(auditWindowImpl
                            .selectByCenter(conndition, ZwfwUserSession.getInstance().getCenterGuid()).getResult());
                }
                return listwindow;
            }

            @Override
            @SuppressWarnings({"rawtypes", "unchecked" })
            public <T> List<T> fetchData(int level, TreeData treeData) {
                List list = new ArrayList();
                // 一开始加载或者点击节点的时候触发
                if (level == FetchHandler9.FETCH_ONELEVEL) {
                    list = auditWindowImpl.getAllByCenter(ZwfwUserSession.getInstance().getCenterGuid()).getResult();
                }
                // 点击checkbox的时候触发
                else {
                    // 如果点击的是部门的checkbox，则返回该部门下所有的人员的list
                    if ("frameou".equals(treeData.getObjectcode())) {
                        list = auditWindowImpl.getWindowListByOU(treeData.getObjectGuid()).getResult();
                        List<FrameOu> ouList = ouService.listOUByGuid(treeData.getObjectGuid(), 3);
                        if(ouList!=null){
                            ouList.removeIf(ou -> {
                                FrameOuExtendInfo ouExt = ouService.getFrameOuExtendInfo(ou.getOuguid());
                                if (StringUtil.isNotBlank(ouExt.get("is_windowou"))
                                        && "1".equals(ouExt.get("is_windowou").toString())) {
                                    return false;
                                }
                                else {
                                    return true;
                                }
                            });
                        }
                        if (ouList != null && ouList.size() > 0) {
                            for (FrameOu ou : ouList) {
                                list.add(ou);
                            }
                        }
                    }

                }
                return list;
            }

            @Override
            public int fetchChildCount(TreeData treeData) {
                int oucount = 0;
                int windowcount = 0;
                oucount = ouService.listOUByGuid(treeData.getObjectGuid(), 5).size();
                windowcount = auditWindowImpl.getWindowListByOU(treeData.getObjectGuid()).getResult().size();
                if (oucount > 0 || windowcount > 0) {
                    return 1;
                }
                else {
                    return 0;
                }
            }

            @Override
            public List<TreeData> changeDBListToTreeDataList(List<?> dataList) {
                List<TreeData> treeList = new ArrayList<TreeData>();
                if (dataList != null) {
                    for (Object obj : dataList) {
                        // 将dataList转化为frameou的list
                        if (obj instanceof FrameOu) {
                            FrameOu frameOu = (FrameOu) obj;
                            TreeData treeData = new TreeData();
                            treeData.setObjectGuid(frameOu.getOuguid());
                            treeData.setTitle(frameOu.getOuname());
                            // 没有子节点的不允许点击
                            List<AuditOrgaWindow> auditOrgaWindow = auditWindowImpl
                                    .getWindowListByOU(treeData.getObjectGuid()).getResult();
                            int childCount = 0;
                            if (auditOrgaWindow != null) {
                                childCount = auditOrgaWindow.size();
                            }
                            if (childCount == 0) {
                                treeData.setNoClick(true);
                            }
                            else {
                                treeData.setObjectcode("frameou");
                                treeList.add(treeData);
                            }
                        }
                        if (obj instanceof AuditOrgaWindow) {
                            AuditOrgaWindow auditOrgaWindow = (AuditOrgaWindow) obj;
                            TreeData treeData = new TreeData();
                            treeData.setObjectGuid(auditOrgaWindow.getRowguid());
                            treeData.setTitle(auditOrgaWindow.getWindowname());
                            treeData.setObjectcode("window");
                            treeList.add(treeData);
                        }
                    }
                }

                return treeList;
            }
        };
        return fetchHandler9;
    }

    public List<SelectItem> getSelectWindow(String recordguid) {
        List<SelectItem> windowItemList = new ArrayList<SelectItem>();
        if (StringUtil.isNotBlank(recordguid)) {
            List<AuditPerformanceRecordObject> lists = objectservice
                    .findFieldByRecordRowguid("objectname,objectguid", recordguid, "2").getResult();
            if (lists != null && lists.size() > 0) {
                for (AuditPerformanceRecordObject list : lists) {
                    windowItemList.add(new SelectItem(list.getObjectguid(), list.getObjectname()));
                }
            }
        }
        return windowItemList;
    }
}
