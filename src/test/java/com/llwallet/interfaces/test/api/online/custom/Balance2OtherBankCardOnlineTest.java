package com.llwallet.interfaces.test.api.online.custom;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import org.testng.Reporter;
import org.testng.annotations.Test;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.llwallet.interfaces.bean.custom.Balance2OtherBankCard;
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
 * 用户余额到他人银行卡接口测试
 */

public class Balance2OtherBankCardOnlineTest {

	@BeforeClass
	public void beforeClass() throws InterruptedException {
		Property.set();
	}

	@SuppressWarnings("unchecked")
	@Test(description = "余额到他人银行卡接口测试", timeOut = 60000, dataProvider = "provider")
	public void test(LinkedHashMap<String, LinkedHashMap<String, String>> dt, ExcelProvider2 excel) throws Exception {

		Balance2OtherBankCard balance2OtherBankCard = new Balance2OtherBankCard();
		Object reqObj = JavaBeanUtil.convertMap(balance2OtherBankCard.getClass(),
				excel.getParamMap(dt.get("balance2OtherBankCard")));
		Map<String, String> map = JSON.parseObject(JSON.toJSONString(reqObj), Map.class);
		balance2OtherBankCard.setSign(GenSign.genSign(JSONObject.parseObject(JSON.toJSONString(map)),
				dt.get("balance2OtherBankCard").get("key")));
		map.put("sign", balance2OtherBankCard.getSign());
		String reqJson = JSON.toJSONString(map);
		String balance2OtherBankCardUrl = Property.get("llwallet.balance2OtherBankCard.url.online");
	
		// 用户余额到他人银行卡请求提交
		String rsp = HttpRequest.httpPostWithJSON(balance2OtherBankCardUrl, reqJson);
		// excel.writeCurrentCell("balance2OtherBankCard", "actual_return",
		// rsp);
		Reporter.log("用户余额到他人银行卡接口测试： " + dt.get("balance2OtherBankCard").get("comment"), true);
		System.out.println("===============================================");
		// 用户余额到他人银行卡请求返回检查
		assert rsp.contains(dt.get("balance2OtherBankCard").get("expect_return"));
	}

	@DataProvider(name = "provider")
	public Iterator<Object[]> provider() throws IOException {
		return new ExcelProvider2(this,4);
	}

	@AfterClass
	public void afterClass() {

	}
}