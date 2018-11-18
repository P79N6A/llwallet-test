package com.llwallet.interfaces.test.api.online.enterprise;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import org.testng.Reporter;
import org.testng.annotations.Test;
import com.alibaba.fastjson.JSON;
import com.llwallet.interfaces.bean.enterprise.UnitCashoutT1Apply;
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
 * 企业用户T+1提现申请接口测试
 */

public class UnitCashoutT1ApplyOnlineTest {

	@BeforeClass
	public void beforeClass() throws InterruptedException {
		Property.set();
	}

	@Test(description = "企业用户提现测试", timeOut = 60000, dataProvider = "unitCashoutT1Apply")
	public void unitCashoutT1Apply(Map<String, String> datadriven) throws Exception {

		Thread.sleep(1000);
		String dt_order = ApiUtils.getCurrentDateSecondStr();
		String no_order = ApiUtils.getCurrentDateMillisecondStr();
		
		UnitCashoutT1Apply unitCashoutT1Apply = new UnitCashoutT1Apply();
		unitCashoutT1Apply.setOid_partner(datadriven.get("oid_partner"));
		unitCashoutT1Apply.setSign_type(datadriven.get("sign_type"));
		unitCashoutT1Apply.setUser_id(datadriven.get("user_id"));
		if ("auto".equals(datadriven.get("no_order"))){
			unitCashoutT1Apply.setNo_order(no_order);
		}else{
			unitCashoutT1Apply.setNo_order(datadriven.get("no_order"));
		}
		if ("auto".equals(datadriven.get("dt_order"))){
			unitCashoutT1Apply.setDt_order(dt_order);
		}else{
			unitCashoutT1Apply.setDt_order(datadriven.get("dt_order"));
		}		
		if ("".equals(datadriven.get("money_order"))){
			unitCashoutT1Apply.setMoney_order(null);
		}else {
			unitCashoutT1Apply.setMoney_order(datadriven.get("money_order"));
		}
		if (datadriven.get("pwd_pay").length() != 0) {
			unitCashoutT1Apply.setPwd_pay(RSAUtil.encrypt(datadriven.get("pwd_pay"), Property.get("rsa_pub_key")));
		} else {
			unitCashoutT1Apply.setPwd_pay(datadriven.get("pwd_pay"));
		}
		unitCashoutT1Apply.setNotify_url(datadriven.get("notify_url"));
		unitCashoutT1Apply.setRisk_item(datadriven.get("risk_item"));
		unitCashoutT1Apply.setFee_data(datadriven.get("fee_data"));		
		unitCashoutT1Apply.setSign(GenSign.genSign(JSON.parseObject(JSON.toJSONString(unitCashoutT1Apply)), datadriven.get("key")));
		String reqJson = JSON.toJSONString(unitCashoutT1Apply);
		String unitCashoutT1ApplyUrl = Property.get("llwallet.unitCashoutT1Apply.url.online");

		// 用户银行卡提现申请请求提交
		HttpFixture hf = new HttpFixture();
		hf.setEncode("UTF-8");
		hf.setUrl(unitCashoutT1ApplyUrl);
		hf.addHeaderValue("Content-Type", "application/json");
		hf.addRequestBody(reqJson);
		hf.Post();

		// 用户银行卡提现申请请求返回检查
		assert hf.getResponseBody().contains(datadriven.get("result_code"));
        
		Reporter.log("企业用户T+1提现申请接口测试： " + datadriven.get("comment"), true);
		System.out.println("===============================================");
	}

	@DataProvider(name = "unitCashoutT1Apply")
	public Iterator<Object[]> data4unitCashoutT1Apply() throws IOException {
		return new ExcelProvider(this, "unitCashoutT1Apply",1);
	}

	@AfterClass
	public void afterClass() {

	}

}
