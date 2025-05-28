package com.epoint.basic.auditsp.dantiinfo.entity;

import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;

import java.util.Date;

/**
 * 单体信息表（单体同子单位工程）实体
 *
 * @version [版本号, 2018-09-20 11:09:09]
 * @作者 15754
 */
@Entity(table = "danti_info", id = "rowguid")
public class DantiInfo extends BaseEntity implements Cloneable {
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
     * 工程类别
     */
    public String getGclb() {
        return super.get("gclb");
    }

    public void setGclb(String gclb) {
        super.set("gclb", gclb);
    }

    /**
     * 是否存在人防工程
     */
    public String getSfczrfgc() {
        return super.get("sfczrfgc");
    }

    public void setSfczrfgc(String sfczrfgc) {
        super.set("sfczrfgc", sfczrfgc);
    }

    /**
     * 超限审查
     */
    public String getCxsc() {
        return super.get("cxsc");
    }

    public void setCxsc(String cxsc) {
        super.set("cxsc", cxsc);
    }

    /**
     * 人防情况
     */
    public String getRfqk() {
        return super.get("rfqk");
    }

    public void setRfqk(String rfqk) {
        super.set("rfqk", rfqk);
    }

    /**
     * 工程性质
     */
    public String getGcxz() {
        return super.get("gcxz");
    }

    public void setGcxz(String gcxz) {
        super.set("gcxz", gcxz);
    }

    /**
     * 投资类型
     */
    public String getTzlx() {
        return super.get("tzlx");
    }

    public void setTzlx(String tzlx) {
        super.set("tzlx", tzlx);
    }

    /**
     * 结构形式
     */
    public String getJgxs() {
        return super.get("jgxs");
    }

    public void setJgxs(String jgxs) {
        super.set("jgxs", jgxs);
    }

    /**
     * 住宅面积（平方米）
     */
    public Double getZzmj() {
        return super.getDouble("zzmj");
    }

    public void setZzmj(Double zzmj) {
        super.set("zzmj", zzmj);
    }

    /**
     * 公建面积（平方米）
     */
    public Double getGjmj() {
        return super.getDouble("gjmj");
    }

    public void setGjmj(Double gjmj) {
        super.set("gjmj", gjmj);
    }

    /**
     * 住宅式公寓面积（平方米）
     */
    public Double getZzsgymj() {
        return super.getDouble("zzsgymj");
    }

    public void setZzsgymj(Double zzsgymj) {
        super.set("zzsgymj", zzsgymj);
    }

    /**
     * 公建式公寓面积（平方米）
     */
    public Double getGjsgymj() {
        return super.getDouble("gjsgymj");
    }

    public void setGjsgymj(Double gjsgymj) {
        super.set("gjsgymj", gjsgymj);
    }

    /**
     * 总建筑面积（平方米）
     */
    public Double getZjzmj() {
        return super.getDouble("zjzmj");
    }

    public void setZjzmj(Double zjzmj) {
        super.set("zjzmj", zjzmj);
    }

    /**
     * 半地下建筑面积（平方米）
     */
    public Double getBdxjzmj() {
        return super.getDouble("bdxjzmj");
    }

    public void setBdxjzmj(Double bdxjzmj) {
        super.set("bdxjzmj", bdxjzmj);
    }

    /**
     * 地下车库面积（平方米）
     */
    public Double getDxckmj() {
        return super.getDouble("dxckmj");
    }

    public void setDxckmj(Double dxckmj) {
        super.set("dxckmj", dxckmj);
    }

    /**
     * 地下设备用房（平方米）
     */
    public Double getDxsbyf() {
        return super.getDouble("dxsbyf");
    }

    public void setDxsbyf(Double dxsbyf) {
        super.set("dxsbyf", dxsbyf);
    }

    /**
     * 地下公建面积（平方米）
     */
    public Double getDxgjmj() {
        return super.getDouble("dxgjmj");
    }

    public void setDxgjmj(Double dxgjmj) {
        super.set("dxgjmj", dxgjmj);
    }

    /**
     * 地下人防面积（平方米）
     */
    public Double getDxrfmj() {
        return super.getDouble("dxrfmj");
    }

    public void setDxrfmj(Double dxrfmj) {
        super.set("dxrfmj", dxrfmj);
    }

    /**
     * 项目标识
     */
    public String getProjectguid() {
        return super.get("projectguid");
    }

    public void setProjectguid(String projectguid) {
        super.set("projectguid", projectguid);
    }

    /**
     * 单位工程标识
     */
    public String getGongchengguid() {
        return super.get("gongchengguid");
    }

    public void setGongchengguid(String gongchengguid) {
        super.set("gongchengguid", gongchengguid);
    }

    /**
     * 单体名称
     */
    public String getDantiname() {
        return super.get("dantiname");
    }

    public void setDantiname(String dantiname) {
        super.set("dantiname", dantiname);
    }

    /**
     * 工程总造价
     */
    public Double getPrice() {
        return super.getDouble("price");
    }

    public void setPrice(Double price) {
        super.set("price", price);
    }

    /**
     * 建筑高度
     */
    public Double getJzgd() {
        return super.getDouble("jzgd");
    }

    public void setJzgd(Double jzgd) {
        super.set("jzgd", jzgd);
    }

    /**
     * 地上层数
     */
    public String getDscs() {
        return super.get("dscs");
    }

    public void setDscs(String dscs) {
        super.set("dscs", dscs);
    }

    /**
     * 地下层数
     */
    public String getDxcs() {
        return super.get("dxcs");
    }

    public void setDxcs(String dxcs) {
        super.set("dxcs", dxcs);
    }

    /**
     * 说明
     */
    public String getRemark() {
        return super.get("remark");
    }

    public void setRemark(String remark) {
        super.set("remark", remark);
    }

    /**
     * 防雷类别
     */
    public String getFllb() {
        return super.get("fllb");
    }

    public void setFllb(String fllb) {
        super.set("fllb", fllb);
    }

    /**
     * 装饰装修工程平米造价
     */
    public Double getProjectpricepercent() {
        return super.getDouble("projectpricepercent");
    }

    public void setProjectpricepercent(Double projectpricepercent) {
        super.set("projectpricepercent", projectpricepercent);
    }

    /**
     * 装修面积
     */
    public Double getZhuangxiuarea() {
        return super.getDouble("zhuangxiuarea");
    }

    public void setZhuangxiuarea(Double zhuangxiuarea) {
        super.set("zhuangxiuarea", zhuangxiuarea);
    }

    /**
     * 装修层数
     */
    public Integer getBuildplies() {
        return super.getInt("buildplies");
    }

    public void setBuildplies(Integer buildplies) {
        super.set("buildplies", buildplies);
    }

    /**
     * 工程用途
     */
    public Integer getProjectuseage() {
        return super.getInt("projectuseage");
    }

    public void setProjectuseage(Integer projectuseage) {
        super.set("projectuseage", projectuseage);
    }

    /**
     * 消防设施
     */
    public Integer getFiredevice() {
        return super.getInt("firedevice");
    }

    public void setFiredevice(Integer firedevice) {
        super.set("firedevice", firedevice);
    }

    /**
     * 装饰装修工程造价
     */
    public Double getProjectprice() {
        return super.getDouble("projectprice");
    }

    public void setProjectprice(Double projectprice) {
        super.set("projectprice", projectprice);
    }

    /**
     * 改造类型
     */
    public Integer getChangetype() {
        return super.getInt("changetype");
    }

    public void setChangetype(Integer changetype) {
        super.set("changetype", changetype);
    }

    /**
     * 工程类型
     */
    public String getProjecttype() {
        return super.get("projecttype");
    }

    public void setProjecttype(String projecttype) {
        super.set("projecttype", projecttype);
    }

    /**
     * 檐口高度
     */
    public Double getYkheight() {
        return super.getDouble("ykheight");
    }

    public void setYkheight(Double ykheight) {
        super.set("ykheight", ykheight);
    }

    /**
     * 容积
     */
    public String getCubage() {
        return super.get("cubage");
    }

    public void setCubage(String cubage) {
        super.set("cubage", cubage);
    }

    /**
     * 基础型式
     */
    public Integer getBasictype() {
        return super.getInt("basictype");
    }

    public void setBasictype(Integer basictype) {
        super.set("basictype", basictype);
    }

    /**
     * 地基处理方法
     */
    public Integer getGroundhandletype() {
        return super.getInt("groundhandletype");
    }

    public void setGroundhandletype(Integer groundhandletype) {
        super.set("groundhandletype", groundhandletype);
    }

    /**
     * 抗震设防类别
     */
    public Integer getKztype() {
        return super.getInt("kztype");
    }

    public void setKztype(Integer kztype) {
        super.set("kztype", kztype);
    }

    /**
     * 是否是超低能耗建筑
     */
    public String getIslowenergy() {
        return super.get("islowenergy");
    }

    public void setIslowenergy(String islowenergy) {
        super.set("islowenergy", islowenergy);
    }

    /**
     * 是否采用无粱楼盖
     */
    public String getIshavecap() {
        return super.get("ishavecap");
    }

    public void setIshavecap(String ishavecap) {
        super.set("ishavecap", ishavecap);
    }

    /**
     * 是否采用减隔震技术
     */
    public String getIslowshock() {
        return super.get("islowshock");
    }

    public void setIslowshock(String islowshock) {
        super.set("islowshock", islowshock);
    }

    /**
     * 工程名称
     */
    public String getProjectname() {
        return super.get("projectname");
    }

    public void setProjectname(String projectname) {
        super.set("projectname", projectname);
    }

    /**
     * 是否需要勘察
     */
    public String getIskanchan() {
        return super.get("iskanchan");
    }

    public void setIskanchan(String iskanchan) {
        super.set("iskanchan", iskanchan);
    }

    /**
     * 建筑面积
     */
    public Double getBuildarea() {
        return super.getDouble("buildarea");
    }

    public void setBuildarea(Double buildarea) {
        super.set("buildarea", buildarea);
    }

    /**
     * 跨度
     */
    public String getSpan() {
        return super.get("span");
    }

    public void setSpan(String span) {
        super.set("span", span);
    }

    /**
     * 结构体系
     */
    public Integer getJiegoutx() {
        return super.getInt("jiegoutx");
    }

    public void setJiegoutx(Integer jiegoutx) {
        super.set("jiegoutx", jiegoutx);
    }

    /**
     * 地基基础设计等级
     */
    public Integer getDjdesignleavel() {
        return super.getInt("djdesignleavel");
    }

    public void setDjdesignleavel(Integer djdesignleavel) {
        super.set("djdesignleavel", djdesignleavel);
    }

    /**
     * 场地土类别
     */
    public Integer getGroundtype() {
        return super.getInt("groundtype");
    }

    public void setGroundtype(Integer groundtype) {
        super.set("groundtype", groundtype);
    }

    /**
     * 建筑场地类别
     */
    public Integer getBuildgroundtype() {
        return super.getInt("buildgroundtype");
    }

    public void setBuildgroundtype(Integer buildgroundtype) {
        super.set("buildgroundtype", buildgroundtype);
    }

    /**
     * 基坑类别
     */
    public Integer getJikengtype() {
        return super.getInt("jikengtype");
    }

    public void setJikengtype(Integer jikengtype) {
        super.set("jikengtype", jikengtype);
    }

    /**
     * 抗震设防烈度
     */
    public Integer getKzlevel() {
        return super.getInt("kzlevel");
    }

    public void setKzlevel(Integer kzlevel) {
        super.set("kzlevel", kzlevel);
    }

    /**
     * 是否有大型公共建筑
     */
    public String getIspublicbuilding() {
        return super.get("ispublicbuilding");
    }

    public void setIspublicbuilding(String ispublicbuilding) {
        super.set("ispublicbuilding", ispublicbuilding);
    }

    /**
     * 是否有附建式人防工程
     */
    public String getIsfjsproject() {
        return super.get("isfjsproject");
    }

    public void setIsfjsproject(String isfjsproject) {
        super.set("isfjsproject", isfjsproject);
    }

    /**
     * 是否采用高强钢筋
     */
    public String getIshaverebar() {
        return super.get("ishaverebar");
    }

    public void setIshaverebar(String ishaverebar) {
        super.set("ishaverebar", ishaverebar);
    }

    /**
     * 采暖方式
     */
    public Integer getHeatingtype() {
        return super.getInt("heatingtype");
    }

    public void setHeatingtype(Integer heatingtype) {
        super.set("heatingtype", heatingtype);
    }

    /**
     * 设计非传统水源利用率
     */
    public Double getWateruserate() {
        return super.getDouble("wateruserate");
    }

    public void setWateruserate(Double wateruserate) {
        super.set("wateruserate", wateruserate);
    }

    /**
     * 设计可再生循环建筑材料用量比
     */
    public Double getMaterialsrate() {
        return super.getDouble("materialsrate");
    }

    public void setMaterialsrate(Double materialsrate) {
        super.set("materialsrate", materialsrate);
    }

    /**
     * 房屋权属证明
     */
    public String getHouseprove() {
        return super.get("houseprove");
    }

    public void setHouseprove(String houseprove) {
        super.set("houseprove", houseprove);
    }

    /**
     * 是否涉及结构检测鉴定
     */
    public String getIsconstuctioncheck() {
        return super.get("isconstuctioncheck");
    }

    public void setIsconstuctioncheck(String isconstuctioncheck) {
        super.set("isconstuctioncheck", isconstuctioncheck);
    }

    /**
     * 装修部位
     */
    public String getBuildpart() {
        return super.get("buildpart");
    }

    public void setBuildpart(String buildpart) {
        super.set("buildpart", buildpart);
    }

    /**
     * 是否采用装配式建筑
     */
    public String getIshaveequipment() {
        return super.get("ishaveequipment");
    }

    public void setIshaveequipment(String ishaveequipment) {
        super.set("ishaveequipment", ishaveequipment);
    }

    /**
     * 是否采用BIM技术
     */
    public String getIshavebim() {
        return super.get("ishavebim");
    }

    public void setIshavebim(String ishavebim) {
        super.set("ishavebim", ishavebim);
    }

    /**
     * 耐火等级
     */
    public Integer getFirelevel() {
        return super.getInt("firelevel");
    }

    public void setFirelevel(Integer firelevel) {
        super.set("firelevel", firelevel);
    }

    /**
     * 给水方式
     */
    public Integer getGivewatertype() {
        return super.getInt("givewatertype");
    }

    public void setGivewatertype(Integer givewatertype) {
        super.set("givewatertype", givewatertype);
    }

    /**
     * 空调方式
     */
    public Integer getKongtiaotype() {
        return super.getInt("kongtiaotype");
    }

    public void setKongtiaotype(Integer kongtiaotype) {
        super.set("kongtiaotype", kongtiaotype);
    }

    /**
     * 照明方式
     */
    public Integer getLighttype() {
        return super.getInt("lighttype");
    }

    public void setLighttype(Integer lighttype) {
        super.set("lighttype", lighttype);
    }

    /**
     * 绿色建筑设计标准
     */
    public Integer getGreenbuildingnorm() {
        return super.getInt("greenbuildingnorm");
    }

    public void setGreenbuildingnorm(Integer greenbuildingnorm) {
        super.set("greenbuildingnorm", greenbuildingnorm);
    }

    /**
     * 设计可再生能源利用率
     */
    public Double getRenewableenergy() {
        return super.getDouble("renewableenergy");
    }

    public void setRenewableenergy(Double renewableenergy) {
        super.set("renewableenergy", renewableenergy);
    }

    /**
     * 座
     */
    public Double getZuo() {
        return super.getDouble("zuo");
    }

    public void setZuo(Double zuo) {
        super.set("zuo", zuo);
    }

    /**
     * 采用工艺
     */
    public Integer getUsecraft() {
        return super.getInt("usecraft");
    }

    public void setUsecraft(Integer usecraft) {
        super.set("usecraft", usecraft);
    }

    /**
     * 管线长度
     */
    public Double getLinelength() {
        return super.getDouble("linelength");
    }

    public void setLinelength(Double linelength) {
        super.set("linelength", linelength);
    }

    /**
     * 管径
     */
    public Double getLinedia() {
        return super.getDouble("linedia");
    }

    public void setLinedia(Double linedia) {
        super.set("linedia", linedia);
    }

    /**
     * 日处理量
     */
    public Double getDayhandingnumber() {
        return super.getDouble("dayhandingnumber");
    }

    public void setDayhandingnumber(Double dayhandingnumber) {
        super.set("dayhandingnumber", dayhandingnumber);
    }

    /**
     * 红线宽度
     */
    public Double getRedlinewidth() {
        return super.getDouble("redlinewidth");
    }

    public void setRedlinewidth(Double redlinewidth) {
        super.set("redlinewidth", redlinewidth);
    }

    /**
     * 管线深度
     */
    public Double getLinedepth() {
        return super.getDouble("linedepth");
    }

    public void setLinedepth(Double linedepth) {
        super.set("linedepth", linedepth);
    }

    /**
     * 道路等级
     */
    public Integer getRoadlevel() {
        return super.getInt("roadlevel");
    }

    public void setRoadlevel(Integer roadlevel) {
        super.set("roadlevel", roadlevel);
    }

    /**
     * 道路立交型式
     */
    public Integer getRoadflyovertype() {
        return super.getInt("roadflyovertype");
    }

    public void setRoadflyovertype(Integer roadflyovertype) {
        super.set("roadflyovertype", roadflyovertype);
    }

    /**
     * 道路长度
     */
    public Double getRoadlength() {
        return super.getDouble("roadlength");
    }

    public void setRoadlength(Double roadlength) {
        super.set("roadlength", roadlength);
    }

    /**
     * 道路面积
     */
    public Double getRoadarea() {
        return super.getDouble("roadarea");
    }

    public void setRoadarea(Double roadarea) {
        super.set("roadarea", roadarea);
    }

    /**
     * 道路设计速度
     */
    public Double getRoaddesignspeed() {
        return super.getDouble("roaddesignspeed");
    }

    public void setRoaddesignspeed(Double roaddesignspeed) {
        super.set("roaddesignspeed", roaddesignspeed);
    }

    /**
     * 道路车辆荷载
     */
    public String getRoadcarmax() {
        return super.get("roadcarmax");
    }

    public void setRoadcarmax(String roadcarmax) {
        super.set("roadcarmax", roadcarmax);
    }

    /**
     * 路面结构类型
     */
    public String getRoadstructuretype() {
        return super.get("roadstructuretype");
    }

    public void setRoadstructuretype(String roadstructuretype) {
        super.set("roadstructuretype", roadstructuretype);
    }

    /**
     * 桥梁线路等级
     */
    public Integer getBridgelinelevel() {
        return super.getInt("bridgelinelevel");
    }

    public void setBridgelinelevel(Integer bridgelinelevel) {
        super.set("bridgelinelevel", bridgelinelevel);
    }

    /**
     * 桥梁长度
     */
    public Double getBridgelength() {
        return super.getDouble("bridgelength");
    }

    public void setBridgelength(Double bridgelength) {
        super.set("bridgelength", bridgelength);
    }

    /**
     * 桥梁结构型式
     */
    public Integer getBridgestructuretype() {
        return super.getInt("bridgestructuretype");
    }

    public void setBridgestructuretype(Integer bridgestructuretype) {
        super.set("bridgestructuretype", bridgestructuretype);
    }

    /**
     * 桥梁跨度型式
     */
    public Integer getBridgespantype() {
        return super.getInt("bridgespantype");
    }

    public void setBridgespantype(Integer bridgespantype) {
        super.set("bridgespantype", bridgespantype);
    }

    /**
     * 桥梁面积
     */
    public Double getBridgearea() {
        return super.getDouble("bridgearea");
    }

    public void setBridgearea(Double bridgearea) {
        super.set("bridgearea", bridgearea);
    }

    /**
     * 桥梁上部
     */
    public String getBridgeup() {
        return super.get("bridgeup");
    }

    public void setBridgeup(String bridgeup) {
        super.set("bridgeup", bridgeup);
    }

    /**
     * 桥梁下部
     */
    public String getBridgedown() {
        return super.get("bridgedown");
    }

    public void setBridgedown(String bridgedown) {
        super.set("bridgedown", bridgedown);
    }

    /**
     * 桥梁孔径布置
     */
    public String getBridgeholeset() {
        return super.get("bridgeholeset");
    }

    public void setBridgeholeset(String bridgeholeset) {
        super.set("bridgeholeset", bridgeholeset);
    }

    /**
     * 桥梁斜交角度
     */
    public Double getBridgeangle() {
        return super.getDouble("bridgeangle");
    }

    public void setBridgeangle(Double bridgeangle) {
        super.set("bridgeangle", bridgeangle);
    }

    /**
     * 桥梁洪水频率
     */
    public Double getBridgewaterrate() {
        return super.getDouble("bridgewaterrate");
    }

    public void setBridgewaterrate(Double bridgewaterrate) {
        super.set("bridgewaterrate", bridgewaterrate);
    }

    /**
     * 桥梁航道等级
     */
    public String getBridgeroadlevel() {
        return super.get("bridgeroadlevel");
    }

    public void setBridgeroadlevel(String bridgeroadlevel) {
        super.set("bridgeroadlevel", bridgeroadlevel);
    }

    /**
     * 隧道线路等级
     */
    public Integer getTunnellinelevel() {
        return super.getInt("tunnellinelevel");
    }

    public void setTunnellinelevel(Integer tunnellinelevel) {
        super.set("tunnellinelevel", tunnellinelevel);
    }

    /**
     * 隧道长度
     */
    public Double getTunnellength() {
        return super.getDouble("tunnellength");
    }

    public void setTunnellength(Double tunnellength) {
        super.set("tunnellength", tunnellength);
    }

    /**
     * 隧道面积
     */
    public Double getTunnelarea() {
        return super.getDouble("tunnelarea");
    }

    public void setTunnelarea(Double tunnelarea) {
        super.set("tunnelarea", tunnelarea);
    }

    /**
     * 快速公交系统长度
     */
    public Double getBrtlength() {
        return super.getDouble("brtlength");
    }

    public void setBrtlength(Double brtlength) {
        super.set("brtlength", brtlength);
    }

    /**
     * 电车系统长度
     */
    public Double getTramsystemlength() {
        return super.getDouble("tramsystemlength");
    }

    public void setTramsystemlength(Double tramsystemlength) {
        super.set("tramsystemlength", tramsystemlength);
    }

    /**
     * 公共交通专用道长度
     */
    public Double getBusroadlength() {
        return super.getDouble("busroadlength");
    }

    public void setBusroadlength(Double busroadlength) {
        super.set("busroadlength", busroadlength);
    }

    /**
     * 公共交通场站面积
     */
    public Double getBusstationarea() {
        return super.getDouble("busstationarea");
    }

    public void setBusstationarea(Double busstationarea) {
        super.set("busstationarea", busstationarea);
    }

    /**
     * 公共交通枢纽面积
     */
    public Double getBushubarea() {
        return super.getDouble("bushubarea");
    }

    public void setBushubarea(Double bushubarea) {
        super.set("bushubarea", bushubarea);
    }

    /**
     * 环境卫生工程处理量
     */
    public Double getEnvironmenthandle() {
        return super.getDouble("environmenthandle");
    }

    public void setEnvironmenthandle(Double environmenthandle) {
        super.set("environmenthandle", environmenthandle);
    }

    /**
     * 燃气工程厂站
     */
    public Double getGasprojectnumber() {
        return super.getDouble("gasprojectnumber");
    }

    public void setGasprojectnumber(Double gasprojectnumber) {
        super.set("gasprojectnumber", gasprojectnumber);
    }

    /**
     * 燃气工程管网线路长度
     */
    public Double getGaslinelength() {
        return super.getDouble("gaslinelength");
    }

    public void setGaslinelength(Double gaslinelength) {
        super.set("gaslinelength", gaslinelength);
    }

    /**
     * 燃气工程管网线路设计压力
     */
    public Double getGaslinepressure() {
        return super.getDouble("gaslinepressure");
    }

    public void setGaslinepressure(Double gaslinepressure) {
        super.set("gaslinepressure", gaslinepressure);
    }

    /**
     * 燃气工程管网线路管径
     */
    public Double getGaslinecaliber() {
        return super.getDouble("gaslinecaliber");
    }

    public void setGaslinecaliber(Double gaslinecaliber) {
        super.set("gaslinecaliber", gaslinecaliber);
    }

    /**
     * 热力工程厂站
     */
    public Integer getHeatprojectnumber() {
        return super.getInt("heatprojectnumber");
    }

    public void setHeatprojectnumber(Integer heatprojectnumber) {
        super.set("heatprojectnumber", heatprojectnumber);
    }

    /**
     * 热力工程管网线路长度
     */
    public Double getHeatlinelength() {
        return super.getDouble("heatlinelength");
    }

    public void setHeatlinelength(Double heatlinelength) {
        super.set("heatlinelength", heatlinelength);
    }

    /**
     * 热力工程管网线路热网等级
     */
    public Integer getHeatlinelevel() {
        return super.getInt("heatlinelevel");
    }

    public void setHeatlinelevel(Integer heatlinelevel) {
        super.set("heatlinelevel", heatlinelevel);
    }

    /**
     * 热力工程管网线路管径
     */
    public Double getHeatlinecaliber() {
        return super.getDouble("heatlinecaliber");
    }

    public void setHeatlinecaliber(Double heatlinecaliber) {
        super.set("heatlinecaliber", heatlinecaliber);
    }

    /**
     * 园林绿化面积
     */
    public Double getGreenarea() {
        return super.getDouble("greenarea");
    }

    public void setGreenarea(Double greenarea) {
        super.set("greenarea", greenarea);
    }

    /**
     * 园林绿化水域面积
     */
    public Double getGreenwaterarea() {
        return super.getDouble("greenwaterarea");
    }

    public void setGreenwaterarea(Double greenwaterarea) {
        super.set("greenwaterarea", greenwaterarea);
    }

    /**
     * 园林绿化树木种类
     */
    public Integer getGreentreetype() {
        return super.getInt("greentreetype");
    }

    public void setGreentreetype(Integer greentreetype) {
        super.set("greentreetype", greentreetype);
    }

    /**
     * 园林绿化花木种类
     */
    public Integer getGreenflowertype() {
        return super.getInt("greenflowertype");
    }

    public void setGreenflowertype(Integer greenflowertype) {
        super.set("greenflowertype", greenflowertype);
    }

    /**
     * 园林绿化假山高度
     */
    public Double getGreenrockeryheight() {
        return super.getDouble("greenrockeryheight");
    }

    public void setGreenrockeryheight(Double greenrockeryheight) {
        super.set("greenrockeryheight", greenrockeryheight);
    }

    /**
     * 园林绿化工程造价
     */
    public Double getGreenprice() {
        return super.getDouble("greenprice");
    }

    public void setGreenprice(Double greenprice) {
        super.set("greenprice", greenprice);
    }

    /**
     * 安装工程划分标准
     */
    public String getInstallstandard() {
        return super.get("installstandard");
    }

    public void setInstallstandard(String installstandard) {
        super.set("installstandard", installstandard);
    }

    /**
     * 建筑层数
     */
    public String getJianzhucengshu() {
        return super.get("jianzhucengshu");
    }

    public void setJianzhucengshu(String jianzhucengshu) {
        super.set("jianzhucengshu", jianzhucengshu);
    }

    /**
     * 地上面积
     */
    public String getDishangmianji() {
        return super.get("dishangmianji");
    }

    public void setDishangmianji(String dishangmianji) {
        super.set("dishangmianji", dishangmianji);
    }

    /**
     * 地下面积
     */
    public String getDixiamianji() {
        return super.get("dixiamianji");
    }

    public void setDixiamianji(String dixiamianji) {
        super.set("dixiamianji", dixiamianji);
    }

    /**
     * 地上高度（m）
     */
    public String getDishanggaodu() {
        return super.get("dishanggaodu");
    }

    public void setDishanggaodu(String dishanggaodu) {
        super.set("dishanggaodu", dishanggaodu);
    }

    /**
     * 地下深度（m）
     */
    public String getDixiashendu() {
        return super.get("dixiashendu");
    }

    public void setDixiashendu(String dixiashendu) {
        super.set("dixiashendu", dixiashendu);
    }

    /**
     * 工程地址
     */
    public String getGongchengdizhi() {
        return super.get("gongchengdizhi");
    }

    public void setGongchengdizhi(String gongchengdizhi) {
        super.set("gongchengdizhi", gongchengdizhi);
    }

    /**
     * 阶段唯一标识
     */
    public String getPhaseguid() {
        return super.get("phaseguid");
    }

    public void setPhaseguid(String phaseguid) {
        super.set("phaseguid", phaseguid);
    }

    /**
     * 监督注册号
     */
    public String getJdzch() {
        return super.get("jdzch");
    }

    public void setJdzch(String jdzch) {
        super.set("jdzch", jdzch);
    }

}
