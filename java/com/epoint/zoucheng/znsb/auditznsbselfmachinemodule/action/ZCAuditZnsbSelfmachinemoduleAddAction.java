package com.epoint.zoucheng.znsb.auditznsbselfmachinemodule.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.auditqueue.auditznsbselfmachinemodule.domain.AuditZnsbSelfmachinemodule;
import com.epoint.basic.auditqueue.auditznsbselfmachinemodule.inter.IAuditZnsbSelfmachinemoduleService;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.common.znsb.util.QueueConstant;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;

/**
 * 自助服务一体机模块配置新增页面对应的后台
 * 
 * @author 54201
 * @version [版本号, 2018-06-14 15:50:54]
 */
@RestController("zcauditznsbselfmachinemoduleaddaction")
@Scope("request")
public class ZCAuditZnsbSelfmachinemoduleAddAction extends BaseController
{
    /**
     * 
     */
    private static final long serialVersionUID = 3696659683094943762L;
    @Autowired
    private IAuditZnsbSelfmachinemoduleService service;
    /**
     * 自助服务一体机模块配置实体对象
     */
    private AuditZnsbSelfmachinemodule dataBean = null;

    /**
    * 模块类型下拉列表model
    */
    private List<SelectItem> moduletypeModel = null;
    
    /**
     * 模块配置类型下拉列表model
     */
     private List<SelectItem> moduleConfigtypeModel = null;

    /**
     * 是否需要登录下拉列表model
     */
    private List<SelectItem> isneedloginModel = null;

    /**
     * 是否启用下拉列表model
     */
    private List<SelectItem> enableModel = null;
    @Autowired
    private ICodeItemsService codeItemService;

    public void pageLoad() {
        String parentmoduleguid = getRequestParameter("parentmoduleguid");
        String parentModuleName = "";
        String moduleTypeValue = "";
        if (StringUtil.isNotBlank(parentmoduleguid)) {
            if (parentmoduleguid.length() == QueueConstant.CONSTANT_INT_ONE) {
                // 点击第一层模块，如政务服务
                parentModuleName = codeItemService.getItemTextByCodeName("邹城首页左侧分类", parentmoduleguid);
                moduleTypeValue = parentmoduleguid;
            }
            else {
                // 点击第二层模块，如办事指南
                AuditZnsbSelfmachinemodule module = service.find(parentmoduleguid).getResult();
                if (StringUtil.isNotBlank(module)) {
                    parentModuleName = module.getModulename();
                    moduleTypeValue = module.getModuletype();
                }
            }
        }
        addCallbackParam("moduleTypeValue", moduleTypeValue);
        addCallbackParam("parentModuleName", parentModuleName);
        dataBean = new AuditZnsbSelfmachinemodule();
        dataBean.setIsneedlogin("0");
        dataBean.setEnable("1");
    }

    /**
     * 保存并关闭
     * 
     */
    public void add() {
        dataBean.setRowguid(UUID.randomUUID().toString());
        dataBean.setOperatedate(new Date());
        dataBean.setOperateusername(userSession.getDisplayName());
        //自动存入中心guid
        dataBean.setCenterguid(ZwfwUserSession.getInstance().getCenterGuid());
        // 判断下是否是二级模块，二级模块的父模块guid应该是为空
        if (StringUtil.isNotBlank(dataBean.getParentmoduleguid())
                && dataBean.getParentmoduleguid().length() == QueueConstant.CONSTANT_INT_ONE) {
            dataBean.setParentmoduleguid("");
        }
        service.insert(dataBean);
        addCallbackParam("msg", "保存成功！");
        dataBean = null;
    }

    /**
     * 保存并新建
     * 
     */
    public void addNew() {
        add();
        dataBean = new AuditZnsbSelfmachinemodule();
    }

    public AuditZnsbSelfmachinemodule getDataBean() {
        if (dataBean == null) {
            dataBean = new AuditZnsbSelfmachinemodule();
        }
        return dataBean;
    }

    public void setDataBean(AuditZnsbSelfmachinemodule dataBean) {
        this.dataBean = dataBean;
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> getModuletypeModel() {
        if (moduletypeModel == null) {
            moduletypeModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "邹城首页左侧分类", null, false));
        }
        return this.moduletypeModel;
    }
    
    @SuppressWarnings("unchecked")
    public List<SelectItem> getModuleConfigTypeModel() {
        if (moduleConfigtypeModel == null) {
            moduleConfigtypeModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "模块配置类型", null, false));
        }
        return this.moduleConfigtypeModel;
    }

    public List<SelectItem> getIsneedloginModel() {
        List<Map<String, String>> maplist = new ArrayList<Map<String, String>>();
        Map<String, String> map = new HashMap<>();
        map.put("跳转页面(不登陆)", "0");
        maplist.add(map);
        map = new HashMap<>();
        map.put("跳转页面(登陆)", "1");
        maplist.add(map);
        map = new HashMap<>();
        map.put("跳转方法", "2");
        maplist.add(map);
        if (isneedloginModel == null) {
            isneedloginModel = DataUtil.convertMap2ComboBox(maplist);
        }
        return this.isneedloginModel;
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> getEnableModel() {
        if (enableModel == null) {
            enableModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "是否", null, false));
        }
        return this.enableModel;
    }

}
