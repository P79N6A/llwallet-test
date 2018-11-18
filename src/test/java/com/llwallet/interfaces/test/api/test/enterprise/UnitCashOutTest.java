package com.llwallet.interfaces.test.api.test.enterprise;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import org.testng.Reporter;
import org.testng.annotations.Test;
import com.alibaba.fastjson.JSON;
import com.llwallet.interfaces.bean.enterprise.UnitCashout;
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
 * 企业用户普通提现申请接口测试
 */

public class UnitCashOutTest {

	@BeforeClass
	public void beforeClass() throws InterruptedException {
		Property.set();
	}

	@Test(description = "企业用户提现测试", timeOut = 60000, dataProvider = "unitCashout")
	public void unitCashout(Map<String, String> datadriven) throws Exception {

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
		
		Thread.sleep(100);
		String dt_order = ApiUtils.getCurrentDateSecondStr();
		String no_order = ApiUtils.getCurrentDateMillisecondStr();
		
		UnitCashout unitCashout = new UnitCashout();
		unitCashout.setOid_partner(datadriven.get("oid_partner"));
		unitCashout.setSign_type(datadriven.get("sign_type"));
		unitCashout.setUser_id(datadriven.get("user_id"));
		if ("auto".equals(datadriven.get("no_order"))){
			unitCashout.setNo_order(no_order);
		}else{
			unitCashout.setNo_order(datadriven.get("no_order"));
		}
		if ("auto".equals(datadriven.get("dt_order"))){
			unitCashout.setDt_order(dt_order);
		}else{
			unitCashout.setDt_order(datadriven.get("dt_order"));
		}		
		if ("".equals(datadriven.get("money_order"))){
			unitCashout.setMoney_order(null);
		}else {
			unitCashout.setMoney_order(datadriven.get("money_order"));
		}
		if (datadriven.get("pwd_pay").length() != 0) {
			unitCashout.setPwd_pay(RSAUtil.encrypt(datadriven.get("pwd_pay"), Property.get("rsa_pub_key")));
		} else {
			unitCashout.setPwd_pay(datadriven.get("pwd_pay"));
		}
		unitCashout.setNotify_url(datadriven.get("notify_url"));
		unitCashout.setRisk_item(datadriven.get("risk_item"));
		unitCashout.setFee_data(datadriven.get("fee_data"));		
		unitCashout.setSign(GenSign.genSign(JSON.parseObject(JSON.toJSONString(unitCashout)), datadriven.get("key")));
		String reqJson = JSON.toJSONString(unitCashout);
		String unitCashoutUrl = Property.get("llwallet.unitCashout.url");

		// 用户银行卡提现申请请求提交
		HttpFixture hf = new HttpFixture();
		hf.setEncode("UTF-8");
		hf.setUrl(unitCashoutUrl);
		hf.addHeaderValue("Content-Type", "application/json");
		hf.addRequestBody(reqJson);
		hf.Post();

		// 用户银行卡提现申请请求返回检查
		assert hf.getResponseBody().contains(datadriven.get("result_code"));
        
		Reporter.log("企业用户普通提现申请接口测试： " + datadriven.get("comment"), true);
		System.out.println("===============================================");
	}

	@DataProvider(name = "unitCashout")
	public Iterator<Object[]> data4unitCashout() throws IOException {
		return new ExcelProvider(this, "unitCashout");
	}

	@AfterClass
	public void afterClass() {

	}

}
