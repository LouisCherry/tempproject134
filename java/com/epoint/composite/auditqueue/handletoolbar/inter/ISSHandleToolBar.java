package com.epoint.composite.auditqueue.handletoolbar.inter;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.common.service.AuditCommonResult;

@Service
public interface ISSHandleToolBar {

	/**
	 * 登录排队叫号初始化
	 *
	 * @param windowguid
	 *            窗口guid
	 * @param userguid
	 *            用户guid
	 * @param displayname
	 *            用户名
	 * @param workstatus
	 *            人员状态
	 * @return
	 */
	public AuditCommonResult<String> initQueueLogin(String windowguid, String userguid, String displayname,
                                                    String workstatus);

	/**
	 * 暂停
	 *
	 * @param windowguid
	 *            窗口guid
	 * @param windowno
	 *            窗口号
	 * @param centerguid
	 *            中心guid
	 * @return
	 */
	public AuditCommonResult<String> pauseQueue(String windowguid, String windowno, String centerguid);

	/**
	 * 注销
	 *
	 * @param windowguid
	 *            窗口guid
	 * @param windowno
	 *            窗口号
	 * @param centerguid
	 *            中心guid
	 * @return
	 */
	public AuditCommonResult<String> zhuXiaoQueue(String windowguid, String windowno, String centerguid);

	/**
	 * 再次呼叫
	 *
	 * @param qno
	 *            排队号
	 * @param windowguid
	 *            窗口guid
	 * @param windowno
	 *            窗口号
	 * @param centerguid
	 *            中心guid
	 * @return
	 */
	public AuditCommonResult<String> reCallQueue(String qno, String windowguid, String windowno, String centerguid);

	/**
	 * 自动获取下一位
	 *
	 * @param qno
	 *            当前排队号
	 * @param WindowGuid
	 *            窗口guid
	 * @param WindowNo
	 *            窗口号
	 * @param CenterGuid
	 *            中心guid
	 * @param UseCall
	 *            是否需要发送语音短信
	 * @return
	 */
	public AuditCommonResult<String> getNextQNO(String WindowGuid, String WindowNo, String CenterGuid, String UserGuid,
                                                Boolean UseCall);

	public AuditCommonResult<String> getNextQNO(String WindowGuid, String WindowNo, String CenterGuid, String UserGuid,
												Boolean UseCall, Boolean isAutoAssign);


	/**
	 * 下一位
	 *
	 * @param qno
	 *            当前排队号
	 * @param WindowGuid
	 *            窗口guid
	 * @param WindowNo
	 *            窗口号
	 * @param CenterGuid
	 *            中心guid
	 * @param UseCall
	 *            是否需要发送语音短信
	 * @return
	 */
	public AuditCommonResult<String> nextQueue(String qno, String WindowGuid, String WindowNo, String CenterGuid, String UserGuid,
                                               Boolean UseCall);
	/**
	 * 过号
	 *
	 * @param qno
	 *            当前排队号
	 * @param WindowGuid
	 *            窗口guid
	 * @param WindowNo
	 *            窗口号
	 * @param CenterGuid
	 *            中心guid
	 * @param UseCall
	 *            是否需要发送语音短信
	 * @return
	 */
	public AuditCommonResult<String> passQueue(String qno, String WindowGuid, String WindowNo, String CenterGuid, String UserGuid,
                                               Boolean UseCall);
	/**
	 * 开始叫号
	 *
	 * @param qno
	 *            当前排队号
	 * @param WindowGuid
	 *            窗口guid
	 * @param WindowNo
	 *            窗口号
	 * @param CenterGuid
	 *            中心guid
	 * @param UseCall
	 *            是否需要发送语音短信
	 * @return
	 */
	public AuditCommonResult<String> startQueue(String WindowGuid, String WindowNo, String CenterGuid, String UserGuid,
                                                Boolean UseCall);

	/**
	 * 自动刷新获取等待人数
	 * @param WindowGuid
	 * @param waitnum
	 * @return
	 */
	public AuditCommonResult<String> getWindowWaitNumAuto(String WindowGuid, String waitnum);
}
