package com.lianpay.interfaces.test;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import org.testng.Reporter;
import org.testng.annotations.Test;
import com.alibaba.fastjson.JSON;
import com.lianpay.interfaces.bean.PwdAuth;
import com.lianpay.tools.dataprovider.ExcelProvider;
import com.lianpay.tools.http.HttpFixture;
import com.lianpay.tools.utils.GenSign;
import com.lianpay.tools.utils.Property;
import com.lianpay.util.RSAUtil2;

import org.testng.annotations.DataProvider;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;

/*
 * @author jiangxm
 * 钱包支付密码验证授权接口测试
 */

public class PwdAuthTest {

	@BeforeClass
	public void beforeClass() throws InterruptedException {
		Property.set();
	}

	@Test(description = "钱包支付密码验证授权接口测试", timeOut = 60000, dataProvider = "pwdAuth")
	public void pwdAuth(Map<String, String> datadriven) throws Exception {

		PwdAuth pwdAuth = new PwdAuth();
		pwdAuth.setOid_partner(datadriven.get("oid_partner"));
		pwdAuth.setSign_type(datadriven.get("sign_type"));
		pwdAuth.setUser_id(datadriven.get("user_id"));
		if (datadriven.get("pwd_pay").length() != 0) {
			pwdAuth.setPwd_pay(RSAUtil2.encrypt(datadriven.get("pwd_pay"), Property.get("rsa_pub_key")));
		} else {
			pwdAuth.setPwd_pay(datadriven.get("pwd_pay"));
		}
		pwdAuth.setNo_idcard(datadriven.get("no_idcard"));
		pwdAuth.setFlag_check(datadriven.get("flag_check"));
		pwdAuth.setMob_bind(datadriven.get("mob_bind"));
		pwdAuth.setSign(GenSign.genSign(JSON.parseObject(JSON.toJSONString(pwdAuth)),datadriven.get("key")));
		String reqJson = JSON.toJSONString(pwdAuth);
		String pwdAuthUrl = Property.get("llwallet.pwdAuth.url");

		// 钱包支付密码验证授权请求提交
		HttpFixture hf = new HttpFixture();
		hf.setEncode("UTF-8");
		hf.setUrl("" + pwdAuthUrl + "");
		hf.addHeaderValue("Content-Type", "application/json");
		hf.addRequestBody(reqJson);
		hf.Post();
		// 钱包支付密码验证授权请求返回检查
		assert hf.getResponseBody().contains(datadriven.get("result_code"));
//		assert hf.getResponseBody().contains(datadriven.get("result_code1"));

		Reporter.log("钱包支付密码验证授权接口测试： " + datadriven.get("comment"), true);
		Reporter.log("===============================================", true);
	}
   
	@DataProvider(name = "pwdAuth")
	public Iterator<Object[]> data4pwdAuth() throws IOException {
		return new ExcelProvider(this, "pwdAuth",1);
	}

	@AfterClass
	public void afterClass() {

	}

}
