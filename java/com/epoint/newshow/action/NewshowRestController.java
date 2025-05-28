package com.epoint.newshow.action;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.epoint.common.util.JsonUtils;
import com.epoint.core.grammar.Record;
import com.epoint.newshow.api.NewshowService;

@RestController
@RequestMapping("/newshow")
public class NewshowRestController {
    @Autowired
	private NewshowService newshowService;
    
    
    @RequestMapping(value = "/getHandleStatus", method = RequestMethod.POST)
    public String getHandleStatus(@RequestBody String params, HttpServletRequest request) {
    	try {
    		  List<Integer> record=new ArrayList<Integer>();
    		  JSONObject dataJson = new JSONObject();
    		  int acceptnum=newshowService.getacceptNum();
    		  int banjienum=newshowService.getbanjieNum();
    		  int allacceptnum=newshowService.getallAcceptNum();
    		  int allbanjienum=newshowService.getallBanjieNum();
    		  record.add(acceptnum);
    		  record.add(banjienum);
    		  record.add(allacceptnum);
    		  record.add(allbanjienum);
    		  dataJson.put("handleStatus",record);
    		
    		 return JsonUtils.zwdtRestReturn("1", "", dataJson.toString());
    		
		} catch (Exception e) {
			 return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
		}

    }
    
    
    @RequestMapping(value = "/getSatisfy", method = RequestMethod.POST)
    public String getSatisfy(@RequestBody String params, HttpServletRequest request) {
    	try {
    		  List<String> record=new ArrayList<String>();
    		  JSONObject dataJson = new JSONObject();
    		  JSONObject dataJson1 = new JSONObject();

    		  Record evaluat=newshowService.getcitySatisfy();
    		  int manyi=evaluat.getInt("fcmanyi");
    		  int bumanyi=evaluat.getInt("bumanyi");
    		  int jbmanyi=evaluat.getInt("jbmanyi");
    		  int total=manyi+bumanyi+jbmanyi;
    		  int totalmanyi=manyi+jbmanyi;
    		  DecimalFormat df = new DecimalFormat( "0.0");
    		  float citySatisfy=(float)totalmanyi*100/total;
    		  float manyiSatisfy=(float)manyi*100/total;
    		  float jbmanyiSatisfy=(float)jbmanyi*100/total;
    		  float bumanyiSatisfy=(float)bumanyi*100/total;
    		  String manyidu=df.format(manyiSatisfy)+"%";
    		  String jbmanyidu=df.format(jbmanyiSatisfy)+"%";
    		  String bumanyidu=df.format(bumanyiSatisfy)+"%";
    		  String manyicitySatisfy=df.format(citySatisfy);
    		  record.add(manyidu);
    		  record.add(jbmanyidu);
    		  record.add(bumanyidu);
    		  dataJson1.put("detail",record);
    		  dataJson1.put("city", manyicitySatisfy);
    		  dataJson.put("satisfy", dataJson1);
    		 return JsonUtils.zwdtRestReturn("1", "", dataJson.toString());
    		
		} catch (Exception e) {
			 return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
		}

    }
    @RequestMapping(value = "/getEventType", method = RequestMethod.POST)
    public String getEventType(@RequestBody String params, HttpServletRequest request) {
    	try {
    
    		  JSONObject dataJson = new JSONObject();

    		  List<Record> list=newshowService.geteventType();
    		  dataJson.put("eventType", list);
    		 return JsonUtils.zwdtRestReturn("1", "", dataJson.toString());
    		
		} catch (Exception e) {
			 return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
		}

    }
    @RequestMapping(value = "/getMapData", method = RequestMethod.POST)
    public String getMapData(@RequestBody String params, HttpServletRequest request) {
    	try {
    		
		      JSONObject dataJson = new JSONObject();

  		      JSONObject dataJson1 = new JSONObject();
    		  List<Record> list=newshowService.getmapData(); 	
    		  dataJson1.put("scatter",list);
    		  List<Record> list1=newshowService.getmapbanjian();
    		  dataJson1.put("map",list1);
    		  dataJson.put("mapData", dataJson1);
    		 return JsonUtils.zwdtRestReturn("1", "", dataJson.toString());
    		
		} catch (Exception e) {
			 return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
		}

    }
    @RequestMapping(value = "/getHandleEvent", method = RequestMethod.POST)
    public String getHandleEvent(@RequestBody String params, HttpServletRequest request) {
    	try {
  		      JSONObject dataJson = new JSONObject();
    		  Record list=newshowService.gethandleEvent(); 	
    		  dataJson.put("handleEvent",list);
    		 return JsonUtils.zwdtRestReturn("1", "", dataJson.toString());
    		
		} catch (Exception e) {
			 return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
		}

    }
    
    
    @RequestMapping(value = "/getCityData", method = RequestMethod.POST)
    public String getCityData(@RequestBody String params, HttpServletRequest request) {
    	try {
            JSONObject json = JSON.parseObject(params);
             String param=json.getString("params");
             JSONObject json1 = JSON.parseObject(param);
             String id=json1.getString("id");
             JSONObject dataJson = new JSONObject();
             Record list=null;
    		if(("0").equals(id)){
      		   list=newshowService.getcityData(); 	
    		 }else{
    			list=newshowService.getcityDatabyid();  
    		 }
    		  dataJson.put("cityData",list);
    		 return JsonUtils.zwdtRestReturn("1", "", dataJson.toString());
    		
		} catch (Exception e) {
			 return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
		}

    }
    
    
    
    @RequestMapping(value = "/getServiceObj", method = RequestMethod.POST)
    public String getServiceObj(@RequestBody String params, HttpServletRequest request) {
    	try {
    		List<String> source=new ArrayList<String>();
             JSONObject dataJson = new JSONObject();
             JSONObject sexObject = new JSONObject();
 			 JSONArray ageArray = new JSONArray();
             JSONObject dataJson1 = new JSONObject();
              sexObject.put("male", "58.3");
              sexObject.put("female", "41.7");
              dataJson1.put("sex", sexObject);
              for(int i=0;i<6;i++){
                  JSONObject ageObject = new JSONObject();
                  if(i==0){
                      ageObject.put("name", "15-30岁");
                      ageObject.put("value", "34");
                  }else if(i==1){
                	  ageObject.put("name", "31-40岁");
                      ageObject.put("value", "26"); 
                  }else if(i==2){
                	  ageObject.put("name", "41-50岁");
                      ageObject.put("value", "20");  
                  }else if(i==3){
                	  ageObject.put("name", "51-60岁");
                      ageObject.put("value", "16");  
                  }else if(i==4){
                	  ageObject.put("name", "61-70岁");
                      ageObject.put("value", "3");  
                  }else if(i==5){
                	  ageObject.put("name", "70岁以上");
                      ageObject.put("value", "1");  
                  }
                  ageArray.add(ageObject);
              }
                  Record record=newshowService.getsource();
            	  int wx=record.getInt("wx");
            	  int ck=record.getInt("chuangkou");
            	  int ww=record.getInt("waiwang");
            	  int zz=record.getInt("zz");
            	  int total=wx+ck+ww+zz;
            	  DecimalFormat df = new DecimalFormat( "0.0");
        		  float wxnum=(float)wx*100/total;
        		  float cknum=(float)ck*100/total;
        		  float wwnum=(float)ww*100/total;
        		  float zznum=(float)zz*100/total;
        		  String wxnumstr=df.format(wxnum)+"%";
        		  String cknumstr=df.format(cknum)+"%";
        		  String wwnumstr=df.format(wwnum)+"%";
        		  String zznumstr=df.format(zznum)+"%";
        		  source.add(wxnumstr);
        		  source.add(cknumstr);
        		  source.add(wwnumstr);
        		  source.add(zznumstr);
              dataJson1.put("age", ageArray);
              dataJson1.put("source", source);
    		  dataJson.put("serviceObj",dataJson1);
    		 return JsonUtils.zwdtRestReturn("1", "", dataJson.toString());
    		
		} catch (Exception e) {
			 return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
		}

    }
    
    
    @RequestMapping(value = "/getEventTop5", method = RequestMethod.POST)
    public String getEventTop5(@RequestBody String params, HttpServletRequest request) {
    	try {
          
             JSONObject dataJson = new JSONObject();
             List<Record> list=newshowService.geteventTop5();
    		  dataJson.put("eventTop5",list);
    		 return JsonUtils.zwdtRestReturn("1", "", dataJson.toString());
    		
		} catch (Exception e) {
			 return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
		}

    }
    
    @RequestMapping(value = "/getTrend", method = RequestMethod.POST)
    public String getTrend(@RequestBody String params, HttpServletRequest request) {
    	try {
          
             JSONObject dataJson = new JSONObject();
             List<Record> list=newshowService.gettrend();
    		  dataJson.put("trend",list);
    		 return JsonUtils.zwdtRestReturn("1", "", dataJson.toString());
    		
		} catch (Exception e) {
			 return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
		}

    }
    
}
