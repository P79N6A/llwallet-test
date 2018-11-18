package com.llwallet.interfaces.test.api.online.personal;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import org.testng.Reporter;
import org.testng.annotations.Test;
import com.alibaba.fastjson.JSON;
import com.llwallet.interfaces.bean.personal.BankCardPrepay;
import com.tools.dataprovider.ExcelProvider;
import com.tools.http.HttpFixture;
import com.tools.utils.GenSign;
import com.tools.utils.Property;
import com.tools.utils.RSAUtil;
import com.tools.utils.ApiUtils;
import org.testng.annotations.DataProvider;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;

/*
 * @author jiangxm
 * 钱包签约支付预处理接口测试
 */

public class BankCardPrepayOnlineTest {

	@BeforeClass
	public void beforeClass() throws InterruptedException {
		Property.set();
	}

	@Test(description = "钱包签约支付预处理接口测试", timeOut = 60000, dataProvider = "bankCardPrepay")
	public void bankCardPrepay(Map<String, String> datadriven) throws Exception {

		Thread.sleep(1000);
		String dt_order = ApiUtils.getCurrentDateSecondStr();
		String no_order = ApiUtils.getCurrentDateMillisecondStr();
		String user_id = ApiUtils.getCurrentDateSecondStr() + "@anonymous";
		String bind_mob = ApiUtils.getMobStr();
		
		BankCardPrepay bankCardPrepay = new BankCardPrepay();
		bankCardPrepay.setOid_partner(datadriven.get("oid_partner"));
		bankCardPrepay.setSign_type(datadriven.get("sign_type"));
		if ("auto".equals(datadriven.get("user_id"))){
			bankCardPrepay.setUser_id(user_id);
		}else{
			bankCardPrepay.setUser_id(datadriven.get("user_id"));
		}
		bankCardPrepay.setBusi_partner(datadriven.get("busi_partner"));
		bankCardPrepay.setApi_version(datadriven.get("api_version"));		
		if ("auto".equals(datadriven.get("no_order"))){
			bankCardPrepay.setNo_order(no_order);
		}else{
			bankCardPrepay.setNo_order(datadriven.get("no_order"));
		}
		if ("auto".equals(datadriven.get("dt_order"))){
			bankCardPrepay.setDt_order(dt_order);
		}else{
			bankCardPrepay.setDt_order(datadriven.get("dt_order"));
		}
		bankCardPrepay.setName_goods(datadriven.get("name_goods"));
		bankCardPrepay.setMoney_order(datadriven.get("money_order"));
		bankCardPrepay.setInfo_order(datadriven.get("info_order"));
		bankCardPrepay.setNotify_url(datadriven.get("notify_url"));
		bankCardPrepay.setValid_order(datadriven.get("valid_order"));		
		bankCardPrepay.setRisk_item(datadriven.get("risk_item"));		
		bankCardPrepay.setPay_type(datadriven.get("pay_type"));
		bankCardPrepay.setCard_no(datadriven.get("card_no"));
		bankCardPrepay.setBank_code(datadriven.get("bank_code"));
		if ("auto".equals(datadriven.get("bind_mob"))){
			bankCardPrepay.setBind_mob(bind_mob);
		}else{
			bankCardPrepay.setBind_mob(datadriven.get("bind_mob"));
		}
		bankCardPrepay.setVali_date(datadriven.get("vali_date"));
		bankCardPrepay.setCvv2(datadriven.get("cvv2"));
		bankCardPrepay.setNo_agree(datadriven.get("no_agree"));
		if (datadriven.get("pwd_pay").length() != 0) {
			bankCardPrepay.setPwd_pay(RSAUtil.encrypt(datadriven.get("pwd_pay"), Property.get("rsa_pub_key")));
		} else {
			bankCardPrepay.setPwd_pay(datadriven.get("pwd_pay"));
		}		
		bankCardPrepay.setCol_oidpartner(datadriven.get("col_oidpartner"));
		bankCardPrepay.setShareing_data(datadriven.get("shareing_data"));
		bankCardPrepay.setType_user(datadriven.get("type_user"));
		bankCardPrepay.setAcct_name(datadriven.get("acct_name"));
		bankCardPrepay.setId_type(datadriven.get("id_type"));
		bankCardPrepay.setId_no(datadriven.get("id_no"));
		bankCardPrepay.setCol_userid(datadriven.get("col_userid"));
		bankCardPrepay.setSecured_partner(datadriven.get("secured_partner"));
		if (datadriven.get("buyer_confirm_valid").length()>0){
			bankCardPrepay.setBuyer_confirm_valid(datadriven.get("buyer_confirm_valid"));
		}
		if (datadriven.get("seller_send_valid").length()>0){
			bankCardPrepay.setSeller_send_valid(datadriven.get("seller_send_valid"));
		}
        bankCardPrepay.setSign(GenSign.genSign(JSON.parseObject(JSON.toJSONString(bankCardPrepay)),datadriven.get("key")));
		String reqJson = JSON.toJSONString(bankCardPrepay);
		String bankCardPrepayUrl = Property.get("llwallet.bankCardPrepay.url.online");

		// 钱包签约支付预处理请求提交
		HttpFixture hf = new HttpFixture();
		hf.setEncode("UTF-8");
		hf.setUrl(bankCardPrepayUrl);
		hf.addHeaderValue("Content-Type", "application/json");
		hf.addRequestBody(reqJson);
		hf.Post();
		// 钱包签约支付预处理请求返回检查
		assert hf.getResponseBody().contains(datadriven.get("result_code"));

		Reporter.log("钱包签约支付预处理接口测试： " + datadriven.get("comment"), true);
		System.out.println("===============================================");
	}

	@DataProvider(name = "bankCardPrepay")
	public Iterator<Object[]> data4bankCardPrepay() throws IOException {
		return new ExcelProvider(this, "bankCardPrepay",16);
	}

	@AfterClass
	public void afterClass() {

	}

}
