package com.epoint.jnzwfw.jndtanddw.api;

import com.epoint.core.grammar.Record;

import java.util.List;

/**
 * @author hzchen
 * @version 1.0
 * @date 2024/11/4 10:39
 */
public interface IJnDtAndDwService {
    List<Record> getDtInfoByItemGuid(String itemGuid);
}
