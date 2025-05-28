package com.epoint.basic.auditsp.dantiinfo.controls;

import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.auditsp.dantiinfo.api.IDantiInfoService;
import com.epoint.basic.auditsp.dantiinfo.entity.DantiInfo;
import com.epoint.basic.auditsp.dantisubrelation.api.IDantiSubRelationService;
import com.epoint.basic.auditsp.dantisubrelation.entity.DantiSubRelation;
import com.epoint.basic.auditsp.dwgcinfo.api.IDwgcInfoService;
import com.epoint.basic.auditsp.dwgcinfo.entity.DwgcInfo;
import com.epoint.basic.controller.BaseController;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * 单位控件
 *
 * @author Lee
 * @version [版本号, 2017-09-13 17:51:06]
 */
@RestController("jndanticontrolsaction")
@Scope("request")
public class JnDantiControlsAction extends BaseController {

    private static final long serialVersionUID = 1L;
    @Autowired
    private IDantiInfoService dantiInfoService;
    @Autowired
    private IDwgcInfoService dwgcInfoService;
    @Autowired
    private IDantiSubRelationService dantiSubRelationService;
    @Autowired
    private ICodeItemsService codeservice;

    @Autowired
    private IAttachService attachService;

    private String subappguid;

    @Override
    public void pageLoad() {

    }

    public String getDantiData(String subappguidtemp, String itemguidtemp) {
        JSONObject jsonMaterials = new JSONObject();
        List<JSONObject> materialList = new ArrayList<JSONObject>();
        subappguid = subappguidtemp;
        //返回防雷装置设计设计审核中的建设规模字段值
        List<DantiInfo> dantiinfolist = null;
        dantiinfolist = dantiInfoService.findListByProjectguid(itemguidtemp);
        //建筑单体栋数
        //总建筑面积
        Double zjzmjz = 0.0;
        //最高高度
        Double zgjzgd = 0.0;
        for (DantiInfo dantiInfo : dantiinfolist) {
            if (dantiInfo.getJzgd() != null && dantiInfo.getJzgd() > zgjzgd) {
                zgjzgd = dantiInfo.getJzgd();
            }
            if (dantiInfo.getZjzmj() != null) {
                zjzmjz += dantiInfo.getZjzmj();
            }
        }

        List<DantiInfo> alldantiInfo = dantiInfoService.findListBySubAppguid(subappguid);
        for (DantiInfo dantiInfo : alldantiInfo) {
            JSONObject jsonMaterial = new JSONObject();
            jsonMaterial.put("rowguid", dantiInfo.getRowguid());
            jsonMaterial.put("phaseguid", dantiInfo.getPhaseguid());
            jsonMaterial.put("dantiguid", dantiInfo.getRowguid());
            jsonMaterial.put("dantiname", dantiInfo.getDantiname());
            jsonMaterial.put("fllb", dantiInfo.getFllb());
            if (StringUtil.isNotBlank(dantiInfo.getGclb())) {
                String name = getNameByCode(dantiInfo.getGclb());
                jsonMaterial.put("gclb", name);
            }
            if (StringUtil.isNotBlank(dantiInfo.getGcxz())) {
                if (dantiInfo.getGcxz().contains(",")) {
                    String[] list = dantiInfo.getGcxz().split(",");
                    StringBuilder sb = new StringBuilder();
                    for (String code : list) {
                        String name = getItemText(code, "工程性质");
                        sb.append(name);
                        sb.append(";");
                    }
                    jsonMaterial.put("gcxz", sb.toString().substring(0, sb.toString().length() - 1));
                } else {
                    jsonMaterial.put("gcxz", getItemText(dantiInfo.getGcxz(), "工程性质"));
                }

            }
            jsonMaterial.put("jzdts", dantiinfolist.size());
            jsonMaterial.put("zjzmjz", zjzmjz);
            jsonMaterial.put("zgjzgd", zgjzgd);
            jsonMaterial.put("zzmj", dantiInfo.getZzmj());
            jsonMaterial.put("zjzmj", dantiInfo.getZjzmj());
            jsonMaterial.put("gjmj", dantiInfo.getGjmj());
            jsonMaterial.put("dxgjmj", dantiInfo.getDxgjmj());
            jsonMaterial.put("dxckmj", dantiInfo.getDxckmj());
            jsonMaterial.put("dishangmianji", dantiInfo.getDishangmianji());
            jsonMaterial.put("dixiamianji", dantiInfo.getDixiamianji());
            jsonMaterial.put("jzgd", dantiInfo.getJzgd());
            jsonMaterial.put("dscs", dantiInfo.getDscs());
            jsonMaterial.put("dxcs", dantiInfo.getDxcs());
            jsonMaterial.put("price", dantiInfo.getPrice());
            jsonMaterial.put("gcguid", dantiInfo.getGongchengguid());
            jsonMaterial.put("rguid", dantiInfo.getStr("rguid"));

            if (StringUtil.isNotBlank(dantiInfo.getStr("dtbm"))) {
                jsonMaterial.put("dtbm", dantiInfo.getStr("dtbm"));
            } else {
                if ("0".equals(dantiInfo.getStr("isfm"))) {
                    jsonMaterial.put("dtbm", "赋码中");
                }
            }

            //新逻辑，查询zjurl 展示预览按钮
            jsonMaterial.put("zjurl", dantiInfo.getStr("zjurl"));

            DwgcInfo dwgcInfo;
            if (StringUtil.isNotBlank(dantiInfo.getGongchengguid())) {
                dwgcInfo = dwgcInfoService.find(dantiInfo.getGongchengguid());
                jsonMaterial.put("gongchengname", dwgcInfo.getGongchengname());
            }
            materialList.add(jsonMaterial);
        }
        jsonMaterials.put("dantidata", materialList);
        return jsonMaterials.toString();
    }

    public String getDwData(String dwTypesStr) {

        String subappguid = getRequestParameter("subappguid");

        String dwTypes[] = dwTypesStr.split(",");
        JSONObject jsonMaterials = new JSONObject();

        for (String dwType : dwTypes) {

            switch (dwType) {
                case "jsdw":
                    jsonMaterials.put(dwType + "data", getJsdwJsonList(subappguid));
                    jsonMaterials.put(dwType + "dataflg", "1");

                    break;
                case "sgdw":
                    jsonMaterials.put(dwType + "data", getJsdwJsonList(subappguid));
                    jsonMaterials.put(dwType + "dataflg", "1");

                    break;
                case "sjdw":
                    jsonMaterials.put(dwType + "data", getJsdwJsonList(subappguid));
                    jsonMaterials.put(dwType + "dataflg", "1");

                    break;
                case "kcdw":
                    jsonMaterials.put(dwType + "data", getJsdwJsonList(subappguid));
                    jsonMaterials.put(dwType + "dataflg", "1");

                    break;
                case "jldw":
                    jsonMaterials.put(dwType + "data", getJsdwJsonList(subappguid));
                    jsonMaterials.put(dwType + "dataflg", "1");

                    break;
                default:
                    break;
            }

        }

        return jsonMaterials.toString();
    }

    /**
     * 根据subappguid获取建设单位
     */
    public List<JSONObject> getJsdwJsonList(String subappguid) {
        List<JSONObject> materialList = new ArrayList<JSONObject>();

        JSONObject jsonMaterial = new JSONObject();
        jsonMaterial.put("corpname", "国泰新点国泰新点国泰新点国泰新点国泰新点");
        jsonMaterial.put("xmfzr", "宋小宝");
        jsonMaterial.put("xmfzr_phone", "1688686");
        jsonMaterial.put("legal", "宋小宝");
        jsonMaterial.put("phone", "1688686");

        materialList.add(jsonMaterial);
        return materialList;
    }

    public void deletesonproj(String guid) {
        dantiSubRelationService.deleteByGuid(guid);
        //TODO 自建单体删除功能 通过relation中的rowgui找到dantiguid 然后去删除danti_info表数据
        addCallbackParam("msg", "成功删除！");
    }

    public void deletedanti(String guid) {
        List<DantiSubRelation> dantiSubRelations = dantiSubRelationService
                .findListByDantiGuid(guid);
        for (DantiSubRelation dantiSubRelation : dantiSubRelations) {
            dantiSubRelationService.deleteByGuid(dantiSubRelation.getRowguid());
        }
        dantiInfoService.deleteByGuid(guid);
        addCallbackParam("msg", "成功删除！");
    }

    public String getNameByCode(String code) {
        //显示父节点及子节点的内容
        String grandson = "";
        String son = "";
        String father = "";
        StringBuilder sb = new StringBuilder();
        String allRood = "";
        if (code.contains(",")) {
            String[] codes = code.split(",");
            for (String codevalue : codes) {
                if (StringUtil.isNotBlank(codevalue)) {
                    grandson = codeservice.getItemTextByCodeName("项目分类", codevalue);

                    if (codevalue.length() > 4) {
                        father = codeservice.getItemTextByCodeName("项目分类", codevalue.substring(0, 4));
                    }
                    if (codevalue.length() > 6) {
                        son = codeservice.getItemTextByCodeName("项目分类", codevalue.substring(0, 6));
                    }
                    if (codevalue.length() == 8) {
                        allRood = father + "·" + son + "·" + grandson;
                    }
                    if (codevalue.length() == 6) {
                        allRood = father + "·" + grandson;
                    }
                    if (codevalue.length() == 4) {
                        allRood = grandson;
                    }
                }

                sb.append(allRood);
                sb.append(";");
            }
        } else {
            if (StringUtil.isNotBlank(code)) {
                grandson = codeservice.getItemTextByCodeName("项目分类", code);

                if (code.length() > 4) {
                    father = codeservice.getItemTextByCodeName("项目分类", code.substring(0, 4));
                }
                if (code.length() > 6) {
                    son = codeservice.getItemTextByCodeName("项目分类", code.substring(0, 6));
                }
                if (code.length() == 8) {
                    return father + "·" + son + "·" + grandson;
                }
                if (code.length() == 6) {
                    return father + "·" + grandson;
                }
                if (code.length() == 4) {
                    return grandson;
                } else {
                    return grandson;
                }
            }
        }

        return sb.toString().substring(0, sb.toString().length() - 1);
    }

    public String getItemText(String itemvalue, String codename) {
        String itemText = codeservice.getItemTextByCodeName(codename, itemvalue);
        return itemText;
    }
}
