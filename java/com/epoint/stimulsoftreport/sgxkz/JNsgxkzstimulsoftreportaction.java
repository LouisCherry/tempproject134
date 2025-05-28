package com.epoint.stimulsoftreport.sgxkz;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.report.service.api.IReportInfoService;
import com.epoint.report.service.entity.ReportInfo;

@RestController("jnsgxkzstimulsoftreportaction")
@Scope("request")
public class JNsgxkzstimulsoftreportaction extends BaseController
{
    private static final long serialVersionUID = 1L;

    @Autowired
    private IReportInfoService reportInfoService;

    @Autowired
    private IAttachService attachService;

    private List<SelectItem> statusModel = null;

    private List<SelectItem> ounameModel = null;

    public String bapplydate;
    public String eapplydate;
    public String bbanjiedate;
    public String ebanjiedate;
    public String status;
    public String ouname;

    @Override
    public void pageLoad() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.YEAR, -1);
        Date m = c.getTime();
        String mon = df.format(m);
        bapplydate = mon;
        eapplydate = df.format(new Date());
        bbanjiedate = mon;
        ebanjiedate = df.format(new Date());
        status = "90";
        ouname = "济宁市";

    }

    /**
     *
     * 此方法用于获取报表RowGuid，一般前台直接传参即可 [功能详细描述]
     *
     * @param reportName
     * @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public void reportAttchGuid(String reportName) {
        try {
            ReportInfo reportInfo = reportInfoService.getReportInfoBytableName(reportName);

            List<FrameAttachInfo> attachList = attachService.getAttachInfoListByGuid(reportInfo.getRowguid());

            if (attachList.size() > 0)
                addCallbackParam("attachguid", attachList.get(0).getAttachGuid());

        }
        catch (Exception ex) {
        }

    }

    public String getBapplydate() {
        return bapplydate;
    }

    public void setBapplydate(String bapplydate) {
        this.bapplydate = bapplydate;
    }

    public String getEapplydate() {
        return eapplydate;
    }

    public void setEapplydate(String eapplydate) {
        this.eapplydate = eapplydate;
    }

    public String getBbanjiedate() {
        return bbanjiedate;
    }

    public void setBbanjiedate(String bbanjiedate) {
        this.bbanjiedate = bbanjiedate;
    }

    public String getEbanjiedate() {
        return ebanjiedate;
    }

    public void setEbanjiedate(String ebanjiedate) {
        this.ebanjiedate = ebanjiedate;
    }

    public List<SelectItem> getstatusModel() {
        if (statusModel == null) {
            statusModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "办件状态", null, false));
        }
        return this.statusModel;
    }

    public String getStatus() {
        return status;
    }

    public String getOuname() {
        return ouname;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<SelectItem> getOunameModel() {
        if (ounameModel == null) {
            ounameModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "区县市", null, false));
        }
        return this.ounameModel;
    }

    public void setOuname(String ouname) {
        this.ouname = ouname;
    }

}
