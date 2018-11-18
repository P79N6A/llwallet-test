package com.llwallet.interfaces.test.api.online.personal;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import org.testng.Reporter;
import org.testng.annotations.Test;
import com.alibaba.fastjson.JSON;
import com.llwallet.interfaces.bean.personal.BalancePwdPay;
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
 * 余额消费接口测试
 */

public class BalancePwdPayOnlineTest {

	@BeforeClass
	public void beforeClass() throws InterruptedException {
		Property.set();
	}

	@Test(description = "余额消费接口测试", timeOut = 60000, dataProvider = "balancePwdPay")
	public void balancePwdPay(Map<String, String> datadriven) throws Exception {

		Thread.sleep(1000);
		String dt_order = ApiUtils.getCurrentDateSecondStr();
		String no_order = ApiUtils.getCurrentDateMillisecondStr();
		
		BalancePwdPay balancePwdPay = new BalancePwdPay();
		balancePwdPay.setOid_partner(datadriven.get("oid_partner"));
		balancePwdPay.setSign_type(datadriven.get("sign_type"));
		balancePwdPay.setUser_id(datadriven.get("user_id"));
		if ("auto".equals(datadriven.get("no_order"))){
			balancePwdPay.setNo_order(no_order);
		}else{
			balancePwdPay.setNo_order(datadriven.get("no_order"));
		}
		if ("auto".equals(datadriven.get("dt_order"))){
			balancePwdPay.setDt_order(dt_order);
		}else{
			balancePwdPay.setDt_order(datadriven.get("dt_order"));
		}		
		balancePwdPay.setBusi_partner(datadriven.get("busi_partner"));
		balancePwdPay.setName_goods(datadriven.get("name_goods"));
		balancePwdPay.setMoney_order(datadriven.get("money_order"));
		balancePwdPay.setInfo_order(datadriven.get("info_order"));
		balancePwdPay.setValid_order(datadriven.get("valid_order"));
		if (datadriven.get("pwd_pay").length() != 0) {
			balancePwdPay.setPwd_pay(RSAUtil.encrypt(datadriven.get("pwd_pay"), Property.get("rsa_pub_key")));
		} else {
//			balancePwdPay.setPwd_pay(datadriven.get("pwd_pay"));
		}
		balancePwdPay.setNotify_url(datadriven.get("notify_url"));
		balancePwdPay.setRisk_item(datadriven.get("risk_item"));
		balancePwdPay.setShareing_data(datadriven.get("shareing_data"));
		balancePwdPay.setCol_oidpartner(datadriven.get("col_oidpartner"));
		balancePwdPay.setCol_userid(datadriven.get("col_userid"));
		balancePwdPay.setSecured_partner(datadriven.get("secured_partner"));
		if (datadriven.get("buyer_confirm_valid").length()>0){
			balancePwdPay.setBuyer_confirm_valid(Integer.parseInt(datadriven.get("buyer_confirm_valid")));
		}
		if (datadriven.get("seller_send_valid").length()>0){
			balancePwdPay.setSeller_send_valid(Integer.parseInt(datadriven.get("seller_send_valid")));
		}
		balancePwdPay.setSign(GenSign.genSign(JSON.parseObject(JSON.toJSONString(balancePwdPay)),datadriven.get("key")));
		String reqJson = JSON.toJSONString(balancePwdPay);
		String balancePwdPayUrl = Property.get("llwallet.balancePwdPay.url.online");

		// 余额消费请求提交
		HttpFixture hf = new HttpFixture();
		hf.setEncode("UTF-8");
		hf.setUrl(balancePwdPayUrl);
		hf.addHeaderValue("Content-Type", "application/json");
		hf.addRequestBody(reqJson);
		hf.Post();
		// 余额消费请求返回检查
		assert hf.getResponseBody().contains(datadriven.get("result_code"));

		Reporter.log("余额消费接口测试： " + datadriven.get("comment"), true);
		System.out.println("===============================================");
	}

	@DataProvider(name = "balancePwdPay")
	public Iterator<Object[]> data4balancePwdPay() throws IOException {
		return new ExcelProvider(this, "balancePwdPay",1);
	}

	@AfterClass
	public void afterClass() {

	}

}