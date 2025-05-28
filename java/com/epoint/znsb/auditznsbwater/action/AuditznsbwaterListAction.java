package com.epoint.znsb.auditznsbwater.action;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.epoint.common.znsb.util.QueueCommonUtil;
import com.epoint.core.utils.classpath.ClassPathUtil;
import com.epoint.znsb.auditznsbwaterjfinfo.api.IAuditZnsbWaterjfinfoService;
import com.epoint.znsb.auditznsbwaterjfinfo.api.entity.AuditZnsbWaterjfinfo;
import com.epoint.znsb.jnzwfw.water.WaterFtpUtil;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.znsb.auditznsbwater.api.entity.Auditznsbwater;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.export.ExportModel;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.frame.service.metadata.mis.util.ListGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import com.epoint.znsb.auditznsbwater.api.IAuditznsbwaterService;


/**
 * 水务对账信息list页面对应的后台
 * 
 * @author HYF
 * @version [版本号, 2021-11-11 16:08:59]
 */
@RestController("auditznsbwaterlistaction")
@Scope("request")
public class AuditznsbwaterListAction  extends BaseController
{
	@Autowired
    private IAuditznsbwaterService service;
    @Autowired
    private IAuditZnsbWaterjfinfoService jfService;
    
    /**
     * 水务对账信息实体对象
     */
    private Auditznsbwater dataBean;
  
    /**
     * 表格控件model
     */
    private DataGridModel<Auditznsbwater> model;
  	
    /**
     * 导出模型
     */
    private ExportModel exportModel;
    
    

    public void pageLoad()
    {
    }


    public void  createTxt(String id){
        Auditznsbwater water = service.find(id);

        String txtname = water.getWaterinfo();
        //这个是日期  获取水务交易信息

        List<AuditZnsbWaterjfinfo> jflsit = jfService.findListByTime(txtname);

        List<JSONObject> jfJsonList = new ArrayList<>();
        for (int i = 0; i < jflsit.size(); i++) {
            JSONObject jfJson = new JSONObject();
            AuditZnsbWaterjfinfo jf = jflsit.get(i);
            jfJson.put("flowon",jf.getWaterflowon());
            jfJson.put("number",jf.getWaternumber());
            jfJson.put("jftime",jf.getWatertime());
            jfJson.put("money",jf.getWaterpaymoney());
            jfJson.put("starttime",jf.getStarttime());
            jfJson.put("endtime",jf.getEndtime());
            jfJson.put("jfdw","");
            jfJsonList.add(jfJson);
        }

        JSONArray objects = JSONArray.parseArray(JSON.toJSONString(jfJsonList));

        String dir = ClassPathUtil.getDeployWarPath() + "jiningzwfw/water/";
        WaterFtpUtil.createTxtFile(objects,dir,txtname);

        String attachguid =  WaterFtpUtil.getAttachguid(id,txtname);
        //system.out.println( QueueCommonUtil.getUrlPath(request.getRequestURL().toString())
//                + "/rest/auditattach/readAttach?attachguid="+ attachguid);
        addCallbackParam("fileurl",QueueCommonUtil.getUrlPath(request.getRequestURL().toString())
                + "/rest/auditattach/readAttach?attachguid="+ attachguid);

        addCallbackParam("msg","下载成功！");
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
    
    public DataGridModel<Auditznsbwater> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<Auditznsbwater>()
            {

                @Override
                public List<Auditznsbwater> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                	List<Object> conditionList = new ArrayList<Object>();
                    String conditionSql = ListGenerator.getSearchSql(getRequestContext().getComponents(), conditionList);
                    List<Auditznsbwater> list = service.findList(
                            ListGenerator.generateSql("AUDIT_ZNSB_WATER", conditionSql, sortField, sortOrder), first, pageSize,
                            conditionList.toArray());
                   int count = service.countAuditznsbwater(ListGenerator.generateSql("AUDIT_ZNSB_WATER", conditionSql), conditionList.toArray());
                    this.setRowCount(count);
                    return list;
                }

            };
        }
        return model;
    }





    
    public Auditznsbwater getDataBean()
    {
    	if(dataBean == null){
    		dataBean = new Auditznsbwater();
    	}
        return dataBean;
    }

    public void setDataBean(Auditznsbwater dataBean)
    {
        this.dataBean = dataBean;
    }
    
    public ExportModel getExportModel() {
        if (exportModel == null) {
            exportModel = new ExportModel("", "");
        }
        return exportModel;
    }
    


}
