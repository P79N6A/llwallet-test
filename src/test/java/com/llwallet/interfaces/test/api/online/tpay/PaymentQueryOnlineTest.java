package com.llwallet.interfaces.test.api.online.tpay;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import org.testng.Reporter;
import org.testng.annotations.Test;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.llwallet.interfaces.bean.tpay.PaymentQuery;
import com.tools.dataprovider.ExcelProvider2;
import com.tools.http.HttpRequest;
import com.tools.utils.GenSign;
import com.tools.utils.JavaBeanUtil;
import com.tools.utils.Property;
import com.tools.utils.RSAUtil;
import org.testng.annotations.DataProvider;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;

/*
 * @author jiangxm
 * 支付结果查询接口测试
 */

public class PaymentQueryOnlineTest {

	@BeforeClass
	public void beforeClass() throws InterruptedException {
		Property.set();
	}

	@SuppressWarnings("unchecked")
	@Test(description = "支付结果查询接口测试", timeOut = 60000, dataProvider = "provider")
	public void test(LinkedHashMap<String, LinkedHashMap<String, String>> dt, ExcelProvider2 excel) throws Exception {

		PaymentQuery paymentQuery = new PaymentQuery();
		Object reqObj = JavaBeanUtil.convertMap(paymentQuery.getClass(), excel.getParamMap(dt.get("paymentQuery")));
		Map<String, String> map = JSON.parseObject(JSON.toJSONString(reqObj), Map.class);
		paymentQuery.setSign(
				GenSign.genSign(JSONObject.parseObject(JSON.toJSONString(map)), dt.get("paymentQuery").get("key")));
		map.put("sign", paymentQuery.getSign());
		String reqJson = JSON.toJSONString(map);
		String paymentQueryUrl = Property.get("tpay.paymentQuery.url.online");
		// 支付结果查询请求提交
		String rsp = HttpRequest.httpPostWithJSON(paymentQueryUrl, reqJson);
		// excel.writeCurrentCell("paymentQuery", "actual_return",rsp);
		Reporter.log("支付结果查询接口测试： " + dt.get("paymentQuery").get("comment"), true);
		System.out.println("===============================================");
		// 支付结果查询请求返回检查
		assert rsp.contains(dt.get("paymentQuery").get("expect_return"));
		// 返回验签
		if (rsp.contains("\"ret_code\":\"0000\",\"ret_msg\":\"交易成功\"")){
			assert RSAUtil.checksign(Property.get("rsa_pub_key"), GenSign.genSignData(JSONObject.parseObject(rsp)), JSONObject.parseObject(rsp).getString("sign"));
		}

	}

	@DataProvider(name = "provider")
	public Iterator<Object[]> provider() throws IOException {
		return new ExcelProvider2(this);
	}

	@AfterClass
	public void afterClass() {

	}
}