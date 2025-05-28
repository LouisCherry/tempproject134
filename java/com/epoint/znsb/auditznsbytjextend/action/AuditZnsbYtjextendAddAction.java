package com.epoint.znsb.auditznsbytjextend.action;

import java.util.*;

import com.epoint.basic.auditqueue.auditznsbmodule.inter.IAuditZnsbModule;
import com.epoint.basic.auditqueue.auditznsbselfmachinemodule.domain.AuditZnsbSelfmachinemodule;
import com.epoint.basic.auditqueue.auditznsbselfmachinemodule.inter.IAuditZnsbSelfmachinemoduleService;
import com.epoint.basic.auditqueue.auditznsbtaskconfigure.domian.AuditZnsbTaskConfigure;
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
import com.epoint.znsb.auditznsbytjlabel.api.IAuditZnsbYtjlabelService;
import com.epoint.znsb.auditznsbytjlabel.api.entity.AuditZnsbYtjlabel;
import org.springframework.beans.factory.annotation.Autowired;
import com.epoint.znsb.auditznsbytjextend.api.entity.AuditZnsbYtjextend;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.basic.controller.RightRelation;
import com.epoint.basic.controller.BaseController;
import com.epoint.znsb.auditznsbytjextend.api.IAuditZnsbYtjextendService;

/**
 * 一体机模块额外配置新增页面对应的后台
 *
 * @author Administrator
 * @version [版本号, 2021-04-20 10:11:49]
 */
@RightRelation(AuditZnsbYtjextendListAction.class)
@RestController("auditznsbytjextendaddaction")
@Scope("request")
public class AuditZnsbYtjextendAddAction extends BaseController {
    @Autowired
    private IAuditZnsbYtjextendService service;

    @Autowired
    private IAuditZnsbYtjlabelService ytjlabelService;
    /**
     * 一体机模块额外配置实体对象
     */
    private AuditZnsbYtjextend dataBean = null;

    /**
     * 是否热门模块单选按钮组model
     */
    private List<SelectItem> is_hotModel = null;

    @Autowired
    private IAttachService attachService;
    /**
     * 底色下拉列表model
     */
    private List<SelectItem> labelcolorModel = null;

    /**
     * 上传图片modul
     */
    private FileUploadModel9 picUploadModel;
    /**
     * 文件附件标识
     */
    private String logocliengguid;


    public void pageLoad() {
        String rowguid = request.getParameter("guid");
        List<AuditZnsbYtjextend> ytjextends = service.findList("moduleguid", rowguid);
        if (ytjextends.size() > 0) {
            dataBean = ytjextends.get(0);
            if (StringUtil.isNotBlank(dataBean.getPngattachguid())) {
                logocliengguid = dataBean.getPngattachguid();
                addViewData("logocliengguid", logocliengguid);
            }
            else {
                if (StringUtil.isBlank(getViewData("logocliengguid"))) {
                    logocliengguid = UUID.randomUUID().toString();
                    addViewData("logocliengguid", logocliengguid);
                }
                else {
                    logocliengguid = getViewData("logocliengguid");
                }
            }
        }

        if (dataBean == null) {
            dataBean = new AuditZnsbYtjextend();
        }
    }

    /**
     * [logo图片上传]
     *
     * @return
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
                public boolean beforeSaveAttachToDB(AttachStorage attachStorage) {
                    return true;
                }

            };
            if (StringUtil.isNotBlank(getViewData("logocliengguid"))) {
                logocliengguid = getViewData("logocliengguid");
            } else {
                logocliengguid = UUID.randomUUID().toString();
                addViewData("logocliengguid", logocliengguid);
            }
            picUploadModel = new FileUploadModel9(new DefaultFileUploadHandlerImpl9(logocliengguid, null, null, handler,
                    userSession.getUserGuid(), userSession.getDisplayName()));
        }
        return picUploadModel;
    }


    /**
     * 保存并关闭
     */
    public void add() {
        String rowguid = request.getParameter("guid");
        dataBean.setOperatedate(new Date());
        dataBean.setOperateusername(userSession.getDisplayName());
        dataBean.setModuleguid(rowguid);
        //system.out.println(dataBean);
        List<FrameAttachInfo> logoattachlist = attachService.getAttachInfoListByGuid(getViewData("logocliengguid"));
        if (logoattachlist != null && !logoattachlist.isEmpty()) {
            dataBean.setPngattachguid(getViewData("logocliengguid"));
        } else {
            addCallbackParam("msg", "fail");
            return;
        }

        List<AuditZnsbYtjextend> ytjextends = service.findList("moduleguid", rowguid);
        if (ytjextends.size() > 0) {
            service.update(dataBean);
            addCallbackParam("msg", "修改成功！");
        } else {
            dataBean.setRowguid(UUID.randomUUID().toString());
            service.insert(dataBean);
            addCallbackParam("msg", "保存成功！");
        }
        dataBean = null;
    }

    /**
     * 保存并新建
     */
    public void addNew() {
        add();
        dataBean = new AuditZnsbYtjextend();
    }

    public AuditZnsbYtjextend getDataBean() {
        if (dataBean == null) {
            dataBean = new AuditZnsbYtjextend();
        }
        return dataBean;
    }

    public void setDataBean(AuditZnsbYtjextend dataBean) {
        this.dataBean = dataBean;
    }

    public List<SelectItem> getIs_hotModel() {
        if (is_hotModel == null) {
            is_hotModel = DataUtil.convertMap2ComboBox((List<Map<String, String>>) CodeModalFactory.factory("单选按钮组", "是否", null, false));
        }
        return this.is_hotModel;
    }


    public List<SelectItem> getLabelcolorModel() {
        if (labelcolorModel == null) {
            //获取配置好的标签
            labelcolorModel = new ArrayList<>();

            List<AuditZnsbYtjlabel> labelList = ytjlabelService.findList("select * from audit_znsb_ytjlabel where 1=1");
            if(labelList.size() > 0){
                for (AuditZnsbYtjlabel label:labelList) {
                   labelcolorModel.add(new SelectItem(label.getRowguid(),label.getLablename()));
                }
            }
            labelcolorModel.add(new SelectItem("","未配置"));
           /* labelcolorModel = DataUtil.convertMap2ComboBox((List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "一体机标签底色", null, true));*/
        }
        return this.labelcolorModel;
    }
}
