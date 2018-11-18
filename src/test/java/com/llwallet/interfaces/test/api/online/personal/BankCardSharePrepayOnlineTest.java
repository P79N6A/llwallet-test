package com.llwallet.interfaces.test.api.online.personal;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import org.testng.Reporter;
import org.testng.annotations.Test;
import com.alibaba.fastjson.JSON;
import com.llwallet.interfaces.bean.personal.BankCardSharePrepay;
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
 * 钱包银行卡支付预处理接口测试
 */

public class BankCardSharePrepayOnlineTest {

	@BeforeClass
	public void beforeClass() throws InterruptedException {
		Property.set();
	}

	@Test(description = "钱包银行卡支付预处理接口测试", timeOut = 60000, dataProvider = "bankCardSharePrepay")
	public void bankCardSharePrepay(Map<String, String> datadriven) throws Exception {

		Thread.sleep(1000);
		String dt_order = ApiUtils.getCurrentDateSecondStr();
		String no_order = ApiUtils.getCurrentDateMillisecondStr();
		
		BankCardSharePrepay bankCardSharePrepay = new BankCardSharePrepay();
		bankCardSharePrepay.setOid_partner(datadriven.get("oid_partner"));
		bankCardSharePrepay.setSign_type(datadriven.get("sign_type"));
		bankCardSharePrepay.setUser_id(datadriven.get("user_id"));
		bankCardSharePrepay.setBusi_partner(datadriven.get("busi_partner"));
		bankCardSharePrepay.setApi_version(datadriven.get("api_version"));		
		if ("auto".equals(datadriven.get("no_order"))){
			bankCardSharePrepay.setNo_order(no_order);
		}else{
			bankCardSharePrepay.setNo_order(datadriven.get("no_order"));
		}
		if ("auto".equals(datadriven.get("dt_order"))){
			bankCardSharePrepay.setDt_order(dt_order);
		}else{
			bankCardSharePrepay.setDt_order(datadriven.get("dt_order"));
		}
		bankCardSharePrepay.setName_goods(datadriven.get("name_goods"));
		bankCardSharePrepay.setMoney_order(datadriven.get("money_order"));
		bankCardSharePrepay.setInfo_order(datadriven.get("info_order"));
		bankCardSharePrepay.setNotify_url(datadriven.get("notify_url"));
		bankCardSharePrepay.setValid_order(datadriven.get("valid_order"));		
		bankCardSharePrepay.setRisk_item(datadriven.get("risk_item"));		
		bankCardSharePrepay.setPay_type(datadriven.get("pay_type"));
		bankCardSharePrepay.setCard_no(datadriven.get("card_no"));
		bankCardSharePrepay.setBank_code(datadriven.get("bank_code"));
		bankCardSharePrepay.setBind_mob(datadriven.get("bind_mob"));
		bankCardSharePrepay.setVali_date(datadriven.get("vali_date"));
		bankCardSharePrepay.setCvv2(datadriven.get("cvv2"));
		bankCardSharePrepay.setNo_agree(datadriven.get("no_agree"));
		if (datadriven.get("pwd_pay").length() != 0) {
			bankCardSharePrepay.setPwd_pay(RSAUtil.encrypt(datadriven.get("pwd_pay"), Property.get("rsa_pub_key")));
		} else {
			bankCardSharePrepay.setPwd_pay(datadriven.get("pwd_pay"));
		}		
		bankCardSharePrepay.setShareing_data(no_order+"1"+datadriven.get("shareing_data"));
        bankCardSharePrepay.setSign(GenSign.genSign(JSON.parseObject(JSON.toJSONString(bankCardSharePrepay)),datadriven.get("key")));
		String reqJson = JSON.toJSONString(bankCardSharePrepay);
		String bankCardSharePrepayUrl = Property.get("llwallet.bankCardSharePrepay.url.online");

		// 钱包签约支付预处理请求提交
		HttpFixture hf = new HttpFixture();
		hf.setEncode("UTF-8");
		hf.setUrl(bankCardSharePrepayUrl);
		hf.addHeaderValue("Content-Type", "application/json");
		hf.addRequestBody(reqJson);
		hf.Post();
		// 钱包签约支付预处理请求返回检查
		assert hf.getResponseBody().contains(datadriven.get("result_code"));

		Reporter.log("钱包银行卡支付预处理接口测试： " + datadriven.get("comment"), true);
		System.out.println("===============================================");
	}

	@DataProvider(name = "bankCardSharePrepay")
	public Iterator<Object[]> data4bankCardSharePrepay() throws IOException {
		return new ExcelProvider(this, "bankCardSharePrepay",1);
	}
	
	@AfterClass
	public void afterClass() {

	}

}