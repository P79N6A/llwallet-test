package com.llwallet.interfaces.test.api.online.custom;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import org.testng.Reporter;
import org.testng.annotations.Test;
import com.alibaba.fastjson.JSON;
import com.llwallet.interfaces.bean.custom.CashOutCardBind;
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
 * 绑定提现卡接口测试
 */

public class CashOutCardBindOnlineTest {

	@BeforeClass
	public void beforeClass() throws InterruptedException {
		Property.set();
	}

	@Test(description = "绑定提现卡接口测试", timeOut = 60000, dataProvider = "cashOutCardBind")
	public void cashOutCardBind(Map<String, String> datadriven) throws Exception {

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
		cashOutCardBind
				.setSign(GenSign.genSign(JSON.parseObject(JSON.toJSONString(cashOutCardBind)), datadriven.get("key")));
		String reqJson = JSON.toJSONString(cashOutCardBind);
		String cashOutCardBindUrl = Property.get("llwallet.cashOutCardBind.url.online");

		// 绑定提现卡请求提交
		HttpFixture hf = new HttpFixture();
		hf.setEncode("UTF-8");
		hf.setUrl(cashOutCardBindUrl);
		hf.addHeaderValue("Content-Type", "application/json");
		hf.addRequestBody(reqJson);
		hf.Post();

		// 绑定提现卡请求返回检查
		assert hf.getResponseBody().contains(datadriven.get("result_code"));

		Reporter.log("绑定提现卡接口测试： " + datadriven.get("comment"), true);
		System.out.println("===============================================");
	}

	@DataProvider(name = "cashOutCardBind")
	public Iterator<Object[]> data4cashOutCardBind() throws IOException {
		return new ExcelProvider(this, "cashOutCardBind",1);
	}

	@AfterClass
	public void afterClass() {

	}
}