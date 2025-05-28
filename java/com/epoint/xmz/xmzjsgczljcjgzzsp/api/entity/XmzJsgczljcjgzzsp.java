package com.epoint.xmz.xmzjsgczljcjgzzsp.api.entity;
import java.util.Date;
import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;
import com.epoint.core.utils.EnumClazz.StrategyType;

/**
 * 建设工程质量检测机构资质审批表实体
 * 
 * @作者  86177
 * @version [版本号, 2021-05-08 17:01:26]
 */
@Entity(table = "xmz_jsgczljcjgzzsp", id = "rowguid")
public class XmzJsgczljcjgzzsp extends BaseEntity implements Cloneable
{
    private static final long serialVersionUID = 1L;

 /**
  * 所属辖区号
  */
  public  String  getBelongxiaqucode(){ return super.get("belongxiaqucode");}
public void setBelongxiaqucode(String  belongxiaqucode){ super.set("belongxiaqucode",belongxiaqucode);} /**
  * 操作者名字
  */
  public  String  getOperateusername(){ return super.get("operateusername");}
public void setOperateusername(String  operateusername){ super.set("operateusername",operateusername);} /**
  * 操作日期
  */
  public  Date  getOperatedate(){ return super.getDate("operatedate");}
public void setOperatedate(Date  operatedate){ super.set("operatedate",operatedate);} /**
  * 序号
  */
  public  Integer  getRow_id(){ return super.getInt("row_id");}
public void setRow_id(Integer  row_id){ super.set("row_id",row_id);} /**
  * 年份标识
  */
  public  String  getYearflag(){ return super.get("yearflag");}
public void setYearflag(String  yearflag){ super.set("yearflag",yearflag);} /**
  * 默认主键字段
  */
  public  String  getRowguid(){ return super.get("rowguid");}
public void setRowguid(String  rowguid){ super.set("rowguid",rowguid);} /**
  * 技术职务
  */
  public  String  getJszw(){ return super.get("jszw");}
public void setJszw(String  jszw){ super.set("jszw",jszw);} /**
  * 职称（技术）
  */
  public  String  getZcjs(){ return super.get("zcjs");}
public void setZcjs(String  zcjs){ super.set("zcjs",zcjs);} /**
  * 在编人员总数
  */
  public  String  getZbryzs(){ return super.get("zbryzs");}
public void setZbryzs(String  zbryzs){ super.set("zbryzs",zbryzs);} /**
  * 技术性质
  */
  public  String  getJsxz(){ return super.get("jsxz");}
public void setJsxz(String  jsxz){ super.set("jsxz",jsxz);} /**
  * 联系电话
  */
  public  String  getLxdh(){ return super.get("lxdh");}
public void setLxdh(String  lxdh){ super.set("lxdh",lxdh);} /**
  * 房屋建筑面积（m2）
  */
  public  String  getFwjzmj(){ return super.get("fwjzmj");}
public void setFwjzmj(String  fwjzmj){ super.set("fwjzmj",fwjzmj);} /**
  * 中级职称人数
  */
  public  String  getZjzcrs(){ return super.get("zjzcrs");}
public void setZjzcrs(String  zjzcrs){ super.set("zjzcrs",zjzcrs);} /**
  * 专业技术人员
  */
  public  String  getZyjsry(){ return super.get("zyjsry");}
public void setZyjsry(String  zyjsry){ super.set("zyjsry",zyjsry);} /**
  * 仪器设备总台（套）数
  */
  public  String  getYqsbzs(){ return super.get("yqsbzs");}
public void setYqsbzs(String  yqsbzs){ super.set("yqsbzs",yqsbzs);} /**
  * 工作面积（m2）
  */
  public  String  getGzmj(){ return super.get("gzmj");}
public void setGzmj(String  gzmj){ super.set("gzmj",gzmj);} /**
  * 机构名称
  */
  public  String  getJgmc(){ return super.get("jgmc");}
public void setJgmc(String  jgmc){ super.set("jgmc",jgmc);} /**
  * 传真
  */
  public  String  getChuanzhen(){ return super.get("chuanzhen");}
public void setChuanzhen(String  chuanzhen){ super.set("chuanzhen",chuanzhen);} /**
  * 注册资金
  */
  public  String  getZczj(){ return super.get("zczj");}
public void setZczj(String  zczj){ super.set("zczj",zczj);} /**
  * 设立时间
  */
  public  Date  getSlsj(){ return super.getDate("slsj");}
public void setSlsj(Date  slsj){ super.set("slsj",slsj);} /**
  * 发证机关（计量）
  */
  public  String  getFzjgjl(){ return super.get("fzjgjl");}
public void setFzjgjl(String  fzjgjl){ super.set("fzjgjl",fzjgjl);} /**
  * 工商注册所在地市
  */
  public  String  getGszcszds(){ return super.get("gszcszds");}
public void setGszcszds(String  gszcszds){ super.set("gszcszds",gszcszds);} /**
  * 证书编号
  */
  public  String  getZsbh(){ return super.get("zsbh");}
public void setZsbh(String  zsbh){ super.set("zsbh",zsbh);} /**
  * 企业名称
  */
  public  String  getQymc(){ return super.get("qymc");}
public void setQymc(String  qymc){ super.set("qymc",qymc);} /**
  * 企业统一社会信用代码
  */
  public  String  getQytyshxydm(){ return super.get("qytyshxydm");}
public void setQytyshxydm(String  qytyshxydm){ super.set("qytyshxydm",qytyshxydm);} /**
  * 发证日期
  */
  public  Date  getFzrq(){ return super.getDate("fzrq");}
public void setFzrq(Date  fzrq){ super.set("fzrq",fzrq);} /**
  * 有效期开始
  */
  public  Date  getYxqks(){ return super.getDate("yxqks");}
public void setYxqks(Date  yxqks){ super.set("yxqks",yxqks);} /**
  * 有效期结束
  */
  public  Date  getYxqjs(){ return super.getDate("yxqjs");}
public void setYxqjs(Date  yxqjs){ super.set("yxqjs",yxqjs);} /**
  * 数据来源
  */
  public  String  getSjly(){ return super.get("sjly");}
public void setSjly(String  sjly){ super.set("sjly",sjly);} /**
  * 备注
  */
  public  String  getBz(){ return super.get("bz");}
public void setBz(String  bz){ super.set("bz",bz);} /**
  * 发证机构
  */
  public  String  getFzjg(){ return super.get("fzjg");}
public void setFzjg(String  fzjg){ super.set("fzjg",fzjg);} /**
  * 详细地址
  */
  public  String  getXxdz(){ return super.get("xxdz");}
public void setXxdz(String  xxdz){ super.set("xxdz",xxdz);} /**
  * 邮编
  */
  public  String  getYoubian(){ return super.get("youbian");}
public void setYoubian(String  youbian){ super.set("youbian",youbian);} /**
  * 电话
  */
  public  String  getDianhua(){ return super.get("dianhua");}
public void setDianhua(String  dianhua){ super.set("dianhua",dianhua);} /**
  * 成立时间
  */
  public  Date  getClsj(){ return super.getDate("clsj");}
public void setClsj(Date  clsj){ super.set("clsj",clsj);} /**
  * 法定代表人
  */
  public  String  getFddbr(){ return super.get("fddbr");}
public void setFddbr(String  fddbr){ super.set("fddbr",fddbr);} /**
  * 法人职务
  */
  public  String  getFrzw(){ return super.get("frzw");}
public void setFrzw(String  frzw){ super.set("frzw",frzw);} /**
  * 法人职称
  */
  public  String  getFrzc(){ return super.get("frzc");}
public void setFrzc(String  frzc){ super.set("frzc",frzc);} /**
  * 技术负责人
  */
  public  String  getJsfzr(){ return super.get("jsfzr");}
public void setJsfzr(String  jsfzr){ super.set("jsfzr",jsfzr);} /**
  * 技术负责人职务
  */
  public  String  getJsfzrzw(){ return super.get("jsfzrzw");}
public void setJsfzrzw(String  jsfzrzw){ super.set("jsfzrzw",jsfzrzw);} /**
  * 技术负责人职称
  */
  public  String  getJsfzrzc(){ return super.get("jsfzrzc");}
public void setJsfzrzc(String  jsfzrzc){ super.set("jsfzrzc",jsfzrzc);} /**
  * 检测范围
  */
  public  String  getJcfw(){ return super.get("jcfw");}
public void setJcfw(String  jcfw){ super.set("jcfw",jcfw);} /**
  * 初次取得资质时间
  */
  public  Date  getCcqdzzsj(){ return super.getDate("ccqdzzsj");}
public void setCcqdzzsj(Date  ccqdzzsj){ super.set("ccqdzzsj",ccqdzzsj);} /**
  * 电子邮箱
  */
  public  String  getDzyx(){ return super.get("dzyx");}
public void setDzyx(String  dzyx){ super.set("dzyx",dzyx);} /**
  * 机构地址
  */
  public  String  getJgdz(){ return super.get("jgdz");}
public void setJgdz(String  jgdz){ super.set("jgdz",jgdz);} /**
  * 高级职称人数
  */
  public  String  getGjzcrs(){ return super.get("gjzcrs");}
public void setGjzcrs(String  gjzcrs){ super.set("gjzcrs",gjzcrs);} /**
  * 计量认证证书号
  */
  public  String  getJlrzzssh(){ return super.get("jlrzzssh");}
public void setJlrzzssh(String  jlrzzssh){ super.set("jlrzzssh",jlrzzssh);} /**
  * 仪器设备固定资产原值（万元）
  */
  public  String  getYqsbgdzcyz(){ return super.get("yqsbgdzcyz");}
public void setYqsbgdzcyz(String  yqsbgdzcyz){ super.set("yqsbgdzcyz",yqsbgdzcyz);} /**
  * 发证机关
  */
  public  String  getFazhengjiguan(){ return super.get("fazhengjiguan");}
public void setFazhengjiguan(String  fazhengjiguan){ super.set("fazhengjiguan",fazhengjiguan);}

}