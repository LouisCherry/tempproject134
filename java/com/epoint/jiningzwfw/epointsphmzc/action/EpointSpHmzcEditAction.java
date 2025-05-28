package com.epoint.jiningzwfw.epointsphmzc.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.jiningzwfw.epointsphmzc.api.entity.EpointSpHmzc;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.fileupload.FileUploadModel9;
import com.epoint.basic.faces.tree.DefaultFileUploadHandlerImpl9;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.frame.service.organ.ou.api.IOuService;
import com.epoint.basic.controller.RightRelation;
import com.epoint.jiningzwfw.epointsphmzc.api.IEpointSpHmzcService;
import org.springframework.beans.factory.annotation.Autowired;

/**import com.epoint.basic.faces.util.DataUtil;
 * 惠企政策库修改页面对应的后台
 * 
 * @author 86180
 * @version [版本号, 2019-10-08 23:39:45]
 */
@RightRelation(EpointSpHmzcListAction.class)
@RestController("epointsphmzceditaction")
@Scope("request")
public class EpointSpHmzcEditAction extends BaseController
{

    @Autowired
    private IEpointSpHmzcService service;
    
    @Autowired
    private IOuService ouservice;

    /**
     * 惠企政策库实体对象
     */
    private EpointSpHmzc dataBean = null;

    /**
    * 行业规模下拉列表model
    */
    private List<SelectItem> jnhygmModel = null;
    /**
     * 企业标签复选框组model
     */
    private List<SelectItem> qybqModel = null;
    /**
     * 是否四新企业单选按钮组model
     */
    private List<SelectItem> sfsxqyModel = null;
    /**
     * 所属部门下拉列表model
     */
    private List<SelectItem> ssbmModel = null;
    /**
     * 生命周期下拉列表model
     */
    private List<SelectItem> wwsmzqModel = null;
    /**
     * 层级下拉列表model
     */
    private List<SelectItem> cengjiModel = null;
    
    private FileUploadModel9 fileUploadModel;
    
    private FileUploadModel9 fileUploadModel1;
    
    private String cliengguid = "";
    
    private String ssxzcliengguid = "";

    public void pageLoad() {
        String guid = getRequestParameter("guid");
        dataBean = service.find(guid);
        cliengguid = dataBean.getZczn();
        ssxzcliengguid = dataBean.getSsxz();
        if (StringUtil.isNotBlank(dataBean.getSsbm())) {
            String ouname = ouservice.getOuByOuGuid(dataBean.getSsbm()).getOuname();
            addCallbackParam("ouname", ouname);
        }
        if (dataBean == null) {
            dataBean = new EpointSpHmzc();
        }else {
            if (StringUtil.isBlank(cliengguid)) {
                cliengguid =  UUID.randomUUID().toString();
                dataBean.setZczn(cliengguid);
            }
            
            if (StringUtil.isBlank(ssxzcliengguid)) {
                ssxzcliengguid =  UUID.randomUUID().toString();
                dataBean.setSsxz(ssxzcliengguid);
            }
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

    public EpointSpHmzc getDataBean() {
        return dataBean;
    }

    public void setDataBean(EpointSpHmzc dataBean) {
        this.dataBean = dataBean;
    }

    
    public FileUploadModel9 getFileUploadModel() {
        if (fileUploadModel == null) {
            // DefaultFileUploadHandlerImpl9具体详情可以去查基础api
            // DefaultFileUploadHandlerImpl9参数为：clientGuid，clientTag，clientInfo，AttachHandler9，attachHandler，userGuid，userName

            // clientGuid一般是地址中获取到的，此处只做参考使用
            //system.out.println("cliengguid："+cliengguid);
            fileUploadModel = new FileUploadModel9(
                    new DefaultFileUploadHandlerImpl9(cliengguid, null, null, null, userSession.getUserGuid(), userSession.getDisplayName()));
        }
        return fileUploadModel;
    }
    
    public FileUploadModel9 getFileUploadModel1() {
        if (fileUploadModel1 == null) {
            // DefaultFileUploadHandlerImpl9具体详情可以去查基础api
            // DefaultFileUploadHandlerImpl9参数为：clientGuid，clientTag，clientInfo，AttachHandler9，attachHandler，userGuid，userName

            // clientGuid一般是地址中获取到的，此处只做参考使用
            
            fileUploadModel1 = new FileUploadModel9(
                    new DefaultFileUploadHandlerImpl9(ssxzcliengguid, null, null, null, userSession.getUserGuid(), userSession.getDisplayName()));
        }
        return fileUploadModel1;
    }
    
    public List<SelectItem> getCengjiModel() {
        if (cengjiModel == null) {
            cengjiModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "层级", null, false));
        }
        return this.cengjiModel;
    }
    
    public List<SelectItem> getJnhygmModel() {
        if (jnhygmModel == null) {
            jnhygmModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "行业规模", null, false));
        }
        return this.jnhygmModel;
    }

    public List<SelectItem> getQybqModel() {
        if (qybqModel == null) {
            qybqModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("复选框组", "行业分类", null, false));
        }
        return this.qybqModel;
    }

    public List<SelectItem> getSfsxqyModel() {
        if (sfsxqyModel == null) {
            sfsxqyModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("单选按钮组", "是否四新企业", null, false));
        }
        return this.sfsxqyModel;
    }

    public List<SelectItem> getSsbmModel() {
        if (ssbmModel == null) {
            ssbmModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "null", null, false));
        }
        return this.ssbmModel;
    }

    public List<SelectItem> getWwsmzqModel() {
        if (wwsmzqModel == null) {
            wwsmzqModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "生命周期", null, false));
        }
        return this.wwsmzqModel;
    }

    public String getCliengguid() {
        return cliengguid;
    }

    public void setCliengguid(String cliengguid) {
        this.cliengguid = cliengguid;
    }

    public String getSsxzcliengguid() {
        return ssxzcliengguid;
    }

    public void setSsxzcliengguid(String ssxzcliengguid) {
        this.ssxzcliengguid = ssxzcliengguid;
    }

    public List<SelectItem> getouList() {
        String areacode = ZwfwUserSession.getInstance().getAreaCode();
            List<Record> list = service.getOuList(areacode);
            List<SelectItem> result = new ArrayList<SelectItem>();
            if (list != null && list.size() > 0) {
                for (Record record : list) {
                    result.add(new SelectItem(record.getStr("ouguid"), record.getStr("ouname")));
                }
            }
            return result;
    }
    
    
}
