package com.lianpay.interfaces.test;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import org.testng.Reporter;
import org.testng.annotations.Test;
import com.alibaba.fastjson.JSON;
import com.lianpay.interfaces.bean.UnitCashOut;
import com.lianpay.tools.dataprovider.ExcelProvider;
import com.lianpay.tools.http.HttpFixture;
import com.lianpay.tools.utils.GenSign;
import com.lianpay.tools.utils.Property;
import org.testng.annotations.DataProvider;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;

/*
 * @author jiangxm
 * 企业用户银行卡提现申请接口测试
 */

public class UnitCashOutTest {

	@BeforeClass
	public void beforeClass() throws InterruptedException {
		Property.set();
	}

	@Test(description = "企业用户银行卡提现申请接口测试", timeOut = 60000, dataProvider = "unitCashOut")
	public void unitCashOut(Map<String, String> datadriven) throws Exception {

		UnitCashOut unitCashOut = new UnitCashOut();
		unitCashOut.setOid_partner(datadriven.get("oid_partner"));
		unitCashOut.setSign_type(datadriven.get("sign_type"));
		unitCashOut.setUser_id(datadriven.get("user_id"));
		unitCashOut.setNo_order(datadriven.get("no_order"));
		unitCashOut.setDt_order(datadriven.get("dt_order"));
		unitCashOut.setMoney_order(Float.parseFloat(datadriven.get("money_order")));
		unitCashOut.setPwd_pay(datadriven.get("pwd_pay"));
		unitCashOut.setFee_data(datadriven.get("fee_data"));
		unitCashOut.setSign(GenSign.genSign(JSON.parseObject(JSON.toJSONString(unitCashOut)),datadriven.get("key")));
		String reqJson = JSON.toJSONString(unitCashOut);
		String unitCashOutUrl = Property.get("llwallet.unitCashOut.url");

		// 企业用户银行卡提现申请请求提交
		HttpFixture hf = new HttpFixture();
		hf.setEncode("UTF-8");
		hf.setUrl("" + unitCashOutUrl + "");
		hf.addHeaderValue("Content-Type", "application/json");
		hf.addRequestBody(reqJson);
		hf.Post();
		// 企业用户银行卡提现申请请求返回检查
		assert hf.getResponseBody().contains(datadriven.get("result_code"));
		// assert hf.getResponseBody().contains(datadriven.get("result_code1"));

		Reporter.log("企业用户银行卡提现申请接口测试： " + datadriven.get("comment"), true);
		Reporter.log("===============================================", true);
	}

	@DataProvider(name = "unitCashOut")
	public Iterator<Object[]> data4unitCashOut() throws IOException {
		return new ExcelProvider(this, "unitCashOut");
	}

	@AfterClass
	public void afterClass() {

	}

}
