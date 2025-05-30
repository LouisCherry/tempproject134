package com.epoint.listener;

import cn.hutool.core.lang.UUID;
import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.auditonlineuser.auditonlineproject.domain.AuditOnlineProject;
import com.epoint.basic.auditonlineuser.auditonlineproject.inter.IAuditOnlineProject;
import com.epoint.basic.auditonlineuser.auditonlineregister.domain.AuditOnlineRegister;
import com.epoint.basic.auditonlineuser.auditonlineregister.inter.IAuditOnlineRegister;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.audittask.extension.inter.IAuditTaskExtension;
import com.epoint.common.service.AuditCommonResult;
import com.epoint.common.util.ZwdtConstant;
import com.epoint.core.EpointFrameDsManager;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.httpclient.HttpUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.listener.util.JnRestUtil;
import com.epoint.listener.util.SM2;
import com.rabbitmq.client.Channel;
import org.apache.log4j.Logger;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * 办件状态变更消息队列监听器
 */
public class OfficeStatusChangeTest
{
    public static void main(String[] args) {
        String requestParameter="{\"data\":{\"sqrxm\":\"测试jncsy测试\",\"receivePwd\":\"195500f9-6d28-493c-9f27-d20ebfac1915\",\"projectname\":\"劳动用工备案变更\",\"linkPhone\":\"15265700098\",\"orgName\":\"金乡县人力资源和社会保障局\",\"itemCode\":\"11370828553372795B4373014006001\",\"regionName\":\"金乡县\",\"appMark\":\"JNZCSZWFWW\",\"idcardNo\":\"370830198412207211\",\"userName\":\"zwfw65382025\",\"uuid\":\"9346ee71b75a4a9a85c7438acb6c95c8\",\"gotoUrl\":\"http://jizwfw.sd.gov.cn/jnzwdt/epointzwmhwz/pages/myspace/detail?projectguid=0babbd23-87cc-40b2-86cb-ed6ea43304fe&taskguid=16223c40-10a7-4623-bb8d-e3924019db6c&tabtype=5&taskcaseguid=null\",\"receiveTime\":\"2025-01-02 16:34:05\",\"lxrxm\":\"曹盛源\",\"itemId\":\"11370828553372795B437301400600106\",\"itemName\":\"劳动用工备案变更\",\"regionCode\":\"370828000000\",\"creditCode\":\"91370800MA3EN1B53H\",\"identityType\":\"统一社会信用代码\",\"orgCode\":\"acf4cca9-8481-44a9-a12b-29d7245619a2\",\"isMiddlePlatform\":\"0\",\"receiveNum\":\"080816251019052\",\"userType\":\"2\",\"email\":\"\"},\"action\":\"14\",\"isChain\":\"0\"}";
        String publicKey = "044C52572D44DCB8D6B871C3C78043081F1954EDC3F6E7964B4F8B745F64D893C78633A0336369377BBE67915E1B0982E102B003C8C3DC4A69EF8ADE005ECF0F7C";
        String result= SM2.encrypt(publicKey, requestParameter);
        System.out.println(result);
    }

}
