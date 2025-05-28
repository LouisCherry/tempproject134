package com.epoint.jnzwfw.evaluationpad.api;

import java.io.Serializable;
import java.util.List;

import com.epoint.core.grammar.Record;

/**
 * 评价pad接口
 * @author 许烨斌
 * @time 2019年1月21日下午5:01:06
 */
public interface IEvaluationPadService extends Serializable
{

    /**
     * @Description:获取部门电话
     * @author:许烨斌
     * @time 2019年1月21日下午5:05:52
     * @param windowguid 窗口guid
     * @return 部门电话
     */
    public String findOUTel(String windowguid);

    /**
     * @Description:获取人员信息
     * @author:许烨斌
     * @time 2019年1月21日下午5:05:42
     * @param userguid 人员guid
     * @return
     */
    public Record findUserInfo(String userguid);

    /**
     * 
     *  @Description:根据窗口获取事项信息
     *  @authory shibin
     *  @version [版本号, 2019年9月19日]
     *  @param windowguid
     *  @return
     */
    public List<Record> findTaskInfoList(String windowguid);

    /**
     * 根据macaddres获取windowguid
     * @authory shibin
     * @version 2019年9月23日 下午2:35:35
     * @param macaddress
     * @return
     */
    public String getWindowguidByMacAddress(String macaddress);

    /**
     * 获取attachguid
     * @authory shibin
     * @version 2019年9月25日 下午12:11:58
     * @param str
     * @return
     */
    public String getAttachguidByCliengguid(String str);

    /**
     * @description
     * @author shibin
     * @date  2020年6月8日 上午10:32:22
     */
    public int updateQRcodeinfoByprojectGuid(String projectGuid, String qRcodeinfo);
    
    
    /**
     * @description
     * @author shibin
     * @date  2020年6月8日 上午10:32:22
     */
    public int updateJSTcodeinfoByprojectGuid(String projectGuid, String jstcodeinfo);
    
    
    /**
     * 保存电子营业执照二维码
     *  @param projectGuid
     *  @param qRcodeinfo
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public int updateLegalQRcodeinfoByprojectGuid(String projectGuid, String qRcodeinfo);

}
