package com.llwallet.interfaces.test.api.test.custom;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import org.testng.Reporter;
import org.testng.annotations.Test;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.llwallet.interfaces.bean.custom.CashOutCardBind;
import com.llwallet.interfaces.bean.custom.CashOutCardUnBind;
import com.llwallet.interfaces.bean.personal.BankCardPay;
import com.llwallet.interfaces.bean.personal.BankCardPrepay;
import com.llwallet.interfaces.bean.personal.UserBankCard;
import com.llwallet.interfaces.dao.DbUtils;
import com.tools.dataprovider.ExcelProvider;
import com.tools.http.HttpFixture;
import com.tools.utils.ApiUtils;
import com.tools.utils.GenSign;
import com.tools.utils.Property;
import com.tools.utils.RSAUtil;
import com.tools.utils.TelnetUtil;

import org.testng.annotations.DataProvider;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;

/*
 * @author jiangxm
 * 解绑提现卡接口测试
 */

public class CashOutCardUnBindTest {

	@BeforeClass
	public void beforeClass() throws InterruptedException {
		Property.set();
	}

	@Test(description = "解绑提现卡接口测试", timeOut = 60000, dataProvider = "cashOutCardUnBind")
	public void cashOutCardUnBind(Map<String, String> datadriven) throws Exception {

		CashOutCardBind cashOutCardBind = new CashOutCardBind();
		cashOutCardBind.setOid_partner(datadriven.get("oid_partner"));
		cashOutCardBind.setSign_type(datadriven.get("sign_type"));
		cashOutCardBind.setUser_id(datadriven.get("user_id"));
		cashOutCardBind.setCard_no(datadriven.get("card_no"));
		cashOutCardBind.setBind_mob(datadriven.get("bind_mob"));
		cashOutCardBind.setAcct_name(datadriven.get("acct_name"));
		cashOutCardBind.setId_no(datadriven.get("id_no"));
		if (datadriven.get("pwd_pay").length() != 0) {
			cashOutCardBind.setPwd_pay(RSAUtil.encrypt(datadriven.get("pwd_pay"), Property.get("rsa_pub_key")));
		} else {
			cashOutCardBind.setPwd_pay(datadriven.get("pwd_pay"));
		}
		cashOutCardBind.setRisk_item(datadriven.get("risk_item"));
		cashOutCardBind.setSign(GenSign.genSign(JSON.parseObject(JSON.toJSONString(cashOutCardBind)), datadriven.get("key")));
		String reqJson = JSON.toJSONString(cashOutCardBind);
		String cashOutCardBindUrl = Property.get("llwallet.cashOutCardBind.url");

		// 绑定提现卡请求提交
		Reporter.log("绑定提现卡-----:" , true);
		HttpFixture hf = new HttpFixture();
		hf.setEncode("UTF-8");
		hf.setUrl(cashOutCardBindUrl);
		hf.addHeaderValue("Content-Type", "application/json");
		hf.addRequestBody(reqJson);
		hf.Post();

		// 绑定提现卡请求返回检查
		assert hf.getResponseBody().contains(datadriven.get("result_code"));
		String no_agree = JSONObject.parseObject(hf.getResponseBody()).getString("no_agree");
				
		CashOutCardUnBind cashOutCardUnBind = new CashOutCardUnBind();
		cashOutCardUnBind.setOid_partner(datadriven.get("oid_partner1"));
		cashOutCardUnBind.setSign_type(datadriven.get("sign_type1"));
		cashOutCardUnBind.setUser_id(datadriven.get("user_id1"));
		if ("auto".equals(datadriven.get("no_agree1"))){
			cashOutCardUnBind.setNo_agree(no_agree);
		}else{
			cashOutCardUnBind.setNo_agree(datadriven.get("no_agree1"));
		}		
		if (datadriven.get("pwd_pay1").length() != 0) {
			cashOutCardUnBind.setPwd_pay(RSAUtil.encrypt(datadriven.get("pwd_pay1"), Property.get("rsa_pub_key")));
		} else {
			cashOutCardUnBind.setPwd_pay(datadriven.get("pwd_pay1"));
		}
		cashOutCardUnBind.setSign(GenSign.genSign(JSON.parseObject(JSON.toJSONString(cashOutCardUnBind)), datadriven.get("key1")));
		String reqJson1 = JSON.toJSONString(cashOutCardUnBind);
		String cashOutCardUnBindUrl = Property.get("llwallet.cashOutCardUnBind.url");

		// 解绑提现卡请求提交
		Reporter.log("解绑提现卡-----:" , true);
		hf.nextRequest();
		hf.setUrl(cashOutCardUnBindUrl);
		hf.addRequestBody(reqJson1);
		hf.Post();

		// 解绑提现卡请求返回检查
		assert hf.getResponseBody().contains(datadriven.get("result_code"));
        
		UserBankCard userBankCard = new UserBankCard();
		userBankCard.setOid_partner(datadriven.get("oid_partner"));
		userBankCard.setSign_type(datadriven.get("sign_type"));
		userBankCard.setUser_id(datadriven.get("user_id"));
		userBankCard.setPay_type(datadriven.get("pay_type"));
		if ("auto".equals(datadriven.get("no_agree1"))){			
			userBankCard.setNo_agree(no_agree);
		}else{
			userBankCard.setNo_agree(datadriven.get("no_agree1"));
		}
		userBankCard.setCard_no(datadriven.get("card_no"));
		userBankCard.setSign(GenSign.genSign(JSON.parseObject(JSON.toJSONString(userBankCard)),datadriven.get("key")));
		String reqJson2 = JSON.toJSONString(userBankCard);
		String userBankCardUrl = Property.get("llwallet.userBankCard.url");

		// 签约查询请求提交
		Reporter.log("签约查询-----:" , true);
		hf.nextRequest();
		hf.setUrl(userBankCardUrl);
		hf.addRequestBody(reqJson2);
		hf.Post();
		// 签约查询请求返回检查
//		System.out.println(hf.getResponseBody().contains(no_agree));
		assert String.valueOf(hf.getResponseBody().contains(no_agree)).equals(datadriven.get("boolean"));
		
		Reporter.log("解绑提现卡接口测试： " + datadriven.get("comment"), true);
		System.out.println("===============================================");
	}
	
	@Test(description = "解绑提现卡接口测试", timeOut = 60000, dataProvider = "cashOutCardUnBind1")
	public void cashOutCardUnBind1(Map<String, String> datadriven) throws Exception {

		// 清除支付密码3次错误缓存
		TelnetUtil telnetUtil = new TelnetUtil(Property.get("host"), Integer.parseInt(Property.get("port")));
		StringBuffer command = new StringBuffer();
		if (datadriven.get("result_code").contains("7701")) {
			DbUtils dbUtils = new DbUtils();			
			String oid_userno = dbUtils.queryOidUserNo(datadriven.get("oid_partner1"), datadriven.get("user_id1")).getOid_userno().trim();
			command.append("delete ").append(oid_userno).append("api_pwd_valid_times_pay_pwd");
			telnetUtil.execute(command.toString());
		}
		telnetUtil.disconnect();
		
		CashOutCardUnBind cashOutCardUnBind = new CashOutCardUnBind();
		cashOutCardUnBind.setOid_partner(datadriven.get("oid_partner1"));
		cashOutCardUnBind.setSign_type(datadriven.get("sign_type1"));
		cashOutCardUnBind.setUser_id(datadriven.get("user_id1"));
		cashOutCardUnBind.setNo_agree(datadriven.get("no_agree1"));	
		if (datadriven.get("pwd_pay1").length() != 0) {
			cashOutCardUnBind.setPwd_pay(RSAUtil.encrypt(datadriven.get("pwd_pay1"), Property.get("rsa_pub_key")));
		} else {
			cashOutCardUnBind.setPwd_pay(datadriven.get("pwd_pay1"));
		}
		cashOutCardUnBind.setSign(GenSign.genSign(JSON.parseObject(JSON.toJSONString(cashOutCardUnBind)), datadriven.get("key1")));
		String reqJson1 = JSON.toJSONString(cashOutCardUnBind);
		String cashOutCardUnBindUrl = Property.get("llwallet.cashOutCardUnBind.url");

		// 解绑提现卡请求提交
		Reporter.log("解绑提现卡-----:" , true);
		HttpFixture hf = new HttpFixture();
		hf.setEncode("UTF-8");
		hf.addHeaderValue("Content-Type", "application/json");
		hf.setUrl(cashOutCardUnBindUrl);
		hf.addRequestBody(reqJson1);
		hf.Post();

		// 解绑提现卡请求返回检查
		assert hf.getResponseBody().contains(datadriven.get("result_code"));
        
		Reporter.log("解绑提现卡接口测试： " + datadriven.get("comment"), true);
		System.out.println("===============================================");
	}
	
	@Test(description = "解绑提现卡接口测试", timeOut = 60000, dataProvider = "cashOutCardUnBind2")
	public void cashOutCardUnBind2(Map<String, String> datadriven) throws Exception {

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
		if ("auto".equals(datadriven.get("no_agree"))){			
			DbUtils dbUtils = new DbUtils();
			String no_agree = dbUtils.querySigned(datadriven.get("user_id")).getAgreementno();
			bankCardPrepay.setNo_agree(no_agree);
		}else{
			bankCardPrepay.setNo_agree(datadriven.get("no_agree"));
		}		
		if (datadriven.get("pwd_pay").length() != 0) {
			bankCardPrepay.setPwd_pay(RSAUtil.encrypt(datadriven.get("pwd_pay"), Property.get("rsa_pub_key")));
		} else {

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
		String bankCardPrepayUrl = Property.get("llwallet.bankCardPrepay.url");

		// 钱包签约支付预处理请求提交
		Reporter.log("银行卡签约支付-----:" , true);
		HttpFixture hf = new HttpFixture();
		hf.setEncode("UTF-8");
		hf.setUrl(bankCardPrepayUrl);
		hf.addHeaderValue("Content-Type", "application/json");
		hf.addRequestBody(reqJson);
		hf.Post();
		
		Thread.sleep(100);
		DbUtils dbUtils = new DbUtils();
		String token = JSONObject.parseObject(hf.getResponseBody()).getString("token");
		String verify_code = dbUtils.queryCdSmscd(datadriven.get("bind_mob")).getTno_smscd();
		
		BankCardPay bankCardPay = new BankCardPay();
		bankCardPay.setOid_partner(datadriven.get("oid_partner1"));		
		if ("auto".equals(datadriven.get("token1"))){
			bankCardPay.setToken(token);
		}else{
			bankCardPay.setToken(datadriven.get("token1"));
		}		
		bankCardPay.setSign_type(datadriven.get("sign_type1"));
		if ("auto".equals(datadriven.get("no_order1"))){
			bankCardPay.setNo_order(no_order);
		}else{
			bankCardPay.setNo_order(datadriven.get("no_order1"));
		}		
		bankCardPay.setMoney_order(datadriven.get("money_order1"));
		bankCardPay.setCol_oidpartner(datadriven.get("col_oidpartner1"));
		if ("auto".equals(datadriven.get("verify_code1"))){
			bankCardPay.setVerify_code(verify_code);
		}else{
			bankCardPay.setVerify_code(datadriven.get("verify_code1"));
		}
		bankCardPay.setSign(GenSign.genSign(JSON.parseObject(JSON.toJSONString(bankCardPay)),datadriven.get("key1")));
		String reqJson1 = JSON.toJSONString(bankCardPay);
		String bankCardPayUrl = Property.get("llwallet.bankCardPay.url");
		
		// 银行卡支付验证请求提交
		Reporter.log("银行卡签约支付验证-----:" , true);
		hf.nextRequest();
		hf.setUrl(bankCardPayUrl);
		hf.addRequestBody(reqJson1);
		hf.Post();

		// 银行卡支付验证请求返回检查
		assert hf.getResponseBody().contains(datadriven.get("result_code"));
		String no_agree = JSONObject.parseObject(hf.getResponseBody()).getString("no_agree");
				
		CashOutCardUnBind cashOutCardUnBind = new CashOutCardUnBind();
		cashOutCardUnBind.setOid_partner(datadriven.get("oid_partner2"));
		cashOutCardUnBind.setSign_type(datadriven.get("sign_type2"));
		cashOutCardUnBind.setUser_id(datadriven.get("user_id2"));
		if ("auto".equals(datadriven.get("no_agree2"))){
			cashOutCardUnBind.setNo_agree(no_agree);
		}else{
			cashOutCardUnBind.setNo_agree(datadriven.get("no_agree2"));
		}
		if (datadriven.get("pwd_pay2").length() != 0) {
			cashOutCardUnBind.setPwd_pay(RSAUtil.encrypt(datadriven.get("pwd_pay2"), Property.get("rsa_pub_key")));
		} else {
			cashOutCardUnBind.setPwd_pay(datadriven.get("pwd_pay2"));
		}
		cashOutCardUnBind.setSign(GenSign.genSign(JSON.parseObject(JSON.toJSONString(cashOutCardUnBind)), datadriven.get("key2")));
		String reqJson2 = JSON.toJSONString(cashOutCardUnBind);
		String cashOutCardUnBindUrl = Property.get("llwallet.cashOutCardUnBind.url");

		// 解绑提现卡请求提交
		Reporter.log("解绑提现卡-----:" , true);
		hf.nextRequest();
		hf.setUrl(cashOutCardUnBindUrl);
		hf.addRequestBody(reqJson2);
		hf.Post();

		// 解绑提现卡请求返回检查
		assert hf.getResponseBody().contains(datadriven.get("result_code"));
        
		UserBankCard userBankCard = new UserBankCard();
		userBankCard.setOid_partner(datadriven.get("oid_partner"));
		userBankCard.setSign_type(datadriven.get("sign_type"));
		userBankCard.setUser_id(datadriven.get("user_id"));
		userBankCard.setPay_type(datadriven.get("pay_type"));
		if ("auto".equals(datadriven.get("no_agree2"))){			
			userBankCard.setNo_agree(no_agree);
		}else{
			userBankCard.setNo_agree(datadriven.get("no_agree2"));
		}
		userBankCard.setCard_no(datadriven.get("card_no"));
		userBankCard.setSign(GenSign.genSign(JSON.parseObject(JSON.toJSONString(userBankCard)),datadriven.get("key")));
		String reqJson3 = JSON.toJSONString(userBankCard);
		String userBankCardUrl = Property.get("llwallet.userBankCard.url");

		// 签约查询请求提交
		Reporter.log("签约查询-----:" , true);
		hf.nextRequest();
		hf.setUrl(userBankCardUrl);
		hf.addRequestBody(reqJson3);
		hf.Post();
		// 签约查询请求返回检查
		assert hf.getResponseBody().contains(no_agree);
		
		Reporter.log("解绑提现卡接口测试： " + datadriven.get("comment"), true);
		System.out.println("===============================================");
	}
	
	@DataProvider(name = "cashOutCardUnBind")
	public Iterator<Object[]> data4cashOutCardUnBind() throws IOException {
		return new ExcelProvider(this, "cashOutCardUnBind",999);
	}

	@DataProvider(name = "cashOutCardUnBind1")
	public Iterator<Object[]> data4cashOutCardUnBind1() throws IOException {
		return new ExcelProvider(this, "cashOutCardUnBind1",1);
	}
	
	@DataProvider(name = "cashOutCardUnBind2")
	public Iterator<Object[]> data4cashOutCardUnBind2() throws IOException {
		return new ExcelProvider(this, "cashOutCardUnBind2",999);
	}
	
	@AfterClass
	public void afterClass() {

	}
}