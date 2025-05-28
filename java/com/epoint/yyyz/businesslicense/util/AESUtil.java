package com.epoint.yyyz.businesslicense.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

/**
 * AES加密工具类
 */
public class AESUtil {
	
	private static String charsetName = "UTF-8";
	private static Log logger = LogFactory.getLog(AESUtil.class);

	public static class Base64Encoder {
		private static final char[] SIXTY_FOUR_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".toCharArray();
		private static final int[] REVERSE_MAPPING = new int[123];

		static {
			for(int i = 0; i < SIXTY_FOUR_CHARS.length; ++i) {
				REVERSE_MAPPING[SIXTY_FOUR_CHARS[i]] = i + 1;
			}

		}

		public Base64Encoder() {
		}

		public String encode(byte[] input) {
			StringBuffer result = new StringBuffer();
			int outputCharCount = 0;

			for(int i = 0; i < input.length; i += 3) {
				int remaining = Math.min(3, input.length - i);
				int oneBigNumber = (input[i] & 255) << 16 | (remaining <= 1 ? 0 : input[i + 1] & 255) << 8 | (remaining <= 2 ? 0 : input[i + 2] & 255);

				for(int j = 0; j < 4; ++j) {
					result.append(remaining + 1 > j ? SIXTY_FOUR_CHARS[63 & oneBigNumber >> 6 * (3 - j)] : '=');
				}

				outputCharCount += 4;
				if (outputCharCount % 76 == 0) {
					result.append('\n');
				}
			}

			return result.toString();
		}

		public byte[] decode(String input) {
			try {
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				StringReader in = new StringReader(input);

				for(int i = 0; i < input.length(); i += 4) {
					int[] a = new int[]{this.mapCharToInt(in), this.mapCharToInt(in), this.mapCharToInt(in), this.mapCharToInt(in)};
					int oneBigNumber = (a[0] & 63) << 18 | (a[1] & 63) << 12 | (a[2] & 63) << 6 | a[3] & 63;

					for(int j = 0; j < 3; ++j) {
						if (a[j + 1] >= 0) {
							out.write(255 & oneBigNumber >> 8 * (2 - j));
						}
					}
				}

				return out.toByteArray();
			} catch (IOException var8) {
				throw new Error(var8 + ": " + var8.getMessage());
			}
		}

		private int mapCharToInt(Reader input) throws IOException {
			int c;
			do {
				if ((c = input.read()) == -1) {
					return -1;
				}

				int result = REVERSE_MAPPING[c];
				if (result != 0) {
					return result - 1;
				}
			} while(c != 61);

			return -1;
		}
	}

	/**
	 * AES加密算法
	 * @param content
	 * @param encryptKey
	 * @return
	 */
	public static String aesEncode(String content, String encryptKey){
		String param = "";
		try {
			byte[] raw = encryptKey.getBytes(charsetName);
			SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding"); //"算法/模式/补码方式"
			cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
			byte[] result = cipher.doFinal(content.getBytes(charsetName));
			param = new Base64Encoder().encode(result);
			//加密后的参数中+替换为^，+传递过程中会变成空格
			param = param.replaceAll("\\+", "^");
			//将换行替换成空
			param = param.replaceAll("\\\n", "");
		} catch (Exception e) {
			logger.error("AES加密失败："+e.getMessage(), e);
		}
		return param;
	}

	public static void main(String[] args) {
		String a = "[{\"serialNo\":\"8ab0a2967283edb9017283edb9c00000\",\"state\":\"07\",\"item\":\"第二类医疗器械经营备案\" }]";
		//system.out.println(aesEncode(a,"0172917e7bd30001"));;
	}
}
