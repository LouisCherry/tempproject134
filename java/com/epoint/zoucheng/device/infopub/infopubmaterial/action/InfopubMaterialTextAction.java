package com.epoint.zoucheng.device.infopub.infopubmaterial.action;

import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.controller.BaseController;
import com.epoint.zoucheng.device.infopub.programstatistic.api.IProgramstatisticService;
import com.epoint.zoucheng.device.infopub.programstatistic.api.entity.Programstatistic;

/**
 * 素材表list页面对应的后台
 * 
 * @author 15394
 * @version [版本号, 2017-08-14 16:30:56]
 */
@RestController("infopubmaterialtextaction")
@Scope("request")
public class InfopubMaterialTextAction extends BaseController
{
    private static final long serialVersionUID = 7837782811985651745L;
    @Autowired
    private IProgramstatisticService pStatisticService;

    @Override
    public void pageLoad() {
    }

    /**
     * 在确认添加时，将添加的素材类型存入节目统计库
     */
    public void saveAdd() {
        Programstatistic pStatistic = new Programstatistic();
        pStatistic.setRowguid(UUID.randomUUID().toString());
        pStatistic.setProgramform("3");//代码项：文本
        pStatistic.setDatetime(new Date());
        pStatisticService.insert(pStatistic);
    }
}
