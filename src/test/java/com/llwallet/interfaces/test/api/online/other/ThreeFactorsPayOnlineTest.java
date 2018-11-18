package com.llwallet.interfaces.test.api.online.other;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import org.testng.Reporter;
import org.testng.annotations.Test;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.llwallet.interfaces.bean.other.ThreeFactorsPay;
import com.tools.dataprovider.ExcelProvider;
import com.tools.http.HttpFixture;
import com.tools.utils.ApiUtils;
import com.tools.utils.GenSign;
import com.tools.utils.Property;
import com.tools.utils.RSAUtil;
import org.testng.annotations.DataProvider;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;

/*
 * @author jiangxm
 * 人脸支付预处理接口测试
 */

public class ThreeFactorsPayOnlineTest {

	@BeforeClass
	public void beforeClass() throws InterruptedException {
		Property.set();
	}

	@Test(description = "人脸支付预处理接口测试", timeOut = 60000, dataProvider = "threeFactorsPay")
	public void threeFactorsPay(Map<String, String> datadriven) throws Exception {
		
		String timestamp = ApiUtils.getCurrentDateSecondStr();
		String dt_order = ApiUtils.getCurrentDateSecondStr();
		String no_order = ApiUtils.getCurrentDateMillisecondStr();
		
		ThreeFactorsPay threeFactorsPay = new ThreeFactorsPay();
		threeFactorsPay.setOid_partner(datadriven.get("oid_partner"));
		threeFactorsPay.setSign_type(datadriven.get("sign_type"));
		threeFactorsPay.setUser_id(datadriven.get("user_id"));
		if ("auto".equals(datadriven.get("timestamp"))) {
			threeFactorsPay.setTimestamp(timestamp);
		}else {
			threeFactorsPay.setTimestamp(datadriven.get("timestamp"));			
		}		
		threeFactorsPay.setApi_version(datadriven.get("api_version"));
		threeFactorsPay.setBusi_partner(datadriven.get("busi_partner"));
		if ("auto".equals(datadriven.get("no_order"))){
			threeFactorsPay.setNo_order(no_order);
		}else{
			threeFactorsPay.setNo_order(datadriven.get("no_order"));
		}
		if ("auto".equals(datadriven.get("dt_order"))){
			threeFactorsPay.setDt_order(dt_order);
		}else{
			threeFactorsPay.setDt_order(datadriven.get("dt_order"));
		}
		threeFactorsPay.setMoney_order(datadriven.get("money_order"));
		threeFactorsPay.setName_goods(datadriven.get("name_goods"));
		threeFactorsPay.setInfo_order(datadriven.get("info_order"));
		threeFactorsPay.setValid_order(datadriven.get("valid_order"));
		threeFactorsPay.setNotify_url(datadriven.get("notify_url"));
		threeFactorsPay.setFaceauth_url(datadriven.get("faceauth_url"));
		if (datadriven.get("pwd_pay").length() != 0) {
			threeFactorsPay.setPwd_pay(RSAUtil.encrypt(datadriven.get("pwd_pay"), Property.get("rsa_pub_key")));
		} else {

		}
		threeFactorsPay.setCard_no(datadriven.get("card_no"));
		threeFactorsPay.setNo_agree(datadriven.get("no_agree"));
		threeFactorsPay.setRisk_item(datadriven.get("risk_item"));
		threeFactorsPay.setCol_oidpartner(datadriven.get("col_oidpartner"));
		threeFactorsPay.setCol_userid(datadriven.get("col_userid"));
		threeFactorsPay.setShareing_data(datadriven.get("shareing_data"));
		threeFactorsPay.setSecured_partner(datadriven.get("secured_partner"));

		threeFactorsPay.setSign(GenSign.genSign(JSON.parseObject(JSON.toJSONString(threeFactorsPay)),datadriven.get("key")));
		String reqJson = JSON.toJSONString(threeFactorsPay);
		String threeFactorsPayUrl = Property.get("llwallet.threeFactorsPay.url.online");

		// 人脸支付预处理请求提交
		HttpFixture hf = new HttpFixture();
		hf.setEncode("UTF-8");
		hf.setUrl(threeFactorsPayUrl);
		hf.addHeaderValue("Content-Type", "application/json");
		hf.addRequestBody(reqJson);
		hf.Post();
		// 人脸支付预处理请求返回检查
		String face_token = JSONObject.parseObject(hf.getResponseBody()).getString("face_token");
		System.out.println("face_token：" + face_token);
		System.out.println("no_order：" + no_order);
		assert hf.getResponseBody().contains(datadriven.get("result_code"));

		Reporter.log("人脸支付预处理接口测试： " + datadriven.get("comment"), true);
		System.out.println("===============================================");
	}

	@DataProvider(name = "threeFactorsPay")
	public Iterator<Object[]> data4threeFactorsPay() throws IOException {
		return new ExcelProvider(this, "threeFactorsPay",1);
	}

	@AfterClass
	public void afterClass() {

	}

}