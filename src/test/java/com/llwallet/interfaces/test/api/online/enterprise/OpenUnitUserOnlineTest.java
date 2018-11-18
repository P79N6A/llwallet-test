package com.llwallet.interfaces.test.api.online.enterprise;

import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import org.testng.Reporter;
import org.testng.annotations.Test;
import com.alibaba.fastjson.JSON;
import com.llwallet.interfaces.bean.enterprise.OpenUnitUser;
import com.tools.dataprovider.ExcelProvider;
import com.tools.http.HttpFixture;
import com.tools.utils.ApiUtils;
import com.tools.utils.GenSign;
import com.tools.utils.Property;
import com.tools.utils.RSAUtil;
import com.tools.utils.SampleFileUtils;
//import com.lianpay.util.RSAUtil;
//import com.lianpay.util.RSAUtil2;
import org.testng.annotations.DataProvider;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;

/*
 * @author jiangxm
 * 企业用户直接开户接口测试
 */

public class OpenUnitUserOnlineTest {

	@BeforeClass
	public void beforeClass() throws InterruptedException {
		Property.set();
	}

	@Test(description = "企业用户直接开户接口测试", timeOut = 60000, dataProvider = "openUnitUser")
	public void openUnitUser(Map<String, String> datadriven) throws Exception {

		String oid_partner = datadriven.get("oid_partner");
		String user_id = null;
		if ("auto".equals(datadriven.get("user_id"))){
			user_id = ApiUtils.getCurrentDateMillisecondStr();
		}else{
			user_id = datadriven.get("user_id");
		}
		String eml_bind = user_id+"@yintong.com.cn";
		String mob_bind = ApiUtils.getMobStr();
		
		// 组织请求报文
		OpenUnitUser openUnitUser = new OpenUnitUser();
		openUnitUser.setOid_partner(oid_partner);
		openUnitUser.setSign_type(datadriven.get("sign_type"));
		openUnitUser.setUser_id(user_id);		
		if ("auto".equals(datadriven.get("mob_bind"))){
			openUnitUser.setMob_bind(mob_bind);
		}else{
			openUnitUser.setMob_bind(datadriven.get("mob_bind"));
		}		
		openUnitUser.setName_user(datadriven.get("name_user"));
		openUnitUser.setType_idcard(datadriven.get("type_idcard"));
		openUnitUser.setExp_idcard(datadriven.get("exp_idcard"));
		openUnitUser.setType_expidcard(datadriven.get("type_expidcard"));
		openUnitUser.setNo_idcard(datadriven.get("no_idcard"));
		openUnitUser.setName_agent(datadriven.get("name_agent"));
		openUnitUser.setIdno_agent(datadriven.get("idno_agent"));
		openUnitUser.setExp_idcard_agent(datadriven.get("exp_idcard_agent"));
		openUnitUser.setType_agentexpidcard(datadriven.get("type_agentexpidcard"));
		if (datadriven.get("pwd_login").length() != 0) {
			openUnitUser.setPwd_login(RSAUtil.encrypt(datadriven.get("pwd_login"), Property.get("rsa_pub_key")));
		} else {
			openUnitUser.setPwd_login(datadriven.get("pwd_login"));
		}
		if (datadriven.get("pwd_pay").length() != 0) {
			openUnitUser.setPwd_pay(RSAUtil.encrypt(datadriven.get("pwd_pay"), Property.get("rsa_pub_key")));
		} else {
			openUnitUser.setPwd_pay(datadriven.get("pwd_pay"));
		}
		if ("auto".equals(datadriven.get("eml_bind"))){
			openUnitUser.setEml_bind(eml_bind);
		}else{
			openUnitUser.setEml_bind(datadriven.get("eml_bind"));
		}
		openUnitUser.setAddr_unit(datadriven.get("addr_unit"));
		openUnitUser.setAddr_pro(datadriven.get("addr_pro"));
		openUnitUser.setAddr_city(datadriven.get("addr_city"));
		openUnitUser.setAddr_dist(datadriven.get("addr_dist"));
		openUnitUser.setBusi_user(datadriven.get("busi_user"));
		openUnitUser.setName_unit(datadriven.get("name_unit"));
		openUnitUser.setNum_license(datadriven.get("num_license"));
		openUnitUser.setExp_license(datadriven.get("exp_license"));
		openUnitUser.setOrg_code(datadriven.get("org_code"));
		openUnitUser.setType_register(datadriven.get("type_register"));
		openUnitUser.setType_license(datadriven.get("type_license"));
		openUnitUser.setType_explicense(datadriven.get("type_explicense"));
		openUnitUser.setType_industry(datadriven.get("type_industry"));
		openUnitUser.setExp_orgcode(datadriven.get("exp_orgcode"));
		openUnitUser.setCity_code(datadriven.get("city_code"));
		openUnitUser.setBrabank_name(datadriven.get("brabank_name"));
		openUnitUser.setCard_no(datadriven.get("card_no"));
		openUnitUser.setBank_code(datadriven.get("bank_code"));
		openUnitUser.setPrcptcd(datadriven.get("prcptcd"));
		openUnitUser.setAcct_name(datadriven.get("acct_name"));
		openUnitUser.setFlag_check(datadriven.get("flag_check"));
		openUnitUser.setRisk_item(datadriven.get("risk_item"));
		openUnitUser.setSign(GenSign.genSign(JSON.parseObject(JSON.toJSONString(openUnitUser)), datadriven.get("key")));
		String reqJson = JSON.toJSONString(openUnitUser);
		String openUnitUserUrl = Property.get("llwallet.openUnitUser.url.online");

		// 企业用户直接开户请求提交
		HttpFixture hf = new HttpFixture();
		hf.setEncode("UTF-8");
		hf.setUrl(openUnitUserUrl);
		hf.addHeaderValue("Content-Type", "application/json");
		hf.addRequestBody(reqJson);
		hf.Post();

		// response写入文件
		SampleFileUtils.appendLine(Property.get("openUnitUserFilepath"), new Date().toString());  
		SampleFileUtils.appendLine(Property.get("openUnitUserFilepath"), reqJson);
		SampleFileUtils.appendLine(Property.get("openUnitUserFilepath"), hf.getResponseBody());
		SampleFileUtils.appendLine(Property.get("openUnitUserFilepath"),
				"===============================================");
		// 企业用户直接开户请求返回检查
		assert hf.getResponseBody().contains(datadriven.get("result_code"));

		Reporter.log("企业用户直接开户接口测试： " + datadriven.get("comment"), true);
		System.out.println("===============================================");
	}
	
	@DataProvider(name = "openUnitUser")
	public Iterator<Object[]> data4openUnitUser() throws IOException {
		 return new ExcelProvider(this, "openUnitUser",41);
	}
	
	@AfterClass
	public void afterClass() {

	}

}
