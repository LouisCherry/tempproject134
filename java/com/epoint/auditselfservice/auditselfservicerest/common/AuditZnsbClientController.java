package com.epoint.auditselfservice.auditselfservicerest.common;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.auditqueue.auditznsbequipment.inter.IAuditZnsbEquipment;
import com.epoint.composite.auditorga.handleconfig.inter.IHandleConfig;

/**
 * 主程序、插件更新操作接口
 *
 * @author qwt
 */
@RestController
@RequestMapping("/auditznsbclient")
public class AuditZnsbClientController
{
    private final Logger log = LogManager.getLogger(getClass());
    public static final String CLIENT_OPERATRTYPE_ADD = "1";
    public static final String CLIENT_OPERATRTYPE_REMOVE = "2";
    public static final String CLIENT_OPERATRTYPE_EDIT = "3";

    @Autowired
    private IAuditZnsbEquipment equipmentService;

    @Autowired
    private IHandleConfig handleConfigService;

}
