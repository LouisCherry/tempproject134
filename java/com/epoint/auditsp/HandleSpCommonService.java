package com.epoint.auditsp;

import com.epoint.basic.auditsp.auditspphase.domain.AuditSpPhase;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.frame.service.metadata.code.entity.CodeItems;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class HandleSpCommonService {

    public List<String> getChooseOuguid(AuditSpPhase auditSpPhase) {
        ICodeItemsService iCodeItemsService = ContainerFactory.getContainInfo().getComponent(ICodeItemsService.class);
        if (auditSpPhase != null) {
            List<CodeItems> items = iCodeItemsService.listCodeItemsByCodeName(auditSpPhase.getRowguid() + "_部门");
            if (items != null && !items.isEmpty()) {
                return items.stream().map(CodeItems::getItemValue).collect(Collectors.toList());
            } else {
                return new ArrayList<>();
            }
        } else {
            return new ArrayList<>();
        }
    }

    public List<String> getChooseTaskguid(AuditSpPhase auditSpPhase) {
        ICodeItemsService iCodeItemsService = ContainerFactory.getContainInfo().getComponent(ICodeItemsService.class);
        if (auditSpPhase != null) {
            List<CodeItems> items = iCodeItemsService.listCodeItemsByCodeName(auditSpPhase.getRowguid() + "_事项");
            if (items != null && !items.isEmpty()) {
                return items.stream().map(CodeItems::getItemValue).collect(Collectors.toList());
            } else {
                return new ArrayList<>();
            }
        } else {
            return new ArrayList<>();
        }
    }

}
