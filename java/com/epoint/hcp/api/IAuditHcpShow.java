package com.epoint.hcp.api;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.epoint.hcp.api.entity.AuditHcpAreainfo;
import com.epoint.hcp.api.entity.AuditHcpOuinfo;
import com.epoint.hcp.api.entity.AuditHcpUserinfo;
import com.epoint.hcp.api.entity.Evainstance;
import com.epoint.core.grammar.Record;
import com.epoint.database.peisistence.crud.impl.model.PageData;

/**
 * 页面展示好差评相关数据
 * @author 马福龙
 *
 */
public interface IAuditHcpShow
{
    
    /**
     *  获取所有辖区
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public List<JSONObject> getArea();
    
    /**
     * 获取部门排名前6的数据
     * @return
     */
    public List<AuditHcpOuinfo> getOuList(Integer num, String areacode);
    
    
    /**
     * 获取全省各个辖区排名数据
     * @return
     */
    public List<AuditHcpAreainfo> getAreaList(Integer num, String areacode);
    
    
    /**
     * 
     * 分页查找一个list
     * 
     * @param conditionMap
     *            条件map
     * @param first
     *            第一条记录
     * @param pageSize
     *            页大小
     * @param sortField
     *            排序字段
     * @param sortOrder
     *            排序顺序
     * @return
     */
    public PageData<AuditHcpUserinfo> getAllByPage(Map<String, String> conditionMap, Integer first,
            Integer pageSize, String sortField, String sortOrder);

    /**
     *  根据辖区编码获取辖区信息
     *  @param areacode
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public AuditHcpAreainfo getAreaByCode(String areacode);

    public List<Record> getUserList(String areacode, int currentPage, int pageSize);

    public int getUserListCount(String areacode);

	int addEvainstance(Evainstance evainstance);

	public List<Record> getEvaluateZb(String areacode,String evatype);
 

}
