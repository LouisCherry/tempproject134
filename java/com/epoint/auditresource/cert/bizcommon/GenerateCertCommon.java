
package com.epoint.auditresource.cert.bizcommon;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.auditresource.auditrsitembaseinfo.domain.AuditRsItemBaseinfo;
import com.epoint.basic.auditresource.auditrsitembaseinfo.inter.IAuditRsItemBaseinfo;
import com.epoint.basic.auditsp.auditspbusiness.domain.AuditSpBusiness;
import com.epoint.basic.auditsp.auditspbusiness.inter.IAuditSpBusiness;
import com.epoint.basic.auditsp.auditspisubapp.domain.AuditSpISubapp;
import com.epoint.basic.auditsp.auditspisubapp.inter.IAuditSpISubapp;
import com.epoint.basic.auditsp.auditspspgcjsxk.domain.AuditSpSpGcjsxk;
import com.epoint.basic.auditsp.auditspspgcjsxk.inter.IAuditSpSpGcjsxkService;
import com.epoint.basic.auditsp.auditspspjgys.domain.AuditSpSpJgys;
import com.epoint.basic.auditsp.auditspspjgys.inter.IAuditSpSpJgysService;
import com.epoint.basic.auditsp.auditspsplxydghxk.domain.AuditSpSpLxydghxk;
import com.epoint.basic.auditsp.auditspsplxydghxk.inter.IAuditSpSpLxydghxkService;
import com.epoint.basic.auditsp.auditspspsgxk.domain.AuditSpSpSgxk;
import com.epoint.basic.auditsp.auditspspsgxk.inter.IAuditSpSpSgxkService;
import com.epoint.basic.basedata.participantsinfo.api.IParticipantsInfoService;
import com.epoint.basic.basedata.participantsinfo.api.entity.ParticipantsInfo;
import com.epoint.cert.basic.certinfo.certinfoextension.domain.CertInfoExtension;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;

/**
 * 照面信息构造类
 */
public class GenerateCertCommon
{

	private static final long serialVersionUID = 1L;

    public final static char[] upper = "零一二三四五六七八九十".toCharArray();

    public static final String zwdturl = ConfigUtil.getConfigValue("zwdtparam", "zwdturl") + "/rest/jncxcltxzdj/";


    private IParticipantsInfoService iParticipantsInfo = ContainerFactory.getContainInfo().getComponent(IParticipantsInfoService.class);

    private IAuditSpSpSgxkService iAuditSpSpSgxkService= ContainerFactory.getContainInfo().getComponent(IAuditSpSpSgxkService.class);

    private IAuditSpISubapp iAuditSpISubapp= ContainerFactory.getContainInfo().getComponent(IAuditSpISubapp.class);

    private IAuditRsItemBaseinfo iAuditRsItemBaseinfo= ContainerFactory.getContainInfo().getComponent(IAuditRsItemBaseinfo.class);

    private IAuditSpSpLxydghxkService iAuditSpSpLxydghxkService= ContainerFactory.getContainInfo().getComponent(IAuditSpSpLxydghxkService.class);

    private ICodeItemsService iCodeItemsService= ContainerFactory.getContainInfo().getComponent(ICodeItemsService.class);

    private IAuditSpSpGcjsxkService iAuditSpSpGcjsxkService= ContainerFactory.getContainInfo().getComponent(IAuditSpSpGcjsxkService.class);

    private IAuditSpSpJgysService iAuditSpSpJgysService= ContainerFactory.getContainInfo().getComponent(IAuditSpSpJgysService.class);

    private IAuditSpBusiness auditSpBusinessService= ContainerFactory.getContainInfo().getComponent(IAuditSpBusiness.class);


    public String getFlowsn(int var1, int var2, String var3, Object... var4){
        ICommonDao baseDao = CommonDao.getInstance();
        String flowSn = "";
        flowSn = baseDao.executeProcudureWithResult(var1, var2, var3, var4).toString();
        return flowSn;
    }

    /**
     * 用地规划许可
     */
    public void ydghxkz(CertInfoExtension dataBean,String subappguid){
        AuditSpISubapp auditSpISubapp = iAuditSpISubapp
                .getSubappByGuid(subappguid).getResult();
        if (auditSpISubapp != null) {
            AuditRsItemBaseinfo auditRsItemBaseinfo = iAuditRsItemBaseinfo
                    .getAuditRsItemBaseinfoByRowguid(auditSpISubapp.getYewuguid()).getResult();
            Map<String,Object> data = rsitemcommoninfo(auditRsItemBaseinfo,"1",subappguid);
            if(data!=null){
                //编号	bh ；证书编号生成规则：地字第3708（行政区划代码）+2023（年份）+G001（年办件量）。
                //如：地字第37082023G001、002、003顺序递增
                String numberName="用地规划许可";
                String numberFlag="地字第3708";
                Object[] args = new Object[7];
                args[0] = numberName;
                args[1] = numberFlag;
                args[2] = Integer.valueOf(EpointDateUtil.getYearOfDate(new Date()));
                args[3] = 4;
                args[4] = Integer.valueOf(EpointDateUtil.getMonthOfDate(new Date()) + 1);
                args[5] = Integer.valueOf(EpointDateUtil.getDayOfDate(new Date()));
                //流水号长度
                args[6] = 3;
                String bh= getFlowsn(args.length + 1, java.sql.Types.VARCHAR, "Common_Getflowsn_ydghxkz", args);
                dataBean.set("bh",bh);
                //日期	rq
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                dataBean.set("rq",format.format(new Date()));
                //用地单位	yddw
                if(data.containsKey("jsdwname")){
                    dataBean.set("yddw",data.get("jsdwname"));
                }
                //用地项目名称	ydxmmc
                if(data.containsKey("itemname")){
                    dataBean.set("ydxmmc",data.get("itemname"));
                }
                //批准用地文号	pzydwh
                if(data.containsKey("pzydwh")){
                    dataBean.set("pzydwh",data.get("pzydwh"));
                }
                //批准用地机关	pzydjg
                if(data.containsKey("pzydjg")){
                    dataBean.set("pzydjg",data.get("pzydjg"));
                }
                //用地位置	ydwz
                if(data.containsKey("itemAddress")){
                    dataBean.set("ydwz",data.get("itemAddress"));
                }
                //用地面积	ydmj
                if(data.containsKey("areaUsed")){
                    dataBean.set("ydmj",data.get("areaUsed"));
                }
                //土地用途	ydxz
                if(data.containsKey("tdyt")){
                    dataBean.set("ydxz",data.get("tdyt"));
                }
                //建设规模	jsgm
                if(data.containsKey("constructionscaleanddesc")){
                    dataBean.set("jsgm",data.get("constructionscaleanddesc"));
                }
                //土地取得方式	tdqdfs
                if(data.containsKey("areaSources")){
                    dataBean.set("tdqdfs",data.get("areaSources"));
                }

            }
        }
    }

    /**
     * 建设工程规划许可证
     */
    public void jsgcghxk(CertInfoExtension dataBean,String subappguid){
        AuditSpISubapp auditSpISubapp = iAuditSpISubapp
                .getSubappByGuid(subappguid).getResult();
        if (auditSpISubapp != null) {
            AuditRsItemBaseinfo auditRsItemBaseinfo = iAuditRsItemBaseinfo
                    .getAuditRsItemBaseinfoByRowguid(auditSpISubapp.getYewuguid()).getResult();
            Map<String,Object> data = rsitemcommoninfo(auditRsItemBaseinfo,"2",subappguid);
            if(data!=null){
                //建筑工程规划许可证的证书编号生成规则为：建字第37082023G001、建字第37082023G002、建字第37082023G003顺序递增
                String numberName="建设工程规划许可证";
                String numberFlag="建字第3708";
                Object[] args = new Object[7];
                args[0] = numberName;
                args[1] = numberFlag;
                args[2] = Integer.valueOf(EpointDateUtil.getYearOfDate(new Date()));
                args[3] = 4;
                args[4] = Integer.valueOf(EpointDateUtil.getMonthOfDate(new Date()) + 1);
                args[5] = Integer.valueOf(EpointDateUtil.getDayOfDate(new Date()));
                //流水号长度
                args[6] = 3;
                String bh= getFlowsn(args.length + 1, java.sql.Types.VARCHAR, "Common_Getflowsn_ydghxkz", args);
                dataBean.set("bh",bh);
                //日期	rq
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                dataBean.set("rq",format.format(new Date()));
                //建设单位（个人）	jsdw
                if(data.containsKey("jsdwname")){
                    dataBean.set("jsdw",data.get("jsdwname"));
                }
                //建设项目名称	jsxmmc
                if(data.containsKey("itemname")){
                    dataBean.set("jsxmmc",data.get("itemname"));
                }
                //建设位置	jswz
                if(data.containsKey("itemAddress")){
                    dataBean.set("jswz",data.get("itemAddress"));
                }
                //建设规模	jsgm
                if(data.containsKey("constructionscaleanddesc")){
                    dataBean.set("jsgm",data.get("constructionscaleanddesc"));
                }
                //不动产单元号	bdcdyh
                if(data.containsKey("bdcdanyuanhao")){
                    dataBean.set("bdcdyh",data.get("bdcdanyuanhao"));
                }

            }
        }
    }

    /**
     * 建设工程消防设计审查
     */
    public void jsgcxfsjsc(CertInfoExtension dataBean,String subappguid){
        AuditSpISubapp auditSpISubapp = iAuditSpISubapp
                .getSubappByGuid(subappguid).getResult();
        if (auditSpISubapp != null) {
            AuditRsItemBaseinfo auditRsItemBaseinfo = iAuditRsItemBaseinfo
                    .getAuditRsItemBaseinfoByRowguid(auditSpISubapp.getYewuguid()).getResult();
            Map<String,Object> data = rsitemcommoninfo(auditRsItemBaseinfo,"3",subappguid);
            if(data!=null){
                // 如：证书编号：高新住建消凭字（2023）第0001号、高新住建消凭字（2023）第0002号顺序递增。
                //生成规则：高新住建消凭字（单位编码）+2023（年份）+第0001号（四位数的年办件量）。
                //证书编号：高新住建消审字（2023）第0001号、高新住建消凭字（2023）第0002号顺序递增。
                //生成规则：高新住建消审字（单位编码）+2023（年份）+第0001号（四位数的年办件量）
                String numberName="建设工程消防设计审查";
                String numberFlag="高新住建消审字（";
                Object[] args = new Object[8];
                args[0] = numberName;
                args[1] = numberFlag;
                args[2] = Integer.valueOf(EpointDateUtil.getYearOfDate(new Date()));
                args[3] = 4;
                args[4] = Integer.valueOf(EpointDateUtil.getMonthOfDate(new Date()) + 1);
                args[5] = Integer.valueOf(EpointDateUtil.getDayOfDate(new Date()));
                args[6] = "）第";
                //流水号长度
                args[7] = 4;
                String bh= getFlowsn(args.length + 1, java.sql.Types.VARCHAR, "Common_Getflowsn_xfsjsc", args);

                args[1] = "高新住建消凭字（";
                String bh1= getFlowsn(args.length + 1, java.sql.Types.VARCHAR, "Common_Getflowsn_xfsjsc", args);

                dataBean.set("zsbh",bh);
                //申请日期	sqrq
                if(data.containsKey("applydate")){
                    dataBean.set("sqrq",data.get("applydate"));
                }
                //建设项目名称	xmmc
                if(data.containsKey("itemname")){
                    dataBean.set("xmmc",data.get("itemname"));
                }
                //建设位置	xmdz
                if(data.containsKey("itemAddress")){
                    dataBean.set("xmdz",data.get("itemAddress"));
                }
                //建筑面积	jzmj
                if(data.containsKey("allBuildArea")){
                    dataBean.set("jzmj",data.get("allBuildArea"));
                }
                //施工图技术性审查报告编号	sgcscbh
                if(data.containsKey("sgtsxmbh")){
                    dataBean.set("sgcscbh",data.get("sgtsxmbh"));
                }
                //申请受理凭证文号	sqwh
                dataBean.set("sqwh",bh1);
                //发证日期	fzrq
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                dataBean.set("fzrq",format.format(new Date()));

                //建设单位	jsdw
                if(data.containsKey("jsdwname")){
                    dataBean.set("jsdw",data.get("jsdwname"));
                }
            }
        }
    }

    /**
     * 建筑工程施工许可证核发
     */
    public void jzgcsgxkz(CertInfoExtension dataBean,String subappguid,String areacode){
        AuditSpISubapp auditSpISubapp = iAuditSpISubapp
                .getSubappByGuid(subappguid).getResult();
        if (auditSpISubapp != null) {
            AuditRsItemBaseinfo auditRsItemBaseinfo = iAuditRsItemBaseinfo
                    .getAuditRsItemBaseinfoByRowguid(auditSpISubapp.getYewuguid()).getResult();
            Map<String,Object> data = rsitemcommoninfo(auditRsItemBaseinfo,"3",subappguid);
            if(data!=null){
                // 1）如：证书编号：370890202301010101、370890202301010201、370890202301010301第15-16位要根据当日发放该证照的数量顺序递增。
                //生成规则：370890（行政区划代码）+2023（当前年份）+01（当前月份）01（当前日）+15-16位为当日办件发放证照数量递增+17-18位为工程序列码+01（房屋建筑工程）。
                //（2）如：证书编号：370890202301010102、370890202301010202、370890202301010302第15-16位要根据当日发放该证照的数量顺序递增。
                //生成规则：370890（行政区划代码）+2023（当前年份）+01（当前月份）01（当前日）+15-16位为当日办件发放证照数量递增+17-18位为工程序列码+02（市政工程）
                //如：地字第37082023G001、002、003顺序递增
                String numberName="建设工程规划许可证";
                String numberFlag=areacode;
                Object[] args = new Object[8];
                args[0] = numberName;
                args[1] = numberFlag;
                args[2] = Integer.valueOf(EpointDateUtil.getYearOfDate(new Date()));
                args[3] = 4;
                args[4] = Integer.valueOf(EpointDateUtil.getMonthOfDate(new Date()) + 1);
                args[5] = Integer.valueOf(EpointDateUtil.getDayOfDate(new Date()));
                args[6] = "";
                AuditSpBusiness auditBusiness = auditSpBusinessService.getAuditSpBusinessByRowguid(auditSpISubapp.getBusinessguid())
                        .getResult();
                if(auditBusiness!=null){
                    if(auditBusiness.getBusinessname().contains("市政")){
                        args[6] = "02";
                    }else{
                        args[6] = "01";
                    }
                }

                //流水号长度
                args[7] = 2;
                String bh= getFlowsn(args.length + 1, java.sql.Types.VARCHAR, "Common_Getflowsn_sgxkz", args);
                dataBean.set("bh",bh);
                Calendar calendar = Calendar.getInstance();
                //年	yymmdd
                dataBean.set("yymmdd",calendar.get(Calendar.YEAR));
                //月	mm
                dataBean.set("mm",calendar.get(Calendar.MONTH)+1);
                //日	dd
                dataBean.set("dd",calendar.get(Calendar.DATE));
                //建设单位	jianshedanweimingchen
                if(data.containsKey("jsdwname")){
                    dataBean.set("jianshedanweimingchen",data.get("jsdwname"));
                }
                //工程名称	xiangmumingchen
                if(data.containsKey("itemname")){
                    dataBean.set("xiangmumingchen",data.get("itemname"));
                }
                //建设地址	xiangmudidian
                if(data.containsKey("itemAddress")){
                    dataBean.set("xiangmudidian",data.get("itemAddress"));
                }
                //建设规模	jiansheguimo
                if(data.containsKey("constructionscaleanddesc")){
                    dataBean.set("jiansheguimo",data.get("constructionscaleanddesc"));
                }
                //合同价格	hetongjiagewanyuan
                if(data.containsKey("htjg")){
                    dataBean.set("hetongjiagewanyuan",data.get("htjg"));
                }
                //勘查单位	kanchadanwei
                if(data.containsKey("kcdwname")){
                    dataBean.set("kanchadanwei",data.get("kcdwname"));
                }
                //设计单位	shejidanwei
                if(data.containsKey("sjdwname")){
                    dataBean.set("shejidanwei",data.get("sjdwname"));
                }
                //施工单位	shigongzongchengbaoqiye
                if(data.containsKey("sgdwname")){
                    dataBean.set("shigongzongchengbaoqiye",data.get("sgdwname"));
                }
                //监理单位	jianlidanwei
                if(data.containsKey("jldwname")){
                    dataBean.set("jianlidanwei",data.get("jldwname"));
                }
                //建设负责人	jianshedanweixiangmufuzeren
                if(data.containsKey("jsdwfzr")){
                    dataBean.set("jianshedanweixiangmufuzeren",data.get("jsdwfzr"));
                }
                //勘查负责人	kanchadanweixiangmufuzeren
                if(data.containsKey("kcdwfzr")){
                    dataBean.set("kanchadanweixiangmufuzeren",data.get("kcdwfzr"));
                }
                //设计负责人	shejidanweixiangmufuzeren
                if(data.containsKey("sjdwfzr")){
                    dataBean.set("shejidanweixiangmufuzeren",data.get("sjdwfzr"));
                }
                //施工负责人	shigongzongchengbaoqiyexiangmu
                if(data.containsKey("sgdwfzr")){
                    dataBean.set("shigongzongchengbaoqiyexiangmu",data.get("sgdwfzr"));
                }
                //不动产单元号	bdcdyh
                if(data.containsKey("bdcdanyuanhao")){
                    dataBean.set("bdcdyh",data.get("bdcdanyuanhao"));
                }

                //发证日期-------fzrq（自动生成，默认当天）
                dataBean.set("fzrq",EpointDateUtil.convertDate2String(new Date(), "yyyy年MM月dd日"));

                //合同工期-------htgq（表单字段，见下图）
                if(data.containsKey("htgq")){
                    dataBean.set("htgq",data.get("htgq"));
                }
                
                //开始工期、结束工期
                if(data.containsKey("hetongkaigongriqi")){
                    dataBean.set("hetongkaigongriqi",data.get("hetongkaigongriqi"));
                }
                if(data.containsKey("hetongjungongriqi")){
                    dataBean.set("hetongjungongriqi",data.get("hetongjungongriqi"));
                }
            }
        }
    }

    /**
     * 根据项目信息，字段一一匹配证照照面信息
     */
    public Map<String,Object> rsitemcommoninfo(AuditRsItemBaseinfo auditRsItemBaseinfo,String phaseId,String subappguid){
        Map<String,Object> data = new HashMap<>();
        if (auditRsItemBaseinfo != null) {
            //获取基本信息
            //建设规模
            data.put("constructionscaleanddesc",auditRsItemBaseinfo.getConstructionscaleanddesc());

            //获取阶段信息
            switch (phaseId) {
                case "1": // 第一阶段
                    getLxydghxkInfo(data,subappguid);
                    break;
                case "2": // 第二阶段
                    getGcjsxkInfo(data,subappguid);
                    break;
                case "3": // 第三阶段
                    getSgxkInfo(data,subappguid);
                    break;
                case "4": // 第四阶段
                    getLxydghxkInfo(data,subappguid);
                    break;
            }


            //获取单位信息
            //获取建设单位，只有一条
            List<ParticipantsInfo> jsdws = getDanwei(auditRsItemBaseinfo.getParentid(),"31");
            if(jsdws!=null && !jsdws.isEmpty()){
                data.put("jsdwname",jsdws.get(0).getCorpname());
                data.put("jsdwfzr",jsdws.get(0).getXmfzr());
            }
            //获取勘查单位，负责人
            List<ParticipantsInfo> kcdws = getDanwei(auditRsItemBaseinfo.getParentid(),"1");
            if(kcdws!=null && !kcdws.isEmpty()){
                data.put("kcdwname",kcdws.get(0).getCorpname());
                data.put("kcdwfzr",kcdws.get(0).getXmfzr());
            }
            //获取设计单位，负责人
            List<ParticipantsInfo> sjdws = getDanwei(auditRsItemBaseinfo.getParentid(),"2");
            if(sjdws!=null && !sjdws.isEmpty()){
                data.put("sjdwname",sjdws.get(0).getCorpname());
                data.put("sjdwfzr",sjdws.get(0).getXmfzr());
            }
            //获取施工单位，负责人
            List<ParticipantsInfo> sgdws = getDanwei(auditRsItemBaseinfo.getParentid(),"3");
            if(sgdws!=null && !sgdws.isEmpty()){
                data.put("sgdwname",sgdws.get(0).getCorpname());
                data.put("sgdwfzr",sgdws.get(0).getXmfzr());
            }
            //获取监理单位，负责人
            List<ParticipantsInfo> jldws = getDanwei(auditRsItemBaseinfo.getParentid(),"4");
            if(jldws!=null && !jldws.isEmpty()){
                data.put("jldwname",jldws.get(0).getCorpname());
                data.put("jldwfzr",jldws.get(0).getXmfzr());
            }
        }
        return data;
    }

    /**
     *  获得单位信息
     * @param itemguid
     * @param type jsdw:31; sgdw:3;sjdw:2;kcdw:1;jldw:4;jcdw:10
     * @return
     */
    public List<ParticipantsInfo>getDanwei(String itemguid,String type){
        //单位信息
        SqlConditionUtil sqlParticipantsInfo = new SqlConditionUtil();
        sqlParticipantsInfo.eq("itemguid", itemguid);
        sqlParticipantsInfo.eq("corptype", type);
        sqlParticipantsInfo.setSelectFields(
                "distinct corptype, corpname, corpcode,xmfzr");
        List<ParticipantsInfo> list = iParticipantsInfo
                .getParticipantsInfoListByCondition(sqlParticipantsInfo.getMap());
        return list;
    }

    /**
     * 加载第一阶段信息
     */
    private void getLxydghxkInfo(Map<String,Object> data,String subappguid) {
        JSONObject dataJson = new JSONObject();
        dataJson.put("phasenum", "lxydghxk");
        AuditSpSpLxydghxk auditSpSpLxydghxk = iAuditSpSpLxydghxkService.findAuditSpSpLxydghxkBysubappguid(subappguid);
        if (auditSpSpLxydghxk != null) {
            dataJson.put("itemname", auditSpSpLxydghxk.getItemname());// 项目名称
            dataJson.put("itemAddress", auditSpSpLxydghxk.getItemaddress()); // 项目地址
            dataJson.put("areaSources", getSelectItemList("土地来源", auditSpSpLxydghxk.getAreasources())); // 土地来源
            dataJson.put("areaUsed", auditSpSpLxydghxk.getAreaused());// 用地面积(公顷)
            //批准用地机关
            dataJson.put("pzydjg", auditSpSpLxydghxk.getStr("pzydjg"));
            //批准用地文号
            dataJson.put("pzydwh", auditSpSpLxydghxk.getStr("pzydwh"));
            //土地用途
            dataJson.put("tdyt", auditSpSpLxydghxk.getStr("tdyt"));
            //建设规模
            dataJson.put("yjsgm", auditSpSpLxydghxk.getStr("jsgm"));

            //注释的内容按需放开
//            dataJson.put("subappname", auditSpSpLxydghxk.getSubappname()); // 子申报名称
//            dataJson.put("itemcode", auditSpSpLxydghxk.getItemcode()); // 投资平台项目代码
//            dataJson.put("projectLevel", getSelectItemList("项目等级", auditSpSpLxydghxk.getProjectlevel())); // 重点项目
//            dataJson.put("area", auditSpSpLxydghxk.getArea()); // 区（园区）
//            dataJson.put("road", auditSpSpLxydghxk.getRoad()); // 街道
//            dataJson.put("eastToArea", auditSpSpLxydghxk.getEasttoarea()); // 四至范围（东至）
//            dataJson.put("westToArea", auditSpSpLxydghxk.getWesttoarea()); // 四至范围（西至）
//            dataJson.put("southToArea", auditSpSpLxydghxk.getSouthtoarea()); // 四至范围（南至）
//            dataJson.put("northToArea", auditSpSpLxydghxk.getNorthtoarea()); // 四至范围（北至）
//            dataJson.put("proTyped", getSelectItemList("立项类型", auditSpSpLxydghxk.getProtyped()));// 立项类型
//            dataJson.put("proOu", auditSpSpLxydghxk.getProou()); // 立项部门
//            dataJson.put("investTyped", getSelectItemList("投资类型", auditSpSpLxydghxk.getInvesttyped()));// 投资类型
//            dataJson.put("hangyeType", auditSpSpLxydghxk.getHangyetype()); // 行业类别
//            dataJson.put("moneySources", getSelectItemList("资金来源", auditSpSpLxydghxk.getMoneysources())); // 资金来源
//            dataJson.put("allMoney", auditSpSpLxydghxk.getAllmoney()); // 总投资
//            dataJson.put("useLandType", auditSpSpLxydghxk.getUselandtype()); // 规划用地性质
//            dataJson.put("newLandType", auditSpSpLxydghxk.getNewlandtype()); // 新增用地面积（公顷）
//            dataJson.put("buildProperties", getSelectItemList("建设性质", auditSpSpLxydghxk.getBuildproperties())); // 建设性质
//            dataJson.put("buildType", getSelectItemList("项目类型", auditSpSpLxydghxk.getBuildtype())); // 项目类型
//            dataJson.put("allBuildArea", auditSpSpLxydghxk.getAllbuildarea()); // 总建筑面积（m²）
//            dataJson.put("overLoadArea", auditSpSpLxydghxk.getOverloadarea()); // 地上
//            dataJson.put("downLoadArea", auditSpSpLxydghxk.getDownloadarea()); // 地下
//            dataJson.put("buildContent", auditSpSpLxydghxk.getBuildcontent()); // 主要建设内容和技术指标(包括必要性)
//            dataJson.put("planStartDate", StringUtil.isBlank(auditSpSpLxydghxk.getPlanstartdate()) ? ""
//                    : EpointDateUtil.convertDate2String(auditSpSpLxydghxk.getPlanstartdate(), "yyyy-MM-dd")); // 计划开工日期
//            dataJson.put("planEndDate", StringUtil.isBlank(auditSpSpLxydghxk.getPlanenddate()) ? ""
//                    : EpointDateUtil.convertDate2String(auditSpSpLxydghxk.getPlanenddate(), "yyyy-MM-dd")); // 计划竣工日期
//            dataJson.put("itemSource", getSelectItemList("项目来源", auditSpSpLxydghxk.getStr("itemsource"))); // 项目来源
//            dataJson.put("xznxfl", auditSpSpLxydghxk.getXznxfl()); // 新增年综合能源消费量吨标煤（当量值）
//            dataJson.put("xznydl", auditSpSpLxydghxk.getXznydl()); // 新增年用电量（万千瓦时）
//
//            dataJson.put("xmjsyj", auditSpSpLxydghxk.getStr("xmjsyj"));
//            dataJson.put("xmnxdz", auditSpSpLxydghxk.getStr("xmnxdz"));
//            dataJson.put("nydmj", auditSpSpLxydghxk.getStr("nydmj"));
//            dataJson.put("njsgm", auditSpSpLxydghxk.getStr("njsgm"));
//            dataJson.put("zbj", auditSpSpLxydghxk.getStr("zbj"));
//            dataJson.put("zztz", auditSpSpLxydghxk.getStr("zztz"));

        }

        data.putAll(dataJson);
    }

    /**
     * 加载第二阶段信息
     */
    private void getGcjsxkInfo(Map<String,Object> data,String subappguid) {
        JSONObject dataJson = new JSONObject();
        dataJson.put("phasenum", "gcjsxk");
        AuditSpSpGcjsxk auditSpSpGcjsxk = iAuditSpSpGcjsxkService.findAuditSpSpGcjsxkBySubappGuid(subappguid);
        if (auditSpSpGcjsxk != null) {
            dataJson.put("itemname", auditSpSpGcjsxk.getItemname());// 项目名称
            //不动产单元号
            dataJson.put("bdcdanyuanhao", auditSpSpGcjsxk.get("bdcdanyuanhao"));
            dataJson.put("itemAddress", auditSpSpGcjsxk.getItemaddress()); // 项目地址
            //申请日期

            dataJson.put("applydate", auditSpSpGcjsxk.getOperatedate());
            dataJson.put("allBuildArea", auditSpSpGcjsxk.getAllbuildarea()); // 总建筑面积（m²）


//注释的内容按需放开
//            dataJson.put("subappname", auditSpSpGcjsxk.getSubappname()); // 子申报名称
//            dataJson.put("itemcode", auditSpSpGcjsxk.getItemcode()); // 投资平台项目代码
//            dataJson.put("projectLevel", getSelectItemList("项目等级", auditSpSpGcjsxk.getProjectlevel())); // 重点项目
//
//            dataJson.put("area", auditSpSpGcjsxk.getArea()); // 区（园区）
//            dataJson.put("road", auditSpSpGcjsxk.getRoad()); // 街道
//            dataJson.put("eastToArea", auditSpSpGcjsxk.getEasttoarea()); // 四至范围（东至）
//            dataJson.put("westToArea", auditSpSpGcjsxk.getWesttoarea()); // 四至范围（西至）
//            dataJson.put("southToArea", auditSpSpGcjsxk.getSouthtoarea()); // 四至范围（南至）
//            dataJson.put("northToArea", auditSpSpGcjsxk.getNorthtoarea()); // 四至范围（北至）
//            dataJson.put("proTyped", getSelectItemList("立项类型", auditSpSpGcjsxk.getProtyped()));// 立项类型
//            dataJson.put("proOu", auditSpSpGcjsxk.getProou()); // 立项部门
//            dataJson.put("investTyped", getSelectItemList("投资类型", auditSpSpGcjsxk.getInvesttyped()));// 投资类型
//            dataJson.put("hangyeType", auditSpSpGcjsxk.getHangyetype()); // 行业类别
//            dataJson.put("moneySources", getSelectItemList("资金来源", auditSpSpGcjsxk.getMoneysources())); // 资金来源
//            dataJson.put("allMoney", auditSpSpGcjsxk.getAllmoney()); // 总投资
//            dataJson.put("areaSources", getSelectItemList("土地来源", auditSpSpGcjsxk.getAreasources())); // 土地来源
//            dataJson.put("useLandType", auditSpSpGcjsxk.getUselandtype()); // 规划用地性质
//            dataJson.put("areaUsed", auditSpSpGcjsxk.getAreaused());// 用地面积(公顷)
//            dataJson.put("newLandType", auditSpSpGcjsxk.getNewlandtype()); // 新增用地面积（公顷）
//            dataJson.put("buildProperties", getSelectItemList("建设性质", auditSpSpGcjsxk.getBuildproperties())); // 建设性质
//            dataJson.put("buildType", getSelectItemList("项目类型", auditSpSpGcjsxk.getBuildtype())); // 项目类型
//
//            dataJson.put("overLoadArea", auditSpSpGcjsxk.getOverloadarea()); // 地上
//            dataJson.put("downLoadArea", auditSpSpGcjsxk.getDownloadarea()); // 地下
//            dataJson.put("buildContent", auditSpSpGcjsxk.getBuildcontent()); // 主要建设内容和技术指标(包括必要性)
//            dataJson.put("planStartDate", StringUtil.isBlank(auditSpSpGcjsxk.getPlanstartdate()) ? ""
//                    : EpointDateUtil.convertDate2String(auditSpSpGcjsxk.getPlanstartdate(), "yyyy-MM-dd")); // 计划开工日期
//            dataJson.put("planEndDate", StringUtil.isBlank(auditSpSpGcjsxk.getPlanenddate()) ? ""
//                    : EpointDateUtil.convertDate2String(auditSpSpGcjsxk.getPlanenddate(), "yyyy-MM-dd")); // 计划竣工日期
//            dataJson.put("itemSource", auditSpSpGcjsxk.getItemsource()); // 项目来源
//            dataJson.put("xznxfl", auditSpSpGcjsxk.getXznxfl()); // 新增年综合能源消费量吨标煤（当量值）
//            dataJson.put("xznydl", auditSpSpGcjsxk.getXznydl()); // 新增年用电量（万千瓦时）
//            dataJson.put("rfywzmj", auditSpSpGcjsxk.getRfywzmj()); //应履行的人防义务总面积
//            dataJson.put("sqjsfk", auditSpSpGcjsxk.getSqjsfk()); //申请建设防空地下室面积
//            dataJson.put("dxsly", auditSpSpGcjsxk.getDxsly()); //申请易地建设防空地下室理由
//            dataJson.put("ydjsdxsmj", auditSpSpGcjsxk.getYdjsdxsmj()); //申请易地建设防空地下室面积
//            dataJson.put("ydjszfgzje", auditSpSpGcjsxk.getYdjszfgzje()); //根据易地建设费征收规则计算的缴费金额（万元）
//            dataJson.put("powertime", StringUtil.isBlank(auditSpSpGcjsxk.getDate("powertime")) ? ""
//                    : EpointDateUtil.convertDate2String(auditSpSpGcjsxk.getDate("powertime"), "yyyy-MM-dd")); // 计划开工日期
//            dataJson.put("fkdxsjsjfrq", StringUtil.isBlank(auditSpSpGcjsxk.getDate("fkdxsjsjfrq")) ? ""
//                    : EpointDateUtil.convertDate2String(auditSpSpGcjsxk.getDate("fkdxsjsjfrq"), "yyyy-MM-dd"));
//            dataJson.put("ydjsfjnse", auditSpSpGcjsxk.getStr("ydjsfjnse"));
//            dataJson.put("fhlb", auditSpSpGcjsxk.getStr("fhlb"));
//            dataJson.put("rfsgtscdw", auditSpSpGcjsxk.getStr("rfsgtscdw"));
//            dataJson.put("jjspyjbh", auditSpSpGcjsxk.getStr("jjspyjbh"));
//            dataJson.put("rfgcjzmj", auditSpSpGcjsxk.getStr("rfgcjzmj"));
//
//            dataJson.put("powercap", auditSpSpGcjsxk.getStr("powercap")); // 区（园区）
        }
        data.putAll(dataJson);
    }

    /**
     * 加载第三阶段信息
     */
    private void getSgxkInfo(Map<String,Object> data,String subappguid) {
        JSONObject dataJson = new JSONObject();
        dataJson.put("phasenum", "sgxk");
        AuditSpSpSgxk auditSpSpSgxk = iAuditSpSpSgxkService.findAuditSpSpSgxkBysubappguid(subappguid);
        if (auditSpSpSgxk != null) {
            dataJson.put("itemname", auditSpSpSgxk.getItemname());// 项目名称
            //不动产单元号
            dataJson.put("bdcdanyuanhao", auditSpSpSgxk.get("bdcdanyuanhao"));
            dataJson.put("itemAddress", auditSpSpSgxk.getItemaddress()); // 项目地址
            dataJson.put("htjg", auditSpSpSgxk.getStr("htjg")); // 合同价格（万元）
            dataJson.put("sgtsxmbh", auditSpSpSgxk.getStr("sgtsxmbh")); // 施工图审查项目编号
            //申请日期
            dataJson.put("applydate", auditSpSpSgxk.getOperatedate());
            dataJson.put("allBuildArea", auditSpSpSgxk.getAllbuildarea()); // 总建筑面积（m²）
            dataJson.put("htgq", auditSpSpSgxk.getStr("htgq")); // 合同工期
            dataJson.put("hetongkaigongriqi", auditSpSpSgxk.getDate("htkgrq")==null? ""
                  : EpointDateUtil.convertDate2String(auditSpSpSgxk.getDate("htkgrq"), "yyyy年MM月dd日")); // 开始工期
            dataJson.put("hetongjungongriqi", auditSpSpSgxk.getDate("htjgrq")==null? ""
                    : EpointDateUtil.convertDate2String(auditSpSpSgxk.getDate("htjgrq"), "yyyy年MM月dd日")); // 结束工期
            
//注释的内容按需放开
//            dataJson.put("subappname", auditSpSpSgxk.getSubappname()); // 子申报名称
//            dataJson.put("itemcode", auditSpSpSgxk.getItemcode()); // 投资平台项目代码
//            dataJson.put("projectLevel", getSelectItemList("项目等级", auditSpSpSgxk.getProjectlevel())); // 重点项目
//
//            dataJson.put("area", auditSpSpSgxk.getArea()); // 区（园区）
//            dataJson.put("road", auditSpSpSgxk.getRoad()); // 街道
//            dataJson.put("eastToArea", auditSpSpSgxk.getEasttoarea()); // 四至范围（东至）
//            dataJson.put("westToArea", auditSpSpSgxk.getWesttoarea()); // 四至范围（西至）
//            dataJson.put("southToArea", auditSpSpSgxk.getSouthtoarea()); // 四至范围（南至）
//            dataJson.put("northToArea", auditSpSpSgxk.getNorthtoarea()); // 四至范围（北至）
//            dataJson.put("proTyped", getSelectItemList("立项类型", auditSpSpSgxk.getProtyped()));// 立项类型
//            dataJson.put("proOu", auditSpSpSgxk.getProou()); // 立项部门
//            dataJson.put("investTyped", getSelectItemList("投资类型", auditSpSpSgxk.getInvesttyped()));// 投资类型
//            dataJson.put("hangyeType", auditSpSpSgxk.getHangyetype()); // 行业类别
//            dataJson.put("moneySources", getSelectItemList("资金来源", auditSpSpSgxk.getMoneysources())); // 资金来源
//            dataJson.put("allMoney", auditSpSpSgxk.getAllmoney()); // 总投资
//            dataJson.put("areaSources", getSelectItemList("土地来源", auditSpSpSgxk.getAreasources())); // 土地来源
//            dataJson.put("useLandType", auditSpSpSgxk.getUselandtype()); // 规划用地性质
//            dataJson.put("areaUsed", auditSpSpSgxk.getAreaused());// 用地面积(公顷)
//            dataJson.put("newLandType", auditSpSpSgxk.getNewlandtype()); // 新增用地面积（公顷）
//            dataJson.put("buildProperties", getSelectItemList("建设性质", auditSpSpSgxk.getBuildproperties())); // 建设性质
//            dataJson.put("buildType", getSelectItemList("项目类型", auditSpSpSgxk.getBuildtype())); // 项目类型
//            dataJson.put("xmfl", getSelectItemList("项目分类", auditSpSpSgxk.getStr("xmfl"))); // 项目类型

//            dataJson.put("overLoadArea", auditSpSpSgxk.getOverloadarea()); // 地上
//            dataJson.put("downLoadArea", auditSpSpSgxk.getDownloadarea()); // 地下
//            dataJson.put("buildContent", auditSpSpSgxk.getBuildcontent()); // 主要建设内容和技术指标(包括必要性)
//            dataJson.put("planStartDate", StringUtil.isBlank(auditSpSpSgxk.getPlanstartdate()) ? ""
//                    : EpointDateUtil.convertDate2String(auditSpSpSgxk.getPlanstartdate(), "yyyy-MM-dd")); // 计划开工日期
//            dataJson.put("planEndDate", StringUtil.isBlank(auditSpSpSgxk.getPlanenddate()) ? ""
//                    : EpointDateUtil.convertDate2String(auditSpSpSgxk.getPlanenddate(), "yyyy-MM-dd")); // 计划竣工日期
//            dataJson.put("lxwh", auditSpSpSgxk.getLxwh()); // 立项文号
//            dataJson.put("lxrq", StringUtil.isBlank(auditSpSpSgxk.getLxrq()) ? ""
//                    : EpointDateUtil.convertDate2String(auditSpSpSgxk.getLxrq(), "yyyy-MM-dd")); // 立项日期
//            dataJson.put("lxjb", auditSpSpSgxk.getLxjb()); // 立项级别
//            dataJson.put("xmbm", auditSpSpSgxk.getXmbm()); // 项目编码
//            dataJson.put("gcyt",  getSelectItemList("工程用途", auditSpSpSgxk.getGcyt())); // 工程用途
//            dataJson.put("gcfl", auditSpSpSgxk.getGcfl()); // 工程分类
//            dataJson.put("zftzgs", auditSpSpSgxk.getZftzgs()); // 政府投资概算
//            dataJson.put("ghyzzpl", auditSpSpSgxk.getGhyzzpl()); // 规划预制装配率
//            dataJson.put("xmbh", auditSpSpSgxk.getStr("xmbh")); // 项目编号
//            dataJson.put("yxsfwjgyh", auditSpSpSgxk.getStr("yxsfwjgyh"));
//            dataJson.put("yxsfwjgzh", auditSpSpSgxk.getStr("yxsfwjgzh"));

//            dataJson.put("xmszsf", auditSpSpSgxk.getStr("xmszsf")); // 项目所在省份
//            dataJson.put("xmszcs", auditSpSpSgxk.getStr("xmszcs")); // 项目所在城市
//            dataJson.put("lxpfjg", auditSpSpSgxk.getStr("lxpfjg")); // 立项批复机关
//            dataJson.put("lxpfsj", StringUtil.isBlank(auditSpSpSgxk.getDate("lxpfsj")) ? ""
//                    : EpointDateUtil.convertDate2String(auditSpSpSgxk.getDate("lxpfsj"), "yyyy-MM-dd")); // 立项批复时间
//            dataJson.put("zbtzsbh", auditSpSpSgxk.getStr("zbtzsbh")); // 中标通知书编号

//            dataJson.put("jsydghxkzbh", auditSpSpSgxk.getStr("jsydghxkzbh")); // 建设用地规划许可证编号
//            dataJson.put("jsgcghxkzbh", auditSpSpSgxk.getStr("jsgcghxkzbh")); // 建设工程规划许可证编号
//            dataJson.put("zcd", auditSpSpSgxk.getStr("zcd")); // 总长度（米）
//            dataJson.put("dscd", auditSpSpSgxk.getStr("dscd")); // 地上长度（米）
//            dataJson.put("dxcd", auditSpSpSgxk.getStr("dxcd")); // 地下长度（米）
//            dataJson.put("kuadu", auditSpSpSgxk.getStr("kuadu")); // 跨度（米）
//            dataJson.put("jsgm", auditSpSpSgxk.getStr("jsgm")); // 建设规模
//
//            dataJson.put("htkgrq", StringUtil.isBlank(auditSpSpSgxk.getDate("htkgrq")) ? ""
//                    : EpointDateUtil.convertDate2String(auditSpSpSgxk.getDate("htkgrq"), "yyyy-MM-dd")); // 合同开工日期
//            dataJson.put("htjgrq", StringUtil.isBlank(auditSpSpSgxk.getDate("htjgrq")) ? ""
//                    : EpointDateUtil.convertDate2String(auditSpSpSgxk.getDate("htjgrq"), "yyyy-MM-dd")); // 合同竣工日期
//            dataJson.put("gctzxz", getSelectItemList("工程投资性质", auditSpSpSgxk.getStr("gctzxz"))); // 工程投资性质
//            dataJson.put("xmszqx", getSelectItemList("所在区县", auditSpSpSgxk.getStr("xmszqx"))); // 所在区县
//            dataJson.put("gcyt", getSelectItemList("工程用途", auditSpSpSgxk.getGcyt())); // 工程用途
//            dataJson.put("Buildproperties", getSelectItemList("建设性质", auditSpSpSgxk.getBuildproperties())); // 建设性质
//            dataJson.put("stdw", auditSpSpSgxk.getStr("stdw")); // 审图单位
//            dataJson.put("stxmfzr", auditSpSpSgxk.getStr("stxmfzr")); // 审图单位项目负责人
//            dataJson.put("stdwtyshxydm", auditSpSpSgxk.getStr("stdwtyshxydm")); // 审图单位统一社会信用代码
//            if ("1".equals(hasjianyi)) {
//                dataJson.put("jiansheleixing", auditSpSpSgxk.getStr("jiansheleixing"));
//                dataJson.put("xiangmudanwei", auditSpSpSgxk.getStr("xiangmudanwei"));
//                dataJson.put("tongyishehuixinyong", auditSpSpSgxk.getStr("tongyishehuixinyong"));
//                dataJson.put("farenleixin", getSelectItemList("简易法人类型", auditSpSpSgxk.getStr("farenleixin")));
//                dataJson.put("fadingdaibiaoren", auditSpSpSgxk.getStr("fadingdaibiaoren"));
//                dataJson.put("tongxundizhi", auditSpSpSgxk.getStr("tongxundizhi"));
//                dataJson.put("youzhengbianma", auditSpSpSgxk.getStr("youzhengbianma"));
//                dataJson.put("xiangmufuzeren", auditSpSpSgxk.getStr("xiangmufuzeren"));
//                dataJson.put("xmlianxidianhua", auditSpSpSgxk.getStr("xmlianxidianhua"));
//                dataJson.put("shouquanshenbaoren", auditSpSpSgxk.getStr("shouquanshenbaoren"));
//                dataJson.put("sqlianxidianhua", auditSpSpSgxk.getStr("sqlianxidianhua"));
//                dataJson.put("shenfenzhengtype", getSelectItemList("简易身份证类型", auditSpSpSgxk.getStr("shenfenzhengtype")));
//                dataJson.put("shenfenzhenghaoma", auditSpSpSgxk.getStr("shenfenzhenghaoma"));
//            }

        }
        data.putAll(dataJson);
    }

    /**
     * 加载第四阶段信息
     */
    private void getJgysInfo(Map<String,Object> data,String subappguid) {
        JSONObject dataJson = new JSONObject();
        dataJson.put("phasenum", "jgys");
        AuditSpSpJgys auditSpSpJgys = iAuditSpSpJgysService.findAuditSpSpJgysBySubappGuid(subappguid);
        if (auditSpSpJgys != null) {
            dataJson.put("itemname", auditSpSpJgys.getItemname());// 项目名称
            dataJson.put("subappname", auditSpSpJgys.getSubappname()); // 子申报名称
            dataJson.put("itemcode", auditSpSpJgys.getItemcode()); // 投资平台项目代码
            dataJson.put("projectLevel", getSelectItemList("项目等级", auditSpSpJgys.getProjectlevel())); // 重点项目
            dataJson.put("itemAddress", auditSpSpJgys.getItemaddress()); // 项目地址
            dataJson.put("area", auditSpSpJgys.getArea()); // 区（园区）
            dataJson.put("road", auditSpSpJgys.getRoad()); // 街道
            dataJson.put("eastToArea", auditSpSpJgys.getEasttoarea()); // 四至范围（东至）
            dataJson.put("westToArea", auditSpSpJgys.getWesttoarea()); // 四至范围（西至）
            dataJson.put("southToArea", auditSpSpJgys.getSouthtoarea()); // 四至范围（南至）
            dataJson.put("northToArea", auditSpSpJgys.getNorthtoarea()); // 四至范围（北至）
            dataJson.put("proTyped", getSelectItemList("立项类型", auditSpSpJgys.getProtyped()));// 立项类型
            dataJson.put("proOu", auditSpSpJgys.getProou()); // 立项部门
            dataJson.put("investTyped", getSelectItemList("投资类型", auditSpSpJgys.getInvesttyped()));// 投资类型
            dataJson.put("hangyeType", auditSpSpJgys.getHangyetype()); // 行业类别
            dataJson.put("moneySources", getSelectItemList("资金来源", auditSpSpJgys.getMoneysources())); // 资金来源
            dataJson.put("allMoney", auditSpSpJgys.getAllmoney()); // 总投资
            dataJson.put("areaSources", getSelectItemList("土地来源", auditSpSpJgys.getAreasources())); // 土地来源
            dataJson.put("useLandType", auditSpSpJgys.getUselandtype()); // 规划用地性质
            dataJson.put("areaUsed", auditSpSpJgys.getAreaused());// 用地面积(公顷)
            dataJson.put("newLandType", auditSpSpJgys.getNewlandtype()); // 新增用地面积（公顷）
            dataJson.put("buildProperties", getSelectItemList("建设性质", auditSpSpJgys.getBuildproperties())); // 建设性质
            dataJson.put("buildType", getSelectItemList("项目类型", auditSpSpJgys.getBuildtype())); // 项目类型
            dataJson.put("allBuildArea", auditSpSpJgys.getAllbuildarea()); // 总建筑面积（m²）
            dataJson.put("overLoadArea", auditSpSpJgys.getOverloadarea()); // 地上
            dataJson.put("downLoadArea", auditSpSpJgys.getDownloadarea()); // 地下
            dataJson.put("buildContent", auditSpSpJgys.getBuildcontent()); // 主要建设内容和技术指标(包括必要性)
            dataJson.put("planStartDate", StringUtil.isBlank(auditSpSpJgys.getPlanstartdate()) ? ""
                    : EpointDateUtil.convertDate2String(auditSpSpJgys.getPlanstartdate(), "yyyy-MM-dd")); // 计划开工日期
            dataJson.put("planEndDate", StringUtil.isBlank(auditSpSpJgys.getPlanenddate()) ? ""
                    : EpointDateUtil.convertDate2String(auditSpSpJgys.getPlanenddate(), "yyyy-MM-dd")); // 计划竣工日期
            dataJson.put("gcmc", auditSpSpJgys.getGcmc()); // 工程名称
            dataJson.put("lxjb", auditSpSpJgys.getLxjb()); // 立项级别
            dataJson.put("jzgd", auditSpSpJgys.getJzgd()); // 建筑高度
            dataJson.put("jclb", auditSpSpJgys.getJclb()); // 基础类别
            dataJson.put("startDate", StringUtil.isBlank(auditSpSpJgys.getStartdate()) ? ""
                    : EpointDateUtil.convertDate2String(auditSpSpJgys.getStartdate(), "yyyy-MM-dd")); // 开工日期
            dataJson.put("endDate", StringUtil.isBlank(auditSpSpJgys.getEnddate()) ? ""
                    : EpointDateUtil.convertDate2String(auditSpSpJgys.getEnddate(), "yyyy-MM-dd")); // 竣工日期
            dataJson.put("bdh", auditSpSpJgys.getBdh()); // 标段号
            dataJson.put("jzzh", auditSpSpJgys.getJzzh()); // 桩号
            dataJson.put("gcgm", auditSpSpJgys.getGcgm()); // 工程规模
            dataJson.put("gczj", auditSpSpJgys.getGczj()); // 工程造价(万元)
            dataJson.put("rfjzmj", auditSpSpJgys.getRfjzmj()); // 人防建筑面积
            dataJson.put("zxmj", auditSpSpJgys.getZxmj()); // 装修面积
            dataJson.put("dscc", auditSpSpJgys.getDscc()); // 地上层次
            dataJson.put("dxcc", auditSpSpJgys.getDxcc()); // 地下层次
            dataJson.put("nhdj", getSelectItemList("耐火等级", auditSpSpJgys.getNhdj())); // 耐火等级
            dataJson.put("fldj", getSelectItemList("防雷等级", auditSpSpJgys.getFldj())); // 防雷等级
            dataJson.put("gclb", auditSpSpJgys.getGclb()); // 工程类别
            dataJson.put("jglx", auditSpSpJgys.getJglx()); // 结构类型
            dataJson.put("sylx", getSelectItemList("使用类型", auditSpSpJgys.getSylx())); // 使用类型

            if (StringUtil.isBlank(auditSpSpJgys.getStr("applycliengguid"))) {
                dataJson.put("applycliengguid", UUID.randomUUID().toString());
            }else {
                dataJson.put("applycliengguid", auditSpSpJgys.getStr("applycliengguid"));
            }

            if (StringUtil.isBlank(auditSpSpJgys.getStr("yscliengguid"))) {
                dataJson.put("yscliengguid", UUID.randomUUID().toString());
            }else {
                dataJson.put("yscliengguid", auditSpSpJgys.getStr("yscliengguid"));
            }

            if (StringUtil.isBlank(auditSpSpJgys.getStr("lhchcliengguid"))) {
                dataJson.put("lhchcliengguid", UUID.randomUUID().toString());
            }else {
                dataJson.put("lhchcliengguid", auditSpSpJgys.getStr("lhchcliengguid"));
            }


        }
        data.putAll(dataJson);
    }



    public String getSelectItemList(String codeName, String filedValue) {
        return iCodeItemsService.getItemTextByCodeName(codeName,filedValue);
    }
}
