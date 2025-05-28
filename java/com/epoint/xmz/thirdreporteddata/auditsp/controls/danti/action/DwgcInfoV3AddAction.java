package com.epoint.xmz.thirdreporteddata.auditsp.controls.danti.action;

import com.epoint.basic.auditsp.dwgcinfo.api.IDwgcInfoService;
import com.epoint.basic.auditsp.dwgcinfo.entity.DwgcInfo;
import com.epoint.basic.auditsp.dwgcjlneed.api.IDwgcJlneedService;
import com.epoint.basic.auditsp.dwgcjlneed.entity.DwgcJlneed;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.utils.collection.EpointCollectionUtils;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.core.utils.web.WebUtil;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.frame.service.metadata.code.entity.CodeItems;
import com.epoint.xmz.thirdreporteddata.dantiinfov3.api.IDantiInfoV3Service;
import com.epoint.xmz.thirdreporteddata.dantiinfov3.api.entity.DantiInfoV3;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * 单位工程表新增页面对应的后台
 *
 * @author WIN-H366O37KOW0$
 * @version [版本号, 2018-05-18 09:48:09]
 */
@RightRelation(DantiInfov3ListAction.class)
@RestController("dwgcinfov3addaction")
@Scope("request")
public class DwgcInfoV3AddAction extends BaseController {
    private static final long serialVersionUID = -7594286459415249310L;

    @Autowired
    private IDantiInfoV3Service dantiInfoService;

    /**
     * 单体信息表（单体同子单位工程）实体对象
     */
    private DantiInfoV3 dantiInfo;
    @Autowired
    private IDwgcJlneedService dwgcJlneedService;

    @Autowired
    private IDwgcInfoService service;

    @Autowired
    private ICodeItemsService codeservie;

    /**
     * 单位工程表实体对象
     */
    private DwgcInfo dataBean;
    /**
     * 表格控件model
     */
    private DataGridModel<DwgcJlneed> model;

    /**
     * 单位工程监理人员要求表实体对象
     */
    private DwgcJlneed dwgcJlneed;
    private String rowguid;

    private Random r = new Random();

    public void pageLoad() {
        dataBean = new DwgcInfo();
        Date date = new Date();
        int num = r.nextInt(9000) + 1000;
        dataBean.setGongchengnum(EpointDateUtil.convertDate2String(date, "yyyy") + num);
    }

    /**
     * 保存并关闭
     */
    public void savebuild(String data) {

        List<DwgcInfo> dwlist = service.findDwgcListByGongchengnum(dataBean.getGongchengnum());
        if (EpointCollectionUtils.isNotEmpty(dwlist)) {
            //          addCallbackParam("msg", "单位工程编号重复！");
        } else {

            if (StringUtil.isBlank(rowguid)) {
                rowguid = UUID.randomUUID().toString();
            }
            if (StringUtil.isNotBlank(data)) {
                String[] list = data.split(",");
                for (int i = 0; i < list.length; i++) {
                    dwgcJlneed = new DwgcJlneed();
                    dwgcJlneed.setZhiwu(list[i]);
                    dwgcJlneed.setNeedcnt(Integer.valueOf(list[++i]).intValue());
                    dwgcJlneed.setGongchengguid(rowguid);
                    dwgcJlneed.setRowguid(UUID.randomUUID().toString());
                    dwgcJlneedService.insert(dwgcJlneed);
                }
            }

            @SuppressWarnings("unchecked") List<String> selectedDanTi = (List<String>) WebUtil.getSessionAttribute(
                    request.getSession(), "rowguids");
            List<DantiInfoV3> selectedDanTiInfo = new ArrayList<>();
            for (String select : selectedDanTi) {
                selectedDanTiInfo.add(dantiInfoService.find(select));
            }

            StringBuilder jiegouTx = new StringBuilder();
            StringBuilder gclb = new StringBuilder();
            Double totalprice = 0d;
            Double buildarea = 0d;
            List<String> dishang = new ArrayList<>();
            List<String> dixia = new ArrayList<>();

            for (DantiInfoV3 dantiinfo : selectedDanTiInfo) {
                if (dantiinfo.getJgtx() != null) {
                    String jiegoutixi = codeservie.getItemTextByCodeName("国标_结构体系", dantiinfo.getJgtx() + "");
                    jiegouTx.append(jiegoutixi + ";");
                }
                if (dantiinfo.getGcyt() != null) {
                    String gongchengleibie = codeservie.getItemTextByCodeName("国标_工程类别",
                            dantiinfo.getGcyt() + "");
                    gclb.append(gongchengleibie + ";");
                }
                if (dantiinfo.getDtgczzj() != null) {
                    totalprice += dantiinfo.getDtgczzj();
                }
                if (dantiinfo.getJzmj() != null) {
                    buildarea += dantiinfo.getJzmj();
                }
                if (dantiinfo.getDscs() != null) {
                    dishang.add(dantiinfo.getDscs());
                }
                if (dantiinfo.getDxcs() != null) {
                    dixia.add(dantiinfo.getDxcs());
                }
            }
            /*int dishangmax = 0;
            int dixiamax = 0;
            if (EpointCollectionUtils.isNotEmpty(dishang)) {
                dishangmax = Collections.max(dishang);
            }
            if (EpointCollectionUtils.isNotEmpty(dixia)) {
                dixiamax = Collections.max(dixia);
            }*/
            String jianzhuarea = buildarea + "平方米";

            dataBean.setJiegoutype(jiegouTx.toString());
            dataBean.setProjecttype(gclb.toString());
            dataBean.setProjectprice(totalprice);
            dataBean.setBuildarea(jianzhuarea);
            //dataBean.setDishangcs(dishangmax);
            //dataBean.setDixiacs(dixiamax);
            dataBean.setRowguid(rowguid);
            buildDanweiProject(rowguid);
            saveDwgcJlneed(rowguid);
            dataBean.setOperatedate(new Date());
            dataBean.setOperateusername(userSession.getDisplayName());
            service.insert(dataBean);
            addCallbackParam("msg", "保存成功！");
            dataBean = null;
            // 单位工程组建完毕删除待办
            String itemguid = getRequestParameter("projectnum");

        }

        //        CommonDao dao = null;
        //        try {
        //            dao = CommonDao.getInstance();
        //            String messageSql = "delete MESSAGES_CENTER where title like '【组建单位工程】%' and ClientIdentifier = ?";
        //            dao.execute(messageSql, new Object[]{itemguid});
        //        }
        //        catch (Exception e) {
        //            e.printStackTrace();
        //            dao.rollBackTransaction();
        //        }
        //        finally {
        //            dao.close();
        //        }
    }

    public DwgcInfo getDataBean() {
        if (dataBean == null) {
            dataBean = new DwgcInfo();
        }
        return dataBean;
    }

    public void setDataBean(DwgcInfo dataBean) {
        this.dataBean = dataBean;
    }

    public DataGridModel<DwgcJlneed> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<DwgcJlneed>() {
                private static final long serialVersionUID = 4585070991128742507L;

                @Override
                public List<DwgcJlneed> fetchData(int first, int pageSize, String sortField, String sortOrder) {

                    List<DwgcJlneed> list = new ArrayList<>();
                    DwgcJlneed dwgcJlneed1;
                    List<CodeItems> codeItems = codeservie.listCodeItemsByCodeName("监理工程师职务");
                    for (CodeItems codeItem : codeItems) {
                        dwgcJlneed1 = new DwgcJlneed();
                        dwgcJlneed1.setZhiwu(codeItem.getItemText());
                        dwgcJlneed1.setRowguid(UUID.randomUUID().toString());
                        list.add(dwgcJlneed1);
                    }
                    return list;
                }

            };
        }
        return model;
    }

    public void saveDwgcJlneed(String guid) {

    }

    /**
     * 删除选定
     *
     */
    //    public void deleteSelect() {
    //        List<String> select = getDataGridData().getSelectKeys();
    //        for (String sel : select) {
    //            dwgcJlneedService.deleteByGuid(sel);
    //        }
    //        addCallbackParam("msg", "成功删除！");
    //    }

    //    public DwgcJlneed getDwgcJlneed() {
    //        if (dwgcJlneed == null) {
    //            dwgcJlneed = new DwgcJlneed();
    //        }
    //        return dwgcJlneed;
    //    }

    //    public void setDwgcJlneed(DwgcJlneed dwgcJlneed) {
    //        this.dwgcJlneed = dwgcJlneed;
    //    }

    /**
     * 组建单位工程
     */
    public void buildDanweiProject(String guid) {
        @SuppressWarnings("unchecked") List<String> select = (List<String>) WebUtil.getSessionAttribute(
                request.getSession(), "rowguids");

        if (select != null) {
            for (String rowguid1 : select) {
                dantiInfo = dantiInfoService.find(rowguid1);
                if (StringUtil.isBlank(dantiInfo.getGongchengguid())) {
                    dantiInfo.setGongchengguid(guid);
                    dantiInfoService.update(dantiInfo);
                    addCallbackParam("msg", "子单位工程组建成功！");

                } else {
                    addCallbackParam("msg", "该子单位工程已经被组建！");
                }

            }
            List<String> empty = null;
            WebUtil.setSessionAttribute(request.getSession(), "rowguids", empty);
        } else {
            addCallbackParam("msg", "请选择要组建的子单位工程！");
        }
    }
}
