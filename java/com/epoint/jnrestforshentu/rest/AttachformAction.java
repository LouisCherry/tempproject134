package com.epoint.jnrestforshentu.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.controller.BaseController;
import com.epoint.core.grammar.Record;
import com.epoint.jnrestforshentu.api.IJnShentuService;

/**
 * 事项登记list页面对应的后台
 * 
 * @author Administrator
 * @version [版本号, 2017年5月22日]
 
 
 */
@RestController("attachformaction")
@Scope("request")
public class AttachformAction extends BaseController
{
    private static final long serialVersionUID = 1L;
    
    @Autowired
    private IJnShentuService service;

    @Override
    public void pageLoad() {
        List<Record> list = service.getThreeFirstByAttachguid("1");
        List<Record> list1 = service.getThreeFirstByAttachguid("2");
        List<Record> list2 = service.getThreeFirstByAttachguid("3");
        List<Record> list3 = service.getThreeFirstByAttachguid("4");
        List<Record> list4 = service.getThreeFirstByAttachguid("5");
        addCallbackParam("list", list);
        addCallbackParam("list1", list1);
        addCallbackParam("list2", list2);
        addCallbackParam("list3", list3);
        addCallbackParam("list4", list4);
        
    }

    /*public DataGridModel<AuditProject> getDataGridData() {
        
    }*/

   
}
