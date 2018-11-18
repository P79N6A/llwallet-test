package com.llwallet.interfaces.test.api.online.personal;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import org.testng.Reporter;
import org.testng.annotations.Test;
import com.account.interfaces.bean.TraderPayment;
import com.alibaba.fastjson.JSON;
import com.lianpay.api.util.ApiUtils;
import com.tools.dataprovider.ExcelProvider;
import com.tools.http.HttpFixture;
import com.tools.utils.GenSign;
import com.tools.utils.Property;
import org.testng.annotations.DataProvider;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;

/*
 * @author jiangxm
 * 接口测试
 */

public class TraderPaymentOnlineTest {

	@BeforeClass
	public void beforeClass() throws InterruptedException {
		Property.set();
	}

	@Test(description = "商户付款钱包用户接口测试", timeOut = 60000, dataProvider = "traderPayment")
	public void traderPayment(Map<String, String> datadriven) throws Exception {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		Date date = cal.getTime();
		String dt_order = sdf.format(date);
		String no_order = ApiUtils.getCurrentDateTimeStr();
		
		TraderPayment traderPayment = new TraderPayment();
		traderPayment.setOid_partner(datadriven.get("oid_partner"));
		traderPayment.setSign_type(datadriven.get("sign_type"));
		traderPayment.setCol_userid(datadriven.get("col_userid"));
		traderPayment.setBusi_partner(datadriven.get("busi_partner"));
		traderPayment.setRisk_item(datadriven.get("risk_item"));
		traderPayment.setInfo_order(datadriven.get("info_order"));
		traderPayment.setProc_src(datadriven.get("proc_src"));
		traderPayment.setName_goods(datadriven.get("name_goods"));
		traderPayment.setFlag_account(datadriven.get("flag_account"));
		if ("auto".equals(datadriven.get("no_order"))){
			traderPayment.setNo_order(no_order);
		}else{
			traderPayment.setNo_order(datadriven.get("no_order"));
		}
		if ("auto".equals(datadriven.get("dt_order"))){
			traderPayment.setDt_order(dt_order);
		}else{
			traderPayment.setDt_order(datadriven.get("dt_order"));
		}		
		if ("".equals(datadriven.get("money_order"))){
			traderPayment.setMoney_order(null);
		}else {
			traderPayment.setMoney_order(Float.parseFloat(datadriven.get("money_order")));
		}
		
		traderPayment.setName_goods(datadriven.get("name_goods"));
		traderPayment.setNotify_url(datadriven.get("notify_url"));
		traderPayment.setSign(GenSign.genSign(JSON.parseObject(JSON.toJSONString(traderPayment)), datadriven.get("key")));
		String reqJson = JSON.toJSONString(traderPayment);
		String traderPaymentUrl = Property.get("llwallet.traderPayment.url.online");

		// 商户付款钱包用户请求提交
		HttpFixture hf = new HttpFixture();
		hf.setEncode("UTF-8");
		hf.setUrl(traderPaymentUrl);
		hf.addHeaderValue("Content-Type", "application/json");
		hf.addRequestBody(reqJson);
		hf.Post();
		Thread.sleep(1000);
		// 商户付款钱包用户请求返回检查
		assert hf.getResponseBody().contains(datadriven.get("result_code"));
        
		Reporter.log("商户付款钱包用户接口测试： " + datadriven.get("comment"), true);
		System.out.println("===============================================");
	}

	@DataProvider(name = "traderPayment")
	public Iterator<Object[]> data4traderPayment() throws IOException {
		return new ExcelProvider(this, "traderPayment",4);
	}

	@AfterClass
	public void afterClass() {

	}
}