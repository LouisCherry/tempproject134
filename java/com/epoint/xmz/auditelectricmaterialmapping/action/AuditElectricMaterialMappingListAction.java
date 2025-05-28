package com.epoint.xmz.auditelectricmaterialmapping.action;

import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.audittask.material.domain.AuditTaskMaterial;
import com.epoint.basic.audittask.material.inter.IAuditTaskMaterial;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.export.ExportModel;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.frame.service.metadata.code.entity.CodeItems;
import com.epoint.frame.service.metadata.mis.util.ListGenerator;
import com.epoint.xmz.auditelectricdata.api.IAuditElectricDataService;
import com.epoint.xmz.auditelectricmaterialmapping.api.IAuditElectricMaterialMappingService;
import com.epoint.xmz.auditelectricmaterialmapping.api.entity.AuditElectricMaterialMapping;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


/**
 * 电力材料映射表list页面对应的后台
 *
 * @author lee
 * @version [版本号, 2023-08-10 15:24:08]
 */
@RestController("auditelectricmaterialmappinglistaction")
@Scope("request")
public class AuditElectricMaterialMappingListAction extends BaseController {
    @Autowired
    private IAuditElectricMaterialMappingService service;

    /**
     * 电力材料映射表实体对象
     */
    private AuditElectricMaterialMapping dataBean;

    /**
     * 表格控件model
     */
    private DataGridModel<AuditElectricMaterialMapping> model;

    /**
     * 导出模型
     */
    private ExportModel exportModel;


    @Autowired
    private IAuditElectricMaterialMappingService materialMappingService;

    @Autowired
    private ICodeItemsService codeItemsService;

    @Autowired
    private IAuditElectricDataService electricDataService;

    @Autowired
    private IAuditTaskMaterial auditTaskMaterial;

    @Autowired
    private IAuditTask auditTask;


    public void pageLoad() {
    }

    /**
     * 初始化材料数据
     */
    public void initMaterialData() {
        List<CodeItems> codeItems = codeItemsService.listCodeItemsByCodeName("在用电力事项");
        if (CollectionUtils.isNotEmpty(codeItems)) {
            codeItems.forEach(codeItem -> {
                AuditTask auditTask = electricDataService.getTaskByItemId(codeItem.getItemValue());
                if (auditTask != null) {
                    List<AuditTaskMaterial> materialList = auditTaskMaterial.getUsableMaterialListByTaskguid(auditTask.getRowguid()).getResult();
                    if (CollectionUtils.isNotEmpty(materialList)) {
                        // 删除已有数据
                        materialMappingService.deleteByItemId(codeItem.getItemValue());
                        // 从新生成
                        materialList.forEach(material -> {
                            AuditElectricMaterialMapping materialMapping = new AuditElectricMaterialMapping();
                            materialMapping.setRowguid(UUID.randomUUID().toString());
                            materialMapping.setOperatedate(new Date());
                            materialMapping.setItemid(codeItem.getItemValue());
                            materialMapping.setOuname(auditTask.getOuname());
                            materialMapping.setTaskname(auditTask.getTaskname());
                            materialMapping.setUpdatetime(new Date());
                            materialMapping.setMaterialid(material.getMaterialid());
                            materialMapping.set("materialName",material.getMaterialname());
                            materialMappingService.insert(materialMapping);
                        });
                    }
                    addCallbackParam("msg", "初始化成功");
                }
            });
        }
        else {
            addCallbackParam("msg", "未配置”在用电力事项“代码项");
        }
    }

    /**
     * 保存按钮
     */
    public void save() {
        List<AuditElectricMaterialMapping> wrappedData = getDataGridData().getWrappedData();
        List<AuditElectricMaterialMapping> modifyList = wrappedData.stream().filter(
                e -> "modified".equals(e.getStr("_state"))).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(modifyList)) {
            modifyList.forEach(e -> {
                e.setUpdatetime(new Date());
                materialMappingService.update(e);
            });
            addCallbackParam("msg", "保存成功");
        }
        else {
            addCallbackParam("msg", "未修改数据");
        }

    }

    /**
     * 删除选定
     */
    public void deleteSelect() {
        List<String> select = getDataGridData().getSelectKeys();
        for (String sel : select) {
            service.deleteByGuid(sel);
        }
        addCallbackParam("msg", l("成功删除！"));
    }

    public DataGridModel<AuditElectricMaterialMapping> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<AuditElectricMaterialMapping>() {

                @Override
                public List<AuditElectricMaterialMapping> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    List<Object> conditionList = new ArrayList<Object>();
                    String conditionSql = ListGenerator.getSearchSql(getRequestContext().getComponents(), conditionList);
                    List<AuditElectricMaterialMapping> list = service.findList(
                            ListGenerator.generateSql("audit_electric_material_mapping", conditionSql, sortField, sortOrder), first, pageSize,
                            conditionList.toArray());
                    int count = service.countAuditElectricMaterialMapping(ListGenerator.generateSql("audit_electric_material_mapping", conditionSql), conditionList.toArray());
                    this.setRowCount(count);
                    return list;
                }

            };
        }
        return model;
    }


    public AuditElectricMaterialMapping getDataBean() {
        if (dataBean == null) {
            dataBean = new AuditElectricMaterialMapping();
        }
        return dataBean;
    }

    public void setDataBean(AuditElectricMaterialMapping dataBean) {
        this.dataBean = dataBean;
    }

    public ExportModel getExportModel() {
        if (exportModel == null) {
            exportModel = new ExportModel("taskname,itemid,ouname,UUID,materialid",
                    "事项名称,事项编码,部门名称,材料来源编码,materialId");
        }
        return exportModel;
    }


}
