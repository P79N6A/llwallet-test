package com.llwallet.interfaces.test.api.online.enterprise;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import org.testng.Reporter;
import org.testng.annotations.Test;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.llwallet.interfaces.bean.enterprise.ModifyUnitUser;
import com.llwallet.interfaces.bean.enterprise.PwdAuth;
import com.llwallet.interfaces.bean.personal.SmsCheck;
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
 * 企业用户基本信息修改接口测试
 */

public class ModifyUnitUserOnlineTest {

	@BeforeClass
	public void beforeClass() throws InterruptedException {
		Property.set();
	}

	@Test(description = "企业用户基本信息修改接口测试", timeOut = 60000, dataProvider = "modifyUnitUser")
	public void modifyUnitUser(Map<String, String> datadriven) throws Exception {

		Thread.sleep(1000);
		
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
		
		ModifyUnitUser modifyUnitUser = new ModifyUnitUser();
		modifyUnitUser.setOid_partner(datadriven.get("oid_partner"));
		modifyUnitUser.setSign_type(datadriven.get("sign_type"));
		modifyUnitUser.setToken(token1);
		modifyUnitUser.setUser_id(datadriven.get("user_id"));
		modifyUnitUser.setName_user(datadriven.get("name_user"));
		modifyUnitUser.setType_idcard(datadriven.get("type_idcard"));
		modifyUnitUser.setNo_idcard(datadriven.get("no_idcard"));
		modifyUnitUser.setAddr_unit(datadriven.get("addr_unit"));
		modifyUnitUser.setAddr_pro(datadriven.get("addr_pro"));
		modifyUnitUser.setAddr_city(datadriven.get("addr_city"));
		modifyUnitUser.setAddr_dist(datadriven.get("addr_dist"));
		modifyUnitUser.setExp_idcard(datadriven.get("exp_idcard"));
		modifyUnitUser.setBusi_user(datadriven.get("busi_user"));
		modifyUnitUser.setName_unit(datadriven.get("name_unit"));
		modifyUnitUser.setNum_license(datadriven.get("num_license1"));
		modifyUnitUser.setExp_license(datadriven.get("exp_license"));
		modifyUnitUser.setType_register(datadriven.get("type_register"));
		modifyUnitUser.setType_explicense(datadriven.get("type_explicense"));
		modifyUnitUser.setRisk_item(datadriven.get("risk_item"));		

		modifyUnitUser.setSign(GenSign.genSign(JSON.parseObject(JSON.toJSONString(modifyUnitUser)),datadriven.get("key")));
		String reqJson1 = JSON.toJSONString(modifyUnitUser);
		String modifyUnitUserUrl = Property.get("llwallet.modifyUnitUser.url.online");
				
		// 企业用户基本信息修改请求提交
		hf.nextRequest();
		hf.setUrl(modifyUnitUserUrl);
		hf.addRequestBody(reqJson1);
		hf.Post();
		// 企业用户基本信息修改请求返回检查
		assert hf.getResponseBody().contains(datadriven.get("result_code1"));

		Reporter.log("企业用户基本信息修改接口测试： " + datadriven.get("comment"), true);
		System.out.println("===============================================");
	}
	
	@Test(description = "企业用户基本信息修改接口测试", timeOut = 60000, dataProvider = "modifyUnitUser1")
	public void modifyUnitUser1(Map<String, String> datadriven) throws Exception {

		SmsCheck smsCheck1 = new SmsCheck();
		smsCheck1.setOid_partner(datadriven.get("oid_partner"));
		smsCheck1.setSign_type(datadriven.get("sign_type"));
		smsCheck1.setUser_id(datadriven.get("user_id"));
		smsCheck1.setToken(datadriven.get("token1"));
		smsCheck1.setVerify_code(datadriven.get("verify_code1"));
		smsCheck1.setSign(GenSign.genSign(JSON.parseObject(JSON.toJSONString(smsCheck1)), datadriven.get("key")));
		String reqJson1 = JSON.toJSONString(smsCheck1);
		String smsCheckUrl = Property.get("llwallet.smsCheck.url.online");

		// 钱包短信验证请求提交
		HttpFixture hf = new HttpFixture();
		hf.setEncode("UTF-8");
		hf.addHeaderValue("Content-Type", "application/json");
		hf.setUrl(smsCheckUrl);
		hf.addRequestBody(reqJson1);
		hf.Post();
		// 钱包短信验证请求返回检查
		assert hf.getResponseBody().contains(datadriven.get("result_code"));
		String token2 = JSONObject.parseObject(hf.getResponseBody()).getString("token");
		
		ModifyUnitUser modifyUnitUser = new ModifyUnitUser();
		modifyUnitUser.setOid_partner(datadriven.get("oid_partner"));
		modifyUnitUser.setSign_type(datadriven.get("sign_type"));
		modifyUnitUser.setToken(token2);
		modifyUnitUser.setUser_id(datadriven.get("user_id"));
		modifyUnitUser.setName_user(datadriven.get("name_user"));
		modifyUnitUser.setType_idcard(datadriven.get("type_idcard"));
		modifyUnitUser.setNo_idcard(datadriven.get("no_idcard"));
		modifyUnitUser.setAddr_unit(datadriven.get("addr_unit"));
		modifyUnitUser.setAddr_pro(datadriven.get("addr_pro"));
		modifyUnitUser.setAddr_city(datadriven.get("addr_city"));
		modifyUnitUser.setAddr_dist(datadriven.get("addr_dist"));
		modifyUnitUser.setExp_idcard(datadriven.get("exp_idcard"));
		modifyUnitUser.setBusi_user(datadriven.get("busi_user"));
		modifyUnitUser.setName_unit(datadriven.get("name_unit"));
		modifyUnitUser.setNum_license(datadriven.get("num_license1"));
		modifyUnitUser.setExp_license(datadriven.get("exp_license"));
		modifyUnitUser.setType_register(datadriven.get("type_register"));
		modifyUnitUser.setType_explicense(datadriven.get("type_explicense"));
		modifyUnitUser.setRisk_item(datadriven.get("risk_item"));		

		modifyUnitUser.setSign(GenSign.genSign(JSON.parseObject(JSON.toJSONString(modifyUnitUser)),datadriven.get("key")));
		String reqJson2 = JSON.toJSONString(modifyUnitUser);
		String modifyUnitUserUrl = Property.get("llwallet.modifyUnitUser.url.online");
				
		// 企业用户基本信息修改请求提交
		hf.nextRequest();
		hf.setUrl(modifyUnitUserUrl);
		hf.addRequestBody(reqJson2);
		hf.Post();
		// 企业用户基本信息修改请求返回检查
		assert hf.getResponseBody().contains(datadriven.get("result_code1"));

		Reporter.log("企业用户基本信息修改接口测试： " + datadriven.get("comment"), true);
		System.out.println("===============================================");
	}
   
	@DataProvider(name = "modifyUnitUser")
	public Iterator<Object[]> data4modifyUnitUser() throws IOException {
		return new ExcelProvider(this, "modifyUnitUser",3);
	}

	@DataProvider(name = "modifyUnitUser1")
	public Iterator<Object[]> data4modifyUnitUser1() throws IOException {
		return new ExcelProvider(this, "modifyUnitUser1",12);
	}
	
	@AfterClass
	public void afterClass() {

	}

}
