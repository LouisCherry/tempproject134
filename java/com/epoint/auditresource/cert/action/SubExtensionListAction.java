package com.epoint.auditresource.cert.action;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.controller.BaseController;
import com.epoint.cert.basic.certcatalog.certmetadata.domain.CertMetadata;
import com.epoint.cert.basic.certinfo.certinfosubextension.domain.CertInfoSubExtension;
import com.epoint.cert.external.ICertConfigExternal;
import com.epoint.cert.external.ICertInfoExternal;
import com.epoint.common.util.ValidateUtil;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.json.JsonUtil;
import com.epoint.core.utils.log.LogUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.frame.service.metadata.code.entity.CodeItems;
import com.epoint.xmz.buildinfo.api.IBuildInfoService;

/**
 * 证照子拓展表列表页面对应的后台(关联证照新增)
 *
 * @author dingwei
 * @version [版本号, 2017-11-21 09:01:53]
 */
@RestController("subextensioninfolistaction")
@Scope("request")
public class SubExtensionListAction extends BaseController
{

    private static final long serialVersionUID = 748634644932411288L;

    /**
     * 子元数据集合
     */
    private List<CertMetadata> subMetadataList;

    /**
     * 子表列表表格
     */
    private DataGridModel<Record> model;

    
    /**
     * 元数据api
     */
    @Autowired
    private ICertConfigExternal certConfigExternalService;
    @Autowired
    private IAuditProject auditProjectServcie;
    
    @Autowired
    private IBuildInfoService iBuildInfoService;
    
    

    /**
     * 元数据api
     */
    @Autowired
    private ICertInfoExternal certInfoExternalService;
    
    /**
     * 代码项接口
     */
    @Autowired
    private ICodeItemsService codeItemsService;


    /**
    * 日志
    */
    private static Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());

    /**
     * 父元数据的主键
     */
    private String guid;

    /**
     * 父级元数据对应的parentguid(随机生成, 父级元数据主键)
     */
    private String parentguid;

    @Override
    public void pageLoad() {
        guid = getRequestParameter("guid");
        parentguid = getRequestParameter("parentguid");
        if (StringUtil.isNotBlank(guid) && StringUtil.isNotBlank(parentguid)) {
            subMetadataList = certConfigExternalService.selectSubDispinListByCertguid(guid);
            if (ValidateUtil.isBlankCollection(subMetadataList)) {
                addCallbackParam("msg", "不存在对应的子元数据!");
                return;
            }
            // 判断是否是第一次加载
            if (!isPostback()) {
                // 生成动态列
                initDynamicColumn();
            }
        }
    }

    public DataGridModel<Record> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<Record>()
            {
                @Override
                public List<Record> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    List<Record> recordList = new ArrayList<Record>();
                    PageData<Record> pageData = new PageData<Record>();
                    pageData.setList(recordList);
                    pageData.setRowCount(recordList.size());
                    if (ValidateUtil.isBlankCollection(subMetadataList)) {
                        return recordList;
                    }
                    // 查找子表中的数据(不分页)
                    List<CertInfoSubExtension> subExtensionList = certInfoExternalService
                            .selectSubExtensionByParentguid("rowguid, subextension", parentguid);
                    // json转成Recordlist
                    if (ValidateUtil.isNotBlankCollection(subExtensionList)) {
                        for (CertInfoSubExtension subExtension : subExtensionList) {
                            String extension = subExtension.getSubextension();
                            Record record = JsonUtil.jsonToObject(extension, Record.class);
                            convertDateSubList(subMetadataList, record);
                            record.set("rowguid", subExtension.getRowguid());
                            recordList.add(record);
                        }
                    }else {
                    	AuditProject project = auditProjectServcie.getAuditProjectByRowGuid(getRequestParameter("projectguid"), null).getResult();
                        if (project != null) {
                       	if (project.getProjectname().contains("人防工程施工图设计文件核准") && StringUtil.isNotBlank(project.getSubappguid())) {
                       		String sql = "select * from build_info where subappguid = ?";
                       		List<Record> records = iBuildInfoService.findList(sql, project.getSubappguid());
                       		for (Record record : records) {
                       			CertInfoSubExtension extension = new CertInfoSubExtension();
                       			String rowguid = UUID.randomUUID().toString();
                                extension.setRowguid(rowguid);
                                extension.setParentguid(parentguid);
                                extension.setSubextension(JsonUtil.objectToJson(record));
                                extension.setOperatedate(new Date());
                                certInfoExternalService.addSubExtension(extension);
                                
                                convertDateSubList(subMetadataList, record);
                                record.set("rowguid", rowguid);
                                recordList.add(record);
                                
                       		}
                       		
                       	 }
                        }
                        
                       
                       
                        
                        
                        
                        
                        
                        
                    }
                    
                   
                    
                    
                    int size = recordList.size();
                    if(first>=0 && pageSize>=0){
                        recordList = recordList.stream().skip(first).limit(pageSize).collect(Collectors.toList());
                    }
                    pageData.setList(recordList);
                    pageData.setRowCount(size);
                    this.setRowCount(pageData.getRowCount());
                    return pageData.getList();
                }
            };
        }

        return model;
    }
    
    public void convertDateSubList(List<CertMetadata> metadataList, Record record) {
        if (ValidateUtil.isBlankCollection(metadataList) || record == null) {
            return;
        }

        for (CertMetadata metadata : metadataList) {
            // 展示显示的字段 时间格式YYYY-MM-DD
            if(ZwfwConstant.INPUT_TYPE_DATE_TIME.equals(metadata.getFieldtype())){
                Date fieldDate = record.getDate(metadata.getFieldname());
                if (fieldDate != null) {
                    record.set(metadata.getFieldname(), EpointDateUtil.convertDate2String(fieldDate));
                }
            }
            // 显示错误代码项
            if(StringUtil.isNotBlank(metadata.getDatasource_codename())) {
                String value = record.getStr(metadata.getFieldname());
                if (StringUtil.isNotBlank(value)) {
                    String itemName = codeItemsService.getItemTextByCodeName(metadata
                            .getDatasource_codename(), value);
                    if (StringUtil.isBlank(itemName)) {
                        record.set(metadata.getFieldname(), value);
                        metadata.setDatasource_codename(null);
                    }else{
                        record.set(metadata.getFieldname(), itemName);
                    }
                }
            }
            
            if("checkbox".equalsIgnoreCase(metadata.getFielddisplaytype())){
                String booleanstr = record.getStr(metadata.getFieldname());
                if("true".equals(booleanstr)){
                    record.set(metadata.getFieldname(),"是");
                }
                if("false".equals(booleanstr)){
                    record.set(metadata.getFieldname(),"否");
                }
            }
        }
    }

    /**
     * 生成动态列
     */
    private void initDynamicColumn() {
        // 返回给前台用json格式
        JSONObject rtnJson = new JSONObject();
        List<Map<String, Object>> columnList = new ArrayList<Map<String, Object>>();

        addColumn(columnList, subMetadataList);
        try {
            rtnJson.put("columns", columnList);
            addCallbackParam("columns", rtnJson.toString());
        }
        catch (JSONException e) {
            e.printStackTrace();
            log.error(String.format("生成动态列失败 [metadataguid = %s]", guid));
        }
    }

    /**
     * 设置每列的属性
     *
     * @param columnList
     */
    private void addColumn(List<Map<String, Object>> columnList, List<CertMetadata> metadataList) {
        // 选择框
        Map<String, Object> checkboxMap = new HashMap<String, Object>();
        checkboxMap.put("type", "checkcolumn");// 设置特殊列
        checkboxMap.put("name", "check_column");
        checkboxMap.put("width", "25");
        columnList.add(checkboxMap);
        // 设置特殊列
        Map<String, Object> idMap = new HashMap<String, Object>();
        idMap.put("type", "indexcolumn");
        idMap.put("width", "25");
        idMap.put("headerAlign", "center");
        idMap.put("align", "center");
        idMap.put("header", "序");
        columnList.add(idMap);

        // 只取前5条(字段太多,前台展示有问题)
        int index = 0;
        for (CertMetadata metadata : metadataList) {
            if (index > 4) {
                break;
            }
            Map<String, Object> fieldMap = new HashMap<String, Object>();
            fieldMap.put("field", StringUtil.toLowerCase(metadata.getFieldname()));
            fieldMap.put("headerAlign", "center");
            fieldMap.put("align", "center");
            fieldMap.put("header", metadata.getFieldchinesename());
            if (StringUtil.isNotBlank(metadata.getDatasource_codename())) {
                fieldMap.put("dataData", getCodeData(metadata.getDatasource_codename()));
            }
            if ("datepicker".equals(StringUtil.toLowerCase(metadata.getFielddisplaytype()))) {
                fieldMap.put("format", "yyyy-MM-dd");
            }
            columnList.add(fieldMap);
            index++;
        }

        // 修改按钮
        Map<String, Object> eidtMap = new HashMap<String, Object>();
        eidtMap.put("width", "25");
        eidtMap.put("headerAlign", "center");
        eidtMap.put("header", "修改");
        eidtMap.put("align", "center");
        eidtMap.put("renderer", "onEditRenderer");
        columnList.add(eidtMap);
        // 查询按钮
        Map<String, Object> detailMap = new HashMap<String, Object>();
        detailMap.put("width", "25");
        detailMap.put("headerAlign", "center");
        detailMap.put("header", "查看");
        detailMap.put("align", "center");
        detailMap.put("renderer", "onDetailRenderer");
        columnList.add(detailMap);
    }

    /**
     * 获取代码项内容
     *
     * @param codeName
     * @return
     */
    public String getCodeData(String codeName) {
        StringBuffer rtnString = new StringBuffer("[");
        List<CodeItems> codeItemList = codeItemsService.listCodeItemsByCodeName(codeName);
        if (ValidateUtil.isBlankCollection(codeItemList)) {
            return "";
        }

        for (CodeItems codeItems : codeItemList) {
            rtnString.append("{id:'" + codeItems.getItemValue() + "',text:'" + codeItems.getItemText() + "'},");
        }
        rtnString = new StringBuffer(rtnString.substring(0, rtnString.length() - 1));
        rtnString.append("]");
        return rtnString.toString();
    }
    
    /**
     *  删除选中
     */
    public void deleteSelect() {
        List<String> selectList = getDataGridData().getSelectKeys();
        if (ValidateUtil.isNotBlankCollection(selectList)) {
            for (String key : selectList) {
                certInfoExternalService.deleteExtensionByRowguid(key);
            }
        }
        addCallbackParam("msg", "删除成功");
    }
}
