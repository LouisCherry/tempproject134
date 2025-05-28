package com.epoint.synctask;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.epoint.core.EpointFrameDsManager;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.log.LogUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;

/**
 * 
 * @author swy
 * @version [版本号, 2018年8月24日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
@DisallowConcurrentExecution
public class EpointSyncMaterialJob implements Job
{

    transient Logger log = LogUtil.getLog(EpointSyncMaterialJob.class);
    private static String prov_filedown = ConfigUtil.getConfigValue("epointframe", "prov_filedown");
    private static String syncareacode = ConfigUtil.getConfigValue("datasyncjdbc", "syncareacode2");


    /**
     * 程序入口
     */
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        try {
            EpointFrameDsManager.begin(null);
            doService();
            EpointFrameDsManager.commit();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            EpointFrameDsManager.close();
        }
    }

    private void doService() {
        try {
        	log.info("【材料补充服务开始】");
        	EpointSyncMaterialService service = new EpointSyncMaterialService();
            //获取前置机所有存在待同步事项的部门ORG_CODE
            //Record r = service.getQLSX();
            //开始时间
            long startTime = System.currentTimeMillis();
            List<Record> rnew = service.findMaterialList(syncareacode);
            //结束之间
            long endTime = System.currentTimeMillis();
            //程序块运行时间
            log.info("【材料补充服务】Time运行时间为：" + (endTime - startTime)/1000 + "秒");
            String innercode = "";
            String MATERIAL_INFO = "";
            for (Record info :  rnew) {
            	innercode = info.getStr("INNER_CODE");
            	MATERIAL_INFO = service.getQLSXnew(innercode);
            	if(StringUtil.isNotBlank(innercode) && StringUtil.isNotBlank(MATERIAL_INFO)) {
            		syncMaterial(MATERIAL_INFO);
            	}else {
            		log.info("【材料补充服务】未查询到前置库数据，默认更新办件数据");
            		service.updateMaterialByTaskguid(info.getStr("TASKGUID"),0,1);
            	}
            }
            log.info("【材料补充服务结束】");
        }
        catch (Exception e) {
            log.info("【材料补充服务同步失败】" + e.getMessage());
        }
    }
    
    
    /**
     * 
     *  [同步材料]
     *  [功能详细描述]    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @SuppressWarnings("unchecked")
    public static void syncMaterial(String material) {
        //默认提交电子 10提交电子文件 20提交纸质文件 35电子或纸质  40电子和纸质
    	EpointSyncMaterialService service = new EpointSyncMaterialService();
        try {
            //解析XML
            Document document = DocumentHelper.parseText(material);
            Element root = document.getRootElement();
            Element nodeList = root.element("MATERIALS");
            List<Element> s = nodeList.elements("MATERIAL");
            for (Element p : s) {
                // 材料类型 表格：10 附件 20
                // 1.来源渠道:(审批系统对应代码项:1-申请人自备;2-政府部门核发;3-其他)
                //当前节点的所有属性的list
                //获取空白表格和示例表格：
                //遍历当前节点的所有属性
            	String code = p.elementText("CODE");
                String copy = p.elementText("COPY");
                String page = p.elementText("ORIGIN");
                int intcopy = Integer.parseInt(StringUtil.isBlank(copy)?"0":copy);
                int intpage = Integer.parseInt(StringUtil.isBlank(page)?"1":page);
                service.updateMaterial(code,intcopy,intpage);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    /**
     * 下载附件
     * @param MNAME
     * @param fileurl
     * @param uuid
     */
    public void getmaterial(String MNAME, String fileurl, String uuid) {
        try {
        	String[] urls = fileurl.split(";");
        	for (int i = 0; i < urls.length; i++) {
        		String url = urls[i];
        		if (StringUtil.isNotBlank(url)) {
        			FrameAttachInfo attachinfo = new FrameAttachInfo();
        			attachinfo.setAttachGuid(UUID.randomUUID().toString());
        			attachinfo.setCliengGuid(uuid);
        			attachinfo.setCliengTag("sdtasksync");
        			attachinfo.setAttachFileName(MNAME);
        			try {
        				IAttachService iAttachService = ContainerFactory.getContainInfo()
        						.getComponent(IAttachService.class);
        				Map<String, Object> map = new HashMap<>();
        				map = downloadFile(prov_filedown + url);
        				if(map!=null){
        					attachinfo.setAttachLength((Long)map.get("length"));
        					iAttachService.addAttach(attachinfo, (InputStream)map.get("stream"));
        				}
        			}
        			catch (Exception e) {
        				log.info("【事项同步附件信息异常】" + prov_filedown + url);
        				e.printStackTrace();
        			}
        		}
			}
        }
        catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    /**
     * 山东附件库
     * @param url
     * @return
     * @throws Exception
     */
    public static Map<String, Object> downloadFile(String url) throws Exception {
        Map<String, Object> map = new HashMap<>();
        HttpClient client = new HttpClient();
        client.getParams().setContentCharset("UTF-8");
        GetMethod getMethod = new GetMethod(url);
        client.executeMethod(getMethod);
        long len = getMethod.getResponseContentLength();
        InputStream inputStream = getMethod.getResponseBodyAsStream();
        map.put("length", len);
        map.put("stream", inputStream);
        return map;
    }
}
