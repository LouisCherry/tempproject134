package com.epoint.xmz.kyinfo.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.auditonlineuser.auditonlinecompany.domain.AuditOnlineCompany;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import com.epoint.basic.faces.fileupload.FileUploadModel9;
import com.epoint.basic.faces.tree.DefaultFileUploadHandlerImpl9;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.StringUtil;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.frame.service.metadata.mis.util.ListGenerator;
import com.epoint.xmz.kyinfo.api.IKyInfoService;
import com.epoint.xmz.kyinfo.api.entity.KyInfo;
import com.epoint.xmz.kyperson.api.IKyPersonService;
import com.epoint.xmz.kyperson.api.entity.KyPerson;
import com.epoint.xmz.kypoint.api.IKyPointService;
import com.epoint.xmz.kypoint.api.entity.KyPoint;

/**
 * 勘验信息表新增页面对应的后台
 * 
 * @author RaoShaoliang
 * @version [版本号, 2023-07-10 16:17:18]
 */
@RightRelation(KyInfoListAction.class)
@RestController("kyofinfo")
@Scope("request")
public class KyOfInfo extends BaseController
{
    @Autowired
    private IKyInfoService service;
    @Autowired
    private IKyPointService pointService;
    @Autowired
    private IKyPersonService personService;
    /**
     * 表格控件model
     */
    private DataGridModel<KyPoint> pointmodel;
    
    /**
     * 表格控件model
     */
    private DataGridModel<KyPerson> personmodel;
    
    /**
     * 勘验信息表实体对象
     */
    private KyInfo dataBean = null;

    /**
     * 勘验城市下拉列表model
     */
    private List<SelectItem> kycityModel = null;

    /**
     * 附件上传
     */
    private FileUploadModel9 fileUploadModel;
    private String guid="";
    public void pageLoad() {
          guid = getRequestParameter("projectguid");
        dataBean = service.findByProjectguid(guid);
        addViewData("clientguid", dataBean.getClientguid());
          if(dataBean==null){
              dataBean=new KyInfo();
          }
    }


    public void deletePoint() {
        List<String> select = pointgetDataGridData().getSelectKeys();
        for (String sel : select) {
             pointService.deleteByGuid(sel);
        }
        addCallbackParam("msg", l("成功删除！"));
    }
    
//    获取要点列表
    public DataGridModel<KyPoint> pointgetDataGridData() {
        // 获得表格对象
        if (pointmodel == null) {
            pointmodel = new DataGridModel<KyPoint>()
            {

                @Override
                public List<KyPoint> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    // 获取where条件Map集合
                    SqlConditionUtil sqlConditionUtil = new SqlConditionUtil();
                    sqlConditionUtil.eq("kyguid",guid);
                    PageData<KyPoint> relationpageData = pointService
                            .paginatorList(sqlConditionUtil.getMap(), first, pageSize);
                    
                    List<KyPoint> relationlist = relationpageData.getList();
                    this.setRowCount(relationpageData.getRowCount());
                    return relationlist;
                }
            };
        }
        return pointmodel;
    }

    //获取人员列表
    public DataGridModel<KyPerson> personGetDataGridData() {
        // 获得表格对象
        if (personmodel == null) {
            personmodel = new DataGridModel<KyPerson>()
            {

                @Override
                public List<KyPerson> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    // 获取where条件Map集合
                    SqlConditionUtil sqlConditionUtil = new SqlConditionUtil();
                    sqlConditionUtil.eq("kyguid", guid);
                    PageData<KyPerson> relationpageData = personService
                            .paginatorList(sqlConditionUtil.getMap(), first, pageSize);
                    
                    List<KyPerson> relationlist = relationpageData.getList();
                    this.setRowCount(relationpageData.getRowCount());
                    addCallbackParam("username", userSession.getDisplayName());
                    return relationlist;
                }
            };
        }
        return personmodel;
    }
    
    

    public KyInfo getDataBean() {
        if (dataBean == null) {
            dataBean = new KyInfo();
        }
        return dataBean;
    }

    public void setDataBean(KyInfo dataBean) {
        this.dataBean = dataBean;
    }

    public List<SelectItem> getKycityModel() {
        if (kycityModel == null) {
            kycityModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "区县市", null, false));
        }
        return this.kycityModel;
    }
    
    public FileUploadModel9 getFileUploadModel() {
        if (fileUploadModel == null) {
            String clientGuid = getViewData("clientguid");
            if (StringUtil.isBlank(clientGuid)) {
                addViewData("clientguid", UUID.randomUUID().toString());
            }
            // DefaultFileUploadHandlerImpl9具体详情可以去查基础api
            // DefaultFileUploadHandlerImpl9参数为：clientGuid，clientTag，clientInfo，AttachHandler9，attachHandler，userGuid，userName
            // clientGuid一般是地址中获取到的，此处只做参考使用
            fileUploadModel = new FileUploadModel9(new DefaultFileUploadHandlerImpl9(getViewData("clientguid"), null,
                    null, null, userSession.getUserGuid(), userSession.getDisplayName()));
        }

        // 该属性设置他为只读，不能被删除
        fileUploadModel.setReadOnly("true");
        return fileUploadModel;
    }

}
