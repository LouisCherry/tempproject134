package com.epoint.jnzwfw.jndtanddw.impl;

import com.epoint.core.grammar.Record;
import com.epoint.jnzwfw.jndtanddw.api.IJnDtAndDwService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author hzchen
 * @version 1.0
 * @date 2024/11/4 10:41
 */
@Service
public class JnDtAndDwServiceImpl implements IJnDtAndDwService {


    @Override
    public List<Record> getDtInfoByItemGuid(String itemGuid) {
        return new JnDtAndDwService().getDtInfoByItemGuid(itemGuid);
    }
}
