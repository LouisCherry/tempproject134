package com.epoint.jnycsl.constant;

/**
 * 部门接口响应代码
 * @author 刘雨雨
 * @time 2018年9月12日上午9:09:58
 */
public enum ResponseStatus {
	
	/**
	 * 处理成功
	 */
	OK("200"),
	
	/**
	 * 处理失败，返回错误信息
	 */
	ERROR("300"),
	
	/**
	 * 错误的请求,检查请求地址格式是否正确
	 */
	BAD_REQUEST("400"),
	
	/**
	 * 请求登录,没有传递有效的Token，或应用系统还未通过授权
	 */
	UNAUTHORIZED("401"),
	
	/**
	 * 无权访问,需要通过授权访问接口
	 */
	FORBIDDEN("403"),
	
	/**
	 * 没有找到指定的服务,通常表现为服务名或方法名错误，检查程序是否正确的填写了服务名和方法名，同时注意区分大小写
	 */
	NOT_FOUND("404"),
	
	/**
	 * 内部服务器错误,服务器内部处理异常，通过返回的错误信息提示定位问题
	 */
	INTERNAL_SERVER_ERROR("500"); 
	
	private String value;

	private ResponseStatus(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return this.value;
	}
	
	public String getValue() {
		return value;
	}
	
}
