package com.epoint.auditselfservice.auditselfservicerest.common;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.aspose.words.Document;
import com.aspose.words.DocumentBuilder;
import com.aspose.words.License;
import com.aspose.words.SaveFormat;
import com.epoint.cert.commonutils.AttachUtil;
import com.epoint.common.util.JsonUtils;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.classpath.ClassPathUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import com.epoint.frame.service.attach.entity.FrameAttachStorage;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/qfznsbyyzz")
public class QFZnsbYyzzRestController {
    @Autowired
    private IAttachService attachService;
    /**
     * 汉语中数字大写
     */
    private static final String[] CN_UPPER_NUMBER = {"零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖"};
    /**
     * 汉语中货币单位大写，这样的设计类似于占位符
     */
    private static final String[] CN_UPPER_MONETRAY_UNIT = {"分", "角", "元", "拾", "佰", "仟", "万", "拾", "佰", "仟", "亿", "拾",
            "佰", "仟", "兆", "拾", "佰", "仟"};
    /**
     * 特殊字符：整
     */
    private static final String CN_FULL = "整";
    /**
     * 特殊字符：负
     */
    private static final String CN_NEGATIVE = "负";
    /**
     * 金额的精度，默认值为2
     */
    private static final int MONEY_PRECISION = 2;
    /**
     * 特殊字符：零元整
     */
    private static final String CN_ZEOR_FULL = "零元" + CN_FULL;

    /**
     * 获取可打印列表
     *
     * @return
     * @params params
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/getYyzzList", method = RequestMethod.POST)
    public String getYyzzList(@RequestBody String params) {
        try {

            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");
            JSONObject dataJson = new JSONObject();

            String fzrmobile = obj.getString("fzrmobile");
            String currentPage = obj.getString("currentpage");
            String pageSize = obj.getString("pagesize");

            List<JSONObject> list = new ArrayList<JSONObject>();
            JSONObject zzlistJson = new JSONObject();

            int pageindex = Integer.parseInt(currentPage);
            int pagesize = Integer.parseInt(pageSize);
            String tablename = "audit_znsb_yyzz_info";
            String ordebystr = "CLDATE";
            String wherestr = " and fzrmobile='" + fzrmobile + "' and IFNULL(ISPRINT,'0')='0' ";
            int beginindex = pageindex * pagesize;
            int totalcount = getlistcount(tablename, wherestr);
            List<Record> datalist = getlist(beginindex, pagesize, "*", tablename, ordebystr, wherestr);

            for (Record zz : datalist) {
                zzlistJson = new JSONObject();
                zzlistJson.put("rowguid", zz.getStr("rowguid"));
                zzlistJson.put("qyname", zz.getStr("qyname"));
                zzlistJson.put("legal", zz.getStr("legal"));
                zzlistJson.put("type", zz.getStr("ZZTYPE"));
                list.add(zzlistJson);
            }

            dataJson.put("zzlist", list);
            dataJson.put("totalcount", StringUtil.getNotNullString(totalcount));

            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        } catch (JSONException e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }
    }

    /**
     * 获取证照详细信息
     *
     * @return
     * @throws Exception
     * @params params
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/getYyzzInfo", method = RequestMethod.POST)
    public String getYyzzInfo(@RequestBody String params) throws Exception {
        try {

            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");
            JSONObject dataJson = new JSONObject();

            String rowguid = obj.getString("rowguid");
            String legal = "";
            String rpreson = "";
            String fzrmobile = "";
            String tyshxydm = "";
            String qyname = "";
            String qytype = "";
            String address = "";
            String zczb = "";
            String cldate = "";
            String fromdate = "";
            String todate = "";
            String rangen = "";
            String zch = "";
            String gsxtdz = "http://sd.gsxt.gov.cn";
            //String rwm = "";

            List<Record> datalist = getinfolist(rowguid);
            if (datalist.size() > 0) {
                legal = datalist.get(0).getStr("legal");
                rpreson = datalist.get(0).getStr("rpreson");
                fzrmobile = datalist.get(0).getStr("fzrmobile");
                tyshxydm = datalist.get(0).getStr("tyshxydm");
                qyname = datalist.get(0).getStr("qyname");
                qytype = datalist.get(0).getStr("qytype");
                address = datalist.get(0).getStr("address");
                if (StringUtil.isNotBlank(datalist.get(0).getStr("zczb"))) {
                    Double zz = Double.parseDouble(datalist.get(0).getStr("zczb"));
                    zczb = number2CNMontrayUnit(BigDecimal.valueOf(zz * 10000));
                } else {
                    zczb = "";
                }
                cldate = datalist.get(0).getStr("cldate");
                fromdate = datalist.get(0).getStr("fromdate");
                todate = datalist.get(0).getStr("todate");
                rangen = datalist.get(0).getStr("rangen");
                zch = datalist.get(0).getStr("zch");
            }
            dataJson.put("legal", legal);
            dataJson.put("rpreson", rpreson);
            dataJson.put("fzrmobile", fzrmobile);
            dataJson.put("tyshxydm", tyshxydm);
            dataJson.put("qyname", qyname);
            dataJson.put("qytype", qytype);
            dataJson.put("address", address);
            dataJson.put("zczb", zczb);
            dataJson.put("cldate", cldate);
            dataJson.put("fromdate", fromdate);
            dataJson.put("todate", todate);
            dataJson.put("rangen", rangen);
            dataJson.put("zch", zch);
            dataJson.put("gsxtdz", gsxtdz);

            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        } catch (JSONException e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }
    }

    /**
     * 获取证照打印地址guid
     *
     * @return
     * @throws Exception
     * @params params
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/getYyzzZBAttachguid", method = RequestMethod.POST)
    public String getYyzzZBAttachguid(@RequestBody String params) throws Exception {
        try {
            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");
            JSONObject dataJson = new JSONObject();
            String zbwordurl = "";
            String rowguid = obj.getString("rowguid");
            String zbofficalDocAttachGuid = "";
            String legal = "";
            String tyshxydm = "";
            String qyname = "";
            String qytype = "";
            String address = "";
            String zczb = "";
            String cldate = "";
            String fromdate = "";
            String todate = "";
            String rangen = "";
            String zch = "";
            String zztype = "";
            List<Record> datalist = getinfolist(rowguid);
            if (datalist.size() > 0) {
                zztype = datalist.get(0).getStr("zztype");
                if ("1".equals(zztype)) {
                    legal = datalist.get(0).getStr("legal");
                    tyshxydm = datalist.get(0).getStr("tyshxydm");
                    qyname = datalist.get(0).getStr("qyname");
                    qytype = datalist.get(0).getStr("qytype");
                    address = datalist.get(0).getStr("address");

                    Double zz = Double.parseDouble(datalist.get(0).getStr("zczb"));
                    zczb = number2CNMontrayUnit(BigDecimal.valueOf(zz * 10000));
                    cldate = datalist.get(0).getStr("cldate");
                    fromdate = datalist.get(0).getStr("fromdate");
                    todate = datalist.get(0).getStr("todate");
                    rangen = datalist.get(0).getStr("rangen");
                    zch = datalist.get(0).getStr("zch");
                    // word域名称
                    String[] fieldNames = {"yyzzID", "yyzzname", "yyzztype", "yyzzaddress", "yyzzlegalperson",
                            "yyzzcapital", "yyzzbirthday", "yyzzyxq", "yyzzBusiScop", "ye", "mo", "day"};
                    // 对应值
                    Object[] values = {tyshxydm, qyname, qytype, address, legal, zczb, getDate(cldate),
                            getDate(fromdate) + "至" + getDate(todate), rangen, getCurrentYear(), getCurrentMonth(),
                            getCurrentDay()};

                    String docCliengguid = getclient11();
                    List<FrameAttachStorage> templateDocs = attachService.getAttachListByGuid(docCliengguid);
                    if (templateDocs != null && templateDocs.size() > 0) {
                        FrameAttachStorage storage = templateDocs.get(0);
                        String licenseName = ClassPathUtil.getClassesPath() + "license.xml";
                        try {
                            new License().setLicense(licenseName);// TODO
                            Document doc = new Document(storage.getContent());

                            doc.getMailMerge().execute(fieldNames, values);

                            //二维码
                            String codecenter = "统一社会信用代码：" + tyshxydm + " 注册号：" + zch + " 企业名称：" + qyname
                                    + " 信用网址：http://sd.gsxt.gov.cb";
                            this.insertImage(this.generateQRCodeStream(codecenter, 500, 500), doc, "qrcord");

                            String newattachguid = UUID.randomUUID().toString();
                            String newcliengguid = UUID.randomUUID().toString();
                            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                            doc.save(outputStream, SaveFormat.DOC);// 保存成word
                            ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
                            long size = inputStream.available();
                            FrameAttachInfo frameAttachInfo = AttachUtil.saveFileInputStream(newattachguid,
                                    newcliengguid, tyshxydm + "营业执照正本.doc", ".doc", tyshxydm, size, inputStream, "",
                                    "营业执照正本");

                            zbofficalDocAttachGuid = frameAttachInfo.getAttachGuid();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                if ("2".equals(zztype) || "4".equals(zztype)) {
                    legal = datalist.get(0).getStr("legal");
                    tyshxydm = datalist.get(0).getStr("tyshxydm");
                    qyname = datalist.get(0).getStr("qyname");
                    qytype = datalist.get(0).getStr("qytype");
                    address = datalist.get(0).getStr("address");
                    cldate = datalist.get(0).getStr("cldate");
                    fromdate = datalist.get(0).getStr("fromdate");
                    todate = datalist.get(0).getStr("todate");
                    rangen = datalist.get(0).getStr("rangen");
                    zch = datalist.get(0).getStr("zch");
                    // word域名称
                    String[] fieldNames = {"yyzzID", "yyzzname", "yyzztype", "yyzzaddress", "yyzzlegalperson",
                            "yyzzbirthday", "yyzzyxq", "yyzzBusiScop", "ye", "mo", "day"};
                    // 对应值
                    Object[] values = {tyshxydm, qyname, qytype, address, legal, getDate(cldate),
                            getDate(fromdate) + "至" + getDate(todate), rangen, getCurrentYear(), getCurrentMonth(),
                            getCurrentDay()};

                    String docCliengguid = getclient12();
                    List<FrameAttachStorage> templateDocs = attachService.getAttachListByGuid(docCliengguid);
                    if (templateDocs != null && templateDocs.size() > 0) {
                        FrameAttachStorage storage = templateDocs.get(0);
                        String licenseName = ClassPathUtil.getClassesPath() + "license.xml";
                        try {
                            new License().setLicense(licenseName);// TODO
                            Document doc = new Document(storage.getContent());

                            doc.getMailMerge().execute(fieldNames, values);

                            //二维码
                            String codecenter = "统一社会信用代码：" + tyshxydm + " 注册号：" + zch + " 企业名称：" + qyname
                                    + " 信用网址：http://sd.gsxt.gov.cb";
                            this.insertImage(this.generateQRCodeStream(codecenter, 500, 500), doc, "qrcord");

                            String newattachguid = UUID.randomUUID().toString();
                            String newcliengguid = UUID.randomUUID().toString();
                            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                            doc.save(outputStream, SaveFormat.DOC);// 保存成word
                            ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
                            long size = inputStream.available();
                            FrameAttachInfo frameAttachInfo = AttachUtil.saveFileInputStream(newattachguid,
                                    newcliengguid, tyshxydm + "分公司营业执照正本.doc", ".doc", tyshxydm, size, inputStream, "",
                                    "分公司营业执照正本");

                            zbofficalDocAttachGuid = frameAttachInfo.getAttachGuid();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                if ("3".equals(zztype)) {
                    legal = datalist.get(0).getStr("legal");
                    tyshxydm = datalist.get(0).getStr("tyshxydm");
                    qyname = datalist.get(0).getStr("qyname");
                    qytype = datalist.get(0).getStr("qytype");
                    address = datalist.get(0).getStr("address");

                    Double zz = Double.parseDouble(datalist.get(0).getStr("zczb"));
                    zczb = number2CNMontrayUnit(BigDecimal.valueOf(zz * 10000));
                    cldate = datalist.get(0).getStr("cldate");
                    fromdate = datalist.get(0).getStr("fromdate");
                    todate = datalist.get(0).getStr("todate");
                    rangen = datalist.get(0).getStr("rangen");
                    zch = datalist.get(0).getStr("zch");
                    // word域名称
                    String[] fieldNames = {"yyzzID", "yyzzname", "yyzztype", "yyzzaddress", "yyzzlegalperson",
                            "yyzzcapital", "yyzzbirthday", "yyzzyxq", "yyzzBusiScop", "ye", "mo", "day"};
                    // 对应值
                    Object[] values = {tyshxydm, qyname, qytype, address, legal, zczb, getDate(cldate),
                            getDate(fromdate) + "至" + getDate(todate), rangen, getCurrentYear(), getCurrentMonth(),
                            getCurrentDay()};

                    String docCliengguid = getclient13();
                    List<FrameAttachStorage> templateDocs = attachService.getAttachListByGuid(docCliengguid);
                    if (templateDocs != null && templateDocs.size() > 0) {
                        FrameAttachStorage storage = templateDocs.get(0);
                        String licenseName = ClassPathUtil.getClassesPath() + "license.xml";
                        try {
                            new License().setLicense(licenseName);// TODO
                            Document doc = new Document(storage.getContent());

                            doc.getMailMerge().execute(fieldNames, values);

                            //二维码
                            String codecenter = "统一社会信用代码：" + tyshxydm + " 注册号：" + zch + " 企业名称：" + qyname
                                    + " 信用网址：http://sd.gsxt.gov.cb";
                            this.insertImage(this.generateQRCodeStream(codecenter, 500, 500), doc, "qrcord");

                            String newattachguid = UUID.randomUUID().toString();
                            String newcliengguid = UUID.randomUUID().toString();
                            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                            doc.save(outputStream, SaveFormat.DOC);// 保存成word
                            ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
                            long size = inputStream.available();
                            FrameAttachInfo frameAttachInfo = AttachUtil.saveFileInputStream(newattachguid,
                                    newcliengguid, tyshxydm + "股份合作制正本.doc", ".doc", tyshxydm, size, inputStream, "",
                                    "股份合作制正本");

                            zbofficalDocAttachGuid = frameAttachInfo.getAttachGuid();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            //zbwordurl = "frame/pages/basic/attach/attachdown?attachGuid=" + zbofficalDocAttachGuid;
            dataJson.put("wordurl", zbofficalDocAttachGuid);
            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        } catch (JSONException e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }
    }

    /**
     * 获取证照打印地址guid
     *
     * @return
     * @throws Exception
     * @params params
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/getYyzzFBAttachguid", method = RequestMethod.POST)
    public String getYyzzFBAttachguid(@RequestBody String params) throws Exception {
        try {
            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");
            JSONObject dataJson = new JSONObject();
            String zbwordurl = "";
            String rowguid = obj.getString("rowguid");
            String zbofficalDocAttachGuid = "";
            String legal = "";
            String tyshxydm = "";
            String qyname = "";
            String qytype = "";
            String address = "";
            String zczb = "";
            String cldate = "";
            String fromdate = "";
            String todate = "";
            String rangen = "";
            String zch = "";
            String zztype = "";
            List<Record> datalist = getinfolist(rowguid);
            if (datalist.size() > 0) {
                zztype = datalist.get(0).getStr("zztype");
                if ("1".equals(zztype)) {
                    legal = datalist.get(0).getStr("legal");
                    tyshxydm = datalist.get(0).getStr("tyshxydm");
                    qyname = datalist.get(0).getStr("qyname");
                    qytype = datalist.get(0).getStr("qytype");
                    address = datalist.get(0).getStr("address");

                    Double zz = Double.parseDouble(datalist.get(0).getStr("zczb"));
                    zczb = number2CNMontrayUnit(BigDecimal.valueOf(zz * 10000));
                    cldate = datalist.get(0).getStr("cldate");
                    fromdate = datalist.get(0).getStr("fromdate");
                    todate = datalist.get(0).getStr("todate");
                    rangen = datalist.get(0).getStr("rangen");
                    zch = datalist.get(0).getStr("zch");
                    // word域名称
                    String[] fieldNames = {"yyzzID", "yyzzname", "yyzztype", "yyzzaddress", "yyzzlegalperson",
                            "yyzzcapital", "yyzzbirthday", "yyzzyxq", "yyzzBusiScop", "ye", "mo", "day"};
                    // 对应值
                    Object[] values = {tyshxydm, qyname, qytype, address, legal, zczb, getDate(cldate),
                            getDate(fromdate) + "至" + getDate(todate), rangen, getCurrentYear(), getCurrentMonth(),
                            getCurrentDay()};

                    String docCliengguid = getclient21();
                    List<FrameAttachStorage> templateDocs = attachService.getAttachListByGuid(docCliengguid);
                    if (templateDocs != null && templateDocs.size() > 0) {
                        FrameAttachStorage storage = templateDocs.get(0);
                        String licenseName = ClassPathUtil.getClassesPath() + "license.xml";
                        try {
                            new License().setLicense(licenseName);// TODO
                            Document doc = new Document(storage.getContent());

                            doc.getMailMerge().execute(fieldNames, values);

                            //二维码
                            String codecenter = "统一社会信用代码：" + tyshxydm + " 注册号：" + zch + " 企业名称：" + qyname
                                    + " 信用网址：http://sd.gsxt.gov.cb";
                            this.insertImage(this.generateQRCodeStream(codecenter, 120, 120), doc, "qrcord");

                            String newattachguid = UUID.randomUUID().toString();
                            String newcliengguid = UUID.randomUUID().toString();
                            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                            doc.save(outputStream, SaveFormat.DOC);// 保存成word
                            ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
                            long size = inputStream.available();
                            FrameAttachInfo frameAttachInfo = AttachUtil.saveFileInputStream(newattachguid,
                                    newcliengguid, tyshxydm + "营业执照副本.doc", ".doc", tyshxydm, size, inputStream, "",
                                    "营业执照副本");

                            zbofficalDocAttachGuid = frameAttachInfo.getAttachGuid();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                if ("2".equals(zztype) || "4".equals(zztype)) {
                    legal = datalist.get(0).getStr("legal");
                    tyshxydm = datalist.get(0).getStr("tyshxydm");
                    qyname = datalist.get(0).getStr("qyname");
                    qytype = datalist.get(0).getStr("qytype");
                    address = datalist.get(0).getStr("address");
                    cldate = datalist.get(0).getStr("cldate");
                    fromdate = datalist.get(0).getStr("fromdate");
                    todate = datalist.get(0).getStr("todate");
                    rangen = datalist.get(0).getStr("rangen");
                    zch = datalist.get(0).getStr("zch");
                    // word域名称
                    String[] fieldNames = {"yyzzID", "yyzzname", "yyzztype", "yyzzaddress", "yyzzlegalperson",
                            "yyzzbirthday", "yyzzyxq", "yyzzBusiScop", "ye", "mo", "day"};
                    // 对应值
                    Object[] values = {tyshxydm, qyname, qytype, address, legal, getDate(cldate),
                            getDate(fromdate) + "至" + getDate(todate), rangen, getCurrentYear(), getCurrentMonth(),
                            getCurrentDay()};

                    String docCliengguid = getclient22();
                    List<FrameAttachStorage> templateDocs = attachService.getAttachListByGuid(docCliengguid);
                    if (templateDocs != null && templateDocs.size() > 0) {
                        FrameAttachStorage storage = templateDocs.get(0);
                        String licenseName = ClassPathUtil.getClassesPath() + "license.xml";
                        try {
                            new License().setLicense(licenseName);// TODO
                            Document doc = new Document(storage.getContent());

                            doc.getMailMerge().execute(fieldNames, values);

                            //二维码
                            String codecenter = "统一社会信用代码：" + tyshxydm + " 注册号：" + zch + " 企业名称：" + qyname
                                    + " 信用网址：http://sd.gsxt.gov.cb";
                            this.insertImage(this.generateQRCodeStream(codecenter, 120, 120), doc, "qrcord");

                            String newattachguid = UUID.randomUUID().toString();
                            String newcliengguid = UUID.randomUUID().toString();
                            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                            doc.save(outputStream, SaveFormat.DOC);// 保存成word
                            ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
                            long size = inputStream.available();
                            FrameAttachInfo frameAttachInfo = AttachUtil.saveFileInputStream(newattachguid,
                                    newcliengguid, tyshxydm + "分公司营业执照副本.doc", ".doc", tyshxydm, size, inputStream, "",
                                    "分公司营业执照副本");

                            zbofficalDocAttachGuid = frameAttachInfo.getAttachGuid();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                if ("3".equals(zztype)) {
                    legal = datalist.get(0).getStr("legal");
                    tyshxydm = datalist.get(0).getStr("tyshxydm");
                    qyname = datalist.get(0).getStr("qyname");
                    qytype = datalist.get(0).getStr("qytype");
                    address = datalist.get(0).getStr("address");

                    Double zz = Double.parseDouble(datalist.get(0).getStr("zczb"));
                    zczb = number2CNMontrayUnit(BigDecimal.valueOf(zz * 10000));
                    cldate = datalist.get(0).getStr("cldate");
                    fromdate = datalist.get(0).getStr("fromdate");
                    todate = datalist.get(0).getStr("todate");
                    rangen = datalist.get(0).getStr("rangen");
                    zch = datalist.get(0).getStr("zch");
                    // word域名称
                    String[] fieldNames = {"yyzzID", "yyzzname", "yyzztype", "yyzzaddress", "yyzzlegalperson",
                            "yyzzcapital", "yyzzbirthday", "yyzzyxq", "yyzzBusiScop", "ye", "mo", "day"};
                    // 对应值
                    Object[] values = {tyshxydm, qyname, qytype, address, legal, zczb, getDate(cldate),
                            getDate(fromdate) + "至" + getDate(todate), rangen, getCurrentYear(), getCurrentMonth(),
                            getCurrentDay()};

                    String docCliengguid = getclient23();
                    List<FrameAttachStorage> templateDocs = attachService.getAttachListByGuid(docCliengguid);
                    if (templateDocs != null && templateDocs.size() > 0) {
                        FrameAttachStorage storage = templateDocs.get(0);
                        String licenseName = ClassPathUtil.getClassesPath() + "license.xml";
                        try {
                            new License().setLicense(licenseName);// TODO
                            Document doc = new Document(storage.getContent());

                            doc.getMailMerge().execute(fieldNames, values);

                            //二维码
                            String codecenter = "统一社会信用代码：" + tyshxydm + " 注册号：" + zch + " 企业名称：" + qyname
                                    + " 信用网址：http://sd.gsxt.gov.cb";
                            this.insertImage(this.generateQRCodeStream(codecenter, 120, 120), doc, "qrcord");

                            String newattachguid = UUID.randomUUID().toString();
                            String newcliengguid = UUID.randomUUID().toString();
                            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                            doc.save(outputStream, SaveFormat.DOC);// 保存成word
                            ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
                            long size = inputStream.available();
                            FrameAttachInfo frameAttachInfo = AttachUtil.saveFileInputStream(newattachguid,
                                    newcliengguid, tyshxydm + "股份合作制副本.doc", ".doc", tyshxydm, size, inputStream, "",
                                    "股份合作制副本");

                            zbofficalDocAttachGuid = frameAttachInfo.getAttachGuid();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            //zbwordurl = "frame/pages/basic/attach/attachdown?attachGuid=" + zbofficalDocAttachGuid;
            dataJson.put("wordurl", zbofficalDocAttachGuid);
            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        } catch (JSONException e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }
    }

    public byte[] generateQRCodeStream(String contents, int width, int height)
            throws WriterException, FileNotFoundException, IOException, com.google.zxing.WriterException {

        contents = new String(contents.getBytes("UTF-8"), "ISO-8859-1");
        QRCodeWriter writer = new QRCodeWriter();
        BitMatrix matrix = writer.encode(contents, BarcodeFormat.QR_CODE, width, height);
        ByteArrayOutputStream imageStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(matrix, "PNG", imageStream);
        byte[] tagInfo = imageStream.toByteArray();
        return tagInfo;
    }

    public void insertImage(byte[] imageByteStream, Document doc, String tag) throws IOException, Exception {
        DocumentBuilder build = new DocumentBuilder(doc);
        build.moveToBookmark(tag);
        build.insertImage(imageByteStream);
    }

    public String getclient11() {
        String client = "";
        CommonDao dao = CommonDao.getInstance();
        String sql = "select TEMPLETCLIENGGUID from cert_catalog WHERE CERTNAME='智能设备营业执照正本' order by OperateDate desc LIMIT 1";
        client = dao.find(sql, String.class);
        dao.close();
        return client;
    }

    public String getclient12() {
        String client = "";
        CommonDao dao = CommonDao.getInstance();
        String sql = "select TEMPLETCLIENGGUID from cert_catalog WHERE CERTNAME='智能设备分公司营业执照正本' order by OperateDate desc LIMIT 1";
        client = dao.find(sql, String.class);
        dao.close();
        return client;
    }

    public String getclient13() {
        String client = "";
        CommonDao dao = CommonDao.getInstance();
        String sql = "select TEMPLETCLIENGGUID from cert_catalog WHERE CERTNAME='智能设备股份合作制正本' order by OperateDate desc LIMIT 1";
        client = dao.find(sql, String.class);
        dao.close();
        return client;
    }

    public String getclient21() {
        String client = "";
        CommonDao dao = CommonDao.getInstance();
        String sql = "select TEMPLETCLIENGGUID from cert_catalog WHERE CERTNAME='智能设备营业执照副本' order by OperateDate desc LIMIT 1";
        client = dao.find(sql, String.class);
        dao.close();
        return client;
    }

    public String getclient22() {
        String client = "";
        CommonDao dao = CommonDao.getInstance();
        String sql = "select TEMPLETCLIENGGUID from cert_catalog WHERE CERTNAME='智能设备分公司营业执照副本' order by OperateDate desc LIMIT 1";
        client = dao.find(sql, String.class);
        dao.close();
        return client;
    }

    public String getclient23() {
        String client = "";
        CommonDao dao = CommonDao.getInstance();
        String sql = "select TEMPLETCLIENGGUID from cert_catalog WHERE CERTNAME='智能设备股份合作制副本' order by OperateDate desc LIMIT 1";
        client = dao.find(sql, String.class);
        dao.close();
        return client;
    }

    @SuppressWarnings("finally")
    public static String getDate(String date1) throws ParseException {
        if (StringUtil.isNotBlank(date1)) {
            String now = "";
            try {
                Date date = new SimpleDateFormat("yyyy-MM-dd").parse(date1);
                now = new SimpleDateFormat("yyyy年MM月dd日").format(date);
            } catch (Exception e) {
                now = "   年   月   日";
            } finally {
                return now;
            }
        } else {
            return "   年    月   日";
        }
    }

    public static String getCurrentYear() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        Date date = new Date();
        return sdf.format(date);
    }

    public static String getCurrentMonth() {
        SimpleDateFormat sdf = new SimpleDateFormat("MM");
        Date date = new Date();
        return sdf.format(date);
    }

    public static String getCurrentDay() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd");
        Date date = new Date();
        return sdf.format(date);
    }

    /**
     * 更新人脸识别信息
     *
     * @return
     * @params params
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/YyzzRzFace", method = RequestMethod.POST)
    public String YyzzRzFace(@RequestBody String params) {
        try {

            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");
            JSONObject dataJson = new JSONObject();

            String rowguid = obj.getString("rowguid");
            String certification = obj.getString("certification");
            String certificationphoto = obj.getString("certificationphoto");
            updatefaceinfo(rowguid, certification, certificationphoto);

            dataJson.put("code", "1");

            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        } catch (JSONException e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }
    }

    /**
     * 获取验证码
     *
     * @return
     * @params params
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/getYyzzRzYZM", method = RequestMethod.POST)
    public String getYyzzRzYZM(@RequestBody String params) {
        try {

            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");
            JSONObject dataJson = new JSONObject();

            String rowguid = obj.getString("rowguid");
            String mobile = getmobile(rowguid);
            int yzm = (int) ((Math.random() * 9 + 1) * 100000);
            insertmessageinfo(mobile, String.valueOf(yzm));

            dataJson.put("yzm", yzm);
            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        } catch (JSONException e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }
    }

    /**
     * 获取手机号码
     *
     * @return
     * @params params
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/getYyzzRzMobile", method = RequestMethod.POST)
    public String getYyzzRzMobile(@RequestBody String params) {
        try {

            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");
            JSONObject dataJson = new JSONObject();

            String rowguid = obj.getString("rowguid");
            String mobile = getmobile(rowguid);

            dataJson.put("mobile", mobile);
            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        } catch (JSONException e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }
    }

    /**
     * 更新手写签名
     *
     * @return
     * @params params
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/YyzzSign", method = RequestMethod.POST)
    public String YyzzSign(@RequestBody String params) {
        try {

            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");
            JSONObject dataJson = new JSONObject();

            String rowguid = obj.getString("rowguid");
            String sign = obj.getString("sign");

            updatesigninfo(rowguid, sign);

            dataJson.put("code", "1");

            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        } catch (JSONException e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }
    }

    /**
     * 更新打证状态
     *
     * @return
     * @params params
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/YyzzSuccess", method = RequestMethod.POST)
    public String YyzzSuccess(@RequestBody String params) {
        try {
            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");
            JSONObject dataJson = new JSONObject();

            String rowguid = obj.getString("rowguid");
            String isprint = obj.getString("isprint");

            updateisprintinfo(rowguid, isprint);

            dataJson.put("code", "1");

            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        } catch (JSONException e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }
    }

    public List<Record> getlist(int beginindex, int pagesize, String searchlist, String tablename, String ordebystr,
                                String wherestr) {
        String sql = "select " + searchlist + " from " + tablename + " ";
        sql += " where 1=1 " + wherestr + " ";
        sql += " order by " + ordebystr + " DESC,RowGuid desc";

        sql += " LIMIT " + beginindex + "," + pagesize + "";
        CommonDao dao = CommonDao.getInstance();
        List<Record> tasklist = dao.findList(sql, Record.class);
        dao.close();
        return tasklist;
    }

    public int getlistcount(String tablename, String wherestr) {
        String sql = "select RowGuid from " + tablename;
        sql += " where 1=1 " + wherestr + " ";
        CommonDao dao = CommonDao.getInstance();
        List<Record> li = dao.findList(sql, Record.class);
        int count = li.size();
        dao.close();
        return count;
    }

    public List<Record> getinfolist(String rowguid) {
        String sql = "select * from  audit_znsb_yyzz_info where rowguid = ? ";
        CommonDao dao = CommonDao.getInstance();
        List<Record> tasklist = dao.findList(sql, Record.class, rowguid);
        dao.close();
        return tasklist;
    }

    public void updatefaceinfo(String rowguid, String certification, String certificationphoto) {
        String sql = "update audit_znsb_yyzz_info set certification = ? ,certificationphoto = ? where rowguid = ? ";
        CommonDao dao = CommonDao.getInstance();
        dao.execute(sql, certification, certificationphoto, rowguid);
        dao.close();
    }

    public String getmobile(String rowguid) {
        String sql = "select FZRMOBILE from audit_znsb_yyzz_info  where rowguid = ? ";
        CommonDao dao = CommonDao.getInstance();
        String mobile = dao.find(sql, String.class, rowguid);
        dao.close();
        return mobile;
    }

    public void insertmessageinfo(String mobile, String yzm) {
        String sql = "insert into messages_center(messageitemguid,content,sendmode,generatedate,issend,messagetarget) values (NEWID(),?,'1',NOW(),'0',?)";
        CommonDao dao = CommonDao.getInstance();
        dao.execute(sql, "您本次取证的验证码是" + yzm, mobile);
        dao.close();
    }

    public void updatesigninfo(String rowguid, String sign) {
        String sql = "update audit_znsb_yyzz_info set sign = ? where rowguid = ? ";
        CommonDao dao = CommonDao.getInstance();
        dao.execute(sql, sign, rowguid);
        dao.close();
    }

    public void updateisprintinfo(String rowguid, String isprint) {
        String sql = "update audit_znsb_yyzz_info set isprint = ? where rowguid = ? ";
        CommonDao dao = CommonDao.getInstance();
        dao.execute(sql, isprint, rowguid);
        dao.close();
    }

    public static String number2CNMontrayUnit(BigDecimal numberOfMoney) {
        StringBuffer sb = new StringBuffer();
        int signum = numberOfMoney.signum();
        // 零元整的情况
        if (signum == 0) {
            return CN_ZEOR_FULL;
        }
        //这里会进行金额的四舍五入
        long number = numberOfMoney.movePointRight(MONEY_PRECISION).setScale(0, 4).abs().longValue();
        // 得到小数点后两位值
        long scale = number % 100;
        int numUnit = 0;
        int numIndex = 0;
        boolean getZero = false;
        // 判断最后两位数，一共有四中情况：00 = 0, 01 = 1, 10, 11
        if (!(scale > 0)) {
            numIndex = 2;
            number = number / 100;
            getZero = true;
        }
        if ((scale > 0) && (!(scale % 10 > 0))) {
            numIndex = 1;
            number = number / 10;
            getZero = true;
        }
        int zeroSize = 0;
        while (true) {
            if (number <= 0) {
                break;
            }
            // 每次获取到最后一个数
            numUnit = (int) (number % 10);
            if (numUnit > 0) {
                if ((numIndex == 9) && (zeroSize >= 3)) {
                    sb.insert(0, CN_UPPER_MONETRAY_UNIT[6]);
                }
                if ((numIndex == 13) && (zeroSize >= 3)) {
                    sb.insert(0, CN_UPPER_MONETRAY_UNIT[10]);
                }
                sb.insert(0, CN_UPPER_MONETRAY_UNIT[numIndex]);
                sb.insert(0, CN_UPPER_NUMBER[numUnit]);
                getZero = false;
                zeroSize = 0;
            } else {
                ++zeroSize;
                if (!(getZero)) {
                    sb.insert(0, CN_UPPER_NUMBER[numUnit]);
                }
                if (numIndex == 2) {
                    if (number > 0) {
                        sb.insert(0, CN_UPPER_MONETRAY_UNIT[numIndex]);
                    }
                } else if (((numIndex - 2) % 4 == 0) && (number % 1000 > 0)) {
                    sb.insert(0, CN_UPPER_MONETRAY_UNIT[numIndex]);
                }
                getZero = true;
            }
            // 让number每次都去掉最后一个数
            number = number / 10;
            ++numIndex;
        }
        // 如果signum == -1，则说明输入的数字为负数，就在最前面追加特殊字符：负
        if (signum == -1) {
            sb.insert(0, CN_NEGATIVE);
        }
        // 输入的数字小数点后两位为"00"的情况，则要在最后追加特殊字符：整
        if (!(scale > 0)) {
            sb.append(CN_FULL);
        }
        return sb.toString();
    }
}
