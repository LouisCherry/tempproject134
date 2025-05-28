package com.epoint.basic.auditsp.dantiinfo.utils;

import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.auditorga.auditarea.domain.AuditOrgaArea;
import com.epoint.basic.auditorga.auditarea.inter.IAuditOrgaArea;
import com.epoint.basic.auditresource.auditrsitembaseinfo.domain.AuditRsItemBaseinfo;
import com.epoint.basic.auditresource.auditrsitembaseinfo.inter.IAuditRsItemBaseinfo;
import com.epoint.basic.auditsp.auditspbusiness.domain.AuditSpBusiness;
import com.epoint.basic.auditsp.auditspbusiness.inter.IAuditSpBusiness;
import com.epoint.basic.auditsp.auditspinstance.domain.AuditSpInstance;
import com.epoint.basic.auditsp.auditspinstance.inter.IAuditSpInstance;
import com.epoint.basic.auditsp.dantiinfo.entity.DantiInfo;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ValidateUtil;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.inter.ISpglsplcxxb;

import java.util.ArrayList;
import java.util.List;

public class DantiFmUtil {

    public String PackagingParametersDanti(DantiInfo info, AuditRsItemBaseinfo rsItemBaseinfo) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("xmdm", rsItemBaseinfo.getItemcode());
        jsonObject.put("xmmc", rsItemBaseinfo.getItemname());
        jsonObject.put("xmxzqhdm", "370800");
        jsonObject.put("appId", "https://www.kdocs.cn/l/cdatfhSLc6Cv");
        ArrayList<JSONObject> monos = new ArrayList<>();
        JSONObject json = new JSONObject();
        json.put("dtid", info.getRowguid());
        json.put("dtmc", info.getDantiname());
        json.put("dtlx", "1");
        json.put("fjid", null);
        json.put("lsh", null);
        json.put("dfsjzj", info.getRowguid());
        json.put("xzqhdm", "370800");
        json.put("gcdm", rsItemBaseinfo.getItemcode());
        json.put("spsxslbm", null);
        json.put("xkbabh", info.getStr("xkbabh"));
        json.put("gcyt", info.getGclb());
        json.put("gmzb", info.getStr("gmzb"));
        json.put("jgtx", info.getJiegoutx());
        json.put("nhdj", info.getFirelevel());
        json.put("jzfs", info.getStr("jzfs"));
        json.put("dtjwdzb", info.getStr("dtjwdzb"));
        json.put("dtgczzj", info.getPrice());
        json.put("jzmj", info.getZjzmj());
        json.put("dsjzmj", info.getDishangmianji());
        json.put("dxjzmj", info.getDixiamianji());
        json.put("zdmj", info.getStr("zdmj"));
        json.put("jzgcgd", info.getJzgd());
        json.put("dscs", info.getDscs());
        json.put("dxcs", info.getDxcs());
        json.put("sjyxbs", 1);
        json.put("sjwxyy", null);
        json.put("sjsczt", 0);
        json.put("sbyy", null);
        monos.add(json);
        jsonObject.put("monos", monos);
        return jsonObject.toJSONString();
    }

    public String PackagingParametersXm(AuditRsItemBaseinfo rsItemBaseinfo, Long lsh) {
        IAuditRsItemBaseinfo iAuditRsItemBaseinfo = ContainerFactory.getContainInfo().getComponent(IAuditRsItemBaseinfo.class);
        IAuditSpInstance iauditspinstance = ContainerFactory.getContainInfo().getComponent(IAuditSpInstance.class);
        IAuditSpBusiness iauditspbusiness = ContainerFactory.getContainInfo().getComponent(IAuditSpBusiness.class);
        IAuditOrgaArea iauditorgaarea = ContainerFactory.getContainInfo().getComponent(IAuditOrgaArea.class);
        ISpglsplcxxb iSpglsplcxxb = ContainerFactory.getContainInfo().getComponent(ISpglsplcxxb.class);
        ArrayList<JSONObject> xm = new ArrayList<>();
        AuditSpInstance auditspinstance = iauditspinstance.getDetailByBIGuid(rsItemBaseinfo.getBiguid())
                .getResult();
        if (auditspinstance != null) {
            AuditSpBusiness auditspbusiness = iauditspbusiness.getAuditSpBusinessByRowguid(
                    auditspinstance.getBusinessguid()).getResult();
            String businessareacode = auditspbusiness.getAreacode();
            AuditOrgaArea area = iauditorgaarea.getAreaByAreacode(businessareacode).getResult();
            if (area != null) {
                // 如果是县级，查找市级主题
                if (ZwfwConstant.CONSTANT_STR_TWO.equals(area.getCitylevel())) {
                    SqlConditionUtil sqlc = new SqlConditionUtil();
                    sqlc.eq("citylevel", ZwfwConstant.CONSTANT_STR_ONE);
                    // 查找市级辖区
                    AuditOrgaArea sjarea = iauditorgaarea.getAuditArea(sqlc.getMap()).getResult();
                    if (sjarea != null) {
                        sqlc.clear();
                        sqlc.eq("splclx", String.valueOf(auditspbusiness.getSplclx()));
                        sqlc.eq("areacode", sjarea.getXiaqucode());
                        List<AuditSpBusiness> listb = iauditspbusiness.getAllAuditSpBusiness(sqlc.getMap()).getResult();
                        if (ValidateUtil.isNotBlankCollection(listb)) {
                            auditspbusiness = listb.get(0);
                        }
                    }
                }
            }
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("lsh", lsh);
            jsonObject.put("dfsjzj", rsItemBaseinfo.getRowguid());
            jsonObject.put("xzqhdm", "370800");
            String xmdm = "";
            if (StringUtil.isNotBlank(rsItemBaseinfo.getParentid())) {
                AuditRsItemBaseinfo pauditRsItemBaseinfo = iAuditRsItemBaseinfo.getAuditRsItemBaseinfoByRowguid(
                        rsItemBaseinfo.getParentid()).getResult();
                xmdm = pauditRsItemBaseinfo.getItemcode();
            } else {
                xmdm = rsItemBaseinfo.getItemcode();
            }
            jsonObject.put("xmdm", xmdm);
            jsonObject.put("xmmc", rsItemBaseinfo.getItemname());
            jsonObject.put("gcdm", rsItemBaseinfo.getItemcode());
            jsonObject.put("gcfw", rsItemBaseinfo.getStr("GCFW"));//工程分类
            jsonObject.put("qjdgcdm", null);
            jsonObject.put("jsdw", rsItemBaseinfo.getDepartname());
            jsonObject.put("jsdwdm", rsItemBaseinfo.getItemlegalcertnum());
            jsonObject.put("jsdwlx", rsItemBaseinfo.getInt("JSDWLX"));
            if (rsItemBaseinfo != null && rsItemBaseinfo.getXmtzly() != null) {
                int xmtzly = rsItemBaseinfo.getXmtzly();
                // 0 不在范围内，特殊处理
                if (0 == rsItemBaseinfo.getXmtzly()) {
                    if (1 == auditspbusiness.getSplclx() || 2 == auditspbusiness.getSplclx()) {
                        xmtzly = 1;
                    } else {
                        xmtzly = 2;
                    }
                }

                jsonObject.put("xmtzly", xmtzly);//项目投资来源
            }

            jsonObject.put("tdhqfs", rsItemBaseinfo.getTdhqfs());//土地获取方式
            jsonObject.put("tdsfdsjfa", rsItemBaseinfo.getTdsfdsjfa());//土地是否带设计方案
            jsonObject.put("sfwcqypg", rsItemBaseinfo.getSfwcqypg());//是否完成区域评估
            jsonObject.put("splclx", auditspbusiness.getSplclx());//审批流程类型
            jsonObject.put("lxlx", rsItemBaseinfo.getStr("LXLX"));//立项类型
            jsonObject.put("gchyfl", rsItemBaseinfo.get("GCHYFL"));//工程行业分类
            jsonObject.put("jsxz", rsItemBaseinfo.getConstructionproperty());//建设性质
            jsonObject.put("xmzjsx", rsItemBaseinfo.getXmzjsx());//项目资金属性
            jsonObject.put("gbhydmfbnd", "2017");//国标行业代码发布年代

            //子项目可能没有国标行业，统一用主项目
            String gbhy = rsItemBaseinfo.getGbhy();
            if (StringUtil.isNotBlank(rsItemBaseinfo.getParentid())) {
                AuditRsItemBaseinfo parentinfo = iAuditRsItemBaseinfo.getAuditRsItemBaseinfoByRowguid(rsItemBaseinfo.getParentid()).getResult();
                gbhy = parentinfo.getGbhy();
            } else {
                gbhy = rsItemBaseinfo.getGbhy();
            }

            jsonObject.put("gbhy", gbhy);//国标行业
            if (StringUtil.isNotBlank(rsItemBaseinfo.getItemstartdate())) {
                jsonObject.put("nkgsj", EpointDateUtil.convertDate2String(rsItemBaseinfo.getItemstartdate(), EpointDateUtil.DATE_TIME_FORMAT));//拟开工时间
            }
            if (StringUtil.isNotBlank(rsItemBaseinfo.getItemfinishdate())) {
                jsonObject.put("njcsj", EpointDateUtil.convertDate2String(rsItemBaseinfo.getItemfinishdate(), EpointDateUtil.DATE_TIME_FORMAT));//拟建成时间
            }
            jsonObject.put("xmsfwqbj", 0);//项目是否完全办结
            jsonObject.put("xmwqbjsj", null);//项目完全办结时间
            jsonObject.put("ztze", rsItemBaseinfo.getTotalinvest());//总投资额（万元）
            jsonObject.put("jsddxzqh", rsItemBaseinfo.getStr("jsddxzqh"));//建设地点行政区划
            jsonObject.put("jsdd", rsItemBaseinfo.getConstructionsite());//建设地点
            jsonObject.put("xmjwdzb", rsItemBaseinfo.get("XMJWDZB"));//项目经纬度坐标
            jsonObject.put("jsgmjnr", rsItemBaseinfo.getConstructionscaleanddesc());//建设规模及内容
            jsonObject.put("ydmj", rsItemBaseinfo.getLandarea() != null ? rsItemBaseinfo.getLandarea() : 0);//用地面积
            jsonObject.put("jzmj", rsItemBaseinfo.getJzmj());//建筑面积
            jsonObject.put("sfxxgc", rsItemBaseinfo.getInt("SFXXGC"));//是否线性工程
            jsonObject.put("cd", rsItemBaseinfo.getDouble("CD"));//长度
            if (StringUtil.isNotBlank(auditspinstance.getCreatedate())) {
                jsonObject.put("sbsj", EpointDateUtil.convertDate2String(auditspinstance.getCreatedate(), EpointDateUtil.DATE_TIME_FORMAT));//申报时间
            }
            jsonObject.put("splcbm", auditspbusiness.getRowguid());//审批流程编码
            Double verison = iSpglsplcxxb.getMaxSplcbbh(auditspbusiness.getRowguid());
            jsonObject.put("splcbbh", verison);//审批流程版本号
            jsonObject.put("sjyxbs", "1");//数据有效标识
            jsonObject.put("sjwxyy", null);//数据无效原因
            jsonObject.put("sjsczt", 0);//数据上传状态
            jsonObject.put("sbyy", null);//失败原因
            xm.add(jsonObject);
        }
        return xm.toString();
    }


}
