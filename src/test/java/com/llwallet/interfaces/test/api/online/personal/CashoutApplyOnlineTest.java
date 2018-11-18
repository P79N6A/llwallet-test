package com.llwallet.interfaces.test.api.online.personal;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import org.testng.Reporter;
import org.testng.annotations.Test;
import com.alibaba.fastjson.JSON;
import com.llwallet.interfaces.bean.personal.CashoutApply;
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
 * 用户银行卡提现申请接口测试
 */

public class CashoutApplyOnlineTest {

	@BeforeClass
	public void beforeClass() throws InterruptedException {
		Property.set();
	}

	@Test(description = "用户银行卡提现申请接口测试", timeOut = 60000, dataProvider = "cashoutApply")
	public void cashoutApply(Map<String, String> datadriven) throws Exception {

		Thread.sleep(1000);
		String dt_order = ApiUtils.getCurrentDateSecondStr();
		String no_order = ApiUtils.getCurrentDateMillisecondStr();
		
		CashoutApply cashoutApply = new CashoutApply();
		cashoutApply.setOid_partner(datadriven.get("oid_partner"));
		cashoutApply.setSign_type(datadriven.get("sign_type"));
		cashoutApply.setUser_id(datadriven.get("user_id"));
		if ("auto".equals(datadriven.get("no_order"))){
			cashoutApply.setNo_order(no_order);
		}else{
			cashoutApply.setNo_order(datadriven.get("no_order"));
		}
		if ("auto".equals(datadriven.get("dt_order"))){
			cashoutApply.setDt_order(dt_order);
		}else{
			cashoutApply.setDt_order(datadriven.get("dt_order"));
		}		
		if ("".equals(datadriven.get("money_order"))){
			cashoutApply.setMoney_order(null);
		}else {
			cashoutApply.setMoney_order(datadriven.get("money_order"));
		}
		cashoutApply.setCard_no(datadriven.get("card_no"));
		if (datadriven.get("pwd_pay").length() != 0) {
			cashoutApply.setPwd_pay(RSAUtil.encrypt(datadriven.get("pwd_pay"), Property.get("rsa_pub_key")));
		} else {
			cashoutApply.setPwd_pay(datadriven.get("pwd_pay"));
		}
		cashoutApply.setNo_agree(datadriven.get("no_agree"));
		cashoutApply.setCity_code(datadriven.get("city_code"));
		cashoutApply.setBrabank_name(datadriven.get("brabank_name"));
		cashoutApply.setInfo_order(datadriven.get("info_order"));
		cashoutApply.setNotify_url(datadriven.get("notify_url"));
		cashoutApply.setApi_version(datadriven.get("api_version"));
		cashoutApply.setPrcptcd(datadriven.get("prcptcd"));		
		cashoutApply.setRisk_item(datadriven.get("risk_item"));
		cashoutApply.setFee_data(datadriven.get("fee_data"));		
		cashoutApply.setInfo_order(datadriven.get("info_order"));
		cashoutApply.setSign(GenSign.genSign(JSON.parseObject(JSON.toJSONString(cashoutApply)), datadriven.get("key")));
		String reqJson = JSON.toJSONString(cashoutApply);
		String cashoutApplyUrl = Property.get("llwallet.cashoutApply.url.online");

		// 用户银行卡提现申请请求提交
		HttpFixture hf = new HttpFixture();
		hf.setEncode("UTF-8");
		hf.setUrl(cashoutApplyUrl);
		hf.addHeaderValue("Content-Type", "application/json");
		hf.addRequestBody(reqJson);
		hf.Post();

		// 用户银行卡提现申请请求返回检查
		assert hf.getResponseBody().contains(datadriven.get("result_code"));
        
		Reporter.log("用户银行卡提现申请接口测试： " + datadriven.get("comment"), true);
		System.out.println("===============================================");
	}

	@DataProvider(name = "cashoutApply")
	public Iterator<Object[]> data4cashoutApply() throws IOException {
		return new ExcelProvider(this, "cashoutApply",3);
	}

	@AfterClass
	public void afterClass() {

	}
}