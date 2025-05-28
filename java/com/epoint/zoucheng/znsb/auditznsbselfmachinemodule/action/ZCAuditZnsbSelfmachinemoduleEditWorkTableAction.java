package com.epoint.zoucheng.znsb.auditznsbselfmachinemodule.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;

import com.epoint.basic.auditqueue.auditznsbselfmachinemodule.domain.AuditZnsbSelfmachinemodule;
import com.epoint.basic.auditqueue.auditznsbselfmachinemodule.inter.IAuditZnsbSelfmachinemoduleService;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.fileupload.AttachHandler9;
import com.epoint.basic.faces.fileupload.AttachStorage;
import com.epoint.basic.faces.fileupload.FileUploadModel9;
import com.epoint.basic.faces.tree.DefaultFileUploadHandlerImpl9;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.zoucheng.znsb.auditznsbselfmachinemodule.domain.ZCAuditZnsbSelfmachinemodule;
import com.epoint.zoucheng.znsb.auditznsbselfmachinemodule.inter.IZCAuditZnsbSelfmachinemoduleService;
import com.epoint.basic.controller.RightRelation;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 自助服务一体机模块配置修改页面对应的后台
 * 
 * @author 54201
 * @version [版本号, 2018-06-14 15:50:54]
 */
@RightRelation(ZCAuditZnsbSelfmachinemoduleListWorkTableAction.class)
@RestController("zcauditznsbselfmachinemoduleeditworktableaction")
@Scope("request")
public class ZCAuditZnsbSelfmachinemoduleEditWorkTableAction extends BaseController
{

    /**
     * 
     */
    private static final long serialVersionUID = 215215397532683420L;

    @Autowired
    private IZCAuditZnsbSelfmachinemoduleService service;
    @Autowired
    private IAttachService attachservice;
    /**
     * 自助服务一体机模块配置实体对象
     */
    private ZCAuditZnsbSelfmachinemodule dataBean = null;

    /**
    * 模块类型下拉列表model
    */
    private List<SelectItem> moduletypeModel = null;

    /**
     * 是否需要登录下拉列表model
     */
    private List<SelectItem> isneedloginModel = null;

    /**
     * 模块配置类型下拉列表model
     */
    private List<SelectItem> moduleConfigtypeModel = null;

    /**
     * 是否启用下拉列表model
     */
    private List<SelectItem> enableModel = null;

    private FileUploadModel9 PicUploadModel;

    /**
     * 图片guid
     */
    private String pictureguid;

    public void pageLoad() {
        String guid = getRequestParameter("guid");

        dataBean = service.find(guid).getResult();
        
        
        if (StringUtil.isNotBlank(dataBean.getPictureguid())) {
            pictureguid = dataBean.getPictureguid();
        }
        else {
            if (StringUtil.isBlank(getViewData("pictureguid"))) {
                pictureguid = UUID.randomUUID().toString();
                addViewData("pictureguid", pictureguid);
            }
            else {
                pictureguid = getViewData("pictureguid");
            }
        }
        
        if (dataBean == null) {
            dataBean = new ZCAuditZnsbSelfmachinemodule();
        }
        String parentModuleName="";
        if (StringUtil.isNotBlank(dataBean.getParentmoduleguid())) {
            ZCAuditZnsbSelfmachinemodule module = service.find(dataBean.getParentmoduleguid()).getResult();
            if (StringUtil.isNotBlank(module)) {
                parentModuleName = module.getModulename();
            }
        }
        
        addCallbackParam("parentModuleName", parentModuleName);
    }

    /**
     * 保存修改
     * 
     */
    public void save() {
        dataBean.setOperatedate(new Date());
        //获取图片guid
        if (StringUtil.isNotBlank(getViewData("pictureguid"))) {
            // 这边判断图片是否上传
            List<FrameAttachInfo> attachInfoListByGuid = attachservice.getAttachInfoListByGuid(getViewData("pictureguid"));
            if(attachInfoListByGuid != null && !attachInfoListByGuid.isEmpty()){
                dataBean.setPictureguid(getViewData("pictureguid"));
            }
        }
        service.update(dataBean);
        addCallbackParam("msg", "修改成功！");
    }

    public ZCAuditZnsbSelfmachinemodule getDataBean() {
        return dataBean;
    }

    public void setDataBean(ZCAuditZnsbSelfmachinemodule dataBean) {
        this.dataBean = dataBean;
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> getModuletypeModel() {
        if (moduletypeModel == null) {
            moduletypeModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "首页左侧分类", null, false));
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

    public FileUploadModel9 getPicUploadModel() {
        if (PicUploadModel == null) {
            AttachHandler9 handler = new AttachHandler9()
            {

                private static final long serialVersionUID = 1L;

                @Override
                public void afterSaveAttachToDB(Object attach) {
                    PicUploadModel.getExtraDatas().put("msg", "上传成功");
                }

                @Override
                public boolean beforeSaveAttachToDB(AttachStorage arg0) {

                    return true;
                }

            };
            PicUploadModel = new FileUploadModel9(new DefaultFileUploadHandlerImpl9(pictureguid, null, null, handler,
                    userSession.getUserGuid(), userSession.getDisplayName()));
        }
        return PicUploadModel;
    }

}
