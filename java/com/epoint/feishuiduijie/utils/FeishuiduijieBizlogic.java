package com.epoint.feishuiduijie.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.auditorga.auditarea.domain.AuditOrgaArea;
import com.epoint.basic.auditorga.auditarea.inter.IAuditOrgaArea;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditprojectoperation.domain.AuditProjectOperation;
import com.epoint.basic.auditproject.auditprojectoperation.inter.IAuditProjectOperation;
import com.epoint.basic.auditresource.auditrsitembaseinfo.domain.AuditRsItemBaseinfo;
import com.epoint.basic.auditresource.auditrsitembaseinfo.inter.IAuditRsItemBaseinfo;
import com.epoint.basic.auditsp.auditspisubapp.domain.AuditSpISubapp;
import com.epoint.basic.auditsp.auditspisubapp.inter.IAuditSpISubapp;
import com.epoint.basic.auditsp.auditspphase.domain.AuditSpPhase;
import com.epoint.basic.auditsp.auditspphase.inter.IAuditSpPhase;
import com.epoint.basic.auditsp.auditspspgcjsxk.domain.AuditSpSpGcjsxk;
import com.epoint.basic.auditsp.auditspspgcjsxk.inter.IAuditSpSpGcjsxkService;
import com.epoint.basic.auditsp.auditspspjgys.domain.AuditSpSpJgys;
import com.epoint.basic.auditsp.auditspspjgys.inter.IAuditSpSpJgysService;
import com.epoint.basic.auditsp.auditspsplxydghxk.domain.AuditSpSpLxydghxk;
import com.epoint.basic.auditsp.auditspsplxydghxk.inter.IAuditSpSpLxydghxkService;
import com.epoint.basic.auditsp.auditspspsgxk.domain.AuditSpSpSgxk;
import com.epoint.basic.auditsp.auditspspsgxk.inter.IAuditSpSpSgxkService;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.audittask.extension.domain.AuditTaskExtension;
import com.epoint.basic.audittask.extension.inter.IAuditTaskExtension;
import com.epoint.cert.basic.certcatalog.certmetadata.domain.CertMetadata;
import com.epoint.cert.basic.certinfo.certinfoextension.domain.CertInfoExtension;
import com.epoint.cert.basic.certinfo.domain.CertInfo;
import com.epoint.cert.external.ICertConfigExternal;
import com.epoint.cert.external.ICertInfoExternal;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ValidateUtil;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.collection.EpointCollectionUtils;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.httpclient.HttpUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.jdbc.connection.DataSourceConfig;
import com.epoint.database.util.MongodbUtil;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.frame.service.metadata.code.entity.CodeItems;
import com.epoint.frame.service.organ.user.api.IUserService;
import com.epoint.frame.service.organ.user.entity.FrameUser;
import org.apache.log4j.Logger;

import java.lang.invoke.MethodHandles;
import java.util.*;
import java.util.stream.Collectors;

public class FeishuiduijieBizlogic {
    /**
     * 日志
     */
    private static Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());

    // auditTask.getItem_id()
    public static void startFeiShui(AuditProject auditProject) {
        IAuditSpISubapp iAuditSpISubapp = ContainerFactory.getContainInfo()
                .getComponent(IAuditSpISubapp.class);
        IAuditRsItemBaseinfo iAuditRsItemBaseinfo = ContainerFactory.getContainInfo().getComponent(IAuditRsItemBaseinfo.class);
        IAuditProjectOperation iAuditProjectOperation = ContainerFactory.getContainInfo().getComponent(IAuditProjectOperation.class);
        ICertInfoExternal iCertInfoExternal = ContainerFactory.getContainInfo().getComponent(ICertInfoExternal.class);
        ICertConfigExternal iCertConfigExternal = ContainerFactory.getContainInfo().getComponent(ICertConfigExternal.class);
        DataSourceConfig dsc = new DataSourceConfig(ConfigUtil.getConfigValue("MongodbUrl"),
                ConfigUtil.getConfigValue("MongodbUserName"), ConfigUtil.getConfigValue("MongodbPassword"));
        MongodbUtil mongodbUtil = new MongodbUtil(dsc.getServerName(), dsc.getDbName(), dsc.getUsername(),
                dsc.getPassword());
        IAuditTask iAuditTask = ContainerFactory.getContainInfo().getComponent(IAuditTask.class);
        IAuditTaskExtension iAuditTaskExtension = ContainerFactory.getContainInfo().getComponent(IAuditTaskExtension.class);

        if(auditProject == null){
            log.info("=====非税推送失败=======auditProject为空");
            return;
        }
        AuditTask auditTask = iAuditTask.selectTaskByRowGuid(auditProject.getTaskguid()).getResult();
        if(auditTask == null){
            log.info("=====非税推送失败=======auditTask为空 auditProject.getTaskguid()："+auditProject.getTaskguid());
        }
        AuditTaskExtension auditTaskExtension = iAuditTaskExtension.getTaskExtensionByTaskGuid(auditTask.getRowguid(), false).getResult();
        if(auditTaskExtension == null){
            log.info("=====非税推送失败=======auditTaskExtension为空 auditTask.getRowguid()："+auditTask.getRowguid());
        }
        //水保费涉及事项(多个以英文分号;分割)
        String shuibaofei_item_id = ConfigUtil.getConfigValue("feishuiduijie","shuibaofei_item_id");
        log.info("=====非税推送=======shuibaofei_item_id："+shuibaofei_item_id);
        //防空费涉及事项(多个以英文分号;分割)
        String fangkongfei_item_id = ConfigUtil.getConfigValue("feishuiduijie","fangkongfei_item_id");
        log.info("=====非税推送=======fangkongfei_item_id："+fangkongfei_item_id);
        List<String> feishuiItemIdList = new ArrayList<>();
        List<String> shuibaofeiitemidlist = new ArrayList<>();
        List<String> fangkongfeiitemidlist = new ArrayList<>();
        if(StringUtil.isNotBlank(shuibaofei_item_id)){
            shuibaofeiitemidlist = Arrays.stream(shuibaofei_item_id.split(";")).collect(Collectors.toList());
            feishuiItemIdList.addAll(shuibaofeiitemidlist);
        }
        if(StringUtil.isNotBlank(fangkongfeiitemidlist)){
            fangkongfeiitemidlist = Arrays.stream(fangkongfei_item_id.split(";")).collect(Collectors.toList());
            feishuiItemIdList.addAll(fangkongfeiitemidlist);
        }
        if(EpointCollectionUtils.isEmpty(feishuiItemIdList)){
            log.info("=====非税推送失败=======feishuiItemIdList为空 请检查是否配置推送事项编码");
            return;
        }
        if(!feishuiItemIdList.contains(auditTask.getItem_id())){
            log.info("=====非税推送失败=======auditTask.getItem_id()："+auditTask.getItem_id()+" 不在配置的推送事项编码中");
            return;
        }

        log.info("=====开始执行非税推送======="+ auditProject.getRowguid());
        //项目信息：根据当前办件audit_project实例数据获取subappguid（子申报标识）
        String subappguid = auditProject.getSubappguid();
        if(StringUtil.isBlank(subappguid)){
            log.info("=====非税推送失败=======subappguid为空 auditProject.getRowguid()："+auditProject.getRowguid());
            return;
        }
        // 根据subappguid查询audit_sp_i_subapp表数据获取yewuguid字段
        AuditSpISubapp auditSpISubapp = iAuditSpISubapp.getSubappByGuid(subappguid).getResult();
        if (auditSpISubapp == null) {
            log.info("=====非税推送失败=======auditSpISubapp为空 subappguid:"+subappguid);
            return;
        }
        String yewuguid = auditSpISubapp.getYewuguid();
        if(StringUtil.isBlank(yewuguid)){
            log.info("=====非税推送失败=======yewuguid为空");
            return;
        }
        // 根据yewuguid对应audit_rs_item_baseinfo表数据
        AuditRsItemBaseinfo rsitem = iAuditRsItemBaseinfo.getAuditRsItemBaseinfoByRowguid(yewuguid).getResult();
        if (rsitem == null) {
            log.info("=====非税推送失败=======AuditRsItemBaseinfo为空 yewuguid:"+yewuguid);
            return;
        }
        // 根据audit_rs_item_baseinfo表itemcode字段按照规则‘-’分割，获取第二位，如“111-320582-xxxx”,第二位应该是320582（辖区），
        // 判断辖区的值，若辖区为：经开区370892和高新区370890，则数据不推送非税平台，结束后续所有逻辑，反之继续。
        String itemcode = rsitem.getItemcode();
        if(StringUtil.isBlank(itemcode)){
            log.info("=====非税推送失败=======itemcode为空");
            return;
        }
        String[] itemcodeArr = itemcode.split("-");
        if(itemcodeArr.length < 3){
            log.info("=====非税推送失败=======itemcodeArr.length<3 itemcode："+itemcode);
            return;
        }
        String areaCode = itemcodeArr[1];
        if(StringUtil.isBlank(areaCode)){
            log.info("=====非税推送失败=======areaCode为空 itemcode："+itemcode);
            return;
        }
        if(areaCode.equals("370892") || areaCode.equals("370890")){
            log.info("=====非税推送失败=======areaCode为370892或370890 itemcode："+itemcode);
            return;
        }

        //2、办件信息：根据audit_project实例的rowguid对应audit_project_operation表projectguid字段，operatetype=60的数据。
        String projectguid = auditProject.getRowguid();
        if(StringUtil.isBlank(projectguid)){
            log.info("=====非税推送失败=======projectguid为空");
            return;
        }
        SqlConditionUtil sql = new SqlConditionUtil();
        sql.eq("projectguid", auditProject.getRowguid());
        sql.eq("operatetype", "60");
        AuditProjectOperation auditProjectOperation = iAuditProjectOperation
                .getAuditOperationByCondition(sql.getMap()).getResult();
        if (auditProjectOperation == null) {
            log.info("=====非税推送失败=======auditProjectOperation为空 projectguid："+projectguid);
            return;
        }

        // 3、照面信息：获取办件audit_project实例的certrowguid字段，对应cert_info表的rowguid字段，按照下述示例代码获取相关照面信息（照面信息应该是存在mongodb库的）
        log.info("=====开始获取电子证照数据=======");
        CertInfoExtension dataBean = new CertInfoExtension();
        String certrowguid = auditProject.getCertrowguid();
        if(StringUtil.isNotBlank(certrowguid)){
            CertInfo certinfo = iCertInfoExternal.getCertInfoByRowguid(certrowguid);
            if (certinfo != null) {
                // 获得元数据配置表所有顶层节点
                List<CertMetadata> metadataList = iCertConfigExternal.selectMetadataByIdAndVersion(certinfo.getCertcatalogid(),
                        String.valueOf(certinfo.getCertcatalogversion()), areaCode);
                // 获得照面信息
                Map<String, Object> filter = new HashMap<>(16);
                // 设置基本信息guid
                filter.put("certinfoguid", certrowguid);
                dataBean = mongodbUtil.find(CertInfoExtension.class, filter, false);
                // 日期格式转换
                convertDate(metadataList, dataBean);
            }
        }
        log.info("=====获取电子证照数据结束=======dataBean:"+dataBean);
        JSONObject formInfo = new JSONObject();
        //4、业务表单数据：获取当前办件事项的表单id字段，再根据subappguid标识对应电子表单实例主键rowguid，调用电子表单接口【/sform/getPageData】获取表单实例数据。
        String formid = auditTaskExtension.get("formid");
        log.info("=====开始获取电子表单数据======= formid:"+formid);
        if(formid != null){
            formInfo = getEformPageData(auditSpISubapp.getRowguid(), formid);
        }
        log.info("=====获取电子表单数据成功=======formInfo:"+formInfo);
        if(shuibaofeiitemidlist.contains(auditTask.getItem_id())){
            log.info("=====非税推送业务项为水保费项目，开始构建推送的参数=======Item_id："+auditTask.getItem_id());
            JSONObject data = buildPushParamshuibaofei(rsitem,areaCode,dataBean,auditSpISubapp,auditProjectOperation,formInfo);
            log.info("=====开始请求非税接口=======");
            JSONObject result = FeishuiduijieUtil.sbfkfydata("sbfSave",areaCode,data);
            log.info("=====非税接口返回结果："+result);

        }
        else if(fangkongfeiitemidlist.contains(auditTask.getItem_id())){
            log.info("=====非税推送业务项为防空费项目，开始构建推送的参数=======Item_id："+auditTask.getItem_id());
            JSONObject data = buildPushParamsfangkongfei(rsitem,areaCode,dataBean,auditSpISubapp,auditProjectOperation,formInfo);
            JSONObject result = FeishuiduijieUtil.sbfkfydata("fkfSave",areaCode,data);
            log.info("=====非税接口返回结果："+result);
        }

    }

    // 构建推送的参数-水保费
    private static JSONObject buildPushParamshuibaofei(AuditRsItemBaseinfo auditRsItemBaseinfo,String areacode,CertInfoExtension dataBean,
                                             AuditSpISubapp auditSpISubapp,AuditProjectOperation auditProjectOperation,JSONObject formInfo){
        log.info("=====开始构建推送的参数=======areacode:"+areacode);
        IAuditOrgaArea iAuditOrgaArea = ContainerFactory.getContainInfo().getComponent(IAuditOrgaArea.class);
        IUserService iUserService = ContainerFactory.getContainInfo().getComponent(IUserService.class);
        ICodeItemsService iCodeItemsService = ContainerFactory.getContainInfo().getComponent(ICodeItemsService.class);

        JSONObject param = new JSONObject();
        // 项目id
        //param.put("xmid", auditRsItemBaseinfo.getItemcode());
        // 项目名称
        param.put("xmmc", auditRsItemBaseinfo.getItemname());
        // 项目审批所属级别代码 根据itemcode按照规则‘-’分割，获取第二位辖区，按照辖区判断，若辖区为370800活着的370891，则为2（市），其余均为3（区县）。
        String xmspssjb_dm = "";
        if(areacode.equals("370800") || areacode.equals("370891")){
            xmspssjb_dm = "2";
        }else{
            xmspssjb_dm = "3";
        }
        param.put("xmspssjb_dm", xmspssjb_dm);
        // 项目审批所属级别名称 若xmspssjb_dm=2则为市，若xmspssjb_dm=3则为区县
        param.put("xmspssjbmc", xmspssjb_dm.equals("2")?"市":"区县");
        // 批复文号 获取照面信息的pfwh字段的值
        String pfwh = dataBean.getStr("pfwh");
        param.put("pfwh", pfwh);
        // 项目所在地省代码 xmszdsh 固定值 山东省370000
        param.put("xmszdsh", "370000");
        // 项目所在地省名称 xmszdshmc 固定值 山东省
        param.put("xmszdshmc", "山东省");
        // 项目所在地地市代码	xmszdds	固定值	370800
        param.put("xmszdds", "370800");
        // 项目所在地地市名称	xmszddsmc	固定值	济宁市
        param.put("xmszddsmc", "济宁市");
        // 	项目所在地县区代码	xmszdxq	规则	根据itemcode按照规则‘-’分割，获取第二位辖区
        param.put("xmszdxq", areacode);
        // 项目所在地县区名称	xmszdxqmc	规则	根据itemcode按照规则‘-’分割，获取第二位辖区，转换成中文
        AuditOrgaArea auditOrgaArea = iAuditOrgaArea
                .getAreaByAreacode(areacode).getResult();
        String areaName = "";
        if (auditOrgaArea != null) {
            areaName = auditOrgaArea.getXiaquname();
        }
        param.put("xmszdxqmc", areaName);
        // 项目所在地 xmszd 规则
        param.put("xmszd", getItemAddress(auditSpISubapp));
        // 审批联系人 splxr audit_project_operation表 operateusername
        param.put("splxr", auditProjectOperation.getOperateusername());
        // 审批联系电话 splxdh frame_user表 根据audit_project_operation表operateuserguid字段获取frame_user表的phone
        log.info("=====开始获取splxdh======= auditProjectOperation.getOperateUserGuid():"+auditProjectOperation.getOperateUserGuid());
        FrameUser frameUser = iUserService.getUserByUserField("UserGuid", auditProjectOperation.getOperateUserGuid());
        log.info("=====获取splxdh成功=======frameUser:"+frameUser);
        if(frameUser != null){
            param.put("splxdh", frameUser.getMobile());
        }
        // 缴费人统一社会信用代码 tyshxydm audit_rs_item_base表 ITEMLEGALCREDITCODE字段
        param.put("tyshxydm", auditRsItemBaseinfo.getItemlegalcreditcode());
        // 缴费人类型代码 jfrlxdm 固定值 2
        param.put("jfrlxdm", "2");
        // 缴费人类型名称 jfrlxmc 固定值 自然人
        param.put("jfrlxmc", "自然人");
        // 缴费人名称	jfrmc	audit_rs_item_base表	legalperson
        param.put("jfrmc", auditRsItemBaseinfo.getLegalperson());
        // 缴费方联系人	jfflxr	audit_rs_item_base表	CONTRACTPERSON
        param.put("jfflxr", auditRsItemBaseinfo.getContractperson());
        // 缴费方联系电话	jfflxdh	audit_rs_item_base表	CONTRACTPHONE
        param.put("jfflxdh", auditRsItemBaseinfo.getContractphone());
        // 是否有批复文号	sfypfwh	规则	第五个字段pfwh是否有值，有值则有1，反之没有0
        param.put("sfypfwh", StringUtil.isBlank(pfwh)?"0":"1");
        // 预计缴费时间	jfsj	表单信息	第四个查询业务表单数据的表单字段信息
        param.put("jfsj", formInfo.getString("jfsj"));
        // 用户id	yhid	固定值	285BC282B6734B9DE063A9640C4C8147
        param.put("yhid", "285BC282B6734B9DE063A9640C4C8147");
        // 录入人id	lrrid	固定值	285BC282B6734B9DE063A9640C4C8147
        param.put("lrrid", "285BC282B6734B9DE063A9640C4C8147");
        // 数据来源	sjly	固定值	2
        param.put("sjly", "2");
        // 接口类型	status	固定值	add
        param.put("status", "add");
        // Json对象hdxx相关字段
        JSONArray hdxxJsonArry = new JSONArray();
        JSONObject hdxxJsonObj = new JSONObject();
        // 主管税务局代码	zgswj_dm	规则	根据itemcode按照规则‘-’分割，获取第二位辖区编码，获取代码项【主管税务机关】，根据辖区对应拓展属性值1找到对应的代码项值
        String zgswj_dm = getItemValueByBz1(areacode,"主管税务机关");
        //String zgswj_dm = "13700000000";
        log.info("=====zgswj_dm:"+zgswj_dm);
        hdxxJsonObj.put("zgswj_dm", zgswj_dm);
        // 主管税务局名称	zgswjmc	规则	根据zgswj_dm获取代码项文本
        if(StringUtil.isNotBlank(zgswj_dm)){
            String zgswjmc = iCodeItemsService.getItemTextByCodeName("主管税务机关", zgswj_dm);
            //String zgswjmc = "国家税务总局山东省税务局";
            hdxxJsonObj.put("zgswjmc", zgswjmc);
        }
        // 征收品目代码	zspm_dm	固定	301760102
        hdxxJsonObj.put("zspm_dm", "301760102");
        // 征收品目名称	zspmmc	固定	水土保持补偿费收入-建设期收入
        hdxxJsonObj.put("zspmmc", "水土保持补偿费收入-建设期收入");
        // 缴费期限代码	jfqx_dm	固定	11
        hdxxJsonObj.put("jfqx_dm", "11");
        // 缴费期限名称	jfqxmc	固定	次
        hdxxJsonObj.put("jfqxmc", "次");
        //预算分配比例代码	ysfpbl_dm	规则	获取代码项【水保费预算分配比例】，根据zgswj_dm主管税务局代码对应拓展属性值1找到对应的代码项值
        /*String ysfpbl_dm = getItemValueByBz1(zgswj_dm, "水保费预算分配比例");
        hdxxJsonObj.put("ysfpbl_dm", ysfpbl_dm);
        //预算分配比例名称	ysfpblmc	规则	根据ysfpbl_dm获取代码项文本
        if(StringUtil.isNotBlank(ysfpbl_dm)){
            String ysfpblmc = iCodeItemsService.getItemTextByCodeName("水保费预算分配比例", ysfpbl_dm);
            hdxxJsonObj.put("ysfpblmc", ysfpblmc);
        }*/
        /*hdxxJsonObj.put("ysfpbl_dm", "13711030");
        hdxxJsonObj.put("ysfpblmc", "中央10%县区90%");*/
        if("2".equals(xmspssjb_dm)){
            hdxxJsonObj.put("ysfpbl_dm", "13711029");
            hdxxJsonObj.put("ysfpblmc", "中央10%地市90%");
        }
        else if ("3".equals(xmspssjb_dm)){
            hdxxJsonObj.put("ysfpbl_dm", "13711030");
            hdxxJsonObj.put("ysfpblmc", "中央10%县区90%");
        }
        //国库代码	gkdm	规则	获取代码项【国库】，根据zgswj_dm主管税务局代码对应拓展属性值1找到对应的代码项值
        String gkdm = getItemValueByBz1(zgswj_dm, "国库");
        hdxxJsonObj.put("gkdm", gkdm);
        //国库名称	gkmc	规则	根据gkdm获取代码项文本
        if(StringUtil.isNotBlank(gkdm)){
            String gkmc = iCodeItemsService.getItemTextByCodeName("国库", gkdm);
            hdxxJsonObj.put("gkmc", gkmc);
        }
        // 复核后批复金额（含滞纳金)	fhhpfje	照面信息	获取照面信息的fhhpfje字段的值
        String fhhpfje = dataBean.getStr("fhhpfje");
        hdxxJsonObj.put("fhhpfje", fhhpfje);
        // 实核定缴费金额（含滞纳金）	shdjfje	照面信息	获取照面信息的shdjfje字段的值
        String shdjfje = dataBean.getStr("shdjfje");
        hdxxJsonObj.put("shdjfje", shdjfje);
        // 预算科目代码 103044609 水土保持补偿费
        hdxxJsonObj.put("yskm_dm", "103044609");
        // 预算科目名称 yskmmc
        hdxxJsonObj.put("yskmmc", "水土保持补偿费");
        hdxxJsonArry.add(hdxxJsonObj);
        param.put("hdxx", hdxxJsonArry);
        return param;
    }

    // 构建推送的参数-防空费
    private static JSONObject buildPushParamsfangkongfei(AuditRsItemBaseinfo auditRsItemBaseinfo,String areacode,CertInfoExtension dataBean,
                                                       AuditSpISubapp auditSpISubapp,AuditProjectOperation auditProjectOperation,JSONObject formInfo){
        log.info("=====开始构建推送的参数=======areacode:"+areacode);
        IAuditOrgaArea iAuditOrgaArea = ContainerFactory.getContainInfo().getComponent(IAuditOrgaArea.class);
        IUserService iUserService = ContainerFactory.getContainInfo().getComponent(IUserService.class);
        ICodeItemsService iCodeItemsService = ContainerFactory.getContainInfo().getComponent(ICodeItemsService.class);

        JSONObject param = new JSONObject();
        // 项目id
        //param.put("xmid", auditRsItemBaseinfo.getItemcode());
        // 项目名称
        param.put("xmmc", auditRsItemBaseinfo.getItemname());
        // 项目审批所属级别代码 根据itemcode按照规则‘-’分割，获取第二位辖区，按照辖区判断，若辖区为370800活着的370891，则为2（市），其余均为3（区县）。
        String xmspssjb_dm = "";
        if(areacode.equals("370800") || areacode.equals("370891")){
            xmspssjb_dm = "2";
        }else{
            xmspssjb_dm = "3";
        }
        param.put("xmspssjb_dm", xmspssjb_dm);
        // 项目审批所属级别名称 若xmspssjb_dm=2则为市，若xmspssjb_dm=3则为区县
        param.put("xmspssjbmc", xmspssjb_dm.equals("2")?"市":"区县");
        // 批复文号 获取照面信息的pfwh字段的值
        String pfwh = dataBean.getStr("pfwh");
        param.put("pfwh", pfwh);
        // 项目所在地省代码 xmszdsh 固定值 山东省370000
        param.put("xmszdsh", "370000");
        // 项目所在地省名称 xmszdshmc 固定值 山东省
        param.put("xmszdshmc", "山东省");
        // 项目所在地地市代码	xmszdds	固定值	370800
        param.put("xmszdds", "370800");
        // 项目所在地地市名称	xmszddsmc	固定值	济宁市
        param.put("xmszddsmc", "济宁市");
        // 	项目所在地县区代码	xmszdxq	规则	根据itemcode按照规则‘-’分割，获取第二位辖区
        param.put("xmszdxq", areacode);
        // 项目所在地县区名称	xmszdxqmc	规则	根据itemcode按照规则‘-’分割，获取第二位辖区，转换成中文
        AuditOrgaArea auditOrgaArea = iAuditOrgaArea
                .getAreaByAreacode(areacode).getResult();
        String areaName = "";
        if (auditOrgaArea != null) {
            areaName = auditOrgaArea.getXiaquname();
        }
        param.put("xmszdxqmc", areaName);
        // 项目所在地 xmszd 规则
        param.put("xmszd", getItemAddress(auditSpISubapp));
        // 审批联系人 splxr audit_project_operation表 operateusername
        param.put("splxr", auditProjectOperation.getOperateusername());
        // 审批联系电话 splxdh frame_user表 根据audit_project_operation表operateuserguid字段获取frame_user表的phone
        log.info("=====开始获取splxdh======= auditProjectOperation.getOperateUserGuid():"+auditProjectOperation.getOperateUserGuid());
        FrameUser frameUser = iUserService.getUserByUserField("UserGuid", auditProjectOperation.getOperateUserGuid());
        log.info("=====获取splxdh成功=======frameUser:"+frameUser);
        if(frameUser != null){
            param.put("splxdh", frameUser.getMobile());
        }
        // 缴费人统一社会信用代码 tyshxydm audit_rs_item_base表 ITEMLEGALCREDITCODE字段
        param.put("tyshxydm", auditRsItemBaseinfo.getItemlegalcreditcode());
        // 缴费人类型代码 jfrlxdm 固定值 2
        param.put("jfrlxdm", "2");
        // 缴费人类型名称 jfrlxmc 固定值 自然人
        param.put("jfrlxmc", "自然人");
        // 缴费人名称	jfrmc	audit_rs_item_base表	legalperson
        param.put("jfrmc", auditRsItemBaseinfo.getLegalperson());
        // 缴费方联系人	jfflxr	audit_rs_item_base表	CONTRACTPERSON
        param.put("jfflxr", auditRsItemBaseinfo.getContractperson());
        // 缴费方联系电话	jfflxdh	audit_rs_item_base表	CONTRACTPHONE
        param.put("jfflxdh", auditRsItemBaseinfo.getContractphone());
        // 是否有批复文号	sfypfwh	规则	第五个字段pfwh是否有值，有值则有1，反之没有0
        param.put("sfypfwh", StringUtil.isBlank(pfwh)?"0":"1");
        // 预计缴费时间	jfsj	表单信息	第四个查询业务表单数据的表单字段信息
        param.put("jfsj", formInfo.getString("jfsj"));
        // 用户id	yhid	固定值	285BC282B6734B9DE063A9640C4C8147
        param.put("yhid", "285BC282B6734B9DE063A9640C4C8147");
        // 录入人id	lrrid	固定值	285BC282B6734B9DE063A9640C4C8147
        param.put("lrrid", "285BC282B6734B9DE063A9640C4C8147");
        // 数据来源	sjly	固定值	2
        param.put("sjly", "2");
        // 接口类型	status	固定值	add
        param.put("status", "add");
        // Json对象hdxx相关字段
        JSONArray hdxxJsonArry = new JSONArray();
        JSONObject hdxxJsonObj = new JSONObject();
        // 主管税务局代码	zgswj_dm	规则	根据itemcode按照规则‘-’分割，获取第二位辖区编码，获取代码项【主管税务机关】，根据辖区对应拓展属性值1找到对应的代码项值
        String zgswj_dm = getItemValueByBz1(areacode, "主管税务机关");
        log.info("=====zgswj_dm:"+zgswj_dm);
        hdxxJsonObj.put("zgswj_dm", zgswj_dm);
        // 主管税务局名称	zgswjmc	规则	根据zgswj_dm获取代码项文本
        if(StringUtil.isNotBlank(zgswj_dm)){
            String zgswjmc = iCodeItemsService.getItemTextByCodeName("主管税务机关", zgswj_dm);
            hdxxJsonObj.put("zgswjmc", zgswjmc);
        }
        // 征收品目代码	zspm_dm	固定	304240101
        hdxxJsonObj.put("zspm_dm", "304240101");
        // 征收品目名称	zspmmc	固定	防空地下室易地建设费
        hdxxJsonObj.put("zspmmc", "防空地下室易地建设费");
        // 缴费期限代码	jfqx_dm	固定	11
        hdxxJsonObj.put("jfqx_dm", "11");
        // 缴费期限名称	jfqxmc	固定	次
        hdxxJsonObj.put("jfqxmc", "次");
        // 预算分配比例代码	ysfpbl_dm	规则	获取代码项【人防预算分配比例】，根据zgswj_dm主管税务局代码对应拓展属性值1找到对应的代码项值
        String ysfpbl_dm = getItemValueByBz1(zgswj_dm, "人防预算分配比例");
        hdxxJsonObj.put("ysfpbl_dm", ysfpbl_dm);
        //预算分配比例名称	ysfpblmc	规则	根据ysfpbl_dm获取代码项文本
        if(StringUtil.isNotBlank(ysfpbl_dm)){
            String ysfpblmc = iCodeItemsService.getItemTextByCodeName("人防预算分配比例", ysfpbl_dm);
            hdxxJsonObj.put("ysfpblmc", ysfpblmc);
        }
        /*if("2".equals(xmspssjb_dm)){
            hdxxJsonObj.put("ysfpbl_dm", "13711029");
            hdxxJsonObj.put("ysfpblmc", "中央10%地市90%");
        }
        else if ("3".equals(xmspssjb_dm)){
            hdxxJsonObj.put("ysfpbl_dm", "13711030");
            hdxxJsonObj.put("ysfpblmc", "中央10%县区90%");
        }*/
        // 国库代码	gkdm	规则	获取代码项【国库】，根据zgswj_dm主管税务局代码对应拓展属性值1找到对应的代码项值
        String gkdm = getItemValueByBz1(zgswj_dm, "国库");
        hdxxJsonObj.put("gkdm", gkdm);
        // 国库名称	gkmc	规则	根据gkdm获取代码项文本
        if(StringUtil.isNotBlank(gkdm)){
            String gkmc = iCodeItemsService.getItemTextByCodeName("国库", gkdm);
            hdxxJsonObj.put("gkmc", gkmc);
        }
        // 复核后批复金额（含滞纳金)	fhhpfje	照面信息	获取照面信息的fhhpfje字段的值
        String fhhpfje = dataBean.getStr("fhhpfje");
        hdxxJsonObj.put("fhhpfje", fhhpfje);
        // 实核定缴费金额（含滞纳金）	shdjfje	照面信息	获取照面信息的shdjfje字段的值
        String shdjfje = dataBean.getStr("shdjfje");
        hdxxJsonObj.put("shdjfje", shdjfje);
        // 预算科目代码 yskm_dm 103042401 防空地下室易地建设费
        hdxxJsonObj.put("yskm_dm", "103042401");
        hdxxJsonObj.put("yskmmc", "防空地下室易地建设费");
        hdxxJsonArry.add(hdxxJsonObj);
        param.put("hdxx", hdxxJsonArry);
        return param;
    }

    /**
     * 更具代码项的备注找代码项值
     * @param areacode
     * @param codename
     * @return
     */
    private static String getItemValueByBz1(String str, String codename) {
        log.info("===开始调用getItemValueByBz1方法 str:"+str+" codename:"+codename);
        String itemValue = null;
        if(StringUtil.isBlank(str)){
            log.info("===结束调用getItemValueByBz1方法 参数str为空");
            return itemValue;
        }
        ICodeItemsService iCodeItemsService = ContainerFactory.getContainInfo().getComponent(ICodeItemsService.class);
        List<CodeItems> list = iCodeItemsService.listCodeItemsByCodeName(codename);
        // 如果 codeitems 的 dmAbr1 包含 areacode 就把 codeitems 对象拿出来
        for (CodeItems codeItems : list) {
            if (StringUtil.isNotBlank(codeItems.getDmAbr1())&&codeItems.getDmAbr1().contains(str)) {
                return codeItems.getItemValue();
            }
        }
        log.info("===结束调用getItemValueByBz1方法 itemValue:"+itemValue);
        return itemValue;
    }


    // 获取项目所在地
    // 根据subappguid查询到audit_sp_i_subapp获取phaseguid字段，
    // 根据phaseguid字段查询audit_sp_phase表phaseid字段，
    // 若phaseid=1,则根据subappguid查询audit_sp_sp_lxydghxk表获取itemAddress字段，
    // 若phaseid=2,则根据subappguid查询audit_sp_sp_gcjsxk表获取itemAddress字段,
    // 若phaseid=3,则根据subappguid查询audit_sp_sp_sgxk表获取itemAddress字段,
    // 若phaseid=4,则根据subappguid查询audit_sp_sp_jgys表获取itemAddress字段,
    // 若phaseid=6,则根据subappguid查询audit_sp_sp_jgys表获取itemAddress字段,
    private static String getItemAddress(AuditSpISubapp auditSpISubapp) {
        log.info("=====开始获取项目所在地=======");
        IAuditSpPhase iAuditSpPhase = ContainerFactory.getContainInfo().getComponent(IAuditSpPhase.class);
        IAuditSpSpLxydghxkService iAuditSpSpLxydghxkService = ContainerFactory.getContainInfo().getComponent(IAuditSpSpLxydghxkService.class);
        IAuditSpSpGcjsxkService iAuditSpSpGcjsxkService = ContainerFactory.getContainInfo().getComponent(IAuditSpSpGcjsxkService.class);
        IAuditSpSpSgxkService iAuditSpSpSgxkService = ContainerFactory.getContainInfo().getComponent(IAuditSpSpSgxkService.class);
        IAuditSpSpJgysService iAuditSpSpJgysService = ContainerFactory.getContainInfo().getComponent(IAuditSpSpJgysService.class);
        String xmszd = "";
        String phaseid = "";
        String subappguid = auditSpISubapp.getRowguid();
        String phaseguid = auditSpISubapp.getPhaseguid();
        log.info("=====获取项目所在地参数=======subappguid:"+subappguid+",phaseguid:"+phaseguid);
        AuditSpPhase auditSpPhase = iAuditSpPhase.getAuditSpPhaseByRowguid(phaseguid).getResult();
        if(auditSpPhase != null){
            phaseid = auditSpPhase.getPhaseId();
            log.info("=====获取项目所在地参数=======phaseid:"+phaseid);
        }
        else{
            log.info("=====获取项目所在地参数======= 失败 auditSpPhase 为空");
        }
        if("1".equals(phaseid)){
            AuditSpSpLxydghxk auditSpSpLxydghxk = iAuditSpSpLxydghxkService.findAuditSpSpLxydghxkBysubappguid(subappguid);
            if(auditSpSpLxydghxk != null){
                xmszd = auditSpSpLxydghxk.getItemaddress();
            }
        }
        else if("2".equals(phaseid)){
            AuditSpSpGcjsxk auditSpSpGcjsxk = iAuditSpSpGcjsxkService.findAuditSpSpGcjsxkBySubappGuid(subappguid);
            if(auditSpSpGcjsxk != null){
                xmszd = auditSpSpGcjsxk.getItemaddress();
            }
        }
        else if("3".equals(phaseid)){
            AuditSpSpSgxk auditSpSpSgxk = iAuditSpSpSgxkService.findAuditSpSpSgxkBysubappguid(subappguid);
            if(auditSpSpSgxk != null){
                xmszd = auditSpSpSgxk.getItemaddress();
            }
        }
        else if ("4".equals(phaseid)||"6".equals(phaseid)) {
            AuditSpSpJgys auditSpSpJgys = iAuditSpSpJgysService.findAuditSpSpJgysBySubappGuid(subappguid);
            if(auditSpSpJgys != null){
                xmszd = auditSpSpJgys.getItemaddress();
            }
        }
        log.info("=====获取项目所在地结果=======xmszd:"+xmszd);
        return xmszd;
    }

    public static void convertDate(List<CertMetadata> metadataList, Record record) {
        ICodeItemsService codeItemsService = ContainerFactory.getContainInfo().getComponent(ICodeItemsService.class);
        if (ValidateUtil.isBlankCollection(metadataList) || record == null) {
            return;
        }
        for (CertMetadata metadata : metadataList) {
            // 展示显示的字段 时间格式YYYY-MM-DD
            if(ZwfwConstant.INPUT_TYPE_DATE_TIME.equals(metadata.getFieldtype())){
                Date fieldDate = record.getDate(metadata.getFieldname());
                if (fieldDate != null) {
                    record.set(metadata.getFieldname(), EpointDateUtil.convertDate2String(fieldDate));
                }
            }
            // 显示错误代码项
            if(StringUtil.isNotBlank(metadata.getDatasource_codename())) {
                String value = record.getStr(metadata.getFieldname());
                if (StringUtil.isNotBlank(value)) {
                    String itemName = codeItemsService.getItemTextByCodeName(metadata
                            .getDatasource_codename(), value);
                    if (StringUtil.isBlank(itemName)) {
                        // bug10823，bug10825
                        /*record.set(metadata.getFieldname(), value + "(" + metadata
                                .getDatasource_codename() + "列表选择错误)");*/
                        record.set(metadata.getFieldname(), value);
                        metadata.setDatasource_codename(null);
                    }
                }
            }
        }
    }


    private static JSONObject getEformPageData(String rowGuid, String formid) {
        JSONObject allInfo = new JSONObject();
        String epointsformurlwt = ConfigUtil.getFrameConfigValue("epointsformurl_innernet_ip");
        if (com.epoint.core.utils.string.StringUtil.isNotBlank(epointsformurlwt) && !epointsformurlwt.endsWith("/")) {
            epointsformurlwt = epointsformurlwt + "/";
        }
        String getPageDataurl = epointsformurlwt + "rest/sform/getPageData";
        Map<String, Object> param = new HashMap<>();
        JSONObject object = new JSONObject();
        object.put("formId", formid);
        object.put("rowGuid", rowGuid);
        param.put("params", object);
        String result = HttpUtil.doPost(getPageDataurl, param);
        if (com.epoint.core.utils.string.StringUtil.isNotBlank(result)) {
            JSONObject obj1 = JSONObject.parseObject(result);
            JSONObject recordData = obj1.getJSONObject("custom").getJSONObject("recordData");
            JSONArray mainList = recordData.getJSONArray("mainList");
            for (int i = 0; i<mainList.size();i++) {
                JSONObject jsonObject = mainList.getJSONObject(i);
                JSONArray rowList = jsonObject.getJSONArray("rowList");
                for (int j = 0; j<rowList.size();j++) {
                    JSONObject rowObject = rowList.getJSONObject(j);
                    allInfo.put(rowObject.getString("FieldName"), rowObject.getString("value"));
                }
            }

            JSONArray personArray = new JSONArray();
            JSONArray deviceArray = new JSONArray();
            JSONArray subList = recordData.getJSONArray("subList");
            if (EpointCollectionUtils.isNotEmpty(subList)) {
                for (int i = 0; i<subList.size();i++) {
                    JSONObject subObject = subList.getJSONObject(i);
                    // 如果有多个子表，此处需要循环遍历
                    JSONArray mainRecordList = subObject.getJSONArray("mainRecordList");
                    for (int j = 0; j<mainRecordList.size(); j++) {
                        // 如果每个子表有多条记录，此处需要循环遍历
                        JSONArray subRecordList = mainRecordList.getJSONObject(j).getJSONArray("subRecordList");
                        if (EpointCollectionUtils.isNotEmpty(subRecordList)) {
                            for (int k = 0; k<subRecordList.size();k++) {
                                JSONObject rowInfo = new JSONObject();
                                JSONObject subRecord = subRecordList.getJSONObject(k);
                                JSONArray rowList = subRecord.getJSONArray("rowList");
                                for (int l = 0; l<rowList.size();l++) {
                                    JSONObject rowObject = rowList.getJSONObject(l);
                                    rowInfo.put(rowObject.getString("FieldName"), rowObject.getString("value"));
                                }
                                if (rowInfo.containsKey("xm")) {
                                    personArray.add(rowInfo);
                                }
                                else if (rowInfo.containsKey("zzmc")) {
                                    deviceArray.add(rowInfo);
                                }
                            }
                        }
                    }
                }
            }
            allInfo.put("personArray", personArray);
            allInfo.put("deviceArray", deviceArray);
        }
        return allInfo;
    }
}
