package com.epoint.cert.basic.certcatalog.certmetadata;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.cert.basic.certcatalog.certmetadata.domain.CertMetadata;
import com.epoint.cert.basic.certcatalog.certmetadata.inter.ICertMetaData;
import com.epoint.cert.commonutils.CertConstant;
import com.epoint.cert.commonutils.SqlUtils;
import com.epoint.cert.commonutils.ValidateUtil;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.metadata.code.api.ICodeMainService;
import com.epoint.frame.service.metadata.code.api.ICodeMainServiceInternal;
import com.epoint.frame.service.metadata.mis.control.api.IControlService;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.frame.service.metadata.mis.util.DataTypeService9;

/**
 * 证照元数据新增页面对应的后台
 *
 * @作者 dingwei
 * @version [版本号, 2017年10月30日]
 */
@RestController("certmetaaddaction")
@Scope("request")
public class CertMetaAddAction extends BaseController
{
    private static final long serialVersionUID = 1L;

    /**
     * 证照元数据实体
     */
    private CertMetadata databean;

    /**
     * 数据类型下拉列表
     */
    private List<SelectItem> dataTypeModel;

    /**
     * 字段显示类型下拉列表
     */
    private List<SelectItem> displayTypeModel;

    /**
     * 选择代码源代码下拉列表
     */
    private List<SelectItem> codeSourceModel;

    /**
     * 是否下拉列表model
     */
    private List<SelectItem> whetherModel;

    /**
     * 自定义校验规则下拉列表model
     */
    private List<SelectItem> validateModel;

    /**
     * 基本信息字段model
     */
    private List<SelectItem> relatedFieldModel;

    /**
     * 日期格式的model
     */
    private List<SelectItem> dataFormatModel;

    /**
     * 数据类型service
     */
    private DataTypeService9 dataTypeService = new DataTypeService9();

    /**
     * 获取显示控件的service
     */
    @Autowired
    private IControlService iControlService;

    /**
     * 获取代码字典的service
     */
    @Autowired
    private ICodeMainServiceInternal iCodeMainServiceInternal;

    /**
     * 证照元数据api
     */
    @Autowired
    private ICertMetaData iCertMetaData;

    /**
     * 前台选中的数据类型
     */
    private String dataTypeSel;

    /**
     * 父级元数据guid
     */
    private String parentguid;

    /**
     * 证照目录guid
     */
    private String certguid;

    @Override
    public void pageLoad() {
        parentguid = getRequestParameter("parentguid");
        certguid = getRequestParameter("certguid");

        databean = new CertMetadata();
        // 是否必填
        databean.setNotnull(CertConstant.CONSTANT_STR_ONE);
        // 是否在页面中显示
        databean.setDispinadd(CertConstant.CONSTANT_STR_ONE);
        // 控件显示宽度
        databean.setControlwidth(CertConstant.CONSTANT_INT_ONE);
        // 字段类型，默认为nvarchar
        databean.setFieldtype("nvarchar");
        // 显示类型，默认为textbox
        databean.setFielddisplaytype("textbox");
        // 是否关联子表表单
        databean.setIsrelatesubtable(CertConstant.CONSTANT_STR_ZERO);

        // 如果能获取到parentguid，说明是子表属性
        if (StringUtil.isNotBlank(parentguid)) {
            databean.setParentguid(parentguid);
        }

        this.initTypeAdd();
    }

    /**
     *
     *  新增元数据
     * @param isclose
     *            是否关闭
     */
    public void add(String isclose) {
        // 校验中英文名称重复
        String message = getMessage(certguid, databean.getParentguid(), databean.getFieldchinesename(),
                databean.getFieldname());

        // 根据错误消息，控制是否可新增
        if (StringUtil.isNotBlank(message)) {
            addCallbackParam("msg", message);
            return;
        }

        databean.setOperatedate(new Date());
        databean.setRowguid(UUID.randomUUID().toString());
        databean.setOperateusername(userSession.getDisplayName());
        // 设置对应的部门证照目录guid
        databean.setCertguid(certguid);
        // 排序号默认为0
        if (StringUtil.isBlank(databean.getOrdernum())) {
            databean.setOrdernum(CertConstant.CONSTANT_INT_ZERO);
        }
        // 最大长度默认为0
        if (StringUtil.isBlank(databean.getMaxlength())) {
            databean.setMaxlength(CertConstant.CONSTANT_INT_ZERO);
        }
        // 空间宽度默认为单倍
        if (databean.getControlwidth() == null) {
            databean.setMaxlength(CertConstant.CONSTANT_INT_ZERO);
        }
        iCertMetaData.addMetaData(databean);

        // 保存并关闭，保存并新增返回不同的提示信息
        if (CertConstant.CONSTANT_STR_ONE.equals(isclose)) {
            addCallbackParam("msg", "epoint.alertAndClose(\"新增成功\");");
        }
        else {
            addCallbackParam("msg",
                    "epoint.alert(\"新增成功\",null,function callback(){location.href = location.href;},null);");
        }

    }

    /**
     *  获得ContionMap
     *  @param certguid 证照目录guid
     *  @param parentguid 父级元数据guid
     *  @param parentguid 中/英文名称
     *  @param isEn 是否英文字段
     *  @return
     */
    private SqlUtils getConditionSql(String certguid, String parentguid, String name, boolean isEn) {
        SqlUtils sqlUtils = new SqlUtils();
        sqlUtils.eq("certguid", certguid);

        // 子表元数据对应的父表属性主键
        if (StringUtil.isNotBlank(parentguid)) {
            sqlUtils.eq("parentguid", parentguid);
        }
        else {
            sqlUtils.isBlank("parentguid");
        }

        // 判断中英文
        if (isEn) {
            sqlUtils.eq("fieldname", name);
        }
        else {
            sqlUtils.eq("fieldchinesename", name);
        }

        return sqlUtils;
    }

    /**
     *  获得验证信息
     *  @param certguid
     *  @param parentguid
     *  @param chineseName
     *  @param englishName
     *  @return
     */
    public String getMessage(String certguid, String parentguid, String chineseName, String englishName) {
        String message = CertConstant.BLANK;
        // 校验中文名称重复
        SqlUtils conditionSql = this.getConditionSql(certguid, parentguid, chineseName, false);
        int chCount = iCertMetaData.getListCount(conditionSql.getMap());
        if (chCount > 0) {
            return "epoint.alert(\"中文名称不允许重复\")";
        }

        // 检验英文名称是否是关键字
        String englishNamelowercase = StringUtil.toLowerCase(englishName);
        if ("int".equals(englishNamelowercase) || "nvarchar".equals(englishNamelowercase)
                || "numeric".equals(englishNamelowercase) || "datetime".equals(englishNamelowercase)
                || "ntext".equals(englishNamelowercase) || "image".equals(englishNamelowercase)
                || "integer".equals(englishNamelowercase)) {
            return "epoint.alert(\"英文名称不允许使用当前填写的关键字\");";
        }
        // 校验英文名称重复
        SqlUtils sqlUtils = this.getConditionSql(certguid, parentguid, englishName, true);
        int enCount = iCertMetaData.getListCount(sqlUtils.getMap());
        if (enCount > 0) {
            return "epoint.alert(\"英文名称不允许重复\");";
        }

        return message;
    }

    /**
     *  初始化新增时字段显示类型
     */
    public void initTypeAdd() {
        // 字段类型
        String fieldType = DataTypeService9.DATATYPE_NVARCHAR;
        // 根据选中的数据类型,查找对应能够显示该类型的控件集
        displayTypeModel = DataUtil.convertMap2ComboBox(iControlService.getDispSelectItemByType(fieldType));
        displayTypeModel.remove(0);
    }

    public List<SelectItem> getDataTypeModel() {
        if (dataTypeModel == null) {
            List<Map<String, String>> dataTypeSelectItemList = dataTypeService.getDataTypeSelectItem(false);
            // 子表配置不支付附件
            if (StringUtil.isNotBlank(parentguid) && ValidateUtil.isNotBlankCollection(dataTypeSelectItemList)) {
                List<Map<String, String>> newDataSelectItemList = new ArrayList<>();
                for (Map<String, String> itemMap : dataTypeSelectItemList) {
                    if (!itemMap.containsKey("二进制流(Image)")) {
                        newDataSelectItemList.add(itemMap);
                    }
                }
                dataTypeSelectItemList = newDataSelectItemList;
            }
            dataTypeModel = DataUtil.convertMap2ComboBox(dataTypeSelectItemList);
        }
        return dataTypeModel;
    }

    public List<SelectItem> getDisplayTypeModel() {
        if (StringUtil.isNotBlank(dataTypeSel)) {
            displayTypeModel = DataUtil.convertMap2ComboBox(iControlService.getDispSelectItemByType(dataTypeSel));
            displayTypeModel.remove(0);
            displayTypeModel.removeIf(selectItem -> {
                if ("webeditor".equals(selectItem.getValue().toString())) {
                    return true;
                }
                else {
                    return false;
                }
            });
        }
        return displayTypeModel;
    }

    public List<SelectItem> getCodesourceModel() {
        if (codeSourceModel == null) {
            codeSourceModel = DataUtil.convertMap2ComboBox(
                    ICodeMainService.changeCodeSelectItems(iCodeMainServiceInternal.listCodeMain(), true));
            codeSourceModel.remove(0);
        }
        return codeSourceModel;
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> getWhetherModel() {
        if (whetherModel == null) {
            whetherModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "是否", null, false));
        }
        return this.whetherModel;
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> getRelatedFieldModel() {
        if (relatedFieldModel == null) {
            relatedFieldModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "证照基本信息字段", null, false));
        }
        return this.relatedFieldModel;
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> getDateFormatModel() {
        if (dataFormatModel == null) {
            dataFormatModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "日期格式", null, false));
        }
        return this.dataFormatModel;
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> getValidateModel() {
        if (validateModel == null) {
            validateModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "自定义校验规则", null, true));
        }
        return this.validateModel;
    }

    public String getDataTypeSel() {
        return dataTypeSel;
    }

    public void setDataTypeSel(String dataTypeSel) {
        this.dataTypeSel = dataTypeSel;
    }

    public CertMetadata getDatabean() {
        return databean;
    }

    public void setDatabean(CertMetadata databean) {
        this.databean = databean;
    }

}
