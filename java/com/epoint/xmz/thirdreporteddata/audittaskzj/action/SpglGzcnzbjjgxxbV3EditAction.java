package com.epoint.xmz.thirdreporteddata.audittaskzj.action;

import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import com.epoint.basic.spgl.inter.ISpglCommon;
import com.epoint.core.EpointFrameDsManager;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.domain.SpglGzcnzbjjgxxbV3;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.inter.ISpglGzcnzbjjgxxbV3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.UUID;

/**
 * 告知承诺制办件监管信息表修改页面对应的后台
 *
 * @author Epoint
 * @version [版本号, 2023-11-07 09:44:38]
 */
@RightRelation(com.epoint.xmz.thirdreporteddata.audittaskzj.action.SpglGzcnzbjjgxxbV3ListAction.class)
@RestController("spglgzcnzbjjgxxbv3editaction")
@Scope("request")
public class SpglGzcnzbjjgxxbV3EditAction extends BaseController
{

    @Autowired
    private ISpglGzcnzbjjgxxbV3Service service;

    @Autowired
    private ISpglCommon ispglcommon;

    /**
     * 告知承诺制办件监管信息表实体对象
     */
    private SpglGzcnzbjjgxxbV3 dataBean = null;

    private String xzqhdm;
    private String gcdm;
    private String spsxslbm;

    public void pageLoad() {
        String guid = getRequestParameter("guid");
        xzqhdm = getRequestParameter("xzqhdm");
        gcdm = getRequestParameter("gcdm");
        spsxslbm = getRequestParameter("spsxslbm");
        dataBean = service.findDominByCondition(xzqhdm, gcdm, spsxslbm);
        if (dataBean == null) {
            dataBean = new SpglGzcnzbjjgxxbV3();
            dataBean.setXzqhdm(xzqhdm);
            dataBean.setSpsxbm(spsxslbm);
            dataBean.setGcdm(gcdm);
        }
    }

    /**
     * 保存修改
     */
    public void save() {
        // 事务控制
        String msg = "上报成功！";
        try {
            EpointFrameDsManager.begin(null);

            if (StringUtil.isNotBlank(dataBean.getRowguid())) {
                SpglGzcnzbjjgxxbV3 oldDataBean = service.findDominByCondition(xzqhdm, gcdm, spsxslbm);
                ispglcommon.editToPushData(oldDataBean, dataBean, true);
            }
            else {
                dataBean.setRowguid(UUID.randomUUID().toString());
                dataBean.setOperatedate(new Date());
                dataBean.setOperateusername(userSession.getDisplayName());
                dataBean.setSjyxbs(1);
                dataBean.set("sync", "0");
                dataBean.setSjsczt(0);
                service.insert(dataBean);
            }

            EpointFrameDsManager.commit();
        }
        catch (Exception e) {
            msg = "上报失败！";
            e.printStackTrace();
            EpointFrameDsManager.rollback();
        }
        finally {
            addCallbackParam("msg", msg);
            EpointFrameDsManager.close();
        }
    }

    public SpglGzcnzbjjgxxbV3 getDataBean() {
        return dataBean;
    }

    public void setDataBean(SpglGzcnzbjjgxxbV3 dataBean) {
        this.dataBean = dataBean;
    }

}
