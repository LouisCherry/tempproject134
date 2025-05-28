package com.epoint.expert.expertinfo.action;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.expert.expertinfo.api.entity.ExpertInfo;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.frame.service.metadata.mis.util.ListGenerator;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.expert.expertcompany.api.IExpertCompanyService;
import com.epoint.expert.expertcompany.api.entity.ExpertCompany;
import com.epoint.expert.expertcompanyavoid.api.IExpertCompanyavoidService;
import com.epoint.expert.expertcompanyavoid.api.entity.ExpertCompanyavoid;
import com.epoint.expert.expertinfo.api.IExpertInfoService;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * 专家表详情页面对应的后台
 * 
 * @author cqsong
 * @version [版本号, 2019-08-21 16:36:51]
 */
@RightRelation(ExpertInfoListAction.class)
@RestController("expertinfodetailaction")
@Scope("request")
public class ExpertInfoDetailAction extends BaseController
{
    /**
    * 
    */
    private static final long serialVersionUID = 7581809339739364250L;

    @Autowired
    private IExpertInfoService service;
    
    @Autowired
    private ICodeItemsService codeItemService;
    
    @Autowired
    private IExpertCompanyavoidService expertCompanyavoidService;
    
    @Autowired
    private IAttachService attachService;
    
    @Autowired
    private IExpertCompanyService expertCompanyService;
    
    
    /**
     * 回避单位表格控件model
     */
    private DataGridModel<ExpertCompanyavoid> hbmodel;
    
    /**
     * 附件表格控件model
     */
    private DataGridModel<FrameAttachInfo> model;

    private static final String CITY_AREA = "市辖区";
    private static final String COUNTY = "县";

    /**
     * 专家表实体对象
     */
    private ExpertInfo dataBean = null;

    public void pageLoad() {
        String guid = getRequestParameter("guid");
        dataBean = service.find(guid);
        if (dataBean == null) {
            dataBean = new ExpertInfo();
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
            String sex = dataBean.getStr("sex");
            if ("0".equals(sex)) {
            	dataBean.set("sexs","男");
            }else {
            	dataBean.set("sexs","女");
            }
            
            String is_yingjis = dataBean.getStr("is_yingjis");
            if ("0".equals(is_yingjis)) {
            	dataBean.set("is_yingjis","否");
            }else {
            	dataBean.set("is_yingjis","是");
            }
            
            String is_zishens = dataBean.getStr("is_zishens");
            if ("0".equals(is_zishens)) {
            	dataBean.set("is_zishens","否");
            }else {
            	dataBean.set("is_zishens","是");
            }
            // 从业单位名称
            ExpertCompany expertCompany = expertCompanyService.find(dataBean.getComanyguid());
            if(expertCompany != null) {
                dataBean.setComanyguid(expertCompany.getCompanyname());
            }
        }
    }
    
    /**
     * 回避单位表格 
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public DataGridModel<ExpertCompanyavoid> getHbDataGridData() {
        // 获得表格对象
        if (hbmodel == null) {
            hbmodel = new DataGridModel<ExpertCompanyavoid>()
            {
                private static final long serialVersionUID = -7024735175738605919L;

                @Override
                public List<ExpertCompanyavoid> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    List<Object> conditionList = new ArrayList<Object>();
                    String conditionSql = ListGenerator.getSearchSql(getRequestContext().getComponents(),
                            conditionList);
                    String expertguid = getRequestParameter("guid");
                    List<ExpertCompanyavoid> list = expertCompanyavoidService.findListByexpertguid(
                            ListGenerator.generateSql("Expert_CompanyAvoid", conditionSql, sortField, sortOrder),
                            expertguid, first, pageSize, conditionList.toArray());
                    int count = expertCompanyavoidService.findListByexpertguid(
                            ListGenerator.generateSql("Expert_CompanyAvoid", conditionSql, sortField, sortOrder),
                            expertguid, conditionList.toArray()).size();
                    this.setRowCount(count);
                    return list;
                }
            };
        }
        return hbmodel;
    }
    
    /**
     * 附件列表 
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public DataGridModel<FrameAttachInfo> getUploadDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<FrameAttachInfo>()
            {
                /**
                 * 
                 */
                private static final long serialVersionUID = 5834872773407058979L;

                @Override
                public List<FrameAttachInfo> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    String expertguid = getRequestParameter("guid");
                    ExpertInfo expertInfo = service.find(expertguid);
                    String cliengguid = expertInfo.getCliengguid();
                    List<FrameAttachInfo> list = new ArrayList<>();
                    int count = 0;
                    if(StringUtil.isNotBlank(cliengguid)) {
                        if(attachService.getAttachInfoListByGuid(cliengguid) != null && !attachService.getAttachInfoListByGuid(cliengguid).isEmpty()) {
                            list = attachService.getAttachInfoListByGuid(cliengguid);
                            count = attachService.getAttachInfoListByGuid(cliengguid).size();
                        }
                    }
                    this.setRowCount(count);
                    return list;
                }

            };
        }
        return model;
    }

    public ExpertInfo getDataBean() {
        return dataBean;
    }
}
