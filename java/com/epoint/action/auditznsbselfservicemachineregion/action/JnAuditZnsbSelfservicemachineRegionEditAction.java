package com.epoint.action.auditznsbselfservicemachineregion.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import com.epoint.basic.faces.fileupload.AttachHandler9;
import com.epoint.basic.faces.fileupload.AttachStorage;
import com.epoint.basic.faces.fileupload.FileUploadModel9;
import com.epoint.basic.faces.tree.DefaultFileUploadHandlerImpl9;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.domain.JnAuditZnsbSelfmachineregion;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.inter.IJnAuditZnsbSelfmachineregion;

/**
 * 智能化一体机区域配置修改页面对应的后台
 * 
 * @author 54201
 * @version [版本号, 2019-09-27 11:16:41]
 */
@RightRelation(JnAuditZnsbSelfservicemachineRegionListAction.class)
@RestController("jnauditznsbselfservicemachineregioneditaction")
@Scope("request")
public class JnAuditZnsbSelfservicemachineRegionEditAction extends BaseController
{

    /**
     * 
     */
    private static final long serialVersionUID = -8159588385172714303L;

    @Autowired
    private IJnAuditZnsbSelfmachineregion service;
    
    @Autowired
    private IAttachService attachService;

    /**
     * 智能化一体机区域配置实体对象
     */
    private JnAuditZnsbSelfmachineregion dataBean = null;

    /**
    * 是否启用下拉列表model
    */
    private List<SelectItem> isenableModel = null;
    /**
     * 上传图片modul
     */
    private FileUploadModel9 picUploadModel;
    /**
     * 图片附件标识
     */
    private String picattachguid;

    /**
     * 父节点下拉列表model
     */
     private List<SelectItem> parentModel  = new ArrayList<SelectItem>();

    public void pageLoad() {
        String guid = getRequestParameter("guid");
        dataBean = service.find(guid);
        
        if (dataBean == null) {
            dataBean = new JnAuditZnsbSelfmachineregion();
        }
        if (StringUtil.isNotBlank(dataBean.getPicattachguid())) {
            picattachguid = dataBean.getPicattachguid();
        }else {
            if(StringUtil.isBlank(getViewData("picattachguid"))) {
                picattachguid = UUID.randomUUID().toString();
                addViewData("picattachguid", picattachguid);
            }else {
                picattachguid = getViewData("picattachguid");
            }
        }
        
        if (StringUtil.isNotBlank(dataBean.getParentguid())) {
            JnAuditZnsbSelfmachineregion parentregion=service.getRegionByRowguid(dataBean.getParentguid()).getResult();
            if (StringUtil.isNotBlank(parentregion)) {
                addCallbackParam("parentName",parentregion.getRegionname());
            }
        }
    }

    /**
     * 保存修改
     * 
     */
    public void save() {
        dataBean.setOperatedate(new Date());
        // 先判断有没有真实的上传附件，还是上传了后上传，但是也有guid
        
        
        List<FrameAttachInfo> logoattachlist = attachService.getAttachInfoListByGuid(picattachguid);
        if (logoattachlist != null && logoattachlist.size() > 0) {
            dataBean.setPicattachguid(picattachguid);
        }else {
            if(StringUtil.isNotBlank(dataBean.getPicattachguid())) {
                dataBean.setPicattachguid("");
            }
        }
        
        service.update(dataBean);
        addCallbackParam("msg", "修改成功！");
    }

    public JnAuditZnsbSelfmachineregion getDataBean() {
        return dataBean;
    }

    public void setDataBean(JnAuditZnsbSelfmachineregion dataBean) {
        this.dataBean = dataBean;
    }

    public List<SelectItem> getIsenableModel() {
        if (isenableModel == null) {
            isenableModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "是否", null, false));
        }
        return this.isenableModel;
    }
    
    public List<SelectItem> getParentModel() {

        List<JnAuditZnsbSelfmachineregion> list=service.getRegionList().getResult();
        for (JnAuditZnsbSelfmachineregion region : list) {
            SelectItem a = new SelectItem(region.getRowguid(), region.getRegionname());
            parentModel.add(a);
        }
        return this.parentModel;
    }
    
    /**
     * 
     *  [本机logo图片上传] 
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public FileUploadModel9 getPicUploadModel() {        
        if (picUploadModel == null) {
            AttachHandler9 handler = new AttachHandler9() {

                private static final long serialVersionUID = 1L;

                @Override
                public void afterSaveAttachToDB(Object attach) {
                    picUploadModel.getExtraDatas().put("msg", "上传成功");
                }

                @Override
                public boolean beforeSaveAttachToDB(AttachStorage arg0) {
                    
                    return true;
                }

            };
            picUploadModel = new FileUploadModel9(new DefaultFileUploadHandlerImpl9(picattachguid, null,
                    null, handler, userSession.getUserGuid(), userSession.getDisplayName()));
        }
        return picUploadModel;
    } 

}
