package com.epoint.auditsp.danwei.action;

import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.basedata.participantsinfo.api.IParticipantsInfoService;
import com.epoint.basic.basedata.participantsinfo.api.entity.ParticipantsInfo;
import com.epoint.basic.controller.BaseController;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 单位控件
 *
 * @author Lee
 * @version [版本号, 2017-09-13 17:51:06]
 */
@RestController("jndanweicontrolsaction")
@Scope("request")
public class JnDanweiControlsAction extends BaseController {

    private static final long serialVersionUID = 1L;

    @Autowired
    private IParticipantsInfoService participantsInfoService;

    @Autowired
    private ICodeItemsService codeItemService;

    @Override
    public void pageLoad() {

    }

    public void save() {
    }

    public void initDwData(String dwTypesStr, String itemguid, String subappguid) {
        String sql = "select * from participants_info where corptype = ? and subappguid = ? order by operatedate desc ";
        ParticipantsInfo info = participantsInfoService.find(sql, "999", subappguid);

        //参见单位是否初始化判断
        if (info == null) {
            info = new ParticipantsInfo();
            info.setOperatedate(new Date());
            info.setRowguid(UUID.randomUUID().toString());
            info.setSubappguid(subappguid);
            info.setCorptype("999");
            info.setCorpname("系统生成");

            String dwTypes[] = dwTypesStr.split(",");
            for (String dwType : dwTypes) {
                if ("jsdw".equals(dwType)) {
                    continue;
                }
                /* 3.0新增逻辑 */
                List<ParticipantsInfo> list = participantsInfoService.findList(
                        "select distinct jsdwlx,xkbabh,cert,legalcardtype,fzrzjlx,corptype,danweilxrsfz,fbsafenum,cbdanweitype,fbtime,fbscopeofcontract,fbqysettime,fbaqglry,fbaqglrysafenum,xmfzperson,xmfzrsafenum,qylxr,qylxdh,gdlxr,gdlxdh,xmfzrphonenum,danweilxr,danweilxrlxdh, corpname, corpcode, legal, phone, address, cert, xmfzr, xmfzr_idcard, xmfzr_zc, xmfzr_phone, xmfzr_certlevel, xmfzr_certnum, xmfzr_certid, jsfzr, jsfzr_zc, jsfzr_phone, itemguid ,fremail ,frphone ,legalpersonicardnum,itemlegaldept,itemlegalcertnum,itemlegalcerttype,legalproperty  from participants_info where itemguid = ? and corptype= ?",
                        itemguid, getDanweiNum(dwType));
                /* 3.0结束逻辑 */
                for (ParticipantsInfo participantsInfo : list) {
                    participantsInfo.setRowguid(UUID.randomUUID().toString());
                    participantsInfo.setOperatedate(new Date());
                    participantsInfo.setSubappguid(subappguid);
                    participantsInfoService.insert(participantsInfo);
                }
            }
            participantsInfoService.insert(info);
        }

    }

    private String getDanweiNum(String dwType) {
        String danweiNum = "";
        if (StringUtil.isNotBlank(dwType)) {
            switch (dwType) {
                case "jsdw":
                    danweiNum = "31";
                    break;
                case "sgdw":
                    danweiNum = "3";
                    break;
                case "sjdw":
                    danweiNum = "2";
                    break;
                case "kcdw":
                    danweiNum = "1";
                    break;
                case "jldw":
                    danweiNum = "4";
                    break;
                case "jcdw":
                    danweiNum = "10";
                    break;
                default:
                    break;
            }
        }
        return danweiNum;
    }

    public String getDwData(String dwTypesStr, String itemguid, String subappguid) {
        String dwTypes[] = dwTypesStr.split(",");
        JSONObject jsonMaterials = new JSONObject();
        for (String dwType : dwTypes) {
            jsonMaterials.put(dwType + "data", getJsonList(getDanweiNum(dwType), itemguid, subappguid));
        }
        return jsonMaterials.toString();
    }

    /**
     * 根据corptype、subappguid获取单位
     * 添加itemguid为list是的查询
     */
    public List<JSONObject> getJsonList(String corptype, String itemguid, String subappguid) {
        List<JSONObject> materialList = new ArrayList<JSONObject>();
        List<ParticipantsInfo> list = new ArrayList<>();
        if (StringUtil.isNotBlank(subappguid)) {
            if ("31".equals(corptype)) {
                String sql = "select * from participants_info where corptype = ? and itemguid = ?";
                list = participantsInfoService.findList(sql, corptype, itemguid);
            } else {
                String sql = "select * from participants_info where corptype = ? and subappguid = ?";
                list = participantsInfoService.findList(sql, corptype, subappguid);
            }

            for (ParticipantsInfo info : list) {
                JSONObject jsonMaterial = new JSONObject();
                jsonMaterial.put("corpname", info.getCorpname());
                jsonMaterial.put("itemlegaldept", info.getItemlegaldept());
                jsonMaterial.put("xmfzr", info.getXmfzr());
                jsonMaterial.put("xmfzr_phone", info.getXmfzr_phone());
                jsonMaterial.put("legal", info.getLegal());
                jsonMaterial.put("phone", info.getPhone());

                jsonMaterial.put("rowguid", info.getRowguid());
                jsonMaterial.put("danweitype", info.getCorptype());
                String codeitems = "";
                if (StringUtil.isNotBlank(info.getCbdanweitype())) {
                    codeitems = codeItemService.getItemTextByCodeName("单位类型", info.getCbdanweitype());
                }
                jsonMaterial.put("cbdanweitype", codeitems);
                jsonMaterial.put("xmfzperson", info.getXmfzperson());
                jsonMaterial.put("xmfzrsafenum", info.getXmfzrsafenum());
                jsonMaterial.put("xmfzrphonenum", info.getXmfzrphonenum());
                jsonMaterial.put("cbdanweitype", codeitems);
                materialList.add(jsonMaterial);
            }
        } else {
//            String item[] = itemguid.split(",");
//            if(item.length==0){
//                return materialList;
//            }
            String sql = "select * from participants_info where corptype = ? and itemguid = ?";
            list = participantsInfoService.findList(sql, corptype, itemguid);
            for (ParticipantsInfo info : list) {
                JSONObject jsonMaterial = new JSONObject();
                jsonMaterial.put("corpname", info.getCorpname());
                jsonMaterial.put("xmfzr", info.getXmfzr());
                jsonMaterial.put("xmfzr_phone", info.getXmfzr_phone());
                jsonMaterial.put("legal", info.getLegal());
                jsonMaterial.put("phone", info.getPhone());

                jsonMaterial.put("rowguid", info.getRowguid());
                jsonMaterial.put("danweitype", info.getCorptype());
                String codeitems = "";
                if (StringUtil.isNotBlank(info.getCbdanweitype())) {
                    codeitems = codeItemService.getItemTextByCodeName("单位类型", info.getCbdanweitype());
                }
                jsonMaterial.put("cbdanweitype", codeitems);
                jsonMaterial.put("xmfzperson", info.getXmfzperson());
                jsonMaterial.put("xmfzrsafenum", info.getXmfzrsafenum());
                jsonMaterial.put("xmfzrphonenum", info.getXmfzrphonenum());
                jsonMaterial.put("cbdanweitype", codeitems);
                materialList.add(jsonMaterial);
            }
        }

        return materialList;
    }

    /**
     * 删除当前
     */
    public void deleteOneDanwei(String guid, String num) {

        try {
            participantsInfoService.deleteByGuid(guid);
            addCallbackParam("msg", "成功删除！");
        } catch (Exception e) {
            addCallbackParam("msg", "删除失败！");
            e.printStackTrace();
        }

        addCallbackParam("num", num);

    }
}
