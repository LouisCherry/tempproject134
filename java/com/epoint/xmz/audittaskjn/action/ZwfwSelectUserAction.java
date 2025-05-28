package com.epoint.xmz.audittaskjn.action;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.tree.ConstValue9;
import com.epoint.basic.faces.tree.EpointTreeHandler9;
import com.epoint.basic.faces.tree.LazyTreeModal9;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.organ.user.api.IUserService;

/**
 *
 * @version [版本号, 2016年8月25日]
 */
@RestController("jnzwfwselectuseraction")
@Scope("request")
public class ZwfwSelectUserAction extends BaseController
{

    private static final long serialVersionUID = -7374174419397243555L;

    @Autowired
    private IUserService userservice;

    private LazyTreeModal9 userTreeModel;

    private String users;

    @Override
    public void pageLoad() {
        String params = request.getParameter("cmdParams");
        if (StringUtil.isNotBlank(params)) {
            users = JSON.parseObject(params).getString("userguids");
        }
    }

    /**
     * [数据回填]
     * 
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public void setSelectedTreeNode() {
        List<SelectItem> sItem = new ArrayList<SelectItem>();
        String userguids = users;
        if (StringUtil.isNotBlank(userguids)) {
            String[] userguidList = userguids.split(",");
            for (String userguid : userguidList) {
                String username = userservice.getUserNameByUserGuid(userguid);
                sItem.add(new SelectItem(userguid, username));
            }
        }
        userTreeModel.setSelectNode(sItem);
    }

    public LazyTreeModal9 getUserTreeModel() {
        if (userTreeModel == null) {
            userTreeModel = new LazyTreeModal9(new EpointTreeHandler9(ConstValue9.FRAMEOU));

            userTreeModel.setRootName("所有部门");
            userTreeModel.setTreeType(ConstValue9.CHECK_MULTI);
            if (!isPostback()) {
                setSelectedTreeNode();
            }
        }
        return userTreeModel;
    }

    public void userlist() {
        String userList = new String();
        String guidList = new String();
        for (SelectItem si : getUserTreeModel().getSelectNode()) {
            userList += si.getText();
            userList += ",";
            guidList += si.getValue();
            guidList += ",";
        }
        if (userList.length() > 0) {
            userList = userList.substring(0, userList.length() - 1);
        }
        if (guidList.length() > 0) {
            guidList = guidList.substring(0, guidList.length() - 1);
        }
        addCallbackParam("nameandguidlist", guidList.toString() + "_SPLIT_" + userList.toString());
    }

    public String getUsers() {
        return users;
    }

    public void setUsers(String users) {
        this.users = users;
    }

}
