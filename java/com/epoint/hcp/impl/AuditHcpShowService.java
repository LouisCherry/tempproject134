package com.epoint.hcp.impl;

import com.alibaba.fastjson.JSONObject;
import com.epoint.common.util.SQLManageUtil;
import com.epoint.core.BaseEntity;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.grammar.Record;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.hcp.api.entity.AuditHcpAreainfo;
import com.epoint.hcp.api.entity.AuditHcpOuinfo;
import com.epoint.hcp.api.entity.AuditHcpUserinfo;
import com.epoint.hcp.api.entity.Evainstance;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 评价跟踪表对应的后台service
 *
 * @author lc
 * @version [版本号, 2019-09-06 15:08:28]
 */
public class AuditHcpShowService {
    /**
     * 数据增删改查组件
     */
    protected ICommonDao baseDao;


    public AuditHcpShowService() {
        baseDao = CommonDao.getInstance();
    }


    public int addEvainstance(Evainstance evainstance) {
        return baseDao.insert(evainstance);
    }

    public List<AuditHcpOuinfo> getOuList(Integer num, String areacode) {
        String sql = "";
        List<AuditHcpOuinfo> list = new ArrayList<>();
        if ("000000".equals(areacode)) {
            sql = "select * from Audit_Hcp_OUInfo order by BJCount desc limit ? ";
            list = baseDao.findList(sql, AuditHcpOuinfo.class, num);
        } else if (areacode.indexOf("000000") != -1) {
            areacode = areacode.substring(0, 4);
            sql = "select * from Audit_Hcp_OUInfo where belongxiaqucode like ? order by BJCount desc limit ? ";
            list = baseDao.findList(sql, AuditHcpOuinfo.class, areacode + "%", num);
        } else if ("370829".equals(areacode)) {

        } else {
            sql = "select * from Audit_Hcp_OUInfo where belongxiaqucode = ? order by BJCount desc limit ? ";
            list = baseDao.findList(sql, AuditHcpOuinfo.class, areacode, num);
        }
        return list;
    }

    public List<AuditHcpAreainfo> getAreaList(Integer num, String areacode) {
        String sql = "";
        List<AuditHcpAreainfo> list = new ArrayList<>();
        if ("000000".equals(areacode)) {
            sql = "select * from Audit_Hcp_AreaInfo order by BJCount desc limit ? ";
            list = baseDao.findList(sql, AuditHcpAreainfo.class, num);
        } else if (areacode.indexOf("000000") != -1) {
            areacode = areacode.substring(0, 4);
            sql = "select * from Audit_Hcp_AreaInfo where areacode like ? order by BJCount desc limit ? ";
            list = baseDao.findList(sql, AuditHcpAreainfo.class, areacode + "%", num);
        } else {
            sql = "select * from Audit_Hcp_AreaInfo where areacode = ? order by BJCount desc limit ? ";
            list = baseDao.findList(sql, AuditHcpAreainfo.class, areacode, num);
        }
        return list;
    }

    public PageData<AuditHcpUserinfo> getAllByPage(Class<? extends BaseEntity> baseClass,
                                                   Map<String, String> conditionMap, Integer first, Integer pageSize, String sortField, String sortOrder) {
        SQLManageUtil sqlManageUtil = new SQLManageUtil(baseClass);
        return sqlManageUtil.getDbListByPage(baseClass, conditionMap, first, pageSize, sortField, sortOrder);
    }

    public List<JSONObject> getArea() {
        List<JSONObject> list = new ArrayList<JSONObject>();
        JSONObject areaJson = new JSONObject(true);
        String areasql = "select xiaqucode as areacode,xiaquname as areaname,rowguid as areaguid,citylevel from audit_orga_area where xiaqucode = '370800' ";
        Record find = baseDao.find(areasql, Record.class);

        areaJson.put("code", find.getStr("areacode"));
        areaJson.put("name", "市本级");

        list.add(areaJson);
        return list;
    }

    private List<JSONObject> children(String code, String areaLel) {
        List<JSONObject> jsonChildren = new ArrayList<>();
        String sql = "select a.areacode,a.areashortname areaname,a.areaguid,a.AreaLel from dn_gov_area a where  a.ParentAreaGuid = ?1  and a.areaguid != ?2  and IFNULL(is_history,'')!='1' and a.is_show='1'  and Cd_operation!='D' and LENGTH(AreaCode)!=9  ORDER BY a.AreaReq DESC";
        List<Record> list = baseDao.findList(sql, Record.class, code, code);
        if ("3".equals(areaLel)) {
            String areasql = "select a.areacode,a.areashortname areaname from dn_gov_area a where a.areaguid = ? and IFNULL(is_history,'')!='1' and is_show='1' and Cd_operation!='D' ";
            Record find = baseDao.find(areasql, Record.class, code);
            JSONObject jsonObject = new JSONObject(true);
            jsonObject.put("name", find.get("areaname") + "本级");
            jsonObject.put("code", find.get("areacode"));
            jsonChildren.add(jsonObject);
        }
        if (list != null && list.size() > 0) {
            for (Record record : list) {
                JSONObject jsonObject = new JSONObject(true);
                if ("2".equals(areaLel)) {
                    jsonObject.put("name", record.getStr("areaname"));
                    jsonObject.put("code", record.getStr("areacode") + "000000");
                    jsonObject.put("children", children(record.getStr("areaguid"), record.getStr("AreaLel")));
                } else {
                    jsonObject.put("name", record.getStr("areaname"));
                    jsonObject.put("code", record.getStr("areacode"));
                    jsonObject.put("children", children(record.getStr("areaguid"), record.getStr("AreaLel")));
                }
                jsonChildren.add(jsonObject);
            }
        }
        return jsonChildren;
    }

    public int getCpzg(String areacode) {
        String sql = "";
        if ("000000".equals(areacode)) {
            sql = "SELECT count(1) FROM evainstance WHERE Evalevel in (1,2) AND answer is NOT NULL";
            return baseDao.queryInt(sql);
        } else if (areacode.indexOf("000000") != -1) {
            areacode = areacode.substring(0, 4);
            sql = "SELECT count(1) FROM evainstance WHERE Evalevel in (1,2) AND answer is NOT NULL and areacode like ? ";
            return baseDao.queryInt(sql, areacode + "%");
        } else {
            sql = "SELECT count(1) FROM evainstance WHERE Evalevel in (1,2) AND answer is NOT NULL and areacode = ? ";
            return baseDao.queryInt(sql, areacode);
        }
    }

    public AuditHcpAreainfo getAreaByCode(String areacode) {
        String sql = "";
        AuditHcpAreainfo info;
        if ("000000".equals(areacode)) {
            sql = "select SUM(bjcount) bjcount,SUM(pjcount) pjcount,SUM(cpcount) cpcount,SUM(mycount) mycount,SUM(cpzgcount) cpzgcount,"
                    + " concat(round(SUM(mycount) / SUM(pjcount) * 100, 2),'%') myd,concat(round(SUM(cpzgcount) / SUM(cpcount) * 100, 2),'%') zgl,"
                    + " SUM(fcmynum) fcmynum,SUM(mynum) mynum,SUM(jbmynum) jbmynum,SUM(bmynum) bmynum,SUM(fcbmynum) fcbmynum,"
                    + " SUM(pcnum) pcnum,SUM(ydnum) ydnum,SUM(padnum) padnum,SUM(ytjnum) ytjnum from Audit_Hcp_AreaInfo ";
            info = baseDao.find(sql, AuditHcpAreainfo.class);
        } else if (areacode.indexOf("000000") != -1) {
            areacode = areacode.substring(0, 4);
            sql = "select SUM(bjcount) bjcount,SUM(pjcount) pjcount,SUM(cpcount) cpcount,SUM(mycount) mycount,SUM(cpzgcount) cpzgcount,"
                    + " concat(round(SUM(mycount) / SUM(pjcount) * 100, 2),'%') myd,concat(round(SUM(cpzgcount) / SUM(cpcount) * 100, 2),'%') zgl,"
                    + " SUM(fcmynum) fcmynum,SUM(mynum) mynum,SUM(jbmynum) jbmynum,SUM(bmynum) bmynum,SUM(fcbmynum) fcbmynum,"
                    + " SUM(pcnum) pcnum,SUM(ydnum) ydnum,SUM(padnum) padnum,SUM(ytjnum) ytjnum from Audit_Hcp_AreaInfo  where areacode like ? ";
            info = baseDao.find(sql, AuditHcpAreainfo.class, areacode + "%");
        } else {
            sql = "select * from Audit_Hcp_AreaInfo where areacode = ? ";
            info = baseDao.find(sql, AuditHcpAreainfo.class, areacode);
        }

        return info;
    }

    public List<Record> getUserList(String areacode, int currentPage, int pageSize) {
        String sql = "";
        if ("000000".equals(areacode)) {
            sql = "SELECT userName,Taskname,proDepart,satisfaction,acceptDate FROM evainstance";
            sql += " order by acceptDate desc limit ?,? ";
            return baseDao.findList(sql, Record.class, currentPage * pageSize, pageSize);
        } else if (areacode.indexOf("000000") != -1) {
            areacode = areacode.substring(0, 4);
            sql = "SELECT userName,Taskname,proDepart,satisfaction,acceptDate FROM evainstance WHERE  areacode like ? ";
            sql += " order by acceptDate desc limit ?,? ";
            return baseDao.findList(sql, Record.class, areacode + "%" + currentPage * pageSize, pageSize);
        } else {
            sql = "SELECT userName,Taskname,proDepart,satisfaction,acceptDate FROM evainstance WHERE areacode = ? ";
            sql += " order by acceptDate desc limit ?,? ";
            return baseDao.findList(sql, Record.class, areacode, currentPage * pageSize, pageSize);
        }
    }

    public int getUserListCount(String areacode) {
        String sql = "";
        if ("000000".equals(areacode)) {
            sql = "SELECT count(1) FROM evainstance WHERE effectivEvalua = 1 and DATE_FORMAT(Createdate,'%Y-%m') = DATE_FORMAT(now(),'%Y-%m')";
            return baseDao.queryInt(sql);
        } else if (areacode.indexOf("000000") != -1) {
            areacode = areacode.substring(0, 4);
            sql = "SELECT count(1) FROM evainstance WHERE effectivEvalua = 1 and areacode like ? and DATE_FORMAT(Createdate,'%Y-%m') = DATE_FORMAT(now(),'%Y-%m')";
            return baseDao.queryInt(sql, areacode + "%");
        } else {
            sql = "SELECT count(1) FROM evainstance WHERE effectivEvalua = 1 and areacode = ? and DATE_FORMAT(Createdate,'%Y-%m') = DATE_FORMAT(now(),'%Y-%m')";
            return baseDao.queryInt(sql, areacode);
        }
    }

    public List<Record> getEvaluateZb(String areacode, String evatype) {
        String sql = "";
        List<String> params = new ArrayList<>();
        if ("000000".equals(areacode)) {
            sql = "select evaldetail,count(1) as total from evainstance where createdate > '2020-09-24' ";
        } else if (areacode.indexOf("000000") != -1) {
            areacode = areacode.substring(0, 4);
            sql = "select evaldetail,count(1) as total from evainstance where createdate > '2020-09-24' and areacode like ? ";
            params.add(areacode + "%");
        } else {
            sql = "select evaldetail,count(1) as total from evainstance where createdate > '2020-09-24' and areacode = ? ";
            params.add(areacode);
        }
        if ("haoping".equals(evatype)) {
            sql += " and evalevel in ('3','4','5') and (evaldetail like '3%' or evaldetail like '4%' or evaldetail like '5%') and evaldetail <> '' ";
        } else {
            sql += " and evalevel in ('1','2') and (evaldetail like '1%' or evaldetail like '2%') and evaldetail <> '' ";
        }
        sql += " group by evaldetail order by count(1) desc";
        return baseDao.findList(sql, Record.class, params.toArray());

    }

}
