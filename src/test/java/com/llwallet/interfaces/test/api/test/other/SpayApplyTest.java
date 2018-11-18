package com.llwallet.interfaces.test.api.test.other;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import org.testng.Reporter;
import org.testng.annotations.Test;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.llwallet.interfaces.bean.other.SpayApply;
//import com.alibaba.fastjson.JSONObject;
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
 * 三星pay支付接口测试
 */

public class SpayApplyTest {

	@BeforeClass
	public void beforeClass() throws InterruptedException {
		Property.set();
	}

	@Test(description = "三星pay支付", timeOut = 60000, dataProvider = "spayApply")
	public void spayApply(Map<String, String> datadriven) throws Exception {

		Thread.sleep(100);
		String dt_order = ApiUtils.getCurrentDateSecondStr();
		String no_order = ApiUtils.getCurrentDateMillisecondStr();
		String user_id = ApiUtils.getCurrentDateSecondStr() + "@anonymous";

		SpayApply spayApply = new SpayApply();
		spayApply.setOid_partner(datadriven.get("oid_partner"));
		spayApply.setSign_type(datadriven.get("sign_type"));
		if ("auto".equals(datadriven.get("user_id"))) {
			spayApply.setUser_id(user_id);
		} else {
			spayApply.setUser_id(datadriven.get("user_id"));
		}
		spayApply.setName_user(datadriven.get("name_user"));
		spayApply.setBusi_partner(datadriven.get("busi_partner"));
		if ("auto".equals(datadriven.get("no_order"))) {
			spayApply.setNo_order(no_order);
		} else {
			spayApply.setNo_order(datadriven.get("no_order"));
		}
		if ("auto".equals(datadriven.get("dt_order"))) {
			spayApply.setDt_order(dt_order);
		} else {
			spayApply.setDt_order(datadriven.get("dt_order"));
		}
		spayApply.setName_goods(datadriven.get("name_goods"));
		spayApply.setInfo_order(datadriven.get("info_order"));
		spayApply.setMoney_order(datadriven.get("money_order"));
		spayApply.setNotify_url(datadriven.get("notify_url"));
		spayApply.setReturn_url(datadriven.get("return_url"));
		spayApply.setRisk_item(datadriven.get("risk_item"));
		spayApply.setPay_type(datadriven.get("pay_type"));
		spayApply.setId_type(datadriven.get("id_type"));
		spayApply.setId_no(datadriven.get("id_no"));
		spayApply.setCol_userid(datadriven.get("col_userid"));
		spayApply.setCol_oidpartner(datadriven.get("col_oidpartner"));
		spayApply.setShareing_data(datadriven.get("shareing_data"));
		spayApply.setSecured_partner(datadriven.get("secured_partner"));
		if (datadriven.get("buyer_confirm_valid").length() > 0) {
			spayApply.setBuyer_confirm_valid(datadriven.get("buyer_confirm_valid"));
		}
		if (datadriven.get("seller_send_valid").length() > 0) {
			spayApply.setSeller_send_valid(datadriven.get("seller_send_valid"));
		}
		if (datadriven.get("valid_order").length() > 0) {
			spayApply.setValid_order(Long.parseLong(datadriven.get("valid_order")));
		}
		spayApply.setSign(GenSign.genSign(JSON.parseObject(JSON.toJSONString(spayApply)), datadriven.get("key")));
		String reqJson = JSON.toJSONString(spayApply);
		String spayApplyUrl = Property.get("llwallet.spayApply.url");

		// 三星pay支付处理请求提交
		HttpFixture hf = new HttpFixture();
		hf.setEncode("UTF-8");
		hf.setUrl(spayApplyUrl);
		hf.addHeaderValue("Content-Type", "application/json");
		hf.addRequestBody(reqJson);
		hf.Post();
		Reporter.log("支付单号： " + JSONObject.parseObject(hf.getResponseBody()).getString("oid_paybill"), true);
		Reporter.log("支付内容： " + JSONObject.parseObject(hf.getResponseBody()).getString("payLoad"), true);

		// 三星pay支付处理请求返回检查
		assert hf.getResponseBody().contains(datadriven.get("result_code"));

		Reporter.log("三星pay支付接口测试： " + datadriven.get("comment"), true);
		System.out.println("===============================================");
	}

	@DataProvider(name = "spayApply")
	public Iterator<Object[]> data4spayApply() throws IOException {
		return new ExcelProvider(this, "spayApply");
	}

	@AfterClass
	public void afterClass() {

	}

}