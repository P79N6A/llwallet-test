package com.llwallet.interfaces.test.api.online.enterprise;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import org.testng.Reporter;
import org.testng.annotations.Test;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.llwallet.interfaces.bean.enterprise.ModifyUnitUserAcct;
import com.llwallet.interfaces.bean.enterprise.PwdAuth;
import com.llwallet.interfaces.bean.personal.SingleUserQuery;
import com.llwallet.interfaces.bean.personal.SmsCheck;
import com.llwallet.interfaces.dao.DbUtils;
import com.tools.dataprovider.ExcelProvider;
import com.tools.http.HttpFixture;
import com.tools.utils.GenSign;
import com.tools.utils.Property;
import com.tools.utils.RSAUtil;
import org.testng.annotations.DataProvider;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;

/*
 * @author jiangxm
 * 企业用户对公账户修改接口测试
 */

public class ModifyUnitUserAcctOnlineTest {

	@BeforeClass
	public void beforeClass() throws InterruptedException {
		Property.set();
	}

	@Test(description = "企业用户对公账户修改接口测试", timeOut = 60000, dataProvider = "modifyUnitUserAcct")
	public void modifyUnitUserAcct(Map<String, String> datadriven) throws Exception {

		Thread.sleep(1000);

		SingleUserQuery singleUserQuery = new SingleUserQuery();
		singleUserQuery.setOid_partner(datadriven.get("oid_partner"));
		singleUserQuery.setSign_type(datadriven.get("sign_type"));
		singleUserQuery.setUser_id(datadriven.get("user_id"));
		singleUserQuery.setSign(GenSign.genSign(JSON.parseObject(JSON.toJSONString(singleUserQuery)),datadriven.get("key")));
		String reqJson = JSON.toJSONString(singleUserQuery);
		String singleUserQueryUrl = Property.get("llwallet.singleUserQuery.url.online");

		// 钱包用户查询请求提交
		HttpFixture hf = new HttpFixture();
		hf.setEncode("UTF-8");
		hf.setUrl(singleUserQueryUrl);
		hf.addHeaderValue("Content-Type", "application/json");
		hf.addRequestBody(reqJson);
		hf.Post();
		String num_license = JSONObject.parseObject(hf.getResponseBody()).getString("num_license");
		
		PwdAuth pwdAuth = new PwdAuth();
		pwdAuth.setOid_partner(datadriven.get("oid_partner"));
		pwdAuth.setSign_type(datadriven.get("sign_type"));
		pwdAuth.setUser_id(datadriven.get("user_id"));
		if (datadriven.get("pwd_pay").length() != 0) {
			pwdAuth.setPwd_pay(RSAUtil.encrypt(datadriven.get("pwd_pay"), Property.get("rsa_pub_key")));
		} else {
			pwdAuth.setPwd_pay(datadriven.get("pwd_pay"));
		}
		if ("auto".equals(datadriven.get("num_license"))) {
			pwdAuth.setNum_license(num_license);
		} else {
			pwdAuth.setNum_license(datadriven.get("num_license"));
		}
		
		pwdAuth.setFlag_check(datadriven.get("flag_check"));
		pwdAuth.setMob_bind(datadriven.get("mob_bind"));
		pwdAuth.setRisk_item(datadriven.get("risk_item"));
		pwdAuth.setSign(GenSign.genSign(JSON.parseObject(JSON.toJSONString(pwdAuth)),datadriven.get("key")));
		String reqJson1 = JSON.toJSONString(pwdAuth);
		String pwdAuthUrl = Property.get("llwallet.pwdAuth.url.online");

		// 钱包支付密码验证授权请求提交
		hf.nextRequest();
		hf.setUrl(pwdAuthUrl);
		hf.addRequestBody(reqJson1);
		hf.Post();
		// 钱包支付密码验证授权请求返回检查
		assert hf.getResponseBody().contains(datadriven.get("result_code"));
		
		String token1 = JSONObject.parseObject(hf.getResponseBody()).getString("token");
		
		ModifyUnitUserAcct modifyUnitUserAcct = new ModifyUnitUserAcct();
		modifyUnitUserAcct.setOid_partner(datadriven.get("oid_partner"));
		modifyUnitUserAcct.setSign_type(datadriven.get("sign_type"));
		modifyUnitUserAcct.setToken(token1);
		modifyUnitUserAcct.setUser_id(datadriven.get("user_id"));
		modifyUnitUserAcct.setCity_code(datadriven.get("city_code"));
		modifyUnitUserAcct.setBrabank_name(datadriven.get("brabank_name"));
		modifyUnitUserAcct.setCard_no(datadriven.get("card_no"));
		modifyUnitUserAcct.setBank_code(datadriven.get("bank_code"));
		modifyUnitUserAcct.setAcct_name(datadriven.get("acct_name"));
		modifyUnitUserAcct.setPrcptcd(datadriven.get("prcptcd"));
		modifyUnitUserAcct.setRisk_item(datadriven.get("risk_item"));
		modifyUnitUserAcct.setSign(GenSign.genSign(JSON.parseObject(JSON.toJSONString(modifyUnitUserAcct)),datadriven.get("key")));
		String reqJson2 = JSON.toJSONString(modifyUnitUserAcct);
		String modifyUnitUserAcctUrl = Property.get("llwallet.modifyUnitUserAcct.url.online");
				
		// 企业用户对公账户修改请求提交
		hf.nextRequest();
		hf.setUrl(modifyUnitUserAcctUrl);
		hf.addRequestBody(reqJson2);
		hf.Post();
		// 企业用户对公账户修改请求返回检查
		assert hf.getResponseBody().contains(datadriven.get("result_code1"));

		Reporter.log("企业用户对公账户修改接口测试： " + datadriven.get("comment"), true);
		System.out.println("===============================================");
	}
	
	@Test(description = "企业用户对公账户修改接口测试", timeOut = 60000, dataProvider = "modifyUnitUserAcct1")
	public void modifyUnitUserAcct1(Map<String, String> datadriven) throws Exception {

		Thread.sleep(1000);

		// 更新用户测试数据
		DbUtils dbUtils = new DbUtils();

		PwdAuth pwdAuth = new PwdAuth();
		pwdAuth.setOid_partner(datadriven.get("oid_partner"));
		pwdAuth.setSign_type(datadriven.get("sign_type"));
		pwdAuth.setUser_id(datadriven.get("user_id"));
		if (datadriven.get("pwd_pay").length() != 0) {
			pwdAuth.setPwd_pay(RSAUtil.encrypt(datadriven.get("pwd_pay"), Property.get("rsa_pub_key")));
		} else {
			pwdAuth.setPwd_pay(datadriven.get("pwd_pay"));
		}
		pwdAuth.setNum_license(datadriven.get("num_license"));
		pwdAuth.setFlag_check(datadriven.get("flag_check"));
		pwdAuth.setMob_bind(datadriven.get("mob_bind"));
		pwdAuth.setRisk_item(datadriven.get("risk_item"));
		pwdAuth.setSign(GenSign.genSign(JSON.parseObject(JSON.toJSONString(pwdAuth)),datadriven.get("key")));
		String reqJson = JSON.toJSONString(pwdAuth);
		String pwdAuthUrl = Property.get("llwallet.pwdAuth.url.online");

		// 钱包支付密码验证授权请求提交
		HttpFixture hf = new HttpFixture();
		hf.setEncode("UTF-8");
		hf.setUrl(pwdAuthUrl);
		hf.addHeaderValue("Content-Type", "application/json");
		hf.addRequestBody(reqJson);
		hf.Post();
		// 钱包支付密码验证授权请求返回检查
		assert hf.getResponseBody().contains(datadriven.get("result_code"));
		
		String token1 = JSONObject.parseObject(hf.getResponseBody()).getString("token");
		String verify_code1 = dbUtils.queryCdSmscd(datadriven.get("mob_bind")).getTno_smscd();

		SmsCheck smsCheck1 = new SmsCheck();
		smsCheck1.setOid_partner(datadriven.get("oid_partner"));
		smsCheck1.setSign_type(datadriven.get("sign_type"));
		smsCheck1.setUser_id(datadriven.get("user_id"));
		smsCheck1.setToken(token1);
		smsCheck1.setVerify_code(verify_code1);
		smsCheck1.setSign(GenSign.genSign(JSON.parseObject(JSON.toJSONString(smsCheck1)), datadriven.get("key")));
		String reqJson1 = JSON.toJSONString(smsCheck1);
		String smsCheckUrl = Property.get("llwallet.smsCheck.url.online");

		// 钱包短信验证请求提交
		hf.nextRequest();
		hf.setUrl(smsCheckUrl);
		hf.addRequestBody(reqJson1);
		hf.Post();
		// 钱包短信验证请求返回检查
		assert hf.getResponseBody().contains(datadriven.get("result_code"));
		String token2 = JSONObject.parseObject(hf.getResponseBody()).getString("token");
		
		ModifyUnitUserAcct modifyUnitUserAcct = new ModifyUnitUserAcct();
		modifyUnitUserAcct.setOid_partner(datadriven.get("oid_partner"));
		modifyUnitUserAcct.setSign_type(datadriven.get("sign_type"));
		modifyUnitUserAcct.setToken(token2);
		modifyUnitUserAcct.setUser_id(datadriven.get("user_id"));
		modifyUnitUserAcct.setCity_code(datadriven.get("city_code"));
		modifyUnitUserAcct.setBrabank_name(datadriven.get("brabank_name"));
		modifyUnitUserAcct.setCard_no(datadriven.get("card_no"));
		modifyUnitUserAcct.setBank_code(datadriven.get("bank_code"));
		modifyUnitUserAcct.setAcct_name(datadriven.get("acct_name"));
		modifyUnitUserAcct.setPrcptcd(datadriven.get("prcptcd"));		
		modifyUnitUserAcct.setRisk_item(datadriven.get("risk_item"));		
		modifyUnitUserAcct.setSign(GenSign.genSign(JSON.parseObject(JSON.toJSONString(modifyUnitUserAcct)),datadriven.get("key")));
		String reqJson2 = JSON.toJSONString(modifyUnitUserAcct);
		String modifyUnitUserAcctUrl = Property.get("llwallet.modifyUnitUserAcct.url.online");
				
		// 企业用户对公账户修改请求提交
		hf.nextRequest();
		hf.setUrl(modifyUnitUserAcctUrl);
		hf.addRequestBody(reqJson2);
		hf.Post();
		// 企业用户对公账户修改请求返回检查
		assert hf.getResponseBody().contains(datadriven.get("result_code1"));

		Reporter.log("企业用户对公账户修改接口测试： " + datadriven.get("comment"), true);
		System.out.println("===============================================");
	}
   
	@DataProvider(name = "modifyUnitUserAcct")
	public Iterator<Object[]> data4modifyUnitUserAcct() throws IOException {
		return new ExcelProvider(this, "modifyUnitUserAcct",4);
	}

	@DataProvider(name = "modifyUnitUserAcct1")
	public Iterator<Object[]> data4modifyUnitUserAcct1() throws IOException {
		return new ExcelProvider(this, "modifyUnitUserAcct1",11);
	}
	
	@AfterClass
	public void afterClass() {

	}

}
