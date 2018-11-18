package com.llwallet.interfaces.test.api.online.custom;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import org.testng.Reporter;
import org.testng.annotations.Test;
import com.alibaba.fastjson.JSON;
import com.llwallet.interfaces.bean.custom.AddUserVbAccount;
import com.tools.dataprovider.ExcelProvider;
import com.tools.http.HttpRequest;
import com.tools.utils.ExcelUtil;
import com.tools.utils.GenSign;
import com.tools.utils.MyConfig;
import com.tools.utils.Property;
import com.tools.utils.RSAUtil;
import org.testng.annotations.DataProvider;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;

/*
 * @author jiangxm
 * 开通企业用户虚拟账户接口测试
 */

public class AddUserVbAccountOnlineTest {

	ExcelUtil excelUtil = new ExcelUtil();
	
	@BeforeClass
	public void beforeClass() throws Exception {
		Property.set();
//		excelUtil.prepare4Excel(this, "addUserVbAccount");
	}

	@Test(description = "开通企业用户虚拟账户接口测试", timeOut = 60000, dataProvider = "addUserVbAccount")
	public void addUserVbAccount(Map<String, String> datadriven) throws Exception {
	
		AddUserVbAccount addUserVbAccount = new AddUserVbAccount();
		addUserVbAccount.setOid_partner(MyConfig.getPartner(datadriven.get("oid_partner")));
		addUserVbAccount.setSign_type(datadriven.get("sign_type"));
		addUserVbAccount.setUser_id(MyConfig.getUser(datadriven.get("user_id")));
		addUserVbAccount.setNotify_url(datadriven.get("notify_url"));
		addUserVbAccount.setBank_code(datadriven.get("bank_code"));
		if (datadriven.get("pwd_pay").length() != 0) {
			addUserVbAccount.setPwd_pay(RSAUtil.encrypt(datadriven.get("pwd_pay"), Property.get("rsa_pub_key")));
		} else {

		}
		addUserVbAccount.setCheck_name(datadriven.get("check_name"));
		addUserVbAccount
				.setSign(GenSign.genSign(JSON.parseObject(JSON.toJSONString(addUserVbAccount)), datadriven.get("key")));
		String reqJson = JSON.toJSONString(addUserVbAccount);
		String addUserVbAccountUrl = Property.get("llwallet.addUserVbAccount.url.online");

		// 开通企业用户虚拟账户请求提交
		String rsp = HttpRequest.httpPostWithJSON(addUserVbAccountUrl, reqJson);
//		excelUtil.writeResult2Excel(this, "addUserVbAccount", 3, rsp);
		// 开通企业用户虚拟账户请求返回检查
		assert rsp.contains(datadriven.get("expect_return"));

		Reporter.log("开通企业用户虚拟账户接口测试： " + datadriven.get("comment"), true);
		System.out.println("===============================================");
	}

	@DataProvider(name = "addUserVbAccount")
	public Iterator<Object[]> data4addUserVbAccount() throws IOException {
		return new ExcelProvider(this, "addUserVbAccount",2);
	}

	@AfterClass
	public void afterClass() {

	}

}