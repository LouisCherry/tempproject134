package com.epoint.xmz.realestateinfo.rest;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.epoint.common.util.JsonUtils;
import com.epoint.common.util.OfficeWebUrlEncryptUtil;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.httpclient.HttpUtil;
import com.epoint.core.utils.web.WebUtil;
import com.epoint.frame.service.attach.entity.FrameAttachStorage;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.kohsuke.rngom.digested.DDataPattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.controller.api.ApiBaseController;
import com.epoint.common.util.ZwdtConstant;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.sql.SqlConditionUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import com.epoint.xmz.realestateinfo.api.IRealEstateInfoService;
import com.epoint.xmz.realestateinfo.api.entity.RealEstateInfo;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

@RestController
@RequestMapping("/realEstate")
public class RealEstateInfoRestController extends ApiBaseController
{
    transient Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());

    @Autowired
    private IAttachService attachService;
    
    @Autowired
    private IRealEstateInfoService iRealEstateInfoService;

    /**
     * 获取栏目列表
     *
     * @param params 参数
     * @return 栏目列表JSON字符串
     */
    @RequestMapping(value = "/getList", method = RequestMethod.POST)
    public String getInfoColumnList(@RequestBody String params) {
        log.info("==开始调用getList接口===");
        log.info("getList params:" + params);
        JSONObject param = JSON.parseObject(params);
        JSONObject custom = new JSONObject();
    	String token = param.getString("token");
		if (ZwdtConstant.SysValidateData.equals(token)) {
			JSONObject paramJson = JSON.parseObject(param.getString("params"));
            Integer currentPage = StringUtil.isNotBlank(paramJson.getString("pageNum"))
                    ? paramJson.getInteger("pageNum")
                    : 0;
            Integer pageSize = StringUtil.isNotBlank(paramJson.getString("pageSize"))
                    ? paramJson.getInteger("pageSize")
                    : 10;
            String reName = StringUtil.isNotBlank(paramJson.getString("reName"))
                    ? paramJson.getString("reName")
                    : "";
            String referencePriceStart = StringUtil.isNotBlank(paramJson.getString("referencePriceStart"))
                    ? paramJson.getString("referencePriceStart")
                    : "";
            String referencePriceEnd = StringUtil.isNotBlank(paramJson.getString("referencePriceEnd"))
                    ? paramJson.getString("referencePriceEnd")
                    : "";
            String preSaleDateStart = StringUtil.isNotBlank(paramJson.getString("preSaleDateStart"))
                    ? paramJson.getString("preSaleDateStart")
                    : "";
            String preSaleDateEnd = StringUtil.isNotBlank(paramJson.getString("preSaleDateEnd"))
                    ? paramJson.getString("preSaleDateEnd")
                    : "";
            String pv = StringUtil.isNotBlank(paramJson.getString("pv"))
                    ? paramJson.getString("pv")
                    : "";
	        String belongArea = StringUtil.isNotBlank(paramJson.getString("belongArea"))
	                ? paramJson.getString("belongArea")
	                : "";
            String houseType = StringUtil.isNotBlank(paramJson.getString("houseType"))
                    ? paramJson.getString("houseType")
                    : "";
            String referencePriceType = StringUtil.isNotBlank(paramJson.getString("referencePriceType")) ? paramJson.getString("referencePriceType") : "";

            SqlConditionUtil sqlUtil = new SqlConditionUtil();
            if (StringUtil.isNotBlank(reName)) {
                sqlUtil.like("re_Name", reName);
            }
            if (StringUtil.isNotBlank(referencePriceStart)) {
                sqlUtil.ge("reference_price", referencePriceStart);
            }
            if (StringUtil.isNotBlank(referencePriceEnd)) {
                sqlUtil.le("reference_price", referencePriceEnd);
            }
            if(StringUtils.isNotBlank(referencePriceType)){
                if ("1".equals(referencePriceType)) {
                    sqlUtil.setOrderAsc("reference_price");
                }else {
                    sqlUtil.setOrderDesc("reference_price");
                }
            }else{
                sqlUtil.setOrderDesc("order_num");
            }

            
            if (StringUtil.isNotBlank(preSaleDateStart)) {
                sqlUtil.ge("pre_sale_date", preSaleDateStart);
            }
            if (StringUtil.isNotBlank(preSaleDateEnd)) {
                sqlUtil.le("pre_sale_date", preSaleDateEnd);
            }
            if(StringUtils.isNotBlank(pv)){
                if ("1".equals(pv)) {
                    sqlUtil.setOrderAsc("pv");
                }else {
                    sqlUtil.setOrderDesc("pv");
                }
            }else{
                sqlUtil.setOrderDesc("order_num");
            }
            
            if (StringUtil.isNotBlank(belongArea) && !"all".equals(belongArea)) {
                sqlUtil.eq("belong_Area", belongArea);
            }

            if (StringUtil.isNotBlank(houseType) && !"all".equals(houseType)) {
                sqlUtil.eq("housetype", houseType);
            }
            
            PageData<RealEstateInfo> pageData = iRealEstateInfoService.paginatorList(sqlUtil.getMap(), currentPage*pageSize,
                    pageSize);
            JSONArray array = new JSONArray();
            
            for (RealEstateInfo column : pageData.getList()) {
                JSONObject obj = new JSONObject();
                JSONArray aerialarray = new JSONArray();
                
                obj.put("reGuid", column.getRowguid());
                obj.put("reName", column.getRe_name());
                obj.put("devUnit", column.getDev_unit());
                obj.put("belongArea", column.getBelong_area());
                obj.put("constContent", column.getConst_content());
                obj.put("referencePrice", column.getReference_price());
                obj.put("pv", column.getPv());
                obj.put("areaaddr", column.getArea_addr());
                String aerialCliengGuid = column.getAerial_view();
                List<FrameAttachInfo> frameAttachInfos = attachService.getAttachInfoListByGuid(
                		aerialCliengGuid);
                if(frameAttachInfos!=null&&!frameAttachInfos.isEmpty()) {
                    for (FrameAttachInfo infoDetail : frameAttachInfos) {
                        if (infoDetail != null && !infoDetail.isEmpty()) {
//                            JSONObject objDetail = new JSONObject();
//                            objDetail.put("attachName", infoDetail.getAttachFileName());
//                            objDetail.put("type", infoDetail.getContentType());
//                            objDetail.put("downUrl", "http://jizwfw.sd.gov.cn/jnzwdt/rest/frame/base/attach/attachAction/getContent?isCommondto=true&attachGuid="
//									+ infoDetail.getAttachGuid());
//                            aerialarray.add(objDetail);
                            
                            aerialarray.add("http://jizwfw.sd.gov.cn/jnzwdt/rest/frame/base/attach/attachAction/getContent?isCommondto=true&attachGuid="
									+ infoDetail.getAttachGuid());
                            
                        }
                    }
                }
                obj.put("aerialView", aerialarray);
                array.add(obj);
            }
            custom.put("list", array);
            custom.put("total", pageData.getRowCount());
            return generateReturnInfo("1", "调用成功", custom);

        }
        else {
            return generateReturnInfo("0", "参数验证失败", custom);

        }
    }

    /**
     * 获取信息发布列内容列表
     *
     * @param params 参数
     * @return 栏目列表JSON字符串
     */
    @RequestMapping(value = "/getBasicDetail", method = RequestMethod.POST)
    public String getInfoReleaseList(@RequestBody String params) {
        log.info("==开始调用getBasicDetail接口===");
        log.info("getBasicDetail params:" + params);
        JSONObject param = JSON.parseObject(params);
        JSONObject custom = new JSONObject();
        String token = param.getString("token");
		if (ZwdtConstant.SysValidateData.equals(token)) {
			 JSONObject paramJson = JSON.parseObject(param.getString("params"));
            String reGuid = paramJson.getString("reGuid");
            
            if (StringUtil.isBlank(reGuid)) {
            	return generateReturnInfo("0", "楼盘标识不能为空！", custom);
            }

            RealEstateInfo estateInfo = iRealEstateInfoService.find(reGuid);
            
            if (estateInfo == null) {
            	return generateReturnInfo("0", "未查询到该楼盘信息！", custom);
            }else{
                //增加浏览量
                int pv = estateInfo.getPv();
                if(pv>0){
                    pv=pv+1;
                }else{
                    pv=0;
                }
                estateInfo.setPv(pv);
                iRealEstateInfoService.update(estateInfo);
            }
            
            
            custom.put("reGuid", estateInfo.getRowguid());
            custom.put("reName", estateInfo.getRe_name());
            custom.put("devUnit", estateInfo.getDev_unit());
            custom.put("areaAddr", estateInfo.getArea_addr());
            custom.put("constContent", estateInfo.getConst_content());
            custom.put("referencePrice", estateInfo.getReference_price());
            custom.put("pv", estateInfo.getPv());
            custom.put("belongArea", estateInfo.getBelong_area());
            custom.put("houseType", estateInfo.getHouse_type());
            custom.put("buildType", estateInfo.getBuild_type());
            custom.put("preSaleDate", EpointDateUtil.convertDate2String(estateInfo.getPre_sale_date(), EpointDateUtil.DATE_TIME_FORMAT));
            custom.put("greenRate", estateInfo.getGreen_rate());
            custom.put("plotRatio", estateInfo.getPlot_ratio());
            custom.put("planCarNum", estateInfo.getPlan_car_num());
            custom.put("motorNum", estateInfo.getMotor_num());
            custom.put("motorDNum", estateInfo.getMotor_d_num());
            custom.put("motorUNum", estateInfo.getMotor_u_num());
            custom.put("nmotorNum", estateInfo.getNmotor_num());
            custom.put("nmotorDNum", estateInfo.getNmotor_d_num());
            custom.put("nmotorUNum", estateInfo.getNmotor_u_num());
            custom.put("houseNum", estateInfo.getHouse_num());
            custom.put("salesTel", estateInfo.getSales_tel());
            custom.put("salesAddr", estateInfo.getSales_addr());
            custom.put("developers", estateInfo.getDevelopers());
            custom.put("developersAddr", estateInfo.getDevelopers_addr());
            custom.put("developersReg", estateInfo.getDevelopers_reg());
            custom.put("vr_url",estateInfo.getStr("vr_url"));
            return generateReturnInfo("1", "调用成功", custom);
        }
        else {
            return generateReturnInfo("0", "参数验证失败", custom);
        }
    }

    /**
     * 获取信息发布内容 根据主键查询信息发布内容
     *
     * @param params 参数
     * @return 栏目列表JSON字符串
     */
    @RequestMapping(value = "/getPermitDetail", method = RequestMethod.POST)
    public String getInfoReleaseByGuid(@RequestBody String params, @Context HttpServletRequest request) {
        log.info("==开始调用getPermitDetail接口===");
        log.info("getPermitDetail params:" + params);
        JSONObject param = JSON.parseObject(params);
        JSONObject custom = new JSONObject();
        String token = param.getString("token");
		if (ZwdtConstant.SysValidateData.equals(token)) {
	        JSONObject paramJson = JSON.parseObject(param.getString("params"));
        	JSONArray permitData = new JSONArray();
            String reGuid = paramJson.getString("reGuid");
            if (StringUtil.isNotBlank(reGuid)) {
            	RealEstateInfo estateInfo = iRealEstateInfoService.find(reGuid);
                if (estateInfo != null && !estateInfo.isEmpty()) {
                	//国有土地使用权证
                	JSONArray lurcarray = new JSONArray();
                	JSONObject lurcobj = new JSONObject();
                	String aerialCliengGuid = estateInfo.getLurc();
                    if(StringUtil.isNotBlank(aerialCliengGuid)){
                        List<FrameAttachInfo> frameAttachInfos = attachService.getAttachInfoListByGuid(
                                aerialCliengGuid);
                        JSONArray preViewUrlArray = new JSONArray();
                        JSONArray isOffice = new JSONArray(); // office文档
                        JSONArray isPhoto = new JSONArray(); // 是否图片
                        if(frameAttachInfos!=null&&!frameAttachInfos.isEmpty()) {

                            for (FrameAttachInfo infoDetail : frameAttachInfos) {
                                if (infoDetail != null && !infoDetail.isEmpty()) {
                                    lurcarray.add("http://jizwfw.sd.gov.cn/jnzwdt/rest/frame/base/attach/attachAction/getContent?isCommondto=true&attachGuid="
                                            + infoDetail.getAttachGuid());
                                    preViewUrlArray.add(getPreviewUrl(infoDetail,request));
                                    // 附件类型
                                    String contentType = infoDetail.getContentType();
                                    if (StringUtil.isNotBlank(contentType) && (".doc".equals(contentType)
                                            || ".docx".equals(contentType) | ".pdf".equals(contentType))) {
                                        isOffice.add(true);
                                        isPhoto.add(false);
                                    }
                                    else {
                                        isOffice.add(false);
                                        isPhoto.add(true);
                                    }

                                }
                            }
                        }

                        lurcobj.put("preViewUrl", preViewUrlArray);
                        lurcobj.put("isOffice", isOffice);
                        lurcobj.put("isPhoto", isPhoto);
                    }
                    lurcobj.put("name", "国有土地使用权证");
                    lurcobj.put("value", lurcarray);
                    JSONArray preViewUrlArray1 = new JSONArray();
                    JSONArray isOffice1 = new JSONArray(); // office文档
                    JSONArray isPhoto1 = new JSONArray(); // 是否图片

                    //建设用地规划许可证
                	JSONArray landusepermitarray = new JSONArray();
                	JSONObject landusepermitobj = new JSONObject();
                	String landusepermitCliengGuid = estateInfo.getLand_use_permit();
                    if(StringUtil.isNotBlank(landusepermitCliengGuid)){
                        List<FrameAttachInfo> landusepermitAttachs = attachService.getAttachInfoListByGuid(
                                landusepermitCliengGuid);
                        if(landusepermitAttachs!=null&&!landusepermitAttachs.isEmpty()) {
                            for (FrameAttachInfo infoDetail : landusepermitAttachs) {
                                if (infoDetail != null && !infoDetail.isEmpty()) {
                                    landusepermitarray.add("http://jizwfw.sd.gov.cn/jnzwdt/rest/frame/base/attach/attachAction/getContent?isCommondto=true&attachGuid="
                                            + infoDetail.getAttachGuid());
                                    preViewUrlArray1.add(getPreviewUrl(infoDetail,request));
                                    // 附件类型
                                    String contentType = infoDetail.getContentType();
                                    if (StringUtil.isNotBlank(contentType) && (".doc".equals(contentType)
                                            || ".docx".equals(contentType) | ".pdf".equals(contentType))) {
                                        isOffice1.add(true);
                                        isPhoto1.add(false);
                                    }
                                    else {
                                        isOffice1.add(false);
                                        isPhoto1.add(true);
                                    }
                                }
                            }
                        }

                        landusepermitobj.put("preViewUrl", preViewUrlArray1);
                        landusepermitobj.put("isOffice", isOffice1);
                        landusepermitobj.put("isPhoto", isPhoto1);
                    }

                    landusepermitobj.put("name", "建设用地规划许可证");
                    landusepermitobj.put("value", landusepermitarray);


                    JSONArray preViewUrlArray2 = new JSONArray();
                    JSONArray isOffice2 = new JSONArray(); // office文档
                    JSONArray isPhoto2 = new JSONArray(); // 是否图片
                    //建设工程规划许可证
                	JSONArray projectpermitarray = new JSONArray();
                	JSONObject projectpermitobj = new JSONObject();
                	String projectpermitCliengGuid = estateInfo.getProject_permit();
                    if(StringUtil.isNotBlank(projectpermitCliengGuid)){
                        List<FrameAttachInfo> projectpermitAttachs = attachService.getAttachInfoListByGuid(
                                projectpermitCliengGuid);
                        if(projectpermitAttachs!=null&&!projectpermitAttachs.isEmpty()) {
                            for (FrameAttachInfo infoDetail : projectpermitAttachs) {
                                if (infoDetail != null && !infoDetail.isEmpty()) {
                                    projectpermitarray.add("http://jizwfw.sd.gov.cn/jnzwdt/rest/frame/base/attach/attachAction/getContent?isCommondto=true&attachGuid="
                                            + infoDetail.getAttachGuid());
                                    preViewUrlArray2.add(getPreviewUrl(infoDetail,request));
                                    // 附件类型
                                    String contentType = infoDetail.getContentType();
                                    if (StringUtil.isNotBlank(contentType) && (".doc".equals(contentType)
                                            || ".docx".equals(contentType) | ".pdf".equals(contentType))) {
                                        isOffice2.add(true);
                                        isPhoto2.add(false);
                                    }
                                    else {
                                        isOffice2.add(false);
                                        isPhoto2.add(true);
                                    }
                                }
                            }
                        }

                        projectpermitobj.put("preViewUrl", preViewUrlArray2);
                        projectpermitobj.put("isOffice", isOffice2);
                        projectpermitobj.put("isPhoto", isPhoto2);
                    }

                    projectpermitobj.put("name", "建设工程规划许可证");
                    projectpermitobj.put("value", projectpermitarray);


                    //建设工程施工许可证
                    JSONArray preViewUrlArray3 = new JSONArray();
                    JSONArray isOffice3 = new JSONArray(); // office文档
                    JSONArray isPhoto3 = new JSONArray(); // 是否图片

                	JSONArray projectconpermitarray = new JSONArray();
                	JSONObject projectconpermitobj = new JSONObject();
                	String projectconpermitCliengGuid = estateInfo.getProject_con_permit();
                    if(StringUtil.isNotBlank(projectconpermitCliengGuid)){
                        List<FrameAttachInfo> projectconpermitAttachs = attachService.getAttachInfoListByGuid(
                                projectconpermitCliengGuid);
                        if(projectconpermitAttachs!=null&&!projectconpermitAttachs.isEmpty()) {
                            for (FrameAttachInfo infoDetail : projectconpermitAttachs) {
                                if (infoDetail != null && !infoDetail.isEmpty()) {
                                    projectconpermitarray.add("http://jizwfw.sd.gov.cn/jnzwdt/rest/frame/base/attach/attachAction/getContent?isCommondto=true&attachGuid="
                                            + infoDetail.getAttachGuid());
                                    preViewUrlArray3.add(getPreviewUrl(infoDetail,request));
                                    // 附件类型
                                    String contentType = infoDetail.getContentType();
                                    if (StringUtil.isNotBlank(contentType) && (".doc".equals(contentType)
                                            || ".docx".equals(contentType) | ".pdf".equals(contentType))) {
                                        isOffice3.add(true);
                                        isPhoto3.add(false);
                                    }
                                    else {
                                        isOffice3.add(false);
                                        isPhoto3.add(true);
                                    }
                                }
                            }
                        }

                        projectconpermitobj.put("preViewUrl", preViewUrlArray3);
                        projectconpermitobj.put("isOffice", isOffice3);
                        projectconpermitobj.put("isPhoto", isPhoto3);
                    }
                    projectconpermitobj.put("name", "建设工程施工许可证");
                    projectconpermitobj.put("value", projectconpermitarray);



                    //商品房预售许可证
                    JSONArray preViewUrlArray4 = new JSONArray();
                    JSONArray isOffice4 = new JSONArray(); // office文档
                    JSONArray isPhoto4 = new JSONArray(); // 是否图片
                	JSONArray presalepermitarray = new JSONArray();
                	JSONObject presalepermitobj = new JSONObject();
                	String presalepermitCliengGuid = estateInfo.getPre_sale_permit();
                    if(StringUtil.isNotBlank(presalepermitCliengGuid)){
                        List<FrameAttachInfo> presalepermitAttachs = attachService.getAttachInfoListByGuid(
                                presalepermitCliengGuid);
                        if(presalepermitAttachs!=null&&!presalepermitAttachs.isEmpty()) {
                            for (FrameAttachInfo infoDetail : presalepermitAttachs) {
                                if (infoDetail != null && !infoDetail.isEmpty()) {
                                    presalepermitarray.add("http://jizwfw.sd.gov.cn/jnzwdt/rest/frame/base/attach/attachAction/getContent?isCommondto=true&attachGuid="
                                            + infoDetail.getAttachGuid());
                                    preViewUrlArray4.add(getPreviewUrl(infoDetail,request));
                                    // 附件类型
                                    String contentType = infoDetail.getContentType();
                                    if (StringUtil.isNotBlank(contentType) && (".doc".equals(contentType)
                                            || ".docx".equals(contentType) | ".pdf".equals(contentType))) {
                                        isOffice4.add(true);
                                        isPhoto4.add(false);
                                    }
                                    else {
                                        isOffice4.add(false);
                                        isPhoto4.add(true);
                                    }
                                }
                            }
                        }

                        presalepermitobj.put("preViewUrl", preViewUrlArray4);
                        presalepermitobj.put("isOffice", isOffice4);
                        presalepermitobj.put("isPhoto", isPhoto4);
                    }

                    presalepermitobj.put("name", "商品房预售许可证");
                    presalepermitobj.put("value", presalepermitarray);


                  //建设工程竣工规划核实证
                    JSONArray preViewUrlArray5 = new JSONArray();
                    JSONArray isOffice5 = new JSONArray(); // office文档
                    JSONArray isPhoto5 = new JSONArray(); // 是否图片

                	JSONArray pcpvcarray = new JSONArray();
                	JSONObject pcpvcobj = new JSONObject();
                	String pcpvcCliengGuid = estateInfo.getPcpvc();
                    if(StringUtils.isNotBlank(pcpvcCliengGuid)){
                        List<FrameAttachInfo> pcpvcAttachs = attachService.getAttachInfoListByGuid(
                                pcpvcCliengGuid);
                        if(pcpvcAttachs!=null&&!pcpvcAttachs.isEmpty()) {
                            for (FrameAttachInfo infoDetail : pcpvcAttachs) {
                                if (infoDetail != null && !infoDetail.isEmpty()) {
                                    pcpvcarray.add("http://jizwfw.sd.gov.cn/jnzwdt/rest/frame/base/attach/attachAction/getContent?isCommondto=true&attachGuid="
                                            + infoDetail.getAttachGuid());
                                    preViewUrlArray5.add(getPreviewUrl(infoDetail,request));
                                    // 附件类型
                                    String contentType = infoDetail.getContentType();
                                    if (StringUtil.isNotBlank(contentType) && (".doc".equals(contentType)
                                            || ".docx".equals(contentType) | ".pdf".equals(contentType))) {
                                        isOffice5.add(true);
                                        isPhoto5.add(false);
                                    }
                                    else {
                                        isOffice5.add(false);
                                        isPhoto5.add(true);
                                    }
                                }
                            }
                        }

                        pcpvcobj.put("preViewUrl", preViewUrlArray5);
                        pcpvcobj.put("isOffice", isOffice5);
                        pcpvcobj.put("isPhoto", isPhoto5);
                    }

                    pcpvcobj.put("name", "建设工程竣工规划核实证");
                    pcpvcobj.put("value", pcpvcarray);


                    //消防验收/备案证明
//                    JSONArray preViewUrlArray6 = new JSONArray();
//                    JSONArray isOffice6 = new JSONArray(); // office文档
//                    JSONArray isPhoto6 = new JSONArray(); // 是否图片
//
//                	JSONArray fpcarray = new JSONArray();
//                	JSONObject fpcobj = new JSONObject();
//                	String fpcCliengGuid = estateInfo.getFpc();
//                    List<FrameAttachInfo> fpcAttachInfos = attachService.getAttachInfoListByGuid(
//                    		fpcCliengGuid);
//                    if(fpcAttachInfos!=null&&!fpcAttachInfos.isEmpty()) {
//                        for (FrameAttachInfo infoDetail : fpcAttachInfos) {
//                            if (infoDetail != null && !infoDetail.isEmpty()) {
//                            	fpcarray.add("http://jizwfw.sd.gov.cn/jnzwdt/rest/frame/base/attach/attachAction/getContent?isCommondto=true&attachGuid="
//    									+ infoDetail.getAttachGuid());
//                                preViewUrlArray6.add(getPreviewUrl(infoDetail,request));
//                                // 附件类型
//                                String contentType = infoDetail.getContentType();
//                                if (StringUtil.isNotBlank(contentType) && (".doc".equals(contentType)
//                                        || ".docx".equals(contentType) | ".pdf".equals(contentType))) {
//                                    isOffice6.add(true);
//                                    isPhoto6.add(false);
//                                }
//                                else {
//                                    isOffice6.add(false);
//                                    isPhoto6.add(true);
//                                }
//                            }
//                        }
//                    }
//                    fpcobj.put("name", "消防验收/备案证明");
//                    fpcobj.put("value", fpcarray);
//                    fpcobj.put("preViewUrl", preViewUrlArray6);
//                    fpcobj.put("isOffice", isOffice6);
//                    fpcobj.put("isPhoto", isPhoto6);

                    //竣工验收备案证明
//                    JSONArray preViewUrlArray7 = new JSONArray();
//                    JSONArray isOffice7 = new JSONArray(); // office文档
//                    JSONArray isPhoto7 = new JSONArray(); // 是否图片
//
//
//                	JSONArray carcarray = new JSONArray();
//                	JSONObject carcobj = new JSONObject();
//                	String carcCliengGuid = estateInfo.getCarc();
//                    List<FrameAttachInfo> carcAttachInfos = attachService.getAttachInfoListByGuid(
//                    		carcCliengGuid);
//                    if(carcAttachInfos!=null&&!carcAttachInfos.isEmpty()) {
//                        for (FrameAttachInfo infoDetail : carcAttachInfos) {
//                            if (infoDetail != null && !infoDetail.isEmpty()) {
//                            	carcarray.add("http://jizwfw.sd.gov.cn/jnzwdt/rest/frame/base/attach/attachAction/getContent?isCommondto=true&attachGuid="
//    									+ infoDetail.getAttachGuid());
//                                preViewUrlArray7.add(getPreviewUrl(infoDetail,request));
//                                // 附件类型
//                                String contentType = infoDetail.getContentType();
//                                if (StringUtil.isNotBlank(contentType) && (".doc".equals(contentType)
//                                        || ".docx".equals(contentType) | ".pdf".equals(contentType))) {
//                                    isOffice7.add(true);
//                                    isPhoto7.add(false);
//                                }
//                                else {
//                                    isOffice7.add(false);
//                                    isPhoto7.add(true);
//                                }
//                            }
//                        }
//                    }
//                    carcobj.put("name", "竣工验收备案证明");
//                    carcobj.put("value", carcarray);
//                    carcobj.put("preViewUrl", preViewUrlArray7);
//                    carcobj.put("isOffice", isOffice7);
//                    carcobj.put("isPhoto", isPhoto7);


                    //竣工验收备案证明
                    JSONArray preViewUrlArray8 = new JSONArray();
                    JSONArray isOffice8 = new JSONArray(); // office文档
                    JSONArray isPhoto8 = new JSONArray(); // 是否图片

                	JSONArray hshgzarray = new JSONArray();
                	JSONObject hshgzobj = new JSONObject();
                	String hshgzCliengGuid = estateInfo.getStr("hshgz");
                    if(StringUtil.isNotBlank(hshgzCliengGuid)){
                        List<FrameAttachInfo> hshgzAttachInfos = attachService.getAttachInfoListByGuid(
                                hshgzCliengGuid);
                        if(hshgzAttachInfos!=null&&!hshgzAttachInfos.isEmpty()) {
                            for (FrameAttachInfo infoDetail : hshgzAttachInfos) {
                                if (infoDetail != null && !infoDetail.isEmpty()) {
                                    hshgzarray.add("http://jizwfw.sd.gov.cn/jnzwdt/rest/frame/base/attach/attachAction/getContent?isCommondto=true&attachGuid="
                                            + infoDetail.getAttachGuid());
                                    preViewUrlArray8.add(getPreviewUrl(infoDetail,request));
                                    // 附件类型
                                    String contentType = infoDetail.getContentType();
                                    if (StringUtil.isNotBlank(contentType) && (".doc".equals(contentType)
                                            || ".docx".equals(contentType) | ".pdf".equals(contentType))) {
                                        isOffice8.add(true);
                                        isPhoto8.add(false);
                                    }
                                    else {
                                        isOffice8.add(false);
                                        isPhoto8.add(true);
                                    }
                                }
                            }
                        }

                        hshgzobj.put("preViewUrl", preViewUrlArray8);
                        hshgzobj.put("isOffice", isOffice8);
                        hshgzobj.put("isPhoto", isPhoto8);
                    }
                    hshgzobj.put("name", "建设工程竣工规划核实合格证");
                    hshgzobj.put("value", hshgzarray);


                    permitData.add(lurcobj);
                    permitData.add(landusepermitobj);
                    permitData.add(projectpermitobj);
                    permitData.add(projectconpermitobj);
                    permitData.add(presalepermitobj);
                    permitData.add(pcpvcobj);
//                    permitData.add(fpcobj);
//                    permitData.add(carcobj);
                    permitData.add(hshgzobj);

                    custom.put("permitData", permitData);
                    return generateReturnInfo("1", "查询成功", custom);
                }
                else {
                    return generateReturnInfo("0", "未查询到相关信息", custom);
                }
            }
            return generateReturnInfo("0", "未查询到相关信息", custom);

        }
        else {
            return generateReturnInfo("0", "参数验证失败", custom);
        }

    }

    /**
     * 或图片列表
     *
     */
    @RequestMapping(value = "/getImagesByGuid", method = RequestMethod.POST)
    public String getImagesByGuid(@RequestBody String params, @Context HttpServletRequest request) {
        log.info("==开始调用getImagesByGuid接口===");
        log.info("getPermitDetail params:" + params);
        JSONObject param = JSON.parseObject(params);
        JSONObject custom = new JSONObject();
        String token = param.getString("token");
        if (ZwdtConstant.SysValidateData.equals(token)) {
            JSONObject paramJson = JSON.parseObject(param.getString("params"));
            JSONArray permitData = new JSONArray();
            String reGuid = paramJson.getString("reGuid");
            if (StringUtil.isNotBlank(reGuid)) {
                RealEstateInfo estateInfo = iRealEstateInfoService.find(reGuid);
                if (estateInfo != null && !estateInfo.isEmpty()) {
                    //封面图
                    JSONArray lurcarray = new JSONArray();
                    JSONObject lurcobj = new JSONObject();
                    String Cover_planCliengGuid = estateInfo.getCover_plan();
                    List<FrameAttachInfo> frameAttachInfos = attachService.getAttachInfoListByGuid(
                            Cover_planCliengGuid);
                    if(frameAttachInfos!=null&&!frameAttachInfos.isEmpty()) {

                        for (FrameAttachInfo infoDetail : frameAttachInfos) {
                            if (infoDetail != null && !infoDetail.isEmpty()) {
                                lurcarray.add("http://jizwfw.sd.gov.cn/jnzwdt/rest/frame/base/attach/attachAction/getContent?isCommondto=true&attachGuid="
                                        + infoDetail.getAttachGuid());

                            }
                        }
                    }
                    lurcobj.put("name", "封面图");
                    lurcobj.put("value", lurcarray);

                    //总平面图
                    JSONArray lurcarray1 = new JSONArray();
                    JSONObject zpmobj = new JSONObject();
                    String Site_planCliengGuid = estateInfo.getSite_plan();
                    List<FrameAttachInfo> frameAttachInfos1 = attachService.getAttachInfoListByGuid(
                            Site_planCliengGuid);
                    if(frameAttachInfos1!=null&&!frameAttachInfos1.isEmpty()) {

                        for (FrameAttachInfo infoDetail : frameAttachInfos1) {
                            if (infoDetail != null && !infoDetail.isEmpty()) {
                                lurcarray1.add("http://jizwfw.sd.gov.cn/jnzwdt/rest/frame/base/attach/attachAction/getContent?isCommondto=true&attachGuid="
                                        + infoDetail.getAttachGuid());

                            }
                        }
                    }
                    zpmobj.put("name", "封面图");
                    zpmobj.put("value", lurcarray1);

                    //鸟瞰图
                    JSONArray lurcarray2 = new JSONArray();
                    JSONObject nkobj = new JSONObject();
                    String Aerial_viewCliengGuid = estateInfo.getAerial_view();
                    List<FrameAttachInfo> frameAttachInfos2 = attachService.getAttachInfoListByGuid(
                            Aerial_viewCliengGuid);
                    if(frameAttachInfos2!=null&&!frameAttachInfos2.isEmpty()) {

                        for (FrameAttachInfo infoDetail : frameAttachInfos2) {
                            if (infoDetail != null && !infoDetail.isEmpty()) {
                                lurcarray2.add("http://jizwfw.sd.gov.cn/jnzwdt/rest/frame/base/attach/attachAction/getContent?isCommondto=true&attachGuid="
                                        + infoDetail.getAttachGuid());

                            }
                        }
                    }
                    nkobj.put("name", "鸟瞰图");
                    nkobj.put("value", lurcarray2);

                    //其他图
                    JSONArray lurcarray3 = new JSONArray();
                    JSONObject otherobj = new JSONObject();
                    String Other_picCliengGuid = estateInfo.getOther_pic();
                    List<FrameAttachInfo> frameAttachInfos3 = attachService.getAttachInfoListByGuid(
                            Other_picCliengGuid);
                    if(frameAttachInfos3!=null&&!frameAttachInfos3.isEmpty()) {

                        for (FrameAttachInfo infoDetail : frameAttachInfos3) {
                            if (infoDetail != null && !infoDetail.isEmpty()) {
                                lurcarray3.add("http://jizwfw.sd.gov.cn/jnzwdt/rest/frame/base/attach/attachAction/getContent?isCommondto=true&attachGuid="
                                        + infoDetail.getAttachGuid());

                            }
                        }
                    }
                    otherobj.put("name", "其他图");
                    otherobj.put("value", lurcarray3);


                    permitData.add(lurcobj);
                    permitData.add(zpmobj);
                    permitData.add(nkobj);
                    permitData.add(otherobj);

                    custom.put("permitData", permitData);
                }
                else {
                    return generateReturnInfo("0", "未查询到相关信息", custom);
                }
            }
            return generateReturnInfo("0", "未查询到相关信息", custom);

        }
        else {
            return generateReturnInfo("0", "参数验证失败", custom);
        }

    }

    /**
     * 根据附件信息返回预览地址
     * @param attachInfo
     * @param request
     * @return
     */
    private String getPreviewUrl(FrameAttachInfo attachInfo,HttpServletRequest request){
        // 365附件预览地址
        String url = ConfigUtil.getConfigValue("officeweb365url");

        String attachguid = attachInfo.getAttachGuid();
        String documentType = attachInfo.getContentType();
        String fname = attachguid + documentType;
        String baseUrl = "http://59.206.96.143:25990/jnzwdt/rest/frame/base/attach/attachAction/getContent?isCommondto=true&attachGuid="
                + attachguid;
        String baseurlency = OfficeWebUrlEncryptUtil.getEncryptUrl(baseUrl, documentType);
        if (baseUrl.contains("https")) {
            baseurlency += "&ssl=1";
        }
        String purl = url + "fname=" + fname + "&" + baseurlency;
        log.info("purl:"+purl);
        return purl;
    }
    /**
     * 获取楼盘楼层图纸
     * @param params
     * @return
     */
    @RequestMapping(value = "/getfloorplanlist", method = RequestMethod.POST)
    public String getfloorplanlist(@RequestBody String params, @Context HttpServletRequest request) {
        log.info("==开始调用getfloorplanlist接口===");
        log.info("getfloorplanlist params:" + params);
        JSONObject param = JSON.parseObject(params);
        JSONObject custom = new JSONObject();
        String token = param.getString("token");
        if (ZwdtConstant.SysValidateData.equals(token)) {
            JSONObject paramJson = JSON.parseObject(param.getString("params"));
            String reGuid = paramJson.getString("reGuid");

            if (StringUtil.isBlank(reGuid)) {
                return generateReturnInfo("0", "楼盘标识不能为空！", custom);
            }

            RealEstateInfo estateInfo = iRealEstateInfoService.find(reGuid);

            if (estateInfo == null) {
                return generateReturnInfo("0", "未查询到该楼盘信息！", custom);
            }


            HashMap<String, HashMap<String, JSONArray>> dataMap = new HashMap<>();

            String firstfloor_pic = estateInfo.getStr("firstfloor_pic");
            String standfloor_pic = estateInfo.getStr("standfloor_pic");
            String topfloor_pic = estateInfo.getStr("topfloor_pic");

            /*查找对应附件*/
            if(StringUtil.isNotBlank(firstfloor_pic)){
                List<FrameAttachInfo> attachInfoList = attachService.getAttachInfoListByGuid(firstfloor_pic);
                if (attachInfoList != null && !attachInfoList.isEmpty()){
                    JSONArray pdfList = new JSONArray();
                    JSONArray cadList = new JSONArray();
                    setDataList(pdfList,cadList,attachInfoList,request);
                    HashMap<String, JSONArray> data = new HashMap<>();
                    data.put("pdflist",pdfList);
                    data.put("cadlist",cadList);
                    dataMap.put("firstfloorplan",data);
                }
            }
            if(StringUtil.isNotBlank(standfloor_pic)){
                List<FrameAttachInfo> attachInfoList = attachService.getAttachInfoListByGuid(standfloor_pic);
                if (attachInfoList != null && !attachInfoList.isEmpty()){
                    JSONArray pdfList = new JSONArray();
                    JSONArray cadList = new JSONArray();
                    setDataList(pdfList,cadList,attachInfoList,request);
                    HashMap<String, JSONArray> data = new HashMap<>();
                    data.put("pdflist",pdfList);
                    data.put("cadlist",cadList);
                    dataMap.put("standfloorplan",data);
                }
            }
            if(StringUtil.isNotBlank(topfloor_pic)){
                List<FrameAttachInfo> attachInfoList = attachService.getAttachInfoListByGuid(topfloor_pic);
                if (attachInfoList != null && !attachInfoList.isEmpty()){
                    JSONArray pdfList = new JSONArray();
                    JSONArray cadList = new JSONArray();
                    setDataList(pdfList,cadList,attachInfoList,request);
                    HashMap<String, JSONArray> data = new HashMap<>();
                    data.put("pdflist",pdfList);
                    data.put("cadlist",cadList);
                    dataMap.put("topfloorplan",data);
                }
            }

            String backData = JSONObject.toJSONString(dataMap);
            return generateReturnInfo("1", "调用成功", JSONObject.parseObject(backData));
        }
        else {
            return generateReturnInfo("0", "参数验证失败", custom);
        }
    }



    private void setDataList(JSONArray pdfList,JSONArray cadList,List<FrameAttachInfo> attachInfoList,HttpServletRequest request){
        // 365附件预览地址
        String url = ConfigUtil.getConfigValue("officeweb365url");
        for (FrameAttachInfo attachInfo : attachInfoList) {
            String attachguid = attachInfo.getAttachGuid();
            String documentType = attachInfo.getContentType();
            String fname = attachguid + documentType;
            String baseUrl = "http://59.206.96.143:25990/jnzwdt/rest/frame/base/attach/attachAction/getContent?isCommondto=true&attachGuid="
                    + attachguid;
            String baseurlency = OfficeWebUrlEncryptUtil.getEncryptUrl(baseUrl, documentType);
            if (baseUrl.contains("https")) {
                baseurlency += "&ssl=1";
            }
            String purl = url + "fname=" + fname + "&" + baseurlency;
            JSONObject node = new JSONObject();
            node.put("attachname",attachInfo.getAttachFileName());
            node.put("attachurl",WebUtil.getRequestCompleteUrl(request)
                    + "/rest/frame/base/attach/attachAction/getContent?isCommondto=true&attachGuid="
                    + attachguid);
            node.put("attachguid",attachguid);
            node.put("attachsize",attachInfo.getAttachLength());
            node.put("attachtype",documentType);
            node.put("previewurl",purl);
            if(documentType.endsWith("pdf")){
                pdfList.add(node);
            }
            if(documentType.endsWith("dwg") || documentType.endsWith("dxf") || documentType.endsWith("dwt")){
                cadList.add(node);
            }
        }
    }

    private String generateReturnInfo(String code, String text, JSONObject custom) {
        JSONObject result = new JSONObject();
        JSONObject status = new JSONObject();
        status.put("code", "200");
        status.put("text", "");
        custom.put("code", code);
        custom.put("text", text);
        result.put("status", status);
        result.put("custom", custom);
        return result.toJSONString();
    }

    /**
     * 获取开发商(企业)失信惩戒信息接口
     *
     * @param params
     * @param request
     * @return
     */
    @RequestMapping(value = "/getDevelopersLossSincerityInfo", method = RequestMethod.POST)
    public String getDevelopersLossSincerityInfo(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getDevelopersLossSincerityInfo接口=======");
            JSONObject paramsObject = JSONObject.parseObject(params);
            JSONObject paramObject = paramsObject.getJSONObject("params");
            if (paramObject == null) {
                return JsonUtils.zwdtRestReturn(ZwfwConstant.CONSTANT_STR_ZERO, "请求参数错误!", "");
            }
            String reGuid = paramObject.getString("reGuid");
            String developersOrgCode = paramObject.getString("developers_orgcode");

            // 返回数据json
            JSONObject returnJson = new JSONObject();

            if (StringUtil.isBlank(developersOrgCode) && StringUtil.isBlank(reGuid)) {
                return JsonUtils.zwdtRestReturn(ZwfwConstant.CONSTANT_STR_ZERO, "reGuid和developers_orgcode参数不可都为空!", "");
            } else {
                // reGuid不为空,查询real_estate_info获取统一社会信用代码
                if (StringUtil.isBlank(developersOrgCode)) {
                    // 楼盘信息实体
                    RealEstateInfo realEstateInfo = iRealEstateInfoService.find(reGuid);
                    if (realEstateInfo == null || StringUtil.isBlank(realEstateInfo.getStr("developers_orgcode"))) {
                        return JsonUtils.zwdtRestReturn(ZwfwConstant.CONSTANT_STR_ZERO, "统一社会信用代码未维护!", "");
                    }
                    developersOrgCode = realEstateInfo.getStr("developers_orgcode");
                }
            }

            // 失信企业
            JSONObject param = new JSONObject();
            param.put("apikey", "596738138009763840");
            param.put("apiname", "api/1/jnfg_hmd");
            param.put("appkey", "4207f3e6fb0d4488834a88fddbab2475");
            param.put("appsecert", "NDIwN2YzZTZmYjBkNDQ4ODgzNGE4OGZkZGJhYjI0NzU");
            JSONObject paramObj = new JSONObject();
            paramObj.put("creditCode", developersOrgCode);
            paramObj.put("flag", "2");
            param.put("param", paramObj);

            // 调用一体机已有接口
            String result = HttpUtil.doHttp("http://112.6.110.176:8080/jnzwfwznsb/rest/companycredit/getUrlBack", param.toJSONString(), "post", 2);
            log.info("调用一体机已有接口http://112.6.110.176:8080/jnzwfwznsb/rest/companycredit/getUrlBack入参: " + param + ",返回结果:" + result);

            JSONObject resultObj = JSONObject.parseObject(result);
            // 返回码(0为成功，其他为失败)
            if (resultObj != null && resultObj.containsKey("custom")
                    && resultObj.getJSONObject("custom").containsKey("data")
                    && "0".equals(resultObj.getJSONObject("custom").getJSONObject("data").getString("code"))) {
                JSONArray hhbList = resultObj.getJSONArray("hhbList");
                returnJson.put("data", hhbList == null ? new ArrayList<>() : hhbList);
                return JsonUtils.zwdtRestReturn(ZwfwConstant.CONSTANT_STR_ONE, "获取开发商(企业)失信惩戒信息成功！", returnJson.toString());
            }
            return JsonUtils.zwdtRestReturn(ZwfwConstant.CONSTANT_STR_ZERO, "获取开发商(企业)失信惩戒信息失败!", "");
        } catch (Exception e) {
            log.error("=======getDevelopersLossSincerityInfo接口参数：params【" + params + "】=======");
            log.error("=======getDevelopersLossSincerityInfo异常信息：" + e.getMessage() + "=======", e);
            return JsonUtils.zwdtRestReturn(ZwfwConstant.CONSTANT_STR_ZERO, "获取开发商(企业)失信惩戒信息失败：" + e.getMessage(), "");
        }
    }

    /**
     * 获取开发商(企业)行政处罚信息接口
     *
     * @param params
     * @param request
     * @return
     */
    @RequestMapping(value = "/getDevelopersPunishInfo", method = RequestMethod.POST)
    public String getDevelopersPunishInfo(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getDevelopersPunishInfo接口=======");
            JSONObject paramsObject = JSONObject.parseObject(params);
            JSONObject paramObject = paramsObject.getJSONObject("params");
            if (paramObject == null) {
                return JsonUtils.zwdtRestReturn(ZwfwConstant.CONSTANT_STR_ZERO, "请求参数错误!", "");
            }
            String reGuid = paramObject.getString("reGuid");
            String developersOrgCode = paramObject.getString("developers_orgcode");

            // 返回数据json
            JSONObject returnJson = new JSONObject();
            if (StringUtil.isBlank(developersOrgCode) && StringUtil.isBlank(reGuid)) {
                return JsonUtils.zwdtRestReturn(ZwfwConstant.CONSTANT_STR_ZERO, "reGuid和developers_orgcode参数不可都为空!", "");
            } else {
                // reGuid不为空,查询real_estate_info获取统一社会信用代码
                if (StringUtil.isBlank(developersOrgCode)) {
                    // 楼盘信息实体
                    RealEstateInfo realEstateInfo = iRealEstateInfoService.find(reGuid);
                    if (realEstateInfo == null || StringUtil.isBlank(realEstateInfo.getStr("developers_orgcode"))) {
                        return JsonUtils.zwdtRestReturn(ZwfwConstant.CONSTANT_STR_ZERO, "统一社会信用代码未维护!", "");
                    }
                    developersOrgCode = realEstateInfo.getStr("developers_orgcode");
                }
            }

            // 行政处罚
            JSONObject param = new JSONObject();
            param.put("apikey", "824689388364496896");
            param.put("apiname", "api/1/jnsfgw_cf1");
            param.put("appkey", "4207f3e6fb0d4488834a88fddbab2475");
            param.put("appsecert", "NDIwN2YzZTZmYjBkNDQ4ODgzNGE4OGZkZGJhYjI0NzU");
            JSONObject paramObj = new JSONObject();
            paramObj.put("codeNumber", developersOrgCode);
            paramObj.put("flag", "2");
            param.put("param", paramObj);

            // 调用一体机已有接口
            String result = HttpUtil.doHttp("http://112.6.110.176:28080/jnzwfwznsb/rest/companycredit/getUrlBack", param.toJSONString(), "post", 2);
            log.info("调用一体机已有接口http://112.6.110.176:28080/jnzwfwznsb/rest/companycredit/getUrlBack入参: " + param + ",返回结果:" + result);

            JSONObject resultObj = JSONObject.parseObject(result);
            // 返回码(0为成功，其他为失败)
            if (resultObj != null && resultObj.containsKey("custom")
                    && resultObj.getJSONObject("custom").containsKey("data")
                    && "0".equals(resultObj.getJSONObject("custom").getJSONObject("data").getString("code"))) {
                JSONArray penaltyList = resultObj.getJSONArray("penaltyList");
                returnJson.put("data", penaltyList == null ? new ArrayList<>() : penaltyList);
                return JsonUtils.zwdtRestReturn(ZwfwConstant.CONSTANT_STR_ONE, "获取开发商(企业)行政处罚信息成功！", returnJson.toString());
            }

            return JsonUtils.zwdtRestReturn(ZwfwConstant.CONSTANT_STR_ZERO, "获取开发商(企业)行政处罚信息失败!", "");
        } catch (Exception e) {
            log.error("=======getDevelopersPunishInfo接口参数：params【" + params + "】=======");
            log.error("=======getDevelopersPunishInfo异常信息：" + e.getMessage() + "=======", e);
            return JsonUtils.zwdtRestReturn(ZwfwConstant.CONSTANT_STR_ZERO, "获取开发商(企业)行政处罚信息失败：" + e.getMessage(), "");
        }
    }

}
