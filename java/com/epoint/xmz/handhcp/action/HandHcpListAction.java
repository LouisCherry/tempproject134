package com.epoint.xmz.handhcp.action;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.export.ExportModel;
import com.epoint.common.util.HttpRequestUtils;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.metadata.mis.util.ListGenerator;
import com.epoint.xmz.handhcp.api.IHandHcpService;
import com.epoint.xmz.handhcp.api.entity.HandHcp;


/**
 * 手动推送好差评表list页面对应的后台
 * 
 * @author 1
 * @version [版本号, 2022-03-29 16:57:08]
 */
@RestController("handhcplistaction")
@Scope("request")
public class HandHcpListAction  extends BaseController
{
	@Autowired
    private IHandHcpService service;
    
    /**
     * 手动推送好差评表实体对象
     */
    private HandHcp dataBean;
  
    /**
     * 表格控件model
     */
    private DataGridModel<HandHcp> model;
  	
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
    
    public DataGridModel<HandHcp> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<HandHcp>()
            {

                @Override
                public List<HandHcp> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                	List<Object> conditionList = new ArrayList<Object>();
                    String conditionSql = ListGenerator.getSearchSql(getRequestContext().getComponents(), conditionList);
                    List<HandHcp> list = service.findList(
                            ListGenerator.generateSql("hand_hcp", conditionSql, sortField, sortOrder), first, pageSize,
                            conditionList.toArray());
                   int count = service.countHandHcp(ListGenerator.generateSql("hand_hcp", conditionSql), conditionList.toArray());
                    this.setRowCount(count);
                    return list;
                }

            };
        }
        return model;
    }
    
    public void hcpevalue(String rowguid){
    	HandHcp hcp = service.find(rowguid);
    	if (hcp != null) {
    		Map<String,String> result = turnEvaluate(hcp.getAreacode(),hcp.getProjectno(),hcp.getName(),hcp.getServicenumber());
    		 if ("1".equals(result.get("status"))) {
    			 hcp.setSbsign("1");
    			 hcp.setRecord(result.get("text"));
    			 service.update(hcp);
    			 addCallbackParam("msg", "重推成功！");
    		 }else {
    			 hcp.setSbsign("0");
    			 hcp.setRecord(result.get("text"));
    			 service.update(hcp);
    			 addCallbackParam("msg", "重推失败！");
    		 }
    	}
    }

	private Map<String,String> turnEvaluate(String areacode, String projectno, String userName, String serviceNumber) {
		String url = ConfigUtil.getConfigValue("zwdtparam", "addEvaluateRecord");
		Map<String,String> result = new HashMap<String,String>();
		JSONObject auditobject = new JSONObject();
		auditobject.put("projectno", projectno);
		auditobject.put("pf", "1");
		auditobject.put("satisfaction", "5");
		Date date = new Date();
        Calendar c = new GregorianCalendar();
        c.setTime(date);//设置参数时间
        c.add(Calendar.MINUTE, -2);//把日期往后增加SECOND 秒.整数往后推,负数往前移动
        date = c.getTime(); //这个时间就是日期往后推一天的结果
        String newassessTime = EpointDateUtil.convertDate2String(date, "yyyy-MM-dd HH:mm:ss");
		auditobject.put("assessTime", newassessTime);
		auditobject.put("assessNumber", serviceNumber);
		auditobject.put("serviceNumber", serviceNumber);
		auditobject.put("evalDetail", "510,517");
		auditobject.put("areacode", areacode);
		auditobject.put("writingEvaluation", "");

		JSONObject dataJson = new JSONObject();
		dataJson.put("params", auditobject);
		String str = "";
		try {
			str = HttpRequestUtils.sendPost(url, dataJson.toJSONString(),
					new String[] { "application/json;charset=utf-8" });
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (StringUtil.isNotBlank(str)) {
			JSONObject jsonobject = JSONObject.parseObject(str);
			JSONObject jsonstatus = (JSONObject) jsonobject.get("status");
			if ("200".equals(jsonstatus.get("code").toString())) {
				JSONObject jsoncustom = (JSONObject) jsonobject.get("custom");
				if ("1".equals(jsoncustom.get("code").toString())) {
					result.put("status", "1");
					result.put("text", jsoncustom.get("text").toString());
					log.info("=====办件数据推送成功！");
				} else
					result.put("status", "0");
					result.put("text", jsoncustom.get("text").toString());
					log.info("=====办件数据推送失败！==传参：" + dataJson.toJSONString() + ";===原因："
							+ jsoncustom.get("text").toString());
			} else {
				log.info("=====请求失败！");
			}
		} else {
			log.info("=====网厅连接失败");
		}
		return result;
	}
    
    public HandHcp getDataBean()
    {
    	if(dataBean == null){
    		dataBean = new HandHcp();
    	}
        return dataBean;
    }

    public void setDataBean(HandHcp dataBean)
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
