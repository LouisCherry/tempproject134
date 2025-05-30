package com.epoint.jnzwdt.zwdtgcjsveryfy.rest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.auditsp.auditspisubapp.inter.IAuditSpISubapp;
import com.epoint.basic.auditsp.auditspphase.inter.IAuditSpPhase;
import com.epoint.basic.auditsp.dantiinfo.api.IDantiInfoService;
import com.epoint.basic.auditsp.dantiinfo.entity.DantiInfo;
import com.epoint.basic.basedata.participantsinfo.api.IParticipantsInfoService;
import com.epoint.basic.basedata.participantsinfo.api.entity.ParticipantsInfo;
import com.epoint.common.util.JsonUtils;
import com.epoint.core.utils.string.StringUtil;

@RestController
@RequestMapping("/zwdtgcjsveryfy")
public class ZwdtGcjsVeryfyController
{
    private Logger log = Logger.getLogger(ZwdtGcjsVeryfyController.class);

    @Autowired
    private IDantiInfoService dantiInfoService;
    @Autowired
    private IParticipantsInfoService participantsInfoService;
    @Autowired
    private IAuditSpISubapp auditSpISubapp;
    @Autowired
    private IAuditSpPhase auditSpPhase;

    /**
     * 校验单位单体表单
     * 
     * @param params
     * @param request
     * @return
     */
    @RequestMapping(value = "/checkDwdt", method = RequestMethod.POST)
    public String checkDwdt(@RequestBody String params, HttpServletRequest request) {
        try {
            log.info("==============开始调用checkDwdt==============");
            JSONObject jsonObject = JSONObject.parseObject(params);
            JSONObject obj = (JSONObject) jsonObject.get("params");

            String subappguid = obj.getString("subappguid");
            // 非空判断
            if (StringUtil.isBlank(subappguid)) {
                return JsonUtils.zwdtRestReturn("0", "subappguid不能为空！", "");
            }
            
            // 提示消息-最终
            StringBuilder allchackMsg = new StringBuilder();
            
//            // 判断是否4阶段
//            AuditSpISubapp subapp = auditSpISubapp.getSubappByGuid(subappguid).getResult();
//            if (subapp != null) {
//                AuditSpPhase spphase = auditSpPhase.getAuditSpPhaseByRowguid(subapp.getPhaseguid()).getResult();
//                if (spphase != null && !"4".equals(spphase.getPhaseId())) {
//                    JSONObject resultObj = new JSONObject(); 
//                    resultObj.put("allchackMsg", allchackMsg.toString());
//                    return JsonUtils.zwdtRestReturn("1", "请求成功！", resultObj);
//                }
//            }

            // 单体遍历
            List<DantiInfo> dantiList = dantiInfoService.findDantiListBySubappguid(new Object[] {subappguid });
            for (DantiInfo info : dantiList) {
                HashMap<String, Object> checkMap = new HashMap<>();
                checkMap.put("单体名称", info.getDantiname());
                checkMap.put("工程类别", info.getGclb());
                checkMap.put("建筑工程高度", info.getJzgd());
                checkMap.put("结构体系", info.getJiegoutx());
                checkMap.put("建筑面积", info.getZjzmj());
                checkMap.put("占地面积", info.getDouble("zdmj"));
                checkMap.put("地上面积", info.getDishangmianji());
                checkMap.put("地下面积", info.getDixiamianji());
                checkMap.put("地上层数", info.getDscs());
                checkMap.put("地下层数", info.getDxcs());
                checkMap.put("规模指标", info.getStr("gmzb"));
                String checkMsg = this.checkFile(checkMap);
                // 单体名称+xx字段名+“必填,请检查”
                if (StringUtil.isNotBlank(checkMsg)) {
                    allchackMsg.append(info.getDantiname() + ":" + checkMsg + "必填,请检查。");
                }
            }

            // 单位信息
            // 建设单位
            List<ParticipantsInfo> jsdwList = participantsInfoService
                    .findParticipantsInfoListBySubappguidAndCorptype(subappguid, "31");
            for (ParticipantsInfo info : jsdwList) {
                HashMap<String, Object> checkMap = new HashMap<>();
                checkMap.put("单位名称", info.getCorpname());
                checkMap.put("建设单位类型", info.getStr("JSDWLX"));
                checkMap.put("建设单位代码", info.getItemlegalcertnum());
                checkMap.put("单位联系电话", info.getPhone());
                checkMap.put("法定代表人", info.getLegal());
                checkMap.put("法人证照类型", info.getStr("legalcardtype"));
                checkMap.put("法人证照号码", info.getLegalpersonicardnum());
                checkMap.put("项目负责人", info.getXmfzr());
                checkMap.put("职称", info.getXmfzr_zc());
                checkMap.put("证照类型", info.getStr("fzrzjlx"));
                checkMap.put("证照号码", info.getXmfzr_idcard());
                checkMap.put("联系电话", info.getXmfzr_phone());
                checkMap.put("联系人", info.getDanweilxr());
                checkMap.put("联系电话", info.getDanweilxrlxdh());
                checkMap.put("联系人身份证", info.getDanweilxrsfz());
                String checkMsg = this.checkFile(checkMap);
                // 单体名称+xx字段名+“必填,请检查”
                if (StringUtil.isNotBlank(checkMsg)) {
                    allchackMsg.append(info.getCorpname() + ":" + checkMsg + "必填,请检查。");
                }
            }
            // 设计单位
            List<ParticipantsInfo> sjdwList = participantsInfoService
                    .findParticipantsInfoListBySubappguidAndCorptype(subappguid, "2");
            for (ParticipantsInfo info : sjdwList) {
                HashMap<String, Object> checkMap = new HashMap<>();
                checkMap.put("单位名称", info.getCorpname());
                checkMap.put("统一社会信用代码", info.getCorpcode());
                checkMap.put("法定代表人", info.getLegal());
                checkMap.put("单位联系电话", info.getPhone());
                checkMap.put("法人证照类型", info.getStr("legalcardtype"));
                checkMap.put("法人证照号码", info.getLegalpersonicardnum());
                checkMap.put("资质等级", info.getStr("CERT"));
                
                checkMap.put("设计负责人", info.getXmfzr());
                checkMap.put("职称", info.getXmfzr_zc());
                checkMap.put("证照类型", info.getStr("fzrzjlx"));
                checkMap.put("证照号码", info.getXmfzr_idcard());
                checkMap.put("联系电话", info.getXmfzr_phone());
                String checkMsg = this.checkFile(checkMap);
                // 单体名称+xx字段名+“必填,请检查”
                if (StringUtil.isNotBlank(checkMsg)) {
                    allchackMsg.append(info.getCorpname() + ":" + checkMsg + "必填,请检查。");
                }
            }
            // 勘察单位
            List<ParticipantsInfo> kcdwList = participantsInfoService
                    .findParticipantsInfoListBySubappguidAndCorptype(subappguid, "1");
            for (ParticipantsInfo info : kcdwList) {
                HashMap<String, Object> checkMap = new HashMap<>();
                checkMap.put("单位名称", info.getCorpname());
                checkMap.put("统一社会信用代码", info.getCorpcode());
                checkMap.put("法定代表人", info.getLegal());
                checkMap.put("单位联系电话", info.getPhone());
                checkMap.put("法人证照类型", info.getStr("legalcardtype"));
                checkMap.put("法人证照号码", info.getLegalpersonicardnum());
                checkMap.put("资质等级", info.getStr("CERT"));
                
                checkMap.put("勘察负责人", info.getXmfzr());
                checkMap.put("职称", info.getXmfzr_zc());
                checkMap.put("证照类型", info.getStr("fzrzjlx"));
                checkMap.put("证照号码", info.getXmfzr_idcard());
                checkMap.put("联系电话", info.getXmfzr_phone());
                String checkMsg = this.checkFile(checkMap);
                // 单体名称+xx字段名+“必填,请检查”
                if (StringUtil.isNotBlank(checkMsg)) {
                    allchackMsg.append(info.getCorpname() + ":" + checkMsg + "必填,请检查。");
                }
            }
            // 施工单位
            List<ParticipantsInfo> sgdwList = participantsInfoService
                    .findParticipantsInfoListBySubappguidAndCorptype(subappguid, "3");
            for (ParticipantsInfo info : sgdwList) {
                HashMap<String, Object> checkMap = new HashMap<>();
                checkMap.put("单位名称", info.getCorpname());
                checkMap.put("统一社会信用代码", info.getCorpcode());
                checkMap.put("法定代表人", info.getLegal());
                checkMap.put("单位联系电话", info.getPhone());
                checkMap.put("法人证照类型", info.getStr("legalcardtype"));
                checkMap.put("法人证照号码", info.getLegalpersonicardnum());
                checkMap.put("单位地址", info.getAddress());
                checkMap.put("承包单位类型", info.getCbdanweitype());
                checkMap.put("资质等级", info.getStr("CERT"));
                
                if ("01".equals(info.getCbdanweitype())) {
                    checkMap.put("项目经理", info.getXmfzr());
                    checkMap.put("职称", info.getXmfzr_zc());
                    checkMap.put("证照类型", info.getStr("fzrzjlx"));
                    checkMap.put("证照号码", info.getXmfzr_idcard());
                    checkMap.put("联系电话", info.getXmfzr_phone());
                    checkMap.put("技术负责人", info.getJsfzr());
                    checkMap.put("职称", info.getJsfzr_zc());
                    checkMap.put("联系电话", info.getJsfzr_phone());
                    checkMap.put("项目负责人", info.getXmfzperson());
                    checkMap.put("安全考核合格证编号", info.getXmfzrsafenum());
                    checkMap.put("联系电话", info.getXmfzrphonenum());
                    checkMap.put("企业联系人", info.getQylxr());
                    checkMap.put("企业联系电话", info.getQylxdh());
                    checkMap.put("工地联系人", info.getGdlxr());
                    checkMap.put("工地联系人电话", info.getGdlxdh());
                }
                else if ("02".equals(info.getCbdanweitype())) {
                    checkMap.put("安全生产许可证编号", info.getFbsafenum());
                    checkMap.put("合同起止日期", info.getFbtime());
                    checkMap.put("承包范围", info.getFbscopeofcontract());
                    checkMap.put("企业成立时间", info.getFbqysettime());
                    checkMap.put("安全管理人员", info.getFbaqglry());
                    checkMap.put("安全考核合格证编号", info.getFbaqglrysafenum());
                }
                
                String checkMsg = this.checkFile(checkMap);
                // 单体名称+xx字段名+“必填,请检查”
                if (StringUtil.isNotBlank(checkMsg)) {
                    allchackMsg.append(info.getCorpname() + ":" + checkMsg + "必填,请检查。");
                }
            }
            // 监理单位
            List<ParticipantsInfo> jldwList = participantsInfoService
                    .findParticipantsInfoListBySubappguidAndCorptype(subappguid, "4");
            for (ParticipantsInfo info : jldwList) {
                HashMap<String, Object> checkMap = new HashMap<>();
                checkMap.put("单位名称", info.getCorpname());
                checkMap.put("统一社会信用代码", info.getCorpcode());
                checkMap.put("法定代表人", info.getLegal());
                checkMap.put("单位联系电话", info.getPhone());
                checkMap.put("法人证照类型", info.getStr("legalcardtype"));
                checkMap.put("法人证照号码", info.getLegalpersonicardnum());
                checkMap.put("资质等级", info.getStr("CERT"));
                
                checkMap.put("项目总监", info.getXmfzr());
                checkMap.put("职称", info.getXmfzr_zc());
                checkMap.put("证照类型", info.getStr("fzrzjlx"));
                checkMap.put("证照号码", info.getXmfzr_idcard());
                checkMap.put("联系电话", info.getXmfzr_phone());
                String checkMsg = this.checkFile(checkMap);
                // 单体名称+xx字段名+“必填,请检查”
                if (StringUtil.isNotBlank(checkMsg)) {
                    allchackMsg.append(info.getCorpname() + ":" + checkMsg + "必填,请检查。");
                }
            }
            // 质量检测单位
            List<ParticipantsInfo> zljcdwList = participantsInfoService
                    .findParticipantsInfoListBySubappguidAndCorptype(subappguid, "10");
            for (ParticipantsInfo info : zljcdwList) {
                HashMap<String, Object> checkMap = new HashMap<>();
                checkMap.put("单位名称", info.getCorpname());
                checkMap.put("统一社会信用代码", info.getCorpcode());
                checkMap.put("法定代表人", info.getLegal());
                checkMap.put("单位联系电话", info.getPhone());
                checkMap.put("法人证照类型", info.getStr("legalcardtype"));
                checkMap.put("法人证照号码", info.getLegalpersonicardnum());
                checkMap.put("资质等级", info.getStr("CERT"));
                
                checkMap.put("质量检测负责人", info.getXmfzr());
                checkMap.put("职称", info.getXmfzr_zc());
                checkMap.put("证照类型", info.getStr("fzrzjlx"));
                checkMap.put("证照号码", info.getXmfzr_idcard());
                checkMap.put("联系电话", info.getXmfzr_phone());
                String checkMsg = this.checkFile(checkMap);
                // 单体名称+xx字段名+“必填,请检查”
                if (StringUtil.isNotBlank(checkMsg)) {
                    allchackMsg.append(info.getCorpname() + ":" + checkMsg + "必填,请检查。");
                }
            }
            
            JSONObject resultObj = new JSONObject(); 
            resultObj.put("allchackMsg", allchackMsg.toString());
            return JsonUtils.zwdtRestReturn("1", "请求成功！", resultObj);

        }
        catch (Exception e) {
            e.printStackTrace();
            return JsonUtils.zwdtRestReturn("0", "异常错误！", "");
        }

    }

    /**
     * 字段空判断
     * 
     * @param checkMap
     * @return
     */
    private String checkFile(HashMap<String, Object> checkMap) {
        // 检查信息
        StringBuilder checkMsg = new StringBuilder();
        for (Map.Entry<String, Object> entry : checkMap.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            // 字符串和其他类型分开判断空
            if (value instanceof String) {
                if (value == null || StringUtil.isBlank(value)) {
                    checkMsg.append(key);
                    checkMsg.append(";");
                }
            }
            else {
                if (value == null) {
                    checkMsg.append(key);
                    checkMsg.append(";");
                }
            }
        }
        return checkMsg.toString();
    }

}

