package com.epoint.auditorga.auditarea.action;

import com.epoint.basic.auditorga.auditarea.domain.AuditOrgaArea;
import com.epoint.basic.auditorga.auditarea.inter.IAuditOrgaArea;
import com.epoint.basic.auditorga.auditcenter.domain.AuditOrgaServiceCenter;
import com.epoint.basic.auditorga.auditcenter.inter.IAuditOrgaServiceCenter;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import com.epoint.basic.faces.fileupload.AttachHandler9;
import com.epoint.basic.faces.fileupload.AttachStorage;
import com.epoint.basic.faces.fileupload.FileUploadModel9;
import com.epoint.basic.faces.tree.*;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.core.dto.base.TreeNode;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.dto.model.TreeModel;
import com.epoint.core.exception.security.ReadOnlyException;
import com.epoint.core.utils.convert.ConvertUtil;
import com.epoint.core.utils.log.LogUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.frame.service.organ.ou.api.IOuService;
import com.epoint.frame.service.organ.ou.api.IOuServiceInternal;
import com.epoint.frame.service.organ.ou.entity.FrameOu;
import com.epoint.frame.service.organ.ou.entity.FrameOuExtendInfo;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * 区域配置修改页面对应的后台
 *
 * @author Dong
 * @version [版本号, 2016-09-26 17:13:31]
 */
@RestController("auditareaeditotherlevelaction")
@RightRelation(AuditAreaListAction.class)
@Scope("request")
public class AuditAreaEditActionOtherLevel extends BaseController {

    /**
     *
     */
    private static final long serialVersionUID = 90412820785420099L;

    /**
     * 区域配置bizlogic中service
     */

    /**
     * 区域配置接口的实现类
     */
    @Autowired
    private IAuditOrgaArea areaImpl;
    @Autowired
    private IOuServiceInternal ouService;
    @Autowired
    private IAuditOrgaServiceCenter auditServiceCenter;

    @Autowired
    private IOuService frameOuService;
    transient Logger log = LogUtil.getLog(AuditAreaEditActionOtherLevel.class);

    /**
     * 区域配置实体对象
     */
    private AuditOrgaArea dataBean = null;

    /**
     * 行政区划级别下拉列表model
     */
    private List<SelectItem> citylevelModel = null;
    /**
     * 附件model
     */
    private FileUploadModel9 attachUploadModel;

    /**
     * 附件标识
     */
    private String attachguid;
    /**
     * 新附件标识
     */
    private String newattachguid;
    /**
     * 老记录的辖区code
     */
    private String oldxiaqucode = "";

    /**
     * 老记录的部门guid
     */
    private String oldouguid = "";

    /**
     * 部门树
     */
    private LazyTreeModal9 treeModel;

    /**
     * 辖区编码是否重复标志位
     */

    private String issamecode = "0";

    /**
     * 部门是否重复标志位
     */

    private String issameguid = "0";

    /**
     * 辖区编号
     */
    private String areacode;

    private TreeModel radiotreeModel;

    private FrameOuExtendInfo frameOuExtendInfo;

    private String ouguid;

    private String citylevel;

    @Override
    public void pageLoad() {
        areacode = getRequestParameter("xiaqucode");
        dataBean = areaImpl.getAreaByAreacode(areacode).getResult();
        citylevel = dataBean.getCitylevel();
        if (dataBean == null) {
            dataBean = new AuditOrgaArea();
        } else {
            FrameOu ou = ouService.getOuByOuGuid(dataBean.getOuguid());
            String ouname = "";
            if (ou != null) {
                ouname = ou.getOuname();
                ouguid = ou.getOuguid();
            }
            addCallbackParam("ouname", ouname);
        }
        attachguid = dataBean.getIntroimg();
        if (StringUtil.isNotBlank(attachguid)) {
            newattachguid = attachguid;
        } else {
            newattachguid = UUID.randomUUID().toString();
        }
        // 此处areacode用作判断市本级、区县本级、乡镇（街道），市本级或区县本级时置空，乡镇（街道）则取前6位
        // areacode不为空且长度大于6，可以判断为是乡镇（街道），截取前6位
        if (StringUtil.isNotBlank(areacode) && ZwfwConstant.AREA_TYPE_XZJ.equals(dataBean.getCitylevel())) {
            areacode = areacode.substring(0, 6);
            addCallbackParam("areacode", areacode);
        } else if (StringUtil.isNotBlank(areacode) && ZwfwConstant.AREA_TYPE_CJ.equals(dataBean.getCitylevel())) {
            areacode = areacode.substring(0, 9);
            addCallbackParam("areacode", areacode);
        }
        // 其他情况默认为市本级或区县本级,置空areacode
        else {
            areacode = null;
            addCallbackParam("areacode", null);
        }
        oldxiaqucode = dataBean.getXiaqucode();
        oldouguid = dataBean.getOuguid();
    }

    /**
     * 保存修改
     */
    public void save() {
        if (StringUtil.isNotBlank(citylevel) && !citylevel.equals(dataBean.getCitylevel())) {
            throw new ReadOnlyException("行政区划级别");
        }
        if (StringUtil.isBlank(dataBean.getOrdernum())) {
            dataBean.setOrdernum(0);
        }
        // 辖区名字是否重复标志位
        String issamexiaquname = "0";
        // 辖区编码是否重复标志位
        String issamecode = "0";
        // 部门是否重复标志位
        String issameguid = "0";
        // 区域标签是否重复标志位
        String isIndividuationfold = "0";
        // 已经存在的辖区编码
        String isusercodevalue = "";
        // 已经使用部门的名字
        String isuserouname = "";
        // 返回msg消息
        String msg = "";
        // 返回的alertmsg消息
        String alertmsg = "";
        // 返回foldmsg消息
        String foldmsg = "";

        // 获取数据库数据
        List<AuditOrgaArea> arealistAll = areaImpl.selectAuditAreaList(null).getResult();
        // 循环判断
        for (AuditOrgaArea area : arealistAll) {
            if (area.getRowguid().equals(dataBean.getRowguid())) {
                continue;
            }
            if (dataBean.getXiaquname().equals(area.getXiaquname())) {
                // 名字存在重复
                issamexiaquname = "1";
            }
            if (dataBean.getXiaqucode().equals(area.getXiaqucode())) {
                // 辖区编码重复
                issamecode = "1";
                isuserouname = area.getOuname();
            }
            if (dataBean.getOuguid().equals(area.getOuguid())) {
                // 部门使用过了
                issameguid = "1";
                isusercodevalue = area.getXiaqucode();
            }
            if (dataBean.getIndividuationfold().equals(area.getIndividuationfold())) {
                // 区域标签使用过了
                isIndividuationfold = "1";
            }
        }
        // 最后根据标志位返回提示消息
        if ("1".equals(issamexiaquname)) {
            msg = "辖区名称已经存在，请重新填写一个辖区名称！";
        }
        if ("1".equals(isIndividuationfold)) {
            foldmsg = "部门标签已被使用，请不要重复添加！";
        } else {
            if ("1".equals(issamecode) && "0".equals(issameguid)) {
                // 辖区编码已经存在，选择的部门在区域表中未配置
                // 如果取消则不保存数据，如果继续，先把是这个辖区编号的全部清空，在新增。
                msg = "iscontinue";
                alertmsg = "辖区编码已经被'" + isuserouname + "'部门使用,是否清除他们的关系，继续新增？";
            }
            if ("0".equals(issamecode) && "1".equals(issameguid)) {
                // 辖区编码不存在可以使用，部门已经被配置过
                // 该部门已经做了关联，如果取消则不保存数据，如果继续，先清空部门括展表中的areacode，删除audit_area表中数据
                msg = "iscontinue";
                alertmsg = "部门已经和'" + isusercodevalue + "'辖区编码做了关联，是否清除他们的关系，继续新增 ？";
            }

            if ("1".equals(issamecode) && "1".equals(issameguid)) {
                // 辖区编码和部门都被使用过
                msg = "iscontinue";
                alertmsg = "辖区编码已经被'" + isuserouname + "'部门使用，部门已经和'" + isusercodevalue + "'辖区编码做了关联，是否清除他们的关系，继续新增 ？";
            }

            if ("0".equals(issamecode) && "0".equals(issameguid)) {
                // 都不存在,正常修改
                // 清空老记录的部门括展表中的areacode
                List<FrameOuExtendInfo> frameOuExtendInfos = ouService.getAllFrameOuExtendInfo();
                for (FrameOuExtendInfo frameOuExtendInfo : frameOuExtendInfos) {
                    if (frameOuExtendInfo.get("areacode") != null
                            && frameOuExtendInfo.get("areacode").toString().equals(oldxiaqucode)) {
                        frameOuExtendInfo.set("areacode", "");
                        FrameOu frameOu = ouService.getOuByOuGuid(frameOuExtendInfo.getOuGuid());
                        ouService.updateFrameOu(frameOu, frameOuExtendInfo);
                    }
                }
                // 清空老记录的部门括展表中的areacode
                FrameOuExtendInfo frameOuExtendInfo = ouService.getFrameOuExtendInfo(oldouguid);
                if (frameOuExtendInfo != null && !frameOuExtendInfo.isEmpty()) {
                    frameOuExtendInfo.set("areacode", "");
                    FrameOu frameOu = ouService.getOuByOuGuid(frameOuExtendInfo.getOuGuid());
                    ouService.updateFrameOu(frameOu, frameOuExtendInfo);
                } else {
                    log.info("ouguid为" + oldouguid + "的部门已经不存在，不再删除！");
                }

                // 通过ouguid更新括展表的辖区编码
                // 获取所有子部门及自身
                List<FrameOu> frameOus = ouService.listOUByGuid(dataBean.getOuguid(), 4);
                for (FrameOu frameOu : frameOus) {
                    FrameOuExtendInfo frameExtendInfo = ouService.getFrameOuExtendInfo(frameOu.getOuguid());
                    String extendinfoareacode = frameExtendInfo.get("areacode");
                    if (StringUtil.isBlank(extendinfoareacode) || extendinfoareacode.equals(dataBean.getXiaqucode())
                            || "3".equals(dataBean.getCitylevel()) || "4".equals(dataBean.getCitylevel())) {
                        frameExtendInfo.set("areacode", dataBean.getXiaqucode());
                    } else {
                        extendinfoareacode = extendinfoareacode.replaceAll(oldxiaqucode, dataBean.getXiaqucode());
                        frameExtendInfo.set("areacode", extendinfoareacode);
                    }
                    frameOu.setExtendInfo(frameExtendInfo);
                    ouService.updateFrameOu(frameOu, frameExtendInfo);
                }
                // 中心配置中辖区更新和事项辖区编码
                updateCenterAndTask(oldxiaqucode, dataBean.getXiaqucode());
                // 如果存在乡镇，则修改乡镇辖区编码
                List<AuditOrgaArea> list = areaImpl.selectZXAuditAreaListByAreaCode(oldxiaqucode).getResult();
                String xzareacode = "";
                for (AuditOrgaArea auditOrgaArea : list) {
                    xzareacode = auditOrgaArea.getXiaqucode().replaceAll(oldxiaqucode, dataBean.getXiaqucode());
                    // 中心配置中辖区更新和事项辖区编码
                    updateCenterAndTask(auditOrgaArea.getXiaqucode(), xzareacode);
                    auditOrgaArea.setXiaqucode(xzareacode);
                    areaImpl.updateArea(auditOrgaArea);
                }

                // 通过ouguid获取部门名称
                FrameOu frameou = ouService.getOuByOuGuid(dataBean.getOuguid());
                if (frameou != null) {
                    dataBean.setOuname(frameou.getOuname());
                }
                if (!ouguid.equals(dataBean.getOuguid())) {
                    msg = "iscontinue";
                    alertmsg = "修改部门可能导致乡镇的部门和市级部门没有所属关系，请确认是否修改?";

                    addCallbackParam("msg", msg);
                    addCallbackParam("alertmsg", alertmsg);
                    addViewData("issamecode", issamecode);
                    addViewData("issameguid", issameguid);
                    return;
                }
                dataBean.setOperatedate(new Date());
                areaImpl.updateArea(dataBean);
                msg = "修改成功！";
            }
        }
        // 返回消息到action
        addCallbackParam("msg", msg);
        addCallbackParam("alertmsg", alertmsg);
        addCallbackParam("foldmsg", foldmsg);
        addViewData("issamecode", issamecode);
        addViewData("issameguid", issameguid);
    }

    private void updateCenterAndTask(String oldxiaqucode, String nowxiaqucode) {
        SqlConditionUtil sql = new SqlConditionUtil();
        sql.eq("belongxiaqu", oldxiaqucode);
        try {
            List<AuditOrgaServiceCenter> serviceCenterList = auditServiceCenter
                    .getAuditOrgaServiceCenterByCondition(sql.getMap()).getResult();
            for (AuditOrgaServiceCenter serviceCenter : serviceCenterList) {
                AuditOrgaServiceCenter serviceCenterInfo = auditServiceCenter
                        .findAuditServiceCenterByGuid(serviceCenter.getRowguid()).getResult();
                if (serviceCenterInfo != null && serviceCenterInfo.getBelongxiaqu().equals(oldxiaqucode)
                        && (!serviceCenterInfo.getBelongxiaqu().equals(nowxiaqucode))) {
                    serviceCenterInfo.set("BelongXiaQu", nowxiaqucode);
                    auditServiceCenter.updateAuditServiceCenter(serviceCenterInfo);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 继续修改(清空原来辖区编码的关系，新增关联关系)
     */
    public void isContinueEditByMsg() {
        issamecode = getViewData("issamecode");
        issameguid = getViewData("issameguid");

        // 判断辖区编号是否存在，不存在可以正常修改，存在就需要判断
        if ("1".equals(issamecode)) {
            // 清空部门括展表中的areacode分三步， 1 先清空修改这条记录原先关联的areacode
            List<FrameOuExtendInfo> frameOuExtendInfos = ouService.getAllFrameOuExtendInfo();
            for (FrameOuExtendInfo frameOuExtendInfo : frameOuExtendInfos) {
                if (frameOuExtendInfo.get("areacode") != null
                        && frameOuExtendInfo.get("areacode").toString().equals(oldxiaqucode)) {
                    frameOuExtendInfo.set("areacode", "");
                    FrameOu frameOu = ouService.getOuByOuGuid(frameOuExtendInfo.getOuGuid());
                    ouService.updateFrameOu(frameOu, frameOuExtendInfo);
                }
            }
            // 2 在修改要被替换的areacode
            List<FrameOuExtendInfo> frameExtendInfos = ouService.getAllFrameOuExtendInfo();
            for (FrameOuExtendInfo frameOuExtendInfo : frameExtendInfos) {
                if (frameOuExtendInfo.get("areacode") != null
                        && frameOuExtendInfo.get("areacode").toString().equals(dataBean.getXiaqucode())) {
                    frameOuExtendInfo.set("areacode", "");
                    FrameOu frameOu = ouService.getOuByOuGuid(frameOuExtendInfo.getOuGuid());
                    ouService.updateFrameOu(frameOu, frameOuExtendInfo);
                }
            }
            // 3 删除要被替换code的audit_area
            areaImpl.deleteAreaByAreacode(dataBean.getXiaqucode());
        }
        // 在判断区域表中是否已经配置过该部门了
        if ("1".equals(issameguid)) {
            // 修改这条记录原先关联的areacode
            FrameOuExtendInfo frameOuExtendInfo = ouService.getFrameOuExtendInfo(oldouguid);
            FrameOu frameOu = ouService.getOuByOuGuid(frameOuExtendInfo.getOuGuid());
            frameOuExtendInfo.set("areacode", "");
            ouService.updateFrameOu(frameOu, frameOuExtendInfo);
            // 删除要被替换部门的audit_area
            areaImpl.deleteAreaByOuguid(dataBean.getOuguid());
        }
        // 通过ouguid更新括展表的辖区编码
        // 获取所有子部门及自身
        List<FrameOu> frameOus = ouService.listOUByGuid(dataBean.getOuguid(), 4);
        for (FrameOu frameOu : frameOus) {
            FrameOuExtendInfo frameOuExtendInfo = ouService.getFrameOuExtendInfo(frameOu.getOuguid());
            frameOuExtendInfo.set("areacode", dataBean.getXiaqucode());
            ouService.updateFrameOu(frameOu, frameOuExtendInfo);
        }
        // 通过ouguid获取部门名称
        FrameOu frameou = ouService.getOuByOuGuid(dataBean.getOuguid());
        if (frameou != null) {
            dataBean.setOuname(frameou.getOuname());
        }
        dataBean.setOperatedate(new Date());
        areaImpl.updateArea(dataBean);
        String msg = "修改成功！";
        addCallbackParam("msg", msg);
    }

    public AuditOrgaArea getDataBean() {
        return dataBean;
    }

    public void setDataBean(AuditOrgaArea dataBean) {
        this.dataBean = dataBean;
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> getCitylevelModel() {
        if (citylevelModel == null) {
            citylevelModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "行政区划级别", null, false));
            // 乡镇修改页面
            if (StringUtil.isNotBlank(areacode)) {
                // 移除市本级,移除区县本级
                citylevelModel.removeIf(selectItem -> {
                    if (ZwfwConstant.AREA_TYPE_SJ.equals(selectItem.getValue())
                            || ZwfwConstant.AREA_TYPE_XQJ.equals(selectItem.getValue())) {
                        return true;
                    } else {
                        return false;
                    }
                });
            }
            // 市本级或区县本级修改页面
            else {
                // 移除乡镇(街道)
                citylevelModel.removeIf(selectItem -> {
                    if (ZwfwConstant.AREA_TYPE_XZJ.equals(selectItem.getValue())) {
                        return true;
                    } else {
                        return false;
                    }
                });
            }
        }
        return this.citylevelModel;
    }

    public TreeModel getTreeModel() {
        if (treeModel == null) {
            if (StringUtil.isNotBlank(areacode)) {
                return getOuNameModel();
            }
            int tableName = ConstValue9.FRAMEOU;
            treeModel = new LazyTreeModal9(new EpointTreeHandler9(tableName));
            treeModel.setRootName("所有部门");
        }
        return treeModel;
    }

    public TreeModel getOuNameModel() {

        if (dataBean.isEmpty()) {
            radiotreeModel = new TreeModel() {
                private static final long serialVersionUID = 1L;

                @Override
                public List<TreeNode> fetch(TreeNode treeNode) {
                    TreeData treeData = TreeFunction9.getData(treeNode);
                    List<TreeNode> list = new ArrayList<>();
                    if (treeData == null) {
                        TreeNode root = new TreeNode();
                        list.add(root);
                    }
                    return list;
                }
            };
        } else {
            if (radiotreeModel == null) {
                radiotreeModel = new TreeModel() {
                    private static final long serialVersionUID = 1L;

                    @Override
                    public List<TreeNode> fetch(TreeNode treeNode) {
                        TreeData treeData = TreeFunction9.getData(treeNode);
                        List<TreeNode> list = new ArrayList<>();
                        String OUGuid = "";
                        String OUName = "";
                        if (areacode != null && areacode != "") {
                            // 获取辖区对应的部门Guid
                            SqlConditionUtil sql = new SqlConditionUtil();
                            sql.eq("xiaqucode", areacode);
                            AuditOrgaArea area = areaImpl.getAuditArea(sql.getMap()).getResult();
                            if (area != null && area.getOuguid() != "") {
                                OUGuid = area.getOuguid();
                            }
                            if (area != null && area.getOuname() != "") {
                                OUName = area.getOuname();
                            }
                        }
                        // 首次加载树结构
                        if (treeData == null) {
                            TreeNode root = new TreeNode();
                            root.setText(OUName);
                            root.setId(OUGuid);
                            root.setPid("-1");
                            root.getColumns().put("dlbm", "true");// 标记：是否是部门节点
                            list.add(root);
                            root.setExpanded(true);// 展开下一层节点
                            list.addAll(fetch(root));// 自动加载下一层树结构
                        }
                        // 每次点击树节点前的加号，进行加载
                        else {
                            String objectGuid = treeData.getObjectGuid();
                            List<FrameOu> listRootOu = frameOuService.listOUByGuid(objectGuid, 2);

                            // 部门的绑定
                            for (int i = 0; i < listRootOu.size(); i++) {
                                if ((listRootOu.get(i).getParentOuguid() == null && objectGuid.equals(""))
                                        || (listRootOu.get(i).getParentOuguid().equals(objectGuid))) {
                                    TreeNode node = new TreeNode();
                                    frameOuExtendInfo = frameOuService
                                            .getFrameOuExtendInfo(listRootOu.get(i).getOuguid());
                                    if (frameOuExtendInfo != null && !ConvertUtil
                                            .convertIntegerToBoolean(frameOuExtendInfo.getIsIndependence())) {
                                        node.getColumns().put("dlbm", "true");
                                    }
                                    node.setId(listRootOu.get(i).getOuguid());
                                    node.setText(listRootOu.get(i).getOuname());
                                    node.setPid(listRootOu.get(i).getParentOuguid());
                                    node.setLeaf(true);

                                    for (int j = 0; j < listRootOu.size(); j++) {
                                        if (listRootOu.get(i).getOuguid().equals(listRootOu.get(j).getParentOuguid())) {
                                            node.setLeaf(false);
                                            break;
                                        }
                                    }
                                    if (!(node.getIsLeaf() == true && "true".equals(node.getColumns().get("dlbm")))) {
                                        list.add(node);
                                    }
                                }
                            }
                        }
                        return list;
                    }
                };
            }
        }
        return radiotreeModel;
    }

    public FileUploadModel9 getAttachUploadModel() {
        if (attachUploadModel == null) {
            AttachHandler9 handler = new AttachHandler9() {
                /**
                 *
                 */
                private static final long serialVersionUID = 1L;

                @Override
                public void afterSaveAttachToDB(Object attach) {
                    attachUploadModel.getExtraDatas().put("msg", "上传成功");
                }

                @Override
                public boolean beforeSaveAttachToDB(AttachStorage attachStorage) {

                    return true;
                }

            };
            attachUploadModel = new FileUploadModel9(new DefaultFileUploadHandlerImpl9(newattachguid, null,
                    null, handler, userSession.getUserGuid(), userSession.getDisplayName()));
        }
        return attachUploadModel;
    }
}
