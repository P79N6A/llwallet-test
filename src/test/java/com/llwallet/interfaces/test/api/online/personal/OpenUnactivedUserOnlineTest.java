package com.llwallet.interfaces.test.api.online.personal;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import org.testng.Reporter;
import org.testng.annotations.Test;
import com.alibaba.fastjson.JSON;
import com.llwallet.interfaces.bean.personal.OpenUnactivedUser;
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
 * 个人用户待激活开户接口测试
 */

public class OpenUnactivedUserOnlineTest {

	@BeforeClass
	public void beforeClass() throws InterruptedException {
		Property.set();
	}

	@Test(description = "个人用户待激活开户接口测试", timeOut = 60000, dataProvider = "openUnactivedUser")
	public void openUnactivedUser(Map<String, String> datadriven) throws Exception {

		Thread.sleep(1000);
		String user_id = ApiUtils.getCurrentDateMillisecondStr();
		String mob_bind = ApiUtils.getMobStr();
		String eml_bind = user_id+"@yintong.com.cn";
		String oid_partner = datadriven.get("oid_partner");	

		// 组织请求报文
		OpenUnactivedUser openUnactivedUser = new OpenUnactivedUser();
		openUnactivedUser.setOid_partner(oid_partner);
		openUnactivedUser.setSign_type(datadriven.get("sign_type"));
		if ("auto".equals(datadriven.get("user_id"))){
			openUnactivedUser.setUser_id(user_id);
		}else{
			openUnactivedUser.setUser_id(datadriven.get("user_id"));
		}
		if ("auto".equals(datadriven.get("mob_bind"))){
			openUnactivedUser.setMob_bind(mob_bind);
		}else{
			openUnactivedUser.setMob_bind(datadriven.get("mob_bind"));
		}		
		openUnactivedUser.setName_user(datadriven.get("name_user"));
		openUnactivedUser.setType_idcard(datadriven.get("type_idcard"));
		openUnactivedUser.setNo_idcard(datadriven.get("no_idcard"));
		openUnactivedUser.setExp_idcard(datadriven.get("exp_idcard"));
		openUnactivedUser.setType_expidcard(datadriven.get("type_expidcard"));
		if (datadriven.get("pwd_login").length() != 0) {
			openUnactivedUser.setPwd_login(RSAUtil.encrypt(datadriven.get("pwd_login"), Property.get("rsa_pub_key")));
		} else {
			openUnactivedUser.setPwd_login(datadriven.get("pwd_login"));
		}
		if ("auto".equals(datadriven.get("eml_bind"))){
			openUnactivedUser.setEml_bind(eml_bind);
		}else{
			openUnactivedUser.setEml_bind(datadriven.get("eml_bind"));
		}
		openUnactivedUser.setAddr_conn(datadriven.get("addr_conn"));
		openUnactivedUser.setAddr_pro(datadriven.get("addr_pro"));
		openUnactivedUser.setAddr_city(datadriven.get("addr_city"));
		openUnactivedUser.setAddr_dist(datadriven.get("addr_dist"));		
		openUnactivedUser.setFlg_sex(datadriven.get("flg_sex"));
		openUnactivedUser.setOid_job(datadriven.get("oid_job"));
		openUnactivedUser.setFlag_auth(datadriven.get("flag_auth"));		
		openUnactivedUser.setFlag_check(datadriven.get("flag_check"));
		openUnactivedUser.setRisk_item(datadriven.get("risk_item"));
		openUnactivedUser.setSign(GenSign.genSign(JSON.parseObject(JSON.toJSONString(openUnactivedUser)), datadriven.get("key")));
		String reqJson = JSON.toJSONString(openUnactivedUser);
		String openUnactivedUserUrl = Property.get("llwallet.openUnactivedUser.url.online");

		// 个人用户待激活开户请求提交
		HttpFixture hf = new HttpFixture();
		hf.setEncode("UTF-8");
		hf.setUrl(openUnactivedUserUrl);
		hf.addHeaderValue("Content-Type", "application/json");
		hf.addRequestBody(reqJson);
		hf.Post();
		// 个人用户待激活开户请求返回检查
		assert hf.getResponseBody().contains(datadriven.get("result_code"));

		Reporter.log("个人用户待激活开户接口测试： " + datadriven.get("comment"), true);
		System.out.println("===============================================");
	}
	
	@DataProvider(name = "openUnactivedUser")
	public Iterator<Object[]> data4openUnactivedUser() throws IOException {
		 return new ExcelProvider(this, "openUnactivedUser",5);
	}

	@AfterClass
	public void afterClass() {

	}

}
