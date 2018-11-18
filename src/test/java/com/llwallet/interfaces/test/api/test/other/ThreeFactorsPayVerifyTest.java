package com.llwallet.interfaces.test.api.test.other;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import org.testng.Reporter;
import org.testng.annotations.Test;
import com.alibaba.fastjson.JSON;
import com.llwallet.interfaces.bean.other.ThreeFactorsPayVerify;
import com.tools.dataprovider.ExcelProvider;
import com.tools.http.HttpFixture;
import com.tools.utils.ApiUtils;
import com.tools.utils.GenSign;
import com.tools.utils.Property;
import org.testng.annotations.DataProvider;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;

/*
 * @author jiangxm
 * 人脸支付验证接口测试
 */

public class ThreeFactorsPayVerifyTest {

	@BeforeClass
	public void beforeClass() throws InterruptedException {
		Property.set();
	}

	@Test(description = "人脸支付验证接口测试", timeOut = 60000, dataProvider = "threeFactorsPayVerify")
	public void threeFactorsPayVerify(Map<String, String> datadriven) throws Exception {
		
		String timestamp = ApiUtils.getCurrentDateSecondStr();
		
		ThreeFactorsPayVerify threeFactorsPayVerify = new ThreeFactorsPayVerify();
		threeFactorsPayVerify.setOid_partner(datadriven.get("oid_partner"));
		threeFactorsPayVerify.setSign_type(datadriven.get("sign_type"));
		threeFactorsPayVerify.setUser_id(datadriven.get("user_id"));
		threeFactorsPayVerify.setApi_version(datadriven.get("api_version"));
		
		if ("auto".equals(datadriven.get("timestamp"))) {
			threeFactorsPayVerify.setTimestamp(timestamp);
		}else {
			threeFactorsPayVerify.setTimestamp(datadriven.get("timestamp"));			
		}		
		threeFactorsPayVerify.setFace_token(datadriven.get("face_token"));
		threeFactorsPayVerify.setNo_order(datadriven.get("no_order"));
		threeFactorsPayVerify.setMoney_order(datadriven.get("money_order"));
		threeFactorsPayVerify.setSign(GenSign.genSign(JSON.parseObject(JSON.toJSONString(threeFactorsPayVerify)),datadriven.get("key")));
		String reqJson = JSON.toJSONString(threeFactorsPayVerify);
		String threeFactorsPayVerifyUrl = Property.get("llwallet.threeFactorsPayVerify.url");

		// 人脸支付验证请求提交
		HttpFixture hf = new HttpFixture();
		hf.setEncode("UTF-8");
		hf.setUrl(threeFactorsPayVerifyUrl);
		hf.addHeaderValue("Content-Type", "application/json");
		hf.addRequestBody(reqJson);
		hf.Post();
		// 人脸支付验证请求返回检查
		assert hf.getResponseBody().contains(datadriven.get("result_code"));
		
		Reporter.log("人脸支付验证接口测试： " + datadriven.get("comment"), true);
		System.out.println("===============================================");
	}

	@DataProvider(name = "threeFactorsPayVerify")
	public Iterator<Object[]> data4threeFactorsPayVerify() throws IOException {
		return new ExcelProvider(this, "threeFactorsPayVerify");
	}

	@AfterClass
	public void afterClass() {

	}

}
