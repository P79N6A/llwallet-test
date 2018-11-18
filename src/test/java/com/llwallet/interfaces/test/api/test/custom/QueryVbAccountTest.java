package com.llwallet.interfaces.test.api.test.custom;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import org.testng.Reporter;
import org.testng.annotations.Test;
import com.alibaba.fastjson.JSON;
import com.llwallet.interfaces.bean.custom.QueryVbAccount;
import com.tools.dataprovider.ExcelProvider;
import com.tools.http.HttpRequest;
import com.tools.utils.ExcelUtil;
import com.tools.utils.GenSign;
import com.tools.utils.MyConfig;
import com.tools.utils.Property;
import org.testng.annotations.DataProvider;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;

/*
 * @author jiangxm
 * 查询虚拟账户接口测试
 */

public class QueryVbAccountTest {
		
	ExcelUtil excelUtil = new ExcelUtil();	

	@BeforeClass
	public void beforeClass() throws Exception {
		Property.set();
		excelUtil.prepare4Excel(this, "queryVbAccount");
	}

	@Test(description = "查询虚拟账户接口测试", timeOut = 60000, dataProvider = "queryVbAccount")
	public void queryVbAccount(Map<String, String> datadriven) throws Exception {

		QueryVbAccount queryVbAccount = new QueryVbAccount();
		queryVbAccount.setOid_partner(MyConfig.getPartner(datadriven.get("oid_partner")));
		queryVbAccount.setSign_type(datadriven.get("sign_type"));
		queryVbAccount.setUser_id(MyConfig.getUser(datadriven.get("user_id")));
		queryVbAccount
				.setSign(GenSign.genSign(JSON.parseObject(JSON.toJSONString(queryVbAccount)), datadriven.get("key")));
		String reqJson = JSON.toJSONString(queryVbAccount);
		String queryVbAccountUrl = Property.get("llwallet.queryVbAccount.url");

		// 查询虚拟账户请求提交
		String rsp = HttpRequest.httpPostWithJSON(queryVbAccountUrl, reqJson);
		excelUtil.writeResult2Excel(this, "queryVbAccount", 3, rsp);
		Reporter.log("查询虚拟账户接口测试： " + datadriven.get("comment"), true);
		System.out.println("===============================================");		
		// 查询虚拟账户请求返回检查
		assert rsp.contains(datadriven.get("expect_return"));
	}

	@DataProvider(name = "queryVbAccount")
	public Iterator<Object[]> data4queryVbAccount() throws IOException {
		return new ExcelProvider(this, "queryVbAccount");
	}

	@AfterClass
	public void afterClass() {

	}

}