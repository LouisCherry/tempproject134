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
 * 智能化一体机区域配置新增页面对应的后台
 * 
 * @author 54201
 * @version [版本号, 2019-09-27 11:16:41]
 */
@RightRelation(JnAuditZnsbSelfservicemachineRegionListAction.class)
@RestController("jnauditznsbselfservicemachineregionaddaction")
@Scope("request")
public class JnAuditZnsbSelfservicemachineRegionAddAction extends BaseController
{
    /**
     * 
     */
    private static final long serialVersionUID = -1782979103006071828L;
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
     private List<SelectItem> parentModel = new ArrayList<SelectItem>();

    public void pageLoad() {
        dataBean = new JnAuditZnsbSelfmachineregion();
        String parentguid = getRequestParameter("parentguid");
        String parentguidName = "";
        if (StringUtil.isNotBlank(parentguid)) {
            JnAuditZnsbSelfmachineregion parentregion=service.getRegionByRowguid(parentguid).getResult();
            if (StringUtil.isNotBlank(parentregion)) {
                addCallbackParam("parentName",parentregion.getRegionname());
            }
        }
       
    }

    /**
     * 保存并关闭
     * 
     */
    public void add() {
        dataBean.setRowguid(UUID.randomUUID().toString());
        dataBean.setOperatedate(new Date());
        dataBean.setOperateusername(userSession.getDisplayName());
       
        
        List<FrameAttachInfo> picattachlist = attachService.getAttachInfoListByGuid(getViewData("picattachguid"));
        if (picattachlist != null && picattachlist.size() > 0) {
            dataBean.setPicattachguid(getViewData("picattachguid"));
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
        dataBean = new JnAuditZnsbSelfmachineregion();
    }

    public JnAuditZnsbSelfmachineregion getDataBean() {
        if (dataBean == null) {
            dataBean = new JnAuditZnsbSelfmachineregion();
        }
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
     *  [logo图片上传] 
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
            if(StringUtil.isNotBlank(getViewData("picattachguid"))) {
                picattachguid = getViewData("picattachguid");
            }else {
                picattachguid = UUID.randomUUID().toString();
                addViewData("picattachguid",picattachguid);
            }
            picUploadModel = new FileUploadModel9(new DefaultFileUploadHandlerImpl9(picattachguid, null,
                    null, handler, userSession.getUserGuid(), userSession.getDisplayName()));
        }
        return picUploadModel;
    } 
}
