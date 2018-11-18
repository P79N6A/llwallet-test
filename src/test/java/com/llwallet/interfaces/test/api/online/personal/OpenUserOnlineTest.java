package com.llwallet.interfaces.test.api.online.personal;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import org.testng.Reporter;
import org.testng.annotations.Test;
import com.alibaba.fastjson.JSON;
import com.llwallet.interfaces.bean.personal.OpenUser;
import com.tools.dataprovider.ExcelProvider;
import com.tools.http.HttpFixture;
import com.tools.utils.ApiUtils;
import com.tools.utils.GenSign;
import com.tools.utils.Property;
import com.tools.utils.RSAUtil;
import org.testng.annotations.DataProvider;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;

/*
 * @author jiangxm
 * 个人用户开户接口测试
 */

public class OpenUserOnlineTest {

	@BeforeClass
	public void beforeClass() throws InterruptedException {
		Property.set();
	}

	@Test(description = "个人用户开户接口测试", timeOut = 60000, dataProvider = "openUser")
	public void openUser(Map<String, String> datadriven) throws Exception {

		Thread.sleep(1000);
		String user_id = ApiUtils.getCurrentDateMillisecondStr();		
		String eml_bind = user_id+"@yintong.com.cn";
		String oid_partner = datadriven.get("oid_partner");		
		// 组织请求报文
		OpenUser openUser = new OpenUser();
		openUser.setOid_partner(oid_partner);
		openUser.setSign_type(datadriven.get("sign_type"));
		if ("auto".equals(datadriven.get("user_id"))){
			openUser.setUser_id(user_id);
		}else{
			openUser.setUser_id(datadriven.get("user_id"));
		}
		openUser.setName_user(datadriven.get("name_user"));
		openUser.setType_idcard(datadriven.get("type_idcard"));
		openUser.setNo_idcard(datadriven.get("no_idcard"));
		openUser.setExp_idcard(datadriven.get("exp_idcard"));
		openUser.setType_expidcard(datadriven.get("type_expidcard"));
		if (datadriven.get("pwd_login").length() != 0) {
			openUser.setPwd_login(RSAUtil.encrypt(datadriven.get("pwd_login"), Property.get("rsa_pub_key")));
		} else {
			openUser.setPwd_login(datadriven.get("pwd_login"));
		}
		if (datadriven.get("pwd_pay").length() != 0) {
			openUser.setPwd_pay(RSAUtil.encrypt(datadriven.get("pwd_pay"), Property.get("rsa_pub_key")));
		} else {
			openUser.setPwd_pay(datadriven.get("pwd_pay"));
		}		
		if ("auto".equals(datadriven.get("eml_bind"))){
			openUser.setEml_bind(eml_bind);
		}else{
			openUser.setEml_bind(datadriven.get("eml_bind"));
		}
		openUser.setAddr_conn(datadriven.get("addr_conn"));
		openUser.setAddr_pro(datadriven.get("addr_pro"));
		openUser.setAddr_city(datadriven.get("addr_city"));
		openUser.setAddr_dist(datadriven.get("addr_dist"));		
		openUser.setNation_user(datadriven.get("nation_user"));
		openUser.setFlg_sex(datadriven.get("flg_sex"));
		openUser.setOid_job(datadriven.get("oid_job"));
		openUser.setFlag_auth(datadriven.get("flag_auth"));		
		openUser.setRisk_item(datadriven.get("risk_item"));
		openUser.setToken(datadriven.get("token"));
		openUser.setSign(GenSign.genSign(JSON.parseObject(JSON.toJSONString(openUser)), datadriven.get("key")));
		String reqJson = JSON.toJSONString(openUser);
		String openUserUrl = Property.get("llwallet.openUser.url.online");

		// 个人用户开户请求提交		
		HttpFixture hf = new HttpFixture();
		hf.setEncode("UTF-8");
		hf.setUrl("" + openUserUrl + "");
		hf.addHeaderValue("Content-Type", "application/json");
		hf.addRequestBody(reqJson);
		hf.Post();
		// 个人用户开户请求返回检查
		assert hf.getResponseBody().contains(datadriven.get("result_code"));

		Reporter.log("个人用户开户接口测试： " + datadriven.get("comment"), true);
		System.out.println("===============================================");
	}
	
	@DataProvider(name = "openUser")
	public Iterator<Object[]> data4openUser() throws IOException {
		 return new ExcelProvider(this, "openUser",1);
	}

	@AfterClass
	public void afterClass() {

	}

}
