package com.epoint.basic.auditsp.dantiinfo.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.auditresource.auditrsitembaseinfo.domain.AuditRsItemBaseinfo;
import com.epoint.basic.auditresource.auditrsitembaseinfo.inter.IAuditRsItemBaseinfo;
import com.epoint.basic.auditsp.dantiinfo.entity.DantiInfo;
import com.epoint.cert.commonutils.HttpUtil;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.frame.service.metadata.systemparameters.api.IConfigService;
import com.esotericsoftware.minlog.Log;

public class JnDantiinfoService
{

    /**
     * 通用dao
     */
    protected ICommonDao commonDao;

    public JnDantiinfoService() {
        commonDao = CommonDao.getInstance();
    }

    public List<DantiInfo> getUnFmDantiInfoList() {
        String sql = "select * from danti_info where isfm = '0' limit 50";
        return commonDao.findList(sql, DantiInfo.class);
    }

    public List<DantiInfo> getErrorDantiInfoListByProjectguid(String projectguid) {
        String sql = "select * from danti_info where isfm = '2' and projectguid=?";
        return commonDao.findList(sql, DantiInfo.class, projectguid);
    }

    public DantiInfo getErrorDantiInfoListByRowguid(String rowguid) {
        String sql = "select * from danti_info where isfm = '2' and rowguid=?";
        return commonDao.find(sql, DantiInfo.class, rowguid);
    }

    public List<DantiInfo> getDantiInfoAttachList() {
        String sql = "select rowguid,dtbm,cadfileguid from danti_info where cadfileguid is not null and isfm = '1' and fjid is null order by operatedate limit 50 ";
        return commonDao.findList(sql, DantiInfo.class);
    }

    public PageData<DantiInfo> pageDantiList(Record record, int first, int pageSize, String sortField,
            String sortOrder) {
        PageData<DantiInfo> pageData = new PageData<>();
        List<String> params = new ArrayList<>();
        String sql = "select a.DANTINAME,a.GCLB,a.dtbm,a.zjurl,b.ITEMCODE,a.rowguid from danti_info a,audit_rs_item_baseinfo b where a.projectguid = b.RowGuid and a.isfm is not null ";
        String sqlcount = "select count(1) from danti_info a,audit_rs_item_baseinfo b where a.projectguid = b.RowGuid and a.isfm is not null";
        if (StringUtil.isNotBlank(record.getStr("dantiname"))) {
            sql += " and a.dantiname like ? ";
            sqlcount += " and a.dantiname like ? ";
            params.add("%" + record.getStr("dantiname") + "%");
        }
        if (StringUtil.isNotBlank(record.getStr("itemcode"))) {
            sql += " and b.itemcode like ? ";
            sqlcount += " and b.itemcode like ? ";
            params.add("%" + record.getStr("itemcode") + "%");
        }
        if (StringUtil.isNotBlank(record.getStr("dtbm"))) {
            sql += " and a.dtbm like ? ";
            sqlcount += " and a.dtbm like ? ";
            params.add("%" + record.getStr("dtbm") + "%");
        }
        if (StringUtil.isNotBlank(record.getStr("gclb"))) {
            sql += " and a.gclb = ? ";
            sqlcount += " and a.gclb = ? ";
            params.add(record.getStr("gclb"));
        }
        if (StringUtil.isNotBlank(record.getStr("isfm"))) {
            if ("1".equals(record.getStr("isfm"))) {
                sql += " and ifnull(a.dtbm,'') != '' ";
                sqlcount += " and ifnull(a.dtbm,'') != '' ";
            }
            else {
                sql += " and ifnull(a.dtbm,'') = '' ";
                sqlcount += " and ifnull(a.dtbm,'') = '' ";
            }
        }

        List<DantiInfo> list = commonDao.findList(sql, first, pageSize, DantiInfo.class, params.toArray());
        int count = commonDao.queryInt(sqlcount, params.toArray());
        pageData.setRowCount(count);
        pageData.setList(list);
        return pageData;
    }

    /***** 工改单体赋码与住建图审系统对接需求登记单开始 *****/
    /**
     * [工改赋码：推送项目信息]
     * 
     * @param itemName
     * @param code
     * @param temporaryCode
     * @param socialCode
     * @param cityCode
     * @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public JSONObject insertInfo(String itemGuid) {
        JSONObject jsonObject = new JSONObject();
        IConfigService iConfigService = ContainerFactory.getContainInfo().getComponent(IConfigService.class);
        IAuditRsItemBaseinfo iAuditRsItemBaseinfo = ContainerFactory.getContainInfo()
                .getComponent(IAuditRsItemBaseinfo.class);
        if (StringUtil.isBlank(itemGuid)) {
            jsonObject.put("msg", "项目标识不能为空！");
            return jsonObject;
        }
        AuditRsItemBaseinfo auditRsItemBaseinfo = iAuditRsItemBaseinfo.getAuditRsItemBaseinfoByRowguid(itemGuid)
                .getResult();
        if (auditRsItemBaseinfo == null) {
            jsonObject.put("msg", "项目不存在！");
            return jsonObject;
        }
        // 推送过的项目不再推送，模拟图审系统返回结果
        if (StringUtil.isNotBlank(auditRsItemBaseinfo.get("itemid"))) {
            JSONObject data = new JSONObject();
            data.put("itemId", auditRsItemBaseinfo.get("itemid"));
            jsonObject.put("data", data);
            return jsonObject;
        }
        // 落图赋码系统地址
        String tuShenUrl = iConfigService.getFrameConfigValue("tushen_url");
        // 落图赋码系统授予己方单位验证码
        String tuShenToken = iConfigService.getFrameConfigValue("tushen_token");
        if (StringUtil.isBlank(tuShenUrl) || StringUtil.isBlank(tuShenToken)) {
            jsonObject.put("msg", "图审系统地址或图审系统授予己方单位验证码不能为空！");
            return jsonObject;
        }
        // 兼容url配置
        tuShenUrl = checkTuShenUrl(tuShenUrl);

        tuShenUrl += "item_codeinfo/InsertInfo";

        // 请求头
        Map<String, String> headers = new HashMap<>();
        headers.put("Token", tuShenToken);
        // headers.put("Content-Type", "application/json");
        // 设置参数
        JSONObject params = new JSONObject();
        // 项目名称
        params.put("ItemName", auditRsItemBaseinfo.getItemname());
        // 工改码
        params.put("Code", auditRsItemBaseinfo.getItemcode());
        // 临时码（非必填）
        params.put("TemporaryCode", "");
        // 建设单位社会统一信用代码
        params.put("SocialCode", auditRsItemBaseinfo.getItemlegalcreditcode());
        // 县级行政区划代码
        // params.put("CityCode", auditRsItemBaseinfo.getBelongxiaqucode());
        params.put("CityCode", "370800");
        Log.info("工改赋码：推送项目信息接口请求参数" + params);
        String data = HttpUtil.doPostJson(tuShenUrl, params.toJSONString(), headers);
        Log.info("工改赋码：推送项目信息接口返回结果" + data);
        if (StringUtil.isNotBlank(data)) {
            // 转为json对象
            JSONObject jsondata = JSONObject.parseObject(data);
            boolean responseStatus = jsondata.getBooleanValue("status");
            if (responseStatus) {
                if (!jsondata.containsKey("data") || jsondata.getJSONObject("data") == null
                        || jsondata.getJSONObject("data").isEmpty()) {
                    jsonObject.put("msg", "图审系统未返回项目信息");
                    return jsonObject;
                }
                String itemId = jsondata.getJSONObject("data").getString("itemId");
                if (StringUtil.isBlank(itemId)) {
                    jsonObject.put("msg", "图审系统未返回项目id");
                    return jsonObject;
                }
                auditRsItemBaseinfo.set("itemid", itemId);
                iAuditRsItemBaseinfo.updateAuditRsItemBaseinfo(auditRsItemBaseinfo);
                return jsondata;
            }
            else {
                jsonObject.put("msg", jsondata.getString("message"));
            }
        }
        else {
            jsonObject.put("msg", "图审系统接口返回空");
        }

        return jsonObject;
    }

    /**
     * [工改赋码：新增单体]
     * 
     * @param danTiInfos
     * @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public JSONObject insertSubsAsync(JSONArray danTiInfos) {
        JSONObject jsonObject = new JSONObject();
        IConfigService iConfigService = ContainerFactory.getContainInfo().getComponent(IConfigService.class);
        // 落图赋码系统地址
        String tuShenUrl = iConfigService.getFrameConfigValue("tushen_url");
        // 落图赋码系统授予己方单位验证码
        String tuShenToken = iConfigService.getFrameConfigValue("tushen_token");
        if (StringUtil.isBlank(tuShenUrl) || StringUtil.isBlank(tuShenToken)) {
            jsonObject.put("msg", "图审系统地址或图审系统授予己方单位验证码不能为空！");
            return jsonObject;
        }
        // 兼容url配置
        tuShenUrl = checkTuShenUrl(tuShenUrl);

        tuShenUrl += "item_codesubinfo/InsertSubsAsync";

        // 请求头
        Map<String, String> headers = new HashMap<>();
        headers.put("Token", tuShenToken);
        headers.put("Content-Type", "application/json");
        Log.info("工改赋码：新增单体接口请求参数" + danTiInfos);
        String data = HttpUtil.doPostJson(tuShenUrl, danTiInfos.toJSONString(), headers);
        Log.info("工改赋码：新增单体接口返回结果" + data);
        if (StringUtil.isNotBlank(data)) {
            // 转为json对象
            JSONObject jsondata = JSONObject.parseObject(data);
            boolean responseStatus = jsondata.getBooleanValue("status");
            if (responseStatus) {
                return jsondata;
            }
            else {
                jsonObject.put("msg", jsondata.getString("message"));
            }
        }
        else {
            jsonObject.put("msg", "图审系统接口返回空");
        }
        return jsonObject;
    }

    /**
     * [工改赋码：更新单体]
     * 
     * @param danTiInfos
     * @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public JSONObject updateSubsAsync(JSONArray danTiInfos) {
        JSONObject jsonObject = new JSONObject();
        IConfigService iConfigService = ContainerFactory.getContainInfo().getComponent(IConfigService.class);
        // 落图赋码系统地址
        String tuShenUrl = iConfigService.getFrameConfigValue("tushen_url");
        // 落图赋码系统授予己方单位验证码
        String tuShenToken = iConfigService.getFrameConfigValue("tushen_token");
        if (StringUtil.isBlank(tuShenUrl) || StringUtil.isBlank(tuShenToken)) {
            jsonObject.put("msg", "图审系统地址或图审系统授予己方单位验证码不能为空！");
            return jsonObject;
        }
        // 兼容url配置
        tuShenUrl = checkTuShenUrl(tuShenUrl);

        tuShenUrl += "item_codesubinfo/updateSubsAsync";

        // 请求头
        Map<String, String> headers = new HashMap<>();
        headers.put("Token", tuShenToken);
        headers.put("Content-Type", "application/json");
        Log.info("工改赋码：更新单体接口请求参数" + danTiInfos);
        String data = HttpUtil.doPostJson(tuShenUrl, danTiInfos.toJSONString(), headers);
        Log.info("工改赋码：更新单体接口返回结果" + data);
        if (StringUtil.isNotBlank(data)) {
            // 转为json对象
            JSONObject jsondata = JSONObject.parseObject(data);
            boolean responseStatus = jsondata.getBooleanValue("status");
            if (responseStatus) {
                return jsondata;
            }
            else {
                jsonObject.put("msg", jsondata.getString("message"));
            }
        }
        else {
            jsonObject.put("msg", "图审系统接口返回空");
        }
        return jsonObject;
    }

    // 兼容url配置
    private String checkTuShenUrl(String tuShenUrl) {
        if (tuShenUrl.contains("api")) {
            if (tuShenUrl.endsWith("api")) {
                tuShenUrl += "/";
            }
            else if (!tuShenUrl.endsWith("api/")) {
                if (tuShenUrl.endsWith("/")) {
                    tuShenUrl += "api/";
                }
                else {
                    tuShenUrl += "/api/";
                }
            }
        }
        else if (!tuShenUrl.endsWith("api/")) {
            if (tuShenUrl.endsWith("/")) {
                tuShenUrl += "api/";
            }
            else {
                tuShenUrl += "/api/";
            }
        }
        return tuShenUrl;
    }
    /***** 工改单体赋码与住建图审系统对接需求登记单结束 *****/
}
