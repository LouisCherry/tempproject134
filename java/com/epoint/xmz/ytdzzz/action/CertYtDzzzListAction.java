package com.epoint.xmz.ytdzzz.action;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.controller.BaseController;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import com.epoint.frame.service.metadata.mis.util.ListGenerator;
import com.epoint.xmz.ytdzzz.api.ICertYtDzzzService;


/**
 * 省际普通货物水路运输经营许可本地库list页面对应的后台
 * 
 * @author 1
 * @version [版本号, 2022-04-26 14:57:52]
 */
@RestController("certytdzzzlistaction")
@Scope("request")
public class CertYtDzzzListAction  extends BaseController
{
	@Autowired
    private ICertYtDzzzService service;
    
    /**
     * 省际普通货物水路运输经营许可本地库实体对象
     */
    private FrameAttachInfo dataBean;
  
    /**
     * 表格控件model
     */
    private DataGridModel<FrameAttachInfo> model;
  	
    
    

    public void pageLoad()
    {
    }

	
    public DataGridModel<FrameAttachInfo> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<FrameAttachInfo>()
            {

                @Override
                public List<FrameAttachInfo> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                	List<Object> conditionList = new ArrayList<Object>();
                    String conditionSql = ListGenerator.getSearchSql(getRequestContext().getComponents(), conditionList);
                    conditionSql += " and name = 'ytdzzz'";
                    
                    List<FrameAttachInfo> list = service.findList(
                            ListGenerator.generateSql("zj_num", conditionSql, sortField, sortOrder), first, pageSize,
                            conditionList.toArray());
                    int count = service.countCertHwslysjyxk(ListGenerator.generateSql("zj_num", conditionSql), conditionList.toArray());
                    this.setRowCount(count);
                    return list;
                }

            };
        }
        return model;
    }

    
    public FrameAttachInfo getDataBean()
    {
    	if(dataBean == null){
    		dataBean = new FrameAttachInfo();
    	}
        return dataBean;
    }

    public void setDataBean(FrameAttachInfo dataBean)
    {
        this.dataBean = dataBean;
    }
    


}
