package com.epoint.zwzt.xxfb.xxfbinforelease.api.entity;

import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;

import java.util.Date;

/**
 * 信息发布表实体
 *
 * @author D0Be
 * @version [版本号, 2022-04-27 15:34:21]
 */
@Entity(table = "xxfb_info_release", id = "rowguid")
public class XxfbInfoRelease extends BaseEntity implements Cloneable
{
    private static final long serialVersionUID = 1L;

    /**
     * 标题名称
     */

    public String getInfo_title() {
        return super.get("info_title");
    }

    public void setInfo_title(String info_title) {
        super.set("info_title", info_title);
    }

    /**
     * 栏目标识
     */

    public String getColumn_guid() {
        return super.get("column_guid");
    }

    public void setColumn_guid(String column_guid) {
        super.set("column_guid", column_guid);
    }

    /**
     * 栏目名称
     */

    public String getColumn_name() {
        return super.get("column_name");
    }

    public void setColumn_name(String column_name) {
        super.set("column_name", column_name);
    }

    /**
     * 信息内容标识
     */

    public String getInfo_content_rowguid() {
        return super.get("info_content_rowguid");
    }

    public void setInfo_content_rowguid(String info_content_rowguid) {
        super.set("info_content_rowguid", info_content_rowguid);
    }

    /**
     * 附件标识
     */
    public String getEnclosure_guids() {
        return super.get("enclosure_guids");
    }

    public void setEnclosure_guids(String enclosure_guids) {
        super.set("enclosure_guids", enclosure_guids);
    }

    /**
     * 图片附件标识
     */
    public String getPicture_guids() {
        return super.get("picture_guids");
    }

    public void setPicture_guids(String picture_guids) {
        super.set("picture_guids", picture_guids);
    }

    /**
     * 超链接
     */
    public String getLinkurl() {
        return super.get("linkurl");
    }

    public void setLinkurl(String linkurl) {
        super.set("linkurl", linkurl);
    }

    /**
     * 视频附件标识
     */

    public String getVedio_guid() {
        return super.get("vedio_guid");
    }

    public void setVedio_guid(String vedio_guid) {
        super.set("vedio_guid", vedio_guid);
    }

    /**
     * 信息状态
     */
    public String getInfo_status() {
        return super.get("info_status");
    }

    public void setInfo_status(String info_status) {
        super.set("info_status", info_status);
    }

    /**
     * 信息类型
     */
    public String getInfo_type() {
        return super.get("info_type");
    }

    public void setInfo_type(String info_type) {
        super.set("info_type", info_type);
    }

    /**
     * 信息发布审核流程标识
     */

    public String getInfo_flow_guid() {
        return super.get("info_flow_guid");
    }

    public void setInfo_flow_guid(String info_flow_guid) {
        super.set("info_flow_guid", info_flow_guid);
    }

    /**
     * 是否置顶
     */
    public String getIs_top() {
        return super.get("is_top");
    }

    public void setIs_top(String is_top) {
        super.set("is_top", is_top);
    }

    /**
     * 是否置顶
     */
    public String getIs_enablePicture() {
        return super.get("is_enablePicture");
    }

    public void setIs_enablePicture(String is_enablePicture) {
        super.set("is_enablePicture", is_enablePicture);
    }

    /**
     * 发布人类型
     */

    public String getPublisher_type() {
        return super.get("publisher_type");
    }

    public void setPublisher_type(String publisher_type) {
        super.set("publisher_type", publisher_type);
    }

    /**
     * 发布人名义
     */
    public String getPublisher() {
        return super.get("publisher");
    }

    public void setPublisher(String publisher) {
        super.set("publisher", publisher);
    }

    /**
     * 截至时间
     */
    public Date getDead_line() {
        return super.getDate("dead_line");
    }

    public void setDead_line(Date dead_line) {
        super.set("dead_line", dead_line);
    }

    /**
     * 创建时间
     */
    public Date getCreate_time() {
        return super.getDate("create_time");
    }

    public void setCreate_time(Date create_time) {
        super.set("create_time", create_time);
    }

    /**
     * 创建人标识
     */

    public String getCreate_userguid() {
        return super.get("create_userguid");
    }

    public void setCreate_userguid(String create_userguid) {
        super.set("create_userguid", create_userguid);
    }

    /**
     * 创建人姓名
     */

    public String getCreate_username() {
        return super.get("create_username");
    }

    public void setCreate_username(String create_username) {
        super.set("create_username", create_username);
    }

    /**
     * 排序号
     */
    public Integer getOrdernum() {
        return super.getInt("ordernum");
    }

    public void setOrdernum(Integer ordernum) {
        super.set("ordernum", ordernum);
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
     * 操作人所在独立部门Guid
     */

    public String getOperateuserbaseouguid() {
        return super.get("operateuserbaseouguid");
    }

    public void setOperateuserbaseouguid(String operateuserbaseouguid) {
        super.set("operateuserbaseouguid", operateuserbaseouguid);
    }

    /**
     * 所属辖区编号
     */

    public String getBelongxiaqucode() {
        return super.get("belongxiaqucode");
    }

    public void setBelongxiaqucode(String belongxiaqucode) {
        super.set("belongxiaqucode", belongxiaqucode);
    }

    /**
     * 主键guid
     */

    public String getRowguid() {
        return super.get("rowguid");
    }

    public void setRowguid(String rowguid) {
        super.set("rowguid", rowguid);
    }

    /**
     * 操作人所在部门Guid
     */

    public String getOperateuserouguid() {
        return super.get("operateuserouguid");
    }

    public void setOperateuserouguid(String operateuserouguid) {
        super.set("operateuserouguid", operateuserouguid);
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
     * 操作人姓名
     */

    public String getOperateusername() {
        return super.get("operateusername");
    }

    public void setOperateusername(String operateusername) {
        super.set("operateusername", operateusername);
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
     * 操作人Guid
     */

    public String getOperateuserguid() {
        return super.get("operateuserguid");
    }

    public void setOperateuserguid(String operateuserguid) {
        super.set("operateuserguid", operateuserguid);
    }

    /**
     * 流程标识
     */

    public String getPviguid() {
        return super.get("pviguid");
    }

    public void setPviguid(String pviguid) {
        super.set("pviguid", pviguid);
    }

    /**
     * 是否热门
     */

    public String getIs_hot() {
        return super.get("is_hot");
    }

    public void setIs_hot(String is_hot) {
        super.set("is_hot", is_hot);
    }

    /**
     * 作者
     */

    public String getInfo_author() {
        return super.get("info_author");
    }

    public void setInfo_author(String info_author) {
        super.set("info_author", info_author);
    }

}
