package com.llwallet.interfaces.test.api.online.combination;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import org.testng.Reporter;
import org.testng.annotations.Test;
import com.alibaba.fastjson.JSON;
import com.llwallet.interfaces.bean.combination.CombinationQuery;
import com.tools.dataprovider.ExcelProvider;
import com.tools.http.HttpFixture;
import com.tools.utils.ApiUtils;
import com.tools.utils.GenSign;
import com.tools.utils.MyConfig;
import com.tools.utils.Property;
import org.testng.annotations.DataProvider;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;

/*
 * @author jiangxm
 * 组合支付结果查询接口测试
 */

public class CombinationQueryOnlineTest {

	@BeforeClass
	public void beforeClass() throws InterruptedException {
		Property.set();
	}

	@Test(description = "组合支付结果查询接口测试", timeOut = 60000, dataProvider = "combinationQuery")
	public void combinationQuery(Map<String, String> datadriven) throws Exception {

		String timestamp = ApiUtils.getCurrentDateSecondStr();

		CombinationQuery combinationQuery = new CombinationQuery();
//		combinationQuery.setOid_partner(datadriven.get("oid_partner"));
		combinationQuery.setOid_partner(MyConfig.getPartner(datadriven.get("oid_partner")));
		combinationQuery.setSign_type(datadriven.get("sign_type"));
		combinationQuery.setApi_version(datadriven.get("api_version"));
		if ("auto".equals(datadriven.get("timestamp"))) {
			combinationQuery.setTimestamp(timestamp);
		} else {
			combinationQuery.setTimestamp(datadriven.get("timestamp"));
		}
		combinationQuery.setNo_order(datadriven.get("no_order"));
		combinationQuery.setDt_order(datadriven.get("dt_order"));
		combinationQuery.setOid_paybill(datadriven.get("oid_paybill"));
		combinationQuery
				.setSign(GenSign.genSign(JSON.parseObject(JSON.toJSONString(combinationQuery)), datadriven.get("key")));
		String reqJson = JSON.toJSONString(combinationQuery);
		String combinationQueryUrl = Property.get("llwallet.combinationQuery.url.online");

		// 组合支付结果查询请求提交
		HttpFixture hf = new HttpFixture();
		hf.setEncode("UTF-8");
		hf.setUrl(combinationQueryUrl);
		hf.addHeaderValue("Content-Type", "application/json");
		hf.addRequestBody(reqJson);
		hf.Post();
		// 组合支付结果查询请求返回检查
		assert hf.getResponseBody().contains(datadriven.get("result_code"));

		Reporter.log("组合支付结果查询接口测试： " + datadriven.get("comment"), true);
		System.out.println("===============================================");
	}

	@DataProvider(name = "combinationQuery")
	public Iterator<Object[]> data4combinationQuery() throws IOException {
		return new ExcelProvider(this, "combinationQuery",1);
	}

	@AfterClass
	public void afterClass() {

	}

}