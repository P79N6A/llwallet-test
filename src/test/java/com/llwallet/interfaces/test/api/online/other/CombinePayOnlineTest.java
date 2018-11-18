package com.llwallet.interfaces.test.api.online.other;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import org.testng.Reporter;
import org.testng.annotations.Test;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.llwallet.interfaces.bean.other.CombinePay;
import com.tools.dataprovider.ExcelProvider;
import com.tools.http.HttpRequest;
import com.tools.utils.GenSign;
import com.tools.utils.JavaBeanUtil;
import com.tools.utils.Property;
import org.testng.annotations.DataProvider;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;

/*
 * @author jiangxm
 * 微信支付宝支付接口测试
 */

public class CombinePayOnlineTest {

	@BeforeClass
	public void beforeClass() throws InterruptedException {
		Property.set();
	}

	@SuppressWarnings("unchecked")
	@Test(description = "微信支付宝支付", timeOut = 60000, dataProvider = "combinePay")
	public void combinePay(Map<String, String> datadriven) throws Exception {

		CombinePay combinePay = new CombinePay();

		Object reqObj = JavaBeanUtil.convertMap(combinePay.getClass(), datadriven);
		combinePay.setSign(GenSign.genSign(JSON.parseObject(JSON.toJSONString(reqObj)), datadriven.get("key")));
		Map<String, String> map = JSON.parseObject(JSON.toJSONString(reqObj), Map.class);
		map.put("sign", combinePay.getSign());
		String reqJson = JSON.toJSONString(map);
		String combinePayUrl = Property.get("llwallet.combinePay.url.online");
		
		// 微信支付宝支付处理请求提交
		String rsp = HttpRequest.httpPostWithJSON(combinePayUrl, reqJson);
		Reporter.log("支付单号： " + JSONObject.parseObject(rsp).getString("oid_paybill"), true);
		Reporter.log("支付内容： " + JSONObject.parseObject(rsp).getString("payLoad"), true);

		Reporter.log("微信支付宝支付接口测试： " + datadriven.get("comment"), true);
		System.out.println("===============================================");

		// 微信支付宝支付处理请求返回检查
		assert rsp.contains(datadriven.get("expect_return"));
	}

	@DataProvider(name = "combinePay")
	public Iterator<Object[]> data4combinePay() throws IOException {
		return new ExcelProvider(this, "combinePay",5);
	}

	@AfterClass
	public void afterClass() {

	}

}