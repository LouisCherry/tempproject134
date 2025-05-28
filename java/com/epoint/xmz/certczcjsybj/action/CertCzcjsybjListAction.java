package com.epoint.xmz.certczcjsybj.action;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Map;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.xmz.certczcjsybj.api.entity.CertCzcjsybj;
import com.epoint.xmz.certczqcgxdj.api.entity.CertCzqcgxdj;

import cn.hutool.core.lang.UUID;
import scala.collection.mutable.ArrayBuilder.ofBoolean;

import com.alibaba.csp.sentinel.util.StringUtil;
import com.epoint.basic.EpointKeyNames9;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.dataimport.DataImportModel9;
import com.epoint.basic.faces.dataimport.ImportExcelHandler;
import com.epoint.basic.faces.export.ExportModel;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.utils.classpath.ClassPathUtil;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.file.FileManagerUtil;
import com.epoint.core.utils.image.ImageUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.frame.service.metadata.code.entity.CodeItems;
import com.epoint.frame.service.metadata.mis.util.ListGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import com.epoint.xmz.certczcjsybj.api.ICertCzcjsybjService;

/**
 * 出租车驾驶员人员背景明细库list页面对应的后台
 * 
 * @author dyxin
 * @version [版本号, 2023-05-22 13:17:57]
 */
@RestController("certczcjsybjlistaction")
@Scope("request")
public class CertCzcjsybjListAction extends BaseController
{
    @Autowired
    private ICertCzcjsybjService service;
    
    @Autowired
    ICodeItemsService iCodeItemsService;

    /**
     * 出租车驾驶员人员背景明细库实体对象
     */
    private CertCzcjsybj dataBean;
    
    
    private Date startDate;
    
    
    private Date endDate;

    /**
     * 表格控件model
     */
    private DataGridModel<CertCzcjsybj> model;
    
    /**
     * 导入模型
     */
    private DataImportModel9 dataImportModel;

    private List<String> struList = new ArrayList<String>();
    
    private List<Map<String, Object>> uploadlist = new ArrayList<Map<String, Object>>();
    
    int totalUploadCount=0;
    
    /**
     * 
     * 导入
     * 
     * @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public DataImportModel9 getDataImportModel() {
        if (dataImportModel == null) {
            dataImportModel = new DataImportModel9(new ImportExcelHandler()
            {
                private static final long serialVersionUID = 1L;

                /**
                 * 此方法会将excel表格数据一行一行处理 每行数据都进入该方法，动态加载到list中
                 *
                 * @param filename
                 *            导入的excel名字
                 * @param sheetName
                 *            sheet名字
                 * @param sheet
                 *            第几个sheet
                 * @param curRow
                 *            第几行数据
                 * @param totalRows
                 *            总共多少行
                 * @param data
                 *            数据
                 * @return String 保存信息(如果失败,那么返回失败提示信息,否则返回null)
                 */
                @Override
                public String saveExcelData(String filename, String sheetName, int sheet, int curRow, int totalRows,
                        Object[] data) {
                    Map<String, Object> map = new HashMap<>();
                    // 默认导入sheet1的内容
                    String message=null;
                    if (sheet == 0) {
                        if (curRow == 0) {
                            // 默认excel的第一行为datagrid的列名
                            for (int i = 0; i < data.length; i++) {
                                struList.add(data[i].toString());
                            }
                        }
                        if (curRow > 0) {
                            CertCzcjsybj newCert = new CertCzcjsybj();
                            newCert.setName(data[0].toString());
                            newCert.setSex(getCodeValue(data[1].toString()));
                            newCert.setId(data[2].toString());
                            
                            newCert.setUploadtime(new Date());
                            newCert.setOperatedate(new Date());
                            newCert.setOperateusername(userSession.getDisplayName());
                            newCert.setRowguid(UUID.randomUUID().toString());    
                            service.insert(newCert);
                            totalUploadCount++;
                        }
                        map.put(struList.get(0) + "", data[0].toString());

                        uploadlist.add(map);
                        
                    }
                    return message;
                }

                /**
                 * saveExcelData把数据都加入list后，这个方法动态加载第一行的数据作为grid表头
                 */
                @Override
                public void refreshTable() {
                    dataImportModel.setMessage("成功导入"+(totalUploadCount)+"条数据！");
                }
            });
        }
        return dataImportModel;
    }
    
    public String getCodeValue(String codeText) {
        String value="";
        List<CodeItems> codeItems=iCodeItemsService.listCodeItemsByCodeName("性别");
        for(CodeItems codeItem:codeItems) {
            if(codeItem.getItemText().equals(codeText)) {
                value=codeItem.getItemValue();
            }
        }
        return value;
    }

    /**
     * 导出模型
     */
    private ExportModel exportModel;

    public void pageLoad() {
    }

    /**
     * 删除选定
     * 
     */
    public void deleteSelect() {
        List<String> select = getDataGridData().getSelectKeys();
        for (String sel : select) {
            service.deleteByGuid(sel);
        }
        addCallbackParam("msg", "成功删除！");
    }

    public DataGridModel<CertCzcjsybj> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<CertCzcjsybj>()
            {

                @Override
                public List<CertCzcjsybj> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    
                    SqlConditionUtil sqlcu = new SqlConditionUtil();
                    if (StringUtil.isNotBlank(dataBean.getName())) {
                        sqlcu.like("name", dataBean.getName());
                    }
                    if (StringUtil.isNotBlank(dataBean.getId())) {
                        sqlcu.like("id", dataBean.getId());
                    }
                    if (startDate!=null&&endDate!=null) {
                        sqlcu.between("uploadtime", startDate, endDate);
                    }
                    else if (startDate!=null&&endDate==null) {
                        sqlcu.gt("uploadtime", startDate);
                    }
                    else if (startDate==null&&endDate!=null) {
                        sqlcu.lt("uploadtime", endDate);
                    }
                    PageData<CertCzcjsybj> list = service.findPageData(sqlcu.getMap(), first, pageSize, sortField,
                            sortOrder);
                    int count = service.getCount(sqlcu.getMap());
                    this.setRowCount(count);
                    return list.getList();
                }

            };
        }
        return model;
    }
    
    public String filepath = ClassPathUtil.getDeployWarPath() + File.separator + "BigFileUpLoadStorage" + File.separator
            + "temp" + File.separator + "出租车驾驶员人员背景明细库模板.xlsx";

    /**
     * 检查导入模板配置文件是否存在
     */
    public void checkdownloadDaorumb() {
        if (StringUtil.isBlank(filepath)) {
            addCallbackParam("msg", "请先配置导入模板！");
            return;
        }
        File shydFile = new File(filepath);
        if (!shydFile.exists()) {
            addCallbackParam("msg", "导入模板不存在，请重新配置~");
        }
        else {
            addCallbackParam("check", true);
        }
    }

    /**
     * 下载导入模板
     */
    public void downloadDaorumb() {
        File shydFile = new File(filepath);

        if (shydFile.exists()) {
            String fileName = shydFile.getName();
            // 后缀
            String contentType = fileName.substring(fileName.lastIndexOf("."), fileName.length());
            String imageType = ImageUtil.getImageType(fileName);
            String contentDisposition = ConfigUtil.getConfigValue("AttachDownType");
            // 图片打开模式
            if (StringUtil.isNotBlank(imageType)) {
                String auto = getRequestParameter(EpointKeyNames9.ATTACH_DOWN_MODE);
                // 如果url中没有特殊指定
                if (StringUtil.isBlank(auto)) {
                    // 查询有没有系统参数配置
                    auto = configService.getFrameConfigValue(EpointKeyNames9.ATTACH_DOWN_MODE);
                }
                // 如果配置了auto
                if (StringUtil.isNotBlank(auto) && ("openAuto").equalsIgnoreCase(auto)) {
                    // 将名字置空,自动打开
                    contentDisposition = "inline";
                }
            }
            byte[] contentFromSystem = FileManagerUtil.getContentFromSystem(filepath);
            InputStream byteArrayInputStream = new ByteArrayInputStream(contentFromSystem);
            // 文件名转码
            if (StringUtil.isNotBlank(fileName)) {
                try {
                    String userAgent = request.getHeader("User-Agent");
                    // userAgent为空或者判断出来是ie情况，还用以前逻辑。否则都转成ISO-8859-1
                    if (StringUtil.isBlank(userAgent) || userAgent.toLowerCase().contains("trident")) {
                        fileName = java.net.URLEncoder.encode(fileName, "UTF-8");
                    }
                    else {
                        fileName = new String(fileName.getBytes(), "ISO-8859-1");
                    }
                    Map<String, String> headers = new HashMap<>();

                    if ("inline".equals(contentDisposition)) {
                        headers.put("Content-disposition", "inline; filename=\"" + fileName + "\"");
                    }
                    else {
                        headers.put("Content-disposition", "attachment; filename=\"" + fileName + "\"");
                    }
                    headers.put("Last-Modified", EpointDateUtil.convertDate2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
                    this.sendRespose(requestContext.getReq(), byteArrayInputStream, fileName, contentType, false,
                            headers);
                }
                catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    public CertCzcjsybj getDataBean() {
        if (dataBean == null) {
            dataBean = new CertCzcjsybj();
        }
        return dataBean;
    }

    public void setDataBean(CertCzcjsybj dataBean) {
        this.dataBean = dataBean;
    }

    public ExportModel getExportModel() {
        if (exportModel == null) {
            exportModel = new ExportModel("name,sex,id,remark,uploadtime", "姓名,性别,身份证号,备注,上传时间");
        }
        return exportModel;
    }
    
    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    
    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    


}
