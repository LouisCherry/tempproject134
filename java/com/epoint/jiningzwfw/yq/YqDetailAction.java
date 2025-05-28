package com.epoint.jiningzwfw.yq;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.controller.BaseController;
import com.epoint.core.grammar.Record;
import com.epoint.newshow2.api.Newshow2Service;

/**
 * 惠企政策库list页面对应的后台
 * 
 * @author 86180
 * @version [版本号, 2019-10-08 23:39:46]
 */
@RestController("yqdetailaction")
@Scope("request")
public class YqDetailAction extends BaseController
{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    @Autowired
    private Newshow2Service newshow2Service;

    public void pageLoad() {
        String rowguid = getRequestParameter("guid");
        
        Record record = newshow2Service.getYqByRowguid(rowguid);
        //system.out.println("yqrecord:"+record);
    }

}
