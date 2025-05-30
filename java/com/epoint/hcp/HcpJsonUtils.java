package com.epoint.hcp;

import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.ValueFilter;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.json.JsonUtil;
import com.epoint.core.utils.string.StringUtil;

public class HcpJsonUtils
{

    /**
     * 
     * 生成json字符号,政务大厅接口返回的方法
     * 形如：{"result":{"data":{"evaList":[{"a":"1"}]},"pageTotal":0},"code":"000","text":"xx","success":""}
     * 
     * @param code
     * @param message
     * @param result
     * @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public static String hcpRestReturn(String code, String message, Boolean isSuccess, String result) {
        JSONObject json = new JSONObject();
        JSONObject resultJson = new JSONObject();
        try {
            if (StringUtil.isNotBlank(result)) {
                resultJson = JSONObject.parseObject(result);
            }
            json.put("custom", resultJson);
            json.put("code", code);
            json.put("message", message);
            json.put("success", isSuccess);
        }
        catch (Exception e) {
            json.put("custom", result);
            json.put("success", false);
        }
        return json.toString();
    }

    /**
     * 
     * 生成json字符号,政务大厅接口返回的方法(null转成空)
     * 形如：{"result":{"data":{"evaList":[{"a":"1"}]},"pageTotal":0},"code":"000","text":"xx","success":""}
     * 
     * @param code
     * @param message
     * @param custom
     * @return
     */
    public static String hcpRestReturn(String code, String message, Boolean isSuccess, JSONObject dataJson) {
        String result = JSON.toJSONString(dataJson, filter, SerializerFeature.WriteDateUseDateFormat);
        JSONObject json = new JSONObject();
        JSONObject resultJson = new JSONObject();
        try {
            if (StringUtil.isNotBlank(result)) {
                resultJson = JSONObject.parseObject(result);
            }
            json.put("custom", resultJson);
            json.put("code", code);
            json.put("message", message);
            json.put("success", isSuccess);
        }
        catch (Exception e) {
            json.put("custom", result);
            json.put("success", false);
        }
        return json.toString();
    }

    // 过滤器
    public static ValueFilter filter = new ValueFilter()
    {
        @Override
        public Object process(Object obj, String s, Object v) {
            if (v == null) {
                return "";
            }
            return v;
        }
    };

    public static List<Record> getResultDataFieldList(String resultStr, String field) {
        JSONObject resultJson = JSON.parseObject(resultStr);
        JSONObject result = (JSONObject) resultJson.get("result");
        if (StringUtil.isBlank(result.getString(field))) {
            return null;
        }
        else {
            return JsonUtil.jsonToList(result.getString(field), Record.class);
        }
    }

    public static Integer getResultDataPageTotal(String resultStr) {
        JSONObject resultJson = JSON.parseObject(resultStr);
        JSONObject result = (JSONObject) resultJson.get("result");
        if (result.getInteger("pageTotal") == null) {
            return 0;
        }
        else {
            return result.getInteger("pageTotal");
        }
    }

    public static Record getResultDataFieldRecord(String resultStr, String field) {
        JSONObject resultJson = JSON.parseObject(resultStr);
        JSONObject result = (JSONObject) resultJson.get("result");
        if (StringUtil.isBlank(result.getString(field))) {
            return null;
        }
        else {
            return JsonUtil.jsonToObject(result.getString(field), Record.class);
        }
    }

    public static String getResultDataFieldString(String resultStr, String field) {
        JSONObject resultJson = JSON.parseObject(resultStr);
        JSONObject result = (JSONObject) resultJson.get("result");
        return result.getString(field);
    }

    public static String getParamsByField(String field, String value) {
        JSONObject params = new JSONObject();
        params.put(field, value);
        return params.toJSONString();
    }

    public static <T> T getResultDataFieldList(String resultStr, String field, Class<T> pojoClass) {
        JSONObject resultJson = JSON.parseObject(resultStr);
        JSONObject result = (JSONObject) resultJson.get("result");
        return JsonUtil.jsonToObject(result.getString(field), pojoClass);
    }

    public static String getPageListParamsBySqlUtil(SqlConditionUtil sUtil, Integer first, Integer pageSize,
            String sortField, String sortOrder) {
        JSONObject params = new JSONObject();
        params.put("conditionMap", sUtil.getMap());
        params.put("first", first);
        params.put("pageSize", pageSize);
        params.put("sortField", sortField);
        params.put("sortOrder", sortOrder);
        return params.toJSONString();
    }

    public static String getNoPageListParamsBySqlUtil(SqlConditionUtil sUtil) {
        JSONObject params = new JSONObject();
        params.put("conditionMap", sUtil.getMap());
        return params.toJSONString();
    }

}
