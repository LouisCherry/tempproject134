package com.epoint.jnycsl.utils;


import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditprojectunusual.domain.AuditProjectUnusual;
import com.epoint.basic.auditproject.auditprojectunusual.inter.IAuditProjectUnusual;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.material.domain.AuditTaskMaterial;
import com.epoint.basic.audittask.material.inter.IAuditTaskMaterial;
import com.epoint.basic.authentication.UserSession;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.organ.ou.api.IOuService;
import com.epoint.frame.service.organ.ou.entity.FrameOu;
import com.epoint.frame.service.organ.ou.entity.FrameOuExtendInfo;
import com.epoint.jn.inproject.api.IWebUploaderService;
import com.epoint.jn.inproject.api.entity.eajcstepbasicinfogt;
import com.epoint.jn.inproject.api.entity.eajcstepdonegt;
import com.epoint.jn.inproject.api.entity.eajcstepprocgt;
import com.epoint.jn.inproject.api.entity.eajcstepspecialnode;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.frame.service.metadata.systemparameters.api.IConfigService;

import static com.peersafe.base.client.enums.Message.response;


/**
 *
 * 浪潮数据推送工具类
 * @作者 lsting
 * @version [版本号, 2018年10月14日]
 */
@SuppressWarnings("deprecation")
public class WavePushInterfaceUtils
{
    /**
     * 获取系统参数API
     */
    private IConfigService iConfigService = ContainerFactory.getContainInfo().getComponent(IConfigService.class);
	//private static final String QfWavePreFixUrl = "http://59.206.96.200:8090/";
//	private static final String QfWavePreFixUrlgg = "http://59.206.96.199:8070/";
	//其他
	private static final String QfWavePreFixUrl = ConfigUtil.getConfigValue("datasyncjdbc", "qfwaveprefixurl");
	//公共服务
    private static final String QfWavePreFixUrlgg = ConfigUtil.getConfigValue("datasyncjdbc", "qfwaveprefixurlgg");
    //行政许可
    private static final String QfWavePreFixUrlXK = ConfigUtil.getConfigValue("datasyncjdbc", "qfwaveprefixurlxk");

    private IOuService ouservice = ContainerFactory.getContainInfo().getComponent(IOuService.class);

    private IWebUploaderService iWebUploaderService  = ContainerFactory.getContainInfo().getComponent(IWebUploaderService.class);

    private IAuditTaskMaterial iAuditTaskMaterial = ContainerFactory.getContainInfo().getComponent(IAuditTaskMaterial.class);

    private IAuditProjectUnusual auditProjectUnusual = ContainerFactory.getContainInfo().getComponent(IAuditProjectUnusual.class);

    // 推送浪潮归集库/省前置库
    public  void TsLcGjk(AuditProject auditProject, AuditTask auditTask, UserSession userSession) {
        FrameOuExtendInfo frameOuExten1 = ouservice.getFrameOuExtendInfo(auditProject.getOuguid());
        String xzareacode = frameOuExten1.getStr("areacode");
        if (StringUtil.isNotBlank(xzareacode) && auditProject != null) {
            String areacodere = "";
            if (xzareacode.length() == 6) {
                areacodere = xzareacode + "000";
            } else {
                areacodere = xzareacode;
            }

            //先查询前置库有没有重复办件
            eajcstepbasicinfogt baseinfo = iWebUploaderService.getQzkBaseInfo(auditProject.getFlowsn());
            if (baseinfo == null) {
                String Orgbusno = UUID.randomUUID().toString();
                baseinfo = new eajcstepbasicinfogt();
                FrameOu frameOu = ouservice.getOuByOuGuid(auditProject.getOuguid());
                if (frameOu == null) {
                    frameOu = iWebUploaderService.getFrameOuByOuname(auditProject.getOuname());
                }
                baseinfo.set("rowguid", Orgbusno);
                baseinfo.setOrgbusno(Orgbusno);
                baseinfo.setProjid(auditProject.getFlowsn());
                baseinfo.setProjpwd(auditProject.getStr("pwd"));
                baseinfo.setValidity_flag("1");
                baseinfo.setDataver("1");
                baseinfo.setStdver("1");
                baseinfo.setItemno(
                        StringUtil.isBlank(auditTask.getStr("inner_code")) ? "无" : auditTask.getStr("inner_code"));
                List<AuditTaskMaterial> materials = iAuditTaskMaterial
                        .getUsableMaterialListByTaskguid(auditTask.getRowguid()).getResult();
                String materialname = "";
                if (materials != null && !materials.isEmpty()) {
                    for (AuditTaskMaterial material : materials) {
                        materialname += "[纸质]" + material.getMaterialname() + ";";
                    }
                } else {
                    materialname = "无";
                }
                baseinfo.set("ACCEPTLIST", materialname);
                String shenpilb = "";
                if ("11".equals(auditTask.getShenpilb())) {
                    shenpilb = "20";
                } else if ("10".equals(auditTask.getShenpilb())) {
                    shenpilb = "10";
                } else if ("07".equals(auditTask.getShenpilb())) {
                    shenpilb = "07";
                } else if ("06".equals(auditTask.getShenpilb())) {
                    shenpilb = "08";
                } else if ("05".equals(auditTask.getShenpilb())) {
                    shenpilb = "05";
                } else if ("02".equals(auditTask.getShenpilb())) {
                    shenpilb = "02";
                } else if ("01".equals(auditTask.getShenpilb())) {
                    shenpilb = "01";
                } else if ("03".equals(auditTask.getShenpilb())) {
                    shenpilb = "03";
                } else if ("04".equals(auditTask.getShenpilb())) {
                    shenpilb = "04";
                } else if ("08".equals(auditTask.getShenpilb())) {
                    shenpilb = "09";
                } else if ("09".equals(auditTask.getShenpilb())) {
                    shenpilb = "06";
                }

                baseinfo.set("ITEMTYPE", shenpilb);
                baseinfo.set("CATALOGCODE", auditTask.getStr("CATALOGCODE"));
                baseinfo.set("TASKCODE", auditTask.getStr("TASKCODE"));
                if (10 == auditProject.getApplyertype()) {
                    baseinfo.set("APPLYERTYPE", "2");
                    baseinfo.set("ApplyerPageType", "01");
                } else {
                    baseinfo.set("APPLYERTYPE", "1");
                    baseinfo.set("ApplyerPageType", "111");
                }
                //申请人证件号码
                baseinfo.set("ApplyerPageCode", auditProject.getCertnum());
                baseinfo.setItemname(auditTask.getTaskname());
                baseinfo.setProjectname(auditProject.getProjectname());
                baseinfo.setApplicant(auditProject.getApplyername());
                baseinfo.setApplicantCardCode(auditProject.getCertnum());
                baseinfo.setApplicanttel(auditProject.getContactmobile());
                if (frameOu != null) {
                    baseinfo.setAcceptdeptid(frameOu.getOucode());
                    baseinfo.setAcceptdeptname(frameOu.getOuname());
                } else {
                    baseinfo.setAcceptdeptid("无");
                    baseinfo.setAcceptdeptname("无");
                }

                baseinfo.setRegion_id(areacodere);
                baseinfo.setApprovaltype("2");
                baseinfo.setPromisetimelimit("0");
                baseinfo.setPromisetimeunit("1");
                baseinfo.setTimelimit("0");
                baseinfo.setItemregionid(areacodere);
                if ("22".equals(auditProject.getCerttype())) {
                    baseinfo.setApplicantCardtype("111");
                } else if ("16".equals(auditProject.getCerttype())) {
                    baseinfo.setApplicantCardtype("01");
                } else if ("14".equals(auditProject.getCerttype())) {
                    baseinfo.setApplicantCardtype("02");
                }
                baseinfo.setSubmit("1");
                                    baseinfo.setOccurtime(EpointDateUtil.convertDate2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
                                    baseinfo.setMaketime(EpointDateUtil.convertDate2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
                baseinfo.set("Status", "0");
                if (StringUtil.isNotBlank(auditProject.getCertnum())) {
                    baseinfo.setAcceptdeptcode(auditProject.getCertnum());
                } else {
                    baseinfo.setAcceptdeptcode("无");
                }
                baseinfo.setAcceptdeptcode1("无");
                baseinfo.setAcceptdeptcode2("无");

                try {
                    iWebUploaderService.insertQzkBaseInfo(baseinfo);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                // 插入办件流程表（受理步骤）
                eajcstepprocgt process = new eajcstepprocgt();
                process.set("rowguid", UUID.randomUUID().toString());
                process.setOrgbusno(Orgbusno);
                process.setProjid(auditProject.getFlowsn());
                process.setValidity_flag("1");
                process.setDataver("1");
                process.setStdver("1");
                process.setSn("1");
                process.setNodename("受理");
                process.setNodecode("act1");
                process.setNodetype("1");
                process.setNodeprocer(UUID.randomUUID().toString());
                process.setNodeprocername(auditProject.getAcceptusername());
                process.setNodeprocerarea(areacodere);
                process.setRegion_id(areacodere);
                if (frameOu != null) {
                    process.setProcunit(frameOu.getOucode());
                    process.setProcunitname(frameOu.getOuname());
                }
                process.setNodestate("02");
                String accepttime = EpointDateUtil.convertDate2String(auditProject.getAcceptuserdate(),
                        EpointDateUtil.DATE_TIME_FORMAT);
                process.setNodestarttime(accepttime);
                process.setNodeendtime(accepttime);
                process.setNoderesult("4");
                                    process.setOccurtime(EpointDateUtil.convertDate2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
                                    process.setMaketime(EpointDateUtil.convertDate2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
                process.setSignstate("0");
                process.setItemregionid(areacodere);
                // FIXME 111
                if(StringUtil.isNotBlank(accepttime) && StringUtils.isNotEmpty(process.getNodeprocername())){
                    try {
                        iWebUploaderService.insertQzkProcess(process);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                eajcstepdonegt done = new eajcstepdonegt();
                done.set("rowguid", UUID.randomUUID().toString());
                done.setOrgbusno(Orgbusno);
                done.setProjid(auditProject.getFlowsn());
                done.setValidity_flag("1");
                done.setStdver("1");
                done.setRegion_id(areacodere);
                done.setDataver("1");
                done.setDoneresult("0");
                done.setApprovallimit(new Date());
                done.setCertificatenam("111");
                done.setCertificateno(auditProject.getCertnum());
                done.setIsfee("0");
                done.setOccurtime(EpointDateUtil.convertDate2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
                done.setTransactor(userSession.getDisplayName());
                done.setMaketime(EpointDateUtil.convertDate2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
                done.setSignstate("0");
                done.setItemregionid(areacodere);
                // FIXME 111
                try {
                    iWebUploaderService.insertQzkDone(done);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                // 插入办件流程表（已办结）
                eajcstepprocgt process1 = new eajcstepprocgt();
                process1.set("rowguid", UUID.randomUUID().toString());
                process1.setOrgbusno(Orgbusno);
                process1.setProjid(auditProject.getFlowsn());
                process1.setValidity_flag("1");
                process1.setDataver("1");
                process1.setStdver("1");
                process1.setSn("2");
                process1.setNodename("办结");
                process1.setNodecode("act2");
                process1.setNodetype("3");
                process1.setNodeprocer(userSession.getUserGuid());
                process1.setNodeprocername(userSession.getDisplayName());
                process1.setNodeprocerarea(areacodere);
                process1.setRegion_id(areacodere);
                if (frameOu != null) {
                    process1.setProcunit(frameOu.getOucode());
                    process1.setProcunitname(frameOu.getOuname());
                }
                process1.setNodestate("02");
                String banjietime = EpointDateUtil.convertDate2String(auditProject.getBanjiedate(),
                        EpointDateUtil.DATE_TIME_FORMAT);
                process1.setNodestarttime(banjietime);
                process1.setNodeendtime(banjietime);
                process1.setNoderesult("1");
                process1.setOccurtime(EpointDateUtil.convertDate2String(auditProject.getBanjiedate(), "yyyy-MM-dd HH:mm:ss"));
                process1.setMaketime(EpointDateUtil.convertDate2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
                process1.setSignstate("0");
                process1.setItemregionid(areacodere);
                // FIXME 111
                if(StringUtil.isNotBlank(banjietime) && StringUtils.isNotEmpty(process1.getNodeprocername())){
                    try {
                        iWebUploaderService.insertQzkProcess(process1);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }


                SqlConditionUtil sql = new SqlConditionUtil();
                sql.eq("projectguid", auditProject.getRowguid());
                sql.in("OPERATETYPE", "10,11");
                List<AuditProjectUnusual> projectUnusualList = auditProjectUnusual
                        .getAuditProjectUnusualListByPage(sql.getMap(), 0, 99, "OperateDate", "asc").getResult().getList();
                int snNum = projectUnusualList.size();
                // 特殊环节信息表
                eajcstepspecialnode specialnode = new eajcstepspecialnode();
                if (snNum > 0) {
                    snNum = snNum / 2;
                    int num = 1;

                    // 原系统业务流水
                    specialnode.setOrgbusno(Orgbusno);
                    // 申办号
                    specialnode.setProjid(auditProject.getFlowsn());
                    // 业务办理行政区划
                    specialnode.setRegion_id(areacodere);
                    // 事项所属区划
                    specialnode.setItemregionid(areacodere);
                    // 标准版本号
                    specialnode.setStdver("1");
                    // 数据版本号
                    specialnode.setDataver("1");
                    if (frameOu != null) {
                        // 处理单位，组织机构代码
                        specialnode.setProcunitid(frameOu.getOucode());
                        // 处理单位名称
                        specialnode.setProcunitname(frameOu.getOuname());
                    }

                    // 环节处理意见
                    specialnode.setNodeprocadv("公示暂停");
                    // 环节处理依据 默认济宁市
                    specialnode.setNodeprocaccord("济宁市");
                    // 材料清单 默认：根据相关法律条文
                    specialnode.setLists("根据相关法律条文");
                    // 环节处理结果 默认:公示
                    specialnode.setNoderesult("3");
                    // 数据存库时间
                    specialnode.setMaketime(EpointDateUtil.convertDate2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
                    // 标志位
                    specialnode.setSignState(areacodere);
                    // 环节名称
                    specialnode.setNodename("暂停和恢复");
                    for (int i = 0; i < snNum; i++) {
                        if (StringUtil.isNotBlank(projectUnusualList.get(i + num - 1))) {
                            Date startDate = projectUnusualList.get(i + num - 1).getOperatedate();
                            Date endDate = projectUnusualList.get(i + num - 1).getOperatedate();
                            if (StringUtil.isNotBlank(projectUnusualList.get(i + num))) {
                                endDate = projectUnusualList.get(i + num).getOperatedate();
                            }
                            // 序号
                            specialnode.setSn(String.valueOf(num));
                            specialnode.set("rowguid", UUID.randomUUID().toString());
                            // 处理人id 操作人用户标识
                            specialnode.setProcerid(projectUnusualList.get(i + num - 1).getOperateuserguid());
                            // 处理人姓名 操作人用户名称
                            specialnode.setProcername(projectUnusualList.get(i + num - 1).getOperateusername());
                            // 环节开始时间 暂停开始时间
                            specialnode.setNodestarttime(startDate);
                            // 环节结束时间 暂停结束时间 时间为空可以填开始时间
                            specialnode.setNodeendtime(endDate);
                            // 环节处理地点 挂起原因
                            specialnode.setNodeprocaddr(projectUnusualList.get(i + num - 1).getNote());
                            // 环节发生时间 暂停开始时间
                            specialnode.setNodetime(startDate);
                            if(startDate!=null){
                                try {
                                    iWebUploaderService.insertQzkSpecialnode(specialnode);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            num++;
                        }

                    }
                }
            }
        }
    }

    /**
     * 根据事项ID生成受理编号和查询密码
     *  @param params
     *  @param request
     *  @return
     * @throws IOException
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public static JSONObject createReceiveNum(String paramsGet, String shenpilb) throws IOException {
        String url = "";
        if ("11".equals(shenpilb)) {
            url = QfWavePreFixUrlgg;
        }
        else if ("01".equals(shenpilb)) {
            url = QfWavePreFixUrlXK;
        }
        else {
            url = QfWavePreFixUrl;
        }
        // 接口返回值
        JSONObject responseJsonObj = new JSONObject();
        // 请求接口地址构造
        String interFaceUrl = url + "web/approval/createReceiveNum";
        // 开始请求接口
        try {
            responseJsonObj = HttplcUtils.getHttp(interFaceUrl, paramsGet);
        }
        catch (JSONException e) {
        }
        return responseJsonObj;
    }

    /**
     * 根据单位获取已发布事项列表的相关信息
     *  @param params
     *  @param request
     *  @return
     * @throws IOException
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @SuppressWarnings("static-access")
    public JSONObject getItemList(String paramsGet) throws IOException {
        // 接口返回值
        JSONObject responseJsonObj = new JSONObject();
        // 请求接口地址构造
        String interFaceUrl = QfWavePreFixUrl + "main/power/getItemList";
        //system.out.println("获取事项code接口请求地址及参数：" + interFaceUrl);
        // 开始请求接口
        try {
            responseJsonObj = HttplcUtils.getHttp(interFaceUrl, paramsGet);
        }
        catch (JSONException e) {
        }
        return responseJsonObj;
    }

    /**
     * 其他申报数据对接
     * @param paramsJson
     * @return
     * @throws IOException
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @SuppressWarnings({"static-access" })
    public JSONObject acceptDataInfo(Map<String, Object> paramsMap) throws IOException {
        // 接口返回值
        JSONObject responseJsonObj = new JSONObject();
        // 请求接口地址构造
        String interFaceUrl = QfWavePreFixUrl + "web/approval/acceptforother";
        // 开始请求接口
        try {
            responseJsonObj = HttplcUtils.postHttp(interFaceUrl, paramsMap);
        }
        catch (JSONException e) {

        }
        return responseJsonObj;
    }

    /**
     * 行政许可申报数据对接
     * @param paramsJson
     * @return
     * @throws IOException
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @SuppressWarnings({"static-access" })
    public JSONObject acceptDataInfoXK(Map<String, Object> paramsMap) throws IOException {
        // 接口返回值
        JSONObject responseJsonObj = new JSONObject();
        // 请求接口地址构造
        String interFaceUrl = QfWavePreFixUrlXK + "web/approval/acceptforother";
        // 开始请求接口
        try {
            responseJsonObj = HttplcUtils.postHttp(interFaceUrl, paramsMap);
        }
        catch (JSONException e) {

        }
        return responseJsonObj;
    }



    /**
     * 公共服务申报数据对接
     * @param paramsJson
     * @return
     * @throws IOException
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @SuppressWarnings({"static-access" })
    public JSONObject acceptDataInfogg(Map<String, Object> paramsMap) throws IOException {
        // 接口返回值
        JSONObject responseJsonObj = new JSONObject();
        // 请求接口地址构造
        String interFaceUrl = QfWavePreFixUrlgg + "web/approval/acceptforother";
        // 开始请求接口
        try {
            responseJsonObj = HttplcUtils.postHttp(interFaceUrl, paramsMap);
        }
        catch (JSONException e) {

        }
        return responseJsonObj;
    }
	/**
	 * 向服务器发送文件
	 *
	 * @param file_map
	 * @param file_url
	 * @param server_url
	 * @return
	 */
	public String startUploadService(Map<String, String> params, String file_url, String server_url) {
		final String CHARSET = HTTP.UTF_8;
			// 开启上传队列
			File file = null;
			if (!"".equals(file_url)) {
				file = new File(file_url);
			}
        DefaultHttpClient httpclient = null;
            String result = "";
            try {
                httpclient = new DefaultHttpClient();
                // 设置通信协议版本
                httpclient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
                // 文件参数设置
                HttpPost httppost = new HttpPost(server_url);
                MultipartEntity mpEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE, null,
                        Charset.forName(CHARSET));
                if (params != null && !params.isEmpty()) {
                    // 编码参数
                    /*
                     * for (String k : params.keySet()) { StringBody valueBody = new
                     * StringBody(params.get(k),Charset.forName(CHARSET));
                     * mpEntity.addPart(k, valueBody); }
                     */

                    for (Entry<String, String> entry : params.entrySet()) {
                        StringBody valueBody = new StringBody(entry.getValue(), Charset.forName(CHARSET));
                        mpEntity.addPart(entry.getKey(), valueBody);
                    }

                }
                ContentBody cbFile = new FileBody(file);
                mpEntity.addPart("file", cbFile);
                httppost.setEntity(mpEntity);
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity resEntity = response.getEntity();
                if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                    EntityUtils.toString(response.getEntity(), CHARSET);
                    // String sss = EntityUtils.toString(response.getEntity(),
                    // CHARSET);
                    // throw new RuntimeException("请求失败");
                    return "{\"code\":\"4000\", \"msg\":\"URL请求失败\", \"result\":\'" + response.getStatusLine().toString()
                            + "\'}";
                }
                result = (resEntity == null) ? null : EntityUtils.toString(resEntity, CHARSET);
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                if(httpclient!=null){
                    httpclient.close();
                }
            }
			return result;
	}

}
