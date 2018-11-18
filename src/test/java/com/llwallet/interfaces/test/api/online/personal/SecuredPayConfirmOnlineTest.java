package com.llwallet.interfaces.test.api.online.personal;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import org.testng.Reporter;
import org.testng.annotations.Test;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.llwallet.interfaces.bean.personal.SecuredPayConfirm;
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
 * 担保交易确认交易接口测试
 */

public class SecuredPayConfirmOnlineTest {

	@BeforeClass
	public void beforeClass() throws InterruptedException {
		Property.set();
	}

	@SuppressWarnings("unchecked")
	@Test(description = "担保交易确认交易接口测试", timeOut = 60000, dataProvider = "provider")
	public void test(LinkedHashMap<String, LinkedHashMap<String, String>> dt, ExcelProvider2 excel) throws Exception {

		SecuredPayConfirm securedPayConfirm = new SecuredPayConfirm();
		
		Object reqObj = JavaBeanUtil.convertMap(securedPayConfirm.getClass(), excel.getParamMap(dt.get("securedPayConfirm")));
		Map<String, String> map = JSON.parseObject(JSON.toJSONString(reqObj), Map.class);
		securedPayConfirm.setSign(
				GenSign.genSign(JSONObject.parseObject(JSON.toJSONString(map)), dt.get("securedPayConfirm").get("key")));
		map.put("sign", securedPayConfirm.getSign());
		String reqJson = JSON.toJSONString(map);
		String securedPayConfirmUrl = Property.get("llwallet.securedPayConfirm.url.online");

		// 担保交易确认交易请求提交
		String rsp = HttpRequest.httpPostWithJSON(securedPayConfirmUrl, reqJson);
		// excel.writeCurrentCell("orderQuery", "actual_return", rsp);
		Reporter.log("担保交易确认交易接口测试： " + dt.get("securedPayConfirm").get("comment"), true);
		System.out.println("===============================================");
		// 担保交易确认交易返回检查
		assert rsp.contains(dt.get("securedPayConfirm").get("expect_return"));
	}	
	
	@DataProvider(name = "provider")
	public Iterator<Object[]> provider() throws IOException {
//		return new ExcelProvider2(this);
		ExcelProvider2 excel =new ExcelProvider2(this,4);
		excel.maxRow=41;
		return excel;
	}
	
	@AfterClass
	public void afterClass() {

	}

}