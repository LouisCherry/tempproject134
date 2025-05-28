package com.epoint.guidang.auditprojectguidang.action;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.export.ExportModel;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.core.utils.zip.ZipUtil;
import com.epoint.frame.service.attach.entity.FrameAttachConfig;
import com.epoint.guidang.auditprojectguidang.api.IAuditprojectguidangService;
import com.epoint.guidang.auditprojectguidang.entity.Auditprojectguidang;

/**
 * 办件归档记录表list页面对应的后台
 * 
 * @author chengninghua
 * @version [版本号, 2018-01-05 14:04:07]
 */
@RestController("auditprojectguidanglistaction")
@Scope("request")
public class AuditprojectguidangListAction extends BaseController
{
    /**
     * 
     */
    private static final long serialVersionUID = -33775042764020262L;
    @Autowired
    private IAuditprojectguidangService service;
    private List<Record> XiaQuModel = null;

    /**
     * 办件归档记录表实体对象
     */
    private Auditprojectguidang dataBean = null;

    /**
     * 表格控件model
     */
    private DataGridModel<Record> model;

    /**
     * 导出模型
     */
    private ExportModel exportModel;

    private CommonDao dao = CommonDao.getInstance();
    public String xiaqucode;
    public String txtflowsn;

    public String getTxtflowsn() {
        return txtflowsn;
    }

    public void setTxtflowsn(String txtflowsn) {
        this.txtflowsn = txtflowsn;
    }

    public String getXiaqucode() {
        return xiaqucode;
    }

    public void setXiaqucode(String xiaqucode) {
        this.xiaqucode = xiaqucode;
    }

    @Override
    public void pageLoad() {
        dataBean = new Auditprojectguidang();
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

    public DataGridModel<Record> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<Record>()
            {

                @Override
                public List<Record> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    String sql = "select ifnull(b.filename,'') as filename,ifnull(b.filepath,'') as filepath,"
                            + " ifnull(b.attachguid,'') as attachguid,a.rowguid,a.applyername,a.flowsn,a.banjiedate,"
                            + " a.banjieresult,a.pviguid,projectname from audit_project a "
                            + " join audit_project_material c on a.RowGuid=c.projectguid "
                            + " join frame_attachinfo d on c.CLIENGGUID=d.CLIENGGUID"
                            + " left join  auditprojectguidang b on a.rowguid=b.projectguid"
                            + " where a.`STATUS`>=90  AND a.APPLYDATE is NOT NULL  AND a.banjiedate is NOT null ";
                    if (StringUtil.isNotBlank(xiaqucode)) {
                        sql += " and a.areacode ='" + xiaqucode + "'";
                    }
                    if (StringUtil.isNotBlank(txtflowsn)) {
                        sql += " and a.flowsn like '%" + txtflowsn + "%'";
                    }
                    sql += " GROUP BY a.RowGuid  ORDER BY a.banjiedate asc";
                    int count = dao.findList(sql, Record.class).size();
                    sql += " limit " + first + "," + pageSize + ";";
                    List<Record> list = dao.findList(sql, Record.class);
                    this.setRowCount(count);
                    return list;
                }

            };
        }
        return model;
    }

    public Auditprojectguidang getDataBean() {
        if (dataBean == null) {
            dataBean = new Auditprojectguidang();
        }
        return dataBean;
    }

    public void setDataBean(Auditprojectguidang dataBean) {
        this.dataBean = dataBean;
    }

    public ExportModel getExportModel() {
        if (exportModel == null) {
            exportModel = new ExportModel("", "");
        }
        return exportModel;
    }

    public List<Record> getXiaQuModel() {
        if (XiaQuModel == null) {
            String sql = "select XiaQuName,XiaQuCode from audit_orga_area  ORDER BY CityLevel ASC,XiaQuCode asc";
            XiaQuModel = dao.findList(sql, Record.class);
        }
        return this.XiaQuModel;
    }

    /**
     * 压缩多个文件
    参数: 
    files - 要压缩的文件数组 
    zipFileName - 压缩后的zip文件名（带路径）
     * 
     */
    public void DoZipFiles() {
        Calendar date = Calendar.getInstance();
        String year = String.valueOf(date.get(Calendar.YEAR));
        String attachguid = "";
        String filepath = "";
        String zipFileName = "";
        String msg = "";
        int success = 0;
        int nosuccess = 0;
        String nosuccesslist = "";
        String nosuccesstask = "";

        //取当初事项配置好的归档地址和年限等信息
        List<String> select = getDataGridData().getSelectKeys();
        for (String sel : select) {
            String sql = "select a.rowguid as projectguid,b.areacode,c.oucode,b.item_id,b.SHENPILB,a.flowsn,d.serviceip,d.saveyear from "
                    + "audit_project a inner join audit_task b on a.taskguid=b.RowGuid "
                    + "inner join frame_ou c on c.OUGUID=b.OUGUID "
                    + "left join guidangmanage d on b.TASK_ID=d.taskid where a.rowguid=?1";
            Record re = dao.find(sql, Record.class, sel);

            if (re != null && StringUtil.isNotBlank(re.getStr("serviceip"))
                    && StringUtil.isNotBlank(re.getStr("saveyear"))) {
                try {
                    //获取文件
                    String filesql = "SELECT CONCAT(a.FILEPATH,a.ATTACHFILENAME) as path from "
                            + "frame_attachinfo a INNER JOIN audit_project_material d "
                            + "on a.CLIENGGUID = d.cliengguid where d.PROJECTGUID=?";
                    List<Record> list = dao.findList(filesql, Record.class, re.getStr("projectguid"));
                    if (list != null && list.size() > 0) {
                        File[] files = new File[list.size()];
                        int i = 0;
                        for (Record r : list) {
                            File file = new File(r.getStr("path"));
                            files[i] = file;
                            i++;
                        }

                        //例如：411600-oucode-item_id-XK-year (4 位)-5年-flowsn。
                        zipFileName = re.getStr("areacode") + "-" + re.getStr("oucode") + "-" + re.getStr("item_id")
                                + "-" + re.getStr("SHENPILB") + "-" + year + "-" + re.getStr("saveyear") + "-"
                                + re.getStr("flowsn") + ".zip";
                        String queryFileConfig = "select * from frame_attachconfig where ISNOWUSE = '1'";
                        String fileConfig = "";
                        CommonDao baseDao = CommonDao.getInstance();
                        FrameAttachConfig frameAttachConfig = baseDao.find(queryFileConfig, FrameAttachConfig.class);
                        if (frameAttachConfig != null
                                && StringUtil.isNotBlank(frameAttachConfig.getAttachConnectionString())) {
                            fileConfig = frameAttachConfig.getAttachConnectionString() + "/BigFileUpLoadStorage/temp/";
                        }
                        else {
                            fileConfig = "/var/share/BigFileUpLoadStorage/temp/";
                        }
                        String dirname = fileConfig + re.getStr("areacode") + "/" + re.getStr("oucode") + "/"
                                + re.getStr("item_id") + "/";
                        File dir = new File(dirname);
                        //system.out.println(dir + ">>>");
                        if (!dir.exists()) {
                            dir.mkdirs();
                        }
                        ZipUtil.doZip(files, dirname + zipFileName);
                        attachguid = UUID.randomUUID().toString();
                        dataBean.setRowguid(UUID.randomUUID().toString());
                        dataBean.setOperatedate(new Date());
                        dataBean.setOperateusername(userSession.getDisplayName());
                        dataBean.setGuidangdate(new Date());
                        dataBean.setGuidangtype("2");//手动归档
                        dataBean.setProjectguid(sel);
                        dataBean.setFlowsn(re.getStr("flowsn"));
                        dataBean.setAttachguid(attachguid);
                        dataBean.setFilename(zipFileName);
                        dataBean.setFilepath(filepath);
                        service.insert(dataBean);
                        dataBean = new Auditprojectguidang();
                        addattach(dirname, zipFileName, attachguid);
                        success++;
                    }
                }
                catch (Exception e) {
                    nosuccess++;
                    nosuccesslist += "办件归档异常：" + re.getStr("flowsn") + ",";
                }
            }
            else {
                nosuccess++;
                nosuccesstask += "归档维护异常：" + re.getStr("item_id") + ",";
            }
        }
        msg = "成功归档办件数：" + success + "；失败归档办件数：" + nosuccess + "<br/>"
                + (nosuccesslist.length() == 0 ? "" : nosuccesslist.substring(0, nosuccesslist.length() - 1)) + "<br/>"
                + (nosuccesstask.length() == 0 ? "" : nosuccesstask.substring(0, nosuccesstask.length() - 1));
        addCallbackParam("msg", msg);
    }

    private void addattach(String dirname, String filename, String attachguid) {
        String sql = "INSERT INTO frame_attachinfo VALUES (null, '" + attachguid + "', '" + filename
                + "', '.zip', null, 'nas', null, '" + attachguid + "', null, 'NasShareDirectory', '" + dirname
                + "', '办件归档', '办件归档', now(), '" + attachguid
                + "', null, null, null, null, null, null, null, null, null, null, null)";
        dao.execute(sql);
        dao.commitTransaction();
    }

}
