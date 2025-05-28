package com.epoint.cert.basic.certcatalog.oucertcatalog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.cert.basic.certcatalog.areacertcatalog.domain.CertCatalog;
import com.epoint.cert.basic.certcatalog.areacertcatalog.inter.ICertCatalog;
import com.epoint.cert.commonutils.CertConstant;
import com.epoint.cert.commonutils.SqlUtils;
import com.epoint.common.cert.authentication.CodeBizlogic;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;

/**
 * 部门证照目录list页面对应的后台
 * update 修改了获取到certificatetypecode的参数进行搜索
 * @作者 dingwei
 * @version [版本号, 2017年10月28日]
 */
@RestController("jnoucertcataloglistaction")
@Scope("request")
public class JNOUCertCatalogListAction extends BaseController
{
    private static final long serialVersionUID = -4197136998082536058L;

    /**
     * 证照目录实体对象
     */
    private CertCatalog dataBean;

    /**
     * 表格控件model
     */
    private DataGridModel<CertCatalog> model;

    /**
     * 所属行业下拉列表model
     */
    private List<SelectItem> belongtypeModel;

    /**
     * 证照分类下拉列表model
     */
    private List<SelectItem> kindModel;

    /**
     * 证照目录api
     */
    @Autowired
    private ICertCatalog iCertCatalog;

    /**
     * 代码项bizlogic
     */
    private CodeBizlogic codeBizlogic = new CodeBizlogic();

    @Override
    public void pageLoad() {
        if (dataBean == null) {
            dataBean = new CertCatalog();
        }
    }

    /**
     * 获得授权过的证照目录
     *
     * @return
     */
    public DataGridModel<CertCatalog> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<CertCatalog>()
            {
                @Override
                public List<CertCatalog> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    SqlUtils sql = new SqlUtils();

                    sql.eq("ishistory", CertConstant.CONSTANT_STR_ZERO);
                    //默认查证照
                    sql.eq("materialtype", CertConstant.CONSTANT_STR_ONE);
                    // 启用的目录
                    sql.eq("isenable", CertConstant.CONSTANT_STR_ONE);
                    // 证照名称
                    if (StringUtil.isNotBlank(dataBean.getCertname())) {
                        sql.like("certname", dataBean.getCertname());
                    }
                    //证照类型代码
                    if (StringUtil.isNotBlank(dataBean.getCertificatetypecode())) {
                        sql.like("certificatetypecode", dataBean.getCertificatetypecode());
                    }
                    // 证照目录编号
                    if (StringUtil.isNotBlank(dataBean.getTycertcatcode())) {
                        sql.like("tycertcatcode", dataBean.getTycertcatcode());
                    }
                    // 证照持有人类型
                    if (StringUtil.isNotBlank(dataBean.getBelongtype())) {
                        sql.like("belongtype", dataBean.getBelongtype());
                    }
                    // 证照分类
                    if (StringUtil.isNotBlank(dataBean.getKind())) {
                        sql.eq("kind", dataBean.getKind());
                    }

                    // 搜索部门条件
                    String ouguid = "";
                    if (StringUtil.isNotBlank(userSession.getBaseOUGuid())) {
                        ouguid = userSession.getBaseOUGuid();
                    }
                    else {
                        ouguid = userSession.getOuGuid();
                    }
                    sql.setOrder(sortField, sortOrder);
                    PageData<CertCatalog> pageData = iCertCatalog.getSynchronizedCertCatalogPageData(sql.getMap(),
                            first, pageSize, sortField, sortOrder, ouguid);
                    List<CertCatalog> certCatalogs = pageData.getList();
                    //system.out.println(certCatalogs);
                    List<CertCatalog> results = new ArrayList<>();
                    Map<String, String> parentMap = new HashMap<>();
                    boolean sort = false;
                    for (CertCatalog certCatalog : certCatalogs) {
                        if (CertConstant.CONSTANT_INT_ZERO.equals(certCatalog.getIsparent())) {
                            if (parentMap.containsKey(certCatalog.getParentid())) {
                                certCatalog.put("pid", parentMap.get(certCatalog.getParentid()));
                                if (sort) {
                                    listSortAdd(results, certCatalog, false);
                                }
                                else {
                                    results.add(certCatalog);
                                }
                            }
                            else {
                                CertCatalog parentCatalog = iCertCatalog
                                        .getLatestCatalogDetailByCatalogid(certCatalog.getParentid());
                                if (parentCatalog != null) {
                                    certCatalog.put("pid", parentCatalog.getRowguid());
                                    parentMap.put(parentCatalog.getCertcatalogid(), parentCatalog.getRowguid());
                                    parentCatalog.put("expanded", false);
                                    listSortAdd(results, parentCatalog, true);
                                    listSortAdd(results, certCatalog, false);
                                    sort = true;
                                }
                            }
                        }
                        else {
                            if (!parentMap.containsKey(certCatalog.getCertcatalogid())) {
                                parentMap.put(certCatalog.getCertcatalogid(), certCatalog.getRowguid());
                                certCatalog.put("expanded", false);
                                results.add(certCatalog);
                            }
                        }
                    }
                    this.setRowCount(results.size());
                    return results;
                }
            };
        }
        return model;
    }

    private void listSortAdd(List<CertCatalog> certCatalogs, CertCatalog catalog, Boolean isFromTop) {
        if (certCatalogs.size() == 0) {
            certCatalogs.add(catalog);
            return;
        }
        int i;
        Integer ordernum = catalog.getOrdernum() == null ? 0 : catalog.getOrdernum();
        if (isFromTop) {

            for (i = 0; i < certCatalogs.size(); i++) {
                Integer listorder = certCatalogs.get(i).getOrdernum() == null ? 0 : certCatalogs.get(i).getOrdernum();
                if (ordernum >= listorder) {
                    break;
                }
            }
            certCatalogs.add(i, catalog);
        }
        else {
            for (i = certCatalogs.size() - 1; i >= 0; i--) {
                Integer listorder = certCatalogs.get(i).getOrdernum() == null ? 0 : certCatalogs.get(i).getOrdernum();
                if (ordernum <= listorder) {
                    break;
                }
            }
            certCatalogs.add(i + 1, catalog);
        }
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> getbelongtypeModel() {
        if (belongtypeModel == null) {
            belongtypeModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "证照持有人类型", null, false));
        }
        return this.belongtypeModel;
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> getkindModel() {
        if (kindModel == null) {
            kindModel = new ArrayList<>();
            if("001".equals(dataBean.getBelongtype()) || "002".equals(dataBean.getBelongtype())){
                kindModel = codeBizlogic.getSelectedItemsListByPrefix("证照分类", dataBean.getBelongtype(), false);
            }else{
                kindModel = codeBizlogic.getSelectedItemsListByPrefix("证照分类", "003", false);
            }
        }
        return this.kindModel;
    }

    public CertCatalog getDataBean() {
        return dataBean;
    }

    public void setDataBean(CertCatalog dataBean) {
        this.dataBean = dataBean;
    }

    public DataGridModel<CertCatalog> getModel() {
        return model;
    }

    public void setModel(DataGridModel<CertCatalog> model) {
        this.model = model;
    }

}
