package com.llwallet.interfaces.test.api.test;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import org.testng.Reporter;
import org.testng.annotations.Test;
import com.alibaba.fastjson.JSONObject;
import com.tools.dataprovider.ExcelProvider;
import com.tools.utils.GenSign;
import com.tools.utils.Property;
import com.tools.utils.RSAUtil;
import org.testng.annotations.DataProvider;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;

/*
 * @author jiangxm
 * 验证签名测试
 */

public class VerifySignTest {

	@BeforeClass
	public void beforeClass() throws InterruptedException {
		Property.set();
	}

	@Test(description = "验证签名测试", timeOut = 60000, dataProvider = "verifySign")
	public void verifySign(Map<String, String> datadriven) throws Exception {

		String rsa_pub_key = datadriven.get("rsa_pub_key");
		JSONObject jsonStr = JSONObject.parseObject(datadriven.get("json_string"));
		String sign = jsonStr.getString("sign");
		String sign_src = GenSign.genSignData(jsonStr);

		System.out.println("sign-----:" + sign);
		System.out.println("sign_src-----:" + sign_src);
		boolean result = RSAUtil.checksign(rsa_pub_key, sign_src, sign);
		System.out.println("验签结果-----:" + result);
		assert result;
		Reporter.log(datadriven.get("comment") + "验签结果：" + result, true);
		System.out.println("===============================================");
	}

	@DataProvider(name = "verifySign")
	public Iterator<Object[]> data4verifySign() throws IOException {
		return new ExcelProvider(this, "verifySign",17);
	}

	@AfterClass
	public void afterClass() {

	}

}
