package com.epoint.banjiantongjibaobiao.service;

import com.epoint.core.BaseEntity;
import com.epoint.banjiantongjibaobiao.api.ITjfxTaskProjectService;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Component
@Service
public class ITjfxTaskProjectServiceImpl implements ITjfxTaskProjectService {


    @Override
    public List<String> getAllOuList(String ouGuid, boolean isParentNode) {
        return new TjfxTaskProjectService().getAllOuList(ouGuid, isParentNode);
    }

    @Override
    public List<? extends BaseEntity> findTaskList(String sql, List<Object> conditionParams, int first, int pageSize) {
        return new TjfxTaskProjectService().findTaskList(sql, conditionParams, first, pageSize);
    }

    @Override
    public int getTaskListCount(String sql, List<Object> conditionParams) {
        return new TjfxTaskProjectService().getTaskListCount(sql, conditionParams);
    }
}
