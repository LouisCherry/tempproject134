package com.epoint.xmz.jzqyzz;

import java.util.List;

import org.json.JSONArray;
import org.json.XML;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.controller.BaseController;
import com.epoint.common.util.JsonUtils;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.jnznsb.shebao.selfservicemachine.DwPspProxy;
import com.epoint.xmz.cxbus.api.ICxBusService;

/**
 * 惠企政策库list页面对应的后台
 * 
 * @author 86180
 * @version [版本号, 2019-10-08 23:39:46]
 */
@RestController("buildcompareaction")
@Scope("request")
public class BuildCompareAction extends BaseController
{
	public static final String licenseKey = "3f8a85b7-551d-4922-9482-eaf7120f0885";
	 
    @Autowired
    private ICxBusService service;

    /**
     * 惠企政策库实体对象
     */
    private Record dataBean;

    /**
     * 表格控件model
     */
    private DataGridModel<Record> model;


    public void pageLoad() {
        
    }

    
    @SuppressWarnings("unchecked")
    public DataGridModel<Record> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<Record>()
            {
                @Override
                public List<Record> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    List<Record> list = null;
                    try{
                        list = service.getApplyerListByPorjectGuid(getRequestParameter("projectguid"));
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    if(list==null || list.size()==0){
                        this.setRowCount(0);
                    }else{
                        int count = list.size();
                        this.setRowCount(count);
                    }

                    return list;
                }

            };
        }
        return model;
    }

    public Record getDataBean() {
        if (dataBean == null) {
            dataBean = new Record();
        }
        return dataBean;
    }

    public void setDataBean(Record dataBean) {
        this.dataBean = dataBean;
    }

    public void selectApplyer (String rowguid) {
        String sfzhm = "";
        String xm = "";
        String jym = "";
        String dybm = "";
        String qsny = "";
        String zzny = "";
        Record rec = null;
        rec = service.getJzqyzzDetailByRowguid("formtable20220627120002", rowguid);
        
        if (rec == null) {
        	rec = service.getJzqyzzDetailByRowguid("formtable20220629170401", rowguid);
        }
        
        if (rec == null) {
        	rec = service.getJzqyzzDetailByRowguid("formtable20220629170950", rowguid);
        }
        
        if (rec == null) {
        	addCallbackParam("msg", "未查询到该申请人信息！");
        	return;
        }
        String sbryxx = "";
        String jsryxx = "";
        String zzzylbdb = "";
        String isqualiy = "";
        
      /*  DwPspProxy.getInstance().setDwPspServerUrl("http://10.156.80.1:8080/dwpspserver");

        String serviceName = "SiServiceAged";
        String operationName = "printYlcbzm";
        String xmlPara = "<?xml version=\"1.0\" encoding=\"GBK\"?><p>" + "<s Sfzhm=\"" + sfzhm + "\"/><s xm=\"" + xm
                + "\"/><s xzbz=\"101,102,109,201,401\"/><s qsny=\"" + qsny + "\"/><s zzny=\"" + zzny
                + "\"/><s jym=\"" + jym + "\"/><s dybm=\"" + dybm + "\"/><s rsxtid=\"3758\"/></p>";

        String xmlresult = DwPspProxy.getInstance().invokeService(licenseKey, serviceName, operationName, xmlPara);

        JSONObject dataJson = new JSONObject();
        org.json.JSONObject xmlJSONObj = XML.toJSONObject(xmlresult);
        org.json.JSONObject p = xmlJSONObj.getJSONObject("p");
        JSONArray s = p.getJSONArray("s");

        //先判断当前接口是否有问题
        int errflagindex = getErrFlagIndex(s, "errflag");
        int textindex = getErrFlagIndex(s, "text");
        int errtextindex = getErrFlagIndex(s, "errtext");

        if (Integer.parseInt(s.getJSONObject(errflagindex).get("errflag").toString()) == 0) {
            //无问题
            dataJson.put("errflag", 0);
            addCallbackParam("msg", "0");
            sbryxx = s.getJSONObject(textindex).getString("text");
        }
        else {
        	sbryxx = "无社保信息";
        }*/
        
        //专业名称
        String zczy = rec.getStr("zczy");
        //注册资质类别
        String sbzzlb = rec.getStr("sbzzlb");
        
        if (StringUtil.isNotBlank(sbzzlb)) {
        	List<Record> jnjzqys = service.getJnJzqyzzzyByZzlb(sbzzlb);
        	boolean haszy = false;
        	for (Record record : jnjzqys) {
        		String zylb = record.getStr("zylb");
        		if (zylb.equals(zczy)) {
        			haszy = true;
        			continue;
        		}
        	}
        	if (haszy) {
        		zzzylbdb = "存在专业信息！";
        	}else {
        		zzzylbdb = "未查询到该专业信息！";
        	}
        }
        
        service.updateEformByRowguid("formtable20220627120002", rec.getStr("rowguid"), sbryxx, jsryxx, zzzylbdb, isqualiy);
        service.updateEformByRowguid("formtable20220629170401", rec.getStr("rowguid"), sbryxx, jsryxx, zzzylbdb, isqualiy);
        service.updateEformByRowguid("formtable20220629170950", rec.getStr("rowguid"), sbryxx, jsryxx, zzzylbdb, isqualiy);
        
        
        
    	
    	 
    }
    
    
  //根据属性名称找Index
    int getErrFlagIndex(JSONArray data, String key) {

        for (int i = 0; i < data.length(); i++) {

            if (data.getJSONObject(i).keys().next().equals(key)) {
                return i;
            }
        }
        return 0;
    }
    
}
