package com.epoint.sghd;

import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.tree.LazyTreeModal9;
import com.epoint.basic.faces.tree.SimpleFetchHandler9;
import com.epoint.basic.faces.tree.TreeData;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.organ.ou.api.IOuService;
import com.epoint.frame.service.organ.ou.entity.FrameOu;
import com.epoint.sghd.commonutil.api.IJnSghdCommonutil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * 政务服务选择窗口部门 注意点：在编辑页面跳转到该action的时候，需要传入两个参数，分别是guid和tablename,
 * guid表示XXX表的guid，tablename表示与ouguid关联的表名。主要的作用是用来回显部门。
 * 
 * @version [版本号, 2016年8月25日]
 */
@RestController("selectfoshanouaction")
@Scope("request")
public class SelectFoShanOuAction extends BaseController
{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * 部门树model
     */
    private LazyTreeModal9 treeModel = null;
    /**
     * 新增的时候显示的部门
     */
    private String ouguid;

    private IOuService ouService = ContainerFactory.getContainInfo().getComponent(IOuService.class);

    @Autowired
    private IJnSghdCommonutil sghdService;

    @Override
    public void pageLoad() {
        ouguid = this.getRequestParameter("ouguid");
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
                    String ouguids = getTaskguids() + "'";
                    String sql = "SELECT * FROM FRAME_OU A WHERE A.OUGUID IN (" + ouguids + ")";
                    List<FrameOu> frameOus = sghdService.findList(sql, FrameOu.class);
                    list = frameOus;
                    return list;
                }

                @Override
                @SuppressWarnings({"unchecked" })
                public <T> List<T> fetchData(int arg0, TreeData treeData) {
                    String ouguids = getTaskguids();
                    if (StringUtil.isNotBlank(ouguids)) {
                        ouguids = (new StringBuilder(String.valueOf(ouguids))).append("'").toString();
                    }
                    String sql = "SELECT * FROM FRAME_OU A WHERE A.OUGUID IN (" + ouguids + ")";
                    List<FrameOu> frameOus = sghdService.findList(sql, FrameOu.class);
                    return (List<T>) frameOus;
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
                                treeData.setTitle(frameOu.getOushortName());
                                treeDataList.add(treeData);
                            }
                        }
                    }
                    return treeDataList;
                }
            });
            treeModel.setRootName("所有部门");
            treeModel.setRootSelect(false);
            if (StringUtil.isNotBlank(ouguid)) {
                treeModel.setSelectNode(new ArrayList<SelectItem>()
                {
                    {
                        add(new SelectItem(ouguid, ouService.getOuByOuGuid(ouguid).getOuname()));
                    }
                });
            }
        }
        return treeModel;
    }

    public String getTaskguids() {
        List<String> ouguidList = sghdService
                .getAllOuguidsByWindowGuids(ZwfwUserSession.getInstance().getWindowGuid());
        StringBuilder ouguids = new StringBuilder("'");
        if (ouguidList != null && ouguidList.size() > 0) {
            ouguidList.forEach(guid -> {
                ouguids.append(guid).append("','");
            });
        }
        return ouguids.toString();
    }
}
