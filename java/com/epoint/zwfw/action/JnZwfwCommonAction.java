package com.epoint.zwfw.action;

import com.epoint.basic.EpointKeyNames9;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("jnzwfwcommonaction")
@Scope("request")
public class JnZwfwCommonAction extends BaseController
{

    /**
     * 
     */
    private static final long serialVersionUID = -8891122366031205337L;

    @Override
    public void pageLoad() {

    }

    public List<SelectItem> geCommonModel(String codename) {
        return DataUtil.convertMap2ComboBox(
                CodeModalFactory.factory(EpointKeyNames9.CHECK_SELECT_GROUP, codename, null, false));
    }

}
