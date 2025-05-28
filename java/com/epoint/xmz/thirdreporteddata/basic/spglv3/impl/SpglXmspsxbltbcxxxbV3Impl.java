package com.epoint.xmz.thirdreporteddata.basic.spglv3.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.common.service.AuditCommonResult;
import com.epoint.common.util.SQLManageUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.domain.SpglXmspsxbltbcxxxbV3;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.inter.ISpglXmspsxbltbcxxxbV3;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.service.SpglXmspsxbltbcxxxbV3Service;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 住建部_项目审批事项办理信息表对应的后台service实现类
 *
 * @author zhpengsy
 * @version [版本号, 2018-11-16 15:08:57]
 */
@Component
@Service
public class SpglXmspsxbltbcxxxbV3Impl implements ISpglXmspsxbltbcxxxbV3 {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Override
    public int insert(SpglXmspsxbltbcxxxbV3 record) {
        SpglXmspsxbltbcxxxbV3Service spglxmspsxbltbcxxxbservice = new SpglXmspsxbltbcxxxbV3Service();
        return spglxmspsxbltbcxxxbservice.insert(record);
    }

    @Override
    public int deleteByGuid(String guid) {
        SpglXmspsxbltbcxxxbV3Service spglxmspsxbltbcxxxbservice = new SpglXmspsxbltbcxxxbV3Service();
        return spglxmspsxbltbcxxxbservice.deleteByGuid(guid);
    }

    @Override
    public int update(SpglXmspsxbltbcxxxbV3 record) {
        SpglXmspsxbltbcxxxbV3Service spglxmspsxbltbcxxxbservice = new SpglXmspsxbltbcxxxbV3Service();
        return spglxmspsxbltbcxxxbservice.update(record);
    }

    @Override
    public SpglXmspsxbltbcxxxbV3 find(Object primaryKey) {
        SpglXmspsxbltbcxxxbV3Service spglxmspsxbltbcxxxbservice = new SpglXmspsxbltbcxxxbV3Service();
        return spglxmspsxbltbcxxxbservice.find(primaryKey);
    }

    @Override
    public SpglXmspsxbltbcxxxbV3 find(String sql, Object... args) {
        SpglXmspsxbltbcxxxbV3Service spglxmspsxbltbcxxxbservice = new SpglXmspsxbltbcxxxbV3Service();
        return spglxmspsxbltbcxxxbservice.find(sql, args);
    }

    @Override
    public List<SpglXmspsxbltbcxxxbV3> findList(String sql, Object... args) {
        SpglXmspsxbltbcxxxbV3Service spglxmspsxbltbcxxxbservice = new SpglXmspsxbltbcxxxbV3Service();
        return spglxmspsxbltbcxxxbservice.findList(sql, args);
    }

    @Override
    public List<SpglXmspsxbltbcxxxbV3> findList(String sql, int pageNumber, int pageSize, Object... args) {
        SpglXmspsxbltbcxxxbV3Service spglxmspsxbltbcxxxbservice = new SpglXmspsxbltbcxxxbV3Service();
        return spglxmspsxbltbcxxxbservice.findList(sql, pageNumber, pageSize, args);
    }

    @Override
    public AuditCommonResult<List<SpglXmspsxbltbcxxxbV3>> getListByCondition(Map<String, String> conditionMap) {
        AuditCommonResult<List<SpglXmspsxbltbcxxxbV3>> result = new AuditCommonResult<>();
        SpglXmspsxbltbcxxxbV3Service spglxmspsxbltbcxxxbservice = new SpglXmspsxbltbcxxxbV3Service();
        result.setResult(spglxmspsxbltbcxxxbservice.getAllRecord(SpglXmspsxbltbcxxxbV3.class, conditionMap));
        return result;
    }

    @Override
    public AuditCommonResult<Integer> getCountByCondition(Map<String, String> conditionMap) {
        SQLManageUtil sqlm = new SQLManageUtil();
        AuditCommonResult<Integer> result = new AuditCommonResult<>();
        Integer count = sqlm.getListCount(SpglXmspsxbltbcxxxbV3.class, conditionMap);
        result.setResult(count);
        return result;
    }

    @Override
    public AuditCommonResult<PageData<SpglXmspsxbltbcxxxbV3>> getAllByPage(Map<String, String> conditionMap,
                                                                           Integer first, Integer pageSize, String sortField, String sortOrder) {
        SpglXmspsxbltbcxxxbV3Service service = new SpglXmspsxbltbcxxxbV3Service();
        AuditCommonResult<PageData<SpglXmspsxbltbcxxxbV3>> result = new AuditCommonResult<PageData<SpglXmspsxbltbcxxxbV3>>();
        PageData<SpglXmspsxbltbcxxxbV3> pageData = new PageData<>();
        try {
            pageData = service.getAllRecordByPage(SpglXmspsxbltbcxxxbV3.class, conditionMap, first, pageSize, sortField,
                    sortOrder);
            result.setResult(pageData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
