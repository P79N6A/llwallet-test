package com.llwallet.interfaces.test.api.test.personal;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import org.testng.Reporter;
import org.testng.annotations.Test;
import com.alibaba.fastjson.JSON;
import com.llwallet.interfaces.bean.personal.BankCardQrPrepay;
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
 * 钱包银行卡支付预处理接口（非同人消费）测试
 */

public class BankCardQrPrepayTest {

	@BeforeClass
	public void beforeClass() throws InterruptedException {
		Property.set();
	}

	@Test(description = "钱包银行卡支付预处理接口（非同人消费）测试", timeOut = 60000, dataProvider = "bankCardQrPrepay")
	public void bankCardQrPrepay(Map<String, String> datadriven) throws Exception {

		Thread.sleep(1000);
		String dt_order = ApiUtils.getCurrentDateSecondStr();
		String no_order = ApiUtils.getCurrentDateMillisecondStr();
		
		BankCardQrPrepay bankCardQrPrepay = new BankCardQrPrepay();
		bankCardQrPrepay.setOid_partner(datadriven.get("oid_partner"));
		bankCardQrPrepay.setSign_type(datadriven.get("sign_type"));
		bankCardQrPrepay.setPay_userid(datadriven.get("pay_userid"));
		bankCardQrPrepay.setCol_userid(datadriven.get("col_userid"));
		bankCardQrPrepay.setBusi_partner(datadriven.get("busi_partner"));
		bankCardQrPrepay.setApi_version(datadriven.get("api_version"));		
		if ("auto".equals(datadriven.get("no_order"))){
			bankCardQrPrepay.setNo_order(no_order);
		}else{
			bankCardQrPrepay.setNo_order(datadriven.get("no_order"));
		}
		if ("auto".equals(datadriven.get("dt_order"))){
			bankCardQrPrepay.setDt_order(dt_order);
		}else{
			bankCardQrPrepay.setDt_order(datadriven.get("dt_order"));
		}

		bankCardQrPrepay.setName_goods(datadriven.get("name_goods"));
		bankCardQrPrepay.setMoney_order(Float.parseFloat(datadriven.get("money_order")));
		bankCardQrPrepay.setInfo_order(datadriven.get("info_order"));
		bankCardQrPrepay.setNotify_url(datadriven.get("notify_url"));
		bankCardQrPrepay.setValid_order(Integer.parseInt(datadriven.get("valid_order")));
		bankCardQrPrepay.setRisk_item(datadriven.get("risk_item"));		
		bankCardQrPrepay.setPay_type(datadriven.get("pay_type"));
		bankCardQrPrepay.setCard_no(datadriven.get("card_no"));
		bankCardQrPrepay.setBank_code(datadriven.get("bank_code"));
		bankCardQrPrepay.setBind_mob(datadriven.get("bind_mob"));
		bankCardQrPrepay.setVali_date(datadriven.get("vali_date"));
		bankCardQrPrepay.setCvv2(datadriven.get("cvv2"));
		bankCardQrPrepay.setNo_agree(datadriven.get("no_agree"));
		if (datadriven.get("pwd_pay").length() != 0) {
			bankCardQrPrepay.setPwd_pay(RSAUtil.encrypt(datadriven.get("pwd_pay"), Property.get("rsa_pub_key")));
		} else {
			bankCardQrPrepay.setPwd_pay(datadriven.get("pwd_pay"));
		}
		
		bankCardQrPrepay.setShareing_data(datadriven.get("shareing_data"));
		bankCardQrPrepay.setSign(GenSign.genSign(JSON.parseObject(JSON.toJSONString(bankCardQrPrepay)),datadriven.get("key")));
		String reqJson = JSON.toJSONString(bankCardQrPrepay);
		String bankCardQrPrepayUrl = Property.get("llwallet.bankCardQrPrepay.url");

		// 钱包签约支付预处理请求提交
		HttpFixture hf = new HttpFixture();
		hf.setEncode("UTF-8");
		hf.setUrl(bankCardQrPrepayUrl);
		hf.addHeaderValue("Content-Type", "application/json");
		hf.addRequestBody(reqJson);
		hf.Post();
		// 钱包签约支付预处理请求返回检查
		assert hf.getResponseBody().contains(datadriven.get("result_code"));

		Reporter.log("钱包银行卡支付预处理接口（非同人消费）测试： " + datadriven.get("comment"), true);
		System.out.println("===============================================");
	}

	@Test(description = "钱包银行卡支付（非同人消费）-二维码", timeOut = 60000, dataProvider = "bankCardQrPrepay1")
	public void bankCardQrPrepay1(Map<String, String> datadriven) throws Exception {

		// 清除支付密码3次错误缓存
		TelnetUtil telnetUtil = new TelnetUtil(Property.get("host"), Integer.parseInt(Property.get("port")));
		StringBuffer command = new StringBuffer();
		if (datadriven.get("result_code").contains("7701")) {
			DbUtils dbUtils = new DbUtils();			
			String oid_userno = dbUtils.queryOidUserNo(datadriven.get("oid_partner"), datadriven.get("user_id")).getOid_userno().trim();
			command.append("delete ").append(oid_userno).append("api_pwd_valid_times_pay_pwd");
			telnetUtil.execute(command.toString());
		}
		telnetUtil.disconnect();
		
		Thread.sleep(1000);
		String dt_order = ApiUtils.getCurrentDateSecondStr();
		String no_order = ApiUtils.getCurrentDateMillisecondStr();
		
		BankCardQrPrepay bankCardQrPrepay = new BankCardQrPrepay();
		bankCardQrPrepay.setOid_partner(datadriven.get("oid_partner"));
		bankCardQrPrepay.setSign_type(datadriven.get("sign_type"));
		bankCardQrPrepay.setPay_userid(datadriven.get("pay_userid"));
		bankCardQrPrepay.setCol_userid(datadriven.get("col_userid"));
		bankCardQrPrepay.setBusi_partner(datadriven.get("busi_partner"));
		bankCardQrPrepay.setApi_version(datadriven.get("api_version"));		
		if ("auto".equals(datadriven.get("no_order"))){
			bankCardQrPrepay.setNo_order(no_order);
		}else{
			bankCardQrPrepay.setNo_order(datadriven.get("no_order"));
		}
		if ("auto".equals(datadriven.get("dt_order"))){
			bankCardQrPrepay.setDt_order(dt_order);
		}else{
			bankCardQrPrepay.setDt_order(datadriven.get("dt_order"));
		}

		bankCardQrPrepay.setName_goods(datadriven.get("name_goods"));
		bankCardQrPrepay.setMoney_order(Float.parseFloat(datadriven.get("money_order")));
		bankCardQrPrepay.setInfo_order(datadriven.get("info_order"));
		bankCardQrPrepay.setNotify_url(datadriven.get("notify_url"));
		bankCardQrPrepay.setValid_order(Integer.parseInt(datadriven.get("valid_order")));
		bankCardQrPrepay.setRisk_item(datadriven.get("risk_item"));		
		bankCardQrPrepay.setPay_type(datadriven.get("pay_type"));
		bankCardQrPrepay.setCard_no(datadriven.get("card_no"));
		bankCardQrPrepay.setBank_code(datadriven.get("bank_code"));
		bankCardQrPrepay.setBind_mob(datadriven.get("bind_mob"));
		bankCardQrPrepay.setVali_date(datadriven.get("vali_date"));
		bankCardQrPrepay.setCvv2(datadriven.get("cvv2"));
		bankCardQrPrepay.setNo_agree(datadriven.get("no_agree"));
		if (datadriven.get("pwd_pay").length() != 0) {
			bankCardQrPrepay.setPwd_pay(RSAUtil.encrypt(datadriven.get("pwd_pay"), Property.get("rsa_pub_key")));
		} else {
			bankCardQrPrepay.setPwd_pay(datadriven.get("pwd_pay"));
		}
		
		bankCardQrPrepay.setShareing_data(datadriven.get("shareing_data"));
		bankCardQrPrepay.setSign(GenSign.genSign(JSON.parseObject(JSON.toJSONString(bankCardQrPrepay)),datadriven.get("key")));
		String reqJson = JSON.toJSONString(bankCardQrPrepay);
		String bankCardQrPrepayUrl = Property.get("llwallet.bankCardQrPrepay.url");

		// 钱包签约支付预处理请求提交
		HttpFixture hf = new HttpFixture();
		hf.setEncode("UTF-8");
		hf.setUrl(bankCardQrPrepayUrl);
		hf.addHeaderValue("Content-Type", "application/json");
		hf.addRequestBody(reqJson);
		hf.Post();
		// 钱包签约支付预处理请求返回检查
		assert hf.getResponseBody().contains(datadriven.get("result_code"));

		Reporter.log("钱包银行卡支付预处理接口（非同人消费）测试： " + datadriven.get("comment"), true);
		System.out.println("===============================================");
	}
	
	@Test(description = "钱包银行卡支付（非同人消费）-分账", timeOut = 60000, dataProvider = "bankCardQrPrepay2")
	public void bankCardQrPrepay2(Map<String, String> datadriven) throws Exception {

		Thread.sleep(1000);
		String dt_order = ApiUtils.getCurrentDateSecondStr();
		String no_order = ApiUtils.getCurrentDateMillisecondStr();
		
		BankCardQrPrepay bankCardQrPrepay = new BankCardQrPrepay();
		bankCardQrPrepay.setOid_partner(datadriven.get("oid_partner"));
		bankCardQrPrepay.setSign_type(datadriven.get("sign_type"));
		bankCardQrPrepay.setPay_userid(datadriven.get("pay_userid"));
		bankCardQrPrepay.setCol_userid(datadriven.get("col_userid"));
		bankCardQrPrepay.setBusi_partner(datadriven.get("busi_partner"));
		bankCardQrPrepay.setApi_version(datadriven.get("api_version"));		
		if ("auto".equals(datadriven.get("no_order"))){
			bankCardQrPrepay.setNo_order(no_order);
		}else{
			bankCardQrPrepay.setNo_order(datadriven.get("no_order"));
		}
		if ("auto".equals(datadriven.get("dt_order"))){
			bankCardQrPrepay.setDt_order(dt_order);
		}else{
			bankCardQrPrepay.setDt_order(datadriven.get("dt_order"));
		}

		bankCardQrPrepay.setName_goods(datadriven.get("name_goods"));
		bankCardQrPrepay.setMoney_order(Float.parseFloat(datadriven.get("money_order")));
		bankCardQrPrepay.setInfo_order(datadriven.get("info_order"));
		bankCardQrPrepay.setNotify_url(datadriven.get("notify_url"));
		bankCardQrPrepay.setValid_order(Integer.parseInt(datadriven.get("valid_order")));
		bankCardQrPrepay.setRisk_item(datadriven.get("risk_item"));		
		bankCardQrPrepay.setPay_type(datadriven.get("pay_type"));
		bankCardQrPrepay.setCard_no(datadriven.get("card_no"));
		bankCardQrPrepay.setBank_code(datadriven.get("bank_code"));
		bankCardQrPrepay.setBind_mob(datadriven.get("bind_mob"));
		bankCardQrPrepay.setVali_date(datadriven.get("vali_date"));
		bankCardQrPrepay.setCvv2(datadriven.get("cvv2"));
		bankCardQrPrepay.setNo_agree(datadriven.get("no_agree"));
		if (datadriven.get("pwd_pay").length() != 0) {
			bankCardQrPrepay.setPwd_pay(RSAUtil.encrypt(datadriven.get("pwd_pay"), Property.get("rsa_pub_key")));
		} else {
			bankCardQrPrepay.setPwd_pay(datadriven.get("pwd_pay"));
		}
		
		bankCardQrPrepay.setShareing_data(datadriven.get("shareing_data"));
		bankCardQrPrepay.setSign(GenSign.genSign(JSON.parseObject(JSON.toJSONString(bankCardQrPrepay)),datadriven.get("key")));
		String reqJson = JSON.toJSONString(bankCardQrPrepay);
		String bankCardQrPrepayUrl = Property.get("llwallet.bankCardQrPrepay.url");

		// 钱包签约支付预处理请求提交
		HttpFixture hf = new HttpFixture();
		hf.setEncode("UTF-8");
		hf.setUrl(bankCardQrPrepayUrl);
		hf.addHeaderValue("Content-Type", "application/json");
		hf.addRequestBody(reqJson);
		hf.Post();
		// 钱包签约支付预处理请求返回检查
		assert hf.getResponseBody().contains(datadriven.get("result_code"));

		Reporter.log("钱包银行卡支付预处理接口（非同人消费）测试： " + datadriven.get("comment"), true);
		System.out.println("===============================================");
	}
	
	@DataProvider(name = "bankCardQrPrepay")
	public Iterator<Object[]> data4bankCardQrPrepay() throws IOException {
		return new ExcelProvider(this, "bankCardQrPrepay");
	}

	@DataProvider(name = "bankCardQrPrepay1")
	public Iterator<Object[]> data4bankCardQrPrepay1() throws IOException {
		return new ExcelProvider(this, "bankCardQrPrepay1");
	}

	@DataProvider(name = "bankCardQrPrepay2")
	public Iterator<Object[]> data4bankCardQrPrepay2() throws IOException {
		return new ExcelProvider(this, "bankCardQrPrepay2");
	}
	
	@AfterClass
	public void afterClass() {

	}

}
