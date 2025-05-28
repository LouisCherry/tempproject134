package com.epoint.expert.expertcompany.action;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.expert.expertcompany.api.IExpertCompanyService;
import com.epoint.expert.expertcompany.api.entity.ExpertCompany;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;

/**
 * 从业单位表详情页面对应的后台
 * 
 * @author cqsong
 * @version [版本号, 2019-08-21 16:09:09]
 */
@RightRelation(ExpertCompanyListAction.class)
@RestController("expertcompanydetailaction")
@Scope("request")
public class ExpertCompanyDetailAction extends BaseController
{
    /**
     * 
     */
    private static final long serialVersionUID = 5578238592828601227L;

    private static final String CITY_AREA = "市辖区";
    private static final String COUNTY = "县";

    @Autowired
    private IExpertCompanyService service;

    @Autowired
    private ICodeItemsService codeItemService;

    @Autowired
    private IAttachService attachService;

    /**
     * 从业单位表实体对象
     */
    private ExpertCompany dataBean = null;

    public void pageLoad() {
        String guid = getRequestParameter("guid");
        dataBean = service.find(guid);
        if (dataBean == null) {
            dataBean = new ExpertCompany();
        }
        else {
            // 省
            String province = codeItemService.getItemTextByCodeName("行政区划国标", dataBean.getProvince());
            // 市 当为县或者市辖区的时候不需要显示市
            String city = codeItemService.getItemTextByCodeName("行政区划国标", dataBean.getCity());
            city = COUNTY.equals(city) ? "" : city;
            city = CITY_AREA.equals(city) ? "" : city;
            // 区
            String country = codeItemService.getItemTextByCodeName("行政区划国标", dataBean.getCountry());
            dataBean.setProvince(province + city + country);

            if (StringUtil.isNotBlank(dataBean.getCliengguid())) {
                this.addCallbackParam("client_guidUrl", getTempUrl(dataBean.getCliengguid()));
            }
            else {
                this.addCallbackParam("client_guidUrl", "无附件！");
            }
        }
    }

    public ExpertCompany getDataBean() {
        return dataBean;
    }

    /**
     * 附件下载地址
     * 
     * @param cliengguid
     *            业务guid
     *
     */
    public String getTempUrl(String clientGuid) {
        String wsmbName = "";
        if (StringUtil.isNotBlank(clientGuid)) {
            List<FrameAttachInfo> frameAttachInfos = attachService.getAttachInfoListByGuid(clientGuid);
            if (frameAttachInfos != null && !frameAttachInfos.isEmpty()
                    && StringUtil.isNotBlank(frameAttachInfos.get(0).getAttachFileName())) {
                FrameAttachInfo frameAttachInfo = frameAttachInfos.get(0);
                String strURL = "onclick=\"goToAttach('" + frameAttachInfo.getAttachGuid() + "')\"";
                wsmbName += "<a style=\"color:blue;text-decoration:underline\" href=\"javascript:void(0)\" " + strURL
                        + ">" + frameAttachInfo.getAttachFileName() + "</a>&nbsp;&nbsp;";
            }
            else {
                wsmbName = "无附件！";
            }
        }
        else {
            wsmbName = "无附件！";
        }
        return wsmbName;
    }
}
