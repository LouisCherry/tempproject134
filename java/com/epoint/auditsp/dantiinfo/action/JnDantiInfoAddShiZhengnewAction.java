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
import com.epoint.basic.common.projectscale.api.ICodeProjectScaleService;
import com.epoint.basic.common.projectscale.entity.CodeProjectScale;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.core.dto.base.TreeNode;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.dto.model.TreeModel;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.frame.service.metadata.code.entity.CodeItems;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.xmz.thirdreporteddata.dantiinfov3.api.IDantiInfoV3Service;
import com.epoint.xmz.thirdreporteddata.dantiinfov3.api.entity.DantiInfoV3;

/**
 * 单体信息表（单体同子单位工程）新增页面对应的后台
 * 
 * @author WIN-H366O37KOW0$
 * @version [版本号, 2018-05-17 21:59:10]
 */
@RightRelation(DantiInfoListAction.class)
@RestController("jndantiinfoaddshizhengnewaction")
@Scope("request")
public class JnDantiInfoAddShiZhengnewAction extends BaseController
{
    private static final long serialVersionUID = -203777081033666401L;

    @Autowired
    private IDantiInfoService service;
    @Autowired
    private IDantiInfoV3Service iDantiInfoV3Service;

    /**
     * 单体信息表（单体同子单位工程）实体对象
     */
    private DantiInfo dataBean = null;

    private DantiInfoV3 threeBean = null;

    private TreeModel treeModel;
    @Autowired
    private ICodeItemsService codeservice;
    @Autowired
    private ICodeProjectScaleService codeProjectScaleService;

    public void pageLoad() {
        dataBean = new DantiInfo();
        threeBean = new DantiInfoV3();
    }

    public void saveProjectScale(String value, String text, String danwei, String rowguid) {
        CodeProjectScale codeProjectScale = new CodeProjectScale();
        if (StringUtil.isNotBlank(rowguid)) {
            codeProjectScale.setDantiguid(rowguid);
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
        codeProjectScaleService.insert(codeProjectScale);

    }

    public void saveArea(String dsjzmj, String dxjzmj, String zjzmj, String rowguid) {
        dataBean = service.find(rowguid);
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

    /**
     * 保存并关闭
     * 
     */
    public void add(String shuliang) {
        String projectguid = getRequestParameter("projectguid");
        String phaseguid = getRequestParameter("phaseguid");
        int i = 0;
        int number = 1;
        if (StringUtil.isNotBlank(shuliang)) {
            number = Integer.parseInt(shuliang);
        }
        Date nowDate = new Date();
        for (i = 0; i < number; i++) {
            String dantiGuid = UUID.randomUUID().toString();
            addCallbackParam("rowguid", dantiGuid);
            // 字段设置值
            JnDantiInfoUtil.setDantiInfoFromV3(dataBean, threeBean);

            threeBean.setItemguid(projectguid);
            threeBean.setOperatedate(nowDate);
            threeBean.setOperateusername(userSession.getDisplayName());
            threeBean.setRowguid(dantiGuid);

            dataBean.setRowguid(dantiGuid);
            dataBean.setOperatedate(nowDate);
            dataBean.setOperateusername(userSession.getDisplayName());
            dataBean.setProjectguid(projectguid);
            if (StringUtil.isNotBlank(phaseguid)) {
                dataBean.setPhaseguid(phaseguid);
            }
            dataBean.setGongchengguid("");

            // 是否需要赋码
            /*String result = JnDantiInfoUtil.handleDantiFm(threeBean, dataBean, dantiGuid, projectguid);
            if (StringUtil.isNotBlank(result)) {
                addCallbackParam("error", result);
                return;
            }*/
            service.insert(dataBean);
            iDantiInfoV3Service.insert(threeBean);
        }
        //
        addCallbackParam("msg", "保存成功！");
        dataBean = null;
    }

    /**
     * 保存并新建
     * 
     */

    public DantiInfo getDataBean() {
        if (dataBean == null) {
            dataBean = new DantiInfo();
        }
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

    // 新增
    public List<SelectItem> getTunnellinelevel() {
        List<SelectItem> result = new ArrayList<SelectItem>();
        List<CodeItems> codeItems = codeservice.listCodeItemsByCodeName("城市隧道工程线路等级");
        for (CodeItems codeItem : codeItems) {
            result.add(new SelectItem(codeItem.getItemValue(), codeItem.getItemText()));
        }
        return result;
    }

    // 新增
    public List<SelectItem> getBridgelinelevel() {
        List<SelectItem> result = new ArrayList<SelectItem>();
        List<CodeItems> codeItems = codeservice.listCodeItemsByCodeName("桥梁线路等级");
        for (CodeItems codeItem : codeItems) {
            result.add(new SelectItem(codeItem.getItemValue(), codeItem.getItemText()));
        }
        return result;
    }

    // 新增
    public List<SelectItem> getBridgespantype() {
        List<SelectItem> result = new ArrayList<SelectItem>();
        List<CodeItems> codeItems = codeservice.listCodeItemsByCodeName("桥梁跨度型式");
        for (CodeItems codeItem : codeItems) {
            result.add(new SelectItem(codeItem.getItemValue(), codeItem.getItemText()));
        }
        return result;
    }

    // 新增
    public List<SelectItem> getBridgestructuretype() {
        List<SelectItem> result = new ArrayList<SelectItem>();
        List<CodeItems> codeItems = codeservice.listCodeItemsByCodeName("桥梁结构型式");
        for (CodeItems codeItem : codeItems) {
            result.add(new SelectItem(codeItem.getItemValue(), codeItem.getItemText()));
        }
        return result;
    }

    // 新增
    public List<SelectItem> getRoadflyovertype() {
        List<SelectItem> result = new ArrayList<SelectItem>();
        List<CodeItems> codeItems = codeservice.listCodeItemsByCodeName("立交型式");
        for (CodeItems codeItem : codeItems) {
            result.add(new SelectItem(codeItem.getItemValue(), codeItem.getItemText()));
        }
        return result;
    }

    // 新增
    public List<SelectItem> getRoadlevel() {
        List<SelectItem> result = new ArrayList<SelectItem>();
        List<CodeItems> codeItems = codeservice.listCodeItemsByCodeName("城市道路等级");
        for (CodeItems codeItem : codeItems) {
            result.add(new SelectItem(codeItem.getItemValue(), codeItem.getItemText()));
        }
        return result;
    }

    // 新增
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

    public List<SelectItem> getRfqkModel() {
        List<SelectItem> result = new ArrayList<SelectItem>();
        List<CodeItems> codeItems = codeservice.listCodeItemsByCodeName("有无");
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

    public List<SelectItem> getJgtxModel() {
        List<SelectItem> result = new ArrayList<SelectItem>();
        List<CodeItems> codeItems = codeservice.listCodeItemsByCodeName("结构体系");
        for (CodeItems codeItem : codeItems) {
            result.add(new SelectItem(codeItem.getItemValue(), codeItem.getItemText()));
        }
        return result;
    }

    public List<SelectItem> getDjjcModel() {
        List<SelectItem> result = new ArrayList<SelectItem>();
        List<CodeItems> codeItems = codeservice.listCodeItemsByCodeName("地基基础设计等级");
        for (CodeItems codeItem : codeItems) {
            result.add(new SelectItem(codeItem.getItemValue(), codeItem.getItemText()));
        }
        return result;
    }

    public List<SelectItem> getJcxsModel() {
        List<SelectItem> result = new ArrayList<SelectItem>();
        List<CodeItems> codeItems = codeservice.listCodeItemsByCodeName("基础型式");
        for (CodeItems codeItem : codeItems) {
            result.add(new SelectItem(codeItem.getItemValue(), codeItem.getItemText()));
        }
        return result;
    }

    public List<SelectItem> getCdtModel() {
        List<SelectItem> result = new ArrayList<SelectItem>();
        List<CodeItems> codeItems = codeservice.listCodeItemsByCodeName("场地土类别");
        for (CodeItems codeItem : codeItems) {
            result.add(new SelectItem(codeItem.getItemValue(), codeItem.getItemText()));
        }
        return result;
    }

    public List<SelectItem> getCdlbModel() {
        List<SelectItem> result = new ArrayList<SelectItem>();
        List<CodeItems> codeItems = codeservice.listCodeItemsByCodeName("场地类别");
        for (CodeItems codeItem : codeItems) {
            result.add(new SelectItem(codeItem.getItemValue(), codeItem.getItemText()));
        }
        return result;
    }

    public List<SelectItem> getDjclModel() {
        List<SelectItem> result = new ArrayList<SelectItem>();
        List<CodeItems> codeItems = codeservice.listCodeItemsByCodeName("地基处理方法");
        for (CodeItems codeItem : codeItems) {
            result.add(new SelectItem(codeItem.getItemValue(), codeItem.getItemText()));
        }
        return result;
    }

    public List<SelectItem> getJklxModel() {
        List<SelectItem> result = new ArrayList<SelectItem>();
        List<CodeItems> codeItems = codeservice.listCodeItemsByCodeName("基坑类别");
        for (CodeItems codeItem : codeItems) {
            result.add(new SelectItem(codeItem.getItemValue(), codeItem.getItemText()));
        }
        return result;
    }

    public List<SelectItem> getKzsfModel() {
        List<SelectItem> result = new ArrayList<SelectItem>();
        List<CodeItems> codeItems = codeservice.listCodeItemsByCodeName("抗震设防类别");
        for (CodeItems codeItem : codeItems) {
            result.add(new SelectItem(codeItem.getItemValue(), codeItem.getItemText()));
        }
        return result;
    }

    public List<SelectItem> getKzsfldModel() {
        List<SelectItem> result = new ArrayList<SelectItem>();
        List<CodeItems> codeItems = codeservice.listCodeItemsByCodeName("抗震设防烈度");
        for (CodeItems codeItem : codeItems) {
            result.add(new SelectItem(codeItem.getItemValue(), codeItem.getItemText()));
        }
        return result;
    }

    public List<SelectItem> getNhdjModel() {
        List<SelectItem> result = new ArrayList<SelectItem>();
        List<CodeItems> codeItems = codeservice.listCodeItemsByCodeName("耐火等级");
        for (CodeItems codeItem : codeItems) {
            result.add(new SelectItem(codeItem.getItemValue(), codeItem.getItemText()));
        }
        return result;
    }

    public List<SelectItem> getGsfsModel() {
        List<SelectItem> result = new ArrayList<SelectItem>();
        List<CodeItems> codeItems = codeservice.listCodeItemsByCodeName("给水方式");
        for (CodeItems codeItem : codeItems) {
            result.add(new SelectItem(codeItem.getItemValue(), codeItem.getItemText()));
        }
        return result;
    }

    public List<SelectItem> getCnfsModel() {
        List<SelectItem> result = new ArrayList<SelectItem>();
        List<CodeItems> codeItems = codeservice.listCodeItemsByCodeName("采暖方式");
        for (CodeItems codeItem : codeItems) {
            result.add(new SelectItem(codeItem.getItemValue(), codeItem.getItemText()));
        }
        return result;
    }

    public List<SelectItem> getKtfsModel() {
        List<SelectItem> result = new ArrayList<SelectItem>();
        List<CodeItems> codeItems = codeservice.listCodeItemsByCodeName("空调方式");
        for (CodeItems codeItem : codeItems) {
            result.add(new SelectItem(codeItem.getItemValue(), codeItem.getItemText()));
        }
        return result;
    }

    public List<SelectItem> getZmfsModel() {
        List<SelectItem> result = new ArrayList<SelectItem>();
        List<CodeItems> codeItems = codeservice.listCodeItemsByCodeName("照明方式");
        for (CodeItems codeItem : codeItems) {
            result.add(new SelectItem(codeItem.getItemValue(), codeItem.getItemText()));
        }
        return result;
    }

    public List<SelectItem> getLsjzModel() {
        List<SelectItem> result = new ArrayList<SelectItem>();
        List<CodeItems> codeItems = codeservice.listCodeItemsByCodeName("绿色建筑设计标准");
        for (CodeItems codeItem : codeItems) {
            result.add(new SelectItem(codeItem.getItemValue(), codeItem.getItemText()));
        }
        return result;
    }

    public List<SelectItem> getZxgzModel() {
        List<SelectItem> result = new ArrayList<SelectItem>();
        List<CodeItems> codeItems = codeservice.listCodeItemsByCodeName("装修改造工程类型");
        for (CodeItems codeItem : codeItems) {
            result.add(new SelectItem(codeItem.getItemValue(), codeItem.getItemText()));
        }
        return result;
    }

    public List<SelectItem> getGcytModel() {
        List<SelectItem> result = new ArrayList<SelectItem>();
        List<CodeItems> codeItems = codeservice.listCodeItemsByCodeName("工程用途");
        for (CodeItems codeItem : codeItems) {
            result.add(new SelectItem(codeItem.getItemValue(), codeItem.getItemText()));
        }
        return result;
    }

    public List<SelectItem> getXfssModel() {
        List<SelectItem> result = new ArrayList<SelectItem>();
        List<CodeItems> codeItems = codeservice.listCodeItemsByCodeName("消防设施种类");
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

    public TreeModel getTreeModel() {
        if (treeModel == null) {
            treeModel = new TreeModel()
            {
                private static final long serialVersionUID = -2385086983986752171L;

                @Override
                public List<TreeNode> fetch(TreeNode root) {
                    List<TreeNode> nodes = new ArrayList<TreeNode>();
                    List<TreeNode> treelist;
                    if (root == null) {// arg0为null，则是根节点
                        root = new TreeNode();
                        // 获取项目projectguid
                        // 缺少通过projectguid查找项目名称的方法暂时用xxx工程代替
                        root.setText("所有分类");
                        root.setId("root");
                        root.setExpanded(true);
                        nodes.add(root);
                        // 子节点
                        treelist = new ArrayList<TreeNode>();
                        List<CodeItems> codeItems = codeservice.listCodeItemsByCodeName("项目分类");
                        for (CodeItems codeItem : codeItems) {
                            if (codeItem.getItemValue().length() == 2 && !"02".equals(codeItem.getItemValue())) {
                                TreeNode node = new TreeNode();
                                node.setId(codeItem.getItemValue());
                                node.setPid("root");
                                node.setText(codeItem.getItemText());
                                node.setLeaf(true);
                                treelist.add(node);
                            }
                            if (codeItem.getItemValue().length() == 4
                                    && !"02".equals(codeItem.getItemValue().substring(0, 2))) {
                                TreeNode node = new TreeNode();
                                node.setId(codeItem.getItemValue());
                                node.setPid(codeItem.getItemValue().substring(0, 2));
                                node.setText(codeItem.getItemText());
                                node.setLeaf(true);
                                treelist.add(node);
                            }
                            if (codeItem.getItemValue().length() == 6
                                    && !"02".equals(codeItem.getItemValue().substring(0, 2))) {
                                TreeNode node = new TreeNode();
                                node.setId(codeItem.getItemValue());
                                node.setPid(codeItem.getItemValue().substring(0, 4));
                                node.setText(codeItem.getItemText());
                                node.setLeaf(true);
                                treelist.add(node);
                            }
                            if (codeItem.getItemValue().length() == 8
                                    && !"02".equals(codeItem.getItemValue().substring(0, 2))) {
                                TreeNode node = new TreeNode();
                                node.setId(codeItem.getItemValue());
                                node.setPid(codeItem.getItemValue().substring(0, 6));
                                node.setText(codeItem.getItemText());
                                node.setLeaf(true);
                                treelist.add(node);
                            }
                        }
                        nodes.addAll(treelist);
                    }
                    return nodes;
                }
            };
        }
        return treeModel;
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
                    CodeItems codeItems = codeservice.getCodeItemByCodeName("项目分类", gclbvalues[j]);
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
