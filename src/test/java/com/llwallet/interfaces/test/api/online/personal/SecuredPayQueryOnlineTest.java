package com.llwallet.interfaces.test.api.online.personal;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import org.testng.Reporter;
import org.testng.annotations.Test;
import com.alibaba.fastjson.JSON;
import com.llwallet.interfaces.bean.personal.SecuredPayQuery;
import com.tools.dataprovider.ExcelProvider;
import com.tools.http.HttpFixture;
import com.tools.utils.GenSign;
import com.tools.utils.Property;
import org.testng.annotations.DataProvider;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;

/*
 * @author jiangxm
 * 担保订单查询接口测试
 */

public class SecuredPayQueryOnlineTest {

	@BeforeClass
	public void beforeClass() throws InterruptedException {
		Property.set();
	}

	@Test(description = "担保订单查询接口测试", timeOut = 60000, dataProvider = "securedPayQuery")
	public void securedPayQuery(Map<String, String> datadriven) throws Exception {

		SecuredPayQuery securedPayQuery = new SecuredPayQuery();
		securedPayQuery.setOid_partner(datadriven.get("oid_partner"));
		securedPayQuery.setSign_type(datadriven.get("sign_type"));
		securedPayQuery.setNo_order(datadriven.get("no_order"));
		securedPayQuery.setDt_order(datadriven.get("dt_order"));
		securedPayQuery.setOid_paybill(datadriven.get("oid_paybill"));
		securedPayQuery.setSign(GenSign.genSign(JSON.parseObject(JSON.toJSONString(securedPayQuery)),datadriven.get("key")));
		String reqJson = JSON.toJSONString(securedPayQuery);
		String securedPayQueryUrl = Property.get("llwallet.securedPayQuery.url.online");

		// 担保订单查询请求提交
		HttpFixture hf = new HttpFixture();
		hf.setEncode("UTF-8");
		hf.setUrl(securedPayQueryUrl);
		hf.addHeaderValue("Content-Type", "application/json");
		hf.addRequestBody(reqJson);
		hf.Post();
		// 担保订单查询请求返回检查
		assert hf.getResponseBody().contains(datadriven.get("result_code"));
		
		Reporter.log("担保订单查询接口测试： " + datadriven.get("comment"), true);
		System.out.println("===============================================");
	}

	@DataProvider(name = "securedPayQuery")
	public Iterator<Object[]> data4securedPayQuery() throws IOException {
		return new ExcelProvider(this, "securedPayQuery",3);
	}

	@AfterClass
	public void afterClass() {

	}

}