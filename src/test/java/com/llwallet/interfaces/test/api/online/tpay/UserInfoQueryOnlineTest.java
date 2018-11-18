package com.llwallet.interfaces.test.api.online.tpay;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import org.testng.Reporter;
import org.testng.annotations.Test;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.llwallet.interfaces.bean.tpay.UserInfoQuery;
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
 * 消费通账户查询接口测试
 */

public class UserInfoQueryOnlineTest {

	@BeforeClass
	public void beforeClass() throws InterruptedException {
		Property.set();
	}

	@SuppressWarnings("unchecked")
	@Test(description = "消费通账户查询接口测试", timeOut = 60000, dataProvider = "provider")
	public void test(LinkedHashMap<String, LinkedHashMap<String, String>> dt, ExcelProvider2 excel) throws Exception {

		UserInfoQuery userInfoQuery = new UserInfoQuery();
		Object reqObj = JavaBeanUtil.convertMap(userInfoQuery.getClass(), excel.getParamMap(dt.get("userInfoQuery")));
		Map<String, String> map = JSON.parseObject(JSON.toJSONString(reqObj), Map.class);
		userInfoQuery.setSign(
				GenSign.genSign(JSONObject.parseObject(JSON.toJSONString(map)), dt.get("userInfoQuery").get("key")));
		map.put("sign", userInfoQuery.getSign());
		String reqJson = JSON.toJSONString(map);
		String userInfoQueryUrl = Property.get("tpay.userInfoQuery.url.online");
		// 消费通账户查询请求提交
		String rsp = HttpRequest.httpPostWithJSON(userInfoQueryUrl, reqJson);
		// excel.writeCurrentCell("userInfoQuery", "actual_return",rsp);
		Reporter.log("消费通账户查询接口测试： " + dt.get("userInfoQuery").get("comment"), true);
		System.out.println("===============================================");
		// 消费通账户查询请求返回检查
		assert rsp.contains(dt.get("userInfoQuery").get("expect_return"));
		// 返回验签
		if (rsp.contains("\"ret_code\":\"0000\",\"ret_msg\":\"交易成功\"")){
			assert RSAUtil.checksign(Property.get("rsa_pub_key"), GenSign.genSignData(JSONObject.parseObject(rsp)), JSONObject.parseObject(rsp).getString("sign"));
		}
	}

	@DataProvider(name = "provider")
	public Iterator<Object[]> provider() throws IOException {
		return new ExcelProvider2(this,5);
	}

	@AfterClass
	public void afterClass() {

	}
}