package com.epoint.cert.basic.certcatalog.areacertcatalog;

import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.fileupload.FileUploadModel9;
import com.epoint.cert.basic.certcatalog.areacertcatalog.domain.CertCatalog;
import com.epoint.cert.basic.certcatalog.areacertcatalog.inter.ICertCatalog;
import com.epoint.cert.commonutils.SqlUtils;
import com.epoint.cert.commonutils.ValidateUtil;
import com.epoint.common.cert.authentication.AttachBizlogic;
import com.epoint.common.cert.authentication.CertInfoBizlogic;
import com.epoint.common.cert.authentication.ConfigBizlogic;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.json.JsonUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

@RestController("jncertcatalogdetailwithversionaction")
@Scope("request")
public class JnCertCatalogDetailWithVersionAction extends BaseController
{
  private static final long serialVersionUID = 1L;
  private CertCatalog certCatalog;

  @Autowired
  private ICertCatalog iCertCatalog;

  @Autowired
  private ICodeItemsService iCodeItemsService;
  private CertInfoBizlogic certInfoBizlogic;
  private AttachBizlogic attachBizlogic;
  private List<SelectItem> certcatalogVersionModal;
  private FileUploadModel9 certFileUploadModel;
  private FileUploadModel9 copyCertFileUploadModel;
  private FileUploadModel9 tdcertFileUploadModel;
  private FileUploadModel9 tdcopyCertFileUploadModel;
  private FileUploadModel9 colourFileUploadModel;
  private FileUploadModel9 grayFileUploadModel;
  private FileUploadModel9 blankFileUploadModel;
  private FileUploadModel9 exampleFileUploadModel;
  private String guid;
  private String parentcertname;

  private String ispush;
  private String pushcerttypeStr;

  public JnCertCatalogDetailWithVersionAction()
  {
    this.certInfoBizlogic = new CertInfoBizlogic();

    this.attachBizlogic = new AttachBizlogic();
  }

  public void pageLoad()
  {
    this.guid = getRequestParameter("guid");
    this.certCatalog = this.iCertCatalog.getCatalogDetailByrowguid(this.guid);
    if (this.certCatalog != null) {
      CertCatalog parentCatalog = this.iCertCatalog.getLatestCatalogDetailByCatalogid(this.certCatalog.getParentid());
      if (parentCatalog != null) {
        this.parentcertname = parentCatalog.getCertname();
      }
    }
    if(certCatalog != null){
      ispush = certCatalog.get("is_push");
      if(StringUtil.isBlank(ispush)){
        ispush = "0";
      }
      pushcerttypeStr = certCatalog.get("pushcerttype");
    }
    addCallbackParam("officeweb365url", new ConfigBizlogic().getOfficeConfig());
    addCallbackParam("ofd", ConfigUtil.getConfigValue("ofdserviceurl"));
    addCallbackParam("ofdpreviewurl", ConfigUtil.getConfigValue("ofdpreviewurl"));
  }

  public List<SelectItem> getVersionModal()
  {
    if (this.certcatalogVersionModal == null) {
      this.certcatalogVersionModal = new ArrayList<SelectItem>();

      if (this.certCatalog == null) {
        return this.certcatalogVersionModal;
      }

      SqlUtils sqlUtils = new SqlUtils();
      sqlUtils.eq("certcatalogid", this.certCatalog.getCertcatalogid());

      sqlUtils.eq("materialtype", "1");

      sqlUtils.setSelectFields("rowguid, version");

      sqlUtils.nq("ishistory", "-1");

      sqlUtils.setOrderDesc("version");
      List<CertCatalog> certCatalogList = this.iCertCatalog.selectCertCatalogByCondition(sqlUtils.getMap());

      if (ValidateUtil.isNotBlankCollection(certCatalogList)) {
        for (CertCatalog certCatalogItem : certCatalogList) {
          this.certcatalogVersionModal
            .add(new SelectItem(certCatalogItem
            .getRowguid(), certCatalogItem.getVersion().toString()));
        }
      }
    }
    return this.certcatalogVersionModal;
  }

  public void compareVersion(String compareVersionGuid) {
    if (StringUtil.isBlank(compareVersionGuid)) {
      return;
    }

    Map<String, Object> certDifference = new HashMap<String,Object>();

    if (this.certCatalog.getRowguid().equals(compareVersionGuid)) {
      addCallbackParam("certDifference", certDifference);
      return;
    }
    CertCatalog compareCertCatalog = this.iCertCatalog.getCatalogDetailByrowguid(compareVersionGuid);
    if (compareCertCatalog == null)
      return;
    certDifference = this.certInfoBizlogic.compare2Bean(this.certCatalog, compareCertCatalog, CertCatalog.class, null);

    String belongtype = (String)certDifference.get("belongtype");
    if (StringUtil.isNotBlank(belongtype)) {
      certDifference.put("belongtype", this.iCodeItemsService.getItemTextByCodeName("证照持有人类型", belongtype));
    }

    String industry = (String)certDifference.get("industry");
    if (StringUtil.isNotBlank(industry)) {
      certDifference.put("industry", this.iCodeItemsService.getItemTextByCodeName("证照所属行业", industry));
    }

    String kind = (String)certDifference.get("kind");
    if (StringUtil.isNotBlank(kind)) {
      certDifference.put("kind", this.iCodeItemsService.getItemTextByCodeName("证照分类", kind));
    }
    String certificatedefineauthoritylevel = (String)certDifference.get("certificatedefineauthoritylevel");
    if (StringUtil.isNotBlank(certificatedefineauthoritylevel)) {
      certDifference.put("certificatedefineauthoritylevel", this.iCodeItemsService
        .getItemTextByCodeName("机构级别", industry));
    }

    String isparent = (String)certDifference.get("isparent");
    if (StringUtil.isNotBlank(isparent)) {
      certDifference.put("isparent", this.iCodeItemsService.getItemTextByCodeName("是否", isparent));
    }

    String isuseapi = (String)certDifference.get("isuseapi");
    if (StringUtil.isNotBlank(isuseapi)) {
      certDifference.put("isuseapi", this.iCodeItemsService.getItemTextByCodeName("是否", isuseapi));
    }

    String issurvery = (String)certDifference.get("issurvery");
    if (StringUtil.isNotBlank(issurvery)) {
      certDifference.put("issurvery", this.iCodeItemsService.getItemTextByCodeName("是否", issurvery));
    }

    String isstandardcert = (String)certDifference.get("isstandardcert");
    if (StringUtil.isNotBlank(isstandardcert)) {
      certDifference.put("isstandardcert", this.iCodeItemsService.getItemTextByCodeName("是否", isstandardcert));
    }

    String ismultiowners = String.valueOf(certDifference.get("ismultiowners"));
    if (StringUtil.isNotBlank(ismultiowners)) {
      certDifference.put("ismultiowners", this.iCodeItemsService.getItemTextByCodeName("是否", ismultiowners));
    }

    String isuploadlevelccert = (String)certDifference.get("isuploadlevelccert");
    if (StringUtil.isNotBlank(isuploadlevelccert)) {
      certDifference.put("isuploadlevelccert", this.iCodeItemsService
        .getItemTextByCodeName("是否", isuploadlevelccert));
    }

    String isonecertinfo = (String)certDifference.get("isoneCertInfo");
    if (StringUtil.isNotBlank(isonecertinfo)) {
      certDifference.put("isonecertinfo", this.iCodeItemsService.getItemTextByCodeName("是否", isonecertinfo));
    }

    String isexpirealert = (String)certDifference.get("isexpirealert");
    if (StringUtil.isNotBlank(isexpirealert)) {
      certDifference.put("isexpirealert", this.iCodeItemsService.getItemTextByCodeName("是否", isexpirealert));
    }

    String parentid = (String)certDifference.get("parentid");
    if (StringUtil.isNotBlank(parentid)) {
      CertCatalog parentCatalog = this.iCertCatalog.getLatestCatalogDetailByCatalogid(parentid);
      if (parentCatalog != null) {
        certDifference.put("parentcertname", parentCatalog.getCertname());
      }
    }

    certDifference.remove("templetcliengguid");
    certDifference.remove("copytempletcliengguid");

    if (StringUtil.isNotBlank(certDifference.get("colourcliengguid"))) {
      certDifference.remove("colourcliengguid", certDifference.get("colourcliengguid"));
    }
    if (StringUtil.isNotBlank(certDifference.get("graycliengguid"))) {
      certDifference.remove("graycliengguid", certDifference.get("graycliengguid"));
    }

    certDifference.remove("blankcliengguid");
    certDifference.remove("examplecliengguid");

    addCallbackParam("certDifference", JsonUtil.objectToJson(certDifference));
  }

  public FileUploadModel9 getCertFileUploadModel()
  {
    if ((this.certFileUploadModel == null) && 
      (StringUtil.isNotBlank(this.certCatalog.getTempletcliengguid()))) {
      this.certFileUploadModel = this.attachBizlogic.miniOaUploadAttach(this.certCatalog.getTempletcliengguid(), "证照模板文件");
    }

    return this.certFileUploadModel;
  }

  public FileUploadModel9 getCopyCertFileUploadModel() {
    if ((this.copyCertFileUploadModel == null) && 
      (StringUtil.isNotBlank(this.certCatalog.getCopytempletcliengguid()))) {
      this.copyCertFileUploadModel = this.attachBizlogic.miniOaUploadAttach(this.certCatalog.getCopytempletcliengguid(), "证照模板文件（副本）");
    }

    return this.copyCertFileUploadModel;
  }

  public FileUploadModel9 getTdCertFileUploadModel() {
    if ((this.tdcertFileUploadModel == null) && 
      (StringUtil.isNotBlank(this.certCatalog.getTdTempletcliengguid()))) {
      this.tdcertFileUploadModel = this.attachBizlogic.miniOaUploadAttach(this.certCatalog.getTdTempletcliengguid(), "套打证照模板文件");
    }

    return this.tdcertFileUploadModel;
  }

  public FileUploadModel9 getTdCopyCertFileUploadModel() {
    if ((this.tdcopyCertFileUploadModel == null) && 
      (StringUtil.isNotBlank(this.certCatalog.getTdCopytempletcliengguid()))) {
      this.tdcopyCertFileUploadModel = this.attachBizlogic.miniOaUploadAttach(this.certCatalog.getTdCopytempletcliengguid(), "套打证照模板文件（副本）");
    }

    return this.tdcopyCertFileUploadModel;
  }

  public FileUploadModel9 getColourFileUploadModel() {
    if ((this.colourFileUploadModel == null) && 
      (StringUtil.isNotBlank(this.certCatalog.getColourcliengguid()))) {
      this.colourFileUploadModel = this.attachBizlogic.miniOaUploadAttach(this.certCatalog.getColourcliengguid(), "彩色封面底图");
    }

    return this.colourFileUploadModel;
  }

  public FileUploadModel9 getGrayFileUploadModel() {
    if ((this.grayFileUploadModel == null) && 
      (StringUtil.isNotBlank(this.certCatalog.getGraycliengguid()))) {
      this.grayFileUploadModel = this.attachBizlogic.miniOaUploadAttach(this.certCatalog.getGraycliengguid(), "灰色封面底图");
    }

    return this.grayFileUploadModel;
  }

  public FileUploadModel9 getBlankFileUploadModel()
  {
    if ((this.blankFileUploadModel == null) && 
      (StringUtil.isNotBlank(this.certCatalog.getBlankcliengguid()))) {
      this.blankFileUploadModel = this.attachBizlogic.miniOaUploadAttach(this.certCatalog.getBlankcliengguid(), "空白底图");
    }

    return this.blankFileUploadModel;
  }

  public FileUploadModel9 getExampleFileUploadModel()
  {
    if ((this.exampleFileUploadModel == null) && 
      (StringUtil.isNotBlank(this.certCatalog.getExamplecliengguid()))) {
      this.exampleFileUploadModel = this.attachBizlogic.miniOaUploadAttach(this.certCatalog.getExamplecliengguid(), "示例底图");
    }

    return this.exampleFileUploadModel;
  }

  public String getGuid() {
    return this.guid;
  }

  public void setGuid(String guid) {
    this.guid = guid;
  }

  public CertCatalog getCertCatalog() {
    return this.certCatalog;
  }

  public void setCertCatalog(CertCatalog certCatalog) {
    this.certCatalog = certCatalog;
  }

  public String getParentcertname() {
    return this.parentcertname;
  }

  public void setParentcertname(String parentcertname) {
    this.parentcertname = parentcertname;
  }

  public String getIspush() {
    return ispush;
  }

  public void setIspush(String ispush) {
    this.ispush = ispush;
  }

  public String getPushcerttypeStr() {
    return pushcerttypeStr;
  }

  public void setPushcerttypeStr(String pushcerttypeStr) {
    this.pushcerttypeStr = pushcerttypeStr;
  }
}