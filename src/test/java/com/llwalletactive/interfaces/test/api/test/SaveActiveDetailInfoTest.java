package com.llwalletactive.interfaces.test.api.test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.testng.Reporter;
import org.testng.annotations.Test;
import com.alibaba.fastjson.JSONObject;
import com.llwalletactive.interfaces.bean.ParameterSource;
import com.tools.dataprovider.ExcelProvider;
import com.tools.utils.HttpUploadFile;
import com.tools.utils.Property;
import org.testng.annotations.DataProvider;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;

/*
 * @author jiangxm
 * 完善账户信息接口测试
 */

public class SaveActiveDetailInfoTest {

	@BeforeClass
	public void beforeClass() throws InterruptedException {
		Property.set();
	}

	@Test(description = "完善账户信息接口测试", timeOut = 60000, dataProvider = "saveActiveDetailInfo")
	public void saveActiveDetailInfo(Map<String, String> datadriven) throws Exception {

		String pwd_pay_new="BssmawzYgjvUEdA3VoBnw8L8jHUjSYd1Sy9vvOeBpvx8C1JJa2/dHuIPeVcrbkXTjLPRSZNWfGYQSmcBBqckTLmp7MXdHZlVS6Gv1bYAT8xvs1zceUNLEL6P/7gLem1PzpLCJuPBJ5oyS6Rak/5mPQqwNi/lKr7CmkCQip95vttbOVhGnLYTbtBgg0yTKQk+XlZrrX0WeSs5UOKMMDo7CMGedMuX6axvUJjkjlpAXVg9NdQvKl6FqLI5oee5RbrRJ1DkHbph+CcQ4rIv3al7NUPZHD0slLz0CkBYuXYpIlHIsaQGJuti/KH9dc5c3VLVho/ankKpgxsyOPTgOFO7EHOVHdzpioZBmf4HSowRKlloRt9AyR9nsCcQ3LBYDWfm6c30TUbUtJr2EUNJRWdftTjM62AWIhPGBRv+18e3yH1RwLPgpuWQka6pJnnq24TlwsBEKDkYeGtw4L4j0cXMBA==";
//		if (datadriven.get("activeAccountIndex.pwd_pay_new").length() != 0) {
//			pwd_pay_new=RSAUtil.encrypt(datadriven.get("activeAccountIndex.pwd_pay_new"), Property.get("rsa_pub_key"));
//		} else {
//			pwd_pay_new = datadriven.get("activeAccountIndex.pwd_pay_new");
//		}
//		
		
		StringBuffer signmsgtemp = new StringBuffer();
		String[] strArray = new ParameterSource().activeDetailInfoParameter();
		
		for (String key : strArray) {
			if (key.equals("activeAccountIndex.pwd_pay_new") ) {
				signmsgtemp.append(key + "=" + pwd_pay_new + "&");
			} else {
				signmsgtemp.append(key + "=" + datadriven.get(key) + "&");
			}
		}
//		String signMsg = signmsgtemp.substring(0, signmsgtemp.length() - 1);
//		SaveActiveDetailInfo saveActiveDetailInfo = new SaveActiveDetailInfo();
//		saveActiveDetailInfo.setOid_partner(datadriven.get("oid_partner"));
//		saveActiveDetailInfo.setSign_type(datadriven.get("sign_type"));
//		saveActiveDetailInfo.setUser_id(datadriven.get("user_id"));
//		saveActiveDetailInfo.setOid_job(datadriven.get("oid_job"));
//		saveActiveDetailInfo.setAddr_pro(datadriven.get("addr_pro"));
//		saveActiveDetailInfo.setAddr_city(datadriven.get("addr_city"));
//		saveActiveDetailInfo.setAddr_dist(datadriven.get("addr_dist"));
//		saveActiveDetailInfo.setAddr_conn(datadriven.get("addr_conn"));
//
//		saveActiveDetailInfo.setSign(GenSign.genSign(JSON.parseObject(JSON.toJSONString(saveActiveDetailInfo)),datadriven.get("key")));
//		String reqJson = JSON.toJSONString(saveActiveDetailInfo);
		String saveActiveDetailInfoUrl = Property.get("llwalletactive.saveActiveDetailInfo.url");

		// 完善账户信息请求提交
//		HttpFixture hf = new HttpFixture();
//		hf.setEncode("UTF-8");
//		hf.setUrl(saveActiveDetailInfoUrl);
//		hf.addHeaderValue("Content-Type", "application/x-www-form-urlencoded");
//		hf.addRequestBody(signMsg);
//		hf.Post();
		
		Map<String, String> textMap = new HashMap<String, String>();
		// 设置input的key，value
		textMap.put("activeAccountIndex.pwd_pay_new", pwd_pay_new);
		textMap.put("activeAccountIndex.user_id", datadriven.get("activeAccountIndex.user_id"));
		textMap.put("activeAccountIndex.oid_job", datadriven.get("activeAccountIndex.oid_job"));
		textMap.put("activeAccountIndex.addr_pro", datadriven.get("activeAccountIndex.addr_pro"));
		textMap.put("activeAccountIndex.addr_city", datadriven.get("activeAccountIndex.addr_city"));
		textMap.put("activeAccountIndex.addr_dist", datadriven.get("activeAccountIndex.addr_dist"));
		textMap.put("activeAccountIndex.addr_conn", datadriven.get("activeAccountIndex.addr_conn"));

		String contentType = "";
		String res = HttpUploadFile.formUpload(saveActiveDetailInfoUrl, textMap, null, contentType);
		System.out.println("req----:" + JSONObject.toJSON(textMap));
		Thread.sleep(1000);
		Reporter.log("接口返回： " + res, true);
		
		// 完善账户信息请求返回检查
//		assert hf.getResponseBody().contains(datadriven.get("result_code"));

		Reporter.log("完善账户信息接口测试： " + datadriven.get("comment"), true);
		System.out.println("===============================================");
	}
   
	@DataProvider(name = "saveActiveDetailInfo")
	public Iterator<Object[]> data4saveActiveDetailInfo() throws IOException {
		return new ExcelProvider(this, "saveActiveDetailInfo",1);
	}

	@AfterClass
	public void afterClass() {

	}

}
