package com.epoint.apimanage.utils.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.apimanage.log.entity.ApiManageLog;
import com.epoint.apimanage.utils.api.ICommonDaoService;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 镇街经纬度对应的后台service实现类
 *
 * @author Administrator
 * @version [版本号, 2019-04-24 15:18:22]
 */
@Component
@Service
public class CommonDaoServiceImpl implements ICommonDaoService {

    @Override
    public List<ApiManageLog> findLogList() {
        return new CommonDaoService().findLogList();
    }

    @Override
    public int update(ApiManageLog apiManageLog) {
        return new CommonDaoService().update(apiManageLog);
    }
}
