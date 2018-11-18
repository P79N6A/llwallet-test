package com.llwallet.interfaces.test.api.online.personal;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import org.testng.Reporter;
import org.testng.annotations.Test;
import com.alibaba.fastjson.JSON;
import com.llwallet.interfaces.bean.personal.BankCardUnbind;
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
 * 银行卡解绑接口测试
 */

public class BankCardUnbindOnlineTest {

	@BeforeClass
	public void beforeClass() throws InterruptedException {
		Property.set();
	}

	@Test(description = "银行卡解绑接口测试", timeOut = 60000, dataProvider = "bankCardUnbind")
	public void bankCardUnbind(Map<String, String> datadriven) throws Exception {
		
		BankCardUnbind bankCardUnbind = new BankCardUnbind();
		bankCardUnbind.setOid_partner(datadriven.get("oid_partner2"));
		bankCardUnbind.setSign_type(datadriven.get("sign_type2"));		
		bankCardUnbind.setUser_id(datadriven.get("user_id2"));
		bankCardUnbind.setNo_agree(datadriven.get("no_agree2"));	
		bankCardUnbind.setPay_type(datadriven.get("pay_type2"));
		if (datadriven.get("pwd_pay2").length() != 0) {
			bankCardUnbind.setPwd_pay(RSAUtil.encrypt(datadriven.get("pwd_pay2"), Property.get("rsa_pub_key")));
		} else {
			bankCardUnbind.setPwd_pay(datadriven.get("pwd_pay2"));
		}		
		bankCardUnbind.setType_user(datadriven.get("type_user2"));
		bankCardUnbind.setSign(GenSign.genSign(JSON.parseObject(JSON.toJSONString(bankCardUnbind)),datadriven.get("key2")));
		String reqJson2 = JSON.toJSONString(bankCardUnbind);
		String bankCardUnbindUrl = Property.get("llwallet.bankCardUnbind.url.online");

		// 银行卡解绑请求提交
		HttpFixture hf = new HttpFixture();
		hf.setEncode("UTF-8");
		hf.addHeaderValue("Content-Type", "application/json");
		hf.setUrl(bankCardUnbindUrl);
		hf.addRequestBody(reqJson2);
		hf.Post();
		// 银行卡解绑请求返回检查
		assert hf.getResponseBody().contains(datadriven.get("result_code"));
		Reporter.log("银行卡解绑接口测试： " + datadriven.get("comment"), true);
		System.out.println("===============================================");
	}
	
	@DataProvider(name = "bankCardUnbind")
	public Iterator<Object[]> data4bankCardUnbind() throws IOException {
		return new ExcelProvider(this, "bankCardUnbind",1);
	}
	
	@AfterClass
	public void afterClass() {

	}

}