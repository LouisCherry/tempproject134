package com.epoint.xmz.xmzjsgczljcjgzzsp.action;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.xmz.xmzjsgczljcjgzzsp.api.entity.XmzJsgczljcjgzzsp;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.export.ExportModel;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.frame.service.metadata.mis.util.ListGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import com.epoint.xmz.xmzjsgczljcjgzzsp.api.IXmzJsgczljcjgzzspService;


/**
 * 建设工程质量检测机构资质审批表list页面对应的后台
 * 
 * @author 86177
 * @version [版本号, 2021-05-08 17:01:26]
 */
@RestController("xmzjsgczljcjgzzsplistaction")
@Scope("request")
public class XmzJsgczljcjgzzspListAction  extends BaseController
{
	@Autowired
    private IXmzJsgczljcjgzzspService service;
    
    /**
     * 建设工程质量检测机构资质审批表实体对象
     */
    private XmzJsgczljcjgzzsp dataBean;
  
    /**
     * 表格控件model
     */
    private DataGridModel<XmzJsgczljcjgzzsp> model;
  	
    /**
     * 导出模型
     */
    private ExportModel exportModel;
    
    

    public void pageLoad()
    {
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
    
    public DataGridModel<XmzJsgczljcjgzzsp> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<XmzJsgczljcjgzzsp>()
            {

                @Override
                public List<XmzJsgczljcjgzzsp> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                	List<Object> conditionList = new ArrayList<Object>();
                    String conditionSql = ListGenerator.getSearchSql(getRequestContext().getComponents(), conditionList);
                    List<XmzJsgczljcjgzzsp> list = service.findList(
                            ListGenerator.generateSql("xmz_jsgczljcjgzzsp", conditionSql, sortField, sortOrder), first, pageSize,
                            conditionList.toArray());
                   int count = service.countXmzJsgczljcjgzzsp(ListGenerator.generateSql("xmz_jsgczljcjgzzsp", conditionSql), conditionList.toArray());
                    this.setRowCount(count);
                    return list;
                }

            };
        }
        return model;
    }

    
    public XmzJsgczljcjgzzsp getDataBean()
    {
    	if(dataBean == null){
    		dataBean = new XmzJsgczljcjgzzsp();
    	}
        return dataBean;
    }

    public void setDataBean(XmzJsgczljcjgzzsp dataBean)
    {
        this.dataBean = dataBean;
    }
    
    public ExportModel getExportModel() {
        if (exportModel == null) {
            exportModel = new ExportModel("ccqdzzsj,chuanzhen,clsj,dianhua,dzyx,fazhengjiguan,fddbr,frzc,frzw,fwjzmj,fzjg,fzjgjl,fzrq,gjzcrs,gszcszds,gzmj,jcfw,jgmc,jlrzzssh,jsfzr,jsfzrzc,jsfzrzw,jsxz,jszw,lxdh,qymc,qytyshxydm,slsj,youbian,yqsbgdzcyz,yqsbzs,yxqjs,yxqks,zbryzs,zcjs,zczj,zjzcrs,zsbh,zyjsry", "初次取得资质时间,传真,成立时间,电话,电子邮箱,发证机关,法定代表人,法人职称,法人职务,房屋建筑面积（m2）,发证机构,发证机关（计量）,发证日期,高级职称人数,工商注册所在地市,工作面积（m2）,检测范围,机构名称,计量认证证书号,技术负责人,技术负责人职称,技术负责人职务,技术性质,技术职务,联系电话,企业名称,企业统一社会信用代码,设立时间,邮编,仪器设备固定资产原值（万元）,仪器设备总台（套）数,有效期结束,有效期开始,在编人员总数,职称（技术）,注册资金,中级职称人数,证书编号,专业技术人员");
        }
        return exportModel;
    }
    


}
