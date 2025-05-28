package com.epoint.xmz.auditfszl.action;

import java.util.*;
import java.util.stream.Collectors;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.epoint.basic.faces.fileupload.FileUploadModel9;
import com.epoint.cert.basic.certinfo.domain.CertInfo;
import com.epoint.cert.basic.certinfo.inter.ICertInfo;
import com.epoint.cert.commonutils.SqlUtils;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.utils.sql.SqlConditionUtil;
import com.epoint.core.utils.sql.SqlHelper;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.frame.service.metadata.code.entity.CodeItems;
import com.epoint.frame.service.metadata.mis.util.ListGenerator;
import com.epoint.xmz.auditfszldevice.api.IAuditFszlDeviceService;
import com.epoint.xmz.auditfszldevice.api.entity.AuditFszlDevice;
import com.epoint.xmz.auditfszlperson.api.IAuditFszlPersonService;
import com.epoint.xmz.auditfszlperson.api.entity.AuditFszlPerson;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.xmz.auditfszl.api.entity.AuditFszl;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.basic.controller.RightRelation;
import com.epoint.xmz.auditfszl.api.IAuditFszlService;
import org.springframework.beans.factory.annotation.Autowired;
import com.epoint.basic.faces.tree.DefaultFileUploadHandlerImpl9;

/**
 * 放射诊疗数据修改页面对应的后台
 *
 * @author ljh
 * @version [版本号, 2024-06-20 10:28:15]
 */
@RightRelation(AuditFszlListAction.class)
@RestController("auditfszleditaction")
@Scope("request")
public class AuditFszlEditAction extends BaseController {

    @Autowired
    private IAuditFszlService service;

    @Autowired
    private IAuditFszlPersonService auditFszlPersonService;

    @Autowired
    private IAuditFszlDeviceService auditFszlDeviceService;

    @Autowired
    private ICodeItemsService codeItemsService;

    /**
     * 放射诊疗数据实体对象
     */
    private AuditFszl dataBean = null;

    /**
     * 表格控件model
     */
    private DataGridModel<AuditFszlPerson> gzryModel;

    private DataGridModel<AuditFszlDevice> fszzModel;

    private FileUploadModel9 sltfileUploadModel;

    private String sltcliengguid = "";

    @Autowired
    private ICertInfo iCertInfo;

    public void pageLoad() {
        String guid = getRequestParameter("guid");
        dataBean = service.find(guid);
        if (dataBean == null) {
            dataBean = new AuditFszl();
        } else {
            SqlUtils sqlConditionUtil = new SqlUtils();
            sqlConditionUtil.eq("certno", dataBean.getCertno());
            List<CertInfo> certInfos = iCertInfo.getListByCondition(sqlConditionUtil.getMap());
            if (CollectionUtils.isNotEmpty(certInfos)) {
                CertInfo certInfo = certInfos.get(0);
                sltcliengguid = certInfo.getCertcliengguid();
            }
        }

        addCallbackParam("fszlGuid", dataBean.getRowguid());
    }

    /**
     * 保存修改
     */
    public void save() {

        SqlConditionUtil sqlc = new SqlConditionUtil();
        sqlc.eq("certno", dataBean.getCertno());
        sqlc.eq("is_history", ZwfwConstant.CONSTANT_STR_ZERO);
        AuditFszl auditFszl = service.find("select * from audit_fszl " + new SqlHelper().getSql(sqlc.getMap()), new ArrayList<>());
        if (auditFszl != null && !dataBean.getRowguid().equals(auditFszl.getRowguid()) && !dataBean.getFszlid().equals(auditFszl.getFszlid())) {
            addCallbackParam("err", "当前放射诊疗许可证号已存在");
            return;
        }
        dataBean.setOperatedate(new Date());
        dataBean.setUpdatetime(new Date());
        // 计算下一次校验日期
        Date jydate = dataBean.getJydate();
        String validitydate = dataBean.getValiditydate();
        dataBean.setNextjydate(AuditFszlAddAction.getNextJydate(jydate, validitydate));
        service.update(dataBean);
        addCallbackParam("msg", l("修改成功") + "！");
    }


    public DataGridModel<AuditFszlPerson> getGzryDataGridData() {
        // 获得表格对象
        if (gzryModel == null) {
            gzryModel = new DataGridModel<AuditFszlPerson>() {

                @Override
                public List<AuditFszlPerson> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    if (StringUtil.isNotBlank(dataBean.getRowguid())) {
                        List<Object> conditionList = new ArrayList<Object>();
                        SqlConditionUtil sqlc = new SqlConditionUtil();
                        sqlc.eq("fszlguid", dataBean.getRowguid());
                        String conditionSql = new SqlHelper().getPatchSql(sqlc.getMap());

                        List<AuditFszlPerson> list = auditFszlPersonService.findList(
                                ListGenerator.generateSql("audit_fszl_person", conditionSql, sortField, sortOrder), first, pageSize,
                                conditionList.toArray());
                        int count = auditFszlPersonService.countAuditFszlPerson(ListGenerator.generateSql("audit_fszl_person", conditionSql), conditionList.toArray());
                        this.setRowCount(count);
                        return list;
                    } else {
                        return new ArrayList<>();
                    }
                }

            };
        }
        return gzryModel;
    }


    public DataGridModel<AuditFszlDevice> getSxzzDataGridData() {
        // 获得表格对象
        if (fszzModel == null) {
            fszzModel = new DataGridModel<AuditFszlDevice>() {

                @Override
                public List<AuditFszlDevice> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    List<Object> conditionList = new ArrayList<Object>();

                    SqlConditionUtil sqlc = new SqlConditionUtil();
                    sqlc.eq("fszlguid", dataBean.getRowguid());
                    String conditionSql = new SqlHelper().getPatchSql(sqlc.getMap());
                    List<AuditFszlDevice> list = auditFszlDeviceService.findList(
                            ListGenerator.generateSql("audit_fszl_device", conditionSql, sortField, sortOrder), first, pageSize,
                            conditionList.toArray());
                    int count = auditFszlDeviceService.countAuditFszlDevice(ListGenerator.generateSql("audit_fszl_device", conditionSql), conditionList.toArray());
                    this.setRowCount(count);
                    return list;
                }

            };
        }
        return fszzModel;
    }

    /**
     * 删除放射设备
     */
    public void fszzdeleteSelect() {
        List<String> select = getSxzzDataGridData().getSelectKeys();
        for (String sel : select) {
            auditFszlDeviceService.deleteByGuid(sel);
        }
        addCallbackParam("msg", l("成功删除！"));
    }

    public void gzrydeleteSelect() {
        List<String> select = getGzryDataGridData().getSelectKeys();
        for (String sel : select) {
            auditFszlPersonService.deleteByGuid(sel);
        }
        addCallbackParam("msg", l("成功删除！"));
    }


    /**
     * 经济类型
     *
     * @return
     */
    public List<SelectItem> getSyzxsModel() {
        List<SelectItem> result = new ArrayList<SelectItem>();
        List<CodeItems> codeItems = codeItemsService.listCodeItemsByCodeName("竣工许可所有制形式");
        for (CodeItems codeItem : codeItems) {
            result.add(new SelectItem(codeItem.getItemValue(), codeItem.getItemText()));
        }
        return result;
    }

    /**
     * 校验有效期
     *
     * @return
     */
    public List<SelectItem> getJyyyqModel() {
        List<SelectItem> result = new ArrayList<SelectItem>();
        List<CodeItems> codeItems = codeItemsService.listCodeItemsByCodeName("校验有效期");
        for (CodeItems codeItem : codeItems) {
            result.add(new SelectItem(codeItem.getItemValue(), codeItem.getItemText()));
        }
        return result;
    }

    /**
     * 许可项目下拉
     *
     * @return
     */
    public List<SelectItem> getXkxmModel() {
        List<SelectItem> result = new ArrayList<SelectItem>();
        List<CodeItems> codeItems = codeItemsService.listCodeItemsByCodeName("放射诊疗许可项目");
        for (CodeItems codeItem : codeItems) {
            result.add(new SelectItem(codeItem.getItemValue(), codeItem.getItemText()));
        }
        return result;
    }

    /**
     * 许可证状态
     *
     * @return
     */
    public List<SelectItem> getXkzztModel() {
        List<SelectItem> result = new ArrayList<SelectItem>();
        List<CodeItems> codeItems = codeItemsService.listCodeItemsByCodeName("放射诊疗许可证状态");
        for (CodeItems codeItem : codeItems) {
            result.add(new SelectItem(codeItem.getItemValue(), codeItem.getItemText()));
        }
        return result;
    }

    /**
     * 版本列表
     *
     * @return
     */
    public List<SelectItem> getVersionModel() {
        List<SelectItem> result = new ArrayList<SelectItem>();
        SqlConditionUtil slqc = new SqlConditionUtil();
        slqc.eq("fszlid", dataBean.getFszlid());
        slqc.setOrderDesc("version");
        List<AuditFszl> list = service.findList("select * from audit_fszl " + new SqlHelper().getSql(slqc.getMap()));
        ArrayList<Integer> has = new ArrayList<>();
        for (AuditFszl auditFszl : list) {
            if (has.contains(auditFszl.getVersion())) {
                continue;
            }
            has.add(auditFszl.getVersion());
            result.add(new SelectItem(auditFszl.getRowguid(), "V" + auditFszl.getVersion()));
        }
        return result;
    }

    //下载证照
    public FileUploadModel9 getSltfileUploadModel() {
        if (sltfileUploadModel == null) {
            sltfileUploadModel = new FileUploadModel9(
                    new DefaultFileUploadHandlerImpl9(sltcliengguid, null, null, null, userSession.getUserGuid(), userSession.getDisplayName()));
        }
        return sltfileUploadModel;
    }

    public AuditFszl getDataBean() {
        return dataBean;
    }

    public void setDataBean(AuditFszl dataBean) {
        this.dataBean = dataBean;
    }

    public String getSltcliengguid() {
        return sltcliengguid;
    }

    public void setSltcliengguid(String sltcliengguid) {
        this.sltcliengguid = sltcliengguid;
    }
}
