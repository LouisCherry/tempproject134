package com.epoint.knowledge.kinforead.action;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.bizlogic.mis.CommonService;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.export.ExportModel;
import com.epoint.basic.faces.tree.LazyTreeModal9;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.knowledge.category.service.CnsKinfoCategoryService;
import com.epoint.knowledge.common.ResourceModelService;
import com.epoint.knowledge.common.domain.CnsKinfo;
import com.epoint.knowledge.common.domain.CnsKinfoCategory;
import com.epoint.knowledge.common.domain.CnsKinfoCollect;
import com.epoint.knowledge.kinforead.service.CnsKinfoCollectService;
import com.epoint.knowledge.oumanage.service.CnsKinfoService;


/**
 * 知识信息表list页面对应的后台
 * 
 * @author xuyunhai
 * @version [版本号, 2017-02-13 10:28:44]
 */
@RestController("kinfolistallaction")
@Scope("request")
public class KinfoListAllAction extends BaseController
{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private CnsKinfoService cnsKinfoService = new CnsKinfoService();
    private CommonService service = new CommonService();
    private ResourceModelService commonModelService = new ResourceModelService();
    private CnsKinfoCollectService kinfoCollectService = new CnsKinfoCollectService();

    private ResourceModelService resourceModelService = new ResourceModelService();
    /**
     * 知识库类型service
     */
    private CnsKinfoCategoryService kinfoCategoryService = new CnsKinfoCategoryService();

    /**
     * 知识信息表实体对象
     */
    private CnsKinfo cnsKinfo;

    /**
     * 表格控件model
     */
    private DataGridModel<CnsKinfo> model;

    /**
     * 知识库类型树
     */
    private LazyTreeModal9 kinfoCategoryTree;

    /**
     * 导出模型
     */
    private ExportModel exportModel;

    /**
     * 类别标识
     */
    private String categoryguid;

    /**
     * 审核状态
     */
    String verifystatus;

    @Override
    public void pageLoad() {
        if (cnsKinfo == null) {
            cnsKinfo = new CnsKinfo();
        }
    }

    /**
     * 删除选定
     * 
     */
    public void deleteSelect() {
        List<String> select = getDataGridData().getSelectKeys();
        for (String sel : select) {
            cnsKinfoService.deleteRecord(sel);
        }
        addCallbackParam("msg", "成功删除！");
    }

    /**
     * 
     *  [获取所有的知识列表（条件：启用的审核过的所有部门的；搜索条件：知识标题；可查看）]
     */
    public DataGridModel<CnsKinfo> getDataGridData() {
        // 获得表格对象
        Map<String, String> conditionMap = new HashMap<>();
        if (model == null) {
            model = new DataGridModel<CnsKinfo>()
            {

                @Override
                public List<CnsKinfo> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    String sql = "select * from cns_kinfo where 1=1 ";
                    if (StringUtil.isNotBlank(cnsKinfo.getKname())) {
                        conditionMap.put("knamelike", cnsKinfo.getKname());
                        sql +=" and kname like '%"+cnsKinfo.getKname()+"%' ";
                    }
//                  
                    sql += " and ISENABLE='1' and ISDEL='0' and KSTATUS='30' and  begindate<=now()";
                    sql += " and Effectdate>= now()";
                    if (StringUtil.isNotBlank(categoryguid)) {
                        //获取该类别下面所有类别，作为条件
                        List<CnsKinfoCategory> childCategory = kinfoCategoryService.getChildCategoryList(categoryguid);
                        if (childCategory != null && childCategory.size() > 0) {
                            String inStr = " ";
                            for (CnsKinfoCategory cnsKinfoCategory : childCategory) {
                                inStr += "'" + cnsKinfoCategory.getRowguid() + "',";
                            }
                            inStr = inStr.substring(0, inStr.length() - 1);
                            conditionMap.put("CATEGORYGUIDIN", inStr);
                            sql+=" and CATEGORYGUID IN ("+inStr + ")";
                        }
                    }
                    List<CnsKinfo> list1 = cnsKinfoService.getReadByCondition(conditionMap, first, pageSize);
                    List<CnsKinfo> list = CommonDao.getInstance().findList(sql, CnsKinfo.class, null);
                    int count = list.size();
                    sql += " limit " + first + "," + pageSize + ";";
                    list = CommonDao.getInstance().findList(sql, CnsKinfo.class, null);
                    if (list != null && list.size() > 0) {
                        for (CnsKinfo cnsk : list) {
                            String begindate = EpointDateUtil.convertDate2String(cnsk.getBegindate(),
                                    EpointDateUtil.DATE_FORMAT);
                            String effectdate = EpointDateUtil.convertDate2String(cnsk.getEffectdate(),
                                    EpointDateUtil.DATE_FORMAT);
                            cnsk.set("date", begindate + " ~ " + effectdate);
                            cnsk.set("iscollect", this.findCollect(cnsk));
                        }
                    }
                    
                    this.setRowCount(count);
                    return list;
                }

                private boolean findCollect(CnsKinfo cnsk) {
                    CnsKinfoCollect cKc = kinfoCollectService.findRecordByKguidAndUserGuid(cnsk.getRowguid(),
                            userSession.getUserGuid());
                    if (cKc != null) {
                        return true;
                    }
                    return false;
                }

            };
        }
        return model;
    }

    /**
     * 
     *  [停用]
     */
    public void onstart(String rowguid) {
        CnsKinfo kinfo = new CnsKinfo();
        kinfo = cnsKinfoService.getBeanByGuid(rowguid);
        service.update(kinfo);
    }

    /**
     *  [启用]
     */
    public void onstop(String rowguid) {
        CnsKinfo kinfo = new CnsKinfo();
        kinfo = cnsKinfoService.getBeanByGuid(rowguid);
        kinfo.setIsenable("1");
        service.update(kinfo);
    }

    public LazyTreeModal9 getCategoryModal() {
        return resourceModelService.getCategoryAllModel();
    }

    public List<SelectItem> getKstatusModel() {
        return commonModelService.getKstatusModel();
    }

    public CnsKinfo getCnsKinfo() {
        return cnsKinfo;
    }

    public void setCnsKinfo(CnsKinfo cnsKinfo) {
        this.cnsKinfo = cnsKinfo;
    }

    public ExportModel getExportModel() {
        if (exportModel == null) {
            exportModel = new ExportModel("kname,publishperson,ouname,begindate,effectdate,creatdate,isenable",
                    "知识名称,发布人,发布部门,生效时间,失效时间,发布时间,是否启用");
        }
        return exportModel;
    }

    public String getCategoryguid() {
        return categoryguid;
    }

    public void setCategoryguid(String categoryguid) {
        this.categoryguid = categoryguid;
    }

    public void collect() {
        List<String> keys = this.getDataGridData().getSelectKeys();
        if (keys != null && keys.size() > 0) {
            for (String key : keys) {
                CnsKinfoCollect cnsKinfoCollect = new CnsKinfoCollect();
                cnsKinfoCollect.setRowguid(UUID.randomUUID().toString());
                cnsKinfoCollect.setOperatedate(new Date());
                cnsKinfoCollect.setKguid(key);
                cnsKinfoCollect.setUserguid(userSession.getUserGuid());
                kinfoCollectService.addRecord(cnsKinfoCollect);
            }

        }
    }

    public void canlCollect() {
        List<String> keys = this.getDataGridData().getSelectKeys();
        if (keys != null && keys.size() > 0) {
            for (String key : keys) {
                kinfoCollectService.deleteRecordByKguidAndUserguid(key, userSession.getUserGuid());
            }
        }
    }
}
