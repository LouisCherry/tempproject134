package com.epoint.xmz.gxhimportcert.action;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.EpointKeyNames9;
import com.epoint.basic.authentication.UserSession;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.dataimport.DataImportModel9;
import com.epoint.basic.faces.dataimport.ImportExcelHandler;
import com.epoint.basic.faces.export.ExportModel;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.utils.classpath.ClassPathUtil;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.file.FileManagerUtil;
import com.epoint.core.utils.image.ImageUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.frame.service.message.api.IMessagesCenterService;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.frame.service.metadata.code.entity.CodeItems;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.xmz.gxhimportcert.api.IGxhImportCertLsService;
import com.epoint.xmz.gxhimportcert.api.entity.GxhImportCert;
import com.epoint.xmz.gxhimportcert.api.entity.GxhImportCertLs;

import cn.hutool.core.lang.UUID;

@RestController("gxhimportcertlslistaction")
@Scope("request")
public class GxhImportCertLsListAction extends BaseController
{
    @Autowired
    private IGxhImportCertLsService service;

    @Autowired
    private ICodeItemsService iCodeItemsService;

    @Autowired
    private IMessagesCenterService messagesCenterService;

    /**
     * 个性化证照导入表实体对象
     */
    private GxhImportCertLs dataBean;

    /**
     * 表格控件model
     */
    private DataGridModel<GxhImportCertLs> model;

    /**
     * 导出模型
     */
    private ExportModel exportModel;

    /**
     * 短信状态下拉列表model
     */
    private List<SelectItem> smstatusModel = null;

    private DataImportModel9 dataImportModel;
    private List<String> struList = new ArrayList<String>();
    int insertCount = 0;
    String formatStr = "yyyy-MM-dd";

    public void pageLoad() {
        
        SqlConditionUtil sql = new SqlConditionUtil();
        sql.in("DATE(yxqenddate)", " DATE_SUB(CURDATE(), INTERVAL 1 DAY), DATE_SUB(CURDATE(), INTERVAL 2 DAY) ");
        List<GxhImportCertLs> list = service.findListByCondition(sql.getMap());
        System.out.print(list.size());
        

    }

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
                public String saveExcelData(String filename, String sheetName, int sheet, int curRow, int totalRows, Object[] data) {
                    Map<String, Object> map = new HashMap<>();
                    String message = null;
                    SqlConditionUtil sql = new SqlConditionUtil();
                    // 默认导入sheet1的内容
                    if (sheet == 0) {

                        if (curRow > 0) {

                            GxhImportCertLs gxhImportCert = new GxhImportCertLs();
                            gxhImportCert.setRowguid(UUID.randomUUID().toString());
                            gxhImportCert.setCertname(data[1].toString());
                            gxhImportCert.setYxqstartdate(EpointDateUtil.convertString2Date(data[2].toString(), formatStr));
                            gxhImportCert.setYxqenddate(EpointDateUtil.convertString2Date(data[3].toString(), formatStr));
                            gxhImportCert.setApplyname(data[4].toString());
                            gxhImportCert.setMobile(data[5].toString());
                            gxhImportCert.setSmstatus("10");
                            service.insert(gxhImportCert);
                            insertCount++;
                        }

                    }
                    return message;
                }

                /**
                 * saveExcelData把数据都加入list后，这个方法动态加载第一行的数据作为grid表头
                 */
                @Override
                public void refreshTable() {
                    dataImportModel.setMessage("导入成功" + insertCount + "条数据！");
                }
            });
        }
        return dataImportModel;
    }

    public String getItemValueByText(String codeName, String text) {
        String value = "";
        List<CodeItems> codeList = iCodeItemsService.listCodeItemsByCodeName(codeName);
        for (CodeItems codeItem : codeList) {
            if (StringUtil.isNotBlank(text) && text.equals(codeItem.getItemText())) {
                value = codeItem.getItemValue();
            }
        }
        return value;
    }

    /**
     * 删除选定
     */
    public void deleteSelect() {
        List<String> select = getDataGridData().getSelectKeys();
        for (String sel : select) {
            service.deleteByGuid(sel);
        }
        addCallbackParam("msg", "成功删除！");
    }

    public DataGridModel<GxhImportCertLs> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<GxhImportCertLs>()
            {

                @Override
                public List<GxhImportCertLs> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    SqlConditionUtil sql = new SqlConditionUtil();

                    if (StringUtil.isNotBlank(dataBean.getApplyname())) {
                        sql.like("applyname", dataBean.getApplyname());
                    }
                    if (StringUtil.isNotBlank(dataBean.getSmstatus())) {
                        sql.eq("smstatus", dataBean.getSmstatus());
                    }
                    PageData<GxhImportCertLs> gxhImportCertPageData = service.findPageData(sql.getMap(), first, pageSize, sortField, sortOrder);
                    int count = service.getListCount(sql.getMap());
                    this.setRowCount(count);
                    return gxhImportCertPageData.getList();
                }

            };
        }
        return model;
    }

    public String filepath = ClassPathUtil.getDeployWarPath() + File.separator + "BigFileUpLoadStorage" + File.separator + "temp" + File.separator + "梁山县证照数据导入模板.xlsx";

    /**
     * 检查导入模板配置文件是否存在
     */
    public void checkdownloadDaorumb() {
        if (com.alibaba.csp.sentinel.util.StringUtil.isBlank(filepath)) {
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
            if (com.alibaba.csp.sentinel.util.StringUtil.isNotBlank(imageType)) {
                String auto = getRequestParameter(EpointKeyNames9.ATTACH_DOWN_MODE);
                // 如果url中没有特殊指定
                if (com.alibaba.csp.sentinel.util.StringUtil.isBlank(auto)) {
                    // 查询有没有系统参数配置
                    auto = configService.getFrameConfigValue(EpointKeyNames9.ATTACH_DOWN_MODE);
                }
                // 如果配置了auto
                if (com.alibaba.csp.sentinel.util.StringUtil.isNotBlank(auto) && ("openAuto").equalsIgnoreCase(auto)) {
                    // 将名字置空,自动打开
                    contentDisposition = "inline";
                }
            }
            byte[] contentFromSystem = FileManagerUtil.getContentFromSystem(filepath);
            InputStream byteArrayInputStream = new ByteArrayInputStream(contentFromSystem);
            // 文件名转码
            if (com.alibaba.csp.sentinel.util.StringUtil.isNotBlank(fileName)) {
                try {
                    String userAgent = request.getHeader("User-Agent");
                    // userAgent为空或者判断出来是ie情况，还用以前逻辑。否则都转成ISO-8859-1
                    if (com.alibaba.csp.sentinel.util.StringUtil.isBlank(userAgent) || userAgent.toLowerCase().contains("trident")) {
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
                    this.sendRespose(requestContext.getReq(), byteArrayInputStream, fileName, contentType, false, headers);
                }
                catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    public GxhImportCertLs getDataBean() {
        if (dataBean == null) {
            dataBean = new GxhImportCertLs();
        }
        return dataBean;
    }

    public void setDataBean(GxhImportCertLs dataBean) {
        this.dataBean = dataBean;
    }

    public ExportModel getExportModel() {
        if (exportModel == null) {
            exportModel = new ExportModel("xkzh,taskname,certname,yxqstartdate,yxqenddate,applyname,tyshxydm,dwfzr,mobile,handleks,ksphone,is_zb", "许可证号,事项名称,证照名称,有效期开始日期,有效期截止日期,申请单位,统一社会信用代码,单位负责人姓名,手机号码,办理科室,办理科室联系电话,是否自办");
        }
        return exportModel;
    }

    public List<SelectItem> getSmstatusModel() {
        if (smstatusModel == null) {
            smstatusModel = DataUtil.convertMap2ComboBox((List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "证照导入短信状态", null, true));
        }
        return this.smstatusModel;
    }

}
