package com.epoint.xmz.gxhimportcert.action;

import cn.hutool.core.lang.UUID;
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
import com.epoint.xmz.gxhimportcert.api.IGxhImportCertService;
import com.epoint.xmz.gxhimportcert.api.entity.GxhImportCert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * 个性化证照导入表list页面对应的后台
 *
 * @author dyxin
 * @version [版本号, 2023-06-12 16:30:18]
 */
@RestController("gxhimportcertlistaction")
@Scope("request")
public class GxhImportCertListAction extends BaseController {
    @Autowired
    private IGxhImportCertService service;

    @Autowired
    private ICodeItemsService iCodeItemsService;

    @Autowired
    private IMessagesCenterService messagesCenterService;

    /**
     * 个性化证照导入表实体对象
     */
    private GxhImportCert dataBean;

    /**
     * 表格控件model
     */
    private DataGridModel<GxhImportCert> model;

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
    private List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
    int insertCount = 0;
    String formatStr = "yyyy-MM-dd";

    public void pageLoad() {
    }

    public DataImportModel9 getDataImportModel() {
        if (dataImportModel == null) {
            dataImportModel = new DataImportModel9(new ImportExcelHandler() {
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
                    String message = null;
                    SqlConditionUtil sql = new SqlConditionUtil();
                    // 默认导入sheet1的内容
                    if (sheet == 0) {
                        if (curRow == 0) {
                            // 默认excel的第一行为datagrid的列名
                            for (int i = 0; i < data.length; i++) {
                                struList.add(data[i].toString());
                            }
                            sql.eq("Ywstatus", "1");
                            List<GxhImportCert> gxhImportCerts = service.findListByCondition(sql.getMap());
                            if (gxhImportCerts != null && gxhImportCerts.size() > 0) {
                                for (GxhImportCert cert : gxhImportCerts) {
                                    cert.setYwstatus("2");
                                    service.update(cert);
                                }
                            }
                        }
                        if (curRow > 0) {

                            GxhImportCert gxhImportCert = new GxhImportCert();
                            String content = "";
                            gxhImportCert.setRowguid(UUID.randomUUID().toString());
                            gxhImportCert.setXkzh(data[1].toString());
                            gxhImportCert.setTaskname(data[2].toString());
                            gxhImportCert.setCertname(data[3].toString());
                            gxhImportCert
                                    .setYxqstartdate(EpointDateUtil.convertString2Date(data[4].toString(), formatStr));
                            gxhImportCert
                                    .setYxqenddate(EpointDateUtil.convertString2Date(data[5].toString(), formatStr));
                            gxhImportCert.setApplyname(data[6].toString());
                            gxhImportCert.setTyshxydm(data[7].toString());
                            gxhImportCert.setDwfzr(data[8].toString());
                            gxhImportCert.setMobile(data[9].toString());
                            gxhImportCert.setHandleks(data[10].toString());
                            gxhImportCert.setKsphone(data[11].toString());
                            gxhImportCert.setIs_zb(getItemValueByText("是否", data[12].toString()));
                            gxhImportCert.setYwstatus("1");
                            gxhImportCert.set("areacode", ZwfwUserSession.getInstance().getAreaCode());
                            service.insert(gxhImportCert);
                            insertCount++;

                            // 发短信
                            if ("1".equals(gxhImportCert.getIs_zb())) {
                                //根据辖区取模板
                                content = configService.getFrameConfigValue("cert_msgtemp_two_" + ZwfwUserSession.getInstance().getAreaCode());
                            } else {
                                content = configService.getFrameConfigValue("cert_msgtemp_one_" + ZwfwUserSession.getInstance().getAreaCode());
                            }
                            if (StringUtil.isNotBlank(content)) {
                                content = content.replace("[#=certname=#]", gxhImportCert.getCertname());
                                content = content.replace("[#=yxqenddate=#]",
                                        EpointDateUtil.convertDate2String(gxhImportCert.getYxqenddate(), formatStr));
                                content = content.replace("[#=ksphone=#]", gxhImportCert.getKsphone());
                            }
                            messagesCenterService.insertSmsMessage(UUID.randomUUID().toString(), content, new Date(), 0,
                                    new Date(), gxhImportCert.getMobile(), "-", gxhImportCert.getMobile(),
                                    UserSession.getInstance().getUserGuid(), UserSession.getInstance().getDisplayName(),
                                    gxhImportCert.getMobile(), UserSession.getInstance().getOuGuid(), "", true,
                                    ZwfwUserSession.getInstance().getAreaCode());
                            gxhImportCert.setSmstatus("1");
                            gxhImportCert.setSmsdate(new Date());
                            service.update(gxhImportCert);

//                                map.put(struList.get(i) + "", data[i].toString());
                        }
                        list.add(map);

                    }
                    return message;
                }

                /**
                 * saveExcelData把数据都加入list后，这个方法动态加载第一行的数据作为grid表头
                 */
                @Override
                public void refreshTable() {
//                    Map<String, Object> map = new HashMap<String, Object>();
//                    List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
//                    if (struList != null && struList.size() > 0) {
//                        for (String st : struList) {
//                            Map<String, Object> itemmap = new HashMap<String, Object>();
//                            // 设置每列的属性
//                            itemmap.put("field", st);
//                            itemmap.put("width", "120");
//                            itemmap.put("headerAlign", "center");
//                            itemmap.put("header", st);
//                            datas.add(itemmap);
//                        }
//                    }
//                    map.put("datas", datas);
//                    addViewData("list", JSONArray.fromObject(list).toString());
//                    dataImportModel.getUploadModel().setExtraDatas(map);
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

    public DataGridModel<GxhImportCert> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<GxhImportCert>() {

                @Override
                public List<GxhImportCert> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    SqlConditionUtil sql = new SqlConditionUtil();
                    if (StringUtil.isNotBlank(dataBean.getTyshxydm())) {
                        sql.like("tyshxydm", dataBean.getTyshxydm());
                    }
                    if (StringUtil.isNotBlank(dataBean.getDwfzr())) {
                        sql.like("dwfzr", dataBean.getDwfzr());
                    }
                    if (StringUtil.isNotBlank(dataBean.getSmstatus())) {
                        sql.eq("smstatus", dataBean.getSmstatus());
                    }

                    sql.eq("areacode", ZwfwUserSession.getInstance().getAreaCode());

                    PageData<GxhImportCert> gxhImportCertPageData = service.findPageData(sql.getMap(), first, pageSize,
                            sortField, sortOrder);
                    int count = service.getListCount(sql.getMap());
                    this.setRowCount(count);
                    return gxhImportCertPageData.getList();
                }

            };
        }
        return model;
    }

    public String filepath = ClassPathUtil.getDeployWarPath() + File.separator + "BigFileUpLoadStorage" + File.separator
            + "temp" + File.separator + "证照数据导入模板.xlsx";

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
        } else {
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
                    } else {
                        fileName = new String(fileName.getBytes(), "ISO-8859-1");
                    }
                    Map<String, String> headers = new HashMap<>();

                    if ("inline".equals(contentDisposition)) {
                        headers.put("Content-disposition", "inline; filename=\"" + fileName + "\"");
                    } else {
                        headers.put("Content-disposition", "attachment; filename=\"" + fileName + "\"");
                    }
                    headers.put("Last-Modified", EpointDateUtil.convertDate2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
                    this.sendRespose(requestContext.getReq(), byteArrayInputStream, fileName, contentType, false,
                            headers);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    public GxhImportCert getDataBean() {
        if (dataBean == null) {
            dataBean = new GxhImportCert();
        }
        return dataBean;
    }

    public void setDataBean(GxhImportCert dataBean) {
        this.dataBean = dataBean;
    }

    public ExportModel getExportModel() {
        if (exportModel == null) {
            exportModel = new ExportModel(
                    "xkzh,taskname,certname,yxqstartdate,yxqenddate,applyname,tyshxydm,dwfzr,mobile,handleks,ksphone,is_zb",
                    "许可证号,事项名称,证照名称,有效期开始日期,有效期截止日期,申请单位,统一社会信用代码,单位负责人姓名,手机号码,办理科室,办理科室联系电话,是否自办");
        }
        return exportModel;
    }

    public List<SelectItem> getSmstatusModel() {
        if (smstatusModel == null) {
            smstatusModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "证照导入短信状态", null, true));
        }
        return this.smstatusModel;
    }

}
