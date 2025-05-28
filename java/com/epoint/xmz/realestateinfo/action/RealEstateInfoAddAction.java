package com.epoint.xmz.realestateinfo.action;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.epoint.basic.auditorga.auditarea.inter.IAuditOrgaArea;
import com.epoint.frame.service.organ.ou.api.IOuService;
import com.epoint.frame.service.organ.ou.api.IOuServiceInternal;
import com.epoint.frame.service.organ.ou.entity.FrameOu;
import com.epoint.frame.service.organ.ou.entity.FrameOuExtendInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import com.epoint.basic.faces.fileupload.FileUploadModel9;
import com.epoint.basic.faces.tree.DefaultFileUploadHandlerImpl9;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.xmz.realestateinfo.api.IRealEstateInfoService;
import com.epoint.xmz.realestateinfo.api.entity.RealEstateInfo;

/**
 * 楼盘信息表新增页面对应的后台
 * 
 * @author 1
 * @version [版本号, 2022-10-17 15:27:39]
 */
@RightRelation(RealEstateInfoListAction.class)
@RestController("realestateinfoaddaction")
@Scope("request")
public class RealEstateInfoAddAction extends BaseController
{
    @Autowired
    private IRealEstateInfoService service;

    @Autowired
    private ICodeItemsService iCodeItemsService;
    /**
     * 楼盘信息表实体对象
     */
    private RealEstateInfo dataBean = null;

    @Autowired
    private IAttachService iAttachService;

    @Autowired
    private IOuServiceInternal ouServiceInternal;



    @Autowired
    private IAuditOrgaArea auditOrgaArea;

    /**
     * 楼盘状态下拉列表model
     */
    private List<SelectItem> re_stateModel = null;
    /**
     * 是否发布下拉列表model
     */
    private List<SelectItem> is_issueModel = null;

    private String fmtcliengguid = "";

    private String nktcliengguid = "";

    private String pmtcliengguid = "";

    private String qatcliengguid = "";

    private String Lurccliengguid = "";

    private String Permitcliengguid = "";

    private String ProjectPermitcliengguid = "";

    private String ProjectConPermitcliengguid = "";

    private String PreSalePermitcliengguid = "";

    private String Pcpvccliengguid = "";

    private String Fpccliengguid = "";

    private String Carccliengguid = "";

    private FileUploadModel9 fileUploadFmtModel;

    private FileUploadModel9 fileUploadNktModel;

    private FileUploadModel9 fileUploadPmtModel;

    private FileUploadModel9 fileUploadQatModel;

    private FileUploadModel9 fileUploadLurcModel;

    private FileUploadModel9 fileUploadPermitModel;

    private FileUploadModel9 fileUploadProjectPermitModel;

    private FileUploadModel9 fileUploadProjectConPermitModel;

    private FileUploadModel9 fileUploadPreSalePermitModel;

    private FileUploadModel9 fileUploadPcpvcModel;

    private FileUploadModel9 fileUploadFpcModel;

    private FileUploadModel9 fileUploadCarcModel;

    private FileUploadModel9 fileUploadFirstFloorModel;
    private FileUploadModel9 fileUploadStandFloorModel;
    private FileUploadModel9 fileUploadTopFloorModel;
    private FileUploadModel9 fileUploadHshgzModel;

    /**
     * 所属县区下拉列表model
     */
    private List<SelectItem> belong_areaModel = null;

    /**
     * 房屋类型下拉列表model
     */
    private List<SelectItem> housetypeModel = null;

    public void pageLoad() {
        fmtcliengguid = getRequestParameter("fmtcliengguid");
        nktcliengguid = getRequestParameter("nktcliengguid");
        pmtcliengguid = getRequestParameter("pmtcliengguid");
        qatcliengguid = getRequestParameter("qatcliengguid");
        Lurccliengguid = getRequestParameter("Lurccliengguid");
        Permitcliengguid = getRequestParameter("Permitcliengguid");
        ProjectPermitcliengguid = getRequestParameter("ProjectPermitcliengguid");
        ProjectConPermitcliengguid = getRequestParameter("ProjectConPermitcliengguid");
        PreSalePermitcliengguid = getRequestParameter("PreSalePermitcliengguid");
        Pcpvccliengguid = getRequestParameter("Pcpvccliengguid");
        Fpccliengguid = getRequestParameter("Fpccliengguid");
        Carccliengguid = getRequestParameter("Carccliengguid");

        dataBean = new RealEstateInfo();
        String ouGuid = userSession.getOuGuid();
        FrameOuExtendInfo frameOuExtendInfo = ouServiceInternal.getFrameOuExtendInfo(ouGuid);
        if(StringUtil.isNotBlank(frameOuExtendInfo.getStr("AREACODE"))){
            String areacode = frameOuExtendInfo.getStr("AREACODE");
            dataBean.setBelong_area(areacode);
            String areaname = iCodeItemsService.getItemTextByCodeName("所属区县", dataBean.getBelong_area());
            dataBean.set("areaname", areaname);
        }
        String firstFloorCliengGuid = getViewData("FirstFloorCliengGuid");
        if(StringUtil.isBlank(firstFloorCliengGuid)){
            String uuid = UUID.randomUUID().toString();
            addViewData("FirstFloorCliengGuid",uuid);
        }

        String standFloorCliengGuid = getViewData("StandFloorCliengGuid");
        if(StringUtil.isBlank(standFloorCliengGuid)){
            String uuid = UUID.randomUUID().toString();
            addViewData("StandFloorCliengGuid",uuid);
        }

        String topFloorCliengGuidd = getViewData("TopFloorCliengGuidd");
        if(StringUtil.isBlank(topFloorCliengGuidd)){
            String uuid = UUID.randomUUID().toString();
            addViewData("TopFloorCliengGuidd",uuid);
        }
        String hshgzCliengGuidd = getViewData("hshgzCliengGuidd");
        if(StringUtil.isBlank(hshgzCliengGuidd)){
            String uuid = UUID.randomUUID().toString();
            addViewData("hshgzCliengGuidd",uuid);
        }
    }

    /**
     * 保存并关闭
     * 
     */
    public void add() {
        dataBean.setRowguid(UUID.randomUUID().toString());
        dataBean.setOperatedate(new Date());
        dataBean.setOperateusername(userSession.getDisplayName());
        dataBean.setCover_plan(fmtcliengguid);
        dataBean.setAerial_view(nktcliengguid);
        dataBean.setSite_plan(pmtcliengguid);
        dataBean.setOther_pic(qatcliengguid);
        dataBean.setLurc(Lurccliengguid);
        dataBean.setLand_use_permit(Permitcliengguid);
        dataBean.setProject_permit(ProjectPermitcliengguid);
        dataBean.setProject_con_permit(ProjectConPermitcliengguid);
        dataBean.setPre_sale_permit(PreSalePermitcliengguid);
        dataBean.setPcpvc(Pcpvccliengguid);
        dataBean.setFpc(Fpccliengguid);
        dataBean.setCarc(Carccliengguid);
        String firstFloorCliengGuid = getViewData("FirstFloorCliengGuid");
        dataBean.set("firstfloor_pic",firstFloorCliengGuid);

        String standFloorCliengGuid = getViewData("StandFloorCliengGuid");
        dataBean.set("standfloor_pic",standFloorCliengGuid);

        String topFloorCliengGuidd = getViewData("TopFloorCliengGuidd");
        dataBean.set("topfloor_pic",topFloorCliengGuidd);
        String hshgzCliengGuidd = getViewData("hshgzCliengGuidd");
        dataBean.set("hshgz",hshgzCliengGuidd);
        dataBean.setPv(0);

        String areaname = iCodeItemsService.getItemTextByCodeName("所属区县", dataBean.getBelong_area());

        if (StringUtil.isNotBlank(areaname)) {
            dataBean.set("areaname", areaname);
        }

        service.insert(dataBean);
        addCallbackParam("msg", "保存成功！");
        dataBean = null;
    }

    /**
     * 保存并新建
     * 
     */
    public void addNew() {
        add();
        dataBean = new RealEstateInfo();
    }

    public RealEstateInfo getDataBean() {
        if (dataBean == null) {
            dataBean = new RealEstateInfo();
        }
        return dataBean;
    }

    public void setDataBean(RealEstateInfo dataBean) {
        this.dataBean = dataBean;
    }

    public List<SelectItem> getRe_stateModel() {
        if (re_stateModel == null) {
            re_stateModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "楼盘状态", null, false));
        }
        return this.re_stateModel;
    }

    public List<SelectItem> getIs_issueModel() {
        if (is_issueModel == null) {
            is_issueModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "是否", null, false));
        }
        return this.is_issueModel;
    }

    public List<SelectItem> getBelong_areaModel() {
        if (belong_areaModel == null) {
            belong_areaModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "所属区县", null, false));
        }
        return this.belong_areaModel;
    }

    public List<SelectItem> getHousetypeModel() {
        if (housetypeModel == null) {
            housetypeModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "房屋类型", null, false));
        }
        return this.housetypeModel;
    }

    public FileUploadModel9 getFileUploadFmtModel() {
        if (fileUploadFmtModel == null) {
            fileUploadFmtModel = new FileUploadModel9(new DefaultFileUploadHandlerImpl9(fmtcliengguid, null, null, null,
                    userSession.getUserGuid(), userSession.getDisplayName()));
        }
        return fileUploadFmtModel;
    }

    public FileUploadModel9 getFileUploadNktModel() {
        if (fileUploadNktModel == null) {
            fileUploadNktModel = new FileUploadModel9(new DefaultFileUploadHandlerImpl9(nktcliengguid, null, null, null,
                    userSession.getUserGuid(), userSession.getDisplayName()));
        }
        return fileUploadNktModel;
    }

    public FileUploadModel9 getFileUploadPmtModel() {
        if (fileUploadPmtModel == null) {
            fileUploadPmtModel = new FileUploadModel9(new DefaultFileUploadHandlerImpl9(pmtcliengguid, null, null, null,
                    userSession.getUserGuid(), userSession.getDisplayName()));
        }
        return fileUploadPmtModel;
    }

    public FileUploadModel9 getFileUploadQatModel() {
        if (fileUploadQatModel == null) {
            fileUploadQatModel = new FileUploadModel9(new DefaultFileUploadHandlerImpl9(qatcliengguid, null, null, null,
                    userSession.getUserGuid(), userSession.getDisplayName()));
        }
        return fileUploadQatModel;
    }

    public FileUploadModel9 getFileUploadLurcModel() {
        if (fileUploadLurcModel == null) {
            fileUploadLurcModel = new FileUploadModel9(new DefaultFileUploadHandlerImpl9(Lurccliengguid, null, null,
                    null, userSession.getUserGuid(), userSession.getDisplayName()));
        }
        return fileUploadLurcModel;
    }

    public FileUploadModel9 getFileUploadPermitModel() {
        if (fileUploadPermitModel == null) {
            fileUploadPermitModel = new FileUploadModel9(new DefaultFileUploadHandlerImpl9(Permitcliengguid, null, null,
                    null, userSession.getUserGuid(), userSession.getDisplayName()));
        }
        return fileUploadPermitModel;
    }

    public FileUploadModel9 getFileUploadProjectPermitModel() {
        if (fileUploadProjectPermitModel == null) {
            fileUploadProjectPermitModel = new FileUploadModel9(
                    new DefaultFileUploadHandlerImpl9(ProjectPermitcliengguid, null, null, null,
                            userSession.getUserGuid(), userSession.getDisplayName()));
        }
        return fileUploadProjectPermitModel;
    }

    public FileUploadModel9 getFileUploadProjectConPermitModel() {
        if (fileUploadProjectConPermitModel == null) {
            fileUploadProjectConPermitModel = new FileUploadModel9(
                    new DefaultFileUploadHandlerImpl9(ProjectConPermitcliengguid, null, null, null,
                            userSession.getUserGuid(), userSession.getDisplayName()));
        }
        return fileUploadProjectConPermitModel;
    }

    public FileUploadModel9 getFileUploadPreSalePermitModel() {
        if (fileUploadPreSalePermitModel == null) {
            fileUploadPreSalePermitModel = new FileUploadModel9(
                    new DefaultFileUploadHandlerImpl9(PreSalePermitcliengguid, null, null, null,
                            userSession.getUserGuid(), userSession.getDisplayName()));
        }
        return fileUploadPreSalePermitModel;
    }

    public FileUploadModel9 getFileUploadPcpvcModel() {
        if (fileUploadPcpvcModel == null) {
            fileUploadPcpvcModel = new FileUploadModel9(new DefaultFileUploadHandlerImpl9(Pcpvccliengguid, null, null,
                    null, userSession.getUserGuid(), userSession.getDisplayName()));
        }
        return fileUploadPcpvcModel;
    }

    public FileUploadModel9 getFileUploadFpcModel() {
        if (fileUploadFpcModel == null) {
            fileUploadFpcModel = new FileUploadModel9(new DefaultFileUploadHandlerImpl9(Fpccliengguid, null, null, null,
                    userSession.getUserGuid(), userSession.getDisplayName()));
        }
        return fileUploadFpcModel;
    }

    public FileUploadModel9 getFileUploadCarcModel() {
        if (fileUploadCarcModel == null) {
            fileUploadCarcModel = new FileUploadModel9(new DefaultFileUploadHandlerImpl9(Carccliengguid, null, null,
                    null, userSession.getUserGuid(), userSession.getDisplayName()));
        }
        return fileUploadCarcModel;
    }
    public FileUploadModel9 getFileUploadFirstFloorModel() {
        if (fileUploadFirstFloorModel == null) {
            fileUploadFirstFloorModel = new FileUploadModel9(new DefaultFileUploadHandlerImpl9(getViewData("FirstFloorCliengGuid"), null, null,
                    null, userSession.getUserGuid(), userSession.getDisplayName()));
        }
        return fileUploadFirstFloorModel;
    }
    public FileUploadModel9 getFileUploadStandFloorModel() {
        if (fileUploadStandFloorModel == null) {
            fileUploadStandFloorModel = new FileUploadModel9(new DefaultFileUploadHandlerImpl9(getViewData("StandFloorCliengGuid"), null, null,
                    null, userSession.getUserGuid(), userSession.getDisplayName()));
        }
        return fileUploadStandFloorModel;
    }
    public FileUploadModel9 getFileUploadTopFloorModel() {
        if (fileUploadTopFloorModel == null) {
            fileUploadTopFloorModel = new FileUploadModel9(new DefaultFileUploadHandlerImpl9(getViewData("TopFloorCliengGuidd"), null, null,
                    null, userSession.getUserGuid(), userSession.getDisplayName()));
        }
        return fileUploadTopFloorModel;
    }
    public FileUploadModel9 getFileUploadHshgzModel() {
        if (fileUploadHshgzModel == null) {
            fileUploadHshgzModel = new FileUploadModel9(new DefaultFileUploadHandlerImpl9(getViewData("hshgzCliengGuidd"), null, null,
                    null, userSession.getUserGuid(), userSession.getDisplayName()));
        }
        return fileUploadHshgzModel;
    }

    /**
     * 同步证照
     */
    public void certsync(String projectnum) {
        String jsydgh_cr_Itemid = configService.getFrameConfigValue("jsydgh_crItemid");// 建设用地规划出让类事项
        String jsydgh_hb_Itemid = configService.getFrameConfigValue("jsydgh_hb_Itemid");// 建设用地规划出划拨事项
        String jzgcsg_Itemid = configService.getFrameConfigValue("jzgcsg_Itemid");// 建筑工程施工许可证核发
        String jzgcjggh_Itemid = configService.getFrameConfigValue("jzgcjggh_Itemid");// 建筑工程竣工规划核实
        String jsgcgh_Itemid = configService.getFrameConfigValue("jsgcgh_Itemid");// 建设工程规划许可证
        String spf_Itmid = configService.getFrameConfigValue("spf_Itmid");// 商品房预售许可

        // TODO 根据项目查询对应办件 对应的证照
        List<AuditTask> certList = service.getCertListByProjectNum(projectnum);
        for (AuditTask task : certList) {
            if (StringUtil.isNotBlank(task.getStr("certcliengguid"))) {
                // //system.out.println("==projectnum:" + projectnum +
                // "===taskname:" + task.getStr("taskname")
                // + "===certcliengguid:" + task.getStr("certcliengguid"));
                // 建设用地规划出让类事项
                if (jsydgh_cr_Itemid.equals(task.getStr("taskname"))) {
                    iAttachService.copyAttachByClientGuid(task.getStr("certcliengguid"), "", "", Permitcliengguid);
                }
                //建设用地规划出划拨事项
                else if (jsydgh_hb_Itemid.equals(task.getStr("taskname"))) {
                    iAttachService.copyAttachByClientGuid(task.getStr("certcliengguid"), "", "", Permitcliengguid);
                }
                //建筑工程施工许可证核发
                else if (jzgcsg_Itemid.equals(task.getStr("taskname"))) {
                    iAttachService.copyAttachByClientGuid(task.getStr("certcliengguid"), "", "",
                            ProjectConPermitcliengguid);
                }
                //建筑工程竣工规划核实
                else if (jzgcjggh_Itemid.equals(task.getStr("taskname"))) {
                    iAttachService.copyAttachByClientGuid(task.getStr("certcliengguid"), "", "", Pcpvccliengguid);

                }
                //建设工程规划许可证
                else if (jsgcgh_Itemid.equals(task.getStr("taskname"))) {
                    iAttachService.copyAttachByClientGuid(task.getStr("certcliengguid"), "", "",
                            ProjectPermitcliengguid);
                }
                //商品房预售许可
                else if (spf_Itmid.equals(task.getStr("taskname"))) {
                    iAttachService.copyAttachByClientGuid(task.getStr("certcliengguid"), "", "",
                            PreSalePermitcliengguid);

                }
                addCallbackParam("msg","同步结束");
            }
        }

    }
}
