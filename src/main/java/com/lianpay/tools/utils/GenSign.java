package com.lianpay.tools.utils;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import com.alibaba.fastjson.JSONObject;
import com.lianpay.api.util.TraderRSAUtil;
import com.lianpay.util.Md5Algorithm;

public class GenSign {

	public static String genSign(JSONObject reqObj, String key) {
		String sign_type = reqObj.getString("sign_type");
		// 生成待签名串
		String sign_src = genSignData(reqObj);
		System.out.println("商户[" + reqObj.getString("oid_partner") + "]待签名原串" + sign_src);

		if ("MD5".equals(sign_type)) {
//			sign_src += "&key=" + Property.get("md5_key");
			sign_src += "&key=" + key;
			return getSignMD5(sign_src);
		}
		if ("RSA".equals(sign_type)) {
//			return getSignRSA(sign_src);
			return getSignRSA(sign_src,key);
		}
		return null;
	}

	/**
	 * MD5签名
	 * @param signSrc
	 * @return
	 */
	public static String getSignMD5(String signSrc) {
		try {
			return Md5Algorithm.getInstance().md5Digest(signSrc.getBytes("utf-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * RSA签名
	 * @param signSrc
	 * @return
	 */
//	public static String getSignRSA(String signSrc) {
//		return TraderRSAUtil.sign(Property.get("rsa_pri_key"), signSrc);
//	}
	
	public static String getSignRSA(String signSrc, String key) {
		return TraderRSAUtil.sign(key, signSrc);
	}

	/**
	 * 生成待签名串
	 * @param
	 * @return
	 */
	public static String genSignData(JSONObject jsonObject) {
		StringBuffer content = new StringBuffer();

		// 按照key做首字母升序排列
		List<String> keys = new ArrayList<String>(jsonObject.keySet());
		Collections.sort(keys, String.CASE_INSENSITIVE_ORDER);
		for (int i = 0; i < keys.size(); i++) {
			String key = (String) keys.get(i);
			// sign 不参与签名
			if ("sign".equals(key)) {
				continue;
			}
			String value = (String) jsonObject.getString(key);
			// 空串不参与签名
			if (null == value | value.length()==0) {
				continue;
			}
			
			content.append((i == 0 ? "" : "&") + key + "=" + value);

		}
		String signSrc = content.toString();
		if (signSrc.startsWith("&")) {
			signSrc = signSrc.replaceFirst("&", "");
		}
		return signSrc;
	}

}
