package com.epoint.tongbufw;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONArray;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.auditresource.auditrsitembaseinfo.domain.AuditRsItemBaseinfo;
import com.epoint.basic.auditresource.auditrsitembaseinfo.impl.AuditRsItemBaseinfoImpl;
import com.epoint.cert.commonutils.HttpUtil;
import com.epoint.core.EpointFrameDsManager;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.log.LogUtil;
import com.epoint.core.utils.string.StringUtil;
@DisallowConcurrentExecution
public class EpointSysncXiangmuJob implements Job {
    transient Logger log = LogUtil.getLog(EpointSysncXiangmuJob.class);

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		try {
			EpointFrameDsManager.begin(null);
			doService();
			EpointFrameDsManager.commit();
		}catch (Exception e) {
		    e.printStackTrace();
		    EpointFrameDsManager.rollback();
		 } finally {
			EpointFrameDsManager.close();
		}

	}
	
	 private void doService() {
	        try {
	            log.info("开始同步项目数据========================");
	        	SimpleDateFormat sdf = new SimpleDateFormat("yyyy");      
	        	Date date = new Date();
	        	String years=sdf.format(date);
	            AuditRsItemBaseinfoImpl iteminfoservice=new AuditRsItemBaseinfoImpl();
	            EpointSynProjectService service = new EpointSynProjectService();
	   		    Map<String,Object> params = new HashMap<String,Object>();
	   		    Map<String,String> headers = new HashMap<String,String>();
	   		    headers.put("Content-Type", "application/x-www-form-urlencoded");
	   		    params.put("sessionGuid", "123");
	   		    params.put("params", "{\"userName\":\"jnfgwspry\",\"passWord\":\"123\"}");
	   		    String response = HttpUtil.doPost("http://59.206.96.143:25963/IDRIProjectNoDB/rest/interfaceForJiNingLangChaoTouZi/login", params, headers);
	            //system.out.println("response:"+response);
	            //获取登录JSESSIONID
                log.info("登录："+response + " =====>时间：" + EpointDateUtil
                        .convertDate2String(new Date(), EpointDateUtil.DATE_TIME_FORMAT));
	            if (StringUtil.isNotBlank(response)) {
	                JSONObject jsonObject = JSONObject.parseObject(response);
	                JSONObject obj = (JSONObject) jsonObject.get("result");
	                String JSESSIONID = obj.getString("JSESSIONID");
	                log.info("登录标识"+JSESSIONID);
		            int totalPages=2;
	                for(int n=1;n<totalPages+1;n++){
                    Map<String,Object> params1 = new HashMap<String,Object>();
                    Map<String,String> headers1 = new HashMap<String,String>();
                    headers1.put("Content-Type", "application/x-www-form-urlencoded");
                    params1.put("sessionGuid", "123");
                    params1.put("params", "{\"JSESSIONID\":\""+JSESSIONID+"\",\"projectCode\":\""+years+"\",\"pageNo\":\""+n+"\"}");
                    String response1 = HttpUtil.doPost("http://59.206.96.143:25963/IDRIProjectNoDB/rest/interfaceForJiNingLangChaoTouZi/getListInfoByPage", params1, headers1);
	                //system.out.println("response1:"+response1);
		            if (StringUtil.isNotBlank(response1)) {
		                log.info("项目信息："+response1 + " =====>时间：" + EpointDateUtil
		                        .convertDate2String(new Date(), EpointDateUtil.DATE_TIME_FORMAT));
		                
		                JSONObject jsonObjectxm = JSONObject.parseObject(response1);
		                JSONObject objxm = (JSONObject) jsonObjectxm.get("result");
		                String totalPage=objxm.getString("totalPages");
		                totalPages=Integer.parseInt(totalPage);
		                String listForProject=objxm.getString("listForProject");
		                JSONArray myJsonArray = new JSONArray(listForProject);
		                for(int i = 0; i < myJsonArray.length(); i++){
		                	 org.codehaus.jettison.json.JSONObject jsonxm = myJsonArray.getJSONObject(i);
		                     String PROJECT_NAME = jsonxm.getString("PROJECT_NAME"); //项目名称
		                     String SEQ_ID = jsonxm.getString("SEQ_ID");//
		                     String ID = jsonxm.getString("ID");//关联id
		                     AuditRsItemBaseinfo info=iteminfoservice.getAuditRsItemBaseinfoByRowguid(ID).getResult();
		                     if(info!=null){
		                    	 continue;
		                      }
		 	                log.info("项目名称: "+PROJECT_NAME+"id: "+SEQ_ID+"ID:"+ID);
		 	               Map<String,Object> params2 = new HashMap<String,Object>();
		                    Map<String,String> headers2 = new HashMap<String,String>();
		                    headers2.put("Content-Type", "application/x-www-form-urlencoded");
		                    params2.put("sessionGuid", "123");
		                    params2.put("params", "{\"JSESSIONID\":\""+JSESSIONID+"\",\"ID\":\""+ID+"\",\"SEQ_ID\":\""+SEQ_ID+"\"}");
		                    String response2 = HttpUtil.doPost("http://59.206.96.143:25963/IDRIProjectNoDB/rest/interfaceForJiNingLangChaoTouZi/getDetailInfo", params2, headers2);
		                    //system.out.println("response2:"+response2);
				            if (StringUtil.isNotBlank(response2)) {
			 	                 log.info("项目详细信息名称: "+response2);
			 	                JSONObject jsonObjectdetail = JSONObject.parseObject(response2);
				                JSONObject objdetail = (JSONObject) jsonObjectdetail.get("result");
				                if(objdetail==null){
				                	continue;
				                }
				                 AuditRsItemBaseinfo iteminfo=new AuditRsItemBaseinfo();
			                     String nkgsj = objdetail.getString("nkgsj"); //拟开工时间
			                     String njcsj = objdetail.getString("njcsj"); //拟竣工时间
			                     String ztz = objdetail.getString("ztz"); //总投资
			                     String xmfzrlxdh = objdetail.getString("xmfzrlxdh"); //项目联系人
			                     //String tzxmhyfl = objdetail.getString("tzxmhyfl"); //投资项目行业分类
			                     String xmdm = objdetail.getString("xmdm"); //项目代码
			                     Record record = service.getBaseinfoByXmdm(xmdm);
			                     if (record != null) {
			                    	 continue;
			                     }
			                     //String sshy = objdetail.getString("sshy"); //所属行业
			                     String jsddxq = objdetail.getString("jsddxq"); //建设地址详情
			                     String xmfzr = objdetail.getString("xmfzr"); //项目负责人
			                     String jsgmjnr = objdetail.getString("jsgmjnr"); //建设规模内容
			                     //String xmssxzqh = objdetail.getString("xmssxzqh"); //
			                     String jsdd = objdetail.getString("jsdd"); //建设地点
			                     String xmlx = objdetail.getString("xmlx");//项目类别
			                     //String sfwstz_jwtz = objdetail.getString("sfwstz_jwtz"); //是否外商投资/境外投资
			                     String jsxz = objdetail.getString("jsxz"); //建设性质
			                     String jsxzdm="";
			                     if(jsxz.equals("新建")){
			                    	 jsxzdm="1";
			                     }else if(jsxz.equals("扩建")){
			                    	 jsxzdm="2";

			                     }else if(jsxz.equals("改建")){
			                    	 jsxzdm="4";
			                     }else if(jsxz.equals("迁建")){
			                    	 jsxzdm="3";
			                     }
			                     String xmlxdm="";
			                     if(xmlx.equals("审批类项目")){
			                    	 xmlxdm="1";
			                     }else if(xmlx.equals("核准类项目")){
			                    	 xmlxdm="1";
			                     }else if(xmlx.equals("备案类项目")){
			                    	 xmlxdm="2";

			                     }
			                     String sbrq = objdetail.getString("sbrq"); //申报日期
			                     //String tzxmhyml = objdetail.getString("tzxmhyml"); //投资项目行业目录
			                     //String gbhy = objdetail.getString("gbhy"); //国标行业
			                     //String cyjgtzzdml = objdetail.getString("cyjgtzzdml");//产业结构调整指导目录
			                     String XMDWInfo=objdetail.getString("XMDWInfo");
					              JSONArray myJsonArray1 = new JSONArray(XMDWInfo);
					              for(int j = 0; j < myJsonArray1.length(); j++){
					                	 org.codehaus.jettison.json.JSONObject jsondetail = myJsonArray1.getJSONObject(j);
					                     String lerepCertno = jsondetail.getString("lerepCertno"); //证照号码
					                     String frzzlx = jsondetail.getString("frzzlx");//证照类别
					                     String frzzlxdm="";
					                     if(frzzlx.equals("企业营业执照")){
					                    	 frzzlxdm="14";
					                     }else if(frzzlx.equals("统一社会信用代码")){
					                    	 frzzlxdm="16";
					                     }
					                     //String contactName = jsondetail.getString("contactName");//证照姓名
					                     //String contactPhone = jsondetail.getString("contactPhone"); //证照电话
					                     String enterpriseName = jsondetail.getString("enterpriseName");//项目名称
					                     String xmdwxz = jsondetail.getString("xmdwxz");//项目单位性质
					                     
					                     String xmdwxzdm="";
					                     if(xmdwxz.contains("国有")){
					                    	 xmdwxzdm="国有企业法人";
					                     }else if(xmdwxz.contains("中外合资")){
					                    	 xmdwxzdm="中外合资企业";

					                     }else if(xmdwxz.contains("民营")){
					                    	 xmdwxzdm="民营企业法人";
					                     }else if(xmdwxz.contains("外商投资")){
					                    	 xmdwxzdm="外资企业法人";
					                     }else if(xmdwxz.contains("其他")){
					                    	 xmdwxzdm="其他";
					                     }
					                     
							             iteminfo.setLegalproperty(xmdwxzdm);
							             iteminfo.setItemlegalcreditcode(lerepCertno);
							             iteminfo.setItemlegalcertnum(lerepCertno);
							             iteminfo.setItemlegalcerttype(frzzlxdm);
							             iteminfo.setItemlegaldept(enterpriseName);
					              }
					              iteminfo.setItemname(PROJECT_NAME);
					              iteminfo.setRowguid(ID);
					              SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
					              SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd");
					              iteminfo.setItemstartdate(formatter.parse(nkgsj));
					              iteminfo.setItemfinishdate(formatter.parse(njcsj));
					              iteminfo.setTotalinvest(Double.valueOf(ztz));
					              iteminfo.setContractphone(xmfzrlxdh);
					              iteminfo.setItemcode(xmdm);
					              String [] str=xmdm.split("-");
					              String areacode = str[1];
					              if ("370812".equals(areacode)) {
					                  areacode = "370882";
					              }else if ("370871".equals(areacode)) {
					                  areacode = "370890";
					              }else if ("370892".equals(areacode)) {
					                  areacode = "370891";
					              }else if ("370893".equals(areacode)) {
					                  areacode = "370892";
					              }
					              iteminfo.setBelongxiaqucode(areacode);
					              iteminfo.setConstructionsitedesc(jsddxq);
					              iteminfo.setContractperson(xmfzr);
					              iteminfo.setConstructionscaleanddesc(jsgmjnr);
					           /*   iteminfo.set("xiaqucode", xmssxzqh);
					              iteminfo.setQuantifyconstructdept(jsdd);*/
					              iteminfo.setItemtype(xmlxdm);
					              iteminfo.setConstructionproperty(jsxzdm);
					              iteminfo.setOperatedate(formatter1.parse(sbrq));
					              iteminfo.setConstructionsite(jsdd);
					            /*  iteminfo.set("xmguid","Y");*/
					              iteminfoservice.addAuditRsItemBaseinfo(iteminfo);
					              
				            }
		                }

		            }
	            }
		          log.info("项目同步完成 =====>时间：" + EpointDateUtil
	                        .convertDate2String(new Date(), EpointDateUtil.DATE_TIME_FORMAT));	
	            }
	        
	        } catch (Exception e) {
	        	e.printStackTrace();
	            log.info("同步失败 =====>时间：" + EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_TIME_FORMAT));
	        }
	 }
}
