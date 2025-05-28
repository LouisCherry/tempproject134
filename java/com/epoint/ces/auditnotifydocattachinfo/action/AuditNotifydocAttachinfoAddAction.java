package com.epoint.ces.auditnotifydocattachinfo.action;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.aspose.words.*;
import com.epoint.basic.faces.fileupload.FileUploadModel9;
import com.epoint.basic.faces.tree.DefaultFileUploadHandlerImpl9;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.common.util.AttachUtil;
import com.epoint.common.util.StringUtil;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.utils.classpath.ClassPathUtil;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import com.epoint.ces.auditnotifydocattachinfo.api.entity.AuditNotifydocAttachinfo;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.basic.controller.RightRelation;
import com.epoint.basic.controller.BaseController;
import com.epoint.ces.auditnotifydocattachinfo.api.IAuditNotifydocAttachinfoService;

/**
 * 办件文书信息表新增页面对应的后台
 *
 * @author jiem
 * @version [版本号, 2022-03-15 14:02:49]
 */
@RightRelation(AuditNotifydocAttachinfoListAction.class)
@RestController("auditnotifydocattachinfoaddaction")
@Scope("request")
public class AuditNotifydocAttachinfoAddAction extends BaseController {
    @Autowired
    private IAuditNotifydocAttachinfoService service;
    @Autowired
    private IAttachService iAttachService;
    /**
     * 办件文书信息表实体对象
     */
    private AuditNotifydocAttachinfo dataBean = null;

    /**
     * 经营类别复选框组model
     */
    private List<SelectItem> businesstypeModel = null;
    /**
     * 文书类别下拉列表model
     */
    private List<SelectItem> doctypeModel = null;
    
    private List<SelectItem> areacodeModel = null;

    private FileUploadModel9 fileUploadModel;

    private String cliengguid;

    Logger logger = LoggerFactory.getLogger(AuditNotifydocAttachinfoAddAction.class);



    @Override
    public void pageLoad() {
        dataBean = new AuditNotifydocAttachinfo();
        cliengguid = getViewData("cliengguid");
        if (StringUtil.isBlank(cliengguid)){
            cliengguid = UUID.randomUUID().toString();
            addViewData("cliengguid", cliengguid);
        }
    }

    /**
     * 保存并关闭
     */
    public void add() {
        dataBean.setRowguid(cliengguid);
        dataBean.setOperatedate(new Date());
        dataBean.setOperateusername(userSession.getDisplayName());
        dataBean.setCreateuserguid(userSession.getUserGuid());
        dataBean.setCreateusername(userSession.getDisplayName());
        service.insert(dataBean);
        addCallbackParam("msg", "保存成功！");
        dataBean = null;
    }

    /**
     * 保存并新建
     */
    public void addNew() {
        add();
        dataBean = new AuditNotifydocAttachinfo();
    }

    public AuditNotifydocAttachinfo getDataBean() {
        if (dataBean == null) {
            dataBean = new AuditNotifydocAttachinfo();
        }
        return dataBean;
    }

    public void setDataBean(AuditNotifydocAttachinfo dataBean) {
        this.dataBean = dataBean;
    }

    public List<SelectItem> getBusinesstypeModel() {
        if (businesstypeModel == null) {
            businesstypeModel = DataUtil.convertMap2ComboBox((List<Map<String, String>>) CodeModalFactory.factory("复选框组", "经营类别", null, false));
        }
        return this.businesstypeModel;
    }

    public List<SelectItem> getDoctypeModel() {
        if (doctypeModel == null) {
            doctypeModel = DataUtil.convertMap2ComboBox((List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "事项文书类别", null, false));
        }
        return this.doctypeModel;
    }
    
    public List<SelectItem> getAreacodeModel() {
        if (areacodeModel == null) {
        	areacodeModel = DataUtil.convertMap2ComboBox((List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "好差评辖区", null, false));
        }
        return this.areacodeModel;
    }

    /**
     * [附件上传]
     *
     * @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public FileUploadModel9 getFileUploadModel() throws Exception {
        if (fileUploadModel == null) {
            fileUploadModel = new FileUploadModel9(new DefaultFileUploadHandlerImpl9(cliengguid,
                    userSession.getUserGuid(), userSession.getDisplayName()));
        }
        return fileUploadModel;
    }

    /**
     * word转pdf
     * @throws Exception
     */
    public void convert2Pdf() throws Exception {
        // 查询出附件，如果为word转换为pdf
        List<FrameAttachInfo> listByGuid = iAttachService.getAttachInfoListByGuid(cliengguid);
        if (listByGuid != null && !listByGuid.isEmpty()){
            FrameAttachInfo attachInfo = listByGuid.get(0);
            if (".doc".equals(attachInfo.getContentType()) || ".docx".equals(attachInfo.getContentType())){
                getPdf(attachInfo);
                addCallbackParam("msg", "word已成功转换为pdf");
            }
        }
    }


    /**
     * word转pdf
     * @param attachInfo
     * @return
     * @throws Exception
     */
    FrameAttachInfo getPdf(FrameAttachInfo attachInfo)
            throws Exception {
        // pdf clientguid
        String cliengGuid = attachInfo.getCliengGuid();
        String attachGuid = attachInfo.getAttachGuid();
        // 先转成pdf
        // 指定字体
        FontSettings.setFontsFolder(ClassPathUtil.getClassesPath() + "font" + File.separator, true);
        String license = ClassPathUtil.getClassesPath() + "license.xml";
        new License().setLicense(license);
        String displayName = attachInfo.getAttachFileName();

        // 转换成word 再转换成pdf
        InputStream inputStream = iAttachService.getAttach(attachInfo.getAttachGuid()).getContent();
        Document doc = new Document(inputStream);
        ByteArrayOutputStream pdfoutputStream = new ByteArrayOutputStream();
        doc.save(pdfoutputStream,SaveFormat.PDF);
        ByteArrayInputStream inputStreampdf = new ByteArrayInputStream(pdfoutputStream.toByteArray());
        long pdfsize = (long) inputStreampdf.available();
        displayName = displayName.replace("docx", "pdf").replace("doc", "pdf");
        // 删除附件
        iAttachService.deleteAttachByGuid(cliengGuid);
        // 保存到数据库
        return AttachUtil.saveFileInputStream(attachGuid, cliengGuid, displayName, ".pdf", displayName,
                pdfsize, inputStreampdf, displayName, displayName);

    }
}
