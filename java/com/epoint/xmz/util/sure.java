package com.epoint.xmz.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import com.epoint.core.utils.classpath.ClassPathUtil;
import com.sure.signserver.SureCSPAPI;
import com.sure.signserver.util.BasicDefine;
import com.sure.signserver.util.ErrCode;

public class sure {
	
	/**
	 * 生成数字签名
	 * @param qmyw
	 * @return
	 */
	public static byte[] qxszqm(byte[] qmyw){
        int result = 0;
		SureCSPAPI handle = new SureCSPAPI();
		
		// 从配置文件中获取许可证名称
        String surecryptocfgurl = "SureCryptoCfg.xml";
        // 获取许可证文件路径
        String path = ClassPathUtil.getClassesPath() + surecryptocfgurl;
        
		//建立链接
		result = handle.InitServerConnect(path);
		if (result != ErrCode.ERR_SUCCESS) {
			//system.out.println("InitServerConnect error, code = " + result);
//			System.exit(0);
		}
//		byte[] plainData = "Hello world.".getBytes();
		ByteArrayOutputStream byteos = new ByteArrayOutputStream();
		result = handle.SVS_PKCS1SignData(BasicDefine.ALG_ASYM_SM2, qmyw, byteos, "SdJnSpjYCSL");
		//system.out.println("SVS_PKCS1_SignData = " + result);
		byte[] bb = byteos.toByteArray();

		result = handle.SVS_PKCS1VerifyData("SdJnSpjYCSL", BasicDefine.ALG_ASYM_SM2,byteos.toByteArray(), qmyw);
		
		try {
			byteos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//释放连接
		result = handle.FinalizeServerConnect();
		
		return bb;
	}
	
}
