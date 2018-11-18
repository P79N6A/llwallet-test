package com.llwallet.interfaces.test.api.online.personal;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import org.testng.Reporter;
import org.testng.annotations.Test;
import com.alibaba.fastjson.JSON;
import com.llwallet.interfaces.bean.personal.UserPayment;
import com.tools.dataprovider.ExcelProvider;
import com.tools.http.HttpFixture;
import com.tools.utils.GenSign;
import com.tools.utils.Property;
import org.testng.annotations.DataProvider;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;

/*
 * @author jiangxm
 * 用户收支明细查询接口测试
 */

public class UserPaymentOnlineTest {

	@BeforeClass
	public void beforeClass() throws InterruptedException {
		Property.set();
	}

	@Test(description = "用户收支明细查询接口测试", timeOut = 60000, dataProvider = "userPayment")
	public void userPayment(Map<String, String> datadriven) throws Exception {

		UserPayment userPayment = new UserPayment();
		userPayment.setOid_partner(datadriven.get("oid_partner"));
		userPayment.setSign_type(datadriven.get("sign_type"));
		userPayment.setUser_id(datadriven.get("user_id"));
		userPayment.setOffset(datadriven.get("offset"));
		userPayment.setMaxrecordes(datadriven.get("maxrecordes"));
		userPayment.setDt_start(datadriven.get("dt_start"));
		userPayment.setDt_end(datadriven.get("dt_end"));
		userPayment.setReverseOrder(datadriven.get("reverseOrder"));
		userPayment.setSign(GenSign.genSign(JSON.parseObject(JSON.toJSONString(userPayment)),datadriven.get("key")));
		String reqJson = JSON.toJSONString(userPayment);
		String userPaymentUrl = Property.get("llwallet.userPayment.url.online");

		// 用户收支明细查询请求提交
		HttpFixture hf = new HttpFixture();
		hf.setEncode("UTF-8");
		hf.setUrl(userPaymentUrl);
		hf.addHeaderValue("Content-Type", "application/json");
		hf.addRequestBody(reqJson);
		hf.Post();
		// 用户收支明细查询请求返回检查
		assert hf.getResponseBody().contains(datadriven.get("result_code"));

		Reporter.log("用户收支明细查询接口测试： " + datadriven.get("comment"), true);
		System.out.println("===============================================");
	}
   
	@DataProvider(name = "userPayment")
	public Iterator<Object[]> data4userPayment() throws IOException {
		return new ExcelProvider(this, "userPayment",3);
	}

	@AfterClass
	public void afterClass() {

	}

}
