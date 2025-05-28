package com.epoint.auditperformance.auditperformancerecordobject.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.auditperformance.auditperformancerecordobject.api.IAuditPerformanceRecordObject;
import com.epoint.basic.auditperformance.auditperformancerecordobject.domain.AuditPerformanceRecordObject;
import com.epoint.basic.auditorga.auditcenter.inter.IAuditOrgaServiceCenter;
import com.epoint.basic.auditorga.auditwindow.domain.AuditOrgaWindow;
import com.epoint.basic.auditorga.auditwindow.inter.IAuditOrgaWindow;
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
import com.epoint.frame.service.organ.user.api.IUserService;
import com.epoint.frame.service.organ.user.entity.FrameUser;
import com.epoint.frame.service.organ.user.entity.ViewFrameUser;

/**
 * 考评记录考核对象list页面对应的后台
 * 
 * @author 14408
 * @version [版本号, 2018-01-09 16:33:26]
 */
@RestController("auditperformancerecorduserlistaction")
@Scope("request")
public class AuditPerformanceRecordUserListAction extends BaseController
{

    /**
     * 
     */
    private static final long serialVersionUID = -6720503910593188651L;
    /**
     * 用户树model
     */
    private LazyTreeModal9 treeModel = null;
    @Autowired
    private IOuService ouService;
    @Autowired
    IAuditPerformanceRecordObject objectservice;
    @Autowired
    private IUserService userService;
    @Autowired
    private IAuditOrgaServiceCenter centerservice;
    @Autowired
    private IAuditOrgaWindow auditOrgaWindowService;

    private String recordguid;
    /**
     * 页面文本框中已选的对象
     */
    private String obj = null;

    private String ouguid;

    @Override
    public void pageLoad() {
        recordguid = getRequestParameter("rowguid");
        String cmdParams = getRequestParameter("cmdParams");
        Map<String, Object> mapparams = JsonUtil.jsonToMap(cmdParams);
        obj = String.valueOf(mapparams.get("obj"));
        ouguid = centerservice.findAuditServiceCenterByGuid(ZwfwUserSession.getInstance().getCenterGuid()).getResult()
                .getOuguid();
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

    public LazyTreeModal9 getTreeModel() {
        if (treeModel == null) {
            String tabKey = requestContext.getReq().getParameter("tabKey");
            if ("allUser".equals(tabKey)) {
                treeModel = new LazyTreeModal9(loadAllUser());
                if (StringUtil.isNotBlank(recordguid)) {
                    treeModel.setSelectNode(getSelectWindow(recordguid));
                }
                treeModel.setTreeType(ConstValue9.CHECK_SINGLE);
                treeModel.setInitType(ConstValue9.GUID_INIT);
                treeModel.setRootName("所有窗口部门");
                treeModel.setRootSelect(true);
            }
            else if (StringUtil.isBlank(tabKey) || "ouUser".equals(tabKey)) {
                // 本部门人员
                treeModel = new LazyTreeModal9(loadCurrentOu());
                if (StringUtil.isNotBlank(recordguid)) {
                    treeModel.setSelectNode(getSelectWindow(recordguid));
                }
                treeModel.setTreeType(ConstValue9.CHECK_SINGLE);
                treeModel.setInitType(ConstValue9.GUID_INIT);
                treeModel.setRootSelect(true);
                treeModel.setRootName("本中心下人员");
            }
        }
        return treeModel;
    }

    /**
     * 加载本部门
     * 
     * @return
     */
    @SuppressWarnings("serial")
    public SimpleFetchHandler9 loadCurrentOu() {

        SimpleFetchHandler9 fetchHandler = new SimpleFetchHandler9()
        {

            @Override
            @SuppressWarnings({"rawtypes", "unchecked" })
            public <T> List<T> search(String conndition) {
                List<FrameOu> list = new ArrayList();
                List listuser = new ArrayList<>();
                if (StringUtil.isNotBlank(conndition)) {
                    //找到中心对应部门
                    FrameOu centerou = ouService.getOuByOuGuid(ouguid);
                    list.add(centerou);
                    if (list != null && list.size() > 0) {
                        for (FrameOu frameou : list) {
                            List<ViewFrameUser> userList = userService.paginatorUserViewByOuGuid(frameou.getOuguid(),
                                    conndition, "", "", "", "", 0, 100, "", "", true, true).getList();
                            if (!listuser.containsAll(userList)) {
                                listuser.addAll(userList);
                            }
                        }
                    }
                }
                return listuser;
            }

            @SuppressWarnings({"unchecked", "rawtypes" })
            @Override
            public <T> List<T> fetchData(int level, TreeData obj) {
                List oulist = new ArrayList<>();
                if (obj != null) {
                    String ouguid = centerservice
                            .findAuditServiceCenterByGuid(ZwfwUserSession.getInstance().getCenterGuid()).getResult()
                            .getOuguid();
                    oulist.addAll(userService.listUserByOuGuid(ouguid, "", "", "", false, true, true, 1));
                }
                return oulist;
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
                            List<FrameUser> frameUsers = userService.listUserByOuGuid(frameOu.getOuguid(), "", "", "",
                                    false, true, true, 3);
                            int childCount = 0;
                            if (frameUsers != null) {
                                childCount = frameUsers.size();
                            }
                            if (childCount == 0) {
                                treeData.setNoClick(true);
                            }
                            else {
                                treeData.setObjectcode("frameou");
                                treeList.add(treeData);
                            }
                        }
                        // 将dataList转化为AuditTask的list
                        if (obj instanceof FrameUser) {
                            FrameUser frameuser = (FrameUser) obj;
                            TreeData treeData = new TreeData();
                            treeData.setObjectGuid(frameuser.getUserGuid());
                            treeData.setTitle(frameuser.getDisplayName());
                            treeData.setObjectcode("frameuser");
                            treeList.add(treeData);
                        }
                        if (obj instanceof ViewFrameUser) {
                            ViewFrameUser frameuser = (ViewFrameUser) obj;
                            TreeData treeData = new TreeData();
                            treeData.setObjectGuid(frameuser.getUserGuid());
                            treeData.setTitle(frameuser.getDisplayName() + "(" + frameuser.getOuname() + ")");
                            treeData.setObjectcode("frameuser");
                            treeList.add(treeData);
                        }
                    }
                }

                return treeList;
            }

            @Override
            public int fetchChildCount(TreeData treeData) {
                int oucount = 0;
                int usercount = 0;
                oucount = ouService.listOUByGuid(treeData.getObjectGuid(), 5).size();
                usercount = userService.getUserCountByOuGuid(treeData.getObjectGuid(), "", "", "", false, false, false,
                        1);
                if (oucount > 0 || usercount > 0) {
                    return 1;
                }
                else {
                    return 0;
                }
            }
        };
        return fetchHandler;
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

    public SimpleFetchHandler9 loadAllUser() {
        SimpleFetchHandler9 fetchHandler9 = new SimpleFetchHandler9()
        {

            /**
             * 
             */
            private static final long serialVersionUID = 1L;

            @Override
            @SuppressWarnings({"rawtypes", "unchecked" })
            public <T> List<T> search(String conndition) {
                List<FrameOu> list = new ArrayList();
                List listuser = new ArrayList<>();
                if (StringUtil.isNotBlank(conndition)) {
                    List<String> ouguids = auditOrgaWindowService
                            .getoulistBycenterguid(ZwfwUserSession.getInstance().getCenterGuid()).getResult();
                    for (String string : ouguids) {
                        //去除没有人员的部门节点
                        List<Object> childlist = new ArrayList<Object>();
                        List<FrameUser> users =  userService.listUserByOuGuid(string, "", "", "", false, true, true, 1);
                        filterWindowUser(childlist,users);
                        if(childlist.size()>0){
                            list.add(ouService.getOuByOuGuid(string));                                    
                        };
                    }
                    if (list != null && list.size() > 0) {
                        for (FrameOu frameou : list) {
                            List<FrameUser> users = userService.listUserByOuGuid(frameou.getOuguid(), "", "", "", false,
                                    true, true, 1);
                            if (users != null) { //获得部门下分配到该中心下的窗口的人员
                                for (FrameUser user : users) {
                                    if (user.getDisplayName().contains(conndition)) {
                                        List<AuditOrgaWindow> auditOrgaWindows = auditOrgaWindowService
                                                .getWindowListByUserGuid(user.getUserGuid()).getResult();
                                        if (auditOrgaWindows != null) {
                                            for (AuditOrgaWindow auditOrgaWindow : auditOrgaWindows) {
                                                if (auditOrgaWindow.getCenterguid()
                                                        .equals(ZwfwUserSession.getInstance().getCenterGuid())) {
                                                    listuser.add(user);
                                                    break;
                                                }
                                            }
                                        }
                                    }
                                }

                            }

                        }
                    }
                }
                return listuser;
            }

            @Override
            @SuppressWarnings({"rawtypes", "unchecked" })
            public <T> List<T> fetchData(int level, TreeData treeData) {
                List list = new ArrayList();
                // 一开始加载或者点击节点的时候触发
                if (level == FetchHandler9.FETCH_ONELEVEL) {
                    // 最开始加载的时候，把所有的窗口部门加载出来，最开始treeData的guid为空
                    if (StringUtil.isBlank(treeData.getObjectGuid())) {
                        List<String> ouguids = auditOrgaWindowService
                                .getoulistBycenterguid(ZwfwUserSession.getInstance().getCenterGuid()).getResult();
                        for (String string : ouguids) {
                            if (StringUtil.isNotBlank(string)) {
                                //去除没有人员的部门节点
                                List<Object> childlist = new ArrayList<Object>();
                                List<FrameUser> users =  userService.listUserByOuGuid(string, "", "", "", false, true, true, 1);
                                filterWindowUser(childlist,users);
                                if(childlist.size()>0){
                                    list.add(ouService.getOuByOuGuid(string));                                    
                                };
                            }
                        }
                        // 如果treeData的guid不为空，则说明该节点下面有子节点，获取该窗口部门下的所有的人员
                    }
                    else {
                        List<FrameUser> users = userService.listUserByOuGuid(treeData.getObjectGuid(), "", "", "",
                                false, true, true, 1);
                        //去除不在当前中心下的人员
                        filterWindowUser(list, users);
                    }
                }
                // 点击checkbox的时候触发
                else if ("frameou".equals(treeData.getObjectcode())) {
                    // 如果点击的是部门的checkbox，则返回该部门下所有的人员的list
                    //去除不在当前中心下的人员
                    List<FrameUser> users =  userService.listUserByOuGuid(treeData.getObjectGuid(), "", "", "", false, true, true, 1);
                    filterWindowUser(list,users);
                }
                //全选时
                else {
                    List<FrameOu> list1 = new ArrayList();
                    List<String> ouguids = auditOrgaWindowService
                            .getoulistBycenterguid(ZwfwUserSession.getInstance().getCenterGuid()).getResult();
                    for (String string : ouguids) {
                        if (StringUtil.isNotBlank(string)) {
                            list1.add(ouService.getOuByOuGuid(string));
                        }
                    }
                    if (list1 != null && list1.size() > 0) {
                        for (FrameOu frameou : list1) {
                            List<FrameUser> users = userService.listUserByOuGuid(frameou.getOuguid(), "", "", "", false,
                                    true, true, 1);
                            filterWindowUser(list, users);
                         }
                    }

                }
                return list;
            }

            private void filterWindowUser(List<Object> list, List<FrameUser> users) {
                if (users != null) { //获得部门下分配到该中心下的窗口的人员
                    for (FrameUser user : users) {
                        List<AuditOrgaWindow> auditOrgaWindows = auditOrgaWindowService
                                .getWindowListByUserGuid(user.getUserGuid()).getResult();
                        if (auditOrgaWindows != null) {
                            for (AuditOrgaWindow auditOrgaWindow : auditOrgaWindows) {
                                if (auditOrgaWindow.getCenterguid()
                                        .equals(ZwfwUserSession.getInstance().getCenterGuid())) {
                                    list.add(user);
                                    break;
                                }
                            }
                        }
                    }
                }
            }

            @Override
            public int fetchChildCount(TreeData treeData) {
                int oucount = 0;
                int usercount = 0;
                oucount = ouService.listOUByGuid(treeData.getObjectGuid(), 5).size();
                usercount = userService.getUserCountByOuGuid(treeData.getObjectGuid(), "", "", "", false, false, false,
                        1);
                if (oucount > 0 || usercount > 0) {
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
                            List<FrameUser> frameUsers = userService.listUserByOuGuid(frameOu.getOuguid(), "", "", "",
                                    false, true, true, 3);
                            int childCount = 0;
                            if (frameUsers != null) {
                                childCount = frameUsers.size();
                            }
                            if (childCount == 0) {
                                treeData.setNoClick(true);
                            }
                            else {
                                treeData.setObjectcode("frameou");
                                treeList.add(treeData);
                            }
                        }
                        // 将dataList转化为AuditTask的list
                        if (obj instanceof FrameUser) {
                            FrameUser frameuser = (FrameUser) obj;
                            TreeData treeData = new TreeData();
                            treeData.setObjectGuid(frameuser.getUserGuid());
                            treeData.setTitle(frameuser.getDisplayName());
                            treeData.setObjectcode("frameuser");
                            treeList.add(treeData);
                        }
                        if (obj instanceof ViewFrameUser) {
                            ViewFrameUser frameuser = (ViewFrameUser) obj;
                            TreeData treeData = new TreeData();
                            treeData.setObjectGuid(frameuser.getUserGuid());
                            treeData.setTitle(frameuser.getDisplayName() + "(" + frameuser.getOuname() + ")");
                            treeData.setObjectcode("frameuser");
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
                    .findFieldByRecordRowguid("objectname,objectguid", recordguid, "3").getResult();
            if (lists != null && lists.size() > 0) {
                for (AuditPerformanceRecordObject list : lists) {
                    windowItemList.add(new SelectItem(list.getObjectguid(), list.getObjectname()));
                }
            }
        }
        return windowItemList;
    }
}
