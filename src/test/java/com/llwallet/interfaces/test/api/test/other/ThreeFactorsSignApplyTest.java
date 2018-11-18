package com.llwallet.interfaces.test.api.test.other;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import org.testng.Reporter;
import org.testng.annotations.Test;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.llwallet.interfaces.bean.other.ThreeFactorsSignApply;
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
 * 人脸签约预处理接口测试
 */

public class ThreeFactorsSignApplyTest {

	@BeforeClass
	public void beforeClass() throws InterruptedException {
		Property.set();
	}

	@Test(description = "人脸签约预处理接口测试", timeOut = 60000, dataProvider = "threeFactorsSignApply")
	public void threeFactorsSignApply(Map<String, String> datadriven) throws Exception {
		
		String timestamp = ApiUtils.getCurrentDateSecondStr();
		
		ThreeFactorsSignApply threeFactorsSignApply = new ThreeFactorsSignApply();
		threeFactorsSignApply.setOid_partner(datadriven.get("oid_partner"));
		threeFactorsSignApply.setSign_type(datadriven.get("sign_type"));
		threeFactorsSignApply.setUser_id(datadriven.get("user_id"));
		if ("auto".equals(datadriven.get("timestamp"))) {
			threeFactorsSignApply.setTimestamp(timestamp);
		}else {
			threeFactorsSignApply.setTimestamp(datadriven.get("timestamp"));			
		}		
		threeFactorsSignApply.setApi_version(datadriven.get("api_version"));
		threeFactorsSignApply.setRisk_item(datadriven.get("risk_item"));
		threeFactorsSignApply.setCard_no(datadriven.get("card_no"));
		threeFactorsSignApply.setFaceauth_url(datadriven.get("faceauth_url"));
		threeFactorsSignApply.setSign(GenSign.genSign(JSON.parseObject(JSON.toJSONString(threeFactorsSignApply)),datadriven.get("key")));
		String reqJson = JSON.toJSONString(threeFactorsSignApply);
		String threeFactorsSignApplyUrl = Property.get("llwallet.threeFactorsSignApply.url");

		// 人脸签约预处理请求提交
		String rsp = HttpRequest.httpPostWithJSON(threeFactorsSignApplyUrl, reqJson);
		// 人脸签约预处理请求返回检查
		String face_token = JSONObject.parseObject(rsp).getString("face_token");
		String no_agree = JSONObject.parseObject(rsp).getString("no_agree");
		System.out.println("face_token：" + face_token);
		System.out.println("no_agree：" + no_agree);
		assert rsp.contains(datadriven.get("result_code"));

		Reporter.log("人脸签约预处理接口测试： " + datadriven.get("comment"), true);
		System.out.println("===============================================");
	}

	@DataProvider(name = "threeFactorsSignApply")
	public Iterator<Object[]> data4threeFactorsSignApply() throws IOException {
		return new ExcelProvider(this, "threeFactorsSignApply",1);
	}

	@AfterClass
	public void afterClass() {

	}

}