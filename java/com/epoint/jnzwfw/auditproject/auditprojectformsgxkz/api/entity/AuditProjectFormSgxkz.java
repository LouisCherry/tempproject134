package com.epoint.jnzwfw.auditproject.auditprojectformsgxkz.api.entity;

import java.util.Date;
import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;

/**
 * 施工许可证表单实体
 * 
 * @作者  zhaoy
 * @version [版本号, 2019-06-02 15:13:10]
 */
@Entity(table = "audit_project_form_sgxkz", id = "rowguid")
public class AuditProjectFormSgxkz extends BaseEntity implements Cloneable
{
    private static final long serialVersionUID = 1L;

    /**
     * 所属辖区号
     */
    public String getBelongxiaqucode() {
        return super.get("belongxiaqucode");
    }

    public void setBelongxiaqucode(String belongxiaqucode) {
        super.set("belongxiaqucode", belongxiaqucode);
    }

    /**
    * 操作者名字
    */
    public String getOperateusername() {
        return super.get("operateusername");
    }

    public void setOperateusername(String operateusername) {
        super.set("operateusername", operateusername);
    }

    /**
    * 操作日期
    */
    public Date getOperatedate() {
        return super.getDate("operatedate");
    }

    public void setOperatedate(Date operatedate) {
        super.set("operatedate", operatedate);
    }

    /**
    * 序号
    */
    public Integer getRow_id() {
        return super.getInt("row_id");
    }

    public void setRow_id(Integer row_id) {
        super.set("row_id", row_id);
    }

    /**
    * 年份标识
    */
    public String getYearflag() {
        return super.get("yearflag");
    }

    public void setYearflag(String yearflag) {
        super.set("yearflag", yearflag);
    }

    /**
    * 默认主键字段
    */
    public String getRowguid() {
        return super.get("rowguid");
    }

    public void setRowguid(String rowguid) {
        super.set("rowguid", rowguid);
    }

    /**
    * 办件标识
    */
    public String getProjectguid() {
        return super.get("projectguid");
    }

    public void setProjectguid(String projectguid) {
        super.set("projectguid", projectguid);
    }

    /**
    * 表单唯一标识
    */
    public String getForm_id() {
        return super.get("form_id");
    }

    public void setForm_id(String form_id) {
        super.set("form_id", form_id);
    }

    /**
    * 办件流水号
    */
    public String getFlowsn() {
        return super.get("flowsn");
    }

    public void setFlowsn(String flowsn) {
        super.set("flowsn", flowsn);
    }

    /**
    * 电子邮件
    */
    public String getDianziyoujian() {
        return super.get("dianziyoujian");
    }

    public void setDianziyoujian(String dianziyoujian) {
        super.set("dianziyoujian", dianziyoujian);
    }

    /**
    * 工程总承包单位
    */
    public String getGongchengzongchengbaodanwei() {
        return super.get("gongchengzongchengbaodanwei");
    }

    public void setGongchengzongchengbaodanwei(String gongchengzongchengbaodanwei) {
        super.set("gongchengzongchengbaodanwei", gongchengzongchengbaodanwei);
    }
    
    /**
    * 工程总承包单位项目经理
    */
    public String getGongchengzongchengbaodanweixia() {
        return super.get("gongchengzongchengbaodanweixia");
    }

    public void setGongchengzongchengbaodanweixia(String gongchengzongchengbaodanweixia) {
        super.set("gongchengzongchengbaodanweixia", gongchengzongchengbaodanweixia);
    }

    /**
    * 合同价格（万元）
    */
    public String getHetongjiagewanyuan() {
        return super.get("hetongjiagewanyuan");
    }

    public void setHetongjiagewanyuan(String hetongjiagewanyuan) {
        super.set("hetongjiagewanyuan", hetongjiagewanyuan);
    }

    /**
    * 合同竣工日期
    */
    public String getHetongjungongriqi() {
        return super.get("hetongjungongriqi");
    }

    public void setHetongjungongriqi(String hetongjungongriqi) {
        super.set("hetongjungongriqi", hetongjungongriqi);
    }

    /**
    * 合同开工日期
    */
    public String getHetongkaigongriqi() {
        return super.get("hetongkaigongriqi");
    }

    public void setHetongkaigongriqi(String hetongkaigongriqi) {
        super.set("hetongkaigongriqi", hetongkaigongriqi);
    }

    /**
    * 检测单位
    */
    public String getJiancedanwei() {
        return super.get("jiancedanwei");
    }

    public void setJiancedanwei(String jiancedanwei) {
        super.set("jiancedanwei", jiancedanwei);
    }

    /**
    * 检测单位项目负责人
    */
    public String getJianCeDanWeiFuZeRen() {
        return super.get("jiancedanweifuzeren");
    }

    public void setJianCeDanWeiFuZeRen(String jiancedanweifuzeren) {
        super.set("jiancedanweifuzeren", jiancedanweifuzeren);
    }

    /**
    * 监理单位
    */
    public String getJianlidanwei() {
        return super.get("jianlidanwei");
    }

    public void setJianlidanwei(String jianlidanwei) {
        super.set("jianlidanwei", jianlidanwei);
    }

    /**
    * 建设单位地址
    */
    public String getJianshedanweidizhi() {
        return super.get("jianshedanweidizhi");
    }

    public void setJianshedanweidizhi(String jianshedanweidizhi) {
        super.set("jianshedanweidizhi", jianshedanweidizhi);
    }

    /**
    * 建设单位电话
    */
    public String getJianshedanweidianhua() {
        return super.get("jianshedanweidianhua");
    }

    public void setJianshedanweidianhua(String jianshedanweidianhua) {
        super.set("jianshedanweidianhua", jianshedanweidianhua);
    }

    /**
    * 建设单位法定代表人
    */
    public String getJianshedanweifadingdaibiaoren() {
        return super.get("jianshedanweifadingdaibiaoren");
    }

    public void setJianshedanweifadingdaibiaoren(String jianshedanweifadingdaibiaoren) {
        super.set("jianshedanweifadingdaibiaoren", jianshedanweifadingdaibiaoren);
    }

    /**
    * 建设单位名称
    */
    public String getJianshedanweimingchen() {
        return super.get("jianshedanweimingchen");
    }

    public void setJianshedanweimingchen(String jianshedanweimingchen) {
        super.set("jianshedanweimingchen", jianshedanweimingchen);
    }

    /**
    * 建设单位所有制形式
    */
    public String getJianshedanweisuoyouzhixingshi() {
        return super.get("jianshedanweisuoyouzhixingshi");
    }

    public void setJianshedanweisuoyouzhixingshi(String jianshedanweisuoyouzhixingshi) {
        super.set("jianshedanweisuoyouzhixingshi", jianshedanweisuoyouzhixingshi);
    }

    /**
    * 建设单位项目负责人
    */
    public String getJianshedanweixiangmufuzeren() {
        return super.get("jianshedanweixiangmufuzeren");
    }

    public void setJianshedanweixiangmufuzeren(String jianshedanweixiangmufuzeren) {
        super.set("jianshedanweixiangmufuzeren", jianshedanweixiangmufuzeren);
    }

    /**
    * 建设工程规划许可证
    */
    public String getJianshegongchengguihuaxukezhen() {
        return super.get("jianshegongchengguihuaxukezhen");
    }

    public void setJianshegongchengguihuaxukezhen(String jianshegongchengguihuaxukezhen) {
        super.set("jianshegongchengguihuaxukezhen", jianshegongchengguihuaxukezhen);
    }

    /**
    * 建设规模
    */
    public String getJiansheguimo() {
        return super.get("jiansheguimo");
    }

    public void setJiansheguimo(String jiansheguimo) {
        super.set("jiansheguimo", jiansheguimo);
    }

    /**
    * 建设性质
    */
    public String getJianshexingzhi() {
        return super.get("jianshexingzhi");
    }

    public void setJianshexingzhi(String jianshexingzhi) {
        super.set("jianshexingzhi", jianshexingzhi);
    }

    /**
    * 勘察单位
    */
    public String getKanchadanwei() {
        return super.get("kanchadanwei");
    }

    public void setKanchadanwei(String kanchadanwei) {
        super.set("kanchadanwei", kanchadanwei);
    }

    /**
    * 勘察单位项目负责人
    */
    public String getKanchadanweixiangmufuzeren() {
        return super.get("kanchadanweixiangmufuzeren");
    }

    public void setKanchadanweixiangmufuzeren(String kanchadanweixiangmufuzeren) {
        super.set("kanchadanweixiangmufuzeren", kanchadanweixiangmufuzeren);
    }

    /**
    * 立项批复时间
    */
    public Date getLixiangpifushijian() {
        return super.getDate("lixiangpifushijian");
    }

    public void setLixiangpifushijian(Date lixiangpifushijian) {
        super.set("lixiangpifushijian", lixiangpifushijian);
    }

    /**
    * 联系人
    */
    public String getLianxiren() {
        return super.get("lianxiren");
    }

    public void setLianxiren(String lianxiren) {
        super.set("lianxiren", lianxiren);
    }

    /**
    * 全过程工程咨询企业
    */
    public String getQuanguochenggongchengzixunqiye() {
        return super.get("quanguochenggongchengzixunqiye");
    }

    public void setQuanguochenggongchengzixunqiye(String quanguochenggongchengzixunqiye) {
        super.set("quanguochenggongchengzixunqiye", quanguochenggongchengzixunqiye);
    }

    /**
    * 设计单位
    */
    public String getShejidanwei() {
        return super.get("shejidanwei");
    }

    public void setShejidanwei(String shejidanwei) {
        super.set("shejidanwei", shejidanwei);
    }

    /**
    * 设计单位项目负责人
    */
    public String getShejidanweixiangmufuzeren() {
        return super.get("shejidanweixiangmufuzeren");
    }

    public void setShejidanweixiangmufuzeren(String shejidanweixiangmufuzeren) {
        super.set("shejidanweixiangmufuzeren", shejidanweixiangmufuzeren);
    }

    /**
    * 审图单位
    */
    public String getShentudanwei() {
        return super.get("shentudanwei");
    }

    public void setShentudanwei(String shentudanwei) {
        super.set("shentudanwei", shentudanwei);
    }

    /**
    * 审图单位项目负责人
    */
    public String getShentudanweixiangmufuzeren() {
        return super.get("shentudanweixiangmufuzeren");
    }

    public void setShentudanweixiangmufuzeren(String shentudanweixiangmufuzeren) {
        super.set("shentudanweixiangmufuzeren", shentudanweixiangmufuzeren);
    }

    /**
    * 施工总承包企业
    */
    public String getShigongzongchengbaoqiye() {
        return super.get("shigongzongchengbaoqiye");
    }

    public void setShigongzongchengbaoqiye(String shigongzongchengbaoqiye) {
        super.set("shigongzongchengbaoqiye", shigongzongchengbaoqiye);
    }

    /**
    * 施工总承包企业项目负责人
    */
    public String getShigongzongchengbaoqiyexiangmu() {
        return super.get("shigongzongchengbaoqiyexiangmu");
    }

    public void setShigongzongchengbaoqiyexiangmu(String shigongzongchengbaoqiyexiangmu) {
        super.set("shigongzongchengbaoqiyexiangmu", shigongzongchengbaoqiyexiangmu);
    }

    /**
    * 手机号
    */
    public String getShoujihao() {
        return super.get("shoujihao");
    }

    public void setShoujihao(String shoujihao) {
        super.set("shoujihao", shoujihao);
    }

    /**
    * 项目编号
    */
    public String getXiangmubianhao() {
        return super.get("xiangmubianhao");
    }

    public void setXiangmubianhao(String xiangmubianhao) {
        super.set("xiangmubianhao", xiangmubianhao);
    }

    /**
    * 项目代码
    */
    public String getXiangmudaima() {
        return super.get("xiangmudaima");
    }

    public void setXiangmudaima(String xiangmudaima) {
        super.set("xiangmudaima", xiangmudaima);
    }

    /**
    * 项目地点
    */
    public String getXiangmudidian() {
        return super.get("xiangmudidian");
    }

    public void setXiangmudidian(String xiangmudidian) {
        super.set("xiangmudidian", xiangmudidian);
    }

    /**
    * 地上长度（米）
    */
    public String getXiangmudishangchangdumi() {
        return super.get("xiangmudishangchangdumi");
    }

    public void setXiangmudishangchangdumi(String xiangmudishangchangdumi) {
        super.set("xiangmudishangchangdumi", xiangmudishangchangdumi);
    }

    /**
    * 地上建筑面积（平方米）
    */
    public String getXiangmudishangjianzhumianjipin() {
        return super.get("xiangmudishangjianzhumianjipin");
    }

    public void setXiangmudishangjianzhumianjipin(String xiangmudishangjianzhumianjipin) {
        super.set("xiangmudishangjianzhumianjipin", xiangmudishangjianzhumianjipin);
    }

    /**
    * 地下长度（米）
    */
    public String getXiangmudixiachangdumi() {
        return super.get("xiangmudixiachangdumi");
    }

    public void setXiangmudixiachangdumi(String xiangmudixiachangdumi) {
        super.set("xiangmudixiachangdumi", xiangmudixiachangdumi);
    }

    /**
    * 地下建筑面积（平方米）
    */
    public String getXiangmudixiajianzhumianjipingf() {
        return super.get("xiangmudixiajianzhumianjipingf");
    }

    public void setXiangmudixiajianzhumianjipingf(String xiangmudixiajianzhumianjipingf) {
        super.set("xiangmudixiajianzhumianjipingf", xiangmudixiajianzhumianjipingf);
    }

    /**
    * 项目分类
    */
    public String getXiangmufenlei() {
        return super.get("xiangmufenlei");
    }

    public void setXiangmufenlei(String xiangmufenlei) {
        super.set("xiangmufenlei", xiangmufenlei);
    }

    /**
    * 跨度（米）
    */
    public String getXiangmukuadumi() {
        return super.get("xiangmukuadumi");
    }

    public void setXiangmukuadumi(String xiangmukuadumi) {
        super.set("xiangmukuadumi", xiangmukuadumi);
    }

    /**
    * 项目名称
    */
    public String getXiangmumingchen() {
        return super.get("xiangmumingchen");
    }

    public void setXiangmumingchen(String xiangmumingchen) {
        super.set("xiangmumingchen", xiangmumingchen);
    }

    /**
    * 项目所在城市
    */
    public String getXiangmusuozaichengshi() {
        return super.get("xiangmusuozaichengshi");
    }

    public void setXiangmusuozaichengshi(String xiangmusuozaichengshi) {
        super.set("xiangmusuozaichengshi", xiangmusuozaichengshi);
    }

    /**
    * 项目所在区县
    */
    public String getXiangmusuozaiquxian() {
        return super.get("xiangmusuozaiquxian");
    }

    public void setXiangmusuozaiquxian(String xiangmusuozaiquxian) {
        super.set("xiangmusuozaiquxian", xiangmusuozaiquxian);
    }

    /**
    * 项目所在省份
    */
    public String getXiangmusuozaishengfen() {
        return super.get("xiangmusuozaishengfen");
    }

    public void setXiangmusuozaishengfen(String xiangmusuozaishengfen) {
        super.set("xiangmusuozaishengfen", xiangmusuozaishengfen);
    }

    /**
    * （房屋建筑必填）总建筑面积（平方米）
    */
    public String getXiangmuzongjianzhumianjipingfa() {
        return super.get("xiangmuzongjianzhumianjipingfa");
    }

    public void setXiangmuzongjianzhumianjipingfa(String xiangmuzongjianzhumianjipingfa) {
        super.set("xiangmuzongjianzhumianjipingfa", xiangmuzongjianzhumianjipingfa);
    }

    /**
    * 总投资（万元）
    */
    public String getXiangmuzongtouziwanyuan() {
        return super.get("xiangmuzongtouziwanyuan");
    }

    public void setXiangmuzongtouziwanyuan(String xiangmuzongtouziwanyuan) {
        super.set("xiangmuzongtouziwanyuan", xiangmuzongtouziwanyuan);
    }

    /**
    * 重点项目
    */
    public String getZhongdianxiangmu() {
        return super.get("zhongdianxiangmu");
    }

    public void setZhongdianxiangmu(String zhongdianxiangmu) {
        super.set("zhongdianxiangmu", zhongdianxiangmu);
    }

    /**
    * 资金来源
    */
    public String getZijinlaiyuan() {
        return super.get("zijinlaiyuan");
    }

    public void setZijinlaiyuan(String zijinlaiyuan) {
        super.set("zijinlaiyuan", zijinlaiyuan);
    }

    /**
    * 总长度（米）
    */
    public String getZongchangdumi1() {
        return super.get("zongchangdumi1");
    }

    public void setZongchangdumi1(String zongchangdumi1) {
        super.set("zongchangdumi1", zongchangdumi1);
    }

    /**
    * 总监理工程师
    */
    public String getZongjianligongchengshi() {
        return super.get("zongjianligongchengshi");
    }

    public void setZongjianligongchengshi(String zongjianligongchengshi) {
        super.set("zongjianligongchengshi", zongjianligongchengshi);
    }
    
    public String getJianSheDanWeiTongYiSheHuiXinYo() {
        return super.get("JianSheDanWeiTongYiSheHuiXinYo");
    }

    public void setJianSheDanWeiTongYiSheHuiXinYo(String JianSheDanWeiTongYiSheHuiXinYo) {
        super.set("JianSheDanWeiTongYiSheHuiXinYo", JianSheDanWeiTongYiSheHuiXinYo);
    }
    
    public String getLiXiangPiFuJiGuan() {
        return super.get("LiXiangPiFuJiGuan");
    }

    public void setLiXiangPiFuJiGuan(String LiXiangPiFuJiGuan) {
        super.set("LiXiangPiFuJiGuan", LiXiangPiFuJiGuan);
    }

    public String getGongChengYongTu() {
        return super.get("GongChengYongTu");
    }

    public void setGongChengYongTu(String GongChengYongTu) {
        super.set("GongChengYongTu", GongChengYongTu);
    }
    
    public String getZhongBiaoTongZhiShuBianHao() {
        return super.get("ZhongBiaoTongZhiShuBianHao");
    }

    public void setZhongBiaoTongZhiShuBianHao(String ZhongBiaoTongZhiShuBianHao) {
        super.set("ZhongBiaoTongZhiShuBianHao", ZhongBiaoTongZhiShuBianHao);
    }
    
    public String getShiGongTuShenChaXiangMuBianHao() {
        return super.get("ShiGongTuShenChaXiangMuBianHao");
    }

    public void setShiGongTuShenChaXiangMuBianHao(String ShiGongTuShenChaXiangMuBianHao) {
        super.set("ShiGongTuShenChaXiangMuBianHao", ShiGongTuShenChaXiangMuBianHao);
    }
    
    /**
     * 建设用地规划许可证编号
     */
    public String getJianSheGongChengGuiHuaXuKeZhen() {
        return super.get("JianSheGongChengGuiHuaXuKeZhen");
    }

    public void setJianSheGongChengGuiHuaXuKeZhen(String JianSheGongChengGuiHuaXuKeZhen) {
        super.set("JianSheGongChengGuiHuaXuKeZhen", JianSheGongChengGuiHuaXuKeZhen);
    }
    
    /**
     * 施工总承包企业统一社会信用代码
     */
    public String getShiGongZongChengBaoQiYeTongYiS() {
        return super.get("ShiGongZongChengBaoQiYeTongYiS");
    }

    public void setShiGongZongChengBaoQiYeTongYiS(String ShiGongZongChengBaoQiYeTongYiS) {
        super.set("ShiGongZongChengBaoQiYeTongYiS", ShiGongZongChengBaoQiYeTongYiS);
    }
    
    /**
     * 全过程工程咨询企业统一社会信用代码
     */
    public String getQuanGuoChengGongChengZiXunXY() {
        return super.get("QuanGuoChengGongChengZiXunXY");
    }

    public void setQuanGuoChengGongChengZiXunXY(String QuanGuoChengGongChengZiXunXY) {
        super.set("QuanGuoChengGongChengZiXunXY", QuanGuoChengGongChengZiXunXY);
    }
    
    /**
     * 全过程工程咨询企业统一社会信用代码
     */
    public String getQuanGuoChengGongChengZiXunFZR() {
        return super.get("QuanGuoChengGongChengZiXunFZR");
    }

    public void setQuanGuoChengGongChengZiXunFZR(String QuanGuoChengGongChengZiXunFZR) {
        super.set("QuanGuoChengGongChengZiXunFZR", QuanGuoChengGongChengZiXunFZR);
    }
    
    /**
     * 勘察单位统一社会信用代码
     */
    public String getKanChaDanWeiCode() {
        return super.get("KanChaDanWeiCode");
    }

    public void setKanChaDanWeiCode(String KanChaDanWeiCode) {
        super.set("KanChaDanWeiCode", KanChaDanWeiCode);
    }
    
    /**
     * 设计单位统一社会信用代码
     */
    public String getSheJiDanWeiCode() {
        return super.get("SheJiDanWeiCode");
    }

    public void setSheJiDanWeiCode(String SheJiDanWeiCode) {
        super.set("SheJiDanWeiCode", SheJiDanWeiCode);
    }
    
    /**
     * 监理单位统一社会信用代码
     */
    public String getJianLiDanWeiCode() {
        return super.get("JianLiDanWeiCode");
    }

    public void setJianLiDanWeiCode(String JianLiDanWeiCode) {
        super.set("JianLiDanWeiCode", JianLiDanWeiCode);
    }
    
    /**
     * 检测单位统一社会信用代码
     */
    public String getJianCeDanWeiCode() {
        return super.get("JianCeDanWeiCode");
    }

    public void setJianCeDanWeiCode(String JianCeDanWeiCode) {
        super.set("JianCeDanWeiCode", JianCeDanWeiCode);
    }
    
    /**
     * 审图单位统一社会信用代码
     */
    public String getShenTuDanWeiCode() {
        return super.get("ShenTuDanWeiCode");
    }

    public void setShenTuDanWeiCode(String ShenTuDanWeiCode) {
        super.set("ShenTuDanWeiCode", ShenTuDanWeiCode);
    }
    
    /**
     * 工程投资性质
     */
    public String getInvPropertyNum() {
        return super.get("InvPropertyNum");
    }

    public void setInvPropertyNum(String InvPropertyNum) {
        super.set("InvPropertyNum", InvPropertyNum);
    }
    
    /**
     * 合同工期
     */
    public String getBargainDays() {
        return super.get("BargainDays");
    }

    public void setBargainDays(String BargainDays) {
        super.set("BargainDays", BargainDays);
    }
}
