package com.epoint.cert.auditcertrelation.action;

import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.tree.TreeData;
import com.epoint.basic.faces.tree.TreeFunction9;
import com.epoint.cert.basic.certcatalog.areacertcatalog.domain.CertCatalog;
import com.epoint.cert.external.ICertConfigExternal;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.core.dto.base.TreeNode;
import com.epoint.core.dto.model.TreeModel;
import com.epoint.core.utils.collection.EpointCollectionUtils;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.metadata.code.api.ICodeItemsServiceInternal;
import com.epoint.frame.service.metadata.code.api.ICodeMainServiceInternal;
import com.epoint.frame.service.metadata.code.entity.CodeItems;
import com.epoint.frame.service.metadata.code.entity.CodeMain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 选择关联的证照的后台
 *
 * @author Dong
 * @version [版本号, 2016-10-09 09:14:32]
 */
@RestController("auditcertrelationselectaction")
@Scope("request")
public class AuditCertRelationSelectAction  extends BaseController {

    /**
     *
     */
    private static final long serialVersionUID = -4941065244342910006L;
    @Autowired
    ICodeMainServiceInternal codeMainImpl;
    @Autowired
    ICodeItemsServiceInternal codeItemsImpl;
    @Autowired
    private ICertConfigExternal certConfigExternalImpl;
    /**
     * 新增的时候显示的材料
     */
    private String certid;

    private TreeModel treeModel;

    @Override
    public void pageLoad() {
        certid = this.getRequestParameter("certid");
    }

    public TreeModel getTreeModel() {
        if (treeModel == null) {
            treeModel = new TreeModel()
            {
                private static final long serialVersionUID = 1L;

                public List<TreeNode> fetch(TreeNode treeNode) {
                    List<TreeNode> list = new ArrayList<>();
                    TreeData treeData = TreeFunction9.getData(treeNode);
                    //首次加载树结构
                    if (treeData == null) {
                        TreeNode root = new TreeNode();
                        root.setText("证照目录");
                        root.setId("f9root");
                        root.setPid("-1");
                        list.add(root);
                    }
                    else{
                        String searchCondition = treeNode.getSearchCondition();
                        String areacode = "";
                        if (Integer.parseInt(ZwfwUserSession.getInstance().getCitylevel()) > Integer.parseInt(ZwfwConstant.AREA_TYPE_XQJ)){
                            areacode = ZwfwUserSession.getInstance().getBaseAreaCode();
                        }else{
                            areacode = ZwfwUserSession.getInstance().getAreaCode();
                        }
                        List<CertCatalog> lists = certConfigExternalImpl.selectCatalogByAreaCode(
                                areacode, searchCondition,"","" , false);

                        if(EpointCollectionUtils.isNotEmpty(lists)){
                            for (CertCatalog certCatalog : lists){
                                TreeNode node = new TreeNode();
                                if (StringUtil.isNotBlank(certid)
                                        && certid.equals(certCatalog.getCertcatalogid())) {
                                    node.setChecked(true);
                                }
                                node.setId(certCatalog.getCertcatalogid());
                                node.setText(certCatalog.getCertname());
                                node.setPid("f9root");
                                node.setCkr(true);
                                node.setLeaf(true);
                                list.add(node);
                            }
                        }
                    }
                    return list;
                }
            };
        }
        return treeModel;

    }

    private List<CertCatalog> getCertCatalogByName(String materialtype, String certname) {
        List<CertCatalog> list = new ArrayList<CertCatalog>();
        if (StringUtil.isNotBlank(materialtype)) {
            String areacode="";
            // 证照和批文要按区域划分
            if (ZwfwConstant.Material_ZZ.equals(materialtype) || ZwfwConstant.Material_PW.equals(materialtype)) {
                if (Integer.parseInt(ZwfwUserSession.getInstance().getCitylevel()) > Integer.parseInt(ZwfwConstant.AREA_TYPE_XQJ)){
                    areacode = ZwfwUserSession.getInstance().getBaseAreaCode();
                }else{
                    areacode = ZwfwUserSession.getInstance().getAreaCode();
                }
            }
            list = certConfigExternalImpl.selectCatalogByAreaCode(areacode,certname,materialtype,"" , false);
        }
        return list;
    }
}
