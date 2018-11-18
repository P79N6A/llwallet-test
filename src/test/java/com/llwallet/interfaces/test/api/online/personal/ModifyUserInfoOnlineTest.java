package com.llwallet.interfaces.test.api.online.personal;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.testng.Reporter;
import org.testng.annotations.Test;
import com.alibaba.fastjson.JSON;
import com.llwallet.interfaces.bean.enterprise.PwdAuth;
import com.llwallet.interfaces.bean.personal.ModifyUserInfo;
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
 * 用户基本信息修改接口测试
 */

public class ModifyUserInfoOnlineTest {

	@BeforeClass
	public void beforeClass() throws InterruptedException {
		Property.set();
	}

	@Test(description = "用户基本信息修改接口测试", timeOut = 60000, dataProvider = "modifyUserInfo")
	public void modifyUserInfo(Map<String, String> datadriven) throws Exception {

		//钱包支付密码验证授权
		PwdAuth pwdAuth = new PwdAuth();
		pwdAuth.setOid_partner(datadriven.get("oid_partner"));
		pwdAuth.setSign_type(datadriven.get("sign_type"));
		pwdAuth.setUser_id(datadriven.get("user_id"));
		if (datadriven.get("pwd_pay").length() != 0) {
			pwdAuth.setPwd_pay(RSAUtil.encrypt(datadriven.get("pwd_pay"), Property.get("rsa_pub_key")));
		} else {
			pwdAuth.setPwd_pay(datadriven.get("pwd_pay"));
		}
		pwdAuth.setNo_idcard(datadriven.get("no_idcard"));
		pwdAuth.setFlag_check(datadriven.get("flag_check1"));
		pwdAuth.setMob_bind(datadriven.get("mob_bind"));
		pwdAuth.setRisk_item(datadriven.get("risk_item"));
		pwdAuth.setSign(GenSign.genSign(JSON.parseObject(JSON.toJSONString(pwdAuth)),datadriven.get("key")));
		String reqJson = JSON.toJSONString(pwdAuth);
		String pwdAuthUrl = Property.get("llwallet.pwdAuth.url.online");
		
		// 钱包支付密码验证授权请求提交
		HttpFixture hf = new HttpFixture();
		hf.setEncode("UTF-8");
		hf.addHeaderValue("Content-Type", "application/json");
		hf.setUrl(pwdAuthUrl);
		hf.addRequestBody(reqJson);
		hf.Post();
		// 钱包短信验证请求返回检查
		assert hf.getResponseBody().contains(datadriven.get("result_code"));
		String token = StringUtils.substringBetween(hf.getResponseBody(), "token\":\"", "\"");
	
		// 组织修改用户基本信息请求报文
		ModifyUserInfo modifyUserInfo = new ModifyUserInfo();
		modifyUserInfo.setOid_partner(datadriven.get("oid_partner"));
		modifyUserInfo.setSign_type(datadriven.get("sign_type"));
		modifyUserInfo.setUser_id(datadriven.get("user_id"));
		modifyUserInfo.setToken(token);
//		modifyUserInfo.setName_user(datadriven.get("name_user"));
//		modifyUserInfo.setType_idcard(datadriven.get("type_idcard"));
//		modifyUserInfo.setNo_idcard(datadriven.get("no_idcard"));
		modifyUserInfo.setExp_idcard(datadriven.get("exp_idcard1"));
//		modifyUserInfo.setFlg_sex(datadriven.get("flg_sex"));
		modifyUserInfo.setAddr_conn(datadriven.get("addr_conn1"));
		modifyUserInfo.setAddr_pro(datadriven.get("addr_pro1"));
		modifyUserInfo.setAddr_city(datadriven.get("addr_city1"));
		modifyUserInfo.setAddr_dist(datadriven.get("addr_dist1"));		
		modifyUserInfo.setOid_job(datadriven.get("oid_job1"));
		modifyUserInfo.setRisk_item(datadriven.get("risk_item"));
		modifyUserInfo.setSign(GenSign.genSign(JSON.parseObject(JSON.toJSONString(modifyUserInfo)), datadriven.get("key")));
		String reqJson1 = JSON.toJSONString(modifyUserInfo);
		String modifyUserInfoUrl = Property.get("llwallet.modifyUserInfo.url.online");

		// 用户基本信息修改请求提交
		hf.nextRequest();
		hf.setUrl(modifyUserInfoUrl);
		hf.addRequestBody(reqJson1);
		hf.Post();
		// 用户基本信息修改请求返回检查
		assert hf.getResponseBody().contains(datadriven.get("result_code1"));

		Reporter.log("用户基本信息修改接口测试： " + datadriven.get("comment"), true);
		System.out.println("===============================================");
	}
	
	@DataProvider(name = "modifyUserInfo")
	public Iterator<Object[]> data4modifyUserInfo() throws IOException {
		 return new ExcelProvider(this, "modifyUserInfo",4);
	}
	
	@AfterClass
	public void afterClass() {

	}

}