package com.llwallet.interfaces.test.api.online.personal;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import org.testng.Reporter;
import org.testng.annotations.Test;
import com.alibaba.fastjson.JSON;
import com.llwallet.interfaces.bean.personal.CashoutT1Apply;
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
 * 用户T+1提现申请接口测试
 */

public class CashoutT1ApplyOnlineTest {

	@BeforeClass
	public void beforeClass() throws InterruptedException {
		Property.set();
	}

	@Test(description = "用户T+1提现申请接口测试", timeOut = 60000, dataProvider = "cashoutT1Apply")
	public void cashoutT1Apply(Map<String, String> datadriven) throws Exception {

		Thread.sleep(1000);
		String dt_order = ApiUtils.getCurrentDateSecondStr();
		String no_order = ApiUtils.getCurrentDateMillisecondStr();
		
		CashoutT1Apply cashoutT1Apply = new CashoutT1Apply();
		cashoutT1Apply.setOid_partner(datadriven.get("oid_partner"));
		cashoutT1Apply.setSign_type(datadriven.get("sign_type"));
		cashoutT1Apply.setUser_id(datadriven.get("user_id"));
		if ("auto".equals(datadriven.get("no_order"))){
			cashoutT1Apply.setNo_order(no_order);
		}else{
			cashoutT1Apply.setNo_order(datadriven.get("no_order"));
		}
		if ("auto".equals(datadriven.get("dt_order"))){
			cashoutT1Apply.setDt_order(dt_order);
		}else{
			cashoutT1Apply.setDt_order(datadriven.get("dt_order"));
		}		
		if ("".equals(datadriven.get("money_order"))){
			cashoutT1Apply.setMoney_order(null);
		}else {
			cashoutT1Apply.setMoney_order(datadriven.get("money_order"));
		}
		cashoutT1Apply.setCard_no(datadriven.get("card_no"));
		if (datadriven.get("pwd_pay").length() != 0) {
			cashoutT1Apply.setPwd_pay(RSAUtil.encrypt(datadriven.get("pwd_pay"), Property.get("rsa_pub_key")));
		} else {
			cashoutT1Apply.setPwd_pay(datadriven.get("pwd_pay"));
		}
		cashoutT1Apply.setNo_agree(datadriven.get("no_agree"));
		cashoutT1Apply.setCity_code(datadriven.get("city_code"));
		cashoutT1Apply.setBrabank_name(datadriven.get("brabank_name"));
		cashoutT1Apply.setInfo_order(datadriven.get("info_order"));
		cashoutT1Apply.setNotify_url(datadriven.get("notify_url"));
		cashoutT1Apply.setApi_version(datadriven.get("api_version"));
		cashoutT1Apply.setPrcptcd(datadriven.get("prcptcd"));		
		cashoutT1Apply.setRisk_item(datadriven.get("risk_item"));
		cashoutT1Apply.setFee_data(datadriven.get("fee_data"));		
		cashoutT1Apply.setInfo_order(datadriven.get("info_order"));
		cashoutT1Apply.setSign(GenSign.genSign(JSON.parseObject(JSON.toJSONString(cashoutT1Apply)), datadriven.get("key")));
		String reqJson = JSON.toJSONString(cashoutT1Apply);
		String cashoutT1ApplyUrl = Property.get("llwallet.cashoutT1Apply.url.online");

		// 用户T+1提现申请请求提交
		HttpFixture hf = new HttpFixture();
		hf.setEncode("UTF-8");
		hf.setUrl(cashoutT1ApplyUrl);
		hf.addHeaderValue("Content-Type", "application/json");
		hf.addRequestBody(reqJson);
		hf.Post();

		// 用户T+1提现申请请求返回检查
		assert hf.getResponseBody().contains(datadriven.get("result_code"));
        
		Reporter.log("用户T+1提现申请接口测试： " + datadriven.get("comment"), true);
		System.out.println("===============================================");
	}

	@DataProvider(name = "cashoutT1Apply")
	public Iterator<Object[]> data4cashoutT1Apply() throws IOException {
		return new ExcelProvider(this, "cashoutT1Apply",3);
	}

	@AfterClass
	public void afterClass() {

	}
}