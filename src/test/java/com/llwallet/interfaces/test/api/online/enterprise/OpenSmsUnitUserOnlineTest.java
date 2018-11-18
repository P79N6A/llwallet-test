package com.llwallet.interfaces.test.api.online.enterprise;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import org.testng.Reporter;
import org.testng.annotations.Test;
import com.alibaba.fastjson.JSON;
import com.llwallet.interfaces.bean.enterprise.OpenSmsUnitUser;
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
 * 企业用户短信开户接口测试
 */

public class OpenSmsUnitUserOnlineTest {

	@BeforeClass
	public void beforeClass() throws InterruptedException {
		Property.set();
	}

	@Test(description = "企业用户短信开户接口测试", timeOut = 60000, dataProvider = "openSmsUnitUser")
	public void openSmsUnitUser(Map<String, String> datadriven) throws Exception {

		String user_id = datadriven.get("user_id");
		String mob_bind = datadriven.get("mob_bind");
		String oid_partner = datadriven.get("oid_partner");

		OpenSmsUnitUser openSmsUnitUser = new OpenSmsUnitUser();
		openSmsUnitUser.setOid_partner(oid_partner);
		openSmsUnitUser.setSign_type(datadriven.get("sign_type"));
		openSmsUnitUser.setUser_id(user_id);
		openSmsUnitUser.setMob_bind(mob_bind);
		openSmsUnitUser.setToken(datadriven.get("token"));
		openSmsUnitUser.setName_user(datadriven.get("name_user"));
		openSmsUnitUser.setType_idcard(datadriven.get("type_idcard"));
		openSmsUnitUser.setExp_idcard(datadriven.get("exp_idcard"));
		openSmsUnitUser.setType_expidcard(datadriven.get("type_expidcard"));
		openSmsUnitUser.setNo_idcard(datadriven.get("no_idcard"));
		openSmsUnitUser.setName_agent(datadriven.get("name_agent"));
		openSmsUnitUser.setIdno_agent(datadriven.get("idno_agent"));
		openSmsUnitUser.setExp_idcard_agent(datadriven.get("exp_idcard_agent"));
		openSmsUnitUser.setType_agentexpidcard(datadriven.get("type_agentexpidcard"));
		if (datadriven.get("pwd_login").length() != 0) {
			openSmsUnitUser.setPwd_login(RSAUtil.encrypt(datadriven.get("pwd_login"), Property.get("rsa_pub_key")));
		} else {
			openSmsUnitUser.setPwd_login(datadriven.get("pwd_login"));
		}
		if (datadriven.get("pwd_pay").length() != 0) {
			openSmsUnitUser.setPwd_pay(RSAUtil.encrypt(datadriven.get("pwd_pay"), Property.get("rsa_pub_key")));
		} else {
			openSmsUnitUser.setPwd_pay(datadriven.get("pwd_pay"));
		}
		openSmsUnitUser.setEml_bind(datadriven.get("eml_bind"));
		openSmsUnitUser.setAddr_unit(datadriven.get("addr_unit"));
		openSmsUnitUser.setAddr_pro(datadriven.get("addr_pro"));
		openSmsUnitUser.setAddr_city(datadriven.get("addr_city"));
		openSmsUnitUser.setAddr_dist(datadriven.get("addr_dist"));
		openSmsUnitUser.setBusi_user(datadriven.get("busi_user"));
		openSmsUnitUser.setName_unit(datadriven.get("name_unit"));
		openSmsUnitUser.setNum_license(datadriven.get("num_license"));
		openSmsUnitUser.setExp_license(datadriven.get("exp_license"));
		openSmsUnitUser.setOrg_code(datadriven.get("org_code"));
		openSmsUnitUser.setType_register(datadriven.get("type_register"));
		openSmsUnitUser.setType_license(datadriven.get("type_license"));
		openSmsUnitUser.setType_explicense(datadriven.get("type_explicense"));
		openSmsUnitUser.setType_industry(datadriven.get("type_industry"));
		openSmsUnitUser.setExp_orgcode(datadriven.get("exp_orgcode"));
		openSmsUnitUser.setCity_code(datadriven.get("city_code"));
		openSmsUnitUser.setBrabank_name(datadriven.get("brabank_name"));
		openSmsUnitUser.setCard_no(datadriven.get("card_no"));
		openSmsUnitUser.setBank_code(datadriven.get("bank_code"));
		openSmsUnitUser.setPrcptcd(datadriven.get("prcptcd"));
		openSmsUnitUser.setRisk_item(datadriven.get("risk_item"));
		openSmsUnitUser.setSign(GenSign.genSign(JSON.parseObject(JSON.toJSONString(openSmsUnitUser)),datadriven.get("key")));
		
		String reqJson = JSON.toJSONString(openSmsUnitUser);
		String openSmsUnitUserUrl = Property.get("llwallet.openSmsUnitUser.url.online");

		// 企业用户短信开户请求提交
		HttpFixture hf = new HttpFixture();
		hf.setEncode("UTF-8");
		hf.setUrl(openSmsUnitUserUrl);
		hf.addHeaderValue("Content-Type", "application/json");
		hf.addRequestBody(reqJson);
		hf.Post();
		// 企业用户短信开户请求返回检查
		assert hf.getResponseBody().contains(datadriven.get("result_code"));

		Reporter.log("企业用户短信开户接口测试： " + datadriven.get("comment"), true);
		System.out.println("===============================================");
	}

	
	@DataProvider(name = "openSmsUnitUser")
	public Iterator<Object[]> data4openSmsUnitUser() throws IOException {
		return new ExcelProvider(this, "openSmsUnitUser",2);
	}	

	@AfterClass
	public void afterClass() {

	}

}
