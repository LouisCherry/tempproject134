package com.epoint.xmz.thirdreporteddata.auditsp.controls.danti.action;

import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.auditsp.dantisubrelation.api.IDantiSubRelationService;
import com.epoint.basic.auditsp.dantisubrelation.entity.DantiSubRelation;
import com.epoint.basic.auditsp.dwgcinfo.api.IDwgcInfoService;
import com.epoint.basic.auditsp.dwgcinfo.entity.DwgcInfo;
import com.epoint.basic.controller.BaseController;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.frame.service.metadata.code.entity.CodeItems;
import com.epoint.xmz.thirdreporteddata.dantiinfov3.api.IDantiInfoV3Service;
import com.epoint.xmz.thirdreporteddata.dantiinfov3.api.entity.DantiInfoV3;
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
@RestController("danticontrolsv3action")
@Scope("request")
public class DantiControlsV3Action extends BaseController {

    private static final long serialVersionUID = 1L;
    @Autowired
    private IDantiInfoV3Service dantiInfoService;
    @Autowired
    private ICodeItemsService iCodeItemsService;

    @Autowired
    private IDwgcInfoService dwgcInfoService;
    @Autowired
    private IDantiSubRelationService dantiSubRelationService;
    //    @Autowired
    //    private ITsProjectinfoService projectinfoservice;
    @Autowired
    private ICodeItemsService codeservice;
    private String subappguid;
    //    private TsProjectinfo dataBean = null;

    @Override
    public void pageLoad() {

    }

    //    public TsProjectinfo getProByPviGuid(String pviguid) {
    //        TsProjectinfo p = projectinfoservice.findProjectByPviGuid(pviguid);
    //        return p;
    //    }

    public String getDantiData(String subappguidtemp, String itemguidtemp) {
        JSONObject jsonMaterials = new JSONObject();
        List<JSONObject> materialList = new ArrayList<JSONObject>();

        //        if (StringUtil.isBlank(getRequestParameter("subappguid"))) {
        //            String processVersionInstanceGuid = getRequestParameter("ProcessVersionInstanceGuid");
        //            dataBean = getProByPviGuid(processVersionInstanceGuid);
        //            subappguid = dataBean.getSubappguid();
        //        }
        //        else {
        subappguid = subappguidtemp;
        //        }

        List<DantiInfoV3> alldantiInfo = dantiInfoService.findListBySubAppguid(subappguid);
        for (DantiInfoV3 dantiInfo : alldantiInfo) {
            JSONObject jsonMaterial = new JSONObject();
            jsonMaterial.put("rowguid", dantiInfo.getRowguid());
            jsonMaterial.put("dantiguid", dantiInfo.getRowguid());
            jsonMaterial.put("dtmc", dantiInfo.getDtmc());
            if (StringUtil.isNotBlank(dantiInfo.getGcyt())) {
                String name = getNameByCode(dantiInfo.getGcyt());
                jsonMaterial.put("GCYT", name);
            }
            jsonMaterial.put("JZMJ", dantiInfo.getZdmj());
            jsonMaterial.put("DSJZMJ", dantiInfo.getDsjzmj());
            jsonMaterial.put("DXJZMJ", dantiInfo.getDxjzmj());
            jsonMaterial.put("dscs", dantiInfo.getDscs());
            jsonMaterial.put("dxcs", dantiInfo.getDxcs());
            jsonMaterial.put("DTGCZZJ", dantiInfo.getDtgczzj());
            jsonMaterial.put("gcguid", dantiInfo.getGongchengguid());
            jsonMaterial.put("rguid", dantiInfo.getStr("rguid"));
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

    public void deletesonproj(String guid) {
        dantiSubRelationService.deleteByGuid(guid);
        addCallbackParam("msg", "成功删除！");
    }

    public void deletedanti(String guid) {
        List<DantiSubRelation> dantiSubRelations = dantiSubRelationService.findListByDantiGuid(guid);
        for (DantiSubRelation dantiSubRelation : dantiSubRelations) {
            dantiSubRelationService.deleteByGuid(dantiSubRelation.getRowguid());
        }
        dantiInfoService.deleteByGuid(guid);
        addCallbackParam("msg", "成功删除！");
    }

    public String getNameByCode(String code) {
        String result = "";
        CodeItems codeitem = iCodeItemsService.getCodeItemByCodeName("国标_工程类别", String.valueOf(code));
        if (codeitem != null) {
            result += codeitem.getItemText();
            String codefl = code.substring(0, 2).replaceAll("0", "");
            CodeItems codeitemfl = iCodeItemsService.getCodeItemByCodeName("国标_工程行业分类", String.valueOf(codefl));
            if (codeitemfl != null) {
                result = codeitemfl.getItemText() + "-" + result;
            }
        }
        return result;
    }

    ;

}
