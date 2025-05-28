package com.epoint.mq.spgl.action;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;
import org.xm.similarity.util.StringUtil;

import com.epoint.basic.auditresource.auditrsitembaseinfo.domain.AuditRsItemBaseinfo;
import com.epoint.basic.auditresource.auditrsitembaseinfo.inter.IAuditRsItemBaseinfo;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.export.ExportModel;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.frame.service.metadata.mis.util.ListGenerator;
import com.epoint.mq.spgl.api.IJnOptionInService;
import com.epoint.mq.spgl.api.entity.JnOptionIn;


/**
 * 成品油零售经营企业库list页面对应的后台
 * 
 * @author 18039505500
 * @version [版本号, 2020-06-23 19:57:28]
 */
@RestController("jnoptioninlistaction")
@Scope("request")
public class JnOptionInListAction  extends BaseController
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Autowired
    private IJnOptionInService service;
    
    /**
     * 成品油零售经营企业库实体对象
     */
    private JnOptionIn dataBean;
  
    /**
     * 表格控件model
     */
    private DataGridModel<JnOptionIn> model;
  	
    /**
     * 导出模型
     */
    private ExportModel exportModel;
    
    /**
     * 项目代码
     */
    private String xmdm;
    
    /**
     * 项目库API
     */
    @Autowired
    private IAuditRsItemBaseinfo iAuditRsItemBaseinfo;

    public void pageLoad()
    {
    	xmdm = getRequestParameter("xmdm");
    }

	

    /**
     * 删除选定
     * 
     */
    public void deleteSelect()
    {
        List<String> select = getDataGridData().getSelectKeys();
        for (String sel : select) {
             service.deleteByGuid(sel);
        }
        addCallbackParam("msg", "成功删除！");
    }
    
    public DataGridModel<JnOptionIn> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<JnOptionIn>()
            {

                @Override
                public List<JnOptionIn> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                	List<Object> conditionList = new ArrayList<Object>();
                    String conditionSql = ListGenerator.getSearchSql(getRequestContext().getComponents(), conditionList);
                    if (StringUtil.isNotBlank(xmdm)) {
                    	conditionSql += " and xmdm = '" + xmdm + "'";
                    }else {
                    	conditionSql += " and 1 = 2 ";
                    }
                    List<JnOptionIn> list = service.findList(
                            ListGenerator.generateSql("spgl_xmqqyjxxb", conditionSql, sortField, sortOrder), first, pageSize,
                            conditionList.toArray());
                   int count = service.countJnOptionIn(ListGenerator.generateSql("spgl_xmqqyjxxb", conditionSql), conditionList.toArray());
                    this.setRowCount(count);
                    return list;
                }

            };
        }
        return model;
    }
    
    /**
     * 修改工改表数据推送
     * 
     */
    public void turnspgl()
    {
    	if (StringUtil.isNotBlank(xmdm)) {
    		List<JnOptionIn> list = service.getJnOptionInByXmdm(xmdm);
        	if (list != null && list.size() > 0) {
        		for (JnOptionIn option : list) {
        			option.setSync(0);
        			option.setOperatedate(new Date());
        			service.update(option);
        		}
        		SqlConditionUtil sql = new SqlConditionUtil();
                sql.eq("itemcode", xmdm);
                List<AuditRsItemBaseinfo> items = iAuditRsItemBaseinfo.selectAuditRsItemBaseinfoByCondition(sql.getMap()).getResult();
                if (items != null && items.size() > 0) {
                	for (AuditRsItemBaseinfo item : items) {
                		item.set("is_qianqi", "1");
                		iAuditRsItemBaseinfo.updateAuditRsItemBaseinfo(item);
                	}
                	addCallbackParam("msg", "前期意见信息录入成功！");
                }else {
                	addCallbackParam("msg", "项目信息不存在！");
                }
        	}else {
        		addCallbackParam("msg", "请进行前期意见的录入！");
        		return;
        	}
    	}else {
    		addCallbackParam("msg", "该项目不存在项目代码！");
    	}
    	
    }
    

    
    public JnOptionIn getDataBean()
    {
    	if(dataBean == null){
    		dataBean = new JnOptionIn();
    	}
        return dataBean;
    }

    public void setDataBean(JnOptionIn dataBean)
    {
        this.dataBean = dataBean;
    }
    
    public ExportModel getExportModel() {
        if (exportModel == null) {
            exportModel = new ExportModel("address,certdate,certificate,certno,code,legal,name,validitytime", "地址,发证日期,发证机关,证书编号,统一社会信用代码,法定代表人(企业负责人),企业名称,有效期");
        }
        return exportModel;
    }



	public String getXmdm() {
		return xmdm;
	}



	public void setXmdm(String xmdm) {
		this.xmdm = xmdm;
	}
    

    

}
