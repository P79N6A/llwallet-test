package com.llwallet.interfaces.test.api.online.personal;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import org.testng.Reporter;
import org.testng.annotations.Test;
import com.alibaba.fastjson.JSON;
import com.llwallet.interfaces.bean.personal.BankCardOpenAuth;
import com.tools.dataprovider.ExcelProvider;
import com.tools.http.HttpFixture;
import com.tools.utils.GenSign;
import com.tools.utils.Property;
import org.testng.annotations.DataProvider;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;

/*
 * @author jiangxm
 * 银行卡绑卡认证接口测试
 */

public class BankCardOpenAuthOnlineTest {

	@BeforeClass
	public void beforeClass() throws InterruptedException {
		Property.set();
	}

	@Test(description = "银行卡绑卡认证接口测试", timeOut = 60000, dataProvider = "bankCardOpenAuth")
	public void bankCardOpenAuth(Map<String, String> datadriven) throws Exception {
			
		BankCardOpenAuth bankCardOpenAuth = new BankCardOpenAuth();
		bankCardOpenAuth.setOid_partner(datadriven.get("oid_partner"));
		bankCardOpenAuth.setSign_type(datadriven.get("sign_type"));
		bankCardOpenAuth.setUser_id(datadriven.get("user_id"));
		bankCardOpenAuth.setPay_type(datadriven.get("pay_type"));
		bankCardOpenAuth.setApi_version(datadriven.get("api_version"));
		bankCardOpenAuth.setRisk_item(datadriven.get("risk_item"));
		bankCardOpenAuth.setCard_no(datadriven.get("card_no"));
		bankCardOpenAuth.setBind_mob(datadriven.get("bind_mob"));
		bankCardOpenAuth.setVali_date(datadriven.get("vali_date"));
		bankCardOpenAuth.setCvv2(datadriven.get("cvv2"));
		bankCardOpenAuth.setFlag_auth(datadriven.get("flag_auth"));		
		bankCardOpenAuth.setSign(GenSign.genSign(JSON.parseObject(JSON.toJSONString(bankCardOpenAuth)),datadriven.get("key")));
		String reqJson = JSON.toJSONString(bankCardOpenAuth);
		String bankCardOpenAuthUrl = Property.get("llwallet.bankCardOpenAuth.url.online");

		// 银行卡绑卡认证请求提交
		HttpFixture hf = new HttpFixture();
		hf.setEncode("UTF-8");
		hf.setUrl(bankCardOpenAuthUrl);
		hf.addHeaderValue("Content-Type", "application/json");
		hf.addRequestBody(reqJson);
		hf.Post();
		// 银行卡绑卡认证请求返回检查
		assert hf.getResponseBody().contains(datadriven.get("result_code"));

		Reporter.log("银行卡绑卡认证接口测试： " + datadriven.get("comment"), true);
		System.out.println("===============================================");
	}

	@DataProvider(name = "bankCardOpenAuth")
	public Iterator<Object[]> data4bankCardOpenAuth() throws IOException {
		return new ExcelProvider(this, "bankCardOpenAuth",7);
	}

	@AfterClass
	public void afterClass() {

	}

}
