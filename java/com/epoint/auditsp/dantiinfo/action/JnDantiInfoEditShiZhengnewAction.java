package com.epoint.auditsp.dantiinfo.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.auditsp.dantiinfo.api.IDantiInfoService;
import com.epoint.basic.auditsp.dantiinfo.entity.DantiInfo;
import com.epoint.basic.common.projectscale.entity.CodeProjectScale;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.frame.service.metadata.code.entity.CodeItems;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.xmz.thirdreporteddata.dantiinfov3.api.IDantiInfoV3Service;
import com.epoint.xmz.thirdreporteddata.dantiinfov3.api.entity.DantiInfoV3;

/**
 * 单体信息表（单体同子单位工程）修改页面对应的后台
 * 
 * @author WIN-H366O37KOW0$
 * @version [版本号, 2018-05-17 21:59:10]
 */
@RightRelation(DantiInfoListAction.class)
@RestController("jndantiinfoeditshizhengnewaction")
@Scope("request")
public class JnDantiInfoEditShiZhengnewAction extends BaseController
{
    private static final long serialVersionUID = 6062888099433221813L;

    @Autowired
    private IDantiInfoService service;
    @Autowired
    private IDantiInfoV3Service iDantiInfoV3Service;

    /**
     * 单体信息表（单体同子单位工程）实体对象
     */
    private DantiInfo dataBean = null;

    private DantiInfoV3 threeBean = null;

    @Autowired
    private ICodeItemsService codeservice;
//    @Autowired
//    private ICodeProjectScaleService codeProjectScaleService;

    public void pageLoad() {
        String guid = getRequestParameter("guid");
        dataBean = service.find(guid);
        if (dataBean == null) {
            dataBean = new DantiInfo();
        }
        threeBean = iDantiInfoV3Service.find(guid);
        if (threeBean == null) {
            threeBean = new DantiInfoV3();
        }
        String gclbcode = dataBean.getGclb();
        String gclbname = getNameByCode(gclbcode);
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
        addCallbackParam("gclbname", gclbname);
        addCallbackParam("gclbcode", gclbcode);
    }

    public void saveArea(String dsjzmj, String dxjzmj, String zjzmj, String rowguid) {
        dataBean = service.find(getRequestParameter("guid"));
        Pattern pattern = Pattern.compile("-?[0-9]+.?[0-9]+");
        Matcher isNum = pattern.matcher(dsjzmj);
        if (isNum.matches()) {
            dataBean.setZzmj(Double.parseDouble(dsjzmj));
        }
        else {
            dataBean.setZzmj(0.0);
        }
        isNum = pattern.matcher(dxjzmj);
        if (isNum.matches()) {
            dataBean.setBdxjzmj(Double.parseDouble(dxjzmj));
        }
        else {
            dataBean.setBdxjzmj(0.0);
        }
        isNum = pattern.matcher(zjzmj);
        if (isNum.matches()) {
            dataBean.setZjzmj(Double.parseDouble(zjzmj));
        }
        else {
            dataBean.setZjzmj(0.0);
        }
        service.update(dataBean);
    }

    public void saveProjectScale(String value, String text, String danwei, String rowguid) {
        CodeProjectScale codeProjectScale = new CodeProjectScale();
        if (StringUtil.isNotBlank(getRequestParameter("guid"))) {
            codeProjectScale.setDantiguid(getRequestParameter("guid"));
        }
        if (StringUtil.isNotBlank(text)) {
            codeProjectScale.setCodetext(text);
        }
        if (StringUtil.isNotBlank(danwei)) {
            codeProjectScale.setDanwei(danwei);
        }
        if (StringUtil.isNotBlank(value)) {
            codeProjectScale.setScale(value);
        }
        String[] codetext = text.split("·");
        CodeItems codeitems = codeservice.getCodeItemByCodeName("项目分类",
                codetext[codetext.length - 1].substring(0, codetext[codetext.length - 1].length() - 4));
        if (codeitems != null && StringUtil.isNotBlank(codeitems.getItemValue())) {
            codeProjectScale.setCodevalue(codeitems.getItemValue());
        }
        codeProjectScale.setRowguid(UUID.randomUUID().toString());
//        codeProjectScaleService.insert(codeProjectScale);

    }

    /**
     * 保存修改
     * 
     */
    public void save() {
        // 字段设置值
        JnDantiInfoUtil.setDantiInfoFromV3(dataBean, threeBean);

        threeBean.setOperatedate(new Date());
        threeBean.setOperateusername(userSession.getDisplayName());

        dataBean.setOperatedate(new Date());
        dataBean.setOperateusername(userSession.getDisplayName());

        // 是否需要赋码
        /*String result = JnDantiInfoUtil.handleDantiFm(threeBean, dataBean, dataBean.getRowguid(),
                dataBean.getProjectguid());
        if (StringUtil.isNotBlank(result)) {
            addCallbackParam("error", result);
            return;
        }*/

        service.update(dataBean);
        iDantiInfoV3Service.update(threeBean);

        addCallbackParam("msg", "修改成功！");
//        List<CodeProjectScale> codeProjectScales = codeProjectScaleService
//                .findList("select*from code_project_scale where dantiguid=?", getRequestParameter("guid"));
//        for (CodeProjectScale codeProjectScale : codeProjectScales) {
//            codeProjectScaleService.deleteByGuid(codeProjectScale.getRowguid());
//        }
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

    // 新增
    public List<SelectItem> getHeatlinelevel() {
        List<SelectItem> result = new ArrayList<SelectItem>();
        List<CodeItems> codeItems = codeservice.listCodeItemsByCodeName("热力工程管网线路热网等级");
        for (CodeItems codeItem : codeItems) {
            result.add(new SelectItem(codeItem.getItemValue(), codeItem.getItemText()));
        }
        return result;
    }
  //新增
    public List<SelectItem> getTunnellinelevel() {
        List<SelectItem> result = new ArrayList<SelectItem>();
        List<CodeItems> codeItems = codeservice.listCodeItemsByCodeName("城市隧道工程线路等级");
        for (CodeItems codeItem : codeItems) {
            result.add(new SelectItem(codeItem.getItemValue(), codeItem.getItemText()));
        }
        return result;
    }
  //新增
    public List<SelectItem> getBridgelinelevel() {
        List<SelectItem> result = new ArrayList<SelectItem>();
        List<CodeItems> codeItems = codeservice.listCodeItemsByCodeName("桥梁线路等级");
        for (CodeItems codeItem : codeItems) {
            result.add(new SelectItem(codeItem.getItemValue(), codeItem.getItemText()));
        }
        return result;
    }
  //新增
    public List<SelectItem> getBridgespantype() {
        List<SelectItem> result = new ArrayList<SelectItem>();
        List<CodeItems> codeItems = codeservice.listCodeItemsByCodeName("桥梁跨度型式");
        for (CodeItems codeItem : codeItems) {
            result.add(new SelectItem(codeItem.getItemValue(), codeItem.getItemText()));
        }
        return result;
    }
  //新增
    public List<SelectItem> getBridgestructuretype() {
        List<SelectItem> result = new ArrayList<SelectItem>();
        List<CodeItems> codeItems = codeservice.listCodeItemsByCodeName("桥梁结构型式");
        for (CodeItems codeItem : codeItems) {
            result.add(new SelectItem(codeItem.getItemValue(), codeItem.getItemText()));
        }
        return result;
    }
  //新增
    public List<SelectItem> getRoadflyovertype() {
        List<SelectItem> result = new ArrayList<SelectItem>();
        List<CodeItems> codeItems = codeservice.listCodeItemsByCodeName("立交型式");
        for (CodeItems codeItem : codeItems) {
            result.add(new SelectItem(codeItem.getItemValue(), codeItem.getItemText()));
        }
        return result;
    }
  //新增
    public List<SelectItem> getRoadlevel() {
        List<SelectItem> result = new ArrayList<SelectItem>();
        List<CodeItems> codeItems = codeservice.listCodeItemsByCodeName("城市道路等级");
        for (CodeItems codeItem : codeItems) {
            result.add(new SelectItem(codeItem.getItemValue(), codeItem.getItemText()));
        }
        return result;
    }
    //新增
    public List<SelectItem> getUsecraft() {
        List<SelectItem> result = new ArrayList<SelectItem>();
        List<CodeItems> codeItems = codeservice.listCodeItemsByCodeName("给水排水厂站工程采用工艺");
        for (CodeItems codeItem : codeItems) {
            result.add(new SelectItem(codeItem.getItemValue(), codeItem.getItemText()));
        }
        return result;
    }
    public List<SelectItem> getCxscModel() {
        List<SelectItem> result = new ArrayList<SelectItem>();
        List<CodeItems> codeItems = codeservice.listCodeItemsByCodeName("是否");
        for (CodeItems codeItem : codeItems) {
            result.add(new SelectItem(codeItem.getItemValue(), codeItem.getItemText()));
        }
        return result;
    }
    
    public List<SelectItem> getJgjcModel() {
        List<SelectItem> result = new ArrayList<SelectItem>();
        List<CodeItems> codeItems = codeservice.listCodeItemsByCodeName("是否");
        for (CodeItems codeItem : codeItems) {
            result.add(new SelectItem(codeItem.getItemValue(), codeItem.getItemText()));
        }
        return result;
    }
    
    public List<SelectItem> getRfqkModel() {
        List<SelectItem> result = new ArrayList<SelectItem>();
        List<CodeItems> codeItems = codeservice.listCodeItemsByCodeName("有无");
        for (CodeItems codeItem : codeItems) {
            result.add(new SelectItem(codeItem.getItemValue(), codeItem.getItemText()));
        }
        return result;
    }

    public List<SelectItem> getTzlxModel() {
        List<SelectItem> result = new ArrayList<SelectItem>();
        List<CodeItems> codeItems = codeservice.listCodeItemsByCodeName("投资类型");
        for (CodeItems codeItem : codeItems) {
            result.add(new SelectItem(codeItem.getItemValue(), codeItem.getItemText()));
        }
        return result;
    }

    public List<SelectItem> getGcxzModel() {
        List<SelectItem> result = new ArrayList<SelectItem>();
        List<CodeItems> codeItems = codeservice.listCodeItemsByCodeName("工程性质");
        for (CodeItems codeItem : codeItems) {
            result.add(new SelectItem(codeItem.getItemValue(), codeItem.getItemText()));
        }
        return result;
    }

    public List<SelectItem> getJgxsModel() {
        List<SelectItem> result = new ArrayList<SelectItem>();
        List<CodeItems> codeItems = codeservice.listCodeItemsByCodeName("结构形式");
        for (CodeItems codeItem : codeItems) {
            result.add(new SelectItem(codeItem.getItemValue(), codeItem.getItemText()));
        }
        return result;
    }

    public List<SelectItem> getFllbModel() {
        List<SelectItem> result = new ArrayList<SelectItem>();
        List<CodeItems> codeItems = codeservice.listCodeItemsByCodeName("单体防雷类别");
        for (CodeItems codeItem : codeItems) {
            result.add(new SelectItem(codeItem.getItemValue(), codeItem.getItemText()));
        }
        return result;
    }
    public List<SelectItem> getSFModel() {
        List<SelectItem> result = new ArrayList<SelectItem>();
        List<CodeItems> codeItems = codeservice.listCodeItemsByCodeName("是否");
        for (CodeItems codeItem : codeItems) {
            result.add(new SelectItem(codeItem.getItemValue(), codeItem.getItemText()));
        }
        return result;
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

    public void showprojectsize(String gclbstext, String gclbsvalue) {

        if (StringUtil.isNotBlank(gclbstext)) {
            String[] gclbtexts = gclbstext.split(";");
            String[] gclbvalues = gclbsvalue.split(",");
            for (String value : gclbvalues) {
                if ("0211".equals(value)) {
                    addCallbackParam("includeArea", 1);
                }
            }
            String[] texts = new String[gclbtexts.length];
            for (int i = 0, j = 0; j < gclbtexts.length; i++, j++) {
                if (!"市政配套房建工程".equals(gclbtexts[j])) {
                    texts[i] = gclbtexts[j];
                }
                else {
                    i--;
                }
            }
            addCallbackParam("gclbtexts", texts);
            String[] danweis = new String[gclbtexts.length];
            for (int i = 0, j = 0; j < gclbvalues.length; i++, j++) {
                if (!"0211".equals(gclbvalues[j])) {
                    CodeItems codeItems = codeservice.getCodeItemByCodeName("项目分类",gclbvalues[j]);
                    String remark = codeItems.getDmAbr1();
                    if (remark == null) {
                        danweis[i] = " ";
                    }
                    else {
                        danweis[i] = remark;
                    }
                }
                else {
                    i--;
                }

            }
            addCallbackParam("danweis", danweis);
        }

    }

    private List<SelectItem> gbGclbModel;

    public List<SelectItem> getGbGclbModel() {
        String gcfl = getRequestParameter("gcfl");
        if (StringUtil.isNotBlank(gcfl)) {
            gcfl = String.format("%2s", gcfl).replaceAll(" ", "0");
        }
        if (gbGclbModel == null) {
            gbGclbModel = DataUtil.convertMap2ComboBox((List) CodeModalFactory.factory("下拉列表", "国标_工程类别", null, true));
            if (StringUtil.isNotBlank(gcfl)) {
                Iterator<SelectItem> iterator = gbGclbModel.iterator();
                while (iterator.hasNext()) {
                    SelectItem next = iterator.next();
                    if (!next.getValue().toString().startsWith(gcfl)) {
                        iterator.remove();
                    }
                }
            }
        }
        return gbGclbModel;
    }

    private List<SelectItem> gbJgtxModel;

    public List<SelectItem> getGbJgtxModel() {
        if (gbJgtxModel == null) {
            gbJgtxModel = DataUtil.convertMap2ComboBox((List) CodeModalFactory.factory("下拉列表", "国标_结构体系", null, true));
        }
        return gbJgtxModel;
    }

    private List<SelectItem> gbNhdjModel;

    public List<SelectItem> getGbNhdjModel() {
        if (gbNhdjModel == null) {
            gbNhdjModel = DataUtil.convertMap2ComboBox((List) CodeModalFactory.factory("下拉列表", "国标_耐火等级", null, true));
        }
        return gbNhdjModel;
    }

    private List<SelectItem> gbJzfsModel;

    public List<SelectItem> getGbJzfsModel() {
        if (gbJzfsModel == null) {
            gbJzfsModel = DataUtil.convertMap2ComboBox((List) CodeModalFactory.factory("下拉列表", "国标_建造方式", null, true));
        }
        return gbJzfsModel;
    }
}
