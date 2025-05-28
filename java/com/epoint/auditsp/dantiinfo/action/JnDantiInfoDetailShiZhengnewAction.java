package com.epoint.auditsp.dantiinfo.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.auditsp.dantiinfo.api.IDantiInfoService;
import com.epoint.basic.auditsp.dantiinfo.entity.DantiInfo;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.xmz.thirdreporteddata.dantiinfov3.api.IDantiInfoV3Service;
import com.epoint.xmz.thirdreporteddata.dantiinfov3.api.entity.DantiInfoV3;

/**
 * 单体信息表（单体同子单位工程）详情页面对应的后台
 * 
 * @author WIN-H366O37KOW0$
 * @version [版本号, 2018-05-17 21:59:10]
 */
@RightRelation(DantiInfoListAction.class)
@RestController("jndantiinfodetailshizhengnewaction")
@Scope("request")
public class JnDantiInfoDetailShiZhengnewAction extends BaseController
{
    private static final long serialVersionUID = 7575772282275343036L;
    @Autowired
    private IDantiInfoService service;
    @Autowired
    private IDantiInfoV3Service iDantiInfoV3Service;
    @Autowired
    private ICodeItemsService codeservice;
    /**
     * 单体信息表（单体同子单位工程）实体对象
     */
    private DantiInfo dataBean = null;

    private DantiInfoV3 threeBean = null;

    public void pageLoad() {
        String guid = getRequestParameter("guid");
        dataBean = service.find(guid);
        threeBean = iDantiInfoV3Service.find(guid);
        if (dataBean == null) {
            dataBean = new DantiInfo();
        }
        if (threeBean == null) {
            threeBean = new DantiInfoV3();
        }
        if (StringUtil.isNotBlank(dataBean.getGclb())) {
            addCallbackParam("gclbcode", dataBean.getGclb());
            String gclbname = getNameByCode(dataBean.getGclb());
            dataBean.setGclb(gclbname);
        }

        if (StringUtil.isNotBlank(dataBean.getRfqk())) {
            dataBean.setRfqk(getItemText(dataBean.getRfqk(), "有无"));
        }
        if (StringUtil.isNotBlank(dataBean.getCxsc())) {
            dataBean.setCxsc(getItemText(dataBean.getCxsc(), "是否"));
        }
        if (StringUtil.isNotBlank(dataBean.getTzlx())) {
            dataBean.setTzlx(getItemText(dataBean.getTzlx(), "投资类型"));
        }
        if (StringUtil.isNotBlank(dataBean.getSfczrfgc())) {
            dataBean.setSfczrfgc(getItemText(dataBean.getSfczrfgc(), "是否"));
        }

        if (StringUtil.isNotBlank(dataBean.getGcxz())) {
            if (dataBean.getGcxz().contains(",")) {
                String[] list = dataBean.getGcxz().split(",");
                StringBuilder sb = new StringBuilder();
                for (String code : list) {
                    String name = getItemText(code, "工程性质");
                    sb.append(name);
                    sb.append(";");
                }
                dataBean.setGcxz(sb.toString().substring(0, sb.toString().length() - 1));
            }
            else {
                dataBean.setGcxz(getItemText(dataBean.getGcxz(), "工程性质"));
            }

        }
        if (StringUtil.isNotBlank(dataBean.getJgxs())) {
            if (dataBean.getJgxs().contains(",")) {
                String[] list = dataBean.getJgxs().split(",");
                StringBuilder sb = new StringBuilder();
                for (String code : list) {
                    String name = getItemText(code, "结构形式");
                    sb.append(name);
                    sb.append(";");
                }
                dataBean.setJgxs(sb.toString().substring(0, sb.toString().length() - 1));
            }
            else {
                dataBean.setJgxs(getItemText(dataBean.getJgxs(), "结构形式"));
            }

        }
//        if (StringUtil.isNotBlank(dataBean.getRowguid())) {
//            List<CodeProjectScale> codeProjectScales = codeProjectScaleService
//                    .listPrjectScaleByDantiguid(dataBean.getRowguid());
//            String[] texts = new String[codeProjectScales.size()];
//            String[] values = new String[codeProjectScales.size()];
//            String[] danweis = new String[codeProjectScales.size()];
//            int i = 0;
//            for (CodeProjectScale codeProjectScale : codeProjectScales) {
//                String text = codeProjectScale.getCodetext();
//                String value = codeProjectScale.getScale();
//                String danwei = codeProjectScale.getDanwei();
//                texts[i] = text;
//                values[i] = value;
//                danweis[i] = danwei;
//                i++;
//            }
//            addCallbackParam("texts", texts);
//            addCallbackParam("values", values);
//            addCallbackParam("danweis", danweis);
//        }
        if (StringUtil.isNotBlank(dataBean.getBdxjzmj())) {
            addCallbackParam("dxjzmj", dataBean.getBdxjzmj());
        }
        if (StringUtil.isNotBlank(dataBean.getZzmj())) {
            addCallbackParam("dsjzmj", dataBean.getZzmj());
        }
        if (StringUtil.isNotBlank(dataBean.getZjzmj())) {
            addCallbackParam("zjzmj", dataBean.getZjzmj());
        }

        if (StringUtil.isNotBlank(dataBean.getJiegoutx())) {
            addCallbackParam("jiegoutx",
                    codeservice.getItemTextByCodeName("结构体系", dataBean.getJiegoutx().toString()));

        }
        if (StringUtil.isNotBlank(dataBean.getDjdesignleavel())) {
            addCallbackParam("djdesignleavel",
                    codeservice.getItemTextByCodeName("地基基础设计等级", dataBean.getDjdesignleavel().toString()));

        }
        if (StringUtil.isNotBlank(dataBean.getBasictype())) {
            addCallbackParam("basictype",
                    codeservice.getItemTextByCodeName("基础型式", dataBean.getBasictype().toString()));

        }
        if (StringUtil.isNotBlank(dataBean.getGroundtype())) {
            addCallbackParam("groundtype",
                    codeservice.getItemTextByCodeName("场地土类别", dataBean.getGroundtype().toString()));

        }
        if (StringUtil.isNotBlank(dataBean.getBuildgroundtype())) {
            addCallbackParam("buildgroundtype",
                    codeservice.getItemTextByCodeName("场地类别", dataBean.getBuildgroundtype().toString()));

        }
        if (StringUtil.isNotBlank(dataBean.getGroundhandletype())) {
            addCallbackParam("groundhandletype",
                    codeservice.getItemTextByCodeName("地基处理方法", dataBean.getGroundhandletype().toString()));

        }
        if (StringUtil.isNotBlank(dataBean.getJikengtype())) {
            addCallbackParam("jikengtype",
                    codeservice.getItemTextByCodeName("基坑类别", dataBean.getJikengtype().toString()));

        }
        if (StringUtil.isNotBlank(dataBean.getKztype())) {
            addCallbackParam("kztype",
                    codeservice.getItemTextByCodeName("抗震设防类别", dataBean.getKztype().toString()));

        }
        if (StringUtil.isNotBlank(dataBean.getKzlevel())) {
            addCallbackParam("kzlevel",
                    codeservice.getItemTextByCodeName("抗震设防烈度", dataBean.getKzlevel().toString()));

        }
        if (StringUtil.isNotBlank(dataBean.getFirelevel())) {
            addCallbackParam("firelevel",
                    codeservice.getItemTextByCodeName("耐火等级", dataBean.getFirelevel().toString()));

        }
        if (StringUtil.isNotBlank(dataBean.getGivewatertype())) {
            addCallbackParam("givewatertype",
                    codeservice.getItemTextByCodeName("给水方式", dataBean.getGivewatertype().toString()));

        }
        if (StringUtil.isNotBlank(dataBean.getHeatingtype())) {
            addCallbackParam("heatingtype",
                    codeservice.getItemTextByCodeName("采暖方式", dataBean.getHeatingtype().toString()));

        }
        if (StringUtil.isNotBlank(dataBean.getKongtiaotype())) {
            addCallbackParam("kongtiaotype",
                    codeservice.getItemTextByCodeName("空调方式", dataBean.getKongtiaotype().toString()));

        }
        if (StringUtil.isNotBlank(dataBean.getLighttype())) {
            addCallbackParam("lighttype",
                    codeservice.getItemTextByCodeName("照明方式", dataBean.getLighttype().toString()));

        }
        if (StringUtil.isNotBlank(dataBean.getGreenbuildingnorm())) {
            addCallbackParam("greenbuildingnorm",
                    codeservice.getItemTextByCodeName("绿色建筑设计标准", dataBean.getGreenbuildingnorm().toString()));

        }
        if (StringUtil.isNotBlank(dataBean.getChangetype())) {
            addCallbackParam("changetype",
                    codeservice.getItemTextByCodeName("装修改造工程类型", dataBean.getChangetype().toString()));

        }
        if (StringUtil.isNotBlank(dataBean.getProjectuseage())) {
            addCallbackParam("projectuseage",
                    codeservice.getItemTextByCodeName("工程用途", dataBean.getProjectuseage().toString()));

        }
        if (StringUtil.isNotBlank(dataBean.getFiredevice())) {
            addCallbackParam("firedevice",
                    codeservice.getItemTextByCodeName("消防设施种类", dataBean.getFiredevice().toString()));

        }
        if (StringUtil.isNotBlank(dataBean.getUsecraft())) {
            addCallbackParam("usecraft",
                    codeservice.getItemTextByCodeName("给水排水厂站工程采用工艺", dataBean.getUsecraft().toString()));

        }
        if (StringUtil.isNotBlank(dataBean.getRoadlevel())) {
            addCallbackParam("roadlevel",
                    codeservice.getItemTextByCodeName("城市道路等级", dataBean.getRoadlevel().toString()));

        }
        if (StringUtil.isNotBlank(dataBean.getRoadflyovertype())) {
            addCallbackParam("roadflyovertype",
                    codeservice.getItemTextByCodeName("立交型式", dataBean.getRoadflyovertype().toString()));

        }
        if (StringUtil.isNotBlank(dataBean.getBridgelinelevel())) {
            addCallbackParam("bridgelinelevel",
                    codeservice.getItemTextByCodeName("桥梁线路等级", dataBean.getBridgelinelevel().toString()));

        }
        if (StringUtil.isNotBlank(dataBean.getBridgestructuretype())) {
            addCallbackParam("bridgestructuretype",
                    codeservice.getItemTextByCodeName("桥梁结构型式", dataBean.getBridgestructuretype().toString()));

        }
        if (StringUtil.isNotBlank(dataBean.getBridgespantype())) {
            addCallbackParam("bridgespantype",
                    codeservice.getItemTextByCodeName("桥梁跨度型式", dataBean.getBridgespantype().toString()));

        }
        if (StringUtil.isNotBlank(dataBean.getTunnellinelevel())) {
            addCallbackParam("tunnellinelevel",
                    codeservice.getItemTextByCodeName("城市隧道工程线路等级", dataBean.getTunnellinelevel().toString()));
            
        }
        if (StringUtil.isNotBlank(dataBean.getHeatlinelevel())) {
            addCallbackParam("heatlinelevel",
                    codeservice.getItemTextByCodeName("热力工程管网线路热网等级", dataBean.getHeatlinelevel().toString()));
            
        }
    }

    public String getItemText(String itemvalue, String codename) {
        return codeservice.getItemTextByCodeName(codename,itemvalue);
    }

    public String getNameByCode(String code) {
        //显示父节点及子节点的内容
        String grandson = "";
        String son = "";
        String father = "";
        String grdfather = "";
        StringBuilder sb = new StringBuilder();
        String allRood = "";
        if (code.contains(",")) {
            String[] codes = code.split(",");
            for (String codevalue : codes) {
                if (StringUtil.isNotBlank(codevalue)) {
                    grandson = codeservice.getItemTextByCodeName( "项目分类",codevalue);

                    if (codevalue.length() > 2) {
                        grdfather = codeservice.getItemTextByCodeName( "项目分类",codevalue.substring(0, 2));
                    }
                    if (codevalue.length() > 4) {
                        father = codeservice.getItemTextByCodeName( "项目分类",codevalue.substring(0, 4));
                    }
                    if (codevalue.length() > 6) {
                        son = codeservice.getItemTextByCodeName( "项目分类",codevalue.substring(0, 6));
                    }
                    if (codevalue.length() == 8) {
                        allRood = grdfather+"·"+father + "·" + son + "·" + grandson;
                    }
                    if (codevalue.length() == 6) {
                        allRood = grdfather+"·"+father + "·" + grandson;
                    }
                    if (codevalue.length() == 4) {
                        allRood = grdfather+"·"+grandson;
                    }
                }

                sb.append(allRood);
                sb.append(";");
            }
        }
        else {
            if (StringUtil.isNotBlank(code)) {
                grandson = codeservice.getItemTextByCodeName( "项目分类",code);

                if (code.length() > 2) {
                    grdfather = codeservice.getItemTextByCodeName( "项目分类",code.substring(0, 2));
                }
                if (code.length() > 2) {
                    father = codeservice.getItemTextByCodeName( "项目分类",code.substring(0, 2));
                }
                if (code.length() > 4) {
                    father = codeservice.getItemTextByCodeName( "项目分类",code.substring(0, 4));
                }
                if (code.length() > 6) {
                    son = codeservice.getItemTextByCodeName( "项目分类",code.substring(0, 6));
                }
                if (code.length() == 8) {
                    return grdfather+"·"+father + "·" + son + "·" + grandson;
                }
                if (code.length() == 6) {
                    return grdfather+"·"+father + "·" + grandson;
                }
                if (code.length() == 4) {
                    return grdfather+"·"+grandson;
                }
                else {
                    return grandson;
                }
            }
        }

        return sb.toString().substring(0, sb.toString().length() - 1);
    }

    public DantiInfo getDataBean() {
        return dataBean;
    }

    public void setDataBean(DantiInfo dataBean) {
        this.dataBean = dataBean;
    }

    public DantiInfoV3 getThreeBean() {
        return threeBean;
    }

    public void setThreeBean(DantiInfoV3 threeBean) {
        this.threeBean = threeBean;
    }
}
