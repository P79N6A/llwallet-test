package com.llwallet.interfaces.test.api.online.other;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import org.testng.Reporter;
import org.testng.annotations.Test;
import com.alibaba.fastjson.JSON;
import com.llwallet.interfaces.bean.other.ThreeFactorsSignVerify;
import com.tools.dataprovider.ExcelProvider;
import com.tools.http.HttpRequest;
import com.tools.utils.ApiUtils;
import com.tools.utils.GenSign;
import com.tools.utils.Property;
import org.testng.annotations.DataProvider;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;

/*
 * @author jiangxm
 * 人脸签约验证接口测试
 */

public class ThreeFactorsSignVerifyOnlineTest {

	@BeforeClass
	public void beforeClass() throws InterruptedException {
		Property.set();
	}

	@Test(description = "人脸签约验证接口测试", timeOut = 60000, dataProvider = "threeFactorsSignVerify")
	public void threeFactorsSignVerify(Map<String, String> datadriven) throws Exception {
		
		String timestamp = ApiUtils.getCurrentDateSecondStr();
		
		ThreeFactorsSignVerify threeFactorsSignVerify = new ThreeFactorsSignVerify();
		threeFactorsSignVerify.setOid_partner(datadriven.get("oid_partner"));
		threeFactorsSignVerify.setSign_type(datadriven.get("sign_type"));
		threeFactorsSignVerify.setUser_id(datadriven.get("user_id"));
		threeFactorsSignVerify.setApi_version(datadriven.get("api_version"));
		
		if ("auto".equals(datadriven.get("timestamp"))) {
			threeFactorsSignVerify.setTimestamp(timestamp);
		}else {
			threeFactorsSignVerify.setTimestamp(datadriven.get("timestamp"));			
		}		
		threeFactorsSignVerify.setFace_token(datadriven.get("face_token"));
		threeFactorsSignVerify.setNo_agree(datadriven.get("no_agree"));
	
		threeFactorsSignVerify.setSign(GenSign.genSign(JSON.parseObject(JSON.toJSONString(threeFactorsSignVerify)),datadriven.get("key")));
		String reqJson = JSON.toJSONString(threeFactorsSignVerify);
		String threeFactorsSignVerifyUrl = Property.get("llwallet.threeFactorsSignVerify.url.online");

		// 人脸签约验证请求提交
		String rsp = HttpRequest.httpPostWithJSON(threeFactorsSignVerifyUrl, reqJson);
		// 人脸签约验证请求返回检查
		assert rsp.contains(datadriven.get("result_code"));
		
		Reporter.log("人脸签约验证接口测试： " + datadriven.get("comment"), true);
		System.out.println("===============================================");
	}

	@DataProvider(name = "threeFactorsSignVerify")
	public Iterator<Object[]> data4threeFactorsSignVerify() throws IOException {
		return new ExcelProvider(this, "threeFactorsSignVerify",1);
	}

	@AfterClass
	public void afterClass() {

	}

}
