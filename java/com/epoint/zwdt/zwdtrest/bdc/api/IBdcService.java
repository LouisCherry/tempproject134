package com.epoint.zwdt.zwdtrest.bdc.api;

import java.io.Serializable;

import com.epoint.core.grammar.Record;

/**
 * 济宁通用service
 * @作者 zym
 * @version [版本号, 2021年8月20日]
 */
public interface IBdcService extends Serializable
{

	public Record getBdcDetailByBdcdyh(String str);

}
