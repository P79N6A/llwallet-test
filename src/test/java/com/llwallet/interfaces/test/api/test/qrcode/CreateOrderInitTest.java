package com.llwallet.interfaces.test.api.test.qrcode;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;
import org.testng.Reporter;
import org.testng.annotations.Test;
import com.alibaba.fastjson.JSON;
import com.llwallet.interfaces.bean.qrcode.CreateOrderInit;
import com.tools.dataprovider.ExcelProvider;
import com.tools.http.HttpFixture;
import com.tools.utils.ApiUtils;
import com.tools.utils.GenSign;
import com.tools.utils.Property;
import org.testng.annotations.DataProvider;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;

/*
 * @author jiangxm
 * 二维码支付接口测试
 */

public class CreateOrderInitTest {

	@BeforeClass
	public void beforeClass() throws InterruptedException {
		Property.set();
	}

	@Test(description = "二维码支付接口测试", timeOut = 60000, dataProvider = "createOrderInit")
	public void createOrderInit(Map<String, String> datadriven) throws Exception {
		
		String dt_order = ApiUtils.getCurrentDateSecondStr();
		String no_order = ApiUtils.getCurrentDateMillisecondStr();
		
		CreateOrderInit createOrderInit = new CreateOrderInit();
		createOrderInit.setOid_partner(datadriven.get("oid_partner"));
		createOrderInit.setSign_type(datadriven.get("sign_type"));		
		if ("auto".equals(datadriven.get("no_order"))){
			createOrderInit.setNo_order(no_order);
		}else{
			createOrderInit.setNo_order(datadriven.get("no_order"));
		}		
		createOrderInit.setOid_biz(datadriven.get("oid_biz"));
		createOrderInit.setMoney_order(datadriven.get("money_order"));
		createOrderInit.setUser_id(datadriven.get("user_id"));
		createOrderInit.setName_goods(datadriven.get("name_goods"));
		if ("auto".equals(datadriven.get("dt_order"))){
			createOrderInit.setDt_order(dt_order);
		}else{
			createOrderInit.setDt_order(datadriven.get("dt_order"));
		}
		createOrderInit.setNotify_url(datadriven.get("notify_url"));
		createOrderInit.setInfo_order(datadriven.get("info_order"));
		createOrderInit.setMemo2(datadriven.get("memo2"));
		createOrderInit.setPay_type(datadriven.get("pay_type"));
		createOrderInit.setRisk_item(datadriven.get("risk_item"));
		createOrderInit.setSign(GenSign.genSign(JSON.parseObject(JSON.toJSONString(createOrderInit)),datadriven.get("key")));

		String reqJson = JSON.toJSONString(createOrderInit);
		reqJson = URLEncoder.encode(reqJson, "UTF-8");
		String createOrderInitUrl = Property.get("o2o.createOrderInit.url");

		// 二维码支付请求提交
		HttpFixture hf = new HttpFixture();
		hf.setEncode("UTF-8");
		hf.setUrl(createOrderInitUrl);
		hf.addHeaderValue("Content-Type", "application/json");
		hf.addRequestBody(reqJson);
		hf.Post();
		System.out.println("二维码支付返回:" + hf.getResponseBody());
		CreateOrderInit rsp=JSON.parseObject(hf.getResponseBody(), CreateOrderInit.class);
		String dimension_url = rsp.getDimension_url();
		System.out.println("二维码数据:" + dimension_url);
		
		// 二维码支付请求返回检查
		assert hf.getResponseBody().contains(datadriven.get("result_code"));

		Reporter.log("二维码支付接口测试： " + datadriven.get("comment"), true);
		System.out.println("===============================================");
	}

	@DataProvider(name = "createOrderInit")
	public Iterator<Object[]> data4createOrderInit() throws IOException {
		return new ExcelProvider(this, "createOrderInit",2);
	}

	@AfterClass
	public void afterClass() {

	}

}
