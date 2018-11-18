package com.llwallet.interfaces.test.api.test.personal;

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
import com.tools.utils.TelnetUtil;
import org.testng.annotations.DataProvider;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;

/*
 * @author jiangxm
 * 银行卡绑卡认证接口测试
 */

public class BankCardOpenAuthTest {

	@BeforeClass
	public void beforeClass() throws InterruptedException {
		Property.set();
	}

	@Test(description = "银行卡绑卡认证接口测试", timeOut = 60000, dataProvider = "bankCardOpenAuth")
	public void bankCardOpenAuth(Map<String, String> datadriven) throws Exception {
		
		// 清除短信发送15次缓存  BankCardbindActionKEY+{oid_partner} +{bind_mob}
		TelnetUtil telnetUtil = new TelnetUtil(Property.get("host"), Integer.parseInt(Property.get("port")));
		StringBuffer command = new StringBuffer();
		if (datadriven.get("result_code").contains("0000")) {
			command.append("delete ").append("BankCardbindActionKEY").append(datadriven.get("oid_partner")).append(datadriven.get("bind_mob"));
			telnetUtil.execute(command.toString());
		}
		telnetUtil.disconnect();
		
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
		String bankCardOpenAuthUrl = Property.get("llwallet.bankCardOpenAuth.url");

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
		return new ExcelProvider(this, "bankCardOpenAuth",1);
	}

	@AfterClass
	public void afterClass() {

	}

}