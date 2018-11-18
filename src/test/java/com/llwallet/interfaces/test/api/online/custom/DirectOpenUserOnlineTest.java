package com.llwallet.interfaces.test.api.online.custom;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import org.testng.Reporter;
import org.testng.annotations.Test;
import com.alibaba.fastjson.JSON;
import com.llwallet.interfaces.bean.custom.DirectOpenUser;
import com.tools.dataprovider.ExcelProvider;
import com.tools.http.HttpRequest;
import com.tools.utils.GenSign;
import com.tools.utils.IdCardGenerator;
import com.tools.utils.MyConfig;
import com.tools.utils.Property;
import com.tools.utils.RSAUtil;
import org.testng.annotations.DataProvider;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;

/*
 * @author jiangxm
 * 个人用户直接开户接口测试
 */

public class DirectOpenUserOnlineTest {

	@BeforeClass
	public void beforeClass() throws InterruptedException {
		Property.set();
	}

	@Test(description = "个人用户直接开户接口测试", timeOut = 60000, dataProvider = "directOpenUser")
	public void directOpenUser(Map<String, String> datadriven) throws Exception {
        
		Thread.sleep(1000);
		String user_id = MyConfig.getUser(datadriven.get("user_id"));
		String mob_bind = MyConfig.getMobBind(datadriven.get("mob_bind"));
		String eml_bind = user_id+"@yintong.com.cn";
		String oid_partner = MyConfig.getPartner(datadriven.get("oid_partner"));
		IdCardGenerator idCardGenerator = new IdCardGenerator();
		String  no_idcard= idCardGenerator.generate();
		
		// 组织请求报文
		DirectOpenUser directOpenUser = new DirectOpenUser();
		directOpenUser.setOid_partner(oid_partner);
		directOpenUser.setSign_type(datadriven.get("sign_type"));
		directOpenUser.setUser_id(user_id);
		directOpenUser.setMob_bind(mob_bind);
		directOpenUser.setName_user(datadriven.get("name_user"));
		directOpenUser.setType_idcard(datadriven.get("type_idcard"));
		if ("auto".equals(datadriven.get("no_idcard"))){
			directOpenUser.setNo_idcard(no_idcard);
		}else{
			directOpenUser.setNo_idcard(datadriven.get("no_idcard"));
		}
		directOpenUser.setExp_idcard(datadriven.get("exp_idcard"));
		directOpenUser.setType_expidcard(datadriven.get("type_expidcard"));
		if (datadriven.get("pwd_login").length() != 0) {
			directOpenUser.setPwd_login(RSAUtil.encrypt(datadriven.get("pwd_login"), Property.get("rsa_pub_key")));
		} else {
			directOpenUser.setPwd_login(datadriven.get("pwd_login"));
		}
		if (datadriven.get("pwd_pay").length() != 0) {
			directOpenUser.setPwd_pay(RSAUtil.encrypt(datadriven.get("pwd_pay"), Property.get("rsa_pub_key")));
		} else {
			directOpenUser.setPwd_pay(datadriven.get("pwd_pay"));
		}
		if ("auto".equals(datadriven.get("eml_bind"))){
			directOpenUser.setEml_bind(eml_bind);
		}else{
			directOpenUser.setEml_bind(datadriven.get("eml_bind"));
		}
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
		String directOpenUserUrl = Property.get("llwallet.directOpenUser.url.online");

		// 个人用户直接开户请求提交
		String rsp = HttpRequest.httpPostWithJSON(directOpenUserUrl, reqJson);
		// 个人用户直接开户请求返回检查
		assert rsp.contains(datadriven.get("result_code"));

		Reporter.log("个人用户直接开户接口测试： " + datadriven.get("comment"), true);
		System.out.println("===============================================");
	}
	
	@DataProvider(name = "directOpenUser")
	public Iterator<Object[]> data4directOpenUser() throws IOException {
		 return new ExcelProvider(this, "directOpenUser",54);
	}

	@AfterClass
	public void afterClass() {

	}

}
