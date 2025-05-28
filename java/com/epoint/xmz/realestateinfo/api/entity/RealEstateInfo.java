package com.epoint.xmz.realestateinfo.api.entity;

import java.util.Date;
import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;
import com.epoint.core.utils.EnumClazz.StrategyType;

/**
 * 楼盘信息表实体
 * 
 * @作者 1
 * @version [版本号, 2022-10-17 15:27:39]
 */
@Entity(table = "real_estate_info", id = "rowguid")
public class RealEstateInfo extends BaseEntity implements Cloneable {
	private static final long serialVersionUID = 1L;

	/**
	 * 备注
	 */
	public String getRemark() {
		return super.get("remark");
	}

	public void setRemark(String remark) {
		super.set("remark", remark);
	}

	/**
	 * 创建时间
	 */
	public Date getCreat_date() {
		return super.getDate("creat_date");
	}

	public void setCreat_date(Date creat_date) {
		super.set("creat_date", creat_date);
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
	 * 修改时间
	 */
	public Date getUpdate_date() {
		return super.getDate("update_date");
	}

	public void setUpdate_date(Date update_date) {
		super.set("update_date", update_date);
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
	 * 访问量
	 */
	public Integer getPv() {
		return super.getInt("pv");
	}

	public void setPv(Integer pv) {
		super.set("pv", pv);
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
	 * 操作日期
	 */
	public Date getOperatedate() {
		return super.getDate("operatedate");
	}

	public void setOperatedate(Date operatedate) {
		super.set("operatedate", operatedate);
	}

	/**
	 * 楼盘状态
	 */
	public String getRe_state() {
		return super.get("re_state");
	}

	public void setRe_state(String re_state) {
		super.set("re_state", re_state);
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
	 * 排序
	 */
	public Integer getOrder_num() {
		return super.getInt("order_num");
	}

	public void setOrder_num(Integer order_num) {
		super.set("order_num", order_num);
	}

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
	 * 是否发布
	 */
	public String getIs_issue() {
		return super.get("is_issue");
	}

	public void setIs_issue(String is_issue) {
		super.set("is_issue", is_issue);
	}

	/**
	 * 楼盘名称
	 */
	public String getRe_name() {
		return super.get("re_name");
	}

	public void setRe_name(String re_name) {
		super.set("re_name", re_name);
	}

	/**
	 * 区域位置
	 */
	public String getArea_addr() {
		return super.get("area_addr");
	}

	public void setArea_addr(String area_addr) {
		super.set("area_addr", area_addr);
	}

	/**
	 * 建设单位
	 */
	public String getDev_unit() {
		return super.get("dev_unit");
	}

	public void setDev_unit(String dev_unit) {
		super.set("dev_unit", dev_unit);
	}

	/**
	 * 参考价格
	 */
	public Integer getReference_price() {
		return super.getInt("reference_price");
	}

	public void setReference_price(Integer reference_price) {
		super.set("reference_price", reference_price);
	}

	/**
	 * 所属县区
	 */
	public String getBelong_area() {
		return super.get("belong_area");
	}

	public void setBelong_area(String belong_area) {
		super.set("belong_area", belong_area);
	}

	/**
	 * 建设内容
	 */
	public String getConst_content() {
		return super.get("const_content");
	}

	public void setConst_content(String const_content) {
		super.set("const_content", const_content);
	}

	/**
	 * 户型
	 */
	public String getHouse_type() {
		return super.get("house_type");
	}

	public void setHouse_type(String house_type) {
		super.set("house_type", house_type);
	}

	/**
	 * 建筑类型
	 */
	public String getBuild_type() {
		return super.get("build_type");
	}

	public void setBuild_type(String build_type) {
		super.set("build_type", build_type);
	}

	/**
	 * 开盘预售时间
	 */
	public Date getPre_sale_date() {
		return super.getDate("pre_sale_date");
	}

	public void setPre_sale_date(Date pre_sale_date) {
		super.set("pre_sale_date", pre_sale_date);
	}

	/**
	 * 绿化率
	 */
	public String getGreen_rate() {
		return super.get("green_rate");
	}

	public void setGreen_rate(String green_rate) {
		super.set("green_rate", green_rate);
	}

	/**
	 * 机动车停车位
	 */
	public String getMotor_num() {
		return super.get("motor_num");
	}

	public void setMotor_num(String motor_num) {
		super.set("motor_num", motor_num);
	}

	/**
	 * 规划车位数
	 */
	public String getPlan_car_num() {
		return super.get("plan_car_num");
	}

	public void setPlan_car_num(String plan_car_num) {
		super.set("plan_car_num", plan_car_num);
	}

	/**
	 * 容积率
	 */
	public String getPlot_ratio() {
		return super.get("plot_ratio");
	}

	public void setPlot_ratio(String plot_ratio) {
		super.set("plot_ratio", plot_ratio);
	}

	/**
	 * 机动车停车位（地上）
	 */
	public String getMotor_u_num() {
		return super.get("motor_u_num");
	}

	public void setMotor_u_num(String motor_u_num) {
		super.set("motor_u_num", motor_u_num);
	}

	/**
	 * 机动车停车位（地下）
	 */
	public String getMotor_d_num() {
		return super.get("motor_d_num");
	}

	public void setMotor_d_num(String motor_d_num) {
		super.set("motor_d_num", motor_d_num);
	}

	/**
	 * 非机动车停车位
	 */
	public String getNmotor_num() {
		return super.get("nmotor_num");
	}

	public void setNmotor_num(String nmotor_num) {
		super.set("nmotor_num", nmotor_num);
	}

	/**
	 * 非机动车停车位（地下）
	 */
	public String getNmotor_d_num() {
		return super.get("nmotor_d_num");
	}

	public void setNmotor_d_num(String nmotor_d_num) {
		super.set("nmotor_d_num", nmotor_d_num);
	}

	/**
	 * 非机动车停车位（地上）
	 */
	public String getNmotor_u_num() {
		return super.get("nmotor_u_num");
	}

	public void setNmotor_u_num(String nmotor_u_num) {
		super.set("nmotor_u_num", nmotor_u_num);
	}

	/**
	 * 房源套数
	 */
	public String getHouse_num() {
		return super.get("house_num");
	}

	public void setHouse_num(String house_num) {
		super.set("house_num", house_num);
	}

	/**
	 * 销售电话
	 */
	public String getSales_tel() {
		return super.get("sales_tel");
	}

	public void setSales_tel(String sales_tel) {
		super.set("sales_tel", sales_tel);
	}

	/**
	 * 销售地址
	 */
	public String getSales_addr() {
		return super.get("sales_addr");
	}

	public void setSales_addr(String sales_addr) {
		super.set("sales_addr", sales_addr);
	}

	/**
	 * 开发商
	 */
	public String getDevelopers() {
		return super.get("developers");
	}

	public void setDevelopers(String developers) {
		super.set("developers", developers);
	}

	/**
	 * 注册地址
	 */
	public String getDevelopers_addr() {
		return super.get("developers_addr");
	}

	public void setDevelopers_addr(String developers_addr) {
		super.set("developers_addr", developers_addr);
	}

	/**
	 * 注册资本
	 */
	public String getDevelopers_reg() {
		return super.get("developers_reg");
	}

	public void setDevelopers_reg(String developers_reg) {
		super.set("developers_reg", developers_reg);
	}

	/**
	 * 封面图
	 */
	public String getCover_plan() {
		return super.getStr("cover_plan");
	}

	public void setCover_plan(String cover_plan) {
		super.set("cover_plan", cover_plan);
	}

	/**
	 * 鸟瞰图
	 */
	public String getAerial_view() {
		return super.getStr("aerial_view");
	}

	public void setAerial_view(String aerial_view) {
		super.set("aerial_view", aerial_view);
	}

	/**
	 * 总平面图
	 */
	public String getSite_plan() {
		return super.getStr("site_plan");
	}

	public void setSite_plan(String site_plan) {
		super.set("site_plan", site_plan);
	}

	/**
	 * 其它图
	 */
	public String getOther_pic() {
		return super.getStr("other_pic");
	}

	public void setOther_pic(String other_pic) {
		super.set("other_pic", other_pic);
	}

	/**
	 * 国有土地使用权证
	 */
	public String getLurc() {
		return super.get("lurc");
	}

	public void setLurc(String lurc) {
		super.set("lurc", lurc);
	}

	/**
	 * 建设用地规划许可证
	 */
	public String getLand_use_permit() {
		return super.get("land_use_permit");
	}

	public void setLand_use_permit(String land_use_permit) {
		super.set("land_use_permit", land_use_permit);
	}

	/**
	 * 建设工程规划许可证
	 */
	public String getProject_permit() {
		return super.get("project_permit");
	}

	public void setProject_permit(String project_permit) {
		super.set("project_permit", project_permit);
	}

	/**
	 * 建设工程施工许可证
	 */
	public String getProject_con_permit() {
		return super.get("project_con_permit");
	}

	public void setProject_con_permit(String project_con_permit) {
		super.set("project_con_permit", project_con_permit);
	}

	/**
	 * 商品房预售许可证
	 */
	public String getPre_sale_permit() {
		return super.get("pre_sale_permit");
	}

	public void setPre_sale_permit(String pre_sale_permit) {
		super.set("pre_sale_permit", pre_sale_permit);
	}

	/**
	 * 建设工程竣工规划核实证
	 */
	public String getPcpvc() {
		return super.get("pcpvc");
	}

	public void setPcpvc(String pcpvc) {
		super.set("pcpvc", pcpvc);
	}

	/**
	 * 消防验收/备案证明
	 */
	public String getFpc() {
		return super.get("fpc");
	}

	public void setFpc(String fpc) {
		super.set("fpc", fpc);
	}

	/**
	 * 竣工验收备案证明
	 */
	public String getCarc() {
		return super.get("carc");
	}

	public void setCarc(String carc) {
		super.set("carc", carc);
	}

}