package com.epoint.fmgl.auditrsitembaseinfoextends.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.epoint.authenticator.identity.Authenticator;
import com.epoint.authenticator.module.OAuthCheckTokenInfo;
import com.epoint.authenticator.utils.CheckTokenUtil;
import com.epoint.basic.auditonlineuser.auditonlineregister.domain.AuditOnlineRegister;
import com.epoint.basic.auditonlineuser.auditonlineregister.inter.IAuditOnlineRegister;
import com.epoint.basic.auditresource.company.domain.AuditRsCompanyBaseinfo;
import com.epoint.basic.auditresource.company.inter.IAuditRsCompanyBaseinfo;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.zwdt.authentication.ZwdtUserSession;
import com.epoint.core.dto.base.TreeNode;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.dto.model.TreeModel;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.fmgl.auditrsitembaseinfoextends.api.IAuditRsItemBaseinfoExtendsService;
import com.epoint.fmgl.auditrsitembaseinfoextends.api.entity.AuditRsItemBaseinfoExtends;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.frame.service.metadata.code.entity.CodeItems;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.jnzwdt.tzzxjgpt.rest.TzzxjgptUtil;

/**
 * 赋码项目基本信息表新增页面对应的后台
 * 
 * @author Administrator
 * @version [版本号, 2020-09-23 15:19:10]
 */
@RightRelation(AuditRsItemBaseinfoExtendsListAction.class)
@RestController("auditrsitembaseinfoextendsaddaction")
@Scope("request")
public class AuditRsItemBaseinfoExtendsAddAction extends BaseController
{
    @Autowired
    private IAuditRsItemBaseinfoExtendsService service;
    /**
     * 赋码项目基本信息表实体对象
     */
    private AuditRsItemBaseinfoExtends dataBean = null;

    /**
    * 行业核准目录下拉列表model
    */
    private List<SelectItem> permititemcodeModel = null;
    /**
     * 建设性质下拉列表model
     */
    private List<SelectItem> constructperModel = null;
    /**
     * 产业结构调整指导目录下拉列表model
     */
    private List<SelectItem> cyjgtzzdmlModel = null;
    /**
     * 项目单位性质下拉列表model
     */
    private List<SelectItem> enterprisenatureModel = null;
    /**
     * 项目单位性质（申报单位）下拉列表model
     */
    private List<SelectItem> enterprisenaturesbModel = null;
    /**
     * 国标行业下拉列表model
     */
    private List<SelectItem> industryModel = null;
    /**
     * 申报单位项目法人证照类型下拉列表model
     */
    private List<SelectItem> lerepcerttypesbModel = null;
    /**
     * 投资项目行业分类下拉列表model
     */
    private List<SelectItem> permitindustryModel = null;
    /**
     * 建设地点下拉列表model
     */
    private List<SelectItem> placecodeModel = null;
    private List<SelectItem> xzqhdmModel = null;
    /**
     * 项目属性下拉列表model
     */
    private List<SelectItem> projectattributesModel = null;
    
    private List<SelectItem> projectkindModel = null;
    /**
     * 项目阶段下拉列表model
     */
    private List<SelectItem> projectstageModel = null;
    /**
     * 项目类型下拉列表model
     */
    private List<SelectItem> projecttypeModel = null;
    /**
     * 状态下拉列表model
     */
    private List<SelectItem> statusModel = null;
    private List<SelectItem> businesstypeModel =null;
    private List<SelectItem> regcountryModel=null;
    private List<SelectItem> contributionmodeModel=null;
    /**
     * 所属行业下拉列表model
     */
    private List<SelectItem> theindustryModel = null;
    
    private List<SelectItem> investmentmodeModel=null;
    private List<SelectItem> investmentmodeModel2=null;
    private List<SelectItem> industrialpolicytypeModel=null;
    private List<SelectItem> getlandmodeModel=null;
    private List<SelectItem> isadddeviceModel=null;
    @Autowired
    private IAuditOnlineRegister iAuditOnlineRegister;
    /**
     * 拟向民间资本推介项目下拉列表model
     */
    private List<SelectItem> tjxmModel = null;
    private TreeModel treeModel;
    private TreeModel treeModel2;
    private TreeModel treeModel3;
    private TreeModel treeModel4;
    private TreeModel treeModel5;
    @Autowired
    private ICodeItemsService codeservice;
    /**
     * 企业基本信息API
     */
    @Autowired
    private IAuditRsCompanyBaseinfo iAuditRsCompanyBaseinfo;

    public void pageLoad() {
        String guid = getRequestParameter("guid");
        if(StringUtil.isNotBlank(guid)){
            dataBean = service.find(guid);
        }
        if (dataBean == null) {
            dataBean = new AuditRsItemBaseinfoExtends();
        }
        String creditcode = getRequestParameter("creditcode");
        AuditOnlineRegister auditOnlineRegister = this.getOnlineRegister(request);
        String idNumber = auditOnlineRegister.getIdnumber();
        SqlConditionUtil sqlConditionUtil = new SqlConditionUtil();
        sqlConditionUtil.eq("orgalegal_idnumber", idNumber);
        sqlConditionUtil.isBlankOrValue("is_history", "0"); // 在用版本的企业信息
        sqlConditionUtil.eq("isactivated", "1");
        sqlConditionUtil.setOrderDesc("registerdate"); // 按注册时间倒序排列
        if (StringUtil.isNotBlank(creditcode)) {
            sqlConditionUtil.eq("creditcode", creditcode);
        }
        //非法人情况
        List<AuditRsCompanyBaseinfo> auditRsCompanyBaseinfos = iAuditRsCompanyBaseinfo
                .selectAuditRsCompanyBaseinfoByCondition(sqlConditionUtil.getMap()).getResult();
        if(auditRsCompanyBaseinfos.size()==0) {
            if (StringUtil.isNotBlank(creditcode)) {
                sqlConditionUtil.clear();
                sqlConditionUtil.eq("creditcode", creditcode);
                sqlConditionUtil.isBlankOrValue("is_history", "0"); // 在用版本的企业信息
                sqlConditionUtil.eq("isactivated", "1");
                sqlConditionUtil.setOrderDesc("registerdate"); // 按注册时间倒序排列
                auditRsCompanyBaseinfos = iAuditRsCompanyBaseinfo
                        .selectAuditRsCompanyBaseinfoByCondition(sqlConditionUtil.getMap()).getResult();

            }
        }
        //获得授权法人身份证号
        for (AuditRsCompanyBaseinfo auditRsCompanyBaseinfo : auditRsCompanyBaseinfos) {
            String organname = auditRsCompanyBaseinfo.getOrganname();
            if(StringUtil.isBlank(creditcode)){
                creditcode = auditRsCompanyBaseinfo.getCreditcode();
            }
            //String creditcode = auditRsCompanyBaseinfo.getCreditcode();
            String legalperson = auditRsCompanyBaseinfo.getOrganlegal();
            String legalpersonicardnum = auditRsCompanyBaseinfo.getOrgalegal_idnumber();
            dataBean.setEnterprisename(organname);
            dataBean.setLerepcertno(creditcode);
            dataBean.setContactname(legalperson);
            dataBean.setLerepcerttypetext("统一社会信用代码");
            dataBean.setId(legalpersonicardnum);
            
            
            
            //测试用
/*            dataBean.setPermitindustry("A00001");
            dataBean.setPermititemcode("A0000101");
            dataBean.setProjecttype("A00002");
            dataBean.setConstructper("1");
            dataBean.setStartyear(new Date());
            dataBean.setEndyear(new Date());
            dataBean.setInvestment("11");
            dataBean.setPlacecode("370613");
            dataBean.setIndustry("A0111");
            dataBean.setCyjgtzzdml("AJ0002");
            dataBean.setTheindustry("A0000101");
            dataBean.setProjectstage("A00012");
            dataBean.setProjectattributes("A00001");
            dataBean.setTjxm("1");
            dataBean.setEnterprisenature("A00003");
            dataBean.setApplydate(new Date());
            dataBean.setLerepcerttypesb("A05300");
            dataBean.setEnterprisenaturesb("A00002");
            dataBean.setProjectname("测试项目");
            dataBean.setLinkman("项目负责人");
            dataBean.setLinkphone("13333333333");
            dataBean.setInvestment("11");
            dataBean.setPlacedetailcode("建设地点详情");
            dataBean.setProjectcontent("建设规模以及内容");
            dataBean.setContacttel("85211314");
            dataBean.setContactemail("5676548@qq.com");
            dataBean.setContactphone("13344445555");
            dataBean.setContactfax("5432123");
            dataBean.setCorrespondenceaddress("单位注册地址(企业法人)");
            dataBean.setEnterprisenamesb("项目（申报）单位");
            dataBean.setLerepcertnosb("370112199701025555");
            
            dataBean.setContactnamesb("张三");
            dataBean.setContacttelsb("85211315");
            dataBean.setContactemailsb("5676548@qq.com");
            dataBean.setContactphonesb("13333333334");
            dataBean.setContactfaxsb("4321234");
            dataBean.setCorrespondenceaddresssb("单位注册地址（申报单位）");*/
        }
    }

    /**
     * 提交
     * 
     */
    public void submit() {
        dataBean.setRowguid(UUID.randomUUID().toString());
        dataBean.setOperatedate(new Date());
        dataBean.setOperateusername(userSession.getDisplayName());
        try {
            AuditOnlineRegister auditOnlineRegister = this.getOnlineRegister(request);
            String resultdata = TzzxjgptUtil.sendtZxmbcxmxxcs(dataBean, auditOnlineRegister);
            JSONObject jsondata = JSONObject.parseObject(resultdata);
            String code = jsondata.getString("code");
            if("1".equals(code)) {
                String investId = jsondata.getString("investId");
                String seqId = jsondata.getString("seqId");
                dataBean.setInvestid(investId);
                dataBean.setSeqid(seqId);
                dataBean.setStatus("1");
                dataBean.setLerepcerttypetext("统一社会信用代码");
                service.insert(dataBean);
                addCallbackParam("msg", "申请提交成功！");
                dataBean = null;
            }else {
                String errorText = jsondata.getString("error");
                if(StringUtil.isNotBlank(errorText)) {
                    addCallbackParam("msg", "申请提交失败！"+errorText);
                }else {
                    addCallbackParam("msg", "申请提交失败！");
                }
            }
        }
        catch (Exception e) {
            addCallbackParam("msg", "申请提交失败！");
        }
       
       
    }

    /**
     * 
     * 保存
     */
    public void addNew() {
        dataBean.setRowguid(UUID.randomUUID().toString());
        dataBean.setOperatedate(new Date());
        dataBean.setOperateusername(userSession.getDisplayName());
        try {               
                dataBean.setStatus("0");
                service.insert(dataBean);
                addCallbackParam("msg", "保存成功！");
        }
        
        catch (Exception e) {
            addCallbackParam("msg", "保存失败！");
        }
    }
    

    /**
     * 保存修改
     * 
     */
    public void save() {
        dataBean.setOperatedate(new Date());
        service.update(dataBean);
        addCallbackParam("msg", "修改成功！");
    }

    public AuditRsItemBaseinfoExtends getDataBean() {
        if (dataBean == null) {
            dataBean = new AuditRsItemBaseinfoExtends();
        }
        return dataBean;
    }

    public void setDataBean(AuditRsItemBaseinfoExtends dataBean) {
        this.dataBean = dataBean;
    }

    public List<SelectItem> getPermititemcodeModel() {
        if (permititemcodeModel == null) {
            permititemcodeModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "省发改_投资项目核准目录行业分类", null, false));
        }
        return this.permititemcodeModel;
    }

    public List<SelectItem> getConstructperModel() {
        if (constructperModel == null) {
            constructperModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "省发改_建设性质", null, false));
        }
        return this.constructperModel;
    }

    public List<SelectItem> getCyjgtzzdmlModel() {
        if (cyjgtzzdmlModel == null) {
            cyjgtzzdmlModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "产业结构222", null, false));
        }
        return this.cyjgtzzdmlModel;
    }

    public List<SelectItem> getEnterprisenatureModel() {
        if (enterprisenatureModel == null) {
            enterprisenatureModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "省发改_申报单位性质", null, false));
        }
        return this.enterprisenatureModel;
    }

    public List<SelectItem> getEnterprisenaturesbModel() {
        if (enterprisenaturesbModel == null) {
            enterprisenaturesbModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "省发改_申报单位性质", null, false));
        }
        return this.enterprisenaturesbModel;
    }

    public List<SelectItem> getIndustryModel() {
        if (industryModel == null) {
            industryModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "国标行业2017", null, false));
        }
        return this.industryModel;
    }

    public List<SelectItem> getLerepcerttypesbModel() {
        if (lerepcerttypesbModel == null) {
            lerepcerttypesbModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "省发改_证件类型", null, false));
        }
        return this.lerepcerttypesbModel;
    }

    public List<SelectItem> getPermitindustryModel() {
        if (permitindustryModel == null) {
            permitindustryModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "省发改_投资项目行业分类", null, false));
        }
        return this.permitindustryModel;
    }

    public List<SelectItem> getPlacecodeModel() {
        if (placecodeModel == null) {
            placecodeModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "济宁区县", null, false));
        }
        return this.placecodeModel;
    }
    public List<SelectItem> getXzqhdmModel() {
        if (xzqhdmModel == null) {
            xzqhdmModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "建设地点济宁区县", null, false));
        }
        return this.xzqhdmModel;
    }

    public List<SelectItem> getProjectattributesModel() {
        if (projectattributesModel == null) {
            projectattributesModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "省发改_项目属性", null, false));
        }
        return this.projectattributesModel;
    }
    public List<SelectItem> getProjectkindModel() {
        if (projectkindModel == null) {
            projectkindModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "省发改_项目性质", null, false));
        }
        return this.projectkindModel;
    }

    public List<SelectItem> getProjectstageModel() {
        if (projectstageModel == null) {
            projectstageModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "省发改_项目阶段", null, false));
        }
        return this.projectstageModel;
    }

    public List<SelectItem> getProjecttypeModel() {
        if (projecttypeModel == null) {
            projecttypeModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "省发改_项目类型", null, true));
        }
        return this.projecttypeModel;
    }

    public List<SelectItem> getStatusModel() {
        if (statusModel == null) {
            statusModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "省发改_建设性质", null, false));
        }
        return this.statusModel;
    }

    public List<SelectItem> getTheindustryModel() {
        if (theindustryModel == null) {
            theindustryModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "省发改_所属行业", null, false));
        }
        return this.theindustryModel;
    }

    public List<SelectItem> getTjxmModel() {
        if (tjxmModel == null) {
            tjxmModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "是否", null, false));
        }
        return this.tjxmModel;
    }
    public List<SelectItem> getInvestmentmodeModel() {
        if (investmentmodeModel == null) {
            investmentmodeModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "省发改_投资方式", null, false));
        }
        return this.investmentmodeModel;
    }
    public List<SelectItem> getInvestmentmodeModel2() {
        if (investmentmodeModel2 == null) {
            investmentmodeModel2 = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "省发改_外商投资方式", null, false));
        }
        return this.investmentmodeModel2;
    }
    public List<SelectItem> getIndustrialpolicytypeModel() {
        if (industrialpolicytypeModel == null) {
            industrialpolicytypeModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "省发改_适用产业政策条目类型", null, false));
        }
        return this.industrialpolicytypeModel;
    }
    public List<SelectItem> getGetlandmodeModel() {
        if (getlandmodeModel == null) {
            getlandmodeModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "省发改_土地获取方式", null, false));
        }
        return this.getlandmodeModel;
    }
    public List<SelectItem> getIsadddeviceModel() {
        if (isadddeviceModel == null) {
            isadddeviceModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "是否", null, false));
        }
        return this.isadddeviceModel;
    }
    
    public List<SelectItem> getBusinesstypeModel() {
        if (businesstypeModel == null) {
            businesstypeModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "省发改_出资类型", null, false));
        }
        return this.businesstypeModel;
    }
    public List<SelectItem> getRegcountryModel() {
        if (regcountryModel == null) {
            regcountryModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "省发改_国别", null, false));
        }
        return this.regcountryModel;
    }
    public List<SelectItem> getContributionmodeModel() {
        if (contributionmodeModel == null) {
            contributionmodeModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "省发改_出资方式", null, false));
        }
        return this.contributionmodeModel;
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
                    if (root == null) {//arg0为null，则是根节点
                        root = new TreeNode();
                        root.setText("国标行业");
                        root.setId("root");
                        root.setExpanded(true);
                        nodes.add(root);
                        //子节点
                        treelist = new ArrayList<TreeNode>();
                        List<CodeItems> codeItems = codeservice.listCodeItemsByCodeName("国标行业2017");
                        for (CodeItems codeItem : codeItems) {
                            if (codeItem.getItemValue().length() ==1) {
                                //System.out.println(codeItem.getItemValue());
                                TreeNode node = new TreeNode();
                              node.setId(codeItem.getItemValue());
                              node.setPid("root");
                              node.setText(codeItem.getItemText());
                              node.setLeaf(true);
                              treelist.add(node);
                            }
                            if (codeItem.getItemValue().length() ==3) {
                                TreeNode node = new TreeNode();
                                node.setId(codeItem.getItemValue());
                                node.setPid(codeItem.getItemValue().substring(0, 1));
                                node.setText(codeItem.getItemText());
                                node.setLeaf(true);
                                treelist.add(node);
                            }
                            if (codeItem.getItemValue().length() ==4) {
                                TreeNode node = new TreeNode();
                                node.setId(codeItem.getItemValue());
                                node.setPid(codeItem.getItemValue().substring(0, 3));
                                node.setText(codeItem.getItemText());
                                node.setLeaf(true);
                                treelist.add(node);
                            }
                            if (codeItem.getItemValue().length() == 5 ) {
                                TreeNode node = new TreeNode();
                                node.setId(codeItem.getItemValue());
                                node.setPid(codeItem.getItemValue().substring(0, 4));
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
    public TreeModel getTreeModel2() {
        if (treeModel2 == null) {
            treeModel2 = new TreeModel()
            {
                private static final long serialVersionUID = -2385086983986752171L;

                @Override
                public List<TreeNode> fetch(TreeNode root) {
                    List<TreeNode> nodes = new ArrayList<TreeNode>();
                    List<TreeNode> treelist;
                    if (root == null) {//arg0为null，则是根节点
                        root = new TreeNode();
                        root.setText("所属行业");
                        root.setId("root");
                        root.setExpanded(true);
                        nodes.add(root);
                        //子节点
                        treelist = new ArrayList<TreeNode>();
                        List<CodeItems> codeItems = codeservice.listCodeItemsByCodeName( "省发改_所属行业");
                        for (CodeItems codeItem : codeItems) {
                            if (codeItem.getItemValue().length() ==1) {
                                TreeNode node = new TreeNode();
                              node.setId(codeItem.getItemValue());
                              node.setPid("root");
                              node.setText(codeItem.getItemText());
                              node.setLeaf(true);
                              treelist.add(node);
                            }
                            if (codeItem.getItemValue().length() ==6) {
                                TreeNode node = new TreeNode();
                                node.setId(codeItem.getItemValue());
                                node.setPid("root");
                                node.setText(codeItem.getItemText());
                                node.setLeaf(true);
                                treelist.add(node);
                            }
                            if (codeItem.getItemValue().length() ==8) {
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
        return treeModel2;
    }
    public TreeModel getTreeModel3() {
        if (treeModel3 == null) {
            treeModel3 = new TreeModel()
            {
                private static final long serialVersionUID = -2385086983986752171L;

                @Override
                public List<TreeNode> fetch(TreeNode root) {
                    List<TreeNode> nodes = new ArrayList<TreeNode>();
                    List<TreeNode> treelist;
                    if (root == null) {//arg0为null，则是根节点
                        root = new TreeNode();
                        root.setText("产业结构调整指导目录");
                        root.setId("root");
                        root.setExpanded(true);
                        nodes.add(root);
                        //子节点
                        treelist = new ArrayList<TreeNode>();
                        List<CodeItems> codeItems = codeservice.listCodeItemsByCodeName("省发改_产业结构调整指导目录");
                        for (CodeItems codeItem : codeItems) {
                            if (codeItem.getItemValue().length() ==1) {
                                TreeNode node = new TreeNode();
                              node.setId(codeItem.getItemValue());
                              node.setPid("root");
                              node.setText(codeItem.getItemText());
                              node.setLeaf(true);
                              treelist.add(node);
                            }
                            if (codeItem.getItemValue().length() ==2) {
                                TreeNode node = new TreeNode();
                                node.setId(codeItem.getItemValue());
                                node.setPid("root");
                                node.setText(codeItem.getItemText());
                                node.setLeaf(true);
                                treelist.add(node);
                            }
                            if (codeItem.getItemValue().length() ==6) {
                                TreeNode node = new TreeNode();
                                node.setId(codeItem.getItemValue());
                                node.setPid(codeItem.getItemValue().substring(0, 2));
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
        return treeModel3;
    }
    public TreeModel getTreeModel4() {
        if (treeModel4 == null) {
            treeModel4 = new TreeModel()
            {
                private static final long serialVersionUID = -2385086983986752171L;

                @Override
                public List<TreeNode> fetch(TreeNode root) {
                    List<TreeNode> nodes = new ArrayList<TreeNode>();
                    List<TreeNode> treelist;
                    if (root == null) {//arg0为null，则是根节点
                        root = new TreeNode();
                        root.setText("省发改_外商投资适用产业政策条目（鼓励类）");
                        root.setId("root");
                        root.setExpanded(true);
                        nodes.add(root);
                        //子节点
                        treelist = new ArrayList<TreeNode>();
                        List<CodeItems> codeItems = codeservice.listCodeItemsByCodeName("省发改_外商投资适用产业政策条目（鼓励类）");
                        for (CodeItems codeItem : codeItems) {
                            if (codeItem.getItemValue().length() ==1) {
                                TreeNode node = new TreeNode();
                              node.setId(codeItem.getItemValue());
                              node.setPid("root");
                              node.setText(codeItem.getItemText());
                              node.setLeaf(true);
                              treelist.add(node);
                            }
                            if (codeItem.getItemValue().length() ==3) {
                                TreeNode node = new TreeNode();
                                node.setId(codeItem.getItemValue());
                                node.setPid(codeItem.getItemValue().substring(0, 1));
                                node.setText(codeItem.getItemText());
                                node.setLeaf(true);
                                treelist.add(node);
                            }
                            if (codeItem.getItemValue().length() ==5) {
                                TreeNode node = new TreeNode();
                                node.setId(codeItem.getItemValue());
                                node.setPid(codeItem.getItemValue().substring(0, 3));
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
        return treeModel4;
    }
    
    public TreeModel getTreeModel5() {
        if (treeModel5 == null) {
            treeModel5 = new TreeModel()
            {
                private static final long serialVersionUID = -2385086983986752171L;

                @Override
                public List<TreeNode> fetch(TreeNode root) {
                    List<TreeNode> nodes = new ArrayList<TreeNode>();
                    List<TreeNode> treelist;
                    if (root == null) {//arg0为null，则是根节点
                        root = new TreeNode();
                        root.setText("省发改_外商投资适用产业政策条目（限制类）");
                        root.setId("root");
                        root.setExpanded(true);
                        nodes.add(root);
                        //子节点
                        treelist = new ArrayList<TreeNode>();
                        List<CodeItems> codeItems = codeservice.listCodeItemsByCodeName("省发改_外商投资适用产业政策条目（限制类）");
                        for (CodeItems codeItem : codeItems) {
                            if (codeItem.getItemValue().length() ==5) {
                                TreeNode node = new TreeNode();
                              node.setId(codeItem.getItemValue());
                              node.setPid("root");
                              node.setText(codeItem.getItemText());
                              node.setLeaf(true);
                              treelist.add(node);
                            }
                            if (codeItem.getItemValue().length() ==6) {
                                TreeNode node = new TreeNode();
                                node.setId(codeItem.getItemValue());
                                node.setPid(codeItem.getItemValue().substring(0, 5));
                                node.setText(codeItem.getItemText());
                                node.setLeaf(true);
                                treelist.add(node);
                            }
                            if (codeItem.getItemValue().length() ==7) {
                                TreeNode node = new TreeNode();
                                node.setId(codeItem.getItemValue());
                                node.setPid(codeItem.getItemValue().substring(0, 5));
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
        return treeModel5;
    }
    /**
     * 获取用户唯一标识
     * 
     * @param httpServletRequest
     * @return
     */
    private AuditOnlineRegister getOnlineRegister(HttpServletRequest httpServletRequest) {
        AuditOnlineRegister auditOnlineRegister;
        OAuthCheckTokenInfo oAuthCheckTokenInfo = CheckTokenUtil.getCheckTokenInfo(httpServletRequest);
        if (oAuthCheckTokenInfo != null) {
            // 手机端
            // 通过登录名获取用户
            auditOnlineRegister = iAuditOnlineRegister.getRegisterByIdorMobile(oAuthCheckTokenInfo.getLoginid())
                    .getResult();
        }
        else {
            // PC端
            String accountGuid = ZwdtUserSession.getInstance("").getAccountGuid();
            if (StringUtil.isNotBlank(accountGuid)) {
                auditOnlineRegister = iAuditOnlineRegister.getRegisterByAccountguid(accountGuid).getResult();
            }
            else {
                // 通过登录名获取用户
                auditOnlineRegister = iAuditOnlineRegister.getRegisterByIdorMobile(Authenticator.getCurrentIdentity())
                        .getResult();
            }
        }
        return auditOnlineRegister;
    }
}
