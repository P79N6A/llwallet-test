package com.llwallet.interfaces.test.api.online.combination;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.testng.Reporter;
import org.testng.annotations.Test;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.llwallet.interfaces.bean.combination.CombinationPay;
import com.llwallet.interfaces.bean.combination.PayeeInfo;
import com.llwallet.interfaces.bean.combination.PayerInfo;
import com.tools.dataprovider.ExcelProvider;
import com.tools.http.HttpRequest;
import com.tools.utils.ApiUtils;
import com.tools.utils.GenSign;
import com.tools.utils.MyConfig;
import com.tools.utils.Property;
import com.tools.utils.RSAUtil;
import org.testng.annotations.DataProvider;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;

/*
 * @author jiangxm
 * 组合支付申请接口测试
 */

public class CombinationPayOnlineTest {

	@BeforeClass
	public void beforeClass() throws InterruptedException {
		Property.set();
	}

	@SuppressWarnings("rawtypes")
	@Test(description = "组合支付申请接口测试", timeOut = 60000, dataProvider = "combinationPay")
	public void combinationPay(Map<String, String> datadriven) throws Exception {

		String timestamp = ApiUtils.getCurrentDateSecondStr();
		CombinationPay combinationPay = new CombinationPay();
		PayerInfo payerInfo = new PayerInfo();
		PayeeInfo payeeInfo = new PayeeInfo();
		combinationPay.setOid_partner(MyConfig.getPartner(datadriven.get("oid_partner")));
		combinationPay.setNo_order(MyConfig.getNoOrder(datadriven.get("no_order")));
		combinationPay.setDt_order(MyConfig.getDtOrder(datadriven.get("dt_order")));
		combinationPay.setSign_type(datadriven.get("sign_type"));
		if ("auto".equals(datadriven.get("timestamp"))) {
			combinationPay.setTimestamp(timestamp);
		} else {
			combinationPay.setTimestamp(datadriven.get("timestamp"));
		}
		combinationPay.setApi_version(datadriven.get("api_version"));
		combinationPay.setBusi_partner(datadriven.get("busi_partner"));
		combinationPay.setMoney_order(datadriven.get("money_order"));
		combinationPay.setName_goods(datadriven.get("name_goods"));
		combinationPay.setInfo_order(datadriven.get("info_order"));
		combinationPay.setValid_order(datadriven.get("valid_order"));
		combinationPay.setNotify_url(datadriven.get("notify_url"));
		combinationPay.setFaceauth_url(datadriven.get("faceauth_url"));
		if (datadriven.get("pwd_pay").length() != 0) {
			combinationPay.setPwd_pay(RSAUtil.encrypt(datadriven.get("pwd_pay"), Property.get("rsa_pub_key")));
		} else {

		}
		combinationPay.setRisk_item(datadriven.get("risk_item"));

		Map<String, String> params = new HashMap<String, String>();
		Iterator it = datadriven.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			if (!entry.getValue().equals("")) {
				params.put((String) entry.getKey(), (String) entry.getValue());
			}
			if (entry.getKey().equals("user_id")) {
				params.put((String) entry.getKey(), MyConfig.getUser(datadriven.get("user_id")));
			}
		}
		BeanUtils.populate(payerInfo, params);
		BeanUtils.populate(payeeInfo, params);
		combinationPay.setPayerInfo(payerInfo);
		combinationPay.setPayeeInfo(payeeInfo);
		combinationPay
				.setSign(GenSign.genSign(JSON.parseObject(JSON.toJSONString(combinationPay)), datadriven.get("key")));
		String reqJson = JSON.toJSONString(combinationPay);
		String combinationPayUrl = Property.get("llwallet.combinationPay.url.online");

		String rsp = HttpRequest.httpPostWithJSON(combinationPayUrl, reqJson);
		// 组合支付申请请求返回检查
		System.out.println("oid_paybill：" + JSONObject.parseObject(rsp).getString("oid_paybill"));
		System.out.println("no_order：" + JSONObject.parseObject(rsp).getString("no_order"));
		System.out.println("payload：" + JSONObject.parseObject(rsp).getString("payload"));

		assert rsp.contains(datadriven.get("result_code"));

		Reporter.log("组合支付申请接口测试： " + datadriven.get("comment"), true);
		System.out.println("===============================================");
	}

	@DataProvider(name = "combinationPay")
	public Iterator<Object[]> data4combinationPay() throws IOException {
		return new ExcelProvider(this, "combinationPay",13);
	}

	@AfterClass
	public void afterClass() {

	}

}