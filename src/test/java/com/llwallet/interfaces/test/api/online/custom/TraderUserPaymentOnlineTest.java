package com.llwallet.interfaces.test.api.online.custom;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import org.testng.Reporter;
import org.testng.annotations.Test;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.llwallet.interfaces.bean.custom.TraderUserPayment;
import com.tools.dataprovider.ExcelProvider;
import com.tools.http.HttpRequest;
import com.tools.utils.GenSign;
import com.tools.utils.MyConfig;
import com.tools.utils.Property;
import org.testng.annotations.DataProvider;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;

/*
 * @author jiangxm
 * 商户付款钱包用户接口测试
 */

public class TraderUserPaymentOnlineTest {

	@BeforeClass
	public void beforeClass() throws InterruptedException {
		Property.set();
	}

	@Test(description = "商户付款钱包用户接口测试", timeOut = 60000, dataProvider = "traderUserPayment")
	public void traderUserPayment(Map<String, String> datadriven) throws Exception {
		
		TraderUserPayment traderUserPayment = new TraderUserPayment();
		traderUserPayment.setPay_oidpartner(datadriven.get("pay_oidpartner"));
		traderUserPayment.setOid_partner(MyConfig.getPartner(datadriven.get("oid_partner")));
		traderUserPayment.setSign_type(datadriven.get("sign_type"));
		traderUserPayment.setCol_userid(MyConfig.getUser(datadriven.get("col_userid")));
		traderUserPayment.setBusi_partner(datadriven.get("busi_partner"));
		traderUserPayment.setRisk_item(datadriven.get("risk_item"));
		traderUserPayment.setInfo_order(datadriven.get("info_order"));
		traderUserPayment.setProc_src(datadriven.get("proc_src"));
		traderUserPayment.setName_goods(datadriven.get("name_goods"));
		traderUserPayment.setNo_order(MyConfig.getNoOrder(datadriven.get("no_order")));
		traderUserPayment.setDt_order(MyConfig.getDtOrder(datadriven.get("dt_order")));		
		if ("".equals(datadriven.get("money_order"))){
			traderUserPayment.setMoney_order(null);
		}else {
			traderUserPayment.setMoney_order(datadriven.get("money_order"));
		}
		
		traderUserPayment.setName_goods(datadriven.get("name_goods"));
		traderUserPayment.setSign(GenSign.genSign(JSON.parseObject(JSON.toJSONString(traderUserPayment)), datadriven.get("key")));
		String reqJson = JSON.toJSONString(traderUserPayment);
		String traderPaymentUrl = Property.get("llwallet.traderUserPayment.url.online");

		// 商户付款钱包用户请求提交
		String rsp = HttpRequest.httpPostWithJSON(traderPaymentUrl, reqJson);
		System.out.println("oid_paybill：" + JSONObject.parseObject(rsp).getString("oid_paybill"));

		// 商户付款钱包用户请求返回检查
		assert rsp.contains(datadriven.get("result_code"));
        
		Reporter.log("商户付款钱包用户接口测试： " + datadriven.get("comment"), true);
		System.out.println("===============================================");
	}
	
	@DataProvider(name = "traderUserPayment")
	public Iterator<Object[]> data4traderUserPayment() throws IOException {
		return new ExcelProvider(this, "traderUserPayment",1);
	}
	
	@AfterClass
	public void afterClass() {

	}
}