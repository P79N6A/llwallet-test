package com.llwallet.interfaces.test.api.online.custom;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import org.testng.Reporter;
import org.testng.annotations.Test;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.llwallet.interfaces.bean.custom.CashOutCardBind;
import com.llwallet.interfaces.bean.custom.CashOutCardUnBind;
import com.llwallet.interfaces.bean.personal.UserBankCard;
import com.tools.dataprovider.ExcelProvider;
import com.tools.http.HttpFixture;
import com.tools.utils.GenSign;
import com.tools.utils.Property;
import com.tools.utils.RSAUtil;
import org.testng.annotations.DataProvider;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;

/*
 * @author jiangxm
 * 解绑提现卡接口测试
 */

public class CashOutCardUnBindOnlineTest {

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
		String cashOutCardBindUrl = Property.get("llwallet.cashOutCardBind.url.online");

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
		String cashOutCardUnBindUrl = Property.get("llwallet.cashOutCardUnBind.url.online");

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
		String userBankCardUrl = Property.get("llwallet.userBankCard.url.online");

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
		String cashOutCardUnBindUrl = Property.get("llwallet.cashOutCardUnBind.url.online");

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
	
	@DataProvider(name = "cashOutCardUnBind")
	public Iterator<Object[]> data4cashOutCardUnBind() throws IOException {
		return new ExcelProvider(this, "cashOutCardUnBind",2);
	}

	@DataProvider(name = "cashOutCardUnBind1")
	public Iterator<Object[]> data4cashOutCardUnBind1() throws IOException {
		return new ExcelProvider(this, "cashOutCardUnBind1",21);
	}
	
	@AfterClass
	public void afterClass() {

	}
}