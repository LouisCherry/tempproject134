package com.epoint.jnrestforyanglao;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.xm.similarity.util.StringUtil;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.epoint.common.util.JsonUtils;
import com.epoint.core.grammar.Record;

@RestController
@RequestMapping("/jnyanglao")
public class JnYangLaoRestController
{
    /**
     * 日志
     */
    private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());

    @Autowired
    private IJnYangLaoService ylService;

    /**
     * 
     *  [接口]获取养老机构列表
     *  @param params
     * @return String[养老列表，养老列表总条数]
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/getyanglao", method = RequestMethod.POST)
    public String getYangLao(@RequestBody String params) {
//        JSONObject rtnjson = new JSONObject();
        try {
            JSONObject e = JSON.parseObject(params);
            JSONObject obj = (JSONObject) e.get("params");
            String cname = obj.getString("cname");
            int pageSize = Integer.parseInt(obj.getString("pagesize"));
            int pageIndex = Integer.parseInt(obj.getString("pageindex"));
            List<Record> ylList = new ArrayList<Record>();
            int total=0;
            //判断是否通过机构名称查询
            if (StringUtil.isNotBlank(cname)) {
                ylList = ylService.getYangLaoListByName(cname, pageIndex, pageSize);
                total = ylService.getCountByCName(cname);
            }
            else {
                ylList = ylService.getYangLaoList(pageIndex, pageSize);
                total = ylService.getCount();
            }
            JSONObject dataJson = new JSONObject();
            dataJson.put("data", ylList);
            dataJson.put("total", total);
            return JsonUtils.zwdtRestReturn("1", "接口调用成功", dataJson);

        }
        catch (Exception e) {
            log.info("【济宁养老机构信息结束调用getYangLao异常】" + params);
            e.printStackTrace();
            return JsonUtils.zwdtRestReturn("0", "接口调用报错", e.getMessage());
        }
    }
    
    /**
     * 
     *  [接口]获取养老机构详情
     *  @param params
     * @return String[养老列表，养老列表总条数]
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/getdetail", method = RequestMethod.POST)
    public String getDetail(@RequestBody String params) {
//        JSONObject rtnjson = new JSONObject();
        try {
            JSONObject e = JSON.parseObject(params);
            JSONObject obj = (JSONObject) e.get("params");
            String pk_old_agency = obj.getString("pk_old_agency");
            Record record=ylService.findByPk(pk_old_agency);
            JSONObject dataJson = new JSONObject();
            dataJson.put("data", record);
            return JsonUtils.zwdtRestReturn("1", "接口调用成功", dataJson);

        }
        catch (Exception e) {
            log.info("【济宁养老机构信息结束调用getYangLao异常】" + params);
            e.printStackTrace();
            return JsonUtils.zwdtRestReturn("0", "接口调用报错", e.getMessage());
        }
    }
}
