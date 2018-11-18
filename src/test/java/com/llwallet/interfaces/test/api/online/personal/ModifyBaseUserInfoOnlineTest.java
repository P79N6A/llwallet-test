package com.llwallet.interfaces.test.api.online.personal;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import org.testng.Reporter;
import org.testng.annotations.Test;
import com.alibaba.fastjson.JSON;
import com.llwallet.interfaces.bean.personal.ModifyBaseUserInfo;
import com.tools.dataprovider.ExcelProvider;
import com.tools.http.HttpFixture;
import com.tools.utils.GenSign;
import com.tools.utils.Property;
import org.testng.annotations.DataProvider;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;

/*
 * @author jiangxm
 * 用户职业住址修改接口测试
 */

public class ModifyBaseUserInfoOnlineTest {

	@BeforeClass
	public void beforeClass() throws InterruptedException {
		Property.set();
	}

	@Test(description = "用户职业住址修改接口测试", timeOut = 60000, dataProvider = "modifyBaseUserInfo")
	public void modifyBaseUserInfo(Map<String, String> datadriven) throws Exception {
		
		// 组织修改请求报文
		ModifyBaseUserInfo modifyBaseUserInfo = new ModifyBaseUserInfo();
		modifyBaseUserInfo.setOid_partner(datadriven.get("oid_partner"));
		modifyBaseUserInfo.setSign_type(datadriven.get("sign_type"));
		modifyBaseUserInfo.setUser_id(datadriven.get("user_id"));
		modifyBaseUserInfo.setOid_job(datadriven.get("oid_job1"));
		modifyBaseUserInfo.setAddr_pro(datadriven.get("addr_pro1"));
		modifyBaseUserInfo.setAddr_city(datadriven.get("addr_city1"));
		modifyBaseUserInfo.setAddr_dist(datadriven.get("addr_dist1"));		
		modifyBaseUserInfo.setAddr_conn(datadriven.get("addr_conn1"));
		modifyBaseUserInfo.setRisk_item(datadriven.get("risk_item"));
		modifyBaseUserInfo.setExp_idcard(datadriven.get("exp_idcard1"));
		modifyBaseUserInfo.setType_expidcard(datadriven.get("type_expidcard1"));		
		modifyBaseUserInfo.setSign(GenSign.genSign(JSON.parseObject(JSON.toJSONString(modifyBaseUserInfo)), datadriven.get("key")));
		String reqJson = JSON.toJSONString(modifyBaseUserInfo);
		String modifyBaseUserInfoUrl = Property.get("llwallet.modifyBaseUserInfo.url.online");

		// 用户职业住址修改请求提交
		HttpFixture hf = new HttpFixture();
		hf.setEncode("UTF-8");
		hf.addHeaderValue("Content-Type", "application/json");
		hf.setUrl(modifyBaseUserInfoUrl);
		hf.addRequestBody(reqJson);
		hf.Post();
		// 用户职业住址修改请求返回检查
		assert hf.getResponseBody().contains(datadriven.get("result_code"));

		Reporter.log("用户职业住址修改接口测试： " + datadriven.get("comment"), true);
		System.out.println("===============================================");
	}
	
	@DataProvider(name = "modifyBaseUserInfo")
	public Iterator<Object[]> data4modifyBaseUserInfo() throws IOException {
		 return new ExcelProvider(this, "modifyBaseUserInfo",1);
	}

	@AfterClass
	public void afterClass() {

	}
}