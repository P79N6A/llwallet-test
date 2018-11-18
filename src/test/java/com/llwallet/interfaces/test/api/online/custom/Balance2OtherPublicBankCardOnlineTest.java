package com.llwallet.interfaces.test.api.online.custom;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import org.testng.Reporter;
import org.testng.annotations.Test;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.llwallet.interfaces.bean.custom.Balance2OtherPublicBankCard;
import com.tools.dataprovider.ExcelProvider2;
import com.tools.http.HttpRequest;
import com.tools.utils.GenSign;
import com.tools.utils.JavaBeanUtil;
import com.tools.utils.Property;
import org.testng.annotations.DataProvider;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;

/*
 * @author jiangxm
 * 用户余额到他人对公银行卡接口测试
 */

public class Balance2OtherPublicBankCardOnlineTest {

	@BeforeClass
	public void beforeClass() throws InterruptedException {
		Property.set();
	}

	@SuppressWarnings("unchecked")
	@Test(description = "余额到他人对公银行卡接口测试", timeOut = 60000, dataProvider = "provider")
	public void test(LinkedHashMap<String, LinkedHashMap<String, String>> dt, ExcelProvider2 excel) throws Exception {

		Balance2OtherPublicBankCard balance2OtherPublicBankCard = new Balance2OtherPublicBankCard();
		Object reqObj = JavaBeanUtil.convertMap(balance2OtherPublicBankCard.getClass(),
				excel.getParamMap(dt.get("balance2OtherPublicBankCard")));
		Map<String, String> map = JSON.parseObject(JSON.toJSONString(reqObj), Map.class);
		balance2OtherPublicBankCard.setSign(GenSign.genSign(JSONObject.parseObject(JSON.toJSONString(map)),
				dt.get("balance2OtherPublicBankCard").get("key")));
		map.put("sign", balance2OtherPublicBankCard.getSign());
		String reqJson = JSON.toJSONString(map);
		String balance2OtherPublicBankCardUrl = Property.get("llwallet.balance2OtherPublicBankCard.url.online");		// 用户余额到他人对公银行卡请求提交
		String rsp = HttpRequest.httpPostWithJSON(balance2OtherPublicBankCardUrl, reqJson);
		// excel.writeCurrentCell("balance2OtherPublicBankCard",
		// "actual_return", rsp);
		Reporter.log("用户余额到他人对公银行卡接口测试： " + dt.get("balance2OtherPublicBankCard").get("comment"), true);
		System.out.println("===============================================");
		// 用户余额到他人对公银行卡请求返回检查
		assert rsp.contains(dt.get("balance2OtherPublicBankCard").get("expect_return"));
	}

	@DataProvider(name = "provider")
	public Iterator<Object[]> provider() throws IOException {
		return new ExcelProvider2(this,3);
	}

	@AfterClass
	public void afterClass() {

	}
}