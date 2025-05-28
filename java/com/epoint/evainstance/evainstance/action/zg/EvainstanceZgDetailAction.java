package com.epoint.evainstance.evainstance.action.zg;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.authentication.UserSession;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.evainstance.IEvainstanceService;
import com.epoint.evainstance.entity.Evainstance;
import com.epoint.frame.service.message.api.IMessagesCenterService;
import com.epoint.frame.service.message.entity.MessagesCenter;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.frame.service.organ.user.api.IUserService;

/**
 * 评价表详情页面对应的后台
 * 
 * @author lizhenjie
 * @version [版本号, 2020-09-06 14:00:35]
 */
@RightRelation(EvainstanceZgListAction.class)
@RestController("evainstancezgdetailaction")
@Scope("request")
public class EvainstanceZgDetailAction extends BaseController
{
    @Autowired
    private IEvainstanceService service;
    @Autowired
    private IMessagesCenterService messageCenterService;
    @Autowired
    private IUserService userService;
    /**
      * 所属辖区下拉列表model
      */
    private List<SelectItem> areacodeModel = null;
    /**
     * 处理等级下拉列表model
     */
    private List<SelectItem> cldjModel = null;
    /**
     * 回访调查单选按钮组model
     */
    private List<SelectItem> hfdcModel = null;
    /**
     * 评价渠道下拉列表model
     */
    private List<SelectItem> pfModel = null;
    /**
     * 满意度下拉列表model
     */
    private List<SelectItem> satisfactionModel = null;
    /**
     * 是否恶意评价单选按钮组model
     */
    private List<SelectItem> sfeypjModel = null;
    /**
     * 是否回复单选按钮组model
     */
    private List<SelectItem> sfhfModel = null;
    /**
     * 是否回访单选按钮组model
     */
    private List<SelectItem> sfhfangModel = null;
    /**
     * 评价表实体对象
     */
    private Evainstance dataBean = null;

    public void pageLoad() {
        String guid = getRequestParameter("guid");
        dataBean = service.find(guid);
        if (dataBean == null) {
            dataBean = new Evainstance();
        }
        String MessageItemGuid = getRequestParameter("MessageItemGuid");
        if(StringUtil.isNotBlank(MessageItemGuid)) {
            addCallbackParam("msg", true);
        }
        if(StringUtil.isBlank(dataBean.getWritingevaluation())) {
            // dataBean.setWritingevaluation("无");
             addCallbackParam("writingevaluation", "无");
         }else {
             addCallbackParam("writingevaluation", dataBean.getWritingevaluation());
         }
        
         if(StringUtil.isBlank(dataBean.getEvaldetail())) {
             addCallbackParam("evaldetail", "无");
            // dataBean.setEvaldetail("无");
         }else {
             String zhibiao = dataBean.get("evalDetail");
             JSONArray js = new JSONArray();
             String[] arr = zhibiao.split(",");
             for (int i = 0; i < arr.length; i++) {
                 Record zb = service.getZhibiao(arr[i]);
                 JSONObject jzb = new JSONObject();
                 if(StringUtil.isBlank(zb)) {
                     jzb.put("itemtext", "无");
                 }else {
                     jzb.put("itemtext", zb.get("itemtext"));
                 }
                 js.add(jzb.get("itemtext"));
             }
             addCallbackParam("evaldetail", js.toString());
         }
        if(dataBean.getZgjg()!=null &&dataBean.getHfjg()!=null) {
            addCallbackParam("type",true);
        }
        if(dataBean.getNbyj()!=null && dataBean.getNbyj()!="") {
            addCallbackParam("wnb",true);
        }
        
        if(dataBean.getLdps()!=null) {
            addCallbackParam("yps",true);
        }
        if(dataBean.get("zbyj")==null) {
            addCallbackParam("ynb",true);
        }
        
        
    }
    public void zhidaole() {
        String MessageItemGuid = getRequestParameter("MessageItemGuid");
        if(StringUtil.isNotBlank(MessageItemGuid)) {
            messageCenterService.deleteMessage(MessageItemGuid, userSession.getUserGuid());
        }
        addCallbackParam("msg", "提交成功！");
    }
    public void add() {
        //整改结果
        String MessageItemGuid = getRequestParameter("MessageItemGuid");
        String fromguid = getRequestParameter("fromguid");
        MessagesCenter ms = messageCenterService.getDetail(MessageItemGuid, userSession.getUserGuid());
        if ("zg".equals(dataBean.get("type"))) {
            if(ms!=null) {
                messageCenterService.insertWaitHandleMessage(UUID.randomUUID().toString(),
                        "【差评整改回复】关于" + dataBean.getTaskname() + "的好差评" + "(" + userSession.getDisplayName() + ")",
                        IMessagesCenterService.MESSAGETYPE_WAIT, ms.getFromUser(), ms.getFromDispName(),
                        userSession.getUserGuid(), userSession.getDisplayName(),
                        "【差评整改回复】关于" + dataBean.getTaskname() + "的好差评" + "(" + userSession.getDisplayName() + ")",
                        "jiningzwfw/evainstance/evainstance/evainstancedetailzg?guid=" + dataBean.getRowguid(),
                        userSession.getOuGuid(), userSession.getBaseOUGuid(), ZwfwConstant.CONSTANT_INT_ONE, "", "", "", "",
                        new Date(), "", userSession.getUserGuid(), "", "");
            }
            MessageItemGuid = getRequestParameter("MessageItemGuid");
            if(StringUtil.isNotBlank(MessageItemGuid)) {
                messageCenterService.deleteMessage(MessageItemGuid, userSession.getUserGuid());
            }
            dataBean.set("zgryguid", userSession.getUserGuid());
            dataBean.set("zgryname", userSession.getDisplayName());
            dataBean.setSfhf("1");
            service.update(dataBean);
            addCallbackParam("msg", "提交成功！");
        }
        dataBean = null;
    }

    public Evainstance getDataBean() {
        return dataBean;
    }

    public List<SelectItem> getAreacodeModel() {
        if (areacodeModel == null) {
            areacodeModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "辖区", null, false));
        }
        return this.areacodeModel;
    }

    public List<SelectItem> getCldjModel() {
        if (cldjModel == null) {
            cldjModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "评审类型", null, false));
        }
        return this.cldjModel;
    }

    public List<SelectItem> getHfdcModel() {
        if (hfdcModel == null) {
            hfdcModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("单选按钮组", "回访调查", null, false));
        }
        return this.hfdcModel;
    }

    public List<SelectItem> getPfModel() {
        if (pfModel == null) {
            pfModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "评价渠道", null, false));
        }
        return this.pfModel;
    }

    public List<SelectItem> getSatisfactionModel() {
        if (satisfactionModel == null) {
            satisfactionModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "好差评满意度层级", null, false));
        }
        return this.satisfactionModel;
    }

    public List<SelectItem> getSfeypjModel() {
        if (sfeypjModel == null) {
            sfeypjModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("单选按钮组", "是否恶意评价", null, false));
        }
        return this.sfeypjModel;
    }

    public List<SelectItem> getSfhfModel() {
        if (sfhfModel == null) {
            sfhfModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("单选按钮组", "是否回复", null, false));
        }
        return this.sfhfModel;
    }

    public List<SelectItem> getSfhfangModel() {
        if (sfhfangModel == null) {
            sfhfangModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("单选按钮组", "是否回访", null, false));
        }
        return this.sfhfangModel;
    }

}
