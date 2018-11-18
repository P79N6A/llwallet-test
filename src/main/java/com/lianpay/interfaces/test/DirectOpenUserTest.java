package com.lianpay.interfaces.test;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import org.testng.Reporter;
import org.testng.annotations.Test;
import com.alibaba.fastjson.JSON;
import com.lianpay.interfaces.bean.DirectOpenUser;
import com.lianpay.interfaces.dao.DbUtils;
import com.lianpay.tools.dataprovider.ExcelProvider;
import com.lianpay.tools.http.HttpFixture;
import com.lianpay.tools.utils.GenSign;
import com.lianpay.tools.utils.Property;
import com.lianpay.tools.utils.TelnetUtil;
import com.lianpay.util.RSAUtil2;
import org.testng.annotations.DataProvider;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;

/*
 * @author jiangxm
 * 个人用户直接开户接口测试
 */

public class DirectOpenUserTest {

	@BeforeClass
	public void beforeClass() throws InterruptedException {
		Property.set();
	}

	@Test(description = "个人用户直接开户接口测试", timeOut = 60000, dataProvider = "directOpenUser")
	public void directOpenUser(Map<String, String> datadriven) throws Exception {

		String user_id = datadriven.get("user_id");
		String oid_partner = datadriven.get("oid_partner");
		// 删除用户测试数据
		DbUtils dbUtils = new DbUtils();
		String oid_userno = dbUtils.queryOidUserNo(oid_partner, user_id).getOid_userno();
		dbUtils.deleteUser(oid_userno);

		// 清除用户缓存
		TelnetUtil telnetUtil = new TelnetUtil(Property.get("host"), Integer.parseInt(Property.get("port")));
		StringBuffer command = new StringBuffer();
		command.append("delete ").append("TRADER_").append(user_id).append(oid_partner).append("llwalltet_userbase");
		telnetUtil.execute(command.toString());
		telnetUtil.disconnect();

		// 组织请求报文
		DirectOpenUser directOpenUser = new DirectOpenUser();
		directOpenUser.setOid_partner(oid_partner);
		directOpenUser.setSign_type(datadriven.get("sign_type"));
		directOpenUser.setUser_id(user_id);
		directOpenUser.setMob_bind(datadriven.get("mob_bind"));
		directOpenUser.setName_user(datadriven.get("name_user"));
		directOpenUser.setType_idcard(datadriven.get("type_idcard"));
		directOpenUser.setNo_idcard(datadriven.get("no_idcard"));
		directOpenUser.setExp_idcard(datadriven.get("exp_idcard"));
		directOpenUser.setType_expidcard(datadriven.get("type_expidcard"));
		if (datadriven.get("pwd_login").length() != 0) {
			directOpenUser.setPwd_login(RSAUtil2.encrypt(datadriven.get("pwd_login"), Property.get("rsa_pub_key")));
		} else {
			directOpenUser.setPwd_login(datadriven.get("pwd_login"));
		}
		if (datadriven.get("pwd_pay").length() != 0) {
			directOpenUser.setPwd_pay(RSAUtil2.encrypt(datadriven.get("pwd_pay"), Property.get("rsa_pub_key")));
		} else {
			directOpenUser.setPwd_pay(datadriven.get("pwd_pay"));
		}
		directOpenUser.setEml_bind(datadriven.get("eml_bind"));
		directOpenUser.setAddr_conn(datadriven.get("addr_conn"));
		directOpenUser.setAddr_pro(datadriven.get("addr_pro"));
		directOpenUser.setAddr_city(datadriven.get("addr_city"));
		directOpenUser.setAddr_dist(datadriven.get("addr_dist"));		
		directOpenUser.setNation_user(datadriven.get("nation_user"));
		directOpenUser.setFlg_sex(datadriven.get("flg_sex"));
		directOpenUser.setOid_job(datadriven.get("oid_job"));
		directOpenUser.setFlag_auth(datadriven.get("flag_auth"));		
		directOpenUser.setFlag_check(datadriven.get("flag_check"));
		directOpenUser.setRisk_item(datadriven.get("risk_item"));
		directOpenUser.setSign(GenSign.genSign(JSON.parseObject(JSON.toJSONString(directOpenUser)), datadriven.get("key")));
		String reqJson = JSON.toJSONString(directOpenUser);
		String directOpenUserUrl = Property.get("llwallet.directOpenUser.url");

		// 个人用户直接开户请求提交
		HttpFixture hf = new HttpFixture();
		hf.setEncode("UTF-8");
		hf.setUrl(directOpenUserUrl);
		hf.addHeaderValue("Content-Type", "application/json");
		hf.addRequestBody(reqJson);
		hf.Post();
		// 个人用户直接开户请求返回检查
		assert hf.getResponseBody().contains(datadriven.get("result_code"));

		Reporter.log("个人用户直接开户接口测试： " + datadriven.get("comment"), true);
		Reporter.log("===============================================", true);
	}
	
	@DataProvider(name = "directOpenUser")
	public Iterator<Object[]> data4directOpenUser() throws IOException {
		 return new ExcelProvider(this, "directOpenUser",4);
	}

	@AfterClass
	public void afterClass() {

	}

}
