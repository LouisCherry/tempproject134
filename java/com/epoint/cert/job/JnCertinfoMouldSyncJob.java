package com.epoint.cert.job;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.epoint.cert.api.IJnCertInfo;
import com.epoint.cert.basic.certcatalog.certmetadata.domain.CertMetadata;
import com.epoint.cert.basic.certcatalog.certmetadata.inter.ICertMetaData;
import com.epoint.cert.basic.certinfo.certinfoextension.domain.CertInfoExtension;
import com.epoint.cert.basic.certinfo.domain.CertInfo;
import com.epoint.cert.basic.certinfo.inter.ICertInfo;
import com.epoint.cert.basic.publish.domain.TZzZmInfo;
import com.epoint.cert.commonutils.CertConstant;
import com.epoint.cert.commonutils.NoSQLSevice;
import com.epoint.cert.commonutils.ValidateUtil;
import com.epoint.core.EpointFrameDsManager;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.json.JsonUtil;
import com.epoint.core.utils.log.LogUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.jdbc.connection.DataSourceConfig;
import com.epoint.frame.service.organ.ou.api.IOuServiceInternal;

/**
 *  证照实例推送服务（使用此服务，请把部门代码设为必填）
 * @作者 dingwei
 * @version [版本号, 2018年3月13日]
 */
@DisallowConcurrentExecution
public class JnCertinfoMouldSyncJob implements Job {

    /**
     * 证照信息API
     */
    private ICertInfo iCertInfo = ContainerFactory.getContainInfo().getComponent(ICertInfo.class);

    /**
     * 元数据API
     */
    private ICertMetaData iCertMetaData = ContainerFactory.getContainInfo().getComponent(ICertMetaData.class);

    /**
     * 部门API
     */
    private IOuServiceInternal iOuService = ContainerFactory.getContainInfo().getComponent(IOuServiceInternal.class);
    
    /**
     * 部门API
     */
    private IJnCertInfo iJnCertInfo = ContainerFactory.getContainInfo().getComponent(IJnCertInfo.class);
    
    /**
     * 前置库数据源配置
     */
    private DataSourceConfig dataSourceConfig = new DataSourceConfig(
            ConfigUtil.getConfigValue("datasyncjdbc", "zzkqzkurl"),
            ConfigUtil.getConfigValue("datasyncjdbc", "zzkqzkname"),
            ConfigUtil.getConfigValue("datasyncjdbc", "zzkqzkpassword"));

    /**
     * 前置库Dao
     */
    private ICommonDao syncCommonDao;

    transient Logger log = LogUtil.getLog(JnCertinfoMouldSyncJob.class);

    /**
     * MongoDB\HBase通用service
     */
    private NoSQLSevice noSQLSevice = new NoSQLSevice();
    
    

    /**
     * 同步证照数量
     */
    private int syncCertinfoCount = 0;
    
    /**
     * 同步证照数量
     */
    private int cancelCertinfoCount = 0;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        try {
            syncCommonDao = CommonDao.getInstance(dataSourceConfig);
            // 设置失效时间5分钟
            syncCommonDao.getDataSource().setRemoveAbandonedTimeout(300);
            // 开启容器数据源和事务
            log.info("========证照实例推送服务开始========");
            EpointFrameDsManager.begin(null);
            // 获得未推送的证照（对应的目录已同步）
            List<CertInfo> certinfoList = iJnCertInfo.getUnPublishCertInfo();
            EpointFrameDsManager.commit();
            EpointFrameDsManager.close();
            if (ValidateUtil.isNotBlankCollection(certinfoList)) {
                for (CertInfo certInfo : certinfoList) {
                	Integer ishistory = certInfo.getIshistory();
                	String status = certInfo.getStatus();
                	//获取当前在用证照数据
                	if ("10".equals(status) && 0 == ishistory) {
                		 try {
                             EpointFrameDsManager.begin(null);
                             // 是否存在历史版本数据
                             TZzZmInfo tZzZmInfo = getOldVersionCertinfo(certInfo.getCertid());
                             // 是否新增
                             boolean isAdd = true;
                             if (tZzZmInfo != null) { // 存在，更新
                                 isAdd = false;
                             } else { // 不存在，新增
                                 tZzZmInfo = new TZzZmInfo();
                                 // 设置为证照版本唯一标识
                                 tZzZmInfo.setId(certInfo.getCertid().replace("-", ""));
                             }
                                 tZzZmInfo.setMould_id(certInfo.getCertname());
                                 tZzZmInfo.setDzzz_no(certInfo.getCertinfocode());//电子证照编号(证照持有人编号+证照编号)
                                 tZzZmInfo.setZz_bh(certInfo.getCertno());//证照编号
                                 tZzZmInfo.setBz_date(certInfo.getAwarddate());//颁证日期
                                 tZzZmInfo.setStar_time(certInfo.getExpiredatefrom());//有效期（起始）
                                 tZzZmInfo.setEnd_time(certInfo.getExpiredateto());//有效期（截止）
                                 tZzZmInfo.setDept_name(certInfo.getCertawarddept());//颁发单位
                                 tZzZmInfo.setUser_name(certInfo.getCertownername());//持有人姓名
                                 tZzZmInfo.setUser_type(convertUserType(certInfo.getCertownertype()));//持有者类型(自然人:001,法人或其他组织:002,混合:003,其他:004)
                                 tZzZmInfo.setUser_zj_type(convertUserZJType(certInfo.getCertownercerttype()));//持有人证件类型
                                 tZzZmInfo.setUser_no(certInfo.getCertownerno());//持有人证件号码
                                 tZzZmInfo.setZz_type(certInfo.getCertlevel());//可信等级

                                 // 照面信息XML格式
                                 tZzZmInfo.setIndividuation(generateIndividuation(certInfo));
                                 // 设置证照文件（证照附件正本）
                                 //tZzZmInfo.setZz_file(zzFile);//附件
                                 tZzZmInfo.setCreate_date(new Date());//创造日期
                                 // 有效
                                 tZzZmInfo.setState(CertConstant.ZZK_PUBLISH_STATE_USE);//A：在用，X：不在用
                                
                                 // 是否更新
                                 if (isAdd) {
                                     tZzZmInfo.setStatus(CertConstant.ZZK_EXCHANGE_STATE_UNEXCHANGED);//状态：新增
                                     syncCommonDao.insert(tZzZmInfo);
                                 } else {
                                     tZzZmInfo.setStatus(CertConstant.ZZK_EXCHANGE_STATE_EXCHANGED);//状态：修改
                                     syncCommonDao.update(tZzZmInfo);
                                 }
                                 // 标记已发布
                                 certInfo.setIspublish(CertConstant.CONSTANT_INT_ONE);
                                 iCertInfo.updateCertInfo(certInfo);
                                 syncCertinfoCount ++;
                             
                            EpointFrameDsManager.commit();
                         } catch (Exception e) {
                             e.printStackTrace();
                             // 标记为同步失败
                             log.error(String.format("证照实例 rowguid = %s同步失败", certInfo.getRowguid()));
                             EpointFrameDsManager.rollback();
                             certInfo.setIspublish(CertConstant.CONSTANT_INT_TWO);
                             EpointFrameDsManager.begin(null);
                             iCertInfo.updateCertInfo(certInfo);
                             EpointFrameDsManager.commit();
                             EpointFrameDsManager.close();
                         }finally {
                             EpointFrameDsManager.close();
                         }
                	}else {
                		 try {
                             EpointFrameDsManager.begin(null);
                             // 是否存在历史版本数据
                             TZzZmInfo tZzZmInfo = getOldVersionCertinfo(certInfo.getCertid());
                             if (tZzZmInfo != null) { 
                            	 tZzZmInfo.setStatus("3");//状态：注销
                            	 tZzZmInfo.setCreate_date(new Date());
                                 tZzZmInfo.setState(CertConstant.ZZK_PUBLISH_STATE_NOT_USE);//A：在用，X：不在用
                                 syncCommonDao.update(tZzZmInfo);
                             } 
                             // 标记已发布
                             certInfo.setIspublish(CertConstant.CONSTANT_INT_ONE);
                             iCertInfo.updateCertInfo(certInfo);
                             cancelCertinfoCount ++;
                            EpointFrameDsManager.commit();
                         } catch (Exception e) {
                             e.printStackTrace();
                             // 标记为同步失败
                             log.error(String.format("证照实例 rowguid = %s同步失败", certInfo.getRowguid()));
                             EpointFrameDsManager.rollback();
                             certInfo.setIspublish(CertConstant.CONSTANT_INT_TWO);
                             EpointFrameDsManager.begin(null);
                             iCertInfo.updateCertInfo(certInfo);
                             EpointFrameDsManager.commit();
                             EpointFrameDsManager.close();
                         }finally {
                             EpointFrameDsManager.close();
                         }
                	}
                   
                }
            }
            log.info(String.format("========证照实例推送服务结束（共%s条证照)========", syncCertinfoCount+"=========注销证照有："+cancelCertinfoCount+"条"));
        }
        catch (Exception e) {
           e.printStackTrace();
        } finally {
            if (syncCommonDao != null) {
                syncCommonDao.close();
            }
        }
    }

    /**
     * 生成证照补充信息
     *
     * @param certInfo
     * @return
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    private byte[] generateIndividuation(CertInfo certInfo) {
        byte[] xmlbyte = null;
        String blankXML = "<License>\r\n" + " <Basic>\r\n" + "  <Field CName=\"电子证照编号\" />\r\n"
                + "  <Field CName=\"证照名称\" />\r\n" + "  <Field CName=\"证照编号\" />\r\n" + "  <Field CName=\"颁证时间\" />\r\n"
                + "  <Field CName=\"有效期（起始）\" />\r\n" + "  <Field CName=\"有效期（截止）\" />\r\n"
                + "  <Field CName=\"颁证单位\" />\r\n" + "  <Field CName=\"持证者\" />\r\n" + "  <Field CName=\"持证者类型\" />\r\n"
                + "  <Field CName=\"持证者证件类型\" />\r\n" + "  <Field CName=\"持证者证件号码\" />\r\n"
                + "  <Field CName=\"证照分类\" />\r\n" + " </Basic>\r\n" + " <Face>\r\n" + " </Face>\r\n" + "</License>";
        Document document = null;
        try {
            document = DocumentHelper.parseText(blankXML);
            if (document != null) {
                Element license = document.getRootElement();
                // basic节点
                Element basic = license.element("Basic");
                List basicfieldList = basic.selectNodes("Field");
                for (int i = 0; i < basicfieldList.size(); i++) {
                    Element node = (Element) basicfieldList.get(i);
                    List<Attribute> listAttr = node.attributes();// 当前节点的所有属性的list
                    for (Attribute attr : listAttr) {// 遍历当前节点的所有属性
                        String name = attr.getName();// 属性名称
                        String value = attr.getValue();// 属性值
                        if ("CName".equals(name)) {
                            switch (value) {
                            case "电子证照编号":
                                node.addAttribute("value", certInfo.getCertinfocode());
                                break;
                            case "证照名称":
                                node.addAttribute("value", certInfo.getCertname());
                                break;
                            case "证照编号":
                                node.addAttribute("value", certInfo.getCertno());
                                break;
                            case "颁证时间":
                                node.addAttribute("value",
                                        EpointDateUtil.convertDate2String(certInfo.getAwarddate(), "yyyy-MM-dd"));
                                break;
                            case "有效期（起始）":
                                node.addAttribute("value",
                                        EpointDateUtil.convertDate2String(certInfo.getExpiredatefrom(), "yyyy-MM-dd"));
                                break;
                            case "有效期（截止）":
                                node.addAttribute("value",
                                        EpointDateUtil.convertDate2String(certInfo.getExpiredateto(), "yyyy-MM-dd"));
                                break;
                            case "颁证单位":
                                node.addAttribute("value", certInfo.getCertawarddept());
                                break;
                            case "持证者":
                                node.addAttribute("value", certInfo.getCertownername());
                                break;
                            case "持证者类型":
                                node.addAttribute("value", certInfo.getCertownertype());
                                break;
                            case "持证者证件类型":
                                node.addAttribute("value", certInfo.getCertownertype());
                                break;
                            case "持证者证件号码":
                                node.addAttribute("value", certInfo.getCertownerno());
                                break;
                            case "证照分类":
                                node.addAttribute("value", certInfo.getCertlevel());
                                break;
                            default:
                                break;
                            }
                        }
                    }
                }

                // Face节点
                Element face = license.element("Face");
                // 获取照面信息
                Map<String, Object> filter = new HashMap<>();
                // 设置基本信息guid
                filter.put("certinfoguid", certInfo.getRowguid());
                CertInfoExtension extension = noSQLSevice.find(CertInfoExtension.class, filter, false);

                if (extension != null && StringUtil.isNotBlank(certInfo.getCertareaguid())) {
                    List<CertMetadata> metadataList = iCertMetaData.selectDispinListByCertguid(certInfo.getCertareaguid());
                    if (ValidateUtil.isNotBlankCollection(metadataList)) {
                        for (CertMetadata metadata : metadataList) {
                            // 是否关联子表
                            if (CertConstant.CONSTANT_STR_ONE.equals(metadata.getIsrelatesubtable())) { // 关联子表
                                // 获取子表数据
                                String subExtension = extension.getStr(metadata.getFieldname());
                                List<Record> subExtensionList = null;
                                try {
                                	// JSON转换出错
                                	subExtensionList = JsonUtil.jsonToList(subExtension, Record.class);
								} catch (Exception e) {
									log.error(String.format("子表JSON转换错误 [certguid = %s, json = %s ", certInfo.getRowguid(), subExtension));
									subExtensionList = new ArrayList<>();
								}
                                Element table = face.addElement("Table");
                                table.addAttribute("CName", metadata.getFieldchinesename());
                                // 添加子记录
                                if (ValidateUtil.isNotBlankCollection(subExtensionList)) {
                                    // 获取子元数据
                                    List<CertMetadata> subMetadataList = iCertMetaData.selectSubDispinListByCertguid(metadata.getRowguid());
                                    for (Record record : subExtensionList) {
                                        Element row = table.addElement("Row");
                                        for (CertMetadata subMetadata : subMetadataList) {
                                            Element field = row.addElement("Field");
                                            field.addAttribute("CName", subMetadata.getFieldchinesename());
                                            field.addAttribute("value", record.get(subMetadata.getFieldname()));
                                        }
                                    }
                                }

                            } else { // 不关联子表
                                if (StringUtil.isBlank(metadata.getParentguid())) { // 非子元数据
                                    Element field = face.addElement("Field");
                                    field.addAttribute("CName", metadata.getFieldchinesename());
                                    field.addAttribute("value", extension.get(metadata.getFieldname()));
                                }
                            }
                        }
                    }
                }
            }
            // 格式化
            OutputFormat format = OutputFormat.createPrettyPrint();
            format.setEncoding("UTF-8");
            StringWriter writer = new StringWriter();
            // 格式化输出流
            XMLWriter xmlWriter = new XMLWriter(writer, format);
            // 将document写入到输出流
            try {
                xmlWriter.write(document);
                xmlWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            String xmlstr = writer.toString();
            xmlbyte = xmlstr.getBytes("UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return xmlbyte;
    }



    /**
     * 获得老版本的推送证照
     *  @param certid 证照版本唯一标识
     *  @return
     */
    private TZzZmInfo getOldVersionCertinfo(String certid) {
        if (StringUtil.isBlank(certid)) {
            return null;
        }
        String sql = "SELECT * FROM t_zz_zm_info WHERE id = ?";
        return syncCommonDao.find(sql, TZzZmInfo.class, certid.replace("-", ""));
    }

    /**
     *  持证者类型转换
     *  @param certownertype 持有人类型
     *  @return
     */
    private String convertUserType(String certownertype) {
        String userType = "";
        if (StringUtil.isNotBlank(certownertype)) {
            userType = certownertype.substring(1);
        }

        return userType;
    }

    /**
     *  持证者证件类型转换
     *  @param certownercerttype 持证者证件类型
     *  @return
     */
    private String convertUserZJType(String certownercerttype) {
        String userZJType = "";
        if (StringUtil.isNotBlank(certownercerttype)) {
        	userZJType = certownercerttype.replace("22", "05").replace("14", "02").replace("16", "01")
        			.replace("24", "06").replace("23", "14").replace("25", "14")
        			.replace("30", "03").replace("31", "14").replace("26", "14");
        }
        return userZJType;
    }
}
