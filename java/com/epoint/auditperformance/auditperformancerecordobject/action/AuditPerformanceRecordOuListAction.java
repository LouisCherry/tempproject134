package com.epoint.auditperformance.auditperformancerecordobject.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.auditorga.auditarea.domain.AuditOrgaArea;
import com.epoint.basic.auditorga.auditarea.inter.IAuditOrgaArea;
import com.epoint.basic.auditorga.auditwindow.inter.IAuditOrgaWindow;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.tree.LazyTreeModal9;
import com.epoint.basic.faces.tree.SimpleFetchHandler9;
import com.epoint.basic.faces.tree.TreeData;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.utils.json.JsonUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.organ.ou.api.IOuService;
import com.epoint.frame.service.organ.ou.entity.FrameOu;

/**
 * 考评记录考核辖区下窗口部门list页面对应的后台
 * 
 * @author 14408
 * @version [版本号, 2018-01-09 16:33:26]
 */
@RestController("auditperformancerecordoulistaction")
@Scope("request")
public class AuditPerformanceRecordOuListAction extends BaseController
{

    /**
     * 
     */
    private static final long serialVersionUID = -6720503910593188651L;

    @Autowired
    private IAuditOrgaArea auditOrgaAreaService;
    @Autowired
    private IAuditOrgaWindow auditOrgaWindowService;

    private LazyTreeModal9 treeModel = null;
    @Autowired
    private IOuService ouService;
    
    /**
     * 页面文本框中已选的对象
     */
    private String obj = null;

    @Override
    public void pageLoad() {
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

    @SuppressWarnings("serial")
    public LazyTreeModal9 getTreeModel() {
        if (treeModel == null) {
            treeModel = new LazyTreeModal9(new SimpleFetchHandler9()
            {
                @Override
                @SuppressWarnings({"rawtypes", "unchecked" })
                public <T> List<T> search(String condition) {
                    List list = new ArrayList();
                    AuditOrgaArea auditOrgaArea = auditOrgaAreaService
                            .getAreaByAreacode(ZwfwUserSession.getInstance().getAreaCode()).getResult();
                    if (auditOrgaArea != null) {
                        List<FrameOu> frameOus =new ArrayList<FrameOu>();
                        List<String> ouguids=auditOrgaWindowService.getoulistBycenterguid(ZwfwUserSession.getInstance().getCenterGuid()).getResult();
                        for (String string : ouguids) {
                            if(StringUtil.isNotBlank(string)){
                                frameOus.add(ouService.getOuByOuGuid(string));
                            }
                        }
                        //List<FrameOu> frameOus = ouService.listDependOuByParentGuid(auditOrgaArea.getOuguid(), "", 2);
                        if (StringUtil.isNotBlank(condition)) {
                            // 筛选并排序
                            list = frameOus.stream()
                                    .filter(x -> x.getOuname().contains(condition)
                                            && !auditOrgaArea.getOuguid().equals(x.getOuguid()))
                                    .sorted((a, b) -> b.getOrderNumber().compareTo(a.getOrderNumber()))
                                    .collect(Collectors.toList());
                        }
                        else {
                            list = frameOus.stream().filter(x -> !auditOrgaArea.getOuguid().equals(x.getOuguid()))
                                    .sorted((a, b) -> b.getOrderNumber().compareTo(a.getOrderNumber()))
                                    .collect(Collectors.toList());
                        }
                    }
                    return list;
                }

                @Override
                @SuppressWarnings({"rawtypes", "unchecked" })
                public <T> List<T> fetchData(int arg0, TreeData treeData) {
                    List list = new ArrayList();
                    List<String> ouguids=auditOrgaWindowService.getoulistBycenterguid(ZwfwUserSession.getInstance().getCenterGuid()).getResult();
                    for (String string : ouguids) {
                        if(StringUtil.isNotBlank(string)){
                            list.add(ouService.getOuByOuGuid(string));
                        }
                    }
                    return list;
                }

                @Override
                public int fetchChildCount(TreeData arg0) {
                    return 0;
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
                                treeDataList.add(treeData);
                            }
                        }
                    }
                    return treeDataList;
                }
            });
            treeModel.setRootName("所有窗口部门");
            treeModel.setRootSelect(true);
        }
        return treeModel;
    }

}
