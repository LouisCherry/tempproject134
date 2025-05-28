package com.epoint.auditsp.auditsphandle.action;

import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.fileupload.AttachHandler9;
import com.epoint.basic.faces.fileupload.AttachStorage;
import com.epoint.basic.faces.fileupload.FileUploadModel9;
import com.epoint.basic.faces.tree.DefaultFileUploadHandlerImpl9;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import com.epoint.xmz.jgtaskmaterialrelation.api.IJgTaskmaterialrelationService;
import com.epoint.xmz.jgtaskmaterialrelation.api.entity.JgTaskmaterialrelation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@RestController("jgtaskauditattachlistaction")
@Scope("request")
public class JgtaskAuditAttachlistAction extends BaseController {
    /**
     *
     */
    private static final long serialVersionUID = 6196380024743989243L;

    private int attachCount;

    private String cliengguid;

    private String attachtype;

    private String itemguid;

    private String itemcode;

    /**
     * 表格控件model
     */
    private DataGridModel<FrameAttachInfo> model;

    @Autowired
    private IAttachService frameAttachInfoService;

    @Autowired
    private IJgTaskmaterialrelationService relationService;

    /**
     * 附件上传model
     */
    private FileUploadModel9 attachUploadModel;

    @Override
    public void pageLoad() {
        cliengguid = getRequestParameter("cliengGuid");
        itemguid = getRequestParameter("itemguid");
        attachtype = getRequestParameter("yewtype");
        itemcode = getRequestParameter("itemcode");
        log.info("cliengguid："+cliengguid);
        log.info("itemguid："+itemguid);
        log.info("attachtype："+attachtype);
        log.info("itemcode："+itemcode);
        if(StringUtil.isBlank(cliengguid)){
            cliengguid = UUID.randomUUID().toString();
        }
        try {
            // 这里需要判断一下当前的附件数，如果大于0个，需要控制显示删除按钮
            List<FrameAttachInfo> list = getAttachList(cliengguid);
            int count = list.size();
            setAttachCount(count);
            String rtnType = "add";
            if (count > 0) {
                rtnType = "addanddel";
            }
            addCallbackParam("type", rtnType);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public FileUploadModel9 getFileUploadModel() {
        if (attachUploadModel == null) {
            AttachHandler9 handler = new AttachHandler9() {
                /**
                 *
                 */
                private static final long serialVersionUID = 1L;

                @Override
                public void afterSaveAttachToDB(Object attach) {
                    log.info("itemguid："+itemguid);
                    log.info("attachtype："+attachtype);
                    if ("gc".equals(attachtype)) {
                        //新增监管事项材料上传关联关系表，先查询
                        JgTaskmaterialrelation relation = relationService.getRelationByItemguid(itemguid);
                        if (relation == null) {
                            relation = new JgTaskmaterialrelation();
                            relation.setRowguid(UUID.randomUUID().toString());
                            relation.setOperatedate(new Date());
                            relation.setItemcode(itemcode);
                            relation.setItemguid(itemguid);
                            relation.setGccliengguid(cliengguid);
                            relationService.insert(relation);
                        } else {
                            relation.setGccliengguid(cliengguid);
                            relationService.update(relation);
                        }
                    } else if ("jz".equals(attachtype)) {
                        //新增监管事项材料上传关联关系表，先查询
                        JgTaskmaterialrelation relation = relationService.getRelationByItemguid(itemguid);
                        if (relation == null) {
                            relation = new JgTaskmaterialrelation();
                            relation.setRowguid(UUID.randomUUID().toString());
                            relation.setOperatedate(new Date());
                            relation.setItemcode(itemcode);
                            relation.setItemguid(itemguid);
                            relation.setJzcliengguid(cliengguid);
                            relationService.insert(relation);
                        } else {
                            relation.setJzcliengguid(cliengguid);
                            relationService.update(relation);
                        }
                    } else if ("rf".equals(attachtype)) {
                        //新增监管事项材料上传关联关系表，先查询
                        JgTaskmaterialrelation relation = relationService.getRelationByItemguid(itemguid);
                        if (relation == null) {
                            relation = new JgTaskmaterialrelation();
                            relation.setRowguid(UUID.randomUUID().toString());
                            relation.setOperatedate(new Date());
                            relation.setItemcode(itemcode);
                            relation.setItemguid(itemguid);
                            relation.setRfcliengguid(cliengguid);
                            relationService.insert(relation);
                        } else {
                            relation.setRfcliengguid(cliengguid);
                            relationService.update(relation);
                        }
                    }
                    attachUploadModel.getExtraDatas().put("msg", "上传成功");
                }

                @Override
                public boolean beforeSaveAttachToDB(AttachStorage attachStorage) {
                    return true;
                }

            };
            attachUploadModel = new FileUploadModel9(new DefaultFileUploadHandlerImpl9(cliengguid, attachtype, null, handler,
                    userSession.getUserGuid(), userSession.getDisplayName()));
        }
        return attachUploadModel;
    }

    public DataGridModel<FrameAttachInfo> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<FrameAttachInfo>() {
                @Override
                public List<FrameAttachInfo> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    List<FrameAttachInfo> list = getAttachList(cliengguid);
                    int count = list.size();
                    this.setRowCount(count);
                    return list;
                }

            };
        }
        return model;
    }

    public void delete() {
        List<String> select = getDataGridData().getSelectKeys();
        for (String sel : select) {
            frameAttachInfoService.deleteAttachByAttachGuid(sel);
        }
        addCallbackParam("msg", "成功删除！");
    }

    public List<FrameAttachInfo> getAttachList(String cliengGuid) {
        return frameAttachInfoService.getAttachInfoListByGuid(cliengGuid);
    }

    public int getAttachCount() {
        return attachCount;
    }

    public void setAttachCount(int attachCount) {
        this.attachCount = attachCount;
    }

    public String getCliengguid() {
        return cliengguid;
    }

    public void setCliengguid(String cliengguid) {
        this.cliengguid = cliengguid;
    }

    public String getAttachtype() {
        return attachtype;
    }

    public void setAttachtype(String attachtype) {
        this.attachtype = attachtype;
    }

    public String getItemguid() {
        return itemguid;
    }

    public void setItemguid(String itemguid) {
        this.itemguid = itemguid;
    }

    public String getItemcode() {
        return itemcode;
    }

    public void setItemcode(String itemcode) {
        this.itemcode = itemcode;
    }
}
