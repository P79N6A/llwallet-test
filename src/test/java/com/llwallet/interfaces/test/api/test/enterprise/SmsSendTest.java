package com.llwallet.interfaces.test.api.test.enterprise;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import org.testng.Reporter;
import org.testng.annotations.Test;
import com.alibaba.fastjson.JSON;
import com.llwallet.interfaces.bean.personal.SmsSend;
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
 * 钱包下发短信接口测试
 */

public class SmsSendTest {

	@BeforeClass
	public void beforeClass() throws InterruptedException {
		Property.set();
	}

	@SuppressWarnings("unchecked")
	@Test(description = "短信下发接口测试", timeOut = 60000, dataProvider = "provider")
	public void test(LinkedHashMap<String, LinkedHashMap<String, String>> dt, ExcelProvider2 excel) throws Exception {

		// 下发短信
		SmsSend smsSend = new SmsSend();
		Object reqObj = JavaBeanUtil.convertMap(smsSend.getClass(), excel.getParamMap(dt.get("smsSend")));
		smsSend.setSign(GenSign.genSign(JSON.parseObject(JSON.toJSONString(reqObj)), dt.get("smsSend").get("key")));
		Map<String, String> map = JSON.parseObject(JSON.toJSONString(reqObj), Map.class);
		map.put("sign", smsSend.getSign());
		String reqJson = JSON.toJSONString(map);
		String smsSendUrl = Property.get("llwallet.smsSend.url");
		// 下发短信请求提交
		String rsp = HttpRequest.httpPostWithJSON(smsSendUrl, reqJson);
		// excel.writeCurrentCell("smsSend", "actual_return", rsp);
		Reporter.log("下发短信接口测试： " + dt.get("smsSend").get("comment"), true);
		System.out.println("===============================================");
		assert rsp.contains(dt.get("smsSend").get("expect_return"));
	}

	@DataProvider(name = "provider")
	public Iterator<Object[]> provider() throws IOException {
		return new ExcelProvider2(this,3);
	}

	@AfterClass
	public void afterClass() {

	}

}