package com.epoint.mq.spgl;

import java.util.List;
import java.util.UUID;

import org.mortbay.log.Log;

import com.epoint.auditclient.listener.AuditClientMessageListener;
import com.epoint.auditclient.mqconstant.MQConstant;
import com.epoint.auditmqmessage.domain.AuditMqMessage;
import com.epoint.basic.auditsp.auditspbasetask.domain.AuditSpBasetask;
import com.epoint.basic.auditsp.auditspbasetask.inter.IAuditSpBasetask;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.spgl.domain.SpglDfxmsplcjdsxxxb;
import com.epoint.basic.spgl.inter.ISpglDfxmsplcjdsxxxb;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.organ.ou.api.IOuService;

/**
 * 消费者客户端
 * @author WindowCC
 * @version [版本号, 2018年4月28日]
 */
public class NeedAddNewVersionHandle extends AuditClientMessageListener
{

    //设置消息类型，判断消息是监听住建系统数据
    public NeedAddNewVersionHandle() {
        super.setMassagetype(MQConstant.MESSAGETYPE_ZJ);
    }

    /**
     *  办理环节操作逻辑
     *  @param proMessage 参数
     *  @return    
     * @exception/throws 
     */
    @Override
    public void handleMessage(AuditMqMessage proMessage) throws Exception {
    	 IAuditTask auditTaskBasicImpl = ContainerFactory.getContainInfo().getComponent(IAuditTask.class);
         ISpglDfxmsplcjdsxxxb ispgldfxmsplcjdsxxxb  = ContainerFactory.getContainInfo().getComponent(ISpglDfxmsplcjdsxxxb.class);
         IAuditSpBasetask iauditspbasetask  = ContainerFactory.getContainInfo().getComponent(IAuditSpBasetask.class);
         IOuService iouservice  = ContainerFactory.getContainInfo().getComponent(IOuService.class);
         
        //解析mq消息内容
        String[] messageContent = proMessage.getSendmessage().split("@")[1].split("\\.");

        if (messageContent == null || messageContent.length < 1) {
            //system.out.println("mq消息信息不正确！");
            return;
        }
        //办件主键
        String taskguid = messageContent[0];

        if (proMessage.getSendroutingkey().split("\\.") == null
                || proMessage.getSendroutingkey().split("\\.").length < 2) {
            //system.out.println("mq消息信息不正确！");
            return;
        }
        
        
        AuditTask audittask = auditTaskBasicImpl.getAuditTaskByGuid(taskguid, false).getResult();
        //根据taskid获取在用事项版本
        audittask = auditTaskBasicImpl.selectUsableTaskByTaskID(audittask.getTask_id()).getResult();
        if(audittask != null){
          List<SpglDfxmsplcjdsxxxb> list = ispgldfxmsplcjdsxxxb.getNeedAddNewVersionByItemId(audittask.getItem_id()).getResult();
          Log.info("待同步的spgl事项表数据数量："+list.size());
          for (SpglDfxmsplcjdsxxxb spglDfxmsplcjdsxxxb1 : list) {
              SpglDfxmsplcjdsxxxb spgldfxmsplcjdsxxxb = new SpglDfxmsplcjdsxxxb();
              spgldfxmsplcjdsxxxb.setRowguid(UUID.randomUUID().toString());
              spgldfxmsplcjdsxxxb.setDfsjzj(spglDfxmsplcjdsxxxb1.getDfsjzj());
              spgldfxmsplcjdsxxxb.setXzqhdm(spglDfxmsplcjdsxxxb1.getXzqhdm());
              spgldfxmsplcjdsxxxb.setSplcbm(spglDfxmsplcjdsxxxb1.getSplcbm());
              spgldfxmsplcjdsxxxb.setSplcbbh(spglDfxmsplcjdsxxxb1.getSplcbbh());
              spgldfxmsplcjdsxxxb.setSpjdxh(spglDfxmsplcjdsxxxb1.getSpjdxh());
              spgldfxmsplcjdsxxxb.setSpsxbbh(Double.valueOf(audittask.getVersion()));
              spgldfxmsplcjdsxxxb.setSpsxmc(audittask.getTaskname());
              spgldfxmsplcjdsxxxb.setSpsxbm(audittask.getItem_id());
             
              AuditSpBasetask basetask = iauditspbasetask.getAuditSpBasetaskByrowguid(spglDfxmsplcjdsxxxb1.getDfsjzj()).getResult();
              if (basetask != null) {
                  spgldfxmsplcjdsxxxb.setDybzspsxbm(basetask.getTaskcode());
                  spgldfxmsplcjdsxxxb.setSflcbsx(StringUtil.isNotBlank(basetask.getSflcbsx())
                          ? Integer.valueOf(basetask.getSflcbsx()) : ZwfwConstant.CONSTANT_INT_ZERO); //是否里程碑事项。默认0
              }
              else {
                  spgldfxmsplcjdsxxxb.setSflcbsx(0);
                  spgldfxmsplcjdsxxxb.setDybzspsxbm("9990");
              }
              String istellpromise = audittask.getStr("is_tellpromise");
              if (StringUtil.isBlank(istellpromise)) {
            	  istellpromise = "0";
              }
              spgldfxmsplcjdsxxxb.setSfsxgzcnz(Integer.parseInt(istellpromise));//是否实行告知承诺制 默认0

              spgldfxmsplcjdsxxxb.setBjlx(audittask.getType());
              if (audittask.getApplyertype().indexOf(ZwfwConstant.APPLAYERTYPE_QY) != -1
                      && audittask.getApplyertype().indexOf(ZwfwConstant.APPLAYERTYPE_GR) != -1) {
                  spgldfxmsplcjdsxxxb.setSqdx(3);
              }else{
                  if (audittask.getApplyertype().indexOf(ZwfwConstant.APPLAYERTYPE_GR) != -1) {
                      spgldfxmsplcjdsxxxb.setSqdx(1);
                  }
                  else if (audittask.getApplyertype().indexOf(ZwfwConstant.APPLAYERTYPE_QY) != -1) {
                      spgldfxmsplcjdsxxxb.setSqdx(2);
                  }
              }
              spgldfxmsplcjdsxxxb.setBljgsdfs("2"); //办理送达方式   默认 申请对象窗口领取
              spgldfxmsplcjdsxxxb.setSpsxblsx(audittask.getPromise_day());
              spgldfxmsplcjdsxxxb.setSpbmbm(iouservice.getOuByOuGuid(audittask.getOuguid()).getOucode());
              spgldfxmsplcjdsxxxb.setSpbmmc(audittask.getOuname());
              spgldfxmsplcjdsxxxb.setQzspsxbm(null);
              spgldfxmsplcjdsxxxb.setSjyxbs(ZwfwConstant.CONSTANT_INT_ONE);
              spgldfxmsplcjdsxxxb.setSjsczt(ZwfwConstant.CONSTANT_INT_ZERO);
              ispgldfxmsplcjdsxxxb.insert(spgldfxmsplcjdsxxxb);
          }
        }
    }
}
